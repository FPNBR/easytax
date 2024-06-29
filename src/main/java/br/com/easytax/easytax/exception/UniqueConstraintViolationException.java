package br.com.easytax.easytax.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UniqueConstraintViolationException extends RuntimeException {
    private String message;
}
