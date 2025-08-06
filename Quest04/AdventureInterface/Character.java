import java.util.ArrayList;
import java.util.List;

public class Character {
    private final int maxHealth;
    private final String name;
    private int currentHealth;
    private static List<Character> allCharacters = new ArrayList<>();
    
    public Character(String name, int maxHealth){
        this.currentHealth = maxHealth;
        this.maxHealth = maxHealth;
        this.name = name;
        if (allCharacters == null) {
            allCharacters = new ArrayList<>();
        }
        allCharacters.add(this);
    }
    
    public int getMaxHealth(){
        return maxHealth;
    }
    public int getCurrentHealth(){
        return currentHealth;
    }
    public String getName(){
        return name;
    }

    @Override
    public String toString(){
        if (currentHealth == 0){
            return String.format("%s : KO", name);
        }
        return String.format("%s : %d/%d", name, currentHealth,maxHealth);
    }

    public void takeDamage(int amount){
        currentHealth -= amount;
        if(currentHealth < 0) {
            currentHealth = 0;
        }
    }

    public void attack(Character ch){
        if(ch == null) return;
        ch.takeDamage(9);
    }
    public static String printStatus() {
        if (allCharacters == null || allCharacters.isEmpty()) {
            return "------------------------------------------\n" +
                   "Nobody's fighting right now !\n" +
                   "------------------------------------------\n";
        }

        StringBuilder charFormat = new StringBuilder();
        for (Character ch : allCharacters) {
            charFormat.append(" - ").append(ch.toString()).append("\n");
        }

        return "------------------------------------------\n" +
               "Characters currently fighting :\n" +
               charFormat.toString() +
               "------------------------------------------\n";
    }


    public static Character fight(Character ch1, Character ch2){
        if (ch1 == null && ch2 == null) return null;
        if (ch1 == null) return ch2;
        if (ch2 == null ) return ch1;
        Character winner;
        
        while (true) {
            ch1.attack(ch2);
            if(ch2.getCurrentHealth() == 0) {
                winner = ch1;
                break;
            }
            ch2.attack(ch1);
            if(ch1.currentHealth == 0) {
                winner = ch2;
                break;
            }
        }
        return winner;
    }

}
