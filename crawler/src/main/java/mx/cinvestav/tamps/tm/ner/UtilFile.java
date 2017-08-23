/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.cinvestav.tamps.tm.ner;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * 
 * @author marcofuentes
 */
public class UtilFile {
    public static String read(String path)throws Exception{
        String content="";
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path),"UTF-8"));
            String line;
            while((line = reader.readLine())!=null){
                content+=line+"\n";
            }
        }
        catch(Exception ex){
            throw new Exception("Error en la lectura del archivo."+ex.getMessage());
        }
        return content;
    }
    public static String[] toArray(String content) throws Exception{
        try {
            String [] datos = content.split("\n");
            
            return datos;
        } catch (Exception ex) {
            throw new Exception("Error al convertir archivo a matriz double."+ex.getMessage());
        }    
    }
}
