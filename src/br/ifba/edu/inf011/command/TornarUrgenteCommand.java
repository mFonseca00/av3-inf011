package br.ifba.edu.inf011.command;

import br.ifba.edu.inf011.model.GestorDocumento;
import br.ifba.edu.inf011.model.documentos.Documento;

public class TornarUrgenteCommand implements Command{
    private DocumentHolder holder;
    private Documento estadoAnterior;
    private GestorDocumento gestor;

    public TornarUrgenteCommand(DocumentHolder holder, GestorDocumento gestor) {
        this.holder = holder;
        this.gestor = gestor;
    }

    @Override
    public void execute() {
        estadoAnterior = holder.getDocument();
        Documento documentoUrgente = gestor.tornarUrgente(estadoAnterior);
        holder.setDocument(documentoUrgente);
        System.out.println("Documento marcado como URGENTE");
    }

    @Override
    public void undo() {
        holder.setDocument(estadoAnterior);
        System.out.println("Urgência removida");
    }

    @Override
    public String getLogInfo() {
        return "Tornar documento urgente";
    }
}
