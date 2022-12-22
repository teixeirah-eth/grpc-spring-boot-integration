package dev.gustavoteixeira.aggregator.service;

import dev.gustavoteixeira.aggregator.dto.RecommendedMovie;
import dev.gustavoteixeira.aggregator.dto.UserGenre;
import dev.gustavoteixeira.grpcflix.common.Genre;
import dev.gustavoteixeira.grpcflix.movie.MovieDto;
import dev.gustavoteixeira.grpcflix.movie.MovieSearchRequest;
import dev.gustavoteixeira.grpcflix.movie.MovieServiceGrpc;
import dev.gustavoteixeira.grpcflix.user.UserGenreUpdateRequest;
import dev.gustavoteixeira.grpcflix.user.UserResponse;
import dev.gustavoteixeira.grpcflix.user.UserSearchRequest;
import dev.gustavoteixeira.grpcflix.user.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Service
public class UserMovieService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    @GrpcClient("movie-service")
    private MovieServiceGrpc.MovieServiceBlockingStub movieStub;

    public List<RecommendedMovie> getUserMovieSuggestions(String loginId) {
        var userSearchRequest = UserSearchRequest.newBuilder()
                .setLoginId(loginId)
                .build();

        var userResponse = userStub.getUserGenre(userSearchRequest);

        MovieSearchRequest movieSearchRequest = MovieSearchRequest.newBuilder()
                .setGenre(userResponse.getGenre()).build();

        return movieStub.getMovies(movieSearchRequest)
                .getMovieList()
                .stream()
                .map(toRecommendedMovie())
                .collect(toList());
    }

    public void setUserGenre(UserGenre userGenre) {
        var userGenreUpdateRequest = UserGenreUpdateRequest.newBuilder()
                .setLoginId(userGenre.getLoginId())
                .setGenre(Genre.valueOf(userGenre.getGenre().toUpperCase()))
                .build();
        UserResponse userResponse = userStub.updateUserGenre(userGenreUpdateRequest);
    }

    private static Function<MovieDto, RecommendedMovie> toRecommendedMovie() {
        return movie -> RecommendedMovie.builder()
                .year(movie.getYear())
                .rating(movie.getRating())
                .title(movie.getTitle())
                .build();
    }


}
