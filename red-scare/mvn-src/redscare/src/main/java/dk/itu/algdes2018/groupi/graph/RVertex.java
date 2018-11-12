package dk.itu.algdes2018.groupi.graph;

public class RVertex {
    private final String name;
    private final boolean red;

    public RVertex(String name, boolean red) {
        this.name = name;
        this.red = red;
    }

    public String getName() {
        return this.name;
    }

    public boolean isRed() {
        return this.red;
    }
}