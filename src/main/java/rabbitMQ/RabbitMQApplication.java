package rabbitMQ;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication(
        exclude = {
                MongoAutoConfiguration.class,
                MongoDataAutoConfiguration.class
        }
)
public class RabbitMQApplication {
    public static void main(String[] args) {
        Map<String, Object> props = new HashMap<>();
        props.put("spring.application.name", "pollapp");
        props.put("spring.rabbitmq.host", "localhost");
        props.put("spring.rabbitmq.port", 5672);
        props.put("spring.rabbitmq.username", "guest");
        props.put("spring.rabbitmq.password", "guest");
        props.put("spring.datasource.url", "jdbc:h2:mem:testdb");
        props.put("spring.datasource.driverClassName", "org.h2.Driver");
        props.put("spring.jpa.hibernate.ddl-auto", "update");
        props.put("spring.h2.console.enabled", true);

        new SpringApplicationBuilder(RabbitMQApplication.class)
                .properties(props)
                .run(args);
    }
}
