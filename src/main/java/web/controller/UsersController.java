package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web.model.User;
import web.service.UserService;

@Controller
@RequestMapping(value="/user")
public class UsersController {

    private final UserService userService;

    @Autowired
    public UsersController(UserService userService){
        this.userService = userService;
    }

    @GetMapping()
    public String showUser(Model model, Authentication authentication,
                           @AuthenticationPrincipal UserDetails userDetails) {
        User logged = userService.getUserByLogin(userDetails.getUsername());
        model.addAttribute("user", userService.getUserByLogin(authentication.getName()));
        model.addAttribute("logged", logged);
        return "user";
    }
}
