package siap.siep.fascicolo.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;

public class ActLoadInserisciFascicolo extends ActionSiap implements ICostantiFascicoloSiep
{
  public String processRequest() throws Exception
  {
    if( isSessionAttributeNullObj("soggetto") && isSessionAttributeNullObj("sentenza") )
      throw new SIEPException( SIEPException.USER_MESSAGE, "Selezionare il soggetto e la sentenza." );
    else if( isSessionAttributeNullObj("soggetto") )
      throw new SIEPException( SIEPException.USER_MESSAGE, "Selezionare il soggetto." );
    else if( isSessionAttributeNullObj("sentenza") )
      throw new SIEPException( SIEPException.USER_MESSAGE, "Selezionare la sentenza." );

    if(!this.isRequestParameterNullObj("lTipoFunzione")) // paramentro passato solo nel caso di iscrizione guidata
    {
      this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
    }


    // Imposta Modalità.
    setRequestAttribute("modalita", "I");
    
    
    {
      //========================================================================
      // New d.f. 14/04/2015
      // Devo controllare se per l'anno corrente sono stati iscritti procedimenti
      // di classe IV. In caso negativo devo obbligare l'utente a indicare 
      // manualmente il numero di procedimento che rappresenterà l'inizio della
      // numerazione automatica per i fascicoli telematici dell'anno corrente. 
      // Infatti sono stati già iscritti sicuramente fascicoli cartacei che 
      // andranno eventualmente caricati manualmente. La numerazione automatica
      // vale solo per i nuovi e non può sovrapporsi a quella cartecea già assegnata
      // dall'ufficio.
      //
      // Verificare se subordinare il controllo al 2015
      //========================================================================
      BigDecimal lAnnoCorrente = new BigDecimal(DateUtils.getSysDate("yyyy"));

      if (lAnnoCorrente.intValue()==2015) {
        // n.b. controllo solo per il 2015, anno di avvio delle Misure Sicurezza
        IMisuraSicurezza lCtrlMS = SIEPLookupRemote.getMisuraSicurezzaRemote();
        boolean lEsisteFascicoloClasseIVAnnoCorrente =  lCtrlMS.ExEsistonoFascicoliClasseIVAnno (getUfficioUtenteConnesso(), lAnnoCorrente );
        
        // Se non esiste devo forzare la numerazione manuale
        if (!lEsisteFascicoloClasseIVAnnoCorrente) {
          setRequestAttribute("NumerazioneManuale", "S");
          setRequestAttribute("EsisteFascicoloClasseIVAnnoCorrente", "N");
          setRequestAttribute("AnnoCorrente", DateUtils.getSysDate("yyyy"));
          
        }
      }
    }    
    
    

    return PG_LOAD_INSERISCIFASCICOLO_SIEP;
  }
}
