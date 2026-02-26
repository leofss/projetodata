package com.leonardo.projetodata.exception;

import com.leonardo.projetodata.enumerator.ErrorMessage;

public class RawMaterialNotFound extends BaseCustomException{
    public RawMaterialNotFound(ErrorMessage errorMessage, Object... params) {
        super(errorMessage, params);
    }
}
