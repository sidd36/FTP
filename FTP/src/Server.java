import java.io.*;
import java.net.*;
import java.util.Scanner;

class ftpserver extends Thread
{
	DataOutputStream out;
	DataInputStream in;
	BufferedReader bufread;
	BufferedWriter bufwrite;
	Scanner s=new Scanner(System.in);
	
	ftpserver(Socket sc) 
	{
		Socket sock;
		{
			try
			{
				sock=sc;
				out=new DataOutputStream(sock.getOutputStream());
				in=new DataInputStream(sock.getInputStream());
				System.out.println("Server has started");
				start();
			}
			catch(Exception ex)
			{
				System.out.println("Error");
			}
		}
	}	
	void receive() throws Exception
	{
		String fname=in.readUTF();
		File file=new File(fname);
		if(file.exists())
		{
			out.writeUTF("File already exists");
			return;
		}
		else
		{
			out.writeUTF("Receivng file");
			bufwrite=new BufferedWriter(new FileWriter(file));
			String data;
			do
			{
				data=in.readUTF();
				bufwrite.write(data);
			}while(data!=null);	
			System.out.println("File successfully received");			
		}
	}
	void send() throws Exception
	{
		String name=in.readUTF();
		File f=new File(name);
		if(!f.exists())
		{
			out.writeUTF("Requested file does not exist");
			return;
		}
		else
		{
			out.writeUTF("Sending file");
			bufread=new BufferedReader(new FileReader(f));
			String data;
			do
			{
				data=bufread.readLine();
			}while(data!=null);
			out.writeUTF(data);
			System.out.println("File successfully sent");
		}
	}
	
	public void run() 
	{
		{
			try
			{
				String cmd;
				cmd=in.readUTF();
				if(cmd.compareTo("Send")==0)
					receive();
				if(cmd.compareTo("Receive")==0)
					send();
			}
			catch(Exception ex)
			{
			}
		}
	}
}
class Server 
{
	public static void main(String args[]) throws IOException
	{
		{
			try
			{
				ServerSocket sc=new ServerSocket(5030);
				ftpserver obj=new ftpserver(sc.accept());
			}
			catch (IOException ex)
			{
				System.out.println("Error");
			}
		}
	}
}
