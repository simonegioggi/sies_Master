package siap.siep.altracausa.action;

import f3b.web.IWebConstants;

/**
* <p>Title: ICostantiAltraCausa</p>
* <p>Description: Classe di costanti di AltraCausa</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public interface ICostantiAltraCausa
{
  public static final String CAMPO_ID_ALTRA_CAUSA = "IdAltraCausa";
  public static final String CAMPO_ANNO = "Anno";
  public static final String CAMPO_NUMERO = "Numero";
  public static final String CAMPO_GIORNO_DATA = "GiornoData";
  public static final String CAMPO_MESE_DATA = "MeseData";
  public static final String CAMPO_ANNO_DATA = "AnnoData";
  public static final String CAMPO_COD_LUOGO = "CodLuogoAltraCausa";
  public static final String CAMPO_COD_AUTORITA = "CodAutorita";
  public static final String CAMPO_GIORNO_DATA_DECORRENZA = "GiornoDataDecorrenza";
  public static final String CAMPO_MESE_DATA_DECORRENZA = "MeseDataDecorrenza";
  public static final String CAMPO_ANNO_DATA_DECORRENZA = "AnnoDataDecorrenza";
  public static final String CAMPO_GIORNO_DATA_SCADENZA = "GiornoDataScadenza";
  public static final String CAMPO_MESE_DATA_SCADENZA = "MeseDataScadenza";
  public static final String CAMPO_ANNO_DATA_SCADENZA = "AnnoDataScadenza";
  public static final String CAMPO_COD_TIPO_POS_GIURIDICA = "CodTipoPosGiuridica";
  //modifica relativa al tipo istituto
  public static final String CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE = "IstDetIdIstitutoDetenzioneAltraCausa";

//  public static final String CAMPO_COD_TIPO_ISTITUTO = "CodTipoIstitutoAltra";
//  public static final String CAMPO_COD_LUOGO_ISTITUTO = "CodLuogoIstituto";
  public static final String CAMPO_ALTRO_LUOGO_ALTRA = "AltroLuogoAltra";
  public static final String CAMPO_NOTE = "Note";
  public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
  public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
  public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
  public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
  public static final String CAMPO_COD_UFFICIO_INSERIMENTO = "CodUfficioInserimento";
  public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento";
  public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento";
  public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento";
  public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento";
  public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO = "CodUfficioAggiornamento";
  public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP = "FasSieIdFascicoloSiep";

  public static final String PG_LOAD_RICERCAALTRACAUSA	= IWebConstants.ROOT_DIR + "files/siap/siep/altracausa/LoadRicercaAltraCausa.jsp";
  public static final String PG_LOAD_DETTAGLIOALTRACAUSA	= IWebConstants.ROOT_DIR + "files/siap/siep/altracausa/LoadRicercaAltraCausa.jsp";
  public static final String PG_RICERCAALTRACAUSA	= IWebConstants.ROOT_DIR + "files/siap/siep/altracausa/RicercaAltraCausa.jsp";
  public static final String PG_LOAD_INSERISCIALTRACAUSA	= IWebConstants.ROOT_DIR + "files/siap/siep/altracausa/LoadInserisciAltraCausa.jsp";
}