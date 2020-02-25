package siap.siep.luogodetenzione.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiLuogoDetenzione</p>
* <p>Description: Classe di costanti di LuogoDetenzione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiLuogoDetenzione
{
  public static final String CAMPO_ID_LUOGO_DETENZIONE = "IdLuogoDetenzione";
//modifica relativa al tipo istituto
  public static final String CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE = "IstDetIdIstitutoDetenzione";
 // public static final String CAMPO_COD_TIPO_ISTITUTO = "CodTipoIstituto";
 // public static final String CAMPO_COD_LUOGO = "CodLuogo";
 // public static final String CAMPO_INDIRIZZO = "Indirizzo";
 // public static final String CAMPO_DESCR = "Descr";
  public static final String CAMPO_NOTE = "Note";
  public static final String CAMPO_GIORNO_DATA_INIZIO_DETENZIONE = "GiornoDataInizioDetenzione";
  public static final String CAMPO_MESE_DATA_INIZIO_DETENZIONE = "MeseDataInizioDetenzione";
  public static final String CAMPO_ANNO_DATA_INIZIO_DETENZIONE = "AnnoDataInizioDetenzione";
  public static final String CAMPO_GIORNO_DATA_FINE_DETENZIONE = "GiornoDataFineDetenzione";
  public static final String CAMPO_MESE_DATA_FINE_DETENZIONE = "MeseDataFineDetenzione";
  public static final String CAMPO_ANNO_DATA_FINE_DETENZIONE = "AnnoDataFineDetenzione";
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
  public static final String CAMPO_FAS_SIU_ID_FASCICOLO_SIUS = "FasSiuIdFascicoloSius";
  public static final String CAMPO_POS_GIU_ID_POSIZIONE_GIURIDICA = "PosGiuIdPosizioneGiuridica";
//modifica relativa al tipo istituto
  public static final String CAMPO_ALTRO_LUOGO = "AltroLuogo";


  public static final String CAMPO_COD_TIPO_ISTITUTO_ALTRA_CAUSA = "CodTipoIstitutoAltraCausa";
  public static final String CAMPO_COD_LUOGO_ALTRA_CAUSA = "CodLuogoAltCausa";

  public static final String PG_LOAD_RICERCALUOGODETENZIONE   = IWebConstants.ROOT_DIR + "files/siap/siep/luogodetenzione/LoadRicercaLuogoDetenzione.jsp";
  public static final String PG_LOAD_DETTAGLIOLUOGODETENZIONE = IWebConstants.ROOT_DIR + "files/siap/siep/luogodetenzione/DettaglioLuogoDetenzione.jsp";
  public static final String PG_RICERCALUOGODETENZIONE	      = IWebConstants.ROOT_DIR + "files/siap/siep/luogodetenzione/RicercaLuogoDetenzione.jsp";
  public static final String PG_LOAD_INSERISCILUOGODETENZIONE = IWebConstants.ROOT_DIR + "files/siap/siep/luogodetenzione/LoadInserisciLuogoDetenzione.jsp";
}