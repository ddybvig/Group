/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.blog.controller;

import com.sg.blog.dao.BlogPostDao;
import com.sg.blog.dao.StaticPageDao;
import com.sg.blog.dao.TagDao;
import com.sg.blog.entities.BlogPost;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author MARIA
 */
@Controller 
public class HomeController {
    
    @Autowired
    BlogPostDao blogDao;
    
    @Autowired
    TagDao tagDao;
    
    @Autowired
    StaticPageDao staticDao;
    
    

    @GetMapping({"/", "/home"})
    public String displayHomePage(Model model) {
        List<BlogPost> posts = blogDao.findAll();
        model.addAttribute("posts", posts);
        return "home";
    }
}