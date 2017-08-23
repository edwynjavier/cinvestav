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
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.cinvestav.tamps.crawling.bll.IScraper;
import mx.cinvestav.tamps.crawling.entities.CinvestavDocument;
import mx.cinvestav.tamps.crawling.exceptions.BllException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Implemements the functionality defined by a scraper for HTML documents
 * @author ealdana@tamps.cinvestav.mx
 */
public class HTMLScraper implements IScraper{

    @Override
    public CinvestavDocument getRawText(String url) throws BllException {
       CinvestavDocument cinvestavDocument;
       try{
            WebClient client =new WebClient(BrowserVersion.CHROME);
            
            client.getOptions().setCssEnabled(false);
            client.getOptions().setJavaScriptEnabled(false);
            HtmlPage page =client.getPage(url);
            Document doc = Jsoup.parse(page.asXml());
            String text=doc.body().text();
            cinvestavDocument=new CinvestavDocument();
            cinvestavDocument.setText(text);
            cinvestavDocument.setSource(new URL(url));
            cinvestavDocument.setRecoveringDate(new Date());
            
            //Obtain related resources associated to the document.
            Elements links = doc.select("a[href^=http]");
            if(links.isEmpty())
                Logger.getLogger(HTMLScraper.class.getName()).log(Level.WARNING, "No links on page {0}",url); 
            else{
                ArrayList <URL> relatedResources=new ArrayList<>();
                for(Element e1:links){
                        try{
                            relatedResources.add(new URL(e1.attr("href")));
                        }
                        catch(Exception ex)
                        {
                          Logger.getLogger(HTMLScraper.class.getName()).log(Level.WARNING, "Error obtaining related resource",ex); 
                          continue;
                        }    
                        cinvestavDocument.setRelatedResources(relatedResources);
            
                }
            }
                
            
                          
            
       }catch(IOException | FailingHttpStatusCodeException e)
       {  
          Object []params={url,e};
          Logger.getLogger(HTMLScraper.class.getName()).log(Level.SEVERE, "It was not possible to get information from {0}. Error:{1}",params);  
          throw new BllException(e);
       }    
        
       return cinvestavDocument; 
    }

    
    public static void main(String args[]){
        try{
            HTMLScraper html=new HTMLScraper();
            CinvestavDocument doc=html.getRawText("http://www.eluniversal.com.mx");
            System.out.println(doc.getText());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
