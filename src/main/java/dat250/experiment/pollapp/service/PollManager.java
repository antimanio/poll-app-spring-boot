package dat250.experiment.pollapp.service;

import org.springframework.stereotype.Component;
import dat250.experiment.pollapp.model.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class PollManager {
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Poll> polls = new HashMap<>();
    private final Map<Long, Vote> votes = new HashMap<>();

    private final AtomicLong userIdCounter = new AtomicLong(1);
    private final AtomicLong pollIdCounter = new AtomicLong(1);
    private final AtomicLong voteIdCounter = new AtomicLong(1);

    public User createUser(String username, String email) {
        User user = new User(username, email);
        long id = userIdCounter.getAndIncrement();
        users.put(id, user);
        return user;
    }

    public Optional<User> getUser(long id) {
        return Optional.ofNullable(users.get(id));
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public Poll createPoll(User creator, String question, Instant validUntil) {
        Poll poll = new Poll(question, Instant.now(), validUntil, creator);
        long id = pollIdCounter.getAndIncrement();
        polls.put(id, poll);
        creator.getPolls().add(poll);
        return poll;
    }

    public Optional<Poll> getPoll(long id) {
        return Optional.ofNullable(polls.get(id));
    }

    public Collection<Poll> getAllPolls() {
        return polls.values();
    }

    public VoteOption addVoteOption(Poll poll, String caption, int presentationOrder) {
        VoteOption option = new VoteOption(caption, presentationOrder, poll);
        poll.getVoteOptions().add(option);
        return option;
    }

    public Vote castVote(User voter, VoteOption option) {
        Optional<Vote> existingVote = votes.values().stream()
                .filter(v -> v.getVoter().equals(voter))
                .filter(v -> v.getVoteOption().getPoll().equals(option.getPoll()))
                .findFirst();

        if (existingVote.isPresent()) {
            existingVote.get().setVoteOption(option);
            existingVote.get().setPublishedAt(Instant.now());
            return existingVote.get();
        }

        Vote vote = new Vote(Instant.now(), option, voter);
        long id = voteIdCounter.getAndIncrement();
        votes.put(id, vote);
        voter.getVotes().add(vote);
        return vote;
    }

    public Collection<Vote> getAllVotes() {
        return votes.values();
    }

    public long countVotesForOption(VoteOption option) {
        return votes.values().stream()
                .filter(v -> v.getVoteOption().equals(option))
                .count();
    }
}
