/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.cinvestav.tamps.crawling.bll.impl;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLProtocolException;
import mx.cinvestav.tamps.crawling.bll.IScraper;
import mx.cinvestav.tamps.crawling.entities.CinvestavDocument;
import mx.cinvestav.tamps.crawling.exceptions.BllException;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * Implements the functionality of a scraper for PDF documents
 * @author EALDANA
 */
public class PDFScraper implements IScraper {

    @Override
    public CinvestavDocument getRawText(String url) throws BllException, IOException{
          InputStream is = null;
          CinvestavDocument doc=new CinvestavDocument();
            try {
              
              WebClient client =new WebClient(BrowserVersion.CHROME);
              is=client.getPage(url).getUrl().openStream();
                            
              ContentHandler contenthandler = new BodyContentHandler(-1); //Unlimited length of text
              Metadata metadata = new Metadata();
              //metadata.set("org.apache.tika.parser.pdf.sortbyposition", "true");
              PDFParser pdfparser = new PDFParser();
              pdfparser.parse(is, contenthandler, metadata, new ParseContext());
              
              doc.setText(contenthandler.toString());
              doc.setSource(new URL(url));
              doc.setRecoveringDate(new Date());
              Logger.getLogger(GoogleResourcesFinder.class.getName()).log(Level.FINE, "The resource:{0}\n was obtained",url);                
            }
            catch (Throwable e) {
                 if(e instanceof SAXException|
                    e instanceof TikaException){//Exceptions associated to parsing process
                      Logger.getLogger(PDFScraper.class.getName()).log(Level.SEVERE, "Error in the parsing process "
                              + "of the resource.",e);
                    throw new BllException(e);
                 }else //Other exceptions
                 {  if(e instanceof IOException ) { 
                        String msg=(e instanceof SSLProtocolException)?"Include the following option in your server:\n"
                                + "-Djsse.enableSNIExtension=false":"It is possible that security credentials are required";
                        doc.setText("The text of the document cannot be obtained. "
                                + msg+".["
                                + e.getMessage()+"]");
                        doc.setSource(new URL(url));
                        doc.setRecoveringDate(new Date());
                        Logger.getLogger(PDFScraper.class.getName()).log(Level.WARNING, msg,e);
                    }
                 }
                 
            }
            finally {
                if (is != null) is.close();
                Logger.getLogger(PDFScraper.class.getName()).log(Level.FINE, "The stream was closed");                
            }
            return doc;
    }
    
}
