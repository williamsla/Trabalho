import java.io.*;  
import java.util.*;

public class FIFO {

	private static File m_file;
	private static int numberOfProcesses = 0;
	private static int elapsedTime = 0;
	private static List<Task> tarefasNovas;
	private static List<Task> tarefasProntas;
	private static List<Task> tarefasExecutando;
	private static List<Task> tarefasTerminadas;

	public static void scheduler(int time){
		System.out.printf("\n_________segundo: %d_____________________ \n\n", time);
		
		List<Task> taskToRemove = new ArrayList<>();

		if(!tarefasNovas.isEmpty()){
			for(int i=0; i<tarefasNovas.size(); i++){
				System.out.println("Processo a ser analisado: "+tarefasNovas.get(i).id);
				if(tarefasNovas.get(i).date == time){
					System.out.println(tarefasNovas.get(i).id + " ficou pronto");
					tarefasNovas.get(i).status = "pronta";
					tarefasProntas.add(tarefasNovas.get(i));
					taskToRemove.add(tarefasNovas.get(i));
				}
			}
		}
		if(!taskToRemove.isEmpty()){
			for(int i=0; i<taskToRemove.size(); i++){
				tarefasNovas.remove(taskToRemove.get(i));
			}
		}
		
		if(tarefasExecutando.isEmpty() != true){
			for(int i=0; i<tarefasExecutando.size(); i++){
				if(tarefasExecutando.get(i).duration == tarefasExecutando.get(i).executingTime){
					System.out.println(tarefasExecutando.get(i).id + " terminou");

					tarefasExecutando.get(i).status = "terminada";
					tarefasTerminadas.add(tarefasExecutando.get(i));
					tarefasExecutando.remove(i);
				} else {
					tarefasExecutando.get(i).executingTime += 1;
					System.out.println(tarefasExecutando.get(i).id + " ainda executando");
				}
			}
		} else if(!tarefasProntas.isEmpty()){
			int highPriority = 0;
			int indexTask = 0;
			for(int i=0; i<tarefasProntas.size(); i++){
				//Procura saber quem tem maior prioridade pra usar o processador.
				//A de maior prioridade sai da lista de prontas para a de executando.
				if(tarefasProntas.get(i).priority > highPriority){
					highPriority = tarefasProntas.get(i).priority;
					indexTask = i;
				}
			}
			System.out.println(tarefasProntas.get(indexTask).id + " começou a executar");
			tarefasProntas.get(indexTask).status = "executando";
			tarefasProntas.get(indexTask).executingTime += 1;
			tarefasExecutando.add(tarefasProntas.get(indexTask));
			tarefasProntas.remove(indexTask);
		}
		
//		if(tarefasExecutando.isEmpty()){
//			System.out.println(Integer.toString(time)+"- "+Integer.toString(time+1)+"   "+"--"+"  --"+"  --"+"  --");
//		}

	}

	public static void main(String[] args) {
		m_file = new File(args[0]);
		tarefasNovas = new ArrayList<>();
		tarefasExecutando = new ArrayList<>();
		tarefasProntas = new ArrayList<>();
		tarefasTerminadas = new ArrayList<>();

		try{  
			Scanner in = new Scanner(m_file);

			while (in.hasNextLine()) {
				numberOfProcesses++;
				in.nextLine();
			}
			in = new Scanner(m_file);

			try{
				for(int linha=0 ; linha < numberOfProcesses ; linha++){
					Task tarefa = new Task();
					tarefa.date = in.nextInt();
					tarefa.duration = in.nextInt();
					tarefa.priority = in.nextInt();
					tarefa.status = "teste";
					tarefa.id = linha;

					tarefasNovas.add(tarefa);
				}
				System.out.printf("tamanho da lista: %d \n\n", tarefasNovas.size());
				in.close();  
			}  
			catch(NoSuchElementException nex){
				System.err.println("Erro na criação da lista com os processos.");  
			}    

		}  
		catch(IOException ex){
			ex.printStackTrace();  
			System.out.println("Erro ao ler o arquivo.");
		}

		System.out.print("tempo   ");
		for (int i=0; i<numberOfProcesses; i++){
			System.out.printf("P%d  ", i);
		}
		System.out.println("\n");

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				scheduler(elapsedTime);
				elapsedTime++;
			}
		}, 0, 3000);

	}

}

class Task {
	public int id; 
	public int date;//em que tempo sera criada
	public int duration; //quanto tempo precisa no processador
	public int executingTime = 0; //tempo de processamento ja decorrido
	public int priority;
	public String status;
};
