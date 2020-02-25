package siap.siep.provvedimentopm.controller;

/**
* <p>Title: ProvvedimentoController</p>
* <p>Description: Classe Controller per Provvedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.io.ByteArrayOutputStream;
import java.util.Vector;

import siap.sico.utente.model.UtenteModel;
import siap.siep.provvedimentopm.model.ProvvedimentoModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface IProvvedimento {

	public ByteArrayOutputStream ExInserisciProvvedimento(ProvvedimentoModel aProvvedimento,
			UtenteModel aUtente) throws F3BException;

	public ByteArrayOutputStream ExGetDocumento(ProvvedimentoModel aProvvedimento) throws F3BException;

	public Vector ExRicercaProvvedimento(ProvvedimentoModel aProvvedimento) throws F3BException;

	public void ExModificaProvvedimento(ProvvedimentoModel aProvvedimento) throws F3BException;

	public void ExCancellaProvvedimento(ProvvedimentoModel aProvvedimento) throws F3BException;

}