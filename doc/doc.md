VertxThread继承自FastThreadLocalThread
execStart属性记录线程启动时的时间

VertxThreadFactory实现ThreadFactory
内部使用了一个WeakHashMap来保存VertxThread实例，这样当VertxThread不再被正常使用时，会删除这个键

BlockedThreadChecker检测线程是否阻塞
内部有一个WeakHashMap保存VertxThread实例，同时有一个timer定时检查每个VertxThread的运行时间，如果线程运行的时间超过的规定时间会打印异常日志


AsyncResult异步任务的结果：成功，异常，是否成功，是否失败

Future继承AsyncResult，封装了一些常用方法,可以设置完成后的handler，有一个实现类FutureImpl,Future通过FutureFactory的工厂方法创建.
我们可以为每个Future设置一个handler，将任务完成之后，会调用handler来处理。

Context保存了handler执行的上下文环境

ContextImpl
	runOnContext()


Vertx接口

You use an instance of this class for functionality including:

 * Creating TCP clients and servers
 * Creating HTTP clients and servers
 * Creating DNS clients
 * Creating Datagram sockets
 * Setting and cancelling periodic and one-shot timers
 * Getting a reference to the event bus API
 * Getting a reference to the file system API
 * Getting a reference to the shared data API
 * Deploying and undeploying verticles

VertxInternal继承自Vertx，封装了一些特殊的接口，这个接口不是公开的接口，不建议用户使用

VertxImpl 实现VertxInternal接口，在创建时会检查当前线程，如果已经存在context上下文，会抛出异常
创建BlockedThreadChecker、
ThreadFactory eventLoopThreadFactory=VertxThreadFactory
ExecutorService workerPool=固定容量的线程池，默认值为20
ExecutorService internalBlockingPool=固定容量的线程池,默认值为20
OrderedExecutorFactory workerOrderedFact使用workerPool的顺序线程池工厂
OrderedExecutorFactory internalOrderedFact使用internalBlockingPool的顺序线程池工厂
FileResolver fileResolver
DeploymentManager deploymentManager
VertxMetrics metrics
NioEventLoopGroup eventLoopGroup
NioEventLoopGroup acceptorEventLoopGroup;
ClusterManager clusterManager
FileSystem fileSystem
HAManager haManager;
EventBus eventBus
boolean closed
SharedData sharedData;

VertxOptions
 
OrderedExecutorFactory，OrderedExecutor的工厂类

OrderedExecutor，按照顺序执行的线程池,线程池将任务委托给了另一个线程池，这个线程池可以在构造时指定,他在内部使用了一个LinkedList，并创建了一个Runnable对象来委托给线程池来执行。
这个Runnable对象从队列中按顺序读取任务并执行,**对Runnable的操作需要考虑线程安全**
OrderedExecutor内部还有个布尔值running，用来表示该线程池是否正在运行。在执行任务时，如果线程池没有运行，会创建一个Runnable对象来委托给线程池执行。如果队列中不再有任务，running会被设为false


Deployment 部署
VerticleFactory，创建Verticle的工厂类
JavaVerticleFactory的createVerticle会通过classloader.loadClass方法读取Class对象，并通过class.newIntance创建一个Verticle对象,
doDeploy方法会为为每个Verticle创建上下文,workerVerticle通过vertx.createWorkerContext方法创建上下文，Verticle通过vertx.createEventLoopContext创建上下文；建立Deployment的父子关系;包括部署结果

undeploy方法

VertxMetrics 性能

HttpServer

HttpClient

EventBus

SharedData

FileResolver

FileSystem

CompilingClassLoader

## stream
StreamBase

ReadStream

## HTTP
**keepAlive** 

http://www.nowamagic.net/academy/detail/23350305

为什么要使用KeepAlive？
 
终极的原因就是需要加快客户端和服务端的访问请求速度。KeepAlive就是浏览器和服务端之间保持长连接，这个连接是可以复用的。当客户端发送一次请求，收到相应以后，第二次就不需要再重新建立连接（慢启动的过程）,就可以直接使用这次的连接来发送请求了。在HTTP1.0及各种加强版中，是默认关闭KeepAlive的，而在HTTP1.1中是默认打开的。
HTTP头是Connection: Keep-Alive

要设置保持多少时间和连接使用：
Keep-alive: 300 

KeepAlive是不是设置越长越好？

并不是这样的。KeepAlive在增加访问效率的同时，也会增加服务器的压力。对于静态文件是会提高其访问性能，但是对于一些动态请求，如果在一次和下一次的请求过程中占用了服务器的资源，则会导致意想不到的结果。

**HTTP管线化(HTTP pipelining)** 

http://blog.csdn.net/dongzhiquan/article/details/6114040

http://www.jdon.com/dl/http2.html

 HTTP管线化是将多个HTTP要求（request）整批提交的技术，而在传送过程中不需先等待服务端的回应。管线化机制须通过永久连接（persistent connection）完成，仅HTTP/1.1支持此技术（HTTP/1.0不支持），并且只有GET和HEAD要求可以进行管线化，而POST则有所限制。此外，初次创建连接时也不应启动管线机制，因为对方（服务器）不一定支持HTTP/1.1版本的协议。

 浏览器将HTTP要求大批提交可大幅缩短页面的加载时间，特别是在传输延迟（lag/latency）较高的情况下（如卫星连接）。此技术之关键在于多个HTTP的要求消息可以同时塞入一个TCP分组中，所以只提交一个分组即可同时发出多个要求，借此可减少网络上多余的分组并降低线路负载。

HTTP Pipelining是一种这样的技术，当在等待上一个请求响应的时候发送另外一个请求，这好像你走入了超市的收银台前排队队列，你无法确切知道排在你前面的人能够快速付款购买完成，还是发生一堆恼人的烦事在那里纠缠不清，这就是堵塞的源头。所以，你得仔细地选择一个排队队伍，有时你相信你选择了一个正确的队伍，能够快速轮到你，但是有时你不得不在几个队伍之间切换排队。创建一个新的排队是有性能和资源损耗的，而且不是可扩展的，这并没有完美解决方案.

开启HTTP管线化，必须开启keepAlive


Netty里的
ResourceLeakDetector

