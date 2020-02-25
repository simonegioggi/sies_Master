package siap.siep.penapresunta.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiPenaPresunta</p>
* <p>Description: Classe di costanti di PenaPresunta</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiPenaPresunta
{
		 public static final String CAMPO_ID_PENA_PRESUNTA = "IdPenaPresunta"; 
		 public static final String CAMPO_GIORNO_DATA_INIZIO = "GiornoDataInizio"; 
		 public static final String CAMPO_MESE_DATA_INIZIO = "MeseDataInizio"; 
		 public static final String CAMPO_ANNO_DATA_INIZIO = "AnnoDataInizio"; 
		 public static final String CAMPO_GIORNO_DATA_FINE = "GiornoDataFine"; 
		 public static final String CAMPO_MESE_DATA_FINE = "MeseDataFine"; 
		 public static final String CAMPO_ANNO_DATA_FINE = "AnnoDataFine"; 
		 public static final String CAMPO_NUM_ANNI_RECLUSIONE = "NumAnniReclusione"; 
		 public static final String CAMPO_NUM_MESI_RECLUSIONE = "NumMesiReclusione"; 
		 public static final String CAMPO_NUM_GIORNI_RECLUSIONE = "NumGiorniReclusione"; 
		 public static final String CAMPO_IMPORTO_MULTA = "ImportoMulta"; 
		 public static final String CAMPO_NUM_ANNI_ARRESTO = "NumAnniArresto"; 
		 public static final String CAMPO_NUM_MESI_ARRESTO = "NumMesiArresto"; 
		 public static final String CAMPO_NUM_GIORNI_ARRESTO = "NumGiorniArresto"; 
		 public static final String CAMPO_IMPORTO_AMMENDA = "ImportoAmmenda"; 
		 public static final String CAMPO_DIES_A_QUO = "DiesAQuo"; 
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
		 public static final String CAMPO_GIORNO_DATA_FINE_RECLUSIONE = "GiornoDataFineReclusione"; 
		 public static final String CAMPO_MESE_DATA_FINE_RECLUSIONE = "MeseDataFineReclusione"; 
		 public static final String CAMPO_ANNO_DATA_FINE_RECLUSIONE = "AnnoDataFineReclusione"; 
		 public static final String CAMPO_GIORNO_DATA_INIZIO_ARRESTO = "GiornoDataInizioArresto"; 
		 public static final String CAMPO_MESE_DATA_INIZIO_ARRESTO = "MeseDataInizioArresto"; 
		 public static final String CAMPO_ANNO_DATA_INIZIO_ARRESTO = "AnnoDataInizioArresto"; 
		 public static final String PG_LOAD_RICERCAPENAPRESUNTA	= IWebConstants.ROOT_DIR + "files/siap/siep/penapresunta/LoadRicercaPenaPresunta.jsp";
		 public static final String PG_LOAD_DETTAGLIOPENAPRESUNTA	= IWebConstants.ROOT_DIR + "files/siap/siep/penapresunta/LoadRicercaPenaPresunta.jsp";
		 public static final String PG_RICERCAPENAPRESUNTA	= IWebConstants.ROOT_DIR + "files/siap/siep/penapresunta/RicercaPenaPresunta.jsp";
		 public static final String PG_LOAD_INSERISCIPENAPRESUNTA	= IWebConstants.ROOT_DIR + "files/siap/siep/penapresunta/LoadInserisciPenaPresunta.jsp";
}