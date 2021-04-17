package com.kyu.gabriel.book.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@Component
@FeignClient("UserServiceProvider")
public interface UserService extends com.kyu.gabriel.core.client.UserService {
}
