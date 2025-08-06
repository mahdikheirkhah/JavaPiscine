public class Templar extends Character implements Healer, Tank {
    private final int healCapacity;
    private final int shield;

    public Templar(String name, int maxHealth, int healCapacity, int shield){
        super(name, maxHealth);
        this.healCapacity = healCapacity;
        this.shield = shield;
    }

    @Override
    public int getHealCapacity() {        
        return healCapacity;
    }
    @Override
    public void heal(Character ch) {
        int currentHealth = ch.getCurrentHealth();
        if (currentHealth + this.getHealCapacity() >= ch.getMaxHealth()){
            ch.setCurrentHealth(ch.getMaxHealth());
        } else {
            ch.setCurrentHealth(currentHealth + this.getHealCapacity());
        }
        
    }

    @Override
    public int getShield() {
        return shield;
    }

    @Override
    public String toString(){
        if (this.getCurrentHealth() == 0) {
            return String.format("%s has been beaten, even with its %d shield. So bad, it could heal %d HP.", this.getName(), this.getShield() ,this.getHealCapacity());
        }
        return String.format("%s is a strong Templar with %d HP. It can heal %d HP and has a shield of %d.", this.getName(),this.getCurrentHealth(), this.getHealCapacity(), this.getShield());
    }
    public void takeDamage(int amount){
        int newHealth = this.getCurrentHealth() - amount + this.getShield();
        if(newHealth < 0) {
            this.setCurrentHealth(0);
        } else {
            this.setCurrentHealth(newHealth);
        }
    }

    public void attack(Character ch){
        if(ch == null) return;
        this.heal(this);
        ch.takeDamage(6);
    }
}
