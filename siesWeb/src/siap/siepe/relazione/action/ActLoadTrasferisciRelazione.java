package siap.siepe.relazione.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel;
import siap.siepe.relazione.controller.IRelazione;
import siap.siepe.relazione.model.RelazioneModel;
import siap.siepe.richiesta.controller.IRichiesta;
import siap.siepe.richiesta.model.RichiestaModel;
import siap.siepe.util.SIEPELookupRemote;
import f3b.log.LogF3B;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadTrasferisciRelazione</p>
* <p>Description: Classe Action per il caricamento dela form per il trasferimento Relazione</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadTrasferisciRelazione extends ActionSiap implements ICostantiRelazione
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getPackage().getName() + "." + this.getClass().getName() + ".processRequest(): inizio");

    String lId = getRequestStringParameter(CAMPO_ID_RELAZIONE);

    // Chiama il controller e riempie il model
    IRelazione lCtrl = SIEPELookupRemote.getRelazioneRemote();
    RelazioneModel lRelMod = lCtrl.ExRicercaRelazioneByKey(new BigDecimal(lId));

    String lStampabile = "SI";
    String lModificabile = "NO";
    String lTrasferibile = "SI";
    String lUpload = "SI";
    String lValidata = "NO";

    // Esegue il controllo di validità.
    if( lRelMod.getFlagDocumentoRegistrato() != null &&  lRelMod.getFlagDocumentoRegistrato().equalsIgnoreCase("S") )
    {
      lModificabile = "NO";
      lTrasferibile = "SI";
      lUpload = "NO";
      lValidata = "NO";
    }

    setRequestAttribute("Stampabile", lStampabile);
    setRequestAttribute("Modificabile", lModificabile);
    setRequestAttribute("Trasferibile", lTrasferibile);
    setRequestAttribute("Upload", lUpload );
    setRequestAttribute("Validata", lValidata );

    setRequestAttribute("FlagFasSiepe", "SI"); // Imposta il flag per abilitare la visualizzazione dei dati di sintesi SIEPE
    setRequestAttribute("relazione", lRelMod);

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( "Relazione : " + lRelMod );

    // Imposta Modalità.
    setRequestAttribute("modalita", "T");

    /* 2007-06-23 Temporaneamente commentato, per gestione ufficio destinario diverso dal mittente
     * del fascicolo siepe, in attesa di chiarimenti/confronti dell'analisi funzionale. Vedere
     * il codice riportato di seguito al medesimo.   
    // Dati aggregati relativi al Fascicolo SIEPE
    FascicoloSiepeEstesoModel lFascicoloEsteso = null;
    // Si risale ai dati in Sessione
    lFascicoloEsteso = (FascicoloSiepeEstesoModel) getSessionAttribute("FascicoloSiepeEsteso");
    // L'ufficio destinatario è il mittente del Fascicolo SIEPE
    String lCodUfficioMittente = lFascicoloEsteso.getFascicoloSiepe().getCodUfficioMittente();
    IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();
    UfficioModel lUffDestinatario = lUffCtrl.ExRicercaUfficioByCod(lCodUfficioMittente);
    setRequestAttribute("ufficioDestinatario",lUffDestinatario );
    */
    
    // 2007-06-23 Patch "provvisoria" per gestione Ufficio di destinazione, poichè sembra che nel
    // caso di trasmissione di una relazione afferente ad una richiesta il codice del destinatario
    // dovrebbe essere quello  della richiesta in caso contrario quello del mittente del fascisolo 
    // siepe questo secondo una mia intepretazione.
    String lCodUfficioDestinatario = null;
    if( lRelMod.getRicIdRichiesta() != null )
    {
      IRichiesta lCtrlRich = SIEPELookupRemote.getRichiestaRemote();
      RichiestaModel lRichiesta = lCtrlRich.ExRicercaRichiestaByKey( lRelMod.getRicIdRichiesta() );
      lCodUfficioDestinatario = lRichiesta.getCodUfficioDestinatario();
    }
    else
    {
      FascicoloSiepeEstesoModel lFascicoloEsteso = null;
      // Si risale ai dati in Sessione
      lFascicoloEsteso = (FascicoloSiepeEstesoModel) getSessionAttribute("FascicoloSiepeEsteso");
      // L'ufficio destinatario è il mittente del Fascicolo SIEPE
      lCodUfficioDestinatario = lFascicoloEsteso.getFascicoloSiepe().getCodUfficioMittente();
    }

    setRequestAttribute("ufficioDestinatario",super.getUfficioByCodUfficio( lCodUfficioDestinatario ) );

    // STUB 04/11/2008 Aggiunta altro destinatario.
    Option lOption = new Option( DecodificheManager.getInstance().getTipoUfficio());
    lOption.setFilter( new String[] {"-", "TDS", "UDS", "UEPE", "UEPESS"} );
    setRequestAttribute("altroUfficioDestinatario", "" + lOption );

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");
    
    return PG_DETTAGLIO_RELAZIONE;
  }
}