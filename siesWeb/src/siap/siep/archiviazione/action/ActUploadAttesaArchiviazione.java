package siap.siep.archiviazione.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.archiviazione.controller.IArchiviazione;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>Title: ActUploadAttesaArchiviazione</p>
 * <p>Description: Validazione dell'attesa archiviazione</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActUploadAttesaArchiviazione extends ActionSiap implements ICostantiEvento
{
  public String processRequest() throws Exception
  {

    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    EventoModel lModel = new EventoModel();
    lModel.setIdEvento(getRequestBigDecimalParameter(CAMPO_ID_EVENTO));

    InputStream lInput= null;
    if(this.isRequestParameterNullObj("noblob"))
    {
      lInput = getFile(ICostantiEvento.CAMPO_BLOB);
    }

    if (lInput != null)
    {
      byte[] lBuffer = new byte[lInput.available()];

      lInput.read(lBuffer);
      ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
      lModel.setDocBlobIn(lSt);
    }


    lModel.setDataAggiornamento(DateUtils.getSysDate());
    lModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    lModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());


    if (isRequestChecked(ICostantiEvento.CAMPO_VALIDA))
    {
      lModel.setFlagDocumentoRegistrato("S");
      IArchiviazione lCtrl = SIEPLookupRemote.getArchiviazioneRemote();
      lCtrl.ExUpdateValidaVistoAttesa(lModel, lFascMod,"0334");
    }
    else
    {
      lModel.setFlagDocumentoRegistrato("N");
      IEvento lCtrl = SICOLookupRemote.getEventoRemote();
      lCtrl.ExUpdateDocument(lModel);
    }

    //Prepara la "pagina" di destinAction
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");


    if (!isRequestParameterNullObj(CAMPO_AZIONE_DETTAGLIO))
    {
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
      lRedirigi.setAction(getRequestStringParameter(CAMPO_AZIONE_DETTAGLIO) + "&" + CAMPO_ID_EVENTO + "=" + getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
    }

    return IWebConstants.PG_MESSAGE;
  }
}