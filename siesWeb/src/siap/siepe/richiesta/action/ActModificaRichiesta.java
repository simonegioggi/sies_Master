package siap.siepe.richiesta.action;


/**
* <p>Title: ActModificaRichiesta</p>
* <p>Description: Classe Action per la modifica di Richiesta</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import org.apache.log4j.Logger;

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siepe.richiesta.controller.IRichiesta;
import siap.siepe.richiesta.model.RichiestaModel;
import siap.siepe.util.SIEPELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;



public class ActModificaRichiesta extends ActionSiap implements ICostantiRichiesta
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
   * Azione di Modifica del Richiesta
   * <p>
   * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
   * @throws Exception propaga errore di eccezione.
   */
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

    String lCodTipoUfficioDest = getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO_DESTINATARIO);
    String lDescrComune = getRequestStringParameter(CAMPO_SEDE);
    // Preleva il codice d'ufficio per la coppia Descrizione Comune e Codice
    // Tipo Ufficio.
    String lCodUfficioDest = getCodUfficioByCodTipoUfficioDescrComune( lCodTipoUfficioDest, lDescrComune );

    // riempie il model
    RichiestaModel lRicMod = new RichiestaModel ();

    lRicMod.setIdRichiesta( getRequestBigDecimalParameter( CAMPO_ID_RICHIESTA) );
    lRicMod.setDataRichiesta( getRequestDateParameter( CAMPO_ANNO_DATA_RICHIESTA,CAMPO_MESE_DATA_RICHIESTA,CAMPO_GIORNO_DATA_RICHIESTA) );
    lRicMod.setCodTipoRichiesta(getRequestStringParameter(CAMPO_COD_TIPO_RICHIESTA));
    lRicMod.setCodTipoRichiedente(getRequestStringParameter(CAMPO_COD_TIPO_RICHIEDENTE));
    lRicMod.setCodUfficioDestinatario(lCodUfficioDest);
    lRicMod.setNote(getRequestStringParameter(CAMPO_NOTE));

    // Da Sostuire con i metodi ereditati dal ActionSiap che ricavano i dati di sessione.
    UtenteModel lUtenteMod = new UtenteModel( (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
    lRicMod.setCodOperatoreAggiornamento(lUtenteMod.getUserId());
    lRicMod.setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());
    lRicMod.setDataAggiornamento(DateUtils.getSysDate());

    // chiama il controller
    IRichiesta lCtrl = SIEPELookupRemote.getRichiestaRemote();
    RichiestaModel lRicModRet = lCtrl.ExModificaRichiesta(lRicMod);

    setRequestAttribute("modalita", "M");
    setRequestAttribute("richiesta", lRicModRet);

    //this.gestioneRitorno();

    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" +
            IWebConstants.ACTION_FIELD + "=siap.siepe.richiesta.action.ActLoadDettaglioRichiesta&"+CAMPO_ID_RICHIESTA+"="+ lRicModRet.getIdRichiesta().toString();

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

    return lPage;
  }
}