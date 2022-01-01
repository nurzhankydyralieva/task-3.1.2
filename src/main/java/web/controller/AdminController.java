package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import web.entity.Role;
import web.entity.User;
import web.service.RoleService;
import web.service.UserService;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;
    private RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String getAllUsers(ModelMap model, Principal principal) {
        model.addAttribute("user", userService.getUserByName(principal.getName()));
        model.addAttribute("users", userService.getAllUser());
        model.addAttribute("newUser", new User());
        model.addAttribute("roles", roleService.getAllRole());
        return "admin";
    }

    @PostMapping
    public String saveNewUser(@ModelAttribute("user") User user, @RequestParam List<String> roleString) {
        Set<Role> userRoles = new HashSet<>();
        for (String role : roleString) {
            userRoles.add(roleService.getRole(role));
        }
        user.setRoles(userRoles);
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/{id}/edit")
    public String editUser(@ModelAttribute("user") User user, @RequestParam List<String> roleString) {
        Set<Role> userRoles = new HashSet<>();
        for (String role : roleString) {
            userRoles.add(roleService.getRole(role));
        }
        user.setRoles(userRoles);
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/{id}")
    private String delete(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }
}
