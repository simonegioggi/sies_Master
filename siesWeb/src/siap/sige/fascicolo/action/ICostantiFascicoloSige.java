package siap.sige.fascicolo.action;

import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ICostantiFascicoloSige
 * </p>
 * <p>
 * Description: Classe di costanti di FascicoloSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
public interface ICostantiFascicoloSige {

	public static final String CAMPO_ID_FASCICOLO_SIGE = "IdFascicoloSige";
	public static final String CAMPO_SOG_ID_SOGGETTO = "SogIdSoggetto";
	public static final String CAMPO_CHIAVE_ANNO = "ChiaveAnno";
	public static final String CAMPO_CHIAVE_UFFICIO = "ChiaveUfficio";
	public static final String CAMPO_CHIAVE_ACCORPATO = "ChiaveAccorpato";
	public static final String CAMPO_CHIAVE_PROGR = "ChiaveProgr";
	public static final String CAMPO_CHIAVE_PROGR_ORIGIN = "ChiaveProgrOrigin";
	public static final String CAMPO_SEZ_ID_SEZIONE = "IdSezione";
	public static final String CAMPO_COD_STATO_FASCICOLO = "CodStatoFascicolo";
	public static final String CAMPO_COD_TIPO_GIUDIZIO = "CodTipoGiudizio";
	public static final String CAMPO_COD_TIPO_UFF_COMP_CORTE_SUPREMA = "CodTipoUffCompCorteSuprema";
	public static final String CAMPO_GIORNO_DATA_ISCRIZIONE = "GiornoDataIscrizione";
	public static final String CAMPO_MESE_DATA_ISCRIZIONE = "MeseDataIscrizione";
	public static final String CAMPO_ANNO_DATA_ISCRIZIONE = "AnnoDataIscrizione";
	public static final String CAMPO_GIORNO_DATA_DEFINIZIONE = "GiornoDataDefinizione";
	public static final String CAMPO_MESE_DATA_DEFINIZIONE = "MeseDataDefinizione";
	public static final String CAMPO_ANNO_DATA_DEFINIZIONE = "AnnoDataDefinizione";
	public static final String CAMPO_RIC_ID_RICHIESTA_SIGE = "RicIdRichiestaSige";
	public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
	public static final String CAMPO_COD_UFFICIO_INSERIMENTO = "CodUfficioInserimento";
	public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
	public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
	public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
	public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento";
	public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO = "CodUfficioAggiornamento";
	public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento";
	public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento";
	public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento";
	public static final String CAMPO_NOTE = "Note";
	public static final String CAMPO_COD_POSIZIONE_GIURIDICA = "CodPosizioneGiuridica";
	public static final String CAMPO_GIORNO_DATA_FINE_PENA = "GiornoDataFinePena";
	public static final String CAMPO_MESE_DATA_FINE_PENA = "MeseDataFinePena";
	public static final String CAMPO_ANNO_DATA_FINE_PENA = "AnnoDataFinePena";
	public static final String CAMPO_DESCR_COMUNE_UFFICIO = "CampoDescrComuneUfficio";
	public static final String CAMPO_DESCR_COMUNE_UFF_COMP_CORTE_SUPREMA = "CampoDescrComuneUffCompCorteSuprema";
	public static final String CAMPO_IS_FASCICOLO_COLLEGATO_RICORSO = "IsFascicoloCollegatoRicorso";
	public static final String CAMPO_ANNO_DECISIONE_RICORSO_COLLEGATO = "AnnoDecisioneCollegato";
	public static final String CAMPO_MESE_DECISIONE_RICORSO_COLLEGATO = "MeseDecisioneCollegato";
	public static final String CAMPO_GIORNO_DECISIONE_RICORSO_COLLEGATO = "GiornoDecisioneCollegato";
	public static final String CAMPO_COD_MAG_ASS = "CodMagAss";
	public static final String CAMPO_UFFICIO_DISTRETTO = "CampoUfficioDistretto";
	public static final String CAMPO_VALIDA_LUOGO_DET = "CheckLuogoDetenzione";
	public static final String PG_LOAD_RICERCAFASCICOLOSIGE = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/LoadRicercaFascicoloSige.jsp";
	public static final String PG_LOAD_DETTAGLIOFASCICOLOSIGE = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/DettaglioFascicoloSige.jsp";
	public static final String PG_RICERCAFASCICOLOSIGE = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/RicercaFascicoloSige.jsp";
	public static final String PG_LOAD_INSERISCIFASCICOLOSIGE = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/LoadInserisciFascicolo.jsp";
	public static final String PG_LOAD_INSERISCIFASCICOLOSIGEMANUALE = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/LoadInserisciFascicoloManuale.jsp";
	public static final String PG_SOGGETTO_FAS_SIEP = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/SintesiSoggFasSiep.jsp";
	public static final String PG_LOAD_SINTESIPROCEDIMENTOSIGE = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp";
	public static final String PG_LOAD_SINTESISIGESIEP = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/SintesiSigeSiep.jsp";

	public static final String PG_INCLUDE_SOGGETTO = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/IncludeSoggetto.jsp";
	public static final String PG_INCLUDE_FAS_SIEP = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/IncludeFasSiep.jsp";
	public static final String PG_INCLUDE_SENTENZE = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/IncludeSentenze.jsp";
	public static final String PG_INCLUDE_DETENZIONE = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/IncludeDetenzione.jsp";
	public static final String PG_INCLUDE_RICERCA_SENTENZA = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/IncLoadRicercaPerSentenza.jsp";
	public static final String PG_INCLUDE_RICERCA_FASCICOLO_SIEP = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/IncLoadRicercaPerFasSiep.jsp";
	public static final String PG_ELENCO_ALTRI_PROVVEDIMENTI = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/ElencoAltriProvvedimenti.jsp";

	public static final String PG_RICERCAFASCICOLOSIGE_PERNUMERO = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/RicercaFascicoloSigePerNumero.jsp";
	public static final String PG_LOAD_RICERCASOGGETTICONPROCSIGE = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/LoadRicercaSoggettiConProcSige.jsp";
	public static final String PG_RICERCASOGGETTICONFASCICOLOSIGE = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/RicercaSoggettiConProcSige.jsp";
	public static final String PG_BUTTONS_FASCICOLI = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/ButtonsFascicoliDelSoggetto.jsp";
	public static final String PG_RICERCA_FASCICOLIDELSOGGETTO = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/RicercaFascicoliDelSoggetto.jsp";
	public static final String PG_ELENCO_FASCICOLIDELSOGGETTO = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/ElencoFascicoliDelSoggetto.jsp";
	public static final String PG_LOAD_INSERISCIRESIDENZAFASCICOLOSIGE = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/LoadInserisciResidenzaFascicoloSige.jsp";
	public static final String PG_RICERCARESIDENZASIGE = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/RicercaResidenzaByProcedimentoSige.jsp";
	public static final String PG_LOAD_INSERISCIDOMICILIOFASCICOLOSIGE = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/LoadInserisciDomicilioFascicoloSige.jsp";
	public static final String PG_RICERCADOMICILISIGE = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/RicercaDomiciliByProcedimentoSige.jsp";
	public static final String PG_LOAD_RICERCAFSIGEPUNTUALE = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/LoadRicercaFSigePuntuale.jsp";
	public static final String PG_LOAD_RICERCAFSIGE_PERESTREMI = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/LoadRicercaFSigePerEstremi.jsp";
	public static final String PG_RICERCAFASCICOLOSIGE_PERESTREMI = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/RicercaFascicoloSigePerEstremi.jsp";
	public static final String PG_LOAD_RICERCAFSIGE_PERTITOLO = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/LoadRicercaFSigePerTitoloEsecutivo.jsp";
	public static final String PG_RICERCAFASCICOLOSIGE_PERTITOLOESEC = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/RicercaFascicoloSigePerPerTitoloEsecutivo.jsp";
	public static final String PG_RICERCA_FASCICOLIPERSENTENZA = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/RicercaFascicoliPerSentenza.jsp";
	public static final String PG_RICERCA_FASCICOLIPERFASSIEP = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/RicercaFascicoliPerFasSiep.jsp";
	public static final String PG_ELENCO_FASCICOLISIEPDELSOGGETTO = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/ElencoFascicoliSIEPDelSoggetto.jsp";

	public static final String PG_LISTA_PROCEDIMENTI_DI_CUMULO_SIGE = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/ListaProcedimentoSiepDiCumulo.jsp";

	public static final String JS_RICERCA_FASCICOLO = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/RicercaFascicoloSige.js";
	public static final String MSG_NON_MODIFICABILE = "Operazione non consentita per questo Procedimento!";

	// Costanti usati per le Ricerche
	public static final String RADIO_TIPO_RICERCA = "TipoRicerca";
	public static final String DIV_RICERCA_FASCICOLO_BASE = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/DivRicercaFascicoloBase.jsp";
	public static final String DIV_RICERCA_FASCICOLO_AVANZATA = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/DivRicercaFascicoloAvanzata.jsp";
	public static final String CAMPO_ANNO_INI = "AnnoIni";
	public static final String CAMPO_NUM_INI = "NumIni";
	public static final String CAMPO_NUM_INI_ORIGIN = "NumIniOrigin";
	public static final String CAMPO_ANNO_FINE = "AnnoFine";
	public static final String CAMPO_NUM_FINE = "NumFine";
	public static final String CAMPO_NUM_FINE_ORIGIN = "NumFineOrigin";
	public static final String CAMPO_TIPO_INTERVALLO = "TipoIntervallo";
	public static final String CAMPO_GIORNO_INIZIALE = "GiornoIniziale";
	public static final String CAMPO_MESE_INIZIALE = "MeseIniziale";
	public static final String CAMPO_ANNO_INIZIALE = "AnnoIniziale";
	public static final String CAMPO_GIORNO_FINALE = "GiornoFinale";
	public static final String CAMPO_MESE_FINALE = "MeseFinale";
	public static final String CAMPO_ANNO_FINALE = "AnnoFinale";
	public static final String CAMPO_TIPO_UFFICIO = "CodTipoUfficio";
	public static final String CAMPO_SEDE = "Sede";
	public static final String ESTREMI_PROVVEDIMENTO_INTERVALLO = "PI";
	public static final String CAMPO_MODALITA_RICERCA = "ModalitaRicerca";
	public static final String DATE_INTERVALLO = "DI";
	public static final String CAMPO_CHIAVE_ANNO_INIZIALE = "CampoChiaveAnnoIniziale";
	public static final String CAMPO_CHIAVE_PROGR_INIZIALE = "CampoChiaveProgrIniziale";
	public static final String CAMPO_CHIAVE_ANNO_FINALE = "CampoChiaveAnnoFinale";
	public static final String CAMPO_CHIAVE_PROGR_FINALE = "CampoChiaveProgrFinale";
	public static final String CAMPO_GIORNO_ISCRIZIONE_INIZIALE = "GiornoIscrizioneIniziale";
	public static final String CAMPO_MESE_ISCRIZIONE_INIZIALE = "MeseIscrizioneIniziale";
	public static final String CAMPO_ANNO_ISCRIZIONE_INIZIALE = "AnnoIscrizioneIniziale";
	public static final String CAMPO_GIORNO_ISCRIZIONE_FINALE = "GiornoIscrizioneFinale";
	public static final String CAMPO_MESE_ISCRIZIONE_FINALE = "MeseIscrizioneFinale";
	public static final String CAMPO_ANNO_ISCRIZIONE_FINALE = "AnnoIscrizioneFinale";
	public static final String CAMPO_GIORNO_DEFINIZIONE_INIZIALE = "GiornoDefinizioneIniziale";
	public static final String CAMPO_MESE_DEFINIZIONE_INIZIALE = "MeseDefinizioneIniziale";
	public static final String CAMPO_ANNO_DEFINIZIONE_INIZIALE = "AnnoDefinizioneIniziale";
	public static final String CAMPO_GIORNO_DEFINIZIONE_FINALE = "GiornoDefinizioneFinale";
	public static final String CAMPO_MESE_DEFINIZIONE_FINALE = "MeseDefinizioneFinale";
	public static final String CAMPO_ANNO_DEFINIZIONE_FINALE = "AnnoDefinizioneFinale";
	public static final String CAMPO_GIORNO_FINE_PENDENZA = "GiornoFinePendenza";
	public static final String CAMPO_MESE_FINE_PENDENZA = "MeseFinePendenza";
	public static final String CAMPO_ANNO_FINE_PENDENZA = "AnnoFinePendenza";

	public static final String PG_LOAD_DEFINIZIONE_PROCEDIMENTO = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/LoadDefinizioneProcedimento.jsp";

	// CODIFICA STATO FASCICOLO
	public static final String COD_DEFINITO = "01";
	public static final String COD_ISCRITTO = "02";
	public static final String COD_UNIFICATO = "05";
	public static final String COD_EMESSO_PROVVEDIMENTO = "07";
	public static final String COD_RINVIATO_NUOVO_RUOLO = "10";

	public static final String COD_ACCOGLIE_FISSA_UDIENZA = "14";
	public static final String COD_ANNULLA_CON_RINVIO = "15";
	public static final String COD_RICORSO_CONVERTITO_OPPOSIZIONE = "16";

	public static final String COD_OPPOSIZIONE = "17";
	public static final String COD_RICORSO = "18";
	public static final String COD_CONVERTE_IN_RICORSO_IN_CASSAZIONE = "19";
	//@emma 10072018 intervento post COLLAUDO 11.2
	public static final String COD_RICORSO_CONVERTITO_OPPOSIZIONE_UDI = "21";
	//@emma 21082018 intervento post COLLAUDO 11.2 (AGGIUNGO LA PROPRIETA PER COD_DECRETO_FISSAZIONE_UDIENZA)
	public static final String COD_DECRETO_FISSAZIONE_UDIENZA = "20";

	// Campi DEFINIZIONE
	public static final String CAMPO_TIPO_DEFINIZIONE = "CampoTipoDefinizione";
	public static final String CAMPO_DESCR_DEFINIZIONE = "CampoDescrDefinizione";

	public static final String PG_LOAD_RICERCAFASCICOLO_SIEP_SIGE = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/LoadRicercaFascicoloSiepSige.jsp";

	public static final String REDIRECT_FASCICOLO_RICERCATO = IWebConstants.PG_MAIN + "?"
			+ IWebConstants.ACTION_FIELD + "=siap.sige.fascicolo.action.ActLoadRicercaFascicoloSige&"
			+ ICostantiFascicoloSige.CAMPO_AZIONE_CHIAMANTE + "=";

	public static final String CAMPO_AZIONE_CHIAMANTE = "AzioneChiamante";

	public static final String PG_BUTTONS_DETTAGLIO_FASCICOLO_SIGE = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/ButtonsDettaglioFascicoloSige.jsp";
	public static final String PG_ELENCO_ATTI_ISTRUTTORI_SHOT = IWebConstants.ROOT_DIR
			+ "files/siap/sige/richiestaatti/ElencoAttiIstruttoriShort.jsp";

	public static final String PG_LOAD_DETTAGLIOPROCEDIMENTOCOLLEGATO = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/DettaglioProcedimentoCollegato.jsp";
	public static final String PG_LOAD_MODIFICACOLLEGAMENTO = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/LoadModificaCollegamento.jsp";

	public static final String CAMPO_COD_MITTENTE_ATTO = "CodMittenteAtto";
	public static final String CAMPO_DESCR_MITTENTE_ATTO = "DescrMittenteAtto";
	public static final String CAMPO_COD_SEDE_MITTENTE = "CodSedeMittente";
	public static final String CAMPO_DESCR_SEDE_MITTENTE = "DescrSedeMittente";
	public static final String CAMPO_ID_FASCICOLO_SIGE_ORIGINE = "IdFascicoloSigeOrigine";

	// MEV_65: aggiunte pagine per gestire nuove funzionalita'
	public static final String PG_LOAD_RICERCASOGGETTISIGE_PERPOSIZIONEGIURIDICA = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/LoadRicercaSoggettiSigePerPosizioneGiuridica.jsp";
	public static final String PG_LOAD_RICERCAPROCEDIMENTISIGE_CONRICORSOOPPOSIZIONE = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/LoadRicercaProcedimentiSigeConRicorsoOpposizione.jsp";
	public static final String PG_RICERCASOGGETTISIGE_PERPOSIZIONEGIURIDICA = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/RicercaSoggettiSigePerPosizioneGiuridica.jsp";
	public static final String PG_RICERCAPROCEDIMENTISIGE_CONRICORSOOPPOSIZIONE = IWebConstants.ROOT_DIR
			+ "files/siap/sige/fascicolo/RicercaProcedimentiSigeConRicorsoOpposizione.jsp";

}