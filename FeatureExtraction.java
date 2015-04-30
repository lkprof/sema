package LogFuc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class FeatureExtraction {
	
	
	public static void generateFeature(String readPath,String writePath) throws IOException{
		BufferedReader br=EasyFile.getBr(readPath);
		BufferedWriter bw=EasyFile.getBw(writePath);
//		br.readLine();//读测试集的时候加上
		String line="";
		while((line=br.readLine())!=null){
			String[] splitArray=line.split("\t");
			bw.write(splitArray[2].trim()+","+f_1(line)+","+f_2(line)+","
			+f_3(line)+","+f_4(line)+","+f_5(line)+","+f_6(line)+","+f_7(line)
			+","+f_8(line)+","+f_9(line)+","+f_10(line));
			bw.newLine();
		}
		bw.flush();bw.close();br.close();
	}
	
	//query匹配上的词的个数除以query中词的个数
	public static String f_1(String line){
		String[] splitArray=line.split("\t");
		String[] Array_Query = splitArray[0].split(" ");
		String[] Array_Biao = splitArray[1].split(" ");
		List<String> queryList=new ArrayList<String>();
		List<String> titleList=new ArrayList<String>();
		
		for(String s:Array_Query){//去掉数组里的空字符串
			if(!s.equals("")){
				queryList.add(s);
			}
		}
		for(String s:Array_Biao){
			if(!s.equals("")){
				titleList.add(s);
			}
		}
		int NumOfMatch = 0;//匹配上词的个数
		for(int i=0;i<queryList.size();i++){
			for(int j=0;j<titleList.size();j++){
				if(titleList.get(j).contains(queryList.get(i))){
					NumOfMatch++;
					break;
				}
			}
		}
		
		float MatchVal = (float) NumOfMatch / queryList.size();
		BigDecimal bd=new BigDecimal(MatchVal).setScale(4, BigDecimal.ROUND_HALF_UP);//保留四位小数，四舍五入
		return bd.toPlainString();
	}
	
	//用二字串做特征,匹配上的query词个数除以query长度
	public static String f_2(String line){
		int MatchNum = 0;
		String[] lineArray = line.split("\t");
		lineArray[0]=lineArray[0].replaceAll(" ", "");//去掉没用的空格
		lineArray[1]=lineArray[1].replaceAll(" ", "");
		
		
		if (lineArray[0].length() > 0 && lineArray[1].length() > 0) {
			String[] Query_Bigrams = new String[lineArray[0].length() - 1];
			String[] Biao_Bigrams = new String[lineArray[1].length() - 1];
			for (int i = 0; i < lineArray[0].length() - 1; i++) {
				Query_Bigrams[i] = String.valueOf(lineArray[0].charAt(i)
						+ lineArray[0].charAt(i + 1));
			}
			for (int j = 0; j < lineArray[1].length() - 1; j++) {
				Biao_Bigrams[j] = String.valueOf(lineArray[1].charAt(j)
						+ lineArray[1].charAt(j + 1));
			}
			for (int i = 0; i < Query_Bigrams.length; i++) {
				for (int j = 0; j < Biao_Bigrams.length; j++) {
					if (Biao_Bigrams[j].equals(Query_Bigrams[i])) {
						MatchNum++;
						break;
					}
				}
			}
			
			float MatchVal = (float) MatchNum / Query_Bigrams.length;
			BigDecimal bd=new BigDecimal(MatchVal).setScale(4, BigDecimal.ROUND_HALF_UP);
			return bd.toPlainString();
		}else{
			return String.valueOf(0);
		}
	}
	
	//query中的词个数能够全匹配上返回1 否则返回0
	public static String f_3(String line){
		String[] splitArray=line.split("\t");
		String[] Array_Query = splitArray[0].split(" ");
		List<String> queryList=new ArrayList<String>();
		
		for(String s:Array_Query){//去掉数组里的空字符串
			if(!s.equals("")){
				queryList.add(s);
			}
		}
		int NumOfMatch = 0;//匹配上词的个数
		for(int i=0;i<queryList.size();i++){
			if(splitArray[1].contains(queryList.get(i))){
				NumOfMatch++;
			}
		}
		if(NumOfMatch==queryList.size()){
			return String.valueOf(1);
		}else{
			return String.valueOf(0);
		}
		
	}
	
	//query中匹配的个数除以标题中词的个数
	public static String f_4(String line){
		String[] splitArray=line.split("\t");
		String[] Array_Query = splitArray[0].split(" ");
		String[] Array_Biao = splitArray[1].split(" ");
		List<String> queryList=new ArrayList<String>();
		List<String> titleList=new ArrayList<String>();
		
		for(String s:Array_Query){//去掉数组里的空字符串
			if(!s.equals("")){
				queryList.add(s);
			}
		}
		for(String s:Array_Biao){
			if(!s.equals("")){
				titleList.add(s);
			}
		}
		int NumOfMatch = 0;//匹配上词的个数
		for(int i=0;i<queryList.size();i++){
			if(titleList.contains(queryList.get(i))){
				NumOfMatch++;
			}
		}
		float MatchVal=0;
		if(titleList.size()!=0){
			MatchVal = (float) NumOfMatch / titleList.size();
		}
		BigDecimal bd=new BigDecimal(MatchVal).setScale(4, BigDecimal.ROUND_HALF_UP);
		return bd.toPlainString();
	}
	
	//匹配上的词除以query中词的个数加标题词的个数减匹配词的个数
	public static String f_5(String line){
		String[] splitArray=line.split("\t");
		String[] Array_Query = splitArray[0].split(" ");
		String[] Array_Biao = splitArray[1].split(" ");
		List<String> queryList=new ArrayList<String>();
		List<String> titleList=new ArrayList<String>();
		
		for(String s:Array_Query){//去掉数组里的空字符串
			if(!s.equals("")){
				queryList.add(s);
			}
		}
		for(String s:Array_Biao){
			if(!s.equals("")){
				titleList.add(s);
			}
		}
		int NumOfMatch = 0;//匹配上词的个数
		for(int i=0;i<queryList.size();i++){
			if(titleList.contains(queryList.get(i))){
				NumOfMatch++;
			}
		}
		
		float MatchVal = (float) NumOfMatch / (titleList.size()+queryList.size()-NumOfMatch);
		BigDecimal bd=new BigDecimal(MatchVal).setScale(4, BigDecimal.ROUND_HALF_UP);
		return bd.toPlainString();
	}
	
	//query的长度除以标题的长度
	public static String f_6(String line){
		String[] splitArray=line.split("\t");
		String[] Array_Query = splitArray[0].split(" ");
		String[] Array_Biao = splitArray[1].split(" ");
		List<String> queryList=new ArrayList<String>();
		List<String> titleList=new ArrayList<String>();
		
		for(String s:Array_Query){//去掉数组里的空字符串
			if(!s.equals("")){
				queryList.add(s);
			}
		}
		for(String s:Array_Biao){
			if(!s.equals("")){
				titleList.add(s);
			}
		}
		
		float val=0;
		if(titleList.size()!=0){
			val=queryList.size()/(float)titleList.size();
		}
//		System.out.println(val);
		BigDecimal bd=new BigDecimal(val).setScale(4, BigDecimal.ROUND_HALF_UP);
		return bd.toPlainString();
	}
	//标题是否包含query 是1否则为0
	public static String f_7(String line){
		String[] splitArray=line.split("\t");
		splitArray[0]=splitArray[0].replaceAll(" ", "");
		splitArray[1]=splitArray[1].replaceAll(" ", "");
		if(splitArray[1].contains(splitArray[0])){
			return String.valueOf(1);
		}else{
			return String.valueOf(0);
		}
	}
	
	//单字做特征
	public static String f_8(String line){
		String[] splitArray=line.split("\t");
		splitArray[0]=splitArray[0].replaceAll(" ", "");
		splitArray[1]=splitArray[1].replaceAll(" ", "");
		int matchNum=0;
		for(int i=0;i<splitArray[0].length();i++){
			if(splitArray[1].indexOf(splitArray[0].charAt(i))!=-1){
				matchNum++;
			}
		}
		float val=matchNum/(float)splitArray[0].length();
		BigDecimal bd=new BigDecimal(val).setScale(4, BigDecimal.ROUND_HALF_UP);
		return bd.toPlainString();
	}
	//querylength
	public static String f_9(String line){
		String[] splitArray=line.split("\t");
		splitArray[0]=splitArray[0].replaceAll(" ", "");
		return String.valueOf(splitArray[0].length());
	}
	//title length
	public static String f_10(String line){
		String[] splitArray=line.split("\t");
		splitArray[1]=splitArray[1].replaceAll(" ", "");
		return String.valueOf(splitArray[1].length());
	}
	
	public static void addFeatures(String readPath,String addedPath,String writePath) throws IOException{
		BufferedReader br=EasyFile.getBr(readPath);
		BufferedReader br1=EasyFile.getBr(addedPath);
		BufferedWriter bw=EasyFile.getBw(writePath);
		String line="";
		int flag=1;
		while((line=br.readLine())!=null){
			bw.write(line+",");
			String[] splitArray=br1.readLine().split("\t");
			for(int i=0;i<splitArray.length;i++){
				BigDecimal bd=new BigDecimal(splitArray[i]).setScale(4, BigDecimal.ROUND_HALF_UP);
				if(i<splitArray.length-1){
					bw.write(bd.toPlainString()+",");
				}else{
					bw.write(bd.toPlainString());
				}
			}
			bw.write(" "+"#"+flag++);
			bw.newLine();
		}
		bw.flush();bw.close();br.close();br1.close();
	}
	
	public static void csvToLibsvmFormat(String readPath,String writePath) throws IOException{
		BufferedReader br=EasyFile.getBr(readPath);
		BufferedWriter bw=EasyFile.getBw(writePath);
		String line="";
		int flag=1;
		while((line=br.readLine())!=null){
			String[] splitArray=line.split(",");
			bw.write(splitArray[0]+" ");
			for(int i=1;i<splitArray.length;i++){
				if(splitArray[i].contains("#")){
					if(Float.valueOf(splitArray[i].split(" ")[0])>=0.00001){
						bw.write(i+":"+splitArray[i].split(" ")[0]+" ");
					}
				}else{
					if(Float.valueOf(splitArray[i])>=0.00001){
						bw.write(i+":"+splitArray[i]+" ");
					}
				}
				
			}
			bw.newLine();
		}
		bw.flush();bw.close();br.close();
	}
	
	public static void main(String[] args) throws IOException {
		// TODO 自动生成的方法存根
		String fenCiPath="C:\\Users\\li\\Desktop\\chulihou.txt";
		String featurePath="C:\\Users\\li\\Desktop\\features";
		String addedPath="C:\\Users\\li\\Desktop\\train_word_distance_score_new.txt";
		String allFeaturesPath="C:\\Users\\li\\Desktop\\allFeatures";
		String svmPath="C:\\Users\\li\\Desktop\\SVMformatFeatures";
		
		
//		String s=" 申通快递 菏泽 单县 曹庄 	 单县 的 申通  单县 吧 	 1 ";
//		System.out.println(f_1(s));
//		BigDecimal bd=new BigDecimal(1.22222333);
//		System.out.println(bd.setScale(55));
		generateFeature(fenCiPath, featurePath);
		addFeatures(featurePath, addedPath, allFeaturesPath);
		csvToLibsvmFormat(allFeaturesPath, svmPath);
		
	}

}
