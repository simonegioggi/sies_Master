package siap.sige.impugnazione.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sige.SIGEException;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.impugnazione.controller.IImpugnazioneSige;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActStampaImpugnazioneSige extends ActionSige implements ICostantiImpugnazioneSige
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
	    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");
	    
	    // Fascicolo Sige Esteso in sessione.
	    FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel)getSessionAttribute("FascicoloSigeEsteso");

	    if (isRequestParameterNullObj(ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE ))
	        throw new SIGEException( SIGEException.USER_MESSAGE, "Impugnazione Sige non presente!" );

	    BigDecimal lIdImpugnazione = getRequestBigDecimalParameter(ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE);
	    
	    // Preleva dalla sessione i dati dell'utente connesso.

	    UfficioModel lUfficio    = getUfficioUtenteConnesso();
	    
	    String lCodTemplate = "";
	    if (!isRequestParameterNullObj(ICostantiImpugnazioneSige.CAMPO_COD_TEMPLATE))
	    		lCodTemplate = getRequestStringParameter(ICostantiImpugnazioneSige.CAMPO_COD_TEMPLATE);

	    // Crea il ByteArrayOutputStream
	    IImpugnazioneSige lCtrl = SIGELookupRemote.getImpugnazioneSigeRemote();
	    ByteArrayOutputStream lReport = lCtrl.ExStampaImpugnazioneSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige(), lIdImpugnazione, lCodTemplate, lUfficio.getCodUfficio(), super.getUtenteConnesso() );
	    setRequestAttribute("report", lReport);
	    //Prepara la pagina di destinazione
	    if (lReport == null)
	        throw new SIGEException(SIGEException.USER_MESSAGE, "Nessun documento è stato generato!");

	    
	    IImpugnazioneSige ctrlImp = SIGELookupRemote.getImpugnazioneSigeRemote();
	    ImpugnazioneSigeModel impugnazione= ctrlImp.ExRicercaImpugnazioneByKey(lIdImpugnazione);
	    
	    EventoModel lEvento = null;
	    if(impugnazione != null && impugnazione.getProvvedimentoSigeGenerato() != null && 
	       impugnazione.getProvvedimentoSigeGenerato().getEventoNotifica() != null && 
	       impugnazione.getProvvedimentoSigeGenerato().getEventoNotifica().getEvento() != null) {
	    		lEvento = impugnazione.getProvvedimentoSigeGenerato().getEventoNotifica().getEvento();
	    } else {
	    	if(impugnazione != null && impugnazione.getProvvedimentoSige() != null && 
	    	   impugnazione.getProvvedimentoSige().getEventoNotifica() != null && 
	    	   impugnazione.getProvvedimentoSige().getEventoNotifica().getEvento() != null) {
	    	   lEvento = impugnazione.getProvvedimentoSige().getEventoNotifica().getEvento();
	    	}
	    }
	    
	    IEvento evCtrl= SICOLookupRemote.getEventoRemote();
	    lEvento.setDataAggiornamento(DateUtils.getSysDate());
        lEvento.setCodUfficioAggiornamento(lUfficio.getCodUfficio());
        lEvento.setCodOperatoreAggiornamento(super.getCodUtenteConnesso());
        //lEvento.setFlagDocumentoRegistrato("N");
        evCtrl.ExModificaEvento(lEvento);
        ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lReport.toByteArray());
        lEvento.setDocBlobIn(lByteArrayInput);
        evCtrl.ExUpdateDocument(lEvento);
	    
	    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");
	    
	    return IWebConstants.PG_DOWNLOAD_NEW;
	  }
}