package siap.siep.sospensione.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>Title: ActUploadDifferimentoNew</p>
 * <p>Description: Classe Action per l' Upload  del Differimento </p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActUploadDifferimentoNew extends ActLoadInserisciDifferimentoMaster
  implements ICostantiEvento
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
   * Action invocata per effettuare l'upload e l'aggiornamento del report
   * di stampa e l'eventuale validazione
   * Sostituisce la vecchia ActUploadDifferimento invocata quando i differimenti
   * venivano salvati sulla tabella DECRETO_ORDINANZA_SIEP
   */
  public String processRequest() throws F3BException
  {

    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    EventoModel lEveUpdateModel = new EventoModel(); // evento contenete i soli dati da aggiornare
    lEveUpdateModel.setIdEvento( getRequestBigDecimalParameter( CAMPO_ID_EVENTO) );

    //==========================================================================
    // Recupero l'evento da Validare
    //==========================================================================
    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
    EventoModel lEveMod=lCtrlEvento.ExRicercaEventoByKey(getRequestBigDecimalParameter( CAMPO_ID_EVENTO));

    //==========================================================================
    // Recupero l'evento associato al decreto/ordinanza della sorveglianza
    //==========================================================================
//    EventoModel lEveModOrd=lCtrlEvento.ExRicercaEventoByKey(lEveMod.getEveIdEvento());

    //==========================================================================
    // Scarico l'eventuale report
    //==========================================================================
    InputStream lInput = getFile(ICostantiEvento.CAMPO_BLOB);

    try {
      if(lInput != null)
      {
        byte[] lBuffer = new byte[lInput.available()];

        lInput.read(lBuffer);
        ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
        lEveUpdateModel.setDocBlobIn(lSt);

        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug("BYTE ARRAY >>> " + lSt.toString());
      }
    }
    catch (IOException e) {
      throw new F3BException( e );
    }
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("BYTE ARRAY <<<" + lInput);


    //==========================================================================
    //
    //==========================================================================
    lEveUpdateModel.setDataAggiornamento         (DateUtils.getSysDate());
    lEveUpdateModel.setCodUfficioAggiornamento   (getCodUfficioUtenteConnesso() );
    lEveUpdateModel.setCodOperatoreAggiornamento (getCodUtenteConnesso() );


    //==========================================================================
    // Validazione
    //==========================================================================
    if(isRequestChecked( ICostantiEvento.CAMPO_VALIDA) )
    { // devo effettuare la validazione

      //==========================================================================
      // Recupero i dati della Misura Alternativa
      //==========================================================================
      IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
      MisuraAlternativaModel lMisAltDiff = null;
      lMisAltDiff = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lEveMod.getEveIdEvento());

      //==========================================================================
      // Determino la nuova posizione giuridica, lo stato del procedimento e il
      // nome del provvedimento
      // - Differimento Provvisorio ==
      //==========================================================================
      String tipoProvvedimento = getTipoProvvedimento(lMisAltDiff);

      String posizioneGiu = "";
      String statoProcedimento = "";
      String nomeProvvedimento = "NP114"; // Ordinanza Differimento Pena (FISSO)

      if ( tipoProvvedimento.equals(DIFFERIMENTO_PROV) ) {
        posizioneGiu      = "17";     // Libero in Differimento Pena (Provvisorio)
        statoProcedimento = "0093";   // (In esecuzione) Emessa Ordinanza di Differimento della Pena
      }
      else if ( tipoProvvedimento.equals(DIFFERIMENTO_DEF) ) {
        posizioneGiu      = "16";     // Libero in Differimento Pena
        statoProcedimento = "0156";   // (In esecuzione) Differimento definitivo ex art. 684 comma 1 c.p.p.
      }
      else if ( tipoProvvedimento.equals(DIFFERIMENTO_RIGETTO) ) {
        //posizioneGiu      = "16";     // Libero in Differimento Pena
        statoProcedimento = "0230";   // (In esecuzione) Rigetto Differimento Pena art.684 c.p.p.-emesso in data
      }
      else if ( tipoProvvedimento.equals(DIFFERIMENTO_REVOCA) ) {
        statoProcedimento = "0158";   // (In esecuzione) Revoca Differimento Pena art.684 c.p.p.
      }

      lEveUpdateModel.setFlagDocumentoRegistrato("S");
      ISospensione lCtrl = SIEPLookupRemote.getSospensioneRemote();
      // Nome provvedimento = NP114 = Ordinanza Differimento Pena
      lCtrl.ExUpdateValidaDifferimentoNew(tipoProvvedimento,
                                          lEveUpdateModel,
                                          lMisAltDiff,
                                          lFascMod.getIdFascicoloSiep(),
                                          posizioneGiu,
                                          nomeProvvedimento,
                                          statoProcedimento);
    }
    else
    { // aggiorno solo il blob
      lEveUpdateModel.setFlagDocumentoRegistrato("N");

      IEvento lCtrl = SICOLookupRemote.getEventoRemote();
      lCtrl.ExUpdateDocument(lEveUpdateModel);
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