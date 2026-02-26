package com.leonardo.projetodata.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.leonardo.projetodata.enumerator.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ErrorMessageBuilder {
    private String path;

    private String method;

    private int status;

    private String message;

    private HttpStatus httpStatus;

    private ErrorMessage errorCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> errors;

    public ErrorMessageBuilder(HttpServletRequest request, HttpStatus status, ErrorMessage errorMessage,
                               Object... params) {
        this.path = request.getRequestURI();
        this.method = request.getMethod();
        this.status = status.value();
        this.httpStatus = status;
        this.errorCode = errorMessage;
        this.message = errorMessage.getMessage(params);
    }

    public ErrorMessageBuilder() {
    }

    public ErrorMessageBuilder(HttpServletRequest request, HttpStatus status, ErrorMessage errorMessage,
                               BindingResult bindingResult, Object... params) {
        this(request, status, errorMessage, params);
        addErrors(bindingResult);
    }

    private void addErrors(BindingResult result) {
        this.errors = new HashMap<>();
        for (FieldError fieldError : result.getFieldErrors()) {
            this.errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }
}
