package com.vico.entity;


import lombok.Data;

import java.io.Serializable;

/**
 * (Logincase)实体类
 *
 * @author makejava
 * @since 2020-08-17 10:57:20
 */
@Data
public class Logincase implements Serializable {
    private static final long serialVersionUID = 922627273428305627L;

    private Object id;

    private String username;

    private String password;

    private String expected;


}