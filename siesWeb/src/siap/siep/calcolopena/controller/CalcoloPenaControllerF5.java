package siap.siep.calcolopena.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataSqlDAO;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.misuraalternativa.dao.MisuraAlternativaSqlDAO;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.CalendarUtil;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeSqlDAO;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.beneficio.dao.BeneficioSqlDAO;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.calcolopena.action.ICostantiCalcoloPena;
//import siap.siep.calcolopena.dao.CalcoloPenaSqlDAO;
import siap.siep.calcolopena.model.CalcoloPenaModel;
//import siap.siep.calcolopena.model.EventiCalcoloPenaModel;
import siap.siep.cumulo.dao.CumuloSqlDAO;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fungibilita.dao.FungibilitaSqlDAO;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.modulocumulo.util.ModuloCumuloUtils;
import siap.siep.penacomplessiva.dao.PenaComplessivaSqlDAO;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penacumulo.dao.PenaCumuloSqlDAO;
import siap.siep.penacumulo.model.PenaCumuloModel;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.sanzionesostitutiva.dao.SanzioneSostitutivaSqlDAO;
import siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel;
import siap.siep.sospensione.dao.SospensioneSqlDAO;
import siap.siep.sospensione.model.SospensioneModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CalcoloPenaControllerF5 extends SiapController implements ICalcoloPenaF5 {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Recupera la Pena Iniziale e valorizza se possibile l'intervallo di date nel quale recuperare gli eventi
	 * che concorrono al calcolo della pena. L'intervallo di date è compreso tra: dataDal = la data
	 * validazione dell'evento a cui è associata la pena iniziale (tranne Pena in sentenza per la quale è
	 * null, non ha senso vanno presi in considerazione tutti i dati) dataAl = data validazione dell'evento in
	 * input (se passato), se l'evento non è validato...???
	 * 
	 * @param aFascID
	 * @param aIdEvento
	 */
	public CalcoloPenaModel exGetPenaIniziale(BigDecimal aFascID, BigDecimal aIdEvento) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Determino la Pena Iniziale...");
		Connection lConn = null;

		// Dichiarazione DAO e SqlDAO
		EventoSqlDAO lEveSqlDao = null;
		CumuloSqlDAO lCumSqlDao = null;
		PenaComplessivaSqlDAO lPenCompSqlDao = null;
		PenaResiduaSqlDAO lPenaResSqlDao = null;
		PenaCumuloSqlDAO lPenaCumSqlDao = null;
		MisuraAlternativaSqlDAO lMisAltSqlDao = null;

		// Dichiarazione Model
		EventoModel lEveModRef = null;
		EventoModel lEventoIniziale = null;
		PenaComplessivaModel lPenCompMod = null;
		PenaCumuloModel lPenaCumuloMod = null;

		// ==========================================================================
		// Inizializzo il CalcoloPenaModel
		// ==========================================================================
		CalcoloPenaModel lCalcoloPenaMod = new CalcoloPenaModel();
		lCalcoloPenaMod.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_NON_DEFINITA);
		lCalcoloPenaMod.setDataDal(null);
		lCalcoloPenaMod.setDataAl(null);

		try {
			lConn = getDBConnection();

			// ========================================================================
			// Recupero la pena complessiva per verificare se trattasi di Ergastolo
			// In quessto caso la pena iniziale è sempre la pena in sentenza (pena
			// complessiva) per ora.
			// ========================================================================
			// lPenCompSqlDao = new PenaComplessivaSqlDAO(lConn);
			// lPenCompSqlDao.ricercaPenaComplessivaByIdFascicolo(aFascID);
			// lPenCompMod = (PenaComplessivaModel) lPenCompSqlDao.getModelByKey();
			// lPenCompSqlDao.stop();

			// ========================================================================
			// Se è stato specificato l'idEvento, lo recupero per conoscere la data
			// inserimento/modifica
			// ========================================================================
			if (aIdEvento != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Recupero l'evento di riferimento...");
				lEveSqlDao = new EventoSqlDAO(lConn);
				lEveSqlDao.ricercaEventoByKey(aIdEvento);
				lEveModRef = (EventoModel) lEveSqlDao.getModelByKey();
				lEveSqlDao.stop();

				// mod beta 2. Utilizzo sempre la data di inserimento in quanto la
				// data di validazione può essere modificata (vedi stato esecuzione)
				lCalcoloPenaMod.setDataAl(lEveModRef.getDataInserimento());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Evento Riferimento = " + lEveModRef);
			}

			/*
			 * n.b. Per ogni potenziale Pena Iniziale devo recuperare prima l'evento associato e verificare
			 * che: - sia VALIDATO - non sia ANNULLATO - abbia data inserimento<data evento di riferimento Le
			 * ricerche possono restituire più di un evento, in questo caso devo prendere quello più recente.
			 * Possono essere presenti più Cumuli, Interruzioni, Differimenti....
			 * 
			 * Effettuo una sola ricerca per evento generico con data inserimento<data evento di riferimento,
			 * scorro la lista fino a che non trovo un evento di interesse (cumulo, interruzione....) e
			 * scegliere questo come evento iniziale.
			 * 
			 * Attenzione!!! Nel caso di Differimento/Sospensione/Interruzione non è detto che la pena sia
			 * stata ricalcolata, ciò avviene solo se è in espiazione. Se il soggetto è libero non viene
			 * calcolato nulla. In questo caso l'evento non costituisce una Pena Iniziale.
			 */

			// ========================================================================
			// Recupero tutti gli eventi inseriti prima dell'evento di riferimento
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero gli eventi presenti sul fascicolo...");
			Vector lListaEventi = new Vector();
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByIdFascicoloSiepDataInsDesc(aFascID, lCalcoloPenaMod.getDataAl());
			lEveSqlDao.start();

			while (lEveSqlDao.next()) {
				lListaEventi.add((EventoModel) lEveSqlDao.getModel());
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lListaEventi.size() = " + lListaEventi.size());
			lEveSqlDao.stop();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			if (siesLogger.isDebugEnabled()) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Lista eventi recuperati...");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger
						.debug("| Tipo Evento | Tipo provv. | Cod. Motivo |       Data Ins        |       Data Agg        | Validato |");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger
						.debug("|-------------|-------------|-------------|-----------------------|-----------------------|----------|");
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("|     01      |      09     |     0099    | 01/10/2006 19:34:15.0 | 01/10/2006 19:34:15.0 |     S    |");
				Iterator itx = lListaEventi.iterator();
				while (itx.hasNext()) {
					EventoModel lEveMod = (EventoModel) itx.next();
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("|     " + lEveMod.getCodTipoEvento() + "      " + "|      "
							+ lEveMod.getCodTipoProvvedimento() + "     " + "|     " + lEveMod.getCodMotivo()
							+ "    " + "| " + lEveMod.getDataInserimento() + " " + "| "
							+ lEveMod.getDataAggiornamento() + " " + "|     "
							+ lEveMod.getFlagDocumentoRegistrato() + "    |");
				}
			}

			// ========================================================================
			// Scorro la lista degli eventi alla ricerca di uno degli eventi che
			// determinano una Pena Iniziale
			// ========================================================================
			PenaResiduaModel lPenaInizialeMod = null;

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ricerca evento per Pena Iniziale...");
			Iterator itx = lListaEventi.iterator();
			while (itx.hasNext()) {
				EventoModel lEveMod = (EventoModel) itx.next();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(lEveMod.getCodTipoEvento() + " - " + lEveMod.getCodTipoProvvedimento()
						+ " - " + lEveMod.getCodMotivo() + " - " + lEveMod.getFlagDocumentoRegistrato());

				// ======================================================================
				// Considero solo gli Eventi Validati non Annullati
				// ======================================================================
				// n.b. non sempre viene rieffettuato il calcolo della pena anche in
				// presenza di una Sospensione/Interruzione.... Le funzione di check
				// oltre a verificare se trattasi di un evento del tipo richiesto,
				// verificano anche se tale evento ha comportato il ricalcolo della
				// pena e restituiscono false altrimenti.
				// Cumulo sempre
				// Sospensione non sempre
				// Interruzione non sempre
				// Differimento (da verificare)
				//
				// ATTENZIONE!! Poichè non sempre sono disponibili i dati della pena
				// associati agli eventi, le funzioni di check non devono
				// ritornare true se effettivamente non è possibile recuperare
				// la pena in modo che non si abbiano null pointer o rilanci
				// di eccezioni.
				// Se l'evento esiste, ma non può essere gestito come pena
				// iniziale si deve tornare indietro come se l'evento non
				// fosse una pena iniziale fino alla pena in sentenza.
				// Se non esiste nemmeno la pena in sentenza si utilizza
				// l'ultima pena validata se presente, altrimenti la non
				// validata altrimenti nulla.
				// I metodi di controllo devono restituire anche la pena associata se
				// trattasi di evento iniziale. Se l'evento è un evento di pena iniziale
				// ma mancano i dati a sistema per recuperare la pena, il metodo deve
				// restituire false. In questo modo si viaggia a ritroso fino alla pena
				// in sentenza (se esiste), il risultato ottenuto sarà errato, ma almeno
				// il fascicolo è lavorabile.
				// IL PROBLEMA è COME PASSARE IN DIETRO DUE VARIABILI.
				// Inserire metodo get_pena_iniziale. Se restituisce un model vuoto vuol
				// dire che c'è un problema!!!!!!!!!!!!
				if (lEveMod.getFlagDocumentoRegistrato() != null
						&& lEveMod.getFlagDocumentoRegistrato().equals("S")) {
					if (isCumulo(lEveMod)) {
						lCalcoloPenaMod.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_IN_CUMULO);
						lEventoIniziale = lEveMod;
						break;
					} else if (isNuovoCumulo(lEveMod)) {
						lCalcoloPenaMod.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_IN_CUMULO_NEW);
						lEventoIniziale = lEveMod;
						break;
					} else if (isSospensione(lEveMod)) {
						lCalcoloPenaMod.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_SOSPENSIONE);
						lEventoIniziale = lEveMod;
						break;
					} else if (isSospensioneRes(lEveMod)) {
						// Provo a vedere se riesco a recuperare la pena da sospensione
						// n.b. non sempre è possibile
						lPenaInizialeMod = getPena(lEveMod, ICostantiCalcoloPena.PENA_SOSPENSIONE_RES);
						if (lPenaInizialeMod != null) {
							lCalcoloPenaMod.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_SOSPENSIONE_RES);
							lEventoIniziale = lEveMod;
							break;
						} else {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.error("Sospensione RES scartata per mancanza di pena ");
						}
					} else if (isSospensioneMigrata(lEveMod)) {
						lCalcoloPenaMod.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_SOSPENSIONE);
						lEventoIniziale = lEveMod;
						break;
					}
					// // Condizione in fase di verifica
					// else if ( isIndultinoRES(lEveMod) ) {
					// lCalcoloPenaMod.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_SOSPENSIONE);
					// lEventoIniziale = lEveMod;
					// break;
					// }
					else if (isInterruzione(lEveMod) || isInterruzioneRES(lEveMod)) {
						// In fase di test
						lCalcoloPenaMod.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_SOSPENSIONE); // n.b.
																									// l'interruzione
																									// è
																									// assimilata
																									// a una
																									// Sospensione
						lEventoIniziale = lEveMod;
						break;
					} else if (isEspulsione(lEveMod)) {
						lCalcoloPenaMod.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_SOSPENSIONE); // n.b.
																									// l'espulsione
																									// è
																									// assimilata
																									// a una
																									// Sospensione
						lEventoIniziale = lEveMod;
						break;
					} else if (isDifferimento(lEveMod)) {
						// Verifico se esiste una pena residua associata
						lCalcoloPenaMod.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_SOSPENSIONE);
						lEventoIniziale = lEveMod;
						break;
					} else if (isRevocaMA(lEveMod)) {
						// Verifico se esiste una pena residua associata
						lCalcoloPenaMod.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_REVOCA_MA);
						lEventoIniziale = lEveMod;
						break;
					} else if (isRevocaIndultino(lEveMod)) {
						// Verifico se esiste una pena residua associata
						lCalcoloPenaMod.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_REVOCA_INDULTINO);
						lEventoIniziale = lEveMod;
						break;
					} else if (isCessazioneMA(lEveMod)) {
						// Verifico se esiste una pena residua associata
						lCalcoloPenaMod.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_CESSAZIONE_MA);
						lEventoIniziale = lEveMod;
						break;
					} else if (isArchiviazioneSIEP(lEveMod)) {
						lPenaInizialeMod = getPena(lEveMod, ICostantiCalcoloPena.PENA_ARCHIVIATA_SIEP);
						if (lPenaInizialeMod != null) {
							lCalcoloPenaMod.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_ARCHIVIATA_SIEP);
							lEventoIniziale = lEveMod;
							break;
						} else {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.error("Archiviazione SIEP scartata per mancanza di pena ");
						}
					} else if (isArchiviazioneRes(lEveMod)) {
						// Provo a vedere se riesco a recuperare la pena da archiviazione
						// n.b. non sempre è possibile
						lPenaInizialeMod = getPena(lEveMod, ICostantiCalcoloPena.PENA_ARCHIVIATA_RES);
						if (lPenaInizialeMod != null) {
							lCalcoloPenaMod.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_ARCHIVIATA_RES);
							lEventoIniziale = lEveMod;
							break;
						} else {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.error("Archiviazione RES scartata per mancanza di pena ");
						}
					} else if (isPenaManuale(lEveMod)) {
						lCalcoloPenaMod.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_MANUALE);
						lEventoIniziale = lEveMod;
						break;
					} else if (isForzaturaRes(lEveMod)) {
						// Verifico se esiste una annotazione manuale associata
						lPenaInizialeMod = getPena(lEveMod, ICostantiCalcoloPena.PENA_MANUALE_RES);
						if (lPenaInizialeMod != null) {
							lCalcoloPenaMod.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_MANUALE_RES);
							lEventoIniziale = lEveMod;
							break;
						} else {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.error("Forzatura RES scartata per mancanza Annotazione ");
						}
					} else if (isInterruzioneIndulto(lEveMod)) {
						lCalcoloPenaMod.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_DA_INDULTO);
						lEventoIniziale = lEveMod;
						break;
					} else if (isRevocaSanzioneSostitutiva(lEveMod)) {
						lCalcoloPenaMod.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_DA_REVOCA_SS);
						lEventoIniziale = lEveMod;
						break;
					}
				} else {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Salto. Evento non validato!!!");
				}
			}

			// ========================================================================
			// 05/04/2007
			// Se la pena in sentenza è un Ergastolo, la pena iniziale coincide con la
			// pena in sentenza in quanto qualsiasi altro evento iniziale non è in grado
			// di rideterminare i quantum di pena (sospensioni) ne di inglobare eventuali
			// LA sul fine pena (vengono sempre accantonate).
			//
			// Fa eccezione la pena manuale ???
			// Il cumulo ???? su un Ergastolo???
			// n.b. il problema per ora si pone solo sul primo calcolo della pena
			// ========================================================================
			// if ( lPenCompMod!=null && lPenCompMod.getCodTipoPenaDetentiva()!=null
			// && ( lPenCompMod.getCodTipoPenaDetentiva().equals("03")
			// || lPenCompMod.getCodTipoPenaDetentiva().equals("04")
			// )
			// )
			// {
			// lCalcoloPenaMod.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_NON_DEFINITA);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Ergastolo!!");
			// }

			// ========================================================================
			// Se non esiste Cumulo, Sospensione, Differimento, Interruzione, RevocaMA
			// , RevocaIndultino, una pena manuale, la pena iniziale non può che essere
			// la Pena in Sentenza
			// ========================================================================
			if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_NON_DEFINITA) {
				// La pena iniziale è la Pena in Sentenza, recupero i dati
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Recupero la Pena Complessiva");
				lCalcoloPenaMod.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_IN_SENTENZA);
				lPenCompSqlDao = new PenaComplessivaSqlDAO(lConn);
				lPenCompSqlDao.ricercaPenaComplessivaByIdFascicolo(aFascID);
				lPenCompMod = (PenaComplessivaModel) lPenCompSqlDao.getModelByKey();
				lPenCompSqlDao.stop();
			}

			// ========================================================================
			// Devo recuperare le pena residua associata all'evento Iniziale
			// n.b. non nel caso in cui la pena iniziale sia la pena in sentenza, in
			// questo caso è già stata recuperata (non è associata a un evento)
			// Nel caso invece di pena da archiviazione devo ricercare l'ultima
			// pena validata prima dell'archiviazione
			// ========================================================================
			if (lCalcoloPenaMod.getTipoPenaIniziale() != ICostantiCalcoloPena.PENA_IN_SENTENZA) {
				// Attenzione al caso di Cumulo. Non sempre esiste una pena residua
				// associata al cumulo in questo caso va in null pointer
				// if ( lCalcoloPenaMod.getTipoPenaIniziale()==ICostantiCalcoloPena.PENA_ARCHIVIATA
				// || lCalcoloPenaMod.getTipoPenaIniziale()==ICostantiCalcoloPena.PENA_SOSPENSIONE_RES
				// || lCalcoloPenaMod.getTipoPenaIniziale()==ICostantiCalcoloPena.PENA_MANUALE_RES
				// )
				if (lPenaInizialeMod != null) {
					// // Nel caso di pena da archiviazione recupero l'ultima pena validata
					// // prima dell'archiviazione se esiste, altrimenti l'ultima pena migrata
					// // RES, alrimenti non è una pena iniziale
					// lPenaResSqlDao = new PenaResiduaSqlDAO(lConn);
					// lPenaResSqlDao.ricercaPenaDaArchiviazione(lEventoIniziale.getFasSieIdFascicoloSiep(),
					// lEventoIniziale.getDataInserimento());
					// lPenaInizialeMod = (PenaResiduaModel)lPenaResSqlDao.getModelByKey();
					// lPenaResSqlDao.stop();
				} else if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_IN_CUMULO) {
					// Nel caso di CUMULO vengono recuperati i quantum di pena direttamente
					// dalla PENA_CUMULO e non dal record PENA_RESIDUA in quanto non sempre
					// tale record è presente (err) e per pena migrate res con prosecuzione
					// provvisora il quantum sul record pena residua non è corretto.
					lPenaCumSqlDao = new PenaCumuloSqlDAO(lConn);
					// lPenaCumSqlDao.ricercaPenaCumuloByIdEvento(lEventoIniziale.getIdEvento());
					lPenaCumSqlDao.ricercaPenaCumuloByIdEvento(lEventoIniziale.getIdEvento(),
							lEventoIniziale.getFasSieIdFascicoloSiep());
					lPenaCumuloMod = (PenaCumuloModel) lPenaCumSqlDao.getModelByKey();
					;
					lPenaCumSqlDao.stop();
					// Scarico i quantum di pena cumulo su un model pena residua per avere
					// i dati omogenei
					lPenaInizialeMod = new PenaResiduaModel();
					lPenaInizialeMod.setIdPenaResidua(new BigDecimal(0)); // devo impostarlo per evitare
																			// l'errore
					lPenaInizialeMod.setNumGiorniReclusione(lPenaCumuloMod.getNumGiorniReclusione());
					lPenaInizialeMod.setNumMesiReclusione(lPenaCumuloMod.getNumMesiReclusione());
					lPenaInizialeMod.setNumAnniReclusione(lPenaCumuloMod.getNumAnniReclusione());
					lPenaInizialeMod.setImportoMulta(lPenaCumuloMod.getImportoMulta());

					lPenaInizialeMod.setNumGiorniArresto(lPenaCumuloMod.getNumGiorniArresto());
					lPenaInizialeMod.setNumMesiArresto(lPenaCumuloMod.getNumMesiArresto());
					lPenaInizialeMod.setNumAnniArresto(lPenaCumuloMod.getNumAnniArresto());
					lPenaInizialeMod.setImportoAmmenda(lPenaCumuloMod.getImportoAmmenda());
				} else {
					// In tutti gli altri casi recupero la pena associata all'evento
					lPenaResSqlDao = new PenaResiduaSqlDAO(lConn);
					lPenaResSqlDao.ricercaPenaResiduaByKeyEvento(lEventoIniziale.getIdEvento());
					lPenaInizialeMod = (PenaResiduaModel) lPenaResSqlDao.getModelByKey();
					lPenaResSqlDao.stop();
				}

				// Pena residua non trovata associata all'evento. Segnalo l'errore e
				// rilancio l'eccezione.
				if (lPenaInizialeMod == null || lPenaInizialeMod.getIdPenaResidua() == null) {
					String lTipoPenaIniziale = "";

					if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_IN_SENTENZA) {
						lTipoPenaIniziale = "Pena Irrogata in Sentenza";
					} else if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_IN_CUMULO) {
						lTipoPenaIniziale = "Pena Irrogata in Cumulo";
					} else if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_IN_CUMULO_NEW) {
						lTipoPenaIniziale = "Pena Irrogata in Cumulo (nuovo cumulo)";
					} else if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_SOSPENSIONE
							|| lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_SOSPENSIONE_RES) {
						lTipoPenaIniziale = "Pena Residua dopo Interruzione/Sospensione";
					} else if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_REVOCA_MA) {
						lTipoPenaIniziale = "Pena Residua dopo Revoca MA";
					} else if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_CESSAZIONE_MA) {
						lTipoPenaIniziale = "Pena Residua dopo Cessazione MA";
					} else if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_REVOCA_INDULTINO) {
						lTipoPenaIniziale = "Pena Residua dopo Revoca Indultino";
					} else if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_MANUALE) {
						lTipoPenaIniziale = "Pena Residua Manuale";
					} else if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_MANUALE_RES) {
						lTipoPenaIniziale = "Pena Residua da Forzatura RES";
					} else if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_ARCHIVIATA_RES
							|| lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_ARCHIVIATA_SIEP) {
						lTipoPenaIniziale = "Pena Residua dopo Archiviazione";
					} else if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_DA_INDULTO) {
						lTipoPenaIniziale = "Pena Residua dopo Scarcerazione Indulto";
					}

					throw new F3BException(
							F3BException.USER_MESSAGE,
							"Impossibile ricostruire la Pena da Espiare. Dati a sistema inconsistenti, manca pena residua associata all'evento di "
									+ lTipoPenaIniziale
									+ ". Utilizzare la funzione di Pena Residua Manuale o segnalare l'anomalia all'help desk.");
				}
			}

			// ========================================================================
			// Setto al Pena Iniziale sull'opportuno campo del CalcoloPenaModel
			// e il campo DataDal
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Evento Iniziale = " + lEventoIniziale);

			if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_NON_DEFINITA) {
				// Caso impossibile, almeno la 'Pena in Sentenza' deve esistere
			} else if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_IN_SENTENZA) {
				lCalcoloPenaMod.setPenaInSentenza(lPenCompMod);
			} else if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_IN_CUMULO) {
				lCalcoloPenaMod.setPenaIrrogataInCumulo(lPenaInizialeMod);
				// Uso la data validazione dell'evento in quanto< data agg pena residua
				// altrimenti non vengono considerate le LA.
				lCalcoloPenaMod.setDataDal(lEventoIniziale.getDataInserimento());
			} else if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_IN_CUMULO_NEW) {
				lCalcoloPenaMod.setPenaIrrogataInCumulo(lPenaInizialeMod);
				lCalcoloPenaMod.setDataDal(lEventoIniziale.getDataInserimento());
			} else if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_SOSPENSIONE
					|| lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_SOSPENSIONE_RES) {
				lCalcoloPenaMod.setPenaDopoSospensione(lPenaInizialeMod);
				// ATTENZIONE nel caso di Interruzione in realtà l'evento dopo la
				// validazione viene svalidato in fase di STAMPA e rivalidatao
				// con modifica della data aggiornamento. Tuttavia non possono
				// essere effettuate operazioni intermedie per cui va bene la
				// data_aggiornamento
				lCalcoloPenaMod.setDataDal(lEventoIniziale.getDataInserimento());
			} else if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_REVOCA_MA
					|| lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_CESSAZIONE_MA) {
				lCalcoloPenaMod.setPenaDopoRevocaMA(lPenaInizialeMod);
				lCalcoloPenaMod.setDataDal(lPenaInizialeMod.getDataInserimento());
			} else if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_REVOCA_INDULTINO) {
				lCalcoloPenaMod.setPenaDopoRevocaIndultino(lPenaInizialeMod);
				lCalcoloPenaMod.setDataDal(lPenaInizialeMod.getDataInserimento());
			} else if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_MANUALE
					|| lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_MANUALE_RES) {
				// n.b. la pena manuale viene inserita e validata contestualemnte all'evento
				//
				lCalcoloPenaMod.setPenaResiduaManuale(lPenaInizialeMod);
				lCalcoloPenaMod.setDataDal(lEventoIniziale.getDataInserimento());
			} else if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_DA_INDULTO) {
				lCalcoloPenaMod.setPenaDaIndulto(lPenaInizialeMod);
				lCalcoloPenaMod.setDataDal(lEventoIniziale.getDataInserimento());
			} else if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_ARCHIVIATA_RES
					|| lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_ARCHIVIATA_SIEP) {
				lCalcoloPenaMod.setPenaDaArchiviazione(lPenaInizialeMod);
				lCalcoloPenaMod.setDataDal(lEventoIniziale.getDataInserimento());
			} else if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_DA_REVOCA_SS) {
				lCalcoloPenaMod.setPenaDaRevocaSS(lPenaInizialeMod);
				lCalcoloPenaMod.setDataDal(lEventoIniziale.getDataInserimento());
			}

			// ========================================================================
			// Imposto evento Pena Iniziale se presente
			// ========================================================================
			if (lEventoIniziale != null && lEventoIniziale.getIdEvento() != null) {
				lCalcoloPenaMod.setEventoPenaIniziale(lEventoIniziale);
			}

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("ddd"+lCalcoloPenaMod.getPenaDaArchiviazione());

			// ========================================================================
			// Nel caso di Revoca Affidamento in prova o Indultino recupero la data
			// Revocata dal che viene visualizzata sulla form F5
			// ========================================================================
			if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_REVOCA_INDULTINO
					|| lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_REVOCA_MA
					|| lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_CESSAZIONE_MA) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Recupero data Revoca/Cessazione");

				// Recupero l'ordinanza di Revoca. La data Revocata Dal si trova sul
				// record MA collegato all'ordinanza
				EventoModel lEveOrd = null;
				lEveSqlDao.ricercaEventoByKey(lEventoIniziale.getEveIdEvento());
				lEveOrd = (EventoModel) lEveSqlDao.getModelByKey();
				lEveSqlDao.stop();

				if (lEveOrd != null && lEveOrd.getIdEvento() != null) {
					MisuraAlternativaModel lMisAltMod = null;
					lMisAltSqlDao = new MisuraAlternativaSqlDAO(lConn);
					lMisAltSqlDao.ricercaMisuraAlternativaByIdEvento(lEveOrd.getIdEvento());
					lMisAltMod = (MisuraAlternativaModel) lMisAltSqlDao.getModelByKey();
					lMisAltSqlDao.stop();

					if (lMisAltMod != null && lMisAltMod.getIdMisuraAlternativa() != null) {
						lCalcoloPenaMod.setDataRevocataDal(lMisAltMod.getDataInizioMisura());
					}
				}
			}
		} catch (DAOException daoEx) {
			throw new F3BException("CalcoloPenaControllerF5.exGetPenaIniziale: Non posso leggere : " + daoEx);
		} catch (F3BException fex) {
			if (fex.getErrorCode() == F3BException.USER_MESSAGE) {
				throw fex;
			} else {
				throw new F3BException("CalcoloPenaControllerF5.exGetPenaIniziale: Non posso leggere  : "
						+ fex);
			}
		} catch (Exception ex) {
			throw new F3BException("CalcoloPenaControllerF5.exGetPenaIniziale: Non posso leggere  : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lCumSqlDao);
			cleanup(lPenCompSqlDao);
			cleanup(lPenaResSqlDao);
			cleanup(lPenaCumSqlDao);
			cleanup(lMisAltSqlDao);

			cleanup(lConn);
		}

		return lCalcoloPenaMod;
	}

	/**
   * 
   */
	public Vector exGetBenefici(BeneficioModel aBeneficio) throws F3BException {
		Connection lConn = null;
		Vector lListaBenefici = new Vector();
		BeneficioSqlDAO lBenSqlDao = null;

		try {
			lConn = getDBConnection();

			lBenSqlDao = new BeneficioSqlDAO(lConn);

			lBenSqlDao.ricercaBeneficio(aBeneficio);
			lListaBenefici = new Vector(lBenSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException("CalcoloPenaControllerF5.exGetBenefici: Non posso leggere : " + daoEx);
		} catch (Exception e) {
			throw new F3BException("CalcoloPenaControllerF5.exGetBenefici: Non posso leggere  : " + e);
		} finally {
			cleanup(lBenSqlDao);
			cleanup(lConn);
		}
		return lListaBenefici;
	}

	/**
	 * Ritorna la Sanzione Sostitutiva Disposta dal Giudice in sentenza se presente
	 * 
	 * @param aPenaComplID
	 *            - Id Della Pena Complessiva
	 * @return la SS o null se non esiste
	 * @throws F3BException
	 */
	public SanzioneSostitutivaModel exGetSanzioneSostitutiva(BigDecimal aPenaComplID) throws F3BException {
		Connection lConn = null;

		SanzioneSostitutivaModel lSanzioneModel = null;

		SanzioneSostitutivaSqlDAO lSanzSqlDao = null;

		try {
			lConn = getDBConnection();

			lSanzSqlDao = new SanzioneSostitutivaSqlDAO(lConn);

			lSanzSqlDao.ricercaSanzioneSostitutivaByIdPenaComplessiva(aPenaComplID);

			lSanzioneModel = (SanzioneSostitutivaModel) lSanzSqlDao.getModelByKey();

		} catch (DAOException daoEx) {
			throw new F3BException("CalcoloPenaControllerF5.exGetSanzioneSostitutiva: Non posso leggere : "
					+ daoEx);
		} catch (Exception e) {
			throw new F3BException("CalcoloPenaControllerF5.exGetSanzioneSostitutiva: Non posso leggere  : "
					+ e);
		} finally {
			cleanup(lSanzSqlDao);

			cleanup(lConn);
		}

		return lSanzioneModel;

	}

	/**
	 * Recupera tutte le Annotazioni Manuali inserite in fase di <b>Richiesta al GE</b> solo quelle con
	 * anticipazione degli effetti. Vengono recuperati:<br>
	 * 
	 * - Depenalizzazione (004)<br>
	 * - Incostituzionalità (013)<br>
	 * - Amnistia (003)<br>
	 * - Indulto (002)<br>
	 * - Illecito Amministrativo (017)<br>
	 *
	 * Per distinguere le Richieste (con o senza anticipazione) dalle Decisioni viene utilizzato il
	 * FlagApprovazioneProvvisoria che nel caso delle Richieste vale R o A mentre nelle decisioni vale '-'
	 * 
	 * Una richiesta con anticipazione degli effetti va conputa nei calcoli solo se non è legata a una
	 * decisione del GE. In questo caso fanno fede i quantum inseriti con la decisione.
	 * 
	 * @param aFascID
	 *            idFascicolo
	 * @param aDataDal
	 * @param aDataAl
	 * @return Vettore di AnnotazioniManualiModel
	 */
	public Vector exGetRichiesteAlGE(BigDecimal aFascID, Date aDataDal, Date aDataAl) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Ricerco le Richieste al GE con anticipazione...");
		Connection lConn = null;
		Vector lListaRichiesteAlGE = new Vector();

		AnnotazioneManualeSqlDAO lAnnManSqlDao = null;

		try {
			lConn = getDBConnection();

			AnnotazioneManualeModel lAnnManMod = new AnnotazioneManualeModel();
			lAnnManMod.setFasSieIdFascicoloSiep(aFascID);
			lAnnManSqlDao = new AnnotazioneManualeSqlDAO(lConn);

			lAnnManSqlDao
					.ricercaAnnManualeRichiesteGEByIdFascicoloDateValidazione(aFascID, aDataDal, aDataAl);

			lAnnManSqlDao.start();

			while (lAnnManSqlDao.next()) {
				lAnnManMod = (AnnotazioneManualeModel) lAnnManSqlDao.getModel();

				lListaRichiesteAlGE.add(lAnnManMod);
			}
			lAnnManSqlDao.stop();

		} catch (DAOException daoEx) {
			throw new F3BException("CalcoloPenaControllerF5.exGetRichiesteAlGE: Non posso leggere : " + daoEx);
		} catch (Exception ex) {
			throw new F3BException("CalcoloPenaControllerF5.exGetRichiesteAlGE: Non posso leggere  : " + ex);
		} finally {
			cleanup(lAnnManSqlDao);

			cleanup(lConn);
		}

		return lListaRichiesteAlGE;
	}

	/**
	 * Recupera tutte le Annotazioni Manuali inserite con le <b>Decisioni Del GE</b> Vengono recuperati:<br>
	 * 
	 * - Depenalizzazione (004)<br>
	 * - Incostituzionalità (013)<br>
	 * - Amnistia (003)<br>
	 * - Indulto (002)<br>
	 * - Illecito Amministrativo (017)<br>
	 * 
	 * Per distinguere le Richieste (con o senza anticipazione) dalle Decisioni viene utilizzato il
	 * FlagApprovazioneProvvisoria che nel caso delle Richieste vale R o A mentre nelle decisioni vale '-'
	 * 
	 * @param aFascID
	 * @param aDataDal
	 * @param aDataAl
	 * @return Vettore di AnnotazioneManualeModel
	 */
	public Vector exGetDecisioniDelGE(BigDecimal aFascID, Date aDataDal, Date aDataAl) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Recupero le decisioni del GE...");
		Connection lConn = null;

		Vector lListaDecisioniDelGE = new Vector();

		AnnotazioneManualeSqlDAO lAnnManSqlDao = null;

		try {
			lConn = getDBConnection();

			AnnotazioneManualeModel lAnnManMod = new AnnotazioneManualeModel();
			lAnnManMod.setFasSieIdFascicoloSiep(aFascID);
			lAnnManSqlDao = new AnnotazioneManualeSqlDAO(lConn);

			// lAnnManSqlDao.ricercaAnnotazioneManualeByIdFascicoloDateIns(aFascID, aDataDal, aDataAl);
			lAnnManSqlDao
					.ricercaAnnManualeDecisioniGEByIdFascicoloDateValidazione(aFascID, aDataDal, aDataAl);

			lAnnManSqlDao.start();

			while (lAnnManSqlDao.next()) {
				lAnnManMod = (AnnotazioneManualeModel) lAnnManSqlDao.getModel();

				// if( lAnnManMod.getFlagAppProvvisoria() != null
				// && lAnnManMod.getFlagAppProvvisoria().equals("-") // Decisioni
				// && lAnnManMod.getFlagValidato() != null
				// && lAnnManMod.getFlagValidato().equals("S") // Solo quelle validate
				// && lAnnManMod.getCodTipoAnnotazione() != null
				// && ( lAnnManMod.getCodTipoAnnotazione().equals("004") // Depenalizzazione
				// || lAnnManMod.getCodTipoAnnotazione().equals("013") // Incostituzionalità
				// || lAnnManMod.getCodTipoAnnotazione().equals("003") // Amnistia
				// || lAnnManMod.getCodTipoAnnotazione().equals("002") // Indulto
				// )
				// )
				// {
				lListaDecisioniDelGE.add(lAnnManMod);
				// }
			}
			lAnnManSqlDao.stop();

		} catch (DAOException daoEx) {
			throw new F3BException("CalcoloPenaControllerF5.exGetDecisioniDelGE: Non posso leggere : "
					+ daoEx);
		} catch (Exception ex) {
			throw new F3BException("CalcoloPenaControllerF5.exGetDecisioniDelGE: Non posso leggere  : " + ex);
		} finally {
			cleanup(lAnnManSqlDao);

			cleanup(lConn);
		}

		return lListaDecisioniDelGE;
	}

	/**
	 * Recupera tutte le Annotazioni Manuali inserite con gli <b>Indulti Migrati RES</b> Vengono recuperati:<br>
	 * 
	 * - richieste con anticipazione (evento 0161 tipo annotazione 002, flag app_provv = 'A') - decisioni
	 * (evento 0284 tipo annotazione 002 flag app_provv = '-')<br>
	 * 
	 * n.b. vengono recuperati solo i dati iscritti res in ordine cronologico dal più recente
	 * 
	 * @param aFascID
	 * @param aDataDal
	 * @param aDataAl
	 * @return
	 * @throws F3BException
	 */
	public Vector exGetIndultiRES(BigDecimal aFascID, Date aDataDal, Date aDataAl) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Recupero gli Indulti RES...");
		Connection lConn = null;

		Vector lListaIndultiRES = new Vector();

		AnnotazioneManualeSqlDAO lAnnManSqlDao = null;

		try {
			lConn = getDBConnection();

			AnnotazioneManualeModel lAnnManMod = new AnnotazioneManualeModel();
			lAnnManSqlDao = new AnnotazioneManualeSqlDAO(lConn);

			lAnnManSqlDao.ricercaAnnManualeIndultiRESByIdFascicoloDateValidazione(aFascID, aDataDal, aDataAl);

			lAnnManSqlDao.start();

			while (lAnnManSqlDao.next()) {
				lAnnManMod = (AnnotazioneManualeModel) lAnnManSqlDao.getModel();

				lListaIndultiRES.add(lAnnManMod);
			}
			lAnnManSqlDao.stop();

		} catch (DAOException daoEx) {
			throw new F3BException("CalcoloPenaControllerF5.exGetIndultiRES: Non posso leggere : " + daoEx);
		} catch (Exception ex) {
			throw new F3BException("CalcoloPenaControllerF5.exGetIndultiRES: Non posso leggere  : " + ex);
		} finally {
			cleanup(lAnnManSqlDao);

			cleanup(lConn);
		}

		return lListaIndultiRES;
	}

	/**
	 * ************************************************************************** Recupera i computi iscritti
	 * e validati nell'intervallo di date specificato: - Presofferto Altro Reato (005-Pena Espiata per lo
	 * Stesso Titolo) - Fungibilità altro Reato - Misura Cautelare (006-Pena Espiata per Altro Titolo) - Pena
	 * Detentiva (007-Pena Espiata Senza Titolo) - Altro (014-Altro)
	 * 
	 * - Computi iscritti RES (evento '0162' di rideterminazione pena con cod tipo annotazione ='-'))
	 * 
	 * n.b. la data validazione coincide con la data di aggiornamento
	 * 
	 * @param lFascID
	 * @param dataDal
	 * @param dataAl
	 * @return Vettore di AnnotazioneManualeModel
	 ************************************************************************** */
	public Vector exGetComputi(BigDecimal aFascID, Date aDataDal, Date aDataAl) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Recupero i computi iscritti: dal "
				+ DateUtils.getDateToString(aDataDal, "dd/MM/yyyy") + " al "
				+ DateUtils.getDateToString(aDataAl, "dd/MM/yyyy)"));

		Connection lConn = null;

		Vector lListaComputi = new Vector();

		AnnotazioneManualeSqlDAO lAnnManSqlDao = null;

		try {
			lConn = getDBConnection();

			AnnotazioneManualeModel lAnnManMod = new AnnotazioneManualeModel();
			lAnnManMod.setFasSieIdFascicoloSiep(aFascID);
			lAnnManSqlDao = new AnnotazioneManualeSqlDAO(lConn);

			lAnnManSqlDao.ricercaAnnManualeComputiByIdFascicoloDataValidazione(aFascID, aDataDal, aDataAl);

			lAnnManSqlDao.start();

			while (lAnnManSqlDao.next()) {
				lAnnManMod = (AnnotazioneManualeModel) lAnnManSqlDao.getModel();

				// if( lAnnManMod.getFlagValidato() != null
				// && lAnnManMod.getFlagValidato().equals("S") // Solo quelle validate
				// && lAnnManMod.getCodTipoAnnotazione() != null
				// && ( lAnnManMod.getCodTipoAnnotazione().equals("005") // Pena Espiata per lo Stesso Titolo
				// (Presofferto)
				// || lAnnManMod.getCodTipoAnnotazione().equals("006") // Pena Espiata per Altro Titolo
				// (Fungibilità)
				// || lAnnManMod.getCodTipoAnnotazione().equals("007") // Pena Espiata Senza Titolo
				// (Fungibilità)
				// || lAnnManMod.getCodTipoAnnotazione().equals("014") // Altro
				// )
				// )
				// {
				lListaComputi.add(lAnnManMod);
				// }
			}
			lAnnManSqlDao.stop();

		} catch (DAOException daoEx) {
			throw new F3BException("CalcoloPenaControllerF5.exGetComputi: Non posso leggere : " + daoEx);
		} catch (Exception ex) {
			throw new F3BException("CalcoloPenaControllerF5.exGetComputi: Non posso leggere  : " + ex);
		} finally {
			cleanup(lAnnManSqlDao);

			cleanup(lConn);
		}

		return lListaComputi;
	}

	/**
	 * ************************************************************************** Recupera le LA iscritte
	 * nell'intervallo di date specificato: n.b. le LA computabili sono solo quelle collegate a un evento
	 * SIEP, vale: - a dire a una Comunicazione (12) nel caso in cui la LA sia stata acquisita per in
	 * condannato libero - a un ordine di scarcerazione per rideterminazione pena, nel caso in cui le LA sono
	 * state concesse a un condannato detenuto, per cui sono state utilizzate per anticipare il fine pena - a
	 * un cumulo - a una pena residua manuale - Liberazione Manuale Anticipata ????
	 * 
	 * @param lFascID
	 * @param dataDal
	 * @param dataAl
	 * @return Vettore di LicenzaLibAnticipataModel
	 ************************************************************************** */
	public Vector exGetLiberazioneAnticipata(BigDecimal aFascID, Date aDataDal, Date aDataAl)
			throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Recupero le LA iscritte: dal " + DateUtils.getDateToString(aDataDal, "dd/MM/yyyy")
				+ " al " + DateUtils.getDateToString(aDataAl, "dd/MM/yyyy)"));

		Connection lConn = null;

		Vector lListaLA = new Vector();

		LicenzaLibanticipataSqlDAO lLASqlDao = null;

		try {
			lConn = getDBConnection();

			LicenzaLibAnticipataModel lLAMod = new LicenzaLibAnticipataModel();
			lLAMod.setFasSieIdFascicoloSiep(aFascID);

			lLASqlDao = new LicenzaLibanticipataSqlDAO(lConn);

			lLASqlDao.ricercaLAComputabiliByIdFascicoloDataValidazione(aFascID, aDataDal, aDataAl);

			lLASqlDao.start();

			while (lLASqlDao.next()) {
				lLAMod = (LicenzaLibAnticipataModel) lLASqlDao.getModel();
				lListaLA.add(lLAMod);
			}
			lLASqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("CalcoloPenaControllerF5.exGetLiberazioneAnticipata: Non posso leggere : "
					+ daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					"CalcoloPenaControllerF5.exGetLiberazioneAnticipata: Non posso leggere  : " + ex);
		} finally {
			cleanup(lLASqlDao);

			cleanup(lConn);
		}

		return lListaLA;
	}

	/**
	 * Recupera tutte le annotazioni manuali inserite su un certo fascicolo applicando i filtri specificati in
	 * input. Le Annotazioni sono quelle utilizzabili ai fini del calcolo della pena Richieste al GE (con
	 * anticipazione) aFlagAppProvvisoria = 'A' - Depenalizzazione (004)<br>
	 * - Incostituzionalità (013)<br>
	 * - Amnistia/Indulto (003/002)<br>
	 * - Illecito Amministrativo (017)<br>
	 * 
	 * Decisioni del GE - Depenalizzazione (004)<br>
	 * - Incostituzionalità (013)<br>
	 * - Amnistia/Indulto (003/002)<br>
	 * - Illecito Amministrativo (017)<br>
	 * 
	 * Computi - Presofferto Altro Reato (005-Pena Espiata per lo Stesso Titolo) - Fungibilità altro Reato -
	 * Misura Cautelare (006-Pena Espiata per Altro Titolo) - Pena Detentiva (007-Pena Espiata Senza Titolo) -
	 * Altro (014-Altro)
	 *
	 * @param aFascID
	 * @param aCodTipoAnnotazione
	 * @param aFlagAppProvvisoria
	 *            ('A' = richieste al GE, '-' altro)
	 * @param aFlagConcesseRevocate
	 *            ('+' = revocate, '-' = concesse) per ora non usato
	 * 
	 * @return
	 * @throws F3BException
	 */
	public Vector exGetAnnotazioniManualiDaComputare(BigDecimal aFascID, String aCodTipoAnnotazione,
			String aFlagAppProvvisoria, String aFlagConcesseRevocate) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		Connection lConn = null;
		Vector lListaAnnotazioni = new Vector();

		AnnotazioneManualeSqlDAO lAnnManSqlDao = null;

		try {
			lConn = getDBConnection();

			AnnotazioneManualeModel lAnnManMod = new AnnotazioneManualeModel();
			lAnnManMod.setFasSieIdFascicoloSiep(aFascID);
			lAnnManSqlDao = new AnnotazioneManualeSqlDAO(lConn);

			// Recupero tutte le Annotazioni
			lAnnManSqlDao.ricercaAnnotazioneManualeByIdFascicolo(aFascID);

			lAnnManSqlDao.start();

			// ========================================================================
			// Applico i filtri
			// ========================================================================
			while (lAnnManSqlDao.next()) {
				lAnnManMod = (AnnotazioneManualeModel) lAnnManSqlDao.getModel();

				if (lAnnManMod.getFlagPiuMeno() != null
						// && lAnnManMod.getFlagPiuMeno().equals(aFlagConcesseRevocate) // concessi/revocate
						&& lAnnManMod.getFlagAppProvvisoria() != null
						&& lAnnManMod.getFlagAppProvvisoria().equals(aFlagAppProvvisoria) // con anticipazione
						&& lAnnManMod.getCodTipoAnnotazione() != null
						&& (lAnnManMod.getFlagValidato() == null || (lAnnManMod.getFlagValidato() != null && lAnnManMod
								.getFlagValidato().equals("N")))) {
					// Vengono considerate solo annotazioni dello stesso tipo di
					// aCodTipoAnnotazione non validate.
					// n.b. AMNISTIA/INDULTO vengono considerata insieme in quanto possono
					// essere inserite contemporaneamente
					// 002 = Indulto
					// 003 = Amnistia

					// if (aValidate.equals("S")

					if ("002".equals(aCodTipoAnnotazione) || "003".equals(aCodTipoAnnotazione)) {
						if ("002".equals(lAnnManMod.getCodTipoAnnotazione())
								|| "003".equals(lAnnManMod.getCodTipoAnnotazione())) {
							lListaAnnotazioni.add(lAnnManMod);
						}
					} else if (lAnnManMod.getCodTipoAnnotazione().equals(aCodTipoAnnotazione)) {
						lListaAnnotazioni.add(lAnnManMod);
					}
				}
			}

			lAnnManSqlDao.stop();

		} catch (DAOException daoEx) {
			throw new F3BException(
					"CalcoloPenaControllerF5.exGetAnnotazioniManualiDaComputare: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					"CalcoloPenaControllerF5.exGetAnnotazioniManualiDaComputare: Non posso leggere  : " + ex);
		} finally {
			cleanup(lAnnManSqlDao);

			cleanup(lConn);
		}

		return lListaAnnotazioni;
	}

	/**
	 * Verifica se l'evento passato in input è relativo a un Cumulo e può essere considerato come Pena
	 * Iniziale. n.b. il metodo non verifica se l'evento è validato o annullato n.b. Il cumulo può essere
	 * sempre considaretao come pena iniziale
	 * 
	 * @param aEveModel
	 * @return
	 */
	private boolean isCumulo(EventoModel aEveModel) {
		boolean isCumulo = false;

		if (aEveModel.getCodTipoEvento() != null && aEveModel.getCodTipoProvvedimento() != null
				&& aEveModel.getCodMotivo() != null && aEveModel.getCodTipoEvento().equals("01") // 01-Provvedimento
				&& aEveModel.getCodTipoProvvedimento().equals("04") // 04-Provvedimento
				&& (aEveModel.getCodMotivo().equals("0222") // 0222-di unificazione di pene concorrenti (con
															// contestuale Ordine di Esecuzione Condannato
															// Libero)
						|| aEveModel.getCodMotivo().equals("0223") // 0223-di unificazione di pene concorrenti
																	// (da espiarsi in regime di detenzione)
						|| aEveModel.getCodMotivo().equals("0224") // 0224-di unificazione di pene concorrenti
																	// (da espiarsi in regime di misura
																	// alternativa)
				// || aEveModel.getCodMotivo().equals("0225") // 0225-di unificazione di pene concorrenti (con
				// contestuale Decreto di Sospensione ex art. 656 Comma 5° CPP)
				|| aEveModel.getCodMotivo().equals("0277") // 0277-di unificazione di pene concorrenti
															// (generico)
				)) {
			// Attenzione!! In alcuni casi, per motivi ignoti, non esiste una pena
			// associata al cumulo
			isCumulo = true;
		}

		return isCumulo;
	}

	/**
	 * Nuovi codici Cumulo MEV26 per ora compresi tra 0630 e 0651
	 * 
	 * @param aEveModel
	 * @return
	 */
	private boolean isNuovoCumulo(EventoModel aEveModel) {
		boolean isNuovoCumulo = false;

		if (aEveModel.getCodTipoEvento() != null && aEveModel.getCodTipoProvvedimento() != null
				&& aEveModel.getCodMotivo() != null && aEveModel.getCodTipoEvento().equals("01") // 01-Provvedimento
				&& aEveModel.getCodTipoProvvedimento().equals("04") // 04-Provvedimento
				&& ModuloCumuloUtils.isCumulo(aEveModel.getCodMotivo())
		// && ( Integer.parseInt(aEveModel.getCodMotivo())>=630
		// && Integer.parseInt(aEveModel.getCodMotivo())<=651
		// )
		) {
			isNuovoCumulo = true;
		}

		return isNuovoCumulo;
	}

	/**
	 * Verifica se l'evento passato in input è relativo a una Sospensione SIEP, e può essere considerato come
	 * Pena Iniziale. n.b. il metodo non verifica se l'evento è validato o annullato
	 * 
	 * @param aEveModel
	 * @return
	 */
	private boolean isSospensione(EventoModel aEveModel) throws F3BException {
		boolean isSospensione = false;
		Connection lConn = null;

		SospensioneSqlDAO lSospSqlDao = null;

		// ==========================================================================
		// Se è un evento migrato esco subito. Le sospensioni migrate vengono gestite
		// da apposito metodo
		// ==========================================================================
		if (aEveModel.getCodOperatoreInserimento().indexOf("res") != -1) {
			return false;
		}

		// ========================================================================
		// Le sospensioni possono essere:
		// - Sospensione del GE
		// - Sospensioni del PM
		// - Decisioni della Sorveglianza
		//
		// ========================================================================
		try {
			// ========================================================================
			// --> Sospensione del GE <--
			// Tipo Evento: 01 = Provvedimento
			// Tipo Provvedimento: 09 = Ordine di Scarcerazione, 12 = Comunicazione
			// Motivo: 0265 = Sospensione Esecuzione pena detentiva
			// Attenzione! La pena residua viene sempre inserita (S) ma il ricalcolo viene
			// fatto solo nel caso di !isLibero() e non Ergastolo. Per cui solo
			// in questo caso può costituire una pena iniziale. Il tipo di
			// provvedimento emesso (12,09) dipende invece dal flag
			// scarcerato/da scarcerare per cui non fornisce indicazioni sul
			// fatto che la pena sia stata ricalcolata o meno.
			// ========================================================================
			if (aEveModel.getCodTipoEvento() != null
					&& aEveModel.getCodTipoProvvedimento() != null
					&& aEveModel.getCodMotivo() != null
					&& aEveModel.getCodTipoEvento().equals("01")
					&& (aEveModel.getCodTipoProvvedimento().equals("09") || aEveModel
							.getCodTipoProvvedimento().equals("12"))
					&& aEveModel.getCodMotivo().equals("0265")) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Sospensione del GE");
				// Devo verificare se è stato effettuato un calcolo della pena in fase di
				// sospensione
				// n.b. devo utilizzare getEveIdEvento e non direttamente IdEvento,
				// perchè in questo caso la sospensione è legata all'ordinanza e
				// non direttamente al provvedimento
				lConn = getDBConnection();
				lSospSqlDao = new SospensioneSqlDAO(lConn);

				lSospSqlDao.ricercaSospensionePerPenaIniziale(aEveModel.getEveIdEvento());
				SospensioneModel lSospMod = (SospensioneModel) lSospSqlDao.getModelByKey();
				lSospSqlDao.stop();

				if (lSospMod != null)
					isSospensione = true;
			}
			// ========================================================================
			// --> Sospensioni del PM <--
			// Tipo Evento: 01 = Provvedimento
			// Tipo Provvedimento: 09 = Ordine di Scarcerazione, 04 = Provvedimento
			// Motivo:
			// 0900 Sospensione esecuzione della pena detentiva ex art. 91 DPR 309/90
			// 0901 Sospensione esecuzione della pena detentiva ex art. 94 DPR 309/90
			// 0902 Sospensione esecuzione della pena detentiva per dubbio sull'identità fisica della persona
			// detenuta ex art. 667 c.p.p.
			// 0903 Sospensione esecuzione della pena detentiva
			// 0937 Sospensione Esecuzione Contro Persona Condannata per Errore di Nome (Art. 668 C.P.P.) new
			// inserito il 17/10/2007
			// Attenzione! La pena residua viene sempre inserita (S) ma il ricalcolo viene
			// fatto solo nel caso di !isLibero() e non Ergastolo. Per cui solo
			// in questo caso può costituire una pena iniziale. Il tipo di
			// provvedimento emesso (04,09) dipende invece dal flag
			// scarcerato/da scarcerare per cui non fornisce indicazioni sul
			// fatto che la pena sia stata ricalcolata o meno.
			// ========================================================================
			else if (aEveModel.getCodTipoEvento() != null
					&& aEveModel.getCodTipoProvvedimento() != null
					&& aEveModel.getCodMotivo() != null
					&& aEveModel.getCodTipoEvento().equals("01")
					&& (aEveModel.getCodTipoProvvedimento().equals("09") || aEveModel
							.getCodTipoProvvedimento().equals("04"))
					&& (aEveModel.getCodMotivo().equals("0900") || aEveModel.getCodMotivo().equals("0901")
							|| aEveModel.getCodMotivo().equals("0902")
							|| aEveModel.getCodMotivo().equals("0903") || aEveModel.getCodMotivo().equals(
							"0937"))) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Sospensione del PM");
				// Devo verificare se è stato effettuato un calcolo della pena in fase di
				// sospensione
				// n.b. utilizo IdEvento, perchè in questo caso la sospensione è legata
				// direttamente al provvedimento
				lConn = getDBConnection();
				lSospSqlDao = new SospensioneSqlDAO(lConn);

				lSospSqlDao.ricercaSospensionePerPenaIniziale(aEveModel.getIdEvento());
				SospensioneModel lSospMod = (SospensioneModel) lSospSqlDao.getModelByKey();
				lSospSqlDao.stop();

				if (lSospMod != null)
					isSospensione = true;

			}
			// ========================================================================
			// --> Decisioni della Sorveglianza <--
			// Tipo Evento: 01 = Provvedimento
			// Tipo Provvedimento: 09 = Ordine di Scarcerazione, 12 = Comunicazione
			// Motivo:
			// 0263-Sospensione esecuzione pena detentiva art.47/4 O.P.
			// 0241-Sospensione esecuzione pena detentiva ex artt. 90 e 91/4 DPR 09.10.1990 n. 309
			//
			// ========================================================================
			else if (aEveModel.getCodTipoEvento() != null
					&& aEveModel.getCodTipoProvvedimento() != null
					&& aEveModel.getCodMotivo() != null
					&& aEveModel.getCodTipoEvento().equals("01")
					&& (aEveModel.getCodTipoProvvedimento().equals("09") || aEveModel
							.getCodTipoProvvedimento().equals("12") // se già liberato dalla Sorveglianza
					) && (aEveModel.getCodMotivo().equals("0263") || aEveModel.getCodMotivo().equals("0241"))) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Sospensione della Sorveglianza");
				// Devo verificare se è stato effettuato un calcolo della pena in fase di
				// sospensione
				// n.b. utilizo IdEvento, perchè in questo caso la sospensione è legata
				// direttamente al provvedimento
				lConn = getDBConnection();
				lSospSqlDao = new SospensioneSqlDAO(lConn);

				lSospSqlDao.ricercaSospensionePerPenaIniziale(aEveModel.getIdEvento());
				SospensioneModel lSospMod = (SospensioneModel) lSospSqlDao.getModelByKey();
				lSospSqlDao.stop();

				if (lSospMod != null)
					isSospensione = true;
			}
		} catch (DAOException daoEx) {
			throw new F3BException("CalcoloPenaControllerF5.isSospensione: Non posso leggere : " + daoEx);
		} catch (Exception ex) {
			throw new F3BException("CalcoloPenaControllerF5.isSospensione: Non posso leggere  : " + ex);
		} finally {
			cleanup(lSospSqlDao);

			cleanup(lConn);
		}

		return isSospensione;
	}

	/**
	 * Verifica se l'evento passato in input è relativo a un Differimento e può essere considerato come Pena
	 * Iniziale. n.b. il metodo non verifica se l'evento è validato o annullato
	 * 
	 * Attenzione!! Per il differimento esistono 4 casi: - SIEP nuova versione (dalla 2.0 01/02/2007) - SIEP
	 * vecchia versione - RES nuova versione - RES vecchia versione
	 * 
	 * Non sempre il differimento interrompe la pena. Il differimento da Libero o assimilati non interrompe la
	 * pena. Il problema è che non è agevole verificare se il differimento ha interrotto o meno la pena in
	 * quanto si dovrebbe far riferimento a strutture dati differenti e dati scritti in modo diverso. Per
	 * questo motivo si è deciso di considerare sempre il differimento come interruzione. I problemi che ne
	 * derivano sono: - SIEP: in presenza di LA iscritte prima del differimento e non eseguite (libero). In
	 * questo caso le LA vanno perse. Caso tuttavia estremamente raro. - RES: differimenti con pena residua
	 * migrata senza quantum. In questo caso infatti la pena residua da cui ripartiranno i calcoli risulta 0.
	 * In questo caso si potrebbe verificare se esistono i quantum sulla pena e in caso negativo non
	 * considerarla come pena iniziale. Che senso ha differire la pena a un soggetto che non ha un residuo da
	 * espiare?
	 * 
	 * n.b. i differimenti non sono eventi frequenti per cui i casi di errato calcolo dovrebbero essere
	 * contenuti.
	 * 
	 * @param aEveModel
	 * @return
	 */
	private boolean isDifferimento(EventoModel aEveModel) throws F3BException {
		boolean isDifferimento = false;

		Connection lConn = null;

		PenaResiduaSqlDAO lPenResSqlDao = null;
		PenaResiduaModel lPenResMod = null;

		if (aEveModel.getCodTipoEvento() != null
				&& aEveModel.getCodTipoProvvedimento() != null
				&& aEveModel.getCodMotivo() != null
				&& aEveModel.getCodTipoEvento().equals("01")
				&& (aEveModel.getCodTipoProvvedimento().equals("09")
						|| aEveModel.getCodTipoProvvedimento().equals("12") || aEveModel
						.getCodTipoProvvedimento().equals("04") // n.b. il cod. 04 è previsto solo nel nuovo
																// diff. per le posizioni giuridiche non
																// gestite
				) && (aEveModel.getCodMotivo().equals("0274") || aEveModel.getCodMotivo().equals("0221"))) {
			// Differimento Provvisorio
			// Tipo Evento: 01 = Provvedimento
			// Tipo Provvedimento: 12 = Comunicazione, 09 = Ordine scarcerazione
			// Codice Motivo: 0274 = Rinvio provvisorio dell'esecuzione ex art.684 c.2 c.p.p.
			//
			// Differimento Definitivo
			// Tipo Evento: 01 = Provvedimento
			// Tipo Provvedimento: 12 = Comunicazione, 09 = Ordine scarcerazione
			// Codice Motivo: 0221 = rinvio dell'esecuzione ex art. 684 c.1 c.p.p.
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Differimento");

			if (aEveModel.getCodOperatoreInserimento().indexOf("res") == -1) {
				// Iscritto siep
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Differimento SIEP");
				isDifferimento = true;
			} else {
				// Iscritto RES verifico la presenza dei quantum di pena
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Differimento RES");
				try {
					lConn = getDBConnection();

					lPenResSqlDao = new PenaResiduaSqlDAO(lConn);

					lPenResSqlDao.ricercaPenaResiduaByKeyEvento(aEveModel.getIdEvento());
					lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
					lPenResSqlDao.stop();

					if (lPenResMod != null
							&& (CalendarUtil.getTotGiorni(lPenResMod.getQuantumReclusione()) > 0 || CalendarUtil
									.getTotGiorni(lPenResMod.getQuantumArresto()) > 0)) { // Quatum positivi
																							// può essere
																							// considerato un
																							// evento
																							// interruttivo
																							// [FT] -
																							// 03/08/2016 -
																							// MAC_LOG -
																							// Utilizzo la
																							// variabile di
																							// istanza
																							// siesLogger al
																							// posto di
																							// LogF3B.getLogger()
						siesLogger.debug("Differimento RES OK quantum positivi");
						isDifferimento = true;
					} else {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Differimento RES NOT OK quantum nulli. Scarto l'evento");
						isDifferimento = false;
					}
				} catch (DAOException daoEx) {
					throw new F3BException("CalcoloPenaControllerF5.isDifferimento: Non posso leggere : "
							+ daoEx);
				} catch (Exception ex) {
					throw new F3BException("CalcoloPenaControllerF5.isDifferimento: Non posso leggere  : "
							+ ex);
				} finally {
					cleanup(lPenResSqlDao);

					cleanup(lConn);
				}
			}
		}

		return isDifferimento;
	}

	/**
	 * Verifica se l'evento passato in input è relativo a un Interruzione e può essere considerato come Pena
	 * Iniziale. n.b. il metodo non verifica se l'evento è validato o annullato
	 * 
	 * @param aEveModel
	 * @return
	 */
	private boolean isInterruzione(EventoModel aEveModel) throws F3BException {
		boolean isInterruzione = false;
		Connection lConn = null;

		SospensioneSqlDAO lSospSqlDao = null;

		// ==========================================================================
		// Se è un evento migrato esco subito. Le Interruzioni migrate vengono gestite
		// da apposito metodo
		// ==========================================================================
		if (aEveModel.getCodOperatoreInserimento().indexOf("res") != -1) {
			return false;
		}

		// ==========================================================================
		// Interruzione
		// Tipo Evento: 01 = Provvedimento
		// Tipo Provvedimento: 12 = Comunicazione, o 25 = Annotazione
		// Motivo Evento:
		// 0266 = avvenuto decesso
		// 0267 = avvenuta evasione
		// 0268 = consegna temporanea art. 709 comma 1c.p.p.
		// 0269 = esecuzione penale all'estero della condanna ex art. 742 c.p.p.
		// 0270 = interruzione della esecuzione della pena
		// 0366 = Scarcerazione provvisoria ex art. 672 co. 3 c.p.p. (new 4.0 upd 01)
		// Attenzione!!! ai casi 0268 e 0269 che non determinano una nuova pena
		// ma la pena resta in espiazione
		// Negli altri casi la pena residua non viene sempre ricalcolata, ma solo se
		// !isLibero(), non ergastolo. Il record PENA_RESIDUA viene SEMPRE inserito,
		// eventualmente copiando l'ultima pena residua. Per verificare se l'interruzione
		// ha determinato un calcolo è necessario verificare che:
		// - esista un record SOSPENSIONE associato alla PENA_RESIDUA
		// - siano <>0 i campi NUM_xxx_PENA_ESPIATA
		// ==========================================================================
		try {
			if (aEveModel.getCodTipoEvento() != null
					&& aEveModel.getCodTipoEvento().equals("01")
					&& aEveModel.getCodTipoProvvedimento() != null
					&& (aEveModel.getCodTipoProvvedimento().equals("12") || aEveModel
							.getCodTipoProvvedimento().equals("25"))
					&& aEveModel.getCodMotivo() != null
					&& (aEveModel.getCodMotivo().equals("0266") || aEveModel.getCodMotivo().equals("0267")
							|| aEveModel.getCodMotivo().equals("0270") || aEveModel.getCodMotivo().equals(
							"0366"))) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Trovata Interruzione");

				lConn = getDBConnection();
				lSospSqlDao = new SospensioneSqlDAO(lConn);
				// Devo verificare se è stato effettuato un calcolo della pena in fase di
				// interruzione
				lSospSqlDao.ricercaInterruzionePerPenaIniziale(aEveModel.getIdEvento());
				SospensioneModel lSospMod = (SospensioneModel) lSospSqlDao.getModelByKey();
				lSospSqlDao.stop();

				if (lSospMod != null)
					isInterruzione = true;
			}
		} catch (DAOException daoEx) {
			throw new F3BException("CalcoloPenaControllerF5.isInterruzione: Non posso leggere : " + daoEx);
		} catch (Exception ex) {
			throw new F3BException("CalcoloPenaControllerF5.isInterruzione: Non posso leggere  : " + ex);
		} finally {
			cleanup(lSospSqlDao);

			cleanup(lConn);
		}

		return isInterruzione;
	}

	/**
	 * Verifica se l'evento passato in input è relativo a un Interruzione Migrata RES e può essere considerato
	 * come Pena Iniziale. n.b. il metodo non verifica se l'evento è validato o annullato
	 * 
	 * @param aEveModel
	 * @return
	 */
	private boolean isInterruzioneRES(EventoModel aEveModel) throws F3BException {
		boolean isInterruzione = false;

		// ==========================================================================
		// Interruzione RES
		// Tipo Evento: 01 = Provvedimento
		// Tipo Provvedimento: 12 = Comunicazione, o 25 = Annotazione
		// Motivo Evento:
		// 0266 = avvenuto decesso (mai trovato in RES)
		// 0267 = avvenuta evasione (solo 12 = Comunicazione)
		// 0270 = interruzione della esecuzione della pena è associato ad un evento
		// 25 ma ha sempre i quantum nulli sia in PENA_RESIDUA che in
		// SOSPENSIONE per cui non lo considero nel calcolo della pena
		// 01-25-0366 Interruzioni per indulto migrate RES
		// ==========================================================================
		if (aEveModel.getCodOperatoreInserimento().indexOf("res") != -1
				&& aEveModel.getCodTipoEvento() != null
				&& aEveModel.getCodTipoProvvedimento() != null
				&& aEveModel.getCodMotivo() != null
				&& aEveModel.getCodTipoEvento().equals("01")
				&& ((aEveModel.getCodTipoProvvedimento().equals("12") && aEveModel.getCodMotivo().equals(
						"0267")) || (aEveModel.getCodTipoProvvedimento().equals("25") && aEveModel
						.getCodMotivo().equals("0366")))) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Trovata Interruzione RES");
			isInterruzione = true;
		}

		return isInterruzione;
	}

	/**
	 * Verifica se l'evento passato in input è relativo a una RevocaMA e può essere considerato come Pena
	 * Iniziale. Viene considerata solo la revoca Affidamento in prova n.b. il metodo non verifica se l'evento
	 * è validato o annullato
	 * 
	 * @param aEveModel
	 * @return
	 */
	private boolean isRevocaMA(EventoModel aEveModel) throws F3BException {
		boolean isRevocaMA = false;
		// ==========================================================================
		// Revoca Affidamento in Prova
		// Tipo Evento: 01 = Provvedimento
		// Tipo Provvedimento: 06 = Ordine di Esecuzione
		// Motivo Evento:
		// 0086 Revoca Affidamento in prova art. 47 quater o.p.
		// 0014 Revoca Affidamento in Prova al CSSA
		// 0015 Revoca Affidamento in casi particolari
		// ATTENZIONE!!! Per le posizioni giuridiche non gestite viene emesso un
		// provvedimento generico(01-04-0000) ma in questo caso non
		// è stato effettuato il ricalcolo della pena per cui non può
		// essere considerato come Pena Iniziale
		// ==========================================================================
		if (aEveModel.getCodTipoEvento() != null
				&& aEveModel.getCodTipoProvvedimento() != null
				&& aEveModel.getCodMotivo() != null
				&& aEveModel.getCodTipoEvento().equals("01")
				&& aEveModel.getCodTipoProvvedimento().equals("06")
				&& (aEveModel.getCodMotivo().equals("0086") || aEveModel.getCodMotivo().equals("0014") || aEveModel
						.getCodMotivo().equals("0015"))) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Trovata Revoca Affidamento in prova");
			isRevocaMA = true;
		}

		// MEV29 - marzo 2015 - la Revoca Espiazione presso il domicilio non veniva
		// considerata come evento di pena iniziale
		if (aEveModel.getCodTipoEvento() != null && aEveModel.getCodTipoProvvedimento() != null
				&& aEveModel.getCodMotivo() != null && aEveModel.getCodTipoEvento().equals("01")
				&& aEveModel.getCodTipoProvvedimento().equals("06")
				&& (aEveModel.getCodMotivo().equals("0316") // Revoca esecuzione presso domicilio della pena
															// detentiva (TDS)
				|| aEveModel.getCodMotivo().equals("2640") // Revoca esecuzione presso domicilio della pena
															// detentiva (MDS)
				)) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Trovata Revoca Espiazione Presso il Domicilio");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Verifico se presente record Sospensione...");

			Connection lConn = null;

			SospensioneSqlDAO lSospSqlDao = null;

			try {
				lConn = getDBConnection();
				lSospSqlDao = new SospensioneSqlDAO(lConn);

				lSospSqlDao.ricercaSospensionePerPenaIniziale(aEveModel.getIdEvento());
				SospensioneModel lSospMod = (SospensioneModel) lSospSqlDao.getModelByKey();
				lSospSqlDao.stop();

				if (lSospMod != null
						&& (lSospMod.getNumAnniPenaEspiata().intValue() > 0
								|| lSospMod.getNumMesiPenaEspiata().intValue() > 0 || lSospMod
								.getNumGiorniPenaEspiata().intValue() > 0))
					isRevocaMA = true;
				else
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Sospensione assente, la pena non è stata rideterminata");

			} catch (DAOException daoEx) {
				throw new F3BException("CalcoloPenaControllerF5.isRevocaMA: Non posso leggere : " + daoEx);
			} catch (Exception ex) {
				throw new F3BException("CalcoloPenaControllerF5.isRevocaMA: Non posso leggere  : " + ex);
			} finally {
				cleanup(lSospSqlDao);

				cleanup(lConn);
			}
		}

		return isRevocaMA;
	}

	/**
	 * Verifica se l'evento passato in input è relativo a una CessazioneMA e può essere considerato come Pena
	 * Iniziale.
	 * 
	 * n.b. il metodo non verifica se l'evento è validato o annullato
	 * 
	 * @param aEveModel
	 * @return
	 * @since 04/2014
	 */
	private boolean isCessazioneMA(EventoModel aEveModel) throws F3BException {
		boolean isCessazioneMA = false;
		// ==========================================================================
		// Cessazione Misura di Sicurezza
		// Tipo Evento: 01 = Provvedimento
		// Tipo Provvedimento: 06 = Ordine di Esecuzione, 12 = comunicazione
		// Motivo Evento:
		// ATTENZIONE!!! Per le posizioni giuridiche non gestite viene emesso un
		// provvedimento generico(01-04-0000) ma in questo caso non
		// è stato effettuato il ricalcolo della pena per cui non può
		// essere considerato come Pena Iniziale
		// ==========================================================================
		List<String> lCodiciCessTDSOrd = Arrays.asList("0167", "0168", "0166", "0024", "0110", "0111",
				"0112", "0169", "0172");
		List<String> lCodiciCessTDS51bis = Arrays.asList("5450", "5451", "5452", "5453", "5454", "5455",
				"5456", "5457", "5458", "5459");
		List<String> lCodiciCessMDS51bis = Arrays.asList("5430", "5431", "5432", "5433", "5434", "5435",
				"5436", "5437", "5438", "5439", "5442");

		if (aEveModel.getCodTipoEvento() != null && aEveModel.getCodTipoProvvedimento() != null
				&& aEveModel.getCodMotivo() != null) {
			if (aEveModel.getCodTipoEvento().equals("01") && aEveModel.getCodTipoProvvedimento().equals("06")
					&& lCodiciCessTDSOrd.contains(aEveModel.getCodMotivo())) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Trovata Cessazione MA TDS Ordinaria");
				isCessazioneMA = true;
			} else if (aEveModel.getCodTipoEvento().equals("01")
					&& aEveModel.getCodTipoProvvedimento().equals("06")
					&& lCodiciCessTDS51bis.contains(aEveModel.getCodMotivo())) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Trovata Cessazione MA TDS 51bis su reclamo PM");
				isCessazioneMA = true;
			} else if (aEveModel.getCodTipoEvento().equals("01")
					&& aEveModel.getCodTipoProvvedimento().equals("06")
					&& lCodiciCessMDS51bis.contains(aEveModel.getCodMotivo())) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Trovata Cessazione MA MDS 51bis ");
				isCessazioneMA = true;
			} else if (aEveModel.getCodTipoEvento().equals("01")
					&& aEveModel.getCodTipoProvvedimento().equals("12")
					&& lCodiciCessMDS51bis.contains(aEveModel.getCodMotivo())) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Trovata Cessazione MA MDS 51bis ");
				isCessazioneMA = true;
			}
		}

		// ==========================================================================
		// Se è un evento di cessazione verifico se è stato effettuato il calcolo
		// della pena
		// ==========================================================================
		if (isCessazioneMA) {
			Connection lConn = null;
			SospensioneSqlDAO lSospSqlDao = null;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Verifico se effettuato il calcolo pena...");
			try {
				lConn = getDBConnection();
				lSospSqlDao = new SospensioneSqlDAO(lConn);

				// Devo verificare se è stato effettuato un calcolo della pena in fase di
				// sospensione
				// n.b. utilizo IdEvento, perchè in questo caso la sospensione è legata
				// direttamente al provvedimento
				lSospSqlDao.ricercaSospensionePerPenaIniziale(aEveModel.getIdEvento());
				SospensioneModel lSospMod = (SospensioneModel) lSospSqlDao.getModelByKey();
				lSospSqlDao.stop();

				if (lSospMod != null) {
					isCessazioneMA = true;
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("...calcolo pena effettuato");
				} else {
					isCessazioneMA = false;
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("...calcolo pena non effettuato");
				}
			} catch (Exception ex) {
				throw new F3BException("CalcoloPenaControllerF5.isCessazioneMA: Non posso leggere  : " + ex);
			} finally {
				cleanup(lSospSqlDao);

				cleanup(lConn);
			}
		}

		// Verifico se la cessazione ha rideterminato la pena e quindi se va
		// considerata come evento di pena iniziale.
		// n.b. NON SEMPRE LA CESSAZIONE RIDETERMINA LA PENA

		return isCessazioneMA;
	}

	/**
	 * Verifica se l'evento passato in input è relativo a una RevocaIndultino e può essere considerato come
	 * Pena Iniziale. n.b. il metodo non verifica se l'evento è validato o annullato
	 * 
	 * @param aEveModel
	 * @return
	 */
	private boolean isRevocaIndultino(EventoModel aEveModel) {
		boolean isRevocaIndultino = false;
		// ==========================================================================
		// Revoca Indultino
		// Tipo Evento: 01 = Provvedimento
		// Tipo Provvedimento: 06 = Ordine di Esecuzione
		// Motivo Evento:
		// 0196 Revoca Sospensione condizionata della pena Art. 2 L. 207/2003
		// ATTENZIONE!!! Per le posizioni giuridiche non gestite viene emesso un
		// provvedimento generico(01-04-0000) ma in questo caso non
		// è stato effettuato il ricalcolo della pena per cui non può
		// essere considerato come Pena Iniziale
		// ==========================================================================
		if (aEveModel.getCodTipoEvento() != null && aEveModel.getCodTipoProvvedimento() != null
				&& aEveModel.getCodMotivo() != null && aEveModel.getCodTipoEvento().equals("01")
				&& aEveModel.getCodTipoProvvedimento().equals("06")
				&& aEveModel.getCodMotivo().equals("0196")) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Trovata Revoca Indultino");
			isRevocaIndultino = true;
		}
		return isRevocaIndultino;
	}

	/**
	 * Verifica se l'evento passato in input è legato a una pena Residua Manuale
	 * 
	 * @param aEveModel
	 * @return
	 */
	private boolean isPenaManuale(EventoModel aEveModel) {
		boolean isPenaManuale = false;

		if (aEveModel.getCodTipoEvento() != null && aEveModel.getCodTipoProvvedimento() != null
				&& aEveModel.getCodMotivo() != null && aEveModel.getCodTipoEvento().equals("01")
				&& aEveModel.getCodTipoProvvedimento().equals("04")
				&& (aEveModel.getCodMotivo().equals("0925") || aEveModel.getCodMotivo().equals("1018"))) // 07/05/2015
																											// mev
																											// 27
		{
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Trovata Pena Manuale");
			isPenaManuale = true;
		}

		return isPenaManuale;
	}

	/**
	 * Verifica se l'evento passato in input è legato a una Forzatura RES n.b. le forzature RES sono legate a
	 * eventi di tipo 01-04-0162 legate ad annotazioni manuali con cod_tipo_annotazione = '003'. I quantum di
	 * pena in questo caso devono essere recuperati dal record pena residua e solo se i quantum sono positivi.
	 * In alcuni casi i quantum sono nulli per mancata migrazione, ma sono valorizzati i quantum di AM
	 * 
	 * @param aEveModel
	 * @return
	 */
	private boolean isForzaturaRes(EventoModel aEveModel) throws F3BException {
		boolean isForzaturaRes = false;

		Connection lConn = null;

		AnnotazioneManualeSqlDAO lAnnoManSqlDao = null;

		if (aEveModel.getCodTipoEvento() != null && aEveModel.getCodTipoProvvedimento() != null
				&& aEveModel.getCodMotivo() != null && aEveModel.getCodTipoEvento().equals("01")
				&& aEveModel.getCodTipoProvvedimento().equals("04")
				&& aEveModel.getCodMotivo().equals("0162")) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Trovata Pena Manuale");

			try {
				lConn = getDBConnection();
				lAnnoManSqlDao = new AnnotazioneManualeSqlDAO(lConn);
				lAnnoManSqlDao.ricercaAnnotazioneManualeByIdEvento(aEveModel.getIdEvento());
				AnnotazioneManualeModel lAnnoMod = (AnnotazioneManualeModel) lAnnoManSqlDao.getModelByKey();
				lAnnoManSqlDao.stop();

				if (lAnnoMod != null && lAnnoMod.getCodTipoAnnotazione().equals("003")) {
					isForzaturaRes = true;
				}
			} catch (DAOException daoEx) {
				throw new F3BException("CalcoloPenaControllerF5.isForzaturaRes: Non posso leggere : " + daoEx);
			} catch (Exception ex) {
				throw new F3BException("CalcoloPenaControllerF5.isForzaturaRes: Non posso leggere  : " + ex);
			} finally {
				cleanup(lAnnoManSqlDao);

				cleanup(lConn);
			}

		}

		return isForzaturaRes;
	}

	/**
	 * Verifica se l'evento passato in input è legato a una sospensione migrata RES, e può essere considerato
	 * come Pena Iniziale. n.b. non è detto che una sospensione abbia comportato il ricalcolo della pena. Va
	 * verificato se all'evento è associata una pena residua e se a questa è a sua volta collegata una
	 * sospensione con valorizzati i campi NUM_xxx_PENA_RESIDUA_yyyy. In questo caso i codici evento sono
	 * differenti da quelli delle sospensioni gestiti da SIEP. Sono legati all'RV_HIGH_VALUE = SOS_MIG
	 * (sospensione migrate) tipo evento = '01' tipo provvedimento = '04' cod motivo = '0904' - Sospensione
	 * esecuzione - affidamento in prova ex art. 47 '0905' - Sospensione esecuzione - affidamento in prova ex
	 * art.47 bis o.p. '0906' - Sospensione esecuzione - detenzione domiciliare art. 47 ter '0907' -
	 * Sospensione esecuzione - semilibertà ex art. 50 L.354/75 '0908' - Sospensione esecuzione - espulsione
	 * dal territorio dello Stato art.7 L.39/70 '0909' - Sospensione esecuzione - differimento pena '0910' -
	 * Sospensione esecuzione - attesa provvedimento altro ufficio '0911' - Sospensione esecuzione - revoca
	 * Ordine di esecuzione '0912' - Sospensione esecuzione - Grazia ex art. 147 n.1 c.p.
	 * 
	 * @param aEveModel
	 * @return
	 */
	private boolean isSospensioneMigrata(EventoModel aEveModel) throws F3BException {

		boolean isSospensioneMigrata = false;
		Connection lConn = null;
		SospensioneSqlDAO lSospSqlDao = null;

		if (aEveModel.getCodTipoEvento() != null
				&& aEveModel.getCodTipoProvvedimento() != null
				&& aEveModel.getCodMotivo() != null
				&& aEveModel.getCodTipoEvento().equals("01")
				&& aEveModel.getCodTipoProvvedimento().equals("04")
				&& (aEveModel.getCodMotivo().equals("0904") || aEveModel.getCodMotivo().equals("0905")
						|| aEveModel.getCodMotivo().equals("0906") || aEveModel.getCodMotivo().equals("0907")
						|| aEveModel.getCodMotivo().equals("0908") || aEveModel.getCodMotivo().equals("0909")
						|| aEveModel.getCodMotivo().equals("0910") || aEveModel.getCodMotivo().equals("0911") || aEveModel
						.getCodMotivo().equals("0912"))) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Trovata Sospensione Res");
			try {
				lConn = getDBConnection();
				lSospSqlDao = new SospensioneSqlDAO(lConn);

				// n.b. utilizo IdEvento, perchè in questo caso la sospensione è legata
				// direttamente al provvedimento
				lSospSqlDao.ricercaSospensionePerPenaIniziale(aEveModel.getIdEvento());
				SospensioneModel lSospMod = (SospensioneModel) lSospSqlDao.getModelByKey();
				lSospSqlDao.stop();

				if (lSospMod != null)
					isSospensioneMigrata = true;

			} catch (DAOException daoEx) {
				throw new F3BException("CalcoloPenaControllerF5.isSospensioneMigrata: Non posso leggere : "
						+ daoEx);
			} catch (Exception ex) {
				throw new F3BException("CalcoloPenaControllerF5.isSospensioneMigrata: Non posso leggere  : "
						+ ex);
			} finally {
				cleanup(lSospSqlDao);
				cleanup(lConn);
			}
		}

		return isSospensioneMigrata;
	}

	/**
	 * Verifica se l'evento passato in input è relativo a una Sospensione Res che, ha gli stessi codici delle
	 * sospensioni SIEP, ma comportamento diverso.
	 * 
	 * Per le sospensioni RES infatti le comunicazioni (12) non vanno mai considerati come pena iniziali in
	 * quanto hanno quantum sempre nulli. E' presente un codice aggiuntivo per le sosp della SORV (0264). Non
	 * sono mai valorizzati i quantum di pena espiata sul record Sospensione, dove presente. In questo caso si
	 * è deciso di considerare SEMPRE la sospensione RES come evento di pena iniziale dato che non è possibile
	 * comunque sapere se ha interroto o meno a pena.
	 * 
	 * @param aEveModel
	 * @return
	 */
	private boolean isSospensioneRes(EventoModel aEveModel) throws F3BException {
		boolean isSospensione = false;

		// ==========================================================================
		// Verifico subito se trattasi di Sospensione migrata RES
		// ==========================================================================
		if (aEveModel.getCodOperatoreInserimento().indexOf("res") != -1
				&& aEveModel.getCodTipoEvento() != null && aEveModel.getCodTipoProvvedimento() != null
				&& aEveModel.getCodMotivo() != null) {
			// ========================================================================
			// Sospensioni del GE
			// ========================================================================
			if (aEveModel.getCodTipoEvento().equals("01")
					&& (aEveModel.getCodTipoProvvedimento().equals("09")
					// || aEveModel.getCodTipoProvvedimento().equals("12")
					) && aEveModel.getCodMotivo().equals("0265")) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Sospensione del GE RES");
				isSospensione = true;
			}
			// ========================================================================
			// Sospensione del PM
			// ========================================================================
			else if (aEveModel.getCodTipoEvento().equals("01")
					&& (aEveModel.getCodTipoProvvedimento().equals("09")
					// || aEveModel.getCodTipoProvvedimento().equals("12") // n.b. in SIEP è 04
					)
					&& (aEveModel.getCodMotivo().equals("0900") || aEveModel.getCodMotivo().equals("0901")
							|| aEveModel.getCodMotivo().equals("0902") || aEveModel.getCodMotivo().equals(
							"0903"))) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Sospensione del PM RES");
				isSospensione = true;
			}
			// ========================================================================
			// Sospensione della SORV
			// ========================================================================
			else if (aEveModel.getCodTipoEvento().equals("01")
					&& (aEveModel.getCodTipoProvvedimento().equals("09")
					// || aEveModel.getCodTipoProvvedimento().equals("12") // NON vanno considerate le
					// comunicazioni
					) && (aEveModel.getCodMotivo().equals("0264") // n.b. solo RES
					// || aEveModel.getCodMotivo().equals("0241")
					|| aEveModel.getCodMotivo().equals("0263") // n.b. in RES non è presente
					)) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Sospensione della SORV RES");
				isSospensione = true;
			} else { // se evento migrato non ricadente nelle precedenti situazioni ritorno false
						// evito di effettuare altri controlli che ritornerebbero comunque un
						// errore
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
				siesLogger.debug("Eve migrato, non SOSP");
				isSospensione = false;
			}
		}

		return isSospensione;
	}

	/**
	 * Verifica se l'evento passato in input è legato a una archiviazione migrata RES. In questo caso viene
	 * considerato come evento iniziale e come pena di partenza verrà recuperata l'ultima pena validata prima
	 * dell'archiviazione.
	 * 
	 * n.b. almeno per la archiviazioni migrate, all'evento non è mai collegata una pena residua. La pena da
	 * archiviazione, se è stata caricata, viene agganciata in genere all'evento precedente.
	 * 
	 * I codici delle Archiviazione sono vari e appartenenti a diversi sottodomini. RV_HIGH_VALUE = ARCH tipo
	 * evento = '01' tipo provvedimento = '04' cod motivo = '0400' - Archiviazione per assorbimento in Cumulo
	 * '0401' - Archiviazione per avvenuta espiazione '0402' - Archiviazione per estinzione pena per morte del
	 * reo '0403' - Archiviazione per espulsione dal territorio dello Stato '0404' - Archiviazione per non
	 * luogo a ulteriori provvedimenti esecutivi '0405' - Archiviazione per revoca sentenza per abolizione
	 * reato '0406' - Archiviazione per sospensione esecuzione ex art. 90 DPR 309/90 '0407' - Archiviazione
	 * per pena espiata in presofferto '0408' - Archiviazione per pena interamente condonata '0409' -
	 * Archiviato DA COMPLETARE CON EVENTUALI ALTRI CODICI DI ARCHIVIAZIONE
	 * 
	 * @param aEveModel
	 * @return
	 */
	private boolean isArchiviazioneRes(EventoModel aEveModel) throws F3BException {

		boolean isArchiviazione = false;

		if (aEveModel.getCodOperatoreInserimento().indexOf("res") != -1 // solo RES
				&& aEveModel.getCodTipoEvento() != null
				&& aEveModel.getCodTipoProvvedimento() != null
				&& aEveModel.getCodMotivo() != null
				&& aEveModel.getCodTipoEvento().equals("01")
				&& aEveModel.getCodTipoProvvedimento().equals("04")
				&& (aEveModel.getCodMotivo().equals("0400") || aEveModel.getCodMotivo().equals("0401")
						|| aEveModel.getCodMotivo().equals("0402") || aEveModel.getCodMotivo().equals("0403")
						|| aEveModel.getCodMotivo().equals("0404") || aEveModel.getCodMotivo().equals("0405")
						|| aEveModel.getCodMotivo().equals("0406") || aEveModel.getCodMotivo().equals("0407")
						|| aEveModel.getCodMotivo().equals("0408") || aEveModel.getCodMotivo().equals("0409"))) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Trovata Archiviazione Res");
			isArchiviazione = true;
		}

		return isArchiviazione;
	}

	/**
	 * Verifica se l'evento passato in input è legato a una archiviazione iscritta SIEP che ha comportato
	 * l'azzeramento della pena. In questo caso viene considerato come evento iniziale e come pena di partenza
	 * verrà recuperata la pena associata all'evento di archiviazione.
	 * 
	 * n.b. questo metodo verifica solo che l'evento sia iscritto SIEP e sia del tipo Archiviazione. La
	 * presenza e il recupero della pena viene verificato dal metodo getPena.
	 * 
	 * @param aEveModel
	 * @return
	 */
	private boolean isArchiviazioneSIEP(EventoModel aEveModel) throws F3BException {
		boolean isArchiviazione = false;

		// Se l'evento è migrato non lo considero
		if (aEveModel.getCodOperatoreInserimento().indexOf("res") != -1) {
			return false;
		}

		// ==========================================================================
		// NON LUOGO A PROVVEDERE (RV_HIGH_VALUE=NLP)
		//
		// 0353 - archiviazione per fascicolo iscritto per errore
		// 0009 - archiviazione per non luogo a provvedere ad ulteriori provvedimenti esecutivi (generico)
		// 0120 - archiviazione per non luogo ad attività di esecuzione (Pena sospesa)
		// 0007 - archiviazione per pena definita con provvedimento di fungibilità
		// 0478 - archiviazione per pena espiata a seguito di concessione L.A.(liberazione anticipata)
		// 0006 - archiviazione per pena espiata in presofferto
		// 0008 - archiviazione per pena interamente condonata (in sentenza)
		// ==========================================================================
		if (aEveModel.getCodTipoEvento() != null
				&& aEveModel.getCodTipoProvvedimento() != null
				&& aEveModel.getCodMotivo() != null
				&& aEveModel.getCodTipoEvento().equals("01")
				&& aEveModel.getCodTipoProvvedimento().equals("25")
				&& (aEveModel.getCodMotivo().equals("0353") || aEveModel.getCodMotivo().equals("0009")
						|| aEveModel.getCodMotivo().equals("0120") || aEveModel.getCodMotivo().equals("0007")
						|| aEveModel.getCodMotivo().equals("0478") || aEveModel.getCodMotivo().equals("0006") || aEveModel
						.getCodMotivo().equals("0008"))) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Trovata Archiviazione SIEP - NON LUOGO A PROVVEDERE");
			isArchiviazione = true;
		}

		// ==========================================================================
		// FINE ESPIAZIONE (RV_HIGH_VALUE=ESPIAZ)
		// 0096 - archiviazione per pena espiata in regime carcerario
		// 0097 - archiviazione per pena espiata in regime di detenzione domiciliare
		// 0098 - archiviazione per pena espiata in regime di semilibertà
		// 0099 - archiviazione per pena espiata in regime di sospensione della parte finale della pena
		// (L.207/2003)
		// 0473 - archiviazione per pena espiata in regime di arresti domiciliari ex art.656 comma 10 c.p.p.
		// 0479 - archiviazione per pena espiata in regime di semidetenzione
		// 0480 - archiviazione per pena espiata in regime di libertà controllata
		// 0481 - archiviazione per pena espiata in regime di lavoro sostitutivo
		// ==========================================================================
		if (aEveModel.getCodTipoEvento() != null
				&& aEveModel.getCodTipoProvvedimento() != null
				&& aEveModel.getCodMotivo() != null
				&& aEveModel.getCodTipoEvento().equals("01")
				&& aEveModel.getCodTipoProvvedimento().equals("25")
				&& (aEveModel.getCodMotivo().equals("0096") || aEveModel.getCodMotivo().equals("0097")
						|| aEveModel.getCodMotivo().equals("0098") || aEveModel.getCodMotivo().equals("0099")
						|| aEveModel.getCodMotivo().equals("0473") || aEveModel.getCodMotivo().equals("0479")
						|| aEveModel.getCodMotivo().equals("0480") || aEveModel.getCodMotivo().equals("0481"))) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Trovata Archiviazione SIEP - FINE ESPIAZIONE");
			isArchiviazione = true;
		}

		// ==========================================================================
		// PERDITA DI COMPETENZA (RV_HIGH_VALUE=CUMU)
		// 0019 - archiviazione per assorbimento in cumulo stesso ufficio
		// 0022 - archiviazione per assorbimento in cumulo altro ufficio
		// ==========================================================================
		if (aEveModel.getCodTipoEvento() != null && aEveModel.getCodTipoProvvedimento() != null
				&& aEveModel.getCodMotivo() != null && aEveModel.getCodTipoEvento().equals("01")
				&& aEveModel.getCodTipoProvvedimento().equals("25")
				&& (aEveModel.getCodMotivo().equals("0019") || aEveModel.getCodMotivo().equals("0022"))) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Trovata Archiviazione SIEP - PERDITA DI COMPETENZA");
			isArchiviazione = true;
		}

		// ==========================================================================
		// PROVVEDIMENTO ALTRA AUTORITA
		//
		// Altra Autorità (RV_HIGH_VALUE=DEFI_ALTRO)
		// 0421 - archiviazione per sospensione esecuzione della pena per giudizio di revisione
		// 0422 - archiviazione per concessione Grazia
		//
		// Giudice Sorveglianza (RV_HIGH_VALUE=DEFI_SORV)
		// 0424 - archiviazione per estinzione pena Affidamento in prova
		// 0425 - archiviazione per estinzione pena Liberazione Condizionale
		// 0426 - archiviazione per sospensione dell'esecuzione della pena detentiva art.90 DPR 309/90
		//
		// Giudice Esecuzione (RV_HIGH_VALUE=DEFI_GE)
		// 0411 - archiviazione per estinzione delle pene per decorso del tempo
		// 0412 - archiviazione per morte del reo dopo la condanna
		// 0413 - archiviazione per dubbio sull'identità fisica della persona detenuta
		// 0414 - archiviazione per persona condannata per errore di nome
		// 0415 - archiviazione per pluralità di sentenze per il medesimo fatto contro la stessa persona
		// 0416 - archiviazione per questioni sul titolo esecutivo
		// 0417 - archiviazione per pena residua interamente amnistiata o condonata
		// 0418 - archiviazione per pena residua dichiarata sospesa o interamente espiata
		// 0419 - archiviazione per abolizione del reato
		// 0420 - archiviazione per altro motivo
		// 0476 - archiviazione per estinzione pena espulsione a titolo di sanzione sostitutiva (ex art.16 c.1
		// D.Lvo 286/1998 e succ.mod.) per decorso dei termini
		// 0477 - archiviazione per estinzione pena espulsione a titolo di sanzione alternativa (ex art.16 c.5
		// D.Lvo 286/1998 e succ.mod.) per decorso dei termini
		// 0474 - archiviazione per pena espiata in regime di sospensione della parte finale della pena
		// (L.207/2003)
		// ==========================================================================
		if (aEveModel.getCodTipoEvento() != null
				&& aEveModel.getCodTipoProvvedimento() != null
				&& aEveModel.getCodMotivo() != null
				&& aEveModel.getCodTipoEvento().equals("01")
				&& aEveModel.getCodTipoProvvedimento().equals("25")
				&& (aEveModel.getCodMotivo().equals("0421")
						|| aEveModel.getCodMotivo().equals("0422")
						// Giudice Sorveglianza
						|| aEveModel.getCodMotivo().equals("0424")
						|| aEveModel.getCodMotivo().equals("0425")
						|| aEveModel.getCodMotivo().equals("0426")
						|| aEveModel.getCodMotivo().equals("0474")
						// Giudice Esecuzione
						|| aEveModel.getCodMotivo().equals("0411") || aEveModel.getCodMotivo().equals("0412")
						|| aEveModel.getCodMotivo().equals("0413") || aEveModel.getCodMotivo().equals("0414")
						|| aEveModel.getCodMotivo().equals("0415") || aEveModel.getCodMotivo().equals("0416")
						|| aEveModel.getCodMotivo().equals("0417") || aEveModel.getCodMotivo().equals("0418")
						|| aEveModel.getCodMotivo().equals("0419") || aEveModel.getCodMotivo().equals("0420")
						|| aEveModel.getCodMotivo().equals("0476") || aEveModel.getCodMotivo().equals("0477"))) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Trovata Archiviazione SIEP - PROVVEDIMENTO ALTRA AUTORITA");
			isArchiviazione = true;
		}

		// ==========================================================================
		// ATTESA ARCHIVIAZIONE (RV_HIGH_VALUE=ATT_ARC)
		// 0482 - in attesa Ordinanza estinzione pena per pena espiata in regime di sospensione ex art.90 DPR
		// 309/90
		// 0483 - in attesa Ordinanza estinzione pena per pena espiata in regime di affidamento in prova al
		// servizio sociale
		// 0484 - in attesa Ordinanza estinzione pena per espulsione a titolo di sanzione sostitutiva (ex
		// art.16 c.1 D.Lvo 286/1998 e succ. mod.)
		// 0485 - in attesa Ordinanza estinzione pena per espulsione a titolo di sanzione alternativa (ex
		// art.16 c.1 D.Lvo 286/1998 e succ. mod.)
		// 0486 - in attesa declaratoria TdS per pena espiata in regime di sospensione della parte finale
		// della pena (L.270/2003)
		// 0487 - in attesa declaratoria estinzione pena per pena espiata in regime di libertà controllata
		// ==========================================================================
		// if ( aEveModel.getCodTipoEvento()!=null
		// && aEveModel.getCodTipoProvvedimento()!=null
		// && aEveModel.getCodMotivo()!=null
		// && aEveModel.getCodTipoEvento().equals("01")
		// && aEveModel.getCodTipoProvvedimento().equals("25")
		// && ( aEveModel.getCodMotivo().equals("0482")
		// || aEveModel.getCodMotivo().equals("0483")
		// || aEveModel.getCodMotivo().equals("0484")
		// || aEveModel.getCodMotivo().equals("0485")
		// || aEveModel.getCodMotivo().equals("0486")
		// || aEveModel.getCodMotivo().equals("0487")
		// )
		// )
		// {
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("Trovata Archiviazione SIEP - ATTESA ARCHIVIAZIONE");
		// isArchiviazione = true;
		// }

		return isArchiviazione;
	}

	/**
	 * Verifica se l'evento passato in input è legato a una Espulsione n.b. nella vecchia versione SIEP,
	 * l'espulsione prevedeva l'inserimento contestuale di due eventi. 01-04-0275 (provvedimento) 01-27-0276
	 * (verbale di espulsione) I due eventi erano collegati. Il verbale puntava il provvedimento. Il calcolo
	 * della pena veniva associato al Provvedimento, ed effettuato solo se condannato detenuto. SOLO in questo
	 * caso il record pena residua è puntato da un record SOSPENSIONE contenente i dati della pena espiata e
	 * delle pena da espiare Ai fini del CP considero quindi solo il Provvedimento
	 * 
	 * Nella nuova versione dell'espulsione l'evento che ridetermina la pena è la Comunicazione (12) del
	 * Verbale di Avvenuta Espulsione con relativa data. (01-12-2141) Avvenuta Espulsione straniero a titolo
	 * di sanzione alternativa (art. 16 comma 5 D.Lvo 286/1998 e succ.mod.) In questo caso all'evento è
	 * associata una PR con a sua volta associata un record SOSPENSIONE riportante il quantum espiato e il
	 * residuo da espiare. Anche in questo caso è sufficiente recuperare i quantum dal record PR
	 * 
	 * Dalle versione 3.0 aggiunto il caso dell'espulsione a titolo di Sanzione Sostitutiva Evento 01-12-0927
	 * oppure 01-25-0927 Inserisce sempre Pena residua e sospensione
	 * 
	 * @param aEveModel
	 * @return
	 * @throws F3BException
	 */
	private boolean isEspulsione(EventoModel aEveModel) throws F3BException {
		boolean isEspulsione = false;

		Connection lConn = null;

		SospensioneSqlDAO lSospSqlDao = null;
		try {

			if (aEveModel.getCodTipoEvento() != null && aEveModel.getCodTipoProvvedimento() != null
					&& aEveModel.getCodMotivo() != null
					&& ( // Vecchia espulsione
					(aEveModel.getCodTipoEvento().equals("01")
							&& aEveModel.getCodTipoProvvedimento().equals("04") && aEveModel.getCodMotivo()
							.equals("0275") // 0275 - Verbale di Espulsione
							)
							|| // Nuova espulsione
							(aEveModel.getCodTipoEvento().equals("01")
									&& aEveModel.getCodTipoProvvedimento().equals("12") && aEveModel
									.getCodMotivo().equals("2141") // 2141 - Avvenuta Espulsione straniero a
																	// titolo di sanzione alternativa (art. 16
																	// comma 5 D.Lvo 286/1998 e succ.mod.)
							) || // Nuova espulsione come Sanzione Sostitutiva
					(aEveModel.getCodTipoEvento().equals("01")
							&& (aEveModel.getCodTipoProvvedimento().equals("12") // Comunicazione
							|| aEveModel.getCodTipoProvvedimento().equals("25") // Annotazione
							) && aEveModel.getCodMotivo().equals("0927") // 0927 - Comunicazione scadenza
																			// termini espulsione
					))) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Trovata Espulsione SIEP");

				lConn = getDBConnection();
				lSospSqlDao = new SospensioneSqlDAO(lConn);

				// n.b. la ricercaSospensionePerPenaIniziale va in join con ESPULSIONE
				// e verifica che i campi siano valorizzati
				lSospSqlDao.ricercaSospensionePerPenaIniziale(aEveModel.getIdEvento());
				SospensioneModel lSospMod = (SospensioneModel) lSospSqlDao.getModelByKey();
				lSospSqlDao.stop();

				if (lSospMod != null)
					isEspulsione = true;
			}
		} catch (DAOException daoEx) {
			throw new F3BException("CalcoloPenaControllerF5.isEspulsione: Non posso leggere : " + daoEx);
		} catch (Exception ex) {
			throw new F3BException("CalcoloPenaControllerF5.isEspulsione: Non posso leggere  : " + ex);
		} finally {
			cleanup(lSospSqlDao);

			cleanup(lConn);
		}

		return isEspulsione;
	}

	/**
	 * Verifica se l'evento passato in input è relativo a una Interruzione a seguito richiesta con
	 * anticipazione degli effetti di indulto che ha determinato la Liberazione del condannato con azzeramento
	 * della pena. Gli eventi possibili sono 2: 01-09-0367 Ordine Scarcerazione - Provvisorio per concessione
	 * Indulto 01-26-0290 Richiesta - Applicazione Benefici - ex art. 174 c.p. e 672 c.p.p.
	 * 
	 * Non sempre questi eventi generano una interruzione. Perchè l'evento possa essere considerato
	 * interruttivo deve avere associata una SOSPENSIONE
	 * 
	 * @param aEveModel
	 * @return
	 * @throws F3BException
	 */
	private boolean isInterruzioneIndulto(EventoModel aEveModel) throws F3BException {
		boolean isInterruzioneIndulto = false;

		Connection lConn = null;

		PenaResiduaSqlDAO lPenResSqlDao = null;
		SospensioneSqlDAO lSospSqlDao = null;

		try {
			if (aEveModel.getCodTipoEvento() != null
					&& aEveModel.getCodTipoProvvedimento() != null
					&& aEveModel.getCodMotivo() != null
					&& ( // OSP
					(aEveModel.getCodTipoEvento().equals("01")
							&& aEveModel.getCodTipoProvvedimento().equals("09") // 09 - Ordine di
																				// scarcerazione
					&& aEveModel.getCodMotivo().equals("0367") // 0367 - Provvisorio per concessione Indulto
					) || // RIchiesta
					(aEveModel.getCodTipoEvento().equals("01")
							&& aEveModel.getCodTipoProvvedimento().equals("26") // 26 - Richiesta
					&& aEveModel.getCodMotivo().equals("0290") // 0290 - Applicazione Benefici - ex art. 174
																// c.p. e 672 c.p.p.
					))) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Trovata Richiesta/OSP Indulto");

				lConn = getDBConnection();

				lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
				lSospSqlDao = new SospensioneSqlDAO(lConn);

				// n.b. Un evento di uindulto è un evento di pena iniziale se ha collegata
				// una PENA_RESIDUA con una SOSPENSIONE
				//
				PenaResiduaModel lPenResMod = null;

				lPenResSqlDao.ricercaPenaResiduaByKeyEvento(aEveModel.getIdEvento());
				lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
				lPenResSqlDao.stop();

				SospensioneModel lSospMod = null;
				if (lPenResMod != null && lPenResMod.getIdPenaResidua() != null) {
					lSospSqlDao.ricercaSospensioneByIdPenaResidua(lPenResMod.getIdPenaResidua());
					lSospMod = (SospensioneModel) lSospSqlDao.getModelByKey();
					lSospSqlDao.stop();

				}

				if (lSospMod != null) {
					isInterruzioneIndulto = true;
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Richiesta/OSP Indulto Interruttiva");
				} else {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Richiesta/OSP Indulto scartata");
				}
			}
		} catch (DAOException daoEx) {
			throw new F3BException("CalcoloPenaControllerF5.isInterruzioneIndulto: Non posso leggere : "
					+ daoEx);
		} catch (Exception ex) {
			throw new F3BException("CalcoloPenaControllerF5.isInterruzioneIndulto: Non posso leggere  : "
					+ ex);
		} finally {
			cleanup(lSospSqlDao);
			cleanup(lPenResSqlDao);

			cleanup(lConn);
		}

		return isInterruzioneIndulto;
	}

	/**
	 * Verifica se l'evento passato in input è relativo a una Revoca Sanzione Sostitutiva (senza cumulo) che
	 * determina i nuovi quantum di pena da eseguire.
	 * 
	 * Gli eventi sono OE con o senza sospensione: 01-06-0397 Per la carcerazione - Libero - Conversione
	 * Sanzione Sostitutiva 01-06-0398 Per la carcerazione - Detenuto altra causa - Conversione Sanzione
	 * Sostitutiva 01-06-0490 Ordine esecuzione con sospensione - Libero - Conversione Sanzione Sostitutiva
	 * 01-06-0491 Ordine esecuzione con sospensione - Detenuto altra causa - Conversione Sanzione Sostitutiva
	 * 
	 * Non sempre questi eventi generano una interruzione. Perchè l'evento possa essere considerato
	 * interruttivo deve avere associata una SOSPENSIONE
	 * 
	 * @param aEveModel
	 * @return
	 * @throws F3BException
	 */
	private boolean isRevocaSanzioneSostitutiva(EventoModel aEveModel) throws F3BException {
		boolean isRevocaSanzioneSostitutiva = false;

		if (aEveModel.getCodTipoEvento() != null && aEveModel.getCodTipoProvvedimento() != null
				&& aEveModel.getCodMotivo() != null && aEveModel.getCodTipoEvento().equals("01")
				&& aEveModel.getCodTipoProvvedimento().equals("06") // 06 - Ordine di esecuzione
				&& (aEveModel.getCodMotivo().equals("0397") // 0397 - Per la carcerazione - Libero -
															// Conversione Sanzione Sostitutiva
						|| aEveModel.getCodMotivo().equals("0398") // 0398 - Per la carcerazione - Detenuto
																	// altra causa - Conversione Sanzione
																	// Sostitutiva
						|| aEveModel.getCodMotivo().equals("0490") // 0490 - Ordine esecuzione con sospensione
																	// - Libero - Conversione Sanzione
																	// Sostitutiva
				|| aEveModel.getCodMotivo().equals("0491") // 0491 - Ordine esecuzione con sospensione -
															// Detenuto altra causa - Conversione Sanzione
															// Sostitutiva
				))

		{
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Trovata Revoca Sanzione Sostitutiva");

			isRevocaSanzioneSostitutiva = true;
		}

		return isRevocaSanzioneSostitutiva;
	}

	/**
	 * Verifica se l'evento è un Indultino (Art. 2 L. 207/2003) migrato RES e se può essere considerato come
	 * pena Interruttiva e quindi come pena iniziale.
	 * 
	 * n.b. in SIES l'indultino non interrompe la pena e quindi non è un evento di pena iniziale. In RES
	 * invece spesso interrompe la pena.
	 * 
	 * L'indultimo migrato viene iscritto con due eventi:
	 * 
	 * 03-Ordinanza 2245-Sospensione Condizionata della Pena Detentiva Art. 2 L. 207/2003
	 * 
	 * 09-Ordine Scarcerazione 2245-Sospensione Condizionata della Pena Detentiva Art. 2 L. 207/2003
	 * 
	 * L'evento viene considerato come pena iniziale solo se la PR associata ha i quantum valorizzati. Infatti
	 * spesso i quantum associati sono migrati nulli per cui non è possibile utilizzare la pena come iniziale.
	 * 
	 * n.b. non è mai presente il record sospensione associato
	 * 
	 * @since sperimentale 06/11/2007
	 * @param aEveModel
	 * @return
	 * @throws F3BException
	 * 
	 */
//	private boolean isIndultinoRES(EventoModel aEveModel) throws F3BException {
//		boolean isIndultinoRES = false;
//
//		// Se non è un evento migrato esco subito
//		if (aEveModel.getCodOperatoreInserimento().indexOf("res") == -1) {
//			return false;
//		}
//
//		Connection lConn = null;
//		PenaResiduaSqlDAO lPenResSqlDao = null;
//
//		try {
//
//			if (aEveModel.getCodTipoEvento() != null
//					&& aEveModel.getCodTipoProvvedimento() != null
//					&& aEveModel.getCodMotivo() != null
//					&& (aEveModel.getCodTipoEvento().equals("01")
//							&& aEveModel.getCodTipoProvvedimento().equals("09") // 09 - Ordine di
//																				// scarcerazione
//					&& aEveModel.getCodMotivo().equals("2245") // 2245 - Sospensione Condizionata della Pena
//																// Detentiva Art. 2 L. 207/2003
//					)) {
//				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//				// LogF3B.getLogger()
//				siesLogger.debug("Trovato Indulto RES");
//
//				lConn = getDBConnection();
//				lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
//
//				// n.b. Un indultino REZ è un evento di pena iniziale se ha collegata
//				// una PENA_RESIDUA con quantum di pena valorizzati
//				//
//				PenaResiduaModel lPenResMod = null;
//
//				lPenResSqlDao.ricercaPenaResiduaByKeyEvento(aEveModel.getIdEvento());
//				lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
//				lPenResSqlDao.stop();
//
//				if (lPenResMod != null
//						&& (CalendarUtil.getTotGiorni(lPenResMod.getQuantumReclusione()) > 0 || CalendarUtil
//								.getTotGiorni(lPenResMod.getQuantumArresto()) > 0)) {
//					isIndultinoRES = true;
//					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//					// LogF3B.getLogger()
//					siesLogger.debug("Indultino RES con quantum valorizzati");
//				} else {
//					isIndultinoRES = false;
//					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//					// LogF3B.getLogger()
//					siesLogger.debug("Indultino RES senza quantum, scartato");
//				}
//			}
//		} catch (DAOException daoEx) {
//			throw new F3BException("CalcoloPenaControllerF5.isIndultinoRES: Non posso leggere : " + daoEx);
//		} catch (Exception ex) {
//			throw new F3BException("CalcoloPenaControllerF5.isIndultinoRES: Non posso leggere  : " + ex);
//		} finally {
//			cleanup(lPenResSqlDao);
//
//			cleanup(lConn);
//		}
//
//		return isIndultinoRES;
//	}

	/**
	 * Restituisce l'elenco di tutte le pene residue VALIDATE per il fascicolo ordinate per data inserimento
	 * decrescente ma inserite prima della data passata in input
	 * 
	 * @param aFascID
	 * @param aDataIns
	 * @return
	 * @throws F3BException
	 */
	public Vector getElencoPeneResidueDataInsDesc(BigDecimal aFascID, Date aDataIns) throws F3BException {
		Connection lConn = null;
		PenaResiduaSqlDAO lPenDao = null;

		Vector lListaPeneResidue = new Vector();

		try {
			lConn = getDBConnection();
			lPenDao = new PenaResiduaSqlDAO(lConn);
			lPenDao.ricercaPenaResiduaValidataPerData(aFascID, aDataIns);
			lListaPeneResidue = new Vector(lPenDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PenaResiduaController.getElencoPeneResidueDataInsDesc: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("SQLException: " + ex);
			throw new F3BException("PenaResiduaController.getElencoPeneResidueDataInsDesc: " + ex);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}

		return lListaPeneResidue;
	}

	/**
	 * Restituisce un vettore di SospensioneModel contenente tutte le sospensioni inserite e VALIDATE nel
	 * periodo specificato
	 * 
	 * @param aFascID
	 *            id del Fascicolo
	 * @param aDataDal
	 *            data dal
	 * @param aDataAl
	 *            data al
	 * @return vettore di SospensioneModel
	 * @throws
	 */
	public Vector exGetPeneEspiate(BigDecimal aFascID, Date aDataDal, Date aDataAl) throws F3BException {
		Connection lConn = null;

		SospensioneSqlDAO lSospSqlDao = null;
		SospensioneModel lSospModel = null;

		Vector lListaPeneGiaEspiate = new Vector();
		try {
			lConn = getDBConnection();
			lSospSqlDao = new SospensioneSqlDAO(lConn);
			lSospSqlDao.ricercaSospensioniValidatePerIntervallo(aFascID, aDataDal, aDataAl);
			lSospSqlDao.start();

			while (lSospSqlDao.next()) {
				lSospModel = (SospensioneModel) lSospSqlDao.getModel();

				lListaPeneGiaEspiate.add(lSospModel);
			}
			lSospSqlDao.stop();

		} catch (DAOException daoEx) {
			throw new F3BException("CalcoloPenaControllerF5.exGetPeneEspiate: Non posso leggere : " + daoEx);
		} catch (Exception ex) {
			throw new F3BException("CalcoloPenaControllerF5.exGetPeneEspiate: Non posso leggere  : " + ex);
		} finally {
			cleanup(lSospSqlDao);

			cleanup(lConn);
		}

		return lListaPeneGiaEspiate;
	}

	/**
	 * Restituisce un vettore di FungibilitaModel contenente tutte le pene espiate in eccesso inserite e
	 * VALIDATE nel periodo specificato
	 * 
	 * @param aFascID
	 *            id del Fascicolo
	 * @param aDataDal
	 *            data dal
	 * @param aDataAl
	 *            data al
	 * @return vettore di FungibilitaModel
	 * @throws
	 */
	public Vector exGetPeneEspiateInEccesso(BigDecimal aFascID, Date aDataDal, Date aDataAl)
			throws F3BException {
		Connection lConn = null;

		FungibilitaSqlDAO lFungSqlDao = null;
		FungibilitaModel lFungModel = null;

		Vector lListaPeneEspiateInEccesso = new Vector();
		try {
			lConn = getDBConnection();

			lFungSqlDao = new FungibilitaSqlDAO(lConn);
			lFungSqlDao.ricercaFungibilitaByFascicoliSiep(aFascID, aDataDal, aDataAl);
			lFungSqlDao.start();

			while (lFungSqlDao.next()) {
				lFungModel = (FungibilitaModel) lFungSqlDao.getModel();

				lListaPeneEspiateInEccesso.add(lFungModel);
			}
			lFungSqlDao.stop();

		} catch (DAOException daoEx) {
			throw new F3BException("CalcoloPenaControllerF5.exGetPeneEspiateInEccesso: Non posso leggere : "
					+ daoEx);
		} catch (Exception ex) {
			throw new F3BException("CalcoloPenaControllerF5.exGetPeneEspiateInEccesso: Non posso leggere  : "
					+ ex);
		} finally {
			cleanup(lFungSqlDao);

			cleanup(lConn);
		}
		return lListaPeneEspiateInEccesso;
	}

	/**
	 * Restituisce la data di scarcerazione legata a una richiesta di Indulto con anticipazione degli effetti.
	 * n.b. la data di scarcerazione viene ricostruita in quanto non viene salvata in alcun modo sul DB. Tale
	 * data viene utilizzata per i calcoli della fungibilità e della pena espiata nel caso di Amnistia/Indulto
	 * (richieste e decisioni).
	 * 
	 * @return la presunta data di scarcerazione o null se non presente o non determinabile
	 */
	public Date getDataScarcerazione(BigDecimal aFascID) throws F3BException {
		// ==========================================================================
		// La data di scarcerazione viene utilizzata al posto della data di sistema
		// nei calcoli della pena.
		// Devo recuperare il record pena residua associata a una richiesta.
		// Verificare se la data fine pena < data inserimento della richiesta che
		// coincide con la data di systema. Se è così, il fine pena coincide con
		// la data di scarcerazione utilizzata nei calcoli.
		// ==========================================================================
		Date lDataScarcerazione = null;

		Connection lConn = null;
		PenaResiduaSqlDAO lPenDao = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaResiduaSqlDAO(lConn);

			// New 03/10/2007 tentativo di recuperare la data di scarcerazione dall'ultima
			// pena validata se data fine < data odierna indipendentemente dal fatto che
			// sia stata inserita effettuata una scarcerazine provvisoria per Indulto
			lPenDao.ricercaPenaResiduaCorrenteByFascicoloSiepUltimaValidata(aFascID);
			PenaResiduaModel lPenMod = (PenaResiduaModel) lPenDao.getModelByKey();
			if (lPenMod != null && lPenMod.getDataInserimento() != null && lPenMod.getDataFine() != null) {
				if (DateUtils.isLower(lPenMod.getDataFine(), DateUtils.getSysDate())) {
					lDataScarcerazione = lPenMod.getDataFine();
				}
			}

			/*
			 * // OLD // Recupero l'ultima pena residua associata a una richiesta di amnistia/indulto // se la
			 * data fine < data inserimento vuol dire che i calcoli sono stati // effettuati utilizzando come
			 * data di sistema la data scarcerazione // non è vero!! se è stato imputato un quantum eccessivo,
			 * la data fine pena // calcolata può essere < alla data di sistema generando fungibilità.
			 * 
			 * Vector lListaPeneResidue = new Vector();
			 * 
			 * lPenDao.ricercaPenaResiduaRichestaAmnistiaIndulto(aFascID); lListaPeneResidue = new
			 * Vector(lPenDao.getModels());
			 * 
			 * if (lListaPeneResidue.size()>0){ PenaResiduaModel lPenMod = (PenaResiduaModel)
			 * lListaPeneResidue.elementAt(0); if (lPenMod.getDataInserimento()!=null &&
			 * lPenMod.getDataFine()!=null){ if (DateUtils.isLower(lPenMod.getDataFine(),
			 * lPenMod.getDataInserimento())){ lDataScarcerazione = lPenMod.getDataFine(); } } }
			 */
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PenaResiduaController.getDataScarcerazione: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("SQLException: " + ex);
			throw new F3BException("PenaResiduaController.getDataScarcerazione: " + ex);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}

		return lDataScarcerazione;
	}

	/**
	 * Restituisce l'elenco delle richieste con o senza anticipazione collegate a una decisione
	 * 
	 * @param aIdDecisione
	 *            id dell'annotazione che rappresenta la decisione
	 * @return vettore di AnnotazioneManualeModel
	 * @throws F3BException
	 */
	public Vector exGetRichiesteAlGEbyIdDecisione(BigDecimal aIdDecisione) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Ricerco le Richieste al GE collegate alla decisione: " + aIdDecisione);
		Connection lConn = null;
		Vector lListaRichiestePerDecisione = new Vector();

		AnnotazioneManualeSqlDAO lAnnManSqlDao = null;

		try {
			lConn = getDBConnection();

			AnnotazioneManualeModel lAnnManMod = new AnnotazioneManualeModel();

			lAnnManSqlDao = new AnnotazioneManualeSqlDAO(lConn);

			lAnnManSqlDao.ricercaAnnManualeRichiesteGEByIdDecisione(aIdDecisione);

			lAnnManSqlDao.start();

			while (lAnnManSqlDao.next()) {
				lAnnManMod = (AnnotazioneManualeModel) lAnnManSqlDao.getModel();

				lListaRichiestePerDecisione.add(lAnnManMod);
			}
			lAnnManSqlDao.stop();

		} catch (DAOException daoEx) {
			throw new F3BException(
					"CalcoloPenaControllerF5.exGetRichiesteAlGEbyIdDecisione: Non posso leggere : " + daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					"CalcoloPenaControllerF5.exGetRichiesteAlGEbyIdDecisione: Non posso leggere  : " + ex);
		} finally {
			cleanup(lAnnManSqlDao);

			cleanup(lConn);
		}

		return lListaRichiestePerDecisione;
	}

	/**
	 * Recupera i fascicoli su cui effettuare il check della pena
	 * 
	 * @param aChiaveUfficio
	 * @param aIscritto
	 * @param aProgrAnno
	 * @return
	 */
	public Vector exGetFascicoliPerCheckPena(String aChiaveUfficio, String aIscritto, String aProgrAnno)
			throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Ricerco i fascicolo: ");

		Connection lConn = null;
		Vector lListaFascicoli = new Vector();

		FascicoloSiepSqlDAO lFascSiepSqlDao = null;

		try {
			lConn = getDBConnection();

			FascicoloSiepModel lFascSiepModel = new FascicoloSiepModel();

			lFascSiepSqlDao = new FascicoloSiepSqlDAO(lConn);

			lFascSiepSqlDao.ricercaFascicoloPerCheckPena(aChiaveUfficio, aIscritto, aProgrAnno);

			lFascSiepSqlDao.start();

			while (lFascSiepSqlDao.next()) {
				lFascSiepModel = (FascicoloSiepModel) lFascSiepSqlDao.getModel();

				lListaFascicoli.add(lFascSiepModel);
			}

			lFascSiepSqlDao.stop();

		} catch (DAOException daoEx) {
			throw new F3BException("CalcoloPenaControllerF5.exGetFascicoliPerCheckPena: Non posso leggere : "
					+ daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					"CalcoloPenaControllerF5.exGetFascicoliPerCheckPena: Non posso leggere  : " + ex);
		} finally {
			cleanup(lFascSiepSqlDao);

			cleanup(lConn);
		}

		return lListaFascicoli;
	}

	/**
	 * Restituisce la pena residua associata all'evento passato in input. Tale evento è l'evento di pena
	 * iniziale, ma a causa di incoerenza sui dati a sistema non sempre è possibile recuperare la pena
	 * associata a tali eventi.
	 * 
	 * @param aEveIniziale
	 *            - Evento di pena iniziale
	 * @return PenaResiduaModel - pena residua associata all'evento, null se non è stato possibile determinare
	 *         la pena
	 * @throws F3BException
	 */
	public PenaResiduaModel getPena(EventoModel aEventoIniziale, int aTipoPena) throws F3BException {
		Connection lConn = null;

		PenaResiduaSqlDAO lPenaResSqlDao = null;
		SospensioneSqlDAO lSospSqlDAO = null;
		AnnotazioneManualeSqlDAO lAnnoManSqlDao = null;

		PenaResiduaModel lPenaResMod = null;

		try {
			lConn = getDBConnection();

			if (aTipoPena == ICostantiCalcoloPena.PENA_ARCHIVIATA_RES) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Cerco pena su archiviazione RES");
				// Nel caso di pena da archiviazione recupero l'ultima pena validata
				// prima dell'archiviazione se esiste, altrimenti l'ultima pena migrata
				// RES, alrimenti non è una pena iniziale
				lPenaResSqlDao = new PenaResiduaSqlDAO(lConn);
				lPenaResSqlDao.ricercaPenaDaArchiviazione(aEventoIniziale.getFasSieIdFascicoloSiep(),
						aEventoIniziale.getDataInserimento());
				lPenaResMod = (PenaResiduaModel) lPenaResSqlDao.getModelByKey();
				lPenaResSqlDao.stop();

				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("Pena da Archiviazione Prima: "+lPenaResMod);

				if (lPenaResMod == null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Pena non trovata su evento precedente, cerco ultima migrata...");
					// ====================================================================
					// Non esiste una pena associata ad eventi precedenti l'archiviazione
					// Recupero l'ultima pena migrata RES (se esiste)
					// ====================================================================
					lPenaResSqlDao = new PenaResiduaSqlDAO(lConn);
					lPenaResSqlDao.ricercaUltimaPenaMigrata(aEventoIniziale.getFasSieIdFascicoloSiep());
					lPenaResMod = (PenaResiduaModel) lPenaResSqlDao.getModelByKey();
					lPenaResSqlDao.stop();
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("Ultima pena migrata RES: "+lPenaResMod);
				}
			} else if (aTipoPena == ICostantiCalcoloPena.PENA_ARCHIVIATA_SIEP) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Cerco pena su archiviazione SIEP");
				// Nel caso di pena da archiviazione SIEP recupero la pena associata
				// all'evento di archiviazione
				lPenaResSqlDao = new PenaResiduaSqlDAO(lConn);
				lPenaResSqlDao.ricercaPenaResiduaByKeyEvento(aEventoIniziale.getIdEvento());
				lPenaResMod = (PenaResiduaModel) lPenaResSqlDao.getModelByKey();
				lPenaResSqlDao.stop();

				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("Pena da Arch : "+lPenaResMod);

				if (lPenaResMod != null
						&& (CalendarUtil.getTotGiorni(lPenaResMod.getQuantumReclusione()) != 0 || CalendarUtil
								.getTotGiorni(lPenaResMod.getQuantumArresto()) != 0)) { // Se i quantum sono
																						// valorizzati vuol
																						// dire che non c'è
																						// stato azzeramento
																						// quindi devo
																						// scartare la pena
																						// [FT] - 03/08/2016 -
																						// MAC_LOG - Utilizzo
																						// la variabile di
																						// istanza siesLogger
																						// al posto di
																						// LogF3B.getLogger()
					siesLogger.debug("Scarto la pena da ARCH SIEP perchè con quantum positivi.");
					lPenaResMod = null;
				}
			} else if (aTipoPena == ICostantiCalcoloPena.PENA_SOSPENSIONE_RES) {
				// Nel caso di una sospensione RES il quantumdi pena va recuperato
				// direttamente sul record SOSPENSIONE e non sul record PENA_RESIDUA
				// in quanto su quest'ultimo i dati non sono affidabili.
				// Nel solo caso di

				SospensioneModel lSospMod = null;

				lSospSqlDAO = new SospensioneSqlDAO(lConn);
				lSospSqlDAO.ricercaSospensionePerPenaIniziale(aEventoIniziale.getIdEvento());
				lSospMod = (SospensioneModel) lSospSqlDAO.getModelByKey();

				// ======================================================================
				// Scarico i quantum di pena su un PenaResiduaModel per rendere i dati
				// omogenei
				// ======================================================================
				if (lSospMod != null) {
					lPenaResMod = new PenaResiduaModel();
					lPenaResMod.setIdPenaResidua(new BigDecimal(0)); // devo impostarlo per evitare l'errore

					lPenaResMod.setNumGiorniReclusione(lSospMod.getNumGiorniPenaResiduaReclus());
					lPenaResMod.setNumMesiReclusione(lSospMod.getNumMesiPenaResiduaReclus());
					lPenaResMod.setNumAnniReclusione(lSospMod.getNumAnniPenaResiduaReclus());
					lPenaResMod.setImportoMulta(lSospMod.getMultaResidua());

					lPenaResMod.setNumGiorniArresto(lSospMod.getNumGiorniPenaResiduaArres());
					lPenaResMod.setNumMesiArresto(lSospMod.getNumMesiPenaResiduaArres());
					lPenaResMod.setNumAnniArresto(lSospMod.getNumAnniPenaResiduaArres());
					lPenaResMod.setImportoAmmenda(lSospMod.getAmmendaResidua());
				}
			} else if (aTipoPena == ICostantiCalcoloPena.PENA_MANUALE_RES) {
				lAnnoManSqlDao = new AnnotazioneManualeSqlDAO(lConn);
				lAnnoManSqlDao.ricercaAnnotazioneManualeByIdEvento(aEventoIniziale.getIdEvento());
				AnnotazioneManualeModel lAnnoMod = (AnnotazioneManualeModel) lAnnoManSqlDao.getModelByKey();
				lAnnoManSqlDao.stop();

				if (lAnnoMod != null && lAnnoMod.getCodTipoAnnotazione().equals("003")) {
					lPenaResMod = new PenaResiduaModel();
					lPenaResMod.setIdPenaResidua(new BigDecimal(0)); // devo impostarlo per evitare l'errore

					lPenaResMod.setNumGiorniReclusione(lAnnoMod.getNumGiorniReclusione());
					lPenaResMod.setNumMesiReclusione(lAnnoMod.getNumMesiReclusione());
					lPenaResMod.setNumAnniReclusione(lAnnoMod.getNumAnniReclusione());
					lPenaResMod.setImportoMulta(lAnnoMod.getImportoMulta()); // Attenzione mancano i decimali

					lPenaResMod.setNumGiorniArresto(lAnnoMod.getNumGiorniArresto());
					lPenaResMod.setNumMesiArresto(lAnnoMod.getNumMesiArresto());
					lPenaResMod.setNumAnniArresto(lAnnoMod.getNumAnniArresto());
					lPenaResMod.setImportoAmmenda(lAnnoMod.getImportoAmmenda()); // Attenzione mancano i
																					// decimali
				}

			}
		} catch (DAOException daoEx) {
			throw new F3BException("CalcoloPenaControllerF5.getPena: Non posso leggere : " + daoEx);
		} catch (Exception ex) {
			throw new F3BException("CalcoloPenaControllerF5.getPena: Non posso leggere  : " + ex);
		} finally {
			cleanup(lPenaResSqlDao);
			cleanup(lSospSqlDAO);
			cleanup(lAnnoManSqlDao);

			cleanup(lConn);
		}

		return lPenaResMod;
	}

	/**
	 * Verifica se l'Evento passato è di tipo interrutivo
	 * 
	 * @param aEvento
	 *            - Evento da verificare
	 * @return boolean - Restituisce vero se è un evento di tipo interruttivo
	 */
	public boolean isInterruzionePerStatoEsecuzione(EventoModel aEveModel) throws F3BException {
		// Verifico se l'evento passato è di tipo interrutivo utilizzando tutti i
		// metodi privati già sviluppati.

		boolean lReturn = false;

		/*
		 * L'archiviazione viene controllata a monte if(isArchiviazioneRes(aEveModel)) return true;
		 * 
		 * if(isArchiviazioneSIEP(aEveModel)) return true;
		 */

		// non controllata
		lReturn = this.isCumulo(aEveModel);
		if (lReturn)
			return true;

		// non controllata

		lReturn = this.isDifferimento(aEveModel);
		if (lReturn)
			return true;

		// ---

		lReturn = this.isEspulsione(aEveModel);
		if (lReturn)
			return true;
		lReturn = this.isForzaturaRes(aEveModel);
		if (lReturn)
			return true;
		lReturn = this.isInterruzione(aEveModel);
		if (lReturn)
			return true;
		lReturn = this.isInterruzioneIndulto(aEveModel);
		if (lReturn)
			return true;
		lReturn = this.isInterruzioneRES(aEveModel);
		if (lReturn)
			return true;
		lReturn = this.isPenaManuale(aEveModel);
		if (lReturn)
			return true;
		lReturn = this.isRevocaIndultino(aEveModel);
		if (lReturn)
			return true;
		lReturn = this.isRevocaMA(aEveModel);
		if (lReturn)
			return true;
		lReturn = this.isSospensione(aEveModel);
		if (lReturn)
			return true;
		lReturn = this.isSospensioneMigrata(aEveModel);
		if (lReturn)
			return true;
		lReturn = this.isSospensioneRes(aEveModel);
		if (lReturn)
			return true;

		return false;

	}

	/**
	 * 
	 * @param aFascID
	 * @return
	 * @throws F3BException
	 */
	public EventoModel exRicercaUltimoEvento(BigDecimal aFascID) throws F3BException {
		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;

		EventoModel lEventoMod = null;

		try {
			lConn = getDBConnection();
			Vector lListaEventi = new Vector();
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByIdFascicoloSiepDataInsDesc(aFascID, null);
			lEveSqlDao.start();

			while (lEveSqlDao.next()) {
				lListaEventi.add((EventoModel) lEveSqlDao.getModel());
			}
			lEveSqlDao.stop();

			if (lListaEventi.size() > 0) {
				lEventoMod = (EventoModel) lListaEventi.elementAt(0);
			}
		} catch (DAOException daoEx) {
			throw new F3BException("CalcoloPenaControllerF5.getPena: Non posso leggere : " + daoEx);
		} catch (Exception ex) {
			throw new F3BException("CalcoloPenaControllerF5.getPena: Non posso leggere  : " + ex);
		} finally {
			cleanup(lEveSqlDao);

			cleanup(lConn);
		}

		return lEventoMod;
	}

}