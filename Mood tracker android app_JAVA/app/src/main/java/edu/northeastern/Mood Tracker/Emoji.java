package edu.northeastern.mobileapplicationteam18;

public class Emoji implements Comparable<Emoji> {
    public int id;
    public String fromUser;
    public String toUser;
    public String sendTime;

    public Emoji(int id, String sender, String receiver, String sendTime) {
        this.id = id;
        this.fromUser = sender;
        this.toUser = receiver;
        this.sendTime = sendTime;
    }

    public String getKey() {
        return id + "|" + fromUser + "|" + toUser + "|" + sendTime;
    }

    @Override
    public int compareTo(Emoji other) {
        return this.sendTime.compareTo(other.sendTime);
    }
}
