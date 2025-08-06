public class Sorcerer extends Character implements Healer {
    private final int healCapacity;

    public Sorcerer(String name, int maxHealth, int healCapacity){
        super(name, maxHealth);
        this.healCapacity = healCapacity;
    }

    @Override
    public int getHealCapacity() {        
        return healCapacity;
    }
    @Override
    public void heal(Character ch) {
        int currentHealth = ch.getCurrentHealth();
        if (currentHealth + healCapacity >= ch.getMaxHealth()){
            ch.setCurrentHealth(ch.getMaxHealth());
        } else {
            ch.setCurrentHealth(currentHealth + healCapacity);
        }
        
    }

    @Override
    public String toString(){
        if (this.getCurrentHealth() == 0) {
            return String.format("%s is a dead sorcerer. So bad, it could heal %d HP.", this.getName(), this.getHealCapacity());
        }
        return String.format("%s is a sorcerer with %d HP. It can heal %d HP.", this.getName(),this.getCurrentHealth(), this.getHealCapacity());
    }
}
