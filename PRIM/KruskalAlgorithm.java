import java.util.*;

public class KruskalAlgorithm {
    // Classe para representar uma aresta
    static class Edge implements Comparable<Edge> {
        int src, dest, weight;

        // Comparar arestas com base no peso
        public int compareTo(Edge other) {
            return this.weight - other.weight;
        }
    }

    // Classe para representar um subconjunto usado no algoritmo Union-Find
    static class Subset {
        int parent, rank;
    }

    // Função para encontrar o conjunto de um elemento (com compressão de caminho)
    static int find(Subset[] subsets, int node) {
        if (subsets[node].parent != node) {
            subsets[node].parent = find(subsets, subsets[node].parent);
        }
        return subsets[node].parent;
    }

    // Função para unir dois subconjuntos (por rank)
    static void union(Subset[] subsets, int x, int y) {
        int rootX = find(subsets, x);
        int rootY = find(subsets, y);

        if (subsets[rootX].rank < subsets[rootY].rank) {
            subsets[rootX].parent = rootY;
        } else if (subsets[rootX].rank > subsets[rootY].rank) {
            subsets[rootY].parent = rootX;
        } else {
            subsets[rootY].parent = rootX;
            subsets[rootX].rank++;
        }
    }

    // Função para imprimir todas as arestas e seus pesos
    static void printGraph(Edge[] edges) {
        System.out.println("=================================");
        System.out.println("        GRAFO ORIGINAL          ");
        System.out.println("=================================");
        System.out.println("Arestas        Peso");
        System.out.println("-------        ----");

        for (Edge edge : edges) {
            System.out.printf(" %d - %d         %2d\n", edge.src, edge.dest, edge.weight);
        }
    }

    // Função para executar o algoritmo de Kruskal
    static void kruskalMST(Edge[] edges, int vertices) {
        // Ordenar todas as arestas pelo peso
        Arrays.sort(edges);

        // Array para armazenar a MST
        Edge[] result = new Edge[vertices - 1];
        int e = 0; // Contador para as arestas na MST

        // Subconjuntos para o Union-Find
        Subset[] subsets = new Subset[vertices];
        for (int i = 0; i < vertices; i++) {
            subsets[i] = new Subset();
            subsets[i].parent = i;
            subsets[i].rank = 0;
        }

        // Processar todas as arestas em ordem
        for (Edge edge : edges) {
            if (e == vertices - 1) break;

            int x = find(subsets, edge.src);
            int y = find(subsets, edge.dest);

            // Inclua a aresta se não formar um ciclo
            if (x != y) {
                result[e++] = edge;
                union(subsets, x, y);
            }
        }

        // Imprimir a MST resultante
        printMST(result);
    }

    // Função para imprimir a MST
    static void printMST(Edge[] result) {
        System.out.println("\n=================================");
        System.out.println("    ÁRVORE GERADORA MÍNIMA      ");
        System.out.println("=================================");
        System.out.println("Arestas        Peso");
        System.out.println("-------        ----");

        for (Edge edge : result) {
            System.out.printf(" %d - %d         %2d\n", edge.src, edge.dest, edge.weight);
        }

        System.out.println("\nRepresentação Visual da MST:");
        for (Edge edge : result) {
            System.out.printf("(%d) --%d--> (%d)\n", edge.src, edge.weight, edge.dest);
        }
    }

    public static void main(String[] args) {
        // Número de vértices
        int vertices = 5;

        // Exemplo de arestas do grafo (origem, destino, peso)
        Edge[] edges = {
            new Edge() {{ src = 0; dest = 1; weight = 2; }},
            new Edge() {{ src = 0; dest = 3; weight = 6; }},
            new Edge() {{ src = 1; dest = 2; weight = 3; }},
            new Edge() {{ src = 1; dest = 3; weight = 8; }},
            new Edge() {{ src = 1; dest = 4; weight = 5; }},
            new Edge() {{ src = 2; dest = 4; weight = 7; }},
            new Edge() {{ src = 3; dest = 4; weight = 9; }}
        };

        // Imprimir as arestas do grafo original
        printGraph(edges);

        // Executar o algoritmo de Kruskal
        kruskalMST(edges, vertices);
    }
}
