package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.ProcedimentoCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciProcedimentoCumulato
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di ProcedimentoCumulato
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadInserisciProcedimentoCumulato extends ActionModuloCumulo
		implements ICostantiProcedimentoCumulato {
	/*****************************************************************************
	 * Azione di caricamento della pagina di Inserimento dei dati. Si occupa anche di precaricare tutti i dati
	 * da visualizzare i tale pagina (es: combo)
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// ==========================================================================
		// Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
		// di DettaglioTitoloCumulato.jsp
		// ==========================================================================
		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		String lModalita = "I";
		if (!isRequestParameterNullObj("modalita")) {
			lModalita = getRequestStringParameter("modalita");
		}

		ProcedimentoCumulatoModel lProcMod = null;
		if ("M".equals(lModalita)) {
			if (!isRequestParameterNullObj(CAMPO_ID_PROCEDIMENTO_CUMULATO)) {
				BigDecimal lIdProcedimentoCumulato = getRequestBigDecimalParameter(
						CAMPO_ID_PROCEDIMENTO_CUMULATO);

				ITitoloCumulato lCtrl = SIEPLookupRemote.getTitoloCumulatoRemote();
				lProcMod = lCtrl.ExRicercaProcedimentoCumulatoById(lIdProcedimentoCumulato);

				if ("S".equals(lProcMod.getFlagAccorpato())) {
					UfficioModel lUfficioOrigine = getUfficioByCodUfficio(lProcMod.getChiaveUfficioOrigine());
					lProcMod.setUfficioOrigine(lUfficioOrigine);
				}

				setRequestAttribute("procedimentoCumulato", lProcMod);

			}
		}

		// Inserire Eventuali ComboBOX
		Option lOptionAutorita = new Option(DecodificheManager.getInstance().getTipoAutoritaCumulo(), "-");
		String[] lFiltroAut = { "PM", "PGCAP", "PMM" };
		lOptionAutorita.setFilter(lFiltroAut);
		if (lProcMod != null)
			lOptionAutorita.setSelected(lProcMod.getCodTipoUfficioFasCumulato());
		setRequestAttribute("autoritaCumulo", "" + lOptionAutorita);

		// ==========================================================================
		// Caricamento dati per gestione uffici accorpati
		// ==========================================================================
		IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();
		Vector lUffAccTotali = lUffCtrl.ListaUfficiAccorpati("PM", null);
		setRequestAttribute("ufficiAccorpati", lUffAccTotali);

		// Imposta la Modalità a Inserimento.
		setRequestAttribute("modalita", lModalita);

		// Restituisce la pagina di Inserimento dei Dati
		return PG_LOAD_INSERISCIPROCEDIMENTOCUMULATO;
	}
}