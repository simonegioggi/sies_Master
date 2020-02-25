package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;
import siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;


/**
 * <p>Title: ActLoadInserisciComunicazioneNuovoResiduoPena</p> 
 * <p>Description: Classe Action per la Load Inserimento Comunicazione Nuovo 
 * residuo pena delle Sanzioni Sostitutive
 * </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadInserisciComunicazioneNuovoResiduoPena extends ActionSiap implements ICostantiSanzioneSostitutiva
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws F3BException
  {
	  
    //==========================================================================
    // Sezione con i controlli preliminari
    //==========================================================================
    // Controllo Presenza del Fascicolo in Sessione
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

    this.isFascicoloSiepDiCompetenza();
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

    // Controllo Validazione Fascicolo
    if (lFascMod.getFlagValidato().equalsIgnoreCase("N"))
    {
      RedirectTo lRedirigi = new RedirectTo();

      lRedirigi.setPage(IWebConstants.PG_MAIN);
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,
                          "Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile procedere!");
      lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
                          ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

      return IWebConstants.PG_MESSAGE;
    }

    // Controllo Fascicolo definito
    if ( lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO") )
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
    
    // Verifico se esiste evento non validato
    this.isEventoNonValidato();
    
    //==========================================================================
    // Sezione per il recupero dei dati da visualizzare nella form.
    // - Posizione giuridica
    // - Pena Residua (con SS de eseguire/eseguita)
    // - Combo 
    //==========================================================================
    IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltr = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lIdFascicolo);

    if (lPosLuoAltr == null || lPosLuoAltr.getPosizioneGiuridica() == null)
      throw new SIEPException(SIEPException.USER_MESSAGE, "Inserire prima la Posizione Giuridica. Impossibile eseguire la richiesta.");

    //==========================================================================
    // Recupero la Sanzione Sostitutiva In Sentenza 
    //==========================================================================
    IPenaComplessiva lCtrlPenaComp = SIEPLookupRemote.getPenaComplessivaRemote();
    PenaComplessivaSanzioneSostitutivaModel lPenaCompSanzModel = lCtrlPenaComp.ExRicercaPenaComplessivaSanzioneSostitutivaByIdFascicoloSiep(lIdFascicolo);
    
    SanzioneSostitutivaModel lSanzSost = lPenaCompSanzModel.getSanzioneSostitutiva();
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lSanzSost = "+lSanzSost);
    
    if (lSanzSost==null || lSanzSost.getIdSanzioneSostitutiva()==null)
    {
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,
                          "Per il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr() + " non risulta presente una Sanzione Sostitutiva!");
      return IWebConstants.PG_MESSAGE;
    }
    
    //==========================================================================
    // Ricerca l'ultima pena residua per quel fascicolo (validata o meno)
    //==========================================================================
    IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
    PenaResiduaModel lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

    if(   lPenaResMod == null
       || (    lPosLuoAltr.getPosizioneGiuridica().getCodPosizioneGiuridica()!= null
           && !lPosLuoAltr.getPosizioneGiuridica().isLibero()
           && !lPosLuoAltr.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("-")
           && lPenaResMod != null && lPenaResMod.getDataInizio()== null 
          )
      )
    {
      RedirectTo lRedirigi = new RedirectTo();

      if (lPenaResMod==null) {
        lRedirigi.setPage(IWebConstants.PG_MAIN);
        lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&" +
            ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
        setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?");
        setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
      }
      else {
        //lRedirigi.setPage(IWebConstants.PG_MAIN);
        setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Pena Residua da Espiare incoerente con Posizione Giuridica. Il condannato risulta 'non libero', ma manca la data inizio pena.");
        //lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&" +
        //    ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
        //setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
      }
      

      return IWebConstants.PG_MESSAGE;
    }

    //==========================================================================
    // Verifico se è stata calcolata una SS da eseguire e se è di tipo Semidetenzione
    // o Libertà controllata, infatti solo in questi casi la pena sostituita 
    // (Sanzione) può essere rideterminata per effetto di computi sulla detentiva 
    //==========================================================================
    ISanzioneSostitutiva lSSCtrl = SIEPLookupRemote.getSanzioneSostitutivaRemote();
    SanzioneSostResiduaModel lSSResiduaModel = lSSCtrl.getUltimaSSResidua(lIdFascicolo,null);
    //SanzioneSostResiduaModel lSSResiduaModel = lSSCtrl.getSSByIdPenaResidua(lPenaResMod.getIdPenaResidua());
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lSSResiduaModel = "+lSSResiduaModel);
    if (lSSResiduaModel==null || lSSResiduaModel.getIdSanzioneSostResidua()==null)
    {
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
      lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&" +
                          ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,
                          "Per il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr() + " non risulta presente una Sanzione Sostitutiva Residua. Rieffettuare il calcolo della pena !");
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

      return IWebConstants.PG_MESSAGE;
      
    }
    
    // Inserisco la SS residua nel model della PR
    lPenaResMod.setSanzSostResidua(lSSResiduaModel);
    
    
    //==========================================================================
    // New dopo rilascio 3.1 upd 01
    // Se la pena non è validata avverto l'utente che deve rieffettuare il 
    // calcolo della pena il quanto potrebbe aver aggiunto dei presofferti in
    // sentenza che non vengono computati se non si rieffettua il calcolo pena
    //==========================================================================
    String lFromCalcoloPena = "N"; // Indica se la chiamata proviene dalla maschera di calcolo pena
    if (!isRequestParameterNullObj("FromCalcoloPena"))
    {
      lFromCalcoloPena = "S";
    }
      
    if (   (lPenaResMod.getFlagValidato()==null || "N".equals(lPenaResMod.getFlagValidato()) )
        && lFromCalcoloPena.equals("N")  
       )
    {
      // Pena non validata e non provengo dalla Maschera di Calcolo Pena
      setRequestAttribute("checkCalcoloPena", "S");
    }
    
    
    //=====================================
    // Ricerca magistrato competente
    //=====================================
    IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
    MagistratoCompetenteMagistratoModel lMagMod = lMagComp.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
    if (lMagMod != null){
      setRequestAttribute("magistratocompetente", lMagMod);
    }

    //==========================================================================
    // Caricamento combo
    //==========================================================================
    // Combo Autorità (dominio TIPO_AUTORITA senza filtro)
    Option lOptionAutorita = null;
    lOptionAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita());
    setRequestAttribute("tipoAutorita", "" + lOptionAutorita );
    
    //==========================================================================
    // Magistrato di Sorveglianza
    //==========================================================================
    
	  //======================
	  //	
    //======================
    setRequestAttribute("penaresidua", lPenaResMod);
    setRequestAttribute("posizioneluogoaltra", lPosLuoAltr);

	  return PG_LOAD_INSERISCI_COM_NUOVO_RES_PENA;	
  }
}