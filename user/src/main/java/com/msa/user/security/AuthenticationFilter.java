package com.msa.user.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msa.user.service.UserService;
import com.msa.user.vo.LoginRequest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
@Component
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private UserService userService;
    private Environment env;



    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, Environment env) {
        super.setAuthenticationManager(authenticationManager);
        this.userService = userService;
        this.env = env;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        try{
            /**
             * 1. http로 들어온 inputstream data를 ObjectMapper를 통해 LoginRequest에 매핑하고,
             * 2. 그 값을 input으로 넣어 token화 시킨 결과값을 AuthenticationManager에 authenticate라는 함수에 넣어서
             * 3. 인증을 받은 결과를 return 한다.
             * */

            LoginRequest creds = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>()));


        } catch (IOException e){
            throw new RuntimeException(e);
        }


    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        //여기서 해야할 것. 1. authresult=>username 추출=>userDetail을 가져옴.
        String userName = ((User)authResult.getPrincipal()).getUsername();
        UserDetails userDetails = userService.loadUserByUsername(userName);
        log.info("token.secret");
        log.info("token.expiration");
        log.info(env.getProperty("token.secret"));
        log.info(env.getProperty("token.expiration"));
        String token = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration"))))
                .signWith(SignatureAlgorithm.HS256, env.getProperty("token.secret"))
                .compact();

        response.addHeader("token", token);
        response.addHeader("username", userName);

    }
}
