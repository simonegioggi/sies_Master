package siap.siep.rateizzazionepp.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe action per il caricamento dell'inserimento della Definizione Procedimento Pena Pecuniaria
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActLoadInserisciDefinizioneProcedimentoPP extends ActionSiap implements ICostantiRateizzazionePP {

  private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

  @SuppressWarnings("rawtypes")
  public String processRequest() throws Exception {

    // info per il log
    siesLogger.debug(getClass().getName() + ".processRequest: inizio");

    FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    // Controlli preliminari all'inserimento di un nuovo evento
    if (fsm.getFlagValidato().equalsIgnoreCase("N")) {
      RedirectTo rt = new RedirectTo();
      rt.setPage(IWebConstants.PG_MAIN);
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + fsm.getChiaveAnno() + "/"
          + fsm.getChiaveProgr()
          + " non è stato Validato. Impossibile Procedere all'Archiviazione!");
      rt.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerValidazione&"
          + ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);

      return IWebConstants.PG_MESSAGE;
    }

    isFascicoloSiepDiCompetenza();

    if (fsm.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
      RedirectTo rt = new RedirectTo();
      rt.setPage(IWebConstants.PG_MAIN);
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + fsm.getChiaveAnno() + "/"
          + fsm.getChiaveProgr() + " Il fascicolo risulta gia' Definito. Impossibile procedere!");
      rt.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
          + ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
      return IWebConstants.PG_MESSAGE;
    }

    // controllo se evento non sia validato
    isEventoNonValidato();

    // Posizione giuridica
    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel pgldacm = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
    IPosizioneGiuridica ipg = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    pgldacm = ipg.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
        fsm.getIdFascicoloSiep());
    if (pgldacm == null || pgldacm.getPosizioneGiuridica() == null) {
      RedirectTo rt = new RedirectTo();
      rt.setPage(IWebConstants.PG_MAIN);
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Al Procedimento N." + fsm.getChiaveAnno() + "/"
          + fsm.getChiaveProgr() + " non è stata associata una Posizione Giuridica.");
      rt.setAction("siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica&"
          + ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);

      return IWebConstants.PG_MESSAGE;
    }
    setRequestAttribute("posizioneluogoaltra", pgldacm);

    // Magistrato
    IMagistratoCompetente imc = SICOLookupRemote.getMagistratoCompetenteRemote();
    MagistratoCompetenteMagistratoModel mcmm = imc
        .ExRicercaMagistratoCompetenteByFascicoloDataFine(fsm.getIdFascicoloSiep());
    setRequestAttribute("magistrato", mcmm);

    Option lOptionAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita());
    setRequestAttribute("codiceAutorita", "" + lOptionAutorita);

    Option lOption = new Option(DecodificheManager.getInstance().getMotivoFineEspiazione());
    lOption.setFilter("1311");
    setRequestAttribute("oggettodefinzione", ""+lOption);  

    setRequestAttribute("modalita", "I");

    // info per il log
    siesLogger.debug(getClass().getName() + ".processRequest: fine");

    // pagina di ritorno
    return PG_LOAD_INSERISCI_DEFINIZIONE_PROCEDIMENTO_PP;
  }

}