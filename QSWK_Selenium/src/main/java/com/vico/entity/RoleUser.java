package com.vico.entity;


import lombok.Data;

import java.io.Serializable;

/**
 * (RoleUser)实体类
 *
 * @author makejava
 * @since 2020-08-27 11:28:11
 */
@Data

public class RoleUser implements Serializable {
    private static final long serialVersionUID = 523135739511632887L;

    private Integer id;

    private String username;

    private String password;

    private String isregister;


}