/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.cinvestav.tamps.crawling.exceptions;

/**
 *
 * @author LTI
 */
public class CinvestavDocumentException extends Exception{
    

   /**
     * Creates a new instance without a detailed message.
     */
    public CinvestavDocumentException() {
    }


    /**
     * Creates a new instance of an exception with a given message.
     * @param msg Detailed message.
     */
    public CinvestavDocumentException(String msg) {
        super(msg);
    }
    /**
     * 
     * @param t 
     */
     public CinvestavDocumentException(Throwable t) {
        super("An error associated to a CinvestavDocument has ocurred, "+
                t.getClass().getCanonicalName()+":"+t.getMessage());
    }
   
}
