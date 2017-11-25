package mlrga;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class TestAny {

	
	public static List<Integer> doube2binary(double min, double max, int p, double num){
		List<Integer> result = new ArrayList();
		int rank = (int) ((Math.pow(2, p)-1)*(num-min)/(max-min));
		String temp = Integer.toBinaryString(rank);
		if(temp.length()>0){
			for(int i = 0;i<=temp.length()-1;i++){
				result.add(Integer.parseInt(temp.substring(i, i+1)));
			}
			while(result.size()<p){
				result.add(0,0);
			}
		}else{
			return null;
		}
		return result;
	}
	//二进制分三部分，第一位是符号位，接下来几位表示整数部分，�?���?��是小数部�?
	public static List<Integer> doube2binary2(double min, double max, int p, double num){
		List<Integer> result = new ArrayList();
		//首先确定符号�?表示+�?表示-
		int sign = num<0?0:1;
		result.add(sign);
		//确定整数部分
		int num_int = (int) Math.abs(num);
		int int_lenth = (int) Math.ceil(Math.log10(Math.max(Math.abs(min), Math.abs(max)))/Math.log10(2));
		String int_str = Integer.toBinaryString(num_int);
		while(int_str.length()<int_lenth){
			int_str = "0"+int_str;
		}
		for(int i = 0;i<=int_lenth-1;i++){
			result.add(Integer.parseInt(int_str.substring(i, i+1)));
		}
		//确定小数部分，这个和精度有关系， 比如小数点后�?位，精度�?0�?4位精度为14位， 5位为17, 6位为20�?
		double pp = Math.pow(2, p);
		int pp_curser = 10;
		while(pp/pp_curser>10){
			pp_curser*=10;
		}
		//根据精度截取小数部分
		double num_float = num-(int)num;
		int num_float_int = (int) Math.abs(num_float*pp_curser);
		int float_lenth = (int) Math.ceil(Math.log10(pp_curser)/Math.log10(2));
		String float_str = Integer.toBinaryString(num_float_int);
		while(float_str.length()<float_lenth){
			float_str = "0"+float_str;
		}
		for(int i = 0;i<=float_lenth-1;i++){
			result.add(Integer.parseInt(float_str.substring(i, i+1)));
		}
		return result;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();
		mlrGA ls = new mlrGA();
		int m = 40;
		int n = 30;
		List<List<Double>> scopes5 = new ArrayList();
        List<List<Double>> scopes30 = new ArrayList();
        List<List<Double>> scopes32 = new ArrayList();
        List<List<Double>> scopes50 = new ArrayList();
        List<List<Double>> scopes100 = new ArrayList();
        List<List<Double>> scopes500 = new ArrayList();
        List<List<Double>> scopes512 = new ArrayList();
        List<List<Double>> scopes600 = new ArrayList();
		for(int i = 0; i<=n-1;i++ ){
			List<Double> sp5 = new ArrayList();
			List<Double> sp30 = new ArrayList();
		    List<Double> sp32 = new ArrayList();
		    List<Double> sp50 = new ArrayList();
		    List<Double> sp100 = new ArrayList();
		    List<Double> sp500 = new ArrayList();
		    List<Double> sp512 = new ArrayList();
		    List<Double> sp600 = new ArrayList();
			
		    sp5.add(-0.5);
		    sp5.add(0.5);
		    scopes5.add(sp5);
		    sp30.add(-30.0);
		    sp30.add(30.0);
		    scopes30.add(sp30);
		    sp32.add(-32.0);
		    sp32.add(32.0);
		    scopes32.add(sp32);
		    sp50.add(-50.0);
		    sp50.add(50.0);
		    scopes50.add(sp50);
		    sp100.add(-100.0);
		    sp100.add(100.0);
		    scopes100.add(sp100);
		    sp500.add(-500.0);
		    sp500.add(500.0);
		    scopes500.add(sp500);
		    sp512.add(-5.12);
		    sp512.add(5.12);
		    scopes512.add(sp512);
		    sp600.add(-600.0);
		    sp600.add(600.0);
		    scopes600.add(sp600);
		}
		try {
			String prefix = "2_mse_";
			List<File> results = new ArrayList();
			
			File result0 = new File(prefix+"ls_x.txt");
			File result1 = new File(prefix+"ls_quar.txt");
			File result2 = new File(prefix+"ls_rosen.txt");
			File result3 = new File(prefix+"ls_step.txt");
			File result4 = new File(prefix+"ls_sch.txt");
			File result5 = new File(prefix+"ls_cos.txt");
			File result6 = new File(prefix+"ls_ackley.txt");
			File result7 = new File(prefix+"ls_gri.txt");
			File result8 = new File(prefix+"ls_pen1.txt");
			File result9 = new File(prefix+"ls_pen2.txt");
			File result10 = new File(prefix+"ls_non.txt");
			File result11 = new File(prefix+"ls_wei.txt");
			File result12 = new File("counts.txt");
			results.add(result0);
			results.add(result1);
			results.add(result2);
			results.add(result3);
			results.add(result4);
			results.add(result5);
			results.add(result6);
			results.add(result7);
			results.add(result8);
			results.add(result9);
			results.add(result10);
			results.add(result11);
			results.add(result12);
			
			BufferedWriter[] output = new BufferedWriter[results.size()];
			
			for(int i = 0; i<= results.size()-1;i++){
				if (results.get(i).exists()) {
					results.get(i).delete();
					if (results.get(i).createNewFile()) {
						System.out.println("result"+i+" file create success!");
					} else {
						System.out.println("result"+i+" file create failed!");
					}
				} else {
					if (results.get(i).createNewFile()) {
						System.out.println("result"+i+" file create success!");
					} else {
						System.out.println("result"+i+" file create failed!");
					}

				}
				output[i] = new BufferedWriter(new FileWriter(results.get(i)));
			}

			int percent = 2;
            for(int bb=0; bb<=0;bb++){       
            	           	
            	ls.Calculate(200, 40, 30, scopes100, new MaxFunction(), percent,  output[0]);
            	//ls.Calculate(200, 40, 30, scopes100, new QuardircMaxFunction(), percent,  output[1]);
            	//ls.Calculate(200, 40, 30, scopes30, new RosenbrockMaxFunction(), percent,  output[2]);
            	//ls.Calculate(200, 40, 30, scopes100, new StepMaxFunction(), percent,  output[3]);
            	//ls.Calculate(200, 40, 30, scopes500, new SchwefelMaxFunction(), percent,  output[4]);
            	//ls.Calculate(200, 40, 30, scopes512, new CosMaxFunction(), percent, output[5]);
            	//ls.Calculate(200, 40, 30, scopes32, new AckleyMaxFunction(), percent,  output[6]);
            	//ls.Calculate(200, 40, 30, scopes600, new GriewankMaxFunction(), percent,  output[7]);
            	//ls.Calculate(200, 40, 30, scopes50, new PenalizedMaxFunction(), percent,  output[8]);
            	//ls.Calculate(200, 40, 30, scopes50, new Penalized2MaxFunction(), percent, output[9]);
            	//ls.Calculate(200, 40, 30, scopes5, new NonMaxFunction(), percent,  output[10]);
            	//ls.Calculate(200, 40, 30, scopes512, new WeiMaxFunction(), percent,  output[11]);
            	
            	output[12].write(MaxFunction.counts+"\t");
            	output[12].write(QuardircMaxFunction.counts+"\t");
        		output[12].write(RosenbrockMaxFunction.counts+"\t");
        		output[12].write(StepMaxFunction.counts+"\t");
        		output[12].write(SchwefelMaxFunction.counts+"\t");
        		output[12].write(CosMaxFunction.counts+"\t");
        		output[12].write(AckleyMaxFunction.counts+"\t");
        		output[12].write(GriewankMaxFunction.counts+"\t");
        		output[12].write(PenalizedMaxFunction.counts+"\t");
        		output[12].write(Penalized2MaxFunction.counts+"\t");
        		output[12].write(NonMaxFunction.counts+"\t");
        		output[12].write(WeiMaxFunction.counts+"\t");

        		//MaxFunction.counts = 0;
        		//CosMaxFunction.counts = 0;
        		//AckleyMaxFunction.counts = 0;
        		//QuardircMaxFunction.counts = 0;
        		//StepMaxFunction.counts = 0;
        		//RosenbrockMaxFunction.counts = 0;
        		//SchwefelMaxFunction.counts = 0;
        		//GriewankMaxFunction.counts = 0;
        		//PenalizedMaxFunction.counts = 0;
        		//Penalized2MaxFunction.counts = 0;
        		//WeiMaxFunction.counts = 0;
        		//NonMaxFunction.counts = 0;
        		
				for (BufferedWriter op : output) {
					   op.write("\n");
					   op.flush();
				}
                	
            }
			for(BufferedWriter op : output){
				op.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		long endTime = System.currentTimeMillis();
		System.out.println("运行时间 " + (endTime - startTime) + "ms");
		System.out.println("x计算次数 " + MaxFunction.counts);
		
	}

}
