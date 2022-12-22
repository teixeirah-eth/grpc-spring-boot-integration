package dev.gustavoteixeira.user.service;

import dev.gustavoteixeira.grpcflix.common.Genre;
import dev.gustavoteixeira.grpcflix.user.UserGenreUpdateRequest;
import dev.gustavoteixeira.grpcflix.user.UserResponse;
import dev.gustavoteixeira.grpcflix.user.UserSearchRequest;
import dev.gustavoteixeira.grpcflix.user.UserServiceGrpc;
import dev.gustavoteixeira.user.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import javax.transaction.Transactional;

@GrpcService
@RequiredArgsConstructor
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    private final UserRepository repository;

    @Override
    public void getUserGenre(UserSearchRequest request, StreamObserver<UserResponse> responseObserver) {
        var userResponseBuilder = UserResponse.newBuilder();

        repository.findById(request.getLoginId())
                .ifPresent(user ->
                        userResponseBuilder.setName(user.getName())
                                .setLoginId(user.getLogin())
                                .setGenre(Genre.valueOf(user.getGenre().toUpperCase())));

        responseObserver.onNext(userResponseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    @Transactional
    public void updateUserGenre(UserGenreUpdateRequest request, StreamObserver<UserResponse> responseObserver) {
        var userResponseBuilder = UserResponse.newBuilder();

        repository.findById(request.getLoginId())
                .ifPresent(user -> {
                    user.setGenre(request.getGenre().toString());
                    userResponseBuilder.setName(user.getName())
                            .setLoginId(user.getLogin())
                            .setGenre(Genre.valueOf(user.getGenre().toUpperCase()));
                });

        responseObserver.onNext(userResponseBuilder.build());
        responseObserver.onCompleted();
    }
}
