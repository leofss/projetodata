package com.leonardo.projetodata.exception;

import com.leonardo.projetodata.enumerator.ErrorMessage;

public class ProductNotFound extends BaseCustomException{

    public ProductNotFound(ErrorMessage errorMessage, Object... params) {
        super(errorMessage, params);
    }
}
