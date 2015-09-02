/*
	UFSCar - Campus Sorocaba
	Sistemas Distribuídos - prof. Fábio
	
	Rafael Brandão Barbosa Fairbanks 552372
	Filipe Santos Rocchi 552194
 */
package multicastordenado;

	
public class Ack {
	public int idProcessoEnviandoAck;
	public int idMensagem;
	
	public Ack(Integer p, Integer m)
	{
		idProcessoEnviandoAck = p;
		idMensagem = m;
	}
}
