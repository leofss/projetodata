package com.leonardo.projetodata.exception;

import com.leonardo.projetodata.enumerator.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RawMaterialNotFound.class)
    public ResponseEntity<ErrorMessageBuilder> handleRawMaterialNotFoundException(
            RawMaterialNotFound rawMaterialNotFound, HttpServletRequest httpServletRequest
    ){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessageBuilder(
                        httpServletRequest,
                        HttpStatus.NOT_FOUND,
                        rawMaterialNotFound.getErrorMessage(),
                        rawMaterialNotFound.getParams()
                ));
    }

    @ExceptionHandler(RawMaterialCodeNotUnique.class)
    public ResponseEntity<ErrorMessageBuilder> handleRawMaterialCodeNotUniqueException(
            RawMaterialCodeNotUnique rawMaterialCodeNotUnique, HttpServletRequest httpServletRequest
    ){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessageBuilder(
                        httpServletRequest,
                        HttpStatus.CONFLICT,
                        rawMaterialCodeNotUnique.getErrorMessage(),
                        rawMaterialCodeNotUnique.getParams()
                ));
    }

    @ExceptionHandler(ProductNotFound.class)
    public ResponseEntity<ErrorMessageBuilder> handleProductNotFoundException(
            ProductNotFound productNotFound, HttpServletRequest httpServletRequest
    ){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessageBuilder(
                        httpServletRequest,
                        HttpStatus.NOT_FOUND,
                        productNotFound.getErrorMessage(),
                        productNotFound.getParams()
                ));
    }

    @ExceptionHandler(ProductCodeNotUnique.class)
    public ResponseEntity<ErrorMessageBuilder> handleProductCodeNotUniqueException(
            ProductCodeNotUnique productCodeNotUnique, HttpServletRequest httpServletRequest
    ){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessageBuilder(
                        httpServletRequest,
                        HttpStatus.CONFLICT,
                        productCodeNotUnique.getErrorMessage(),
                        productCodeNotUnique.getParams()
                ));
    }

    @ExceptionHandler(DuplicateRawMaterialInProduct.class)
    public ResponseEntity<ErrorMessageBuilder> handleDuplicateRawMaterialInProductException(
            DuplicateRawMaterialInProduct duplicateRawMaterialInProduct, HttpServletRequest httpServletRequest
    ){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessageBuilder(
                        httpServletRequest,
                        HttpStatus.CONFLICT,
                        duplicateRawMaterialInProduct.getErrorMessage(),
                        duplicateRawMaterialInProduct.getParams()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageBuilder> handleMethodArgumentNotValidException(HttpServletRequest request,
                                                                                     BindingResult bindingResult) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessageBuilder(
                        request,
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        ErrorMessage.INVALID_FIELD,
                        bindingResult
                ));
    }

}
