package br.ifba.edu.inf011.command;

import br.ifba.edu.inf011.model.FWDocumentException;

// Command interface
public interface Command {
    void execute() throws FWDocumentException;
    void undo();
    String getLogInfo();
}
