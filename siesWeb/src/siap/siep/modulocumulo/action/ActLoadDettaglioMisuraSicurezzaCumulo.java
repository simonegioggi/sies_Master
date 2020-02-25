package siap.siep.modulocumulo.action;

/**
* <p>Title: ActLoadDettaglioMisuraSicurezzaCumulo</p>
* <p>Description: Classe Action per la load dettaglio di MisuraSicurezzaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import f3b.util.F3BException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.modulocumulo.controller.IMisuraSicurezzaCumulo;
import siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

public class ActLoadDettaglioMisuraSicurezzaCumulo extends ActionModuloCumulo
		implements ICostantiMisuraSicurezzaCumulo {

	/*****************************************************************************
	 * Azione di caricamento della pagina di Dettaglio dei dati.
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
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
		// BigDecimal lIdTito =
		// getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);
		// ==========================================
		// Recupera la key del record da Visualizzare
		// ==========================================
		BigDecimal lIdMisuraSicurezzaCumulo = getRequestBigDecimalParameter(CAMPO_ID_MISURA_SICUREZZA_CUMULO);

		// ==========================================
		// Recupera i dati del record
		// ==========================================
		IMisuraSicurezzaCumulo lCtrl = SIEPLookupRemote.getMisuraSicurezzaCumuloRemote();
		MisuraSicurezzaCumuloModel lMisMod = lCtrl
				.ExRicercaMisuraSicurezzaCumuloById(lIdMisuraSicurezzaCumulo);

		// ====================================================
		// Passa il Model alla componente di visualizzazione
		// ====================================================
		setRequestAttribute("lMisuraSicurezzaCumulo", lMisMod);

		// =========================================================
		// Restituisce la pagina di Visualizzazione del Dettaglio.
		// =========================================================
		return PG_LOAD_DETTAGLIO_MISURASICUREZZA_CUMULO;
	}
}