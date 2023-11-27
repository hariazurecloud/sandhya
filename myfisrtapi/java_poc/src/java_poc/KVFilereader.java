package java_poc;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;

public class KVFilereader {
	private static final String SEMICOLON = ";";
    public static <E> void main(String args[]) throws IOException, NoMatchFoundException 
    {
        	BufferedReader br = new BufferedReader(new FileReader("C:\\old laptop data\\Practice\\KVK01.HYD.808.20221108.123217.txt"));
        	String hauditC="";
        	String amount="";//
    		String str = br.readLine();
    		while (str != null) {
    			if(str.startsWith("HAUDIT"))
    			{
    				hauditC= str.split(";")[1];
    				amount= str.split(";")[2];
    				break;
    			}
    		}
    		
    		double amount1 = Double.parseDouble(amount);
    		
            List<DataPOJO> dList = new ArrayList<DataPOJO>();
            String line = "";
            double sum=0;
            //Read to skip the header	
           // br.readLine(); ---> Commented it as it is not necessary
            //Reading from the second line     
            while ((line = br.readLine()) != null) 
            {
                String data[] = line.split(";");
                DataPOJO details = new DataPOJO(data[0],Integer.parseInt(data[1]),data[2],data[3],Double.parseDouble(data[4]));
                //System.out.println("test1 "+details);
                dList.add(details);
                //System.out.println("test "+dList);
        		sum=sum+Double.parseDouble(data[4]);  		
            }	
            double sum1=sum;
            //Lets print the List
            System.out.println("----Below is the list we are having----");
            for(DataPOJO a : dList)
            {
                System.out.println(a.getRecord_Type()+"   "+a.getLineNumber()+"   "
                		+a.getRefDocNum()+"   "+a.getCity()+" "+a.getAmount());
                
               // System.out.println(dList);
                //break;
               // System.out.println(a.getRefDocNum());
            }
           // System.out.println(dList);
            System.out.println();
            //System.out.println("Below is the ref doc and its related data");
            System.out.println();
            
            Map<String, List<DataPOJO>> refDoc1 = dList.stream().collect(java.util.stream.Collectors.groupingBy(DataPOJO::getRefDocNum));
            
           /* List<List<DataPOJO>> list = new ArrayList<List<DataPOJO>>();
            list.addAll(refDoc1.values());
            
            System.out.println("Different data of same ref Doc:-");
            Set<Entry<String, List<DataPOJO>>> set=refDoc1.entrySet();
            
			
			  for(Entry<String, List<DataPOJO>> eachEntry: refDoc1.entrySet()) {
			  list.add(eachEntry.getValue());			  
			  }
			 
            System.out.println(list);
            */
            
            //System.out.println("test "+);
            
           
          //TEMP
       
            System.out.println("===============");

            List<List<DataPOJO>> listOfLists = new ArrayList<List<DataPOJO>>(); 
            listOfLists.addAll(refDoc1.values());
            System.out.println(listOfLists);
            //System.out.println("test "+listOfLists);
            
            //System.out.println("test "+listOfLists.addAll(listOfLists));
            //listOfLists.forEach((refDoc1)->
              //  list.forEach((refDoc1)->System.out.println(refDoc1));
           // listOfLists.add();   
            //System.out.println("test "+refDoc1.); 
            System.out.println("===============");
            //TEMP
            
            //System.out.println("Different data of same ref Doc:-");
    		
    		//System.out.println(refDoc1);
    		System.out.println();
    		int r= refDoc1.size();
    		int temp=r;
    		String s1=Integer.toString(temp);
    		//System.out.println("Ref Doc Count after eliminating duplicates "+r);           
            
    		if(hauditC.equals(s1))
    		{
    			System.out.println("Ref Doc given in headed file matched with documents given in file is "+hauditC);
    		}
    		else
    		{
    			//throw new NoMatchFoundException("Ref Doc given in headed file not matched with documents given in file is "+hauditC+" it should be "+r);
    			throw new NoMatchFoundException("Ref Doc given in headed file not matched with documents given in file is "+hauditC+" it should be "+r);
    			//System.out.println("Ref Doc given in headed file not matched with documents given in file is "+hauditC);
    		}
               
    		System.out.println();
    		System.out.println();
    		/*double amountSum=Integer.parseInt(amount);*/
    		if(sum1==amount1)
    		{
    			System.out.println("Amounts given in file header are equal to given in file line data ");
    			System.out.println("Amounts in header "+amount1);
    			System.out.println("Amounts in file "+sum1);
    		}
    		else
    		{
    			throw new NoMatchFoundException("Amounts given in file header are not equal to given in file line data, Amounts in header "+amount1+" and Amounts in file "+sum1);
    			//System.out.println("Amounts given in file header are not equal to given in file line data, Amounts in header "+amount1+" and Amounts in file "+sum1);
    		}
    		br.close();
    }
}