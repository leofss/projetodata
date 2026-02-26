package com.leonardo.projetodata.exception;

import com.leonardo.projetodata.enumerator.ErrorMessage;

public class ProductCodeNotUnique extends BaseCustomException{
    public ProductCodeNotUnique(ErrorMessage errorMessage, Object... params) {
        super(errorMessage, params);
    }
}
