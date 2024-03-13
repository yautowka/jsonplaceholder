package com.example.jsonplaceholder;

import com.example.jsonplaceholder.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserControllerTest extends AbstractTest {

    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void postUser() throws Exception {
        super.setUp();
        String uri = "/api/users/1";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        UserFromServer user = super.mapFromJson(content, UserFromServer.class);
        user.setName("foo");
        String uripost = "/api/users";
        String inputJson = super.mapToJson(user);
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uripost)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
        content = mvcResult.getResponse().getContentAsString();
        user = super.mapFromJson(content, UserFromServer.class);
        assertEquals("foo", user.getName());
        assertEquals(11, user.getId());
    }


    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void getUsers() throws Exception {
        super.setUp();
        String uri = "/api/users";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        UserFromServer[] users = super.mapFromJson(content, UserFromServer[].class);
        assertTrue(users.length > 0);
    }

    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void getUser() throws Exception {
        super.setUp();
        String uri = "/api/users/1";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        UserFromServer user = super.mapFromJson(content, UserFromServer.class);
        assertEquals("1-770-736-8031 x56442", user.getPhone());
    }

    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void updateUser() throws Exception {
        super.setUp();
        String uri = "/api/users/1";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        UserFromServer user = super.mapFromJson(content, UserFromServer.class);
        user.setName("foo");
        String inputJson = super.mapToJson(user);

        mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        content = mvcResult.getResponse().getContentAsString();
        user = super.mapFromJson(content, UserFromServer.class);
        assertEquals(user.getName(), "foo");
    }

    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void deleteUser() throws Exception {
        super.setUp();
        String uri = "/api/users/1";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "Deleted");
    }
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void getPostsFromUser() throws Exception {
        super.setUp();
        String uri = "/api/users/1/posts";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Post[] posts = super.mapFromJson(content, Post[].class);
        assertTrue(posts.length > 0);
        assertEquals(posts[0].getUserId(), 1);
    }
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void getAlbumsFromUser() throws Exception {
        super.setUp();
        String uri = "/api/users/1/albums";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Album[] albums = super.mapFromJson(content, Album[].class);
        assertTrue(albums.length > 0);
        assertEquals(albums[0].getUserId(), 1);
    }
    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void getTodosFromUser() throws Exception {
        super.setUp();
        String uri = "/api/users/1/todos";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Todo[] todos = super.mapFromJson(content, Todo[].class);
        assertTrue(todos.length > 0);
        assertEquals(todos[0].getUserId(), 1);
    }

}
