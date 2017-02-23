/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.cinvestav.tamps.util.geo;

import com.maxmind.geoip.Location;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GeoLocation {

    private final String countryCode;
    private final String countryName;
    private final String postalCode;
    private final String city;
    private final String region;
    private final int areaCode;
    private final int dmaCode;
    private final int metroCode;
    private final float latitude;
    private final float longitude;

    public GeoLocation(String countryCode, String countryName, String postalCode, String city, String region,
                       int areaCode, int dmaCode, int metroCode, float latitude, float longitude) {
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.postalCode = postalCode;
        this.city = city;
        this.region = region;
        this.areaCode = areaCode;
        this.dmaCode = dmaCode;
        this.metroCode = metroCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public GeoLocation()
    {
        this.countryCode = "";
        this.countryName = "";
        this.postalCode = "";
        this.city = "";
        this.region = "";
        this.areaCode = 0;
        this.dmaCode = 0;
        this.metroCode = 0;
        this.latitude = 0.0f;
        this.longitude = 0.0f; 
    }        

    // -- getters ommitted

    public static GeoLocation map(Location loc){
        try{
            return new GeoLocation(loc.countryCode, loc.countryName, loc.postalCode, loc.city, loc.region,
                loc.area_code, loc.dma_code, loc.metro_code, loc.latitude, loc.longitude);
        }
        catch(Exception e){
           return new GeoLocation();
        }
        
    }

    @Override
    public String toString() {
        return "GeoLocation{" +
                "countryCode='" + countryCode + '\'' +
                ", countryName='" + countryName + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                ", region='" + region + '\'' +
                ", areaCode=" + areaCode +
                ", dmaCode=" + dmaCode +
                ", metroCode=" + metroCode +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}