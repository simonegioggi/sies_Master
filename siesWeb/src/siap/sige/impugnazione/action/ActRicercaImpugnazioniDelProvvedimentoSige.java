package siap.sige.impugnazione.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;

/**
* <p>Title: ActRicercaImpugnazioniDelProvvedimentoSige</p>
* <p>Description: Classe Action per la load dell'Elenco Impugnazioni Sige</p>
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActRicercaImpugnazioniDelProvvedimentoSige extends ActionSige implements ICostantiImpugnazioneSige {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: inizio" );
    this.setLinkRitorno();
    
    // Recupero del Provvedimento
    IProvvedimentoSige lCtrlPS = SIGELookupRemote.getProvvedimentoRemote();
    ProvvedimentoSigeEventoModel lPSMod = lCtrlPS.ExRicercaProvvedimentoById(getRequestBigDecimalParameter(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE));
    
    if (!super.isRequestParameterNullObj (CAMPO_COD_TIPO_IMPUGNAZIONE))
    	super.setSessionAttribute ("codTipoImpugnazione", super.getRequestStringParameter(CAMPO_COD_TIPO_IMPUGNAZIONE));
    
    String lCodTipoImpugnazione = (String) getSessionAttribute("codTipoImpugnazione");
    Vector <ImpugnazioneSigeModel>lImpugnazioni= (lCodTipoImpugnazione.equals("01")?lPSMod.getRicorsi():lPSMod.getOpposizioni());
    setRequestAttribute("impugnazioni", lImpugnazioni);

    BigDecimal countImpugnazioni = new BigDecimal ( lImpugnazioni.size());
    setRequestAttribute("numero_Impugnazioni", countImpugnazioni.toString());
    setRequestAttribute("provvedimento", lPSMod);
    
    String showComboTemplate="true";
    if (!super.isRequestParameterNullObj(CAMPO_SHOW_COMBO_TEMPLATE))
    	showComboTemplate=super.getRequestStringParameter(CAMPO_SHOW_COMBO_TEMPLATE);
    
    setRequestAttribute("showComboTemplate", showComboTemplate );
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: fine" );
     return PG_ELENCOIMPUGNAZIONISIGE;
    //return PG_ELENCOIMPUGNAZIONISIGEPROVVEDIMENTO;
  }
}