package com.kyu.gabriel.manager.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@Component
@FeignClient("BookServiceProvider")
public interface BookService extends com.kyu.gabriel.core.client.BookService {
}
