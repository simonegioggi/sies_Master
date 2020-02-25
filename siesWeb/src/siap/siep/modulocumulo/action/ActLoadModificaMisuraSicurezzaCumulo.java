package siap.siep.modulocumulo.action;

/**
* <p>Title: ActLoadModificaMisuraSicurezzaCumulo</p>
* <p>Description: Classe Action per la load modifica di MisuraSicurezzaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.modulocumulo.controller.IMisuraSicurezzaCumulo;
import siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

public class ActLoadModificaMisuraSicurezzaCumulo extends ActionModuloCumulo
		implements ICostantiMisuraSicurezzaCumulo {
	/*****************************************************************************
	 * Azione di caricamento della pagina di Modifica dei dati. Si occupa anche di precaricare tutti i dati da
	 * visualizzare i tale pagina (es: combo)
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
		//
		// ==========================================================================
		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		// ==========================================
		// Recupera la key del record da modificare
		// ==========================================
		BigDecimal lIdMisuraSicurezzaCumulo = getRequestBigDecimalParameter(CAMPO_ID_MISURA_SICUREZZA_CUMULO);

		// ==========================================
		// Recupera i dati del record da modificare
		// ==========================================
		IMisuraSicurezzaCumulo lCtrl = SIEPLookupRemote.getMisuraSicurezzaCumuloRemote();
		MisuraSicurezzaCumuloModel lMisuraSicurezzaCumulo = lCtrl
				.ExRicercaMisuraSicurezzaCumuloById(lIdMisuraSicurezzaCumulo);

		// ====================================================
		// Passa il Model alla componente di visualizzazione
		// ====================================================
		setRequestAttribute("misurasicurezzacumulo", lMisuraSicurezzaCumulo);

		// ==========================================================================
		// Caricamento Dati delle Combo
		// ==========================================================================

		// Natura Misura Sicurezza Cumulo
		Option lOptionN = new Option(DecodificheManager.getInstance().getNaturaMisuraSicurezza(),
				lMisuraSicurezzaCumulo.getCodNatura());
		setRequestAttribute("naturaMisuraSicurezza", "" + lOptionN);

		// Tipo Misura Sicurezza Cumulo
		Vector lVec = (Vector) DecodificheManager.getInstance().getTipoMisuraSicurezza();
		setRequestAttribute("tipoMisuraSicurezza", lVec);

		// Stato Misura Sicurezza Cumulo
		Option lOptionS = new Option(DecodificheManager.getInstance().getStatoMisuraSicurezzaCumulo());
		if (lMisuraSicurezzaCumulo.getFlagStatoMisura() != null)
			lOptionS.setSelected(lMisuraSicurezzaCumulo.getFlagStatoMisura());

		setRequestAttribute("StatoMisuraSicurezza", "" + lOptionS);

		// Codice autorità emittenete procedimento Classe IV a cui è iscritta la Misura Sicurezza Cumulo
		UfficioModel lUffMod = null;
		IUfficio CtrlU = SICOLookupRemote.getUfficioRemote();

		Option lOptionAutorita = new Option(DecodificheManager.getInstance().getTipoUfficio(), "-");
		String[] lFiltroAut = { "PM", "PGCAP", "PMM" };
		lOptionAutorita.setFilter(lFiltroAut);
		if (lMisuraSicurezzaCumulo.getCodAutoritaEmittenteIV() != null) {
			lUffMod = CtrlU.ExRicercaUfficioByCod(lMisuraSicurezzaCumulo.getCodAutoritaEmittenteIV());
			if (lUffMod != null && lUffMod.getCodUfficio() != null && lUffMod.getCodTipoUfficio() != null)
				lOptionAutorita.setSelected(lUffMod.getCodTipoUfficio());
		}

		setRequestAttribute("autoritaCumulo", "" + lOptionAutorita);

		// Imposta Modalità.
		String lModalita = MODALITA_MODIFICA;
		setRequestAttribute(MODALITA, lModalita);

		// ==========================================================================
		// Restituisce la pagina di modifica.
		// n.b. è la stessa della pagina di Inserimento ma con modalità differente
		// ==========================================================================
		return PG_LOAD_INSERISCI_MISURASICUREZZA_CUMULO;
	}
}