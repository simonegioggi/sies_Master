package siap.siep.rinnovo.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiRinnovo</p>
* <p>Description: Classe di costanti di Rinnovo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiRinnovo
{
		 public static final String CAMPO_ID_RINNOVO = "IdRinnovo";
		 public static final String CAMPO_COD_TIPO_RINNOVO = "CodTipoRinnovo";
		 public static final String CAMPO_GIORNO_DATA_RINNOVO = "GiornoDataRinnovo";
		 public static final String CAMPO_MESE_DATA_RINNOVO = "MeseDataRinnovo";
		 public static final String CAMPO_ANNO_DATA_RINNOVO = "AnnoDataRinnovo";
		 public static final String CAMPO_COD_TIPO_AUTORITA_RINNOVO = "CodTipoAutoritaRinnovo";
		 public static final String CAMPO_COD_LUOGO_RINNOVO = "CodLuogoRinnovo";
     public static final String CAMPO_COD_TIPO_AUTORITA_RINNOVO_A = "CodTipoAutoritaRinnovoA";
     public static final String CAMPO_COD_LUOGO_RINNOVO_A = "CodLuogoRinnovoA";

		 public static final String CAMPO_NOTE = "NoteRinnovo";
     public static final String CAMPO_NOTE_AL = "NoteRinnovoAL";
     public static final String CAMPO_COD_TIPO_AUTORITA_RINNOVO_AL = "CodTipoAutoritaRinnovoAL";
     public static final String CAMPO_COD_LUOGO_RINNOVO_AL = "CodLuogoRinnovoAL";

     public static final String CAMPO_NOTE_A = "NoteRinnovoA";

		 public static final String CAMPO_DOC_BLOB = "CampoBlob";
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
		 public static final String CAMPO_NOT_ID_NOTIFICA = "NotIdNotifica";
		 public static final String CAMPO_VER_ID_VERBALE = "VerIdVerbale";
     public static final String CAMPO_FLAG_DOCUMENTO_REGISTRATO = "FlagDocumentoRegistrato";
		 public static final String CAMPO_TEM_ID_TEMPLATE = "TemIdTemplate";
 		 public static final String CAMPO_LUOGO_NUOVA_NOTIFICA = "LuogoNuovaNotifica";

     public static final String CAMPO_LUOGO_NUOVA_NOTIFICA_UG_AR = "LuogoNuovaNotificaUgAr";


     public static final String CAMPO_VALIDA = "CampoValida";

     public static final String CAMPO_GIORNO_DATA_RELATA = "GiornoDataRelata";
     public static final String CAMPO_MESE_DATA_RELATA = "MeseDataRelata";
     public static final String CAMPO_ANNO_DATA_RELATA = "AnnoDataRelata";

     public static final String CAMPO_GIORNO_DATA_RINNOVO_RN = "GiornoDataRinnovoRn";
     public static final String CAMPO_MESE_DATA_RINNOVO_RN = "MeseDataRinnovoRn";
     public static final String CAMPO_ANNO_DATA_RINNOVO_RN = "AnnoDataRinnovoRn";

     public static final String CAMPO_GIORNO_DATA_RINNOVO_AR = "GiornoDataRinnovoAr";
     public static final String CAMPO_MESE_DATA_RINNOVO_AR = "MeseDataRinnovoAr";
     public static final String CAMPO_ANNO_DATA_RINNOVO_AR = "AnnoDataRinnovoAr";

     public static final String CAMPO_GIORNO_DATA_RINNOVO_RA = "GiornoDataRinnovoRa";
     public static final String CAMPO_MESE_DATA_RINNOVO_RA = "MeseDataRinnovoRa";
     public static final String CAMPO_ANNO_DATA_RINNOVO_RA = "AnnoDataRinnovoRa";


     public static final String CAMPO_COD_TIPO_AUTORITA_RINNOVO_UG = "CodTipoAutoritaRinnovoUg";
		 public static final String CAMPO_COD_LUOGO_RINNOVO_UG = "CodLuogoRinnovoUg";
		 public static final String CAMPO_COD_LUOGO_RINNOVO_UG_AR = "CodLuogoRinnovoUgAr";



		 public static final String PG_LOAD_RICERCARINNOVO	= IWebConstants.ROOT_DIR + "files/siap/siep/refertoscarcerazione/LoadRicercaRinnovo.jsp";
		 public static final String PG_LOAD_DETTAGLIORINNOVO	= IWebConstants.ROOT_DIR + "files/siap/siep/refertoscarcerazione/LoadRicercaRinnovo.jsp";
		 public static final String PG_RICERCARINNOVO	= IWebConstants.ROOT_DIR + "files/siap/siep/refertoscarcerazione/RicercaRinnovo.jsp";
		 public static final String PG_LOAD_INSERISCIRINNOVO	= IWebConstants.ROOT_DIR + "files/siap/siep/refertoscarcerazione/LoadInserisciRinnovo.jsp";
}