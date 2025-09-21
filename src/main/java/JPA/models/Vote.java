package JPA.models;

import jakarta.persistence.*;

@Entity
@Table(name = "vote")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "voter_id")
    private User voter;

    @ManyToOne
    @JoinColumn(name = "vote_option_id")
    private VoteOption votesOn;

    public Vote() {
        // required by JPA
    }

    public Vote(User voter, VoteOption option) {
        this.voter = voter;
        this.votesOn = option;
    }

    // getters and setters
    public Long getId() { return id; }
    public User getVoter() { return voter; }
    public VoteOption getVotesOn() { return votesOn; }
}

