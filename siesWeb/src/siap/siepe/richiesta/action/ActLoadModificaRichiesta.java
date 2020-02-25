package siap.siepe.richiesta.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.web.ActionSiap;
import siap.siepe.richiesta.controller.IRichiesta;
import siap.siepe.richiesta.model.RichiestaModel;
import siap.siepe.util.SIEPELookupRemote;
import f3b.log.LogF3B;
import f3b.web.html.Option;


/**
* <p>Title: ActLoadModificaRichiesta</p>
* <p>Description: Classe Action per la load della form di modifica della Richiesta</p>
* <p>Copyright: Copyright (c) 2004</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadModificaRichiesta extends ActionSiap implements ICostantiRichiesta
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

    String lId = getRequestStringParameter(CAMPO_ID_RICHIESTA);

    // chiama il controller
    IRichiesta lCtrl = SIEPELookupRemote.getRichiestaRemote();
    // riempie il model
    RichiestaModel lRicMod = lCtrl.ExRicercaRichiestaByKey(new BigDecimal(lId));

    // Imposta il radio Button in virtù del tipo del richiedente ( Soggeto o Ufficio )
    String lCodAlt = DecodificheUtils.getCodAltebyCode( DecodificheManager.getInstance().getTipoRichiedenteSiepe(), lRicMod.getCodTipoRichiedente() );
    setRequestAttribute ( "tipoChecked", lCodAlt ); // Imposta in request il valore restiuto del cod Tipo Richiedente

    // Elenco tipiRichiedente CBX soggetto.
    Option lOption = new Option( DecodificheUtils.getDecodificheFiltrateByCodAlt(DecodificheManager.getInstance().getTipoRichiedenteSiepe(), "S" ) );
    lOption.setSelected( lRicMod.getCodTipoRichiedente() ); // Imposta il selected alla Option
    setRequestAttribute("elencoTipiRichiedenteSoggetto", "" + lOption );

    // Elenco tipiRichiedente CBX ufficio.
    lOption = new Option( DecodificheUtils.getDecodificheFiltrateByCodAlt(DecodificheManager.getInstance().getTipoRichiedenteSiepe(), "U" ) );
    lOption.setSelected( lRicMod.getCodTipoRichiedente() ); // Imposta il selected alla Option
    setRequestAttribute("elencoTipiRichiedenteUfficio", "" + lOption );

    // Elenco Tipi Richieste CBX entità soggetto.
    lOption = new Option( DecodificheUtils.getDecodificheFiltrateByCodAlt(DecodificheManager.getInstance().getTipoRichiestaSiepe(), "S" ) );
    lOption.setSelected( lRicMod.getCodTipoRichiesta() ); // Imposta il selected alla Option
    setRequestAttribute("elencoTipiRichiestaSoggetto", "" + lOption );

    // Elenco Tipi Richieste CBX entità ufficio.
    lOption = new Option( DecodificheUtils.getDecodificheFiltrateByCodAlt(DecodificheManager.getInstance().getTipoRichiestaSiepe(), "U" ) );
    lOption.setSelected( lRicMod.getCodTipoRichiesta() ); // Imposta il selected alla Option
    setRequestAttribute("elencoTipiRichiestaUfficio", "" + lOption );

    // Elenco Destinatari per tipo ufficio CBX.
    lOption = new Option( DecodificheManager.getInstance().getTipoUfficio());
    String[] lCodes = {"-","TDS","UDS","UEPE","PM", "PGCAP", "UEPESS"};
    lOption.setFilter( lCodes ); // Imposta il filtro di uguaglianza.
    // Ricava il tipo Ufficio dal codice ufficio
    String lCodTipoUfficio =  getUfficioByCodUfficio( lRicMod.getCodUfficioDestinatario() ).getCodTipoUfficio();
    lOption.setSelected( lCodTipoUfficio ); // Imposta il selected alla Option
    setRequestAttribute("elencoDestinatari", "" + lOption );

    setRequestAttribute("FlagFasSiepe", "SI"); // Imposta il flag per abilitare la visualizzazione dei dati di sintesi SIEPE
    setRequestAttribute("modalita","M"); // Imposta la modalità di modifica

    setRequestAttribute("richiesta", lRicMod);

    //this.gestioneRitorno();

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

    return PG_LOAD_INSERISCIRICHIESTA;
  }
}