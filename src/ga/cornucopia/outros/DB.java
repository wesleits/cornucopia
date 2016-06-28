package ga.cornucopia.outros;
import ga.cornucopia.Config;

import java.sql.*;

public class DB 
{

	public static final String conector = "jdbc:mysql://" + Config.MySQL.servidor + ":" + Config.MySQL.porta + "/" + Config.MySQL.db;
	
	public Connection con = null;
	public int idMercado;
	public int idExtracao;
	
	public DB(String url, int idExtracao)
	{
		this();
		idMercado = obterIdMercado(url);	
		this.idExtracao = idExtracao;
	}
	
	public DB()
	{
		try 
		{ 
			
			Class.forName("com.mysql.jdbc.Driver"); 
		    con = DriverManager.getConnection(conector, Config.MySQL.usuario, Config.MySQL.senha);
		    
		}
		catch (Exception ex) 
		{
			System.out.println(ex);
		} 
	}
	
	public int gerarIdExtracao(String versaoCrawler)
	{
	    try 
	    {
	    	String sql = "insert into extracao (crawler) values (?)";
	    	PreparedStatement stmt = con.prepareStatement(sql);
	    	stmt.setString(1, versaoCrawler);
	    	stmt.executeUpdate();
	    	
	    	sql = "select id from extracao order by id desc limit 1";
	    	stmt = con.prepareStatement(sql);

	    	ResultSet resultado = stmt.executeQuery();
	    	resultado.next();
	    	
	    	return resultado.getInt("id");
		} 
	    catch (Exception e) 
	    {
			System.out.println(e);
		}	
		
	    return -1;
	}
	
	public int obterIdMercado(String url)
	{
	    try 
	    {
	    	String sql = "select id from mercado where url='" + url + "';";
	    	PreparedStatement stmt = con.prepareStatement(sql);
	    	
	    	ResultSet resultado = stmt.executeQuery();
	    	resultado.next();	
			
	    	return resultado.getInt("id");
	    
	    }
	    catch (Exception e) 
	    {
			System.out.println(e);
		}	
	    
	    return -1;
	}
	
	public void inserirDadoBruto(String titulo, String valor, String link)
	{
	    try 
	    {
	    	System.out.println("insert into bruto (titulo, valor, link, mercado_id, extracao_id) values ('" + titulo + "', " + valor + ", '" + link + "', " + idMercado +  ", " + idExtracao + ");");
	    	
	    	String sql = "insert into bruto (titulo, valor, link, mercado_id, extracao_id) values (?, ?, ?, ?, ?)";
	    	PreparedStatement stmt = con.prepareStatement(sql);
	    	stmt.setString(1, titulo);
	    	stmt.setString(2, valor);
	    	stmt.setString(3, link);
	    	stmt.setInt(4, idMercado);
	    	stmt.setInt(5, idExtracao);
	    	stmt.executeUpdate();	
		} 
	    catch (Exception e) 
	    {
			System.out.println(e);
		}
	}
	
	public void fechar()
	{
		try 
		{
			con.close();
		}
	    catch (Exception e) 
	    {
			System.out.println(e);
		}
	}
}
