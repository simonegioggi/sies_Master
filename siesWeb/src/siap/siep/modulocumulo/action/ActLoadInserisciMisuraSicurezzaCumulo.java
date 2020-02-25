package siap.siep.modulocumulo.action;

/**
* <p>Title: ActLoadInserisciMisuraSicurezzaCumulo</p>
* <p>Description: Classe Action per la load inserisci di MisuraSicurezzaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;

public class ActLoadInserisciMisuraSicurezzaCumulo extends ActionModuloCumulo
		implements ICostantiMisuraSicurezzaCumulo {
	/*****************************************************************************
	 * Azione di caricamento della pagina di Inserimento dei dati. Si occupa anche di precaricare tutti i dati
	 * da visualizzare i tale pagina (es: combo)
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

		// Imposta Modalità: MODALITA_INSERIMENTO = 'I' per INSERIMENTO, 'M' per MODIFICA, 'C' per
		// CANCELLAZIONE
		String lModalita = MODALITA_INSERIMENTO;

		setRequestAttribute(MODALITA, lModalita);

		// ==========================================================================
		// Caricamento Dati delle Combo
		// ==========================================================================
		Option lOptionN = new Option(DecodificheManager.getInstance().getNaturaMisuraSicurezza());
		setRequestAttribute("naturaMisuraSicurezza", "" + lOptionN);

		Vector lVec = (Vector) DecodificheManager.getInstance().getTipoMisuraSicurezza();
		setRequestAttribute("tipoMisuraSicurezza", lVec);

		Option lOptionS = new Option(DecodificheManager.getInstance().getStatoMisuraSicurezzaCumulo());
		lOptionS.setSelected("V"); // preseleziono lo stato Valida
		setRequestAttribute("StatoMisuraSicurezza", "" + lOptionS);

		Option lOptionAutorita = new Option(DecodificheManager.getInstance().getTipoUfficio(), "-");
		String[] lFiltroAut = { "PM", "PGCAP", "PMM" };
		lOptionAutorita.setFilter(lFiltroAut);
		setRequestAttribute("autoritaCumulo", "" + lOptionAutorita);

		// Restituisce la pagina di Inserimento dei Dati
		return PG_LOAD_INSERISCI_MISURASICUREZZA_CUMULO;
	}
}