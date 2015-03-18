// Author Aayush Bhandari

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.*;
import com.cybozu.labs.langdetect.*;

public class XmlGenerator 
{
	// Pattern for recognizing a URL, based off RFC 3986
	
	private static final Pattern urlPattern = Pattern.compile(
				        "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
				        + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
				        + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
				        Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	
	public static void main(String[] args)
	{
		
		String queryNumber[]={"MB51","MB52","MB53","MB54","MB55","MB56","MB57","MB58","MB59",
				"MB60","MB61","MB62","MB63","MB64","MB65","MB66","MB67","MB68","MB69",
				"MB70","MB71","MB72","MB73","MB74","MB75","MB76","MB77","MB78","MB79",
				"MB80","MB81","MB82","MB83","MB84","MB85","MB86","MB87","MB88","MB89",
				"MB90","MB91","MB92","MB93","MB94","MB95","MB96","MB97","MB98","MB99",
				"MB100","MB101","MB102","MB103","MB104","MB105","MB106","MB107","MB108",
				"MB109","MB110"};
		
		long queryTweetTime[]= {35124912364457984L,33199275462627328L,35123660406657024L,34836253874593792L,
				34782005925249024L,33375478505013248L,32919462151720960L,35043166813949952L,35012866822119424L,34436767029399552L,
				34695232985645056L,35111217613766657L,33986811273420800L,35002593419726848L,34438945404092416L,
				33684239400566784L,34414281218859008L,32532358276063232L,35073980729802752L,34729342210678784L,
				31692185296441344L,32187009527193600L,33505508052701184L,34509105032859648L,35094611483426816L,
				34922941233762304L,34580405671698432L,35066972253192192L,33616636950880256L,35067092726185984L,
				34846177434275840L,33190436147306496L,34913594311311361L,35060229741744128L,34764832553041920L,
				32075949373652992L,35114725998215168L,34775520600129536L,32987948139941888L,34921291089711104L,
				31492039682039808L,34780760116305920L,34936026753404928L,34971360627261440L,35092866069962752L,
				31146959217631232L,35100296694861824L,34856175732264960L,35105993557807104L,34737748464115712L,
				35017330933108736L,35051942250020864L,34763068797886464L,34637550534529026L,34803471081275392L,
				34915635595059201L,34941706969288704L,33823828090036224L,34390055866859520L,34838554811043840L};
		
		try
		{
			// Load Language Detection Library
			DetectorFactory.loadProfile("langdetect-03-03-2014/profiles");
			
			
			//Do this for the 60 different query topics
			 BufferedWriter fos[]= new BufferedWriter[60];
			 for (int i=0; i<60; i++)
			 {
				 // Open an output stream to .trectext file
				  File filename = new File("/Users/Aayush/Desktop/School/Fall-2014/849/TermProject/data/"+ queryNumber[i]+ ".trec");//xml file path to be created with the id name
		    	  FileWriter fw = new FileWriter(filename.getAbsoluteFile());
		    	  fos[i]= new BufferedWriter(fw,10000000);
			 }
				
			//map generator
			  File filename = new File("/Users/Aayush/Desktop/School/Fall-2014/849/TermProject/data/map.txt");//xml file path to be created with the id name
	    	  FileWriter fw = new FileWriter(filename.getAbsoluteFile());
	    	  BufferedWriter mapWriter= new BufferedWriter(fw,10000000);
				
				// Open a input stream to .json file
				Reader reader= new InputStreamReader(new FileInputStream("/Users/Aayush/Desktop/School/Fall-2014/849/TermProject/merged.json"), "utf-8");
				BufferedReader fis = new BufferedReader(reader, 10000000);

				// Read .json file for parsing purposes
				String jsonString;
				int totalTweets=0;
				int totalRetweets=0;
				int totalNonEnglishTweets=0;
				int totalTweetsIncluded=0;
				
				while((jsonString=fis.readLine())!=null)
				{
					totalTweets++;
					// Parse the .json file
					JSONObject obj = new JSONObject(jsonString);
					String text = (String) obj.get("text");
					String id = (String) obj.get("id_str");
					long idLong= Long.parseLong(id);
					boolean retweet = (boolean) obj.get("retweeted");
					if (retweet==true)
					{
						totalRetweets++;
					}
					// Detect the language
					Detector detector = DetectorFactory.create();
					detector.append(text);
					
					String language="";
					
					try 
					{
						language = detector.detect();
						if (!language.equals("en"))
						{
							totalNonEnglishTweets++;
						}
					}
					catch (com.cybozu.labs.langdetect.LangDetectException e) {}
					
					if(retweet==false && language.equals("en"))
					{
						totalTweetsIncluded++;
						//Find all the urls in the tweet
						boolean urlFound=false;
						 String tempText = new String(text);
						 Matcher matcher = urlPattern.matcher(text);
						 while (matcher.find()) 
						 {
						     int matchStart = matcher.start(1);
						     int matchEnd = matcher.end();
						     urlFound=true;
						     // now you have the offsets of a URL match
						     String url = text.substring(matchStart, matchEnd);
						     tempText=tempText.replace(url, "");
						}
						
						text=tempText;

						String trecMessage= 
						"<DOC>\n" +
						"	<DOCNO>" + id + "</DOCNO>\n" +
						"	<TEXT>"	+ text + "</TEXT>\n"+
						"</DOC>\n";
						if(urlFound)
						{
							mapWriter.write(id+"\n");
						}
						 //write messages to appropriate files based on query tweet time
						 for (int j=0; j<60; j++)
						 {
							 if (idLong<=queryTweetTime[j])
							 {
								 fos[j].write(trecMessage);
							 }
						 }
					}
				}
				
				System.out.println("Total Tweets: " +  totalTweets + "\n" +
									"Total ReTweets: " +	totalRetweets + "\n" +
									"Total Non English Tweet: "+ totalNonEnglishTweets + "\n" +
									"Total Tweet Included: " + totalTweetsIncluded + "\n");		
				//close the file stream
				fis.close();
				for(int i=0; i<60; i++)
				{
					fos[i].close();
				}
				mapWriter.close();
			
			
			
		} catch (Exception e){ e.printStackTrace();}
	}
}
