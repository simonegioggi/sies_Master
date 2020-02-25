package siap.siep.modulocumulo.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiPenaComplessivaCumulo</p>
* <p>Description: Classe di costanti di PenaComplessiva</p>
* <p>		in ambito Cumulo (Pena_complessiva_Cumulo) </p>
* @version 1.0
*/
public interface ICostantiPenaComplessivaCumulo
{
  public static final String CAMPO_ID_PENA_COMPLESSIVA_CUM = "IdPenaComplessivaCum";
  public static final String CAMPO_COD_TIPO_PENA_DETENTIVA = "CodTipoPenaDetentiva";
  
  public static final String CAMPO_NUM_ANNI_RECLUSIONE = "NumAnniReclusione";
  public static final String CAMPO_NUM_MESI_RECLUSIONE = "NumMesiReclusione";
  public static final String CAMPO_NUM_GIORNI_RECLUSIONE = "NumGiorniReclusione";
  public static final String CAMPO_IMPORTO_MULTA = "ImportoMulta";

  public static final String CAMPO_NUM_ANNI_ARRESTO = "NumAnniArresto";
  public static final String CAMPO_NUM_MESI_ARRESTO = "NumMesiArresto";
  public static final String CAMPO_NUM_GIORNI_ARRESTO = "NumGiorniArresto";
  public static final String CAMPO_IMPORTO_AMMENDA = "ImportoAmmenda";

  public static final String CAMPO_NUM_ANNI_ISOLAMENTO_DIURNO = "NumAnniIsolamentoDiurno";
  public static final String CAMPO_NUM_MESI_ISOLAMENTO_DIURNO = "NumMesiIsolamentoDiurno";
  public static final String CAMPO_NUM_GIORNI_ISOLAMENTO_DIURNO = "NumGiorniIsolamentoDiurno";

  public static final String CAMPO_ANNO_DATA_PRESCRIZIONE = "AnnoDataPrescrizione";
  public static final String CAMPO_MESE_DATA_PRESCRIZIONE = "MeseDataPrescrizione";
  public static final String CAMPO_GIORNO_DATA_PRESCRIZIONE = "GiornoDataPrescrizione";

  public static final String CAMPO_FLAG_PENA_IN_CONTINUAZIONE = "FlagPenaInContinuazione";

  public static final String CAMPO_FLAG_STATO      = "FlagStatoPC";
  public static final String CAMPO_MOTIVO_MODIFICA = "MotivoModifica";
  public static final String CAMPO_TIT_ID_TITOLO_CUMULATO = "TitIdTitoloCumulato";
  public static final String CAMPO_ID_PENA_COMPLESSIVA_ORIGINE = "IdPenaComplessivaOrigine";
  
  

  public static final String CAMPO_FLAG_SANZIONE_SOSTITUTIVA = "FlagSanzioneSostitutiva";

  public static final String CAMPO_INTERO_IMPORTO_MULTA = "interoImportoMulta";
  public static final String CAMPO_DECIMALE_IMPORTO_MULTA = "decimaleImportoMulta";
  public static final String CAMPO_INTERO_IMPORTO_AMMENDA = "interoImportoAmmenda";
  public static final String CAMPO_DECIMALE_IMPORTO_AMMENDA = "decimaleImportoAmmenda";
  public static final String CAMPO_VALUTA_IMPORTO_MULTA = "valutaImportoMulta";
  public static final String CAMPO_VALUTA_IMPORTO_AMMENDA = "valutaImportoAmmenda";

  public static final String PG_LOAD_DETTAGLIOPENACOMPLESSIVA_CUM = IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/DettaglioPenaComplessivaCumulo.jsp";
  public static final String PG_RICERCAPENACOMPLESSIVA_CUM	      = IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/RicercaPenaComplessivaCumulo.jsp";
  public static final String PG_LOAD_INSERISCIPENACOMPLESSIVA_CUM = IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/LoadInserisciPenaComplessivaCumulo.jsp";
  public static final String PG_LOAD_CANCELLAPENACOMPLESSIVA_CUM  = IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/LoadCancellaPenaComplessivaCumulo.jsp";
  public static final String PG_LOAD_MODIFICA_CONTINUAZIONE_CUM = IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/LoadModificaContinuazioneCumulo.jsp";
  
  public static final String PG_LOAD_INSERISCI_ULTERIORI_CONTINUAZIONI_CUM = IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/LoadInserisciUlterioriContinuazioniCumulo.jsp";
  
  public static final String PG_LOAD_RICERCAPENACOMPLESSIVA   = IWebConstants.ROOT_DIR + "files/siap/siep/penacomplessiva/LoadRicercaPenaComplessiva.jsp";

  public static final String PG_LOAD_DETTAGLIOMODIFICAPENACOMPLESSIVA  = IWebConstants.ROOT_DIR + "files/siap/siep/penacomplessiva/DettaglioModificaPenaComplessiva.jsp";

  public static final String PG_POPUP_TITOLI_PER_CONTINUAZIONE = IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/PopupListaTitoliPerContinuazione.jsp";

}