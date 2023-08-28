package com.btk.ordercorner.model.vm;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchOrdersByDateVm {
    
    private LocalDate date;
    @NotNull
    private int customerId;
}
