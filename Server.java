import java.io.IOException;
import java.net.ServerSocket;
import ftpserver.Ftpserver;

class Server 
{
	public static void main(String args[]) throws IOException
	{
		System.out.println("Server has started");
		ServerSocket sock=new ServerSocket(6003);
		while(true)
		{	
			Ftpserver obj=new Ftpserver(sock.accept());
		}
	}
}