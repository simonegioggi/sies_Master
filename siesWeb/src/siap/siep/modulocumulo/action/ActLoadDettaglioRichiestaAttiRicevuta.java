package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.ufficio.model.UfficioAccorpatoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.ufficio.util.UfficioAccorpatoUtils;
import siap.sico.web.ActionSiap;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DatiSiepPerTrasferimentoModel;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;

public class ActLoadDettaglioRichiestaAttiRicevuta extends ActionSiap implements ICostantiModuloCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException, Exception {

		// =======================================
		// Recupero il messaggio da visualizzare
		// =======================================
		BigDecimal lIdMessage = null;
		if (!isRequestParameterNullObj(ICostantiMessaggio.CAMPO_ID_MESSAGGIO)) {
			lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
		} else if (!this.isRequestAttributeNullObj(ICostantiMessaggio.CAMPO_ID_MESSAGGIO)) {
			lIdMessage = (BigDecimal) this.getRequestAttribute(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
		}

		MessaggioModel lMessModel = new MessaggioModel();
		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		if (lIdMessage != null) {
			lMessModel = lCrtl.ExRicercaMessaggioByKey(lIdMessage);
		}

		siesLogger.debug("lMessModel.getIsErroreParser() = " + lMessModel.getIsErroreParser());

		Vector<MessaggioModel> lVectSolleciti = new Vector();

		// Cerco eventuali solleciti
		if (lMessModel != null && lMessModel.getIdMessaggio() != null) {
			// Caso di Solleciti provenienti da stessa BDI
			lVectSolleciti = lCrtl.ExRicercaSollecitiMessaggioRichiestaAtti(ICostantiJMS.RICHIESTA,
					ICostantiJMS.SOLLECITO_RICHIESTA_TRASMISSIONE_ATTI_PER_COMP,
					lMessModel.getIdMessaggio().toString());
			if (lVectSolleciti != null && lVectSolleciti.size() > 0) {
				lMessModel.setMessaggiSollecito(lVectSolleciti);
				siesLogger.debug("Trovati n. " + lVectSolleciti.size() + " solleciti da stessa BDI");
			} else {
				// Caso di Solleciti provenienti da Diverse BDI
				lVectSolleciti = lCrtl.ExRicercaSollecitiMessaggioRichiestaAtti(ICostantiJMS.RICHIESTA,
						ICostantiJMS.SOLLECITO_RICHIESTA_TRASMISSIONE_ATTI_PER_COMP,
						lMessModel.getJmsCorrelationIdMessage());
				if (lVectSolleciti != null && lVectSolleciti.size() > 0) {
					lMessModel.setMessaggiSollecito(lVectSolleciti);
					siesLogger.debug("Trovati n. " + lVectSolleciti.size() + " solleciti da altra BDI");
				}

			}

		}

		siesLogger.debug("lMessModel = " + lMessModel);

		if (lMessModel.getTreeModel() != null) {
			// ======================================
			// Parsing del messaggio
			// ======================================
			ParserMessage lParser = null;
			if (lMessModel != null) {
				lParser = new ParserMessage(lMessModel.getTreeModel());
			}

			// ================================================================
			// Passo il DettaglioFascicoloModel alla jsp di visualizzazione
			// ================================================================
			DettaglioFascicoloModel lDettaglioFasModel = null;
			if (lParser != null && lParser.getDettaglioFascicoloSiep() != null)
				lDettaglioFasModel = lParser.getDettaglioFascicoloSiep();
			else
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Errore nella Ricezione del Procedimento. <BR>Rivolgersi all'amministratore di sistema! ");

			this.setRequestAttribute("dettaglioFasSIEP", lDettaglioFasModel);
			this.setRequestAttribute("Messaggio", lMessModel);

			// Cerco il record COMPETENZA da cui estrarre i dati del Procedimeto/Titolo
			// Richiesto
			// Vector lListaEventi = (Vector) lDettaglioFasModel.getEventi();
			// for (int i = 0; i< lListaEventi.size(); i++){
			// EventoNotificaModel lEventoNot = (EventoNotificaModel) lListaEventi.elementAt(i);
			// EventoModel lEvento = lEventoNot.getEvento();
			// if ( "01".equals(lEvento.getCodTipoEvento())
			// && "26".equals(lEvento.getCodTipoProvvedimento())
			// && "0340".equals(lEvento.getCodMotivo())
			// )
			// {
			// siesLogger.debug("Trovata richiesta trasmissione = "+lEvento);
			// ricerco la COMPETENZA
			DatiSiepPerTrasferimentoModel lDSPT = lDettaglioFasModel.getDatiSiepPerTrasferimento();
			Vector lListaCompetenze = (Vector) lDSPT.getListCompetenze();

			CompetenzaModel lUltimaCompetenza = null;
			if (lListaCompetenze != null && !lListaCompetenze.isEmpty())
				lUltimaCompetenza = (CompetenzaModel) lListaCompetenze.lastElement();

			siesLogger.debug("lUltimaCompetenza = " + lUltimaCompetenza);
			this.setRequestAttribute("Competenza", lUltimaCompetenza);

			if (lUltimaCompetenza.getChiaveAnno() != null && lUltimaCompetenza.getChiaveProgr() != null) {
				UfficioAccorpatoModel lUfficioAccorpato = null;

				UfficioAccorpatoUtils lUffAccorpUtils = new UfficioAccorpatoUtils();
				lUfficioAccorpato = lUffAccorpUtils.getUfficioAccorpatoByCodAccorpanteProgr(
						lUltimaCompetenza.getChiaveUfficio(), lUltimaCompetenza.getChiaveProgr());

				if (lUfficioAccorpato != null) {
					this.setRequestAttribute("UfficioOld",
							getUfficioByCodUfficio(lUfficioAccorpato.getCodUfficio()));

					this.setRequestAttribute("UfficioAccorpatoOrigine", lUfficioAccorpato);
				}
			}

			// Controllo che il procedimento richiesto sia effettivamente di questo ufficio
			// e sia presente a sistema.
			// FascicoloSiepModel lFascicolo = lDettaglioFasModel.getFascicoloSiep();

			// Attenzione Il richiedente potrebbe non aver specificato il numero
			// di procedimento (anno e numero) in quanto non noto, ma solo i
			// dati del titolo.
			if (!lUltimaCompetenza.getChiaveUfficio().equals(this.getCodUfficioUtenteConnesso())) {
				// Il procedimento richiesto non appartiene a questo ufficio
				UfficioModel lUfficio = getUfficioByCodUfficio(lUltimaCompetenza.getChiaveUfficio());
				this.setRequestAttribute("alertMsg",
						"Attenzione! Il procedimento richiesto (" + lUltimaCompetenza.getChiaveAnno() + "/"
								+ lUltimaCompetenza.getChiaveProgr() + " " + lUfficio.getDescrTipoUfficio()
								+ " " + lUfficio.getDescrComune() + ") non appartiene a questo Ufficio");
			} else if (lUltimaCompetenza.getChiaveAnno() != null
					&& lUltimaCompetenza.getChiaveProgr() != null) {
				// Ricerco il fascicolo
				FascicoloSiepModel lFascRich = new FascicoloSiepModel();
				lFascRich.setChiaveAnno(lUltimaCompetenza.getChiaveAnno());
				lFascRich.setChiaveProgr(lUltimaCompetenza.getChiaveProgr());
				lFascRich.setChiaveUfficio(lUltimaCompetenza.getChiaveUfficio());

				IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
				FascicoloSiepModel lFasRet = lCtrl.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFascRich);

				if (lFasRet == null) {
					// Il procedimento richiesto non esiste
					this.setRequestAttribute("alertMsg",
							"Attenzione! Il procedimento richiesto (" + lFascRich.getChiaveAnno() + "/"
									+ lFascRich.getChiaveProgr() + ") non è presente su questo Ufficio");
				}
			} else {
				// this.setRequestAttribute("alertMsg", "Attenzione! L'ufficio richiedente non ha indiato gli
				// estremi del procedimento di esecuzione del titolo!");
			}

			// }
			// }
		} else {
			// Il DAO non è riuscito a deserializzare il blob. Al 99% è cambiata la
			// versione SIEP.

			this.setRequestAttribute("Messaggio", lMessModel);
			this.setRequestAttribute("isErrParser", "SI");
		}

		// ===========================================================================================================================
		// Se il messaggio di RICHIESTA ATTI (Cod=00075) ha il Codice_esito valorizzato a 01006 (TRASMESSO),
		// si tratta di messaggio cui ha fatto seguito una trasmissiome ATTI.
		// In questo caso nella form di dettaglio NON ci devono essere campi INPUT Type o Bottoni di Ricerca;
		// Alcune informazioni potrebbero trovarsi nel messaggio di TRASMISSIONE ATTI (Cod=00066) collegato
		// alla RICHIESTA corrente.
		// ===========================================================================================================================
		MessaggioModel lMess00066 = new MessaggioModel();

		if (lMessModel != null && "01006".equals(lMessModel.getCodEsito())) {
			String aTipoMes = "01";
			String aTipoOperazione = "00066";
			String aCodUffMittenete = this.getCodUfficioUtenteConnesso();

			// BigDecimal aIdRichiesta= lMessModel.getIdMessaggio();
			BigDecimal aIdRichiesta = new BigDecimal(lMessModel.getJmsCorrelationIdMessage());

			lMess00066 = lCrtl.ExRicercaMessaggioByIdRichiesta(aTipoMes, aTipoOperazione, aCodUffMittenete,
					aIdRichiesta);
			this.setRequestAttribute("MessTrasmissione", lMess00066);
		}

		return PG_DETTAGLIO_RICHIESTA_ATTI_RICEVUTA;
	}

}