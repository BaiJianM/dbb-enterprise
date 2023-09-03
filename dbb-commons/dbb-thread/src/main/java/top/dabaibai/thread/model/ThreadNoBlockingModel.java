package top.dabaibai.thread.model;

import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author: 白剑民
 * @dateTime: 2023/5/9 21:25
 */
@Slf4j
public class ThreadNoBlockingModel {

    public class Producer implements Runnable {

        private Task task;

        public Producer(Task task) {
            this.task = task;
        }

        @Override
        public void run() {

        }
    }

    public class Consumer implements Runnable {
        @Override
        public void run() {

        }
    }

}
