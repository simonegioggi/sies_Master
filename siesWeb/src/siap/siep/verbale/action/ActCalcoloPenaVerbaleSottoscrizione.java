package siap.siep.verbale.action;


/**
* <p>Title: ActCalcoloPenaVerbaleSottoscrizione</p>
* <p>Description: Classe Action per il calcolo delle pena per la registrazione data inizio misura</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;


public class ActCalcoloPenaVerbaleSottoscrizione extends ActionSiap implements ICostantiVerbale
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
  * Azione di Inserimento Aggiornamento del PenaResidua nel caso di Verbale
  * Sottoscrizione agli Obblighi di condannato Libero(). In questo caso va 
  * ricalcolato il quantum di pena e soprattutto le date di decorrenza a partire
  * dalla data di firma del Verbale.
  * @return Nome della pagina JSP da visualizzare del risultato al termine dell'elaborazione
  * @throws F3BException
  */
  public String processRequest() throws Exception
  {
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    PenaResiduaModel lPenModFin = new PenaResiduaModel();

    // n.b. questa Action NON viene invocata dalle jsp ma dalla ActLoadDettaglioVerbaleSottoscrizione
    VerbaleModel lVerModel = new VerbaleModel();
    lVerModel = (VerbaleModel)getRequestAttribute("verbale");

    String vedoDataIntermedia = "N";

    Date lDataFinePenaPres = null;
    //Date lDataInizioArresto = null;
    //Date lDataFineReclusione= null;

    Date lDataInizio = lVerModel.getDataEmissione();

    // Recupero l'ultima pena residua a sistema (validata o meno)
    PenaResiduaModel lPenMod = new PenaResiduaModel();
    IPenaResidua IPenRes = SIEPLookupRemote.getPenaResiduaRemote();
    lPenMod = IPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

    PenaResiduaModel lPenaModel = new PenaResiduaModel();

    //========================================================================
    // Recupero i quantum di pena Validati che concorrono alla calcolo della
    // pena 
    //========================================================================
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("Recupero la situazione attuale della Pena (dati Validati)");
    ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
    CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lFascMod.getIdFascicoloSiep(), null);

// ***********************************NO*ERGASTOLO********************************************/
    //===========================================================================
    // Il ricalcolo viene effettuato solo se non Ergastolo, altrimenti viene 
    // semplicemente impostato il fine pena a 31/12/9999
    //===========================================================================
    if(lPenMod.getFlagErgastolo().equals("N"))
    {
//      ICalcoloPena ICalPen = SIEPLookupRemote.getCalcoloPenaRemote();
//
//      Vector lVectFine =  ICalPen.exCalcolaDataFinePena(lDataInizio,lPenMod, true); // il flag true indica CON DIES_A_QUO
//
//      if(lVectFine.size() == 0 )
//      {
//        throw new F3BException(F3BException.USER_MESSAGE,"Impossibile calcolare la data di Fine Pena");
//      }
//
//      if(lVectFine.size() == 1 )
//      {
//        lDataFinePena = (Date) lVectFine.get(0);
//      }
//      
//      if(lVectFine.size() == 2)
//      {
//        lDataFinePena = (Date) lVectFine.get(1);
//        lDataFineReclusione = (Date) lVectFine.get(0);
//        lDataInizioArresto = DateUtils.moveDateTo(lDataFineReclusione,Calendar.DAY_OF_MONTH,1);
//        lPenaModel.setDataInizioArresto(lDataInizioArresto);
//        lPenaModel.setDataFineReclusione(lDataFineReclusione);
//        vedoDataIntermedia = "S";
//      }

     lPenaModel = lCalcoloPenaModel.getPenaDaEspiare(lDataInizio, null,"all");
     lDataFinePenaPres = lPenaModel.getDataFine();
     //lPenaModel.setDataFinePresunta(lDataFinePena);
     // lPenMod.setDataFine(lDataFinePena);
     // lPenMod.setDataInizioArresto(lDataInizioArresto);
     if(lPenaModel != null && (lPenaModel.getDataFineReclusione() != null ||
                               lPenaModel.getDataInizioArresto() != null))
     {
       vedoDataIntermedia = "S";
     }
   }
   else  //inizio ergastolo
   {
     lPenaModel = new PenaResiduaModel(lPenMod);
     lDataFinePenaPres = DateUtils.getDate(9999,12,31);
     lPenaModel.setDataFine(lDataFinePenaPres);
   } //fine ergastolo

    lPenaModel.setDataInizio(lDataInizio);
    lPenaModel.setDataFinePresunta(lDataFinePenaPres);

    int lTotGiorniLA = lCalcoloPenaModel.getLiberazioneAnticipata();
    int lTotGiorniRD = lCalcoloPenaModel.getRimediRisarcitori();
    

/*
     if( lPenaModel.getDataFinePresunta() != null )
     {
       // CERCA IL TOTALE GIORNI LIB ANTICIPATA
       ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
       // N.B. cerca quelli con FLAG_ELABORATO ad N o null
       lTotGiorni = lCtrlLib.ExTotalePeriodiConcessiNonElaboratiByIdFascicoloSiep(lFascMod.getIdFascicoloSiep());

       lPenaModel.setDataFine( lPenaModel.getDataFinePresunta() );


       if(lTotGiorni != 0)
       {
         Date lDataFineRicalcolata = DateUtils.moveDateTo( lPenaModel.getDataFinePresunta(), Calendar.DAY_OF_MONTH, -(lTotGiorni) );

//-- NON TOCCA PIU' I QUANTUM
         //lPenaModel = PenaResiduaUtil.calcolaPenaNuovaDataFine(lDataFineRicalcolata, lPenaModel, true);

         lPenaModel.setDataFine(lDataFineRicalcolata);

       }
     }
*/

     // Perchè il metodo PenaResiduaUtil.calcolaPenaNuovaDataFine() non tocca
     // la DATA_FINE_PRESUNTA
     //lPenaModel.setDataFinePresunta(lPenaModel.getDataFine());
     lPenaModel.setEveIdEvento(lVerModel.getEveIdEvento());
     // n.b. Devo tenere la stesso stato dell'ultima pena per consentire alla 
     //      funzione di aggiornamento di stabilire se Aggiornare (non validata) 
     //      o inserire (se validato)
     lPenaModel.setFlagValidato(lPenMod.getFlagValidato());
     // aggiunto il 10/01/2008 serve alla ExAggiornaPenaVerbale per andare in update per pena non validate
     lPenaModel.setIdPenaResidua(lPenMod.getIdPenaResidua()); // A8RR004
     //------
     lPenaModel.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
     lPenaModel.setFlagErgastolo(lPenMod.getFlagErgastolo());
     lPenaModel.setDiesAQuo("S");

     // Se l'ultima pena residua era Validata
     if(lPenMod.getFlagValidato().equals("S"))
     {
      lPenaModel.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
      lPenaModel.setCodOperatoreInserimento(this.getCodUtenteConnesso());
      lPenaModel.setDataInserimento(DateUtils.getSysDate());

      lPenaModel.setCodUfficioAggiornamento(null);
      lPenaModel.setCodOperatoreAggiornamento(null);
      lPenaModel.setDataAggiornamento(null);
    }

    if(lPenMod.getFlagValidato().equals("N"))
    {
      lPenaModel.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
      lPenaModel.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
      lPenaModel.setDataAggiornamento(DateUtils.getSysDate());
    }
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("xxx lPenModFin = "+lPenModFin);

    //==========================================================================
    // Aggiorna la pena_residua e le LA (da N a E)
    //==========================================================================
    IVerbale IVerCtrl = SIEPLookupRemote.getVerbaleRemote();
    lPenModFin = IVerCtrl.ExAggiornaPenaVerbale(lPenaModel);

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("xxx lPenModFin = "+lPenModFin);
//prepara i dati per la jsp
    setRequestAttribute("penaresidua", lPenModFin);
    setRequestAttribute("vedoDataIntermedia", vedoDataIntermedia);
    setRequestAttribute("verbale",lVerModel);
    setRequestAttribute("cssa",getRequestAttribute("cssa"));
    setRequestAttribute("istitutodetenzione", getRequestAttribute("istitutodetenzione"));
    setRequestAttribute("lTotGiorniConcessi",""+lTotGiorniLA);
    setRequestAttribute("lTotGiorniRDConcessi",""+lTotGiorniRD);
    

    return PG_LOAD_DETTAGLIO_PENA_RESIDUA_VERBALE_SOTTOSCRIZIONE;
  }
}