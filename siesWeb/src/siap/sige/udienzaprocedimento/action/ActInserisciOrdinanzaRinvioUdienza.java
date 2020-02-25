package siap.sige.udienzaprocedimento.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sige.aula.action.ICostantiAula;
import siap.sige.aula.controller.IAula;
import siap.sige.aula.model.AulaUdienzaModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;

/**
 * <p>Title: ActInserisciOrdinanzaRinvioUdienza </p>
 * <p>Description: Classe Action per l'inserimento dell' Ordinanza Rinvio Udienza.</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: Agileservizi S.r.l.</p>
 * @version 1.0
 */
public class ActInserisciOrdinanzaRinvioUdienza extends ActInserisciRinvioUdienza 
implements ICostantiUdienzaProcedimentoSige
{ 
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: inizio" );
      
    mEvento.setCodTipoEvento("01");                       // Tipo Evento = Provvedimento
    mEvento.setCodTipoProvvedimento("03");                // Tipo Provvedimento = Ordinanza 
    mEvento.setCodEsito("0603");                          // Ordinanza Rinvio Udienza.
    mEvento.setTemIdTemplate("OR1");
    
    mProvModel.setCodTipoProvvedimento("03");             // Tipo Provvedimento = Ordinanza
    mProvModel.setCodTipoProvvedimentoSige("04");         // Tipo Provvedimento = Ordinanza Rinvio
        
    mActionRet = 
      "siap.sige.udienzaprocedimento.action.ActLoadDettaglioOrdinanzaRinvioUdienza";
    
    BigDecimal idAula=null;
    try {
        idAula=super.getRequestBigDecimalParameter(ICostantiAula.CAMPO_ID_AULA);
    } catch (Exception e) {
    	
    }
    if (idAula != null) {
    	IAula ictrl = SIGELookupRemote.getAulaRemote();
    	AulaUdienzaModel aulaModel=ictrl.ExRicercaAulaByIdAula(idAula);
    	super.setRequestAttribute("aula", aulaModel);
    }
    String lPage = super.processRequest();
   
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: fine" );
    
    return lPage;
  }
  
  
  
  
  
 
}