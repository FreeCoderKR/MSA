package com.msa.user.service;

import com.msa.user.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);

    Iterable<UserDto> findAllUser();

    UserDto findUserByUserId(String userId);


}
