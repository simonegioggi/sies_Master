package siap.sico.webservice.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.model.EventoModel;
import siap.sico.provvedimentisiesnsc.controller.IProvvSiesNsc;
import siap.sico.provvedimentisiesnsc.model.ProvvSiesNscModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.fascicolo.controller.FascicoloSiusController;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

@SuppressWarnings("rawtypes")
public class ActUDSTrasferimentoEsecuzione extends ActWsBase {

	BigDecimal lIdEvento, lIdDocAllegato;

	public String processRequest() throws Exception {

		lIdEvento = getRequestBigDecimalParameter("IdEvento");
		lIdDocAllegato = getRequestBigDecimalParameter("IdDocumentoAllegato");

		// Evento
		EventoModel lEvento = CercaEvento(lIdEvento);
		// Fascicolo SIUS
		FascicoloGPModel lFascicoloGPModel = CercaFascicoloSISU(lEvento.getFasSiuIdFascicoloSius());
		// Documento Allegato
		IDocumentoAllegato lCtrlDA = SIUSLookupRemote.getDocumentoAllegatoRemote();
		DocumentoAllegatoModel lDocAllModel = lCtrlDA.ExRicercaDocumentoAllegatoByKey(lIdDocAllegato);
		// Fascicolo SIEP
		IFascicoloSiep lCtrlFas = SIEPLookupRemote.getFascicoloSiepRemote();
		DettaglioFascicoloModel lDettaglio = lCtrlFas.ExDettaglioFascicoloSiep(
				lFascicoloGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
		FascicoloSiepModel lFascicoloSIEP = lDettaglio.getFascicoloSiep();

		// ----> Controllo Validazione del Deposito Ordinanza/Decreto
		if (lDocAllModel != null && !lDocAllModel.getFlagDocumentoRegistrato().equals("S")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			if (lEvento.getCodTipoProvvedimento() != null && lEvento.getCodTipoProvvedimento().equals("02")) {
				// DECRETO
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Il Deposito Decreto non è stato Validato. Impossibile effettuare il trasferimento!");
				lRedirigi.setAction(
						"siap.sius.depositodecreto.action.ActLoadDettaglioDataDepositoDecreto&IdDocumentoAllegato="
								+ lIdDocAllegato);
			} else { // ORDINANZA
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Il Deposito Ordinanza non è stato Validato. Impossibile effettuare il trasferimento!");
				lRedirigi.setAction(
						"siap.sius.depositoordinanzapc.action.ActLoadDettaglioDataDepositoOrdinanza&IdDocumentoAllegato="
								+ lIdDocAllegato);
			}
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		// ----> Controllo che il provvedimento di sorveglianza sia tra quelli di interesse del sistema NSC
		ProvvSiesNscModel lProvvSiesNscModel = new ProvvSiesNscModel();
		lProvvSiesNscModel.setProvvDomain("MDS");
		lProvvSiesNscModel.setProvvSiesMotivo(lEvento.getCodMotivo());
		lProvvSiesNscModel.setProvvSiesEsito(lEvento.getCodEsito());

		IProvvSiesNsc lCtrlDecodificaCodCentr = SICOLookupRemote.getProvvSiesNscRemote();
		Vector lListaProvvSiesNsc;
		lListaProvvSiesNsc = lCtrlDecodificaCodCentr.ExRicercaProvvSiesNsc(lProvvSiesNscModel);

		if (lListaProvvSiesNsc.size() == 0) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Provvedimento SIUS non è di interesse del Sistema NSC. Impossibile effettuare il trasferimento!");
			if (lEvento.getCodTipoProvvedimento() != null && lEvento.getCodTipoProvvedimento().equals("02")) {
				// DECRETO
				lRedirigi.setAction(
						"siap.sius.depositodecreto.action.ActLoadDettaglioDataDepositoDecreto&IdDocumentoAllegato="
								+ lIdDocAllegato);
			} else { // ORDINANZA
				lRedirigi.setAction(
						"siap.sius.depositoordinanzapc.action.ActLoadDettaglioDataDepositoOrdinanza&IdDocumentoAllegato="
								+ lIdDocAllegato);
			}
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		// ----> Controllo che il Titolo Esecutivo (SIEP) sia stato già accoppiato con NSC
		if (lFascicoloSIEP.getKeyProvvNsc() == null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Provvedimento Principale non è stato associato con NSC. Impossibile effettuare il trasferimento del Provvedimento di Esecuzione!");
			if (lEvento.getCodTipoProvvedimento() != null && lEvento.getCodTipoProvvedimento().equals("02")) {
				// DECRETO
				lRedirigi.setAction(
						"siap.sius.depositodecreto.action.ActLoadDettaglioDataDepositoDecreto&IdDocumentoAllegato="
								+ lIdDocAllegato);
			} else { // ORDINANZA
				lRedirigi.setAction(
						"siap.sius.depositoordinanzapc.action.ActLoadDettaglioDataDepositoOrdinanza&IdDocumentoAllegato="
								+ lIdDocAllegato);
			}
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		setRequestAttribute("IdEvento", lIdEvento);
		setRequestAttribute("IdDocumentoAllegato", lIdDocAllegato);
		return IWebConstants.ROOT_DIR + "/files/siap/sico/webservice/TrasferimentoEsecuzioneUDS.jsp";
	}

	private FascicoloGPModel CercaFascicoloSISU(BigDecimal lIdFascicoloSius) throws F3BException {
		FascicoloSiusController lFascContr = new FascicoloSiusController();
		FascicoloGPModel lFascicoloGPModel = new FascicoloGPModel();
		try {
			lFascicoloGPModel = lFascContr.ExRicercaFascicoloByKey(lIdFascicoloSius);
		} catch (SIEPException e) {
			throw new F3BException(
					"ActPrelevaEseUDS - Errore nella Ricerca del Fascicolo SIUS. Contattare il servizio di Help Desk");
		}

		return lFascicoloGPModel;
	}

}