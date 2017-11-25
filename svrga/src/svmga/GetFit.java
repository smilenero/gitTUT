package svmga;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import libsvm.svm_model;

import org.jgap.Configuration;
import org.jgap.FitnessFunction;
import org.jgap.IChromosome;
import org.jgap.Population;

public class GetFit {
	
	public void getfitness(Population a_pop, Configuration a_conf, SVMGA obj,
			FitnessFunction fitness, int percent, double gamma, double c, BufferedWriter output){
//		getfitnesshistory(a_pop, a_conf, obj, fitness, percent, gamma, c, output);
		getfitnesslocal(a_pop, a_conf, obj, fitness, percent, gamma, c, output);
	}
	
	public void getfitnesshistory(Population a_pop, Configuration a_conf, SVMGA obj,
			FitnessFunction fitness, int percent, double gamma, double c, BufferedWriter output){
//确保没有算过适应度的个体的染色体都是false
    	
    	int oldpopsize = a_conf.getPopulationSize();
    	int nowpopsize = a_pop.size();
    	for(int i = oldpopsize;i<nowpopsize;i++){
    		a_pop.getChromosome(i).setIscenter(false);
    	}
		if(obj.getSvmin_history().size()<200){
			for(IChromosome chrom:a_pop.getChromosomes()){
				chrom.getFitnessValue();
				chrom.setIscenter(true);
				obj.getSvmin_history().add(chrom);
			}
		}else{
			List<IChromosome> chroms = a_pop.getChromosomes();
			List<IChromosome> pre_chrom = new ArrayList();
			for(int i = 0; i<=chroms.size()-1;i++){
				if(!chroms.get(i).isIscenter()){
					if(i%percent==0){
						chroms.get(i).getFitnessValue();
						chroms.get(i).setIscenter(true);
						obj.getSvmin_history().add(chroms.get(i));
					}else{
						pre_chrom.add(chroms.get(i));
					}
				}
			}
			SVMTest st = new SVMTest();
			st.predict(st.getmodel(obj.getSvmin_history(), gamma, c), pre_chrom);
		}
		
	}
	
	public void getfitnesslocal(Population a_pop, Configuration a_conf, SVMGA obj,
			FitnessFunction fitness, int percent, double gamma, double c, BufferedWriter output){
//确保没有算过适应度的个体的染色体都是false
		SVMTest st = new SVMTest();
    	int oldpopsize = a_conf.getPopulationSize();
//		int oldpopsize = 0;
    	int nowpopsize = a_pop.size();
    	for(int i = oldpopsize;i<nowpopsize;i++){
    		a_pop.getChromosome(i).setIscenter(false);
    	}
		if(obj.getSvmin_history().size()<200){
			for(IChromosome chrom:a_pop.getChromosomes()){
				chrom.getFitnessValue();
				chrom.setIscenter(true);
				if(chrom.getFitnessValue()>1000){
					System.out.println("before!");
				}
				obj.getSvmin_history().add(chrom);
			}
		}else{
			List<IChromosome> chroms = a_pop.getChromosomes();
			List<IChromosome> pre_chrom = new ArrayList();
//			for(int i = 0; i<=chroms.size()-1;i++){
//				if(!chroms.get(i).isIscenter()){
//					if(i%percent==0){
//						chroms.get(i).getFitnessValue();
//						chroms.get(i).setIscenter(true);
//						obj.getSvmin_history().add(chroms.get(i));
//					}else{
//						pre_chrom.add(chroms.get(i));
//						chroms.get(i).setFitnessValueDirectly(st.predict(st.getmodel(obj.getSvmin_local(), gamma, c), chroms.get(i)));
//						chroms.get(i).setIscenter(false);
//					}
//				}
//			}
			for(int i = 40; i<=chroms.size()-1;i+=2){
				if(!chroms.get(i).isIscenter()){
						chroms.get(i).setFitnessValueDirectly(fitness.evaluate(chroms.get(i)));
						chroms.get(i).setIscenter(true);
						if(chroms.get(i).getFitnessValue()>1000){
							System.out.println("ininin!"+chroms.get(i).getFitnessValue());
						}
						obj.getSvmin_history().add(chroms.get(i));
			     }
			}
			obj.setSvmin_local(new ArrayList());
			for(int i = obj.getSvmin_history().size()-200;i<= obj.getSvmin_history().size()-1;i++){
				obj.getSvmin_local().add(obj.getSvmin_history().get(i));
			}
			svm_model model = st.getmodel(obj.getSvmin_local(), gamma, c);
			for(int i = 41; i<=chroms.size()-1;i+=2){
				if(!chroms.get(i).isIscenter()){
//						chroms.get(i).setFitnessValueDirectly(st.predict(st.getmodel(obj.getSvmin_local(), gamma, c), chroms.get(i)));
						chroms.get(i).setFitnessValueDirectly(st.predict(model, chroms.get(i)));
						chroms.get(i).setIscenter(false);
				}
			}
			
//			SVMTest st = new SVMTest();
//			st.predict(st.getmodel(obj.getSvmin_local(), gamma, c), pre_chrom);
		}
		return;
	}

}
