package com.edgar.vertx.unit;

import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.Repeat;
import io.vertx.ext.unit.junit.RepeatRule;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class TimeOutTest {

  @Rule
  public RepeatRule rule = new RepeatRule();

  @Repeat(1000)
  @Test
  public void testSomething(TestContext context) {
    // This will be executed 1000 times
    System.out.println("repeat");
  }
}