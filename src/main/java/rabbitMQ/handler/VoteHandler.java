package rabbitMQ.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rabbitMQ.models.Poll;
import rabbitMQ.repository.PollRepository;

@Component
public class VoteHandler {
    private final PollRepository repo;
    private final ObjectMapper mapper = new ObjectMapper();

    public VoteHandler(PollRepository repo) {
        this.repo = repo;
    }

    // This method will be invoked by the listener adapter.
    @Transactional
    public void handleVote(String body) throws Exception {
        // Parse JSON
        JsonNode node = mapper.readTree(body);
        Long pollId = node.get("pollId").asLong();
        String option = node.get("option").asText();

        // Find poll and increment vote
        Poll poll = repo.findById(pollId)
                .orElseThrow(() -> new RuntimeException("poll not found: " + pollId));
        poll.incrementOption(option);
        repo.save(poll);

    }
}

