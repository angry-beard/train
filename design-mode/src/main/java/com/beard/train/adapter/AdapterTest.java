package com.beard.train.adapter;

import com.beard.train.adapter.objectadapter.AC220;
import com.beard.train.adapter.objectadapter.PowerAdapter;

public class AdapterTest {

    public static void main(String[] args) {
        PowerAdapter powerAdapter = new PowerAdapter(new AC220());
        powerAdapter.output5V();
    }
}
