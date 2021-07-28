package com.msa.user.service;

import com.msa.user.client.OrderServiceClient;
import com.msa.user.dto.UserDto;
import com.msa.user.jpa.UserEntity;
import com.msa.user.jpa.UserRepository;
import com.msa.user.vo.OrderResponse;
import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder;
    Environment env;
    RestTemplate restTemplate;

    OrderServiceClient orderServiceClient;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, Environment env, RestTemplate restTemplate, OrderServiceClient orderServiceClient) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.env = env;
        this.restTemplate = restTemplate;
        this.orderServiceClient = orderServiceClient;
    }




    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null)
            throw new UsernameNotFoundException(email);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
                true, true,
                true, true, new ArrayList<>());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        String encryptedPwd = passwordEncoder.encode(userDto.getPwd());
        userEntity.setEncryptedPwd(encryptedPwd);
        userRepository.save(userEntity);

        return userDto;
    }

    @Override
    public List<UserDto> findAllUser() {
        Iterable<UserEntity> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        users.forEach(u->{
            userDtos.add(mapper.map(u, UserDto.class));
        });
        return userDtos;
    }

    @Override
    public UserDto findUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null){
            throw new UsernameNotFoundException("user not found");
        }
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(userEntity, UserDto.class);


        /**
         * using as rest template
         * */
//        String orderUrl = String.format(env.getProperty("orderservice.url"), userId);
//        ResponseEntity<List<OrderResponse>> orderListResponse =
//                restTemplate.exchange(orderUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<OrderResponse>>() {//
//        });
//        List<OrderResponse> ordersList = orderListResponse.getBody();
        /**
         * using as feign client
         * */
        List<OrderResponse> ordersList = orderServiceClient.getOrders(userId);
        userDto.setOrders(ordersList);
        return userDto;

    }
}
