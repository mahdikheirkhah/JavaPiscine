import java.util.*;

public class WeddingComplex {
    public static Map<String, String> createBestCouple(Map<String, List<String>> first, Map<String, List<String>> second) {
        // Initialize data structures
        Map<String, String> matches = new HashMap<>(); // first -> second
        Map<String, String> reverseMatches = new HashMap<>(); // second -> first
        Queue<String> freeFirst = new LinkedList<>(first.keySet());
        
        // Create preference rankings for second group (for quick lookup)
        Map<String, Map<String, Integer>> secondRankings = new HashMap<>();
        for (String person : second.keySet()) {
            Map<String, Integer> ranking = new HashMap<>();
            List<String> preferences = second.get(person);
            for (int i = 0; i < preferences.size(); i++) {
                ranking.put(preferences.get(i), i);
            }
            secondRankings.put(person, ranking);
        }
        
        // Track current proposal index for each person in first group
        Map<String, Integer> proposalIndex = new HashMap<>();
        for (String person : first.keySet()) {
            proposalIndex.put(person, 0);
        }
        
        // Gale-Shapley algorithm
        while (!freeFirst.isEmpty()) {
            String firstPerson = freeFirst.poll();
            
            // Get the next person in firstPerson's preference list
            List<String> preferences = first.get(firstPerson);
            int currentIndex = proposalIndex.get(firstPerson);
            
            if (currentIndex >= preferences.size()) {
                continue; // No more options (shouldn't happen in valid input)
            }
            
            String secondPerson = preferences.get(currentIndex);
            proposalIndex.put(firstPerson, currentIndex + 1);
            
            // If secondPerson is free, accept the proposal
            if (!reverseMatches.containsKey(secondPerson)) {
                matches.put(firstPerson, secondPerson);
                reverseMatches.put(secondPerson, firstPerson);
            } else {
                // secondPerson is already matched, check if they prefer firstPerson
                String currentPartner = reverseMatches.get(secondPerson);
                
                Map<String, Integer> secondPreferences = secondRankings.get(secondPerson);
                int currentPartnerRank = secondPreferences.get(currentPartner);
                int newPartnerRank = secondPreferences.get(firstPerson);
                
                // If secondPerson prefers firstPerson over current partner
                if (newPartnerRank < currentPartnerRank) {
                    // Break up current match
                    matches.remove(currentPartner);
                    reverseMatches.remove(secondPerson);
                    
                    // Create new match
                    matches.put(firstPerson, secondPerson);
                    reverseMatches.put(secondPerson, firstPerson);
                    
                    // Current partner becomes free again
                    freeFirst.offer(currentPartner);
                } else {
                    // secondPerson prefers current partner, firstPerson stays free
                    freeFirst.offer(firstPerson);
                }
            }
        }
        
        return matches;
    }
}