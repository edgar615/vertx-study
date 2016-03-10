package com.edgar.vertx.unit;

import io.vertx.ext.unit.TestOptions;
import io.vertx.ext.unit.TestSuite;
import io.vertx.ext.unit.report.ReportOptions;
import org.junit.Test;

/**
 * Created by Edgar on 2016/3/10.
 *
 * @author Edgar  Date 2016/3/10
 */
public class TestSuiteTest {

  @Test
  public void run() {
    TestOptions testOptions = new TestOptions().addReporter(new ReportOptions().setTo("console"));
    TestSuite testSuite = TestSuite.create("the_test_suite");
    testSuite.test("my_test_suite", context -> {
      String value = "hello";
      context.assertEquals("hello", value);
    });
    //The run method will execute the suite and go through all the tests of the suite.
    testSuite.run(testOptions);
  }

  @Test
  public void fluent() {
    TestOptions testOptions = new TestOptions().addReporter(new ReportOptions().setTo("console"));
    TestSuite testSuite = TestSuite.create("the_test_suite");
    testSuite.test("my_test_suite", context -> {
      String value = "hello";
      context.assertEquals("hello", value);
    }).test("my_test_suite2", context -> {
      String value = "hello2";
      context.assertEquals("hello", value);
    }).test("my_test_suite3", context -> {
      String value = "hello";
      context.assertEquals("hello", value);
    });
//    The test cases declaration order is not guaranteed, so test cases should not rely on the
// execution of another test case to run. Such practice is considered as a bad one.
    testSuite.run(testOptions);
  }

  @Test
  public void beforeAfter() {
    TestOptions testOptions = new TestOptions().addReporter(new ReportOptions().setTo("console"));
    TestSuite testSuite = TestSuite.create("the_test_suite");
    testSuite.before(context -> System.out.println("before")).test("my_test_suite", context -> {
      String value = "hello";
      context.assertEquals("hello", value);
    }).test("my_test_suite2", context -> {
      String value = "hello2";
      context.assertEquals("hello", value);
    }).test("my_test_suite3", context -> {
      String value = "hello";
      context.assertEquals("hello", value);
    }).after(context -> System.out.println("after"));
//    The before callback is executed before any tests, when it fails, the test suite execution
// will stop and the failure is reported. The after callback is the last callback executed by the
// testsuite, unless the before callback reporter a failure.
    testSuite.run(testOptions);
  }

  @Test
  public void beforeAfterEach() {
    TestOptions testOptions = new TestOptions().addReporter(new ReportOptions().setTo("console"));
    TestSuite testSuite = TestSuite.create("the_test_suite");
    testSuite.beforeEach(context -> System.out.println("before")).test("my_test_suite", context -> {
      String value = "hello";
      context.assertEquals("hello", value);
    }).test("my_test_suite2", context -> {
      String value = "hello2";
      context.assertEquals("hello", value);
    }).test("my_test_suite3", context -> {
      String value = "hello";
      context.assertEquals("hello", value);
    }).afterEach(context -> System.out.println("after"));
//    The beforeEach callback is executed before each test case, when it fails, the test case is
// not executed and the failure is reported. The afterEach callback is the executed just after
// the test case callback, unless the beforeEach callback reported a failure.
    testSuite.run(testOptions);
  }
}
