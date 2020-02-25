package siap.sius.prescrizione.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiPrescrizione</p>
* <p>Description: Classe di costanti di Prescrizione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiPrescrizione
{
  public static final String CAMPO_ID_PRESCRIZIONE = "IdPrescrizione";
  public static final String CAMPO_COD_TIPO_PRESCRIZIONE = "CodTipoPrescrizione";
  public static final String CAMPO_COD_LUOGO_AFFIDAMENTO = "CodLuogoAffidamento";
  public static final String CAMPO_COD_UFF_MAGISTRATO_COMPETENTE = "CodUffMagistratoCompetente";
  public static final String CAMPO_COD_LUOGO_AUTORIZZATO = "CodLuogoAutorizzato";
  public static final String CAMPO_ID_CSSA_COMPETENTE = "IdCssaCompetente";
  public static final String CAMPO_COMUNE_CSSA_COMP = "ComuneCssaComp";
  public static final String CAMPO_DESCR_MANSIONE_LAVORATIVA = "DescrMansioneLavorativa";
  public static final String CAMPO_DESCR_LUOGO_LAVORO = "DescrLuogoLavoro";
  public static final String CAMPO_COD_PROVINCIA_AUTORIZZATA = "CodProvinciaAutorizzata";
  public static final String CAMPO_ORA_USCITA_ABITAZIONE = "OraUscitaAbitazione";
  public static final String CAMPO_ORA_RIENTRO_ABITAZIONE = "OraRientroAbitazione";
  public static final String CAMPO_AUTORITA_COMPETENTE_CONTROLLO = "AutoritaCompetenteControllo";
  public static final String CAMPO_NUM_VOLTE_CONTROLLO = "NumVolteControllo";
  public static final String CAMPO_DESCR_ALTRA_PRESCRIZIONE = "DescrAltraPrescrizione";
  // STUB : 20030710 - da Eliminare anche per  Azioni Modifica e Ricerca usare il getRequestStringParameters.
  public static final String CAMPO_DESCR_ALTRA_PRESCRIZIONE1 = "DescrAltraPrescrizione1";
  public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
  public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
  public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
  public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
  public static final String CAMPO_COD_UFFICIO_INSERIMENTO = "CodUfficioInserimento";
  public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO    = "CodOperatoreAggiornamento";
  public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO      = "GiornoDataAggiornamento";
  public static final String CAMPO_MESE_DATA_AGGIORNAMENTO        = "MeseDataAggiornamento";
  public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO        = "AnnoDataAggiornamento";
  public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO      = "CodUfficioAggiornamento";
  public static final String CAMPO_EVE_ID_EVENTO                  = "EveIdEvento";
//  public static final String CAMPO_DEP_OPID_DEPOSITO_ORDINANZA_PC = "DepOpidDepositoOrdinanzaPc";
  public static final String CAMPO_DESCR_COMUNITA_TERAPEUTICA     = "DescrComunitaTerapeutica";
  public static final String CAMPO_PROGR_PRESCRIZIONE             = "ProgrPrescrizione";
  public static final String NEXT_ACTION                          = "nextaction";
  
  // STUB 20030710 - Anche questo potrebbe essere unico ....
  public static final String CAMPO_CK_01 = "Check01";
  public static final String CAMPO_CK_02 = "Check02";
  public static final String CAMPO_CK_03 = "Check03";
  public static final String CAMPO_CK_04 = "Check04";
  public static final String CAMPO_CK_05 = "Check05";
  public static final String CAMPO_CK_06 = "Check06";
  public static final String CAMPO_CK_07 = "Check07";
  public static final String CAMPO_CK_08 = "Check08";
  public static final String CAMPO_CK_09 = "Check09";
  public static final String CAMPO_CK_10 = "Check10";
  public static final String CAMPO_CK_11 = "Check11";
  public static final String CAMPO_CK_12 = "Check12";
  public static final String CAMPO_CK_13 = "Check13";
  public static final String CAMPO_CK_14 = "Check14";
  public static final String CAMPO_CK_15 = "Check15";
  public static final String CAMPO_CK_16 = "Check16";
  public static final String CAMPO_CK_17 = "Check17";
  public static final String CAMPO_CK_18 = "Check18";
  public static final String CAMPO_CK_90 = "Check90";
  public static final String CAMPO_CK_91 = "Check91";

  public static final String PG_LOAD_RICERCAPRESCRIZIONE	= IWebConstants.ROOT_DIR + "files/siap/sius/prescrizione/LoadRicercaPrescrizione.jsp";
  public static final String PG_LOAD_DETTAGLIOPRESCRIZIONE	= IWebConstants.ROOT_DIR + "files/siap/sius/prescrizione/DettaglioPrescrizione.jsp";
  public static final String PG_RICERCAPRESCRIZIONE	= IWebConstants.ROOT_DIR + "files/siap/sius/prescrizione/RicercaPrescrizione.jsp";
  public static final String PG_LOAD_INSERISCIPRESCRIZIONE	= IWebConstants.ROOT_DIR + "files/siap/sius/prescrizione/LoadInserisciPrescrizione.jsp";
  public static final String PG_INCLUDE_PRESCRIZIONI	= IWebConstants.ROOT_DIR + "files/siap/sius/prescrizione/ElencoPrescrizioni.jsp";
  
  // Per nuova gestione Prescrizioni 
  public static final String PG_LOAD_INSERISCIPRESCRIZIONE_NEW	= IWebConstants.ROOT_DIR + "files/siap/sius/prescrizione/LoadInserisciPrescrizioneNew.jsp";
    
}