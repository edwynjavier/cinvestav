/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.cinvestav.tamps.crawling.services;



import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import mx.cinvestav.tamps.crawling.bll.IResourcesFinder;
import mx.cinvestav.tamps.crawling.bll.IScraper;
import mx.cinvestav.tamps.crawling.bll.ResourcesFinderManager;
import mx.cinvestav.tamps.crawling.bll.ScraperManager;
import mx.cinvestav.tamps.crawling.entities.CinvestavDocument;
import mx.cinvestav.tamps.crawling.entities.ResourcesQuery;
import mx.cinvestav.tamps.crawling.exceptions.ServiceException;


/**
 *Allows to expose several functionality from business layer.
 * @author ealdana@tamps.cinvestav.mx
 */
@Path("crawler")
public class CrawlerServices {
    
    /**
     *Test service
     * @return
     */
    @GET
    public String sayHello() {
        return "Hello!";
    }

    /**
     * Test Service
     * @param name
     * @return
     */
    @GET
    @Path("{name}")
    public Response sayHello(@PathParam("name") String name) {
        return Response.status(200)
                       .entity("Hello " + name + "!")
                       .type("text/plain; charset=utf-8")
                       .build();
    }
    
    /**
     *
     * @param sender
     * @param content
     * @return
     */
    @GET
    @Path("sendMsg")
    @Produces(MediaType.APPLICATION_JSON)
    public Message sendMsg(@QueryParam("sender") String sender,
            @QueryParam("content") String content) {
        return new Message(sender, content);
    }
    
    /**
     * 
     * <B>SERVICE NAME:scraper. </B>
     * Allows to obtain a XML that contains the results of search process given
     * an URL. The structure of the XML corresponds to the structure of Cinvestav
     * Document. 
     * @param url String with the URL that defines the location os a resource (Document)
     * @return CinvestavDocument
     */
    @GET
    @Path("scraper")
    @Produces(MediaType.APPLICATION_XML)
    public JAXBElement<CinvestavDocument> getDocument(@QueryParam("url") String url) {
        CinvestavDocument doc=new CinvestavDocument();
        try {
                    
                    String contentType =ScraperManager.getContentType(url);
                                       
                    IScraper scraper=ScraperManager.getInstance(contentType);
                    if(scraper!=null)
                    {
                       doc=scraper.getRawText(url);
                       doc.setContentType(contentType);
                    }
                    else
                    {

                        throw new ServiceException("It is not possible to create a new instance"
                                + " of IScraper for the contenType="+contentType);
                    }
                    return new JAXBElement<>(new QName("CinvestavDocument"), CinvestavDocument.class, doc);
            
        } 
        catch (Throwable t) {
            Logger.getLogger(CrawlerServices.class.getName()).log(Level.SEVERE, "Severe error has ocurred, an empty document will be returned",t);  
            return new JAXBElement<>(new QName("CinvestavDocument"), CinvestavDocument.class, doc);
        }
        
       
    }
    
    /**
     * <B>SERVICE NAME:documents. </B>
     * Allows to obtain a XML that contains the results of search process given
     * an URL. The structure of the XML corresponds to the structure of Cinvestav
     * Document. 
     * @param query String with the terms related to the desired topic
     * @param engine String that defines the engine that must be used. In the URL 
     * this value will be given as: engine=enginegoogle  engine=googleScholar,
     * the default value is google.
     * @param strLength Number of documents to display, the value will be given as: 
     * maxDocLength=20
     * @return ArrayList with CinvestavDocuments
     */
    @GET
    @Path("documents")
    @Produces(MediaType.APPLICATION_XML)
    public ArrayList<CinvestavDocument> getDocuments(@QueryParam("query") String query,
                                                     @DefaultValue("") @QueryParam("engine") String engine,
                                                     @DefaultValue("")@QueryParam("maxDocLength") String strLength){
        ArrayList<CinvestavDocument> docs=new ArrayList<>(); 
        ResourcesQuery q;
        
        try {
                  
            q=new ResourcesQuery();
            IResourcesFinder finder=ResourcesFinderManager.getInstance(engine);
            q.setResourcesLocation(finder.getResources(query));
            q.setCriteria(query);
            q.setQueryDate(new Date());
            ArrayList<URL> urls=q.getResourcesLocation();
            String contentType="";
            CinvestavDocument doc;
            for(URL url:urls)
            {   try{
                        HttpURLConnection connection = (HttpURLConnection) new URL(url.toString()).openConnection();
                        if(connection!=null){
                            connection.setRequestMethod("HEAD");
                            connection.connect();
                            contentType=connection.getContentType();
                        }
                        IScraper scraper=ScraperManager.getInstance(contentType);
                        
                        doc=scraper.getRawText(url.toString());
                        if(!strLength.equals("")) //Truncate the length of the text
                             doc.setText(doc.getText().substring(0, Integer.parseInt(strLength)));   
                        doc.setContentType(contentType);
                        docs.add(doc); 
                        
               }
               catch(Throwable t)
               {   Object[]params={url,contentType};
                   Logger.getLogger(CrawlerServices.class.getName()).log(Level.WARNING, "Error obtaining a document from {0} with content-type {1}",params);  
                   continue;
               }
                   
            }  
            return docs;
        } 
        catch (Throwable t) {
            Logger.getLogger(CrawlerServices.class.getName()).log(Level.SEVERE, "Severe error has ocurred.",t);
            return docs;
        }
        
        
    }
    /**
     * <B>SERVICE NAME:finder. </B>
     * Allows us to search possible resources about a topic defined by a string that
     * contains the terms or words associated to it.
     * @param query String with the terms related to the desired topic.
     * @param engine String that defines the engine that must be used. In the URL 
     * this value will be given as: engine=enginegoogle  engine=googleScholar,
     * the default value is google.
     * @return ResourceQuery object that contains the location of the possible resources.
     * @throws ServiceException
     */
    @GET
    @Path("finder")
    @Produces(MediaType.APPLICATION_XML)
    public ResourcesQuery searchResources(@QueryParam("query") String query,
                                          @DefaultValue("") @QueryParam("engine") String engine)
           throws ServiceException {
        ResourcesQuery q=new ResourcesQuery();
        try {
           
            IResourcesFinder finder=ResourcesFinderManager.getInstance(engine);
            q.setResourcesLocation(finder.getResources(query));
            q.setCriteria(query);
            q.setQueryDate(new Date());
        } 
        catch (Throwable t) {
            Logger.getLogger(CrawlerServices.class.getName()).log(Level.SEVERE, "Severe error has ocurred.",t);  
            throw new ServiceException(t);
        }
        
        return q;
    }
    
    @POST
    @Path("sendMsg")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Message sendMsg(Message msg) {
        System.out.printf("Sender: %s, Content: %s%n", msg.getSender(), msg.getContent());
        return new Message("server", "OK");
    }
    
}
