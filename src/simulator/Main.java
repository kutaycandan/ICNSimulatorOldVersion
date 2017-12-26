package simulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	// TODO Auto-generated method stub
	// Sırf mainde çalıştırabilmek için static olmamsı gereken şeyleri static yapmak yanlış
	//Bunun başka yolları da var.
	//O listelerin mainde olmasının zaten gereği yoktu
	//Gerçi bu case için static olmaları yanlış değil zira bütün projece kullanılabilir durumdalar
	//hatta bir tane edge hashMap bile eklenebilir bence ki O(1) de edge bulalım.
	
	
	public static void main(String[] args) {
		ReadInput rd = new ReadInput("input.txt");
		rd.readInput();
		Simulator sim = new Simulator(rd.getPrefixList(),rd.getNodeList(),rd.getEdgeList());
		sim.run();
	}


}
