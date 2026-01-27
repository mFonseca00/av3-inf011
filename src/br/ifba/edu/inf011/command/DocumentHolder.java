package br.ifba.edu.inf011.command;

import br.ifba.edu.inf011.model.documentos.Documento;

// Usado para armazenar o documento atual no Command pattern antes de executar comandos
public class DocumentHolder {
    private Documento documento;

    public DocumentHolder(Documento documento) {
        this.documento = documento;
    }

    public Documento getDocument() {
        return documento;
    }

    public void setDocument(Documento documento) {
        this.documento = documento;
    }
}
