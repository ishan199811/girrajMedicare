package com.girrajmedico.girrajmedico.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.girrajmedico.girrajmedico.model.Post.Post;
import com.girrajmedico.girrajmedico.repository.PostRepository;

@Service
public class PostService {

	@Autowired
	PostRepository postRepository;

	public ResponseEntity<?> savePost(Post post) {
		// TODO Auto-generated method stub
		postRepository.save(post);
		
		return ResponseEntity.ok("Post Saved");
	}

	public ResponseEntity<?> getAllPost() {
		// TODO Auto-generated method stub
		return ResponseEntity.ok(postRepository.findAll());
	}

	public ResponseEntity<?> editPost(Post post, Long id) {
		
		
		// TODO Auto-generated method stub
		Optional<Post> postById=postRepository.findById(id);
		if(!postById.isPresent()) {
			return ResponseEntity.ok("Post not found");
		}
Post poss=postById.get();

poss.setActive(post.isActive());
poss.setCreateDate(post.getCreateDate());
poss.setCreaterName(post.getCreaterName());
poss.setPostLink(post.getPostLink());
poss.setPostType(post.getPostType());
postRepository.save(poss);
		return ResponseEntity.ok("PostUpdated successfully");
	}
	
	
}
