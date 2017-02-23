/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.cinvestav.tamps.crawling.bll;

import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.cinvestav.tamps.crawling.bll.impl.OFFICEScraper;
import mx.cinvestav.tamps.crawling.bll.impl.HTMLScraper;
import mx.cinvestav.tamps.crawling.bll.impl.PDFScraper;

/**
 * Allows to obtain an instance of IScraper in accordance to content-type 
 * @author ealdana@tamps.cinvestav.mx
 */
public class ScraperManager {
    
    public static String HTML="text/html";
    public static String PDF="application/pdf";
    public static String DOC="application/msword";
    public static String DOCX="application/vnd.openxmlformats";
    public static String PPT="application/vnd.ms-powerpoint";
    public static String PPTX="application/vnd.openxmlformats-officedocument.presentationml.presentation";
    public static String XLS="application/vnd.ms-excel";
    public static String XLSX="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";        
    private static final Map<String, String> DEFAULT_CONTENT_TYPES = new HashMap<>();
    static {
        DEFAULT_CONTENT_TYPES.put("doc", "application/msword");
        DEFAULT_CONTENT_TYPES.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        DEFAULT_CONTENT_TYPES.put("xls", "application/vnd.ms-excel");
        DEFAULT_CONTENT_TYPES.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        DEFAULT_CONTENT_TYPES.put("ppt", "application/vnd.ms-powerpoint");
        DEFAULT_CONTENT_TYPES.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
    }
    
    
    
    public static IScraper getInstance(String contentType)
    {
        contentType=(contentType==null||contentType.equals(""))?HTML:contentType;
        
        if(contentType.contains(HTML))
            return new HTMLScraper();
        else{
             if(contentType.contains(PDF))
                 return new PDFScraper();
             else{
                  if(contentType.contains(DOC)||
                     contentType.contains(DOCX)||
                     contentType.contains(PPT)||
                     contentType.contains(PPTX)||
                     contentType.contains(XLS)||
                     contentType.contains(XLSX))
                      return new OFFICEScraper();
                  else{
                        String [] params={IScraper.class.getName(),contentType};
                        Logger.getLogger(ScraperManager.class.getName()).log(Level.WARNING, "It was not possible"
                                + " to create an instance of {0} with the content-type {1}",
                                params); 
                       return null;
                  }     
             }
        }
    }
    /**b
     * 
     * @param url
     * @return 
     */
    public static String getContentType(String url)
    {   String contentType ="";
        try {
            
            contentType=URLConnection.guessContentTypeFromName(url);
            if(contentType==null)
            {
                 
                if(url.contains(".")) {
                     String extension = url.substring(url.lastIndexOf(".")+1);
                     contentType=DEFAULT_CONTENT_TYPES.get(extension);
                     
                }
            }    
            
            
        } catch (Throwable t) {
            Logger.getLogger(ScraperManager.class.getName()).log(Level.SEVERE, null, t);
        }
    
        return contentType;
    }
}

