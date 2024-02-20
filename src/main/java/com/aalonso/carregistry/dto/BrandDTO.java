package com.aalonso.carregistry.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class BrandDTO implements Serializable {
    private String id;
    private String name;
    private int warranty;
    private String country;
}
