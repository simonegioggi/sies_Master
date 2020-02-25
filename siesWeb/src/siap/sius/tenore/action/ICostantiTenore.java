package siap.sius.tenore.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiTenore</p>
* <p>Description: Classe di costanti di Tenore</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiTenore
{
   public static final String CAMPO_ID_TENORE = "IdTenore";
   public static final String CAMPO_COD_ESITO_TENORE = "CodEsitoTenore";
   public static final String CAMPO_GIORNO_DATA = "GiornoData";
   public static final String CAMPO_MESE_DATA = "MeseData";
   public static final String CAMPO_ANNO_DATA = "AnnoData";
   public static final String CAMPO_COD_MAGISTRATO = "CodMagistrato";
   public static final String CAMPO_NOTE = "Note";
   public static final String CAMPO_COD_OGGETTO_TENORE = "CodOggettoTenore";
   public static final String CAMPO_COD_DETTAGLIO_OGGETTO = "CodDettaglioOggetto";
   public static final String CAMPO_DESCR_OGGETTO_TENORE = "DescrOggettoTenore";
   public static final String CAMPO_PROGR_TENORE = "ProgrTenore";
   // 05/11/2003 REWORKFascicoloGPModel.
   //public static final String CAMPO_FLAG_ESITO_TENORE = "FlagEsitoTenore";
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
   public static final String CAMPO_GEN_PRID_GENERALE_PROCEDIMENTO = "GenPridGeneraleProcedimento";
   public static final String CAMPO_DEP_OPID_DEPOSITO_ORDINANZA_PC = "DepOpidDepositoOrdinanzaPc";
   public static final String CAMPO_IMP_ID_IMPUGNAZIONE = "ImpIdImpugnazione";
   public static final String CAMPO_DEP_DEC_ID_DEPOSITO_DECRETO = "DepDecIdDepositoDecreto";

   public static final String PG_LOAD_RICERCATENORE	= IWebConstants.ROOT_DIR + "files/siap/sius/tenore/LoadRicercaTenore.jsp";
   public static final String PG_LOAD_DETTAGLIOTENORE	= IWebConstants.ROOT_DIR + "files/siap/sius/tenore/LoadRicercaTenore.jsp";
   public static final String PG_RICERCATENORE	= IWebConstants.ROOT_DIR + "files/siap/sius/tenore/RicercaTenore.jsp";
   public static final String PG_LOAD_INSERISCITENORE	= IWebConstants.ROOT_DIR + "files/siap/sius/tenore/LoadInserisciTenore.jsp";
}