package ga.cornucopia;

import java.net.*;

public class Config 
{
	static public class MySQL
	{
		public static final String servidor = "localhost";
		public static final int porta = 3306;
		public static final String db = "c1";
		public static final String usuario = "root";
		public static final String senha = "";
	}
	
	
	//public static final Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 3128));
	public static final Proxy proxy = null;
	public static final String versao = "v0.2.1";
}
