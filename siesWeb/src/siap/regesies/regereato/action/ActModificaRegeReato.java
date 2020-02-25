package siap.regesies.regereato.action;

import siap.regesies.action.ActionRegeSiap;
import siap.regesies.regereato.controller.IRegeReato;
import siap.regesies.regereato.model.RegeReatoModel;
import siap.regesies.util.RegeSiesLookupRemote;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActModificaRegeReato</p>
 * <p>Description: Classe Action per la modifica di RegeReato</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */
public class ActModificaRegeReato extends ActionRegeSiap implements ICostantiRegeReato
{
  /**
   * Azione di Modifica del RegeReato
   * @return Nome della pagina JSP da visualizzare
   * al termine dell'elaborazione
   * @throws F3BException
   */
  public String processRequest() throws F3BException
  {

    String lId = getRequestStringParameter(CAMPO_ID_FILE);
    // riempie il model
    RegeReatoModel lRegMod = new RegeReatoModel();

    lRegMod.setIdFile(lId);

    lRegMod.setProgrReato(getRequestIntParameter(CAMPO_PROGR_REATO));
    lRegMod.setProgrCircostanza(getRequestIntParameter(CAMPO_PROGR_CIRCOSTANZA));
    lRegMod.setCodFonte(getRequestStringParameter(CAMPO_COD_FONTE));
    lRegMod.setNumeroFonte(getRequestStringParameter(CAMPO_NUMERO_FONTE));
    lRegMod.setCodSottonumerazione(getRequestStringParameter(CAMPO_COD_SOTTONUMERAZIONE));
    lRegMod.setComma(getRequestStringParameter(CAMPO_COMMA));
    lRegMod.setLettera(getRequestStringParameter(CAMPO_LETTERA));
    lRegMod.setNumero(getRequestStringParameter(CAMPO_NUMERO));
    lRegMod.setArticolo(getRequestStringParameter(CAMPO_ARTICOLO));

    if (!isRequestParameterNullObj(CAMPO_COD_TIPO_REATO))
      lRegMod.setCodTipoReato(getRequestStringParameter(CAMPO_COD_TIPO_REATO));
      //lRegMod.setDataReato(getRequestDateParameter(CAMPO_ANNO_DATA_REATO, CAMPO_MESE_DATA_REATO, CAMPO_GIORNO_DATA_REATO));

    if (!isRequestParameterNullObj(CAMPO_PROGR_NUMERO_MANUALE))
      lRegMod.setProgrNumeroManuale(getRequestStringParameter(CAMPO_PROGR_NUMERO_MANUALE));

    if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_INIZIO)
      && !isRequestParameterNullObj(CAMPO_MESE_DATA_INIZIO)
      && !isRequestParameterNullObj(CAMPO_GIORNO_DATA_INIZIO))
      lRegMod.setDataInizio(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO, CAMPO_MESE_DATA_INIZIO, CAMPO_GIORNO_DATA_INIZIO));

    if (isIntParameter(CAMPO_ANNO_DATA_INIZIO))
      lRegMod.setAnnoInizio(getRequestIntParameter(CAMPO_ANNO_DATA_INIZIO));
    if (isIntParameter(CAMPO_MESE_DATA_INIZIO))
      lRegMod.setMeseInizio(getRequestIntParameter(CAMPO_MESE_DATA_INIZIO));
    if (isIntParameter(CAMPO_GIORNO_DATA_INIZIO))
      lRegMod.setGiornoInizio(getRequestIntParameter(CAMPO_GIORNO_DATA_INIZIO));

    if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_FINE)
      && !isRequestParameterNullObj(CAMPO_MESE_DATA_FINE)
      && !isRequestParameterNullObj(CAMPO_GIORNO_DATA_FINE))
      lRegMod.setDataFine(getRequestDateParameter(CAMPO_ANNO_DATA_FINE, CAMPO_MESE_DATA_FINE, CAMPO_GIORNO_DATA_FINE));

    if (isIntParameter(CAMPO_ANNO_DATA_FINE))
      lRegMod.setAnnoFine(getRequestIntParameter(CAMPO_ANNO_DATA_FINE));
    if (isIntParameter(CAMPO_MESE_DATA_FINE))
      lRegMod.setMeseFine(getRequestIntParameter(CAMPO_MESE_DATA_FINE));
    if (isIntParameter(CAMPO_GIORNO_DATA_FINE))
      lRegMod.setGiornoFine(getRequestIntParameter(CAMPO_GIORNO_DATA_FINE));

    if (!isRequestParameterNullObj(CAMPO_COD_PERIODO_CONSUMAZIONE))
      lRegMod.setCodPeriodoConsumazione(getRequestStringParameter(CAMPO_COD_PERIODO_CONSUMAZIONE));

    if (!isRequestParameterNullObj(CAMPO_DESC_LUOGO))
      lRegMod.setDescLuogo(getRequestStringParameter(CAMPO_DESC_LUOGO));

    if (isIntParameter(CAMPO_ANNO_FONTE))
      lRegMod.setAnnoFonte(getRequestIntParameter(CAMPO_ANNO_FONTE));

    if (!isRequestParameterNullObj(CAMPO_NOTE))
      lRegMod.setNote(getRequestStringParameter(CAMPO_NOTE));

    UtenteModel lUtenteMod = new UtenteModel((UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
    lRegMod.setCodOperatoreAggiornamento(lUtenteMod.getUserId());
    lRegMod.setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());
    lRegMod.setDataAggiornamento(DateUtils.getSysDate());

    // chiama il controller
    IRegeReato lCtrl = RegeSiesLookupRemote.getRegeReatoRemote();
    RegeReatoModel lRegModRet = lCtrl.ExModificaRegeReato(lRegMod);

    setRequestAttribute("regereato", lRegModRet);

    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
      "=siap.regesies.regereato.action.ActDettaglioRegeReato&" + CAMPO_ID_FILE + "=" + lRegModRet.getIdFile() +
      "&" + CAMPO_PROGR_REATO + "=" + lRegModRet.getProgrReato() + "&" + CAMPO_PROGR_CIRCOSTANZA + "=" + lRegModRet.getProgrCircostanza(); ;

    return lPage;
  }

}