package siap.siep.richiesta.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.xml.TreeModel;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPSender;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siepe.SIEPEException;

/**
 * <p>
 * Title: ActUploadSollecitoTrasmComp
 * </p>
 * <p>
 * Description: Produzione e contestuale invio del Sollecito Trasmssione Provvedimento di cumulo
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
public class ActUploadSollecitoTrasmComp extends ActionSiap implements ICostantiRichiesta, ICostantiJMS {

	public String processRequest() throws Exception {

		// ID del messaggio ESITO
		BigDecimal idMessaggio = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
		// siesLogger.debug("--XX-- >>>>>>>>>>> Start UpLoadSoll - Id Messaggio da sollecitare =
		// "+idMessaggio);

		// Prendo il Messaggio
		MessaggioModel lMess = new MessaggioModel();
		if (idMessaggio != null) {
			IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
			lMess = lCrtl.ExRicercaMessaggioByKey(idMessaggio);
		}

		//
		if (!isRequestChecked(ICostantiEvento.CAMPO_VALIDA)) {
			throw new SIEPEException(SIEPEException.USER_MESSAGE,
					"Il documento non è stato validato!<BR>Nessun sollecito inviato");
		}

		// LEGGO L'ID DELL'EVENTO E LO TOLGO DALLA SESSIONE
		BigDecimal mIdEveSollecito = null;
		if (!isSessionAttributeNullObj(FIELD_TEMP_ID_EVENTO_SOLLECITO)) {
			mIdEveSollecito = (BigDecimal) this.getSessionAttribute(FIELD_TEMP_ID_EVENTO_SOLLECITO);
			this.removeSessionAttribute(FIELD_TEMP_ID_EVENTO_SOLLECITO);
		} else {
			throw new SIEPEException(SIEPEException.USER_MESSAGE,
					"ERRORE NELLA TRASMISSIONE E VALIDAZIONE DEL SOLLECITO!");
		}

		// ------ Update del Documento BLOB sull'evento
		EventoModel lModel = new EventoModel();
		lModel.setIdEvento(mIdEveSollecito);

		InputStream lInput = getFile(ICostantiEvento.CAMPO_BLOB);

		if (lInput != null) {
			byte[] lBuffer = new byte[lInput.available()];
			lInput.read(lBuffer);
			ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
			lModel.setDocBlobIn(lSt);
		}

		lModel.setDataAggiornamento(DateUtils.getSysDate());
		lModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lModel.setFlagDocumentoRegistrato("S");

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lCtrl.ExUpdateDocument(lModel);

		// Preparo la trasmissione del sollecito
		// ricerco il fascicolo INVIATO

		FascicoloSiepModel aFas = new FascicoloSiepModel();
		aFas.setChiaveAnno(lMess.getChiaveAnnoSiep());
		aFas.setChiaveProgr(lMess.getChiaveProgrSiep());
		aFas.setChiaveUfficio(lMess.getChiaveUfficioSiep());

		// aFas.setChiaveUfficio(lMess.getCodUfficioDestinatario());

		// d.f. 23/12/2015 non serve inviare l'intero fascicolo nel sollecito
		// l'ufficio sollecitato ha già preso in carico il provvedimento
		// IFascicoloSiep lCrtlFas = SIEPLookupRemote.getFascicoloSiepRemote();
		// FascicoloSiepModel mFas = lCrtlFas.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(aFas);
		// IRicercaJMS lCtrlMes = SIEPLookupRemote.getRicercaJMS();
		// MessaggioModel lMessage = lCtrlMes.ExRicercaFascicoloSiepPerTrasferimento(mFas);

		MessaggioModel lMessage = new MessaggioModel();
		lMessage.setTreeModel(new TreeModel()); // nessun carico

		UfficioModel lBDIMittente = getUfficioByCodUfficio(getUfficioUtenteConnesso().getCodDistretto());

		// -------->>>>>>>>>>> Inserire un meccanismo di reperimento della BDI a partire
		lMessage.setCodTipoMessaggio(RICHIESTA);
		lMessage.setCodTipoOperazione(SOLLECITO_TRASFERIMENTO_COMPETENZA);
		lMessage.setCodEsito(POSITIVO); // genericamente Positivo, in realtà non è significativo

		lMessage.setDescrBdiDestinataria(lMess.getDescrBdiMittente());
		lMessage.setCodBdiDestinataria(lMess.getCodBdiMittente());
		lMessage.setCodUfficioDestinatario(lMess.getCodUfficioMittente());

		lMessage.setCodBdiMittente(lBDIMittente.getCodUfficio());
		lMessage.setDescrBdiMittente(lBDIMittente.getDescrComune());
		lMessage.setCodUfficioMittente(getCodUfficioUtenteConnesso());
		lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());

		lMessage.setDataInvio(DateUtils.getSysDate());

		// SETTA RIFERIMENTI FASCICOLO SIEP
		lMessage.setChiaveAnnoSiep(lMess.getChiaveAnnoSiep());
		lMessage.setChiaveProgrSiep(lMess.getChiaveProgrSiep());
		lMessage.setChiaveUfficioSiep(lMess.getChiaveUfficioSiep());

		lMessage.setChiaveAnnoFasCumulante(lMess.getChiaveAnnoFasCumulante());
		lMessage.setChiaveProgrFasCumulante(lMess.getChiaveProgrFasCumulante());
		lMessage.setChiaveUfficioFasCumulante(lMess.getChiaveUfficioFasCumulante());

		// Id del Messaggio Sollecitato
		lMessage.setIdMessaggioSollecitato(idMessaggio.toString());
		// lMessage.setIdMessaggioSollecitato(""+idMessaggio);

		// INVIO DEL MESSAGGIO
		SIAPSender lSender = new SIAPSender();
		lSender.send(lMessage);
		// ===============================
		// siesLogger.debug("--XX-- >>>>>>>>>>> Messaggio Inviato ??? ="+lMessage);

		// Passo l'id del messaggio per caricare il dettaglio
		this.setRequestAttribute(ICostantiMessaggio.CAMPO_ID_MESSAGGIO, idMessaggio);
		// setta la risposta nella request
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Trasmissione del Sollecito sottomessa al Sistema!");

		// Prepara la "pagina" di destinAction
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);

		// lRedirigi.setAction( "siap.siep.richiesta.action.ActLoadRiscontroTrasmissioneCompetenza" );
		lRedirigi.setAction("siap.siep.richiesta.action.ActLoadRicercaTrasmissioniSolleciti");

		lRedirigi.setParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO, idMessaggio + "");

		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

		return IWebConstants.PG_MESSAGE;
	}

}