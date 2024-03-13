package com.example.jsonplaceholder.controllers;

import com.example.jsonplaceholder.models.*;
import com.example.jsonplaceholder.repos.AlbumRepository;
import com.example.jsonplaceholder.repos.PostRepository;
import com.example.jsonplaceholder.repos.TodoRepository;
import com.example.jsonplaceholder.repos.UserFromServerRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;


@RestController
@RequestMapping(path = "/api/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USERS_EDITOR') || hasRole('ADMIN')")
public class UsersController {
    private final RestTemplate restTemplate;
    private final UserFromServerRepository userFromServerRepository;
    private final TodoRepository todoRepository;
    private final AlbumRepository albumRepository;
    private final PostRepository postRepository;
    private final Set<Long> setTodo = new HashSet<>();
    private final Set<Long> setAlbum = new HashSet<>();
    private final Set<Long> setPost = new HashSet<>();
    private final String url = "https://jsonplaceholder.typicode.com/users";
    private int count = 10;

    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USERS_VIEWER')")
    public @ResponseBody UserFromServer[] getUsers() {
        List<UserFromServer> users = userFromServerRepository.findAll();
        if (users.size() == count) return users.toArray(new UserFromServer[0]);
        ResponseEntity<UserFromServer[]> resp = restTemplate.getForEntity(
                this.url,
                UserFromServer[].class);
        if (resp.getStatusCode() == HttpStatus.OK) {
            UserFromServer[] users_arr = resp.getBody();
            userFromServerRepository.saveAll(Arrays.asList(users_arr));
            return users_arr;
        }
        return null;
    }

    @GetMapping(path = "/{index}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USERS_VIEWER')")
    public @ResponseBody UserFromServer getUser(@PathVariable long index) {
        Optional<UserFromServer> user = userFromServerRepository.findById(index);
        if (user.isPresent()) return user.get();
        ResponseEntity<UserFromServer> resp = restTemplate.getForEntity(
                url + "/" + index,
                UserFromServer.class);

        if (resp.getStatusCode() == HttpStatus.OK) {
            UserFromServer userFromServer = resp.getBody();
            userFromServerRepository.save(userFromServer);
            return userFromServer;
        }
        return null;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody UserFromServer createUser(@RequestBody UserFromServer u) {
//        ResponseEntity<UserFromServer> resp = restTemplate.postForEntity(
//                url,
//                userFromServer,
//                UserFromServer.class);
//        if (resp.getStatusCode() == HttpStatus.OK) {
//        System.out.println(1);
        count = 11;
//        UserFromServer u = mapFromJson(s, UserFromServer.class);
        u.setId(11);
        userFromServerRepository.save(u);
        return u;
//        }
//        return null;
    }


    @RequestMapping(value = "/{index}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody UserFromServer updateUser(@PathVariable long index, @RequestBody UserFromServer newUser) {
//        restTemplate.put(
//                url + "/" + index,
//                newUser,
//                UserFromServer.class);
        newUser.setId(index); // т.к. данные не меняются, то остается сделать так
        userFromServerRepository.save(newUser);
        return newUser;
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
    public @ResponseBody String deleteUser(@PathVariable long index) {
//        restTemplate.delete(url + "/" + index);
        userFromServerRepository.deleteById(index);
        return "Deleted";
    }

    @GetMapping(path = "/{index}/posts")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USERS_VIEWER')")
    public @ResponseBody Post[] getPosts(@PathVariable long index) {
        if (setPost.contains(index)) return postRepository.findAllByUserId(index).toArray(new Post[0]);

        ResponseEntity<Post[]> resp = restTemplate.getForEntity(
                url + "/" + index + "/posts",
                Post[].class);
        if (resp.getStatusCode() == HttpStatus.OK) {
            Post[] posts_arr = resp.getBody();
            postRepository.saveAll(Arrays.asList(posts_arr));
            setPost.add(index);
            return posts_arr;
        }
        return null;
    }

    @GetMapping(path = "/{index}/albums")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USERS_VIEWER')")
    public @ResponseBody Album[] getAlbums(@PathVariable long index) {
        if (setAlbum.contains(index)) return albumRepository.findAllByUserId(index).toArray(new Album[0]);

        ResponseEntity<Album[]> resp = restTemplate.getForEntity(
                url + "/" + index + "/albums",
                Album[].class);
        if (resp.getStatusCode() == HttpStatus.OK) {
            Album[] albums_arr = resp.getBody();
            albumRepository.saveAll(Arrays.asList(albums_arr));
            setAlbum.add(index);
            return albums_arr;
        }
        return null;
    }

    @GetMapping(path = "/{index}/todos")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USERS_VIEWER')")
    public @ResponseBody Todo[] getTodos(@PathVariable long index) {
        if (setTodo.contains(index)) return todoRepository.findAllByUserId(index).toArray(new Todo[0]);

        ResponseEntity<Todo[]> resp = restTemplate.getForEntity(
                url + "/" + index + "/todos",
                Todo[].class);
        if (resp.getStatusCode() == HttpStatus.OK) {
            Todo[] todos_arr = resp.getBody();
            todoRepository.saveAll(Arrays.asList(todos_arr));
            setTodo.add(index);
            return todos_arr;
        }
        return null;
    }
}