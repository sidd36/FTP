import java.net.Socket;
import ftpclient.Ftpclient;

class Client
{
	public static void main(String args[]) throws Exception
	{
		while(true)
		{	
			Socket sock=new Socket("192.168.10.15",6003);
			Ftpclient obj=new Ftpclient(sock);
			obj.menu();	
		}
	}
}