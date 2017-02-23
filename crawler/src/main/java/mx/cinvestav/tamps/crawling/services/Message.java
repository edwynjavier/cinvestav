/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.cinvestav.tamps.crawling.services;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author LTI
 */

@XmlRootElement
public class Message {
    private String sender;
    private String content;
    
    public Message() {
    }
    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }
    
    //Setter and getter methods
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}