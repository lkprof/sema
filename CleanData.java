package LogFuc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CleanData {
	
	
	public static void ChuLiShuJu(String filePath) throws IOException {
		FileInputStream fis = new FileInputStream(new File(filePath));
		InputStreamReader is = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(is);
		br.readLine();
		FileWriter fw = new FileWriter(new File(
				"C:\\Users\\li\\Desktop\\chulihou.txt"));
		BufferedWriter bw = new BufferedWriter(fw);
		String lineTxt = "";
		String deleteSym = "_-";// 如果有其中一种字符，那么就去掉字符后面的字串
		while ((lineTxt = br.readLine()) != null) {
			String[] splitArray = lineTxt.split("\t");
			String newSplit1 = "", newSplit2 = "";
			System.out.println(lineTxt+splitArray[1]);
			newSplit1=toSBC(splitArray[0]);//全角转半角
			newSplit2=toSBC(splitArray[1]);
			newSplit1=newSplit1.toLowerCase();//处理大小写
			newSplit2=newSplit2.toLowerCase();
			
			// 去掉后面没用的部分,这种方法有问题，假如一行里有两个deleteSym,应该去掉哪个后面没用的部分，先这样做着
			for (int i = 0; i < deleteSym.length(); i++) {
				
				if (newSplit2.contains(String.valueOf(deleteSym.charAt(i)))) {
					int index = newSplit2.lastIndexOf(String.valueOf(deleteSym
							.charAt(i)));
					newSplit2 = newSplit2.substring(0, index);
					break;
				}
			}

			newSplit1=deleteNoUseSymbol(newSplit1);
			newSplit2=deleteNoUseSymbol(newSplit2);
			String tempString =newSplit1 + "\t"
					+ newSplit2 + "\t" + splitArray[2];
			bw.write(tempString);
			bw.newLine();

		}
		bw.flush();
		fw.close();
		fis.close();
	}
	
	//全角转半角
	public static String toSBC(String s){
		char[] charArray=s.toCharArray();
		for(int i=0;i<charArray.length;i++){
			if(charArray[i]=='\u3000'){
				charArray[i]=' ';
			}else if(charArray[i] > '\uFF00' && charArray[i] < '\uFF5F'){
				charArray[i]=(char)(charArray[i]-65248);
			}	
		}
		
//		return charArray.toString(); //返回的不是字符串
		String c=new String(charArray);
		return c;
	}
	//删除没用的标点符号
	public static String deleteNoUseSymbol(String s){
		int index=0;
		char[] charArray=s.toCharArray();
		for(int i=0;i<charArray.length;i++){
			if((charArray[i]>=19968&&charArray[i]<=40869)||(charArray[i]>=97&&charArray[i]<=122)
					||(charArray[i]>=65&&charArray[i]<=90)
					||(charArray[i]>=48&&charArray[i]<=57)
					||(charArray[i]==' ')){
				charArray[index]=charArray[i];
				index++;
			}
		}
		char[] temp=new char[index];
		for(int i=0;i<index;i++){
			temp[i]=charArray[i];
		}
		String newString=new String(temp);
		return newString;
	}
	
	public static void main(String[] args) throws IOException {
		// TODO 自动生成的方法存根
		CleanData.ChuLiShuJu("C:\\Users\\li\\Desktop\\测试集has_point_nz_cut.txt");
//		String s="液吗？＿育儿问答＿宝宝树写？＿已解决　－　阿里巴巴生意经";
//		System.out.println(toSBC(s));
		
		
		


	}
		
		

}
