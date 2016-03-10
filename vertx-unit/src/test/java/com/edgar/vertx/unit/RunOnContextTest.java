package com.edgar.vertx.unit;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Edgar on 2016/3/10.
 *
 * @author Edgar  Date 2016/3/10
 */
@RunWith(VertxUnitRunner.class)
public class RunOnContextTest {

  /*
* This rule wraps the junit calls in a Vert.x context, the Vert.x instance can be created by the
* rule or provided like in this case.
*/
  @Rule
  public final RunTestOnContext rule = new RunTestOnContext(Vertx::vertx);

  private Thread thread;

  @Before
  public void before(TestContext context) {
    context.assertTrue(Context.isOnEventLoopThread());
    thread = Thread.currentThread();
  }

  @Test
  public void theTest(TestContext context) {
    context.assertTrue(Context.isOnEventLoopThread());
    context.assertEquals(thread, Thread.currentThread());
  }

  @After
  public void after(TestContext context) {
    context.assertTrue(Context.isOnEventLoopThread());
    context.assertEquals(thread, Thread.currentThread());
  }

}
