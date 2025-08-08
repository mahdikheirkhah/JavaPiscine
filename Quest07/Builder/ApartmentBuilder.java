public class ApartmentBuilder implements HousingBuilder {
    private Apartment apartment;

    public ApartmentBuilder() {
        this.apartment = new Apartment();
    }

    @Override
    public HousingBuilder setSize(int size) {
        apartment.setSize(size);
        return this;
    }

    @Override
    public HousingBuilder setPrice(int price) {
        apartment.setPrice(price);
        return this;
    }

    @Override
    public HousingBuilder setRooms(int rooms) {
        apartment.setRooms(rooms);
        return this;
    }

    @Override
    public HousingBuilder setName(String name) {
        apartment.setName(name);
        return this;
    }

    @Override
    public Housing build() {
        return apartment;
    }
}
