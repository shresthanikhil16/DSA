public class venuesAndThemes {

    public static int minAmount(int[][] Amount) {
        int n = Amount.length; // Number of venues
        int k = Amount[0].length; // Number of themes

        //Dynamic programming table to store minimum costs
        int[][] dp = new int[n][k];

        // Fill the first row with initial costs
        for (int j = 0; j < k; j++) {
            dp[0][j] = Amount[0][j];
        }

        // Iterate through each venue starting from the second one
        for (int i = 1; i < n; i++) {
            // Iterate through each theme for the current venue
            for (int j = 0; j < k; j++) {
                // Initialize the minimum cost for the current theme
                int minAmountforVenues = Integer.MAX_VALUE;
                // Iterate through each theme for the previous venue
                for (int prevVenueThemeIndex = 0; prevVenueThemeIndex < k; prevVenueThemeIndex++) {
                    // Skip if the current and previous venues have the same theme
                    if (j == prevVenueThemeIndex) {
                        continue;
                    }
                    // Update the minimum cost considering the cost of the current theme
                    minAmountforVenues = Math.min(minAmountforVenues, dp[i - 1][prevVenueThemeIndex] + Amount[i][j]);
                }
                dp[i][j] = minAmountforVenues;
            }
        }

        // Find the minimum cost from the last row
        int min = Integer.MAX_VALUE;
        for (int j = 0; j < k; j++) {
            min = Math.min(min, dp[n - 1][j]);
        }
        return min;
    }

    public static void main(String[] args) {
        int[][] costsofEvents = {{1, 3, 2}, {4, 6 , 8}, {3 ,1 , 5}};
        System.out.println("The minimum cost to decorate all venues is  " +minAmount(costsofEvents)); // Output: 7
    }
}