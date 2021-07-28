package com.msa.user.client;

import com.msa.user.vo.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "orderservice")
public interface OrderServiceClient {
    @GetMapping("/orderservice/{userId}/orders")
    List<OrderResponse> getOrders(@PathVariable String userId);

}
