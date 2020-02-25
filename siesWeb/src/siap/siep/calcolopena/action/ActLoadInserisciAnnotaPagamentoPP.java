package siap.siep.calcolopena.action;

/**
 * <p>Title: ActLoadInserisciAnnotaPagamentoPP</p>
 * <p>Description: Azione Load nella Converisione delle Pene Pecuniarie</p>
 * <p>MicroFunzione: Inserimento Annotazione avvenuto pagamento Pene Pecuniarie</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: </p>
 * @author Ambrosino Claudio
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

public class ActLoadInserisciAnnotaPagamentoPP extends ActionSiap implements ICostantiEvento
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
 
  public String processRequest() throws Exception
  {
    //===============================================
    // Controllo Presenza del Fascicolo in Sessione
    //===============================================
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
    	return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

    this.isFascicoloSiepDiCompetenza();

    //===============================================
    // Controllo Validazione Fascicolo
    //===============================================
    if (lFascMod.getFlagValidato().equalsIgnoreCase("N"))
    {
	      RedirectTo lRedirigi = new RedirectTo();
	
	      lRedirigi.setPage(IWebConstants.PG_MAIN);
	      setRequestAttribute(IWebConstants.MESSAGE_TEXT,
	                          "Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile procedere!");
	      lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerValidazione&" +
	                          ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
	      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
	
	      return IWebConstants.PG_MESSAGE;
    }

    //===============================================
    // Controllo Fascicolo definito
    //===============================================
/*    if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO"))
    {
	      RedirectTo lRedirigi = new RedirectTo();
	
	      lRedirigi.setPage(IWebConstants.PG_MAIN);
	      setRequestAttribute(IWebConstants.MESSAGE_TEXT,
	                          "Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr() + " risulta Definito. Impossibile procedere!");
	      lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
	                          ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
	      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
	
	      return IWebConstants.PG_MESSAGE;
    }
*/
    this.isEventoNonValidato();

    //==========================================================================
    // Recupero la Posizione Giuridica
    //==========================================================================
    IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltr = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lIdFascicolo);

    if (lPosLuoAltr == null || lPosLuoAltr.getPosizioneGiuridica() == null)
      throw new SIEPException(SIEPException.USER_MESSAGE, "Inserire prima la Posizione Giuridica. Impossibile eseguire la richiesta.");

    //==========================================================================
    // Recupero la Pena Complessiva
    //==========================================================================
    IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
    PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);

    if (lPenComMod == null)
      throw new SIEPException(SIEPException.USER_MESSAGE, "Pena Complessiva non presente. Impossibile eseguire la richiesta.");

    //==========================================================================
    // Recupero i dati della pena da visualizzare nella form.
    // Se non libero deve esistere una pena residua Validata
    // Se libero può non esistere, in questo caso passo i dati della Pena complessiva
    // Recupero l'ultima pena residua validata
    //==========================================================================
    IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();
    PenaResiduaModel lUltimaPenaValidata = lCtrlPenRes.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo);
//    if (lUltimaPenaValidata == null)
//    { // se non presente una pena validata utilizzo l'ultima in assoluto NO!
//    // non ha senso! Se non esiste una validata tanto vale ricalcolarla come
//    // se fosse il primo calcolo della pena ricalcolando la data inizio.
//    // Se si usa la data inizio pena dll'ultima non validata si rischia di 
//    // agganciare un calcolo intermedio errato effettuato prima della modifica
//    // della posizione giuridica
//      lUltimaPenaValidata = lCtrlPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
//    }
    if (lUltimaPenaValidata == null)
    { // Non esiste proprio un pena validata non è mai stato fatto il primo calcolo
      // della pena lo effettuo ora per avere dati da visualizzare sulla form
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Pena residua assente effettuo il primo calcolo");
      ActCalcoloPenaMain lActCalcoloPenaMain = new ActCalcoloPenaMain();
      CalcoloPenaModel lCalcPenaModel = lActCalcoloPenaMain.calcoloPena(lIdFascicolo,null);
      
      Date lDataInizioPena = null;
      try {
        lDataInizioPena = lActCalcoloPenaMain.getDataPrimoCalcolo(lIdFascicolo);
      }
      catch (F3BException e)
      { // Primo calcolo ma posizioni giuridiche e MC non coerenti, non posso 
        // proseguire. Come primo calcolo della pena
        RedirectTo lRedirigi = new RedirectTo();
        lRedirigi.setPage(IWebConstants.PG_MAIN);
        setRequestAttribute(IWebConstants.MESSAGE_TEXT,  e.getMessage() );
        lRedirigi.setAction( "siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&" +ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP+"="+((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep() );
        setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
        return IWebConstants.PG_MESSAGE;
      }
      
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Data inizio pena: "+lDataInizioPena);
      try{
        lUltimaPenaValidata = lCalcPenaModel.getPenaDaEspiare(lDataInizioPena,null,null);
      }
      catch(Exception e){
        // pezza da togliere serve solo per gestire la catch
        throw new F3BException(e);
      }
    }
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("Ultima pena residua VALIDATA = "+lUltimaPenaValidata);
   
    
    // Controllo Esistenza Pena Residua validata se != LIBERO
    // 12-01-2007 aggiunta la posizione Latitante ('Prima')
    if(    !lPosLuoAltr.getPosizioneGiuridica().isLibero() 
        && !lPosLuoAltr.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("05")
      )
    {
      if (lUltimaPenaValidata == null)
      {
        String lErrore = null;
        String lAzioneChiamante = null;
        lErrore = "Pena Residua da Espiare Inesistente o non validata. Eseguire Calcolo della pena?";
        lAzioneChiamante = "siap.siep.calcolopena.action.ActLoadCalcoloPena";
        // Ridireziona sul calcolo della pena se non presente validato
        RedirectTo lRedirigi = new RedirectTo();
        lRedirigi.setPage(IWebConstants.PG_MAIN);
        setRequestAttribute(IWebConstants.MESSAGE_TEXT, lErrore);
        lRedirigi.setAction(lAzioneChiamante+"&" +
                            ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
        setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

        return IWebConstants.PG_MESSAGE;
      }
    }

    //==========================================================================
    // Recupero Reclusione e Arresto
    //==========================================================================
    CalendarModel lCalReclusione = new CalendarModel();
    CalendarModel lCalArresti    = new CalendarModel();
    lCalReclusione.setErrorMsg("-");

    
    //==========================================================================
    // Nuova gestione caricamento pena in corso di espiazione
    // Se non ergastolo viene ricalcolata la pena in base ai dati a sistema 
    // e passata alla form di visualizzazione
    //==========================================================================
    Date lDataInizioPena = null;
    if (lUltimaPenaValidata!=null)
    	lDataInizioPena = lUltimaPenaValidata.getDataInizio();
    
    if(  lPenComMod.getCodTipoPenaDetentiva() != null
        && lPenComMod.getCodTipoPenaDetentiva() != ""
        && !( lPenComMod.getCodTipoPenaDetentiva().equals("03") || lPenComMod.getCodTipoPenaDetentiva().equals("04") )
      )
    {
    
		     // Effettuo il ricalcolo della pena
		      ActCalcoloPenaMain lActCalcoloPenaMain = new ActCalcoloPenaMain();
		      CalcoloPenaModel lCalcPenaModel = lActCalcoloPenaMain.calcoloPena(lIdFascicolo,null);
		      // n.b. terzo parametro a null per non considerare la LA non ancora computate
		      PenaResiduaModel lPenaResiduaCorrente = lCalcPenaModel.getPenaDaEspiare(lDataInizioPena,null,null);
		    
		      // Recupero la Reclusione
		      lCalReclusione = lPenaResiduaCorrente.getQuantumReclusione();
		      if (lPenaResiduaCorrente.getImportoMulta() == null)
		    	  lCalReclusione.setImportoMulta(new BigDecimal(0).doubleValue());
		      else
		    	  lCalReclusione.setImportoMulta(lPenaResiduaCorrente.getImportoMulta().doubleValue());
		    
		      // Recupero li arresti
		      lCalArresti = lPenaResiduaCorrente.getQuantumArresto();
		      if (lPenaResiduaCorrente.getImportoAmmenda() == null)
		    	  lCalArresti.setImportoAmmenda(new BigDecimal(0).doubleValue());
		      else
		    	  lCalArresti.setImportoAmmenda(lPenaResiduaCorrente.getImportoAmmenda().doubleValue());
		    
		      // Imposto i giorni di LA
		      setRequestAttribute("LAConcesse", new BigDecimal(lCalcPenaModel.getLiberazioneAnticipataGiaConcesse()) );
		      setRequestAttribute("LADaConcedere", new BigDecimal(lCalcPenaModel.getLiberazioneAnticipataDaConcedere()) );

      // Pena in espiazione
	      if (lDataInizioPena!=null)
	      {
		        lCalReclusione.setErrorMsg("Non Libero");
		        lCalReclusione.setDataInizio (lUltimaPenaValidata.getDataInizio());
		        lCalReclusione.setDataFine   (lUltimaPenaValidata.getDataFine());
	      }
	      else 
	      {
	    	  lCalReclusione.setErrorMsg("Libero - PENA RESIDUA");
	      }
    }
    else 
    {
	      // Ergastolo
	      setRequestAttribute("LAConcesse", new BigDecimal(0) );
	      setRequestAttribute("LADaConcedere", new BigDecimal(0) );
      
	      if (lDataInizioPena!=null)
	      {
		        lCalReclusione.setErrorMsg("Non Libero");
		        lCalReclusione.setDataInizio (lUltimaPenaValidata.getDataInizio());
		        lCalReclusione.setDataFine   (lUltimaPenaValidata.getDataFine());
	      }
	      else 
	      {
	    	  	lCalReclusione.setErrorMsg("Libero - PENA RESIDUA");
	      }
    }
    //==========================================================================
    // Passo i dati alla form
    //==========================================================================
    setRequestAttribute("posizioneluogoaltra", lPosLuoAltr);
    setRequestAttribute("PenRes1",lCalReclusione); // Reclusione
    setRequestAttribute("PenRes2",lCalArresti); // Arresti
    setRequestAttribute("PenaComplessiva", lPenComMod);
    setRequestAttribute("PenaResidua", lUltimaPenaValidata);

    //==========================================================================
    // Recupero i dati per i destinatari
    //==========================================================================
/*    //Avvocato
    IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
    Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
    setRequestAttribute("avvocati", lAvvocati);
*/
    //  ricerca magistrato competente
    IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
    MagistratoCompetenteMagistratoModel lMagMod = lMagComp.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
    if (lMagMod != null)
       setRequestAttribute("magistratocompetente", lMagMod);

    //Autorità esterna altra
  //  Option lOptionAutoritaAltra = null;
 //   lOptionAutoritaAltra = new Option(DecodificheManager.getInstance().getAutoritaRdpGE());
 //   setRequestAttribute("autoritaEsternaAltra", "" + lOptionAutoritaAltra);

    //Autorità esterna altra   ottenuta con set filter
    Option lOption = new Option( DecodificheManager.getInstance().getTipoAutoritaEmittente());
    lOption.setFilter( new String[] {"CAP","CAS","CASAP","CSS","GIP","GIPM","GUP","GUPM","TRIBSD","CAPSM","DIB","DIBM"});
    setRequestAttribute("autoritaEsternaAltra", "" + lOption );


    String lPage=IWebConstants.ROOT_DIR+"/files/siap/siep/calcolopena/LoadInserisciAnnotaPagamentoPP.jsp";

    return lPage;
  }
}