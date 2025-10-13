package rabbitMQ.dto;

import java.util.Map;

public class CreatePollRequest {
    public String question;
    public Map<String, Integer> options;
}
