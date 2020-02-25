package siap.siep.sospensione.action;

/**
 * <p>Title: ActLoadInserisciSospensioneDifferimento</p>
 * <p>Description: Classe Action per il caricamento della finestra di 
 *    inserimento/modifica Sospensione Differimento</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
//import java.util.Collection;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.decretoordinanza.controller.IDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

public class ActLoadInserisciSospensioneDifferimento extends ActionSiap
                                         implements ICostantiSospensione
{
  public String processRequest() throws Exception
  {
//Controllo Presenza del Fascicolo in Sessione
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

    this.isFascicoloSiepDiCompetenza();

    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

//Controllo Validazione Fascicolo
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

//Controllo Fascicolo definito
    if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO"))
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

    // Verifico se esistono eventi non validati
    this.isEventoNonValidato();

/******************************* Posizione Giuridica **********************************/
    IPosizioneGiuridica lCtrlPosGiu = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    PosizioneGiuridicaModel lPosizione  = lCtrlPosGiu.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lIdFascicolo);

    if (lPosizione == null)
      throw new SIEPException(SIEPException.USER_MESSAGE, "Inserire prima la Posizione Giuridica. Impossibile eseguire la richiesta.");

    setRequestAttribute("posizione", lPosizione);

/******************************* Pena Complessiva *****************************/
    IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
    PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);

    if (lPenComMod == null)
      throw new SIEPException(SIEPException.USER_MESSAGE, "Pena Complessiva non presente. Impossibile eseguire la richiesta.");

    String lFlagErgastolo = "N";
    // se la Pena Complessiva è un ergastolo o ergastolo con isolamento diurno
    if(  lPenComMod.getCodTipoPenaDetentiva() != null && lPenComMod.getCodTipoPenaDetentiva() != "")
    {
      if(lPenComMod.getCodTipoPenaDetentiva().equals("03"))
      {
        lFlagErgastolo = "S";
      }
      else
      if(lPenComMod.getCodTipoPenaDetentiva().equals("04"))
      {
       lFlagErgastolo = "D";
      }
    }


    setRequestAttribute("flagergastolo", lFlagErgastolo);
/******************************* Fine Pena Complessiva ************************/

/***********************************  Pena Residua  ***************************/
    /* Verifico la presenza della Pena Residua. Deve esistere
     *
     */
    IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();

    PenaResiduaModel lPenaResidua = null;
    if(lPosizione.getCodPosizioneGiuridica() != null && !lPosizione.isLibero() ) 
    { // Detenuto
      lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo);
     /* if (lPenaResidua != null && lPenaResidua.getFlagPenaSospesa() != null)
      {
        if (lPenaResidua.getFlagPenaSospesa().equals("S"))
          throw new F3BException(F3BException.USER_MESSAGE, "Pena Residua risulta Sospesa");
        if (lPenaResidua.getFlagPenaSospesa().equals("I"))
          throw new F3BException(F3BException.USER_MESSAGE, "Pena Residua risulta Interrotta");
        if (lPenaResidua.getFlagPenaSospesa().equals("D"))
          throw new F3BException(F3BException.USER_MESSAGE, "Pena Residua risulta Definita");
      }*/

    }
    else
    { // Libero
      lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
    }

    // La Pena Residua deve essere presente e deve avere settate le date inizio/fine
    // (a meno del caso libero in cui le date non ci sono e puo' non essere validata)
    String lErrore = null;
    String lAzioneChiamante = null;

    if(lPenaResidua == null)
    {
      //lErrore = "Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?";
      lErrore = "Pena Residua da Espiare Inesistente o non validata. Eseguire Calcolo della pena?";
      lAzioneChiamante = "siap.siep.calcolopena.action.ActLoadCalcoloPena";
    }else if(  !lPosizione.isLibero()
         // non è libero
            && ( lPenaResidua.getDataInizio() == null // non ha le date
              || lPenaResidua.getDataFine() == null)
            && lFlagErgastolo.equals("N") )
    {
      lErrore = "Data decorrenza pena inestistente. Rivedere la Posizione Giuridica";
      lAzioneChiamante = "siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica";
    }

    if(lErrore != null)
    {
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, lErrore);
      lRedirigi.setAction(lAzioneChiamante+"&" +
                          ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

      return IWebConstants.PG_MESSAGE;
    }

    setRequestAttribute("penaresidua", lPenaResidua);


    /*==========================================================================
     * Ricerca di un eventuale decreto già a sistema ma non ancora elaborato
     * (FLAG_ELABORATO='N').
     * n.b. Questa funzione consente anche la modifica, inoltre non è possibile 
     *      inserire un differimento senza aver prima elaborato quallo precedente 
     * 
     *========================================================================*/
// **  Ricerca su DECRETO_ORDINANZA_SIEP **
    String [] aOggetto = {"0030","0031","0032","0033","0201","0202","0210","0211"};

    IDecretoOrdinanzaSiep lCtrlDecOrd = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();
    DecretoOrdinanzaSiepModel lDecOrd = lCtrlDecOrd.ExRicercaDecretoOrdinanzaSiepByOggettoProcedimento(aOggetto,lFascMod);
    if(lDecOrd != null)
    {
      setRequestAttribute("decretoordinanza", lDecOrd);
      String lFlagDec = (lDecOrd==null ? "N" : "S");
      setRequestAttribute("flagdecretoordinanza", lFlagDec);
    }
    
    //==========================================================================
    // Caricamento combo:
    // - Tipo Provvedimento  (bean: tipoprovvedimento)
    // - Autorità Emittente  (bean: autoritaemittente)
    // - Contenuto Decisione (bean: oggettodecisione)
    // - Oggetto Decisione   (bean: tipologiadecisione)
    //==========================================================================
    //TIPO REGISTRO ORDINANZA
    /** TODO la finestra non usa questo bean */
//    Collection lCollTipoReg = DecodificheManager.getInstance().getTipoRegistroOrdinanza();
//    setRequestAttribute("tiporegistroordinanza", lCollTipoReg );

    //TIPO PROVVEDIMENTO
    Option lOption = new Option( DecodificheManager.getInstance().getTipoProvvedimenti());
    lOption.setFilter( new String[] {"02", "03"} ); //solo DECRETO o ORDINANZA
    if(lDecOrd != null)
    {
      lOption.setSelected(lDecOrd.getCodTipoProvvedimento());
    }
    setRequestAttribute("tipoprovvedimento", "" + lOption );

    if(lDecOrd != null)
    { // se il decreto esiste precarico le combo con i dati del descreto
      //AUTORITA' EMITTENTE (TIPO_UFFICIO_SOSP)
      lOption = new Option(DecodificheManager.getInstance().getListaAutoritaSospTDSUDS(),lDecOrd.getCodTipoAutoritaEmittente());
      setRequestAttribute("autoritaemittente", "" + lOption);

      //CONTENUTO DECISIONE
      Option loggettoDecisione = new Option(DecodificheManager.getInstance().getOggettoSospensioneDiff(), lDecOrd.getCodOggettoDecisione());
      setRequestAttribute("oggettodecisione", "" + loggettoDecisione);

      //TIPOLOGIA DECISIONE
      Option lTipologiaDecisione = new Option(DecodificheManager.getInstance().getTipologiaDecisioneSospensioneDiff(), lDecOrd.getCodOggettoProcedimento());
      setRequestAttribute("tipologiadecisione", "" + lTipologiaDecisione);
    }
    else
    {
      // AUTORITA' EMITTENTE (TIPO_UFFICIO_SOSP)
      lOption = new Option(DecodificheManager.getInstance().getListaAutoritaSospTDSUDS());
      setRequestAttribute("autoritaemittente", "" + lOption);

      //CONTENUTO DECISIONE
      Option loggettoDecisione = new Option(DecodificheManager.getInstance().getOggettoSospensioneDiff());
      setRequestAttribute("oggettodecisione", "" + loggettoDecisione);

      //TIPOLOGIA DECISIONE
      Option lTipologiaDecisione = new Option(DecodificheManager.getInstance().getTipologiaDecisioneSospensioneDiff());
      setRequestAttribute("tipologiadecisione", "" + lTipologiaDecisione);
    }

    setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, "siap.siep.sospensione.action.ActLoadInserisciSospensioneDifferimento");

    return PG_LOAD_INSERISCI_SOSPENSIONE_DIFFERIMENTO;  //restituisce la jsp di VIEW
  }
}
