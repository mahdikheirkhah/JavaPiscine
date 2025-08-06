public interface Healer {
    public void heal(Character ch) throws DeadCharacterException;
    public int getHealCapacity();
}
