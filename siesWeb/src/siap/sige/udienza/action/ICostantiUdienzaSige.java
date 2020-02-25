package siap.sige.udienza.action;

import f3b.web.IWebConstants;

/**
* <p>Title: ICostantiUdienzaSige</p>
* <p>Description: Classe di costanti di UdienzaSige</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia </p>
* @version 1.0
*/
public interface ICostantiUdienzaSige {

	// ==========================================================================
	// Costanti utilizzate nelle jsp e che rappresentano i campi della tabella
	// ==========================================================================
	public static final String CAMPO_ID_UDIENZA_SIGE = "IdUdienzaSige";
	public static final String CAMPO_GIORNO_DATA_UDIENZA = "GiornoDataUdienza";
	public static final String CAMPO_MESE_DATA_UDIENZA = "MeseDataUdienza";
	public static final String CAMPO_ANNO_DATA_UDIENZA = "AnnoDataUdienza";
	public static final String CAMPO_COD_GIUDICE = "CodGiudice";
	public static final String CAMPO_COD_PROCURATORE = "CodProcuratore";
	public static final String CAMPO_COD_ID_ASSISTENTE = "CodIdAssistente";
	public static final String CAMPO_NUMERO_MAX_FASCICOLI = "NumeroMaxFascicoli";
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
	public static final String CAMPO_LUOGO_UDIENZA = "LuogoUdienza";
	public static final String CAMPO_COD_UFFICIO_APPARTENENZA = "CodUfficioAppartenenza";
	public static final String CAMPO_ORA_INIZIO = "OraInizio";
	public static final String CAMPO_MIN_INIZIO = "MinInizio";
	public static final String CAMPO_ORA_FINE = "OraFine";
	public static final String CAMPO_MIN_FINE = "MinFine";
	public static final String CAMPO_COD_SEZIONE_UDIENZA = "IdSezioneUdienza";
	public static final String CAMPO_COD_AULA_UDIENZA = "IdAulaUdienza";

	public static final String CAMPO_COD_LUOGO_DETENZIONE = "CodLuogoDetenzione";
	public static final String CAMPO_COD_IST_DETENZIONE = "CodIstDetenzione";
	public static final String CAMPO_PROCURA_GENERALE = "ProcuraGenerale";
	public static final String CAMPO_TIPONOTIFICA = "TipoNotifica";
	public static final String CAMPO_COD_AVVOCATO = "CodAvvocato";
	public static final String CODTIPONOTIFICACOMUNICAZIONE = "C";
	public static final String CODTIPONOTIFICA = "N";

	// =================================================================
	// Aggiungere qui eventuali altre costanti utilizzate nelle jsp e
	// nelle classi del progetto
	// =================================================================
	public static final String CAMPO_DATA_UDIENZA = "DataUdienza";
	public static final String CAMPO_NUMERO_UDIENZE_MAGRISTRATO = "NumeroUdienzeMagistrato";

	// ==========================================
	// Costanti che rappresentano le pagine jsp
	// ==========================================
	public static final String PG_LOAD_RICERCAUDIENZASIGE = IWebConstants.ROOT_DIR + "files/siap/sige/udienza/LoadRicercaUdienzaSige.jsp";
	public static final String PG_LOAD_DETTAGLIOUDIENZASIGE = IWebConstants.ROOT_DIR + "files/siap/sige/udienza/DettaglioUdienzaSige.jsp";
	public static final String PG_RICERCAUDIENZASIGE = IWebConstants.ROOT_DIR + "files/siap/sige/udienza/RicercaUdienzaSige.jsp";
	public static final String PG_LOAD_INSERISCIUDIENZASIGE = IWebConstants.ROOT_DIR + "files/siap/sige/udienza/LoadInserisciUdienzaSige.jsp";
	public static final String PG_LOAD_CANCELLAUDIENZASIGE = IWebConstants.ROOT_DIR + "files/siap/sige/udienza/DettaglioUdienzaSige.jsp";
	public static final String PG_LOAD_INSERISCIFISSAZIONEUDIENZA = IWebConstants.ROOT_DIR + "files/siap/sige/udienza/LoadInserisciFissazioneUdienza.jsp";
	public static final String PG_ELENCOUDIENZE = IWebConstants.ROOT_DIR + "files/siap/sige/udienza/ElencoUdienze.jsp";
	public static final String PG_LOAD_RICERCAUDIENZAXPROCEDIMENTI = IWebConstants.ROOT_DIR + "files/siap/sige/udienza/LoadRicercaUdienzePerProcedimento.jsp";
	public static final String PG_LOAD_DETTAGLIOFISSAZIONEUDIENZA = IWebConstants.ROOT_DIR + "files/siap/sige/udienza/DettaglioFissazioneUdienza.jsp";
	public static final String PG_BUTTONS = IWebConstants.ROOT_DIR + "files/siap/sige/udienza/BottoniFissUdienza.jsp";
	public static final String PG_LOAD_DESTINATARI = IWebConstants.ROOT_DIR + "files/siap/sige/udienza/Destinatari.jsp";
	// INTERVENTO PER 11.2.1 NUOVA GESTIONE UDIENZE MONOCRATICHE/COLLEGIALI
	public static final String STATO_FASCICOLO =  "ND"; // ND=NON DEFINITI
	public static final String FLAG_MODIF_BLOCCO =  "B"; // B=MODIFICATO IN BLOCCO DA FUNZIONI DI SUPPORTI
	public static final String CAMPO_COD_MAG_ASS = "codMagis";
	
	 // intervento per 11.2.1
	 public static final String CAMPO_ENTITA_LISTA_UDI = "ChiavelistaIdUdienze";
	 public static final String CAMPO_VALORE_LISTA_UDI = "ValorelistaIdUdienze";  


}