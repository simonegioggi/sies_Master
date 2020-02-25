package siap.sico.jms.action;

import java.util.Vector;

import siap.jms.jmscode.controller.JmsCodeController;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadListaEsitiRicerca
 * </p>
 * <p>
 * Description: Classe Action per la load della Lista degli Esiti della Ricerca
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActLoadListaEsitiRicercaFascAltreBDI extends ActionSiap implements ICostantiSicoJMS {

	public String processRequest() throws F3BException {

		// Imposta la combo dei Tipi di Operazioni.
		JmsCodeController lCrtl = new JmsCodeController();

		Option lOption = new Option(
				lCrtl.ExRicercaPerDominioEDescrizione("TIPO_OPERAZIONE", "TRASFERIMENTO"));
		setRequestAttribute("tipoOperazione", "" + lOption);

		// esegue la query per recuperare l'elenco degli uffici accorpati
		IUfficio lUACon = SICOLookupRemote.getUfficioRemote();
		Vector lUffAccTotali = lUACon.ListaUfficiAccorpati(null, null);
		setRequestAttribute("ufficiAccorpati", lUffAccTotali);

		return PG_LOAD_LISTA_ESITI_RICERCA_FASC_ALTRE_BDI; // restituisce la jsp di VIEW
	}

}