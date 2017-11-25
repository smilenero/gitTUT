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
public class NonMaxFunction 
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
    double total2 = 0;
    double a = 0.5;
    double b = 3;
    int kmax = 20;
    int time_delay = 0;
    try {
		Thread.sleep(time_delay);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    int n = a_subject.size();
    for (int i = 0; i < n; i++) {
    	double total1 = 0;
        double mydata = ((DoubleGene)a_subject.getGene(i)).doubleValue();
        for(int k = 0; k<=kmax;k++){
        	total1 += (Math.pow(a, k)*Math.cos(2*Math.PI*Math.pow(b, k)*(mydata+0.5)));
        	total2 += (Math.pow(a, k)*Math.cos(2*Math.PI*Math.pow(b, k)*0.5));
        }
    	total +=  total1;
      
    }
    counts ++;
    double result = n*4-total+total2;
    result = 1000*(result)/(4*n);
    return result;
  }


}
