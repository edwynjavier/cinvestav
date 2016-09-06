/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.cinvestav.tamps.hipparchus.crawler;

/**
 *
 * @author LTI
 */
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.net.*;
import java.io.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WikiScraper {
 public static void main(String[] args) {
        scrapeTopic("/wiki/Zika_fever");
 }

 public static void scrapeTopic(String url){
    String html = getUrl("http://www.eltiempo.com");//"https://en.wikipedia.org"+url);
    Document doc = Jsoup.parse(html);
    String text=doc.body().text();
    /*Element content = doc.getElementById("div");
    Elements links = content.getElementsByTag("p");
    for (Element link : links) {
      //String linkHref = link.attr("href");
      String linkText = link.text();
      
      System.out.println(linkText);
    }*/

    //String contentText = doc.select("p").first().text();
    System.out.println(text);
 }

 public static String getUrl(String url){
        URL urlObj = null;
        try{
            urlObj = new URL(url);
        }
        catch(MalformedURLException e){
           System.out.println("The url was malformed!");
         return "";
        }
        URLConnection urlCon = null;
        BufferedReader in = null;
        String outputText = "";
        try{
            urlCon = urlObj.openConnection();
            in = new BufferedReader(new
            InputStreamReader(urlCon.getInputStream()));
            String line = "";
            while((line = in.readLine()) != null){
                outputText += line;
            }
        in.close();
        }
        catch(IOException e){
            System.out.println("There was an error connecting to the URL");
            return "";
        }
        return outputText;
 }
 
}