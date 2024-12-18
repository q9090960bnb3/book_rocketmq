package com.q9090960bnb3.brocketmqbootp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgModel {
    private String OrderSn;
    private Integer useId; 
    private String desc;  // 下单 短信 物流

    // xxx
}
