/*
	UFSCar - Campus Sorocaba
	Sistemas Distribuídos - prof. Fábio
	
	Rafael Brandão Barbosa Fairbanks 552372
	Filipe Santos Rocchi 552194
 */
package multicastordenado;


public class Ack {
	public Integer tempo;
	public Integer tempoMensagem;
	
	public Ack(Integer t, Integer tm){
		tempo = t;
		tempoMensagem = tm;
	}
}
