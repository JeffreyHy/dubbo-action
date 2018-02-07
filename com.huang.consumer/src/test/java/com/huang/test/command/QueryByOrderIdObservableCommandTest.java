package com.huang.test.command;

import com.huang.command.QueryByOrderIdObservableCommand;
import com.huang.provider.OrderServiceProvider;
import com.huang.test.AbstractTestCase;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Observer;

import javax.annotation.Resource;

/**
 * @author huangyongbo
 * @date Created by  2018/2/2 14:14
 */
public class QueryByOrderIdObservableCommandTest extends AbstractTestCase {
    private final static Logger logger = LoggerFactory.getLogger(QueryByOrderIdObservableCommandTest.class);
    @Resource
    private OrderServiceProvider orderServiceProvider;

    @Test
    public void testObserveCommand() throws InterruptedException {
        orderServiceProvider.reset();
        int i = 1;
        for (; i < 15; i++) {
            final String count = String.valueOf(i);
            Observable<Integer> observable = new QueryByOrderIdObservableCommand(orderServiceProvider).observe();
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

        //等待8s，使得熔断器进入半打开状态
        Thread.sleep(8000);

        for (; i < 20; i++) {
            final String count = String.valueOf(i);
            Observable<Integer> observable = new QueryByOrderIdObservableCommand(orderServiceProvider).observe();
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

    }

    @Test
    public void testToObservableCommand() throws InterruptedException {
        orderServiceProvider.reset();
        int i = 1;
        for (; i < 15; i++) {
            final String count = String.valueOf(i);
            new QueryByOrderIdObservableCommand(orderServiceProvider).toObservable()
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

        //等待6s，使得熔断器进入半打开状态
        Thread.sleep(6000);

        for (; i < 20; i++) {
            final String count = String.valueOf(i);
            new QueryByOrderIdObservableCommand(orderServiceProvider).toObservable()
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

    }
}
