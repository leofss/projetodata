package com.leonardo.projetodata.exception;

import com.leonardo.projetodata.enumerator.ErrorMessage;

public class RawMaterialCodeNotUnique extends BaseCustomException{
    public RawMaterialCodeNotUnique(ErrorMessage errorMessage, Object... params) {
        super(errorMessage, params);
    }
}
