package siap.sius.provvedimento.action;


import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>Title: ActLoadDettaglioAltriProvvedimenti </p>
 * <p>Description: Classe Responsabile della visualizzazione dati di dettaglio
 * di un evento generato dalla funzionalità della richiesta parere.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class ActLoadDettaglioAltriProvvedimenti
extends ActionSius implements ICostantiProvvedimento
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( "" + getClass().getName() + " .processRequest: inizio " );
    
    // Reindirizzamento alla pagina di dettaglio specifica del tipo di altro provvedimento

    String lRetPage = PG_ELENCOPROVVEDIMENTI;
    RedirectTo lPage = new RedirectTo();
    lPage.setPage(IWebConstants.PG_MAIN);
    
    BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    EventoModel lEve = lCtrl.ExRicercaEventoByKey(lIdEvento);
    if(lEve == null)
        throw new SIUSException( SIUSException.USER_MESSAGE, "Dato Assente !" );

    lPage.setAction("siap.sius.produzioneatti.action.ActLoadDettaglioRichiestaParere");
    
    if (lEve.getCodTipoEvento().compareTo("08") == 0 )    
    	lPage.setAction("siap.sius.produzioneatti.action.ActLoadDettaglioRichiestaParere");
    
    if (lEve.getCodTipoEvento().compareTo("13") == 0 )    
    	lPage.setAction("siap.sius.sanzionesostitutiva.action.ActLoadDettaglioSanzioneSostitutivaUDS");
    
    if (lEve.getCodTipoEvento().compareTo("20") == 0 )    
    	lPage.setAction("siap.sius.misurasicurezza.action.ActLoadDettaglioMisuraSicurezzaUDS");

    lRetPage = lPage.toString();
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( "" + getClass().getName() + " .processRequest: fine " );

    return lRetPage;
  }
}