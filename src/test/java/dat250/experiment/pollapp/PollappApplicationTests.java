package dat250.experiment.pollapp;

import dat250.experiment.pollapp.model.Poll;
import dat250.experiment.pollapp.model.User;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import dat250.experiment.pollapp.model.Vote;
import dat250.experiment.pollapp.model.VoteOption;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class PollappApplicationTests {

    private final String BASE_URL = "http://localhost:8080/";

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    public void testScenario() {

        //Create user 1 and 2
        User user1 = new User("anton", "anton@hvl.com");
        ResponseEntity<User> user1Response = restTemplate.postForEntity(
                BASE_URL + "api/users",
                user1,
                User.class
        );

        User user2 = new User("martin", "martin@hvl.com");
        ResponseEntity<User> user2Response = restTemplate.postForEntity(
                BASE_URL + "api/users",
                user2,
                User.class
        );

        assertEquals(HttpStatus.CREATED, user1Response.getStatusCode(), "Expected HTTP 201 CREATED");
        assertEquals(HttpStatus.CREATED, user2Response.getStatusCode(), "Expected HTTP 201 CREATED");


        // List all users
        ResponseEntity<User[]> usersAllRespone = restTemplate.getForEntity(
                BASE_URL + "api/users",
                User[].class
        );

        assertNotNull(usersAllRespone.getBody(), "Users list should not be null");
        assertEquals(2, usersAllRespone.getBody().length, "Expected 2 users in the list");

        // Create Poll user 1
        String createPollUrl = BASE_URL + "api/polls?creatorId=1&question=Favorite%20color?&validUntilEpoch=1924992000";
        ResponseEntity<Poll> poll1Response = restTemplate.postForEntity(createPollUrl, null, Poll.class);
        assertEquals(HttpStatus.CREATED, poll1Response.getStatusCode(), "Expected HTTP 201 CREATED");


        //Add options for Poll created by user 1
        ResponseEntity<VoteOption> voteOption1 = restTemplate.postForEntity(BASE_URL + "api/polls/1/options?caption=Red&order=0", null, VoteOption.class);
        ResponseEntity<VoteOption> voteOption2 = restTemplate.postForEntity(BASE_URL + "api/polls/1/options?caption=Blue&order=1", null, VoteOption.class);
        assertEquals(HttpStatus.CREATED, voteOption1.getStatusCode(), "Expected HTTP 201 CREATED");
        assertEquals(HttpStatus.CREATED, voteOption2.getStatusCode(), "Expected HTTP 201 CREATED");

        // List all polls
        ResponseEntity<Poll[]> pollsResponse = restTemplate.getForEntity(BASE_URL + "api/polls", Poll[].class);
        assertNotNull(pollsResponse.getBody(), "Poll list should not be null");
        assertEquals(1, pollsResponse.getBody().length, "Expected 1 users in the list");

        // User 2 votes on the poll
        ResponseEntity<Vote> vote1Response = restTemplate.postForEntity(BASE_URL + "api/votes?userId=2&pollId=1&optionIndex=0", null, Vote.class);
        assertEquals(HttpStatus.OK, vote1Response.getStatusCode(), "Expected HTTP 200 OK");

        // User 2 changes his vote
        ResponseEntity<Vote> vote2Response = restTemplate.postForEntity(BASE_URL + "api/votes?userId=2&pollId=1&optionIndex=1", null, Vote.class);
        assertEquals(HttpStatus.OK, vote2Response.getStatusCode(), "Expected HTTP 200 OK");

        // List Votes
        ResponseEntity<Vote[]> voteResultsForPoll1 =
                restTemplate.getForEntity(BASE_URL + "api/votes/results/1", Vote[].class);
        Vote[] votes = voteResultsForPoll1.getBody();
        assertNotNull(votes, "The votes should not be null");


        // Delete the poll
        restTemplate.delete(BASE_URL + "/api/polls/1");

        //  List all polls
        ResponseEntity<Poll[]> pollsAfterDeletion = restTemplate.getForEntity(BASE_URL + "api/polls", Poll[].class);
        assertEquals(HttpStatus.NO_CONTENT, pollsAfterDeletion.getStatusCode(), "Expected HTTP 204");


        // List votes (should be empty)
        ResponseEntity<Vote[]> voteResultsAfterDeletion = restTemplate.getForEntity(BASE_URL + "api/votes/results/1", Vote[].class);
        assertEquals(HttpStatus.OK, voteResultsAfterDeletion.getStatusCode(), "Expected HTTP 200 OK");
        assertNotNull(voteResultsAfterDeletion.getBody(), "Response body should not be null");
        assertEquals(0, voteResultsAfterDeletion.getBody().length, "Vote list should be empty");
    }

}
