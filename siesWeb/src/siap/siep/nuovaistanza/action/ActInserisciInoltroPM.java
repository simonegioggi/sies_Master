package siap.siep.nuovaistanza.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.model.EventoModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.nuovaistanza.controller.INuovaIstanza;
import siap.siep.nuovaistanza.model.NuovaIstanzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * 
 * @author Giselda De Vita
 *
 */
public class ActInserisciInoltroPM extends ActionSiap {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
	
	public String processRequest() throws F3BException
	 {
		
		String lCodMagistrato = this.getRequestStringParameter("CodMagistrato");
		BigDecimal lIstanza = this.getRequestBigDecimalParameter("IdNuovaIstanza");
		BigDecimal lEventoIstanza = this.getRequestBigDecimalParameter("IdEventoNuovaIstanza");
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
	    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();
		

		if (lFascMod.getCodStatoFascicolo().compareTo("01")==0)
    	  	throw new F3BException(F3BException.USER_MESSAGE, "Attenzione : Il fascicolo indicato è già archiviato! Impossibile procedere.");

				      
	      /* devo inserire un nuovo evento con tipo_evento=03,
	       * tipo_provvedimento=08 e codice motivo=1001,
	       * e modificare il rec dell'istanza con la data di inoltro
	       * poi visualizzare la pagina del dettaglio istanza con i bottoni
	       * per la validazione con e senza stampa
	      */
	       
		EventoModel lEveMod = new EventoModel();
	    lEveMod.setCodTipoEvento("03");        
	    lEveMod.setCodTipoProvvedimento("08");
	    lEveMod.setCodMotivo("1001");

	    lEveMod.setEveIdEvento(lEventoIstanza);
	    
	    lEveMod.setFlagDocumentoRegistrato("N");
	    lEveMod.setFlagVideoSiep("N");
	    lEveMod.setFlagStampaSiep("N");
	    lEveMod.setFasSieIdFascicoloSiep(lIdFascicolo);
	    lEveMod.setDataInserimento(DateUtils.getSysDate());  
	    lEveMod.setCodOperatoreInserimento(getCodUtenteConnesso());
	    lEveMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());  
	    // 11/07/2011 La Data Emissione va inserita nel formato dd/MM/yyyy
	  	// lEveMod.setDataEmissione(DateUtils.getSysDate());
	    lEveMod.setDataEmissione(DateUtils.getDate(DateUtils.getSysDate("dd/MM/yyyy"), "dd/MM/yyyy"));
	    lEveMod.setCodEsito("-");
	    lEveMod.setCodMagistrato(lCodMagistrato);
	    lEveMod.setCodLuogoDestinatario("-");
	    lEveMod.setCodTipoUfficioDestinatario("-");
		lEveMod.setCodLuogoEmittente(getCodComuneUtenteConnesso());
		lEveMod.setCodUfficioEmittente(getCodUfficioUtenteConnesso());		  

		NuovaIstanzaModel lNuoMod = new NuovaIstanzaModel();
		lNuoMod.setIdNuovaIstanza(lIstanza);
	    lNuoMod.setDataAggiornamento(DateUtils.getSysDate());  
	    lNuoMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
	    lNuoMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); 
	    
	    lNuoMod.setDataInoltroPM(getRequestDateParameter(ICostantiNuovaIstanza.CAMPO_ANNO_DATA_INOLTRO_PM,
	    		ICostantiNuovaIstanza.CAMPO_MESE_DATA_INOLTRO_PM,
	    		ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_INOLTRO_PM));
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.info(">>>>anno>>>>> "+ getRequestDateParameter(ICostantiNuovaIstanza.CAMPO_ANNO_DATA_INOLTRO_PM,
	    		ICostantiNuovaIstanza.CAMPO_MESE_DATA_INOLTRO_PM,
	    		ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_INOLTRO_PM));

	    //lNuoMod.setCodTipoUfficioDestinatario("-");
	    //lNuoMod.setCodLuogoDestinatario("-");

	    INuovaIstanza lCtrl = SIEPLookupRemote.getNuovaIstanzaRemote();
	    NuovaIstanzaModel lNuoRetMod = new NuovaIstanzaModel();
	    lNuoRetMod=lCtrl.ExInoltraNuovaIstanza(lEveMod,lNuoMod);
	    
	    String lPage="";
	    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.nuovaistanza.action.ActLoadDettaglioNuovaIstanza";
	    lPage += "&" + "IdEvento" + "=" + lNuoRetMod.getEveIdEvento().toString();
	    lPage += "&" + "TipoVis" + "=Inoltro";
	    lPage += "&AzioneChiamante=siap.siep.nuovaistanza.action.ActInserisciInoltroPM";

	    return lPage;
	 }
	
	
	

}