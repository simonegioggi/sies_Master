package siap.siep.penasospesa.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.log.LogF3B;

/**
 * <p>Title: ActDettaglioRicEstinzioneReato</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: Agile</p>
 * <p> @author Luigi</p>
 * @version 1.0
 */
public class ActDettaglioRicEstinzioneReato extends ActSIESDettaglioProvvedimento implements ICostantiPenaSospesa
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

  public String processRequest() throws Exception
  {
	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.debug("ActDettaglioRicEstinzioneReato: inizio");

	    // id dell'evento inserito
	    BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);


	    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
	    EventoNotificaModel lEveMod = new EventoNotificaModel();
	    lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
	    setRequestAttribute("eventonotifica", lEveMod);
	
	// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	siesLogger.debug("ActDettaglioRicEstinzioneReato: fine");

    return PG_DETTAGLIO_RICHIESTAESTINZIONEREATO;
  }
 
  
  
}