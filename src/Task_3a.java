public class Task_3a {

    // main function for solving the problem
    public static void minAssignmentCost(int costMatrix[][]) {

        int n = costMatrix.length;

        int minCost = 0;
        String[] result = new String[n];

        for (int i = 0; i < n; i++) {

            int localMin = Integer.MAX_VALUE;
            int indexOfLocalMin = -1;

            // greedy search for the lowest subscription price for the current person, if it is not already taken
            for (int j = 0; j < n; j++) {

                if (costMatrix[i][j] < localMin){
                    localMin = costMatrix[i][j];
                    indexOfLocalMin = j;
                }
            }

            // adding a choice to the results
            result[i] = "Person " + (i + 1) + " doing " + "job " + (indexOfLocalMin + 1) + " (cost=" + costMatrix[i][indexOfLocalMin] + ")";
            minCost += localMin;

            // "crossing out" a column
            for (int k = 0; k < n; k++) {
                costMatrix[k][indexOfLocalMin] = Integer.MAX_VALUE;
            }
        }

        // printing a final result
        System.out.println("MinAssignmentCost is " + minCost + "\n");
        for (int i = 0; i < result.length; i++) {
            System.out.println(result[i]);
        }
    }


    public static void main(String[] args) {

        int[][] costMatrix = {{10, 5, 5},
                {2, 4, 10},
                {5, 1, 7}};

        minAssignmentCost(costMatrix);
    }
}
