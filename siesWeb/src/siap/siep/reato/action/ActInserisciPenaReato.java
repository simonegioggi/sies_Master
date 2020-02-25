package siap.siep.reato.action;

/**
* <p>Title: ActInserisciPenaReato</p>
* <p>Description: Classe Action per l'inserimento di Pena Reato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoModel;
import siap.siep.util.SIEPLookupRemote;

public class ActInserisciPenaReato extends ActionSiap implements ICostantiReato {

	/**
	 * Azione di Inserimento del Reato
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		// preparazione dei dati
		ReatoModel lReaMod = LetturaDati();

		ReatoModel lReaModRet = inserimento(lReaMod);

		// Prepara la pagina di destinazione
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.reato.action.ActLoadDettaglioPenaReato&" + CAMPO_ID_REATO + "="
				+ lReaModRet.getIdReato().toString();

		return lPage;
	}

	// Lettura dei dati dalla request e valorizzazione del ReatoModel
	protected ReatoModel LetturaDati() throws F3BException {
		// Si istanzia ReatoModel
		ReatoModel lReaMod = new ReatoModel();

		if (!this.isRequestParameterNullObj("lTipoFunzione")) // paramentro passato solo nel caso di
																// iscrizione guidata
		{
			this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
		}

		lReaMod.setIdReato(getRequestBigDecimalParameter(CAMPO_ID_REATO));
		lReaMod.setCodTipoPenaDetentiva(getRequestStringParameter(CAMPO_COD_TIPO_PENA_DETENTIVA));
		lReaMod.setNumAnni(getRequestBigDecimalParameter(CAMPO_NUM_ANNI));
		lReaMod.setNumMesi(getRequestBigDecimalParameter(CAMPO_NUM_MESI));
		lReaMod.setNumGiorni(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI));
		lReaMod.setNumAnniIsolamentoDiurno(getRequestBigDecimalParameter(CAMPO_ANNI_ISOLAMENTO_DIURNO));
		lReaMod.setNumMesiIsolamentoDiurno(getRequestBigDecimalParameter(CAMPO_MESI_ISOLAMENTO_DIURNO));
		lReaMod.setNumGiorniIsolamentoDiurno(getRequestBigDecimalParameter(CAMPO_GIORNI_ISOLAMENTO_DIURNO));
		lReaMod.setCodTipoSanzione(getRequestStringParameter(CAMPO_COD_TIPO_SANZIONE));

		////// MODIFICATO PER TENERE ALLINEATI I DATI DI TUTTE LE NORME
		// Carico tutte le norme
		lReaMod.setProgrReato(getRequestBigDecimalParameter(CAMPO_PROGR_REATO));
		lReaMod.setFasSieIdFascicoloSiep(getRequestBigDecimalParameter(CAMPO_FAS_SIE_ID_FASCICOLO_SIEP));

		if (getRequestStringParameter("SP_int") != null && !getRequestStringParameter("SP_int").equals("")) {
			if (getRequestStringParameter("Valuta").compareTo("LIT") == 0) {
				lReaMod.setSanzionePecuniaria(Utils.toEuro(getRequestStringParameter("SP_int")));
			} else {
				if (getRequestStringParameter("SP_int") != null)
					lReaMod.setSanzionePecuniaria(new BigDecimal(
							getRequestStringParameter("SP_int") + "." + getRequestStringParameter("SP_dec")));
			}
		} else {
			lReaMod.setSanzionePecuniaria(new BigDecimal(0));
		}
		return lReaMod;
	}

	/**
	 * Inserimento Pena Reato
	 * 
	 * @return
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	private ReatoModel inserimento(ReatoModel aReaMod) throws F3BException {

		IReato lCtrl = SIEPLookupRemote.getReatoRemote();
		Vector lNorme = lCtrl.ExRicercaReato(aReaMod);
		ReatoModel lReaModRet = lCtrl.ExModificaPenaReato(aReaMod, lNorme);

		////// FINE
		setRequestAttribute("reato", lReaModRet);

		return lReaModRet;
	}

}