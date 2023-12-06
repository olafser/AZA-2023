public class Node {
    int job;
    int deadline;
    int profit;

    public Node(int job, int deadline, int profit) {
        this.job = job;
        this.deadline = deadline;
        this.profit = profit;
    }

    @Override
    public String toString() {
        return " job " + job +
                " (deadline=" + deadline +
                ", profit=" + profit +
                ')';
    }

    // =======================================> Sorting <======================================================
    public static void quickSort(Node[] nodes, int low, int high) {
        if (low < high) {
            int partitionIndex = partition(nodes, low, high);

            quickSort(nodes, low, partitionIndex - 1);
            quickSort(nodes, partitionIndex + 1, high);
        }
    }
    private static int partition(Node[] nodes, int low, int high) {
        int pivot = nodes[high].profit;
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (nodes[j].profit > pivot) {
                i++;

                // Swap nodes[i] and nodes[j]
                Node temp = nodes[i];
                nodes[i] = nodes[j];
                nodes[j] = temp;
            }
        }

        // Swap nodes[i+1] and nodes[high] (pivot)
        Node temp = nodes[i + 1];
        nodes[i + 1] = nodes[high];
        nodes[high] = temp;

        return i + 1;
    }

    // ========================================================================================================

}
