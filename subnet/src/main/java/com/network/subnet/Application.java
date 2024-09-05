package com.network.subnet;
import java.util.Scanner;

import socket.Network;

public class Application {

	public static void main(String[] args) {
        Network network = new Network();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter command:");
            String command = scanner.nextLine();
            String[] parts = command.split(" ");

            switch (parts[0]) {
                case "create":
                    network.createSubnet(parts[2], Integer.parseInt(parts[3]));
                    break;
                case "connect":
                    network.connectSystems(parts[1], parts[2], Integer.parseInt(parts[3]));
                    break;
                case "route":
                    network.findShortestPath(parts[1], parts[2]);
                    break;
                case "send":
                    network.sendPacket(parts[1], parts[2], command.substring(command.indexOf("\"") + 1, command.lastIndexOf("\"")));
                    break;
                case "remove":
                    if (parts[1].equals("system")) {
                        network.removeSystem(parts[2]);
                    } else if (parts[1].equals("subnet")) {
                        network.removeSubnet(parts[2]);
                    }
                    break;
                case "show":
                    network.showTopology();
                    break;
                case "exit":
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid command.");
            }
        }
    }
   }