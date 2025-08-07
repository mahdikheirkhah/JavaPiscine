import java.util.*;

public class KeepTheChange {
    public static List<Integer> computeChange(int amount, Set<Integer> coins) {
        if (amount == 0) {
            return new ArrayList<>();
        }
        
        // Convert set to sorted list for consistent ordering
        List<Integer> coinList = new ArrayList<>(coins);
        Collections.sort(coinList);
        
        // dp[i] represents the minimum number of coins needed to make amount i
        int[] dp = new int[amount + 1];
        // parent[i] stores which coin was used to reach the optimal solution for amount i
        int[] parent = new int[amount + 1];
        
        // Initialize dp array
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        
        // Fill dp array using bottom-up approach
        for (int i = 1; i <= amount; i++) {
            for (int coin : coinList) {
                if (coin <= i && dp[i - coin] != Integer.MAX_VALUE) {
                    if (dp[i - coin] + 1 < dp[i]) {
                        dp[i] = dp[i - coin] + 1;
                        parent[i] = coin;
                    }
                }
            }
        }
        
        // If no solution exists
        if (dp[amount] == Integer.MAX_VALUE) {
            return null;
        }
        
        // Reconstruct the solution by backtracking
        List<Integer> result = new ArrayList<>();
        int current = amount;
        while (current > 0) {
            int coin = parent[current];
            result.add(coin);
            current -= coin;
        }
        
        // Sort the result in descending order to match expected output format
        Collections.sort(result, Collections.reverseOrder());
        
        return result;
    }
}
