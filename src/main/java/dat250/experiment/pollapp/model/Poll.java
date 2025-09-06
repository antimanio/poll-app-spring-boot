package dat250.experiment.pollapp.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Poll {
    private String question;
    private Instant publishedAt;
    private Instant validUntil;
    private User creator;
    private List<VoteOption> voteOptions = new ArrayList<>();

    public Poll() {}

    public Poll(String question, Instant publishedAt, Instant validUntil, User creator) {
        this.question = question;
        this.publishedAt = publishedAt;
        this.validUntil = validUntil;
        this.creator = creator;
    }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public Instant getPublishedAt() { return publishedAt; }
    public void setPublishedAt(Instant publishedAt) { this.publishedAt = publishedAt; }

    public Instant getValidUntil() { return validUntil; }
    public void setValidUntil(Instant validUntil) { this.validUntil = validUntil; }

    public User getCreator() { return creator; }
    public void setCreator(User creator) { this.creator = creator; }

    public List<VoteOption> getVoteOptions() { return voteOptions; }
    public void setVoteOptions(List<VoteOption> voteOptions) { this.voteOptions = voteOptions; }
}
