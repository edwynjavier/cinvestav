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
import mx.cinvestav.tamps.crawling.bll.IResourcesFinder;
import mx.cinvestav.tamps.crawling.exceptions.BllException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Allows to find location of resources (documents) associated to a query using 
 * Google Scholar search engine.
 * @author ealdana@tamps.cinvestav.mx
 */
public class GoogleScholarResourcesFinder implements IResourcesFinder{

    @Override
    public ArrayList<URL> getResources(String query)throws BllException {
        ArrayList<URL> urls=new ArrayList<>();  
        try{
            WebClient client =new WebClient(BrowserVersion.CHROME);
            client.getOptions().setCssEnabled(false);
            client.getOptions().setJavaScriptEnabled(false);
            String url="https://scholar.google.com.mx/scholar?hl=en&q="+query;
                  
            HtmlPage page =client.getPage(url);
            Document doc =Jsoup.parse(page.asXml());
            Logger.getLogger(GoogleScholarResourcesFinder.class.getName()).log(Level.FINE, "Query executed");
            Elements links = doc.select("a[href^=http]");
            String strLink;
            for (Element link : links) {
                strLink=link.attr("href");
                //Possible exceptions found
                if(!strLink.contains("https://accounts.google.com")&&
                   !strLink.contains("https://scholar.googleusercontent.com/scholar?q=cache")&&
                   !strLink.contains("http://scholar.googleusercontent.com/scholar?q=cache") ){
                    urls.add(new URL(strLink));
                    Logger.getLogger(GoogleScholarResourcesFinder.class.getName()).log(Level.FINE, "{0} was added.", strLink);
                }    
            }
                                  
       }catch(IOException | FailingHttpStatusCodeException e)
       {
           Logger.getLogger(GoogleScholarResourcesFinder.class.getName()).log(Level.SEVERE, null,e);
           throw new BllException(e);
       }
        
        return urls;
    }
    
}
    

