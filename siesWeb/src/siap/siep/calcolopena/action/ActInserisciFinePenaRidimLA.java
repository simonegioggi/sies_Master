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
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
//import siap.siep.scadenzario.controller.IScadenzario;
//import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciFinePenaRidimLA extends ActionSiap
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
/**
* Azione di Inserimento del nuovo Fine Pena Residua validata. Questa action recupera
* i dati della pena residua direttamente dalla jsp e imposta il flag validata a N.
* Inoltre se è presente anche una fungibilità inserita
* contestualmente al calcolo della pena residua, valida anche questo record.
*
* Copiata dalla ActInserisciNuovaPenaValidata
*

* @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
*         tale jsp è la pagina di visualizzazione della scelta della stampa o validazione
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
    if( !isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO) )
      lPenRes.setEveIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("id_evento  letto da request = "+lPenRes.getEveIdEvento());

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
    // Inserimento aggiornamento pena residua con l dati recuperati dalla
    // form (posso aver corretto ladata fine pena!!!!!!)
    //==========================================================================
    IPenaResidua lPR = SIEPLookupRemote.getPenaResiduaRemote();
    lPenRes = lPR.ExInsertOrUpdatePenaResidua(lPenRes);

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("DOPO ExInsertOrUpdatePenaResidua!!!!!!!!!");

    //Cerca l'ultima pena residua validata...
    ///////Verificare se serve ultima validata o la corrente!!!!!
      
    //////////!!!!ANNA   lPenRes = lPR.ExRicercaPenaResiduaUltimaValidata(lFascMod.getIdFascicoloSiep());
    //...se non la trova cerca l'ultima in assoluto
    //////////!!!!ANNA   if(lPenRes == null)
    	lPenRes = lPR.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());
    
    setRequestAttribute("PenaResidua", lPenRes);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("id_evento  = "+lPenRes.getEveIdEvento());

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
    IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
    PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);

    setRequestAttribute("PenaComplessiva", lPenComMod);


    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("id_evento  = "+lPenRes.getEveIdEvento());

//    RedirectTo lRedirigi = new RedirectTo();
//    lRedirigi.setPage( IWebConstants.PG_MAIN );
//    lRedirigi.setAction( "siap.siep.calcolopena.action.ActLoadGrigliaRidetPenaRidimLA");
//    lRedirigi.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, lPenRes.getEveIdEvento().toString());
//    return lRedirigi.toString();

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: fine" );

    BigDecimal lIdEvento= lPenRes.getEveIdEvento();
    String lPage = "";
    //lPage =  IWebConstants.ROOT_DIR +"/files/siap/siep/calcolopena/DettaglioRidetPenaRidimLA.jsp";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.calcolopena.action.ActDettaglioRidimensionamentoLA&"+ICostantiEvento.CAMPO_ID_EVENTO+"="+lIdEvento.toString();
    
    return lPage;

  }
}