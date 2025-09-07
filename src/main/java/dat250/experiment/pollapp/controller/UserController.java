package dat250.experiment.pollapp.controller;

import dat250.experiment.pollapp.model.User;
import dat250.experiment.pollapp.service.PollManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private PollManager pollManager;

    //public UserController(PollManager pollManager) {
    //    this.pollManager = pollManager;
    //}

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User createdUser = pollManager.createUser(user.getUsername(), user.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            // In case your service throws an exception for invalid data
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<Collection<User>> getUsers() {
        Collection<User> users = pollManager.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content if no users
        }
        return ResponseEntity.ok(users); // 200 OK with users
    }
}

