/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.cinvestav.tamps.crawling.bll;

import java.util.logging.Level;
import java.util.logging.Logger;
import mx.cinvestav.tamps.crawling.bll.impl.GoogleResourcesFinder;
import mx.cinvestav.tamps.crawling.bll.impl.GoogleScholarResourcesFinder;
import mx.cinvestav.tamps.crawling.bll.impl.HTMLScraper;

/**
 * Defines some utilitary methods to manage {@link IResourcesFinder} implementations
 * @author ealdana@tamps.cinvestav.mx
 */
public class ResourcesFinderManager {
 
    public static String LANGUAGE="eng";
    public static String GOOGLE_ENGINE="google";
    public static String GOOGLE_SCHOLAR_ENGINE="googleScholar";
    /**
     * Allows to create an instance of  an IResourceFinder implementation.
     * @param engineName String whose values are: google, googleScholar
     * @return IResourceFinder implementation.
     */
    public static IResourcesFinder getInstance(String engineName){
        
        engineName=(engineName==null||engineName.equals(""))?GOOGLE_ENGINE:engineName;
        
        if(engineName.equals(GOOGLE_ENGINE))
            return new GoogleResourcesFinder();
        else{
             if(engineName.equals(GOOGLE_SCHOLAR_ENGINE))
                 return new GoogleScholarResourcesFinder();
             else{
                 
                 
                  String [] params={IResourcesFinder.class.getName(),engineName};
                  Logger.getLogger(HTMLScraper.class.getName()).log(Level.WARNING, "It was not possible"
                          + " to create an instance of {0} with the search engine {1}",
                          params);    
                 return null;
                    
             }
        }
            
    }
}
