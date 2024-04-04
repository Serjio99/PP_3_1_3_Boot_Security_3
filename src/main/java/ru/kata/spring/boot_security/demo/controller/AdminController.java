package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.validation.Valid;

@Controller
public class AdminController {


    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;

    @Autowired
    public AdminController(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")   // получаем всех пользователей и отображаем страницу со списком пользователей
    public String getAllUsersPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @GetMapping("/admin/show")  // получаем пользователя по указанному идентификатору и отображаем информацию о нем.
    public String getUserById(@RequestParam("id") Long id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        return "user";
    }

    @GetMapping("/admin/new")   //  отображаем форму для создания нового пользовател
    public String createUserFrom(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getListOfRoles());
        return "newUserCreate";
    }

    @GetMapping("/admin/edit")  // форма для редактирования пользователя
    public String editUserById(@RequestParam(value = "id") Long id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        model.addAttribute("roles", roleService.getListOfRoles());
        return "editUser";
    }

    @PostMapping("/admin/addUser")
    public String processAddUserForm(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return showNewUserForm(user, model);
        }
        saveUserAndRedirect(user);
        return "redirect:/admin";
    }

    @PostMapping("/admin/save")
    public String processEditUserForm(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return showEditUserForm(user, model);
        }
        updateUserAndRedirect(user);
        return "redirect:/admin";
    }

    private String showNewUserForm(User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getListOfRoles());
        return "newUserCreate";
    }

    private void saveUserAndRedirect(User user) {
        user.setPassword(user.getPassword());
        userService.addUser(user);
    }

    private String showEditUserForm(User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getListOfRoles());
        return "editUser";
    }

    private void updateUserAndRedirect(User user) {
        user.setPassword(user.getPassword());
        userService.editUserById(user);
    }

    @GetMapping(value = "/admin/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.removeUserById(id);
        return "redirect:/admin";
    }
}
