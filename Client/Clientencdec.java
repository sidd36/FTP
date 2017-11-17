package Client;

import java.io.*;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Clientencdec{

   public static void encdec(int cipherMode,String key,File inputFile,File outputFile){
	 try {
	       SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
	       byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		   IvParameterSpec ivspec = new IvParameterSpec(iv);
	       Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	       cipher.init(cipherMode, secretKey,ivspec);
	       FileInputStream inputStream = new FileInputStream(inputFile);
	       long x=inputFile.length()%16;
	       int y=(int) inputFile.length()-(int)x;
	       byte[] inputBytes = new byte[y];
	       inputStream.read(inputBytes);
	       byte[] outputBytes = cipher.doFinal(inputBytes);
	       FileOutputStream outputStream = new FileOutputStream(outputFile);
	       outputStream.write(outputBytes);

	       inputStream.close();
	       outputStream.close();

	    } 
	 catch (Exception e)
	 	{
		 e.printStackTrace();
	 	}
     }
}