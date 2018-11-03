import java.util.*;

public class Kattis {
    public static void main(String[] args) {
        FlowGraph fg = readInput();
        solve(fg);
        printSolution(fg);
    }

    static FlowGraph readInput() {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        int s = sc.nextInt();
        int t = sc.nextInt();

        System.out.print(n);
        System.out.print(' ');

        Vertex[] vertices = new Vertex[n];
        for (int i = 0; i < n; i++) {
            vertices[i] = new Vertex(i);
        }

        for (int i = 0; i < m; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            int c = sc.nextInt();
            
            Vertex vu = vertices[u];
            Vertex vv = vertices[v];
            Edge e = new Edge(vu, vv, c);
            vu.addEdge(e);
            vv.addEdge(e.counterpart);
        }

        sc.close();

        FlowGraph fg = new FlowGraph(vertices[s], vertices[t]);
        return fg;
    }

    static void solve(FlowGraph g) {
        PathFinder pathFinder = new PathFinder(g);
        while(true) {
            // Find a path
            List<Edge> path = pathFinder.getPath();
            // If no path, get edges out of reachable set of vertices and return it.
            if(path.size() == 0) {
                return;
            }
            // If there is a path, find bottleneck
            int min = Integer.MAX_VALUE;
            for(Edge e : path) {
                if(e.capacity < min)
                    min = e.capacity;
            }
            // Subtract bottleneck value from all edges in path
            for(Edge e : path) {
                e.setCapacity(e.capacity - min);
            }
            // Start over
        }
    }

    static void printSolution(FlowGraph fg) {
        int flow = 0;
        for (Edge e : fg.source.edges) {
            if (!e.isReverse) {
                flow += e.getTotalCapacity() - e.capacity;
            }
        }
        
        List<Edge> outputs = getFlowingEdges(fg);
        Collections.sort(outputs);

        System.out.print(flow);
        System.out.print(' ');
        System.out.println(outputs.size());
        for (Edge e : outputs) {
            System.out.println(e.from.id + " " + e.to.id + " " + (e.getTotalCapacity() - e.capacity));
        }
    }

    static List<Edge> getFlowingEdges(FlowGraph fg) {
        LinkedList<Edge> queue = new LinkedList<>();
        List<Edge> edges = new ArrayList<Edge>();
        HashSet<Vertex> visited = new HashSet<>();
        
        for (Edge e : fg.source.edges) {
            if (!e.isReverse) {
                queue.add(e);
            }
        }
        
        while (!queue.isEmpty()) {
            Edge e = queue.pop();
            if (e.capacity != e.getTotalCapacity()) {
                edges.add(e);
            }
            if (!visited.contains(e.to)) {
                for (Edge e2 : e.to.edges) {
                    if (!e2.isReverse) {
                        queue.add(e2);
                    } 
                }
                visited.add(e.to);
            }
        }

        return edges;
    }

    static class PathFinder {
        private Vertex s;
        private Vertex t;
        private FlowGraph g;
        
        public PathFinder(FlowGraph g) {
            this.s = g.source;
            this.t = g.sink;
            this.g = g;
        }
        
        public List<Edge> getPath() {
            HashSet<Vertex> visited = new HashSet<>();
            SinglyLinkedList<Edge> l = dfs(s, t, visited, new SinglyLinkedList<>());
            List<Edge> path = new ArrayList<>();
            for (Edge e : l) {
                path.add(e);
            }
            return path;
        }
        
        public SinglyLinkedList<Edge> dfs(Vertex v, Vertex t, HashSet<Vertex> visited, SinglyLinkedList<Edge> path) {
            visited.add(v);
            
            for (Edge e : v.edges) {
                Vertex w = e.to;
    
                if (e.capacity == 0) {
                    continue;
                }
                
                if (w == t) {
                    return path.add(e);
                }
                
                if (!visited.contains(w)) {
                    SinglyLinkedList<Edge> path2 = dfs(w, t, visited, path.add(e));
                    if(path2.first.val.to == t) {
                        return path2;
                    }
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
                        return current != null;
                    }
                    
                    public T next() {
                        T val = current.val;
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

    static class Vertex {
        int id;
        Set<Edge> edges; //edges going from this node

        public Vertex(int id){
            this.id = id;
            this.edges = new HashSet<>();
        }

        public void addEdge(Edge e) {
            this.edges.add(e);
        }
    }

    static class Edge implements Comparable<Edge> {
        int capacity;
        Vertex from;
        Vertex to;
        Edge counterpart;
        boolean isReverse;

        public Edge(Vertex from, Vertex to, int capacity) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
            this.counterpart = new Edge(to, from, this);
            isReverse = false;
        }

        private Edge(Vertex from, Vertex to, Edge counterpart) {
            this.from = from;
            this.to = to;
            this.capacity = 0;
            this.counterpart = counterpart;
            isReverse = true;
        }

        public void setCapacity(int value) {
            int difference = this.capacity - value;
            this.capacity = value;
            counterpart.capacity = difference + counterpart.capacity;
        }

        public int getTotalCapacity() {
            return capacity + counterpart.capacity;
        }

        public int compareTo(Edge e) {
            if (this.from.id == e.from.id) {
                return Integer.compare(this.to.id, e.to.id);
            }
            else {
                return Integer.compare(this.from.id, e.from.id);
            }
        }
    }

    static class FlowGraph {
        Vertex source;
        Vertex sink;

        public FlowGraph(Vertex source, Vertex sink) {
            this.source = source;
            this.sink = sink;
        }
    }
}