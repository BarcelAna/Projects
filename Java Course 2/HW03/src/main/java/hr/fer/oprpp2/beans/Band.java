package hr.fer.oprpp2.beans;

import java.util.Comparator;

public class Band {
    private String id;
    private String name;
    private String url;
    private int votes;

    public Band(String id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }


}
