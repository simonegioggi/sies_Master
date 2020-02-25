package siap.siep.beneficio.action;

/**
* <p>Title: ActInserisciBeneficio</p>
* <p>Description: Classe Action per l'inserimento di Beneficio</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
*/

import java.math.BigDecimal;
import java.util.ArrayList;

import siap.sico.web.ActionSiap;
import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.tipologiaorario.action.ICostantiTipologiaOrario;
import siap.siep.tipologiaorario.model.TipologiaOrarioModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciBeneficio extends ActionSiap implements ICostantiBeneficio,
 														ICostantiTipologiaOrario	
{
    protected ArrayList<TipologiaOrarioModel> mTipologie = null;  
    // Beneficio
    protected BeneficioModel mBenMod = null;
    // Beneficio Non Menzione
    protected BeneficioModel mBenNMMod = null;
 /**
  * Azione di Inserimento del Beneficio
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws F3BException
  */
  public String processRequest() throws Exception
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
    mBenMod = lCtrl.ExInserisciBeneficioTipOrario(mBenMod,mTipologie,null,mBenNMMod);

    //Prepara la destinazione
	 String lPage = "";
	 lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.beneficio.action.ActLoadDettaglioBeneficio&"+CAMPO_ID_BENEFICIO+"="+mBenMod.getIdBeneficio().toString();
	 return lPage;

  }
  
  
  
  protected void letturaBeneficio (BigDecimal aIdFascicoloSiep) throws F3BException
  {
  
  //BENEFICI
  mBenMod = new BeneficioModel();
  
  mBenMod.setCodTipoBeneficio("01");
  mBenMod.setCodSottotipoBeneficio(getRequestStringParameter(CAMPO_COD_SOTTOTIPO_BENEFICIO));
  mBenMod.setCodNaturaBeneficio("C");
  mBenMod.setCodDpr("-");
  
  if(getRequestStringParameter(CAMPO_NUM_ANNI_SOSPENSIONE) != null && !getRequestStringParameter(CAMPO_NUM_ANNI_SOSPENSIONE).equals("")) 
    mBenMod.setNumAnniSospensione(this.getRequestBigDecimalParameter(CAMPO_NUM_ANNI_SOSPENSIONE));
  else
    mBenMod.setNumAnniSospensione(new BigDecimal(0));  	
  
  if(!this.isRequestParameterNullObj(CAMPO_COD_TIPO_SOSP_SUBORDINATA))
  {
   mBenMod.setCodTipoSospSubordinata(getRequestStringParameter(CAMPO_COD_TIPO_SOSP_SUBORDINATA));

   mBenMod.setNote(getRequestStringParameter(CAMPO_NOTE));
  

   mBenMod.setNumAnniAdempimento(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_ADEMPIMENTO));
   mBenMod.setNumMesiAdempimento(getRequestBigDecimalParameter(CAMPO_NUM_MESI_ADEMPIMENTO));
   mBenMod.setNumGiorniAdempimento(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_ADEMPIMENTO));
  }
  else
  {
      mBenMod.setCodTipoSospSubordinata("-");
	
  }

  mBenMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
  mBenMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
  mBenMod.setDataInserimento(DateUtils.getSysDate());
  mBenMod.setFasSieIdFascicoloSiep(aIdFascicoloSiep);   
  mBenMod.setRifCodTipoAutoEmittente("-");
  mBenMod.setRifCodTipoProvvedimento("-");

	
   //creazione beneficio per non menzione
  if(isRequestChecked(CAMPO_FLAG_NON_MENZIONE))
  {
  	mBenNMMod = new BeneficioModel(); 
  	mBenNMMod.setCodTipoBeneficio("02");
  	mBenNMMod.setCodSottotipoBeneficio("-");
  	mBenNMMod.setCodNaturaBeneficio("C");
  	mBenNMMod.setCodDpr("-");
	     
  	mBenNMMod.setCodTipoSospSubordinata("-");

  	mBenNMMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
  	mBenNMMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
  	mBenNMMod.setDataInserimento(DateUtils.getSysDate());
  	mBenNMMod.setFasSieIdFascicoloSiep(aIdFascicoloSiep);       
  	mBenNMMod.setRifCodTipoAutoEmittente("-");
  	mBenNMMod.setRifCodTipoProvvedimento("-");    	    
  }    
  if(!this.isRequestParameterNullObj(CAMPO_NUM_GIORNI_PRESTAZIONE))
  {     
   mBenMod.setNumGiorniPrestazione(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_PRESTAZIONE));
   mBenMod.setNumMesiPrestazione(getRequestBigDecimalParameter(CAMPO_NUM_MESI_PRESTAZIONE));
   mBenMod.setNumOreSettimanali(getRequestBigDecimalParameter(CAMPO_NUM_ORE_SETTIMANALI));
  
   if(getRequestStringParameter(CAMPO_FLAG_FREQUENZA_SETTIMANALE) != null &&
     (getRequestStringParameter(CAMPO_FLAG_FREQUENZA_SETTIMANALE)).equals("1"))
   {
  	mBenMod.setFlagFrequenzaSettimanale("N"); //Non determinata	
   }
   else if(getRequestStringParameter(CAMPO_FLAG_FREQUENZA_SETTIMANALE) != null &&
  	    (getRequestStringParameter(CAMPO_FLAG_FREQUENZA_SETTIMANALE)).equals("2"))
   {
    	
  	mBenMod.setFlagFrequenzaSettimanale("D"); //Determinata	   	
   }

   
   
   if(mBenMod.getFlagFrequenzaSettimanale().equals("D"))
   {
    //TIPOLOGIA ORARIO  
    mTipologie = new ArrayList<TipologiaOrarioModel>();  
    //Lunedì
    if(!this.isRequestParameterNullObj(CAMPO_COD_NUM_GIORNO_LUN))
    {
	      TipologiaOrarioModel lTipOrLunMod = new TipologiaOrarioModel();
	      lTipOrLunMod.setCodNumGiorno(getRequestStringParameter(CAMPO_COD_NUM_GIORNO_LUN ));
	      lTipOrLunMod.setDalleOre(getRequestStringParameter(CAMPO_DALLE_ORE_LUN));
	      lTipOrLunMod.setAlleOre(getRequestStringParameter(CAMPO_ALLE_ORE_LUN));
	      lTipOrLunMod.setEnteIncaricato(getRequestStringParameter(CAMPO_ENTE_INCARICATO));
	      lTipOrLunMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
	      lTipOrLunMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
	      lTipOrLunMod.setDataInserimento(DateUtils.getSysDate());
	     
	      mTipologie.add(lTipOrLunMod);   
    }

    //Martedì
    if(!this.isRequestParameterNullObj(CAMPO_COD_NUM_GIORNO_MAR))
    {
	      TipologiaOrarioModel lTipOrMarMod = new TipologiaOrarioModel();
	      lTipOrMarMod.setCodNumGiorno(getRequestStringParameter(CAMPO_COD_NUM_GIORNO_MAR ));
	      lTipOrMarMod.setDalleOre(getRequestStringParameter(CAMPO_DALLE_ORE_MAR));
	      lTipOrMarMod.setAlleOre(getRequestStringParameter(CAMPO_ALLE_ORE_MAR));
	      lTipOrMarMod.setEnteIncaricato(getRequestStringParameter(CAMPO_ENTE_INCARICATO));
	      lTipOrMarMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
	      lTipOrMarMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
	      lTipOrMarMod.setDataInserimento(DateUtils.getSysDate());
	     
        mTipologie.add(lTipOrMarMod); 
    }
   
    //Mercoledì
    if(!this.isRequestParameterNullObj(CAMPO_COD_NUM_GIORNO_MER))
    {
	      TipologiaOrarioModel lTipOrMerMod = new TipologiaOrarioModel();
	      lTipOrMerMod.setCodNumGiorno(getRequestStringParameter(CAMPO_COD_NUM_GIORNO_MER ));
	      lTipOrMerMod.setDalleOre(getRequestStringParameter(CAMPO_DALLE_ORE_MER));
	      lTipOrMerMod.setAlleOre(getRequestStringParameter(CAMPO_ALLE_ORE_MER));
	      lTipOrMerMod.setEnteIncaricato(getRequestStringParameter(CAMPO_ENTE_INCARICATO));
	      lTipOrMerMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
	      lTipOrMerMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
	      lTipOrMerMod.setDataInserimento(DateUtils.getSysDate());
	     
	      mTipologie.add(lTipOrMerMod);  
    }
  
    //Giovedì
    if(!this.isRequestParameterNullObj(CAMPO_COD_NUM_GIORNO_GIOV))
    {
	      TipologiaOrarioModel lTipOrGiovMod = new TipologiaOrarioModel();
	      lTipOrGiovMod.setCodNumGiorno(getRequestStringParameter(CAMPO_COD_NUM_GIORNO_GIOV));
	      lTipOrGiovMod.setDalleOre(getRequestStringParameter(CAMPO_DALLE_ORE_GIOV));
	      lTipOrGiovMod.setAlleOre(getRequestStringParameter(CAMPO_ALLE_ORE_GIOV));
	      lTipOrGiovMod.setEnteIncaricato(getRequestStringParameter(CAMPO_ENTE_INCARICATO));
	      lTipOrGiovMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
	      lTipOrGiovMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
	      lTipOrGiovMod.setDataInserimento(DateUtils.getSysDate());
	    
	      mTipologie.add(lTipOrGiovMod); 
    }
  
    //Venerdì
    if(!this.isRequestParameterNullObj(CAMPO_COD_NUM_GIORNO_VEN))
    {     
	      TipologiaOrarioModel lTipOrVenMod = new TipologiaOrarioModel();
	      lTipOrVenMod.setCodNumGiorno(getRequestStringParameter(CAMPO_COD_NUM_GIORNO_VEN));
	      lTipOrVenMod.setDalleOre(getRequestStringParameter(CAMPO_DALLE_ORE_VEN));
	      lTipOrVenMod.setAlleOre(getRequestStringParameter(CAMPO_ALLE_ORE_VEN));
	      lTipOrVenMod.setEnteIncaricato(getRequestStringParameter(CAMPO_ENTE_INCARICATO));
	      lTipOrVenMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
	      lTipOrVenMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
	      lTipOrVenMod.setDataInserimento(DateUtils.getSysDate());
	     
	      mTipologie.add(lTipOrVenMod);   
    }

     //Sabato
    if(!this.isRequestParameterNullObj(CAMPO_COD_NUM_GIORNO_SAB))
    {   
	       TipologiaOrarioModel lTipOrSabMod = new TipologiaOrarioModel();
	       lTipOrSabMod.setCodNumGiorno(getRequestStringParameter(CAMPO_COD_NUM_GIORNO_SAB));
	       lTipOrSabMod.setDalleOre(getRequestStringParameter(CAMPO_DALLE_ORE_SAB));
	       lTipOrSabMod.setAlleOre(getRequestStringParameter(CAMPO_ALLE_ORE_SAB));
	       lTipOrSabMod.setEnteIncaricato(getRequestStringParameter(CAMPO_ENTE_INCARICATO));
	       lTipOrSabMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
	       lTipOrSabMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
	       lTipOrSabMod.setDataInserimento(DateUtils.getSysDate());
	    
	       mTipologie.add(lTipOrSabMod);
    }  

     //Domenica
    if(!this.isRequestParameterNullObj(CAMPO_COD_NUM_GIORNO_DOM))
    {   
     TipologiaOrarioModel lTipOrDomMod = new TipologiaOrarioModel();
     lTipOrDomMod.setCodNumGiorno(getRequestStringParameter(CAMPO_COD_NUM_GIORNO_DOM));
     lTipOrDomMod.setDalleOre(getRequestStringParameter(CAMPO_DALLE_ORE_DOM));
     lTipOrDomMod.setAlleOre(getRequestStringParameter(CAMPO_ALLE_ORE_DOM));
     lTipOrDomMod.setEnteIncaricato(getRequestStringParameter(CAMPO_ENTE_INCARICATO));
     lTipOrDomMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
     lTipOrDomMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
     lTipOrDomMod.setDataInserimento(DateUtils.getSysDate());
     
  
     mTipologie.add(lTipOrDomMod); 
    }
   }
  }
  }
  
  
  
}