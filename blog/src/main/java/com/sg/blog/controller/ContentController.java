/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.blog.controller;

import com.sg.blog.dao.BlogPostDao;
import com.sg.blog.dao.StaticPageDao;
import com.sg.blog.dao.TagDao;
import com.sg.blog.dao.UserDao;
import com.sg.blog.entities.BlogPost;
import com.sg.blog.entities.StaticPage;
import com.sg.blog.entities.Tag;
import com.sg.blog.entities.User;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
public class ContentController {

    @Autowired
    TagDao tagDao;

    @Autowired
    BlogPostDao blogDao;

    @Autowired
    UserDao userDao;

    @Autowired
    StaticPageDao staticDao;

    @GetMapping("/content")
    public String displayContentPage() {
        return "content";
    }

    @GetMapping("addContent")
    public String addContent(Model model) {
        model.addAttribute("blogpost", new BlogPost());
        model.addAttribute("staticpages", staticDao.findAll());
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
        if (request.getParameter("expDate") == "") {
            blogPost.setExpirationDate(LocalDate.parse("2999-12-31", DateTimeFormatter.ISO_DATE));
        } else {
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
        for (Tag tag : blogPost.getTags()) {
            tags.add(tag.getName());
        }
        model.addAttribute("tags", tags);
        model.addAttribute("staticpages", staticDao.findAll());
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
        if (Boolean.parseBoolean(updateDate)) {
            blogPost.setDate(LocalDateTime.now().withNano(0));
        }
        if (request.getParameter("expDate") != null) {
            blogPost.setExpirationDate(LocalDate.parse(request.getParameter("expDate"), DateTimeFormatter.ISO_DATE));
        }
        blogPost.setApproved(request.isUserInRole("ROLE_ADMIN"));
        blogPost = blogDao.save(blogPost);
        return "redirect:/home";
    }

    @GetMapping("deleteContent")
    public String deleteContent(Integer id) {
        blogDao.deleteById(id);
        return "redirect:/home";
    }

    @GetMapping("addStaticPage")
    public String addStaticPage(Model model) {
        model.addAttribute("staticpage", new StaticPage());
        model.addAttribute("staticpages", staticDao.findAll());
        return "addStaticPage";
    }

    @PostMapping("addStaticPage")
    public String performAddStaticPage(StaticPage staticPage) {
        staticPage = staticDao.save(staticPage);
        return "redirect:/home";
    }

    @GetMapping("viewPage")
    public String viewPage(Integer id, Model model) {
        StaticPage staticPage = staticDao.findById(id).orElse(null);
        model.addAttribute("staticpage", staticPage);
        model.addAttribute("staticpages", staticDao.findAll());
        return "viewPage";
    }

    @GetMapping("viewBlogPost")
    public String viewBlogPost(Integer id, Model model) {
        BlogPost blogPost = blogDao.findById(id).orElse(null);
        model.addAttribute("blogpost", blogPost);
        model.addAttribute("staticpages", staticDao.findAll());
        Map<Integer, List<String>> tagMap = new HashMap<>();
        List<Tag> tags = blogPost.getTags();
        tagMap.put(blogPost.getId(), blogPost.getTags().stream()
                .map(t -> t.getName())
                .collect(Collectors.toList()));
        model.addAttribute("tagMap", tagMap);
        return "viewBlogPost";
    }

    @GetMapping("editStaticPage")
    public String editStaticPage(Integer id, Model model) {
        StaticPage staticPage = staticDao.findById(id).orElse(null);
        model.addAttribute("staticpage", staticPage);
        model.addAttribute("staticpages", staticDao.findAll());
        return "editStaticPage";
    }

    @PostMapping("editStaticPage")
    public String performEditStaticPage(StaticPage staticPage) {
        staticPage = staticDao.save(staticPage);
        return "redirect:/home";
    }

    @GetMapping("deleteStaticPage")
    public String deleteStaticPage(Integer id) {
        staticDao.deleteById(id);
        return "redirect:/home";
    }
}
