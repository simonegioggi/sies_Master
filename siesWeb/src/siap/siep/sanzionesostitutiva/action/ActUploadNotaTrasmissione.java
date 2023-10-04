package siap.siep.sanzionesostitutiva.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.util.SIEPLookupRemote;


/**
 * 
 * @author d.fiorletta
 * @since MEV_2023-33
 */
public class ActUploadNotaTrasmissione extends ActionSiap implements ICostantiEvento {

  private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

  public String processRequest() throws Exception {

    siesLogger.info(getClass().getName() + ".processRequest: inizio");

    // ===============================================
    // Recupero l'id dell'evento
    // ===============================================
    EventoModel lEveUpdateModel = new EventoModel();
    lEveUpdateModel.setIdEvento(getRequestBigDecimalParameter(CAMPO_ID_EVENTO));

    InputStream lInput = getFile(ICostantiEvento.CAMPO_BLOB);

    if (lInput != null) {
      byte[] lBuffer = new byte[lInput.available()];

      lInput.read(lBuffer);
      ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
      lEveUpdateModel.setDocBlobIn(lSt);
    }

    lEveUpdateModel.setDataAggiornamento(DateUtils.getSysDate());
    lEveUpdateModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    lEveUpdateModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());

    if (isRequestChecked(ICostantiEvento.CAMPO_VALIDA)) {
      lEveUpdateModel.setFlagDocumentoRegistrato("S");

      ISanzioneSostitutiva lSanzioneCtrl = SIEPLookupRemote.getSanzioneSostitutivaRemote();

      lSanzioneCtrl.exUpdateNotaTrasmissione(lEveUpdateModel);
    } else { // aggiorno solo il blob
      lEveUpdateModel.setFlagDocumentoRegistrato("N");

      IEvento lCtrl = SICOLookupRemote.getEventoRemote();
      lCtrl.ExUpdateDocument(lEveUpdateModel);
    }

    //
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");

    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage(IWebConstants.PG_MAIN);
    lRedirigi.setAction(getRequestStringParameter(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO));
    lRedirigi.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, lEveUpdateModel.getIdEvento().toString());
    setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

    // info per il log
    siesLogger.info(getClass().getName() + ".processRequest: fine");

    // valore di ritorno
    return IWebConstants.PG_MESSAGE;
  }

}
