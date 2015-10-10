
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author williams
 */
public class Escalonamento {

    public static final String STATUS_NOVA = "nova";
    public static final String STATUS_EXECUTANDO = "executando";
    public static final String STATUS_PARADO = "parado";
    public static final String STATUS_TERMINADO = "terminado";

    public static String gerarLog(int tempo, List<Tarefa> tarefas) {
        StringBuilder log = new StringBuilder();
        log.append(tempo).append("-").append(tempo + 1).append("    ");

        for (Tarefa tarefa : tarefas) {
            switch (tarefa.status) {
                case STATUS_PARADO:
                    log.append("--");
                    break;
                case STATUS_EXECUTANDO:
                    log.append("##");
                    break;
                case STATUS_TERMINADO:
                    log.append("  ");
                    break;
                default:
                    break;
            }
            log.append("  ");
        }
        log.append("\n");
        return log.toString();
    }

    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
        List<Tarefa> tarefas = new ArrayList<>();
        File input = new File("/home/williams/Projetos/Escalonamento/src/input.txt");
        FileReader reader = new FileReader(input);
        BufferedReader br = new BufferedReader(reader);

        File output = new File("/home/williams/Projetos/Escalonamento/src/output.txt");
        FileWriter writer = new FileWriter(output, true);
        writer.write("tempo   P1  P2  P3  P4\n");

        while (br.ready()) {
            String[] linha = br.readLine().split(" ");
            Tarefa tarefa = new Tarefa();
            tarefa.data = Integer.parseInt(linha[0]);
            tarefa.tempoTotal = Integer.parseInt(linha[1]);
            tarefa.prioridade = Integer.parseInt(linha[2]);
            tarefa.status = STATUS_NOVA;
            tarefas.add(tarefa);
        }
        br.close();
        int tempo = 0;
        long quantum = 200;//milisegundos
        while (tarefas.stream().anyMatch((tarefa) -> (!tarefa.status.equals(STATUS_TERMINADO)))) {
            for (Tarefa tarefa : tarefas) {
                tarefa.status = STATUS_EXECUTANDO;
                Thread.currentThread().sleep(quantum);
                writer.write(gerarLog(tempo, tarefas));
                tempo += 1;
                tarefa.tempoExecutado += (quantum / 100);
                if (tarefa.tempoExecutado < tarefa.tempoTotal) {
                    tarefa.status = STATUS_PARADO;
                } else {
                    tarefa.status = STATUS_TERMINADO;
                }
            }
        }
        writer.close();

    }

}
