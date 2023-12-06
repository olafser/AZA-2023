public class Task_3b {

    private static class Node {
        int price;
        boolean crossedValue = false;
        boolean intersectionValue = false;
    }

    // main function for solving the problem
    private static void minAssignmentCost(int[][] costMatrix) {

        int n = costMatrix.length;
        int[][] costMatrixModified = makeCopy(costMatrix, n);

        // finding the minimums in each row and subtracting them from each element in the rows
        findMinInEveryRow(costMatrixModified, n);
        // finding the minimums in each column and subtracting them from each element in the columns
        findMinInEveryColumn(costMatrixModified, n);

        // create an array of nodes, as it simplifies our work with drawing rows and choosing a combination of minimums
        Node[][] coveredMatrix = makeNodeMatrix(costMatrixModified, n);;
        int coveredLines = 0;

        while (coveredLines != n) {

            // look for the minimum number of lines to cross out all zeros
            coveredLines = findMinimalZeroCoveringMatrix(coveredMatrix, n);

            if (coveredLines == n) {
                break;
            } else {
                // if the number of lines is less than n, then modify the previous matrix again
                findBetterZeroCovering(coveredMatrix, n);
            }
        }

        // in order to find the final result, we need to update the flags, as we will need them in calculateResult
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                coveredMatrix[i][j].crossedValue = false;
                coveredMatrix[i][j].intersectionValue = false;
            }
        }

        int[] result = calculateResult(coveredMatrix, n);

        printResult(result, costMatrix, n);
    }

    private static Node[][] makeNodeMatrix(int[][] costMatrixModified, int n) {

        Node[][] nodeMatrix = new Node[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                nodeMatrix[i][j] = new Node();
                nodeMatrix[i][j].price = costMatrixModified[i][j];
            }
        }

        return nodeMatrix;
    }

    private static int[][] makeCopy(int[][] costMatrix, int n) {

        int copy[][] = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                copy[i][j] = costMatrix[i][j];
            }
        }

        return copy;
    }


    // finding the minimums in each row and subtracting them from each element in the rows
    private static void findMinInEveryRow(int[][] costMatrixCopy, int n) {

        for (int i = 0; i < n; i++) {

            int localMin = Integer.MAX_VALUE;

            for (int j = 0; j < n; j++) {

                if (costMatrixCopy[i][j] < localMin){
                    localMin = costMatrixCopy[i][j];
                }
            }

            for (int j = 0; j < n; j++) {

                costMatrixCopy[i][j] -= localMin;
            }
        }
    }

    // finding the minimums in each column and subtracting them from each element in the columns
    private static void findMinInEveryColumn(int[][] costMatrixCopy, int n) {

        for (int j = 0; j < n; j++) {

            int localMin = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {

                if (costMatrixCopy[i][j] < localMin){
                    localMin = costMatrixCopy[i][j];
                }
            }

            for (int i = 0; i < n; i++) {

                costMatrixCopy[i][j] -= localMin;
            }
        }
    }


    // In this function, I am looking for the minimum number of lines that will cross out all the zeros
    // in the matrix. I achieve this by first going through the entire array and counting
    // the number of zeros in each row and column. Then I select the row or column with the highest
    // number of zeros and cross it out. The process is repeated until I have crossed out all the zeros.
    // Then I return the number of lines that need to be crossed out.
    private static int findMinimalZeroCoveringMatrix(Node[][] nodeMatrix, int n) {

        int numberOfLines = 0;

        for (int k = 0; k < n; k++) {

            int[] rows = new int[n];
            int[] cols = new int[n];

            boolean isZeroes = false;

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (nodeMatrix[i][j].price == 0 && nodeMatrix[i][j].crossedValue != true) {
                        rows[i]++;
                        cols[j]++;
                        isZeroes = true;
                    }
                }
            }


            if (!isZeroes) {
                break;
            }

            int[] maxZeroes = {0, 0};
            int[] maxZeroesCoo = {0, 0};
            for (int i = 0; i < n; i++) {
                if (maxZeroes[0] < rows[i]) {
                    maxZeroesCoo[0] = i;
                    maxZeroes[0] = rows[i];
                }
                if (maxZeroes[1] < cols[i]) {
                    maxZeroesCoo[1] = i;
                    maxZeroes[1] = cols[i];
                }
            }

            int row = rows[maxZeroesCoo[0]];
            int col = cols[maxZeroesCoo[1]];
            if (row > col) {
                for (int i = 0; i < n; i++) {
                    if (!nodeMatrix[maxZeroesCoo[0]][i].crossedValue) {
                        nodeMatrix[maxZeroesCoo[0]][i].crossedValue = true;
                    } else {
                        nodeMatrix[maxZeroesCoo[0]][i].intersectionValue = true;
                    }
                }
            } else {
                for (int i = 0; i < n; i++) {
                    if (!nodeMatrix[i][maxZeroesCoo[1]].crossedValue) {
                        nodeMatrix[i][maxZeroesCoo[1]].crossedValue = true;
                    } else {
                        nodeMatrix[i][maxZeroesCoo[1]].intersectionValue = true;
                    }
                }
            }

            numberOfLines++;
        }

        return numberOfLines;
    }


    // This function reduces the matrix in an attempt to obtain such a combination of zeros
    // that, with their minimum number of zeros, it will be necessary to have
    // the same number as the size of this array
    // To understand how this is implemented, I advise you to watch the video from 7:06,
    // there are only a couple of seconds to understand the principle
    // https://youtu.be/ezSx8OyBZVc?si=HcjXUbJ42oV2R9ZM
    // But in general, we choose the minimum of what is not crossed out and subtract it from the other
    // unscratched numbers, and then add this minimum to the values at the intersection of the lines
    private static void findBetterZeroCovering(Node[][] coveredMatrix, int n) {

        int minPrice = Integer.MAX_VALUE;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (coveredMatrix[i][j].price < minPrice && coveredMatrix[i][j].crossedValue != true) {
                    minPrice = coveredMatrix[i][j].price;
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (!coveredMatrix[i][j].crossedValue) {
                    coveredMatrix[i][j].price -= minPrice;
                } else if (coveredMatrix[i][j].intersectionValue){
                    coveredMatrix[i][j].price += minPrice;
                }

                coveredMatrix[i][j].crossedValue = false;
                coveredMatrix[i][j].intersectionValue = false;
            }
        }
    }


    // This function searches for such a combination of zeros from the modified matrix
    // that crosses out all the elements of the matrix, but so that they do not intersect with each other
    // and the combination of elements in the places of these zeros
    // in the initial matrix is the optimal minimum for the solution
    // To understand how this is implemented, I advise you to watch the video from 9:30,
    // there are only a couple of seconds to understand the principle
    // https://youtu.be/ezSx8OyBZVc?si=HcjXUbJ42oV2R9ZM
    // But in general, we look for the zero that is in the row and column with the least number of zeros in them
    // and cross it out, and then repeat the algorithm on the un-crossed zeros
    private static int[] calculateResult(Node[][] nodeMatrix, int n) {

        int[] result = new int[n];

        for (int k = 0; k < n; k++) {

            int[] rows = new int[n];
            int[] cols = new int[n];

            for (int i = 0; i < n; i++) {
                rows[i] = Integer.MAX_VALUE;
                cols[i] = Integer.MAX_VALUE;
            }

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (nodeMatrix[i][j].price == 0 && nodeMatrix[i][j].crossedValue != true) {
                        rows[i]++;
                        cols[j]++;
                    }
                }
            }

            int[] minZeroes = {Integer.MAX_VALUE, Integer.MAX_VALUE};
            int[] minZeroesCoo = {0, 0};
            for (int i = 0; i < n; i++) {
                if (minZeroes[0] > rows[i]) {
                    minZeroesCoo[0] = i;
                    minZeroes[0] = rows[i];
                }
                if (minZeroes[1] > cols[i]) {
                    minZeroesCoo[1] = i;
                    minZeroes[1] = cols[i];
                }
            }

            int row = rows[minZeroesCoo[0]];
            int col = cols[minZeroesCoo[1]];

            if (row < col) {
                int cCoo = -1;
                for (int i = 0; i < n; i++) {

                    if (nodeMatrix[minZeroesCoo[0]][i].price == 0 && nodeMatrix[minZeroesCoo[0]][i].crossedValue == false) {
                        cCoo = i;
                    }

                    if (!nodeMatrix[minZeroesCoo[0]][i].crossedValue) {
                        nodeMatrix[minZeroesCoo[0]][i].crossedValue = true;
                    } else {
                        nodeMatrix[minZeroesCoo[0]][i].intersectionValue = true;
                    }
                }
                for (int i = 0; i < n; i++) {
                    if (!nodeMatrix[i][cCoo].crossedValue) {
                        nodeMatrix[i][cCoo].crossedValue = true;
                    } else {
                        nodeMatrix[i][cCoo].intersectionValue = true;
                    }
                }

                result[minZeroesCoo[0]] = cCoo;

            } else {
                int rCoo = -1;
                for (int i = 0; i < n; i++) {

                    if (nodeMatrix[i][minZeroesCoo[1]].price == 0 && nodeMatrix[i][minZeroesCoo[1]].crossedValue == false) {
                        rCoo = i;
                    }

                    if (!nodeMatrix[i][minZeroesCoo[1]].crossedValue) {
                        nodeMatrix[i][minZeroesCoo[1]].crossedValue = true;
                    } else {
                        nodeMatrix[i][minZeroesCoo[1]].intersectionValue = true;
                    }

                }
                for (int i = 0; i < n; i++) {
                    if (!nodeMatrix[rCoo][i].crossedValue) {
                        nodeMatrix[rCoo][i].crossedValue = true;
                    } else {
                        nodeMatrix[rCoo][i].intersectionValue = true;
                    }
                }

                result[rCoo] = minZeroesCoo[1];
            }
        }
        return result;
    }

    // printing a final result
    private static void printResult(int[] result, int[][] costMatrix, int n) {

        int totalCost = 0;

        for (int i = 0; i < n; i++) {
            totalCost += costMatrix[i][result[i]];
        }

        System.out.println("MinAssignmentCost is: " + totalCost + "\n");

        for (int i = 0; i < n; i++) {
            System.out.println("Person " + (i + 1) + " doing " + "job " + (result[i] + 1) + " (cost=" + costMatrix[i][result[i]] + ")");
        }
    }

    public static void main(String[] args) {

        int[][] costMatrix = {{10, 5, 5},
                {2, 4, 10},
                {5, 1, 7}};

        minAssignmentCost(costMatrix);
    }
}
