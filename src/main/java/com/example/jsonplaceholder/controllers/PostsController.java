package com.example.jsonplaceholder.controllers;

import com.example.jsonplaceholder.entity.AuthenticationResponse;
import com.example.jsonplaceholder.entity.LoginForm;
import com.example.jsonplaceholder.entity.SimpleResponse;
import com.example.jsonplaceholder.models.Comment;
import com.example.jsonplaceholder.models.Post;
import com.example.jsonplaceholder.models.UserFromServer;
import com.example.jsonplaceholder.repos.CommentRepository;
import com.example.jsonplaceholder.repos.PostRepository;
import com.example.jsonplaceholder.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.EventListener;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@RestController
@RequestMapping(path = "/api/posts")
@RequiredArgsConstructor
@EnableAspectJAutoProxy
@PreAuthorize("hasRole('POSTS_EDITOR') || hasRole('ADMIN')")
@Slf4j
public class PostsController {
    private final RestTemplate restTemplate;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private int count = 100;
    private final String url = "https://jsonplaceholder.typicode.com/posts";

    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('POSTS_VIEWER')")
    public @ResponseBody Post[] getPosts() {
        List<Post> users = postRepository.findAll();
        if (users.size() == count) return users.toArray(new Post[0]);
        ResponseEntity<Post[]> resp = restTemplate.getForEntity(
                this.url,
                Post[].class);
        if (resp.getStatusCode() == HttpStatus.OK) {
            Post[] posts_arr = resp.getBody();
            postRepository.saveAll(Arrays.asList(posts_arr));
            return posts_arr;
        }
        return null;
    }

    @GetMapping(path = "/{index}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('POSTS_VIEWER')")
    public @ResponseBody Post getPost(@PathVariable long index) {
        Optional<Post> post = postRepository.findById(index);
        if (post.isPresent()) return post.get();
        ResponseEntity<Post> resp = restTemplate.getForEntity(
                url + "/" + index,
                Post.class);

        if (resp.getStatusCode() == HttpStatus.OK) {
            Post post_req = resp.getBody();
            postRepository.save(post_req);
            return post_req;
        }
        return null;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Post createPost(@RequestBody Post newPost) {
        count = 101;
        newPost.setId(101);
        postRepository.save(newPost);
        return newPost;
    }

    @RequestMapping(value = "/{index}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Post updatePost(@PathVariable long index, @RequestBody Post newPost) {
//        restTemplate.put(
//                url + "/" + index,
//                newPost,
//                Post.class);
        newPost.setId(index); // т.к. данные не меняются, то остается сделать так)
        postRepository.save(newPost);
        return newPost;
    }

//    @PatchMapping(path = "/{index}")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    public @ResponseBody Post patchPost(@PathVariable long index, Map<String, String> params) {
////        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(params);
//        ResponseEntity<Post> response = restTemplate.exchange(
//                "https://jsonplaceholder.typicode.com/posts/" + index,
//                HttpMethod.PATCH,
//                new HttpEntity<>(params),
//                Post.class
//        );
//        return response.getStatusCode().is2xxSuccessful() ? response.getBody() : null;
//    }

    @DeleteMapping(path = "/{index}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String deletePost(@PathVariable long index) {
//        restTemplate.delete(url + "/" + index);
        postRepository.deleteById(index);
        return "Deleted";
    }

    @GetMapping(path = "/{index}/comments")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('POSTS_VIEWER')")
    public @ResponseBody Comment[] getComments(@PathVariable long index) {
        List<Comment> commentsByID = commentRepository.findAllByPostId(index);
        if (!commentsByID.isEmpty()) return commentsByID.toArray(new Comment[0]);
        ResponseEntity<Comment[]> resp = restTemplate.getForEntity(
                url + "/" + index + "/comments",
                Comment[].class);
        if (resp.getStatusCode() == HttpStatus.OK) {
            Comment[] coms_arr = resp.getBody();
            commentRepository.saveAll(Arrays.asList(coms_arr));
            return coms_arr;
        }
        return null;
    }
}