package com.pcmmtl.azure.redis.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BicCodeList {
    String country;
    String country_code;
    List<BicCode> list;
}
