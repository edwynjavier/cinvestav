/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.cinvestav.tamps.crawling.bll;

import java.io.IOException;
import mx.cinvestav.tamps.crawling.entities.CinvestavDocument;
import mx.cinvestav.tamps.crawling.exceptions.BllException;

/**
 * Defines the functionality associated to a scraper component. In general, an
 * scraper allows us to obtain the text from a textual resource (html, pdf, doc, etc.)
 * given its URL.
 * @author ealdana@tamps.cinvestav.mx
 */
public interface IScraper {
    
    /**
     * Allows to obtain the raw text from a textual resource with the URL given as parameter
     * @param url URL object that represents the location of the <b>textual</b> resource.
     * @return String that contains the text of the document.
     * @throws mx.cinvestav.tamps.crawling.exceptions.BllException
     * @throws java.io.IOException
     */
    public  CinvestavDocument getRawText(String url)throws BllException, IOException;

    
}
