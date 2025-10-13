package rabbitMQ.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;
import rabbitMQ.dto.CreatePollRequest;
import rabbitMQ.dto.VoteMessage;
import rabbitMQ.dto.VoteRequest;
import rabbitMQ.service.PollService;
import rabbitMQ.models.Poll;

import java.util.Map;

@RestController
@RequestMapping("/api/polls")
public class PollController {
    private final PollService pollService;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public PollController(PollService pollService, RabbitTemplate rabbitTemplate) {
        this.pollService = pollService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping
    public Poll createPoll(@RequestBody CreatePollRequest req) {
        return pollService.createPoll(req.question, req.options);
    }

    @PostMapping("/{id}/vote")
    public String vote(@PathVariable("id") Long id, @RequestBody VoteRequest voteReq) throws Exception {
        // publish to exchange poll.<id> with a JSON body
        String exchange = "poll." + id;
        VoteMessage msg = new VoteMessage(id, voteReq.option);
        rabbitTemplate.convertAndSend(exchange, "", mapper.writeValueAsString(msg)); // fanout: routingKey ignored
        return "published";
    }

    @GetMapping("/{id}")
    public Poll get(@PathVariable Long id) { return pollService.findById(id); }

}
