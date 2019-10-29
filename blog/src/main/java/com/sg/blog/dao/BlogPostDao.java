/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.blog.dao;

import com.sg.blog.entities.BlogPost;
import com.sg.blog.entities.Tag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author MARIA
 */
@Repository
public interface BlogPostDao extends JpaRepository<BlogPost, Integer> {

    List<BlogPost> findByApprovedFalse();

    List<BlogPost> findByApprovedTrue();

    List<BlogPost> findAllByTags(Tag tag);
}
