import java.util.*;

public class Prim {

    public static Map<Vertex, Vertex> prim(Graph graph, Map<Edge, Integer> weights, Vertex root) {
        Map<Vertex, Integer> key = new HashMap<>();
        Map<Vertex, Vertex> pi = new HashMap<>();
        Set<Vertex> q = new HashSet<>(graph.getVertices());

        for (Vertex u : graph.getVertices()) {
            key.put(u, Integer.MAX_VALUE);
            pi.put(u, null);
        }
        key.put(root, 0);

        while (!q.isEmpty()) {
            Vertex u = extractMin(q, key);
            q.remove(u);

            for (Vertex v : graph.getAdjacentVertices(u)) {
                Edge edge = new Edge(u, v);
                if (q.contains(v) && weights.get(edge) < key.get(v)) {
                    pi.put(v, u);
                    key.put(v, weights.get(edge));
                }
            }
        }

        return pi;
    }

    private static Vertex extractMin(Set<Vertex> q, Map<Vertex, Integer> key) {
        Vertex minVertex = null;
        int minValue = Integer.MAX_VALUE;
        for (Vertex v : q) {
            if (key.get(v) < minValue) {
                minValue = key.get(v);
                minVertex = v;
            }
        }
        return minVertex;
    }

    // ... (Classes auxiliares Graph, Vertex e Edge)
}