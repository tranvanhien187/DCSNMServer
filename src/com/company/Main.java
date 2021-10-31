package com.company;

public class Main {
    public static void main(String[] args) {
        int[] a = new int[]{1,2,3,4,5,6,7,8,9,0};
        int[] b = a.clone();
        b[0] = 10;
        System.out.println(a[0]+"   "+b[0]);

    }
}
