package com.leonardo.projetodata.exception;

import com.leonardo.projetodata.enumerator.ErrorMessage;

public class DuplicateRawMaterialInProduct extends BaseCustomException{
    public DuplicateRawMaterialInProduct(ErrorMessage errorMessage, Object... params) {
        super(errorMessage, params);
    }
}
