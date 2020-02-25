package siap.siep.posizione.action;

/**
* <p>Title: ICostantiPosizioneGiuridica</p>
* <p>Description: Classe di costanti di PosizioneGiuridica</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.web.IWebConstants;

public interface ICostantiPosizioneGiuridica {
	public static final String CAMPO_ID_POSIZIONE_GIURIDICA = "IdPosizioneGiuridica";
	public static final String CAMPO_COD_POSIZIONE_GIURIDICA = "CodPosizioneGiuridica";
	public static final String CAMPO_GIORNO_DATA_INIZIO = "GiornoDataInizio";
	public static final String CAMPO_MESE_DATA_INIZIO = "MeseDataInizio";
	public static final String CAMPO_ANNO_DATA_INIZIO = "AnnoDataInizio";
	public static final String CAMPO_GIORNO_DATA_FINE = "GiornoDataFine";
	public static final String CAMPO_MESE_DATA_FINE = "MeseDataFine";
	public static final String CAMPO_ANNO_DATA_FINE = "AnnoDataFine";
	public static final String CAMPO_COD_LUOGO_PENA = "CodLuogoPena";
	public static final String CAMPO_COD_POSIZIONE_PROCESSUALE = "CodPosizioneProcessuale";
	public static final String CAMPO_NOTE = "Note";
	public static final String CAMPO_LUOGO_PROVA_AFFIDAMENTO = "LuogoProvaAffidamento";
	public static final String CAMPO_LUOGO_LAVORO_SEMILIBERTA = "LuogoLavoroSemiliberta";
	public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
	public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
	public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
	public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
	public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento";
	public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento";
	public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento";
	public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento";
	public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP = "FasSieIdFascicoloSiep";
	public static final String CAMPO_COD_TIPO_ISTITUTO = "CodTipoIstituto";
	public static final String CAMPO_INDIRIZZO_LUOGO_PENA = "IndirizzoLuogoPena";
	public static final String CAMPO_DESCR_ISTITUTO = "DescrIstituto";
	public static final String CAMPO_ID_EVENTO_RIFERIMENTO = "IdEventoRiferimento";

	public static final String CAMPO_LUOGO_ESPIAZIONE = "LuogoEspiazione";
	public static final String CAMPO_AUTORITA_COMPETENTE = "AutoritaCompetente";
	public static final String CAMPO_AUTORITA_COMPETENTE_SEDE = "AutoritaCompetenteSede";
	public static final String CAMPO_AUTORITA_COMPETENTE_INDIRIZZO = "AutoritaCompetenteIndirizzo";
	public static final String CAMPO_COD_MASCHERA = "CodMaschera";

	public static final String CAMPO_TIPO_MASCHERA_Libero = "L";
	public static final String CAMPO_TIPO_MASCHERA_LiberoIstituto = "L1";
	public static final String CAMPO_TIPO_MASCHERA_LiberoCautelareIstituto = "L2";
	public static final String CAMPO_TIPO_MASCHERA_LiberoCautelareAltro = "L3";
	public static final String CAMPO_TIPO_MASCHERA_EspiazioneIstituto = "EI";
	public static final String CAMPO_TIPO_MASCHERA_EspiazioneAltro = "EA";

	// public static final String CAMPO_FLAG_PRIMA_POSIZIONE = "FlagPrimaPosizione";

	public static final String PG_LOAD_RICERCAPOSIZIONEGIURIDICA = IWebConstants.ROOT_DIR + "files/siap/siep/posizione/LoadRicercaPosizioneGiuridica.jsp";
	public static final String PG_RICERCAPOSIZIONEGIURIDICA = IWebConstants.ROOT_DIR + "files/siap/siep/posizione/RicercaPosizioneGiuridica.jsp";
	public static final String PG_LOAD_INSERISCIPOSIZIONEGIURIDICA = IWebConstants.ROOT_DIR + "files/siap/siep/posizione/LoadInserisciPosizioneGiuridica.jsp";
	public static final String PG_LOAD_DETTAGLIOPOSIZIONEGIURIDICA = IWebConstants.ROOT_DIR + "files/siap/siep/posizione/DettaglioPosizioneGiuridica.jsp";
}