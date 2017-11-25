package mlrga;

import java.util.List;

import org.jgap.IChromosome;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

public class SVMTest {
	
	public svm_problem getdata2(int start, int count, int lenth){
		svm_problem prob = new svm_problem();
		prob.l = count;
		double[] y = new double[count];
		svm_node[][] x = new svm_node[count][lenth];
		for (int i = start; i <= start + count - 1; i ++) {
			y[i - start] = i*2;
			for (int j = 1; j <= lenth; j++) {
				x[i - start][j - 1] = new svm_node();
				x[i - start][j - 1].index = j;
				x[i - start][j - 1].value = i*2;
			}
		}
		prob.x = x;
		prob.y = y;
		return prob;
	}
	
	public svm_problem getdata3(int start, int count, int lenth){
		svm_problem prob = new svm_problem();
		prob.l = count;
		double[] y = new double[count];
		svm_node[][] x = new svm_node[count][lenth];
		for (int i = start; i <= start + count - 1; i ++) {
			y[i - start] = i*2-1;
			for (int j = 1; j <= lenth; j++) {
				x[i - start][j - 1] = new svm_node();
				x[i - start][j - 1].index = j;
				x[i - start][j - 1].value = i*2-1;
			}
		}
		prob.x = x;
		prob.y = y;
		return prob;
	}
	
	public svm_problem getdata(int start, int count, int lenth){
		svm_problem prob = new svm_problem();
		prob.l = count;
		double[] y = new double[count];
		svm_node[][] x = new svm_node[count][lenth];
		for(int i = start; i<=start+count-1; i++){
			y[i-start] = i;
			for(int j = 1;j<=lenth;j++){
				x[i-start][j-1] = new svm_node();
				x[i-start][j-1].index = j;
				x[i-start][j-1].value = i;
			}
		}
		prob.x = x;
		prob.y = y;
		return prob;
	}
	
	public svm_problem getdata(List<IChromosome> chroms){
		svm_problem prob = new svm_problem();
		prob.l = chroms.size();
		double[] y = new double[prob.l];
		int lenth = chroms.get(0).size();
		svm_node[][] x = new svm_node[prob.l][lenth];
		for(int i=0;i<=prob.l-1;i++){
			y[i] = chroms.get(i).getFitnessValue();
			for(int j = 1;j<=lenth;j++){
				x[i][j-1] = new svm_node();
				x[i][j-1].index = j;
				x[i][j-1].value = (Double)(chroms.get(i).getGene(j-1).getAllele());
			}
		}
		prob.x = x;
		prob.y = y;
		return prob;
	}

	public svm_parameter initsvm(){
		
		svm_parameter param = new svm_parameter();
		// default values
		param.svm_type = svm_parameter.EPSILON_SVR;
		param.kernel_type = svm_parameter.RBF;
		param.degree = 3;
		param.gamma = 1.0/200;	// 1/num_features
		param.coef0 = 0;
		param.nu = 0.99;
		param.cache_size = 100;
		param.C = 500;
		param.eps = 1e-3;
		param.p = 0.0625;
		param.shrinking = 1;
		param.probability = 0;
		param.nr_weight = 0;
		param.weight_label = new int[0];
		param.weight = new double[0];
		
		return param;
	}
	
public svm_parameter initsvm(double gamma, double c){
		
		svm_parameter param = new svm_parameter();
		// default values
		param.svm_type = svm_parameter.EPSILON_SVR;
		param.kernel_type = svm_parameter.RBF;
		param.degree = 3;
		param.gamma = gamma;	// 1/num_features
		param.coef0 = 0;
		param.nu = 0.99;
		param.cache_size = 100;
		param.C = c;
		param.eps = 1e-3;
		param.p = 0.0625;
		param.shrinking = 1;
		param.probability = 0;
		param.nr_weight = 0;
		param.weight_label = new int[0];
		param.weight = new double[0];
		
		return param;
	}
	
	public svm_model train(svm_problem prob, svm_parameter param){
		svm_model model = svm.svm_train(prob,param);
		return model;
	}
	
	public double[] predict(svm_model model, svm_problem prob){
		double[] pre_y = new double[prob.l];
		for(int i=0;i<=prob.l-1;i++){
			pre_y[i] = svm.svm_predict(model,prob.x[i]);
		}
		return pre_y;
	}
	
	public double predict(svm_model model, IChromosome chrom){
		double pre_y = 0.0;
		int lenth = chrom.size();
		svm_node[] chromnode = new  svm_node[lenth];		
		for(int i=1;i<=lenth;i++){
			chromnode[i-1] = new svm_node();
			chromnode[i-1].index = i;
			chromnode[i-1].value = (Double)(chrom.getGene(i-1).getAllele());			
		}
		pre_y = svm.svm_predict(model,chromnode);
		return pre_y;
	}
	
	public void predict(svm_model model, List<IChromosome> chroms){
		for(IChromosome chrom:chroms){
			chrom.setFitnessValueDirectly(predict(model, chrom));
			chrom.setIscenter(false);
		}
	}
	
	public svm_model getmodel(List<IChromosome> chroms, double gamma, double c){
		svm_problem prob_train = getdata(chroms);
		svm_parameter param = initsvm(gamma, c);
		svm_model model = train(prob_train, param);
		return model;
	}
	
	public void runsvm(){
		svm_problem prob_train = getdata2(1, 200, 10);
		svm_problem prob_predict = getdata3(101, 50, 10);
		svm_parameter param = initsvm();
		svm_model model = train(prob_train, param);
		double[] result = predict(model, prob_predict);
		int count = 0;
		for(int i = 0;i<=49; i++){
			double error = 0.0;
			error = Math.abs(result[i]-prob_predict.y[i]);
			if(error<=1){
				count++;
			}
		}
		System.out.println(count);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SVMTest obj = new SVMTest();
		obj.runsvm();
	}

}
