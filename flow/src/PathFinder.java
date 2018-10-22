import java.util.*;

public class PathFinder {
	private Vertex s;
	private Vertex t;
	private FlowGraph g;
	
	public PathFinder(FlowGraph g) {
		this.s = g.getSource();
		this.t = g.getSink();
		this.g = g;
	}
	
	public List<DiEdge> getPath() {
		HashSet<Vertex> visited = new HashSet<>();
		SinglyLinkedList<DiEdge> l = dfs(s, t, visited, new SinglyLinkedList<DiEdge>());
		List<DiEdge> path = new ArrayList<>();
		for (DiEdge e : l) {
			path.add(e);
		}
		return path;
	}
	
	public SinglyLinkedList<DiEdge> dfs(Vertex v, Vertex t, HashSet<Vertex> visited, SinglyLinkedList<DiEdge> path) {
		visited.add(v);
		
		for (DiEdge e : v.getEdges()) {
			Vertex w = e.getTo();
			
			if (w == t) {
				return path;
			}
			
			if (!visited.contains(w)) {
				return dfs(w, t, visited, path.add(e));
			}
		}
			
		return path;
	}
	
	private class SinglyLinkedList<T> implements Iterable<T> {
		private class SNode {
			T val;
			SNode next;
			
			private SNode(T val, SNode next) {
				this.val = val;
				this.next = next;
			}
		}
		
		private SNode first;
		
		private SinglyLinkedList() {
			first = null;
		}
		
		public SinglyLinkedList(SNode n) {
			first = n;
		}
		
		public SinglyLinkedList<T> add(T val) {
			return new SinglyLinkedList<>(new SNode(val, first));
		}
		
		public Iterator<T> iterator() {
			return new Iterator<T>(){
				private SNode current = first;
				
				public boolean hasNext() {
					return current.next != null;
				}
				
				public T next() {
					T val = current.next.val;
					current = current.next;
					return val;
				}
				
				public void remove() {
					throw new RuntimeException("Not supported!");
				}
			};
		}
	}
}