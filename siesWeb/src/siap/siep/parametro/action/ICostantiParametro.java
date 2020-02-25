package siap.siep.parametro.action;

import f3b.web.IWebConstants;
/**
 * <p>Title: ICostantiParametro</p>
 * <p>Description: Classe di costanti di Parametro</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public interface ICostantiParametro
{
  public static final String PERIODO_FERIALE = "PERIODO FERIALE";

  public static final String CAMPO_ID_PARAMETRO = "IdParametro";
  public static final String CAMPO_NOME_PARAMETRO = "NomeParametro";
  public static final String CAMPO_VALORE = "Valore";
  public static final String CAMPO_ANNI = "Anni";
  public static final String CAMPO_MESI = "Mesi";
  public static final String CAMPO_GIORNI = "Giorni";
  public static final String CAMPO_IMPORTO = "Importo";
  public static final String CAMPO_GIORNO_DATA_INIZIO_VALIDITA = "GiornoDataInizioValidita";
  public static final String CAMPO_MESE_DATA_INIZIO_VALIDITA = "MeseDataInizioValidita";
  public static final String CAMPO_ANNO_DATA_INIZIO_VALIDITA = "AnnoDataInizioValidita";
  public static final String CAMPO_GIORNO_DATA_FINE_VALIDITA = "GiornoDataFineValidita";
  public static final String CAMPO_MESE_DATA_FINE_VALIDITA = "MeseDataFineValidita";
  public static final String CAMPO_ANNO_DATA_FINE_VALIDITA = "AnnoDataFineValidita";
  public static final String CAMPO_COD_UFFICIO_VALIDITA = "CodUfficioValidita";
  public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
  public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
  public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
  public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
  public static final String CAMPO_COD_UFFICIO_INSERIMENTO = "CodUfficioInserimento";
  public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento";
  public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento";
  public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento";
  public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento";
  public static final String CAMPO_COD_UFFICIO_AGGIORMANENTO = "CodUfficioAggiormanento";
  
  public static final String PG_LOAD_RICERCAPARAMETRO	= IWebConstants.ROOT_DIR + "files/siap/siep/parametro/LoadRicercaParametro.jsp";
  public static final String PG_LOAD_DETTAGLIOPARAMETRO	= IWebConstants.ROOT_DIR + "files/siap/siep/parametro/DettaglioParametro.jsp";
  public static final String PG_RICERCAPARAMETRO	= IWebConstants.ROOT_DIR + "files/siap/siep/parametro/RicercaParametro.jsp";
  public static final String PG_LOAD_INSERISCIPARAMETRO	= IWebConstants.ROOT_DIR + "files/siap/siep/parametro/LoadInserisciParametro.jsp";
}