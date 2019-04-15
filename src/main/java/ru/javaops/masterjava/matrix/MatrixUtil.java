package ru.javaops.masterjava.matrix;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {


    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        return matrixC;
    }

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply3(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        new ForkJoinPool(10).submit(
                () -> IntStream.range(0, matrixSize)
                        .parallel()
                        .forEach(row -> {
                            final int[] rowA = matrixA[row];
                            final int[] rowC = matrixC[row];

                            for (int idx = 0; idx < matrixSize; idx++) {
                                final int elA = rowA[idx];
                                final int[] rowB = matrixB[idx];
                                for (int col = 0; col < matrixSize; col++) {
                                    rowC[col] += elA * rowB[col];
                                }
                            }
                        })
        ).get();
        return matrixC;
    }

    public static int[][] concurrentMultiply2(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        return Arrays.stream(matrixA)
                .map(r -> IntStream.range(0, matrixB[0].length)
                        .map(i -> IntStream.range(0, matrixB.length)
                                .map(j -> r[j] * matrixB[j][i])
                                .sum())
                        .toArray()).parallel()
                .toArray(int[][]::new);
    }


    // TODO optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        int thatColumn[] = new int[matrixSize];

        try {
            for (int j = 0; j < matrixSize; j++) {
                for (int k = 0; k < matrixSize; k++) {
                    thatColumn[k] = matrixB[k][j];
                }

                for (int i = 0; i < matrixSize; i++) {
                    int thisRow[] = matrixA[i];
                    int sum = 0;
                    for (int k = 0; k < matrixSize; k++) {
                        sum += thisRow[k] * thatColumn[k];
                    }
                    matrixC[i][j] = sum;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("IndexOutOfBoundsException IndexOutOfBoundsException IndexOutOfBoundsException");
        }




//        for (int i = 0; i < matrixSize; i++) {
//            for (int j = 0; j < matrixSize; j++) {
//                BT[j][i] = BT[i][j];
//            }
//        }
//
//        for (int i = 0; i < matrixSize; i++) {
//            for (int j = 0; j < matrixSize; j++) {
//                int summand = 0;
//                for (int k = 0; k < matrixSize; k++) {
//                    summand += matrixA[i][k]*BT[j][k];
//                }
//                matrixC[i][j] = summand;
//            }
//        }

//        for (int i = 0; i < matrixSize; i++) {
//            for (int j = 0; j < matrixSize; j++) {
//                int sum = 0;
//                for (int k = 0; k < matrixSize; k++) {
//                    sum += matrixA[i][k] * matrixB[k][j];
//                }
//                matrixC[i][j] = sum;
//            }
//        }
        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
