package com.javenock.gatewayservice.filter;

import com.javenock.gatewayservice.exception.UnAuthorizedException;
import com.javenock.gatewayservice.jwtValidator.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;

//    @Autowired
//    RestTemplate restTemplate;
        @Autowired
        private JwtUtils jwtUtils;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            if(routeValidator.isSecured.test(exchange.getRequest())){
                //header contain token?
                if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                    throw new RuntimeException("missing authorization header");
                }
                 String authheader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

                if (StringUtils.hasText(authheader) && authheader.startsWith("Bearer ")) {
                    authheader = authheader.substring(7);
                }

                Boolean result = jwtUtils.validateJwtToken(authheader);

                        if(result.equals(false)){
                            try {
                                throw new UnAuthorizedException("un authorized access to application");
                            } catch (UnAuthorizedException e) {
                                throw new RuntimeException(e);
                            }
                        }

            }
            return chain.filter(exchange);
        };
    }

    public static class Config{

    }
}
