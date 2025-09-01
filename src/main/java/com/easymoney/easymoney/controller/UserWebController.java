package com.easymoney.easymoney.controller;

import com.easymoney.easymoney.model.User;
import com.easymoney.easymoney.service.UserService;
import com.easymoney.easymoney.scylla.log.service.ScyllaLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/users")
public class UserWebController {

    @Autowired
    private UserService userService;

    @Autowired
    private ScyllaLogService scyllaLogService;

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        scyllaLogService.log("system", "List users", "INFO", "Se accedió al listado de usuarios",
                "UserWebController.list");
        return "users/index";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        scyllaLogService.log("system", "Create user form", "INFO", "Se accedió al formulario de creación de usuario",
                "UserWebController.createForm");
        return "users/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute User user) {
        userService.save(user);
        scyllaLogService.log(user.getName(), "Save user", "INFO", "Usuario guardado correctamente",
                "UserWebController.save");
        return "redirect:/web/users/";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        scyllaLogService.log("system", "Edit user form", "INFO",
                "Se accedió al formulario de edición para usuario ID: " + id, "UserWebController.editForm");
        return "users/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        userService.delete(id);
        scyllaLogService.log("system", "Delete user", "INFO", "Usuario eliminado con ID: " + id,
                "UserWebController.delete");
        return "redirect:/web/users/";
    }
}