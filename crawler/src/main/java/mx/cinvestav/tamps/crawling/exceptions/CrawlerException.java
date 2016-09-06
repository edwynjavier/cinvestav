package mx.cinvestav.tamps.crawling.exceptions;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 * Allows to handle and get information about errors or exceptions thrown by 
 * components of the <b>business layer</b>.
 * @author ealdana
 */
public class CrawlerException extends Exception{

    /**
     * Crea una nueva instancia de  <code>CrawlerException</code> sin un mensaje detallado.
     */
    public CrawlerException() {
    }


    /**
     * Construye una instancia de <code>CaseManagerBllException</code> con un mensaje especifico.
     * @param msg Mensaje detallado.
     */
    public CrawlerException(String msg) {
        super(msg);
    }
}
