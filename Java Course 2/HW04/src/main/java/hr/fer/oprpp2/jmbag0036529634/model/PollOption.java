package hr.fer.oprpp2.jmbag0036529634.model;

public class PollOption {
    private long id;
    private String optionTitle;
    private String optionLink;
    private long pollID;
    private long votesCount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOptionTitle() {
        return optionTitle;
    }

    public void setOptionTitle(String optionTitle) {
        this.optionTitle = optionTitle;
    }

    public String getOptionLink() {
        return optionLink;
    }

    public void setOptionLink(String optionLink) {
        this.optionLink = optionLink;
    }

    public long getPollID() {
        return pollID;
    }

    public void setPollID(long pollID) {
        this.pollID = pollID;
    }

    public long getVotesCount() {
        return votesCount;
    }

    public void setVotesCount(long votesCount) {
        this.votesCount = votesCount;
    }
}
