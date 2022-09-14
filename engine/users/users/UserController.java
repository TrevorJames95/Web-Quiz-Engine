package engine.users.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/api/register")
    public User registerUsers(@Valid @RequestBody User user) {
        return userService.registerUsers(user);
    }

    @GetMapping("/test")
    public String authChecker() {
        return "Non authenticated user works.";
    }
}
