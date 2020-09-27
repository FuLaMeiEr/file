package com.wahaha.demo.contents;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {
    private Integer code;
    private String messge;

}
