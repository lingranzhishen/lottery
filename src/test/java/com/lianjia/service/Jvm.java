package com.lianjia.service;

import com.dhu.common.util.StringUtil;
import com.dhu.lottery.model.LotteryRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class Jvm {
    private String a = null;
    private int i = 0;

    public void getA() {
        System.out.println(i++);
    }

    static class run implements Runnable {
        private Jvm jvm;
        private CountDownLatch latch;
        run(Jvm jvm,CountDownLatch latch) {
            this.jvm = jvm;
            this.latch=latch;
        }

        @Override
        public void run() {
            jvm.getA();
            latch.countDown();

        }
    }

    public static void main(String[] args)  {

    }
}
