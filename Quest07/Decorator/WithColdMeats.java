public class WithColdMeats extends RacletteDecorator {
    private Raclette decoratedRaclette;

    public WithColdMeats(Raclette raclette) {
        super(raclette);
        this.decoratedRaclette = raclette;
    }

    @Override
    public int getCalories() {
        return decoratedRaclette.getCalories() + 350;
    }

    @Override
    public String getIngredients() {
        return decoratedRaclette.getIngredients() + ", charcuterie";
    }
}
