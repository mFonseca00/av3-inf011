package br.ifba.edu.inf011.model;

import br.ifba.edu.inf011.af.DocumentOperatorFactory;
import br.ifba.edu.inf011.command.*;
import br.ifba.edu.inf011.model.documentos.Documento;
import br.ifba.edu.inf011.model.documentos.Privacidade;
import br.ifba.edu.inf011.model.operador.Operador;
import br.ifba.edu.inf011.strategy.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// client
public class GerenciadorDocumentoModel {
    private Map<Integer , AuthStrategy> strategies;
	private List<Documento> repositorio;
    private DocumentOperatorFactory factory;
    private Autenticador autenticador;
    private GestorDocumento gestor;
    private DocumentHolder holder;
    private CommandManager commandManager;

    public GerenciadorDocumentoModel(DocumentOperatorFactory factory) {
        this.factory = factory;
        this.repositorio = new ArrayList<>();
        this.autenticador = new Autenticador();
        this.gestor = new GestorDocumento();
        this.holder = new DocumentHolder(null);
        this.commandManager = new CommandManager();
        this.strategies = Map.of(
            0, new CriminalAuthStrategy(),
            1, new PessoalAuthStrategy(),
            2, new ExportAuthStrategy()
        );
    }

    public Documento criarDocumento(int tipoAutenticadorIndex, Privacidade privacidade) throws FWDocumentException {
        // Mock de operador
        Operador operador = factory.getOperador();
        operador.inicializar("jdc", "João das Couves");

        AuthStrategy strategy = strategies.get(tipoAutenticadorIndex);
        if (strategy == null) {
            strategy = new DefaultAuthStrategy();
        }
        this.autenticador.setStrategy(strategy);
        Command comando = new CriarDocCommand(holder, factory, autenticador,
                privacidade, operador);
        commandManager.executeCommand(comando);
        Documento doc = holder.getDocument();
        if (doc != null && !repositorio.contains(doc)) {
            repositorio.add(doc);
        }
        return doc;
    }

    public void salvarDocumento(String conteudo) throws FWDocumentException {
        if (holder.getDocument() == null) {
            System.err.println("Nenhum documento selecionado");
            return;
        }
        Command comando = new SalvarCommand(holder, conteudo);
        commandManager.executeCommand(comando);
    }

    public void assinarDocumento() throws FWDocumentException {
        // Mock de operador
        Operador operador = factory.getOperador();
        operador.inicializar("jdc", "João das Couves");

        if (holder.getDocument() == null) {
            System.err.println("Nenhum documento selecionado");
            return;
        }
        Documento documentoAntigo = holder.getDocument();
        Command comando = new AssinarCommand(holder, gestor, operador);
        commandManager.executeCommand(comando);
        atualizarRepositorio(documentoAntigo, holder.getDocument());
    }

    public void protegerDocumento() throws FWDocumentException {
        if (holder.getDocument() == null) {
            System.err.println("Nenhum documento selecionado");
            return;
        }
        Documento documentoAntigo = holder.getDocument();
        Command comando = new ProtegerCommand(holder, gestor);
        commandManager.executeCommand(comando);
        atualizarRepositorio(documentoAntigo, holder.getDocument());
    }

    public void tornarUrgente() throws FWDocumentException {
        if (holder.getDocument() == null) {
            System.err.println("Nenhum documento selecionado");
            return;
        }
        Documento documentoAntigo = holder.getDocument();
        Command comando = new TornarUrgenteCommand(holder, gestor);
        commandManager.executeCommand(comando);
        atualizarRepositorio(documentoAntigo, holder.getDocument());
    }

    public void alterarEAssinar(String conteudo) throws FWDocumentException {
        // Mock de operador
        Operador operador = factory.getOperador();
        operador.inicializar("jdc", "João das Couves");

        if (holder.getDocument() == null) {
            System.err.println("Nenhum documento selecionado");
            return;
        }
        Documento documentoAntigo = holder.getDocument();
        MacroCommand macro = new MacroCommand("Alterar e Assinar");
        macro.adicionarComando(new SalvarCommand(holder, conteudo));
        macro.adicionarComando(new AssinarCommand(holder, gestor, operador));
        commandManager.executeCommand(macro);
        atualizarRepositorio(documentoAntigo, holder.getDocument());
    }

    public void priorizar() throws FWDocumentException {
        Operador operador = factory.getOperador();
        operador.inicializar("jdc", "João das Couves");

        if (holder.getDocument() == null) {
            System.err.println("Nenhum documento selecionado");
            return;
        }
        Documento documentoAntigo = holder.getDocument();
        MacroCommand macro = new MacroCommand("Priorizar");
        macro.adicionarComando(new TornarUrgenteCommand(holder, gestor));
        macro.adicionarComando(new AssinarCommand(holder, gestor, operador));
        commandManager.executeCommand(macro);
        atualizarRepositorio(documentoAntigo, holder.getDocument());
    }

    public boolean undo() {
        Documento docAtual = holder.getDocument();
        boolean resultado = commandManager.undo();
        if (resultado) {
            Documento docNovo = holder.getDocument();
            // Se documento mudou após undo
            if (docAtual != docNovo) {
                // Remove documento que foi desfeito
                if (docNovo == null && docAtual != null) {
                    repositorio.remove(docAtual);
                    System.out.println("Documento removido do repositório (undo de criação)");
                }
                // Se ambos não são null, era um decorator - att
                else if (docAtual != null && docNovo != null) {
                    atualizarRepositorio(docAtual, docNovo);
                    System.out.println("Documento atualizado no repositório (undo de decorator)");
                }
            }
        }
        return resultado;
    }

    public boolean redo() throws FWDocumentException {
        Documento docAtual = holder.getDocument();
        boolean resultado = commandManager.redo();
        if (resultado) {
            Documento docNovo = holder.getDocument();
            if (docAtual != docNovo) {
                // Se era null e agora tem documento, é recriação - add
                if (docAtual == null && docNovo != null) {
                    if (!repositorio.contains(docNovo)) {
                        repositorio.add(docNovo);
                        System.out.println("Documento adicionado ao repositório (redo de criação)");
                    }
                }
                // Se ambos não são null, era decorator - att
                else if (docAtual != null && docNovo != null) {
                    atualizarRepositorio(docAtual, docNovo);
                    System.out.println("Documento atualizado no repositório (redo de decorator)");
                }
            }
        }
        return resultado;
    }

    public void consolidar() {
        commandManager.consolidate();
    }

    private void atualizarRepositorio(Documento antigo, Documento novo) {
        if (antigo != null && novo != null && antigo != novo) {
            int index = repositorio.indexOf(antigo);
            if (index >= 0) {
                repositorio.set(index, novo);
            } else if (!repositorio.contains(novo)) {
                repositorio.add(novo);
            }
        }
    }

    public Documento getDocumentoAtual() {
        return holder.getDocument();
    }

    public List<Documento> getRepositorio() {
        return repositorio;
    }

	public void setDocumentoAtual(Documento doc) {
		this.holder.setDocument(doc);
	}
    
    public int getUndoSize(){
        return this.commandManager.getUndoSize();
    }
    
    public int getRedoSize(){
        return this.commandManager.getRedoSize();
    }
}
