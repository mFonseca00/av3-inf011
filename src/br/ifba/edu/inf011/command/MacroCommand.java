package br.ifba.edu.inf011.command;

import br.ifba.edu.inf011.model.FWDocumentException;

import java.util.ArrayList;
import java.util.List;

// Concrete Command e composite
public class MacroCommand implements Command{
    private List<Command> comandos;
    private String descricao;

    public MacroCommand(String descricao) {
        this.comandos = new ArrayList<>();
        this.descricao = descricao;
    }

    public void adicionarComando(Command comando) {
        comandos.add(comando);
    }

    @Override
    public void execute() {
        System.out.println("--- Executando Macro: " + descricao + " ---");
        for (Command comando : comandos) {
            try {
                comando.execute();
            } catch (FWDocumentException e) {
                System.out.println("Erro ao executar comando na macro: " + e.getMessage());
            }
        }
        System.out.println("--- Macro concluída ---");
    }

    @Override
    public void undo() {
        System.out.println("--- Desfazendo Macro: " + descricao + " ---");
        for (int i = comandos.size() - 1; i >= 0; i--) {
            comandos.get(i).undo();
        }
        System.out.println("--- Macro desfeita ---");
    }

    @Override
    public String getLogInfo() {
        return "Macro: " + descricao + " (" + comandos.size() + " operações)";
    }

    public int size() {
        return comandos.size();
    }
}
