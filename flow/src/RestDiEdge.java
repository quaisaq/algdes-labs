public class RestDiEdge extends DiEdge<Integer> {
    private RestDiEdge counterpart;
    private boolean isReverse;

    public RestDiEdge(Vertex from, Vertex to) {
        super(from, to);
        this.counterpart = new RestDiEdge(to, from, this);
        this.isReverse = false;
        to.addEdge(counterpart);
    }

    private RestDiEdge(Vertex from, Vertex to, RestDiEdge counterpart) {
        super(from, to);
        this.isReverse = true;
        this.counterpart = counterpart;
    }

    public void setRestValue(int x) {
        // Set the value
        int difference = getValue() - x;
        setValue(x);
        // Update the edge counterpart
        counterpart.setValue(difference + counterpart.getValue());
    }

    public int getCapactiy() {
        return getValue() + counterpart.getValue();
    }

    public boolean isReverse() {
        return isReverse;
    }
}