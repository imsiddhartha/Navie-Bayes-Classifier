import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class data
{
//	String label;
	int count;
	public data()
	{
		count=1;
	}
}
public class Assignment4 {

	/**
	 * @param args
	 */
	public static HashMap<String,data> classlabel=new HashMap<String,data>();
	static HashMap<Integer,HashMap<String,HashMap<String,data>>> features=new HashMap<Integer, HashMap<String,HashMap<String,data>>>();
	//static List<HashMap<String,HashMap<String,data>>> features1=new ArrayList<HashMap<String,HashMap<String,data>>>();
	//static List<HashMap<String,HashMap<String,data>>> features2=new ArrayList<HashMap<String,HashMap<String,data>>>();
	static List<String> filedata1=new ArrayList<String>();
	static List<String> missclasssified=new ArrayList<String>();
	static List<String> testdata=new ArrayList<String>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//readdata("breast-cancer-wisconsin.data");
		readdata("BankMarketing_Discrete.csv");
		
		//Collections.shuffle(filedata1);
		System.out.println("Bank Data");
		System.out.println("No. of lines readed: "+filedata1.size());
		int conmat[][]=new int[2][2];
		double accu[]=new double[10];double sum=0.0,max=0.0;
		for(int i=0;i<2;i++)
		{
			for(int j=0;j<2;j++)
					conmat[i][j]=0;
		}
		for(int i=0;i<10;i++)
		{
			randshuffle();
			separatedata();
			double acc=bayes(conmat);
			if(acc>max)
				max=acc;
			accu[i]=acc;
			sum=sum+acc;
			features.clear();
			testdata.clear();
			if(i!=9)
				missclasssified.clear();
			classlabel.clear();
		}
		double mean=sum/10;
		sum=0;
		for(int i=0;i<10;i++)
		{
			sum=sum+(mean-accu[i])*(mean-accu[i]);
		}
		double dev=Math.sqrt(sum/10);
		System.out.println("Best Accuracy: "+max);
		System.out.println("Mean Accuracy: "+mean);
		System.out.println("Deviaton Accuracy: "+dev);
		System.out.println("Misssclassified Data: "+missclasssified);
		System.out.println("Confusion Matrix");
		System.out.println(conmat[0][0]+" "+conmat[0][1]);
		System.out.println(conmat[1][0]+" "+conmat[1][1]);
		filedata1.clear();
		missclasssified.clear();
		
		readdata("breast-cancer-wisconsin.data");
		System.out.println("Breast Cancer Data");
		//System.out.println("No. of lines readed: "+filedata1.size());
		int trainingmatrix[][]=new int[filedata1.size()/2+1][10];
		int conmat2[][]=new int[2][2];
		max=0.0;
		for(int j=0;j<10;j++)
		{
			randshuffle();
			separatedata2(trainingmatrix);
//			System.out.println(filedata1.size()/2+1);
//			System.out.println(classlabel.size());
//			System.out.println(classlabel.get("2").count);
//			System.out.println(classlabel.get("4").count);
			double means[]=new double[10];		//means of all cols
			double devs[]=new double[10];		//deviatopns of all cols
			for(int i=0;i<10;i++)
			{
				means[i]=getmean(trainingmatrix,i);
				devs[i]=getdev(trainingmatrix,i,means[i]);
			}
			
			double acc=bayes2(conmat2,means,devs);
			//System.out.println(acc);
			if(acc>max)
				max=acc;
			accu[j]=acc;
			sum=sum+acc;
			features.clear();
			testdata.clear();
			if(j!=9)
				missclasssified.clear();
			classlabel.clear();
		}
		mean=sum/10;
		sum=0;
		for(int i=0;i<10;i++)
		{
			sum=sum+(mean-accu[i])*(mean-accu[i]);
		}
		dev=Math.sqrt(sum/10);
		System.out.println("Best Accuracy: "+max);
		System.out.println("Mean Accuracy: "+mean);
		System.out.println("Deviaton Accuracy: "+dev);
		System.out.println("Misssclassified Data: "+missclasssified);
		System.out.println("Confusion Matrix");
		System.out.println(conmat2[0][0]+" "+conmat2[0][1]);
		System.out.println(conmat2[1][0]+" "+conmat2[1][1]);
	}
	
	
	private static double bayes2(int[][] conmat,double[] means,double[] devs) {
		// TODO Auto-generated method stub
		int accuracy=0;
		int count=0;
		//int notin=0;
		for(String line:testdata)
		{
			double ans=1.0,max=0.0,cur=0.0,problabel; 
			String arr[]=line.split(",");
			String maxlabel="";
			int len=arr.length;
			int size=filedata1.size()/2+1;			//training data size
			for (Map.Entry<String, data> entry : classlabel.entrySet()) 
			{
				ans=1.0;
				String label=entry.getKey();
				int val=entry.getValue().count;
				problabel=1.0*val/size;		//p(wi)
				//System.out.println(label+" "+val+" "+size+" "+problabel+" ");
				for(int i=0;i<len-1;i++)			//i=1 ignoring first col
				{
					if(!features.containsKey(i))
						continue;
					else
					{
						if(!features.get(i).containsKey(arr[i]))
						{
							//System.out.println(i+" "+arr[i]+" feature value not in training data");
							
							continue;
						}
						else
						{
							if(!features.get(i).get(arr[i]).containsKey(label))
							{
								//System.out.println(i+" "+arr[i]+" "+label+" label not in training data");
								//notin++;
								continue;
							}
							int c=Integer.parseInt(arr[i]);		//if c is zero than add something
							double ds=gaussian(c,means[i],devs[i]);
							ans=ans*ds;		//count of intersection/class lbel count
							c=features.get(i).get(arr[i]).get(label).count;		//if c is zero than add something
							ans=ans*ds*c/val;		//count of intersection/class lbel count
						}
					}
					
				}
				
				cur=ans*problabel;
				if(cur>max)
				{
					max=cur;
					maxlabel=label;
				}
			}
			if(maxlabel.equals(arr[len-1]))		//maxlabel ==classlabel
			{
				accuracy++;
				if(maxlabel.equals("2"))
					conmat[0][0]++;
				else
					conmat[1][1]++;
			}
			else
			{
				if(count<3)
					missclasssified.add(line);
				if(maxlabel.equals("2") && arr[len-1].equals("4"))
					conmat[0][1]++;
				else
					conmat[1][0]++;
				count++;
			}
			
		}
		//System.out.println("accuracy: "+accuracy);
		//System.out.println("Misssclassified Data: "+missclasssified);
		//System.out.println("% accuracy: "+accuracy*100.0/testdata.size());
		return accuracy*100.0/testdata.size();
	}
	
	private static double gaussian(int c, double mean, double dev) {
		// TODO Auto-generated method stub
		double ans=1/(3.14*2*Math.sqrt(dev));
		double p=(c-mean)/dev;
		p=p*p;
		ans=ans*Math.pow(Math.E, -p);
		return ans;
	}


	private static double getdev(int[][] trainingmatrix, int i,double mean) {
		// TODO Auto-generated method stub
		int size=filedata1.size()/2+1;
		double sum=0;
		for(int j=0;j<size;j++)
		{
			sum=sum+(trainingmatrix[j][i]-mean)*(trainingmatrix[j][i]-mean);
		}
		return Math.sqrt(sum/size);
	}

	private static double getmean(int[][] trainingmatrix, int i) {
		// TODO Auto-generated method stub
		int size=filedata1.size()/2+1;
		double sum=0;
		for(int j=0;j<size;j++)
		{
			sum+=trainingmatrix[j][i];
		}
		return 1.0*sum/size;
	}

	private static void separatedata2(int [][]trainingmatrix) {
		// TODO Auto-generated method stub
		int c=0;
		int size=filedata1.size();
		for(String line:filedata1)
		{
			if(c<size/2+1)	//80% training data
			{
				
				String arr[]=line.split(",");
				int len=arr.length;
				if(!classlabel.containsKey(arr[len-1]))
					classlabel.put(arr[len-1], new data());
				else
					classlabel.get(arr[len-1]).count++;
				
				for(int i=0;i<len-1;i++)
				{
					trainingmatrix[c][i]=Integer.parseInt(arr[i]);
					if(!features.containsKey(i))
					{
						HashMap<String,data>lmap=new HashMap<String,data>();
						lmap.put(arr[len-1], new data());
						HashMap<String,HashMap<String,data>> lmap1=new HashMap<String,HashMap<String,data>>();
						lmap1.put(arr[i], lmap);
						features.put(i, lmap1);
					}
					else
					{
						if(!features.get(i).containsKey(arr[i]))
						{
							HashMap<String,data>lmap=new HashMap<String,data>();
							lmap.put(arr[len-1], new data());
							features.get(i).put(arr[i], lmap);
						}
						else
						{
							if(!features.get(i).get(arr[i]).containsKey(arr[len-1]))
							{
								features.get(i).get(arr[i]).put(arr[len-1], new data());
							}
							else
								features.get(i).get(arr[i]).get(arr[len-1]).count++;
						}
						
					}
					
				}
			}
			else		//test data
			{
				testdata.add(line);
				
			}
			c++;	
		}
	}







	/*-------------------Function for Bank DATA---------------------------------------*/
	private static double bayes(int[][] conmat) {
		// TODO Auto-generated method stub
		int accuracy=0;
		int count=0;
		//int notin=0;
		for(String line:testdata)
		{
			double ans=1.0,max=0.0,cur=0.0,problabel; 
			String arr[]=line.split(",");
			String maxlabel="";
			int len=arr.length;
			int size=filedata1.size()/2+1;			//training data size
			for (Map.Entry<String, data> entry : classlabel.entrySet()) 
			{
				ans=1.0;
				String label=entry.getKey();
				int val=entry.getValue().count;
				problabel=1.0*val/size;		//p(wi)
				//System.out.println(label+" "+val+" "+size+" "+problabel+" ");
				for(int i=0;i<len-1;i++)			//i=1 ignoring first col
				{
					if(!features.containsKey(i))
						continue;
					else
					{
						if(!features.get(i).containsKey(arr[i]))
						{
							System.out.println(i+" "+arr[i]+" feature value not in training data");
							
							continue;
						}
						else
						{
							if(!features.get(i).get(arr[i]).containsKey(label))
							{
								System.out.println(i+" "+arr[i]+" "+label+" label not in training data");
								//notin++;
								continue;
							}
							int c=features.get(i).get(arr[i]).get(label).count;		//if c is zero than add something
							double ds=1.0*c/val;
							ans=ans*ds;		//count of intersection/class lbel count
						}
					}
				}
				cur=ans*problabel;
				if(cur>max)
				{
					max=cur;
					maxlabel=label;
				}
			}
			if(maxlabel.equals(arr[len-1]))		//maxlabel ==classlabel
			{
				accuracy++;
				if(maxlabel.equals("\"yes\""))
					conmat[0][0]++;
				else if(maxlabel.equals("\"no\""))
					conmat[1][1]++;
			}
			else
			{
				if(count<3)
					missclasssified.add(line);
				if(maxlabel.equals("\"yes\"") && arr[len-1].equals("\"no\""))
					conmat[0][1]++;
				else
					conmat[1][0]++;
				count++;
			}
			
		}
		//System.out.println("accuracy: "+accuracy);
		//System.out.println("Misssclassified Data: "+missclasssified);
		//System.out.println("% accuracy: "+accuracy*100.0/testdata.size());
		return accuracy*100.0/testdata.size();
	}

	private static void separatedata() {
		// TODO Auto-generated method stub
		int c=0;
		int size=filedata1.size();
		for(String line:filedata1)
		{
			if(c<size/2+1)	//80% training data
			{
			
				String arr[]=line.split(",");
				int len=arr.length;
				if(!classlabel.containsKey(arr[len-1]))
					classlabel.put(arr[len-1], new data());
				else
					classlabel.get(arr[len-1]).count++;
				
				for(int i=0;i<len-1;i++)
				{
					if(!features.containsKey(i))
					{
						HashMap<String,data>lmap=new HashMap<String,data>();
						lmap.put(arr[len-1], new data());
						HashMap<String,HashMap<String,data>> lmap1=new HashMap<String,HashMap<String,data>>();
						lmap1.put(arr[i], lmap);
						features.put(i, lmap1);
					}
					else
					{
						if(!features.get(i).containsKey(arr[i]))
						{
							HashMap<String,data>lmap=new HashMap<String,data>();
							lmap.put(arr[len-1], new data());
							features.get(i).put(arr[i], lmap);
						}
						else
						{
							if(!features.get(i).get(arr[i]).containsKey(arr[len-1]))
							{
								features.get(i).get(arr[i]).put(arr[len-1], new data());
							}
							else
								features.get(i).get(arr[i]).get(arr[len-1]).count++;
						}
						
					}
					
				}
			}
			else		//test data
			{
				testdata.add(line);
				
			}
			c++;	
		}
	}

	private static void randshuffle() {
		// TODO Auto-generated method stub
		Collections.shuffle(filedata1);
//		Collections.shuffle(filedata1);
//		Collections.shuffle(filedata1);
		
	}

	private static void readdata(String filename) {
		// TODO Auto-generated method stub
		try {
			BufferedReader br=new BufferedReader(new FileReader(filename));
			String line=br.readLine();
			while(line!=null)
			{
				if(line.contains("?"))			//removing missing values
				{
					line=br.readLine();
					continue;
				}
				filedata1.add(line);
				line=br.readLine();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
