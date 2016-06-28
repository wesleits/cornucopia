package ga.cornucopia.crawler;

import static ga.cornucopia.Utilidades.ler;
import static ga.cornucopia.Utilidades.requisitar;
import ga.cornucopia.Utilidades;
import ga.cornucopia.outros.DB;
import ga.cornucopia.outros.Paralelismo;

import java.util.List;

public class ZonaSul 
{
	public static final String url = "http://www.zonasulatende.com.br";
	public int idExtracao;
	
	public ZonaSul(int idExtracao)
	{
		this.idExtracao = idExtracao;	
	}
	
	public void extrairDados()
	{
		final DB db = new DB(url, idExtracao);
		
		String html = ler(requisitar("http://www.zonasulatende.com.br/"));
		String bloco = Utilidades.obterPrimeiraOcorrencia(html, "(?s)<div id=\"ctl00_pnlNav\" class=\"nav\">(.*?)</div>");
		
		List<String> categorias = Utilidades.obterTodasAsOcorrencias(bloco, "(?s)<a.*?href=\"(.*?)\">");
		
		for (final String categoria : categorias)
		{
			new Paralelismo()
			{
				public void procedimento() throws InterruptedException
				{
					String html = ler(requisitar("http://www.zonasulatende.com.br" + categoria));
					String bloco = Utilidades.obterPrimeiraOcorrencia(html, "(?s)<div class=\"destaque_subsecao_cont\">.*?<ul.*?>(.*?)</ul>.*?</div>");
					List<String> subCategorias = Utilidades.obterTodasAsOcorrencias(bloco, "(?s)<div class=\"prod_info\">.*?<a.*?href=\"(.*?)\"");
					
					for (final String subCategoria : subCategorias)
					{
						new Paralelismo()
						{
							public void procedimento() throws InterruptedException
							{
								int i = 1;
								while(true)
								{
									String html = ler(requisitar("http://www.zonasulatende.com.br" + subCategoria + "?Pagina=" + i++));
									String bloco = Utilidades.obterPrimeiraOcorrencia(html, "(?s)<div id=\"visualizacao\">(.*?)<div class=\"paginacao\">");
									
									List<String> produtos = Utilidades.obterTodasAsOcorrencias(bloco, "(?s)<li.*?>(.*?)</li>");
									
									if (produtos.size() > 0)
									{
										for (String produto : produtos)
										{
											String titulo = Utilidades.obterPrimeiraOcorrencia(produto, "(?s)<div class=\"prod_image\">.*?<a.*?title=\"(.*?)\"");
											String valor = Utilidades.obterPrimeiraOcorrencia(produto, "(?s)<p class=\"preco\">(.*?)</p>");
											String link = Utilidades.obterPrimeiraOcorrencia(produto, "(?s)<div class=\"prod_image\">.*?<a.*?href=\"(.*?)\"");
										
											if (titulo != null && valor != null && link != null)
											{
												valor = valor.replaceAll("R\\$|R|\\$| |\t|\r|\n|\r\n", "").replaceAll(",", ".");
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
			}.executar();
		}
	}
}
