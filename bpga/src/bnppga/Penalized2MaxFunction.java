/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package bnppga;

import java.util.List;

import org.jgap.*;
import org.jgap.impl.*;


/**
 * Fitness function for our example. See evaluate(...) method for details.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 2.0
 */
public class Penalized2MaxFunction 
    extends FitnessFunction{
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.6 $";
  
  public static int counts = 0;

  /**
   * This example implementation calculates the fitness value of Chromosomes
   * using BooleanAllele implementations. It simply returns a fitness value
   * equal to the numeric binary value of the bits. In other words, it
   * optimizes the numeric value of the genes interpreted as bits. It should
   * be noted that, for clarity, this function literally returns the binary
   * value of the Chromosome's genes interpreted as bits. However, it would
   * be better to return the value raised to a fixed power to exaggerate the
   * difference between the higher values. For example, the difference
   * between 254 and 255 is only about .04%, which isn't much incentive for
   * the selector to choose 255 over 254. However, if you square the values,
   * you then get 64516 and 65025, which is a difference of 0.8% -- twice
   * as much and, therefore, twice the incentive to select the higher
   * value.
   *
   * @param a_subject the Chromosome to be evaluated
   * @return defect rate of our problem
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 2.0
   */
  public double evaluate(IChromosome a_subject) {
    double total = 0;
    double total1 = 0;
    int time_delay = 0;
    try {
		Thread.sleep(time_delay);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    int n = a_subject.size();
    for (int i = 0; i < n; i++) {
    	double data = ((DoubleGene)a_subject.getGene(i)).doubleValue(); 
    	if(i<n-1){
    		double data1 = ((DoubleGene)a_subject.getGene(i+1)).doubleValue();
    		total+=((data-1)*(data-1)*(1+Math.sin(3*Math.PI*data1)*Math.sin(3*Math.PI*data1)));
    	}
    	total1+=u(data,5,100,4);
      
    }
    double data0 = ((DoubleGene)a_subject.getGene(0)).doubleValue(); 
    double datam = ((DoubleGene)a_subject.getGene(n-1)).doubleValue(); 
    double result = 0;
    result = 0.1*(Math.sin(3*Math.PI*data0)*Math.sin(3*Math.PI*data0)+total+(datam-1)*(datam-1))+total1;
    counts ++;
    double result1 = (1999999999-result)<=0?1:1999999999-result;
    result1 = 1000*(result1)/1999999999;
    return result1;
//    return n*Math.pow(10, 2.0)-total; 
  }
  
  private double u(double x, int a, int k, int m){
	  double result = 0;
	  if (x>a){
		  result = k*Math.pow(x-1, m);
	  }else if (x<-1*a){
    	  result = k*Math.pow(-1*x-a, m);
	  }else{
    	  result= 0;
	  }
	  return result;
  }


}
