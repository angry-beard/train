package com.beard.train.delegate;

public class Boss {

    public void command(String task, Leader leader) {
        leader.doing(task);
    }
}
