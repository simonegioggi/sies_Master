package siap.siep.nuovaistanza.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.nuovaistanza.controller.INuovaIstanza;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>Title: ActUploadAnnProvCumulo</p>
 * <p>Description: Validazione archiviazione</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActUploadInoltroIstanza extends ActionSiap implements ICostantiEvento
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
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
      INuovaIstanza lCtrl = SIEPLookupRemote.getNuovaIstanzaRemote();
      lCtrl.ExUpdateValidaInoltroNuovaIstanza(lModel);
    }
    else
    {
      lModel.setFlagDocumentoRegistrato("N");
      IEvento lCtrl = SICOLookupRemote.getEventoRemote();
      lCtrl.ExUpdateDocument(lModel);
    }

    //Prepara la "pagina" di destinAction
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");

    /*dopo aver validato l'evento e quindi archiviato il fascicolo si ha bisogno di una nuova ricerca del fascicolo
      per settare il nuovo model del fascicolo in sessione*/
    IFascicoloSiep lCtrlFasc = SIEPLookupRemote.getFascicoloSiepRemote();
    FascicoloSiepModel lFascicoloMod = lCtrlFasc.ExRicercaFascicoloByKey(lFascMod.getIdFascicoloSiep());
    this.setSessionAttribute("fascicolo",lFascicoloMod);

    if (!isRequestParameterNullObj(CAMPO_AZIONE_DETTAGLIO))
    {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.info("DENTRO ACTUPLOAD--->getRequestStringParameter(CAMPO_AZIONE_DETTAGLIO)=" + getRequestStringParameter(CAMPO_AZIONE_DETTAGLIO));
	  String actchiamante="&AzioneChiamante=siap.siep.nuovaistanza.action.ActUploadInoltroIstanza";
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);

      if (!this.isRequestParameterNullObj("TipoVis")){
        	if (this.getRequestStringParameter("TipoVis").equals("Disposizione")) {
                lRedirigi.setAction(getRequestStringParameter(CAMPO_AZIONE_DETTAGLIO) + "&" + CAMPO_ID_EVENTO + "=" + getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO)
            	      	+ "&TipoVis=Disposizione" +actchiamante);
          	}else {
                lRedirigi.setAction(getRequestStringParameter(CAMPO_AZIONE_DETTAGLIO) + "&" + CAMPO_ID_EVENTO + "=" + getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO)
            	      	+ "&TipoVis=Inoltro"+actchiamante);
            }
      } else {
        lRedirigi.setAction(getRequestStringParameter(CAMPO_AZIONE_DETTAGLIO) + "&" + CAMPO_ID_EVENTO + "=" + getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO)
    	      	+ "&TipoVis=Inoltro"+actchiamante);
      }
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.info("DENTRO ACTUPLOAD--->lRedirigi=" + lRedirigi.toString());
		lRedirigi.setParameter("AzioneChiamante", actchiamante);
		
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
    }

    return IWebConstants.PG_MESSAGE;
  }
}