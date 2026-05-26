import java.io.File;
import java.io.IOException;

public class Driver {
	public static void main(String [] args) {
		Polynomial p = new Polynomial();
		System.out.println(p.evaluate(3));
		double [] c1 = {6,5};
		int [] e1 = {0,3};
		Polynomial p1 = new Polynomial(c1,e1);
		double [] c2 = {-2,-9};
		int [] e2 = {1,4};
		Polynomial p2 = new Polynomial(c2,e2);
		Polynomial s = p1.add(p2);
		System.out.println("s(0.1) = " + s.evaluate(0.1));
		if(s.hasRoot(1))
			System.out.println("1 is a root of s");
		else
			System.out.println("1 is not a root of s");
		Polynomial p3 = p1.multiply(p2);
		System.out.println("p3(2) = " + p3.evaluate(2));
		String filename = "test.txt";
		File file = new File(filename);
		try{
			p3.saveToFile(filename);
			Polynomial p3FromFile = new Polynomial(file);
			System.out.println("File loaded polynomial at x=3: " + p3FromFile.evaluate(3));
		}catch(IOException e){
			System.err.println(e.getMessage());
		}
		
	}
}