public class Sorcerer extends Character implements Healer {
    private final int healCapacity;

    public Sorcerer(String name, int maxHealth, int healCapacity, Weapon weapon){
        super(name, maxHealth, weapon);
        this.healCapacity = healCapacity;
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
    public String toString(){
        if (this.getCurrentHealth() == 0) {
            return String.format("%s is a dead sorcerer. So bad, it could heal %d HP. He has the weapon %s", this.getName(), this.getHealCapacity(), this.getWeapon().toString());
        }
        return String.format("%s is a sorcerer with %d HP. It can heal %d HP. He has the weapon %s", this.getName(),this.getCurrentHealth(), this.getHealCapacity(), this.getWeapon().toString());
    }

    public void takeDamage(int amount){
        int newHealth = this.getCurrentHealth() - amount;
        if(newHealth < 0) {
            this.setCurrentHealth(0);
        } else {
            this.setCurrentHealth(newHealth);
        }
    }

    public void attack(Character ch){
        if(ch == null) return;
        this.heal(this);
        Weapon weapon = ch.getWeapon();
        if (weapon == null || weapon.getDamage() == 0){
            ch.takeDamage(10);
        } else {
            ch.takeDamage(weapon.getDamage());
        }

    }
}
