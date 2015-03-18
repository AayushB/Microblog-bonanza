// Author Aayush Bhandari

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class Enhancer
{

	public static void main(String[] args) 
	{
		HashMap<String, Integer> map= new HashMap<String, Integer>();
		String filename= "/Users/Aayush/Desktop/School/Fall-2014/849/TermProject/map.txt";
		System.out.println(filename);
			
			// Open a input stream to the map file
			Reader reader;
			try {
				reader = new InputStreamReader(new FileInputStream(filename), "utf-8");
				BufferedReader fis = new BufferedReader(reader, 10000000);
				String line;
				//store the id's with url in the map
				while((line=fis.readLine())!=null)
				{
					System.out.println(line);
					map.put(line, 1);
				}
				
				fis.close();
				
			} catch (UnsupportedEncodingException | FileNotFoundException e) {	e.printStackTrace();} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			for (int i=51; i<=110; i++)
			{
				filename="/Users/Aayush/Desktop/School/Fall-2014/849/TermProject/unenhanced/MB" + i + ".out";
				try {
					
					 File filename2 = new File("/Users/Aayush/Desktop/School/Fall-2014/849/TermProject/enhanced/MB" + i + ".out");//xml file path to be created with the id name
			    	 FileWriter fw = new FileWriter(filename2.getAbsoluteFile());
			    	 BufferedWriter writer= new BufferedWriter(fw,10000000);
			    	 //write to the new result file
					
					reader = new InputStreamReader(new FileInputStream(filename), "utf-8");
					BufferedReader fis = new BufferedReader(reader, 10000000);
					String line;
					while((line=fis.readLine())!=null)
					{
						Scanner scanner= new Scanner(line);
						String queryNum= scanner.next();
						String id=scanner.next();
						String score=scanner.next();
						String run=scanner.next();
						scanner.close();
						
						Double score_double= Double.parseDouble(score);
						score_double=score_double/1.12;
						
						
						if(map.containsKey(id))
						{
							//write the changed score
							writer.write(queryNum+ " "+ id + " "+ Double.toString(score_double) + " "+ run + "\n");
							//System.out.println(Double.toString(score_double));
						}
						else
						{
							writer.write(queryNum+ " "+ id + " "+ score + " "+ run + "\n");
							//System.out.println(Double.toString(score_double));
						}
						
						
					}
					
					fis.close();
					writer.close();
						
				} catch (UnsupportedEncodingException | FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				
			}
			

	}

}
