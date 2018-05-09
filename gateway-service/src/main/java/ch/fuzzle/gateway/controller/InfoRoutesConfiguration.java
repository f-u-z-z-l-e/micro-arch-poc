package ch.fuzzle.gateway.controller;

import ch.fuzzle.gateway.repository.InfoRepository;
import ch.fuzzle.model.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.method;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class InfoRoutesConfiguration {

    @Bean
    RouterFunction<?> routes(InfoRepository infoRepository) {
        return nest(path("/info"),

                route(RequestPredicates.GET("/{id}"),
                        request -> ok().body(infoRepository.findById(request.pathVariable("id")), Info.class))

                        .andRoute(method(HttpMethod.POST),
                                request -> {
                                    infoRepository.insert(request.bodyToMono(Info.class));
                                    return ok().build();
                                })
        );
    }
}
