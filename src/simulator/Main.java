package simulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	// TODO Auto-generated method stub
	
	
	public static void main(String[] args) throws IOException {
		ReadInput rd = new ReadInput("input.txt");
		rd.readInput();
		int time = 2000;
		Simulator sim = new Simulator(rd.getPrefixList(),rd.getNodeList(),rd.getEdgeList(),time);
		//Write 3 for 3-way dijsktra or 1 for 1-way dijsktra
		sim.run(1);
	}


}
