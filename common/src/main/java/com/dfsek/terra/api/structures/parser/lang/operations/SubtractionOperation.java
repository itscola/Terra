package com.dfsek.terra.api.structures.parser.lang.operations;

import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.tokenizer.Position;

public class SubtractionOperation extends BinaryOperation<Number, Number> {
    public SubtractionOperation(Returnable<Number> left, Returnable<Number> right, Position position) {
        super(left, right, position);
    }

    @Override
    public Number apply(Number left, Number right) {
        return left.doubleValue() - right.doubleValue();
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.NUMBER;
    }
}
