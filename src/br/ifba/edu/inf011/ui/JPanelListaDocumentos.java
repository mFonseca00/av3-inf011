package br.ifba.edu.inf011.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class JPanelListaDocumentos<T> extends JPanel{
	
	private JList<T> listDocumentos;
	
	public JPanelListaDocumentos(DefaultListModel<T> listModel, ListSelectionListener listener) {
		super(new BorderLayout());
		this.listDocumentos = new JList<T>(listModel);
		this.listDocumentos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.listDocumentos.setPreferredSize(new Dimension(200, 0));
		this.listDocumentos.setBorder(BorderFactory.createTitledBorder("Documentos"));
		this.listDocumentos.addListSelectionListener(listener);
		this.add(this.listDocumentos, BorderLayout.CENTER);
	}

	public void addDoc(T doc) {
		DefaultListModel<T> model = (DefaultListModel<T>)this.listDocumentos.getModel(); 
        model.addElement(doc);
        this.listDocumentos.setSelectedIndex(model.size() - 1);
        this.updateUI();
	}

	public void removerDocumento(int index) {
		DefaultListModel<T> model = (DefaultListModel<T>)this.listDocumentos.getModel();
		if (index >= 0 && index < model.size()) {
			model.remove(index);

			if (model.size() == 0) {
				this.listDocumentos.clearSelection();
			}
			else if (index > 0) {
				this.listDocumentos.setSelectedIndex(index - 1);
			} else {
				this.listDocumentos.setSelectedIndex(0);
			}

			this.updateUI();
		}
	}

	public int getIndiceDocSelecionado() {
		return this.listDocumentos.getSelectedIndex();
	}


	public DefaultListModel<T> getModel() {
		return (javax.swing.DefaultListModel<T>) this.listDocumentos.getModel();
	}
}
