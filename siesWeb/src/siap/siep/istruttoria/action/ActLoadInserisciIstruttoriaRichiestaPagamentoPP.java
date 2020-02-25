package siap.siep.istruttoria.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
 *
 * <p>Title: ActLoadInserisciIstruttoriaRichiestaPagamentoPP</p>
 * <p>Description: Classe per la Lod di Inserimento Richiesta/istruttoria</p> 
 * <p> 				notizie di Pagamento Pena Pecuniaria/p> 
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActLoadInserisciIstruttoriaRichiestaPagamentoPP extends ActionSiap implements ICostantiIstruttoria
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	//private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {

    if (isSessionAttributeNullObj("fascicolo"))
    {
      String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
        "=siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
        ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" +
        "siap.siep.istruttoria.action.ActLoadInserisciPosizioneGiuridica";

      return lPage;
    }

    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    this.isFascicoloSiepDiCompetenza();
    this.isEventoNonValidato();
    
    // Se vengo da IstruttoriaCUMULO
    if(!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) )
    {
    	setRequestAttribute("IdIstruttoriaCumulo", getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));
    	//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    	//siesLogger.debug("--XX-- ActLoadInserisciIstruttoriaRichiestaPagamentoPP - ID_ISTRU_CUM = "+getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));
    }

    // Riempimento  ComboBoX
    Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
    lOption.setFilter( new String[] {"36","99","57","37","98","38","-"});
    setRequestAttribute("autorita", "" + lOption);
    setRequestAttribute("datairrevocabilita", DateUtils.getDateToString(lFascicoloModel.getDataIrrevocabilita(), "dd/MM/yyyy"));

    return PG_LOAD_INSERISCI_RICHIESTA_PAGAMENTO_PENA_PEC ;

  }

}