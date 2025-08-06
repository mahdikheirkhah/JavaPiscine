public class DeadCharacterException {
    private Character character;

    public DeadCharacterException(Character character){
        this.character = character;
    }

    public String getMessage(){
        String result;
        if (character == null) {
            return null;
        }
        if (character instanceof Monster){
            result = String.format("The monster %s is dead.", character.getName());
        } else if (character instanceof Sorcerer){
            result = String.format("The sorcerer %s is dead.", character.getName());
        } else if (character instanceof Templar){
            result = String.format("The templar %s is dead.", character.getName());
        } else {
            result = String.format("The character %s is dead.", character.getName());
        }
        return result;
    }
}
