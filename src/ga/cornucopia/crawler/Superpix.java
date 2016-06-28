package ga.cornucopia.crawler;

import static ga.cornucopia.Utilidades.ler;
import static ga.cornucopia.Utilidades.requisitar;
import ga.cornucopia.Utilidades;
import ga.cornucopia.outros.DB;

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import com.google.api.client.http.HttpRequest;

public class Superpix 
{
	public static final String url = "http://www.superprix.com.br";
	public int idExtracao;
	
	public Superpix (int idExtracao)
	{
		this.idExtracao = idExtracao;	
	}
	
	public void extrairDados()
	{
		DB db = new DB(url, idExtracao);
	
		String html = ler(requisitar("http://www.superprix.com.br/catalog/MenuCategoryNavigationHome"));
		List<String> categorias = Utilidades.obterTodasAsOcorrencias(html, "(?s)\"Id\":(.*?),");
		
		for (String categoria : categorias)
		{	
			if (!categoria.endsWith("000"))
			{
				int i = 1;
				while(true)
				{
					HttpRequest req = Utilidades.requisitar("http://www.superprix.com.br/catalog/CategoryOverviewJson?categoryId=" + categoria + "&orderBy=1&pageNumber=" + i++);
			        
			        req.setRequestMethod("POST");
	
					html = Utilidades.ler(req);
					
					String bloco = Utilidades.obterPrimeiraOcorrencia(html, "(?s)\"ProductsOverviews\":\\[(.*?)\\]");
					if (bloco != null)
					{
						List<String> produtos = Utilidades.obterTodasAsOcorrencias(bloco, "(?s)\\{(.*?)\\}");
						for (String produto : produtos)
						{
							String titulo = Utilidades.obterPrimeiraOcorrencia(produto, "(?s)\"Name\":\"(.*?)\",");
							String valor = Utilidades.obterPrimeiraOcorrencia(produto, "(?s)\"Price\":(.*?),");
							String link = Utilidades.obterPrimeiraOcorrencia(produto, "(?s)\"SeName\":\"(.*?)\",");
							
							if (titulo != null && valor != null && link != null)
							{
								titulo = StringEscapeUtils.unescapeJava(titulo);
								link = "http://www.superprix.com.br/" + link;
								db.inserirDadoBruto(titulo, valor, link);
							}	
						}
					}
					else
						break;
				}  
			}
		};

		db.fechar();
	}
}
