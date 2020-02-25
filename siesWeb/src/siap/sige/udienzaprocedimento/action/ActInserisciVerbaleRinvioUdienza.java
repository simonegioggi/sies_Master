package siap.sige.udienzaprocedimento.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;

/**
 * <p>Title: ActInserisciVerbaleRinvioUdienza </p>
 * <p>Description: Classe Action per l'inserimento del Rinvio Udienza da Verbale</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: Eutelia S.p.A.</p>
 * @version 1.0
 */
public class ActInserisciVerbaleRinvioUdienza extends ActInserisciRinvioUdienza 
implements ICostantiUdienzaProcedimentoSige
{   
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: inizio" );
    
    mEvento.setCodTipoEvento("01");                       // Tipo Evento = Provvedimento
    mEvento.setCodTipoProvvedimento("03");                 
    mEvento.setCodEsito("0603");                          // Rinvio Udienza.

    mProvModel.setCodTipoProvvedimento("03");             // Tipo Provvedimento = Ordinanza
    mProvModel.setCodTipoProvvedimentoSige("50");         // Tipo Provvedimento = Verbale
    
    mActionRet = 
      "siap.sige.udienzaprocedimento.action.ActLoadDettaglioVerbaleRinvioUdienza";
    
    String lPage = super.processRequest();
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: fine" );
      
    return lPage;
  }
}