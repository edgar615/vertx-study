package com.edgar.util.vertx.task;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;

/**
 * Created by Edgar on 2016/7/21.
 *
 * @author Edgar  Date 2016/7/21
 */
public class Tuple2TaskImpl<T1, T2> extends BaseTask<Tuple2<T1, T2>> implements Tuple2Task<T1, T2> {

    Tuple2TaskImpl(Future<T1> futureT1, Future<T2> futureT2) {
        super(null, Future.<Tuple2<T1, T2>>future());
//        task = Task.create(Future.<Tuple2<T1, T2>>future());
        CompositeFuture compositeFuture = CompositeFuture.all(futureT1, futureT2);
        compositeFuture.setHandler(ar -> {
            if (ar.succeeded()) {
                T1 t1 = ar.result().result(0);
                T2 t2 = ar.result().result(1);
                complete(Tuple2.create(t1, t2));
            } else {
                fail(ar.cause());
            }
        });
    }

//    @Override
//    public <R> Task<R> flatMap(String desc, Consumer3<T1, T2, Task<R>> consumer) {
////        return task.flatMap(desc, (t, rTask) -> consumer.accept(t.getT1(), t.getT2(), rTask));
//        return null;
//    }

//    @Override
//    public <R> Task<R> flatMap(String desc, BiFunction<T1, T2, Future<R>> function) {
////        return task.flatMap(desc, (t, rTask) -> {
////            Future<R> future = function.apply(t.getT1(), t.getT2());
//////            rTask.completer(future);
////        });
//        return null;
//    }


//    @Override
//    public <R> Task<R> map(String desc, Function<Tuple2<T1, T2>, R> function) {
//        return task.map(desc, t -> function.apply(t));
//    }
//
//    @Override
//    public Task<Tuple2<T1, T2>> andThen(String desc, Consumer<Tuple2<T1, T2>> consumer) {
//        return task.andThen(desc, t -> consumer.accept(t));
//    }

}
