/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.cinvestav.tamps.crawling.entities;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Defines the properties of a query executed by a <b> {@link mx.cinvestav.tamps.crawling.bll.ResourcesFinderManager}</b>
 * @author ealdana@tamps.cinvestav.mx
 */
@XmlRootElement
public class ResourcesQuery {
    
    /**
     * Date in which the query was executed
     */
    private Date queryDate;
    
    /**
     * Contains the criteria with the query is executed
     */
    private String criteria;
    
    /**
     * Contains the location of that resources obtained by the execution of a
     * query.
     */
    private ArrayList<URL> resourcesLocation;

        
    /**
     * Contains the criteria with the query was executed
     * @return the criteria
     */
    public String getCriteria() {
        return criteria;
    }

    /**
     * Contains the criteria with the query is executed
     * @param criteria the criteria to set
     */
    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    /**
     * Contains the location of that resources obtained by the execution of a
     * query.
     * @return the resourcesLocation
     */
    public ArrayList<URL> getResourcesLocation() {
        return resourcesLocation;
    }

    /**
     * Contains the location of that resources obtained by the execution of a
     * query.
     * @param resourcesLocation the resourcesLocation to set
     */
    public void setResourcesLocation(ArrayList<URL> resourcesLocation) {
        this.resourcesLocation = resourcesLocation;
    }

    /**
     * Date in which the query was executed
     * @return the queryDate
     */
    public Date getQueryDate() {
        return queryDate;
    }

    /**
     * Date in which the query was executed
     * @param queryDate the queryDate to set
     */
    public void setQueryDate(Date queryDate) {
        this.queryDate = queryDate;
    }
}
