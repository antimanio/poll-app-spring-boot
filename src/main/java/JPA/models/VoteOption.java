package JPA.models;


import jakarta.persistence.*;

@Entity
@Table(name = "voteoption")
public class VoteOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String caption;

    private int presentationOrder;

    @ManyToOne
    @JoinColumn(name = "poll_id")
    private Poll poll;

    public VoteOption() {
        // required by JPA
    }

    public VoteOption(Poll poll, String caption, int presentationOrder) {
        this.poll = poll;
        this.caption = caption;
        this.presentationOrder = presentationOrder;
    }

    // getters and setters
    public Long getId() { return id; }
    public String getCaption() { return caption; }
    public int getPresentationOrder() { return presentationOrder; }
    public Poll getPoll() { return poll; }
}

