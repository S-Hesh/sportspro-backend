package com.sportspro.controller;

import com.sportspro.dto.SearchResponse;
import com.sportspro.dto.SuggestResponse;
import com.sportspro.dto.SearchResultDTO;
import com.sportspro.repository.OpportunityRepository;
import com.sportspro.repository.PostRepository;
import com.sportspro.repository.UserRepository;
import com.sportspro.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService service;
    private final UserRepository userRepo;
    private final PostRepository postRepo;
    private final OpportunityRepository oppRepo;

    @GetMapping
    public SearchResponse search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String types // "users,posts,opportunities"
    ) {
        List<SearchResultDTO> all = service.searchAll(q);

        if (types != null && !types.isBlank()) {
            Set<String> allow = Set.of(types.toLowerCase().split(","));
            all = all.stream()
                    .filter(r -> allow.contains(mapType(r.getType())))
                    .collect(Collectors.toList());
        }

        int from = Math.min(page * pageSize, all.size());
        int to   = Math.min(from + pageSize, all.size());
        List<SearchResultDTO> pageData = all.subList(from, to);

        return new SearchResponse(q, page, pageSize, all.size(), pageData);
    }

    // Optional: suggestions for future dropdown
    @GetMapping("/suggest")
    public SuggestResponse suggest(@RequestParam String q) {
        List<String> users = userRepo.searchBasic(q).stream()
                .limit(5)
                .map(u -> u.getName())
                .collect(Collectors.toList());

        List<String> posts = postRepo.searchBasic(q).stream()
                .limit(5)
                .map(p -> {
                    String c = p.getContent() == null ? "" : p.getContent();
                    return c.length() > 60 ? c.substring(0, 60) + "…" : c;
                })
                .collect(Collectors.toList());

        List<String> opps = oppRepo.searchBasic(q).stream()
                .limit(5)
                .map(o -> o.getTitle())
                .collect(Collectors.toList());

        return new SuggestResponse(q, users, posts, opps);
    }

    // Java 11–style switch
    private static String mapType(String t) {
        if (t == null) return "";
        switch (t) {
            case "user":
                return "users";
            case "post":
                return "posts";
            case "opportunity":
                return "opportunities";
            default:
                return t;
        }
    }
}
