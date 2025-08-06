public class Character {
    private final int maxHealth;
    private final String name;
    private int currentHealth;
    
    public Character(String name, int maxHealth){
        this.currentHealth = maxHealth;
        this.maxHealth = maxHealth;
        this.name = name;
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

}
