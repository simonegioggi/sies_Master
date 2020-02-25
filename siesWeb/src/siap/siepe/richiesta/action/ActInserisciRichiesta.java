package siap.siepe.richiesta.action;


/**
* <p>Title: ActInserisciRichiesta</p>
* <p>Description: Classe Action per l'inserimento di Richiesta</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel;
import siap.siepe.richiesta.controller.IRichiesta;
import siap.siepe.richiesta.model.RichiestaModel;
import siap.siepe.util.SIEPELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;


public class ActInserisciRichiesta extends ActionSiap implements ICostantiRichiesta
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
   * Azione di Inserimento del Richiesta
   * <p>
   * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
   * @throws F3BException propaga errore di eccezione.
   */
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

    String lCodTipoUfficioDest = getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO_DESTINATARIO);
    String lDescrComune = getRequestStringParameter(CAMPO_SEDE);
    //
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( "lCodTipoUfficioDest : " + lCodTipoUfficioDest );
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( "lDescrComune : " + lDescrComune );
    // Preleva il codice d'ufficio per la coppia Descrizione Comune e Codice
    // Tipo Ufficio.
    String lCodUfficioDest = getCodUfficioByCodTipoUfficioDescrComune( lCodTipoUfficioDest, lDescrComune );

    RichiestaModel lRicMod = new RichiestaModel();
    FascicoloSiepeEstesoModel lFasEsteso = (FascicoloSiepeEstesoModel) this.getSessionAttribute("FascicoloSiepeEsteso");

    lRicMod.setDataRichiesta(getRequestDateParameter(CAMPO_ANNO_DATA_RICHIESTA,CAMPO_MESE_DATA_RICHIESTA, CAMPO_GIORNO_DATA_RICHIESTA));
    lRicMod.setCodTipoRichiesta(getRequestStringParameter(CAMPO_COD_TIPO_RICHIESTA));
    lRicMod.setCodTipoRichiedente(getRequestStringParameter(CAMPO_COD_TIPO_RICHIEDENTE));
    lRicMod.setNote(getRequestStringParameter(CAMPO_NOTE));

    lRicMod.setCodOperatoreInserimento(super.getCodUtenteConnesso());
    lRicMod.setCodUfficioInserimento(super.getCodUfficioUtenteConnesso());
    lRicMod.setDataInserimento(DateUtils.getSysDate());
    lRicMod.setFasSieIdFasSiepe(lFasEsteso.getFascicoloSiepe().getIdFascicoloSiepe());
    lRicMod.setCodUfficioDestinatario( lCodUfficioDest );

    // Stampa il contenuto del Model
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( "lRicMod : " + lRicMod.toString() );

    //---Aggiungere in SIEPELookupRemote il metodo getRichiestaRemote() --//
    IRichiesta lCtrl = SIEPELookupRemote.getRichiestaRemote();
    RichiestaModel lRicModRet = lCtrl.ExInserisciRichiesta(lRicMod); // setta la risposta nella request

    setRequestAttribute("richiesta", lRicModRet);

    // Prepara la pagina di destinazione.
    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" +
        IWebConstants.ACTION_FIELD +"=siap.siepe.richiesta.action.ActLoadDettaglioRichiesta&" +
        CAMPO_ID_RICHIESTA + "=" + lRicModRet.getIdRichiesta().toString();

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

    return lPage;
  }
}