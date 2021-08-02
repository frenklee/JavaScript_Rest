package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import web.dao.RoleDAO;
import web.model.Role;
import web.model.User;
import web.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RestAdminController {

    private final RoleDAO roleDAO;
    private final UserService userService;

    @Autowired
    public RestAdminController(RoleDAO roleDAO, UserService userService) {
        this.roleDAO = roleDAO;
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<User>> getUsers(){
        List<User> users = this.userService.listUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles(){
        List<Role> roles = this.roleDAO.listRoles();
        if (roles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable(name = "id") int id){
        if (id == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = this.userService.getUser(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable(name = "id") int id){
        User user = this.userService.getUser(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody @Valid User user){
        HttpHeaders headers = new HttpHeaders();
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        this.userService.addUser(user);
        return new ResponseEntity<>(user, headers, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> update(@RequestBody @Valid User user, @PathVariable(name = "id") int id){
        HttpHeaders headers = new HttpHeaders();
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        this.userService.updateUser(id,user);
        return new ResponseEntity<>(user, headers, HttpStatus.OK);
    }
}
