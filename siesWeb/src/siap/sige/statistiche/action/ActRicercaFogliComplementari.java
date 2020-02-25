package siap.sige.statistiche.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.ufficio.model.UfficioModel;
import siap.sige.statistiche.controller.IStatisticheSige;
import siap.sige.statistiche.model.RicercaFogliCompModel;
import siap.sige.statistiche.model.StatisticheFogliComplementariModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
* <p>Title: ActRicercaFogliComplementari</p>
* <p>Description: Classe Action per la ricerca Fogli Complementari</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
public class ActRicercaFogliComplementari extends ActionSige implements ICostantiStatistiche{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	// Elenco risultato della ricerca
	private Vector <StatisticheFogliComplementariModel>mVect = null;

	public String processRequest() throws Exception {
	   String lReturnPage = PG_STATISTICHE_FOGLI_COMP;
	   
	   RicercaFogliCompModel mRicercaModel=new RicercaFogliCompModel();
	   boolean fcTrasmessi=this.isReportSelected(ICostantiStatistiche.CHECK_FOGLI_TRASMESSI);
	   boolean fcFogliManuali=this.isReportSelected(ICostantiStatistiche.CHECK_FOGLI_MANUALI);
       boolean provvedimentiPriviFC=this.isReportSelected(ICostantiStatistiche.CHECK_PROVV_PRIVI_FC);
       boolean provvedimentiFCNonTrasmessi=this.isReportSelected(ICostantiStatistiche.CHECK_FOGLI_NON_TRASMESSI);
       boolean fcTrasmessiErrore=this.isReportSelected(ICostantiStatistiche.CHECK_FOGLI_ERRORE);
       boolean fcAnnullati=this.isReportSelected(ICostantiStatistiche.CHECK_FOGLI_ANNULLATI);
	   
       mRicercaModel.setFcTrasmessi(fcTrasmessi);
       mRicercaModel.setFcIscrittiManualmente(fcFogliManuali);
       mRicercaModel.setProvvedimentiFCNonTrasmessi(provvedimentiFCNonTrasmessi);
       mRicercaModel.setProvvedimentiPriviFC(provvedimentiPriviFC);
       mRicercaModel.setFcTrasmessiErrore(fcTrasmessiErrore);
       mRicercaModel.setFcAnnullati(fcAnnullati);
       
       BigDecimal annoIniziale=super.getRequestBigDecimalParameter(ICostantiStatistiche.CAMPO_ANNO_INIZIALE);
       BigDecimal annoFinale=super.getRequestBigDecimalParameter(ICostantiStatistiche.CAMPO_ANNO_FINALE);
	   mRicercaModel.setAnnoIniziale(annoIniziale);
	   mRicercaModel.setAnnoFinale(annoFinale);
	   
	   Date dataInizialeEmissione=super.getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE_INIZIO, CAMPO_MESE_DATA_EMISSIONE_INIZIO, CAMPO_GIORNO_DATA_EMISSIONE_INIZIO);
	   Date dataFinaleEmissione=super.getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE_FINE, CAMPO_MESE_DATA_EMISSIONE_FINE, CAMPO_GIORNO_DATA_EMISSIONE_FINE);
	   
	   mRicercaModel.setDataEmissioneIniziale(dataInizialeEmissione);
	   mRicercaModel.setDataEmissioneFinale(dataFinaleEmissione);
	   UfficioModel ufficioConnesso=super.getUfficioUtenteConnesso();
	   mRicercaModel.setUfficioConnesso(ufficioConnesso);
	   
	   mRicercaModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
	   IStatisticheSige ctrl=SIGELookupRemote.getStatisticheSigeRemote();
	   
	   String lPagina = "1";
	   if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE)) {
           lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);
	   } else {
		   super.setSessionAttribute(CAMPO_DESC_CALCOLI, null);
	   }

	   setRequestAttribute("CountRisultati", this.handlePagination(mRicercaModel));
	   mVect=ctrl.ExEstraiStatisticheFogliComplementari(mRicercaModel, Integer.parseInt(lPagina));
	   setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
	   setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING,  getCompleteRequestURL());
	   setSessionAttribute("FiltroRicerca", mRicercaModel);
	   setRequestAttribute("Provvedimenti", mVect);
	   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	   siesLogger.debug( "Model di Ricerca: "	 + mRicercaModel);
	   return lReturnPage;
}
	
private BigDecimal handlePagination (RicercaFogliCompModel mRicercaModel) throws F3BException{
	
	IStatisticheSige ctrl=SIGELookupRemote.getStatisticheSigeRemote();
	BigDecimal countRisultati = null;
	try {
		countRisultati=(BigDecimal)getSessionAttribute(CAMPO_DESC_CALCOLI);
	} catch (Exception e) {}
	
	if (countRisultati == null) {
    	countRisultati = ctrl.ExCountEstraiStatisticheFogliComplementari(mRicercaModel);
    }
    return countRisultati;
}
	
 
  private boolean isReportSelected (String paramName) {
	  boolean flag=false;
	  try {
		  flag=Boolean.parseBoolean(super.getRequestStringParameter(paramName));
	  } catch (F3BException f3b) {
	  }
	  return flag;
  }

}