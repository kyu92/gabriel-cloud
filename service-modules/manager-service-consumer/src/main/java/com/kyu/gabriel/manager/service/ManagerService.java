package com.kyu.gabriel.manager.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@Component
@FeignClient("ManagerServiceProvider")
public interface ManagerService extends com.kyu.gabriel.core.client.ManagerService {
}
