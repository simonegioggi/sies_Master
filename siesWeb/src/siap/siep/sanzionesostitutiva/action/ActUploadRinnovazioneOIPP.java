package siap.siep.sanzionesostitutiva.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.rinnovo.action.ICostantiRinnovo;
import siap.siep.rinnovo.controller.IRinnovo;
import siap.siep.rinnovo.model.RinnovoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Funzione di validazione per la Rinnovazione notifica per gli Ordini di ingiunzione
 * @author d.fiorletta
 * @since MEV_2023-33 
 */
public class ActUploadRinnovazioneOIPP extends ActionSiap implements ICostantiNotifica{
  public String processRequest() throws Exception
  {
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    RinnovoModel lModel = new RinnovoModel();
    lModel.setIdRinnovo(getRequestBigDecimalParameter(ICostantiRinnovo.CAMPO_ID_RINNOVO));

    InputStream lInput = getFile(ICostantiRinnovo.CAMPO_DOC_BLOB);

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

    IRinnovo lCtrl = SIEPLookupRemote.getRinnovoRemote();
    if (isRequestChecked(ICostantiRinnovo.CAMPO_VALIDA))
    {
      lModel.setFlagDocumentoRegistrato("S");
      lCtrl.ExUpdateValidaRinnovazioneNotifichePP(lFascMod,lModel);
    }
    else
    {
      lModel.setFlagDocumentoRegistrato("N");
      lCtrl.ExUpdateDocument(lModel);
    }

    //Prepara la "pagina" di destinAction
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");

   if (!isRequestParameterNullObj(CAMPO_AZIONE_DETTAGLIO))
    {
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
      lRedirigi.setAction(getRequestStringParameter(CAMPO_AZIONE_DETTAGLIO) + "&" +
            ICostantiRinnovo.CAMPO_ID_RINNOVO + "=" + getRequestStringParameter(ICostantiRinnovo.CAMPO_ID_RINNOVO));
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
    }

    return IWebConstants.PG_MESSAGE;
  }
}
