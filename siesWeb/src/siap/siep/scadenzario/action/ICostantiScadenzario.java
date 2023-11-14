package siap.siep.scadenzario.action;

import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ICostantiScadenzario
 * </p>
 * <p>
 * Description: Classe di costanti di Scadenzario
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
public interface ICostantiScadenzario {

	public static final String CAMPO_ID_SCADENZARIO = "IdScadenzario";
	public static final String CAMPO_COD_TIPO_SCADENZARIO = "CodTipoScadenzario";
	public static final String CAMPO_GIORNO_DATA_INIZIO_SCADENZA = "GiornoDataInizioScadenza";
	public static final String CAMPO_MESE_DATA_INIZIO_SCADENZA = "MeseDataInizioScadenza";
	public static final String CAMPO_ANNO_DATA_INIZIO_SCADENZA = "AnnoDataInizioScadenza";
	public static final String CAMPO_GIORNO_DATA_FINE_SCADENZA = "GiornoDataFineScadenza";
	public static final String CAMPO_MESE_DATA_FINE_SCADENZA = "MeseDataFineScadenza";
	public static final String CAMPO_ANNO_DATA_FINE_SCADENZA = "AnnoDataFineScadenza";

	public static final String CAMPO_ANNI_SCADENZA = "AnniScadenza";
	public static final String CAMPO_MESI_SCADENZA = "MesiScadenza";
	public static final String CAMPO_GIORNI_SCADENZA = "GiorniScadenza";
	public static final String CAMPO_TIPO_RICERCA = "TipoRicerca";

	public static final String CAMPO_FLAG_VISTO = "FlagVisto";
	public static final String CAMPO_GIORNO_DATA_VISTO = "GiornoDataVisto";
	public static final String CAMPO_MESE_DATA_VISTO = "MeseDataVisto";
	public static final String CAMPO_ANNO_DATA_VISTO = "AnnoDataVisto";
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
	public static final String CAMPO_EVE_ID_EVENTO = "EveIdEvento";

	public static final String PG_LOAD_RICERCASCADENZARIO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/LoadRicercaScadenzario.jsp";
	public static final String PG_LOAD_RICERCASCADENZARIODIFFERIMENTO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/LoadRicercaScadenzarioDifferimento.jsp";
	public static final String PG_LOAD_DETTAGLIOSCADENZARIO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/DettaglioScadenzario.jsp";
	public static final String PG_RICERCASCADENZARIO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/RicercaScadenzario.jsp";
	public static final String PG_LOAD_RICERCA_DECRETISOSP_IN_DEFINIZIONE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/LoadRicercaDecretiSospInDefinizione.jsp";
	public static final String PG_RICERCA_DECRETISOSP_IN_DEFINIZIONE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/RicercaDecretiSospInDefinizione.jsp";
	public static final String PG_RICERCASCADENZARIODIFFERIMENTO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/RicercaScadenzarioDifferimento.jsp";
	public static final String PG_LOAD_INSERISCISCADENZARIO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/LoadInserisciScadenzario.jsp";
	public static final String PG_LOAD_RICERCASCADENZARIOFINEPENA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/LoadRicercaScadenzarioFinePena.jsp";
	public static final String PG_RICERCASCADENZARIOFINEPENA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/RicercaScadenzarioFinePena.jsp";
	public static final String PG_LOAD_DETTAGLIOSCADENZARIOFINEPENA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/DettaglioScadenzarioFinePena.jsp";
	public static final String PG_LOAD_RICERCASCADENZARIOVANERICERCHE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/LoadRicercaScadenzarioVaneRicerche.jsp";
	public static final String PG_RICERCASCADENZARIOVANERICERCHE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/RicercaScadenzarioVaneRicerche.jsp";
	public static final String PG_LOAD_DETTAGLIOSCADENZARIOVANERICERCHE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/DettaglioScadenzarioVaneRicerche.jsp";

	public static final String PG_LOAD_RICERCASCADENZARIO_OTTEMPERAOBBLIGHI = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/LoadRicercaScadenzarioOttemperaObblighi.jsp";
	public static final String PG_RICERCASCADENZARIO_OTTEMPERAOBBLIGHI = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/RicercaScadenzarioOttemperaObblighi.jsp";
	public static final String PG_LOAD_DETTAGLIOSCADENZARIO_OTTEMPERAOBBLIGHI = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/DettaglioScadenzarioOttemperaObblighi.jsp";

	public static final String PG_LOAD_RICERCASCADENZARIO_SOSPECONDIZIO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/LoadRicercaScadenzarioSospeCondizio.jsp";
	public static final String PG_RICERCASCADENZARIO_SOSPECONDIZIO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/RicercaScadenzarioSospeCondizio.jsp";
	public static final String PG_LOAD_DETTAGLIOSCADENZARIO_SOSPECONDIZIO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/DettaglioScadenzarioSospeCondizio.jsp";

	public static final String PG_LOAD_RICERCA_SCADE_DET_DOM_SPE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/LoadRicercaScadeDetDomSpe.jsp";
	public static final String PG_RICERCA_SCADE_DET_DOM_SPE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/RicercaScadeDetDomSpe.jsp";
	public static final String PG_LOAD_DETTAGLIO_SCADE_DET_DOM_SPE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/DettaglioScadeDetDomSpe.jsp";
	// AMBROSINO
	public static final String PG_RICERCASCADENZARIO_RINNOVO_VANERICERCHE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/RicercaScadenzarioRinnovoVerbaleVaneRicerche.jsp";
	public static final String PG_LOAD_RICERCASCADENZARIO_RINNOVO_VANERICERCHE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/LoadRicercaScadenzarioRinnovoVerbaleVaneRicerche.jsp";
	// Decreto Legge 78/2013
	public static final String PG_LOAD_RICERCA_PROCEDIMENTI_TRASMESSI_L78_2013 = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/LoadRicercaProcTrasmessiL78del2013.jsp";
	public static final String PG_RICERCA_PROCEDIMENTI_TRASMESSI_L78_2013 = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/RicercaProcedimentiTrasmessiL78del2013.jsp";
	// Misure Sicurezza
	public static final String PG_LOAD_RICERCASCADENZARIOFINEPENA_MS = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/LoadRicercaScadenzarioFinePenaMisureSicurezza.jsp";
	public static final String PG_RICERCASCADENZARIOFINEPENA_MS = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/RicercaScadenzarioFinePenaMisureSicurezza.jsp";
	public static final String PG_LOAD_DETTAGLIOSCADENZARIOFINEPENA_MS = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/DettaglioScadenzarioFinePenaMisureSicurezza.jsp";

	/*
	 * ISSUE MEV : aggiunte costanti Numero MEV : 39 Autore : Gioggi Data : 24/mar/2017 Branch : MEV_39
	 */
	public static final String PG_LOAD_RICERCASCADENZARIODIFFERIMENTO_MS = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/LoadRicercaScadenzarioDifferimentoMisureSicurezza.jsp";
	public static final String PG_RICERCASCADENZARIODIFFERIMENTO_MS = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/RicercaScadenzarioDifferimentoMisureSicurezza.jsp";
	public static final String PG_LOAD_DETTAGLIOSCADENZARIODIFFERIMENTO_MS = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/DettaglioScadenzarioDifferimentoMisureSicurezza.jsp";
	public static final String PG_LOAD_AGGIORNASCADENZA_MS = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/LoadAggiornaScadenzaMisuraSicurezza.jsp";
	// ***** FINE INTERVENTO MEV_39 *****//

	// MEV_2023-33
	 public static final String PG_LOAD_RICERCA_SCADENZARI_PP = IWebConstants.ROOT_DIR
       + "/files/siap/siep/scadenzario/LoadRicercaScadenzarioPP.jsp";
   public static final String PG_RICERCA_SCADENZARI_PP = IWebConstants.ROOT_DIR
       + "/files/siap/siep/scadenzario/RicercaScadenzarioPP.jsp";
	 // MEV_2023-33 - FINE
}