import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.min;

class Task_2 {


    private static class DisjointSetDataStructureNode {

        // reference to the smallest parent
        DisjointSetDataStructureNode[] smallest = {this};
        // reference to the node with which we merge with this one
        DisjointSetDataStructureNode parent = this;
        int id;
        Node job;

        // function that searches for a set that is not yet occupied
        DisjointSetDataStructureNode small() {
            return smallest[0];
        }

        // function for joining sets by reference
        void merge(DisjointSetDataStructureNode parent) {
            this.parent = parent;
            this.smallest[0] = parent.smallest[0];
            parent.smallest = this.smallest;
        }
    }

    // main function for solving the problem
    private static void schedulingProblem(Node[] jobs) {

        int n = jobs.length;

        // Disjoint Set Data Structure
        List<DisjointSetDataStructureNode> disjointSet = createDisjointSet(jobs);

        // sorting
        Node.quickSort(jobs, 0, n - 1);

        for (int i = 0; i < n; i++) {

            int min = min(jobs[i].deadline, n);

            DisjointSetDataStructureNode smallNode = disjointSet.get(min).small();

            // checking if only the 0th set is available, then do not add this job
            if (smallNode.id == 0) {
                continue;
            }

            // book this job and join this set with the previous one
            smallNode.job = jobs[i];
            smallNode.merge(disjointSet.get(smallNode.id - 1));
        }

        // printing a final result
        for (int i = 1; i < disjointSet.size(); i++) {
            System.out.println(i +":   doing" + disjointSet.get(i).job.toString());
        }
    }


    // function for creating a disjoint set based on the maximum deadline
    private static List<DisjointSetDataStructureNode> createDisjointSet(Node[] jobs) {

        int maxDeadline = -1;

        for (int i = 0; i < jobs.length; i++) {
            if (maxDeadline < jobs[i].deadline) {
                maxDeadline = jobs[i].deadline;
            }
        }

        List<DisjointSetDataStructureNode> disjointSet = new LinkedList<>();

        for (int i = 0; i <= maxDeadline; i++) {
            DisjointSetDataStructureNode e = new DisjointSetDataStructureNode();
            e.id = i;
            disjointSet.add(e);
        }

        return disjointSet;
    }

    public static void main(String[] args) {

        Node[] jobs = {
                new Node(1, 2 ,40),
                new Node(2, 4 ,15),
                new Node(3, 3 ,60),
                new Node(4, 2 ,20),
                new Node(5, 3 ,10),
                new Node(6, 1 ,45),
                new Node(7, 1 ,55)
        };

        schedulingProblem(jobs);
    }
}