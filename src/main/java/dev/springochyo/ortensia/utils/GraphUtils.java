package dev.springochyo.ortensia.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GraphUtils {
    private GraphUtils() {
    }

    public static List<String> kahnsAlgorithm(Map<String, List<String>> graph) {
        List<String> queue = new ArrayList<>();

        while (!graph.isEmpty()) {
            boolean found = false;
            for (String key : graph.keySet()) {
                if (graph.get(key).isEmpty()) {
                    found = true;
                    queue.add(key);
                }
            }

            if (!found)
                throw new CycleException("Cyclic dependencies");

            queue.forEach(graph.keySet()::remove);

            for (String key : graph.keySet())
                graph.get(key).removeAll(queue);
        }

        return queue;
    }
}