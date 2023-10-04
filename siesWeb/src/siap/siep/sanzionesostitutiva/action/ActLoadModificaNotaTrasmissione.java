package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Funzione della Load Modifica nota di trasmissione per i bollettini successivi al primo.
 * 
 * - deve obbligare alla selezione dell'OI
 * - deve verificare che il tipo di rateizzazione sia R
 * - deve verificare che per le rate con origr > 1 siano stati generati i bollettini
 * 
 * 
 * @author d.fiorletta
 * @since MEV_2023-33
 */
public class ActLoadModificaNotaTrasmissione extends ActionSiap implements ICostantiSanzioneSostitutiva {

  private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

  @SuppressWarnings("unchecked")
  public String processRequest() throws Exception {

    siesLogger.debug(getClass().getName() + ".processRequest: inizio");
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    BigDecimal idNotaTrasmissione = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
    siesLogger.debug("ActLoadInserisciNotaTrasmissione: Sono in modifica della nota con ID "+idNotaTrasmissione);
  
    // Recupero la nota di trasmissione per precaricare in form Data emissione, data trasmissione e notifiche
    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
    EventoNotificaModel lNotaTrasmissione = lCtrlEvento.ExRicercaEventoNotificaByKey(idNotaTrasmissione);
    setRequestAttribute("eventoNotaTrasmissione", lNotaTrasmissione);
    
    // Recupero l'OI puntato dalla nota di trasmissione e le rate
    EventoNotificaModel lEveNotMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lNotaTrasmissione.getEvento().getEveIdEvento());
    setRequestAttribute("ordineIngiunzione", lEveNotMod);

    // Recupero le rate
    IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
    Vector<RateizzazionePPModel> listaRate = irpp.exRicercaRateizzazioniBollettiniByIdEvento(lEveNotMod.getEvento().getIdEvento());
    setRequestAttribute("listaRateizzazioni", listaRate);
      
    //FIXME aggiungere i controlli
    // Se mancanno lerate rilancio errore
    // Se il tipo di rateizzazione è unica rilancio errore
    // Se non sono stati generati i bollettini sulle rate successive alla prima rilancio errore
    /*throw new F3BException(F3BException.USER_MESSAGE,
    "Nessun Verbale Vane Ricerche Registrato per l'ordine di Ingiunzione selezionato");*/
      
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
    
    setRequestAttribute("modalita", "M");
    
    return PG_LOAD_INSERISCI_NOTA_TRASNISSIONE;
  }

}
