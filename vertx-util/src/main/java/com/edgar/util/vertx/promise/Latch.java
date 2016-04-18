package com.edgar.util.vertx.promise;

/**
 * 异步事件的工具类.
 *
 * @author truelove@cyngn.com (Jeremy Truelove) 10/15/14
 */
public class Latch {
  /**
   * 异步请求完成后的回调函数
   */
  private Action onComplete;

  /**
   * 异步请求的请求数量
   */
  private int count;

  /**
   * 已经执行的异步请求数量
   */
  private int currentCount;

  /**
   * @param count      需要完成的异步请求数量
   * @param onComplete 异步请求完成后的回调函数
   */
  public Latch(int count, Action onComplete) {
    validateParams(count, onComplete);
    this.count = count;
    currentCount = 0;
    this.onComplete = onComplete;
  }

  /**
   * 触发完成事件.
   * 当最后一个请求完成之后会触发回调函数。
   *
   * @throws IllegalStateException 如果所有的请求都已经完成，抛出异常.
   */
  public void complete() {
    if (currentCount == count) {
      throw new IllegalStateException("Latch has already been completed.");
    }
    currentCount++;
    if (currentCount == count) {
      onComplete.callback();
    }
  }

  /**
   * 重置latch
   */
  public void reset() {
    reset(count, onComplete);
  }

  /**
   * 使用一个新的请求数重置latch.
   *
   * @param count 新的请求数
   */
  public void reset(int count) {
    reset(count, onComplete);
  }

  /**
   * 使用新的请求数和回调函数重置latch
   *
   * @param count      新的请求数
   * @param onComplete 新的回调函数
   */
  public void reset(int count, Action onComplete) {
    validateParams(count, onComplete);
    currentCount = 0;
    this.count = count;
    this.onComplete = onComplete;
  }

  private void validateParams(int count, Action onComplete) {
    if (count < 1) {
      throw new IllegalArgumentException("Count must be greater than 0");
    }
    if (onComplete == null) {
      throw new IllegalArgumentException("Cannot set a null callback for complete");
    }
  }
}