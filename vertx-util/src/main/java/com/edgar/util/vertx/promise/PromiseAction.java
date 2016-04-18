package com.edgar.util.vertx.promise;

import io.vertx.core.json.JsonObject;

import java.util.function.Consumer;

/**
 * 异步请求
 */
public interface PromiseAction {
  /**
   * 异步请求的实际操作.
   *
   * @param context  异步请求的上下文环境
   * @param onResult the callback that collects the result of any given PromiseAction necessary for
   *                 continuing or completing the chain of actions in a promise.
   */
  void execute(JsonObject context, Consumer<Boolean> onResult);
}