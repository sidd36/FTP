package ftpserver;

import java.io.*;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Serverencdec{

   public static void encdec(int cipherMode,String key,File inputFile,File outputFile){
	 try {
	       SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
	       byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		   IvParameterSpec ivspec = new IvParameterSpec(iv);
	       Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	       cipher.init(cipherMode, secretKey,ivspec);
	       FileInputStream inputStream = new FileInputStream(inputFile);
	       byte[] inputBytes = new byte[(int) inputFile.length()];
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