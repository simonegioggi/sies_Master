package siap.sige.fogliocomplementare.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fogliocomplementare.model.FoglioComplementareModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.statistiche.action.ICostantiStatistiche;
import siap.sige.util.SIGELookupRemote;
import f3b.util.F3BException;

public class ActRicercaFoglioComplementare extends ActRicercaFSigePuntuale implements ICostantiFoglioComp, ICostantiStatistiche,ICostantiProvvedimentoSige {
	FascicoloSigeEstesoModel mFascicoloEsteso=null;
	public String processRequest () throws Exception{
		mFascicoloEsteso= (FascicoloSigeEstesoModel)super.getRequest().getSession().getAttribute("FascicoloSigeEsteso");
		
		if( mFascicoloEsteso==null )
		      super.processRequest();
		 
        setLinkRitorno();
   	    this.ricercaFogli();
   	    return PG_ELENCOPROVVEDIMENTICFC_TASTOFUNZIONE;
    }
	
	private void ricercaFogli () throws F3BException{
	    FascicoloSigeEstesoModel mFascicoloEsteso= (FascicoloSigeEstesoModel)super.getRequest().getSession().getAttribute("FascicoloSigeEsteso");
	    BigDecimal lIdFascicolo=mFascicoloEsteso.getFascicoloSige().getIdFascicoloSige();
	    IProvvedimentoSige mCtrl = SIGELookupRemote.getProvvedimentoRemote();
	    String lTipiProvv = "'" + COD_DECRETO_GENERICO + "','" + COD_ORDINANZA_GENERICA + "'" ;
	    //Vector <ProvvedimentoSigeEventoModel>provvedimenti = mCtrl.ExRicercaProvvSigeXCFC(lIdFascicolo, lTipiProvv, COD_EVENTO_PROVVEDIMENTO);
	    Vector <FoglioComplementareModel>provvedimenti=mCtrl.ExRicercaFogliComplementariByFascicolo(lIdFascicolo, lTipiProvv);
	    // L'elenco viene passato nella request
	    setRequestAttribute("provvedimenti", provvedimenti);
    }
}
