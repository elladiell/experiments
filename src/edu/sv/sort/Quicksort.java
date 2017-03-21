package edu.sv.sort;

import java.util.Arrays;
import java.util.Random;

public class Quicksort {
    public static void main(String[] args) {
        int[] arr = {3, 5, 8, 0, 34, 1, 21, 17, 7};
        System.out.println(Arrays.toString(arr));
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    private static void sort(int[] arr) {
        sort(arr, 0, arr.length - 1);

    }

    private static void sort(int[] arr, int left, int right) {
        System.out.println("left = " + left + "   r = " + right);
        int idx = new Random().nextInt(right - left + 1) + left;
        System.out.println("idx = " + idx);
        int x = arr[idx];
        int i = left;
        int j = right;
        while (i <= j) {
            while (arr[i] < x) ++i;
            while (arr[j] > x) --j;
            if (i <= j) {
                int buf = arr[i];
                arr[i] = arr[j];
                arr[j] = buf;
                ++i;
                --j;
            }
        }
        if (left < j) sort(arr, left, j);
        if (i < right) sort(arr, i, right);
    }
}
