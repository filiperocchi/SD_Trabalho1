/*
	UFSCar - Campus Sorocaba
	Sistemas Distribuídos - prof. Fábio
	
	Rafael Brandão Barbosa Fairbanks 552372
	Filipe Santos Rocchi 552194
 */
package multicastordenado;

import static java.lang.Math.max;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;


public class Processo extends Thread
{
	public static int contadorIdsMensagens;

	public static ArrayList<Processo> processos = new ArrayList<>();
	public int idProcesso;
	private Aplicacao aplicacao;

	private PriorityQueue<Mensagem> filaMensagens;

	private HashMap<Integer, ArrayList<Integer>> mapMsgIdToAckList; // id da mensagem, lista de ids de processos que faltam dar ack
	//(conjunto de acks faltando, e quando vazio está pronta?)
	
	private Queue<Mensagem> msgToDo;
	private Queue<Ack> acksToDo;

	private int relogioLogico;
	
	public Processo(int i)
	{
		processos.add(this);
		
		idProcesso = i;
		aplicacao = new Aplicacao(idProcesso);

		Comparator<Mensagem> comparador = new ComparadorTempoMsg();
		filaMensagens = new PriorityQueue<Mensagem>(10, comparador);

		mapMsgIdToAckList = new HashMap<Integer, ArrayList<Integer>>();
		
		msgToDo = new ConcurrentLinkedQueue<>();
		acksToDo = new ConcurrentLinkedQueue<>();

		relogioLogico = 0;
	}
	
	public void enviarMensagem(String conteudo)
	{
		relogioLogico++;

		Mensagem msg = criarMensagem(idProcesso, relogioLogico, conteudo);

		//System.out.println("PROCESSO "+idProcesso+": enviando msg "+msg.id);
		
		for(Processo p : processos)
			p.receberMensagem(msg);
	}
	public void receberMensagem(Mensagem m)
	{
		//synchronized(this){
			msgToDo.add(new Mensagem(m));
		//}
	}
	
	public void processarMensagem()
	{
		Mensagem msg;
		//synchronized(this){
			msg = msgToDo.poll();
		//}
		
		//System.out.println("PROCESSO "+idProcesso+": processando msg "+msg.id);
		
		relogioLogico = max(relogioLogico, msg.tempoRemetente);

		relogioLogico++;
		
		ArrayList<Integer> listaAcksFaltando = new ArrayList<>();
		for(Processo p : processos)
		{
			if(p.idProcesso != idProcesso) // exceto ele mesmo
				listaAcksFaltando.add(p.idProcesso);
		}

		filaMensagens.add(msg); // insere na lista ordenado pelo tempo
		mapMsgIdToAckList.put(msg.id, listaAcksFaltando);
		
		enviarAck(msg.id); // multicast ack
		
	}
	
	public void enviarAck(int idMensagem)
	{
		//System.out.println("PROCESSO "+idProcesso+": enviando ack "+idMensagem);
		
		relogioLogico++;

		// simula mandar o Ack para todos processos
		for(Processo p : processos)
		{
			if(p.idProcesso != idProcesso) // exceto ele mesmo
				p.receberAck(new Ack(idProcesso, idMensagem));
		}
	}
	
	public void receberAck(Ack a)
	{
		//synchronized(this){
			acksToDo.add(a);
		//}
	}
	
	public void processarAck()
	{
		//?relogioLogico++;
		Ack ack;
		
		//synchronized(this){
			ack = acksToDo.poll();	
		//}
		
		ArrayList<Integer> missingAcks = mapMsgIdToAckList.get(ack.idMensagem); // lista de acks faltando da msg que foi ack
		
		if(missingAcks != null)
			missingAcks.remove(new Integer(ack.idProcessoEnviandoAck));
	}

	@Override
	public void run()
	{
		System.out.println("Started process "+idProcesso);
		
		while(true)
		{
			while(!msgToDo.isEmpty())
				processarMensagem();
			
			while(!acksToDo.isEmpty())
				processarAck(); // multicast ack
			
			Random rand = new Random();
			
			if(rand.nextInt(50000) < 1)
			{
				enviarMensagem(UUID.randomUUID().toString());
				//System.out.println("PROCESSO "+idProcesso+": enviando msg");
			}
			
			if(filaMensagens.peek() != null)
			{
				if(mapMsgIdToAckList.get(filaMensagens.peek().id).isEmpty())
				{
					relogioLogico++;
					if(idProcesso == 0)
					{
						System.out.println("PROCESSO "+idProcesso+": (relogio "+relogioLogico+") Executando msg idRem: "+filaMensagens.peek().idRemetente+" id: "+filaMensagens.peek().id+" t: "+filaMensagens.peek().tempoRemetente);
						//aplicacao.send(filaMensagens.peek());
					
						//filaMensagens.peek().imprimir(); // executa cabeça da fila, "mensagem é executada na aplicação"
					}
					
					mapMsgIdToAckList.remove(filaMensagens.peek().id);
					filaMensagens.poll();
				}
			}
		}
	}

	private static synchronized Mensagem criarMensagem(int idCriador, int relogioLogico, String conteudo)
	{
		contadorIdsMensagens++;
		Mensagem m = new Mensagem(idCriador, contadorIdsMensagens, relogioLogico, conteudo); // cria mensagem com relogio e conteudo
		
		return m;
	}
}