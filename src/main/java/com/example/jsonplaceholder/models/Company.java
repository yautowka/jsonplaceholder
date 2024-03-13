package com.example.jsonplaceholder.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native")
    @Column(name = "id")
    @JsonIgnore
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "catchPhrase")
    private String catchPhrase;
    @Column(name = "bs")
    private String bs;
}
