package com.ec.tecnologia.utils;

import java.util.ArrayList;

public class hksf {
    public static void main(String[] args) {

        int[] nums = {1,3,5,6};

        System.out.println(searchInsert(nums, 2));
    }

    public static int searchInsert(int[] nums, int target) {

        int output = 0;

        for(int i=0; i<=nums.length; i++){

            if(nums[i] < target && nums[i+1] > target){
                if(i==nums.length){
                    output = i+1;
                }else{
                    output = i;
                }
            }
        }

        return output;

    }
}

