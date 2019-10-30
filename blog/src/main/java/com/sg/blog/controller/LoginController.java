/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.blog.controller;

import com.sg.blog.dao.StaticPageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author MARIA
 */
@Controller
public class LoginController {

    @Autowired
    StaticPageDao staticDao;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("staticpages", staticDao.findAll());
        return "login";
    }
}
