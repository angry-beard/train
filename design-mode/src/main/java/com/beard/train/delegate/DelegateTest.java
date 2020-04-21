package com.beard.train.delegate;

public class DelegateTest {

    public static void main(String[] args) {
        new Boss().command("爬虫", new Leader());
        new Boss().command("海报", new Leader());
        new Boss().command("卖萌", new Leader());
    }
}
