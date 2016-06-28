package ga.cornucopia.outros;



public class Paralelismo
{
	private Thread recipiente = null;
	private boolean continuar = true;
	public int indice = 0;
	public String texto = null;
	
	public Paralelismo()
	{
	} 

	public Paralelismo(int indice)
	{
		this.indice = indice;
	} 
	
	public Paralelismo(String texto)
	{
		this.texto = texto;
	} 
	
	public void executar()
	{ 
		if (!executando())
		{
			recipiente = new Thread()
			{
				public void run()
				{
					continuar = true;

					try 
					{
						procedimento();
					} 
					catch (InterruptedException e){}
				}     
			};   
			recipiente.start();
		}
	}  
 
	 
	public void parar() 
	{
		if (executando())
		{ 
			continuar = false;
			recipiente.interrupt();
		} 	 
	}

	protected void autoParar() throws InterruptedException
	{
		continuar = false;
		throw new InterruptedException();
	} 
	
	public void aguardar() throws InterruptedException
	{
		if (executando())
			recipiente.join();	
	} 
	
	public boolean executando()
	{
		return (recipiente != null && recipiente.isAlive());
	}
	
	public boolean continuar() throws InterruptedException
	{
		if (!continuar) throw new InterruptedException();
		return true;   
	}
	
	public void pausar()
	{
		
	}
	
	public void resumir()
	{
		
	}
	
	// Reescrever
	public void procedimento() throws InterruptedException {};
}
