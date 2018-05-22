package ch.fuzzle.gateway.configuration;

import org.springframework.context.annotation.Configuration;

@Configuration
public class InfoRoutesConfiguration {

//    @Bean
//    RouterFunction<ServerResponse> routes(InfoRepository infoRepository) {
//        return nest(path("/info"),
//                route(GET("/{id}"), request -> ok().body(infoRepository.findById(request.pathVariable("id")), Info.class))
//                        .andRoute(method(POST),
//                                request -> {
//                                    infoRepository.insert(request.bodyToMono(Info.class));
//                                    return ok().build();
//                                })
//        );
//    }

}
