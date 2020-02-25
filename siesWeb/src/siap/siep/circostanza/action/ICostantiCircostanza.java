package siap.siep.circostanza.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiCircostanza</p>
* <p>Description: Classe di costanti di Circostanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiCircostanza
{
  public static final String CAMPO_ID_CIRCOSTANZA = "IdCircostanza";
  public static final String CAMPO_COD_TIPO_CIRCOSTANZA = "CodTipoCircostanza";
  public static final String CAMPO_COD_FONTE = "CCodFonte";
  public static final String CAMPO_ANNO_FONTE = "CAnnoFonte";
  public static final String CAMPO_NUMERO_FONTE = "CNumeroFonte";
  public static final String CAMPO_COD_SOTTONUMERAZIONE = "CCodSottonumerazione";
  public static final String CAMPO_COMMA = "CComma";
  public static final String CAMPO_LETTERA = "CLettera";
  public static final String CAMPO_NUMERO = "CNumero";
  public static final String CAMPO_ARTICOLO = "CArticolo";
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
  // Fabio - REWORK Bilanciamento Circostanze (a7-rr-165)
  public static final String CAMPO_FLAG_SENTENZA_APPLICAZ_PENA = "FlagSentenzaApplicazPena";
  public static final String CAMPO_COD_BILANCIAMENTO_CIRCOSTANZE = "CodBilanciamentoCircostanze";
  public static final String CAMPO_FLAG_GIUDIZIO_ABBREVIATO = "FlagGiudizioAbbreviato";
  public static final String CAMPO_NOTE_BILANCIAMENTO = "NoteBilanciamento";
  //***********************************************************************************
  //Federica - a9-rr-078
  //aggiunto campo Comma-Qualificante 
  public static final String CAMPO_COMMA_QUALIFICANTE = "CCommaQualificante";
  //***********************************************************************************
  public static final String CAMPO_DESC_FONTE = "DescFonteCirco";
  public static final String CAMPO_DESC_SOTTONUMERAZIONE = "DescSottonumerazioneCirco";
  public static final String CAMPO_DESC_COMMA_QUAL = "DescCommaQualificanteCirco";


  public static final String PG_LOAD_RICERCACIRCOSTANZA	= IWebConstants.ROOT_DIR + "files/siap/siep/circostanza/LoadRicercaCircostanza.jsp";
  public static final String PG_LOAD_DETTAGLIOCIRCOSTANZA	= IWebConstants.ROOT_DIR + "files/siap/siep/circostanza/DettaglioCircostanza.jsp";
  public static final String PG_RICERCACIRCOSTANZA	= IWebConstants.ROOT_DIR + "files/siap/siep/circostanza/RicercaCircostanza.jsp";
  public static final String PG_LOAD_INSERISCICIRCOSTANZA	= IWebConstants.ROOT_DIR + "files/siap/siep/circostanza/LoadInserisciCircostanza.jsp";
  public static final String PG_LOAD_MODIFICACIRCOSTANZA	= IWebConstants.ROOT_DIR + "files/siap/siep/circostanza/LoadModificaCircostanza.jsp";
}