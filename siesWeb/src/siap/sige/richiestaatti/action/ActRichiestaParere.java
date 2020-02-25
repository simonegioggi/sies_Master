package siap.sige.richiestaatti.action;

import java.util.Collection;
import java.util.Date;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.util.SICOLookupRemote;
import siap.sige.SIGEException;
import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import f3b.util.DateUtils;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadRichiestaParere
 * </p>
 * <p>
 * Description: Classe Action per la load di RichiestaCarichiPendenti
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActRichiestaParere extends ActRicercaFSigePuntuale implements ICostantiRichiestaAtti {

	public String processRequest() throws Exception {
    
	  if( ! isRequestParameterNullObj( CAMPO_CHIAVE_ANNO ) ){
		  super.processRequest();
	  }
	  
//		BigDecimal lIdFascicolo = null;
      Date lDataInserimento=null ;

  	  // Parametro che indica la pagina chiamante
  	  String pageCall = "";
  	  if(!isRequestParameterNullObj("Provenienza")){
  		  pageCall = getRequestStringParameter("Provenienza");
  	  }
     
  	 // 30//11/2018 aggiungo blocco su richiesta Nunzia (email del 29/11/2018 -Unificazione procedimento.docx)  	
  	 if (IsFascicoloUnificato())
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"Non è possibile emettere un parere per questo Procedimento!");
  	 
  	  setRequestAttribute("pageCall", pageCall);

//		UtenteModel lUtenteConnesso = (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
      // Data Fascicolo SIUS
		// Date lDataInserimento =
		// ((FascicoloGPModel)getSessionAttribute("fascicoloSiusGP")).getFascicoloSiusModel().getDataInserimento();
      if (!IsFascicoloSigeIscrittoCompetenza())
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"Non è possibile emettere provvedimento per questo Procedimento!");
   
     // ANGELA *****************************
     // Fascicolo Sige Esteso in sessione.
      FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel)getSessionAttribute("FascicoloSigeEsteso");
//		lIdFascicolo = lFasEsteso.getFascicoloSige().getIdFascicoloSige();
      lDataInserimento =lFasEsteso.getFascicoloSige().getDataInserimento();   
     // ANGELA *****************************
    
    
    String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
    setRequestAttribute("dataInsFS",lDataInserimentoString);
      
    // 25/09/2006 Si Imposta l'Autorita Competente.
    Collection lCol = (DecodificheManager.getInstance()).getTipoUfficio();
    Option lOption = new Option( lCol);
    lOption.setFilter( new String[] {"-", "PM", "PGCAP","PMI","PMM"} ); //solo le Autorità competenti.
    
    // PM Procura della repubblica c/o il tribunale
	// PGCAP Procura Generale c/o la corte d'appello
	// PMM Procura Minori
	// PMI Procura Militare
    setRequestAttribute("codTipoUfficioS", "" + lOption );

    Collection lColMotivoParere = null;
    DecodificheModel lModel = new DecodificheModel();

    IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
    lModel.setContesto("MOTIVO_PROVVEDIMENTO");
    lModel.setFiltro("PARERE");
    
    lColMotivoParere = lDecodifiche.ExRicercaDecodifiche(lModel);

    lOption = new Option( lColMotivoParere,35 );

    if(getUfficioUtenteConnesso().getCodTipoUfficio().equals("TDS"))
      lOption = new Option( lColMotivoParere,"0750",35 );

    setRequestAttribute("colMotivoParere","" + lOption);
    
		// 28.11.2008 selezione motivi inammissibilità x RV_ABBREVIATION=CPP IN CASO DI PROCEDIMENTO PENE
		// PECUNIARIE
    //Istanzio il Model
    //FascicoloGPModel lFasGP = new FascicoloGPModel() ;
    
//		String StrCodiceUfficioUtente = lUtenteConnesso.getUfficioUtente().getCodUfficio();
    //setRequestAttribute("CodiceUfficioUtente", StrCodiceUfficioUtente);
    //lFasGP.getFascicoloSiusModel().setChiaveUfficio(StrCodiceUfficioUtente);

    if( ! isRequestParameterNullObj( CAMPO_CHIAVE_ANNO ) )
      //lFasGP.getFascicoloSiusModel().setChiaveAnno(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO));
     	lFasEsteso.getFascicoloSige().setChiaveAnno(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO));
    
     if( ! isRequestParameterNullObj( CAMPO_CHIAVE_PROGR ) )
      //lFasGP.getFascicoloSiusModel().setChiaveProgr(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR));
    	lFasEsteso.getFascicoloSige().setChiaveProgr(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR));
    
     //listaMotiviCPP();
     
    //FascicoloSiepController lCtrl = new FascicoloSiepController();
  //OKK *************  IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
     
    // STUB 07/06/2004 Sdoppio la chiamata
   //OKK if (this.mControl)
		// OKK mFasGPMod =
		// lCtrl.ExRicercaFascicoloByAnnoProgrCodUfficio(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO),
		// getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR), StrCodiceUfficioUtente );
   //OKK else
		// OKK mFasGPMod =
		// lCtrl.ExRicercaFascicoloByAnnoProgrCodUfficioNoControl(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO),
		// getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR), StrCodiceUfficioUtente );

		// OKKif ((mFasGPMod != null && mFasGPMod.getGeneraleProcedimentoModel() != null) &&
		// (mFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U070") == 0 ))
	 //OKK{
  //OKK  	if(getUfficioUtenteConnesso().getCodTipoUfficio().equals("UDS"))
  //OKK  		listaMotiviCPP();
    	
    	//OKK}
  //OKK  else
  //OKK  {	
  //OKK  	listaMotivi();
  //OKK  }

     return PG_LOAD_RICHIESTAPARERE; //restituisce la jsp di VIEW
  }

//	private void listaMotivi() throws Exception {
//
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug(getClass().getName() + ".listaMotivi: inizio");
//		String lTipoUff = getUfficioUtenteConnesso().getCodTipoUfficio().toUpperCase();
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("tipo ufficio ->" + lTipoUff);
//
//		// Chiamata Controller per la ricerca delle Decodifiche.
//		IDecodifiche lDecCtrl = SICOLookupRemote.getDecodificheRemote();
//		Vector lVect = new Vector(lDecCtrl.ExListaMotiviInammissibilita(lTipoUff));
//		setRequestAttribute("motivi", lVect);
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug(getClass().getName() + ".listaMotivi: fine");
//	}

//	private void listaMotiviCPP() throws Exception {
//
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug(getClass().getName() + ".listaMotiviCPP: inizio");
//		String lTipoUff = getUfficioUtenteConnesso().getCodTipoUfficio().toUpperCase();
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("tipo ufficio ->" + lTipoUff);
//
//		// Chiamata Controller per la ricerca delle Decodifiche.
//		IDecodifiche lDecCtrl = SICOLookupRemote.getDecodificheRemote();
//		Vector lVect = new Vector(lDecCtrl.ExListaMotiviInammissibilitaCPP(lTipoUff));
//		setRequestAttribute("motivi", lVect);
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug(getClass().getName() + ".listaMotiviCPP: fine");
//	}

}