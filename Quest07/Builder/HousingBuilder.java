public interface HousingBuilder {
    public HousingBuilder setSize(int size);
    public HousingBuilder setPrice(int price);
    public HousingBuilder setRooms(int rooms);
    public HousingBuilder setName(String name);
    public Housing build();
}
