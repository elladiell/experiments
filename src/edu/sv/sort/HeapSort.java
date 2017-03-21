package edu.sv.sort;

import java.util.Arrays;

/**
 * Max heap sort
 */
public class HeapSort {

    private static int heapSize = 0;
    private static int[] arr = {3, 5, 8, 0, 34, 1, 21, 17, 7};

    public static void main(String[] args) {
        System.out.println(Arrays.toString(arr));
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void sort(int[] a) {
        arr = a;
        heapifySiftDown();
        System.out.println(Arrays.toString(arr));

        for (int i = 0; i < arr.length - 1; i++) {
            arr[arr.length - i - 1] = extractMax();
        }
    }

    private static void heapifySiftUp() {
        heapSize = arr.length;
        for (int i = 0; i < arr.length; i++) {
            siftUp(i);
        }
    }

    private static void heapifySiftDown() {
        heapSize = arr.length;
        for (int i = arr.length - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    private static int extractMax() {
        int max = arr[0];
        arr[0] = arr[heapSize - 1];
        --heapSize;
        siftDown(0);
        return max;
    }

    private static void insert(int v) {
        heapSize++;
        if (heapSize > arr.length) throw new RuntimeException();
        arr[heapSize - 1] = v;
        siftUp(heapSize - 1);
    }

    //просеивание вверх при добавлении в кучу
    private static void siftUp(int i) {
        if (i >= heapSize && i < 0) throw new RuntimeException();
        while (i > 0) {
            int parentIdx = (i - 1) / 2;
            if (arr[parentIdx] < arr[i]) {
                swap(parentIdx, i);
                i = parentIdx;
            } else break;
        }
    }

    //просеивание вниз при добавлении в кучу
    private static void siftDown(int i) {
        if (i >= heapSize && i < 0) throw new RuntimeException();
        while (2 * i + 1 < heapSize) {
            int child1 = i * 2 + 1;
            int child2 = i * 2 + 2;
            int maxIdx = i;
            if (arr[child1] > arr[maxIdx]) maxIdx = child1;
            if (child2 < heapSize && arr[child2] > arr[maxIdx]) maxIdx = child2;
            if (maxIdx == i) break;
            swap(i, maxIdx);
            i = maxIdx;
        }
    }

    private static void swap(int i1, int i2) {
        int buf = arr[i1];
        arr[i1] = arr[i2];
        arr[i2] = buf;
    }
}
