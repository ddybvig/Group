/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.blog.controller;

import com.sg.blog.dao.RoleDao;
import com.sg.blog.dao.UserDao;
import com.sg.blog.entities.Role;
import com.sg.blog.entities.User;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author MARIA
 */
@Controller
public class AdminController {
    @Autowired
    RoleDao roles;
    
    @Autowired
    UserDao users;
    
    @Autowired
    PasswordEncoder encoder;
    
    
    @GetMapping ("/admin")
    public String diplayAdminPage(){
        return "admin";    
    }
    
      @PostMapping("/addUser")
    public String addUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setEnabled(true);
        
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(roles.findByRole("ROLE_USER"));
        user.setRoles(userRoles);
        
        users.save(user);
        
        return "redirect:/admin"; 
    }
    
     @GetMapping("/deleteUser")
    public String deleteUser(Integer id) {
        users.deleteById(id);
        return "redirect:/admin";
    }
     
    @GetMapping("/editUser")
    public String editUserDisplay(Model model, Integer id, Integer error) {
        User user = users.findById(id).orElse(null);
        List<Role> roleList = roles.findAll();
        
        model.addAttribute("user", user);
        model.addAttribute("roles", roleList);
        
         if(error != null) {
            if(error == 1) {
                model.addAttribute("error", "Passwords did not match, password was not updated.");
            }
        }
        return "editUser";
    }
    
    @PostMapping(value="/editUser")
    public String editUserAction(String[] roleIdList, Boolean enabled, Integer id) {
        User user = users.findById(id).orElse(null);
        if(enabled != null) {
            user.setEnabled(enabled);
        } else {
            user.setEnabled(false);
        }
        
        Set<Role> roleList = new HashSet<>();
        for(String roleId : roleIdList) {
            Role role = roles.findById(Integer.parseInt(roleId)).orElse(null);
            roleList.add(role);
        }
        user.setRoles(roleList);
        users.save(user);
         
        return "redirect:/admin";
    }
     @PostMapping("editPassword") 
    public String editPassword(Integer id, String password, String confirmPassword) {
        User user = users.findById(id).orElse(null);
        
        if(password.equals(confirmPassword)) {
            user.setPassword(encoder.encode(password));
            users.save(user);
            return "redirect:/admin";
        } else {
            return "redirect:/editUser?id=" + id + "&error=1";
        }
     
    }
}
