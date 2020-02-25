package siap.siep.sospensione.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


/**
 * <p>Title: ActUploadSospensioneOE</p>
 * <p>Description: Classe Action per l'Upload di Sospensione Ordine Esecuzione e
 *                 Comunicazione nel caso di Interruzioni</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActUploadSospensioneOE extends ActionSiap
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

    //====================================================
    // Effettuo la validazione
    //====================================================
    if(isRequestChecked( ICostantiEvento.CAMPO_VALIDA) )
    {
      // Recupero posizione giuridica perchè se Evaso devo aggiornare anche lo
      // Stato del Procedimento e il Nome Provvedimento 
      IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
      PosizioneGiuridicaModel lPos = new PosizioneGiuridicaModel();
      lPos = lPosCtrl.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascMod.getIdFascicoloSiep());

      lModel.setFlagDocumentoRegistrato("S");
      ISospensione lCtrl = SIEPLookupRemote.getSospensioneRemote();
      // Il flag va sempre valorizzato. Luigi 11-11-06
      // 0001 - Emesso Ordine di Esecuzione con Arresto Il
      // NP200 - Ordine Esecuzione  - Evaso
      if(lPos.getCodPosizioneGiuridica().equals("20"))  // evaso
        lCtrl.ExUpdateValidaSospensioneOE(lModel, lFascMod,"NP200","0001");
      else
//    if(lPos.getCodPosizioneGiuridica().equals("30"))
        lCtrl.ExUpdateValidaSospensioneOE(lModel, lFascMod,"","");
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
