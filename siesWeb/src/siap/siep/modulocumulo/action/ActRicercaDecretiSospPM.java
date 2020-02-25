package siap.siep.modulocumulo.action;

import java.util.Vector;

import f3b.util.F3BException;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action che recupera la lista dei Decreti di sospensione disposti dal PM sul titolo indicato
 *
 * - Sospensione esecuzione ex art. 656 c.p.p - DL 78/2013 - Legge 199/2010
 *
 * @author d.fiorletta
 *
 */
public class ActRicercaDecretiSospPM extends ActionModuloCumulo implements ICostantiStatoEsecTitoloCumulato {

	public String processRequest() throws F3BException {

		// ==========================================================================
		// Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
		// di DettaglioTitoloCumulato.jsp
		// ==========================================================================
		super.getDatiIstruttoria();
		TitoloCumulatoModel lTitolo = super.getDatiTitoloCumulato();

		// ==========================================================================
		// Effettuo la ricerca dei Decreti di Sosp disposti dal PM collegati al Titolo
		// ==========================================================================
		IStatoEsecTitoloCumulato lCtrlStatoEsec = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
		Vector<StatoEsecTitoloCumulatoModel> lListaDecretiSosp = lCtrlStatoEsec
				.ExRicercaSospensioniDelPMByIdTitolo(lTitolo.getIdTitoloCumulato());

		setRequestAttribute("ListaDecretiSosp", lListaDecretiSosp);

		return PG_LOAD_ELENCO_SOSP_PM;

	}

}