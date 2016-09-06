/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.cinvestav.tamps.util.net;

import com.maxmind.geoip.LookupService;
import java.io.File;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.cinvestav.tamps.util.geo.GeoLocation;

@SuppressWarnings("StaticNonFinalUsedInInitialization")
/**
 * Allow to determine location properties (eg. COUNTRY, CITY, GEOGRAPHICAL COORDINATES)
 * from an IP address
 * @author ealdana@tamps.cinvestav.mx
 */
public class GeoIp {

    private static LookupService lookUp;
    private static String DATABASE_DIRECTORY="target/classes/geoip";
    static {
        try {
            
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            lookUp=new LookupService(new File(classloader.getResource("GeoLiteCity.dat").getPath()));
            Logger.getLogger(LookupService.class.getName()).log(Level.FINE, "GeoIP Database loaded:{0}",lookUp.getDatabaseInfo());
            
        } catch (Throwable e) {
            Logger.getLogger(LookupService.class.getName()).log(Level.SEVERE, "Could not load geo ip database:{0}",e.getMessage());
                       
        }
    }

    /**
     * Allows to obtain GeoLocation object that contains location attributes.
     * @param ipAddress String ip address
     * @return GeoLocation object
     */
    public static GeoLocation getLocation(String ipAddress) {
        return GeoLocation.map(lookUp.getLocation(ipAddress));
    }
    /**
     * Allows to obtain GeoLocation object that contains location attributes.
     * @param ipAddress Numerical (long) ip address
     * @return GeoLocation object
     */
    public static GeoLocation getLocation(long ipAddress){
        return GeoLocation.map(lookUp.getLocation(ipAddress));
    }
    /**
     * Allows to obtain GeoLocation object that contains location attributes.
     * @param ipAddress InetAddress object that represents an ip address
     * @return GeoLocation object
     */
    public static GeoLocation getLocation(InetAddress ipAddress){
        return GeoLocation.map(lookUp.getLocation(ipAddress));
    }
    
    /*public static void main(String... args) throws UnknownHostException {

        long ipAddress = new BigInteger(InetAddress.getByName("148.247.202.31").getAddress()).longValue();

        System.out.println("By String IP address: \n" +
                GeoIp.getLocation("148.247.202.31"));

        System.out.println("By long IP address: \n" +
                GeoIp.getLocation(ipAddress));

        System.out.println("By InetAddress IP address: \n" +
                GeoIp.getLocation(InetAddress.getByName("151.38.39.114")));

    }*/
}