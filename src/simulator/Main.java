package simulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	// TODO Auto-generated method stub
	static ArrayList <Node> nodes = new ArrayList<Node>();
	static ArrayList <Edge> edges = new ArrayList<Edge>();
	static ArrayList <Prefix> prefixes = new ArrayList<Prefix>();
	public static void main(String[] args) {
		initialize();
		Simulator sim = new Simulator(prefixes,nodes,edges);
		sim.run();
	}

	public static void initialize() {
		File file = new File("input.txt");
		Scanner sc;
		try {
			sc = new Scanner(file);
			int numberOfPrefixes=Integer.parseInt(sc.nextLine().split("-")[1]);
			for(int i = 0 ; i < numberOfPrefixes ; i ++) {
				String[] prefixInfo = sc.nextLine().split("-");
				Prefix prefix = new Prefix(prefixInfo[0],Integer.parseInt(prefixInfo[1]));
				prefixes.add(prefix);
			}
			int numberOfNodes = Integer.parseInt(sc.nextLine().split("-")[1]);
			for(int i = 0 ; i < numberOfNodes ; i ++) {
				Node node = new Node(i);
				nodes.add(node);
			}
			sc.nextLine();
			for(int i = 0 ; i < numberOfNodes ; i ++) {
				String[] nodeInfo = sc.nextLine().split("-");
				for(int j = 0 ; j < nodeInfo.length -1 ; j ++ ) {
					nodes.get(i).addServedPrefix(nodeInfo[j+1]);
				}
			}
			sc.nextLine();
			for(int i = 0 ; i < numberOfNodes ; i ++) {
				String[] nodeInfo = sc.nextLine().split("-");
				for(int j = 0 ; j < nodeInfo.length -1 ; j ++ ) {
					nodes.get(i).addDemandedPrefix(nodeInfo[j+1]);
				}
			}

			int numberOfEdges = Integer.parseInt(sc.nextLine().split("-")[1]);
			for(int i = 0 ; i < numberOfEdges ; i ++) {
				String[] edgeInfo = sc.nextLine().split("-");
				Edge edge1 = new Edge (Integer.parseInt(edgeInfo[0]),Integer.parseInt(edgeInfo[1]),Integer.parseInt(edgeInfo[2]));
				Edge edge2 = new Edge (Integer.parseInt(edgeInfo[1]),Integer.parseInt(edgeInfo[0]),Integer.parseInt(edgeInfo[2]));
				edges.add(edge1);
				edges.add(edge2);
				nodes.get(Integer.parseInt(edgeInfo[0])).addEdge(edge1);
				nodes.get(Integer.parseInt(edgeInfo[1])).addEdge(edge2);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Has file problem.");
		}
	}

}
