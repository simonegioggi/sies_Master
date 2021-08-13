package siap.siep.avvocato.action;

/**
* <p>Title: ICostantiAvvocato</p>
* <p>Description: Classe di costanti di Avvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.web.IWebConstants;

public interface ICostantiAvvocato {

	public static final String CAMPO_ID_AVVOCATO = "IdAvvocato";
	public static final String CAMPO_COGNOME = "CognomeAvvocato";
	public static final String CAMPO_NOME = "NomeAvvocato";
	public static final String CAMPO_COD_TIPO = "CodTipo";
	public static final String CAMPO_FORO = "Foro";
	public static final String CAMPO_INDIRIZZO = "Indirizzo";
	public static final String CAMPO_TELEFONO = "Telefono";
	public static final String CAMPO_FAX = "Fax";
	public static final String CAMPO_E_MAIL = "EMail";
	public static final String CAMPO_NOTE = "Note";
	public static final String CAMPO_CODICE_FISCALE = "CodiceFiscale";
	public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
	public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
	public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
	public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
	public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento";
	public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento";
	public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento";
	public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento";
	public static final String CAMPO_COD_COMUNE_RESIDENZA = "CodComuneResidenza";
	public static final String CAMPO_COD_LUOGO_NASCITA = "CodLuogoNascita";
	public static final String CAMPO_GIORNO_DATA_NASCITA = "GiornoDataNascita";
	public static final String CAMPO_MESE_DATA_NASCITA = "MeseDataNascita";
	public static final String CAMPO_ANNO_DATA_NASCITA = "AnnoDataNascita";
	public static final String CAMPO_GIORNO_DATA_SOSPENSIONE = "GiornoDataSospensione";
	public static final String CAMPO_MESE_DATA_SOSPENSIONE = "MeseDataSospensione";
	public static final String CAMPO_ANNO_DATA_SOSPENSIONE = "AnnoDataSospensione";
	public static final String CAMPO_GIORNO_DATA_RADIAZIONE = "GiornoDataRadiazione";
	public static final String CAMPO_MESE_DATA_RADIAZIONE = "MeseDataRadiazione";
	public static final String CAMPO_ANNO_DATA_RADIAZIONE = "AnnoDataRadiazione";
	public static final String CAMPO_COD_NON_ATTIVITA = "CodNonAttivita";
	public static final String CAMPO_COD_UFFICIO_APPARTENENZA = "CodUfficioAppartenenza";
	public static final String CAMPO_GIORNO_DATA_DESIGNAZIONE = "GiornoDataDesignazione";
	public static final String CAMPO_MESE_DATA_DESIGNAZIONE = "MeseDataDesignazione";
	public static final String CAMPO_ANNO_DATA_DESIGNAZIONE = "AnnoDataDesignazione";
	public static final String CAMPO_GIORNO_DATA_NOMINA = "GiornoDataNomina";
	public static final String CAMPO_MESE_DATA_NOMINA = "MeseDataNomina";
	public static final String CAMPO_ANNO_DATA_NOMINA = "AnnoDataNomina";
	public static final String CAMPO_COD_MOTIVO_DESIGNAZIONE = "CodMotivoDesignazione";
	public static final String CAMPO_INDIRIZZO_TIPO_AUTORITA = "IndirizzoAutorita";
	public static final String CAMPO_COD_TIPO_AUTORITA_DIF = "CodTipoAutoritaDif";
	public static final String CAMPO_COD_SEDE_AUTORITA_DIF = "CodTipoSedeAutoritaDif";

	public static final String PG_LOAD_RICERCAAVVOCATO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/LoadRicercaAvvocato.jsp";
	public static final String PG_RICERCAAVVOCATO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/RicercaAvvocato.jsp";
	public static final String PG_LOAD_INSERISCIAVVOCATO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/LoadInserisciAvvocato.jsp";
	public static final String PG_DETTAGLIO_AVVOCATO_SENTENZA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/DettaglioAvvocatoSentenza.jsp";
	public static final String PG_DETTAGLIO_AVVOCATO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/DettaglioAvvocato.jsp";
	public static final String PG_DETTAGLIO_AVVOCATO_POPUP = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/DettaglioAvvocatoPopUp.jsp";
	public static final String PG_RICERCAAVVOCATOBREVE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/RicercaAvvocatoBreve.jsp";
	public static final String PG_LOAD_RICERCAAVVOCATOBREVE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/LoadRicercaAvvocatoBreve.jsp";
	public static final String PG_INSERISCI_DIFENSORE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/LoadInserisciDifensore.jsp";
	public static final String PG_INSERISCI_DIFENSORE_POPUP = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/LoadInserisciDifensorePopUp.jsp";
	public static final String PG_RICERCA_DIFENSORE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/LoadRicercaDifensore.jsp";
	public static final String PG_LISTA_DIFENSORI = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/ListaDifensori.jsp";
	public static final String PG_DIFENSORE_DA_MODIFICARE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/ModificaDifensore.jsp";
	public static final String PG_MODIFICA_DIFENSORE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/LoadModificaDifensore.jsp";
	public static final String PG_CANCELLA_RICERCA_DIFENSORE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/LoadRicercaCancellaDifensore.jsp";
	public static final String PG_DIFENSORE_DA_CANCELLARE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/CancellaDifensore.jsp";
	public static final String PG_ELENCO_AVVOCATI = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/ElencoAvvocati.jsp";
	public static final String PG_ELENCO_STORICO_AVVOCATI = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/ElencoStoricoAvvocati.jsp";
	public static final String PG_SOSTITUZIONE_AVVOCATO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/SostituzioneDifensore.jsp";
	public static final String PG_ASSEGNA_INSERISCI_AVVOCATO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/AssegnazioneElencoAvvocati.jsp";
	public static final String PG_ASSEGNA_INSERISCI_DIFENSORE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/InserimentoAssegnazioneDifensore.jsp";
	public static final String PG_DETTAGLIO_AVVOCATO_AVVOCATO_FASCICOLO_SIEP = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/DettaglioAvvocatoAvvocatoFascicoloSiep.jsp";
	public static final String PG_FILTRAAVV = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/FiltraAvv.jsp";
	public static final String PG_BUTTONS = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/buttonsAvvocato.jsp";
	public static final String PG_LOAD_LISTA_AVVOCATO_POP_UP = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/LoadListaAvvocatoPopup.jsp";
	public static final String PG_FILTRA_LISTA_AVV_POP_UP = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/FiltraListaAvvocatiPopup.jsp";
	public static final String PG_RICERCA_LISTA_AVVOCATO_POP_UP = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/ListaAvvocatoPopup.jsp";
	// MEV_21: aggiunte costanti e pagine per chiamata a WS per individuare lista avvocato in RegInde
	public static final String CAMPO_PEC = "Pec";
	public static final String CAMPO_FLAG_TUTTI_FORI = "FlagTuttiFori";
	public static final String PG_LOAD_RICERCA_AVVOCATO_REGINDE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/LoadRicercaAvvocatoRegInde.jsp";
	public static final String PG_RICERCA_AVVOCATO_REGINDE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/RicercaAvvocatoRegInde.jsp";
	public static final String PG_FILTRA_AVV_REGINDE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/FiltraAvvRegInde.jsp";
	public static final String CAMPO_DESC_COMUNE_NASCITA_REGINDE = "DescComuneNascitaEstero";
	public static final String CAMPO_DESC_COMUNE_STUDIO = "DescComuneStudio";
	public static final String CAMPO_DESC_STATO_NASCITA = "DescStatoNascita";
	public static final String CAMPO_COD_STATO_NASCITA = "CodStatoNascita";
	// 20210809 MEV_21
	public static final String PG_LOAD_RICERCA_INSAVV_REGINDE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/LoadRicercaInsAvvRegInde.jsp";
	public static final String PG_RICERCA_INSAVV_REGINDE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/RicercaInsAvvRegInde.jsp";
	public static final String PG_FILTRA_INS_AVV_REGINDE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/FiltraInsAvvRegInde.jsp";
	public static final String CAMPO_ID_AVVOCATO_P = "IdAvvocatoP";
	public static final String CAMPO_COGNOME_P = "CognomeAvvocatoP";
	public static final String CAMPO_NOME_P = "NomeAvvocatoP";
	public static final String CAMPO_COD_LUOGO_NASCITA_P = "CodLuogoNascitaP";
	public static final String CAMPO_GIORNO_DATA_NASCITA_P = "GiornoDataNascitaP";
	public static final String CAMPO_MESE_DATA_NASCITA_P = "MeseDataNascitaP";
	public static final String CAMPO_ANNO_DATA_NASCITA_P = "AnnoDataNascitaP";
	public static final String CAMPO_COD_STATO_NASCITA_P = "CodStatoNascitaP";
	public static final String CAMPO_DESC_COMUNE_NASCITA_REGINDE_P = "DescComuneNascitaEsteroP";
	public static final String CAMPO_FORO_P = "ForoP";
	public static final String CAMPO_INDIRIZZO_P = "IndirizzoP";
	public static final String CAMPO_DESC_COMUNE_STUDIO_P = "DescComuneStudioP";
	public static final String CAMPO_TELEFONO_P = "TelefonoP";
	public static final String CAMPO_FAX_P = "FaxP";
	public static final String CAMPO_E_MAIL_P = "EMailP";
	public static final String CAMPO_PEC_P = "PecP";
	public static final String CAMPO_NOTE_P = "NoteP";
	public static final String CAMPO_CODICE_FISCALE_P = "CodiceFiscaleP";
	public static final String CAMPO_COD_NON_ATTIVITA_P = "CodNonAttivitaP";
	public static final String PG_RICERCA_INS_AVVOCATO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/avvocato/RicercaInsAvvocato.jsp";

}