import java.util.ArrayList;
import java.util.List;

public  abstract class Character {
    private final int maxHealth;
    private final String name;
    private int currentHealth;
    private Weapon weapon;
    private static List<Character> allCharacters = new ArrayList<>();
    
    public Character(String name, int maxHealth, Weapon weapon){
        this.currentHealth = maxHealth;
        this.maxHealth = maxHealth;
        this.name = name;
        this.weapon = weapon;
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
    protected void setCurrentHealth(int currentHealth){
        this.currentHealth = currentHealth; 
    }
    public String getName(){
        return name;
    }
    public Weapon getWeapon(){
        return weapon;
    }

    @Override
    public String toString(){
        if (currentHealth == 0){
            return String.format("%s : KO", name);
        }
        return String.format("%s : %d/%d", name, currentHealth,maxHealth);
    }

    public abstract void takeDamage(int amount) throws DeadCharacterException;
    public abstract void attack(Character ch) throws DeadCharacterException;

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


    public static Character fight(Character ch1, Character ch2) {
        if (ch1 == null && ch2 == null) return null;
        if (ch1 == null) return ch2;
        if (ch2 == null) return ch1;

        while (true) {
            try {
                ch1.attack(ch2);
                if (ch2.getCurrentHealth() <= 0) return ch1;
            } catch (DeadCharacterException e) {
                return e.getCharacter() == ch1 ? ch2 : ch1;
            }

            try {
                ch2.attack(ch1);
                if (ch1.getCurrentHealth() <= 0) return ch2;
            } catch (DeadCharacterException e) {
                return e.getCharacter() == ch2 ? ch1 : ch2;
            }
        }
    }

}
