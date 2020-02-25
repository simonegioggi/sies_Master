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
import siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;


/**
 * <p>Title: ActLoadInserisciAnnotazioneEspulsione</p> 
 * <p>Description: Classe Action per la Load Inserimento dell'annotazione avvenuta
 * espulsione.
 * </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadInserisciAnnotazioneEspulsione extends ActionSiap implements ICostantiSanzioneSostitutiva
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
    if (   lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")  )
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
    // - Pena Residua
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
    
    if (lSanzSost==null || !lSanzSost.getCodTipoSanzione().equals("E")) // E = Espulsione
    {
      // Non esiste Sanzione Sostitutiva di Espulsione
      RedirectTo lRedirigi = new RedirectTo();

      lRedirigi.setPage(IWebConstants.PG_MAIN);
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,
                          "Per il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr() + " non risulta iscritta una Espulsione a titolo di Sanzione Sostitutiva. Rivedere Pena Complessiva.");
      lRedirigi.setAction("siap.siep.sanzionesostitutiva.action.ActGestioneEspulsione");
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

      return IWebConstants.PG_MESSAGE;
    }
    
    //==========================================================================
    // Recupero la pena residua (ultima validata)
    //==========================================================================
//    IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();
//    PenaResiduaModel lUltimaPenaValidata = lCtrlPenRes.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo);
//    if (lUltimaPenaValidata == null){
//    }

    //==========================================================================
    // Gestita solo le posizioni giuridiche 
    // - 01 = Custodia Cautelare per Questa Causa in Regime di Detenzione
    // - 02 = Custodia Cautelare per Questa Causa in Regime di Arresti Domiciliari	
    // - 07 = Libero (PRIMA)
    // - 10 = Libero (Revocata Sanzione)
    // - 06 = Internato (?)
    //==========================================================================
    if (   lPosLuoAltr.getPosizioneGiuridica().getCodPosizioneGiuridica()!= null
        && !lPosLuoAltr.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("01")
        && !lPosLuoAltr.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("02")
        && !lPosLuoAltr.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("07")
        && !lPosLuoAltr.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("10")
        //&& !lPosLuoAltr.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("06")
       )
    {
      RedirectTo lRedirigi = new RedirectTo();

      lRedirigi.setPage(IWebConstants.PG_MAIN);
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Posizione giuridica '"+lPosLuoAltr.getPosizioneGiuridica().getDescrPosizioneGiuridica()+"' non prevista per questa funzione!");
      lRedirigi.setAction("siap.siep.sanzionesostitutiva.action.ActGestioneEspulsione");
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

      return IWebConstants.PG_MESSAGE;
    }
    
    //==========================================================================
    // Ricerca l'ultima pena residua per quel fascicolo
    //==========================================================================
    IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
    PenaResiduaModel lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

    if(   lPenaResMod == null
       || (   lPosLuoAltr.getPosizioneGiuridica().getCodPosizioneGiuridica()!= null
           && !lPosLuoAltr.getPosizioneGiuridica().isLibero()
           && !lPosLuoAltr.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("-")
           && lPenaResMod != null && lPenaResMod.getDataInizio()== null 
          )
      )
    {
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
      if (lPenaResMod==null) {
        setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?");
      }
      else {
        setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Pena Residua da Espiare incoerente con Posizione Giuridica. Eseguire Calcolo della pena?");
      }
      
      lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&" +
                          ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

      return IWebConstants.PG_MESSAGE;
    }

    if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N"))
    {
      setRequestAttribute("dataeditabile","S");
    }
    else
    {
    }
    
    //==========================================================================
    // Caricamento combo
    //==========================================================================
    // Combo Autorità (dominio TIPO_AUTORITA con filtro e Questura preselezionata)
    // Filtro e preselezione inserità in fase di revisione 22/05/2008
    Option lOptionAutorita = null;
    lOptionAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita(),"20");
    
    // Per ora non sono previsti filtri
    String[] lFilter = new String[10];
    lFilter[0] = "-";
    lFilter[1] = "92"; // Carabinieri
    lFilter[2] = "28"; // Carabinieri - Comando Stazione 
    lFilter[3] = "58"; // Carabinieri - Nucleo Operativo
    lFilter[4] = "19"; // Commissariato di P.S.
    lFilter[5] = "32"; // Guardia di Finanza
    
    lFilter[6] = "93"; // Polizia di Stato
    lFilter[7] = "94"; // Polizia Municipale
    lFilter[8] = "79"; // Polizia Penitenziaria
    lFilter[9] = "20"; // Questura
    lOptionAutorita.setFilter(lFilter);
  
    setRequestAttribute("tipoAutorita", "" + lOptionAutorita );
    
    //=====================================
    // Ricerca magistrato competente
    //=====================================
    IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
    MagistratoCompetenteMagistratoModel lMagMod = lMagComp.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
    if (lMagMod != null)
       setRequestAttribute("magistratocompetente", lMagMod);
    

	  //======================
	  //	
    //======================
    setRequestAttribute("sanzioneEspulsione", lSanzSost);
    setRequestAttribute("penaresidua", lPenaResMod);
    setRequestAttribute("posizioneluogoaltra", lPosLuoAltr);

	  return PG_LOAD_INSERISCI_ANNOTAZIONE_ESPULSIONE;	
  }
}