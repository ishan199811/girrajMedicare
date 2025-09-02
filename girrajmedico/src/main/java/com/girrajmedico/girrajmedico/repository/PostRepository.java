package com.girrajmedico.girrajmedico.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.girrajmedico.girrajmedico.model.Post.Post;

public interface PostRepository extends JpaRepository<Post , Long>{

}
