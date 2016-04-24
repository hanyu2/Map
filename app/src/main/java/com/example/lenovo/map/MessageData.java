package com.example.lenovo.map;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class MessageData implements Parcelable {

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

    public MessageData(){}

    public MessageData(Parcel in){
        //顺序要和writeToParcel写的顺序一样
        email = in.readString();
        time = in.readString();
        dead_time = in.readString();
        nick_name = in.readString();
        //in.readDoubleArray(location);!!!!!!!!!
        location = in.createDoubleArray();
        title = in.readString();
        message = in.readString();
        type = in.readInt();
        tag = in.createStringArray();
    }

    public String tagsToString(String[] tag){
        String result = "";
        for(int i=0;i<tag.length;i++){
            result += tag[i] + "#";
        }
        return result;
    }

    public String tagToString(String[] tag){
        String result = "";
        for(int i=0;i<tag.length;i++){
            result += tag[i] + " ";
        }
        result = result.trim();
        return result;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(email);
        dest.writeString(time);
        dest.writeString(dead_time);
        dest.writeString(nick_name);
        dest.writeDoubleArray(location);
        dest.writeString(title);
        dest.writeString(message);
        dest.writeInt(type);
        dest.writeStringArray(tag);
    }

    public static final Parcelable.Creator<MessageData> CREATOR = new Parcelable.Creator<MessageData>() {
        public MessageData createFromParcel(Parcel in) {
            return new MessageData(in);
        }

        public MessageData[] newArray(int size) {
            return new MessageData[size];
        }
    };
    public void setTag(String tagstr){
        int l = 0, r=0;
        List<String> ss = new ArrayList<String>();
        while(r < tagstr.length()){
            if(tagstr.charAt(r) == '#'){
                ss.add(tagstr.substring(l, r));
                l = r;
            }
            ++r;
        }
        if(l == 0) tag = new String[]{tagstr};
        else tag = ss.toArray(new String[0]);
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

    public double getLatitude(){return  location[0];}
    public double getLongitude(){return  location[1];}

}
