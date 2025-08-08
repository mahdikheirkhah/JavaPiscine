public class Context {

    private OperationStrategy operationStrategy;
    
    public Context(){
        operationStrategy = new AddStrategy();
    }

    public void changeStrategy(OperationStrategy operationStrategy){
        this.operationStrategy = operationStrategy;
    }

    public int execute(int a, int b){
        return this.operationStrategy.execute(a, b);
    }
    
}