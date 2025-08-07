package Quest04.AdventureException;
public class Monster extends Character {
    public Monster(String name, int maxHealth, Weapon weapon){
        super(name, maxHealth, weapon);
    }

    @Override
    public String toString(){
        if(this.getCurrentHealth() == 0){
            return String.format("%s is a monster and is dead. He has the weapon %s", this.getName(), this.getWeapon().toString());
        }
        return String.format("%s is a monster with %d HP. He has the weapon %s",this.getName(), this.getCurrentHealth(), this.getWeapon().toString());
    }

    public void takeDamage(int amount){
        int newHealth = this.getCurrentHealth() - ((int)(amount * 0.8));
        if(newHealth < 0) {
            this.setCurrentHealth(0);
        } else {
            this.setCurrentHealth(newHealth);
        }
    }

    public void attack(Character ch){
        if(ch == null) return;
        Weapon weapon = this.getWeapon();
        if (weapon == null || weapon.getDamage() <= 0){
            ch.takeDamage(7);
        } else {
            ch.takeDamage(weapon.getDamage());
        }
    }
}
