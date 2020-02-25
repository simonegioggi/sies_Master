package siap.sico.libertaanticipata.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiPeriodoLibanticipata</p>
* <p>Description: Classe di costanti di PeriodoLibanticipata</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiLibertaAnticipata
{
  public static final int NUM_TOTALE_SEMESTRI  = 12;
  public static final int NUM_RIGHE_SEMESTRI  = 4;
  public static final int NUM_COLONNE_SEMESTRI  = 3;
  public static final int NUM_PERIODI  = 10;

  // PERIODO_LIBANTICIPATA
     public static final String CAMPO_ID_PERIODO_LIBANTICIPATA = "IdPeriodoLibanticipata";
     public static final String CAMPO_GIORNO_DATA_INIZIO = "ggDal";
     public static final String CAMPO_MESE_DATA_INIZIO = "mmDal";
     public static final String CAMPO_ANNO_DATA_INIZIO = "aaDal";
     public static final String CAMPO_GIORNO_DATA_FINE = "ggAl";
     public static final String CAMPO_MESE_DATA_FINE = "mmAl";
     public static final String CAMPO_ANNO_DATA_FINE = "aaAl";
     public static final String CAMPO_CHECK_CONCESSI = "ggg";
     public static final String CAMPO_CHECK_PERIODO = "ggg0";
     public static final String CAMPO_CHECK_RIGETTATI = "ggg1";
     public static final String CAMPO_CHECK_INAMMISSIBILI = "ggg2";
     public static final String CAMPO_CHECK_NLP = "ggg3";
     public static final String CAMPO_RADIO_TIPO_CONCESSIONE = "modo";

     public static final String CAMPO_FLAG_CONCESSO = "FlagConcesso";
     public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
     public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
     public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
     public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
     public static final String CAMPO_COD_UFFICIO_INSERIMENTO = "CodUfficioInserimento";
     public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento";
     public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento";
     public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento";
     public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento";
     public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO = "CodUfficioAggiornamento";
     public static final String CAMPO_LIC_ID_LICENZA_LIBANTICIPATA = "LicIdLicenzaLibanticipata";
// decreto legge 146/2013
     public static final String CAMPO_RADIO_QUALE_LA_CONCEDE = "tipoLA";
     public static final String CAMPO_QUALE_SELEZIONATO = "codicesel";
     public static final String CAMPO_QUALE_IND_SELEZIONATO = "indicesel";
     public static final String CAMPO_SALVA_GIORNI_TOT = "salvagiorniTOT";
//     
     public static final String CAMPO_SALVA_GIORNI_LA = "salvagiorniLA";
     public static final String CAMPO_SALVA_GIORNI_LA_SPE = "salvagiorniLA_SPE";
     public static final String CAMPO_SALVA_GIORNI_LA_INT = "salvagiorniLA_INT";
// 
     public static final String CAMPO_GIORNO_DATA_INIZIO_SPE = "ggDalspe";
     public static final String CAMPO_MESE_DATA_INIZIO_SPE = "mmDalspe";
     public static final String CAMPO_ANNO_DATA_INIZIO_SPE = "aaDalspe";
     public static final String CAMPO_GIORNO_DATA_FINE_SPE = "ggAlspe";
     public static final String CAMPO_MESE_DATA_FINE_SPE = "mmAlspe";
     public static final String CAMPO_ANNO_DATA_FINE_SPE = "aaAlspe";
     
     public static final String CAMPO_CHECK_CONCESSI_SPE = "gggspe";
     public static final String CAMPO_CHECK_PERIODO_SPE = "gggspe0";
     public static final String CAMPO_CHECK_RIGETTATI_SPE = "gggspe1";
     public static final String CAMPO_CHECK_INAMMISSIBILI_SPE = "gggspe2";
     public static final String CAMPO_CHECK_NLP_SPE = "gggspe3";
     public static final String CAMPO_RADIO_TIPO_CONCESSIONE_SPE = "modo_SPE";
//     
     public static final String CAMPO_GIORNO_DATA_INIZIO_INT = "ggDalint";
     public static final String CAMPO_MESE_DATA_INIZIO_INT = "mmDalint";
     public static final String CAMPO_ANNO_DATA_INIZIO_INT = "aaDalint";
     public static final String CAMPO_GIORNO_DATA_FINE_INT = "ggAlint";
     public static final String CAMPO_MESE_DATA_FINE_INT = "mmAlint";
     public static final String CAMPO_ANNO_DATA_FINE_INT = "aaAlint";
     
     public static final String CAMPO_CHECK_CONCESSI_INT = "gggint";
     public static final String CAMPO_CHECK_PERIODO_INT = "gggint0";
     public static final String CAMPO_CHECK_RIGETTATI_INT = "gggint1";
     public static final String CAMPO_CHECK_INAMMISSIBILI_INT = "gggint2";
     public static final String CAMPO_CHECK_NLP_INT = "gggint3";     
     public static final String CAMPO_RADIO_TIPO_CONCESSIONE_INT = "modo_INT";
// END Decreto
     
     
     public static final String CAMPO_TIPO_COMPUTO_LA  = "TIPO_COMPUTO_LA"; // RIDIMENSIONAMENTO/REVOCA
     public static final String TIPO_COMPUTO_LA_RIDIM  = "RIDIMENSIONAMENTO";
     public static final String TIPO_COMPUTO_LA_REVOCA = "REVOCA"; 
     
     
     public static final String PG_LOAD_RICERCAPERIODOLIBANTICIPATA	= IWebConstants.ROOT_DIR + "files/siap/sico/libertaanticipata/LoadRicercaPeriodoLibanticipata.jsp";
     public static final String PG_LOAD_DETTAGLIOPERIODOLIBANTICIPATA	= IWebConstants.ROOT_DIR + "files/siap/sico/libertaanticipata/LoadRicercaPeriodoLibanticipata.jsp";
     public static final String PG_RICERCAPERIODOLIBANTICIPATA	= IWebConstants.ROOT_DIR + "files/siap/sico/libertaanticipata/RicercaPeriodoLibanticipata.jsp";
     public static final String PG_LOAD_INSERISCIPERIODOLIBANTICIPATA	= IWebConstants.ROOT_DIR + "files/siap/sico/libertaanticipata/LoadInserisciPeriodoLibanticipata.jsp";
     public static final String PG_DETTAGLIO_LIBANTICIPATA	= IWebConstants.ROOT_DIR + "files/siap/sico/libertaanticipata/DettaglioLibanticipata.jsp";
//     public static final String JS_CONTROLLI_LIBANTICIPATA    = IWebConstants.ROOT_DIR   + "files/siap/sico/libertaanticipata/LibanticipataFun.js";

// Revoca L.A.
     public static final String PG_DETTAGLIO_REVOCA_LIBANTICIPATA	= IWebConstants.ROOT_DIR + "files/siap/sico/libertaanticipata/DettaglioRevocaLibanticipata.jsp";


  //LIBERAZIONE ANTICIPATA SIEP
  public static final String PG_LOAD_INSERISCILIBANTICIPATA	= IWebConstants.ROOT_DIR + "files/siap/siep/liberazioneanticipata/LoadInserisciLiberazioneAnticipata.jsp";
  public static final String PG_LOAD_DETTAGLIO_LIBANTICIPATA	= IWebConstants.ROOT_DIR + "files/siap/siep/liberazioneanticipata/DettaglioLiberazioneAnticipata.jsp";
  
  // DL 92 Rimedi Risarcitori
  public static final String PG_LOAD_INSERISCI_RIMEDI_RISARCIORI = IWebConstants.ROOT_DIR + "files/siap/siep/liberazioneanticipata/LoadInserisciRimediRisarcitori.jsp";
  public static final String PG_LOAD_DETTAGLIO_RIMEDI_RISARCIORI = IWebConstants.ROOT_DIR + "files/siap/siep/liberazioneanticipata/DettaglioRimediRisarcitori.jsp";
  
  // DL 92 Reclamo Rimedi Risarcitori
  public static final String PG_LOAD_INSERISCI_RECLAMO_35TER = IWebConstants.ROOT_DIR + "files/siap/siep/liberazioneanticipata/LoadInserisciReclamo35Ter.jsp";
  public static final String PG_LOAD_DETTAGLIO_RECLAMO_RIMEDI_RISARCIORI = IWebConstants.ROOT_DIR + "files/siap/siep/liberazioneanticipata/DettaglioReclamoRimediRisarcitori.jsp";
}