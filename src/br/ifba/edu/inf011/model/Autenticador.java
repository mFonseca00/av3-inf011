package br.ifba.edu.inf011.model;

import br.ifba.edu.inf011.model.documentos.Documento;
import br.ifba.edu.inf011.strategy.AuthStrategy;
import br.ifba.edu.inf011.strategy.DefaultAuthStrategy;

public class Autenticador {
	// context
	private AuthStrategy strategy;
    
    public Autenticador() {
        this.strategy = new DefaultAuthStrategy();
    }
    
    public Autenticador(AuthStrategy strategy) {
        this.strategy = strategy;
    }
    
    public void setStrategy(AuthStrategy strategy) {
        this.strategy = strategy;
    }

	public void autenticar(Documento documento) {
        String numero = this.strategy.generateAuthCode(documento);
        documento.setNumero(numero);
	}

}
