package siap.sige.detenzione.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiFasSigeDetenzione</p>
* <p>Description: Classe di costanti di FasSigeDetenzione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiFasSigeDetenzione
{
		 public static final String CAMPO_ID_FAS_SIGE_DETENZIONE = "IdFasSigeDetenzione"; 
		 public static final String CAMPO_FAS_ID_FAS_SIGE = "FasIdFasSige"; 
		 public static final String CAMPO_LD_ID_LUOGO_DETENZIONE = "LdIdLuogoDetenzione"; 
		 public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento"; 
		 public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento"; 
		 public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento"; 
		 public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento"; 
		 public static final String CAMPO_COD_UFFICIO_INSERIMENTO = "CodUfficioInserimento"; 
		 public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento"; 
		 public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento"; 
		 public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento"; 
		 public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento"; 
		 public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO = "CodUfficioAggiornamento"; 
		 public static final String CAMPO_AC_ID_ALTRA_CAUSA = "AcIdAltraCausa"; 
		 public static final String PG_LOAD_DETTAGLIOFASSIGEDETENZIONE	= IWebConstants.ROOT_DIR + "files/siap/sige/detenzione/LoadRicercaFasSigeDetenzione.jsp";
		 public static final String PG_LOAD_INSERISCIFASSIGEDETENZIONE	= IWebConstants.ROOT_DIR + "files/siap/sige/detenzione/LoadInserisciDetenzioneFasSige.jsp";
		 public static final String PG_RICERCADETENZIONESIGE       			= IWebConstants.ROOT_DIR + "files/siap/sige/detenzione/RicercaDetenzioneByProcedimentoSige.jsp";
		 public static final String PG_LOAD_DETTAGLIOLUOGODETENZIONE		= IWebConstants.ROOT_DIR + "files/siap/sige/detenzione/LoadDettaglioDetenzioneFasSige.jsp";
}