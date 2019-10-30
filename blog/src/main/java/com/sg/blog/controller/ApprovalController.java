/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.blog.controller;

import com.sg.blog.dao.BlogPostDao;
import com.sg.blog.dao.StaticPageDao;
import com.sg.blog.entities.BlogPost;
import com.sg.blog.entities.Tag;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author MARIA
 */
@Controller
public class ApprovalController {

    @Autowired
    BlogPostDao blogDao;

    @Autowired
    StaticPageDao staticDao;

    @GetMapping("approval")
    public String displayApprovalPage(Model model) {
        List<BlogPost> posts = blogDao.findByApprovedFalse();
        Map<Integer, List<String>> tagMap = new HashMap<>();
        for (BlogPost post : posts) {
            List<Tag> tags = post.getTags();
            tagMap.put(post.getId(), post.getTags().stream()
                    .map(t -> t.getName())
                    .collect(Collectors.toList()));
        }
        model.addAttribute("posts", posts);
        model.addAttribute("tagMap", tagMap);
        model.addAttribute("staticpages", staticDao.findAll());
        return "approval";
    }

    @GetMapping("approveContent")
    public String approveContent(Integer id) {
        BlogPost post = blogDao.findById(id).orElse(null);
        post.setApproved(true);
        blogDao.save(post);
        return "approval";
    }
}
