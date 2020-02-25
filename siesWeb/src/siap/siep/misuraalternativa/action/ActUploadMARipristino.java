package siap.siep.misuraalternativa.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.controller.IMisuraAlternativaIndultino;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>Title: ActUploadMARipristino</p>
 * <p>Description: Validazione della misura alternativa</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActUploadMARipristino extends ActionSiap
  implements ICostantiEvento
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

    lModel.setDataAggiornamento( DateUtils.getSysDate());

    lModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso() );
    lModel.setCodOperatoreAggiornamento(getCodUtenteConnesso() );
    
    //ricerca evento notifica
    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
    EventoNotificaModel lEveMod = new EventoNotificaModel();
    lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(getRequestBigDecimalParameter( CAMPO_ID_EVENTO));

    // ricerca misura per il fascicolo
    IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
    MisuraAlternativaModel lOrd = new MisuraAlternativaModel();

    if(lEveMod.getEvento().getEveIdEvento() != null)
    lOrd = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lEveMod.getEvento().getEveIdEvento());
    
    if(isRequestChecked( ICostantiEvento.CAMPO_VALIDA) )
    {
      lModel.setFlagDocumentoRegistrato("S");
      
      // PER tipo misura indultino "INDULTINO"
      if(lOrd.getCodTipoMisura().equalsIgnoreCase("0196"))
      
      {
        IMisuraAlternativaIndultino lCtrlIndu = SICOLookupRemote.getMisuraAlternativaRemoteIndultino();
        lCtrlIndu.ExUpdateValidaMARipristinoIndultino(lModel, lFascMod);
      }else
       {
        IMisuraAlternativa lCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
        lCtrl.ExUpdateValidaMARipristino(lModel, lFascMod);
       }
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
