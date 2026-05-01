package com.finstream360.clearing.engine;

import com.finstream360.clearing.domain.enums.ClearingStatus;
import org.springframework.stereotype.Component;

@Component
public class ClearingStateMachine {

    public ClearingStatus next(ClearingStatus current) {
        switch (current) {
            case RECEIVED: return ClearingStatus.NETTED;
            case NETTED: return ClearingStatus.MARGIN_CALCULATED;
            case MARGIN_CALCULATED: return ClearingStatus.CLEARED;
            default: throw new ClearingException("Invalid state");
        }
    }
}

