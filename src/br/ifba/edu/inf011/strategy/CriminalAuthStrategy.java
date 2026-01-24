package br.ifba.edu.inf011.strategy;

import br.ifba.edu.inf011.model.documentos.Documento;

import java.time.LocalDate;

public class CriminalAuthStrategy implements AuthStrategy {
    // concrete strategy
    @Override
    public String generateAuthCode(Documento documento) {
        return "CRI-" + LocalDate.now().getYear() + "-" + documento.hashCode();
    }
}
