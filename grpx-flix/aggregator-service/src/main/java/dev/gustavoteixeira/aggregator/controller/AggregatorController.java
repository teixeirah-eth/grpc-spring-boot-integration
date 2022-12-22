package dev.gustavoteixeira.aggregator.controller;

import dev.gustavoteixeira.aggregator.dto.RecommendedMovie;
import dev.gustavoteixeira.aggregator.dto.UserGenre;
import dev.gustavoteixeira.aggregator.service.UserMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AggregatorController {

    private final UserMovieService userMovieService;

    @GetMapping("/user/{loginId}")
    public List<RecommendedMovie> getMovies(@PathVariable String loginId) {
        return userMovieService.getUserMovieSuggestions(loginId);
    }

    @PutMapping("/user")
    public void setUserGenre(@RequestBody UserGenre userGenre) {
        userMovieService.setUserGenre(userGenre);
    }

}
