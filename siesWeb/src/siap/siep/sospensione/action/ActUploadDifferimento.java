package siap.siep.sospensione.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.decretoordinanza.controller.IDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>Title: ActUploadDifferimento</p>
 * <p>Description: Classe Action per l'Upload del Differimento </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @deprecated Vecchia gestione del differimento
 * @see siap.siep.sospensione.action.ActUploadDifferimento
 * @version 1.0
*/

public class ActUploadDifferimento extends ActionSiap
  implements ICostantiEvento
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
   * Effettua l'upload del report e la validazione
   */
  public String processRequest() throws Exception
  {

    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    
    EventoModel lModel = new EventoModel();
    lModel.setIdEvento( getRequestBigDecimalParameter( CAMPO_ID_EVENTO) );

    //==========================================================================
    // Recupero l'evento da Validare
    //==========================================================================
    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
    EventoModel EveMod=lCtrlEvento.ExRicercaEventoByKey(getRequestBigDecimalParameter( CAMPO_ID_EVENTO));

    //==========================================================================
    // Recupero l'evento (decreto/ordinanza) della sorveglianza legato al provvedimento
    //==========================================================================
    EventoModel EveModOrd=lCtrlEvento.ExRicercaEventoByKey(EveMod.getEveIdEvento());

    //==========================================================================
    // Recupero il Decreto Ordinanza Siep (legato al decreto/ordinanza)
    //==========================================================================
    IDecretoOrdinanzaSiep lCtrlDec = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();
    DecretoOrdinanzaSiepModel lDecOrdMod = lCtrlDec.ExRicercaDecretoOrdinanzaSiepByKey(EveModOrd.getDecIdDecretoOrdinanzaSiep());

    //==========================================================================
    // Devo aggiornare la posizione giuridica
    //==========================================================================
    String posizioneGiu = "16";
    if(lDecOrdMod!= null) 
    {
      if( lDecOrdMod.getCodTipoAutoritaEmittente().equals("UDS"))
      {
        posizioneGiu = "17";  // Libero in Differimento Pena (Provvisorio)
      }
      else if(lDecOrdMod.getCodTipoAutoritaEmittente().equals("TDS"))
      {
        posizioneGiu = "16"; // Libero in Differimento Pena
      }
    }
    
//lDecOrdMod.getDescrTipoAutoritaEmittente()
    //==========================================================================
    // Scarico l'eventuale report
    //==========================================================================
    InputStream lInput = getFile(ICostantiEvento.CAMPO_BLOB);

    if(lInput != null)
    {
      byte[] lBuffer = new byte[lInput.available()];

      lInput.read(lBuffer);
      ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
      lModel.setDocBlobIn(lSt);
      
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("BYTE ARRAY >>> " + lSt.toString());
    }
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("BYTE ARRAY <<<" + lInput);

    
    lModel.setDataAggiornamento         (DateUtils.getSysDate());
    lModel.setCodUfficioAggiornamento   (getCodUfficioUtenteConnesso() );
    lModel.setCodOperatoreAggiornamento (getCodUtenteConnesso() );


    if(isRequestChecked( ICostantiEvento.CAMPO_VALIDA) )
    {
      lModel.setFlagDocumentoRegistrato("S");
      ISospensione lCtrl = SIEPLookupRemote.getSospensioneRemote();
      // Nome provvedimento = NP114 = Ordinanza Differimento Pena
      // Stato procedimento = 0093  = (In esecuzione) Emessa Ordinanza di Differimento della Pena
      lCtrl.ExUpdateValidaDifferimento(lModel, lFascMod,posizioneGiu,"NP114","0093");
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
			lRedirigi.setAction(getRequestStringParameter(CAMPO_AZIONE_DETTAGLIO) + "&" + CAMPO_ID_EVENTO + "=" + getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO)+"&lflagAzione=D");
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		}

  	return IWebConstants.PG_MESSAGE;
  }
}