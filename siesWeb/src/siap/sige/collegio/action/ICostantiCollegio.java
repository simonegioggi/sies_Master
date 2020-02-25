package siap.sige.collegio.action;

import f3b.web.IWebConstants;

/**
* <p>Title: ICostantiCollegio</p>
* <p>Description: Classe costanti di Collegio</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia S.p.A.</p>
* @version 1.0
*/
public interface ICostantiCollegio {

	public static final int NUM_GIUDICI_POPOLARI = 8;

	public static final String CAMPO_ID_COLLEGIO = "IdCollegio";
	public static final String CAMPO_COD_COLLEGIO = "CodCollegio";
	public static final String CAMPO_SEZ_ID_SEZIONE = "SezIdSezione";
	public static final String CAMPO_COD_UFFICIO_APPARTENENZA = "CodUfficioAppartenenza";
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
	public static final String CAMPO_GIORNO_DATA_INIZIO_VALIDITA = "GiornoDataInizioValidita";
	public static final String CAMPO_MESE_DATA_INIZIO_VALIDITA = "MeseDataInizioValidita";
	public static final String CAMPO_ANNO_DATA_INIZIO_VALIDITA = "AnnoDataInizioValidita";
	public static final String CAMPO_GIORNO_DATA_FINE_VALIDITA = "GiornoDataFineValidita";
	public static final String CAMPO_MESE_DATA_FINE_VALIDITA = "MeseDataFineValidita";
	public static final String CAMPO_ANNO_DATA_FINE_VALIDITA = "AnnoDataFineValidita";
	public static final String FORM_DEF_COLLEGIO = "FormDefCollegio";
	
	// intervento per 11.2.1
	public static final String CAMPO_GIORNO_ISCRIZIONE_INIZIALE = "GiornoIscrizioneIniziale";
	public static final String CAMPO_MESE_ISCRIZIONE_INIZIALE = "MeseIscrizioneIniziale";
	public static final String CAMPO_ANNO_ISCRIZIONE_INIZIALE = "AnnoIscrizioneIniziale";
	public static final String CAMPO_GIORNO_ISCRIZIONE_FINALE = "GiornoIscrizioneFinale";
	public static final String CAMPO_MESE_ISCRIZIONE_FINALE = "MeseIscrizioneFinale";
	public static final String CAMPO_ANNO_ISCRIZIONE_FINALE = "AnnoIscrizioneFinale";
	

	public static final String PG_LOAD_RICERCACOLLEGIO = IWebConstants.ROOT_DIR + "files/siap/sige/collegio/LoadRicercaCollegio.jsp";
	public static final String PG_LOAD_DETTAGLIOCOLLEGIO = IWebConstants.ROOT_DIR + "files/siap/sige/collegio/DettaglioCollegio.jsp";
	public static final String PG_RICERCACOLLEGIO = IWebConstants.ROOT_DIR + "files/siap/sige/collegio/RicercaCollegio.jsp";
	// public static final String PG_LOAD_INSERISCICOLLEGIO =
	// IWebConstants.ROOT_DIR + "files/siap/sige/collegio/LoadInserisciCollegio.jsp";
	public static final String PG_LOAD_RICERCA_COLLEGIO_LISTA = IWebConstants.ROOT_DIR + "files/siap/sige/collegio/LoadRicercaCollegioLista.jsp";
	public static final String PG_RICERCA_COLLEGIO_LISTA = IWebConstants.ROOT_DIR + "files/siap/sige/collegio/RicercaCollegioLista.jsp";
	public static final String PG_RICERCA_MAGISTRATI_LISTA = IWebConstants.ROOT_DIR + "files/siap/sige/collegio/RicercaMagistratiLista.jsp";
	public static final String PG_FILTRA_COLLEGIO_LISTA = IWebConstants.ROOT_DIR + "files/siap/sige/collegio/FiltraCollegioLista.jsp";

	public static final String PG_LOAD_INSERISCICOLLEGIOCAP = IWebConstants.ROOT_DIR + "files/siap/sige/collegio/LoadInserisciCollegioCAP.jsp";
	public static final String PG_LOAD_INSERISCICOLLEGIOCAS = IWebConstants.ROOT_DIR + "files/siap/sige/collegio/LoadInserisciCollegioCAS.jsp";
	public static final String PG_LOAD_INSERISCICOLLEGIOCASAP = IWebConstants.ROOT_DIR + "files/siap/sige/collegio/LoadInserisciCollegioCASAP.jsp";
	public static final String PG_LOAD_INSERISCICOLLEGIODIB = IWebConstants.ROOT_DIR + "files/siap/sige/collegio/LoadInserisciCollegioDIB.jsp";
	public static final String PG_LOAD_INSERISCICOLLEGIODIBM = IWebConstants.ROOT_DIR + "files/siap/sige/collegio/LoadInserisciCollegioDIBM.jsp";
	public static final String PG_LOAD_INSERISCICOLLEGIOCAPSM = IWebConstants.ROOT_DIR + "files/siap/sige/collegio/LoadInserisciCollegioCAPSM.jsp";
}