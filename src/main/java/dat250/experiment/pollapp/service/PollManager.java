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
        user.setId(id);
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
        poll.setId(id);
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

    public Vote upVote(User voter, VoteOption option) {
        Vote vote = new Vote(Instant.now(), option, voter);
        long id = voteIdCounter.getAndIncrement();
        votes.put(id, vote);
        voter.getVotes().add(vote);
        return vote;
    }

    public Vote downVote(User voter, VoteOption option) {
        Optional<Vote> existingVote = voter.getVotes().stream()
                .filter(v -> v.getVoteOption().equals(option))
                .findFirst();

        if (existingVote.isPresent()) {
            Vote vote = existingVote.get();

            votes.values().remove(vote);

            voter.getVotes().remove(vote);

            return vote;
        }

        return null;
    }

    public Collection<Vote> getAllVotes() {
        return votes.values();
    }

    public long countVotesForOption(VoteOption option) {
        return votes.values().stream()
                .filter(v -> v.getVoteOption().equals(option))
                .count();
    }

    public void deletePoll(long pollId) {
        Poll poll = polls.remove(pollId);
        if (poll == null) {
            throw new IllegalArgumentException("Poll not found");
        }

        User creator = poll.getCreator();
        if (creator != null) {
            creator.getPolls().remove(poll);
        }

        // Remove any votes tied to this poll
        votes.entrySet().removeIf(entry ->
                entry.getValue().getVoteOption().getPoll().equals(poll)
        );
    }
}
