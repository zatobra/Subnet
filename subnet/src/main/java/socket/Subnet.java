package socket;

import java.util.ArrayList;
import java.util.List;

import system.SystemNode;

public class Subnet {
    private String id;
    private List<SystemNode> systems;

    public Subnet(String id, int numberOfSystems) {
        this.id = id;
        systems = new ArrayList<>();
        for (int i = 1; i <= numberOfSystems; i++) {
            systems.add(new SystemNode(id + i));
        }
    }

    public String getId() {
        return id;
    }

    public List<SystemNode> getSystems() {
        return systems;
    }
}