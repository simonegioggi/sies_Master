package siap.siepe.richiesta.action;

// Import per le combo
import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
//import siap.sico.web.ActionSiap;
import siap.siepe.fascicolo.action.ActRicercaFasSiepePuntuale;
import siap.siepe.fascicolo.action.ICostantiFascicoloSiepe;
import f3b.log.LogF3B;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadInserisciRichiesta</p>
* <p>Description: Classe Action per la load inserisci di Richiesta</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadInserisciRichiesta extends ActRicercaFasSiepePuntuale
implements ICostantiRichiesta
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

    // STUB : 2006-09-25 ( pensare dove è meglio inserire il controllo )
    //if (this.isRequestParameterNullObj("ritorno"))
    if ( !this.isRequestParameterNullObj(ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO ) )
    {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Si è chiamata la classe dalla voce di menù!");
      // Invoca la process Request della superclasse se si proviene dal menu'.
      super.processRequest();
    }

    // Elenco tipiRichiedente CBX.
    Option lOption = new Option( DecodificheUtils.getDecodificheFiltrateByCodAlt(DecodificheManager.getInstance().getTipoRichiedenteSiepe(), "S" ) );
    setRequestAttribute("elencoTipiRichiedenteSoggetto", "" + lOption ); // Da Sostitutire con quello appropriato

    // Elenco tipiRichiedente CBX.
    lOption = new Option( DecodificheUtils.getDecodificheFiltrateByCodAlt(DecodificheManager.getInstance().getTipoRichiedenteSiepe(), "U" ) );
    setRequestAttribute("elencoTipiRichiedenteUfficio", "" + lOption ); // Da Sostitutire con quello appropriato

    // Elenco Tipi Richieste CBX entità soggetto.
    lOption = new Option( DecodificheUtils.getDecodificheFiltrateByCodAlt(DecodificheManager.getInstance().getTipoRichiestaSiepe(), "S" ) );
    setRequestAttribute("elencoTipiRichiestaSoggetto", "" + lOption );

    // Elenco Tipi Richieste CBX entità ufficio.
    lOption = new Option( DecodificheUtils.getDecodificheFiltrateByCodAlt(DecodificheManager.getInstance().getTipoRichiestaSiepe(), "U" ) );
    setRequestAttribute("elencoTipiRichiestaUfficio", "" + lOption );

    // Elenco Destinatari per tipo ufficio CBX.
    lOption = new Option( DecodificheManager.getInstance().getTipoUfficio());
    String[] lCodes = {"-","TDS","UDS","UEPE","PM", "PGCAP", "UEPESS"};
    lOption.setFilter( lCodes ); // Imposta il filtro di uguaglianza.
    setRequestAttribute("elencoDestinatari", "" + lOption );   // Da sostituire con quello appropriato.

    setRequestAttribute("FlagFasSiepe", "SI");
    setRequestAttribute("modalita", "I"); // Imposta Modalità.

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

    return PG_LOAD_INSERISCIRICHIESTA;  //restituisce la jsp di VIEW
  }
}