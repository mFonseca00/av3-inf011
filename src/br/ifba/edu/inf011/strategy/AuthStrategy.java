package br.ifba.edu.inf011.strategy;

import br.ifba.edu.inf011.model.documentos.Documento;

public interface AuthStrategy {
    // strategy
    String generateAuthCode(Documento documento);
}
