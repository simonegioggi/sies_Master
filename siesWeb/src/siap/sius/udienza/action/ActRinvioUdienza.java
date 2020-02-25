package siap.sius.udienza.action;

/**
 * <p>Title: ActCancellaUdienza</p>
 * <p>Description: Azione  Rinvio Udienza</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @version 1.0
 */
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.avvocato.model.AvvocatoSiusModel;
import siap.sius.avvocatura.action.ICostantiAvvisiAvvocato;
import siap.sius.avvocatura.model.AvvisiAvvocatoModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.udienza.controller.IUdienza;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActRinvioUdienza extends ActionSiap implements ICostantiUdienza
{
  /**
   * Azione di aggiornamento della data udienza sulla tabella GeneraleProcedimento e dell'inserimento
   * di un nuovo record sulla tabella Udienza_Procedimento
   * @return Nome della pagina JSP su cui posizionarsi
   * al termine dell'elaborazione
   * @throws F3BException
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public String processRequest() throws F3BException
  {
    UdienzaModel lUdienza = new UdienzaModel();

    // Preleva dalla sessione i dati dell'utente connesso.
    String lCodiceOperatore = getCodUtenteConnesso();
    String lCodiceUfficio   = getCodUfficioUtenteConnesso();

    // Preleva i dati dalla request
    BigDecimal lId = this.getRequestBigDecimalParameter(CAMPO_ID_UDIENZA);

    FascicoloGPModel lFasGPMod = new FascicoloGPModel();
    lFasGPMod = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");
    
    // riempe il model
    if (getRequestStringParameter( ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA ).length() >2)
    {
       // 29/08/2008  Controllo x impedire un rinvio a udienza precedente.
       Date lDataUdienza = getRequestDateParameter(CAMPO_ANNO_DATA_UDIENZA, CAMPO_MESE_DATA_UDIENZA, CAMPO_GIORNO_DATA_UDIENZA);
       lUdienza.setDataUdienza( lDataUdienza );
       if (lFasGPMod.getGeneraleProcedimentoModel().getDataCameraConsiglio().after(lDataUdienza) )
    	   throw new SIUSException (SIUSException.USER_MESSAGE, "Rinvio Udienza non consentito.  La data udienza selezionata non può essere precedente all'udienza già fissata.");
    
       lUdienza.setLuogoUdienza(getRequestStringParameter(ICostantiUdienza.CAMPO_LUOGO_UDIENZA));
       lUdienza.setCodOperatoreInserimento(lCodiceOperatore);
       lUdienza.setCodUfficioInserimento(lCodiceUfficio);
       lUdienza.setIdUdienza(lId);
    }

    GeneraleProcedimentoModel aGeneraleProcedimentoold = new GeneraleProcedimentoModel();
    aGeneraleProcedimentoold.setIdGeneraleProcedimento(lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
    aGeneraleProcedimentoold.setUdiIdUdienza(lFasGPMod.getGeneraleProcedimentoModel().getUdiIdUdienza());
    aGeneraleProcedimentoold.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

    GeneraleProcedimentoModel aGeneraleProcedimento = new GeneraleProcedimentoModel();
    // N.B. Mi leggo l' ID di Generale Procedimento e l'ID di udienza (risultato della lista)
    // i dati risultati dalla lista sono utili per visualizzarli in dettaglio udienza
    aGeneraleProcedimento.setUdiIdUdienza(lId);
    //prelevo l'ID di generale procedimento al fine di creare la relazione tra GeneraleProcedimento e UdienzaProcedimento
    aGeneraleProcedimento.setIdGeneraleProcedimento(lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
    
    // Aggiunta
    aGeneraleProcedimento.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
    aGeneraleProcedimento.setCodOperatoreAggiornamento(getCodUtenteConnesso());
    aGeneraleProcedimento.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    aGeneraleProcedimento.setDataAggiornamento( DateUtils.getSysDate());
    aGeneraleProcedimento.setDataCameraConsiglio(lFasGPMod.getGeneraleProcedimentoModel().getDataCameraConsiglio());
    
    // STUB 21/06/2004 Aggiunta la condizione di controllo del CHECK RUOLO.
    if (lUdienza.getIdUdienza() == null &&
        isRequestParameterNullObj(CAMPO_CHECK_RUOLO) )
    {
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Rinvio Udienza non consentito. Per il procedimento non risulta fissata la nuova data udienza.");
      return IWebConstants.PG_MESSAGE;
    }

    // STUB 22/06/2004 modificata la condizione di rinvio udienza.
    //if (lFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo().equalsIgnoreCase("02"))
    if (lFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("05") != 0   &&
        lFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("01") != 0 )
    {
      if (lFasGPMod.getGeneraleProcedimentoModel().getDataCameraConsiglio() == null)
      {
        setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Rinvio Udienza non consentito. Per il procedimento non risulta fissata una precedente data udienza.");
        return IWebConstants.PG_MESSAGE;
      }
      else
      {
        
        // Inizializza EventoModel
        EventoModel lEvento = new EventoModel();
        setDatiEvento( lEvento );
        
   	 	//********************************************* 
   	 	// MEV_AVVOCATURA - INIZIO
   	 	//**********************************************
  	    UtenteModel user = (UtenteModel) getSessionAttribute("UtenteConnesso");
  	    // Ufficio Emittente
  	    String ufficio = user.getUfficioUtente().getDescrTipoUfficio() + " di " +
                         user.getUfficioUtente().getDescrComune();
  			  
  	    // Ricerca avvocati assegnati al fascicolo 
  	    IAvvocato lAvvCtrl = SIUSLookupRemote.getAvvocatoRemote();
  	    Vector<AvvocatoSiusModel> avvocati = null;
  	    avvocati = lAvvCtrl.ExRicercaAvvocatiByFascicoloNoError(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
  	    
  	    // Recupero i dati del soggetto dalla sessione @emma 25/08/2016 - avvocatura
		String cognomeSoggetto = "";
		String nomeSoggetto = "";
		if(!isSessionAttributeNullObj("soggetto")){
		  SoggettoModel datiSoggetto = (SoggettoModel) getSessionAttribute("soggetto");
		  cognomeSoggetto = datiSoggetto.getCognome();
		  nomeSoggetto = datiSoggetto.getNome();
		}else if(lFasGPMod != null && lFasGPMod.getFascicoloSiusModel() != null){
		  // provo a verificare se è presente nell'oggetto FascicoloGPModel
		  cognomeSoggetto = lFasGPMod.getFascicoloSiusModel().getSoggetto() != null ? lFasGPMod.getFascicoloSiusModel().getSoggetto().getCognome() : "";
		  //EC@19/05/2017: correzione su nomeSoggetto. Inserivamo il nome sul cognome!	
		  nomeSoggetto = lFasGPMod.getFascicoloSiusModel().getSoggetto() != null ? lFasGPMod.getFascicoloSiusModel().getSoggetto().getNome() : "";		  
		}
		else {
		  // devo procedere con una ricerca del soggetto per chiave soggetto
		  BigDecimal idSoggetto= lFasGPMod.getFascicoloSiusModel().getSogIdSoggetto();
		  ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();          
		  SoggettoModel s = lSogCtrl.ExRicercaSoggettoByKey(idSoggetto);
		  cognomeSoggetto = s.getCognome();
		  nomeSoggetto = s.getNome();
		}		

  	    Vector<AvvisiAvvocatoModel> lAvvvisiAvvocato = new Vector<AvvisiAvvocatoModel>();
	  
  	    AvvisiAvvocatoModel lAvvisiAvvModel = null;
  	   
  	    Iterator itxAvv = avvocati.iterator();
  	    while (itxAvv.hasNext())
        {
  	    	 lAvvisiAvvModel = new AvvisiAvvocatoModel();

  			 AvvocatoSiusModel lAvv = (AvvocatoSiusModel)itxAvv.next();
  			 
  			 lAvvisiAvvModel.setIdAvvocato(lAvv.getAvvocato().getIdAvvocato());
  			 lAvvisiAvvModel.setCognomeSoggeto(cognomeSoggetto);
  			 lAvvisiAvvModel.setNomeSoggetto(nomeSoggetto);
  			 //lAvvisiAvvModel.setIdProvvedimento(eveMod.getIdEvento()); // evento non ancora inserito, viene settato nel controller
  			 lAvvisiAvvModel.setDescProvvedimento(ICostantiAvvisiAvvocato.CONTENUTO_RINVIO_UDIENZA);
  			 lAvvisiAvvModel.setUfficioEmittente(ufficio);
  			 lAvvisiAvvModel.setTestoAvviso(ICostantiAvvisiAvvocato.CONTENUTO_RINVIO_UDIENZA);
  			 lAvvisiAvvModel.setFlagVisualizzazione("N");
  			 lAvvisiAvvModel.setCodOperatoreInserimento(user.getUserId());
  			 lAvvisiAvvModel.setCodUfficioInserimento(user.getUfficioUtente().getCodUfficio());
  			 lAvvisiAvvModel.setDataInserimento(DateUtils.getSysDate());

  			 lAvvvisiAvvocato.add(lAvvisiAvvModel);
  		 
  	    }
  	   
     	//*********************************************************************** 
    	// MEV_AVVOCATURA aggiunto parametro lAvvvisiAvvocato 
    	//***********************************************************************
        IUdienza lCtrl = SIUSLookupRemote.getUdienzaRemote();
        lCtrl.ExRinvioUdienzaVerbale(lUdienza,aGeneraleProcedimento,lEvento, lFasGPMod.getTenori(), lAvvvisiAvvocato);
        //lCtrl.ExRinvioUdienzaVerbale(lUdienza,aGeneraleProcedimento,lEvento, lFasGPMod.getTenori());

     	//********************************************* 
     	// MEV_AVVOCATURA - FINE
     	//**********************************************
        
        // Aggiornamento del Fascicolo in sessione
        IFascicoloSius lCtrFas = SIUSLookupRemote.getFascicoloSiusRemote();
        lFasGPMod = lCtrFas.ExRicercaFascicoloByKey(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
        setSessionAttribute("fascicoloSiusGP", lFasGPMod);

        // setta la risposta nella request.
        setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Rinvio Udienza Avvenuto Correttamente!");
        // STUB 22/06/2004 Nel caso di rinvio a Nuovo ruolo, Setta la data udienza per non far bloccare la funzione dal LOG_ATTIVITA.
        if (!isRequestParameterNullObj(CAMPO_CHECK_RUOLO) )
          lUdienza.setDataUdienza( lFasGPMod.getGeneraleProcedimentoModel().getDataCameraConsiglio() );

        // Se NUOVO RUOLO si passa l'ID Udienza vecchia
        if (isRequestParameterNullObj(CAMPO_CHECK_RUOLO) )
          lUdienza.setIdUdienza ( lFasGPMod.getGeneraleProcedimentoModel().getUdiIdUdienza() );


        // Prepara la pagina di redirezione.
        RedirectTo lPage = new RedirectTo();
        lPage.setPage(IWebConstants.PG_MAIN);
        lPage.setAction("siap.sius.udienza.action.ActDettaglioRinvioUdienza");
        lPage.setParameter(ICostantiUdienza.CAMPO_ID_UDIENZA,"" + lUdienza.getIdUdienza());
        String lReturnPage =lPage.toString();
        //   lReturnPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.udienza.action.ActDettaglioRinvioUdienza&"+ICostantiUdienza.CAMPO_ID_UDIENZA+"="+aUdienza.getIdUdienza()+"&"+ICostantiUdienza.CAMPO_DATA_UDIENZA+"="+DateUtils.getDateToString(aUdienza.getDataUdienza(),"yyyyMMdd" )+"&"+ICostantiUdienza.CAMPO_LUOGO_UDIENZA+"="+aUdienza.getLuogoUdienza();

        return lReturnPage;
      }
    }
    else
    {
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Rinvio Udienza non consentito. Procedimento già definito.");
      return IWebConstants.PG_MESSAGE;
    }
    // return IWebConstants.PG_MESSAGE; //restituisce la jsp di VIEW
  }
  
  protected void setDatiEvento( EventoModel aEvento )
  throws F3BException 
  { 
    aEvento.setCodTipoEvento("01"); //Tipo Evento = Provvedimento
    aEvento.setCodTipoProvvedimento("50"); // Rinvio Udienza Verbale
    aEvento.setCodMotivo("0603");  // Rinvio Udienza
    
    // Imposta la data di emissione
    aEvento.setDataEmissione( 
        getRequestDateParameter( ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE, 
                                 ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, 
                                 ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE ) );

    aEvento.setFasSiuIdFascicoloSius( 
        ((FascicoloGPModel)getSessionAttribute("fascicoloSiusGP")).getFascicoloSiusModel().getFasSiuIdFascicoloSius() );
    aEvento.setCodOperatoreInserimento(getCodUtenteConnesso());
    aEvento.setCodLuogoEmittente(getCodComuneUtenteConnesso());
    aEvento.setCodUfficioEmittente(getCodUfficioUtenteConnesso());
    aEvento.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
    aEvento.setDataInserimento(DateUtils.getSysDate());
    aEvento.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
    aEvento.setCodEsito("0603");
    aEvento.setCodLuogoDestinatario("-");
    aEvento.setCodTipoUfficioDestinatario("-");
    aEvento.setFasSieIdFascicoloSiep(
        ((FascicoloGPModel)getSessionAttribute("fascicoloSiusGP")).getFascicoloSiusModel().getFasSieIdFascicoloSiep());
    aEvento.setFasSiuIdFascicoloSius(
        ((FascicoloGPModel)getSessionAttribute("fascicoloSiusGP")).getFascicoloSiusModel().getIdFascicoloSius());
  }
}