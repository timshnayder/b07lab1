public class Polynomial{
	private double[] coefficients;
	public Polynomial(){
		this.coefficients = new double[1];
        this.coefficients[0]=0.0;
	}
	public Polynomial(double[] coefficients){
		this.coefficients = new double[coefficients.length];
		this.coefficients = coefficients.clone();
	}
	public Polynomial add(Polynomial p){
		int maxLen=Math.max(this.coefficients.length, p.coefficients.length);
		double[] res = new double[maxLen];
		for(int i = 0; i < maxLen; i++){
			if(i < this.coefficients.length){
				res[i]+=this.coefficients[i];
			}
            if(i < p.coefficients.length){
                res[i]+=p.coefficients[i];
            }
		}
        return new Polynomial(res);
	}
    public double evaluate(double x) {
        double result = 0.0;
        for (int i = 0; i < this.coefficients.length; i++) {
            result+=this.coefficients[i]*Math.pow(x, i);
        }
        return result;
    }
    public boolean hasRoot(double x) {
        return this.evaluate(x) == 0.0;
    }
}