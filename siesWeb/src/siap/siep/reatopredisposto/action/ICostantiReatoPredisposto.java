package siap.siep.reatopredisposto.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiReatoPredisposto</p>
* <p>Description: Classe di costanti di ReatoPredisposto</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiReatoPredisposto
{		
	     public static final String CAMPO_ID_REATO_PREDISPOSTO = "IdReatoPredisposto";
	     public static final String CAMPO_PROGR_NORMA = "ProgrNorma";
		 public static final String CAMPO_NOME_ELEMENTO = "NomeElemento"; 
		 public static final String CAMPO_COD_FONTE = "CodFonte"; 
		 public static final String CAMPO_ANNO_FONTE = "AnnoFonte"; 
		 public static final String CAMPO_NUMERO_FONTE = "NumeroFonte"; 
		 public static final String CAMPO_COD_SOTTONUMERAZIONE = "CodSottonumerazione"; 
		 public static final String CAMPO_COMMA = "Comma"; 
		 public static final String CAMPO_LETTERA = "Lettera"; 
		 public static final String CAMPO_NUMERO = "Numero"; 
		 public static final String CAMPO_ARTICOLO = "Articolo"; 
		 public static final String CAMPO_NOTE_ELEMENTO = "NoteElemento"; 
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
		 public static final String PG_LOAD_RICERCAREATOPREDISPOSTO	= IWebConstants.ROOT_DIR + "files/siap/siep/reatopredisposto/LoadRicercaReatoPredisposto.jsp";
		 public static final String PG_LOAD_DETTAGLIOREATOPREDISPOSTO	= IWebConstants.ROOT_DIR + "files/siap/siep/reatopredisposto/DettaglioReatoPredisposto.jsp";
		 public static final String PG_RICERCAREATOPREDISPOSTO	= IWebConstants.ROOT_DIR + "files/siap/siep/reatopredisposto/RicercaReatoPredisposto.jsp";
		 public static final String PG_LOAD_INSERISCIREATOPREDISPOSTO	= IWebConstants.ROOT_DIR + "files/siap/siep/reatopredisposto/LoadInserisciReatoPredisposto.jsp";
		 public static final String PG_LOAD_MODIFICAREATOPREDISPOSTO	= IWebConstants.ROOT_DIR + "files/siap/siep/reatopredisposto/LoadInserisciReatoPredisposto.jsp";
		 public static final String PG_LOAD_POPUPREATIPREDISPOSTI	= IWebConstants.ROOT_DIR + "files/siap/siep/reatopredisposto/LoadPopupReatiPredisposti.jsp";
		 public static final String SEP_NORME = "|@";
		 public static final String SEP_CAMPI = "#~";
}