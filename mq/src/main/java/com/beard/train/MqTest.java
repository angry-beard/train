package com.beard.train;

import java.util.*;

public class MqTest {

    public static void main(String[] args) {
        int[] arr = new int[]{2, 2, 7, 11, 15, 1, 8,};
        System.out.println(twoSum(arr, 9));
    }

    public static List<Integer> twoSum(int[] nums, int target) {
        List<Integer> result = new ArrayList();
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    result.add(i);
                    result.add(j);
                    break;
                }
            }
        }
        return result;
    }
}
