package siap.sige.impugnazione.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.template.util.UtilTemplate;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.impugnazione.controller.IImpugnazioneSige;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

public class ActLoadDettaglioImpugnazioneSige extends ActionSige implements ICostantiImpugnazioneSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");
		this.setLinkRitorno();
		IImpugnazioneSige lCtrl = SIGELookupRemote.getImpugnazioneSigeRemote();
		ImpugnazioneSigeModel impugnazione = lCtrl
				.ExRicercaImpugnazioneByKey(getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE));
		if (impugnazione == null && !super.isRequestParameterNullEmptyObj(IWebConstants.FLAG_RITORNO)) {
			RedirectTo lPage = new RedirectTo();
			lPage.setPage(IWebConstants.PG_MAIN);
			lPage.setAction("siap.sige.impugnazione.action.ActLoadGrigliaMenuImpugnazioni");
			return lPage.toString();
		}

		// @emma 12072018 intervento post COLLAUDO 11.2
		if (impugnazione != null && impugnazione.getFlagValidazioneEsito() != null) {
			setRequestAttribute("validazioneEsito",
					"S".equals(impugnazione.getFlagValidazioneEsito())
							? impugnazione.getFlagValidazioneEsito()
							: "N");
		}

		setRequestAttribute("impugnazione", impugnazione);

		ProvvedimentoSigeEventoModel lPSMod = impugnazione.getProvvedimentoSige();
		gestioneTemplateImpugnazioneSige(impugnazione);
		setRequestAttribute("provvedimento", lPSMod);

		// Modifica del 08/03/2017 *** INIZIO ***
		// Verifico se sul Fascicolo in sessione è stato inserito un ricorso
		// a cui è stato collegato un nuovo procedimento. In caso affermativo
		// quando visualizzo il dettaglio del Ricorso non visualizzo i pulsanti
		// "Iscrizione Procedimento Collegato" e "Fissazione Udienza".
		IFascicoloSige lCtrlFascSige = SIGELookupRemote.getFascicoloSigeRemote();
		// 20190530 [SG]: errore se si proviene da Monitoraggio/Ricerche/Estrazione Dati » Ricerca
		// Procedimenti con Ricorso/Opposizione
		FascicoloSigeEstesoModel lFasEst = null;
		if (!isSessionAttributeNullObj("FascicoloSigeEsteso"))
			lFasEst = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");
		FascicoloSigeModel lFascSigeOrigine = null;
		// MAC 2017/04/21 Inizio
		String fascicoloCollegato = "N";

		if (lFasEst != null && lFasEst.getFascicoloSige() != null
				&& lFasEst.getFascicoloSige().getIdFascicoloSige() != null) {
			lFascSigeOrigine = lCtrlFascSige
					.ExRicercaFascicoloCollegato(lFasEst.getFascicoloSige().getIdFascicoloSige());
		}

		if (lFascSigeOrigine != null) {
			fascicoloCollegato = "S";
		}
		setRequestAttribute("fascicoloCollegato", fascicoloCollegato);
		// MAC 2017/04/21 Fine
		// Modifica del 08/03/2017 *** FINE ***

		String showComboTemplate = "true";
		if (!super.isRequestParameterNullEmptyObj(CAMPO_SHOW_COMBO_TEMPLATE))
			showComboTemplate = super.getRequestStringParameter(CAMPO_SHOW_COMBO_TEMPLATE);

		super.setRequestAttribute("showComboTemplate", showComboTemplate);
		super.setRequestAttribute("showDestinatari",
				String.valueOf(this.showDestinatariImpugnazioni(impugnazione)));
		super.setRequestAttribute("notifiche", impugnazione.getNotifiche());

		// MEV_65: aggiunto controllo preventivo
		if (!isRequestParameterNullObj(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE)) {
			// removeSessionAttribute("FascicoloSigeEsteso");
			IFascicoloSige ifs = SIGELookupRemote.getFascicoloSigeRemote();
			FascicoloSigeEstesoModel fsem = ifs.ExRicercaEstesaFascicoloSigeByKey(
					getRequestBigDecimalParameter(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE));
			setSessionAttribute("FascicoloSigeEsteso", fsem);
		}
		// FINE MEV_65

		super.setRequestAttribute("showDestinatariOpposizione",
				String.valueOf(this.showDestinatariImpugnazioniOpposizione(impugnazione)));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return PG_DETTAGLIOIMPUGNAZIONESIGE;
	}

	// Funzione per la costruzione della combo con i template di stampa previsti
	// private void gestioneTemplate(BigDecimal alIdEvento) throws Exception
	private void gestioneTemplateImpugnazioneSige(ImpugnazioneSigeModel impugnazione) throws Exception {

		Option lOptTemplate = null;
		String lCodTipoImpugnazione = COD_TIPO_RICORSO;
		String lCodProvvSige = "15";

		if (impugnazione.getCodTipoImpugnazione() != null
				&& !impugnazione.getCodTipoImpugnazione().equals("")) {
			if (impugnazione.getCodTipoImpugnazione().equals(COD_TIPO_OPPOSIZIONE)) {
				lCodProvvSige = "16";
			}
		} else {
			if (!isSessionAttributeNullObj("codTipoImpugnazione"))
				lCodTipoImpugnazione = (String) getSessionAttribute("codTipoImpugnazione");
			if (lCodTipoImpugnazione.equals(COD_TIPO_OPPOSIZIONE))
				lCodProvvSige = "16";
		}

		lOptTemplate = UtilTemplate.listaTemplateByCodProvvSige(lCodProvvSige);
		// MAC 2017/04/21 Inizio
		if (impugnazione.getCodTipoImpugnazione().equals(COD_TIPO_OPPOSIZIONE)) {
			// Quando non è presente l'esito visualizzo solo le Note di Trasmissione
			if (impugnazione.getCodTenoreDecisione() == null) {
				lOptTemplate.setFilter(new String[] { "SIGE_IM_002", "SIGE_IM_003", "SIGE_IM_005" });
			} else {
				if (impugnazione.getCodTenoreDecisione() != null
						&& (impugnazione.getCodTenoreDecisione().equals("05")
								|| impugnazione.getCodTenoreDecisione().equals("13"))) {
					// con esito = "Rigetto" o "Dichiara Inammissibile" la lista contiene solo la Nota
					// Opposizione
					lOptTemplate.setFilter(new String[] { "SIGE_IM_006" });
				}
			}
		}

		if (impugnazione.getCodTipoImpugnazione().equals(COD_TIPO_RICORSO)) {
			// Quando non è presente l'esito visualizzo solo le Note di Trasmissione
			if (impugnazione.getCodTenoreDecisione() == null) {
				lOptTemplate.setFilter(new String[] { "SIGE_IM_001", "SIGE_IM_004" });
			} else {
				if (impugnazione.getCodTenoreDecisione() != null
						&& (impugnazione.getCodTenoreDecisione().equals("05")
								|| impugnazione.getCodTenoreDecisione().equals("04"))) {
					// con esito = "Rigetto" o "Dichiara Inammissibile il ricorso" la lista contiene solo la
					// Nota Ricorso
					lOptTemplate.setFilter(new String[] { "SIGE_IM_007" });
				}
			}
		}
		// MAC 2017/04/21 Fine
		setRequestAttribute("ElencoTemplate", "" + lOptTemplate);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ElencoTemplate -> " + lOptTemplate);
		return;
	}

}