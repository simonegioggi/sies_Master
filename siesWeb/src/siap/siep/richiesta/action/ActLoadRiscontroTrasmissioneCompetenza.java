package siap.siep.richiesta.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;

/**
 * <p>
 * Title: ActLoadRiscontroTrasmissioneCompetenza
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActLoadRiscontroTrasmissioneCompetenza extends ActionSiap implements ICostantiRichiesta {

	public String processRequest() throws Exception {

		// SE ESISTE TOLGO DALLA SESSIONE L'ID DELL'EVENTO SOLLECITO
		if (!isSessionAttributeNullObj(FIELD_TEMP_ID_EVENTO_SOLLECITO))
			this.removeSessionAttribute(FIELD_TEMP_ID_EVENTO_SOLLECITO);

		BigDecimal lIdMessage = null;

		if (!isRequestParameterNullObj(ICostantiMessaggio.CAMPO_ID_MESSAGGIO)) {
			lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
		} else if (!this.isRequestAttributeNullObj(ICostantiMessaggio.CAMPO_ID_MESSAGGIO)) {
			lIdMessage = (BigDecimal) this.getRequestAttribute(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
		}

		MessaggioModel lMess = null;
		if (lIdMessage == null) {
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"ERRORE NELLA RICEZIONE DEL MESSAGGIO. Non è stato possibile recuperare l'idMessaggio!");
		}

		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		lMess = lCrtl.ExRicercaMessaggioByKey(lIdMessage);

		if (lMess == null) {
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"ERRORE NELLA RICEZIONE DEL MESSAGGIO. Non è stato possibile recuperare il Messaggio con id = "
							+ lIdMessage);
		}

		// Recupera il messaggio di trasmizzione atti solo se NON è messaggio tipo COMUNICAZIONE
		// (tipo_operazione = 00079)
		MessaggioModel lTrasmCompetenzaMsg = null;

		if (!"00079".equals(lMess.getCodTipoOperazione())) {
			if (lMess.getJmsCorrelationIdMessage() != null) {
				lTrasmCompetenzaMsg = lCrtl
						.ExRicercaMessaggioByKey(new BigDecimal(lMess.getJmsCorrelationIdMessage()));
			}
		}

		this.setRequestAttribute("MessaggioEsito", lMess);
		this.setRequestAttribute("MessaggioTrasm", lTrasmCompetenzaMsg);
		// siesLogger.debug("--XX-- MessaggioEsito = "+lMess);
		// siesLogger.debug("--XX-- MessaggioTrasm = "+lTrasmCompetenzaMsg);

		// ricerco il fascicolo INVIATO (???????) (Fasc. CUMULATO)
		FascicoloSiepModel aFas = new FascicoloSiepModel();
		aFas.setChiaveAnno(lMess.getChiaveAnnoSiep());
		aFas.setChiaveProgr(lMess.getChiaveProgrSiep());
		aFas.setChiaveUfficio(lMess.getChiaveUfficioSiep());

		IFascicoloSiep lCrtlFas = SIEPLookupRemote.getFascicoloSiepRemote();
		FascicoloSiepModel mFas = lCrtlFas.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(aFas);

		if (mFas == null)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"ERRORE. Non è stato possibile recuperare gli estremi del procedimento trasmesso. !");

		// pena residua per dettaglio fascicolo
		IPenaResidua lCrtlP = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel mPena = lCrtlP
				.ExRicercaPenaResiduaCorrenteByFascicoloSiep(mFas.getFasSieIdFascicoloSiep());
		this.setRequestAttribute("penaresiduaInviata", mPena);
		this.setRequestAttribute("fascicoloInviato", mFas);

		// mi serve anche in sessione !!! non so perchè
		// --------------------------------------
		setSessionAttribute("fascicolo", mFas);
		// --------------------------------------

		// ============================================================
		// Cerco Solleciti By IdMessaggioSollecitato
		// ============================================================
		Vector<MessaggioModel> lVecSoll = null;

		if ("00067".equals(lMess.getCodTipoOperazione())) {
			IMessaggio CtrlMess = JMSLookupRemote.getMessaggioRemote();
			lVecSoll = new Vector<>(CtrlMess.ExRicercaMessaggioByIdMessaggioSollecitato("00068",
					"" + lMess.getIdMessaggio()));

			// La Ricerca Solleciti è Ordinata in modo decrescente;
			if (lVecSoll != null && lVecSoll.size() > 0) {
				lMess.setMessaggiSollecito(lVecSoll);
			}
		}

		setRequestAttribute("solleciti", lVecSoll);

		return PG_DETTAGLIO_RISCONTRO_TRASMISSIONE_COMPETENZA;
	}

}