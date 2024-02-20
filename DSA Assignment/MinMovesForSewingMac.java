public class MinMovesForSewingMac {

    public static int minMovesToEqualizeDresses(int[] dresses) {
        int totalDresses = 0;
        for (int dressCount : dresses) {
            totalDresses += dressCount;
        }

        int numMachines = dresses.length;
        if (totalDresses % numMachines != 0) {
            return -1;
        }

        int targetDresses = totalDresses / numMachines;
        int moves = 0;
        for (int dressCount : dresses) {
            moves += Math.abs(targetDresses - dressCount);
        }

        // Each move involves two machines, so we divide by 2
        return moves / 2;
    }

    public static void main(String[] args) {
        int[] inputDresses = {1, 0, 5};
        System.out.println("The minimum moves to equalize number of dresses: "+ " " + minMovesToEqualizeDresses(inputDresses)); 
    }
}
