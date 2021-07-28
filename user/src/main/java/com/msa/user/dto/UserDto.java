package com.msa.user.dto;

import com.msa.user.vo.OrderResponse;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String userId;
    private String email;
    private String name;
    private String pwd;
    private List<OrderResponse> orders;
}
