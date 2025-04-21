package application;

import java.util.Arrays;

public class myArrayList<T extends Comparable<T>> {
    private T[] arr;
    private int n = 0; // Number of elements in the list

    public myArrayList() {
        arr = (T[]) new Comparable[50]; // Initial capacity
    }

    private void resize() {
        // Double the size of the array when full
        arr = Arrays.copyOf(arr, arr.length * 2);
    }

    public void add(T o) {
        if (n == arr.length) {
            resize();
        }
        arr[n] = o;
        n++;
    }

    public boolean delete(T data) {
        for (int i = 0; i < n; i++) {
            if (data.compareTo(arr[i]) == 0) {
                for (int j = i + 1; j < n; j++) {
                    arr[j - 1] = arr[j];
                }
                n--;
                arr[n] = null; // Clear the last element
                return true;
            }
        }
        return false;
    }

    public boolean search(T value) {
        for (int i = 0; i < n; i++) {
            if (value.compareTo(arr[i]) == 0) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        // Clear the array and reset size
        Arrays.fill(arr, 0, n, null);
        n = 0;
    }

    public void print() {
        for (int i = 0; i < n; i++) {
            System.out.println(i + "\t" + arr[i]);
        }
    }

    public T getAt(int index) {
        if (index >= 0 && index < n) {
            return arr[index];
        }
        return null;
    }

    public int size() {
        return n;
    }
}