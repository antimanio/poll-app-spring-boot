# Redis & MongoDB

## Use case 1:
Operations on Set datatype:
- SADD: Add one or more members to a set	
- SREM:	Remove one or more members from a set	
- SMEMBERS: List all members of the set	
- SISMEMBER: Check if a specific member is in the set
- SCARD: Count how many members are in the set

1. Initial state: no user is logged in
   - `SMEMBERS logged_in_users` -> (empty array)
2. User "alice" logs in
   - `SADD logged_in_users alice` -> 1
3. User "bob" logs in
   - `SADD logged_in_users bob` -> 1
4. Check users
   - `SMEMBERS logged_in_users` -> 1) "alice", 2) "bob"
5. User "alice" logs off 
   - `SREM logged_in_users alice` -> 1
   - `SISMEMBER logged_in_users alice` -> 0
6. User "eve" logs in
   - `SADD logged_in_users eve` -> 1
7. Check users
   - `SMEMBERS logged_in_users` -> 1) "bob", 2) "eve"
8. Clear the set
   - `DEL logged_in_users` -> 1

## Use Case 2:

### Using Hash:
###### Create title: 
- `HSET poll:03ebcb7b-bd69-440b-924e-f5b7d664af7b title "Pineapple on Pizza?"`

###### Create hash per poll option:
- `HSET poll:03ebcb7b-bd69-440b-924e-f5b7d664af7b:option:yes caption "Yes, yammy!" votes 269`
- `HSET poll:03ebcb7b-bd69-440b-924e-f5b7d664af7b:option:no caption "Mamma mia, nooooo!" votes 268`
- `HSET poll:03ebcb7b-bd69-440b-924e-f5b7d664af7b:option:neutral caption "I do not really care ..." votes 42`

###### Get all fields for option yes: 
- `HGETALL poll:03ebcb7b-bd69-440b-924e-f5b7d664af7b:option:yes`

###### Increment vote count:
-`HINCRBY poll:03ebcb7b-bd69-440b-924e-f5b7d664af7b:option:yes votes 1`
- Redis Has support atomic increment with `HINCRBY` without rewriting the whole object. 

###### Check result: 
- `HGET poll:03ebcb7b-bd69-440b-924e-f5b7d664af7b:option:yes votes`

###### Flow:
![Hash-Redis.png](images/Hash-Redis.png)

### Using ReJSON
###### Store Json Values:
- `JSON.SET poll:03ebcb7b-bd69-440b-924e-f5b7d664af7b $ '{"id": "03ebcb7b-bd69-440b-924e-f5b7d664af7b", "title": "Pineapple on Pizza?", "options": [ { "caption": "Yes, yammy!", "voteCount": 269 }, { "caption": "Mamma mia, nooooo!", "voteCount": 268 }, { "caption": "I do not really care ...", "voteCount": 42 } ]}'`
###### Retrieve Json Values:
- `JSON.GET poll:03ebcb7b-bd69-440b-924e-f5b7d664af7b`
###### Increment vote count for the first option: 
- `JSON.NUMINCRBY poll:03ebcb7b-bd69-440b-924e-f5b7d664af7b $.options[0].voteCount 1`
###### Delete key: 
- `DEL poll:03ebcb7b-bd69-440b-924e-f5b7d664af7b`
###### Flow:
![ReJson-Redis.png](images/ReJson-Redis.png)


## Implementasjon using java

##### Set up MongoDB in Docker
- Pull latest image from dockerhub: `docker pull mongo:latest`. 
- Started a container: `docker run -d --name mongodb -p 27017:27017 mongo:latest`
- Check running container: `docker ps -a`

##### Interacte with MongoDB using mongosh (optional)
- open shell: `docker exec -it mongodb mongosh`
![mongodb-redis.png](images/mongodb-redis.png)

- See some data...
![db.polls.find.pretty.png](images/db.polls.find.pretty.png)


##### RedisApplication
- You can see the detailed implementation in `RedisApplication.java`.
- In this project, we implemented a polling application using MongoDB for persistence and Redis for cache.
- We used RedisJson to store the denormalized poll objects and JedisPooled as Redis client. 
- Cache Entries have a TTL of 60 seconds here. 
- We invalidate the cache whenever we have to go through the database, otherwise just updated the cache itself.  

##### Observartion
Database:
![fetchdata-from-mongodb.png](images/fetchdata-from-mongodb.png)

Cache:
![fetchdata-from-redis.png](images/fetchdata-from-redis.png)

- You can see that the initial fetch of the data took 152ms, whereas retrieving it from the cache only took 11ms. Huge improvement. 


##### Overall 
- No technical problems were encountered during MongoDB installation or use, and all tasks were completed successfully without issues.