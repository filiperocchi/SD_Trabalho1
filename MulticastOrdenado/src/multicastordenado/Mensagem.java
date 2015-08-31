/*
	UFSCar - Campus Sorocaba
	Sistemas Distribuídos - prof. Fábio
	
	Rafael Brandão Barbosa Fairbanks 552372
	Filipe Santos Rocchi 552194
 */
package multicastordenado;


public class Mensagem {
	public Integer tempo;
	public String conteudo;
	public boolean pronto;
	
	public Mensagem(Integer t, String c){
		tempo = t;
		conteudo = c;
		pronto = false;
	}
	
	public void imprimir(){
		System.out.println(conteudo);
	}
}
