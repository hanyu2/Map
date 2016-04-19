package com.example.lenovo.map;

import java.util.ArrayList;
import java.util.List;

public class MessageData {

    private String email;
    private String time;
    private String dead_time;
    private String nick_name;
    private double[] location;
    private String title;
    private String message;
    private int type;//personal commercial social event
    private String[] tag;

    public MessageData(String email,String time,String dead_time,String nick_name,double[] location,String title,String message,int type,String[] tag){
        this.setEmail(email);
        this.setTime(time);
        this.setDead_time(dead_time);
        this.setNick_name(nick_name);
        this.setLocation(location);
        this.setTitle(title);
        this.setMessage(message);
        this.setType(type);
        this.setTag(tag);
    }

    public MessageData(){

    }

    public String tagsToString(String[] tag){
        String result = "";
        for(int i=0;i<tag.length;i++){
            result += tag[i] + "#";
        }
        return result;
    }

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getTime() {return time;}
    public void setTime(String time) {this.time = time;}
    public String getDead_time() {return dead_time;}
    public void setDead_time(String dead_time) {this.dead_time = dead_time;}
    public String getNick_name() {return nick_name;}
    public void setNick_name(String nick_name) {this.nick_name = nick_name;}
    public double[] getLocation() {return location;}
    public void setLocation(double[] location) {this.location = location;}
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getMessage() {return message;}
    public void setMessage(String message) {this.message = message;}
    public int getType() {return type;}
    public void setType(int type) {this.type = type;}
    public String[] getTag() {return tag;}
    public void setTag(String[] tag) {this.tag = tag;}

    public void setTag(String tagstr){
        int l = 0, r=0;
        List<String> ss = new ArrayList<String>();
        while(r < tagstr.length()){
            if(tagstr.charAt(r) == '#'){
                ss.add(tagstr.substring(l, r-1));
                l = r;
            }
            ++r;
        }
        if(l == 0) tag = new String[]{tagstr};
        else tag = (String[])(ss.toArray());
    }

    public String MToString(){
        String output = "<[email]=" + email + ">";
        output += ", <[time]=" + time + ">";
        output += ", <[deadtime]=" + dead_time + ">";
        output += ", <[lat]=" + location[0] + ">";
        output += ", <[lng]=" + location[1] + ">";
        output += ", <[title]=" + title + ">";
        output += ", <[description]=" + message + ">";
        output += ", <[type]=" + type + ">";
        output += ", <[tags]=" + tagsToString(tag) + ">";
        return output;
    }
}
