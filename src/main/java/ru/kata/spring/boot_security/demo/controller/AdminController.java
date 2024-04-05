package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;

    //    @Autowired
    public AdminController(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()   // отображение списка всех пользователей
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @GetMapping("/show")  // получаем пользователя по указанному идентификатору и отображаем информацию о нем.
    public String getUserById(@RequestParam("id") Long id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        return "user";
    }

    @GetMapping("/new")   //  ?отображаем форму для создания нового пользовател
    public String createUserFrom(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getListOfRoles());
        return "newUserCreate";
    }

    @PostMapping("/addUser")  //  добавляем нового пользователя.
    public String addNewUser(@ModelAttribute("user") @Valid User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getListOfRoles());
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit")  // форма для редактирования пользователя
    public String editUserById(@RequestParam(value = "id") Long id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        model.addAttribute("roles", roleService.getListOfRoles());
        return "editUser";
    }

    @PostMapping("/save")     // сохраняем данные пользователя
    public String saveUser(@ModelAttribute("user") @Valid User user) {
        userService.editUserById(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.removeUserById(id);
        return "redirect:/admin";
    }
}

