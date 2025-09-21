package JPA.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "polls")
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("presentationOrder ASC")
    private List<VoteOption> options;

    public Poll() {
        // required by JPA
    }

    public Poll(String question, User createdBy) {
        this.question = question;
        this.createdBy = createdBy;
        this.options = new ArrayList<>();
    }

    public VoteOption addVoteOption(String caption) {
        int order = this.options.size();
        VoteOption option = new VoteOption(this, caption, order);
        this.options.add(option);
        return option;
    }

    // getters and setters
    public Long getId() { return id; }
    public String getQuestion() { return question; }
    public User getCreatedBy() { return createdBy; }
    public List<VoteOption> getOptions() { return options; }
}
