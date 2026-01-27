package br.ifba.edu.inf011.command;

import br.ifba.edu.inf011.model.FWDocumentException;
import br.ifba.edu.inf011.model.documentos.Documento;

public class SalvarCommand implements Command{
    private DocumentHolder holder;
    private String novoConteudo;
    private String conteudoAnterior;

    public SalvarCommand(DocumentHolder holder, String novoConteudo) {
        this.holder = holder;
        this.novoConteudo = novoConteudo;
    }

    @Override
    public void execute() {
        Documento documento = holder.getDocument();
        try {
            conteudoAnterior = documento.getConteudo();
        } catch (FWDocumentException e) {
            conteudoAnterior = "";
        }
        documento.setConteudo(novoConteudo);
        System.out.println("Conteúdo salvo com sucesso");
    }

    @Override
    public void undo() {
        Documento documento = holder.getDocument();
        documento.setConteudo(conteudoAnterior);
        System.out.println("Conteúdo restaurado");
    }

    @Override
    public String getLogInfo() {
        return "Salvar conteúdo do documento";
    }
}
