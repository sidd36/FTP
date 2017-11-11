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
			System.out.println("Requested file does not exist");
		else
		{
			System.out.println("Receiving file");
			File f=new File(name);
			if(f.exists())
				System.out.println("Requested file already exists");
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

	void chat() throws IOException
	{
		Boolean con;
		out.writeBoolean(false);
		while(true)
		{
			con=in.readBoolean();
			if(con)
			{
				String msgi=in.readUTF();
				System.out.println("Client: "+msgi);
				out.writeBoolean(false);
			}
			else
			{
				out.writeBoolean(true);
				System.out.print("Server: ");
				String msgo=bufread.readLine();
				out.writeUTF(msgo);
			}
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
					else if(cmd.compareTo("Chat")==0)
					{
						chat();
						continue;
					}
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
		System.out.println("Server has started");
		ServerSocket sock=new ServerSocket(6003);
		while(true)
		{	
			ftpserver obj=new ftpserver(sock.accept());
		}
	}
}