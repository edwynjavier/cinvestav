package mx.cinvestav.tamps.tm.ner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.cinvestav.tamps.crawling.entities.CinvestavDocument;
import mx.cinvestav.tamps.crawling.services.CrawlerServices;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.apache.commons.io.FileUtils;
import org.apache.tika.language.LanguageIdentifier;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author cgaytan@tamps.cinvestav.mx
 */
public class EntitiesCrawling {
    
    /**
     * Time in milliseconds that the crawler wait, after a request/search has 
     * end, to send another.
     */
    private int delay = 10000; //10 seconds
    
    /**
     * File object representing the directory (url path) that contains all the
     * Sentence Detector Model files that OpenNLP needs to segment texts into 
     * sentences.
     */
    private File models;

    /**
     * File object representing the directory (url path) where all the files
     * will be saved.
     */
    private File directory;
    
    /**
     * Allows to get all de documents/recourses given a query.
     */
    private CrawlerServices crawler;
    
    /**
     * A list with all the entities that the crawler will download/search.
     */
    private List<Entity> entities;

    public EntitiesCrawling(List<Entity> entities, File directory,
            File models) {
        this.entities = entities;
        this.models = models;
        this.directory = directory;
        this.crawler = new CrawlerServices();
    }
    
    /**
     * For each entity from the "entities" object list, do the fallow: 
     * 1) sends a query to Google, 
     * 2) download each query result/resource, 
     * 3) parse its cointain into a plane text, 
     * 4) segment the text into sentences, 
     * 5) normilize each sentences,
     */
    public void crawl() {
        File file;
        List<String> sentences;
        String query, name, filename;
        List<CinvestavDocument> docs;
        for (Entity entity : entities) {
            sentences = new ArrayList();
            query = entity.name + entity.context; //DESCRIBE WHY "context"
            docs = crawler.getDocuments(query, entity.lang, "", "");
            for (CinvestavDocument doc : docs) {
                name = normalizeText(entity.name);
                for (String sentence : sentenceSegmenter(doc.getText())) {
                    sentence = normalizeText(sentence);
                    if (sentence.contains(name)) {
                        if (!sentences.contains(sentence)) {
                            sentences.add(sentence);
                        }
                    }
                }
            }
            
            if (!sentences.isEmpty()) {
                filename = entity.lang + " " + 
                           entity.type + " " + 
                           entity.subtype + " " + 
                           entity.name;
                filename = normalizeText(filename).replaceAll(" ", "-");
                filename += ".json";
                file = new File(this.directory, filename);
                saveFile(file, entity, sentences);
            }

            try {
                Thread.sleep(this.delay);
            } catch (InterruptedException e) {
                Logger.getLogger(EntitiesCrawling.class.getName()).log(
                        Level.SEVERE,"Error crawling: can't delay download.",e);
            }
            System.out.println("\n- - - - - - - - - - - - - - - - - - - - -\n");
        }
    }

    /**
     * Segments a String object (text) into sentences. The segmentation is done
     * with the OpenNLP Sentence Detector  tool, so, it requires a Model file.
     * @param   text        a String object to be segmented.
     * @return              a String object list of the sentences segmented.
     * @throws IOException  If the Sentence Detector Model file doesn't exist.
     */
    private List<String> sentenceSegmenter(String text) {
        List<String> sentences = new ArrayList();
        String lang = null;
        try {
            lang = new LanguageIdentifier(text).getLanguage();
        } catch (Throwable t) {
            Logger.getLogger(EntitiesCrawling.class.getName()).log(Level.SEVERE, 
                "Error segmenting sentences: text language not identified.", t);
            return sentences;
        }
        
        String fileName = lang + ".bin";
        if (!Arrays.asList(this.models.list()).contains(fileName)) {
            Logger.getLogger(EntitiesCrawling.class.getName()).log(Level.SEVERE, 
                "Error segmenting sentences: \"" + lang + "\" language not "
                    + "supported. Instead, english default language segmenter "
                    + "will be used.");
            fileName = "en.bin";
        }
        
        try {
            File file = new File(this.models.getAbsoluteFile(), fileName);
            InputStream inputStream = new FileInputStream(file);
            SentenceModel model = new SentenceModel(inputStream);
            SentenceDetectorME detector = new SentenceDetectorME(model);
            sentences = Arrays.asList(detector.sentDetect(text));
        } catch (IOException ex) {
            Logger.getLogger(EntitiesCrawling.class.getName()).log(Level.SEVERE, 
                "Error segmenting sentences: Sentence Detector Model file "
                    + "doesn't exist.", ex);
        }
        return sentences;
    }

    /**
     * Saves an Entity object and a list of sentences into a file text in a JSON 
     * format.
     * @param   file        File object (url path) where the given Entity object
     *                      and the given list of sentences will be saved.
     * @param   entity      Entity object to be saved.
     * @param   sentences   String object list to be saved.
     * @return              true if the file was successfully saved, or false if 
     *                      not.
     * @throws IOException  If is not possible to create a new file, or write on
     *                      it.
     */
    private boolean saveFile(File file, Entity entity, List<String> sentences) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Logger.getLogger(EntitiesCrawling.class.getName()).log(
                    Level.SEVERE,"Error saving file: can't create new file.",e);
                return false;
            }
        }
        
        JSONObject jsonFile = new JSONObject();
                   jsonFile.put("name", entity.name);
                   jsonFile.put("type", entity.type);
                   jsonFile.put("subtype", entity.subtype);
                   jsonFile.put("hypernym", entity.hypernym);
                   jsonFile.put("lang", entity.lang);
                   jsonFile.put("sentences", new JSONArray(sentences));

        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(jsonFile.toString(4));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(EntitiesCrawling.class.getName()).log(Level.SEVERE, 
                    "Error saving file: can't write on file.", ex);
            return false;
        }
        
        return true;
    }

    /**
     * From a URL, its gets only the protocolo, host, domain, an path. For
     * example from "http://www.eluniversal.com.mx/#Deportes" or
     * "http://www.eluniversal.com.mx/search?get=deportes" it return
     * "http://www.eluniversal.com.mx/".
     * @param   url                     String object (URL) to be parsed.
     * @return                          a parsed URL, or null if not URL.
     * @throws MalformedURLException    If the given URL is not a URL.
     */
    public static String parseURL(String url) {
        try {
            URL parseURL = new URL(url);
            return parseURL.getProtocol() + 
                   "://" + 
                   parseURL.getHost() + 
                   parseURL.getPath();
        } catch (MalformedURLException ex) {
            Logger.getLogger(EntitiesCrawling.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Normalize string by removing symbols, numbers, and diacritics (7 bit 
     * approximation). Also, it returns the string in low case.
     * @param   text    String object to normalize.
     * @return          a text normalized.
     */
    public static String normalizeText(String text) {
        char one;
        char[] vysl = new char[text.length()];
        String tab00c0 = "AAAAAAACEEEEIIIIDNOOOOO\u00d7\u00d8UUUUYI\u00dfaaaaaa"
                + "ceeeeiiii\u00f0nooooo\u00f7\u00f8uuuuy\u00feyAaAaAaCcCcCcCcD"
                + "dDdEeEeEeEeEeGgGgGgGgHhHhIiIiIiIiIiJjJjKkkLlLlLlLlLlNnNnNnnN"
                + "nOoOoOoOoRrRrRrSsSsSsSsTtTtTtUuUuUuUuUuUuWwYyYZzZzZzF";
        for (int i = 0; i < text.length(); i++) {
            one = text.charAt(i);
            if (one >= '\u00c0' && one <= '\u017f') {
                one = tab00c0.charAt((int) one - '\u00c0');
            }
            vysl[i] = one;
        }
        text = new String(vysl);
        text = text.replaceAll("[^a-zA-Z ]", "");
        text = text.replaceAll("[0-9]", "");
        text = text.replaceAll("\\s+", " ");
        text = text.toLowerCase();
        text = text.trim();
        text = Normalizer.normalize(text, Normalizer.Form.NFD);
        return text;
    }

    /**
     * Normalize a string list by removing symbols, numbers, and diacritics (7 
     * bit approximation). Also, it returns the strings in low case.
     * @param   texts   String object list to normalize.
     * @return          text normalized.
     */
    public static List<String> normalizeText(List<String> texts) {
        for (int i = 0; i < texts.size(); i++) {
            texts.set(i, normalizeText(texts.get(i)));
        }
        return texts;
    }

    public static void main(String[] args) throws Exception {
        //MAC
        File directoryDocuments = new File("/Users/cristopherdiaz/ner/docs/es/loc");
        File directoryEntities = new File("/Users/cristopherdiaz/Dropbox/lexicons/es/loc/mac");
        File directoryModels = new File("/Users/cristopherdiaz/Dropbox/cinvestav/tesis/software/cinvestav_crawler/crawler/lib/opennlp/sentencedetector");

        //UBUNTU
//        File directoryDocuments = new File("/home/cristopherrgd/ner/docs/es/loc");
//        File directoryEntities = new File("/home/cristopherrgd/Dropbox/lexicons/es/loc/ubuntu");
//        File directoryModels = new File("/home/cristopherrgd/Dropbox/cinvestav/tesis/software/cinvestav_crawler/crawler/lib/opennlp/sentencedetector");
        
        /* Gets all the entities that have been download. */
        String text;
        JSONObject json;
        Entity entity;
        File[] files = directoryDocuments.listFiles();
        List<Entity> entitiesDownloaded = new ArrayList();
        for (File f : files) {
            if (f.getName().endsWith(".json")) {
                text = FileUtils.readFileToString(f);
                json = new JSONObject(text);
                entity = new Entity(
                        json.getString("lang"),
                        json.getString("type"),
                        json.getString("subtype"),
                        //                        json.getString("hypernym"),
                        //                        json.getString("context"),
                        json.getString("name")
                );
                entitiesDownloaded.add(entity);
            }
        }

        /* Gets all the entitites from lexicon files especified of the folder.*/
        Entity e;
        String[] split;
        files = directoryEntities.listFiles();
        List<Entity> entitiesLexicon = new ArrayList();
        for (File f : files) {
            if (f.getName().endsWith(".txt")) {
                for (String line : FileUtils.readLines(f)) {
                    try {
                        split = line.split("\t");
                        e = new Entity(split[0], split[1], split[2],
                                       split[3], split[4], split[5]);
                        entitiesLexicon.add(e);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            }
        }
        
        /* Remove all the entities that have been download. */
        entitiesLexicon.removeAll(entitiesDownloaded);
        
        
        /* start crawling */
        EntitiesCrawling crawler = new EntitiesCrawling(entitiesLexicon, 
                                                        directoryDocuments, 
                                                        directoryModels);
        crawler.crawl();
    }
}