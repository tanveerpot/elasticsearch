// Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
// or more contributor license agreements. Licensed under the Elastic License
// 2.0; you may not use this file except in compliance with the Elastic License
// 2.0.
package org.elasticsearch.xpack.esql.expression.function.scalar.math;

import java.lang.Override;
import java.lang.String;
import org.elasticsearch.compute.data.Block;
import org.elasticsearch.compute.data.LongBlock;
import org.elasticsearch.compute.data.LongVector;
import org.elasticsearch.compute.data.Page;
import org.elasticsearch.compute.operator.DriverContext;
import org.elasticsearch.compute.operator.EvalOperator;
import org.elasticsearch.core.Releasables;

/**
 * {@link EvalOperator.ExpressionEvaluator} implementation for {@link Abs}.
 * This class is generated. Do not edit it.
 */
public final class AbsLongEvaluator implements EvalOperator.ExpressionEvaluator {
  private final EvalOperator.ExpressionEvaluator fieldVal;

  private final DriverContext driverContext;

  public AbsLongEvaluator(EvalOperator.ExpressionEvaluator fieldVal, DriverContext driverContext) {
    this.fieldVal = fieldVal;
    this.driverContext = driverContext;
  }

  @Override
  public Block eval(Page page) {
    try (LongBlock fieldValBlock = (LongBlock) fieldVal.eval(page)) {
      LongVector fieldValVector = fieldValBlock.asVector();
      if (fieldValVector == null) {
        return eval(page.getPositionCount(), fieldValBlock);
      }
      return eval(page.getPositionCount(), fieldValVector).asBlock();
    }
  }

  public LongBlock eval(int positionCount, LongBlock fieldValBlock) {
    try(LongBlock.Builder result = driverContext.blockFactory().newLongBlockBuilder(positionCount)) {
      position: for (int p = 0; p < positionCount; p++) {
        if (fieldValBlock.isNull(p) || fieldValBlock.getValueCount(p) != 1) {
          result.appendNull();
          continue position;
        }
        result.appendLong(Abs.process(fieldValBlock.getLong(fieldValBlock.getFirstValueIndex(p))));
      }
      return result.build();
    }
  }

  public LongVector eval(int positionCount, LongVector fieldValVector) {
    try(LongVector.Builder result = driverContext.blockFactory().newLongVectorBuilder(positionCount)) {
      position: for (int p = 0; p < positionCount; p++) {
        result.appendLong(Abs.process(fieldValVector.getLong(p)));
      }
      return result.build();
    }
  }

  @Override
  public String toString() {
    return "AbsLongEvaluator[" + "fieldVal=" + fieldVal + "]";
  }

  @Override
  public void close() {
    Releasables.closeExpectNoException(fieldVal);
  }

  static class Factory implements EvalOperator.ExpressionEvaluator.Factory {
    private final EvalOperator.ExpressionEvaluator.Factory fieldVal;

    public Factory(EvalOperator.ExpressionEvaluator.Factory fieldVal) {
      this.fieldVal = fieldVal;
    }

    @Override
    public AbsLongEvaluator get(DriverContext context) {
      return new AbsLongEvaluator(fieldVal.get(context), context);
    }

    @Override
    public String toString() {
      return "AbsLongEvaluator[" + "fieldVal=" + fieldVal + "]";
    }
  }
}
