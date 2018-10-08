public class RestDiEdge extends DiEdge<Integer> {
    public void setRestValue(int x) {
        // Set the value
        int y = getValue() - x;
        setValue(x);
        // Update the edge counterpart
        counterpart.setValue(y + counterpart.getValue());
    }
}