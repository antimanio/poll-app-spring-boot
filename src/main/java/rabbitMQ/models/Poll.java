package rabbitMQ.models;

import jakarta.persistence.*;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
public class Poll {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String optionsJson; // stores JSON map: option -> count

    public Poll() {}

    public Poll(String question, Map<String,Integer> options) {
        this.question = question;
        setOptions(options);
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Map<String,Integer> getOptions() {
        try {
            return MAPPER.readValue(optionsJson, new TypeReference<Map<String,Integer>>() {});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setOptions(Map<String,Integer> opts) {
        try {
            this.optionsJson = MAPPER.writeValueAsString(opts);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void incrementOption(String option) {
        Map<String,Integer> m = getOptions();
        m.put(option, m.getOrDefault(option, 0) + 1);
        setOptions(m);
    }

    // getters & setters for id, question, optionsJson
    public Long getId() { return id; }
    public String getQuestion() { return question; }
    public void setQuestion(String q) { this.question = q; }
    public String getOptionsJson() { return optionsJson; }
    public void setOptionsJson(String json) { this.optionsJson = json; }
}
