public class WithPickles extends RacletteDecorator {
    private Raclette raclette;

    public WithPickles(Raclette raclette) {
        super(raclette);
        this.raclette = raclette;
    }

    @Override
    public int getCalories() {
        return raclette.getCalories() + 50;
    }

    @Override
    public String getIngredients() {
        return raclette.getIngredients() + ", cornichons";
    }
}
