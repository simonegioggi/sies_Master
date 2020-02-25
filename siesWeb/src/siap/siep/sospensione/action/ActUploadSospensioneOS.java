package siap.siep.sospensione.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>Title: ActUploadSospensioneOS</p>
 * <p>Description: Classe Action per l'Upload  di Sospensione  Ordine Scarcerazione </p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActUploadSospensioneOS extends ActionSiap
  implements ICostantiEvento
{

  public String processRequest() throws Exception
  {
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    
    //=====================================================
    // Imposto l'evento da Aggiornare/Validare
    //=====================================================
    EventoModel lModel = new EventoModel();
    lModel.setIdEvento( getRequestBigDecimalParameter( CAMPO_ID_EVENTO) );

    
    //=====================================================
    // Recupero il Documento dalla Form (se Upload)
    //=====================================================
    InputStream lInput = getFile(ICostantiEvento.CAMPO_BLOB);

    if(lInput != null)
    {
      byte[] lBuffer = new byte[lInput.available()];

      lInput.read(lBuffer);
      ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
      lModel.setDocBlobIn(lSt);
    }

    lModel.setDataAggiornamento         (DateUtils.getSysDate());
    lModel.setCodUfficioAggiornamento   (getCodUfficioUtenteConnesso() );
    lModel.setCodOperatoreAggiornamento (getCodUtenteConnesso() );

    String lStatoProc = "0011";

    //==========================================================================
    // Recupero il tipo di evento per determinae lo steo procedimento 
    //==========================================================================
    EventoModel lEveMod = null;
    IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
    
    lEveMod = lEveCtrl.ExRicercaEventoByKey(lModel.getIdEvento());
    
    if (   lEveMod!=null && lEveMod.getCodMotivo()!=null
        && lEveMod.getCodMotivo().equalsIgnoreCase("0366")
       )
    {
      lStatoProc = "0072"; // Emesso Ordine Provvisorio di Scarcerazione Il
    }
    
    //====================================================
    // Effettuo la validazione o semplice Upload
    //====================================================
    if(isRequestChecked( ICostantiEvento.CAMPO_VALIDA) )
    {
      lModel.setFlagDocumentoRegistrato("S");
      ISospensione lCtrl = SIEPLookupRemote.getSospensioneRemote();
      lCtrl.ExUpdateValidaSospensioneOE(lModel, lFascMod,"NP022",lStatoProc);
      // NP022 - Ordine di Scarcerazione
      // 0011 - Emesso Ordine di Esecuzione con Contestuale Sospensione il 
    }
    else
    { // Solo Upload
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