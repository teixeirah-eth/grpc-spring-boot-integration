package dev.gustavoteixeira.movie.service;


import dev.gustavoteixeira.grpcflix.movie.MovieDto;
import dev.gustavoteixeira.grpcflix.movie.MovieSearchRequest;
import dev.gustavoteixeira.grpcflix.movie.MovieSearchResponse;
import dev.gustavoteixeira.grpcflix.movie.MovieServiceGrpc;
import dev.gustavoteixeira.movie.repository.MovieRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
public class MovieService extends MovieServiceGrpc.MovieServiceImplBase {

    private final MovieRepository repository;

    @Override
    public void getMovies(MovieSearchRequest request, StreamObserver<MovieSearchResponse> responseObserver) {
        var genre = request.getGenre().toString();
        var movieDtos = repository.getMovieByGenreOrderByYearDesc(genre)
                .stream()
                .map(movie -> MovieDto.newBuilder()
                        .setTitle(movie.getTitle())
                        .setYear(movie.getYear())
                        .setRating(movie.getRating())
                        .build())
                .collect(Collectors.toSet());
        var movieSearchResponse = MovieSearchResponse.newBuilder()
                .addAllMovie(movieDtos)
                .build();
        responseObserver.onNext(movieSearchResponse);
        responseObserver.onCompleted();
    }
}
