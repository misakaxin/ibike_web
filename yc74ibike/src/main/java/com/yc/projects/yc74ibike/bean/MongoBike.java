package com.yc.projects.yc74ibike.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class MongoBike  implements Serializable {
    private String id;
    private Integer status;
    private Double[] loc;
    private String qrcode;
}