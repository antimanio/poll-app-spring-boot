package rabbitMQ.dto;

public class VoteMessage {
    public Long pollId;
    public String option;

    public VoteMessage(Long pollId, String option) {
        this.pollId = pollId;
        this.option = option;
    }
}
