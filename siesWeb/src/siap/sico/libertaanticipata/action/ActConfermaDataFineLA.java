package siap.sico.libertaanticipata.action;

/**
* <p>Title: ActConfermaDataFineLA</p>
* <p>Description: Classe Action per la conferma del fine pena sulla liberazione anticipata</p>
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.utente.model.DatiOperazioneModel;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.controller.ICalcoloPena;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActConfermaDataFineLA extends ActionSiap
                                   implements ICostantiLibertaAnticipata
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
   * Azione di Conferma fine pena per la  Liberazione Anticipata
   * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
   * @throws F3BException
   */
   public String processRequest() throws Exception
   {
     FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
     BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

     isFascicoloSiepDiCompetenza();

     /******************************* Posizione Giuridica **********************************/
     IPosizioneGiuridica lCtrlPosGiu = SIEPLookupRemote.getPosizioneGiuridicaRemote();
     PosizioneGiuridicaModel lPosizione  = lCtrlPosGiu.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lIdFascicolo);
     if (lPosizione == null)
       throw new SIEPException(SIEPException.USER_MESSAGE, "Inserire prima la Posizione Giuridica. Impossibile eseguire la richiesta.");

     DatiOperazioneModel lOperMod = new DatiOperazioneModel( getCodUtenteConnesso(),
                                                             DateUtils.getSysDate(),
                                                             getCodUfficioUtenteConnesso(),
                                                             "" );

     //==========================================================================
     // Pagina di ritorno
     //==========================================================================
     String lPage;

     //==========================================================================
     // L'id dell'evento di Ordinanza selezionato da lista o inserito
     //==========================================================================
     BigDecimal lEveIdEventoOrdinanza = getRequestBigDecimalParameter("lEveIdEventoOrdinanza");
     setRequestAttribute("lEveIdEventoOrdinanza", ""+lEveIdEventoOrdinanza);
     
     //==========================================================================
     // Recupera la data fine pena selezionata dall'utente
     //==========================================================================
     Date lDataFineSelezionata = null;
     String lRadioButtonScelto = getRequestStringParameter("DataFine");
     if("DataFineAbInitio".equals(lRadioButtonScelto))
     {
       lDataFineSelezionata = getRequestDateParameter("AnnoDataFineAbInitio", "MeseDataFineAbInitio", "GiornoDataFineAbInitio");
     }
     else
     {
       lDataFineSelezionata = getRequestDateParameter("AnnoDataFineAbFine", "MeseDataFineAbFine", "GiornoDataFineAbFine");
     }
     
     //==========================================================================
     // Costruisce un CalcoloPenaModel che contiene in modo strutturato
     // tutte le informazioni necessarie a determinare la Pena Residua
     // ad un dato momento
     //==========================================================================
     // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     siesLogger.debug("Recupero la situazione attuale della Pena (dati Validati)");
     ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
     CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lIdFascicolo, null);

     // NEW!!!!!!
     Date dataSistemaPerCalcoli = null;
     Date dataScarcerazione = null;
     
     if ( !isRequestParameterNullObj("GiornoDataScarcerazione" )) 
     {
       dataScarcerazione = getRequestDateParameter( "AnnoDataScarcerazione",
                                                    "MeseDataScarcerazione",
                                                    "GiornoDataScarcerazione");
     }     
     
     if (dataScarcerazione!=null)
     {
       dataSistemaPerCalcoli = dataScarcerazione;
     }
     else
     {
       Date lOggi = DateUtils.getSysDate();
       // Devo eliminare minuti e secondi dalla sysdate altrimenti le funzioni
       // di compare tra date falliscono
       String lGiorno = DateUtils.getDateToString(lOggi,"dd");
       String lMese   = DateUtils.getDateToString(lOggi,"MM");
       String lAnno   = DateUtils.getDateToString(lOggi,"yyyy");
       dataSistemaPerCalcoli = DateUtils.getDate( lAnno, lMese, lGiorno );
     }
     
     //==========================================================================
     // Effettuo il calcolo e l'inserimento/aggiornamento della data fine pena
     // della pena residua e inserisco l'eventuale fungibilità
     //==========================================================================
     ICalcoloPena lCtrlCal = SIEPLookupRemote.getCalcoloPenaRemote();
     CalcoloPenaModel lCalcoloPena = lCtrlCal.exCalcoloLiberazioneAnticipata(lCalcoloPenaModel, 
                                                                             lIdFascicolo, 
                                                                             lEveIdEventoOrdinanza,
                                                                             dataSistemaPerCalcoli,
                                                                             lDataFineSelezionata,
                                                                             null,
                                                                             lOperMod);

     //==========================================================================
     //Calcolo della data fine pena considerando non il nuovo metodo di calcolo,
     //ma DATA_FINE_CALCOLATO = DATA_FINE_PENA_RESIDUA_CORRENTE - LA_CONCESSE
     //==========================================================================
     
     // RICERCA DEPOSITO_ORDINANZA_PC per ottenere i giorni di LA concessi
     IDepositoOrdinanzaPc lCtrlDep = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
     DepositoOrdinanzaPcModel lDepOrdMod = lCtrlDep.ExRicercaDepositoOrdinanzaPcByEvento(lEveIdEventoOrdinanza);
     
     int lTotLA = 0;
     if(lDepOrdMod != null && lDepOrdMod.getNumGiorniLibanticipata() != null)
     {
       lTotLA = lDepOrdMod.getNumGiorniLibanticipata().intValue();
     }
          
     if(lPosizione.isMisAlt() || lPosizione.getCodPosizioneGiuridica().equals("04") // Arresti domiciliari ex art.656/10
    	|| lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("70")
    	|| lPosizione.getCodPosizioneGiuridica().equals("71") || lPosizione.getCodPosizioneGiuridica().equals("72") 
    	|| lPosizione.getCodPosizioneGiuridica().equals("82") || lPosizione.getCodPosizioneGiuridica().equals("83")
    	|| lPosizione.getCodPosizioneGiuridica().equals("84") || lPosizione.getCodPosizioneGiuridica().equals("85")
    	|| lPosizione.getCodPosizioneGiuridica().equals("86") || lPosizione.getCodPosizioneGiuridica().equals("87")
        ) 
       lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.ordinescarcerazione.action.ActLoadInserisciOSLiberazioneAnticipataMA&lAzioneOS=libant&"+ICostantiLicenzaLibanticipata.CAMPO_EVE_ID_EVENTO+"="+lEveIdEventoOrdinanza;
     else
       lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.ordinescarcerazione.action.ActLoadInserisciOSLiberazioneAnticipata&lAzioneOS=libant&"+ICostantiLicenzaLibanticipata.CAMPO_EVE_ID_EVENTO+"="+lEveIdEventoOrdinanza;

     FungibilitaModel lFunMod = lCalcoloPena.getFungibilitaCalcolata();
     if(    lFunMod != null
         && (    (lFunMod.getNumAnni()   != null && lFunMod.getNumAnni().intValue()   > 0)
              || (lFunMod.getNumMesi()   != null && lFunMod.getNumMesi().intValue()   > 0)
              || (lFunMod.getNumGiorni() != null && lFunMod.getNumGiorni().intValue() > 0)
             )
       )
     {
       setRequestAttribute("TotaleGiorniLibAnt", "" + lTotLA);
      
       //==========================================================================
       // Restituisco i dati alla finestra di visualizzazione
       //==========================================================================
       setRequestAttribute("PenaResidua"      , lCalcoloPena.getPenaResiduaRicalcolata()); // la pena rideterminata
       setRequestAttribute("PenaGiaEspiata"   , lCalcoloPena.getPenaEspiata());            // pena già espiata
       setRequestAttribute("Fungibilita"      , lCalcoloPena.getFungibilitaCalcolata());   // fungibilità

       setRequestAttribute("dataScarcerazione",dataScarcerazione);

       lPage = ICostantiLicenzaLibanticipata.PG_DETTAGLIO_CALCOLO_PENA;
     }

     return lPage;
   }
}