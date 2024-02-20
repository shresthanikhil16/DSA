import java.util.Collections;
import java.util.PriorityQueue;

public class ScoreTrackingSystem {
    private PriorityQueue<Double> minHeap;
    private PriorityQueue<Double> maxHeap;

    public ScoreTrackingSystem() {
        minHeap = new PriorityQueue<>();
        maxHeap = new PriorityQueue<>(Collections.reverseOrder());
    }

    public void addScore(double score) {
        // Adding score to appropriate heap
        if (maxHeap.isEmpty() || score <= maxHeap.peek()) {
            maxHeap.offer(score);
        } else {
            minHeap.offer(score);
        }

        // Balancing heaps
        if (maxHeap.size() > minHeap.size() + 1) {
            minHeap.offer(maxHeap.poll());
        } else if (minHeap.size() > maxHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }

    public double getMedianScore() {
        if (maxHeap.isEmpty()) {
            throw new IllegalStateException("No scores added yet.");
        }

        if (maxHeap.size() == minHeap.size()) {
            return (maxHeap.peek() + minHeap.peek()) / 2.0;
        } else {
            return maxHeap.peek();
        }
    }

    public static void main(String[] args) {
        ScoreTrackingSystem scoreTracker = new ScoreTrackingSystem();
        scoreTracker.addScore(85.5);
        scoreTracker.addScore(92.3);
        scoreTracker.addScore(77.8);
        scoreTracker.addScore(90.1);
        double median1 = scoreTracker.getMedianScore();
        System.out.println("Average of 90.1 and 85.5 (Median 1) : " + median1); // Output: 87.8
        scoreTracker.addScore(81.2);
        scoreTracker.addScore(88.7);
        double median2 = scoreTracker.getMedianScore();
        System.out.println("Average of 88.7 and 85.5 (Median 2) : " + median2); // Output: 87.1
    }
}