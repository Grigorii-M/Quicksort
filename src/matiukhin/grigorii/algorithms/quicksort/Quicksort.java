package matiukhin.grigorii.algorithms.quicksort;

import java.util.*;

public class Quicksort {

    private static int numOperationsLomuto = 0;
    private static int numOperationsHoare = 0;

    public static void main(String[] args) {
        // Ask user for an array
        System.out.println("Enter elements of the array separated by whitespaces:");
        Scanner scanner = new Scanner(System.in);
        String[] inputArr = scanner.nextLine().split("\\s+");
        int[] arr = new int[inputArr.length];
        for (int i = 0; i < inputArr.length; i++) {
            arr[i] = Integer.parseInt(inputArr[i]);
        }

        // Ask user which algorithm to use
        String input;
        do {
            System.out.println("Select algorithm:\n" +
                    " Lomuto's partition (random (lr), last element (le), median(lm))\n" +
                    " Hoare's initial algorithm (h)");
            input = scanner.nextLine();
        } while (!input.matches("h|(l[rem])"));

        System.out.println(Arrays.toString(arr));

        long timeBefore = System.currentTimeMillis(); // Save current system time

        // Execute chosen algorithm
        if (input.charAt(0) == 'l') {
            if (input.charAt(1) == 'r') {
                lomutoQuicksort(arr, 0, arr.length - 1, LomutoRule.RANDOM);
            } else if (input.charAt(1) == 'e') {
                lomutoQuicksort(arr, 0, arr.length - 1, LomutoRule.LAST_ELEMENT);
            } else {
                lomutoQuicksort(arr, 0, arr.length - 1, LomutoRule.MEDIAN);
            }
            System.out.println(Arrays.toString(arr));
            System.out.println("operations: " + numOperationsLomuto);
        } else {
            hoareQuicksort(arr, 0, arr.length - 1);
            System.out.println(Arrays.toString(arr));
            System.out.println("operations: " + numOperationsHoare);
        }

        // Calculate and show time elapsed
        long timeAfter = System.currentTimeMillis();
        System.out.println("time: " + (timeAfter - timeBefore));
    }

    private static void lomutoQuicksort(int[] arr, int begin, int end, LomutoRule rule) {
        if (begin < 0 || end < 0 || begin >= end || end >= arr.length) {
            return;
        }
        // Choose pivotValue according to user's specification
        int pivotValue;
        if (rule == LomutoRule.RANDOM) {
            swap(arr, new Random().nextInt(begin, end + 1), end);
            pivotValue = arr[end];
        } else if (rule == LomutoRule.LAST_ELEMENT) {
            pivotValue = arr[end];
        } else {
            int median = (begin + end) / 2;
            if (arr[median] < arr[begin]) {
                swap(arr, median, begin);
                numOperationsLomuto++;
                System.out.println(Arrays.toString(arr) + " initial swap");
            }
            if (arr[end] < arr[begin]) {
                swap(arr, end, begin);
                numOperationsLomuto++;
                System.out.println(Arrays.toString(arr) + " initial swap");
            }
            if (arr[median] < arr[end]) {
                swap(arr, median, end);
                numOperationsLomuto++;
                System.out.println(Arrays.toString(arr) + " initial swap");
            }
            pivotValue = arr[end];
        }

        int pivotIndex = begin - 1; // Used to store index of the first element that is greater than pivot, among all elements visited

        for (int i = begin; i < end; i++ ) {
            if (arr[i] < pivotValue) {
                // When we find an element < pivot we relocate it to the beginning of the array

                pivotIndex++; // Right now pivotIndex points to the first element that is bigger than pivotElement

                swap(arr, pivotIndex, i);

                numOperationsLomuto++;
                System.out.println(Arrays.toString(arr) + " pivot: " + pivotValue + " pivotIndex: " + pivotIndex);
            }
        }

        // Relocate pivotIndex into the middle of the array, right after the elements smaller than pivotElement
        pivotIndex++;
        swap(arr, pivotIndex, end);

        numOperationsLomuto++;
        System.out.println(Arrays.toString(arr) + " pivot: " + pivotValue + " pivotIndex: " + pivotIndex);

        lomutoQuicksort(arr, begin, pivotIndex - 1, rule);
        lomutoQuicksort(arr, pivotIndex + 1, end, rule);
    }

    private static void hoareQuicksort(int[] arr, int begin, int end) {
        if (begin < 0 || end < 0 || begin >= end || end >= arr.length) {
            return;
        }

        int pivotValue = arr[(end + begin) / 2];

        int leftPointer = begin - 1;
        int rightPointer = end + 1;

        while (true) {
            // Find an element in the left part of the array which is greater than pivotValue
            do {
                leftPointer++;
            } while (arr[leftPointer] < pivotValue);

            // Find an element in the right part of the array which is less than pivotValue
            do {
                rightPointer--;
            } while (arr[rightPointer] > pivotValue);

            // If pointers cross, exit the loop
            if (leftPointer >= rightPointer) {
                break;
            }

            swap(arr, leftPointer, rightPointer);

            numOperationsHoare++;
            System.out.println(Arrays.toString(arr) + " pivot: " + pivotValue);
        }

        hoareQuicksort(arr, begin, rightPointer);
        hoareQuicksort(arr, rightPointer + 1, end);
    }

    private static void swap(int[] arr, int i1, int i2) {
        int temp = arr[i1];
        arr[i1] = arr[i2];
        arr[i2] = temp;
    }

    private enum LomutoRule {
        RANDOM,
        LAST_ELEMENT,
        MEDIAN;
    }
}
