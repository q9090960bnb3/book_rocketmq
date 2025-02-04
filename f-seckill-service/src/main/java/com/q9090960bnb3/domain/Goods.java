package com.q9090960bnb3.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Goods implements Serializable {
    private Integer id;

    private String goodsName;

    private BigDecimal price;

    private Integer stocks;

    private Integer status;

    private String pic;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}