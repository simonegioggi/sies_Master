package siap.siep.scadenzario.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.web.ActionSiap;
import siap.siep.calcolopena.controller.CalcoloPenaController;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.scadenzario.controller.IScadenzario;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.util.SIEPLookupRemote;

public class ActRicercaScadenzarioStatoPagamentiPP extends ActionSiap implements ICostantiScadenzario {

  @SuppressWarnings("rawtypes")
  public String processRequest() throws Exception {

    ScadenzarioModel lScaMod = new ScadenzarioModel();
    String titolo = new String();
    titolo = "Tutti";

    String lPagina = "1";
    if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
      lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

    // Imposto fisso 30=PAGAMENTO PENA PECUNIARIA e N=Tutti i non visti
    lScaMod.setCodTipoScadenzario("30");
    lScaMod.setFlagVisto("N");
    lScaMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

    
    if (!isRequestParameterNullEmptyObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO)){
      lScaMod.setChiaveAnnoIniziale(getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO));
      lScaMod.setChiaveProgrIniziale(getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR));
      
      lScaMod.setChiaveAnnoFinale(getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO));
      lScaMod.setChiaveProgrFinale(getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR));
    }    
    
    if (getRequestStringParameter("tipo").equals("oggi")) {
      lScaMod.setDataFineScadenza(DateUtils.getSysDateAsDate("dd/MM/yyyy"));
      titolo = "In Scadenza Oggi";
      lScaMod.setTipoRic("oggi");
    }

    if (getRequestStringParameter("tipo").equals("sette")) {
      BigDecimal lAnni = getRequestBigDecimalParameter(CAMPO_ANNI_SCADENZA);
      BigDecimal lMesi = getRequestBigDecimalParameter(CAMPO_MESI_SCADENZA);
      BigDecimal lGiorni = getRequestBigDecimalParameter(CAMPO_GIORNI_SCADENZA);

      CalendarModel lCalMod = new CalendarModel();
      CalendarModel lDataInizio = new CalendarModel();

      lDataInizio.setNumAnni(new BigDecimal(DateUtils.getYearToString(DateUtils.getSysDate())));
      lDataInizio.setNumMesi(new BigDecimal(DateUtils.getMonthToString(DateUtils.getSysDate())));
      lDataInizio.setNumGiorni(new BigDecimal(DateUtils.getDayToString(DateUtils.getSysDate())));

      if (lAnni != null && lAnni.intValue() != 0) {
        lScaMod.setNumAnni(lAnni);
        lCalMod.setNumAnni(lScaMod.getNumAnni());
      }
      if (lGiorni != null && lGiorni.intValue() != 0) {
        lScaMod.setNumGiorni(lGiorni);
        lCalMod.setNumGiorni(lScaMod.getNumGiorni());
      }
      if (lMesi != null && lMesi.intValue() != 0) {
        lScaMod.setNumMesi(lMesi);
        lCalMod.setNumMesi(lScaMod.getNumMesi());
      }

      lScaMod.setTipoRic("sette");
      CalcoloPenaController lCalPen = new CalcoloPenaController();
      lScaMod.setDataFineScadenza(lCalPen.exCalcolaNuovaDataFine(lDataInizio, lCalMod, false));
      lScaMod.setDataInizioScadenza(DateUtils.getSysDateAsDate("dd/MM/yyyy"));
      titolo = "In Scadenza";
    }

    if (getRequestStringParameter("tipo").equals("scaduto")) {
      lScaMod.setDataInizioScadenza(DateUtils.getSysDateAsDate("dd/MM/yyyy"));
      titolo = "Scaduti";
      lScaMod.setTipoRic("scaduto");
    }

    IScadenzario lCtrl = SIEPLookupRemote.getScadenzarioRemote();
    BigDecimal CountRisultati;
    if (isRequestParameterNullObj("CountRisultati")) {
      CountRisultati = lCtrl.ExGetCountScadenzariPP(lScaMod);
    } else
      CountRisultati = getRequestBigDecimalParameter("CountRisultati");

    Vector lVect = lCtrl.ExRicercaScadenzarioPagedPP(lScaMod, Integer.parseInt(lPagina));
    setRequestAttribute("scadenzario", lVect);

    setRequestAttribute("CountRisultati", CountRisultati);
    setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
    setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());    
    setRequestAttribute("tipo", getRequestStringParameter("tipo"));
    setRequestAttribute("titolo", titolo);

    return PG_RICERCA_SCADENZARI_PP;
  }
}
  
