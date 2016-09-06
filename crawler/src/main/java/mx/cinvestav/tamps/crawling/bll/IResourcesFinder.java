/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.cinvestav.tamps.crawling.bll;

import java.net.URL;
import java.util.ArrayList;
import mx.cinvestav.tamps.crawling.exceptions.BllException;

/**
 *Define the functionality of a "finder" of locations (URLs) of resources associated
 *to a given topic. 
 * @author ealdana@tamps.cinvestav.mx
 */
public interface IResourcesFinder {
            /**
             * Allows us to obtain a set of location associated to a query parameter
             * @param query String that contains the parameters to search a resource
             * using a search engine. Examples: <BR> 
             * getResources("zika virus");<BR>
             * getResources("zika+virus");<BR>
             * getResources("zika virus PDF");<BR>
             * getResources("'zika virus'");<BR>
             * @return Array with the locations found
             * @throws mx.cinvestav.tamps.crawling.exceptions.BllException
             */
            public ArrayList<URL> getResources(String query)throws BllException;

}
