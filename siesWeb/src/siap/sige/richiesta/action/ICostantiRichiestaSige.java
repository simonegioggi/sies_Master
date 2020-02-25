package siap.sige.richiesta.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiRichiestaSige</p>
* <p>Description: Classe di costanti di RichiestaSige</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiRichiestaSige
{
		 public static final String CAMPO_ID_RICHIESTA_SIGE = "IdRichiestaSige"; 
		 public static final String CAMPO_COD_TIPO_ATTO = "CodTipoRichAtto"; 
		 public static final String CAMPO_COD_TIPO_RICHIEDENTE = "CodTipoRichiedente"; 
		 public static final 	String	CAMPO_COD_SEDE_RICHIEDENTE = "CodSedeRichiedente"; 
		 public static final 	String	CAMPO_SEDE_RICHIEDENTE = "SedeRichiedente"; 
		 public static final 	String	CAMPO_DESC_SEDE_RICHIEDENTE = "DescSedeRichiedente"; 
		 public static final 	String	CAMPO_DESC_RICHIEDENTE = "DesceRichiedente"; 
		 public static final 	String	CAMPO_COD_UFFICIO_RICHIEDENTE = "CodUfficioRichiedente"; 
		 public static final String CAMPO_GIORNO_DATA_EMISSIONE = "GiornoDataEmissione"; 
		 public static final String CAMPO_MESE_DATA_EMISSIONE = "MeseDataEmissione"; 
		 public static final String CAMPO_ANNO_DATA_EMISSIONE = "AnnoDataEmissione"; 
		 public static final String CAMPO_GIORNO_DATA_DEPOSITO = "GiornoDataDeposito"; 
		 public static final String CAMPO_MESE_DATA_DEPOSITO = "MeseDataDeposito"; 
		 public static final String CAMPO_ANNO_DATA_DEPOSITO = "AnnoDataDeposito"; 
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
		 public static final String CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA = "GiornoDataArrivoCancelleria"; 
		 public static final String CAMPO_MESE_DATA_ARRIVO_CANCELLERIA = "MeseDataArrivoCancelleria"; 
		 public static final String CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA = "AnnoDataArrivoCancelleria"; 
		 public static final String PG_LOAD_RICERCARICHIESTASIGE	= IWebConstants.ROOT_DIR + "files/siap/sige/richiesta/LoadRicercaRichiestaSige.jsp";
		 public static final String PG_LOAD_DETTAGLIORICHIESTASIGE	= IWebConstants.ROOT_DIR + "files/siap/sige/richiesta/LoadRicercaRichiestaSige.jsp";
		 public static final String PG_RICERCARICHIESTASIGE	= IWebConstants.ROOT_DIR + "files/siap/sige/richiesta/RicercaRichiestaSige.jsp";
		 public static final String PG_LOAD_INSERISCIRICHIESTASIGE	= IWebConstants.ROOT_DIR + "files/siap/sige/richiesta/LoadInserisciRichiestaSige.jsp";
}