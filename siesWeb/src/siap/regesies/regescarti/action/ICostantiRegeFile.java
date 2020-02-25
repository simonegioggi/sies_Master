package siap.regesies.regescarti.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiRegeFile</p>
* <p>Description: Classe di costanti di file scartati REGE</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiRegeFile
{
		 public static final String CAMPO_ID_FILE = "IdFile";
		 public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
		 public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
		 public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
		 public static final String CAMPO_COD_COMUNE = "CodComune";
		 public static final String CAMPO_FILE_BLOB = "FileBlob";
		 public static final String CAMPO_AF10FASC = "Af10fasc";
		 public static final String CAMPO_AF10PROG = "Af10prog";
		 public static final String CAMPO_AF10TIPOR = "Af10tipor";
		 public static final String CAMPO_COD_STATO = "CodStato";
		 public static final String CAMPO_DESC_ERR = "DescErr";
		 public static final String PG_LOAD_RICERCAREGEFILE	= IWebConstants.ROOT_DIR + "files/siap/regesies/regescarti/LoadRicercaRegeFile.jsp";
		 public static final String PG_LOAD_DETTAGLIOREGEFILE	= IWebConstants.ROOT_DIR + "files/siap/regesies/regescarti/DettaglioRegeFile.jsp";
		 public static final String PG_RICERCAREGEFILE	= IWebConstants.ROOT_DIR + "files/siap/regesies/regescarti/RicercaRegeFile.jsp";
		 public static final String PG_LOAD_INSERISCIREGEFILE	= IWebConstants.ROOT_DIR + "files/siap/regesies/regescarti/LoadInserisciRegeFile.jsp";
}