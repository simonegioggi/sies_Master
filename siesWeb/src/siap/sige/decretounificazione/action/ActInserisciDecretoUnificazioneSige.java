package siap.sige.decretounificazione.action;

import java.util.Date;

import siap.sico.web.ActionSiap;
import siap.sige.decretounificazione.controller.IDecretoUnificazioneSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>Title: ActInserisciDecretoUnificazioneSige</p>
 * <p>Description: Classe Action di inserimento del Decreto di Unificazione di due Procedimenti SIGE
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Bull</p>
 */

public class ActInserisciDecretoUnificazioneSige extends ActionSiap implements ICostantiDecretoUnificazioneSige
{
  public String processRequest() throws Exception
  {

    // Recupero della data di Unificazione digitata e del numero dei fascicoli da unificare.
    Date dataUnificazione = getRequestDateParameter(CAMPO_DATA_AAAA_UNIFICAZIONE, CAMPO_DATA_MM_UNIFICAZIONE, CAMPO_DATA_GG_UNIFICAZIONE);
    String annoDaUnif = getRequestStringParameter( CAMPO_ANNO_DA_UNIF);
    String numeroDaUnif = getRequestStringParameter( CAMPO_NUMERO_DA_UNIF);
    String annoUnificante = getRequestStringParameter( CAMPO_ANNO_UNIFICANTE);
    String numeroUnificante = getRequestStringParameter( CAMPO_NUMERO_UNIFICANTE);

    // Unificazione dei 2 Procedimenti.
    IDecretoUnificazioneSige lCtrl = SIGELookupRemote.getDecretoUnificazioneSigeRemote();
    ProvvedimentoSigeModel lProvvUnificazione = lCtrl.ExInserisciDecretoUnificazioneSige(annoDaUnif, numeroDaUnif, annoUnificante, numeroUnificante, getCodUfficioUtenteConnesso(), getCodUtenteConnesso(), getCodComuneUtenteConnesso(), dataUnificazione );

    //restituisce la jsp di VIEW.
    // Nella request viaggia l'ID dell'Evento di Unificazione appena inserito.
 // restituisce la jsp di VIEW.
    // Nella request viaggia l'ID dell'Evento di Unificazione appena inserito.
    RedirectTo lRedir = new RedirectTo();
	lRedir.setPage(IWebConstants.PG_MAIN);
	lRedir.setAction("siap.sige.decretounificazione.action.ActLoadDettaglioDecretoUnificazioneSige");
	lRedir.setParameter(CAMPO_ID_DECRETO_UNIFICAZIONE, lProvvUnificazione.getIdProvvedimentoSige().toString());
	lRedir.setParameter("FlagIns", "Y");

	// Torna alla pagina di dettaglio parti senza aggiungerla allo stack
	lRedir.setParameter(IWebConstants.LINK_RITORNO, "20");
    
    return lRedir.toString(); 
    
    //return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sige.decretounificazione.action.ActLoadDettaglioDecretoUnificazioneSige&"+CAMPO_ID_DECRETO_UNIFICAZIONE+"="+lProvvUnificazione.getIdProvvedimentoSige().toString()+"&FlagIns=Y";
  }
}
