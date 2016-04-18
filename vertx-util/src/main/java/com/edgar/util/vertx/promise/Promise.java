package com.edgar.util.vertx.promise;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.function.Consumer;

/**
 * 一组异步请求.
 *
 * @author truelove@cyngn.com (Jeremy Truelove) 7/30/15
 */
public interface Promise {
  String CONTEXT_FAILURE_KEY = "failure";

  /**
   * 并行运行所有PromiseAction.
   *
   * @param actions 需要执行的PromiseAction
   * @return the promise representing the actions
   */
  Promise all(PromiseAction... actions);

  /**
   * 顺序执行所有的PromiseAction。
   *
   * @param actions 需要执行的PromiseAction
   * @return the promise representing the actions
   */
  Promise allInOrder(PromiseAction... actions);

  /**
   * 添加一个PromiseAction.
   *
   * @param action the action to execute
   * @return the promise representing the actions
   */
  Promise then(PromiseAction action);

  /**
   * 异常处理.
   *
   * @param onFailure 异常处理的回调函数
   * @return the promise representing the actions
   */
  Promise except(Consumer<JsonObject> onFailure);

  /**
   * 成功执行完毕后的处理.
   *
   * @param onComplete 成功完成后的回调
   * @return the promise representing the actions
   */
  Promise done(Consumer<JsonObject> onComplete);

  /**
   * 设置异步请求的超时时间
   *
   * @param time 超时时间
   * @return the promise representing the actions
   */
  Promise timeout(long time);

  /**
   * 请求是否成功.如果请求尚未完成，返回false;
   *
   * @return 如果请求成功，返回true，否则返回false
   */
  boolean succeeded();

  /**
   * Has the promise completed yet? Either by completing all tasks or failing to.
   *
   * @return true if all actions or done completing or the promise has failed.
   */
  boolean completed();

  /**
   * Called when you are ready to begin resolution of the promise chain.
   *
   * @return the promise you are evaluating
   */
  Promise eval();

  /**
   * If the promise has no actions in it
   *
   * @return true if the promise has no actions, false otherwise
   */
  boolean isEmpty();

  /**
   * Create a new promise.
   *
   * @param vertx the vertx instance to run it on
   * @return the newly created Promise
   */
  static Promise newInstance(Vertx vertx) {
    return new PromiseImpl(vertx);
  }
}