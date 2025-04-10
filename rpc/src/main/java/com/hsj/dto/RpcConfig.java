package com.hsj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcConfig {
    private String protocol;
    private String hostname;
    private Integer port;
}
