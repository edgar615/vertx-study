setPeriodic方法,周期性地执行任务，接受两个参数，第一个参数是周期，第二个参数是对timer的id处理的handler
long setPeriodic(long delay, Handler<Long> handler);

setTimer方法,延时执行任务，接受两个参数，第一个参数是延时时间，第二个参数是对timer的id处理的handler
long setTimer(long delay, Handler<Long> handler);

timerId是在VertxImpl类中通过一个AtomicInteger自增得到

setPeriodic和setTimer方法最终都会调用<code>scheduleTimeout(ContextImpl context, Handler<Long> handler, long delay, boolean periodic)</code>方法

scheduleTimeout方法会创建一个InternalTimerHandler对象<code>InternalTimerHandler task = new InternalTimerHandler(timerId, handler, periodic, delay, context);</code>
InternalTimerHandler实现了Handler接口InternalTimerHandler implements Handler<Void>, Closeable
在InternalTimerHandler的构造方法中，它将自己放入Context中运行.<code>Runnable toRun = () -> context.runOnContext(this);</code>
并通过EventLoop的scheduleAtFixedRate和schedule来分别时间定期任务和延时任务,
这两个方法会返回一个Future对象，所以<code> cancelTimer(long id)</code>可以通过调用future.cancel方法来取消任务.
close方法会在context关闭的时候执行.
如果是延时任务，在任务完成之后还需要将Vertx对象中保存的InternalTimerHandler对象删除

cancelTimer方法，会删除Vertx对象中保存的InternalTimerHandler对象

runOnContext方法，调用context的runOnContext方法
void runOnContext(Handler<Void> action);

