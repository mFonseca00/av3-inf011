package br.ifba.edu.inf011.command;

import br.ifba.edu.inf011.af.DocumentOperatorFactory;
import br.ifba.edu.inf011.model.Autenticador;
import br.ifba.edu.inf011.model.FWDocumentException;
import br.ifba.edu.inf011.model.documentos.Documento;
import br.ifba.edu.inf011.model.documentos.Privacidade;
import br.ifba.edu.inf011.model.operador.Operador;

// Concrete Command
public class CriarDocCommand implements Command{
    private DocumentHolder holder;
    private DocumentOperatorFactory factory;
    private Autenticador autenticador;
    private Privacidade privacidade;
    private Documento documentoAnterior;
    private Operador operador;

    public CriarDocCommand(DocumentHolder holder,
                             DocumentOperatorFactory factory,
                             Autenticador autenticador,
                             Privacidade privacidade,
                             Operador operador) {
        this.holder = holder;
        this.factory = factory;
        this.autenticador = autenticador;
        this.privacidade = privacidade;
        this.operador = operador;
    }

    @Override
    public void execute() {
        try {
            documentoAnterior = holder.getDocument();
            Documento documento = factory.getDocumento();
            documento.inicializar(operador, privacidade);
            autenticador.autenticar(documento);
            holder.setDocument(documento);
            System.out.println("Documento criado e autenticado");
        } catch (FWDocumentException e) {
            System.err.println("Erro ao criar documento: " + e.getMessage());
        }
    }

    @Override
    public void undo() {
        holder.setDocument(documentoAnterior);
        System.out.println("Criação desfeita");
    }

    @Override
    public String getLogInfo() {
        return "Criar documento - Privacidade: " + privacidade;
    }
}
