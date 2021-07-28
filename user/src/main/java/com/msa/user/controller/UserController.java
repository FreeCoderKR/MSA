package com.msa.user.controller;

import com.msa.user.dto.UserDto;
import com.msa.user.service.UserService;
import com.msa.user.vo.Greeting;
import com.msa.user.vo.UserRequest;
import com.msa.user.vo.UserResponse;
import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class UserController {

    Environment env;
    UserService userService;

    @Autowired
    Greeting greeting;

    @Autowired
    public UserController(Environment env, UserService userService) {
        this.env = env;
        this.userService = userService;
    }

    @GetMapping("/admin/health_check")
    public String healthCheck() {
        return String.format("It's working in User Service"
                        + ", port(local.server.port)=" + env.getProperty("local.server.port")
                        + ", port(server.port)=" + env.getProperty("server.port")
                        + ", token.secret=" + env.getProperty("token.secret")
                        + ", token.expiration=" + env.getProperty("token.expiration"));


    }

    @GetMapping("/admin/welcome")
    public String welcome() {
        return greeting.getMessage();
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest) {

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(userRequest, UserDto.class);
        UserDto resultUserDto = userService.createUser(userDto);
        UserResponse userResponse = mapper.map(userDto, UserResponse.class);


        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getUsers() {
        Iterable<UserDto> users = userService.findAllUser();
        List<UserResponse> userResponses = new ArrayList<>();
        users.forEach(u -> {
            userResponses.add(new ModelMapper().map(u, UserResponse.class));
        });
        return ResponseEntity.status(HttpStatus.OK).body(userResponses);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserResponse> getUserByUserId(@PathVariable("userId") String userId) {
        UserDto user = userService.findUserByUserId(userId);
        UserResponse userRes = new ModelMapper().map(user, UserResponse.class);
        return ResponseEntity.status(HttpStatus.OK).body(userRes);

    }
}
