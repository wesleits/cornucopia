package ga.cornucopia;

import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.*;
import java.util.*;
import java.util.regex.*;


import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;


public class Utilidades 
{
	static final HttpTransport HTTP_TRANSPORTE = new NetHttpTransport();
	static final HttpTransport HTTP_TRANSPORTE_PROXY = new NetHttpTransport.Builder().setProxy(Config.proxy).build();;
	
    public static String ler(InputStream is, String codificacao) 
    {
        Scanner s = new Scanner(is, codificacao);
        s.useDelimiter("\\A");
        String resultado = s.hasNext() ? s.next() : "";
        s.close();
        return resultado;
    }
	
    public static String ler(InputStream is) 
    {
    	return ler(is, Charset.defaultCharset().toString());
    }
    
    static public String ler(HttpResponse res)
    {
    	try
    	{
    		String resultado = ler(res.getContent());
    		res.disconnect();
    		return resultado;
    	}
    	catch(Exception e) {}
    	
    	return null;
    }
    
    static public String ler(HttpRequest req)
    {
    	try
    	{
    		HttpResponse res = req.execute();
    		String resultado = ler(res.getContent());
    		res.disconnect();
    		return resultado;
    	}
    	catch(Exception e) {}
    	
    	return null;
    }
    
    static public HttpResponse resposta(HttpRequest req)
    {
    	try
    	{
    		return req.execute();
    	}
    	catch(Exception e) {}
    	
    	return null;
    }
    
	public static String obterPrimeiraOcorrencia(String texto, String regex) 
	{
		try
		{
	        Matcher m = Pattern.compile(regex).matcher(texto);
	        m.find();
	        return m.group(1);
		}
		catch (Exception e){};
		
		return null;
    }
	
	public static String remover(String regex, String content) 
	{
	    Pattern p = Pattern.compile(regex);
	    return p.matcher(content).replaceAll("");
	}
    
	public static List<String> obterTodasAsOcorrencias(String texto, String regex) 
	{
		List<String> correspondente = new ArrayList<String>();
		
		try
		{
	        Matcher m = Pattern.compile(regex).matcher(texto);
	        while(m.find())
	        	correspondente.add(m.group(1));
		}
		catch (Exception e){};
        
        return correspondente;
    }
   
    public static String MD5(String texto) 
    {
    	MessageDigest md = null;
		try 
		{
			md = MessageDigest.getInstance("MD5");
		} 
		catch (NoSuchAlgorithmException e){}
		
	    byte[] messageDigest = md.digest(texto.getBytes());

	    BigInteger number = new BigInteger(1, messageDigest);

	    return String.format("%032x", number);
    }
    
    static public HttpRequest requisitar(String url)
    {
    	try
    	{
	    	return ((Config.proxy == null) ? HTTP_TRANSPORTE : HTTP_TRANSPORTE_PROXY).createRequestFactory().buildGetRequest(
	        		new GenericUrl(url));
    	}
    	catch(Exception e) {}
    	
    	return null;
    }
}
