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

	public static void scheduler(int time) {
		// System.out.printf("\n_________segundo: %d_____________________ \n\n",
		// time);

		List<Task> taskToRemove = new ArrayList<>();

		if (!tarefasNovas.isEmpty()) {
			for (int i = 0; i < tarefasNovas.size(); i++) {
				// System.out.println("Processo a ser analisado:
				// "+tarefasNovas.get(i).getID());
				if (tarefasNovas.get(i).getDate() == time) {
					// System.out.println(tarefasNovas.get(i).getID() + " ficou
					// pronto");
					tarefasNovas.get(i).setStatus("ready");
					tarefasProntas.add(tarefasNovas.get(i));
					taskToRemove.add(tarefasNovas.get(i));
				}
			}
		}
		if (!taskToRemove.isEmpty()) {
			for (int i = 0; i < taskToRemove.size(); i++) {
				tarefasNovas.remove(taskToRemove.get(i));
			}
		}

		if (tarefasExecutando.isEmpty() != true) {
			for (int i = 0; i < tarefasExecutando.size(); i++) {
				if (tarefasExecutando.get(i).getTotalTime() == tarefasExecutando.get(i).getElapsedTime()) {
					// System.out.println(tarefasExecutando.get(i).getID() + "
					// terminou");

					tarefasExecutando.get(i).setStatus("finished");
					tarefasTerminadas.add(tarefasExecutando.get(i));
					tarefasExecutando.remove(i);

				} else {
					tarefasExecutando.get(i).addElapsedTime(1);
					// System.out.println(tarefasExecutando.get(i).getID() + "
					// ainda executando");
				}
			}
		}
		if (!tarefasProntas.isEmpty() && tarefasExecutando.isEmpty()) {
			int highPriority = 0;
			int indexTask = 0;
			for (int i = 0; i < tarefasProntas.size(); i++) {
				// Procura saber quem tem maior prioridade pra usar o
				// processador.
				// A de maior prioridade sai da lista de prontas para a de
				// executando.
				if (tarefasProntas.get(i).getPriority() > highPriority) {
					highPriority = tarefasProntas.get(i).getPriority();
					indexTask = i;
				}
			}
			tarefasProntas.get(indexTask).setStatus("executando");
			tarefasProntas.get(indexTask).addElapsedTime(1);
			tarefasExecutando.add(tarefasProntas.get(indexTask));
			tarefasProntas.remove(indexTask);
		}

		System.out.print(Integer.toString(time) + "- " + Integer.toString(time + 1) + "    ");

//		for (int i = 0; i < numberOfProcesses; i++) {
//			if(!tarefasExecutando.isEmpty() && Integer.parseInt(tarefasExecutando.get(0).getID()) == i){
//				System.out.print("##  ");
//			} else if (!tarefasProntas.isEmpty()){
//				for (int j = 0; j < tarefasProntas.size(); j++) {
//					if (Integer.parseInt(tarefasProntas.get(j).getID()) == i) {
//						System.out.print("--  ");
//						j = tarefasProntas.size();
//					}
//				}
//			} else {
//				System.out.print("    ");
//			}
//		}

		if (tarefasExecutando.isEmpty() && !tarefasProntas.isEmpty()) {
			for (int i = 0; i < tarefasProntas.size(); i++) {
				System.out.print("--  ");
			}
		} else {
			for (int i = 0; i < numberOfProcesses; i++) {
				if (Integer.parseInt(tarefasExecutando.get(0).getID()) == i) {
					System.out.print("##  ");
				} else {
					System.out.print("--  ");
				}
			}
		}
		System.out.print("\n");

	}

	public static void main(String[] args) {
		m_file = new File(args[0]);
		tarefasNovas = new ArrayList<>();
		tarefasExecutando = new ArrayList<>();
		tarefasProntas = new ArrayList<>();
		tarefasTerminadas = new ArrayList<>();

		try {
			Scanner in = new Scanner(m_file);

			while (in.hasNextLine()) {
				numberOfProcesses++;
				in.nextLine();
			}
			in = new Scanner(m_file);

			try {
				for (int linha = 0; linha < numberOfProcesses; linha++) {
					Task tarefa = new Task();
					tarefa.setDate(in.nextInt());
					tarefa.setTotalTime(in.nextInt());
					tarefa.setPriority(in.nextInt());
					tarefa.setID(Integer.toString(linha));

					tarefasNovas.add(tarefa);
				}
				// System.out.printf("tamanho da lista: %d \n\n",
				// tarefasNovas.size());
				in.close();
			} catch (NoSuchElementException nex) {
				System.err.println("Erro na criação da lista com os processos.");
			}

		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("Erro ao ler o arquivo.");
		}

		System.out.print("tempo   ");
		for (int i = 0; i < numberOfProcesses; i++) {
			System.out.printf("P%d  ", i);
		}
		System.out.print("\n");

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				scheduler(elapsedTime);
				elapsedTime++;
			}
		}, 0, 1000);

	}

}

// class Task {
// public int id;
// public int date;//em que tempo sera criada
// public int duration; //quanto tempo precisa no processador
// public int executingTime = 0; //tempo de processamento ja decorrido
// public int priority;
// public String status;
// };
