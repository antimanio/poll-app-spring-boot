package dat250.experiment.pollapp;
import com.mongodb.client.model.Filters;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.json.Path2;


@SpringBootApplication
@RestController
@RequestMapping("/polls")
public class RedisApplication {

    private final JedisPooled jedis;
    private final MongoCollection<Document> collection;

    public RedisApplication() {
        // Redis
        this.jedis = new JedisPooled("localhost", 6379);

        // MongoDB
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("polls_db");
        this.collection = database.getCollection("polls");
    }

    @GetMapping("/{pollId}")
    public String getPoll(@PathVariable String pollId) {
        String redisKey = "poll:" + pollId;

        // Check cache
        Object cached = jedis.jsonGet(redisKey);
        if (cached != null) {
            return cached.toString();
        }

        // Fetch from MongoDB
        Document pollDoc = collection.find(Filters.eq("_id", pollId)).first();
        if (pollDoc != null) {
            jedis.jsonSet(redisKey, Path2.of("$"), pollDoc.toJson()); // store JSON in Redis
            jedis.expire(redisKey, 60);
            return pollDoc.toJson();
        }

        return "{\"error\":\"Poll not found\"}";
    }


    @PostMapping("/{pollId}/vote")
    public String vote(@PathVariable String pollId, @RequestParam String option) {

        String redisKey = "poll:" + pollId;

        // Increment vote in RedisJSON if cached
        if (jedis.exists(redisKey)) {
            try {
                jedis.jsonNumIncrBy(redisKey, Path2.of(".options[?(@.caption=='" + option + "')] .voteCount"), 1);
                return "{\"status\":\"Vote counted (Redis cache updated)\"}";
            } catch (Exception e) {
                System.err.println("Redis cache increment failed: " + e.getMessage());
            }
        }

        // Fallback: update MongoDB
        collection.updateOne(
                Filters.and(Filters.eq("_id", pollId), Filters.eq("options.caption", option)),
                new Document("$inc", new Document("options.$.voteCount", 1))
        );

        // Invalidate cache
        jedis.del(redisKey);

        return "{\"status\":\"Vote counted (cache invalidated, fallback MongoDB)\"}";
    }

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }

}
