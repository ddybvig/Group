/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.blog.controller;

import com.sg.blog.dao.BlogPostDao;
import com.sg.blog.dao.TagDao;
import com.sg.blog.entities.BlogPost;
import com.sg.blog.entities.Tag;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    @GetMapping("/content")
    public String displayContentPage() {
        return "content";
    }

    @PostMapping("addContent")
    public String addContent(HttpServletRequest request) {
        String content = request.getParameter("mytextarea");
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
        blogPost = blogDao.save(blogPost);
        return "redirect:/home";
    }
}
