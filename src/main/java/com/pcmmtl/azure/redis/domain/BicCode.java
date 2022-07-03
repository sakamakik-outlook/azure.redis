package com.pcmmtl.azure.redis.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public
class BicCode {
    private int id;
    private String bank;
    private String city;
    private String branch;
    private String swift_code;
}