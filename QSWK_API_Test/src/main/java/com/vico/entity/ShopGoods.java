package com.vico.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * (ShopGoods)实体类
 *
 * @author makejava
 * @since 2020-08-19 11:19:04
 */
@Data
public class ShopGoods implements Serializable {
    private static final long serialVersionUID = -41191620604854682L;

    private Integer id;

    /**
     * 订单号
     */
    private String shopno;

    private String buynum;

    private String supnum;

    private String cycleday;

    private String cloundpayids;

    private String eachtnum;

    private String dayelec;

    private String packagename;
    private int type;


}