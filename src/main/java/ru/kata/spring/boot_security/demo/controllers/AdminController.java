package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String readAllUsers(Model model) {
        model.addAttribute("users", userService.readAllUsers());
        return "admin_page";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.findAll());
        return "create";
    }

    @PostMapping("/createauser")
    public String create(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult,
                         @RequestParam("role") String selectedRole) {
        if (bindingResult.hasErrors()) {
            return "create";
        }
        userService.createUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/update")
    public String updateForm(Model model,
                             @RequestParam("id") Integer id) {
        model.addAttribute(userService.readUserById(id));
        return "update";
    }

    @PostMapping("/updateauser")
    public String update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult,
                         @RequestParam("role") String selectedRole,
                         @RequestParam("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "update";
        }
        userService.updateUser(id, user);
        return "redirect:/admin";
    }

    @GetMapping("/delete")
    public String deleteForm(Model model,
                             @RequestParam("id") Integer id) {
        model.addAttribute(userService.readUserById(id));
        return "delete";
    }

    @PostMapping("/deleteauser")
    public String delete(@RequestParam("id") Integer id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

}