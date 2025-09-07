package dat250.experiment.pollapp.controller;

import dat250.experiment.pollapp.model.Poll;
import dat250.experiment.pollapp.model.User;
import dat250.experiment.pollapp.model.Vote;
import dat250.experiment.pollapp.model.VoteOption;
import dat250.experiment.pollapp.service.PollManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/votes")
public class VoteController {

    @Autowired
    private PollManager pollManager;

    //public VoteController(PollManager pollManager) {
    //    this.pollManager = pollManager;
    //}

    @PostMapping
    public ResponseEntity<?> castVote(@RequestParam long userId,
                                      @RequestParam long pollId,
                                      @RequestParam int optionIndex) {

        Optional<User> voter = pollManager.getUser(userId);
        Optional<Poll> poll = pollManager.getPoll(pollId);

        if (voter.isEmpty() || poll.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User or Poll not found"));
        }

        List<VoteOption> options = poll.get().getVoteOptions();
        if (options.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Poll has no vote options"));
        }
        if (optionIndex < 0 || optionIndex >= options.size()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Invalid option index"));
        }

        Vote vote = pollManager.castVote(voter.get(), options.get(optionIndex));
        return ResponseEntity.ok(vote);
    }

    @GetMapping("/results/{pollId}")
    public ResponseEntity<?> getResults(@PathVariable long pollId) {
        Optional<Poll> poll = pollManager.getPoll(pollId);
        if (poll.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Poll not found"));
        }

        var results = poll.get().getVoteOptions().stream()
                .map(option -> Map.of(
                        "caption", option.getCaption(),
                        "votes", pollManager.countVotesForOption(option)
                ))
                .toList();

        return ResponseEntity.ok(results);
    }
}

