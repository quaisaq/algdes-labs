public class Vertex {
    private int id;
    private String name;

    public Vertex(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<DiEdge> getEdges() {
        return null;
    }
}