public class HouseBuilder implements HousingBuilder {
    private House house;

    public HouseBuilder() {
        this.house = new House();
    }

    @Override
    public HousingBuilder setSize(int size) {
        house.setSize(size);
        return this;
    }

    @Override
    public HousingBuilder setPrice(int price) {
        house.setPrice(price);
        return this;
    }

    @Override
    public HousingBuilder setRooms(int rooms) {
        house.setRooms(rooms);
        return this;
    }

    @Override
    public HousingBuilder setName(String name) {
        house.setName(name);
        return this;
    }

    @Override
    public Housing build() {
        return house;
    }
}
