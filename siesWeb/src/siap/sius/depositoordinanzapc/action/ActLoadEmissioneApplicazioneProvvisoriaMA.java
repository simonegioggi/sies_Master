package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.Utils;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.action.ActLoadEmissioneDecreto;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

public class ActLoadEmissioneApplicazioneProvvisoriaMA extends ActLoadEmissioneDecreto {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		String lPage = super.processRequest();
		if (this.isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "fascicoloSiusGP non in sessione");

		// ExRicercaDepositoDecretoByIdEvento
		// Si aggiunge il controllo che il contenuto sia C050/C051
		// e che sia stato emesso un EVENTO con cod_tipo_provvedimento = 02, cod_esito = 0610 e
		// flag_documento_registrato = ‘S’

		FascicoloGPModel lFascicoloGPModel = new FascicoloGPModel(
				(FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));
		if (!"C050".equals(lFascicoloGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento())
				&& !"C051".equals(
						lFascicoloGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento())) {
			// throw new SIUSException(SIUSException.USER_MESSAGE, "Operazione non consentita per il Contenuto
			// del fascicolo");
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Operazione non consentita per il Procedimento di "
							+ lFascicoloGPModel.getGeneraleProcedimentoModel().getDescrOggettoProcedimento());
		}

		// Verifica esistenza di un deposito decreto per il fascicolo sius selezionato e tipo decreto
		BigDecimal idGP = lFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento();
		IDepositoDecreto idd = SIUSLookupRemote.getDepositoDecretoRemote();
		if (idd.ExVerificaEsistenzaDepositoDecretoByIdGenProcCodTipoDec(idGP,
				DECRETO_DESIGNAZIONE_MAGISTRATO_RELATORE_PER_MA)) {
			// Se già esiste un decreto viene chiamato il dettaglio.
			DepositoDecretoModel ddm = idd.ExRicercaDepositoDecretoByGenProc(idGP,
					DECRETO_DESIGNAZIONE_MAGISTRATO_RELATORE_PER_MA);

			// Ricerco l'evento legato al deposito decreto
			IEvento ie = SICOLookupRemote.getEventoRemote();
			EventoModel em = ie.ExRicercaEventoByKey(ddm.getIdEventoGenerato());
			String codEsito = !Utils.isNullObj(em.getCodEsito()) ? em.getCodEsito() : "";

			if (!"S".equals(em.getFlagDocumentoRegistrato()))
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Per il procedimento indicato non è stato emesso il provvedimento di designazione Magistrato Relatore");

			if (!"0610".equals(codEsito))
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Per il procedimento indicato non è stato emesso il provvedimento di designazione Magistrato Relatore!");

			if (!"22".equals(lFascicoloGPModel.getFascicoloSiusModel().getCodStatoFascicolo()))
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Stato Procedimento non coerente con l'emissione dell'Applicazione Provvisoria!");
		} else {
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Per il procedimento indicato non è stato emesso il provvedimento di designazione Magistrato Relatore!");
		}

		setRequestAttribute("isOrdProvvisoria", "SI"); // AM Ammissione Provvisoria
		// Verifica esistenza di un eventuale rigetto

		setRequestAttribute("flagOrdinanza", "ordinanza");

		siesLogger.debug("lPage = " + lPage);
		return lPage;
	}

}