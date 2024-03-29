package com.example.neuromovieclient;

import android.util.Log;

public class MarkovModel {

	
	
	int time = 0;
	int win_size = 0;
	String state = "NERVOUS";
	
	int[] d00 = new int[30], d01 = new int[30], d10 = new int[30], d11 = new int[30];
	double sum_d00 = 0, sum_d01 = 0, sum_d10 = 0, sum_d11 = 0; 

	public int ComputeProbability(double alpha) {
		
		time ++;
		
		if (time >= 10) {
							
		double a_threshold = 15000;		
		win_size = time % 30;
												
		for (int i = 0; i < 30; i++) {
			d00[i] = d01[i] = d10[i] = d11[i] = 0;
		}
		
		double p00 = 0, p01 = 0, p10 = 0, p11 = 0;
				
		if (state == "NERVOUS") {
			
			if (alpha > a_threshold) {
				
				state = "RELAX";
				d00[win_size] = 0;
				d01[win_size] = 1;
				d10[win_size] = 0; 
				d11[win_size] = 0;				
				
			} else {
				
				d00[win_size] = 1;
				d01[win_size] = 0;
				d10[win_size] = 0; 
				d11[win_size] = 0;
																
			}
					
		} else {
			
			if (alpha > a_threshold) {
				
				d00[win_size] = 0;
				d01[win_size] = 0;
				d10[win_size] = 0; 
				d11[win_size] = 1;				
				
			} else {
				
				state = "NERVOUS";
				d00[win_size] = 0;
				d01[win_size] = 0;
				d10[win_size] = 1; 
				d11[win_size] = 0;
																
			}					
		}
		
		
		for (int i = 0; i < 30; i++) {
			 
		 	sum_d00 += d00[i];
			sum_d01 += d01[i]; 
			sum_d10 += d10[i];
			sum_d11 += d11[i];
		 			 
	 }		
		
		if (time >= 39) {		
	 		 	 
			 Log.d("ADebugTag", "Value: " + sum_d00);
		
			// Markov probabilities
			 if (((sum_d00 + sum_d01) != 0) && ((sum_d10 + sum_d11) != 0)) {			 
				 
				 p00 = sum_d00 / (sum_d00 + sum_d01);
				 p01 = sum_d01 / (sum_d00 + sum_d01);
				 p10 = sum_d10 / (sum_d10 + sum_d11);
				 p11 = sum_d11 / (sum_d10 + sum_d11);
			 
			 double pi1 = (sum_d01/(sum_d00 + sum_d01)) / (1 + (sum_d01 / (sum_d00 + sum_d01)) - (sum_d11 / (sum_d10 + sum_d11)));
			 Log.d("ADebugTag", "Value: " + pi1 + " , " + p00);
			 			 
			 if (pi1 > 0.5)
			 {
				 return 1;
			 } else {
				 return 0;
			 }
		}
			 
			 			 
		} else {
			return -1;
		}
		
		}
		return -1;
	}
}		
		
		
		
						
