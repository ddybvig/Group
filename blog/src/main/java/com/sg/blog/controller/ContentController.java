/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.blog.controller;

import com.sg.blog.dao.BlogPostDao;
import com.sg.blog.dao.TagDao;
import com.sg.blog.dao.UserDao;
import com.sg.blog.entities.BlogPost;
import com.sg.blog.entities.Tag;
import com.sg.blog.entities.User;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author MARIA
 */
@Controller
public class ContentController {

    @Autowired
    TagDao tagDao;

    @Autowired
    BlogPostDao blogDao;
    
    @Autowired
    UserDao userDao;

    @GetMapping("/content")
    public String displayContentPage() {
        return "content";
    }
    
    @GetMapping("addContent")
    public String addContent(Model model) {
        model.addAttribute("blogpost", new BlogPost());
        return "addContent";
    }

    @PostMapping("addContent")
    public String performAddContent(HttpServletRequest request, Principal principal) {
        String content = request.getParameter("blogtextarea");
        BlogPost blogPost = new BlogPost();
        blogPost.setBody(content);
        String title = request.getParameter("title");
        blogPost.setTitle(title);
        String rawTags = request.getParameter("tags");
        String[] tagsArray = rawTags.split(" ");
        List<Tag> tags = new ArrayList<>();
        for (String tagName : tagsArray) {
            Tag tag = tagDao.findByName(tagName);
            if (tag == null) {
                tag = new Tag();
                tag.setName(tagName);
                tag = tagDao.save(tag);
            }
            tags.add(tag);
        }
        blogPost.setTags(tags);
        blogPost.setDate(LocalDateTime.now().withNano(0));
        if (request.getParameter("expDate") != null) {
            blogPost.setExpirationDate(LocalDate.parse(request.getParameter("expDate"), DateTimeFormatter.ISO_DATE));
        }
        blogPost.setApproved(request.isUserInRole("ROLE_ADMIN"));
        User user = userDao.findByUsername(principal.getName());
        blogPost.setUser(user);
        blogPost = blogDao.save(blogPost);
        return "redirect:/home";
    }
    
    @GetMapping("editContent")
    public String editContent(Integer id, Model model) {
        BlogPost blogPost = blogDao.findById(id).orElse(null);
        model.addAttribute("blogpost", blogPost);
        List<String> tags = new ArrayList<>();
        for(Tag tag: blogPost.getTags()){
            tags.add(tag.getName());
        }
        model.addAttribute("tags", tags);
        return "editContent";
    }

    @PostMapping("editContent")
    public String performEditContent(Integer id, HttpServletRequest request) {
        String content = request.getParameter("blogtextarea");
        BlogPost blogPost = blogDao.findById(id).orElse(null);
        blogPost.setBody(content);
        String title = request.getParameter("title");
        blogPost.setTitle(title);
        String rawTags = request.getParameter("tags");
        String[] tagsArray = rawTags.split(" ");
        List<Tag> tags = new ArrayList<>();
        for (String tagName : tagsArray) {
            Tag tag = tagDao.findByName(tagName);
            if (tag == null) {
                tag = new Tag();
                tag.setName(tagName);
                tag = tagDao.save(tag);
            }
            tags.add(tag);
        }
        blogPost.setTags(tags);
        String updateDate = request.getParameter("updateDate");
        if(Boolean.parseBoolean(updateDate)){
            blogPost.setDate(LocalDateTime.now().withNano(0));
        }
        if (request.getParameter("expDate") != null) {
            blogPost.setExpirationDate(LocalDate.parse(request.getParameter("expDate"), DateTimeFormatter.ISO_DATE));
        }
        blogPost.setApproved(request.isUserInRole("ROLE_ADMIN"));
        blogPost = blogDao.save(blogPost);
        return "redirect:/home";
    }
}
