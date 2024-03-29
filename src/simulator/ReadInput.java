package simulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ReadInput {
	String inputFileName;
	ArrayList <Node> nodes = new ArrayList<Node>();
	HashMap <String,Edge> edges = new HashMap<String,Edge>();
	ArrayList <Prefix> prefixes = new ArrayList<Prefix>();
	public ReadInput(String inputFileName) {
		this.inputFileName=inputFileName;
		
	}
	public  void readInput() {
		File file = new File(inputFileName);
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
					prefixes.get(getPrefixID(nodeInfo[j+1])).addServingNode(i);
					nodes.get(Integer.parseInt(nodeInfo[0])).addServedPrefix(nodeInfo[j+1]);
				}
			}
			sc.nextLine();
			for(int i = 0 ; i < numberOfNodes ; i ++) {
				String[] nodeInfo = sc.nextLine().split("-");
				for(int j = 0 ; j < nodeInfo.length -1 ; j ++ ) {
					nodes.get(Integer.parseInt(nodeInfo[0])).addDemandedPrefix(nodeInfo[j+1]);
				}
			}

			int numberOfEdges = Integer.parseInt(sc.nextLine().split("-")[1]);
			for(int i = 0 ; i < numberOfEdges ; i ++) {
				String[] edgeInfo = sc.nextLine().split("-");
				Edge edge1 = new Edge (Integer.parseInt(edgeInfo[0]),Integer.parseInt(edgeInfo[1]),Integer.parseInt(edgeInfo[2]));
				Edge edge2 = new Edge (Integer.parseInt(edgeInfo[1]),Integer.parseInt(edgeInfo[0]),Integer.parseInt(edgeInfo[2]));
				edges.put(edgeInfo[0]+"-"+edgeInfo[1],edge1);
				edges.put(edgeInfo[1]+"-"+edgeInfo[0],edge2);
				nodes.get(Integer.parseInt(edgeInfo[0])).addEdge(Integer.parseInt(edgeInfo[1]),edge1);
				nodes.get(Integer.parseInt(edgeInfo[1])).addEdge(Integer.parseInt(edgeInfo[0]),edge2);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Has file problem.");
		}
	}
	public  ArrayList <Node> getNodeList(){
		return nodes;
	}
	public  HashMap <String,Edge> getEdgeList(){
		return edges;
	}
	public  ArrayList <Prefix> getPrefixList(){
		return prefixes;
	}
	public int getPrefixID(String prefixName) {
		int id=-1;
		for(int i = 0 ; i < prefixes.size();i++) {
			if(prefixes.get(i).getName().equalsIgnoreCase(prefixName)) {
				return i;
			}
		}
		return id;
	}
}
