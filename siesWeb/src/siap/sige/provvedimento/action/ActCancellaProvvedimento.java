package siap.sige.provvedimento.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sige.SIGEException;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;

 /**
 * <p>Title: ActCancellaProvvedimento</p>
 * <p>Description: Classe Action per la cancellazione del Provvedimento Sige</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Eutelia</p>
 * @version 1.0
 */

public class ActCancellaProvvedimento extends ActLoadEmissioneOrdinanza
implements ICostantiProvvedimentoSige
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
	 // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 siesLogger.debug(getClass().getName() + ".processRequest : inizio");
	    
	 gestioneRitorno();
	 
	if (this.isRequestParameterNullObj(CAMPO_ID_PROVVEDIMENTO_SIGE))
			throw new SIGEException(SIGEException.USER_MESSAGE,  "Manca ID");
	
	BigDecimal lIdProvvedimento = getRequestBigDecimalParameter(CAMPO_ID_PROVVEDIMENTO_SIGE);

    // Impostazione ProvvedimentoModel
    ProvvedimentoSigeModel lProvModel = new ProvvedimentoSigeModel();
    lProvModel.setIdProvvedimentoSige(lIdProvvedimento);
    lProvModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());  //Codice dell'operatore che inserisce
    lProvModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); //Codice dell'ufficio dell'operatore che inserisce
    lProvModel.setDataAggiornamento(DateUtils.getSysDate());
	
	 // Controller del Provvedimento SIGE
	 IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
	 
	 // Cancellazione
	lCtrlProv.ExCancellaProvvedimentoSige(lProvModel);

	 // Ritorno al punto di partenza
	// String lRetPage = ritornoDopoCancellazione("Il Provvedimento è stato cancellato !", null);
  
	 // setta la risposta nella request
	// setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Provvedimento è stato cancellato !");
    // setRequestAttribute(IWebConstants.GOTO_PAGE, ISIAPCostantiWeb.PG_PAGINA_VUOTA);
	
	FascicoloSigeEstesoModel sesFascicoloEsteso=super.getFascicoloSigeEstesoInSessione();
	
	IFascicoloSige lCtrl = SIGELookupRemote.getFascicoloSigeRemote();
	FascicoloSigeEstesoModel fascicoloEsteso = lCtrl.ExRicercaEstesaFascicoloSigeByKey(sesFascicoloEsteso.getFascicoloSige().getIdFascicoloSige());
    setSessionAttribute("FascicoloSigeEsteso", fascicoloEsteso);
    String lRetPage =  ritornoDopoCancellazione("Il Provvedimento è stato cancellato !", null);	 
	// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	siesLogger.debug(getClass().getName() + ".processRequest : fine");
	return lRetPage; //restituisce la jsp di VIEW

  }
}