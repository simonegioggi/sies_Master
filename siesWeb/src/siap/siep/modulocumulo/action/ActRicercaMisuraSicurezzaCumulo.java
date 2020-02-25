package siap.siep.modulocumulo.action;

/**
* <p>Title: ActRicercaMisuraSicurezzaCumulo</p>
* <p>Description: Classe Action per la ricerca di MisuraSicurezzaCumulo per un
*                 certo Titolo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: </p>
* @version 1.0
*/

import java.util.Vector;

import f3b.util.F3BException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.modulocumulo.controller.IMisuraSicurezzaCumulo;
import siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

public class ActRicercaMisuraSicurezzaCumulo extends ActionModuloCumulo
		implements ICostantiMisuraSicurezzaCumulo {
	/*****************************************************************************
	 * Azione di Ricerca Misure di sicurezza per titolo. Recupera i dati dalla form ed effettua la ricerca.
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		// ==========================================================================
		// Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
		// di DettaglioTitoloCumulato.jsp
		// ==========================================================================
		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		// ==============================================================
		// Recupero dalla form i campi di ricerca
		// n.b. cancellare o commentare i campi non presenti nella form
		// ==============================================================
		MisuraSicurezzaCumuloModel lMisMod = new MisuraSicurezzaCumuloModel();
		lMisMod.setTitIdTitoloCumulato(
				getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));

		// =========================================================
		// Istanzio il controller ed effettuo la ricerca semplice
		// =========================================================
		IMisuraSicurezzaCumulo lCtrl = SIEPLookupRemote.getMisuraSicurezzaCumuloRemote();
		Vector lVect = lCtrl.ExRicercaMisuraSicurezzaCumulo(lMisMod);

		setRequestAttribute("ListaMisureSicurezza", lVect);

		return PG_ELENCO_MISURASICUREZZA_CUMULO;
	}

}