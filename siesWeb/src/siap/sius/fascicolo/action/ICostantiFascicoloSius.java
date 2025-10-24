package siap.sius.fascicolo.action;

import f3b.web.IWebConstants;

/**
 * ICostantiFascicoloSius - Classe di costanti di FascicoloSius
 *
 * @version 1.0
 */
public interface ICostantiFascicoloSius {

	public static final String CAMPO_ID_FASCICOLO_SIUS = "IdFascicoloSius";
	public static final String CAMPO_CHIAVE_ANNO = "ChiaveAnno";
	public static final String CAMPO_CHIAVE_UFFICIO = "ChiaveUfficio";
	public static final String CAMPO_CHIAVE_PROGR = "ChiaveProgr";
	public static final String CAMPO_COD_STATO_FASCICOLO = "CodStatoFascicolo";
	public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
	public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
	public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
	public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
	public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento";
	public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento";
	public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento";
	public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento";
	public static final String CAMPO_SOG_ID_SOGGETTO = "SogIdSoggetto";
	public static final String CAMPO_FAS_SIU_ID_FASCICOLO_SIUS = "FasSiuIdFascicoloSius";
	public static final String CAMPO_FAS_SIU_SOG_ID_SOGGETTO = "FasSiuSogIdSoggetto";

	public static final String CAMPO_GIORNO_ISCRIZIONE_ATTI = "GiornoIscrizioneAtti";
	public static final String CAMPO_MESE_ISCRIZIONE_ATTI = "MeseIscrizioneAtti";
	public static final String CAMPO_ANNO_ISCRIZIONE_ATTI = "AnnoIscrizioneAtti";
	public static final String CAMPO_COD_TIPO_ATTO = "CodTipoAtto";
	public static final String CAMPO_DESCR_TIPO_ATTO = "DescrTipoAtto";
	public static final String CAMPO_GIORNO_DATA_ATTO = "GiornoDataAtto";
	public static final String CAMPO_MESE_DATA_ATTO = "MeseDataAtto";
	public static final String CAMPO_ANNO_DATA_ATTO = "AnnoDataAtto";
	public static final String CAMPO_COD_MITTENTE_ATTO = "CodMittenteAtto";
	public static final String CAMPO_DESCR_MITTENTE_ATTO = "DescrMittenteAtto";
	public static final String CAMPO_COD_SEDE_MITTENTE = "CodSedeMittente";
	public static final String CAMPO_DESCR_SEDE_MITTENTE = "DescrSedeMittente";
	public static final String CAMPO_MAGISTRATO = "Magistrato";
	public static final String CAMPO_NOTE = "Note";
	public static final String CAMPO_COD_CONTENUTO = "CodContenuto";
	public static final String CAMPO_DESCR_CONTENUTO = "DescrContenuto";
	public static final String CAMPO_COD_OGGETTO = "CodOggetto";
	public static final String CAMPO_COD_DETTAGLIO_OGGETTO = "CodDettaglioOggetto";
	public static final String CAMPO_DESCR_OGGETTO = "DescrOggetto";
	public static final String CAMPO_GIORNO_DATA_ARRIVO = "GiornoDataArrivo";
	public static final String CAMPO_MESE_DATA_ARRIVO = "MeseDataArrivo";
	public static final String CAMPO_ANNO_DATA_ARRIVO = "AnnoDataArrivo";
	public static final String CAMPO_DESCR_COMUNE_UFFICIO = "CampoDescrComuneUfficio";
	public static final String CAMPO_GIORNO_FINE_PENA = "GiornoFinePena";
	public static final String CAMPO_MESE_FINE_PENA = "MeseFinePena";
	public static final String CAMPO_ANNO_FINE_PENA = "AnnoFinePena";
	public static final String CAMPO_COD_POS_GIURIDICA = "CodPosGiuridica";
	public static final String CAMPO_DESCR_POS_GIURIDICA = "DescrPosGiuridica";
	public static final String CAMPO_COD_MAGISTRATO = "CodMagistrato";

	public static final String CAMPO_CHIAVE_ANNO_S22 = "ChiaveAnnoS22";
	public static final String CAMPO_CHIAVE_PROGR_S22 = "ChiaveProgrS22";
	public static final String CAMPO_COD_TIPO_REGISTRO = "CodTipoRegistro";

	public static final String CAMPO_CHIAVE_PROGR_INIZIALE = "CampoChiaveProgrIniziale";
	public static final String CAMPO_CHIAVE_ANNO_INIZIALE = "CampoChiaveAnnoIniziale";
	public static final String CAMPO_CHIAVE_PROGR_FINALE = "CampoChiaveProgrFinale";
	public static final String CAMPO_CHIAVE_ANNO_FINALE = "CampoChiaveAnnoFinale";

	// 28/12/2003 Gestione del luogo detenzione.
	public static final String CAMPO_VALIDA_LUOGO_DET = "CampoValidaLuogoDet";
	public static final String LUOGO_DETENZIONE = "luogoDetenzione";
	public static final String ID_LUOGO_DETENZIONE = "idLuogoDetenzione";
	public static final String ID_ALTRA_CAUSA = "idAltraCausa";

	// DATA DEFINIZIONE
	public static final String CAMPO_GIORNO_DATA_DEFINIZIONE = "GiornoDataDefinizione";
	public static final String CAMPO_MESE_DATA_DEFINIZIONE = "MeseDataDefinizione";
	public static final String CAMPO_ANNO_DATA_DEFINIZIONE = "AnnoDataDefinizione";
	// Campi DEFINIZIONE
	public static final String CAMPO_TIPO_DEFINIZIONE = "CampoTipoDefinizione";
	public static final String CAMPO_DESCR_DEFINIZIONE = "CampoDescrDefinizione";

	// 14/01/2004 Descrizione Mittente.
	public static final String CAMPO_DESCR_MITTENTE = "CampoDescrMittente";

	// 02/12/2004 Iscrizione Procedimento da SIUS.
	public static final String ID_FASCICOLO_SIUS_ORIGINE = "idFascicoloSiusOrigine";

	// 23/01/2004 Nuova ricerca soggetti con procedimenti di Sorveglianza.
	public static final String CAMPO_INCLUDE_UFFICIO = "CampoIncludeUfficio";
	public static final String CAMPO_INCLUDE_DISTRETTO = "CampoIncludeDistretto";
	public static final String CAMPO_INCLUDE_ARCHIVIATI = "CampoIncludeArchiviati";
	public static final String CAMPO_INCLUDE_SOLTANTO = "CampoIncludeSoltanto";
	public static final String CAMPO_SOLO_ARCHIVIATI = "CampoSoloArchiviati"; // STUB 28/06/2005

	// 08/06/2004 Ricerca Avanzata fascicolo SIUS.
	public static final String CAMPO_GIORNO_ISCRIZIONE = "GiornoIscrizione";
	public static final String CAMPO_MESE_ISCRIZIONE = "MeseIscrizione";
	public static final String CAMPO_ANNO_ISCRIZIONE = "AnnoIscrizione";
	public static final String CAMPO_GIORNO_ISCRIZIONE_INIZIALE = "GiornoIscrizioneIniziale";
	public static final String CAMPO_MESE_ISCRIZIONE_INIZIALE = "MeseIscrizioneIniziale";
	public static final String CAMPO_ANNO_ISCRIZIONE_INIZIALE = "AnnoIscrizioneIniziale";
	public static final String CAMPO_GIORNO_ISCRIZIONE_FINALE = "GiornoIscrizioneFinale";
	public static final String CAMPO_MESE_ISCRIZIONE_FINALE = "MeseIscrizioneFinale";
	public static final String CAMPO_ANNO_ISCRIZIONE_FINALE = "AnnoIscrizioneFinale";
	public static final String CAMPO_CHIAVE_UFFICIO2 = "ChiaveUfficio2";
	public static final String CAMPO_DESCR_COMUNE_UFFICIO2 = "CampoDescrComuneUfficio2";
	public static final String CAMPO_GIORNO_ARRIVO_INIZIALE = "GiornoArrivoIniziale";
	public static final String CAMPO_MESE_ARRIVO_INIZIALE = "MeseArrivoIniziale";
	public static final String CAMPO_ANNO_ARRIVO_INIZIALE = "AnnoArrivoIniziale";
	public static final String CAMPO_GIORNO_ARRIVO_FINALE = "GiornoArrivoFinale";
	public static final String CAMPO_MESE_ARRIVO_FINALE = "MeseArrivoFinale";
	public static final String CAMPO_ANNO_ARRIVO_FINALE = "AnnoArrivoFinale";
	public static final String CAMPO_GIORNO_ATTO_INIZIALE = "GiornoAttoIniziale";
	public static final String CAMPO_MESE_ATTO_INIZIALE = "MeseAttoIniziale";
	public static final String CAMPO_ANNO_ATTO_INIZIALE = "AnnoAttoIniziale";
	public static final String CAMPO_GIORNO_ATTO_FINALE = "GiornoAttoFinale";
	public static final String CAMPO_MESE_ATTO_FINALE = "MeseAttoFinale";
	public static final String CAMPO_ANNO_ATTO_FINALE = "AnnoAttoFinale";
	public static final String CAMPO_GIORNO_FINE_PENDENZA = "GiornoFinePendenza"; // 11/04/2011
	public static final String CAMPO_MESE_FINE_PENDENZA = "MeseFinePendenza"; // 11/04/2011
	public static final String CAMPO_ANNO_FINE_PENDENZA = "AnnoFinePendenza"; // 11/04/2011
	public static final String CAMPO_GIORNO_DEFINIZIONE_INIZIALE = "GiornoDefinizioneIniziale"; // 11/04/2011
	public static final String CAMPO_MESE_DEFINIZIONE_INIZIALE = "MeseDefinizioneIniziale"; // 11/04/2011
	public static final String CAMPO_ANNO_DEFINIZIONE_INIZIALE = "AnnoDefinizioneIniziale"; // 11/04/2011
	public static final String CAMPO_GIORNO_DEFINIZIONE_FINALE = "GiornoDefinizioneFinale"; // 11/04/2011
	public static final String CAMPO_MESE_DEFINIZIONE_FINALE = "MeseDefinizioneFinale"; // 11/04/2011
	public static final String CAMPO_ANNO_DEFINIZIONE_FINALE = "AnnoDefinizioneFinale"; // 11/04/2011
	public static final String RADIO_STATO_PROCEDIMENTO = "statoProcedimento"; // 11/04/2011

	public static final String CAMPO_ID_FASCICOLO_SIUS_ORIGINE = "IdFascicoloOrigine";

	// Descrizione eccezione
	public static final String MSG_NON_MODIFICABILE = "Operazione non consentita per questo Procedimento!";

	public static final String PG_LOAD_RICERCAFASCICOLOSIUS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/LoadRicercaFascicoloSius.jsp";
	public static final String PG_LOAD_DETTAGLIOFASCICOLOSIUS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/LoadRicercaFascicoloSius.jsp";
	public static final String PG_RICERCAFASCICOLO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/RicercaFascicolo.jsp";
	public static final String PG_RICERCAFASCICOLOSIUS_PERNUMERO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/RicercaFascicoloSiusPerNumero.jsp";
	public static final String PG_RICERCAFASCICOLOSIUS_PERESTREMIATTO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/RicercaFascicoloSiusPerEstremiAtto.jsp";
	public static final String PG_RICERCAFASCICOLO_SIUS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/RicercaFascicoloSius.jsp";
	public static final String PG_RICERCAFASCICOLO_SIUS_SORV = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/RicercaFascicoloSiusSorv.jsp";
	public static final String PG_RICERCAFASCICOLO_SIUS_UNICO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/RicercaFascicoloSiusUnico.jsp";
	public static final String PG_LOAD_RICERCAFASCICOLO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/LoadRicercaFascicolo.jsp";
	public static final String PG_LOAD_RICERCASOGGETTOFASCICOLOSIEP = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/LoadRicercaSoggettoFascicoloSiep.jsp";
	public static final String PG_RICERCASOGGETTOFASCICOLOSIEP = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/RicercaSoggettoFascicoloSiep.jsp";
	public static final String PG_LOAD_INSERISCIFASCICOLOSIUS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/LoadInserisciFascicolo.jsp";
	public static final String PG_LOAD_RICERCASOGGETTOFASCICOLOSIUS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/LoadRicercaSoggettoFascicoloSius.jsp";
	public static final String PG_RICERCASOGGETTOFASCICOLOSIUS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/RicercaSoggettoFascicoloSius.jsp";
	public static final String PG_DETTAGLIOFASCICOLOSIUS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/DettaglioFascicolo.jsp";
	public static final String PG_LOAD_RICERCAFSPUNTUALE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/LoadRicercaFSPuntuale.jsp";
	public static final String PG_LISTAOGGETTI = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/ListaOggetti.jsp";
	public static final String PG_LOAD_RICERCAPROCEDIMENTOPERESTREMI = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/LoadRicercaProcedimentoPerEstremi.jsp";
	public static final String PG_LOAD_SINTESIPROCEDIMENTOSIUS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/SintesiProcedimentoSius.jsp";
	public static final String PG_RICERCARESIDENZASIUS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/RicercaResidenzaByProcedimentoSius.jsp";
	public static final String PG_RICERCADOMICILISIUS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/RicercaDomiciliByProcedimentoSius.jsp";
	public static final String PG_LOAD_INSERISCRESIDENZAFASCICOLOSIUS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/LoadInserisciResidenzaFascicoloSius.jsp";
	public static final String PG_LOAD_INSERISCDOMICILIOFASCICOLOSIUS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/LoadInserisciDomicilioFascicoloSius.jsp";
	public static final String PG_LOAD_INSERISCIFASCICOLOSIUSUDS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/LoadInserisciFascicoloUDS.jsp";
	public static final String PG_LOAD_INSERISCIFASCICOLODASOGGETTOUDS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/LoadInserisciFascicoloDaSoggettoUDS.jsp"; // STUB 31/03/2004

	public static final String PG_LOAD_RICERCASOGGETTICONPROCEDIMENTI = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/LoadRicercaSoggettiConProcedimenti.jsp";
	public static final String PG_RICERCA_SOGGETTICONPROCEDIMENTI = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/RicercaSoggettiConProcedimenti.jsp";
	public static final String PG_BUTTONS_PROVVEDIMENTI = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/ButtonsProvvedimentiDelSoggetto.jsp";
	public static final String PG_BUTTONS_PROC_DI_EMA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/esecuzionemisuraalternativa/buttonsProcedimentiDiEMA.jsp";
	public static final String PG_RICERCA_PROCEDIMENTIDELSOGGETTO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/RicercaProcedimentiDelSoggetto.jsp";
	public static final String PG_LOAD_RICERCASOGGETTICONPROCDIESECUZIONE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/LoadRicercaSoggettiConProcDiEsecuzione.jsp";
	public static final String PG_RICERCA_SOGGETTICONPROCDIESECUZIONE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/RicercaSoggettiConProcDiEsecuzione.jsp";
	public static final String PG_RICERCA_PROCDIESECUZIONEDELSOGGETTO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/RicercaProcDiEsecuzioneDelSoggetto.jsp";
	public static final String PG_RICERCA_PROCEDIMENTIPERNUMEROSIEP = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/RicercaProcedimentiPerNumeroSIEP.jsp";
	public static final String PG_RICERCA_PROCDIESECESTERNAPERFASSIUS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/RicercaProcDiEsecEsternaPerFasSius.jsp";
	public static final String PG_LOAD_RICERCA_PROC_PERDFP = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/LoadRicercaProcedimentiPerDFP.jsp";
	public static final String PG_RICERCA_PROC_PERDFP = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/RicercaProcedimentiPerDFP.jsp";
	public static final String PG_LOAD_MODIFICAFASCICOLOSIUSUDS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/LoadModificaFascicoloUDS.jsp";
	public static final String PG_LOAD_INSERISCIFASCICOLOSIUSMANUALE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/LoadInserisciFascicoloManuale.jsp"; // STUB 03/06/2004
	public static final String PG_LOAD_INSERISCIFASCICOLOSIUSUDSMANUALE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/LoadInserisciFascicoloUDSManuale.jsp"; // STUB 03/06/2004
	public static final String PG_LOAD_INSERISCIFASCICOLODASOGGETTOUDSMANUALE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/LoadInserisciFascicoloDaSoggettoUDSManuale.jsp"; // STUB 03/06/2004
	public static final String PG_LOAD_DEFINIZIONE_PROCEDIMENTO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/LoadDefinizioneProcedimento.jsp";
	public static final String PG_LISTA_FASCICOLI_ORIGINE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/ListaFascicoliOrigine.jsp";
	public static final String PG_LOAD_RICERCASOGGETTICONPROCDIESECUZIONESS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/LoadRicercaSoggettiConProcDiEsecuzioneSS.jsp";
	public static final String PG_LOAD_RICERCASOGGETTICONPROCDIESECUZIONEMS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/LoadRicercaSoggettiConProcDiEsecuzioneMS.jsp";
	public static final String PG_RICERCA_SOGGETTICONPROCDIESECUZIONESS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/RicercaSoggettiConProcDiEsecuzioneSS.jsp";
	public static final String PG_RICERCA_SOGGETTICONPROCDIESECUZIONEMS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/RicercaSoggettiConProcDiEsecuzioneMS.jsp";
	public static final String PG_RICERCA_PROCDIESECUZIONESSDELSOGGETTO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/RicercaProcDiEsecuzioneSSDelSoggetto.jsp";
	public static final String PG_RICERCA_PROCDIESECUZIONEMSDELSOGGETTO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/RicercaProcDiEsecuzioneMSDelSoggetto.jsp";
	public static final String PG_BUTTONS_PROC_DI_ESS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/esecuzionesanzionesostitutiva/buttonsProcedimentiDiESS.jsp";
	public static final String PG_BUTTONS_PROC_DI_EMS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/esecuzionemisurasicurezza/buttonsProcedimentiDiEMS.jsp";
	public static final String PG_LOAD_RICERCA_SPORTELLO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/LoadRicercaSportello.jsp"; // 10/03/2008
	public static final String PG_RICERCA_SPORTELLO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/RicercaSportello.jsp"; // 11/03/2008
	public static final String PG_RICERCA_PROC_SOGGETTO_SPORTELLO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/RicercaProcedimentiSoggettoPerSportello.jsp"; // 11/03/2008
	public static final String PG_BUTTONS_PROVV_SOGG_SPORTELLO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/ButtonsProvvedimentiDiSoggettoPerSportello.jsp"; // 11/03/2008

	// Codifica Oggetto Procedimento
	public static final String COD_OGGETTO_PROCEDIMENTO_MA = "U004"; // Esecuzione Misura Alternativa
	public static final String COD_OGGETTO_PROCEDIMENTO_SS = "U019"; // Esecuzione Sanzioni Sostitutive
	public static final String COD_OGGETTO_PROCEDIMENTO_MS = "U024"; // Esecuzione Misure di Sicurezza
	// MEV_2023-35
	public static final String COD_OGGETTO_PROCEDIMENTO_PS = "U126"; // Esecuzione Pen Sostitutive

	// CODIFICA STATO FASCICOLO
	public static final String COD_DEFINITO = "01";
	public static final String COD_ISCRITTO = "02";
	public static final String COD_UNIFICATO = "05";
	public static final String COD_EMESSO_PROVVEDIMENTOO = "07";
	public static final String COD_RINVIATO_NUOVO_RUOLO = "10";

	public static final String PG_SINTESIPROCEDIMENTOORIGINESIUS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/SintesiProcedimentoOrigineSius.jsp";
	public static final String PG_LOAD_DETTAGLIOPROCEDIMENTOCOLLEGATO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/DettaglioProcedimentoCollegato.jsp";
	public static final String PG_LOAD_MODIFICACOLLEGAMENTO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/LoadModificaCollegamento.jsp";

	// Codifica Tenore Codifica Affidamento al servizio sociale
	public static final String COD_AFFIDAMENTO_SERVIZIO_SOCIALE = "0002";

	public static final String PG_LOAD_RICHIESTA_CERTIFICATO_PENALE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/fascicolo/LoadRichiestaCertificatoPenale.jsp";

	// MEV 15 - Revisione SIGE
	public static final String COD_FUNZIONE_90020012 = "90020012"; // Visualizzazione form di Ricerca Soggetti
																	// con Proc. SIEP
	public static final String COD_FUNZIONE_90010000 = "90010000"; // Iscrizione manuale
	public static final String COD_FUNZIONE_90020000 = "90020000"; // Ricerche
	public static final String COD_FUNZIONE_90030000 = "90030000"; // Fase Istruttoria
	public static final String COD_FUNZIONE_90040000 = "90040000"; // Provvedimenti Interlocutori
	public static final String COD_FUNZIONE_90050000 = "90050000"; // Udienze
	public static final String COD_FUNZIONE_90060000 = "90060000"; // Ordinanze
	public static final String COD_FUNZIONE_90110000 = "90110000"; // Funzioni Amministrative

}