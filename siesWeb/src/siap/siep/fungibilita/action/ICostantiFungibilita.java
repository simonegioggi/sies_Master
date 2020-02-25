package siap.siep.fungibilita.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiFungibilita</p>
* <p>Description: Classe di costanti di Fungibilita</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiFungibilita
{
  public static final String CAMPO_ID_FUNGIBILITA = "IdFungibilita";
  public static final String CAMPO_COD_TIPO_FUNGIBILITA = "CodTipoFungibilita";
  public static final String CAMPO_NUM_ANNI = "NumAnni";
  public static final String CAMPO_NUM_MESI = "NumMesi";
  public static final String CAMPO_NUM_GIORNI = "NumGiorni";
  public static final String CAMPO_GIORNO_DATA_DA = "GiornoDataDa";
  public static final String CAMPO_MESE_DATA_DA = "MeseDataDa";
  public static final String CAMPO_ANNO_DATA_DA = "AnnoDataDa";
  public static final String CAMPO_GIORNO_DATA_A = "GiornoDataA";
  public static final String CAMPO_MESE_DATA_A = "MeseDataA";
  public static final String CAMPO_ANNO_DATA_A = "AnnoDataA";
  public static final String CAMPO_GIORNO_DATA_INIZIO_VALIDITA = "GiornoDataInizioValidita";
  public static final String CAMPO_MESE_DATA_INIZIO_VALIDITA = "MeseDataInizioValidita";
  public static final String CAMPO_ANNO_DATA_INIZIO_VALIDITA = "AnnoDataInizioValidita";
  public static final String CAMPO_GIORNO_DATA_FINE_VALIDITA = "GiornoDataFineValidita";
  public static final String CAMPO_MESE_DATA_FINE_VALIDITA = "MeseDataFineValidita";
  public static final String CAMPO_ANNO_DATA_FINE_VALIDITA = "AnnoDataFineValidita";
  public static final String CAMPO_COD_UFFICIO_FRUITORE = "CodUfficioFruitore";
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
  public static final String CAMPO_EVE_ID_EVENTO = "EveIdEvento";
  public static final String CAMPO_FLAG_VALIDATO = "FlagValidato";
  public static final String CAMPO_NUM_GIORNI_FRUITI = "NumGiorniFruiti";
  public static final String CAMPO_NUM_GIORNI_NON_FRUITI = "NumGiorniNonFruiti";

  public static final String PG_LOAD_RICERCAFUNGIBILITA	= IWebConstants.ROOT_DIR + "files/siap/siep/fungibilita/LoadRicercaFungibilita.jsp";
  public static final String PG_LOAD_DETTAGLIOFUNGIBILITA	= IWebConstants.ROOT_DIR + "files/siap/siep/fungibilita/LoadRicercaFungibilita.jsp";
  public static final String PG_RICERCAFUNGIBILITA	= IWebConstants.ROOT_DIR + "files/siap/siep/fungibilita/RicercaFungibilita.jsp";
  public static final String PG_LOAD_INSERISCIFUNGIBILITA	= IWebConstants.ROOT_DIR + "files/siap/siep/fungibilita/LoadInserisciFungibilita.jsp";

  //TIPO FUNGIBILITA'
  public static final String FUNGIBILITA              = "01";
  public static final String PENA_ESPIATA_IN_ECCESSSO = "02";
  public static final String PENA_SENZA_TITOLO        = "03";

}