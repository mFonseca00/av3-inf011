package br.ifba.edu.inf011.strategy;

import br.ifba.edu.inf011.model.documentos.Documento;
import br.ifba.edu.inf011.model.documentos.Privacidade;

public class ExportAuthStrategy implements AuthStrategy {
    // concrete strategy
    @Override
    public String generateAuthCode(Documento documento) {
        if (documento.getPrivacidade() == Privacidade.SIGILOSO) {
            return "SECURE-" + documento.hashCode();
        } else {
            return "PUB-" + documento.hashCode();
        }
    }
}
