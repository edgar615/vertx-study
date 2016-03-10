package com.edgar.vertx.unit;

import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Edgar on 2016/3/10.
 *
 * @author Edgar  Date 2016/3/10
 */
@RunWith(VertxUnitRunner.class)
public class AssertTest {

  @Test
  public void testEquals(TestContext context) {
    context.assertEquals(10, 10);
    context.assertEquals(10, 11, "Should have been 10 instead of " + 11);
  }

  @Test
  public void testNotEquals(TestContext context) {
    context.assertNotEquals(10, 11);
  }

  @Test
  public void testNull(TestContext context) {
    context.assertNull(null);
    context.assertNotNull(new Object());
  }

  @Test
  public void testRange(TestContext context) {
    //TODO
    // Assert that 0.1 is equals to 0.2 +/- 0.5
    context.assertInRange(0.1, 0.2, 0.5);
  }
  @Test
   public void testTrue(TestContext context) {
    context.assertTrue(true);
    context.assertFalse(1 > 2);
  }

  @Test
  public void testFail(TestContext context) {
    context.fail();
  }

}
