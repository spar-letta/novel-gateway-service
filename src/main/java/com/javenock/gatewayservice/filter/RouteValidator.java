package com.javenock.gatewayservice.filter;


import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndPoints = List.of(
            "/auth/signup",
            "/auth/signin",
            "/eureka"

    );

    public Predicate<ServerHttpRequest> isSecured = request -> openApiEndPoints.stream().noneMatch(uri -> request.getURI().getPath().contains(uri));
}
