import java.util.*;

public class Task_1 {

    // main function for solving the problem
    private static void schedulingProblem(Node[] jobs) {

        int n = jobs.length;

        // sorting
        Node.quickSort(jobs, 0, n - 1);

        // the best combination of scheduling
        List<Node> result = new ArrayList<>();

        // first combination
        result.add(jobs[0]);

        // finding the best combination of scheduling
        for (int i = 1; i < n; i++) {
            result = isFeasible(result, jobs[i]);
        }

        // printing a final result
        for (int i = 0; i < result.size(); i++) {
            System.out.println((i + 1) +":   doing" + result.get(i).toString());
        }
    }

    private static List<Node> isFeasible(List<Node> result, Node job) {

        List<Node> combination = new ArrayList<>(result);

        // adding an item to the correct position to ensure that it is sorted according to deadlines
        int prev = 0;
        for (Node node : combination) {
            if (node.deadline > job.deadline) {
                combination.add(prev, job);
                break;
            }
            prev++;
        }
        if (prev == combination.size()) {
            combination.add(job);
        }

        // checking whether we have any conflicts when adding this element
        int currentTime = 0;
        for (Node node : combination) {
            currentTime++;

            if (currentTime > node.deadline) {
                return result;
            }
        }

        return combination;
    }


    public static void main(String[] args) {
/*        Node[] jobs = {
                new Node(1, 2 ,40),
                new Node(2, 4 ,15),
                new Node(3, 4 ,60),
                new Node(4, 2 ,20),
                new Node(5, 4 ,10),
                new Node(6, 1 ,45),
                new Node(7, 1 ,55)
        };*/

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
