package com.girrajmedico.girrajmedico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.girrajmedico.girrajmedico.model.Post.Post;
import com.girrajmedico.girrajmedico.service.PostService;

@RestController
@RequestMapping("/api")
public class PostController {


@Autowired 
PostService postService;




@PostMapping("/savePost")
ResponseEntity<?> savePost(@RequestBody Post post){
	return ResponseEntity.ok(postService.savePost(post));
}


@GetMapping("/getAllPost")
ResponseEntity<?> getAllPost(){
	return ResponseEntity.ok(postService.getAllPost());
}


@PostMapping("/editPost/{id}")
ResponseEntity<?> editPost(@PathVariable Long id ,@RequestBody Post post){
	return ResponseEntity.ok(postService.editPost(post,id));
}


}
