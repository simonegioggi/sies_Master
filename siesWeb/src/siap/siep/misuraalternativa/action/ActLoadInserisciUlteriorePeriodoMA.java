package siap.siep.misuraalternativa.action;

import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;
//import per le combo
//import f3b.web.html.Option;
//import siap.sico.decodifiche.controller.DecodificheManager;

/**
 * <p>Title: ActLoadInserisciUlteriorePeriodoMA</p>
 * <p>Description: Classe Action per la load inserisci di MisuraAlternativa</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadInserisciUlteriorePeriodoMA
    extends ActMisuraAlternativa
    implements ICostantiMisuraAlternativa
{
  public String processRequest() throws F3BException
  {
    //avviso di pagina in costruzione
     return ISIAPCostantiWeb.PG_UNDER_CONSTRUCTION;

   /* scommentare appena si toglie l'avviso

    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

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


        this.isFascicoloSiepDiCompetenza();

    if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO"))
    {
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,
                          "Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr() + " Il fascicolo risulta Definito. Impossibile procedere!");
      lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
                          ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
      return IWebConstants.PG_MESSAGE;
    }

    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
    IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod.getIdFascicoloSiep());
    String lPosGiu = null;
    if(lPos != null && lPos.getPosizioneGiuridica()!= null ){
       lPosGiu = lPos.getPosizioneGiuridica().getCodPosizioneGiuridica();
       setRequestAttribute("posizioneluogoaltra", lPos);
       if ((lPos.getPosizioneGiuridica() != null) && (!lPosGiu.equals("11") && !lPosGiu.equals("12") && !lPosGiu.equals("13") && !lPosGiu.equals("14")))
           throw new SIEPException(SIEPException.USER_MESSAGE, "Non è possibile emettere un'ulteriore misura alternativa per posizione giuridica non corretta.");

    }
    if (lPos == null || lPos.getPosizioneGiuridica() == null)
      throw new SIEPException(SIEPException.USER_MESSAGE,
                              "Al Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr() + " non à stata associata una Posizione Giuridica.");



//Controllo Esistenza pena residua non validata per quel fascicolo
    PenaResiduaModel lPenaResMod = new PenaResiduaModel();
    IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
    lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

    if (lPenaResMod == null || lPenaResMod.getIdPenaResidua() == null)
      throw new SIEPException(SIEPException.USER_MESSAGE, "Eseguire prima il calcolo della pena. Impossibile eseguire la misura alternativa.");

    if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N") && lPenaResMod.getDataFine() == null)
    {
      setRequestAttribute("dataeditabile", "S");

    }
    else
    {

    }

// ricerca misura per il fascicolo
//   MisuraAlternativaModel lMisAlModCorr = new MisuraAlternativaModel();
    IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();

// ricerca evento inserito dal TDS insieme alla misura alternativa
    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
//Ricerca Ultima Ordinanza di tipo Concessione legata al Fascicolo
    MisuraAlternativaModel lMisAlModConcessa = null;
//ricerca evento notifica ordinanza
    Hashtable lTable = new Hashtable();
    EventoNotificaModel lEveMod = new EventoNotificaModel();

    if (this.isRequestParameterNullObj("warning"))
    {
      lMisAlModConcessa = lMisAltCtrl.ExRicercaMisuraAlternativaCorrenteByIdFascicolo(lFascMod.getIdFascicoloSiep());

      if (lMisAlModConcessa != null && lMisAlModConcessa.getIdMisuraAlternativa() != null)
      {

        UfficioModel lUffEmiMod = getUfficioByCodUfficio(lMisAlModConcessa.getChiaveUfficioFascicoloSius());

        setRequestAttribute("sedeUfficioEmittente", lUffEmiMod);

// Controllo Coerenza Richiesta con Ordinanza TDS
       if (
              lMisAlModConcessa.getCodTipoDecisione() == null ||
              !lMisAlModConcessa.getCodTipoDecisione().equals("03") ||
              lMisAlModConcessa.getCodNaturaDecisione() == null ||
              !lMisAlModConcessa.getCodNaturaDecisione().equals("UP") ||
              lMisAlModConcessa.getCodTipoMisura() == null ||
              !lMisAlModConcessa.getCodTipoMisura().equals("0101")
              )
         {
          //set goto page set flag misura
          setRequestAttribute( IWebConstants.ACTION_FIELD, "" + getClass().getName());
          setRequestAttribute( IWebConstants.MESSAGE_TEXT, "Ordinanza assente o dati incoerenti, si vuole procedere all'inserimento dell' Ordinanza e alla contestuale emissione del Provvedimento?");

          return "/jsp/files/warning.jsp";
        }
        else
        {
          setRequestAttribute("misuraalternativa", lMisAlModConcessa);
                   lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lMisAlModConcessa.getEveIdEvento());

                   EventoModel lEveModelNonOrd = lCtrlEvento.ExRicercaEventoMANonRegistratoByFascicoloSiep(lFascMod.getIdFascicoloSiep(), lMisAlModConcessa.getEveIdEvento());

                 if (lEveModelNonOrd != null)
                 {
//ricerca evento notifica non riferito all'ordinanza
                   EventoNotificaModel lEveModSucc = lCtrlEvento.ExRicercaEventoNotificaByKey(lEveModelNonOrd.getIdEvento());
                   this.setRequestAttribute("eventonotifica", lEveModSucc);
                   lTable = this.ricercaNotifiche(lEveModSucc.getNotifiche());

                 }
                 else
                 {
                   lTable = this.ricercaNotifiche(lEveMod.getNotifiche());

                 }


        }

    }
    else
    {
      setRequestAttribute("flagmisura", "N");
    }
  }
  else
  {
    setRequestAttribute("flagmisura", "N");
  }




// ricerca misura alternativa concessa precedente

    MisuraAlternativaModel lMisAlModConcessaPrec = null;
    lMisAlModConcessaPrec = lMisAltCtrl.ExRicercaUltimaMisuraAlternativaConcessaByFascicoloOrdinanza(lFascMod.getIdFascicoloSiep());
    if(lMisAlModConcessaPrec != null)
    {
      setRequestAttribute("MisuraConcessaPrec", lMisAlModConcessaPrec);

    }

    // ricerca cssa associato alla misura altenativa
    String IndirizzoCssa = null;
    String ComuneCssa = null;


    if (lMisAlModConcessa != null && lMisAlModConcessa.getIdMisuraAlternativa() != null)
    {
    lMisAlModConcessa.getCssIdCssa();
    ICSSA lCssaCtrl = SICOLookupRemote.getCSSARemote();
     CSSAModel lCssaMod=lCssaCtrl.getCSSAByKey(lMisAlModConcessa.getCssIdCssa());

     IndirizzoCssa= lCssaMod.getIndirizzo();
     ComuneCssa = lCssaMod.getComune();
     setRequestAttribute("IndirizzoCssa", IndirizzoCssa);
     setRequestAttribute("ComuneCssa", ComuneCssa);
     setRequestAttribute("CssaModel", lCssaMod);


    }
//RICERCA NOTIFICHE PER LA VISUALIZZAZIONE

//ricerca magistrato competente

    IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
    MagistratoCompetenteMagistratoModel lMagMod = lMagComp.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
    if (lMagMod != null)
      setRequestAttribute("magistratocompetente", lMagMod);

//Avvocato
    IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
    Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
    setRequestAttribute("avvocati", lAvvocati);

//Pena Residua

    IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote();
    PenaResiduaModel llPenMod = lCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

    setRequestAttribute("penaresidua", llPenMod);

//Autorità esterna E

    AutoritaEsternaModel lAutE = null;

    if (lTable.get("AutE") != null)
    {
      lAutE = ( (NotificaModel) lTable.get("AutE")).getAutoritaEsterna();
      setRequestAttribute("autoritaEsternaE", lAutE);

    }

    Option lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
    setRequestAttribute("codiceAutoritaE", "" + lOptionAutoritaE);


//Autorità esterna N

    AutoritaEsternaModel lAutN = null;
    if (lTable.get("AutN") != null)
    {
      lAutN = ( (NotificaModel) lTable.get("AutN")).getAutoritaEsterna();
      setRequestAttribute("autoritaEsternaN", lAutN);

    }

    Option lOptionAutoritaN = new Option(DecodificheManager.getInstance().getTipoAutorita());
    setRequestAttribute("codiceAutoritaN", "" + lOptionAutoritaN);

//Autorità esterna C

    AutoritaEsternaModel lAutC = null;
    if (lTable.get("AutC") != null)
    {
      lAutC = ( (NotificaModel) lTable.get("AutC")).getAutoritaEsterna();
      setRequestAttribute("autoritaEsternaC", lAutC);

    }

    Option lOptionAutoritaC = new Option(DecodificheManager.getInstance().getTipoAutorita());
    setRequestAttribute("codiceAutoritaC", "" + lOptionAutoritaC);

//Cssa

    String lCssa = null;
    if (lTable.get("NotCssa") != null)
    {
      lCssa = ( (NotificaModel) lTable.get("NotCssa")).getCSSA().getComune() + " " + ( (NotificaModel) lTable.get("NotCssa")).getCSSA().getIndirizzo();
      setRequestAttribute("Cssa", lCssa);
      setRequestAttribute("daticssa", ( (NotificaModel) lTable.get("NotCssa")).getCSSA());

    }

//Ufficio TDS

    String UffTDS = null;
    if (lTable.get("UffTDS") != null)
    {
      UffTDS = ( (NotificaModel) lTable.get("UffTDS")).getUfficio().getDescrComune();
      setRequestAttribute("UffTDS", UffTDS);

    }

//Ufficio UDS

    String UffUDS = null;
    if (lTable.get("UffUDS") != null)
    {
      UffUDS = ( (NotificaModel) lTable.get("UffUDS")).getUfficio().getDescrComune();
      setRequestAttribute("UffUDS", UffUDS);

    }

//setto il campo codice motivo
// Option lOption = new Option(DecodificheManager.getInstance().getMotivoProvvedimentoMAffP());
// setRequestAttribute("motivoProvv", "" + lOption);
    Collection lmotivo = DecodificheManager.getInstance().getMotivoProvvedimentoUlteriorePeriodoMA();
    String lCodMotivo = "";
    if (lmotivo != null && !lmotivo.isEmpty())
    {
      Iterator lIter = lmotivo.iterator();
      if (lIter.hasNext())
      {
        DecodificheModel lDecMod = (DecodificheModel) lIter.next();
        lCodMotivo = lDecMod.getDescription();
        setRequestAttribute("motivoProvv", lDecMod);

      }
    }

    Option lOption = new Option(DecodificheManager.getInstance().getTipoIstituto());
    setRequestAttribute("tipoIstituto", "" + lOption);

    lOption = new Option(DecodificheManager.getInstance().getTipoUfficio(), "TDS");
    setRequestAttribute("tipoUfficio", "" + lOption);

    lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
    setRequestAttribute("autoritaEsternaAvv", "" + lOption);

    return PG_LOAD_INSERISCI_ULTERIORE_MA;  fine*/
  }

}