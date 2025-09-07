package dat250.experiment.pollapp.controller;


import dat250.experiment.pollapp.model.Poll;
import dat250.experiment.pollapp.model.User;
import dat250.experiment.pollapp.model.VoteOption;
import dat250.experiment.pollapp.service.PollManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/polls")
public class PollController {

    @Autowired
    private PollManager pollManager;

    //public PollController(PollManager pollManager) {
    //    this.pollManager = pollManager;
    //}

    @PostMapping
    public ResponseEntity<?> createPoll(@RequestParam long creatorId,
                                        @RequestParam String question,
                                        @RequestParam long validUntilEpoch) {
        Optional<User> creator = pollManager.getUser(creatorId);
        if (creator.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Creator not found"));
        }

        Poll poll = pollManager.createPoll(creator.get(), question, Instant.ofEpochSecond(validUntilEpoch));
        return ResponseEntity.status(HttpStatus.CREATED).body(poll);
    }

    @GetMapping
    public ResponseEntity<Collection<Poll>> getPolls() {
        Collection<Poll> polls = pollManager.getAllPolls();
        if (polls.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(polls);
    }

    @PostMapping("/{pollId}/options")
    public ResponseEntity<?> addOption(@PathVariable long pollId,
                                       @RequestParam String caption,
                                       @RequestParam int order) {
        Optional<Poll> poll = pollManager.getPoll(pollId);
        if (poll.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Poll not found"));
        }

        VoteOption option = pollManager.addVoteOption(poll.get(), caption, order);
        return ResponseEntity.status(HttpStatus.CREATED).body(option);
    }

    @DeleteMapping("/{pollId}")
    public ResponseEntity<?> deletePoll(@PathVariable long pollId) {
        Optional<Poll> poll = pollManager.getPoll(pollId);
        if (poll.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Poll not found"));
        }

        pollManager.deletePoll(pollId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
