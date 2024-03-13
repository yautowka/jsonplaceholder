package com.example.jsonplaceholder.controllers;

import com.example.jsonplaceholder.models.Audit;
import com.example.jsonplaceholder.models.UserFromServer;
import com.example.jsonplaceholder.repos.AuditsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping(path = "/api/audits")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AuditsController {
    private final AuditsRepository auditsRepository;

    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Page<Audit> getAudits(int size, int offset) {
        Pageable pageable = PageRequest.of(offset, size);;
        return auditsRepository.findAll(pageable);
    }
}