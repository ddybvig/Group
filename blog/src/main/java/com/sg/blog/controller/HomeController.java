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
import com.sg.blog.entities.Tag;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
public class HomeController {
    
    @Autowired
    BlogPostDao blogDao;
    
    @Autowired
    TagDao tagDao;
    
    @Autowired
    StaticPageDao staticDao;
    
    String searchInput;
    
    @GetMapping({"/", "/home"})
    public String displayHomePage(Model model) {
        List<BlogPost> posts = blogDao.findAll();
        model.addAttribute("posts", posts);
        Map<Integer, List<String>> tagMap = new HashMap<>();
        for (BlogPost post : posts) {
            List<Tag> tags = post.getTags();
            tagMap.put(post.getId(), post.getTags().stream()
                    .map(t -> t.getName())
                    .collect(Collectors.toList()));
        }
        model.addAttribute("tagMap", tagMap);
        return "home";
    }
    
    @PostMapping("searchByTag")
    public String searchByTag(HttpServletRequest request) {
        searchInput = request.getParameter("searchInput");
        
        return "searchResults";
    }
    
    @GetMapping("searchResults")
    public String getSearchResults(Model model) {
        List<Tag> tags = tagDao.findAll();
        Tag tagFromSearch = null;
        for (Tag someTag : tags) {
            if (someTag.getName().equalsIgnoreCase(searchInput)) {
                tagFromSearch = someTag;
            } else {
                return "redirect:/"; //or maybe make an error and display it on the page?
            }
        }
        Integer tagId = tagFromSearch.getId();
        List<BlogPost> posts = new ArrayList<>();
        for (BlogPost post : blogDao.findAll()) {
            for (Tag tag : post.getTags()) {
                if (tag.getId()==tagId) {
                    posts.add(post);
                }
            }
        }
        model.addAttribute("posts", posts);
        return "searchResults";
    }
    
}