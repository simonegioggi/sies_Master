package siap.siep.calcolopena.action;

/**
* <p>Title: ICostantiAvvocato</p>
* <p>Description: Classe di costanti di Calcolo Pena</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.web.IWebConstants;

public interface ICostantiCalcoloPena {
  // Costanti che indicano quale è la 'Pena Iniziale'
  public static final int PENA_NON_DEFINITA     = 0;
  public static final int PENA_IN_SENTENZA      = 1;
  public static final int PENA_IN_CUMULO        = 2;
  public static final int PENA_SOSPENSIONE      = 3;
  public static final int PENA_REVOCA_MA        = 4;
  public static final int PENA_REVOCA_INDULTINO = 5;
  public static final int PENA_MANUALE          = 6;
  public static final int PENA_ARCHIVIATA_RES   = 7;
  public static final int PENA_SOSPENSIONE_RES  = 8;
  public static final int PENA_MANUALE_RES      = 9; // Forzatura pena RES
  public static final int PENA_ULTIMA_VALIDATA  = 10; 
  public static final int PENA_DA_ERGASTOLO     = 11; 
  public static final int PENA_DA_INDULTO       = 12; // Scarcerazione Provvisoria
  public static final int PENA_ARCHIVIATA_SIEP  = 13;
  public static final int PENA_DA_REVOCA_SS     = 14;
  public static final int PENA_CESSAZIONE_MA    = 15; // add 04/2014 DL 146
  public static final int PENA_IN_CUMULO_NEW    = 16;
  
  public static final String CAMPO_GIORNO_DATA_INIZIO = "ggDal";
  public static final String CAMPO_MESE_DATA_INIZIO = "mmDal";
  public static final String CAMPO_ANNO_DATA_INIZIO = "aaDal";
  public static final String CAMPO_GIORNO_DATA_FINE = "ggAl";
  public static final String CAMPO_MESE_DATA_FINE = "mmAl";
  public static final String CAMPO_ANNO_DATA_FINE = "aaAl";

  // 20/05/2014 Nuova L.A. - DL 146/2013
  public static final String CAMPO_NUM_GIORNI_LA = "totggLA";
  public static final String CAMPO_NUM_GIORNI_LA_SPE = "totggLASPE";
  public static final String CAMPO_NUM_GIORNI_LA_INT = "totggLAINT";
  public static final String CAMPO_SALVA_NUM_GIORNI_LA = "salggLA";
  public static final String CAMPO_SALVA_NUM_GIORNI_LA_SPE = "salggLASPE";
  public static final String CAMPO_SALVA_NUM_GIORNI_LA_INT = "salggLAINT";
  
  public static final String CAMPO_GIORNO_DATA_INIZIO_SPE = "ggDalSPE";
  public static final String CAMPO_MESE_DATA_INIZIO_SPE = "mmDalSPE";
  public static final String CAMPO_ANNO_DATA_INIZIO_SPE = "aaDalSPE";
  public static final String CAMPO_GIORNO_DATA_FINE_SPE = "ggAlSPE";
  public static final String CAMPO_MESE_DATA_FINE_SPE = "mmAlSPE";
  public static final String CAMPO_ANNO_DATA_FINE_SPE = "aaAlSPE";
  
  public static final String CAMPO_GIORNO_DATA_INIZIO_INT = "ggDalINT";
  public static final String CAMPO_MESE_DATA_INIZIO_INT = "mmDalINT";
  public static final String CAMPO_ANNO_DATA_INIZIO_INT = "aaDalINT";
  public static final String CAMPO_GIORNO_DATA_FINE_INT = "ggAlINT";
  public static final String CAMPO_MESE_DATA_FINE_INT = "mmAlINT";
  public static final String CAMPO_ANNO_DATA_FINE_INT = "aaAlINT";
  
  //MEV_2024-092
  public static final String CAMPO_NUM_ANNI_PRESOFFERTO   = "NumAnniPresofferto";
  public static final String CAMPO_NUM_MESI_PRESOFFERTO   = "NumMesiPresofferto";
  public static final String CAMPO_NUM_GIORNI_PRESOFFERTO = "NumGiorniPresofferto";
  
  public static final String CAMPO_POSIZIONE_GIURIDICA = "PosizioneGiuridica";
  public static final String POSIZIONE_GIURIDICA_LIBERO = "L";
  public static final String POSIZIONE_GIURIDICA_DETENUTO = "D";
  //MEV_2024-092 - FINE
  
  public static final String PG_F5 = IWebConstants.ROOT_DIR + "files/siap/siep/calcolopena/LoadF5.jsp";
  
  // Rideterminazione della pena (Ordine Scarcerazione nuovo residuo pena)
  public static final String PG_LOAD_INS_OS_NUOVO_RES_PENA_RIDET_PENA_ALTRO = IWebConstants.ROOT_DIR + "files/siap/siep/calcolopena/LoadInsOSNuovoResPenaRidetPenaAltro.jsp";
  public static final String PG_LOAD_DETT_OS_NUOVO_RES_PENA_RIDET_PENA_ALTRO = IWebConstants.ROOT_DIR + "files/siap/siep/calcolopena/DettOSNuovoResPenaRidetPenaAltro.jsp";

  
  // Comunicazione nuovo residuo Pena
  public static final String PG_LOAD_INS_COM_NUOVO_RES_PENA_RIDET_PENA_ALTRO = IWebConstants.ROOT_DIR + "files/siap/siep/calcolopena/LoadInsComNuovoResPenaRidetPenaAltro.jsp";
  public static final String PG_LOAD_DETT_COM_NUOVO_RES_PENA_RIDET_PENA_ALTRO = IWebConstants.ROOT_DIR + "files/siap/siep/calcolopena/DettComNuovoResPenaRidetPenaAltro.jsp";
    
  public static final String PG_LOAD_INS_RIDET_PENA_SORV = IWebConstants.ROOT_DIR + "files/siap/siep/liberazioneanticipata/LoadInsRidetPenaAltroSORV.jsp";
  
  // Anna
  public static final String PG_LOAD_DETTAGLIO_RIDETERMINAZIONE_PENA_SCOMP_SORV = IWebConstants.ROOT_DIR + "files/siap/siep/calcolopena/DettaglioRidetPenaScomputiSORV.jsp";
  // Rideterminazione della pena - Ridimensionamento LA
  public static final String PG_DETT_RIDET_PENA_RIDIM_LA = IWebConstants.ROOT_DIR + "files/siap/siep/calcolopena/DettaglioRidetPenaRidimLA.jsp";
  public static final String PG_GRIGLIA_RIDETERMINAZIONE_PENA_RIDIM_LA = IWebConstants.ROOT_DIR + "files/siap/siep/rideterminazionepena/GrigliaBottoniRidetPenaRidimLA.jsp";

  public static final String PG_LOAD_INS_OS_NUOVO_RES_PENA_RIDET_PENA_RIDIM_LA = IWebConstants.ROOT_DIR + "files/siap/siep/calcolopena/LoadInsOSNuovoResPenaRidetPenaRidimLA.jsp";
  public static final String PG_LOAD_DETT_OS_NUOVO_RES_PENA_RIDET_PENA_RIDIM_LA = IWebConstants.ROOT_DIR + "files/siap/siep/calcolopena/DettOSNuovoResPenaRidetPenaRidimLA.jsp";
  
  // MEV_2024-092
  public static final String PG_LOAD_CALCOLOPENA_DL92 = IWebConstants.ROOT_DIR + "files/siap/siep/calcolopena/LoadCalcoloPenaDL92.jsp";
  public static final String PG_CALCOLOPENA_DL92      = IWebConstants.ROOT_DIR + "files/siap/siep/calcolopena/EsitoCalcoloPenaDL92.jsp";
  // MEV_2024-092 - FINE
  
  // MEV_2026-1
  public static final String CAMPO_ID_CALCOLO_PENA_DL92= "idCalcoloPenaDL92";
  public static final String PG_LOAD_STORICO_CALCPENA_DL92 = IWebConstants.ROOT_DIR + "files/siap/siep/calcolopena/StoricoCalcoloPenaDL92.jsp";
  public static final String PG_DETT_STORICO_CALCPENA_DL92 = IWebConstants.ROOT_DIR + "files/siap/siep/calcolopena/DettStoricoCalcoloPenaDL92.jsp";
  // MEV_2026-1 - FINE

}