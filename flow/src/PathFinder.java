public class PathFinder {
	private Vertex s;
	private Vertex t;
	private Graph g;
	
	public PathFinder(Graph g, Vertex s, Vertex t) {
		this.s = s;
		this.t = t;
		this.g = g;
	}
	
	public List<Edge> getPath() {
		List<Edge> es = s.getEdges();
		
		return es;
	}
}