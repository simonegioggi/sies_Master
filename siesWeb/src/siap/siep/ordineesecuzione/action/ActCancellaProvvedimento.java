package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.sanzionesostitutiva.controller.IPeriodoAltraSanzione;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.util.SIUSLookupRemote;
import siap.web.ISIAPCostantiWeb;

/**
 * <p>
 * Title: ActCancellaProvvedimento
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Scadenzario
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
public class ActCancellaProvvedimento extends ActionSiap implements ICostantiOrdineEsecuzione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// String validato = getRequestStringParameter("docRegistrato");
		String motivazioni = null;
		BigDecimal lId = getRequestBigDecimalParameter("IdEvento");

		// riempie il model
		EventoModel lEveModRic = new EventoModel();
		CampoNotaModel lCampoMod = new CampoNotaModel();
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();

		IOrdineEsecuzione lCtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		lEveModRic = lCtrlEvento.ExRicercaEventoByKey(lId);

		if (!isRequestParameterNullObj("lOrdinamento"))
			setRequestAttribute("lOrdinamento", getRequestStringParameter("lOrdinamento"));

		// Nel caso l'evento da cancellare/annullare sia un Ordine di Esecuzione
		// e sia l'unico per quel fasciolo deve inviare un messaggio all'utente
		boolean lMessaggioOE = false;
		// Serve per distinguere nel messaggio utente "per questa"/"per altra" causa
		String lCausaPosizione = "questa";
		if (lEveModRic != null
				// OE - Detenuto per questa causa
				&& ("0058".equals(lEveModRic.getCodMotivo())
						// OE - Detenuto per altra causa
						|| "0059".equals(lEveModRic.getCodMotivo())
						// OE - Arresti domiciliari
						|| "0060".equals(lEveModRic.getCodMotivo()))) {
			if (!isRequestParameterNullObj("lOrdinamento"))
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lOrdinamento : " + getRequestStringParameter("lOrdinamento"));

			if (!isRequestParameterNullObj("nextAction"))
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("nextAction : " + getRequestStringParameter("nextAction"));

			if (!isRequestParameterNullObj("nonValidati"))
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("nonValidati : " + getRequestStringParameter("nonValidati"));

			// Controlla che sia l'unico per quel fascicolo
			EventoModel lEveModRicOE = new EventoModel();

			lEveModRicOE.setFasSieIdFascicoloSiep(lEveModRic.getFasSieIdFascicoloSiep());
			lEveModRicOE.setCodTipoEvento("01");
			lEveModRicOE.setFlagDocumentoRegistrato("NOANN"); // Per non selezionare gli eventi annullati

			String[] lCodTipoProvv = { "06", "06", "06" };
			String[] lCodMotivi = { "0058", "0059", "0060" };

			List lEventiOE = lCtrlEvento.ExRicercaEventoTipoProvTipoMot(lEveModRicOE, lCodTipoProvv,
					lCodMotivi);

			if (lEventiOE != null && (lEventiOE.size() == 1)) {
				// OE - Detenuto per altra causa
				if ("0059".equals(lEveModRic.getCodMotivo()))
					lCausaPosizione = "altra";
				lMessaggioOE = true;
			}
		}

		if (lEveModRic != null) {
			if (lEveModRic.getFlagDocumentoRegistrato() == null
					|| (lEveModRic.getFlagDocumentoRegistrato().equals("")
							|| lEveModRic.getFlagDocumentoRegistrato().equals("N"))) {
				// provvedimenti non validati, cancellazione fisica
				lCtrl.ExCancellaEventoConStoreProcedure(lEveModRic);
			} else {
				// provvedimenti validati, in questo caso c'e' una cancellazione logica
				motivazioni = getRequestStringParameter("motivazioni");
				lEveModRic.setFlagDocumentoRegistrato("A");
				lCampoMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				lCampoMod.setCodOperatoreInserimento(getCodUtenteConnesso());
				lCampoMod.setDataInserimento(DateUtils.getSysDate());
				lCampoMod.setEveIdEvento(lId);
				lCampoMod.setDescr(motivazioni);
				lCtrl.ExAggiornaEventoInserisciCampoNota(lEveModRic, lCampoMod);
				// metto in session il fascicolo aggiornato
				// Recupero il Fascicolo Siep dalla Sessione
				IFascicoloSiep lCtrlFasSie = SIEPLookupRemote.getFascicoloSiepRemote();
				FascicoloSiepModel lFasMod = lCtrlFasSie
						.ExRicercaFascicoloByKey(lEveModRic.getFasSieIdFascicoloSiep());
				setSessionAttribute("fascicolo", lFasMod);
			}
			// In caso di provvedimento di LA, dopo la cancellazione o aggiornamento dell'evento
			// occorre resettare il flag elaborato dell'eventuale ordinanza associata in SIUS
			if (lEveModRic.getEveIdEvento() != null) {
				DepositoOrdinanzaPcModel DepOrdSius = new DepositoOrdinanzaPcModel();
				IDepositoOrdinanzaPc ctrDepOrdSius = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
				DepOrdSius = ctrDepOrdSius.ExRicercaDepositoOrdinanzaPcByEvento(lEveModRic.getEveIdEvento());
				if (DepOrdSius != null && DepOrdSius.getCodTipoOrdinanza() != null
						&& DepOrdSius.getCodTipoOrdinanza().compareTo("LA") == 0) {
					DepOrdSius.setFlagElaborato(null);
					DepOrdSius = ctrDepOrdSius.ExModificaDepositoOrdinanzaPc(DepOrdSius);
				}
			}
			// Ticket#20190723011 — Anomalia fascicolo SIEP: 20190725 [SG]
			// Durante la concessione dell'affidamento in prova si può creare un SIUS manualmente
			// Alla cancellazione del SIEP il SIUS rimane appeso
			if (Utils.isPresent(lEveModRic.getEveIdEvento())) {
				EventoModel em = lCtrlEvento.ExRicercaEventoByKey(lEveModRic.getEveIdEvento());
				if (em != null
						&& ("02".equals(em.getCodTipoProvvedimento())
								|| "03".equals(em.getCodTipoProvvedimento()))
						&& "01".equals(em.getCodTipoEvento()) && em.getDataTrasmissioneAtti() != null
						&& em.getDataTrasmissioneAtti().compareTo(em.getDataEmissione()) == 0
						&& em.getCodOperatoreInserimento().equals(lEveModRic.getCodOperatoreInserimento())
						// Ticket#20230718019 - In caso di Annullamento di un verbale di sottoscrizione non si annulla 
						//                      anche l'ordinanza in quanto puntata anche dalla richiesta del verbale
						//                       
						&& !(   lEveModRic.getCodTipoEvento().equals("07")   // 07-Verbale
							 && (   lEveModRic.getCodTipoProvvedimento().equals("16") // 16-Verbale
							     || lEveModRic.getCodTipoProvvedimento().equals("18") // 18-Verbale obblighi
							    )
							 && (   lEveModRic.getCodMotivo().equals("0314") // 0314-sottoscrizione obblighi
								 || lEveModRic.getCodMotivo().equals("0312") // 0312-verbale
								)
							)
						// Ticket#20230718019 - FINE
					) 
				{
					em.setFlagDocumentoRegistrato("A");
					lCtrl.ExAggiornaEventoInserisciCampoNota(em, lCampoMod);
				}
			}
		}

		String lPage;
		String lAzione = "";

		// Stub 03/03/2006 si consente di attivare con un parametro la prossima azione da eseguire.
		if (!isRequestParameterNullObj("nextAction")) {
			if (!isRequestParameterNullObj("ActRicercaProvvedimentiCancellati"))
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" caso 1 dell'if ");
			lAzione = getRequestStringParameter("nextAction");
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "="
					+ getRequestStringParameter("nextAction");
		} else if (!isRequestParameterNullObj("nonValidati")) {
			if (!isRequestParameterNullObj("ActRicercaProvvedimentiCancellati"))
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" caso 2 dell'if ");
			lAzione = "siap.sico.evento.action.ActRicercaProvvedimentiNonValidati";
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.sico.evento.action.ActRicercaProvvedimentiNonValidati";
		} else {
			if (!isRequestParameterNullObj("ActRicercaProvvedimentiCancellati"))
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" caso 3 dell'if ");
			lAzione = "siap.siep.ordineesecuzione.action.ActRicercaProvvedimentiCancellati";
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.ordineesecuzione.action.ActRicercaProvvedimentiCancellati";
		}

		// 19/05/2008 Ricerca il Periodo Altra Sanzione tramite l'ID dell'evento
		IPeriodoAltraSanzione lCtrlPAS = SIUSLookupRemote.getPeriodoAltraSanzioneRemote();
		PeriodoAltraSanzioneModel mPerAlSanz = lCtrlPAS.ExRicercaSanzioneSostitutivaByIdEvento(lId);

		// Nel caso di annullamento provvedimento SIUS si utilizza LINK_RITORNO
		if (!isRequestParameterNullObj(IWebConstants.LINK_RITORNO)) {
			// 19/05/2008 Diversificazione del messaggio in caso di presenza Periodo Altra Sanzione.
			if (mPerAlSanz != null && mPerAlSanz.getIdPeriodoAltraSanzione() != null)
				lPage = ritornoDopoCancellazione(
						"Provvedimento Annullato! <br><br> Attenzione: esistenza di periodo sanzione sostitutiva! <br> Verificare durata sanzione con la specifica funzione!",
						null);
			else
				lPage = ritornoDopoCancellazione("Provvedimento Annullato!", null);
		}

		if (lMessaggioOE == true) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction(lAzione);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Attenzione, se si vuole emettere un OE con altra posizione giuridica (es. libero) occorre prima <U>cancellare</U> la posizione giuridica detenuto per "
							+ lCausaPosizione
							+ " causa e poi inserire la posizione giuridica libero. Se sono state inserite delle misure cautelari errate occorre prima cancellarle e reimmettere quelle giuste.");
			setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, "" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		return lPage;
	}

}