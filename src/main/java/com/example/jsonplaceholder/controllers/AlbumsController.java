package com.example.jsonplaceholder.controllers;

import com.example.jsonplaceholder.models.*;
import com.example.jsonplaceholder.models.Album;
import com.example.jsonplaceholder.repos.AlbumRepository;
import com.example.jsonplaceholder.repos.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(path = "/api/albums")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ALBUMS_EDITOR') || hasRole('ADMIN')")
public class AlbumsController {
    private final RestTemplate restTemplate;
    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;
    private int count = 100;
    private final String url = "https://jsonplaceholder.typicode.com/albums";

    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ALBUMS_VIEWER')")
    public @ResponseBody Album[] getAlbums() {
        List<Album> albums = albumRepository.findAll();
        if (albums.size() == count) return albums.toArray(new Album[0]);
        ResponseEntity<Album[]> resp = restTemplate.getForEntity(
                this.url,
                Album[].class);
        if (resp.getStatusCode() == HttpStatus.OK) {
            Album[] albums_arr = resp.getBody();
            albumRepository.saveAll(Arrays.asList(albums_arr));
            return albums_arr;
        }
        return null;
    }

    @GetMapping(path = "/{index}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ALBUMS_VIEWER')")
    public @ResponseBody Album getAlbum(@PathVariable long index) {
        Optional<Album> album = albumRepository.findById(index);
        if (album.isPresent()) return album.get();
        ResponseEntity<Album> resp = restTemplate.getForEntity(
                url + "/" + index,
                Album.class);

        if (resp.getStatusCode() == HttpStatus.OK) {
            Album respBody = resp.getBody();
            albumRepository.save(respBody);
            return respBody;
        }
        return null;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Album createAlbum(@RequestBody Album newAlbum) {
//        ResponseEntity<Album> resp = restTemplate.postForEntity(
//                url,
//                Album,
//                Album.class);
//        return resp.getStatusCode() == HttpStatus.CREATED ? resp.getBody() : null;
        count = 101;
        newAlbum.setId(101);
        albumRepository.save(newAlbum);
        return newAlbum;
    }

    @RequestMapping(value = "/{index}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Album updateAlbum(@PathVariable long index, @RequestBody Album newAlbum) {
//        restTemplate.put(
//                url + "/" + index,
//                newAlbum,
//                Album.class);
        newAlbum.setId(index); // т.к. данные не меняются, то остается сделать так
        albumRepository.save(newAlbum);
        return newAlbum;

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
    public @ResponseBody String deleteAlbum(@PathVariable long index) {
//        restTemplate.delete(url + "/" + index);
        albumRepository.deleteById(index);
        return "Deleted";

    }

    @GetMapping(path = "/{index}/photos")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ALBUMS_VIEWER')")
    public @ResponseBody Photo[] getPosts(@PathVariable long index) {
        List<Photo> commentsByID = photoRepository.findAllByAlbumId(index);
        if (!commentsByID.isEmpty()) return commentsByID.toArray(new Photo[0]);
        ResponseEntity<Photo[]> resp = restTemplate.getForEntity(
                url + "/" + index + "/photos",
                Photo[].class);
        if (resp.getStatusCode() == HttpStatus.OK) {
            Photo[] photos_arr = resp.getBody();
            photoRepository.saveAll(Arrays.asList(photos_arr));
            return photos_arr;
        }
        return null;
    }
}