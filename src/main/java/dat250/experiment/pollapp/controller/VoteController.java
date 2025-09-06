package dat250.experiment.pollapp.controller;

import dat250.experiment.pollapp.model.Poll;
import dat250.experiment.pollapp.model.User;
import dat250.experiment.pollapp.model.Vote;
import dat250.experiment.pollapp.model.VoteOption;
import dat250.experiment.pollapp.service.PollManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public Vote castVote(@RequestParam long userId,
                         @RequestParam long pollId,
                         @RequestParam int optionIndex) {

        Optional<User> voter = pollManager.getUser(userId);
        Optional<Poll> poll = pollManager.getPoll(pollId);

        if (voter.isEmpty() || poll.isEmpty()) {
            throw new IllegalArgumentException("User or Poll not found");
        }

        List<VoteOption> options = poll.get().getVoteOptions();
        if (options.isEmpty()) {
            throw new IllegalArgumentException("Poll has no vote options");
        }
        if (optionIndex < 0 || optionIndex >= options.size()) {
            throw new IllegalArgumentException("Invalid option index");
        }

        VoteOption option = poll.get().getVoteOptions().get(optionIndex);
        return pollManager.castVote(voter.get(), option);
    }

    @GetMapping("/results/{pollId}")
    public Object getResults(@PathVariable long pollId) {
        Optional<Poll> poll = pollManager.getPoll(pollId);
        if (poll.isEmpty()) {
            throw new IllegalArgumentException("Poll not found");
        }

        // return structured JSON: option → count
        return poll.get().getVoteOptions().stream()
                .map(option -> new Object() {
                    public final String caption = option.getCaption();
                    public final long votes = pollManager.countVotesForOption(option);
                })
                .toList();
    }
}

