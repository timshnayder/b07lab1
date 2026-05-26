import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class Polynomial{
	private double[] coefficients;
	private int[] exponents;

	public Polynomial(){
		this.coefficients = new double[1];
        this.coefficients[0]=0.0;
		this.exponents = new int[1];
        this.exponents[0]=0;
	}
	public Polynomial(double[] coefficients, int[] exponents){
		this.coefficients = coefficients.clone();
		this.exponents = exponents.clone();
	}
	public Polynomial (File f) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String line = reader.readLine();
		if(line == null || line.trim().isEmpty()){
			this.coefficients = new double[1];
			this.coefficients[0]=0.0;
			this.exponents = new int[1];
			this.exponents[0]=0;
			reader.close();
			return;
		}
		line = line.trim();
		//positive lookahead of + and - so it doesn't remove the
		String[] terms = line.split("(?=[+-])");
		double[] coeff = new double[terms.length];
		int[] exp = new int[terms.length];

		for(int i =0; i < terms.length; i++){
			if(terms[i].contains("x")){
				String[] power = terms[i].split("x");
				//first handle coeff
				if(power[0].equals("-")){
					coeff[i]=-1.0;
				}else if(power[0].equals("+")||power[0].isEmpty()){
					coeff[i]=1.0;
				}else{
					coeff[i]=Double.parseDouble(power[0]);
				}

				//now handle exponenet
				if(power.length<2 ||power[1].isEmpty()){
					exp[i]=1;
				}else{
					exp[i]=Integer.parseInt(power[1]);
				}
			}else{
				//constant number
				exp[i]=0;
				coeff[i]=Double.parseDouble(terms[i]);
			}
		}
		reader.close();
		this.coefficients=coeff;
		this.exponents=exp;
	}

	public void saveToFile(String f) throws IOException{
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(f))){
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < this.coefficients.length; i++){
				double c = this.coefficients[i];
				int e = this.exponents[i];
				if(c == 0.0 && this.coefficients.length>1) continue;

				if(c > 0.0 && sb.length()>0) sb.append("+");

				if(e == 0){
					sb.append(c);
				}else{
					if(c==1.0){
						sb.append("x");
					}else if(c==-1.0){
						sb.append("-x");
					}else{
						sb.append(c);
						sb.append("x");
					}
					if(e > 1){
						sb.append(e);
					}
				}
			}
			if (sb.length() == 0){
            	sb.append("0");
        	}
			bw.write(sb.toString());	
		}
	}
	
	public Polynomial add(Polynomial p){
		int maxTerms = p.coefficients.length+this.coefficients.length;
		double[] tempCoeff = new double[maxTerms];
		int[] tempExpon = new int[maxTerms];

		int tempIdx=0;
		int pIdx=0;
		int thisIdx=0;
		while(pIdx < p.coefficients.length && thisIdx < this.coefficients.length){
			if(this.exponents[thisIdx] == p.exponents[pIdx]){
				if(this.coefficients[thisIdx]+p.coefficients[pIdx] !=0.0){
					tempCoeff[tempIdx]=this.coefficients[thisIdx]+p.coefficients[pIdx];
					tempExpon[tempIdx]=p.exponents[pIdx];
					tempIdx++;
				}
				pIdx++;
				thisIdx++;

			} else if(this.exponents[thisIdx] < p.exponents[pIdx]) {
				tempCoeff[tempIdx] = this.coefficients[thisIdx];
				tempExpon[tempIdx] = this.exponents[thisIdx];

				thisIdx++;
				tempIdx++;
			} else{
				tempCoeff[tempIdx] = p.coefficients[pIdx];
				tempExpon[tempIdx] = p.exponents[pIdx];
				pIdx++;
				tempIdx++;
			}
		}
		while(pIdx < p.coefficients.length){
			tempCoeff[tempIdx]=p.coefficients[pIdx];
			tempExpon[tempIdx]=p.exponents[pIdx];
			tempIdx++;
			pIdx++;
		}
		while(thisIdx < this.coefficients.length){
			tempCoeff[tempIdx]=this.coefficients[thisIdx];
			tempExpon[tempIdx]=this.exponents[thisIdx];
			tempIdx++;
			thisIdx++;
		}
		if(tempIdx==0){
			return new Polynomial();
		}
		double[] resCoeff = new double[tempIdx];
		int[] resExpon = new int[tempIdx];
		for(int i = 0; i < tempIdx; i++){
			resCoeff[i]=tempCoeff[i];
			resExpon[i]=tempExpon[i];
		}

		return new Polynomial(resCoeff,resExpon);
	}

	public Polynomial multiply(Polynomial p){
		Polynomial res = new Polynomial();

		for(int i = 0; i < this.coefficients.length; i++){
			double[] curCoeff = new double[p.coefficients.length];
			int[] curExpon = new int[p.exponents.length];
			for(int j = 0; j < p.coefficients.length; j++){
				curCoeff[j]=this.coefficients[i]*p.coefficients[j];
				curExpon[j]=this.exponents[i]+p.exponents[j];
			}
			Polynomial curPoly = new Polynomial(curCoeff, curExpon);
			res = res.add(curPoly);
		}
		return res;
	}

    public double evaluate(double x) {
        double result = 0.0;
        for (int i = 0; i < this.coefficients.length; i++) {
            result+=this.coefficients[i]*Math.pow(x, this.exponents[i]);
        }
        return result;
    }
    public boolean hasRoot(double x) {
        return this.evaluate(x) == 0.0;
    }
}