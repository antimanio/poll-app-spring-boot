package dat250.experiment.pollapp.controller;

import dat250.experiment.pollapp.model.User;
import dat250.experiment.pollapp.service.PollManager;
import org.springframework.beans.factory.annotation.Autowired;
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
    public User createUser(@RequestBody User user) {
        return pollManager.createUser(user.getUsername(), user.getEmail());
    }

    @GetMapping
    public Collection<User> getUsers() {
        return pollManager.getAllUsers();
    }
}

