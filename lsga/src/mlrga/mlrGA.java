package mlrga;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.Population;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.DoubleGene;


public class mlrGA {
private int nIterateCount=0;
private List<IChromosome> svmin_history = new ArrayList();
private List<IChromosome> svmin_local = new ArrayList();
private List<String> fitnessvalues = new ArrayList();
private int progress = 0;
	
	
	public int getProgress() {
	return progress;
}

public void setProgress(int progress) {
	this.progress = progress;
}


	public List<String> getFitnessvalues() {
	return fitnessvalues;
}

public void setFitnessvalues(List<String> fitnessvalues) {
	this.fitnessvalues = fitnessvalues;
}

	public int getnIterateCount() {
		return nIterateCount;
	}

	public void setnIterateCount(int nIterateCount) {
		this.nIterateCount = nIterateCount;
	}
	
	

	public List<IChromosome> getSvmin_history() {
		return svmin_history;
	}

	public void setSvmin_history(List<IChromosome> svmin_history) {
		this.svmin_history = svmin_history;
	}

	public List<IChromosome> getSvmin_local() {
		return svmin_local;
	}

	public void setSvmin_local(List<IChromosome> svmin_local) {
		this.svmin_local = svmin_local;
	}

	/**
	 * 基因算法优化
	 * @param fitness 目标函数
	 * @param percent    群体中有多少是真实算的用来加入到训练集
	 * @param scopes 每个参数取值范围矩阵 2行列
	 * @param popSize  种群大小
	 * @param chromeSize     参数个数
	 * @param max_gen        基因算法内部优化迭代次数
	 */
	public void Calculate(int max_gen, int popSize, int chromeSize, List<List<Double>> scopes, FitnessFunction fitness, 
			int percent,  BufferedWriter output) {
		nIterateCount=0;
		svmin_history = new ArrayList();
		svmin_local = new ArrayList();
		fitnessvalues = new ArrayList();
		progress = 0;
		int numEvolutions = max_gen;
		Configuration gaConf = new DefaultConfiguration();
		gaConf.reset();
		gaConf.setPreservFittestIndividual(true);
		gaConf.setKeepPopulationSizeConstant(false);
		Genotype genotype = null;
		
		
		
		try {
			//构建基因(Gene)
			Gene[] sampleGenes = new DoubleGene[chromeSize];//基因长度
			 for (int i = 0; i < sampleGenes.length; i++) {					    
					sampleGenes[i] = new DoubleGene(gaConf, scopes.get(i).get(0),scopes.get(i).get(1));
			 }
			// 构建染色�?Chromosome)
			 IChromosome sampleChromosome = new Chromosome(gaConf, sampleGenes);
			gaConf.setSampleChromosome(sampleChromosome);
			gaConf.setPopulationSize(popSize);	
			gaConf.setFitnessFunction(fitness);
			genotype = Genotype.randomInitialGenotype(gaConf);
			
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
			System.exit(-2);
		}
		progress = 0;
		double sume =0;
		int percentEvolution = numEvolutions / 10;
		for (int i = 0; i < numEvolutions; i++) {
        	//Start GA
			genotype.evolve(this, fitness, percent,  output);
			// Print progress.
//			try {
//				Population temppop = genotype.getPopulation();
//				for (IChromosome mychrom : temppop.getChromosomes()) {
//					output.write(Math.abs(fitness.evaluate(mychrom)-mychrom.getFitnessValueDirectly()) + "\t");
//				}
//				output.write("\n");
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			progress++;
			// ---------------
			if (percentEvolution > 0 && i % percentEvolution == 0) {
				
				IChromosome fittest  = genotype.getFittestChromosome();
				double sum = 0;
				
				double bestfitness = fittest.getFitnessValue();
				sume = sume+bestfitness;
				System.out.println("Currently fittest Chromosome has fitness "+ bestfitness);
//			    for (int j = 0; j < fittest.size(); j++) {
//			        sum += (Double)fittest.getGene(j).getAllele(); ;
//			    }				
//			    double max =  (1/bestfitness)-(sum*fittest.size());
//				System.out.println("Currently fittest Chromosome has fitness "+ bestfitness +" max= "+max);
			}
		}
		//System.out.println("average "+(sume/10) );
		// Print summary.
		// --------------
		IChromosome fittest3 = null;
		genotype.getPopulation().sortByFitness();
		for(IChromosome fittest:genotype.getPopulation().getChromosomes()){
			if(((Chromosome)fittest).isIscenter()){
				fittest3 = fittest;
				break;
			}
		}
		IChromosome fittest2 = fittest3;
		if(fittest3==null){
			fittest2 = svmin_history.get(0);
		}
		for(IChromosome fittest:svmin_history){
			
			if(fittest2.getFitnessValueDirectly()<fittest.getFitnessValueDirectly()){
				fittest2 = fittest;
			}
		}
		
		if(fittest2!=null){
			System.out.println("Fittest Chromosome in genotype has fitness "+ (fittest2.getFitnessValueDirectly()));
//			System.out.println("true fitness3 "+ fitness.evaluate(fittest3));
//			System.out.println("true fitness2 "+ fitness.evaluate(fittest2));
			
		}
		
		DecimalFormat myformat = new DecimalFormat("#0.00");
		try {
			output.write(fittest2.getFitnessValueDirectly() + "\t");
            output.flush();
			for (int i = 0; i < chromeSize; i++) {
				System.out.print(myformat
						.format(fittest2.getGene(i).getAllele()) + "	");
//				output.write(myformat.format(fittest2.getGene(i).getAllele())+ "	");
			}
//			for(int i = 0;i<=popSize-1;i++){
//				for (int j = 0; j < chromeSize; j++) {
//					output.write(myformat.format(genotype.getPopulation().getChromosome(i).getGene(j).getAllele())+ "\t");
//				}
//				output.write("\n");
//				output.flush();
//			}
			System.out.println();
//			output.write("\n");
//			System.out.println("sum counts: " + AckleyMaxFunction.counts);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//处理返回结果

	}// end of this math
	

	private void printsth(String filename, List<String> contents){
		try {
			File result = new File(filename);
			if (result.exists()) {
				result.delete();
				if (result.createNewFile()) {
					System.out.println("result file create success!");
				} else {
					System.out.println("result file create failed!");
				}
			} else {
				if (result.createNewFile()) {
					System.out.println("result file create success!");
				} else {
					System.out.println("result file create failed!");
				}

			}

			BufferedWriter output = new BufferedWriter(new FileWriter(result));
			
			for(String pattern : contents){
				output.write(pattern+"\n");
			}
			
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
