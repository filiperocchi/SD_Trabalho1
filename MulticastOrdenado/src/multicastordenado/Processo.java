/*
	UFSCar - Campus Sorocaba
	Sistemas Distribuídos - prof. Fábio
	
	Rafael Brandão Barbosa Fairbanks 552372
	Filipe Santos Rocchi 552194
 */
package multicastordenado;

import java.util.ArrayList;
import java.util.HashMap;


public class Processo {
	public static ArrayList<Processo> processos;
	private HashMap<Integer, Mensagem> filaMensagens;
	private HashMap<Integer, Integer> acksPorMensagem;
	public int id;
	private Aplicacao aplicacao;
	
	public Processo(int i){
		processos.add(this);
		
		id = i;
		
		aplicacao = new Aplicacao(id);
	}
	
	public void novaMensagem(Integer t, Mensagem m){
		// insere ordenado pelo tempo
		filaMensagens.put(t, m);
		acksPorMensagem.put(t, 0);
		
		mandarAck(m);
		
	}
	
	public void mandarAck(Mensagem m){
		// cria o ack baseado na Mensagem que vai mandar
		Ack a = new Ack(m.tempo+1, m.tempo);
		
		// simula mandar o Ack para todos processos
		for(Processo p: processos){
			if(p.id!=id){ // exceto ele mesmo
				p.receberAck(a);
			}
		}
	}
	
	public void receberAck(Ack a){
		
		if(a.tempo < a.tempoMensagem){
			System.out.print("Ack de mensagem com tempo menor que o da mensagem.\n");
		}
		// incrementa o número de acks da mensagem que chegou o Ack
		acksPorMensagem.replace(a.tempoMensagem, acksPorMensagem.get(a.tempoMensagem)+1);
		
		// se o número de acks for o número de processos, está pronto pra enviar
		if(acksPorMensagem.get(a.tempoMensagem) == processos.size()-1){
			filaMensagens.get(a.tempoMensagem).pronto = true;
		}
	}
	
}
