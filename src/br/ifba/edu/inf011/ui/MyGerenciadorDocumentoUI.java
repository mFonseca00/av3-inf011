package br.ifba.edu.inf011.ui;

import br.ifba.edu.inf011.af.DocumentOperatorFactory;
import br.ifba.edu.inf011.model.FWDocumentException;
import br.ifba.edu.inf011.model.documentos.Documento;
import br.ifba.edu.inf011.model.documentos.Privacidade;

import javax.swing.*;

public class MyGerenciadorDocumentoUI extends AbstractGerenciadorDocumentosUI{
	
	
	 public MyGerenciadorDocumentoUI(DocumentOperatorFactory factory) {
		super(factory);
	}

	protected JPanelOperacoes montarMenuOperacoes() {
		JPanelOperacoes comandos = new JPanelOperacoes();
		comandos.addOperacao("➕ Criar Publico", e -> this.criarDocumentoPublico());
		comandos.addOperacao("➕ Criar Privado", e -> this.criarDocumentoPrivado());
		comandos.addOperacao("💾 Salvar", e-> this.salvarConteudo());
		comandos.addOperacao("🔑 Proteger", e->this.protegerDocumento());
		comandos.addOperacao("✍️ Assinar", e->this.assinarDocumento());
		comandos.addOperacao("⏰ Urgente", e->this.tornarUrgente());

		comandos.addOperacao("📝 Alterar e Assinar", e->this.alterarEAssinar());
		comandos.addOperacao("🚨 Priorizar", e->this.priorizar());
		comandos.addOperacao("◄ Desfazer", e->this.undo());
		comandos.addOperacao("► Refazer", e->this.redo());
		comandos.addOperacao("✓ Consolidar", e->this.consolidar());
		return comandos;
	 }
	
	protected void criarDocumentoPublico() {
		this.criarDocumento(Privacidade.PUBLICO);
	}
	
	protected void criarDocumentoPrivado() {
		this.criarDocumento(Privacidade.SIGILOSO);
	}
	
	protected void salvarConteudo() {
        try {
			if(this.atual == null){
				JOptionPane.showMessageDialog(this,"Ação Bloqueada, nenhum documento selecionado na lista de documentos.");
				return;
			}
            this.controller.salvarDocumento(this.areaEdicao.getConteudo());
			this.atual = this.controller.getDocumentoAtual();
			this.refreshUI();
        } catch (Exception e) {
        	JOptionPane.showMessageDialog(this, "Erro ao Salvar: " + e.getMessage());
        }
    }	
	
	protected void protegerDocumento() {
		try {
			if(this.atual == null){
				JOptionPane.showMessageDialog(this,"Ação Bloqueada, nenhum documento selecionado na lista de documentos.");
				return;
			}
			this.controller.protegerDocumento();
			this.atual = this.controller.getDocumentoAtual();
			this.refreshUI();
		} catch (FWDocumentException e) {
			JOptionPane.showMessageDialog(this, "Erro ao proteger: " + e.getMessage());
		}
	}

	protected void assinarDocumento() {
		try {
			if(this.atual == null){
				JOptionPane.showMessageDialog(this,"Ação Bloqueada, nenhum documento selecionado na lista de documentos.");
				return;
			}
			this.controller.assinarDocumento();
			this.atual = this.controller.getDocumentoAtual();
			this.refreshUI();
		} catch (FWDocumentException e) {
			JOptionPane.showMessageDialog(this, "Erro ao assinar: " + e.getMessage());
		}		
	}
	
	protected void tornarUrgente() {
		try {
			if(this.atual == null){
				JOptionPane.showMessageDialog(this,"Ação Bloqueada, nenhum documento selecionado na lista de documentos.");
				return;
			}
			this.controller.tornarUrgente();
			this.atual = this.controller.getDocumentoAtual();
			this.refreshUI();
		} catch (FWDocumentException e) {
			JOptionPane.showMessageDialog(this, "Erro ao tornar urgente: " + e.getMessage());
		}		
	}	

	private void criarDocumento(Privacidade privacidade) {
        try {
            int tipoIndex = this.barraSuperior.getTipoSelecionadoIndice();
            this.atual = this.controller.criarDocumento(tipoIndex, privacidade);
            this.barraDocs.addDoc("[" + atual.getNumero() + "]");
            this.refreshUI();
        } catch (FWDocumentException e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }

	protected void alterarEAssinar() {
		try {
			if(this.atual == null){
				JOptionPane.showMessageDialog(this,"Ação Bloqueada, nenhum documento selecionado na lista de documentos.");
				return;
			}
			String conteudo = this.areaEdicao.getConteudo();
			this.controller.alterarEAssinar(conteudo);

			this.atual = this.controller.getDocumentoAtual();
			this.refreshUI();
		} catch (FWDocumentException e) {
			JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
		}
	}

	protected void priorizar() {
		try {
			if(this.atual == null){
				JOptionPane.showMessageDialog(this,"Ação Bloqueada, nenhum documento selecionado na lista de documentos.");
				return;
			}
			this.controller.priorizar();

			this.atual = this.controller.getDocumentoAtual();
			this.refreshUI();
		} catch (FWDocumentException e) {
			JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
		}
	}

	protected void undo() {
		if(this.controller.getUndoSize()<1){
			JOptionPane.showMessageDialog(this, "⚠ Nada para desfazer");
			return;
		}
		Documento docAntes = this.controller.getDocumentoAtual();
		this.controller.undo();
		Documento docDepois = this.controller.getDocumentoAtual();
		if (docAntes != null && docDepois == null) {
			// Era um documento criado, agora é null = precisa remover da lista
			// Busca o último item (documento mais recente)
			DefaultListModel<String> model = this.barraDocs.getModel();
			if (model.getSize() > 0) {
				model.remove(model.getSize() - 1);
			}
		}

		this.atual = docDepois;
		this.refreshUI();
		JOptionPane.showMessageDialog(this, "✓ Operação desfeita!");
	}

	protected void redo() {
		if(this.controller.getRedoSize()<1){
			JOptionPane.showMessageDialog(this, "⚠ Nada para refazer");
			return;
		}
		try {
			Documento docAntes = this.controller.getDocumentoAtual();
			this.controller.redo();
			Documento docDepois = this.controller.getDocumentoAtual();
			if (docAntes == null && docDepois != null) {
				// Era null, agora é documento = foi redo de criação
				// Adiciona de volta na lista visual
				this.barraDocs.addDoc("[" + docDepois.getNumero() + "]");
			}
			this.atual = docDepois;
			this.refreshUI();
			JOptionPane.showMessageDialog(this, "✓ Operação refeita!");
		} catch (FWDocumentException e) {
			JOptionPane.showMessageDialog(this, "Erro ao refazer: " + e.getMessage());
		}
	}

	protected void consolidar() {
		int resposta = JOptionPane.showConfirmDialog(this,
				"Consolidar irá limpar o histórico de undo/redo.\nDeseja continuar?",
				"Confirmar Consolidação",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);

		if (resposta == JOptionPane.YES_OPTION) {
			this.controller.consolidar();
			JOptionPane.showMessageDialog(this, "✓ Histórico consolidado!");
		}
	}

}
