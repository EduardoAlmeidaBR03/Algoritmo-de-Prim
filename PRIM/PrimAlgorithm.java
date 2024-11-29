import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

public class PrimAlgorithm {
    // Número de vértices no grafo
    private static int V;

    // Função para imprimir todas as arestas e pesos do grafo
    private static void printGraph(int[][] graph) {
        System.out.println("=================================");
        System.out.println("        GRAFO ORIGINAL          ");
        System.out.println("=================================");
        System.out.println("Arestas        Peso");
        System.out.println("-------        ----");

        int totalPeso = 0; // Para somar o peso total das arestas
        for (int i = 0; i < V; i++) {
            for (int j = i + 1; j < V; j++) { // Evita duplicar arestas
                if (graph[i][j] != 0) {
                    System.out.printf(" %d - %d         %2d\n", i, j, graph[i][j]);
                    totalPeso += graph[i][j]; // Adiciona o peso da aresta ao total
                }
            }
        }

        // Escrever no arquivo de texto o número de arestas e o peso total antes do
        // algoritmo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resultadoPRIM.txt", true))) {
            writer.write("=================================\n");
            writer.write("        GRAFO ORIGINAL          \n");
            writer.write("=================================\n");
            writer.write("Número de arestas: " + getEdgeCount(graph) + "\n");
            writer.write("Peso total das arestas: " + totalPeso + "\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Função para imprimir a MST
    private static void printMST(int[] parent, int[][] graph) {
        System.out.println("\n=================================");
        System.out.println("    ÁRVORE GERADORA MÍNIMA      ");
        System.out.println("=================================");
        System.out.println("Arestas        Peso");
        System.out.println("-------        ----");

        int totalPesoMST = 0; // Para somar o peso total da MST
        for (int i = 1; i < V; i++) {
            System.out.printf(" %d - %d         %2d\n", parent[i], i, graph[i][parent[i]]);
            totalPesoMST += graph[i][parent[i]]; // Adiciona o peso da aresta ao total
        }

        // Escrever no arquivo de texto o peso total da MST depois do algoritmo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resultadoPRIM.txt", true))) {
            writer.write("\n=================================\n");
            writer.write("    ÁRVORE GERADORA MÍNIMA      \n");
            writer.write("=================================\n");
            writer.write("Número de arestas na MST: " + (V - 1) + "\n");
            writer.write("Peso total da MST: " + totalPesoMST + "\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Função para calcular o número de arestas no grafo
    private static int getEdgeCount(int[][] graph) {
        int count = 0;
        for (int i = 0; i < V; i++) {
            for (int j = i + 1; j < V; j++) {
                if (graph[i][j] != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    // Função principal que calcula a MST usando o Algoritmo de Prim com fila de
    // prioridade
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

        // Utiliza uma fila de prioridade (min-heap) para escolher o vértice com o menor
        // peso
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));

        // Adiciona o primeiro vértice à fila de prioridade
        pq.offer(new int[] { 0, 0 }); // {vértice, peso}

        // Construa a MST com V-1 arestas
        while (!pq.isEmpty()) {
            // Remove o vértice de menor peso da fila
            int[] minNode = pq.poll();
            int u = minNode[0]; // Vértice escolhido

            // Se o vértice já está na MST, continue
            if (mstSet[u])
                continue;

            // Adicione o vértice à MST
            mstSet[u] = true;

            // Atualize os valores das chaves e os pais dos vértices adjacentes
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !mstSet[v] && graph[u][v] < key[v]) {
                    // Atualiza o peso da chave
                    key[v] = graph[u][v];
                    parent[v] = u;

                    // Adiciona o vértice na fila de prioridade com o novo peso
                    pq.offer(new int[] { v, key[v] });
                }
            }
        }

        // Imprima a MST
        printMST(parent, graph);
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
            primMST(graph);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
