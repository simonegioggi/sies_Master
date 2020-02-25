package siap.siep.calcolopena.action;

/**
* <p>Title: ActInserisciNuovaPenaValidata</p>
* <p>Description: Classe Action per l'inserimento della nuova Pena validata</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.util.CalendarUtil;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fungibilita.controller.IFungibilita;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
//import siap.siep.scadenzario.controller.IScadenzario;
//import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciNuovaPenaValidata extends ActionSiap
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
/**
* Azione di Inserimento della nuova Pena Residua validata. Questa action recupera
* i dati della pena residua direttamente dalla jsp e imposta il flag validata a N.
* Inoltre se è presente anche una fungibilità inserita
* contestualmente al calcolo della pena residua, valida anche questo record.
*
* Richiamata SOLO dalla
* - VediNuovoCalcoloPenaNEW.jsp
*
* Quindi viene utilizzata per la conferma dei dati del calcolo della pena e
* della fungibilità nel caso di:
* - richieste al GE con anticipazione
* - decisioni del GE
* - computi (presofferto, fungibilità MC, fungibilità PD)
*
* @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
*         tale jsp è la pagina di visualizzazione del dettaglio della pena
*         validata (decorrenza/scadenza e quantum) e di scelta delle stampe da
*         produrre
* @throws F3BException
*/
  public String processRequest() throws Exception
  {
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

    // Recupero l'id dell'ultima Annotazione Manuale inserita
    // per passarlo alla JSP
    if ( !isRequestParameterNullObj(ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE) )
    {
      String lIdAnnotazioneManuale = getRequestStringParameter(ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE);
      setRequestAttribute ("IdAnnotazioneManuale", lIdAnnotazioneManuale);
    }

    Date dataFinePena = null;
    boolean cancellaFungibilita = false;

    //==========================================================================
    // Nuova gestione della fungibilità.
    // In caso di fungibilità
    // APV,MPV,GPV = data fine pena calcolata. In caso di fungibilità non è
    //               possibili cambiarla per l'utente
    //==========================================================================
    if (!isRequestParameterNullObj("flagFungibilita"))
    {
      String flagFungibilita = getRequestStringParameter("flagFungibilita");
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("flagFungibilita = "+flagFungibilita);
      if (flagFungibilita.equals("1")){
        // Data fine pena = data di calcolo (sysdate o data selezionata dall'operatore) con fungibilità.
        cancellaFungibilita = false;

        if ( !isRequestParameterNullObj("GPS") && !getRequestStringParameter("GPS").equals(""))
          dataFinePena = getRequestDateParameter("APS","MPS","GPS");
//        if ( !isRequestParameterNullObj("GPV") && !getRequestStringParameter("GPV").equals(""))
//          dataFinePena = getRequestDateParameter("APV","MPV","GPV");
      }
      else if (flagFungibilita.equals("2")){
        // Data fine pena = data fine pena provvisoria, quella calcolata
        // con i quantum e fungibilità nulla
        cancellaFungibilita = true;

        if ( !isRequestParameterNullObj("GPV") && !getRequestStringParameter("GPV").equals(""))
          dataFinePena = getRequestDateParameter("APV","MPV","GPV");
      }
      else if (flagFungibilita.equals("3")){
        // Data fine pena = data di calcolo (sysdate o data selezionata dall'operatore)
        // annulla fungibilità
        cancellaFungibilita = true;

        if ( !isRequestParameterNullObj("GPS") && !getRequestStringParameter("GPS").equals(""))
          dataFinePena = getRequestDateParameter("APS","MPS","GPS");
      }
    }

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("dataFinePena = "+dataFinePena);

    //==========================================================================
    // Se è presente la fungibilità, verifica se validarla o eliminarla in
    // funzione delle scelte dell'utente
    //==========================================================================
    if (   !isRequestParameterNullObj("IdFungibilita")
        && !getRequestStringParameter("IdFungibilita").equals("")
        && !getRequestStringParameter("IdFungibilita").equals("null"))
    {
      IFungibilita IFung = SIEPLookupRemote.getFungibilitaRemote();
      BigDecimal idFungibilita = getRequestBigDecimalParameter("IdFungibilita");
      if (cancellaFungibilita)
      {
        FungibilitaModel lFunModel = new FungibilitaModel();
        lFunModel.setIdFungibilita(idFungibilita);
        IFung.ExCancellaFungibilita(lFunModel);
      }
      else
      {
        IFung.ExValidaFungibilita ( idFungibilita );
      }
    }

    //==========================================================================
    //
    //==========================================================================
    PenaResiduaModel lPenRes=new PenaResiduaModel();
    //lPenRes.setFlagValidato("S");
    lPenRes.setFlagValidato("N");

    if (   !isRequestParameterNullObj("IdPenaResidua")
        && !getRequestStringParameter("IdPenaResidua").equals("")
        && !getRequestStringParameter("IdPenaResidua").equals("null")
       )
    {
      lPenRes.setIdPenaResidua( getRequestBigDecimalParameter("IdPenaResidua") );
    }

//    if (!isRequestParameterNullObj("GPV") && !getRequestStringParameter("GPV").equals(""))
//      lPenRes.setDataFine(getRequestDateParameter("APV","MPV","GPV"));
    //==========================================================================
    // Imposto il fine pena. Se dataFinePena!=null vuol dire che hoh fungibilità
    // è la data fine pena è stata scelta dall'utente tramite una delle tre
    // possibilita (check), altrimenti la recupero direttamente dai campi
    //==========================================================================
    if (dataFinePena!=null)
    {
      lPenRes.setDataFine(dataFinePena);
    }
    else if (!isRequestParameterNullObj("GPV") && !getRequestStringParameter("GPV").equals(""))
    {
      lPenRes.setDataFine(getRequestDateParameter("APV","MPV","GPV"));
    }

    //==========================================================================
    // Recupero i Quantum rideterminati
    // Nuova gestione dei quantum rideterminati (02-10-2006)
    // Se negativi vengono azzerati in quanto già inglobati nella fungibilità
    //==========================================================================
    CalendarUtil lCalUtil = new CalendarUtil();

    CalendarModel lCalReclusione = new CalendarModel();
    CalendarModel lCalArresti    = new CalendarModel();

    lCalReclusione.setNumAnni   (getRequestBigDecimalParameter("Arec"));
    lCalReclusione.setNumMesi   (getRequestBigDecimalParameter("Mrec"));
    lCalReclusione.setNumGiorni (getRequestBigDecimalParameter("Grec"));

    lCalArresti.setNumAnni   (getRequestBigDecimalParameter("Aarr"));
    lCalArresti.setNumMesi   (getRequestBigDecimalParameter("Marr"));
    lCalArresti.setNumGiorni (getRequestBigDecimalParameter("Garr"));

    if (   lCalUtil.isPositiveTime(lCalReclusione) // >=0
        && lCalUtil.isPositiveTime(lCalArresti)    // >=0
       )
    {
      lPenRes.setNumAnniReclusione   (getRequestBigDecimalParameter("Arec"));
      lPenRes.setNumMesiReclusione   (getRequestBigDecimalParameter("Mrec"));
      lPenRes.setNumGiorniReclusione (getRequestBigDecimalParameter("Grec"));
      lPenRes.setImportoMulta        (getRequestBigDecimalParameter("Multa"));

      lPenRes.setNumAnniArresto      (getRequestBigDecimalParameter("Aarr"));
      lPenRes.setNumMesiArresto      (getRequestBigDecimalParameter("Marr"));
      lPenRes.setNumGiorniArresto    (getRequestBigDecimalParameter("Garr"));
      lPenRes.setImportoAmmenda      (getRequestBigDecimalParameter("Ammenda"));
    }
    else
    {
      // Azzero i quantum della pena residua rideterminata
      lPenRes.setNumAnniReclusione   (new BigDecimal(0));
      lPenRes.setNumMesiReclusione   (new BigDecimal(0));
      lPenRes.setNumGiorniReclusione (new BigDecimal(0));
      lPenRes.setImportoMulta        (getRequestBigDecimalParameter("Multa"));

      lPenRes.setNumAnniArresto      (new BigDecimal(0));
      lPenRes.setNumMesiArresto      (new BigDecimal(0));
      lPenRes.setNumGiorniArresto    (new BigDecimal(0));
      lPenRes.setImportoAmmenda      (getRequestBigDecimalParameter("Ammenda"));
    }

    //==========================================================================
    // Recupero le date di decorrenza
    //==========================================================================
    // Recupero la Data Inizio Pena
    if(!isRequestParameterNullObj("Gdatainiziopena"))
    {
      lPenRes.setDataInizio(getRequestDateParameter("Adatainiziopena",
                                                    "Mdatainiziopena",
                                                    "Gdatainiziopena"));
    }

    // Recupero la Data Fine Pena Reclusione
    if(!isRequestParameterNullObj("Gdatafinereclusione"))
    {
      lPenRes.setDataFineReclusione(getRequestDateParameter("Adatafinereclusione",
                                                            "Mdatafinereclusione",
                                                            "Gdatafinereclusione"));
    }

    // Recupero la Data Inizio Arresto
    if(!isRequestParameterNullObj("Gdatainizioarresto"))
    {
      lPenRes.setDataInizioArresto(getRequestDateParameter("Adatainizioarresto",
                                                           "Mdatainizioarresto",
                                                           "Gdatainizioarresto"));
    }

    // Recupero la Data Fine Pena Presunta
    if(!isRequestParameterNullObj("Gdatafinepenapresunta"))
    {
      lPenRes.setDataFinePresunta(getRequestDateParameter("Adatafinepenapresunta",
                                                          "Mdatafinepenapresunta",
                                                          "Gdatafinepenapresunta"));
    }

    //==========================================================================
    //???????????????  INUTILE, setta la data fine presunta con lo stesso valore
    // della istruzione precedente
    //==========================================================================
    if (    getRequestStringParameter("FlagAltraCausa").equals("S")
         && !isRequestParameterNullObj("Gdatainiziopena")
         || (   isRequestParameterNullObj("GPV")
             || getRequestStringParameter("GPV").equals("")
            )
       )
    {
      if (!isRequestParameterNullObj("Adatafinepenapresunta"))
        lPenRes.setDataFinePresunta(getRequestDateParameter("Adatafinepenapresunta",
                                                            "Mdatafinepenapresunta",
                                                            "Gdatafinepenapresunta"));
    }

//*********************************************************************
    //** DL AGGIUNTO PER LE ANNOTAZIONI MANUALI
    if( !isRequestParameterNullObj("IdEvento") )
      lPenRes.setEveIdEvento(getRequestBigDecimalParameter("IdEvento"));

    lPenRes.setFasSieIdFascicoloSiep(lIdFascicolo);
    lPenRes.setDiesAQuo("N");
    lPenRes.setFlagErgastolo("N");
    lPenRes.setCodOperatoreInserimento (getCodUtenteConnesso());
    lPenRes.setCodUfficioInserimento   (getCodUfficioUtenteConnesso());
    lPenRes.setDataInserimento         (DateUtils.getSysDate());
//*********************************************************************
    
    if( !isRequestParameterNullObj("FlagPenaSospesa") )
      lPenRes.setFlagPenaSospesa(getRequestStringParameter("FlagPenaSospesa"));
    
    //==========================================================================
    // Inserimento aggiornamento pena residua con i dati recuperati dalla
    // form
    //==========================================================================
    IPenaResidua lPR = SIEPLookupRemote.getPenaResiduaRemote();
    lPenRes = lPR.ExInsertOrUpdatePenaResidua(lPenRes);

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("DOPO ExInsertOrUpdatePenaResidua PenRes="+lPenRes);
    //==========================================================================
    //                     Prepara la pagina di destinazione
    // la pagina di destinazione visualizza il dettagli della pena:
    // - quantum
    // - decorrenza/scadenza
    // e l'elenco delle stampe producibili
    //==========================================================================

    //==========================================================================
    // Pena Complessiva (utilizzata dalla IntestazionePenaValidataAnnotazioni.jsp)
    // impoortata dalle pagine di stampa
    //==========================================================================
    //ANNA 
    //IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
    //PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);
    //setRequestAttribute("PenaComplessiva", lPenComMod);
    //ANNA 17/09
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("DOPO ExInsertOrUpdatePenaResidua!!!!!!!!!");

    //ANNA Aggiunto Settembre 2010 Cerca l'ultima pena residua validata...
    ///////Verificare se serve ultima validata o la corrente!!!!!
    //lPenRes = lPR.ExRicercaPenaResiduaUltimaValidata(lFascMod.getIdFascicoloSiep());
    //...se non la trova cerca l'ultima in assoluto
    //if(lPenRes == null)
    	lPenRes = lPR.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());
    	
    	if (!isRequestParameterNullObj("CodPosizioneGiuridica"))
    	{
    	    setRequestAttribute ("CodPosizioneGiuridica", getRequestStringParameter("CodPosizioneGiuridica") );
    	}
    		
    setRequestAttribute("PenaResidua", lPenRes);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("PenaResidua="+lPenRes);

    // da modificare , gestire correttamente i flags
    if (!isRequestParameterNullObj("lFlagPage"))
    {
      String lFlagPage = getRequestStringParameter("lFlagPage");

      //========================================================================
      // Il paramentro lFlagPage indica la provenienza della chiamata e assume i
      // seguenti valori:
      // Rideterminazione pena / Richieste al GE (Depenalizzazione, Incostituzionalità, Amnistia/Indulto)
      //   lFlagPage = GE
      // Rideterminazione pena / Provvedimenti del PM
      //   lFlagPage = A - Altro Titolo (Fungibilità altro reato Misura Cautelare)
      //   lFlagPage = S - Senza Titolo (Fungibilità altro reato Pena Detentiva)
      //   lFlagPage = D - Stesso Titolo (Presofferto)
      //Rideterminazione pena / Pagamento PenePecuniarie
	  //	lFlagPage = ANNOTAPP
      // Rideterminazione pena / Altro
      //   lFlagPage = ALTRO
      // Decisioni del GE / Applicazione Benefici
      //   lFlagPage = AMNI   - Aministia
      //   lFlagPage = DEPEN  - Depenalizzazione
      //   lFlagPage = INCOST - Incostituzionalità
      //========================================================================
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Ritorno alla chiamante ");
      if (lFlagPage.equals("GE"))
      { // Pagina con le stampe delle richieste al GE
        return IWebConstants.ROOT_DIR +"/files/siap/siep/calcolopena/VediCalcoloPenaValidataRichiestaGE.jsp";
      }
      else if(lFlagPage.equals("A") || lFlagPage.equals("S") || lFlagPage.equals("D"))
      { //
        setRequestAttribute("lFlagPage", lFlagPage);
        return IWebConstants.ROOT_DIR +"/files/siap/siep/calcolopena/VediCalcoloPenaValidataAnnotazioniComputo.jsp";
      }
      else if(lFlagPage.equals("AMNI") || lFlagPage.equals("DEPEN") || lFlagPage.equals("INCOST"))
      {
        return IWebConstants.ROOT_DIR +"/files/siap/siep/calcolopena/VediCalcoloPenaValidataAnnotazioni.jsp";
      }
      else if(lFlagPage.equals("ALTRO"))
      {
//        return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.calcolopena.action.ActLoadDettaglioRidetPena&" + ICostantiEvento.CAMPO_ID_EVENTO + "=" + lPenRes.getEveIdEvento();
        return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.calcolopena.action.ActLoadDettaglioRidetPenaNew&" + ICostantiEvento.CAMPO_ID_EVENTO + "=" + lPenRes.getEveIdEvento();
      }
      // ANNA sett.2010
      else if(lFlagPage.equals("SCOMPUTO"))
      {
          // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
          siesLogger.debug("PAGINA DA CHIAMARE:"+ICostantiEvento.CAMPO_ID_EVENTO+"="+lPenRes.getEveIdEvento());
          return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.calcolopena.action.ActLoadDettaglioRidetPenaScomputiSorv&"+ICostantiEvento.CAMPO_ID_EVENTO+"="+lPenRes.getEveIdEvento();
      
      }
      // AMBROSINO Pagamento PP 04/2011
      else if(lFlagPage.equals("ANNOTAPP"))
      {
    	  return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.calcolopena.action.ActLoadDettaglioAnnotaPagamentoPP&" + ICostantiEvento.CAMPO_ID_EVENTO + "=" + lPenRes.getEveIdEvento();
      }
     
    }

    String lPage = IWebConstants.ROOT_DIR +"/files/siap/siep/calcolopena/VediCalcoloPenaValidataAnnotazioni.jsp";

    return lPage;
  }
}