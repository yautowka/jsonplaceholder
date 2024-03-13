package com.example.jsonplaceholder.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "photos")
public class Photo {
    private int albumId;
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
//    @GenericGenerator(name = "native")
    private long id;
    private String title;
    private String url;
    private String thumbnailUrl;
}
