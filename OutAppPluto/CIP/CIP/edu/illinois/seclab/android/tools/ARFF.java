package edu.illinois.seclab.android.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class ARFF {

	/**************************************************************************************************/
	/**************************************************************************************************/
	/******************************* WRITE AN ARFF FORMATTED HEADER ***********************************/
	/**************************************************************************************************/
	/**
	 * 
	 * @param apps List of arff attributes
	 * @param mode 0: <b>SPARSE</b> e.g. {TRUE, FALSE}, 1 <b>BINARY</b> for {0, 1}
	 * @return
	 */
	public static boolean writeHeader(String[] apps, int mode) {
		
		//validate
				if(apps == null || apps.length == 0){
					Log.debug("No apps to store!");
					return Utils.FAILURE;
				}
				
				String attrValues = "";
				String filename = "";
				if(mode == Preferences.APRIORI){
					filename = Preferences.arffPath;
					attrValues = "{TRUE, FALSE}";
				}
				else{ //if (mode == Preferences.FPGROWTH){
					filename = Preferences.bin_arffPath;
					attrValues = "{0, 1}";
				}
				
				try {
					BufferedWriter outBufferedWriter = new BufferedWriter(
							new FileWriter(filename, true));
					
					outBufferedWriter.append("@relation androidApps.symbolic" + "\n");
					outBufferedWriter.append("\n");
					
					for(int i = 0; i < apps.length; i++){
                        
                        if(apps[i] != null && !apps[i].isEmpty() && DataParser.isAppName(apps[i])){
                            //String appItem[] = apps[i].split("\\s+");
                            outBufferedWriter.append("@attribute " + apps[i] + " " + attrValues + " \n");
                        }
                        
					}
					
					outBufferedWriter.append("\n");
					outBufferedWriter.append("@data\n");
					
					outBufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.error("Could not write app list for device to file!");
					e.printStackTrace();
					return Utils.FAILURE;
				}
				
				return Utils.SUCCESS;
		
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	/************************************ ARFF DATA SECTION:  *****************************************/
	/**************************************************************************************************/
	/**
	 * 
	 * @param hs_apps
	 * @param apps
	 * @param binArffpath
	 * @param mode
	 * @return
	 */
	public static boolean writeData(HashSet<String> hs_apps, String[] apps, int mode) {
		
		if(mode == Preferences.APRIORI){
			//sparse: apriori
			if(!ARFF.writeData(hs_apps, apps, Preferences.arffPath)){
				return Utils.FAILURE;
			}
		}
		else{ //mode == any other number
			//even if mode is -1, do fpgrowth
			if(!ARFF.writeDataBinary(hs_apps, apps, Preferences.bin_arffPath)){
				return Utils.FAILURE;
			}
		}
		
		return Utils.SUCCESS;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	/**************************** ARFF DATA SECTION: BINARY *******************************************/
	/**************************************************************************************************/
	/**
	 * Creates arff formatted file in binary representation. Non sparse. Suitable for use
	 * 	with FPGrowth
	 * @param hs_apps
	 * @param apps
	 * @param arffpath 
	 * @return
	 */
	private static boolean writeDataBinary(HashSet<String> hs_apps, String[] apps, String arffpath) {
		
		try {
			/* Write to arff file */
			BufferedWriter outBufferedWriter = new BufferedWriter(
					new FileWriter(arffpath, true));
			
			/* Read apps per device */
			BufferedReader inBufferedWriter = new BufferedReader(new FileReader(Preferences.outAppsDir 
					+ Preferences.outAppsFile));
			

			for(String line = inBufferedWriter.readLine(); line !=null; 
						line = inBufferedWriter.readLine()){
				if(!line.isEmpty()){
					
					/* Read device's apps in a hashset */
					String[] device_apps = line.split(",");
					HashSet<String> hs_lineApps = new HashSet<String>();
					for(String app : device_apps){
						hs_lineApps.add(app);
					}
					
					boolean firstInLine = true;
					
					/* traverse index array and indicate in output data file whether the app is
					 * in this line */
					for(int i = 0; i < apps.length; i++){
                        if(!DataParser.isAppName(apps[i])){
                            continue;
                        }
                        
						String out = "";
						
						if(!firstInLine){
							out += ", ";
						}
						else{
							firstInLine = false;
						}
						
						if(hs_lineApps.contains(apps[i])){
							//1
							outBufferedWriter.append(out + "1");
						}
						else{
							//0
							outBufferedWriter.append(out + "0");
						}
					}
					
					outBufferedWriter.append("\n");
					
				}	
				
			}
			
			outBufferedWriter.close();
			inBufferedWriter.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("Could not find file with apps per user... should abort!");
			e.printStackTrace();
			return Utils.FAILURE;
		} catch (IOException e) {
			System.out.println("IO Exception while reading line of apps per user... should abort!");
			e.printStackTrace();
			return Utils.FAILURE;
		}
		
		
		
		return Utils.SUCCESS;
		
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	/**************************** ARFF DATA SECTION: SPARSE *******************************************/
	/**************************************************************************************************/
	/**
	 * Creates arff formatted file in a <b>SPARSE</b> representation. Suitable for use with Apriori
	 * @param hs_apps
	 * @param apps
	 * @param arffPath 
	 * @return
	 */
	private static boolean writeData(HashSet<String> hs_apps, String[] apps, String arffPath) {
		
		try {
			/* Write to arff file */
			BufferedWriter outBufferedWriter = new BufferedWriter(
					new FileWriter(arffPath, true));
			
			/* Read apps per device */
			BufferedReader inBufferedWriter = new BufferedReader(new FileReader(Preferences.outAppsDir 
					+ Preferences.outAppsFile));
			

			for(String line = inBufferedWriter.readLine(); line !=null; 
						line = inBufferedWriter.readLine()){
				if(!line.isEmpty()){
					
					/* Read device's apps in a hashset */
					String[] device_apps = line.split(",");
					HashSet<String> hs_lineApps = new HashSet<String>();
					for(String app : device_apps){
						hs_lineApps.add(app);
					}
					
					outBufferedWriter.append('{');
					boolean firstInLine = true;
					
					/* traverse index array and indicate in output data file whether the app is
					 * in this line */
					for(int i = 0; i < apps.length; i++){
                        if(!DataParser.isAppName(apps[i])){
                            continue;
                        }
						if(hs_lineApps.contains(apps[i])){
							//write datum
							String outString = "";
							
							if(firstInLine){
								outString = i + " TRUE";
								firstInLine = false;
							}
							else{
								outString = ", "  + i + " TRUE";
							}
							outBufferedWriter.append(outString);
						}
					}
					
					outBufferedWriter.append("} \n");
				}	
				
			}
			
			outBufferedWriter.close();
			inBufferedWriter.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("Could not find file with apps per user... should abort!");
			e.printStackTrace();
			return Utils.FAILURE;
		} catch (IOException e) {
			System.out.println("IO Exception while reading line of apps per user... should abort!");
			e.printStackTrace();
			return Utils.FAILURE;
		}
		
		
		
		return Utils.SUCCESS;
		
	}


}
