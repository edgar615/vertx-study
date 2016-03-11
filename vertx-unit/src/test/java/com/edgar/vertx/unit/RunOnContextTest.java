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

  /**
   * By default the thread invoking the test methods is the JUnit thread. The RunTestOnContext
   * JUnit rule can be used to alter this behavior for running these test methods with a Vert.x
   * event loop thread.
   * <p>
   * Thus there must be some care when state is shared between test methods and Vert.x handlers
   * as they won’t be on the same thread, e.g incrementing a counter in a Vert.x handler and
   * asserting the counter in the test method. One way to solve this is to use proper
   * synchronization, another is to execute test methods on a Vert.x context that will be
   * propagated to the created handlers.
   */

  /*
* This rule wraps the junit calls in a Vert.x context, the Vert.x instance can be created by the
* rule or provided like in this case.
*/

  /**
   * keep in mind that you cannot block the event loop when using this rule. Usage of classes
   * like CountDownLatch or similar classes must be done with care.
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
