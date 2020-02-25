package siap.siep.penasospesa.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IComune;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.log.LogF3B;

/**
 * <p>Title: ActDettaglioAnnotazioneTermini</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: Agile</p>
 * <p> @author Luigi</p>
 * @version 1.0
 */
public class ActDettaglioAnnotazioneTermini extends ActSIESDettaglioProvvedimento implements ICostantiPenaSospesa
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

  public String processRequest() throws Exception
  {
	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.debug("ActDettaglioAnnotazioneTermini: inizio");

	    // id dell'evento inserito
	    BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
	    
	   // STUB: initile cercare anche le notifiche basta solo l'evento.
	    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
	    EventoNotificaModel lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lIdEvento);
	    setRequestAttribute("eventonotifica", lEveMod);

        IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
        AnnotazioneManualeModel lAnnMod = lCtrlAnn.ExRicercaAnnotazioniManualiByIdEvento(lIdEvento);
        setRequestAttribute("annotazioneManuale", lAnnMod);
       
        String lDescTipoUff = DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoUfficio(), lAnnMod.getCodTipoUfficioSiep());
        setRequestAttribute("strDescrTipoUfficio", lDescTipoUff);
        
        IComune comctrl=SICOLookupRemote.getComuneRemote();
        ComuneModel comunemod = comctrl.ExRicercaComuneByKey(lAnnMod.getCodLuogoUfficioSiep());
        setRequestAttribute("strDescrComune", comunemod.getDescrizione());

        
	// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	siesLogger.debug("ActDettaglioAnnotazioneTermini: fine");

    return PG_DETTAGLIO_ANNOTAZIONETERMINI;
  }
 
  
  
}