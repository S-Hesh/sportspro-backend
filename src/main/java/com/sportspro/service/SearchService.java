package com.sportspro.service;

import com.sportspro.dto.SearchResultDTO;
import com.sportspro.model.Opportunity;
import com.sportspro.model.Post;
import com.sportspro.model.User;
import com.sportspro.repository.OpportunityRepository;
import com.sportspro.repository.PostRepository;
import com.sportspro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final UserRepository userRepo;
    private final PostRepository postRepo;
    private final OpportunityRepository oppRepo;

    @Transactional(readOnly = true)
    public List<SearchResultDTO> searchAll(String rawQ) {
        String q = rawQ == null ? "" : rawQ.trim();
        if (q.isBlank()) return List.of();

        List<SearchResultDTO> out = new ArrayList<>();

        // Users
        for (User u : userRepo.searchBasic(q)) {
            double score = score(u.getName(), q) * 2.0
                    + score(u.getSport(), q)
                    + score(u.getLocation(), q)
                    + score(u.getBio(), q)
                    + recency(u.getCreatedAt());
            out.add(SearchResultDTO.builder()
                    .type("user")
                    .id(u.getUserId())
                    .title(nz(u.getName()))
                    .subtitle(joinDot(u.getSport(), u.getLocation()))
                    .excerpt(snippet(u.getBio(), q, 140))
                    .avatarUrl(u.getAvatar())
                    .url(null)  // keep null to avoid linking to pages you haven't built
                    .createdAt(u.getCreatedAt())
                    .score(score)
                    .build());
        }

        // Posts
        for (Post p : postRepo.searchBasic(q)) {
            String author = p.getUser() != null ? p.getUser().getName() : "Unknown";
            String avatar = p.getUser() != null ? p.getUser().getAvatar() : null;
            double score = score(p.getContent(), q) * 1.6
                    + score(p.getTags(), q)
                    + score(author, q)
                    + recency(p.getCreatedAt());
            out.add(SearchResultDTO.builder()
                    .type("post")
                    .id(p.getPostId())
                    .title("Post by " + author)
                    .subtitle(author)
                    .excerpt(snippet(p.getContent(), q, 160))
                    .avatarUrl(avatar)
                    .url(null)
                    .createdAt(p.getCreatedAt())
                    .score(score)
                    .build());
        }

        // Opportunities
        for (Opportunity o : oppRepo.searchBasic(q)) {
            double score = score(o.getTitle(), q) * 1.8
                    + score(o.getCompany(), q)
                    + score(o.getDescription(), q)
                    + score(o.getRequirements(), q)
                    + score(o.getLocation(), q)
                    + score(o.getSport(), q)
                    + recency(o.getPostedAt());
            out.add(SearchResultDTO.builder()
                    .type("opportunity")
                    .id(o.getOpportunityId())
                    .title(nz(o.getTitle()))
                    .subtitle(joinDot(o.getCompany(), o.getLocation()))
                    .excerpt(snippet(o.getDescription(), q, 160))
                    .avatarUrl(null)
                    .url(null) // list page exists, but we keep null to avoid accidental navigation
                    .createdAt(o.getPostedAt())
                    .score(score)
                    .build());
        }

        // rank: primary by score, secondary by recency
        out.sort(Comparator.comparingDouble(SearchResultDTO::getScore).reversed()
                .thenComparing(SearchResultDTO::getCreatedAt,
                        Comparator.nullsLast(Comparator.reverseOrder())));
        return out;
    }

    // -------- helpers --------
    private static String nz(String s) { return s == null ? "" : s; }
    private static String joinDot(String a, String b) {
        boolean A = a != null && !a.isBlank(), B = b != null && !b.isBlank();
        if (A && B) return a + " • " + b;
        return A ? a : (B ? b : null);
    }
    private static double score(String hay, String needle) {
        if (hay == null || hay.isBlank() || needle == null || needle.isBlank()) return 0;
        String H = hay.toLowerCase(), N = needle.toLowerCase();
        if (H.equals(N)) return 10;
        if (H.startsWith(N)) return 6;
        if (H.contains(N)) return 3;
        return 0;
    }
    private static double recency(LocalDateTime t) {
        if (t == null) return 0;
        long days = Math.max(1, java.time.temporal.ChronoUnit.DAYS.between(t, LocalDateTime.now()));
        return 5.0 / Math.sqrt(days);
    }
    private static String snippet(String text, String q, int max) {
        if (text == null || text.isBlank()) return null;
        String lower = text.toLowerCase(), needle = q.toLowerCase();
        int at = lower.indexOf(needle);
        String s;
        if (at < 0) {
            s = text.length() > max ? text.substring(0, max) + "…" : text;
        } else {
            int start = Math.max(0, at - 40);
            int end = Math.min(text.length(), at + needle.length() + 40);
            s = (start > 0 ? "…" : "") + text.substring(start, end) + (end < text.length() ? "…" : "");
            if (s.length() > max) s = s.substring(0, max) + "…";
        }
        return s;
    }
}
