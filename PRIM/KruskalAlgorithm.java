import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

public class KruskalAlgorithm {
    // Número de vértices no grafo
    private static int V;

    // Classe para representar uma aresta
    static class Edge implements Comparable<Edge> {
        int src, dest, weight;

        // Construtor
        Edge(int src, int dest, int weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }

        // Método de comparação para ordenar as arestas por peso
        @Override
        public int compareTo(Edge other) {
            return this.weight - other.weight;
        }
    }

    // Função para imprimir todas as arestas e pesos do grafo
    private static void printGraph(int[][] graph) {
        System.out.println("=================================");
        System.out.println("        GRAFO ORIGINAL          ");
        System.out.println("=================================");
        System.out.println("Arestas        Peso");
        System.out.println("-------        ----");

        int totalPeso = 0; // Para somar o peso total das arestas
        List<Edge> edges = new ArrayList<>();

        // Adiciona as arestas no formato (src, dest, peso) e soma o peso
        for (int i = 0; i < V; i++) {
            for (int j = i + 1; j < V; j++) { // Evita duplicar arestas
                if (graph[i][j] != 0) {
                    edges.add(new Edge(i, j, graph[i][j]));
                    totalPeso += graph[i][j]; // Adiciona o peso da aresta ao total
                }
            }
        }

        // Escrever no arquivo de texto o número de arestas e o peso total antes do
        // algoritmo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resultadoKRUSKAL.txt", true))) {
            writer.write("=================================\n");
            writer.write("        GRAFO ORIGINAL          \n");
            writer.write("=================================\n");
            writer.write("Número de arestas: " + edges.size() + "\n");
            writer.write("Peso total das arestas: " + totalPeso + "\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Função para imprimir a MST
    private static void printMST(List<Edge> mst) {
        System.out.println("\n=================================");
        System.out.println("    ÁRVORE GERADORA MÍNIMA      ");
        System.out.println("=================================");
        System.out.println("Arestas        Peso");
        System.out.println("-------        ----");

        int totalPesoMST = 0; // Para somar o peso total da MST
        for (Edge e : mst) {
            System.out.printf(" %d - %d         %2d\n", e.src, e.dest, e.weight);
            totalPesoMST += e.weight; // Adiciona o peso da aresta ao total
        }

        // Escrever no arquivo de texto o peso total da MST depois do algoritmo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resultadoKRUSKAL.txt", true))) {
            writer.write("\n=================================\n");
            writer.write("    ÁRVORE GERADORA MÍNIMA      \n");
            writer.write("=================================\n");
            writer.write("Número de arestas na MST: " + mst.size() + "\n");
            writer.write("Peso total da MST: " + totalPesoMST + "\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Função para encontrar o representante de um conjunto (representante raiz)
    private static int find(int[] parent, int i) {
        if (parent[i] == i) {
            return i;
        }
        // Caminhamento de compressão
        parent[i] = find(parent, parent[i]);
        return parent[i];
    }

    // Função para unir dois conjuntos
    private static void union(int[] parent, int[] rank, int x, int y) {
        int rootX = find(parent, x);
        int rootY = find(parent, y);

        if (rootX != rootY) {
            if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
        }
    }

    // Função principal que calcula a MST usando o Algoritmo de Kruskal
    public static void kruskalMST(int[][] graph) {
        List<Edge> edges = new ArrayList<>();

        // Adiciona as arestas no formato (src, dest, peso)
        for (int i = 0; i < V; i++) {
            for (int j = i + 1; j < V; j++) { // Evita duplicar arestas
                if (graph[i][j] != 0) {
                    edges.add(new Edge(i, j, graph[i][j]));
                }
            }
        }

        // Ordena as arestas pelo peso
        Collections.sort(edges);

        // Inicializa o array de pais e o de rank para o Union-Find
        int[] parent = new int[V];
        int[] rank = new int[V];
        for (int i = 0; i < V; i++) {
            parent[i] = i; // Cada vértice é seu próprio pai inicialmente
            rank[i] = 0;
        }

        // Lista para armazenar as arestas da MST
        List<Edge> mst = new ArrayList<>();

        // Processa as arestas, sempre que uma aresta não formar ciclo, adiciona à MST
        for (Edge e : edges) {
            int x = find(parent, e.src);
            int y = find(parent, e.dest);

            // Se adicionar essa aresta não formar ciclo, inclui na MST
            if (x != y) {
                mst.add(e);
                union(parent, rank, x, y);
            }
        }

        // Imprime a MST
        printMST(mst);
    }

    // Função para ler o XML e extrair a matriz de adjacência
    public static int[][] parseXML(String filePath) throws Exception {
        File xmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        // Obter a lista de vértices
        NodeList vertexList = doc.getElementsByTagName("vertex");
        V = vertexList.getLength(); // Número de vértices

        // Inicializa a matriz de adjacência
        int[][] graph = new int[V][V];

        // Preenche a matriz de adjacência com os custos das arestas
        for (int i = 0; i < V; i++) {
            Node vertexNode = vertexList.item(i);
            if (vertexNode.getNodeType() == Node.ELEMENT_NODE) {
                Element vertexElement = (Element) vertexNode;
                NodeList edgeList = vertexElement.getElementsByTagName("edge");

                for (int j = 0; j < edgeList.getLength(); j++) {
                    Node edgeNode = edgeList.item(j);
                    if (edgeNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element edgeElement = (Element) edgeNode;

                        try {
                            // Verifica se o conteúdo do vértice é válido
                            int targetVertex = Integer.parseInt(edgeElement.getTextContent()) - 1; // Índice do vértice
                                                                                                   // (base 0)

                            if (targetVertex >= 0 && targetVertex < V) { // Verifica se o índice é válido
                                double cost = Double.parseDouble(edgeElement.getAttribute("cost"));
                                graph[i][targetVertex] = (int) cost; // Preenche a matriz de adjacência
                            } else {
                                System.out.println("Índice de vértice inválido: " + targetVertex);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Erro ao ler o vértice ou custo: " + edgeElement.getTextContent());
                        }
                    }
                }
            }
        }
        return graph;
    }

    public static void main(String[] args) {
        try {
            // Lê o arquivo XML e constrói a matriz de adjacência
            int[][] graph = parseXML("./a280.xml");

            // Imprime o grafo original
            printGraph(graph);

            // Calcula a MST
            kruskalMST(graph);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
