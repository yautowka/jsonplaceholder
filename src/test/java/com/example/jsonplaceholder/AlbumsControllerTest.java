package com.example.jsonplaceholder;

import com.example.jsonplaceholder.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlbumsControllerTest extends AbstractTest {

    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void postAlbums() throws Exception {
        super.setUp();
        String uri = "/api/albums/1";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        Album album = super.mapFromJson(content, Album.class);
        album.setTitle("foo");
        String uripost = "/api/albums";
        String inputJson = super.mapToJson(album);
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uripost)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
        content = mvcResult.getResponse().getContentAsString();
        album = super.mapFromJson(content, Album.class);
        assertEquals("foo", album.getTitle());
        assertEquals(101, album.getId());
    }


    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void getAlbums() throws Exception {
        super.setUp();
        String uri = "/api/albums";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Album[] albums = super.mapFromJson(content, Album[].class);
        assertTrue(albums.length > 0);
    }

    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void getAlbum() throws Exception {
        super.setUp();
        String uri = "/api/albums/1";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Album album = super.mapFromJson(content, Album.class);
        assertEquals(1, album.getUserId());
    }

    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void updateAlbum() throws Exception {
        super.setUp();
        String uri = "/api/albums/1";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        Album album = super.mapFromJson(content, Album.class);
        album.setTitle("foo");
        String inputJson = super.mapToJson(album);

        mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        content = mvcResult.getResponse().getContentAsString();
        album = super.mapFromJson(content, Album.class);
        assertEquals(album.getTitle(), "foo");
    }

    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void deleteAlbum() throws Exception {
        super.setUp();
        String uri = "/api/albums/1";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "Deleted");
    }

    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void getPhotosFromAlbum() throws Exception {
        super.setUp();
        String uri = "/api/albums/1/photos";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Photo[] photos = super.mapFromJson(content, Photo[].class);
        assertTrue(photos.length > 0);
        assertEquals(photos[0].getTitle(), "accusamus beatae ad facilis cum similique qui sunt");
    }
}
