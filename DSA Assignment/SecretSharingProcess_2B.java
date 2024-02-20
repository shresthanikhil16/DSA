import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SecretSharingProcess_2B {

    public static List<Integer> secretKeepers(int n, int[][] timeIntervals, int firstPerson) {
        Set<Integer> known = new HashSet<>();
        known.add(firstPerson);

        // Iterate through each interval
        for (int[] intervals : timeIntervals) {
            int start = intervals[0];
            int end = intervals[1];
            // Sharingsecret with individuals within the current interval
            for (int i = start; i <= end; i++) {
                if (i < n) { // Check if ID is withinvalid range
                    known.add(i);
                }
                
            }
        }

        // Convert set to list and sort it
        List<Integer> results = new ArrayList<>(known);
        results.sort(null);
        return results;
    }

    public static void main(String[] args) {
        int n = 5;
        int[][] intervals = {{0, 2}, {1, 3}, {2,4}};
        int firstPerson = 0;

        List<Integer> result = secretKeepers(n, intervals, firstPerson);
        System.out.println("Set of individuals who know secret is "+result);  
    }
}