package socket;

import java.util.*;

import system.SystemNode;
import system.SystemNodeDistance;

public class Network {
    private Map<String, Subnet> subnets = new HashMap<>();
    private Map<String, Map<String, Integer>> connections = new HashMap<>();

    // Create Subnet
    public void createSubnet(String subnetId, int numberOfSystems) {
        Subnet subnet = new Subnet(subnetId, numberOfSystems);
        subnets.put(subnetId, subnet);
        System.out.println("Subnet " + subnetId + " with " + numberOfSystems + " systems created.");
    }

    // Connect Systems
    public void connectSystems(String system1, String system2, int cost) {
        connections.putIfAbsent(system1, new HashMap<>());
        connections.putIfAbsent(system2, new HashMap<>());
        connections.get(system1).put(system2, cost);
        connections.get(system2).put(system1, cost);
        System.out.println("Connection established between " + system1 + " and " + system2 + " with cost " + cost);
    }

    // Find Shortest Path using Dijkstra's Algorithm
    public void findShortestPath(String source, String destination) {
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<SystemNodeDistance> pq = new PriorityQueue<>(Comparator.comparingInt(SystemNodeDistance::getDistance));

        for (String node : connections.keySet()) {
            distances.put(node, Integer.MAX_VALUE);
            previous.put(node, null);
        }
        distances.put(source, 0);
        pq.add(new SystemNodeDistance(source, 0));

        while (!pq.isEmpty()) {
            SystemNodeDistance current = pq.poll();
            String currentNode = current.getSystem();

            for (Map.Entry<String, Integer> neighbor : connections.get(currentNode).entrySet()) {
                String neighborNode = neighbor.getKey();
                int newDist = distances.get(currentNode) + neighbor.getValue();
                if (newDist < distances.get(neighborNode)) {
                    distances.put(neighborNode, newDist);
                    previous.put(neighborNode, currentNode);
                    pq.add(new SystemNodeDistance(neighborNode, newDist));
                }
            }
        }

        // Output the shortest path
        if (distances.get(destination) == Integer.MAX_VALUE) {
            System.out.println("No path found from " + source + " to " + destination);
        } else {
            List<String> path = new ArrayList<>();
            String step = destination;
            while (step != null) {
                path.add(step);
                step = previous.get(step);
            }
            Collections.reverse(path);
            System.out.println("Shortest path from " + source + " to " + destination + ": " + String.join(" -> ", path) + " with cost " + distances.get(destination));
        }
    }
    public void sendPacket(String source, String destination, String message) {
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<SystemNodeDistance> pq = new PriorityQueue<>(Comparator.comparingInt(SystemNodeDistance::getDistance));

        for (String node : connections.keySet()) {
            distances.put(node, Integer.MAX_VALUE);
            previous.put(node, null);
        }
        distances.put(source, 0);
        pq.add(new SystemNodeDistance(source, 0));

        while (!pq.isEmpty()) {
            SystemNodeDistance current = pq.poll();
            String currentNode = current.getSystem();

            for (Map.Entry<String, Integer> neighbor : connections.get(currentNode).entrySet()) {
                String neighborNode = neighbor.getKey();
                int newDist = distances.get(currentNode) + neighbor.getValue();
                if (newDist < distances.get(neighborNode)) {
                    distances.put(neighborNode, newDist);
                    previous.put(neighborNode, currentNode);
                    pq.add(new SystemNodeDistance(neighborNode, newDist));
                }
            }
        }

        if (distances.get(destination) == Integer.MAX_VALUE) {
            System.out.println("No path found from " + source + " to " + destination);
        } else {
            List<String> path = new ArrayList<>();
            String step = destination;
            while (step != null) {
                path.add(step);
                step = previous.get(step);
            }
            Collections.reverse(path);
            System.out.println("Packet sent from " + source + " to " + destination + " through path: " + String.join(" -> ", path) + ". Message: \"" + message + "\"");
        }
    }
    public void removeSystem(String systemId) {
        if (!connections.containsKey(systemId)) {
            System.out.println("System " + systemId + " does not exist.");
            return;
        }

        connections.remove(systemId);  // Remove all connections originating from this system
        for (Map<String, Integer> neighbors : connections.values()) {
            neighbors.remove(systemId);  // Remove all connections leading to this system
        }

        // Remove the system from its subnet
        for (Subnet subnet : subnets.values()) {
            subnet.getSystems().removeIf(system -> system.getId().equals(systemId));
        }

        System.out.println("System " + systemId + " removed.");
    }
    
    public void removeSubnet(String subnetId) {
        if (!subnets.containsKey(subnetId)) {
            System.out.println("Subnet " + subnetId + " does not exist.");
            return;
        }

        Subnet subnet = subnets.get(subnetId);
        for (SystemNode system : subnet.getSystems()) {
            removeSystem(system.getId());
        }

        subnets.remove(subnetId);  // Remove the subnet
        System.out.println("Subnet " + subnetId + " removed.");
    }
    public void showTopology() {
        System.out.println("Subnets and Systems:");
        for (Subnet subnet : subnets.values()) {
            System.out.print("Subnet " + subnet.getId() + ": ");
            for (SystemNode system : subnet.getSystems()) {
                System.out.print(system.getId() + " ");
            }
            System.out.println();
        }

        System.out.println("Connections:");
        for (String system : connections.keySet()) {
            for (Map.Entry<String, Integer> neighbor : connections.get(system).entrySet()) {
                System.out.println(system + " <-> " + neighbor.getKey() + " with cost " + neighbor.getValue());
            }
        }
    }
}