**ConnectionManager**连接池
内部保存了一个ConnQueue的map对象，Map<TargetAddress, ConnQueue> connQueues = new ConcurrentHashMap<>();
调用getConnection获取连接的时候会从connQueues中找到地址所对应的连接池,没有则创建一个


**ConnQueue**同一个地址的连接池
内部有一个队列保存了可用的连接
Queue<ClientConnection> availableConnections = new ArrayDeque<>();
一个队列保存了等待的请求
Queue<Waiter> waiters = new ArrayDeque<>();

getConnection方法
getConnection(Handler<ClientConnection> handler, Handler<Throwable> connectionExceptionHandler,ContextImpl context, BooleanSupplier canceled)
会从availableConnections队列中取出一个Connection并放入context中运行context.runOnContext(v -> handler.handle(conn));，如果从队列中没有取到Connection则需要创建一个新的连接。1）当前连接数connCount小于最大连接数maxSockets，创建一个新的连接；2）connCount == maxSockets时，如果等待获取连接的数量小于最大的等待数量，则加入一个等待队列,否则抛出异常

requestEnded方法，如果请求是pipelining的，则从等待队列中取出一个waiter，并在context中运行，达到重用连接的目的waiter.context.runOnContext(v -> waiter.handler.handle(conn));

responseEnded方法，如果请求是pipelining而且keepalive的，则从等待队列中取出一个waiter，并在context中运行，达到重用连接的目的waiter.context.runOnContext(v -> waiter.handler.handle(conn));如果没有waiter且!pipelining || conn.getOutstandingRequestCount() == 0，则将连接放入availableConnections中重用,
如果请求不是是pipelining或者keepalive的，关闭连接

connectionClosed方法,请求关闭时从allConnections和availableConnections中删除连接。
如果等待队列中有waiter，创建一个新的连接。如果connCount==0，将ConnQueue从ConnectionManager中删除


**ConnectionBase**tcp连接的基础类**此处依赖netty部分未看懂**