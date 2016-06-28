/*
	Autor: Weslei Teixeira da Silveira
*/

package ga.cornucopia;

import ga.cornucopia.crawler.*;
import ga.cornucopia.outros.*;



public class Principal 
{
	public static void main(String[] args) 
	{
		final int id = new DB().gerarIdExtracao(Config.versao);

		// /*
		new Paralelismo()
		{
			public void procedimento() throws InterruptedException
			{
				new PaoDeAcucar(id).extrairDados();
			}
		}.executar();
		// */
		
		// /*
		new Paralelismo()
		{
			public void procedimento() throws InterruptedException
			{
				new Extra(id).extrairDados();
			}
		}.executar();
		// */
		
		// /*
		new Paralelismo()
		{
			public void procedimento() throws InterruptedException
			{
				new Princesa(id).extrairDados();
			}
		}.executar();
		// */
		
		// /*
		new Paralelismo()
		{
			public void procedimento() throws InterruptedException
			{
				new Superpix(id).extrairDados();
			}
		}.executar();
		// */
		
		// /*
		new Paralelismo()
		{
			public void procedimento() throws InterruptedException
			{
				new ZonaSul(id).extrairDados();
			}
		}.executar();
		// */
	}
}