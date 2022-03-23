package com.hyb.ServiceBase.ExceptionHandler;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HybException extends RuntimeException{
    private Integer code;
    private String msg;
}
