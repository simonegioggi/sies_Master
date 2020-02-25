package siap.siep.modulocumulo.action;

import f3b.web.IWebConstants;

/**
* <p>Title: ICostantiCircostanzaCumulo</p>
* <p>Description: Interfaccia di costanti di Circostanza_Cumulo</p>
* @version 1.0
*/

public interface ICostantiCircostanzaCumulo
{
  public static final String CAMPO_ID_CIRCOSTANZA_CUMULO = "IdCircostanza";
  public static final String CAMPO_COD_TIPO_CIRCOSTANZA = "CodTipoCircostanza";
  public static final String CAMPO_COD_FONTE = "CCodFonte";
  public static final String CAMPO_ANNO_FONTE = "CAnnoFonte";
  public static final String CAMPO_NUMERO_FONTE = "CNumeroFonte";
  public static final String CAMPO_ARTICOLO = "CArticolo";
  public static final String CAMPO_COD_SOTTONUMERAZIONE = "CCodSottonumerazione";
  public static final String CAMPO_COMMA = "CComma";
  public static final String CAMPO_COMMA_QUALIFICANTE = "CCommaQualificante";
  public static final String CAMPO_LETTERA = "CLettera";
  public static final String CAMPO_NUMERO = "CNumero";
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
  public static final String CAMPO_FLAG_SENTENZA_APPLICAZ_PENA = "FlagSentenzaApplicazPena";
  public static final String CAMPO_COD_BILANCIAMENTO_CIRCOSTANZE = "CodBilanciamentoCircostanze";
  public static final String CAMPO_FLAG_GIUDIZIO_ABBREVIATO = "FlagGiudizioAbbreviato";
  public static final String CAMPO_NOTE_BILANCIAMENTO = "NoteBilanciamento";

  public static final String CAMPO_DESC_FONTE = "DescFonteCirco";
  public static final String CAMPO_DESC_SOTTONUMERAZIONE = "DescSottonumerazioneCirco";
  public static final String CAMPO_DESC_COMMA_QUAL = "DescCommaQualificanteCirco";

  public static final String PG_LOAD_INSERISCICIRCOSTANZA_CUMULO	= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/LoadInserisciCircostanzaCumulo.jsp";
  public static final String PG_DETTAGLIO_CIRCOSTANZA_CUMULO		= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/DettaglioCircostanzaCumulo.jsp";
  public static final String PG_RICERCA_CIRCOSTANZA_CUMULO			= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/RicercaCircostanzaCumulo.jsp";
  public static final String PG_LOAD_MODIFICACIRCOSTANZA_CUMULO		= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/LoadModificaCircostanzaCumulo.jsp";
}