package rabbitMQ.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rabbitMQ.models.Poll;

public interface PollRepository extends JpaRepository<Poll, Long> {}
