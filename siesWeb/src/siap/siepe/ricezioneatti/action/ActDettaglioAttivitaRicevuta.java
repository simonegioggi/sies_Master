package siap.siepe.ricezioneatti.action;

 /**
 * <p>Title: ActDettaglioAttivitaRicevuta</p>
 * <p>Description: Classe Action per la load dettaglio documento ricevuto</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.web.ActionSiap;
//import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siepe.SIEPEException;
import siap.siepe.attivita.model.AttivitaModel;
import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel;
import siap.siepe.fascicolo.model.FascicoloSiepeModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
//import siap.sius.fascicolo.model.FascicoloSiusModel;
//import siap.sico.evento.model.EventoNotificaModel;
//import f3b.util.report.ReportGenerator;
import f3b.log.LogF3B;

public class ActDettaglioAttivitaRicevuta extends ActionSiap 
implements ICostantiRicezioneAtti
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: inizio" );
    
    this.gestioneRitorno();

    BigDecimal lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMessage);

    this.setRequestAttribute("Messaggio", lMess);

    ParserMessage lParser = new ParserMessage(lMess.getTreeModel());
    
    // Parser di FascicoloSiepeEstesoModel
    FascicoloSiepeEstesoModel lFasEsteso = new FascicoloSiepeEstesoModel();
    if( lParser.getFascicoloSiepeEsteso() != null )
      lFasEsteso = lParser.getFascicoloSiepeEsteso();
    else
      throw new SIEPEException( SIEPEException.USER_MESSAGE, "Errore nella Ricezione dell'Fascicolo Siepe riferito alla Richiesta Trasmessa. " );

    //PARSER di SOGGETTO
    /*
    SoggettoModel lSogg = new SoggettoModel();
    if (lParser.getSoggetto()!=null)
      lSogg = lParser.getSoggetto();
    else
      throw new SIEPEException( SIEPEException.USER_MESSAGE, "Errore nella Ricezione del Soggetto riferito alla Richiesta Trasmessa. " );
    */

    // Recupero del Soggetto dal fascisolo siepe esteso model.
    SoggettoModel lSogg = new SoggettoModel();
    if (lFasEsteso.getSoggetto()!=null)
      lSogg = lFasEsteso.getSoggetto();
    else
      throw new SIEPEException( SIEPEException.USER_MESSAGE, "Errore nella Ricezione del Soggetto riferito alla Richiesta Trasmessa. " );

    //PARSER di EVENTO
    /*
    EventoNotificaModel lEveNot = new EventoNotificaModel();
    if (lParser.getEvento()!=null)
      lEveNot = lParser.getEvento();
    else
      throw new SIEPEException( SIEPEException.USER_MESSAGE, "Errore nella Ricezione dell'Evento riferito al Provvedimento Trasmesso. " );
    */
    
    // Recupero dell'evento dal facicolo siepe esteso model.
    EventoModel lEveModel = new EventoModel();
    if (lFasEsteso.getEvento()!=null)
      lEveModel = lFasEsteso.getEvento();
    else
      throw new SIEPEException( SIEPEException.USER_MESSAGE, "Errore nella Ricezione dell'Evento riferito alla Richiesta Trasmessa. " );

   
    //PARSER di SENTENZA
    /*
    SentenzaModel lSentenza = new SentenzaModel();
    if (lParser.getSentenza()!=null)
      lSentenza = lParser.getSentenza();
    else
      throw new SIEPEException( SIEPEException.USER_MESSAGE, "Errore nella Ricezione della Sentenza riferita al Provvedimento Trasmesso. " );
    */
    
    //PARSER di FASCICOLO SIEP
    /*
    FascicoloSiepModel lFascicolo = new FascicoloSiepModel();
    if (lParser.getFascicolo()!=null)
      lFascicolo = lParser.getFascicolo();
    else
      throw new SIEPEException( SIEPEException.USER_MESSAGE, "Errore nella Ricezione del Fascicolo SIEP riferito al Provvedimento Trasmesso. " );
    */

    //Recupero di FASCICOLO SIEP dal fascicolo siepe esteso model.
    // Siccome può capitare che un fascisolo SIUS non abbia un fascicolo SIEP
    // questo controllo non è obbligatorio
    FascicoloSiepModel lFascicolo = new FascicoloSiepModel();
    if (lFasEsteso.getFascicoloSiep()!=null)
      lFascicolo = lFasEsteso.getFascicoloSiep();
    
    //Recupero di FASCICOLO SIUS dal fascicolo siepe esteso model.
    FascicoloGPModel lFascicoloSiusGP = new FascicoloGPModel();
    if (lFasEsteso.getFascicoloSius()!=null)
      lFascicoloSiusGP = lFasEsteso.getFascicoloSius();
    else
      throw new SIEPEException( SIEPEException.USER_MESSAGE, "Errore nella Ricezione del Fascicolo SIUS. " );

    //Recupero di FASCICOLO SIEPE dal fascicolo siepe esteso model.
    FascicoloSiepeModel lFascicoloSiepe = new FascicoloSiepeModel();
    if (lFasEsteso.getFascicoloSiepe()!=null)
      lFascicoloSiepe = lFasEsteso.getFascicoloSiepe();
    else
      throw new SIEPEException( SIEPEException.USER_MESSAGE, "Errore nella Ricezione del Fascicolo SIEPE. " );
    
    // Recupero dell'Attività dal Parser 
    AttivitaModel lAttivita = new AttivitaModel();
    if (lParser.getAttivita()!=null)
      lAttivita = lParser.getAttivita();
    else
      throw new SIEPEException( SIEPEException.USER_MESSAGE, "Errore nella Ricezione della Richiesta Trasmessa. " );
    
    // Inserisce i request i dati da visualizzare nella JSP.
    
    //this.setRequestAttribute("eventoNotifica", lEveNot);
    this.setRequestAttribute("evento", lEveModel );
    this.setRequestAttribute("fascicolo", lFascicolo );
    this.setRequestAttribute("fascicoloSiusGP", lFascicoloSiusGP );
    this.setRequestAttribute("fascicoloSiepe", lFascicoloSiepe );
    this.setRequestAttribute("soggetto", lSogg );
    //this.setRequestAttribute("sentenza", lSentenza);
    this.setRequestAttribute( "attivita", lAttivita );

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: fine" );
    
    return PG_DETTAGLIO_ATTIVITA_RICEVUTA;
  }
}