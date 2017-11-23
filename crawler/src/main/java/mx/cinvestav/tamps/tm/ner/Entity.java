package mx.cinvestav.tamps.tm.ner;

/**
 * @author cgaytan@tamps.cinvestav.mx
 */
public class Entity {
    public String lang;
    public String type;
    public String subtype;
    public String hypernym;
    public String name;
    public String context;
    
    public Entity(String l, String t, String s, String n) {
        this.lang = l;
        this.type = t;
        this.subtype = s;
        this.name = n;
    }
    
    public Entity(String l, String t, String s, String h, String n, String c) {
        this.lang = l;
        this.type = t;
        this.subtype = s;
        this.hypernym = h;
        this.name = n;
        this.context = c;
    }
    
    public String toString() {
        return this.lang + "," + 
               this.type + "," + 
               this.subtype + "," +
               this.hypernym + "," +
               this.name + "," +
               this.context;
    }
    
    public boolean equals(Object o) {
        Entity e = (Entity)o;
        return e.lang.equals(this.lang) &&
               e.type.equals(this.type) &&
               e.subtype.equals(this.subtype) &&
               e.name.equals(this.name);
    }
}