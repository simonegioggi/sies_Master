package siap.siep.misurasicurezza.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.jms.action.ICostantiSicoJMS;
import siap.sico.web.ActionSiap;
import siap.sius.presaincarico.action.ICostantiPresaincarico;
import f3b.log.LogF3B;
import f3b.web.html.Option;


/**
 * 
 * @author d.fiorletta
 *
 */
public class ActLoadRicercaAttiTrasmessi extends ActionSiap implements ICostantiMisuraSicurezza 
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception 
  {
    //========================
    // Dati per le combo
    //========================
    Option lOption = null;
    
    // Tipo UFFICIO Destinatario    
    lOption = new Option(DecodificheManager.getInstance().getTipoUfficioPM());
    String[] lFiltroUffici =  {"-", "PM", "PMM"}; // i destinatari sono solo PM e PMM
    lOption.setFilter(lFiltroUffici);
    
    if (   !isRequestParameterNullObj(ICostantiPresaincarico.CAMPO_COD_TIPO_UFFICIO)
        && getRequestStringParameter(ICostantiPresaincarico.CAMPO_COD_TIPO_UFFICIO).length()>0)
    {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("tipo ufficio"+getRequestStringParameter(ICostantiPresaincarico.CAMPO_COD_TIPO_UFFICIO));
      lOption.setSelected(getRequestStringParameter(ICostantiPresaincarico.CAMPO_COD_TIPO_UFFICIO));
    }
    //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    //siesLogger.debug("lOption = "+lOption);
    setRequestAttribute("ufficioPM", "" + lOption );
    
    
    // Imposta ESITO
    Vector <DecodificheModel> lEsiti = new Vector <DecodificheModel>();
    lEsiti.add(new DecodificheModel("TUTTI","TUTTI","","","","","","","") );
    lEsiti.add(new DecodificheModel("01001","PRESI IN CARICO","","","","","","","") );
    lEsiti.add(new DecodificheModel("01003","RESTITUITI AL MITTENTE","","","","","","","") );
    //lEsiti.add(new DecodificheModel("01004","INOLTRATI PER COMPETENZA","","","","","","","") );
    lEsiti.add(new DecodificheModel("01005","ISCRITTI IN CLASSE IV","","","","","","","") );
    lEsiti.add(new DecodificheModel("-","<font color='red'>in attesa di risposta</font>","","","","","","","") );
    
    Option lOptionEsiti = new Option( lEsiti);
    //lOptionEsiti.setSelected("-"); // default in attesa di risposta    
    lOptionEsiti.setSelected("TUTTI"); // default TUTTI    
    if (   !isRequestParameterNullObj(ICostantiSicoJMS.CAMPO_TIPO_ESITO)
        && getRequestStringParameter(ICostantiSicoJMS.CAMPO_TIPO_ESITO).length()>0)
    {
      lOptionEsiti.setSelected(getRequestStringParameter(ICostantiSicoJMS.CAMPO_TIPO_ESITO));
    }
    setRequestAttribute("listaEsiti", "" + lOptionEsiti );
    
    return PG_LOAD_RICERCA_ATTI_TRASMESSI;
  }
}