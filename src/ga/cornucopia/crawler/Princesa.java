package ga.cornucopia.crawler;


import java.util.List;

import ga.cornucopia.Utilidades;
import ga.cornucopia.outros.*;
import static java.util.Arrays.asList;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;

import static ga.cornucopia.Utilidades.*;

public class Princesa 
{
	public static final String url = "http://www.princesasupermercados.com.br";
	public int idExtracao;
	
	public Princesa (int idExtracao)
	{
		this.idExtracao = idExtracao;	
	}
	
	public String obterCookieDeSessao()
	{
		try 
		{
	        HttpRequest req = requisitar("http://www.princesasupermercados.com.br/regiao/visitante");
	        
	        req.setFollowRedirects(false);
	        req.setThrowExceptionOnExecuteError(false);
	        HttpResponse res = resposta(req);
	        res.disconnect();
	        
	        return res.getHeaders().getFirstHeaderStringValue("Set-Cookie").split(";")[0];
		} 
		catch (Exception e) 
		{
			System.out.println(e);
		}

		return null;
	}
		
	static public String obterPagina(String URL, String cookie)
	{
		try 
		{
	        HttpRequest req = requisitar(URL);
	        
	        req.setHeaders(new HttpHeaders().set("Cookie", asList(cookie)));
	        req.setFollowRedirects(false);
	        req.setThrowExceptionOnExecuteError(false);

	    	return ler(req);
		} 
		catch (Exception e) 
		{
			System.out.println(e);
		}
		
		return null;
	}
	
	
	public void extrairDados()
	{
		final DB db = new DB(url, idExtracao);
		final String cookies = obterCookieDeSessao();
		
		for (int i = 0; i < 400; i++)
		{
			String html = obterPagina("http://www.princesasupermercados.com.br/produtos/pagina?paginaAtual=0&numPorPagina=2147483647&pf.ordenacao=RELEVANCE&pf.secao=" + i, cookies);
			List<String> produtos = Utilidades.obterTodasAsOcorrencias(html, "(?s)<article class=\"prod-box\">(.*?)</article>");

			for (String produto : produtos)
			{
				String titulo = Utilidades.obterPrimeiraOcorrencia(produto, "(?s)<h3 class=\"prod-box-title\">.*?<a.*?>(.*?)</a>");
				String valor = Utilidades.obterPrimeiraOcorrencia(produto, "(?s)<span>por</span>(.*?)</p>");
				String link = Utilidades.obterPrimeiraOcorrencia(produto, "(?s)<figure class=\"prod-box-image\">.*?href=\"(.*?)\">");
			
				if (titulo != null && valor != null && link != null)
				{
					titulo = titulo.trim();
					valor = valor.replaceAll("R\\$|R|\\$| |\t|\r|\n|\r\n", "").replaceAll(",", ".");
					link = "http://www.princesasupermercados.com.br" + link;
					db.inserirDadoBruto(titulo, valor, link);
				}
			};
		}

		db.fechar();
	}
}
