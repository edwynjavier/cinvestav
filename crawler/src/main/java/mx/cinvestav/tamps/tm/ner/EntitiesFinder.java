package mx.cinvestav.tamps.tm.ner;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mx.cinvestav.tamps.util.file.UtilFile;

/**
 * It allows us to apply different tasks relative to the search process of named
 * entities from a text dataset.
 * @author marcofuentes
 */
public class EntitiesFinder {
    private static final String tab00c0 = "AAAAAAACEEEEIIII" +
    "DNOOOOO\u00d7\u00d8UUUUYI\u00df" +
    "aaaaaaaceeeeiiii" +
    "\u00f0nooooo\u00f7\u00f8uuuuy\u00fey" +
    "AaAaAaCcCcCcCcDd" +
    "DdEeEeEeEeEeGgGg" +
    "GgGgHhHhIiIiIiIi" +
    "IiJjJjKkkLlLlLlL" +
    "lLlNnNnNnnNnOoOo" +
    "OoOoRrRrRrSsSsSs" +
    "SsTtTtTtUuUuUuUu" +
    "UuUuWwYyYZzZzZzF";
    static Connection con=null;
    static String DIRECTORY="data";
    
    /**
     * It allows us to obtain preliminar tokens relative to possible entities
     * associated into the text of a document
     * @param fileName Document name
     * @param schema   Database schema in which the information will be stored
     * @param length   Length of the tokens to be stored
     * @param type     Type of cantidate entity
     * @param lang     Language identifier
     * @throws Exception 
     */
    public  void processDataEntity(String fileName, String schema, int length,
                                   char type, String lang)throws Exception
    {
        int i;
        String place=fileName.substring(0, fileName.indexOf(".txt"));
        place=removeDiacritic(place);
        place=place.toLowerCase();
        String content = UtilFile.read(DIRECTORY+"/"+fileName);
        String[] lines = UtilFile.toArray(content);
        String q,sql, p, s;
        PreparedStatement stm = null;
        for(String line : lines){              
            line=removeDiacritic(line);
            line = line.replaceAll("[^a-zA-Z ]", "").toLowerCase();
            line = line.replaceAll("[0-9]", "");
            line = Normalizer.normalize(line, Normalizer.Form.NFD);
            
            ArrayList<String> words = new ArrayList<String>();
            Pattern pattern = Pattern.compile("\\b(?:"+ place +")\\b|\\w+");
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                words.add(matcher.group(0));
            }
            
            for (i = 0; i < words.size(); i++) {
                q = words.get(i);
                if(q.equals(place)) {
                    p = ""; s = "";
                    for(int j = length; j > 0; j--) {
                        try{
                            p += words.get(i - j) + " ";
                        } catch(Error e){
                             Logger.getLogger(EntitiesFinder.class.getName()).log(Level.WARNING, null, e);
                        } finally{
                            continue;
                        }
                    }
                    for(int j = 1; j <= length; j++){
                        try{
                            s += words.get(i + j) + " ";
                        } catch(Error e){
                            Logger.getLogger(EntitiesFinder.class.getName()).log(Level.WARNING, null, e);
                        } finally{
                            continue;
                        }
                    }
                    sql = "INSERT INTO " + schema + ".candidate_entities(sentence,pre,name,suf, length, type, lang) "
                            + "values (?,?,?,?,?,?,?)";
                    
   
                    stm=con.prepareStatement(sql);
                    stm.setString(1, line);
                    stm.setString(2, p);
                    stm.setString(3, q);
                    stm.setString(4, s);
                    stm.setInt(5, length);
                    stm.setString(6, String.valueOf(type));
                    stm.setString(7, String.valueOf(lang));
                    stm.execute();
                }
            }
        }// primer for
    }
    
    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String schema = "locations_repository";
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+schema+"?allowMultiQueries=true", "root", "tresct");
        File directorio= new File("data");
        String [] archivos = directorio.list();
		
		//Number of prefixes and suffixes
        int length = 3;
        char type='L';
        EntitiesFinder ef=new EntitiesFinder();
        for(String s:archivos){
            try{
             ef.processDataEntity(s, schema, length,type,"ESP");
             System.out.println("Archivo "+s+" procesado");
            }
            catch(Exception e)
            {
                System.out.println("Error procesando archivo "+s+":"+e.getMessage());
            }
        }
        
        if(con!=null)
        {con.close();
         System.out.println("conexion cerrada");
        }
    }
    
    /**
    * Returns string without diacritics - 7 bit approximation.
    * @param source string to convert
    * @return corresponding string without diacritics
    */
    public static String removeDiacritic(String source) {
       char[] vysl = new char[source.length()];
       char one;
       for (int i = 0; i < source.length(); i++) {
           one = source.charAt(i);
           if (one >= '\u00c0' && one <= '\u017f') {
               one = tab00c0.charAt((int) one - '\u00c0');
           }
           vysl[i] = one;
       }
       return new String(vysl);
   }
}