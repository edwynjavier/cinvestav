/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.cinvestav.tamps.crawling.exceptions;


/**
 * Allows to handle and get information about errors or exceptions thrown by 
 * components of the <b>data layer</b>.
 * @author ealdana
 */
public class DataException extends CrawlerException {

    /**
     * Creates a new instance without a detailed message.
     */
    public DataException() {
    }


    /**
     * Creates a new instance of an exception with a given message.
     * @param msg Detailed message.
     */
    public DataException(String msg) {
        super(msg);
    }
}
