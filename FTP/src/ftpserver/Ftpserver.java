package ftpserver;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import serverchat.*;

public class Ftpserver extends Thread
{
	protected DataOutputStream out;
	protected DataInputStream in;
	protected BufferedReader bufread;
	//BufferedWriter bufwrite;
	Scanner s=new Scanner(System.in);
	Socket sc;

	public Ftpserver()
	{}
	public Ftpserver(Socket sock) 
	{
		{
			try
			{
				sc=sock;
				out=new DataOutputStream(sc.getOutputStream());
				in=new DataInputStream(sc.getInputStream());
				bufread=new BufferedReader(new InputStreamReader(System.in));
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
		String name=in.readUTF();
		String msg=in.readUTF();
		if(msg.compareTo("Requested file does not exist")==0)
			System.out.println("File does not exist");
		else
		{
			System.out.println("Receiving file");
			File f=new File(name);
			if(f.exists())
			{
				System.out.println("File already exists");
				return;
			}
			else if(msg.compareTo("Sending file")==0)
			{
				FileOutputStream fo=new FileOutputStream(f);
				int data;
				do
				{
					data=in.read();
					fo.write(data);
				}while(data!=-1);
				System.out.println("File succesfully received");
				in.close();
				out.close();
				fo.close();
			}
		}
	}

	void send() throws Exception
	{
		System.out.println("Enter the name of the file to be sent");
		String name=bufread.readLine();
		out.writeUTF(name);
		File f=new File(name);
		if(!f.exists())
		{
			out.writeUTF("Requested file does not exist");
			return;
		}
		else
		{
			out.writeUTF("Sending file");
			System.out.println("Sending file");;
			FileInputStream fi=new FileInputStream(f);
			int data;
			do
			{
				data=fi.read();
				out.write(data);
			}while(data!=-1);
			System.out.println("File successfully sent");
			fi.close();
			in.close();
			out.close();
		}
	}
	
	

	public void run() 
	{
		while(true)
		{
			{
				try
				{
					String cmd;
					cmd=in.readUTF();
					if(cmd.compareTo("Send")==0)
					{	
						receive();
						continue;
					}
					else if(cmd.compareTo("Receive")==0)
					{	
						send();
						continue;
					}
					else if(cmd.compareTo("Exit")==0)
					{
						System.out.println("Server closed");
						System.exit(0);
					}
				}	
				catch(Exception ex)
				{
				}
			}
		}
	}
}