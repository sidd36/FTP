import java.net.*;
import java.util.Scanner;
import java.io.*;

class ftpclient
{
	DataOutputStream out;
	DataInputStream in;
	BufferedReader bufread;
	BufferedWriter bufwrite;
	Scanner s=new Scanner(System.in); 
	
	ftpclient(Socket socket) throws Exception
	{
		Socket sc;
		{
			try
			{
				sc=socket;
				out=new DataOutputStream(sc.getOutputStream());
				in=new DataInputStream(sc.getInputStream());
			}
			catch(Exception ex)
			{
				System.out.println("Error");
			}
		}
	}
	
	void send() throws Exception
	{
		System.out.println("Enter the name of the file to be sent");
		bufread=new BufferedReader(new InputStreamReader(System.in));
		String fname=bufread.readLine();
		File file=new File(fname);
		if(!file.exists())
		{
			System.out.println("File does not exist");
			out.writeUTF("Requested file doesn't exist");
		}
		out.writeUTF(fname);
		String msg=in.readUTF();
		if(msg.compareTo("File already exists")==0)
			System.out.println("File already exists");
		else
		{
			System.out.println("Sending file");
			bufread=new BufferedReader(new FileReader(file));
			String data;
			do
			{
				data=bufread.readLine();
			}while(data!=null);
			out.writeUTF(data);
			System.out.println("File successfully sent");
		}
	}

	void receive() throws Exception
	{
		System.out.println("Enter the name of the file required");
		bufread=new BufferedReader(new InputStreamReader(System.in));
		String name=bufread.readLine();
		out.writeUTF(name);
		String msg=in.readUTF();
		if(msg.compareTo("Requested file does not exist")==0)
			System.out.println("Requested file does not exist");
		else
		{
			System.out.println("Receiving file");
			File f=new File(name);
			if(f.exists())
				System.out.println("Requested file already exists");
			else
			{
				bufwrite=new BufferedWriter(new FileWriter(f));
				String data;
				do
				{
					data=in.readUTF();
					bufwrite.write(data);
				}while(data!=null);	
				System.out.println("File successfully received");
			}
		}
	}
	
	void menu() throws Exception
	{
		System.out.println("1:Send");
		System.out.println("2:Receive");
		System.out.println("3:Exit");
		int opt;
		do
		{
			System.out.println("Enter the service required");
			opt=s.nextInt();
			switch(opt)
			{
				case 1:out.writeUTF("Send");
					   send();
					break;
				case 2:out.writeUTF("Receive");
					   receive();
					break;
			}
		}while(opt!=3);
	}
}

class Client
{
	public static void main(String args[]) throws Exception
	{
		Socket sock=new Socket("localhost",5030);
		ftpclient obj=new ftpclient(sock);
		obj.menu();	
	}
}