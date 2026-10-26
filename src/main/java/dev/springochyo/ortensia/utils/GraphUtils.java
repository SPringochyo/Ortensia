package dev.springochyo.ortensia.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GraphUtils {
    private GraphUtils() {}

    public static List<String> KhansAlgorithm(Map<String, List<String>> graph) {

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
}
