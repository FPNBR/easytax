package br.com.easytax.easytax.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InvalidUserInformation extends RuntimeException {
    private String message;
}
