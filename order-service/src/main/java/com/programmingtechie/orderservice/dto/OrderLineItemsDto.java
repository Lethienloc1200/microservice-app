package com.programmingtechie.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItemsDto {
    private Long id;
    @NotBlank(message = "'skuCode' cannot be blank")
    @Length(min = 5, max = 200, message = "skuCode must be between 2-200 characters")
    private String skuCode;
    @Min(value = 10, message = "'price' price must be greater or equal to 10")
    @Max(value = 9999, message = "'price' prices must be less than or equal to 9999")
    private BigDecimal price;
    @Min(value = 1, message = "'quantity' price must be greater or equal to 1")
    @Max(value = 9999, message = "'quantity' prices must be less than or equal to 9999")
    private Integer quantity;
}
