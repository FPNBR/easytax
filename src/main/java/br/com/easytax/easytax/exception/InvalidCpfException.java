package br.com.easytax.easytax.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InvalidCpfException extends RuntimeException {
    private String message;
}
