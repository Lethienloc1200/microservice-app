package com.programmingtechie.productservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    @NotEmpty(message = "Field 'name' must not be Empty-> DTO")
    private String name;
    @NotEmpty(message = "Field 'description' must not be Empty-> DTO")
    private String description;
    @NotNull(message = "Field 'Price' must not be Null-> DTO")
    private BigDecimal price;
}
