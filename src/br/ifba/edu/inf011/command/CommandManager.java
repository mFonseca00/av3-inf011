package br.ifba.edu.inf011.command;

import br.ifba.edu.inf011.model.FWDocumentException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Stack;

// Invoker no padrão Command
public class CommandManager {
    private Stack<Command> undoStack;
    private Stack<Command> redoStack;
    private static final String LOG_FILE = "operations.log";
    private DateTimeFormatter formatter;

    public CommandManager() {
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    public void executeCommand(Command command) throws FWDocumentException {
        command.execute();
        undoStack.push(command);
        redoStack.clear(); // Limpa redo ao executar novo comando
        log("EXECUTE", command.getLogInfo());
    }

    public boolean undo() {
        if (undoStack.isEmpty()) {
            System.out.println("Não há nenhum comando para desfazer");
            return false;
        }
        Command command = undoStack.pop();
        command.undo();
        redoStack.push(command);
        log("UNDO", command.getLogInfo());
        return true;
    }

    public boolean redo() throws FWDocumentException {
        if (redoStack.isEmpty()) {
            System.out.println("Não há nenhum comando para refazer");
            return false;
        }
        Command command = redoStack.pop();
        command.execute();
        undoStack.push(command);
        log("REDO", command.getLogInfo());
        return true;
    }

    public void consolidate() {
        int undoSize = undoStack.size();
        int redoSize = redoStack.size();
        undoStack.clear();
        redoStack.clear();
        log("CONSOLIDAR - ", String.format("Histórico limpo - %d undo(s) e %d redo(s) removidos",undoSize, redoSize));
        System.out.println("Operações consolidadas. Histórico limpo.");
    }

    private void log(String action, String description) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            String timestamp = LocalDateTime.now().format(formatter);
            String logEntry = String.format("[%s] %s - %s", timestamp, action, description);
            pw.println(logEntry);
            System.out.println("INFO: Log registrado: " + logEntry);
        } catch (IOException e) {
            System.err.println("Erro ao registrar log: " + e.getMessage());
        }
    }

    public int getUndoSize() {
        return undoStack.size();
    }

    public int getRedoSize() {
        return redoStack.size();
    }
}
