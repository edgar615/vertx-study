## EventBus
EventBus
 send()只有一个消费者收到
 publish()所有的消费者都收到
 consumer()消费事件
localConsumer()只处理本地事件，不会传播到集群
registerCodec注册编解码类
unregisterCodec注销编解码类的注册
start()
close()
addInterceptor()添加拦截器
removeInterceptor()删除拦截器

EventBusImpl
使用一个CopyOnWriteArrayList来保存拦截器
AtomicLong replySequence，用来生成replyAddress
CodecManager codecManager
ConcurrentMap<String, Handlers> handlerMap

send和publish方法会调用replySequence来生成一个replyAddress
如果是send，eventbus会采用轮询的方式选择一个consumer来处理。
如果是publish，eventbus会遍历所有的consumer来处理

consumer会封装为handlerRegistration来处理


HandlerRegistration

HandlerHolder


SendContext

Message
MessageImpl是Message的实现类，里面主要包括3个参数，sentBody,receivedBody,messageCodec。其中receivedBody是通过messageCodec.transfrom(sentBody)得到
message的reply方法会调用eventbus的sendReply方法发送响应数据

ClusteredMessage

ClusteredEventBus

MessageConsumer

MessageProducer


EventBusMetrics
