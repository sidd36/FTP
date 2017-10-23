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
	Socket sc;
	
	ftpserver(Socket sock) 
	{
		{
			try
			{
				sc=sock;
				out=new DataOutputStream(sc.getOutputStream());
				in=new DataInputStream(sc.getInputStream());
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
			out.writeUTF("Receiving file");
			System.out.println("Receiving file");
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
			System.out.println("Sending file");;
			FileInputStream fi=new FileInputStream(f);
			int data;
			do
			{
				data=fi.read();
				out.write(data);
				System.out.println("File successfully sent");
			}while(data!=-1);
			fi.close();
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
						System.exit(0);
				}
				catch(Exception ex)
				{
				}
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
				ServerSocket sock=new ServerSocket(6003);
				ftpserver obj=new ftpserver(sock.accept());
				System.out.println("Server has started");
			}
			catch (IOException ex)
			{
				System.out.println("Error");
			}
		}
	}
}
