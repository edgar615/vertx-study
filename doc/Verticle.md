start方法
Verticle部署的时候会调用start方法

stop方法
Verticle取消部署的时候会调用stop方法

Verticle部署的时候会创建一个Context,和vertx一起存入Verticle



There are three different types of verticles:

Standard Verticles

    These are the most common and useful type - they are always executed using an event loop thread. We’ll discuss this more in the next section.
Worker Verticles

    These run using a thread from the worker pool. An instance is never executed concurrently by more than one thread.
Multi-threaded worker verticles

    These run using a thread from the worker pool. An instance can be executed concurrently by more than one thread