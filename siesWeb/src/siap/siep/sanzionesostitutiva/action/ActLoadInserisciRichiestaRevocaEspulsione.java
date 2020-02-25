package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;


/**
 * <p>Title: ActLoadInserisciRichiestaRevocaEspulsione</p> 
 * <p>Description: Classe Action per la Load Inserimento della Richiesta al GE 
 * di Revoca Espulsione
 *  * </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadInserisciRichiestaRevocaEspulsione extends ActionSiap implements ICostantiSanzioneSostitutiva
{
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
    if (   lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO") )
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
    // Gestita solo le posizioni giuridiche 
    // - 07 = Libero (PRIMA)
    // - (?)02 = Custodia Cautelare per Questa Causa in Regime di Arresti Domiciliari (?)
    // - 01 = Custodia Cautelare per Questa Causa in Regime di Detenzione
    // - (?)06 = Internato
    // - Detenuto altra causa
    //==========================================================================
    if (   lPosLuoAltr.getPosizioneGiuridica().getCodPosizioneGiuridica()!= null
        && !lPosLuoAltr.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("07") // Libero prima
        && !lPosLuoAltr.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("10") // Libero dopo
        && !lPosLuoAltr.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("26") // 26 - Espulso
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
    // Ricerca l'ultima pena residua per quel fascicolo (validata o meno)
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

    // Se l'ultima pena non è validata rendo la fine pena editabile
    if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N"))
    {
      setRequestAttribute("dataeditabile","S");
    }
    else
    {
    }
    
    
    //=======================
    // Combo Tipo Richiesta
    //=======================
    Option tipoRichiesta = null;
    tipoRichiesta = new Option(DecodificheManager.getInstance().getMotivoProvvedimento());
    String[] lFilter = new String[5];
    lFilter[0] = "-";
    lFilter[1] = "0932"; // Richiesta revoca espulsione per irreperibilità
    lFilter[2] = "0933"; // Richiesta revoca espulsione per sanzione applicata a cittadino comunitario
    lFilter[3] = "0938"; // Richiesta Revoca Espulsione per Rientro nel Territorio dello Stato senza Autorizzazione
    lFilter[4] = "0939"; // Richiesta Revoca Espulsione per Altro Motivo
    tipoRichiesta.setFilter(lFilter);
    setRequestAttribute("tipoRichiesta", "" + tipoRichiesta );
    
    
    //==========================================================================
    // Combo Ufficio Giudice dell'Esecuzione
    //==========================================================================
    String[] aFiltroUffici = {"CAP", "CAS", "CASAP", "GIP", "DIB", "TRIBSD"};
    Option lOption  = new Option( DecodificheManager.getInstance().getTipoUfficio(), true);
    lOption.setValueBlankItem("-");
    lOption.setFilter(aFiltroUffici);

    //setRequestAttribute("UfficioGE", "" + lOption );
    
    SentenzaModel lSentMod = (SentenzaModel) lFascMod.getSentenza();
    Option lOptionUffGE = new Option(DecodificheManager.getInstance().getTipoUfficio(), lSentMod.getCodTipoAutoritaEmittente());
    setRequestAttribute("UfficioGE", "" + lOptionUffGE);
    setRequestAttribute("sentenza", lSentMod);
    
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
    setRequestAttribute("penaresidua", lPenaResMod);
    setRequestAttribute("posizioneluogoaltra", lPosLuoAltr);

	  return PG_LOAD_INSERISCI_RICHIESTA_REVOCA_ESPULSIONE;	  
  }
}