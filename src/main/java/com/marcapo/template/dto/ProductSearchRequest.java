package com.marcapo.template.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class ProductSearchRequest {
    private String productId;
    private String name;
    private double price;
    private String description;
    private String manufacturingDate;
}
