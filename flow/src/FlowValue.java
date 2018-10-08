public class FlowValue {
    private int capacity;
    private int flow;

    public FlowValue(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setFlow(int flow) {
        this.flow = flow;
    }

    public boolean isFull() {
        return flow >= capacity;
    }
}