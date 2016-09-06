/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.cinvestav.tamps.crawling.exceptions;


/**
 * Allows to handle and get information about errors or exceptions thrown by 
 * components of the <b>business layer</b>.
 * @author ealdana
 */
public class ServiceException extends CrawlerException {

    /**
     * Creates a new instance without a detailed message.
     */
    public ServiceException() {
    }


    /**
     * Creates a new instance of an exception with a given message.
     * @param msg Detailed message.
     */
    public ServiceException(String msg) {
        super("An error in the Services  Layer has ocurred, "+msg);
    }
    
    public ServiceException(Throwable t) {
        super("An error in the Services  Layer has ocurred, "+
                t.getClass().getCanonicalName()+":"+t.getMessage());
    }
    
  
}
