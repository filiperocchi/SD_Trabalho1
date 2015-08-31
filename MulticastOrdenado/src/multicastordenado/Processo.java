/*
	UFSCar - Campus Sorocaba
	Sistemas Distribuídos - prof. Fábio
	
	Rafael Brandão Barbosa Fairbanks 552372
	Filipe Santos Rocchi 552194
 */
package multicastordenado;

import java.util.ArrayList;


public class Processo {
	public static ArrayList<Processo> processos;
	private ArrayList<Mensagem> filaMensagens;
	private int id;
	private Aplicacao aplicacao;
	
	public Processo(int i){
		processos.add(this);
		
		id = i;
		
		aplicacao = new Aplicacao(id);
	}
	
}
