package br.ifba.edu.inf011.command;

import br.ifba.edu.inf011.model.GestorDocumento;
import br.ifba.edu.inf011.model.documentos.Documento;
import br.ifba.edu.inf011.model.operador.Operador;

// Concrete Command
public class AssinarCommand implements Command{
    private DocumentHolder holder;
    private Documento estadoAnterior;
    private GestorDocumento gestor;
    private Operador operador;

    public AssinarCommand(DocumentHolder holder, GestorDocumento gestor, Operador operador) {
        this.holder = holder;
        this.gestor = gestor;
        this.operador = operador;
    }

    @Override
    public void execute() {
        estadoAnterior = holder.getDocument();
        Documento documentoAssinado = gestor.assinar(estadoAnterior, operador);
        holder.setDocument(documentoAssinado);
        System.out.println("Documento assinado com sucesso por: " + operador.getNome());
    }

    @Override
    public void undo() {
        holder.setDocument(estadoAnterior);
        System.out.println("Assinatura removida");
    }

    @Override
    public String getLogInfo() {
        return "Assinar documento - Operador: " + operador.getNome();
    }
}
