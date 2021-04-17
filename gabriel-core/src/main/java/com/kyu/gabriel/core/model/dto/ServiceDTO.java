package com.kyu.gabriel.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDTO {

    private String host;
    private int port;
    private String instanceId;
    private String scheme;
    private boolean isSecure;
}
