package dat250.experiment.pollapp.controller;


import dat250.experiment.pollapp.model.Poll;
import dat250.experiment.pollapp.model.User;
import dat250.experiment.pollapp.model.VoteOption;
import dat250.experiment.pollapp.service.PollManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Collection;
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
    public Poll createPoll(@RequestParam long creatorId,
                           @RequestParam String question,
                           @RequestParam long validUntilEpoch) {
        Optional<User> creator = pollManager.getUser(creatorId);
        if (creator.isEmpty()) {
            throw new IllegalArgumentException("Creator not found");
        }
        return pollManager.createPoll(creator.get(), question, Instant.ofEpochSecond(validUntilEpoch));
    }

    @GetMapping
    public Collection<Poll> getPolls() {
        return pollManager.getAllPolls();
    }

    @PostMapping("/{pollId}/options")
    public VoteOption addOption(@PathVariable long pollId,
                                @RequestParam String caption,
                                @RequestParam int order) {
        Optional<Poll> poll = pollManager.getPoll(pollId);
        if (poll.isEmpty()) {
            throw new IllegalArgumentException("Poll not found");
        }
        return pollManager.addVoteOption(poll.get(), caption, order);
    }
}
