package siap.sige.udienzamonocratica.action;

/**
* <p>Title: ICostantiUdienzaMonocraticaSige</p>
* <p>Description: Classe di costanti di UdienzaMonocraticaSige</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
import f3b.web.IWebConstants;

public interface ICostantiUdienzaMonocraticaSige {

	// ==========================================================================
	// Costanti utilizzate nelle jsp e che rappresentano i campi della tabella
	// ==========================================================================
	// public static final String CAMPO_ID_UDIENZA_MONOCRATICA_SIGE = "IdUdienzaMonocraticaSige";
	// public static final String CAMPO_UDI_ID_UDIENZA_SIGE = "UdiIdUdienzaSige";
	// public static final String CAMPO_COD_GIUDICE = "CodGiudice";
	// public static final String CAMPO_COD_UFFICIO_APPARTENENZA = "CodUfficioAppartenenza";
	// public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
	// public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
	// public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
	// public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
	// public static final String CAMPO_COD_UFFICIO_INSERIMENTO = "CodUfficioInserimento";
	// public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento";
	// public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento";
	// public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento";
	// public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento";
	// public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO = "CodUfficioAggiornamento";

	// =================================================================
	// Aggiungere qui eventuali altre costanti utilizzate nelle jsp e
	// nelle classi del progetto
	// =================================================================

	// ==========================================
	// Costanti che rappresentano le pagine jsp
	// ==========================================
	public static final String PG_LOAD_RICERCAUDIENZAMONOCRATICASIGE = IWebConstants.ROOT_DIR + "files/siap/sige/udienzamonocratica/LoadRicercaUdienzaMonocraticaSige.jsp";
	public static final String PG_LOAD_DETTAGLIOUDIENZAMONOCRATICASIGE = IWebConstants.ROOT_DIR + "files/siap/sige/udienzamonocratica/DettaglioUdienzaMonocraticaSige.jsp";
	public static final String PG_RICERCAUDIENZAMONOCRATICASIGE = IWebConstants.ROOT_DIR + "files/siap/sige/udienzamonocratica/RicercaUdienzaMonocraticaSige.jsp";
	public static final String PG_LOAD_INSERISCIUDIENZAMONOCRATICASIGE = IWebConstants.ROOT_DIR + "files/siap/sige/udienzamonocratica/LoadInserisciUdienzaMonocraticaSige.jsp";
	public static final String PG_LOAD_CANCELLAUDIENZAMONOCRATICASIGE = IWebConstants.ROOT_DIR + "files/siap/sige/udienzamonocratica/DettaglioUdienzaMonocraticaSige.jsp";

	public static final String PG_LOAD_INSERISCIUDIENZAMONOCRATICASIGE_FIX = IWebConstants.ROOT_DIR + "files/siap/sige/udienzamonocratica/LoadInserisciUdienzaMonocraticaSigeFix.jsp";
	public static final String PG_LOAD_DETTAGLIOUDIENZAMONOCRATICASIGE_FIX = IWebConstants.ROOT_DIR + "files/siap/sige/udienzamonocratica/DettaglioUdienzaMonocraticaSigeFix.jsp";
	
	// INTERVENTO  PER NUOVA GESTIONE UDIENZE MONOCRATICHE/COLLEGIALI VERS. 11.2.1
	public static final String PG_LOAD_DETTAGLIOUDIENZAPROCEDMONOCRATICASIGE = IWebConstants.ROOT_DIR + "files/siap/sige/udienzamonocratica/DettaglioUdienzaMonocraticaProcedimentoSige.jsp";


}