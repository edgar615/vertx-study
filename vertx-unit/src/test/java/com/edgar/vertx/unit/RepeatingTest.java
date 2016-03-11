package com.edgar.vertx.unit;

import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.Repeat;
import io.vertx.ext.unit.junit.RepeatRule;
import io.vertx.ext.unit.junit.Timeout;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

@RunWith(VertxUnitRunner.class)
public class RepeatingTest {

  @Rule
  public Timeout rule = Timeout.millis(70);

//  @Test(timeout = 1000l)
  @Test
  public void testSomething(TestContext context) throws InterruptedException {
    // This will be executed 1000 times
    TimeUnit.MILLISECONDS.sleep(100);
  }
}