package simulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList <Node> nodes = new ArrayList<Node>();
		ArrayList <Edge> edges = new ArrayList<Edge>();
		ArrayList <Prefix> prefixes = new ArrayList<Prefix>();
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
				Node node = new Node(i+1);
				nodes.add(node);
			}
			sc.nextLine();
			for(int i = 0 ; i < numberOfNodes ; i ++) {
				String[] nodeInfo = sc.nextLine().split("-");
				for(int j = 0 ; j < nodeInfo.length -1 ; j ++ ) {
					nodes.get(i+1).addServedPrefix(nodeInfo[j+1]);
				}
			}
			sc.nextLine();
			for(int i = 0 ; i < numberOfNodes ; i ++) {
				String[] nodeInfo = sc.nextLine().split("-");
				for(int j = 0 ; j < nodeInfo.length -1 ; j ++ ) {
					nodes.get(i+1).addDemandedPrefix(nodeInfo[j+1]);
				}
			}
			
			int numberOfEdges = Integer.parseInt(sc.nextLine().split("-")[1]);
			for(int i = 0 ; i < numberOfEdges ; i ++) {
				String[] edgeInfo = sc.nextLine().split("-");
				Edge edge = new Edge (Integer.parseInt(edgeInfo[0]),Integer.parseInt(edgeInfo[1]),Integer.parseInt(edgeInfo[2]));
				edges.add(edge);
			}
			Simulator sim = new Simulator(prefixes,nodes,edges);
			sim.run();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Has file problem.");
		}
	}

}