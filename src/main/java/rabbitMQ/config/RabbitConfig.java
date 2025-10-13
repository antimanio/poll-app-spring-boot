package rabbitMQ.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // Bean for declaring exchanges/queues programmatically
    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    // Message listener container bean (we can attach listeners dynamically)
    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory cf) {
        return new SimpleMessageListenerContainer(cf);
    }
}
