package com.q9090960bnb3.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRecords implements Serializable {
    private Integer id;

    private Integer userId;

    private String orderSn;

    private Integer goodsId;

    private Date createTime;

    private static final long serialVersionUID = 1L;
}