package br.ifba.edu.inf011.strategy;

import br.ifba.edu.inf011.model.documentos.Documento;

public class DefaultAuthStrategy implements AuthStrategy {
    // concrete strategy
    @Override
    public String generateAuthCode(Documento documento) {
        return "DOC-" + System.currentTimeMillis();
    }
}
