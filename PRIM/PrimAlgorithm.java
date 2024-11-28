import java.util.Arrays;

public class PrimAlgorithm {
    // Número de vértices no grafo
    private static final int V = 5;

    // Função para encontrar o vértice com o menor peso que ainda não foi incluído na MST
    private static int minKey(int[] key, boolean[] mstSet) {
        int min = Integer.MAX_VALUE, minIndex = -1;

        for (int v = 0; v < V; v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                minIndex = v;
            }
        }
        return minIndex;
    }

    // Função para imprimir todas as arestas e pesos do grafo
    private static void printGraph(int[][] graph) {
        System.out.println("=================================");
        System.out.println("        GRAFO ORIGINAL          ");
        System.out.println("=================================");
        System.out.println("Arestas        Peso");
        System.out.println("-------        ----");

        for (int i = 0; i < V; i++) {
            for (int j = i + 1; j < V; j++) { // Evita duplicar arestas
                if (graph[i][j] != 0) {
                    System.out.printf(" %d - %d         %2d\n", i, j, graph[i][j]);
                }
            }
        }
    }

    // Função para imprimir a MST
    private static void printMST(int[] parent, int[][] graph) {
        System.out.println("\n=================================");
        System.out.println("    ÁRVORE GERADORA MÍNIMA      ");
        System.out.println("=================================");
        System.out.println("Arestas        Peso");
        System.out.println("-------        ----");

        for (int i = 1; i < V; i++) {
            System.out.printf(" %d - %d         %2d\n", parent[i], i, graph[i][parent[i]]);
        }

        System.out.println("\nRepresentação Visual da MST:");
        for (int i = 1; i < V; i++) {
            System.out.printf("(%d) --%d--> (%d)\n", parent[i], graph[i][parent[i]], i);
        }
    }

    // Função principal que calcula a MST usando o Algoritmo de Prim
    public static void primMST(int[][] graph) {
        // Array para armazenar a MST
        int[] parent = new int[V];

        // Chaves usadas para escolher a aresta de menor peso
        int[] key = new int[V];

        // Para acompanhar os vértices incluídos na MST
        boolean[] mstSet = new boolean[V];

        // Inicializa todas as chaves com um valor infinito
        Arrays.fill(key, Integer.MAX_VALUE);

        // Sempre comece do primeiro vértice
        key[0] = 0;
        parent[0] = -1; // O primeiro nó é sempre a raiz

        // Construa a MST com V-1 arestas
        for (int count = 0; count < V - 1; count++) {
            // Escolha o vértice de menor peso que ainda não está na MST
            int u = minKey(key, mstSet);

            // Adicione o vértice escolhido ao conjunto da MST
            mstSet[u] = true;

            // Atualize os valores das chaves e os pais dos vértices adjacentes
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !mstSet[v] && graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
            }
        }

        // Imprima a MST
        printMST(parent, graph);
    }

    public static void main(String[] args) {
        // Exemplo de grafo representado como matriz de adjacência
        int[][] graph = {
            {0, 2, 0, 6, 0},
            {2, 0, 3, 8, 5},
            {0, 3, 0, 0, 7},
            {6, 8, 0, 0, 9},
            {0, 5, 7, 9, 0}
        };

        // Imprima o grafo inicial
        printGraph(graph);

        // Calcula a MST
        primMST(graph);
    }
}
