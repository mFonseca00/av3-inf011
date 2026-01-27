package br.ifba.edu.inf011.command;

import br.ifba.edu.inf011.model.GestorDocumento;
import br.ifba.edu.inf011.model.documentos.Documento;

// Concrete Command
public class ProtegerCommand implements Command{
    private DocumentHolder holder;
    private Documento estadoAnterior;
    private GestorDocumento gestor;

    public ProtegerCommand(DocumentHolder holder, GestorDocumento gestor) {
        this.holder = holder;
        this.gestor = gestor;
    }

    @Override
    public void execute() {
        estadoAnterior = holder.getDocument();
        Documento documentoProtegido = gestor.proteger(estadoAnterior);
        holder.setDocument(documentoProtegido);
        System.out.println("Documento protegido com sucesso");
    }

    @Override
    public void undo() {
        holder.setDocument(estadoAnterior);
        System.out.println("Proteção removida");
    }

    @Override
    public String getLogInfo() {
        return "Proteger documento";
    }
}
