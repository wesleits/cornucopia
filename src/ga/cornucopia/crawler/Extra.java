package ga.cornucopia.crawler;

import ga.cornucopia.outros.DB;
import java.util.List;
import ga.cornucopia.Utilidades;
import static ga.cornucopia.Utilidades.*;

public class Extra 
{
	public static final String url = "http://www.deliveryextra.com.br";
	public int idExtracao;
	
	public Extra(int idExtracao)
	{
		this.idExtracao = idExtracao;	
	}
	
	public void extrairDados()
	{
		final DB db = new DB(url, idExtracao);
		
		String html = ler(requisitar("http://www.deliveryextra.com.br/secoes/"));
		String bloco = Utilidades.obterPrimeiraOcorrencia(html, "(?s)<div class='menu-dept-full'>(.*?)</div>");
		List<String> categorias = Utilidades.obterTodasAsOcorrencias(bloco, "(?s)<a href='(.*?)'>");
		
		for (final String categoria : categorias)
		{
			html = ler(requisitar(categoria));
			bloco = Utilidades.obterPrimeiraOcorrencia(html, "(?s)<div class='menu-dept-full'>.*?<ul class=''>(.*?)</ul>");
			List<String> subCategorias = Utilidades.obterTodasAsOcorrencias(bloco, "(?s)<a href='(.*?)'>");
			
			for (final String subCategoria : subCategorias)
			{
				int i = 0;
				while(true)
				{
					html = ler(requisitar(subCategoria + "?sort=&rows=12&q=&offset=" + i++ * 12));
					bloco = Utilidades.obterPrimeiraOcorrencia(html, "(?s)<tbody>(.*?)</tbody>");
					List<String> produtos = Utilidades.obterTodasAsOcorrencias(bloco, "(?s)<tr(.*?)</tr>");
					
					if (produtos.size() > 0)
					{
						for (String produto : produtos)
						{
							String titulo = Utilidades.obterPrimeiraOcorrencia(produto, "(?s)class=\"prdNome\">.*?<span>(.*?)</span>");
							String valor = Utilidades.obterPrimeiraOcorrencia(produto, "(?s)<strong>(.*?)</strong>");
							String link = Utilidades.obterPrimeiraOcorrencia(produto, "(?s)<a href=\"(.*?)\"");
							
							if (valor == null)
								valor = Utilidades.obterPrimeiraOcorrencia(produto, "(?s)<p class=\"progressiveDiscount-baseValue\">(.*?)</p>");
							
							if (titulo != null && valor != null)
							{
								valor = valor.replaceAll("R\\$|R|\\$| ", "").replaceAll(",", ".");
								db.inserirDadoBruto(titulo, valor, link);
							}	
						}
					}
					else
						break;
				}
			}			
		}	
	}
}
