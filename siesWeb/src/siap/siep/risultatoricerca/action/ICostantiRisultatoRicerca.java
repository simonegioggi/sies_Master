package siap.siep.risultatoricerca.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiRisultatoRicerca</p>
* <p>Description: Classe di costanti di RisultatoRicerca</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiRisultatoRicerca
{
		 public static final String CAMPO_ID_RICERCA = "IdRicerca";
		 public static final String CAMPO_COD_UFFICIO = "CodUfficio";
		 public static final String CAMPO_ID_FASCICOLO_SIEP = "IdFascicoloSiep";
		 public static final String CAMPO_CHIAVE_ANNO = "ChiaveAnno";
		 public static final String CAMPO_CHIAVE_PROGR = "ChiaveProgr";
		 public static final String CAMPO_COGNOME = "Cognome";
		 public static final String CAMPO_NOME = "Nome";
		 public static final String CAMPO_LUOGO_NASCITA = "LuogoNascita";
		 public static final String CAMPO_GIORNO_DATA_NASCITA = "GiornoDataNascita";
		 public static final String CAMPO_MESE_DATA_NASCITA = "MeseDataNascita";
		 public static final String CAMPO_ANNO_DATA_NASCITA = "AnnoDataNascita";
		 public static final String CAMPO_GIORNO_DATA_REATO = "GiornoDataReato";
		 public static final String CAMPO_MESE_DATA_REATO = "MeseDataReato";
		 public static final String CAMPO_ANNO_DATA_REATO = "AnnoDataReato";
		 public static final String CAMPO_GIORNO_DATA_FINE_PENA = "GiornoDataFinePena";
		 public static final String CAMPO_MESE_DATA_FINE_PENA = "MeseDataFinePena";
		 public static final String CAMPO_ANNO_DATA_FINE_PENA = "AnnoDataFinePena";
		 public static final String CAMPO_NUM_ANNI_PENA_RES = "NumAnniPenaRes";
		 public static final String CAMPO_NUM_MESI_PENA_RES = "NumMesiPenaRes";
		 public static final String CAMPO_NUM_GIORNI_PENA_RES = "NumGiorniPenaRes";
		 public static final String CAMPO_COD_POSIZIONE_GIURIDICA = "CodPosizioneGiuridica";
		 public static final String CAMPO_DESCR_POSIZIONE_GIURIDICA = "DescrPosizioneGiuridica";
		 public static final String CAMPO_POSIZIONE_GIURIDICA_AGGREGATA = "PosizioneGiuridicaAggregata";
		 public static final String CAMPO_CHIAVE_ANNO_INIZIALE = "ChiaveAnnoIniziale";
		 public static final String CAMPO_CHIAVE_PROGR_INIZIALE = "ChiaveProgrIniziale";
		 public static final String CAMPO_CHIAVE_ANNO_FINALE = "ChiaveAnnoFinale";
		 public static final String CAMPO_CHIAVE_PROGR_FINALE = "ChiaveProgrFinale";
		 public static final String CAMPO_NUM_ANNI_PENA_SEN = "NumAnniPenaSen";
     	 public static final String CAMPO_NUM_MESI_PENA_SEN = "NumMesiPenaSen";
     	 public static final String CAMPO_NUM_GIORNI_PENA_SEN = "NumGiorniPenaSen";
     	 public static final String CAMPO_COD_NAZIONE = "CodNazione";

		 public static final String PG_LOAD_RICERCA_RISULTATO_RICERCA	= IWebConstants.ROOT_DIR + "files/siap/siep/risultatoricerca/LoadRicercaRisultatoRicerca.jsp";
		 public static final String PG_LOAD_DETTAGLIORISULTATORICERCA	= IWebConstants.ROOT_DIR + "files/siap/siep/risultatoricerca/LoadRicercaRisultatoRicerca.jsp";
		 public static final String PG_RICERCARISULTATORICERCA	= IWebConstants.ROOT_DIR + "files/siap/siep/risultatoricerca/RicercaRisultatoRicerca.jsp";
		 public static final String PG_LOAD_INSERISCIRISULTATORICERCA	= IWebConstants.ROOT_DIR + "files/siap/siep/risultatoricerca/LoadInserisciRisultatoRicerca.jsp";
}