package dat250.experiment.pollapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityReference;

import java.time.Instant;

public class Vote {
    private Instant publishedAt;

    @JsonIdentityReference(alwaysAsId = true)
    private VoteOption voteOption;

    @JsonBackReference
    private User voter;

    public Vote() {}

    public Vote(Instant publishedAt, VoteOption voteOption, User voter) {
        this.publishedAt = publishedAt;
        this.voteOption = voteOption;
        this.voter = voter;
    }

    public Instant getPublishedAt() { return publishedAt; }
    public void setPublishedAt(Instant publishedAt) { this.publishedAt = publishedAt; }

    public VoteOption getVoteOption() { return voteOption; }
    public void setVoteOption(VoteOption voteOption) { this.voteOption = voteOption; }

    public User getVoter() { return voter; }
    public void setVoter(User voter) { this.voter = voter; }
}
