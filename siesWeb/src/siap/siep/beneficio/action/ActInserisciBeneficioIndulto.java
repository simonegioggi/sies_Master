package siap.siep.beneficio.action;

/**
* <p>Title: ActInserisciBeneficioIndulto</p>
* <p>Description: Classe Action per l'inserimento di Beneficio per l'indulto e l'amnistia</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
*/

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaaccessoria.action.ICostantiPenaAccessoria;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciBeneficioIndulto extends ActionSiap implements ICostantiBeneficio
																		
{
    // Beneficio
    protected BeneficioModel mBenMod = null;
    // Pene Accessorie
    protected String[] mListaIdPeneAcc = null;    
	
 /**
  * Azione di Inserimento del Beneficio
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws F3BException
  */
  public String processRequest() throws F3BException
  {

//per iscrizione guidata
    if(!this.isRequestParameterNullObj("lTipoFunzione")) // paramentro passato solo nel caso di iscrizione guidata
    {
      this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
    }
   
    FascicoloSiepModel lFascMod =(FascicoloSiepModel)getSessionAttribute("fascicolo"); 
    
    // lettura dati dalla form
    letturaBeneficio(lFascMod.getIdFascicoloSiep());

   
  //INSERIMENTO BENEFICI TIPOLOGIA ORARIO ALL'INTERNO DEL CONTROLLER 
    IBeneficio lCtrl = SIEPLookupRemote.getBeneficioRemote();
    mBenMod = lCtrl.ExInserisciBeneficioTipOrario(mBenMod,null,mListaIdPeneAcc,null);

    //Prepara la destinazione
	 String lPage = "";
	 lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.beneficio.action.ActLoadDettaglioBeneficioIndulto&"+CAMPO_ID_BENEFICIO+"="+mBenMod.getIdBeneficio().toString();

    return lPage;
  }
  
  protected void letturaBeneficio (BigDecimal aIdFascicoloSiep) throws F3BException
  {
	    //BENEFICI
	   	mBenMod = new BeneficioModel();
	 
	    mBenMod.setCodNaturaBeneficio("C");
	    mBenMod.setCodTipoBeneficio(getRequestStringParameter(CAMPO_COD_TIPO_BENEFICIO));
	    mBenMod.setCodSottotipoBeneficio(getRequestStringParameter(CAMPO_COD_SOTTOTIPO_BENEFICIO));
	    mBenMod.setCodDpr(getRequestStringParameter(CAMPO_COD_DPR));
	    mBenMod.setNote(getRequestStringParameter(CAMPO_NOTE));
	    
	    //mBenMod.setFlagNonmenzione("N");
	    mBenMod.setCodTipoSospSubordinata("-");

	    
	    //===========================================
	    // reclusione
	    //===========================================
	    String GRec = getRequestStringParameter("GRec");
	    String MRec = getRequestStringParameter("MRec");
	    String ARec = getRequestStringParameter("ARec");
	    String Multa = getRequestStringParameter("Multa");
	    String Multa_dec = getRequestStringParameter("Mul_dec");

	    if (!ARec.equals(""))
	    	mBenMod.setNumAnniReclusione(new BigDecimal(ARec));
	    else
	    	mBenMod.setNumAnniReclusione(new BigDecimal(0));

	    if (!MRec.equals(""))
	    	mBenMod.setNumMesiReclusione(new BigDecimal(MRec));
	    else
	    	mBenMod.setNumMesiReclusione(new BigDecimal(0));

	    if (!GRec.equals(""))
	    	mBenMod.setNumGiorniReclusione(new BigDecimal(GRec));
	    else
	    	mBenMod.setNumGiorniReclusione(new BigDecimal(0));

	    if (!Multa.equals("")) {
	      if (!Multa_dec.equals("")) {
	    	  mBenMod.setImportoMulta(new BigDecimal(Multa + "." + Multa_dec));
	      } else
	    	  mBenMod.setImportoMulta(new BigDecimal(Multa));
	    } else if (!Multa_dec.equals(""))
	    	mBenMod.setImportoMulta(new BigDecimal("0." + Multa_dec));

	    //===========================================
	    // arresto
	    //===========================================
	    String GArr = getRequestStringParameter("GArr");
	    String MArr = getRequestStringParameter("MArr");
	    String AArr = getRequestStringParameter("AArr");
	    String Ammenda = getRequestStringParameter("Ammenda");
	    String Ammenda_dec = getRequestStringParameter("Amm_dec");

	    if (!AArr.equals(""))
	    	mBenMod.setNumAnniArresto(new BigDecimal(AArr));
	    else
	    	mBenMod.setNumAnniArresto(new BigDecimal(0));

	    if (!MArr.equals(""))
	    	mBenMod.setNumMesiArresto(new BigDecimal(MArr));
	    else
	    	mBenMod.setNumMesiArresto(new BigDecimal(0));

	    if (!GArr.equals(""))
	    	mBenMod.setNumGiorniArresto(new BigDecimal(GArr));
	    else
	    	mBenMod.setNumGiorniArresto(new BigDecimal(0));

	    if (!Ammenda.equals("")) {
	      if (!Ammenda_dec.equals("")) {
	    	  mBenMod.setImportoAmmenda(new BigDecimal(Ammenda + "." + Ammenda_dec));
	      } else
	    	  mBenMod.setImportoAmmenda(new BigDecimal(Ammenda));
	    } else if (!Ammenda_dec.equals(""))
	    	mBenMod.setImportoAmmenda(new BigDecimal("0." + Ammenda_dec));
		
	  //  mBenMod.setRifIdProvvedimento(lFascMod.getSenIdSentenza());
	    mBenMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
	    mBenMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
	    mBenMod.setDataInserimento(DateUtils.getSysDate());
	    mBenMod.setFasSieIdFascicoloSiep(aIdFascicoloSiep);  
	    mBenMod.setRifCodTipoAutoEmittente("-");
	    mBenMod.setRifCodTipoProvvedimento("-");
	     
	    if(!isRequestParameterNullObj(ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA))
	    {
	     mListaIdPeneAcc =  getRequestStringParameters(ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA);    
	    }
	    
  }
  
}