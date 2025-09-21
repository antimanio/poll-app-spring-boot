package JPA.models;

import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Poll> created;

    public User() {
        // required by JPA
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.created = new LinkedHashSet<>();
    }

    public Poll createPoll(String question) {
        Poll poll = new Poll(question, this);
        this.created.add(poll);
        return poll;
    }

    public Vote voteFor(VoteOption option) {
        return new Vote(this, option);
    }

    // getters and setters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public Set<Poll> getCreated() { return created; }
}
