import java.util.PriorityQueue;

public class MinTimeForBuildingEngine {

    public static int minTimeToBuildEngines(int[] engines, int splitCost) {
        int totalTime = 0;
        int availableEngineers = 1;
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[0] - b[0]); // min-heap, [time, engineerCount]
        
        for (int time : engines) {
            heap.offer(new int[]{time, 1});
        }

        while (!heap.isEmpty()) {
            int[] engineInfo = heap.poll();
            int time = engineInfo[0];
            int engineerCount = engineInfo[1];
            totalTime = Math.max(totalTime, time);

            if (engineerCount > availableEngineers) {
                int splitTime = splitCost * (engineerCount - availableEngineers);
                totalTime += splitTime;
                availableEngineers += engineerCount - availableEngineers;
            }

            availableEngineers -= engineerCount;
            if (availableEngineers < 1) {
                totalTime += splitCost;
                availableEngineers += 1;
            }
        }

        return totalTime;
    }

    public static void main(String[] args) {
        int[] engines = {1, 2, 3};
        int splitCost = 1;
        System.out.println("The minimum time to build engine is: " + " " + minTimeToBuildEngines(engines, splitCost)); 
    }
}