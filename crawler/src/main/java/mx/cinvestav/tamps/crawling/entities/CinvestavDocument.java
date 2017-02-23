/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.cinvestav.tamps.crawling.entities;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlRootElement;
import mx.cinvestav.tamps.util.geo.GeoLocation;
import mx.cinvestav.tamps.util.net.GeoIp;

/**
 * Allows to define the basic properties of a document in the context of the 
 * system.
 * @author ealdana@tamps.cinvestav.mx
 */
@XmlRootElement
public class CinvestavDocument {
    
    /**
     *Object GeoLocation that represents the physical location of the document. 
     */
    private GeoLocation physicalLocation;
    
    /**
     * Array that contains the links of related resources associated to this
     * document
     */
    private ArrayList <URL> relatedResources;
    /**
     * MIME type of the document
     */
    private String contentType;
    /**
     *Identifier of the document 
     */
    private String idDocument;
    /**
     * Source from which the document is recovered
     */
    private URL source;
    /**
     * Text of the document
     */
    private String text;
    /**
     * Date in which the document was recovered
     */
    private Date recoveringDate;
    
    /**
     * Date in which the document was updated
     */
    private Date updatingDate;
    
    /**
     * 
     */
    private String language;
    /**
     * Default constructor
     */
    public CinvestavDocument(){}

    /**
     * Allows to get the source (URL) of the document
     * @return the source
     */
    public URL getSource() {
        return source;
    }

    /**
     * Allows to set the source (URL) of the document
     * @param source the source to set
     */
    public void setSource(URL source) {
        this.source = source;
        if(this.source!=null)
        {  
            MessageDigest md;
            
            try {
                byte[] bytesOfMessage = source.toString().getBytes("UTF-8");
                md = MessageDigest.getInstance("MD5");
                byte[] thedigest = md.digest(bytesOfMessage);
                BigInteger bigInt = new BigInteger(1,thedigest);
                String hashtext = bigInt.toString(16);
                this.setIdDocument(hashtext);
                //Set the location of the document
                InetAddress address = InetAddress.getByName(source.getHost());
                //System.out.println("IP>>>>>>>>>>>>>>>>>>>>>>>>>>>"+address.toString()+"\n"+
                //                   GeoIp.getLocation(address).toString());
               // setPhysicalLocation(GeoIp.getLocation(address));
            } catch (Throwable ex) {
                 
                Logger.getLogger(CinvestavDocument.class.getName()).log(Level.SEVERE, null, ex);
                
            }
            
        }    
    }

    /**
     * Allows to get the text of a document
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Allows to set the text of a document
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Allows to get the recovering date
     * @return  recoveringDate
     */
    public Date getRecoveringDate() {
        return recoveringDate;
    }

    /**
     * Allows to set the recovering date
     * @param recoveringDate   
     */
    public void setRecoveringDate(Date recoveringDate) {
        this.recoveringDate = recoveringDate;
    }

    /**
     * Allows to get the updating date
     * @return updatingDate
     */
    public Date getUpdatingDate() {
        return updatingDate;
    }

    /**
     * Allows to set the updating date
     * @param updatingDate 
     */
    public void setUpdatingDate(Date updatingDate) {
        this.updatingDate = updatingDate;
    }

    /**
     * Allows to get the numerical  identifier of the document
     * @return the idDocument
     */
    public String getIdDocument() {
        return idDocument;
    }

    /**
     * Allows to set the numerical identifier of the document
     * @param idDocument the idDocument to set
     */
    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    /**
     * MIME type of the document
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * MIME type of the document
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType) {
        
        this.contentType = contentType;
    }

    /**
     * Array that contains the links of related resources associated to this
     * document
     * @return the relatedResources
     */
    public ArrayList<URL> getRelatedResources() {
        return relatedResources;
    }

    /**
     * Array that contains the links of related resources associated to this
     * document
     * @param relatedResources the relatedResources to set
     */
    public void setRelatedResources(ArrayList<URL> relatedResources) {
        this.relatedResources = relatedResources;
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Object GeoLocation that represents the physical location of the document.
     * @return the physicalLocation
     */
    public GeoLocation getPhysicalLocation() {
        return physicalLocation;
    }

    /**
     * Object GeoLocation that represents the physical location of the document.
     * @param physicalLocation the physicalLocation to set
     */
    public void setPhysicalLocation(GeoLocation physicalLocation) {
        this.physicalLocation = physicalLocation;
    }
    
}
