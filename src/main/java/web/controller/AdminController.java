package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.dao.RoleDAO;
import web.model.Role;
import web.model.User;
import web.service.UserService;

import java.util.ArrayList;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleDAO roleDAO;

    @Autowired
    public AdminController(UserService userService, RoleDAO roleDAO){
        this.userService = userService;
        this.roleDAO = roleDAO;
    }

    @GetMapping()
    public String getUsers(Model model, @AuthenticationPrincipal UserDetails userDetails){
        ArrayList<Role> roles = (ArrayList<Role>) roleDAO.listRoles();
        User logged = userService.getUserByLogin(userDetails.getUsername());
        model.addAttribute("list",userService.listUsers());
        model.addAttribute("user",new User());
        model.addAttribute("logged", logged);
        System.out.println(userDetails.getUsername());
        model.addAttribute("allRoles", roles);
        return "admin";
    }

    @GetMapping("/{id}")
    public String getUser(@PathVariable(name = "id") int id, Model model){
        model.addAttribute("user",userService.getUser(id));
        return "user";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable(name = "id") int id){
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @PostMapping()
    public String createUser(@ModelAttribute("user") User user){
        userService.addUser(user);
        return "redirect:/admin";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User user, @PathVariable("id") int id){
        userService.updateUser(id, user);
        return "redirect:/admin";
    }
}
