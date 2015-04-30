package LogFuc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class EasyFile {

	public static BufferedReader getBr(String readPath) throws FileNotFoundException{
		File file=new File(readPath);
		FileInputStream fis=new FileInputStream(file);
		InputStreamReader isr=new InputStreamReader(fis);
		BufferedReader br=new BufferedReader(isr);
		return br;
	}
	
	public static BufferedWriter getBw(String writePath) throws IOException{
		File file=new File(writePath);
		FileWriter fw=new FileWriter(file);
		BufferedWriter bw=new BufferedWriter(fw);
		return bw;
		
	}
	
	public static ObjectInputStream open(String rPath) throws IOException{
		FileInputStream fis=new FileInputStream(new File(rPath));
		ObjectInputStream ois=new ObjectInputStream(fis);
		return ois;
	}
	
	public static ObjectOutputStream save(String wPath) throws IOException{
		FileOutputStream fos=new FileOutputStream(wPath);
		ObjectOutputStream oos=new ObjectOutputStream(fos);
		return oos;
	}
}
