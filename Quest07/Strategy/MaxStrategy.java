public class MaxStrategy implements OperationStrategy {
    @Override
    public int execute(int a, int b){
        return Integer.max(a, b);
    }
}
