public class PlaneDriver extends Driver {
    @Override
    public Transport createTransport(){
        return new Plane();
    }
}
