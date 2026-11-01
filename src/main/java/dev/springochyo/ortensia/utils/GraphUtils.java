package dev.springochyo.ortensia.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GraphUtils {
    private GraphUtils() {}

    public static List<String> KhansAlgorithm(Map<String, List<String>> graph) {
        if (hasCycle(graph))
            throw new CycleException("Cyclic dependencies");

        List<String> queue = new ArrayList<>();

        while (!graph.isEmpty()) {
            for (String key : graph.keySet()) {
                if (graph.get(key).isEmpty())
                    queue.add(key);
            }

            queue.forEach(graph.keySet()::remove);

            for (String key : graph.keySet())
                graph.get(key).removeAll(queue);
        }

        return queue;
    }

    private static boolean hasCycle(Map<String, List<String>> graph) {
        List<String> visited = new ArrayList<>();
        List<String> currentPath = new ArrayList<>();

        for (String vertex : graph.keySet()) {
            if (dfs(graph, vertex, visited, currentPath)) {
                return true;
            }
        }

        return false;
    }

    private static boolean dfs(Map<String, List<String>> graph, String vertex, List<String> visited, List<String> currentPath) {
        visited.add(vertex);
        currentPath.add(vertex);

        for (String neighbor : graph.get(vertex)) {
            if (!visited.contains(neighbor) && dfs(graph, neighbor, visited, currentPath)) {
                return true;
            } else if (currentPath.contains(neighbor)) {
                return true;
            }
        }

        currentPath.remove(vertex);
        return false;
    }
}
