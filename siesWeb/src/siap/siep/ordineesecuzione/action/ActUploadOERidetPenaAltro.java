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
 * <p>Title: ActUploadOERidetPenaAltro</p>
 * <p>Description: Action per l'Upload e Validazione dell'Ordine di Esecuzione
 *                 per rideterminazione pena Altro.
 * </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @since 4.0
 */
public class ActUploadOERidetPenaAltro extends ActionSiap implements ICostantiEvento
{
  public String processRequest() throws Exception
  {
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    EventoModel lModel = new EventoModel();
    lModel.setIdEvento( getRequestBigDecimalParameter( CAMPO_ID_EVENTO) );

    InputStream lInput = getFile(ICostantiEvento.CAMPO_BLOB);

    if(lInput != null)
    {
      byte[] lBuffer = new byte[lInput.available()];

      lInput.read(lBuffer);
      ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
      lModel.setDocBlobIn(lSt);
    }

    lModel.setDataAggiornamento         ( DateUtils.getSysDate());
    lModel.setCodUfficioAggiornamento   (getCodUfficioUtenteConnesso() );
    lModel.setCodOperatoreAggiornamento (getCodUtenteConnesso() );

    if(isRequestChecked( ICostantiEvento.CAMPO_VALIDA) )
    {
      lModel.setFlagDocumentoRegistrato("S");

      IOrdineEsecuzione lCtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();
      lCtrl.ExUpdateValidaOERidetPenaAltro(lModel, lFascMod);
    }
    else
    { // Effettua il solo upload del BLOB
      lModel.setFlagDocumentoRegistrato("N");

      IEvento lCtrl = SICOLookupRemote.getEventoRemote();
      lCtrl.ExUpdateDocument(lModel);
    }

    //Prepara la "pagina" di destinazione
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
