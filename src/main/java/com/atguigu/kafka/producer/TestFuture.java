package com.atguigu.kafka.producer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @Author lw
 * @Create2020-03-29 18:35
 */
public class TestFuture {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService executor = Executors.newCachedThreadPool();

        Future<?> future = executor.submit(new Runnable() {
            public void run() {
                for (int i = 0; i < 100; i++) {
                    System.out.println("i=" + i);
                }
            }
        });

        future.get();//阻塞
        System.out.println("===============");

        executor.shutdown();

    }
}
