package siap.regesies.action;

import f3b.web.IWebConstants;

/**
 *
 * <p>Title: ICostantiRegeSies</p>
 * <p>Description: Costanti Comuni a tutto il progetto regeSies</p>
 */
public interface ICostantiRegeSies
{
  public static final String PAGE_DETTAGLIO_PROVVEDIMENTO_INCLUDE = IWebConstants.ROOT_DIR + "files/siap/regesies/regesentenza/DettaglioProvvedimentoSoggettoInclude.jsp";
  public static final String PAGE_DETTAGLIO_PROVVEDIMENTO_SOLO_INCLUDE = IWebConstants.ROOT_DIR + "files/siap/regesies/regesentenza/DettaglioProvvedimentoInclude.jsp";

  public static final String ID_FILE = "IdFile";
  public static final String CAMPO_ENTITA_CHIAVE_UNO = "ChiaveUno";
  public static final String CAMPO_VALORE_CHIAVE_UNO = "ValoreUno";
  public static final String CAMPO_ENTITA_CHIAVE_DUE = "ChiaveDue";
  public static final String CAMPO_VALORE_CHIAVE_DUE = "ValoreDue";
  public static final String CAMPO_ENTITA_CHIAVE_TRE = "ChiaveTre";
  public static final String CAMPO_VALORE_CHIAVE_TRE = "ValoreTre";
  public static final String CAMPO_ENTITA_CHIAVE_QUATTRO = "ChiaveQuattro";
  public static final String CAMPO_VALORE_CHIAVE_QUATTRO = "ValoreQuattro";
  public static final String CAMPO_TORNA_INDIETRO = "TornaIndietro";

  public static final String UTIL_DATA = "onFocus=\"javascript:textboxSelect(this)\" onkeypress=\"return TicTabNumField(this,event)\" onBlur=\"javascript:value=FillDM(value)\"";

  public static final String PAGE_DETTAGLIO_RITORNO = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.regesies.regesentenza.action.ActDettaglioProvvedimento&"+ICostantiRegeSies.ID_FILE+"=";
  public static final String PAGE_BUTTONS_REGE = IWebConstants.ROOT_DIR + "files/siap/regesies/buttonsRege.jsp";
  public static final String PG_TOOLBAR_REGE_HEADER = IWebConstants.ROOT_DIR + "files/siap/regesies/toolbarRegeheader.jsp";

}