public class Monster extends Character {
    public Monster(String name, int maxHealth){
        super(name, maxHealth);
    }

    @Override
    public String toString(){
        if(this.getCurrentHealth() == 0){
            return String.format("%s is a monster and is dead", this.getName());
        }
        return String.format("%s is a monster with %d HP",this.getName(), this.getCurrentHealth());
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
        ch.takeDamage(7);
    }
}
