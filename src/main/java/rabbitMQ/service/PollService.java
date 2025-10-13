package rabbitMQ.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rabbitMQ.handler.VoteHandler;
import rabbitMQ.models.Poll;
import rabbitMQ.repository.PollRepository;

import java.util.Map;

@Service
public class PollService {
    private final PollRepository repo;
    private final AmqpAdmin amqpAdmin;
    private final SimpleMessageListenerContainer listenerContainer;
    private final ObjectMapper mapper = new ObjectMapper();
    private final VoteHandler voteHandler;

    public PollService(PollRepository repo, AmqpAdmin amqpAdmin, SimpleMessageListenerContainer listenerContainer, VoteHandler voteHandler) {
        this.repo = repo;
        this.amqpAdmin = amqpAdmin;
        this.listenerContainer = listenerContainer;
        this.voteHandler = voteHandler;
    }

    @Transactional
    public Poll createPoll(String question, Map<String,Integer> options) {
        Poll p = new Poll(question, options);
        p = repo.save(p);

        // Create exchange and queue for this poll
        String exchangeName = exchangeName(p.getId());
        FanoutExchange ex = new FanoutExchange(exchangeName, true, false);
        amqpAdmin.declareExchange(ex);

        String queueName = queueName(p.getId());
        Queue q = new Queue(queueName, true, false, false);
        amqpAdmin.declareQueue(q);

        Binding binding = BindingBuilder.bind(q).to(ex);
        amqpAdmin.declareBinding(binding);

        // Register listener for this queue
        // We use a MessageListenerAdapter that forwards to voteHandler.handleVote
        MessageListenerAdapter adapter = new MessageListenerAdapter(voteHandler, "handleVote");
        adapter.setDefaultListenerMethod("handleVote");
        adapter.setMessageConverter(new org.springframework.amqp.support.converter.SimpleMessageConverter());

        // configure the listener container to listen to this queue
        listenerContainer.addQueueNames(queueName);
        listenerContainer.setupMessageListener(adapter);

        // Start container if not running
        if (!listenerContainer.isRunning()) {
             listenerContainer.start();
        }

        // For testing
        //listenerContainer.stop();  // <-- stop it here

        return p;
    }

    public Poll findById(Long id) { return repo.findById(id).orElseThrow(() -> new RuntimeException("not found")); }

    // naming helpers
    private static String exchangeName(Long pollId) { return "poll." + pollId; }
    private static String queueName(Long pollId) { return "poll." + pollId + ".queue.pollapp"; }
}
