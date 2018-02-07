package com.huang.test.command;

import com.huang.command.QueryByOrderIdCommand;
import com.huang.provider.OrderServiceProvider;
import com.huang.test.AbstractTestCase;
import com.netflix.hystrix.HystrixCommand;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Observer;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by JeffreyHy on 2018/1/18.
 */
public class QueryByOrderIdCommandTest extends AbstractTestCase {
    private final static Logger logger = LoggerFactory.getLogger(QueryByOrderIdCommandTest.class);
    @Resource
    private OrderServiceProvider orderServiceProvider;

    /**
     * 使用自定义Command处理服务熔断与降级
     */
    @Test
    public void testExecuteCommand() throws InterruptedException {
        orderServiceProvider.reset();
        int i = 1;
        for (; i < 15; i++) {
            HystrixCommand<Integer> command = new QueryByOrderIdCommand(orderServiceProvider);
            Integer r = command.execute();
            String method = r == -1 ? "fallback" : "run";
            logger.info("call {} times,result:{},method:{},isCircuitBreakerOpen:{}", i, r, method, command.isCircuitBreakerOpen());
        }
        //等待6s，使得熔断器进入半打开状态
        Thread.sleep(6000);
        for (; i < 20; i++) {
            HystrixCommand<Integer> command = new QueryByOrderIdCommand(orderServiceProvider);
            Integer r = command.execute();
            String method = r == -1 ? "fallback" : "run";
            logger.info("call {} times,result:{},method:{},isCircuitBreakerOpen:{}", i, r, method, command.isCircuitBreakerOpen());
        }
    }

    @Test
    public void testQueueCommand() throws ExecutionException, InterruptedException {
        orderServiceProvider.reset();
        int i = 1;
        for (; i < 15; i++) {
            Future<Integer> future = new QueryByOrderIdCommand(orderServiceProvider).queue();
            logger.info("call {} times,result:{}", i, future.get());
        }

        //等待6s，使得熔断器进入半打开状态
        Thread.sleep(6000);

        for (; i < 20; i++) {
            Future<Integer> future = new QueryByOrderIdCommand(orderServiceProvider).queue();
            logger.info("call {} times,result:{}", i, future.get());
        }

    }

    @Test
    public void testObserveCommand() throws InterruptedException {
        orderServiceProvider.reset();
        int i = 1;
        for (; i < 15; i++) {
            final String count = String.valueOf(i);
            Observable<Integer> observable = new QueryByOrderIdCommand(orderServiceProvider).observe();
            observable.subscribe(new Observer<Integer>() {
                @Override
                public void onCompleted() {
                    logger.info("call {} times,onCompleted", count);
                }

                @Override
                public void onError(Throwable e) {
                    logger.error("cause:{}", e.getCause());
                }

                @Override
                public void onNext(Integer integer) {
                    logger.info("call {} times,result:{}", count, integer);
                }
            });
        }

        //等待10s，使得熔断器进入半打开状态
        Thread.sleep(10000);

        for (; i < 20; i++) {
            final String count = String.valueOf(i);
            Observable<Integer> observable = new QueryByOrderIdCommand(orderServiceProvider).observe();
            observable.subscribe(new Observer<Integer>() {
                @Override
                public void onCompleted() {
                    logger.info("call {} times,onCompleted", count);
                }

                @Override
                public void onError(Throwable e) {
                    logger.error("cause:{}", e.getCause());
                }

                @Override
                public void onNext(Integer integer) {
                    logger.info("call {} times,result:{}", count, integer);
                }
            });
        }
        //等待10s,等待子线程完成再退出main
        Thread.sleep(2000);
    }

    @Test
    public void testToObservableCommand() throws InterruptedException {
        orderServiceProvider.reset();
        int i = 1;
        for (; i < 15; i++) {
            final String count = String.valueOf(i);
            new QueryByOrderIdCommand(orderServiceProvider).toObservable()
                    .subscribe(new Observer<Integer>() {
                        @Override
                        public void onCompleted() {
                            logger.info("call {} times,onCompleted", count);
                        }

                        @Override
                        public void onError(Throwable e) {
                            logger.error("cause:{}", e.getCause());
                        }

                        @Override
                        public void onNext(Integer integer) {
                            logger.info("call {} times,result:{}", count, integer);
                        }
                    });

        }

        //等待10s，使得熔断器进入半打开状态
        Thread.sleep(10000);

        for (; i < 20; i++) {
            final String count = String.valueOf(i);
            new QueryByOrderIdCommand(orderServiceProvider).toObservable()
                    .subscribe(new Observer<Integer>() {
                        @Override
                        public void onCompleted() {
                            logger.info("call {} times,onCompleted", count);
                        }

                        @Override
                        public void onError(Throwable e) {
                            logger.error("cause:{}", e.getCause());
                        }

                        @Override
                        public void onNext(Integer integer) {
                            logger.info("call {} times,result:{}", count, integer);
                        }
                    });
        }
        //等待10s,等待子线程完成再退出main
        Thread.sleep(2000);
    }
}
