package siap.siep.richiesta.action;

/**
* <p>Title: ActRichiestaGE</p>
* <p>Description: Classe Action per l'inserimento di Circostanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.evento.model.EventoModel;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class ActRichiestaGE extends ActionSiap
                            implements ICostantiRichiesta
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
   * Recupera dalla form i dati dell'annotazione manuale e carica il model 
   * @return
   * @throws Exception
   */
  protected AnnotazioneManualeModel setAnnotazioneManuale() throws Exception
  {
    BigDecimal IdReato = null;

    AnnotazioneManualeModel lAnnMod = new AnnotazioneManualeModel();

    lAnnMod.setFlagValidato("N");

//    lAnnMod.setCodDpr(getRequestStringParameter("dpr"));
    lAnnMod.setDataRichiesta(getRequestDateParameter( CAMPO_ANNO_DATA_RICHIESTA, CAMPO_MESE_DATA_RICHIESTA, CAMPO_GIORNO_DATA_RICHIESTA) );

    lAnnMod.setFlagPiuMeno(getRequestStringParameter(CAMPO_FLAG_PIU_MENO));

//    lAnnMod.setCodTipoAnnotazione(getRequestStringParameter(CAMPO_COD_TIPO_ANNOTAZIONE));

    lAnnMod.setFlagAppProvvisoria("R"); //RICHIESTA SENZA ANTICIPAZIONE
    if (isRequestChecked(CAMPO_FLAG_APP_PROVVISORIA))
    {
      lAnnMod.setFlagAppProvvisoria("A"); //RICHIESTA CON ANTICIPAZIONE
    }

    lAnnMod.setFlagConforme("-");
    lAnnMod.setCodFonte("-");
    lAnnMod.setCodSottonumerazione("-");
    lAnnMod.setCodCausaleComputo("-");

    String GRec=getRequestStringParameter("GRec");
    String MRec=getRequestStringParameter("MRec");
    String ARec=getRequestStringParameter("ARec");
    String Multa=getRequestStringParameter("Multa");
    String Multa_dec=getRequestStringParameter("Mul_dec");

    String noteRec=getRequestStringParameter("noteRec");

    if (!ARec.equals(""))
      lAnnMod.setNumAnniReclusione(new BigDecimal(ARec));
    if (!MRec.equals(""))
      lAnnMod.setNumMesiReclusione(new BigDecimal(MRec));
    if (!GRec.equals(""))
      lAnnMod.setNumGiorniReclusione(new BigDecimal(GRec));

    lAnnMod.setFasSieIdFascicoloSiep(((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep());
    if (!isRequestParameterNullObj("IdReato"))
    {
      IdReato = getRequestBigDecimalParameter("IdReato");
      lAnnMod.setReaIdReato(IdReato);
    }
    
    if (!Multa.equals(""))
    {
      if (!Multa_dec.equals(""))
      {
        lAnnMod.setImportoMulta(new BigDecimal(Multa+"."+Multa_dec));
      }
      else
        lAnnMod.setImportoMulta(new BigDecimal(Multa));
    }
    else if (!Multa_dec.equals(""))
      lAnnMod.setImportoMulta(new BigDecimal("0."+Multa_dec));

    lAnnMod.setNoteReclusione(noteRec);

    String GArr = getRequestStringParameter("GArr");
    String MArr = getRequestStringParameter("MArr");
    String AArr = getRequestStringParameter("AArr");
    String Ammenda     = getRequestStringParameter("Ammenda");
    String Ammenda_dec = getRequestStringParameter("Amm_dec");

    if (!AArr.equals(""))
      lAnnMod.setNumAnniArresto(new BigDecimal(AArr));
    if (!MArr.equals(""))
      lAnnMod.setNumMesiArresto(new BigDecimal(MArr));
    if (!GArr.equals(""))
      lAnnMod.setNumGiorniArresto(new BigDecimal(GArr));

    lAnnMod.setFasSieIdFascicoloSiep(((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep());
    if (!isRequestParameterNullObj("IdReato"))
    {
      IdReato = getRequestBigDecimalParameter("IdReato");
      lAnnMod.setReaIdReato(IdReato);
    }
    if (!Ammenda.equals(""))
    {
      if (!Ammenda_dec.equals(""))
      {
        lAnnMod.setImportoAmmenda(new BigDecimal(Ammenda+"."+Ammenda_dec));
      }
      else
        lAnnMod.setImportoAmmenda(new BigDecimal(Ammenda));
    }
    else if  (!Ammenda_dec.equals(""))
      lAnnMod.setImportoAmmenda(new BigDecimal("0."+Ammenda_dec));

    
    //lAnnMod.setMotivazioni(noteArr);
    lAnnMod.setCodOperatoreInserimento (getCodUtenteConnesso());
    lAnnMod.setCodUfficioInserimento   (getCodUfficioUtenteConnesso());
    lAnnMod.setDataInserimento         (DateUtils.getSysDate());

    return lAnnMod;
  }

  
  /**
   * 
   * @return
   * @throws Exception
   */
  private EventoModel setEvento() throws Exception
  {
    EventoModel lEveMod = new EventoModel();

    lEveMod.setCodTipoEvento("01"); //RICHIESTA AL GE
    //lEveMod.setCodTipoProvvedimento("04"); //PROVVEDIMENTO
    lEveMod.setFlagDocumentoRegistrato(null);
    lEveMod.setFlagStampaSiep("S");
    lEveMod.setFlagVideoSiep("S");
    lEveMod.setCodUfficioEmittente(getUfficioUtenteConnesso().getCodUfficio());
    lEveMod.setCodLuogoEmittente(getUfficioUtenteConnesso().getCodComune());
    lEveMod.setFasSieIdFascicoloSiep(((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep());
    //lEveMod.setDataEmissione(  );

    return lEveMod;
  }

  // STUB 28/09/2005 REWORK STATO ESECUZIONE
  protected EventoModel setEvento( String aCodTipoProvvedimento, String aCodMotivo) throws Exception
  {
    EventoModel lEveMod = setEvento();

    lEveMod.setCodTipoProvvedimento(aCodTipoProvvedimento);
    lEveMod.setCodMotivo(aCodMotivo);

    return lEveMod;
  }

// Luigi 27-09-2005
  protected EventoModel setEvento(String aCodMotivo) throws Exception
  {
    EventoModel lEveMod = setEvento();

    lEveMod.setCodTipoProvvedimento("26");
    lEveMod.setCodMotivo(aCodMotivo);

    lEveMod.setFlagDocumentoRegistrato("S"); //*****************************

    return lEveMod;
  }

  /**
   * Effettua l'inserimento della richiesta e delle annotazioni 
   *  
   * @param aCampoAzione - Action da invocare in caso di ritorno (cioè mai)
   * @param lAnnMod
   * @param lEveMod
   * @return
   * @throws Exception
   */
  protected String eseguiRichiesta(String aCampoAzione,
                                   AnnotazioneManualeModel lAnnMod,
                                   EventoModel lEveMod) throws Exception
  {
    String lPage = null;

    IAnnotazioneManuale lAnnManCtrl=SIEPLookupRemote.getAnnotazioneManualeRemote();
    
    //==========================================================================
    // Imposto il lFlagRich da passare alla jsp di visualizzazione del dettaglio
    // per sapere che tipo di richiesta sta trattando
    //==========================================================================
    String lFlagRich= null;

    if(lEveMod.getCodMotivo().equals("0210"))
    {
      lFlagRich = "RICH_DEPEN";
    }
    else if(lEveMod.getCodMotivo().equals("0211"))
    {
      lFlagRich = "RICH_INCOST";
    }
    else
    {
      lFlagRich = "RICH_AMNI";
    }
    this.setRequestAttribute("lFlagRich", lFlagRich);

    
    //==========================================================================
    // Il flag operazione vale sempre Quantum non vengono più gestiti i casi
    // Torna e Stampa presenti inizialmente (luglio 2004) sulla pagina di inserimento
    // delle richieste
    //==========================================================================
    if (getRequestStringParameter("operazione").equals("Torna"))
    {
      lAnnManCtrl.ExInserisciAnnotazioneManuale(lAnnMod);

      lPage = IWebConstants.ROOT_DIR+"Main.jsp?Action="+aCampoAzione;
    }
    else if (getRequestStringParameter("operazione").equals("Stampa"))
    {
      lAnnManCtrl.ExInserisciAnnotazioneManualeEvento(lAnnMod, lEveMod);

      lPage = IWebConstants.ROOT_DIR+"/files/siap/siep/calcolopena/VediCalcoloPenaValidataRichiestaGE.jsp";
      //return lPage;
    }
    else if (getRequestStringParameter("operazione").equals("Quantum"))
    {
      AnnotazioneManualeModel lAnnManIns = lAnnManCtrl.ExInserisciAnnotazioneManualeEvento(lAnnMod, lEveMod);
      
      setRequestAttribute("lFlagPage", "GE");

      if(lFlagRich.equals("RICH_AMNI")) {
        String dataScarcerazioneStr = "";
        if ( !isRequestParameterNullObj(ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE )) {
          Date dataScarcerazione = getRequestDateParameter( ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_SCARCERAZIONE, 
                                                            ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE, 
                                                            ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE);
          dataScarcerazioneStr = DateUtils.getDateToString(dataScarcerazione,"dd/MM/yyyy");
          dataScarcerazioneStr = "&dataScarcerazione="+dataScarcerazioneStr;
        }
        lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.richiesta.action.ActLoadDettaglioAnnotazioniAnticipazioniAmnistia&" + ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE +"=" + lAnnManIns.getIdAnnotazioneManuale()+"&lFlagPage=GE&lFlagRich="+lFlagRich+dataScarcerazioneStr;
      }
      else if(lFlagRich.equals("RICH_DEPEN"))
        lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.richiesta.action.ActLoadDettaglioAnnotazioniAnticipazioniDepen&" + ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE +"=" + lAnnManIns.getIdAnnotazioneManuale()+"&lFlagPage=GE&lFlagRich="+lFlagRich;
      else
        lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.richiesta.action.ActLoadDettaglioAnnotazioniAnticipazioniIncost&" + ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE +"=" + lAnnManIns.getIdAnnotazioneManuale()+"&lFlagPage=GE&lFlagRich="+lFlagRich;

    }
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".eseguiRichiesta() Pagina di ritorno ->" + (lPage != null ? lPage.toString() : "null"));

    return lPage;
  }
}