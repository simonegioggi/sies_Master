package siap.siep.ordineesecuzione.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * 
 * @author 
 *
 */
public class ActUploadVariazioneDecorrenzaScadenzaQC extends ActionSiap
    implements ICostantiEvento
{
  public String processRequest() throws Exception
  {
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    EventoModel lEveModel = new EventoModel();
    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();

    lEveModel = lCtrlEvento.ExRicercaEventoByKey(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
    lEveModel.setIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));

    InputStream lInput = getFile(ICostantiEvento.CAMPO_BLOB);

    if (lInput != null)
    {
      byte[] lBuffer = new byte[lInput.available()];

      lInput.read(lBuffer);
      ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
      lEveModel.setDocBlobIn(lSt);
    }

    lEveModel.setDataAggiornamento(DateUtils.getSysDate());
    lEveModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    lEveModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());

    IOrdineEsecuzione lCtrlOrdine = SIEPLookupRemote.getOrdineEsecuzioneRemote();

    if (isRequestChecked(ICostantiEvento.CAMPO_VALIDA))
    {
      lEveModel.setFlagDocumentoRegistrato("S");
      lCtrlOrdine.ExUpdateValidaVariazioneDecScadQC(lEveModel, lFascMod);
    }
    else
    {
      lEveModel.setFlagDocumentoRegistrato("N");
      lCtrlEvento.ExUpdateDocument(lEveModel);
    }

    //Prepara la "pagina" di destinAction
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");

    if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO))
    {
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
      lRedirigi.setAction(getRequestStringParameter(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO) + "&" + ICostantiEvento.CAMPO_ID_EVENTO + "=" +
                          getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
    }

    return IWebConstants.PG_MESSAGE;
  }
}