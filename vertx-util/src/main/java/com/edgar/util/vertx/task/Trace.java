package com.edgar.util.vertx.task;

/**
 * 日志类.
 *
 * @author Edgar  Date 2016/5/9
 */
public interface Trace {

  /**
   * 日志名称
   *
   * @return
   */
  String name();

  /**
   * 开始时间，精确到毫秒
   *
   * @return
   */
  long startMills();

  /**
   * 结束时间，精确到毫秒
   *
   * @return
   */
  long endMills();

  /**
   * 耗时
   *
   * @return 精确到毫秒
   */
  long elapsed();

  /**
   * 成功
   */
  void success();

  /**
   * 失败
   */
  void fail();

  /**
   * 日志
   *
   * @return
   */
  String trace();

  static Trace create(String name) {
    return new TraceImpl(name);
  }
}
