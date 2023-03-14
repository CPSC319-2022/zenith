package com.blog;

import com.blog.controller.PostController;
import com.blog.exception.BlogException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
public class BlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}

	@GetMapping("/getPost")
	@ResponseBody
	public ResponseEntity<JSONObject> getPost(@RequestParam String input) {
		try {
			return ResponseEntity.ok(PostController.getPost(new JSONObject(input)));
		} catch (BlogException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/getPosts")
	@ResponseBody
	public ResponseEntity<JSONArray> getPosts(@RequestParam String input) {
		try {
			return ResponseEntity.ok(PostController.getPosts(new JSONObject(input)));
		} catch (BlogException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/createPost")
	@ResponseBody
	public ResponseEntity<JSONObject> createPost(@RequestParam String input) {
		try {
			PostController.createPost(new JSONObject(input));
			return ResponseEntity.ok().build();
		} catch (BlogException e) {
			return ResponseEntity.badRequest().build();
		}
	}
}
