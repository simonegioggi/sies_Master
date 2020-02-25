package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.util.SIEPLookupRemote;

public class ActDettaglioRichiestaAttiCumulo extends ActionModuloCumulo
		implements ICostantiModuloCumulo, ICostantiMessaggio {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// ==========================================================================
		// Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
		// di DettaglioTitoloCumulato.jsp
		// ==========================================================================
		// IstruttoriaCumuloModel IstruModel = null;
		if (!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)
				&& getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) != null
				&& !getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)
						.equals(null)) {
			/* IstruModel = (IstruttoriaCumuloModel) */super.getDatiIstruttoria();
		}

		BigDecimal lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

		super.setLinkRitorno();
		MessaggioModel lMessaggioRisposta0066 = null;
		MessaggioModel lMessaggioRisposta0077 = new MessaggioModel();
		Vector<MessaggioModel> listaSolleciti = null;
		String lRisposta = "";

		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		MessaggioModel lMessaggioRichiesta = lCrtl.ExRicercaMessaggioByKey(lIdMessage);

		// Verifico se è arrivata la risposta:
		if (lMessaggioRichiesta != null && lMessaggioRichiesta.getIdMessaggio() != null) {
			/// Provo a Cercare un messaggio di risposta di tipo 00066 : Richiesta Accettata
			lMessaggioRisposta0066 = (MessaggioModel) lCrtl.ExRicercaMessaggioByIdRichiesta(
					ICostantiJMS.RICHIESTA, ICostantiJMS.TRASFERIMENTO_COMPETENZA,
					lMessaggioRichiesta.getCodUfficioDestinatario(), lMessaggioRichiesta.getIdMessaggio());
			if (lMessaggioRisposta0066 != null && lMessaggioRisposta0066.getIdMessaggio() != null) {
				if (lMessaggioRisposta0066.getCodEsito() != null
						&& lMessaggioRisposta0066.getCodEsito().compareTo("01007") == 0) {
					lRisposta += "Atti Ricevuti e Restituiti il ";
					lRisposta += DateUtils.getDateToString(lMessaggioRisposta0066.getDataEsito(),
							"dd-MM-yyyy HH:mm");
					lMessaggioRichiesta.setRapportoEsito(lRisposta);

					lMessaggioRichiesta.setCodEsito("01007");
					lMessaggioRichiesta.setDescrEsito("ATTI RICEVUTI E RESTITUITI");
					lMessaggioRichiesta.setFlagVisto("S");
				} else if (lMessaggioRisposta0066.getCodEsito() != null
						&& lMessaggioRisposta0066.getCodEsito().compareTo("01003") == 0) {
					lRisposta += "Atti Presi in Carico e Restituiti il ";
					lRisposta += DateUtils.getDateToString(lMessaggioRisposta0066.getDataEsito(),
							"dd-MM-yyyy HH:mm");
					lMessaggioRichiesta.setRapportoEsito(lRisposta);

					lMessaggioRichiesta.setCodEsito("01003");
					lMessaggioRichiesta.setDescrEsito("ATTI RESTITUITI dopo PRESA in CARICO");
					lMessaggioRichiesta.setFlagVisto("S");
				} else if (lMessaggioRisposta0066.getCodEsito() != null
						&& lMessaggioRisposta0066.getCodEsito().compareTo("01001") == 0) {
					lRisposta += "Atti Ricevuti e Presi in Carico il ";
					lRisposta += DateUtils.getDateToString(lMessaggioRisposta0066.getDataEsito(),
							"dd-MM-yyyy HH:mm");
					lMessaggioRichiesta.setRapportoEsito(lRisposta);

					lMessaggioRichiesta.setRapportoEsito(lRisposta);
					lMessaggioRichiesta.setCodEsito("01001");
					lMessaggioRichiesta.setDescrEsito("ATTI RICEVUTI E PRESI in CARICO");
					lMessaggioRichiesta.setFlagVisto("S");
				} else {
					lMessaggioRichiesta.setRapportoEsito("Atti Ricevuti");
					lMessaggioRichiesta.setCodEsito("01006");
					lMessaggioRichiesta.setDescrEsito("TRASMESSO");
					lMessaggioRichiesta.setFlagVisto("S");
				}

				// Cerco Eventuali Solleciti prima della Richiesta Accettata
				listaSolleciti = lCrtl.ExRicercaSollecitiMessaggioRichiestaAtti(ICostantiJMS.RICHIESTA,
						ICostantiJMS.SOLLECITO_RICHIESTA_TRASMISSIONE_ATTI_PER_COMP,
						lMessaggioRichiesta.getIdMessaggio().toString());
				if (listaSolleciti != null && listaSolleciti.size() > 0) {
					siesLogger.debug("Trovati solleciti prima di Risposta a "
							+ lMessaggioRichiesta.getDescrUfficioDestinatario() + " di "
							+ lMessaggioRichiesta.getDescrSedeUfficioDestinatario() + " per id Mess "
							+ lMessaggioRichiesta.getIdMessaggio());

					lMessaggioRichiesta.setMessaggiSollecito(listaSolleciti);
				}
			} else {
				// Provo a Cercare un messaggio di risposta di tipo 00077 : Richiesta Rigettata
				lMessaggioRisposta0077 = (MessaggioModel) lCrtl.ExRicercaMessaggioByIdRichiesta(
						ICostantiJMS.ESITO, ICostantiJMS.RIGETTO_RICHIESTA_TRASMISSIONE_ATTI_PER_COMP,
						lMessaggioRichiesta.getCodUfficioDestinatario(),
						lMessaggioRichiesta.getIdMessaggio());
				if (lMessaggioRisposta0077 != null && lMessaggioRisposta0077.getIdMessaggio() != null) {
					lMessaggioRichiesta.setRapportoEsito("Richiesta Rigettata");

					lMessaggioRichiesta.setCodEsito("01007");
					lMessaggioRichiesta.setDescrEsito("RIGETTATO");
					lMessaggioRichiesta.setFlagVisto("S");
				} else {
					// Se NON c'è una Risposta cerco Eventuali Solleciti
					listaSolleciti = lCrtl.ExRicercaSollecitiMessaggioRichiestaAtti(ICostantiJMS.RICHIESTA,
							ICostantiJMS.SOLLECITO_RICHIESTA_TRASMISSIONE_ATTI_PER_COMP,
							lMessaggioRichiesta.getIdMessaggio().toString());
					if (listaSolleciti != null && listaSolleciti.size() > 0) {
						siesLogger.debug(
								"Trovati solleciti a " + lMessaggioRichiesta.getDescrUfficioDestinatario()
										+ " di " + lMessaggioRichiesta.getDescrSedeUfficioDestinatario()
										+ " per id Mess " + lMessaggioRichiesta.getIdMessaggio());
						lMessaggioRichiesta.setMessaggiSollecito(listaSolleciti);
						lMessaggioRichiesta.setRapportoEsito("Inviato Sollecito");
					} else
						lMessaggioRichiesta.setRapportoEsito("In Attesa di Risposta...");

				} // Chiude Else lMessaggioRisposta0077

			} // Chiude Else lMessaggioRisposta0066

		} // Chiude if(lMessaggioRichiesta NOT null)

		setRequestAttribute("Messaggio", lMessaggioRichiesta);
		setRequestAttribute("MessaggioRisposta", lMessaggioRisposta0066);
		setRequestAttribute("MessaggioRigetto", lMessaggioRisposta0077);
		setRequestAttribute("listaSolleciti", listaSolleciti);

		// Passo alla form i dati identificativi degli atti trasmessi
		// - FascicoloSiep (Soggetto, Sentenza)
		if (lMessaggioRichiesta.getTreeModel() != null) {
			ParserMessage lParser = new ParserMessage(lMessaggioRichiesta.getTreeModel());

			this.setRequestAttribute("fascicolo", lParser.getDettaglioFascicoloSiep().getFascicoloSiep());
			this.setRequestAttribute("penaresidua", lParser.getDettaglioFascicoloSiep().getPenaResidua());
		} else {
			siesLogger.warn(
					"ERRORE nel Parsing del BLOB messaggio trasmesso per possibile errore Versione classe");
			// Se il DAO non è riuscito a deserializzare il blob, rovo a recuperare i
			// dati direttamente dal DB
			FascicoloSiepModel lFasMod = new FascicoloSiepModel();

			lFasMod.setChiaveUfficio(lMessaggioRichiesta.getChiaveUfficioSiep());
			lFasMod.setChiaveProgr(lMessaggioRichiesta.getChiaveProgrSiep());
			lFasMod.setChiaveAnno(lMessaggioRichiesta.getChiaveAnnoSiep());

			IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
			FascicoloSiepModel lFasRet = lCtrl.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFasMod);

			if (lFasRet != null) {
				this.setRequestAttribute("fascicolo", lFasRet);
			} else {
				// Messaggio di errore e ritorno sulla maschera elenco risultati ricerca
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Non è possibile visualizzare il dettaglio del Messaggio in quanto inviato con una versione SIEP differente da quella attualmente in uso.");

				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + super.goToRitorno());

				return IWebConstants.PG_MESSAGE;
			}
		}

		return PG_DETTAGLIO_RICHIESTA_ATTI_CUMULO;
	}

}