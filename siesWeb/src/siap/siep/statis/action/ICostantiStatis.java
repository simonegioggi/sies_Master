package siap.siep.statis.action;

import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ICostantiStatoFascicoloRes
 * </p>
 * <p>
 * Description: Classe di costanti di StatoFascicoloRes
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public interface ICostantiStatis {

	public static final String PG_LOAD_CREARIEPILOGO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/statis/LoadCreaRiepilogo.jsp";
	public static final String PG_LOAD_ATTIVITAMAGISTRATI = IWebConstants.ROOT_DIR
			+ "files/siap/siep/statis/LoadAttivitaMagistrati.jsp";
	public static final String PG_LOAD_TEMPIISCRIZIONI = IWebConstants.ROOT_DIR
			+ "files/siap/siep/statis/LoadTempiIscrizioni.jsp";
	public static final String PG_LOAD_TEMPIEMISSIONE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/statis/LoadTempiEmissione.jsp";
	public static final String PG_ATTESA = IWebConstants.ROOT_DIR + "files/siap/siep/statis/Attesa.jsp";
	public static final String PG_LOAD_SELEZIONA_UFFICIO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/statis/LoadSelezionaUfficio.jsp";

	// MEV 27
	public static final String PG_LOAD_GRIGLIA_ESTRAZIONE_DATI_STATISTICHE_UFFICIO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/statis/GrigliaBottoniEstrazioneDatiStatisticheUfficio.jsp";
	public static final String PG_LOAD_STAT_RIEPILOGO_ISCRIZIONI_E_ATTIVITA_CPP = IWebConstants.ROOT_DIR
			+ "files/siap/siep/statis/LoadStatisticaRiepilogoIscrizionieAttivitaCPP.jsp";
	public static final String PG_LOAD_STAT_TEMPI_ISCRIZIONI_FASCICOLI_CPP = IWebConstants.ROOT_DIR
			+ "files/siap/siep/statis/LoadStatisticaTempiIscrizioneFascicoliCPP.jsp";
	public static final String PG_LOAD_PRE_RIEPILOGO_PENDENTI_CPP = IWebConstants.ROOT_DIR
			+ "files/siap/siep/statis/LoadPreRiepilogoCPP.jsp";
	public static final String PG_LOAD_CREARIEPILOGO_CPP = IWebConstants.ROOT_DIR
			+ "files/siap/siep/statis/LoadCreaRiepilogo_CPP.jsp";

	public static final String PG_LOAD_STAT_RIEPILOGO_PENDENTI_CPP = IWebConstants.ROOT_DIR
			+ "files/siap/siep/statis/LoadStatisticaRiepilogoPendentiCPP.jsp";

	public static final String CAMPO_SOLO_ANNO_INIZIALE = "SoloAnnoIniziale";
	public static final String CAMPO_SOLO_ANNO_FINALE = "SoloAnnoFinale";
	public static final String CAMPO_ANNO_SEMESTRE = "AnnoSemestre";
	public static final String CAMPO_ANNO_TRIMESTRE = "AnnoTrimestre";
	//

	public static final String CAMPO_COD_STATO_FASCICOLO = "CodStatoFascicolo";
	public static final String CAMPO_DESCRIZIONE = "Descrizione";
	// MEV_39: modificato valore (ex "Descrizione")
	public static final String CAMPO_LISTA_STATI = "ListaStati";

	public static final String CAMPO_GIORNO_INIZIALE = "GiornoIniziale";
	public static final String CAMPO_MESE_INIZIALE = "MeseIniziale";
	public static final String CAMPO_ANNO_INIZIALE = "AnnoIniziale";
	public static final String CAMPO_GIORNO_FINALE = "GiornoFinale";
	public static final String CAMPO_MESE_FINALE = "MeseFinale";
	public static final String CAMPO_ANNO_FINALE = "AnnoFinale";
	public static final String CAMPO_LISTA_MAGISTRATI = "ComboMagistrati";

	public static final String CAMPO_LISTA_DISTINTE = "ComboDistinte";
	public static final String CAMPO_LISTA_INTERVALLI = "ComboIntervalli";

	public static final String COD_ARCHIVIAZIONI = "131";
	public static final String COD_ALTRE_POSIZIONI = "129";

	// public static final String COD_TRASN_UDS = "172";

	public static final String COD_TITOLO1_ARCHIVIAZIONI = "1020";
	public static final String COD_TITOLO1_ALTRE_POSIZIONI = "1021";

	public static final String CAMPO_COD_ACCORPATO_1 = "codiceUffaccorpato1";

	public static final String TIPO_CAMPO_TITOLO1 = "T1";
	public static final String TIPO_CAMPO_TITOLO2 = "T2";
	public static final String TIPO_CAMPO_VOCE = "V0";

	// MEV_39: aggiunta costante x pag jsp
	public static final String PG_LOAD_MOVIMENTO_PROCEDIMENTI = IWebConstants.ROOT_DIR
			+ "files/siap/siep/statis/LoadMovimentoProcedimenti.jsp";
	public static final String PG_LOAD_ATTIVITA_MAGISTRATI = IWebConstants.ROOT_DIR
			+ "files/siap/siep/statis/LoadAttivitaMagistratiMS.jsp";
	
	public static final String COD_ARCHIVIAZIONI_MS = "18";
	public static final String COD_ALTRE_POSIZIONI_MS = "19";
	public static final String COD_TITOLO1_ARCHIVIAZIONI_MS = "1023";
	public static final String COD_TITOLO1_ALTRE_POSIZIONI_MS = "1025";

}