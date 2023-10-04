package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.pagoPA.model.BollettinoPagopaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.EventoRateizzazionePPModel;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Funzione della Load inserimento nota di trasmissione per i bollettini successivi al primo.
 * 
 * - deve obbligare alla selezione dell'OI
 * - deve verificare che il tipo di rateizzazione sia R
 * - deve verificare che per le rate con origr > 1 siano stati generati i bollettini
 * 
 * 
 * @author d.fiorletta
 * @since MEV_2023-33
 */
public class ActLoadInserisciNotaTrasmissione extends ActionSiap implements ICostantiSanzioneSostitutiva {

  private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

  @SuppressWarnings("unchecked")
  public String processRequest() throws Exception {

    siesLogger.debug(getClass().getName() + ".processRequest: inizio");
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    // Controlli preliminari all'inserimento di un nuovo evento
    if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,
          "Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
              + " non e' stato Validato. Impossibile inserire il Rinnovo Ricerche!");
      lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerValidazione&"
          + ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

      return IWebConstants.PG_MESSAGE;
    }

    isFascicoloSiepDiCompetenza();

    if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,
          "Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
              + " Il fascicolo risulta Definito. Impossibile procedere!");
      lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
          + ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
      return IWebConstants.PG_MESSAGE;
    }

    this.isEventoNonValidato();

    // Verifico se presenti più Ordini di Ingiunzione
    // se assenti - errore
    // se presente solo uno lo seleziono
    // se presente più di uno restituisco la pagina di scelta
    if (isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO)) {

      String esitoCheck = checkEventi();

      if (esitoCheck != null)
        return esitoCheck;
      // else se presente un solo OI carico direttamente la pagina?

    } else {
      // Ho selezionato l'evento dalla lista, carico la pagina
      BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

      IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
      EventoNotificaModel lEveNotMod = lCtrlEvento.ExRicercaEventoNotificaByKey(idEvento);
      setRequestAttribute("ordineIngiunzione", lEveNotMod);

      // Recupero le rate
      IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
      Vector<RateizzazionePPModel> listaRate = irpp.exRicercaRateizzazioniBollettiniByIdEvento(idEvento);
      setRequestAttribute("listaRateizzazioni", listaRate);
      
      //FIXME aggiungere i controlli
      // Se mancanno le rate rilancio errore
      
      // Se il tipo di rateizzazione è unica rilancio errore
      if ("U".equals(listaRate.elementAt(0).getTipoRateizzazione()))
         throw new F3BException(F3BException.USER_MESSAGE,
          "Impossibile emettere una nota di trasmissione, il provvedimento selezionato è relativo a un pagamento in un'unica rata.");
      
      // Se non sono stati generati i bollettini sulle rate successive alla prima rilancio errore
      int contaBollettini = 0;
      for (RateizzazionePPModel rata:listaRate) {
        Vector <BollettinoPagopaModel> listaBollettini = rata.getListaBollettini();
        for (BollettinoPagopaModel bollettino:listaBollettini) {
          if (bollettino.getProgRata()>1 ) {
            contaBollettini++;
            if  (bollettino.getIuv()==null || bollettino.getIuv().equals("")) 
              throw new F3BException(F3BException.USER_MESSAGE,
                "Impossibile emettere la nota di trasmissione, non sono stati generati i bollettini per le rate successive alla prima.");
          }
        }
      }
      
      if (contaBollettini==0) 
        throw new F3BException(F3BException.USER_MESSAGE,
            "Impossibile emettere la nota di trasmissione, non sono stati generati i bollettini per le rate successive alla prima.");
      
      // ?? va verificarìti che siano stati generati tutti i bollettini delle rimanenti rate?
      
      // Posizione giuridica
      PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
      IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
      lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
          lEveNotMod.getEvento().getFasSieIdFascicoloSiep());
      setRequestAttribute("posizioneluogoaltra", lPos);

      // Autorità esterna
      Option lOptionAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
      // Verifico se sovrescrivere l'auturità esterna
      if (lFascMod.getFlagAltraCausa() != null && lFascMod.getFlagAltraCausa().equals("S")) {
        // modifica relativa al tipo istituto
        if (lPos.getAltraCausa() != null && (lPos.getAltraCausa().getCodTipoPosGiuridica().equals("23")
            || lPos.getAltraCausa().getCodTipoPosGiuridica().equals("78")
            || lPos.getAltraCausa().getCodTipoPosGiuridica().equals("79")
            || lPos.getAltraCausa().getCodTipoPosGiuridica().equals("80")
            || lPos.getAltraCausa().getCodTipoPosGiuridica().equals("81"))) {
          lOptionAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
        } else {
          if (lPos.getAltraCausa() != null && lPos.getAltraCausa().getIstitutoDetenzione() != null)
            lOptionAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita(),
                lPos.getAltraCausa().getIstitutoDetenzione().getCodTipoIstituto());
        }
      } else {
        if (lPos.getPosizioneGiuridica().isLibero()
            || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("02")
            || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("04")) {
          lOptionAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
        } else {
          if (lPos.getLuogoDetenzione() != null
              && lPos.getLuogoDetenzione().getIstitutoDetenzione() != null)
            lOptionAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita(),
                lPos.getLuogoDetenzione().getIstitutoDetenzione().getCodTipoIstituto());
        }
      }
      lOptionAutoritaEsternaE.setSelected("-");
      setRequestAttribute("autoritaEsternaE", "" + lOptionAutoritaEsternaE);


      // Magistrato
      IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
      MagistratoCompetenteMagistratoModel lMagi = lMagCtrl
          .ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());
      setRequestAttribute("magistrato", lMagi);
      
    }

    setRequestAttribute("modalita", "I");
    
    return PG_LOAD_INSERISCI_NOTA_TRASNISSIONE;
  }

  /**
   * 
   * @return
   * @throws Exception
   */
  private String checkEventi() throws Exception {

    String returnPage = null;

    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    // Recupero la lista degli eventi e i dati da visualizzare
    IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
    
    // 2023.09.26 - si includono nella ricerca anche i codici
    //Vector<EventoRateizzazionePPModel> listaOrdiniIngiunzione = irpp
    //    .exRicercaEventoRateizzazionePP(lFascMod.getIdFascicoloSiep(), "");
    Vector<EventoRateizzazionePPModel> listaOrdiniIngiunzione = irpp
        .exRicercaEventoRateizzazionePP(lFascMod.getIdFascicoloSiep(), "ALL");
    // 2023.09.26 - FINE
    
    
    if (listaOrdiniIngiunzione.isEmpty()) {
      // non ho trovato ordini di ingiunzione esco con errore
      RedirectTo rt = new RedirectTo();
      rt.setPage(IWebConstants.PG_MAIN);
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,
          "Attenzione! Non sono presenti ordini di ingiunzione per questo fascicolo.");
      rt.setAction("siap.siep.sanzionesostitutiva.action.ActGrigliaOrdineIngiunzione");
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
      return IWebConstants.PG_MESSAGE;
    } else if (listaOrdiniIngiunzione.size() > 0) { // n.b per test deve essere >1
      // Carico la pagina con la scelta degli OI
      setRequestAttribute("listaOrdiniIngiunzione", listaOrdiniIngiunzione);
      setRequestAttribute("azioneChiamante", this.getClass().getName());

      return PG_LOAD_SELEZIONA_ORDINE_INGIUNZIONE;
    }

    return returnPage;
  }

}
