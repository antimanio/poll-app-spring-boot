package dat250.experiment.pollapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class VoteOption {
    private String caption;
    private int presentationOrder;

    @JsonBackReference
    private Poll poll;

    public VoteOption() {}

    public VoteOption(String caption, int presentationOrder, Poll poll) {
        this.caption = caption;
        this.presentationOrder = presentationOrder;
        this.poll = poll;
    }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    public int getPresentationOrder() { return presentationOrder; }
    public void setPresentationOrder(int presentationOrder) { this.presentationOrder = presentationOrder; }

    public Poll getPoll() { return poll; }
    public void setPoll(Poll poll) { this.poll = poll; }
}
