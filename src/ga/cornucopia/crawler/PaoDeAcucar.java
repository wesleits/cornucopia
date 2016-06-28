package ga.cornucopia.crawler;

import static ga.cornucopia.Utilidades.ler;
import static ga.cornucopia.Utilidades.requisitar;
import ga.cornucopia.Utilidades;
import ga.cornucopia.outros.DB;
import ga.cornucopia.outros.Paralelismo;

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

public class PaoDeAcucar 
{
	public static final String url = "http://www.paodeacucar.com.br";
	public int idExtracao;
	
	public PaoDeAcucar (int idExtracao)
	{
		this.idExtracao = idExtracao;	
	}
	
	public void extrairDados()
	{
		final DB db = new DB(url, idExtracao);
		
		String html = ler(requisitar("http://www.paodeacucar.com.br/"));
		String bloco = Utilidades.obterPrimeiraOcorrencia(html, "(?s)<li class=\"nhgpa_noborder\">(.*?)</li>");
		List<String> categorias = Utilidades.obterTodasAsOcorrencias(bloco, "(?s)<a href=\"(.*?)\"");
		
		for (final String categoria : categorias)
		{
			new Paralelismo()
			{
				public void procedimento() throws InterruptedException
				{
				    int i = 0;
					while(true)
					{
						String html = StringEscapeUtils.unescapeHtml4(ler(requisitar(categoria + "?&p=" + i++ + "&ftr=")));
						List<String> produtos = Utilidades.obterTodasAsOcorrencias(html, "(?s)<!-- INI comp\\(generic,33\\) -->(.*?)<!-- END comp\\(generic,33\\) -->");
						if (produtos.size() > 0)
						{
							for (String produto : produtos)
							{
								String titulo = Utilidades.obterPrimeiraOcorrencia(produto, "(?s)class=\"link\" title=\"(.*?)\"");
								String valor = Utilidades.obterPrimeiraOcorrencia(produto, "(?s)<span class=\"fromTo\">Por:.*?<span class=\"value\">(.*?)</span>");
								String link = Utilidades.obterPrimeiraOcorrencia(produto, "(?s)<h3 class=\"showcase-item__name\">.*?<a href=\"(.*?)\"");
								
								if (titulo != null && valor != null && link != null)
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
			}.executar();
		}
	}
}
