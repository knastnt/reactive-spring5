package ru.knastnt.reactivespring5.n6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.knastnt.reactivespring5.n6.web.OrderHandler;

@SpringBootApplication
public class ReactiveSpring5Application {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveSpring5Application.class, args);
	}

	@Bean
	public RouterFunction<ServerResponse> routes(OrderHandler handler) {
		return
				RouterFunctions.nest(
						RequestPredicates.path("/orders"),
						RouterFunctions.nest(
								RequestPredicates.accept(MediaType.APPLICATION_JSON),
								RouterFunctions.route(
										RequestPredicates.GET("/{id}"),
										handler::get
								)
						)
						.andNest(
								RequestPredicates.contentType(MediaType.APPLICATION_JSON),
								RouterFunctions.route(
										RequestPredicates.POST("/"),
										handler::create
								)
						)
				);
	}
}
