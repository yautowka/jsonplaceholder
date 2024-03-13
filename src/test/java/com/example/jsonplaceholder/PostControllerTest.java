package com.example.jsonplaceholder;

import com.example.jsonplaceholder.models.*;
import com.example.jsonplaceholder.models.Post;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PostControllerTest extends AbstractTest {

    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void postPost() throws Exception {
        super.setUp();
        String uri = "/api/posts/1";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        Post post = super.mapFromJson(content, Post.class);
        post.setBody("foo");
        String uripost = "/api/posts";
        String inputJson = super.mapToJson(post);
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uripost)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
        content = mvcResult.getResponse().getContentAsString();
        post = super.mapFromJson(content, Post.class);
        assertEquals("foo", post.getBody());
        assertEquals(101, post.getId());
    }


    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void getPosts() throws Exception {
        super.setUp();
        String uri = "/api/posts";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Post[] Posts = super.mapFromJson(content, Post[].class);
        assertTrue(Posts.length > 0);
    }

    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void getPost() throws Exception {
        super.setUp();
        String uri = "/api/posts/1";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Post post = super.mapFromJson(content, Post.class);
        assertEquals(1, post.getId());
    }

    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void updatePost() throws Exception {
        super.setUp();
        String uri = "/api/posts/1";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        Post post = super.mapFromJson(content, Post.class);
        post.setTitle("foo");
        String inputJson = super.mapToJson(post);

        mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        content = mvcResult.getResponse().getContentAsString();
        post = super.mapFromJson(content, Post.class);
        assertEquals(post.getTitle(), "foo");
    }

    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void deletePost() throws Exception {
        super.setUp();
        String uri = "/api/posts/1";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "Deleted");
    }

    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void getCommentsFromPost() throws Exception {
        super.setUp();
        String uri = "/api/posts/1/comments";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Comment[] comments = super.mapFromJson(content, Comment[].class);
        assertTrue(comments.length > 0);
        assertEquals(comments[0].getEmail(), "Eliseo@gardner.biz");
    }
}
