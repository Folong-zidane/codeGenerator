package com.basiccode.generator.model;

import java.util.*;

public class SequenceDiagram {
    private List<Participant> participants = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();
    private String title;
    
    public List<Participant> getParticipants() { return participants; }
    public void setParticipants(List<Participant> participants) { this.participants = participants; }
    
    public List<Message> getMessages() { return messages; }
    public void setMessages(List<Message> messages) { this.messages = messages; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}

class Participant {
    private String name;
    private String type;
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}