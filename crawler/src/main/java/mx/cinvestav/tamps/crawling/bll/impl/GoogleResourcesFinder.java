/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.cinvestav.tamps.crawling.bll.impl;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.cinvestav.tamps.crawling.exceptions.BllException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import mx.cinvestav.tamps.crawling.bll.IResourcesFinder;

/**
 * Allows to find possible locations of resources (documents) associated to a query 
 * using Google search engine.
 * @author ealdana@tamps.cinvestav.mx
 */
public class GoogleResourcesFinder implements IResourcesFinder {

    /**
     * Defines the index of the result from which the results must be displayed 
     */
    public static int START=0;
    /**
     * Defines the number of results that will be include into the resultset 
     * the value is between 1 and 20 due to Google constraints.
     */
    public static int NUM=20;
    @Override
    public ArrayList<URL> getResources(String query)throws BllException {
        ArrayList<URL> urls=new ArrayList<>();  
        try{
            WebClient client =new WebClient(BrowserVersion.CHROME);
            client.getOptions().setCssEnabled(false);
            client.getOptions().setJavaScriptEnabled(false);
            String url="https://www.google.com/search?hl=es&q="+query+"&start="+START+"&num="+NUM;
            Logger.getLogger(GoogleResourcesFinder.class.getName()).log(Level.INFO, "URL:{0} will be executed.", url);      
            HtmlPage page =client.getPage(url);
            Document doc =Jsoup.parse(page.asXml());
            Logger.getLogger(GoogleResourcesFinder.class.getName()).log(Level.FINE, "Query executed");
            Elements links = doc.select("a[href^=/url]");
            String rawLink, strLink;
            int start, end;
            for (Element link : links) {
                rawLink=link.attr("href");
                start=rawLink.indexOf("http");
                end=rawLink.indexOf("&sa");
                if(start!=-1&&end!=-1){
                   strLink=rawLink.substring(start, end);
                    if(!strLink.contains("webcache")){
                      urls.add(new URL(strLink));
                      Logger.getLogger(GoogleResourcesFinder.class.getName()).log(Level.FINE, "{0} was added.", strLink);
                    }
                }
               
            }
                                  
       }catch(IOException | FailingHttpStatusCodeException e)
       {
           Logger.getLogger(GoogleResourcesFinder.class.getName()).log(Level.SEVERE, null,e);
           throw new BllException(e);
       }
        
        return urls;
    }
    
    public static void main(String args[])
    {
        try {
            GoogleResourcesFinder finder=new GoogleResourcesFinder();
            finder.getResources("zika+pdf");
        } catch (BllException ex) {
            Logger.getLogger(GoogleResourcesFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
}
