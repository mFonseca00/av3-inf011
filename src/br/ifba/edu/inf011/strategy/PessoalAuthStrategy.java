package br.ifba.edu.inf011.strategy;

import br.ifba.edu.inf011.model.documentos.Documento;

import java.time.LocalDate;

public class PessoalAuthStrategy implements AuthStrategy{
    // concrete strategy
    @Override
    public String generateAuthCode(Documento documento) {
        return "PES-" + LocalDate.now().getDayOfYear() + "-" + documento.getProprietario().hashCode();
    }
}
