package com.edgar.util.vertx.task;

/**
 * Created by Edgar on 2016/5/6.
 *
 * @author Edgar  Date 2016/5/6
 */
class TraceImpl implements Trace {
  /**
   * 开始时间，精确到微秒
   */
  private final long startMills = System.currentTimeMillis();

  /**
   * 名称
   */
  private final String name;

  /**
   * 结束时间，警情到微秒
   */
  private long endMills;

  /**
   * 耗时endMills - startMills
   */
  private long elapsed;

  private boolean failed;

  private boolean succeeded;

  TraceImpl(String name) {
    this.name = name;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public long startMills() {
    return startMills;
  }

  @Override
  public long endMills() {
    return endMills;
  }

  public void setEndMills(long endMills) {
    this.endMills = endMills;
  }

  @Override
  public long elapsed() {
    return elapsed;
  }

  public void setElapsed(long elapsed) {
    this.elapsed = elapsed;
  }

  @Override
  public void success() {
    succeeded = true;
    failed = false;
    end();
  }

  @Override
  public void fail() {
    failed = true;
    succeeded = false;
    end();
  }

  @Override
  public String trace() {
    return String.format("{name:%s,succeeded:%s,"
                         + "startMills:%s,"
                         + "endMills:%s,elapsed:%s}",
                         name,
                         succeeded,
                         startMills,
                         endMills,
                         elapsed);
  }

  @Override
  public String toString() {
    return trace();
  }

  private void end() {
    this.endMills = System.currentTimeMillis();
    this.elapsed = this.endMills - this.startMills;
  }
}
