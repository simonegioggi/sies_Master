package siap.sico.avvocato.controller;

/**
* <p>Title: AvvocatoController</p>
* <p>Description: Classe Controller per Avvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Vector;

import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface IAvvocato {

	public Vector ExRicercaForo() throws F3BException;

}