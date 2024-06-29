package br.com.easytax.easytax.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
}
