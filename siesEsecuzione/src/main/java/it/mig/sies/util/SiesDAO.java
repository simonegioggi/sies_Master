package it.mig.sies.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import it.mig.sies.model.ConversionePPCumulo;
import it.mig.sies.model.DatiPubblicoMinistero;
import it.mig.sies.model.DatiTribunaleSorveglianza;
import it.mig.sies.model.DatiUfficioSorveglianza;
import it.mig.sies.model.DeclaratoriaEstinzionePena;
import it.mig.sies.model.DescrizioneProvvedimento;
import it.mig.sies.model.DettagliFascicolo;
import it.mig.sies.model.LiberazioneAnticipata;
import it.mig.sies.model.LiberazioneAnticipataCumulo;
import it.mig.sies.model.MisuraSicurezza;
import it.mig.sies.model.MisuraSicurezzaCumulo;
import it.mig.sies.model.PenaAccessoriaCumulo;
import it.mig.sies.model.PeriodoLibertaAnticipata;
import it.mig.sies.model.ProvvedimentoCollegato;
import it.mig.sies.model.ResponseData;
import it.mig.sies.model.RichiesteGECumulo;
import it.mig.sies.model.SanzioniGPCumulo;
import it.mig.sies.model.SanzioniSostitutiveCumulo;
import it.mig.sies.model.Soggetto;
import it.mig.sies.model.TitoloEsecutivo;
import it.mig.sies.model.TitoloGiudiziario;
import it.mig.sies.model.Utente;
import it.mig.sies.type.esecuzione_NEW.ChiaviAnagrafica;
import it.mig.sies.type.esecuzione_NEW.ChiaviProvvedimentoEsecutivo;
import it.mig.sies.type.esecuzione_NEW.ChiaviProvvedimentoGiudiziario;

/**
 * SIES FASE 2 - Classe DAO generale per le operazioni sugli entity definiti tramite MyBatis
 *
 * @author Federico Paparoni
 */
@SuppressWarnings("unchecked")
public class SiesDAO {

	private static final Logger logger = Logger.getLogger(SiesDAO.class);
	private static final SiesDAO istance = new SiesDAO();
	private static final String CUP_DEP = "C006XXXX0012";

	private SiesDAO() {
	}

	public static SiesDAO getIstance() {
		return istance;
	}

	/**
	 * Recupero della sessione dal factory di MyBatis
	 */
	public SqlSession getSession() {
		return MyBatisSessionFactory.getSession();
	}

	/**
	 * Update del soggetto con le nuovi chiavi tornate come risposta
	 */
	public void updateSoggetto(ChiaviAnagrafica chiaviAnagrafica) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		try {
			// Apertura connessione
			session = getSession();
			Soggetto soggetto = new Soggetto();
			soggetto.setChiaveNSC(chiaviAnagrafica.getNsc().longValue());
			soggetto.setChiaveSies(chiaviAnagrafica.getSies().longValue());
			int i = session.update("it.mig.sies.model.Soggetto.update", soggetto);
			logger.info("Update chiavi soggetto: NSC[" + soggetto.getChiaveNSC() + "] SIES["
					+ soggetto.getChiaveSies() + "] Risultato[" + i + "]");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
	}

	/**
	 * Update dei dati del Tribunale di Sorveglianza con le nuove chiavi tornate come risposta
	 */
	public void updateDTS(ChiaviProvvedimentoEsecutivo chiaviProvvedimentoEsecutivo) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		try {
			session = getSession();
			DatiTribunaleSorveglianza dts = new DatiTribunaleSorveglianza();
			dts.setChiaveNSC(chiaviProvvedimentoEsecutivo.getNsc().longValue());
			dts.setChiaveSies(chiaviProvvedimentoEsecutivo.getSies().longValue());
			int i = session.update("it.mig.sies.model.DatiTribunaleSorveglianza.update", dts);
			logger.info("Update chiavi DTS: NSC[" + dts.getChiaveNSC() + "] SIES[" + dts.getChiaveSies()
					+ "] Risultato[" + i + "]");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
	}

	/**
	 * Update dei dati dell'Ufficio di Sorveglianza con le nuove chiavi tornate come risposta
	 */
	public void updateDUS(ChiaviProvvedimentoEsecutivo chiaviProvvedimentoEsecutivo) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		try {
			session = getSession();
			DatiUfficioSorveglianza dus = new DatiUfficioSorveglianza();
			dus.setChiaveNSC(chiaviProvvedimentoEsecutivo.getNsc().longValue());
			dus.setChiaveSies(chiaviProvvedimentoEsecutivo.getSies().longValue());
			int i = session.update("it.mig.sies.model.DatiUfficioSorveglianza.update", dus);
			logger.info("Update chiavi DUS: NSC[" + dus.getChiaveNSC() + "] SIES[" + dus.getChiaveSies()
					+ "] Risultato[" + i + "]");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
	}

	/**
	 * Salvataggio della risposta
	 */
	public void insertResponse(ResponseData responseData) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		try {
			session = getSession();
			session.insert("it.mig.sies.model.ResponseData.insert", responseData);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
	}

	/**
	 * Caricamento dell'utente collegato ad uno specifico idEvento
	 */
	public Utente loadUtente(String idUtente) {
		// Apertura connessione
		SqlSession session = getSession();
		try {
			Utente utente = (Utente) session.selectOne("it.mig.sies.model.Utente.lookup", idUtente);
			return utente;
		} catch (Throwable t) {
			logger.error(ExceptionUtils.getFullStackTrace(t));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
		return null;
	}

	/**
	 * Caricamento di un soggetto collegato ad uno specifico idEvento
	 */
	public Soggetto loadSoggetto(String idEvento) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		Soggetto soggetto = null;
		try {
			session = getSession();
			soggetto = (Soggetto) session.selectOne("it.mig.sies.model.Soggetto.lookupPrincipale",
					Long.valueOf(idEvento));
			if (soggetto == null)
				soggetto = (Soggetto) session.selectOne("it.mig.sies.model.Soggetto.lookup",
						Long.valueOf(idEvento));
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
		return soggetto;
	}

	/**
	 * Caricamento della lista di titoli principali collegati ad uno specifico idEvento
	 */
	public List<TitoloGiudiziario> loadTitoloGiudiziario(String idEvento) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		List<TitoloGiudiziario> titoloGiudiziarioList = null;
		try {
			session = getSession();
			titoloGiudiziarioList = session.selectList("it.mig.sies.model.TitoloGiudiziario.lookup",
					Long.valueOf(idEvento));
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
		return titoloGiudiziarioList;
	}

	/**
	 * Update dei dati del titolo principale con le nuove chiavi tornate come risposta
	 */
	public void updateTitoloGiudiziario(ChiaviProvvedimentoGiudiziario chiaviProvvedimentoGiudiziario) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		try {
			session = getSession();
			Map<String, Long> parameters = new HashMap<>(2);
			parameters.put("sies", Long.valueOf(chiaviProvvedimentoGiudiziario.getSies().longValue()));
			parameters.put("nsc", Long.valueOf(chiaviProvvedimentoGiudiziario.getNsc().longValue()));
			int i = session.update("it.mig.sies.model.TitoloGiudiziario.update", parameters);
			logger.info("Update chiavi Titolo Principale: NSC[" + parameters.get("nsc") + "] SIES["
					+ parameters.get("sies") + "] Risultato[" + i + "]");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
	}

	private boolean testDeclaratoriaEstinzionePena(SqlSession session, String idEvento) {
		boolean ret = false;
		DeclaratoriaEstinzionePena dep = (DeclaratoriaEstinzionePena) session
				.selectOne("it.mig.sies.model.DeclaratoriaEstinzionePena.lookup", Long.valueOf(idEvento));
		if (dep.getTotale() == 2 && dep.getTipo_a() == 1 && dep.getTipo_b() == 1) {
			ret = true;
		}
		return ret;
	}

	private boolean testLACode(String code) {
		boolean ret = false;
		if (code != null && code.length() > 4) {
			if ("0020".equals(code.substring(code.length() - 4, code.length()))) {
				ret = true;
			}
		}
		return ret;
	}

	private String formattaLACode(LiberazioneAnticipata liberazioneAnticipata, String code) {
		String ret = "";
		if (liberazioneAnticipata.getAnticipata() > 0 && liberazioneAnticipata.getSpeciale() > 0
				&& liberazioneAnticipata.getIntegrazione() > 0) {
			ret = "7013";
		} else if (liberazioneAnticipata.getAnticipata() == 0 && liberazioneAnticipata.getSpeciale() > 0
				&& liberazioneAnticipata.getIntegrazione() > 0) {
			ret = "7012";
		} else if (liberazioneAnticipata.getAnticipata() > 0 && liberazioneAnticipata.getSpeciale() == 0
				&& liberazioneAnticipata.getIntegrazione() > 0) {
			ret = "7011";
		} else if (liberazioneAnticipata.getAnticipata() > 0 && liberazioneAnticipata.getSpeciale() > 0
				&& liberazioneAnticipata.getIntegrazione() == 0) {
			ret = "7010";
		}
		if (!"".equals(ret)) {
			ret = "XXXX" + ret + "0020";
		}
		return ret;
	}

	// private int calcolaGiorni(List<PeriodoLibertaAnticipata> periodoLibertaAnticipataList, String stato) {
	// int x = 0;
	// for (PeriodoLibertaAnticipata elem : periodoLibertaAnticipataList) {
	// if (stato.equals(elem.getStatoPermesso())) {
	// x = x + elem.getNumeroGiorni();
	// }
	// }
	// return x;
	// }

	/**
	 * Caricamento dei dati relativi all'Ufficio di Sorveglianza per uno specifico idEvento
	 */
	public DatiUfficioSorveglianza loadUDS(String idEvento) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		DatiUfficioSorveglianza datiUfficioSorveglianza = null;
		try {
			session = getSession();
			datiUfficioSorveglianza = (DatiUfficioSorveglianza) session
					.selectOne("it.mig.sies.model.DatiUfficioSorveglianza.lookup", Long.valueOf(idEvento));
			List<PeriodoLibertaAnticipata> periodoLibertaAnticipataList = session
					.selectList("it.mig.sies.model.PeriodoLibertaAnticipata.lookup", Long.valueOf(idEvento));
			datiUfficioSorveglianza.setPeriodoLibertaAnticipataList(periodoLibertaAnticipataList);

			// Verifica se siamo nel caso di L. A. Speciale (Mev 11 - s2)
			if (testLACode(datiUfficioSorveglianza.getCodiceUnivocoProvvedimento())) {
				// Recupera le informazioni relative alle possibili combinazioni
				LiberazioneAnticipata tenori = (LiberazioneAnticipata) session.selectOne(
						"it.mig.sies.model.LiberazioneAnticipata.lookupTenori", Long.valueOf(idEvento));

				// Procede nella verifica dei casi con piu' di una L.A. per impostare il codice univoco
				if (tenori.getTotale() > 1) {
					String laCode = formattaLACode(tenori,
							datiUfficioSorveglianza.getCodiceUnivocoProvvedimento());
					if (!"".equals(laCode)) {
						datiUfficioSorveglianza.setCodiceUnivocoProvvedimento(laCode);
					}
				}

				// Procede nella verifica delle L.A. per impostare il tipo di giorni
				if (tenori.getTotale() > 0) {
					LiberazioneAnticipata licenza = (LiberazioneAnticipata) session.selectOne(
							"it.mig.sies.model.LiberazioneAnticipata.lookupLicenza", Long.valueOf(idEvento));
					datiUfficioSorveglianza.setNumGiorniLibAnticipata(licenza.getAnticipata());
					datiUfficioSorveglianza.setNumGiorniLibAnticipataLs(licenza.getSpeciale());
					datiUfficioSorveglianza.setNumGiorniLibAnticipataLi(licenza.getIntegrazione());
				}
			}

			// Verifica se siamo nel caso di Declaratoria Estinzione Pena (Mev 11 - s3)
			if (testDeclaratoriaEstinzionePena(session, idEvento)) {
				datiUfficioSorveglianza.setCodiceUnivocoProvvedimento(CUP_DEP);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
		return datiUfficioSorveglianza;
	}

	/**
	 * Caricamento dei dati relativi al Tribunale di Sorveglianza per uno specifico idEvento
	 */
	public DatiTribunaleSorveglianza loadTDS(String idEvento) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		DatiTribunaleSorveglianza datiTribunaleSorveglianza = null;
		try {
			session = getSession();
			datiTribunaleSorveglianza = (DatiTribunaleSorveglianza) session
					.selectOne("it.mig.sies.model.DatiTribunaleSorveglianza.lookup", Long.valueOf(idEvento));
			if (!PropertyUtil.isPresent(datiTribunaleSorveglianza))
				logger.error(
						"ATTENZIONE!!! Dati TdS non trovati! Provvedimento non trasmissibile come Foglio Complementare!");
			List<PeriodoLibertaAnticipata> periodoLibertaAnticipataList = session
					.selectList("it.mig.sies.model.PeriodoLibertaAnticipata.lookup", Long.valueOf(idEvento));

			// [SG]: 20190208 aggiunto controllo consistenza del dato per gestione nullpointer
			if (PropertyUtil.isPresent(datiTribunaleSorveglianza)) {
				datiTribunaleSorveglianza.setPeriodoLibertaAnticipataList(periodoLibertaAnticipataList);

				// Verifica se siamo nel caso di L. A. Speciale (Mev 11 - s2)
				if (testLACode(datiTribunaleSorveglianza.getCodiceUnivocoProvvedimento())) {
					// Recupera le informazioni relative alle possibili combinazioni
					LiberazioneAnticipata tenori = (LiberazioneAnticipata) session.selectOne(
							"it.mig.sies.model.LiberazioneAnticipata.lookupTenori", Long.valueOf(idEvento));

					// Procede nella verifica dei casi con piu' di una L.A. per impostare il codice univoco
					if (tenori.getTotale() > 1) {
						String laCode = formattaLACode(tenori,
								datiTribunaleSorveglianza.getCodiceUnivocoProvvedimento());
						if (!"".equals(laCode)) {
							datiTribunaleSorveglianza.setCodiceUnivocoProvvedimento(laCode);
						}
					}

					// Procede nella verifica delle L.A. per impostare il tipo di giorni
					if (tenori.getTotale() > 0) {
						LiberazioneAnticipata licenza = (LiberazioneAnticipata) session.selectOne(
								"it.mig.sies.model.LiberazioneAnticipata.lookupLicenza", Long.valueOf(idEvento));
						datiTribunaleSorveglianza.setGiorniLibertaAnticipata(licenza.getAnticipata());
						datiTribunaleSorveglianza.setGiorniLibertaAnticipataLs(licenza.getSpeciale());
						datiTribunaleSorveglianza.setGiorniLibertaAnticipataLi(licenza.getIntegrazione());
					}
				}

				// Verifica se siamo nel caso di Declaratoria Estinzione Pena (Mev 11 - s3)
				if (testDeclaratoriaEstinzionePena(session, idEvento)) {
					datiTribunaleSorveglianza.setCodiceUnivocoProvvedimento(CUP_DEP);
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
		return datiTribunaleSorveglianza;
	}

	/**
	 * Delete logica del soggetto
	 */
	public void deleteLogicaSoggetto(ChiaviAnagrafica chiaviAnagrafica) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		try {
			session = getSession();
			Soggetto soggetto = new Soggetto();
			soggetto.setChiaveSies(chiaviAnagrafica.getSies().longValue());
			int i = session.update("it.mig.sies.model.Soggetto.deleteLogica", soggetto);
			logger.info("Update chiavi soggetto: NSC[" + soggetto.getChiaveNSC() + "] SIES["
					+ soggetto.getChiaveSies() + "] Risultato[" + i + "]");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
	}

	/**
	 * Delete logica dei dati del Tribunale di Sorveglianza
	 */
	public void deleteLogicaDTS(ChiaviProvvedimentoEsecutivo chiaviProvvedimentoEsecutivo) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		try {
			session = getSession();
			DatiTribunaleSorveglianza dts = new DatiTribunaleSorveglianza();
			dts.setChiaveSies(chiaviProvvedimentoEsecutivo.getSies().longValue());
			int i = session.update("it.mig.sies.model.DatiTribunaleSorveglianza.deleteLogica", dts);
			logger.info("Update chiavi DTS: NSC[" + dts.getChiaveNSC() + "] SIES[" + dts.getChiaveSies()
					+ "] Risultato[" + i + "]");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
	}

	/**
	 * Delete logica dei dati dell'Ufficio di Sorveglianza
	 */
	public void deleteLogicaDUS(ChiaviProvvedimentoEsecutivo chiaviProvvedimentoEsecutivo) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		try {
			session = getSession();
			DatiUfficioSorveglianza dus = new DatiUfficioSorveglianza();
			dus.setChiaveSies(chiaviProvvedimentoEsecutivo.getSies().longValue());
			int i = session.update("it.mig.sies.model.DatiUfficioSorveglianza.deleteLogica", dus);
			logger.info("Update chiavi DUS: NSC[" + dus.getChiaveNSC() + "] SIES[" + dus.getChiaveSies()
					+ "] Risultato[" + i + "]");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
	}

	/**
	 * Delete logica del titolo principale
	 */
	public void deleteLogicaTitoloGiudiziario(ChiaviProvvedimentoGiudiziario chiaviProvvedimentoGiudiziario) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		try {
			session = getSession();
			Map<String, Long> parameters = new HashMap<>(2);
			parameters.put("sies", Long.valueOf(chiaviProvvedimentoGiudiziario.getSies().longValue()));
			int i = session.update("it.mig.sies.model.TitoloGiudiziario.deleteLogica", parameters);
			logger.info("Update chiavi Titolo Principale: NSC[" + parameters.get("nsc") + "] SIES["
					+ parameters.get("sies") + "] Risultato[" + i + "]");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
	}

	/**
	 * Chiude la sessione aperta verso il database
	 *
	 * @param session
	 */
	public void closeSession(SqlSession session) {
		session.commit();
		session.close();
	}

	/**
	 * Caricamento dei dettagli relativi ad un singolo idEvento
	 */
	public DettagliFascicolo loadDettagliFascicolo(String idEvento) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		DettagliFascicolo dettagliFascicolo = null;
		try {
			session = getSession();
			dettagliFascicolo = (DettagliFascicolo) session
					.selectOne("it.mig.sies.model.DettagliFascicolo.lookup", Long.valueOf(idEvento));
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
		return dettagliFascicolo;
	}

	/**
	 * Caricamento del campo note di un Tribunale di Sorveglianza
	 */
	public String loadNoteTribunaleSorveglianza(String idEvento) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		String note = "";
		try {
			session = getSession();
			note = (String) session.selectOne("it.mig.sies.model.DatiTribunaleSorveglianza.lookupNote",
					Long.valueOf(idEvento));
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
		return note;
	}

	/**
	 * Caricamento del campo idRevocato (terzo collegato) per l'Ufficio di Sorveglianza
	 */
	public long loadIdRevocatoUfficioSorveglianza(String idEvento) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		Object objectReturned = null;
		try {
			session = getSession();
			objectReturned = session.selectOne("it.mig.sies.model.DatiUfficioSorveglianza.lookupRevocato",
					Long.valueOf(idEvento));
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
		if (objectReturned == null)
			return 0;
		else
			return (Long) objectReturned;
	}

	/**
	 * MEV 23010 - Modificato oggetto di ritorno per mappare chiave SIES
	 *
	 * Caricamento del terzo collegato per il Tribunale di Sorveglianza
	 */
	public List<ProvvedimentoCollegato> loadRevocatoTribunaleSorveglianza(String idEvento) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		List<ProvvedimentoCollegato> objectReturned = null;
		try {
			session = getSession();
			objectReturned = session.selectList("it.mig.sies.model.DatiTribunaleSorveglianza.lookupRevocato",
					Long.valueOf(idEvento));
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
		if ((objectReturned == null) || (objectReturned.size() == 0))
			return null;
		else
			return objectReturned;
	}

	/**
	 * Caricamento di una lista di titoli da trasferire
	 */
	public List<TitoloEsecutivo> loadTrasferEntry(String tipologia) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		List<TitoloEsecutivo> entryList = null;
		try {
			session = getSession();
			entryList = session.selectList("it.mig.sies.model.TitoloEsecutivo.lookupTrasfer", tipologia);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
		return entryList;
	}

	/**
	 * Caricamento del titolo esecutivo relativo ad uno specifico idEvento
	 */
	public TitoloEsecutivo loadTitoloEsecutivo(String idEvento) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		TitoloEsecutivo titoloEsecutivo = null;
		try {
			session = getSession();
			titoloEsecutivo = (TitoloEsecutivo) session.selectOne("it.mig.sies.model.TitoloEsecutivo.lookup",
					Long.valueOf(idEvento));
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
		return titoloEsecutivo;
	}

	/**
	 * Caricamento della risposta di una specifica operazione
	 */
	public ResponseData loadResponse(String id) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		ResponseData responseData = null;
		try {
			session = getSession();
			responseData = (ResponseData) session.selectOne("it.mig.sies.model.ResponseData.lookup",
					Long.valueOf(id));
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
		return responseData;
	}

	/**
	 * Caricamento delle operazioni effettuate su un singolo idEvento
	 */
	public List<ResponseData> loadResponseList(String idEvento) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		List<ResponseData> responseDataList = null;
		try {
			session = getSession();
			responseDataList = session.selectList("it.mig.sies.model.ResponseData.search",
					Long.valueOf(idEvento));
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
		return responseDataList;
	}

	public List<MisuraSicurezza> loadMisuraSicurezza(String idEvento) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		List<MisuraSicurezza> misuraSicurezzaList = null;
		try {
			session = getSession();
			misuraSicurezzaList = session.selectList("it.mig.sies.model.MisuraSicurezza.lookup",
					Long.valueOf(idEvento));
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
		return misuraSicurezzaList;
	}

	/**
	 * MEV 35253 Caricamento dei profili relativi all'utente
	 */
	public List<String> loadProfiliUtente(String idUtente) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		List<String> profiliList = null;
		try {
			session = getSession();
			profiliList = session.selectList("it.mig.sies.model.Utente.verificaProfili", idUtente);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
		return profiliList;
	}

	/**
	 * MEV 23010 Caricamento del dettaglio relativo al singolo codice univoco
	 */
	public DescrizioneProvvedimento loadDescrizioneProvvedimento(String codiceUnivoco) {
		String codiceOggetto = codiceUnivoco.substring(0, 4);
		String codiceMotivo = codiceUnivoco.substring(4, 8);
		String codiceEsito = codiceUnivoco.substring(8, 12);
		Map<String, String> params = new HashMap<>();
		params.put("codiceOggetto", codiceOggetto);
		params.put("codiceMotivo", codiceMotivo);
		params.put("codiceEsito", codiceEsito);

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		DescrizioneProvvedimento descrizioneProvvedimento = null;
		try {
			session = getSession();
			descrizioneProvvedimento = (DescrizioneProvvedimento) session
					.selectOne("it.mig.sies.model.DescrizioneProvvedimento.lookup", params);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
		if ("MISURECAUTE1".equals(codiceUnivoco))
			descrizioneProvvedimento = new DescrizioneProvvedimento();
		descrizioneProvvedimento.setCodiceUnivoco(codiceUnivoco);
		return descrizioneProvvedimento;
	}

	/**
	 * Update della tabella D_DOCUMENTO_ALLEGATO. Si settano i campi DATA_EMISSIONE e DATA_TRASMISSIONE uguali
	 * a sysdate nel caso di Trasmissione FC, il campo DATA_ULT_INVIO uguale a sysdate nel caso di Modifica
	 * FC, il campo DATA_TRASMISSIONE uguale a null in caso di Cancellazione FC e il campo DATA_ANNULLAMENTO
	 * uguale a sysdate, e FLAG_DOCUMENTO_REGISTRATO uguale ad 'A' in caso di Annullamento FC. In caso di
	 * Annullamento del FC viene settato il campo FLAG_DOCUMENTO_REGISTRATO uguale ad 'A' sulla tabella EVENTO
	 */
	public void updateDocumentoAllegato(String idEvento, String action) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		try {
			session = getSession();
			if (action.equals("INSERT")) {
				session.update("it.mig.sies.model.TitoloEsecutivo.updateDocAllegatoTras", Long.valueOf(idEvento));
			} else if (action.equals("UPDATE")) {
				session.update("it.mig.sies.model.TitoloEsecutivo.updateDocAllegatoMod", Long.valueOf(idEvento));
			} else if (action.equals("DELETE")) {
				session.update("it.mig.sies.model.TitoloEsecutivo.updateDocAllegatoCanc", Long.valueOf(idEvento));
			} else if (action.equals("ANNULLA")) {
				session.update("it.mig.sies.model.TitoloEsecutivo.updateDocAllegatoAnnul",
						Long.valueOf(idEvento));
				session.update("it.mig.sies.model.TitoloEsecutivo.updateEventoAnnul", Long.valueOf(idEvento));
			} else if (action.equals("ANNULLACFC")) {
				session.update("it.mig.sies.model.TitoloEsecutivo.updateDocAllegatoAnnul",
						Long.valueOf(idEvento));
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
	}

	/**
	 * MEV 06 Verifica che il foglio complementare sia stato trasmesso ad NSC e che quindi sia possibile
	 * cancellarlo
	 */
	public boolean verificaPresenza(String idEvento) {
		boolean presente = false;

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		try {
			session = getSession();
			int count = (Integer) session.selectOne("it.mig.sies.model.ResponseData.verificaPresenza",
					Long.valueOf(idEvento));
			presente = count > 0 ? true : false;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
		return presente;
	}

	/**
	 * MEV 16 + MEV 31: metodo per il recupero della descrizione del comune di nascita del sinonimo
	 *
	 * @param codiceLuogoNascita
	 * @return String
	 */
	public String getDescComune(String codiceLuogoNascita) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		String descComune = "";
		try {
			session = getSession();
			descComune = (String) session.selectOne("it.mig.sies.model.Soggetto.getDescComune",
					codiceLuogoNascita);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
		return descComune;
	}

	/**
	 * MEV 16 + MEV 31: metodo per il recupero della descrizione della nazione di nascita del sinonimo
	 *
	 * @param codiceNazioneNascita
	 * @return String
	 */
	public String getDescNazione(String codiceNazioneNascita) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		String descNazione = "";
		try {
			session = getSession();
			descNazione = (String) session.selectOne("it.mig.sies.model.Soggetto.getDescNazione",
					codiceNazioneNascita);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
		return descNazione;
	}

	/**
	 * MEV 16: Recupero dati Soggetto dato il suo ID
	 *
	 * @param idSoggetto
	 * @return String
	 */
	public Soggetto getSoggettoByID(String idSoggetto) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		Soggetto soggetto = null;
		try {
			session = getSession();
			soggetto = (Soggetto) session.selectOne("it.mig.sies.model.Soggetto.getSoggettoByID",
					Long.valueOf(idSoggetto));
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
		return soggetto;
	}

	/**
	 * MEV 16: Recupero dati titolo esecutivo dato il suo ID
	 *
	 * @param idEvento
	 * @return TitoloEsecutivo
	 */
	public TitoloEsecutivo getTitoloEsecutivoByID(String idEvento) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		TitoloEsecutivo titoloEsecutivo = null;
		try {
			session = getSession();
			titoloEsecutivo = (TitoloEsecutivo) session.selectOne(
					"it.mig.sies.model.TitoloEsecutivo.getTitoloEsecutivoByID", Long.valueOf(idEvento));
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
		return titoloEsecutivo;
	}

	/**
	 * MEV 16: Recupero dati titolo giudiziario dato il suo ID
	 *
	 * @param idSentenza
	 * @param idFascicoloSiep
	 * @param idEvento
	 * @param isForCumulo
	 * @param idEvento
	 * @return List<TitoloGiudiziario>
	 */
	public List<TitoloGiudiziario> getTitoloGiudiziarioByID(String idSentenza, String idFascicoloSiep,
			// MEV 16 CUMULO: aggiunto parametro di passaggio
			boolean isForCumulo, String idEvento) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		// MEV 16 CUMULO: aggiunte query per estrazioni dati cumulo
		List<TitoloGiudiziario> titoloGiudiziarioList = null;
		try {
			session = getSession();

			Map<String, Long> param = new HashMap<>(2);
			if (isForCumulo) {
				param.put("idFascicoloSiep", Long.valueOf(idFascicoloSiep));
				param.put("idEvento", Long.valueOf(idEvento));
				String idIstruttoriaCumulo = (String) session
						.selectOne("it.mig.sies.model.TitoloGiudiziario.getIdIstruttoriaCumuloByID", param);
				titoloGiudiziarioList = session.selectList(
						"it.mig.sies.model.TitoloGiudiziario.getTitoliEsecutiviPerCumuloByID",
						Long.valueOf(idIstruttoriaCumulo));
			} else {
				param.put("idSentenza", Long.valueOf(idSentenza));
				param.put("idFascicoloSiep", Long.valueOf(idFascicoloSiep));
				titoloGiudiziarioList = session
						.selectList("it.mig.sies.model.TitoloGiudiziario.getTitoloGiudiziarioByID", param);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
		return titoloGiudiziarioList;
	}

	/**
	 * MEV 16: Recupero dati utente dato il suo ID
	 *
	 * @param idUtente
	 * @return Utente
	 */
	public Utente getUtenteByID(String idUtente) {

		// Apertura connessione
		SqlSession session = getSession();
		Utente utente = null;
		try {
			utente = (Utente) session.selectOne("it.mig.sies.model.Utente.getUtenteByID", idUtente);
		} catch (Throwable t) {
			logger.error(ExceptionUtils.getFullStackTrace(t));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
		return utente;
	}

	/**
	 * MEV 16: Caricamento dei dati relativi al Pubblico Ministero per uno specifico idEvento
	 *
	 * @param idEvento
	 * @param idFascicoloSiep
	 * @param isForCumulo
	 * @param isAvvenutaEsecuzionePena
	 * @return List<DatiPubblicoMinistero>
	 */
	public List<DatiPubblicoMinistero> loadDatiProvvedimentoPM(String idEvento, String idFascicoloSiep,
			boolean isForCumulo, boolean isAvvenutaEsecuzionePena) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		List<DatiPubblicoMinistero> datiPubblicoMinisteroList = null;
		try {
			session = getSession();

			// controllo se trattasi di cumulo, cambia la query di estrazione dati
			if (isForCumulo) {
				datiPubblicoMinisteroList = session.selectList(
						"it.mig.sies.model.DatiPubblicoMinistero.lookupCumulo", Long.valueOf(idEvento));
				// MEV 16 CUMULO: aggiunta gestione MS + PA + altri dati associati al cumulo
				if (datiPubblicoMinisteroList != null && !datiPubblicoMinisteroList.isEmpty()) {
					DatiPubblicoMinistero dpm = datiPubblicoMinisteroList.getFirst();
					List<MisuraSicurezzaCumulo> msc = session.selectList(
							"it.mig.sies.model.DatiPubblicoMinistero.findMSCumulo", Long.valueOf(idEvento));
					List<PenaAccessoriaCumulo> pac = session.selectList(
							"it.mig.sies.model.DatiPubblicoMinistero.findPACumulo", Long.valueOf(idEvento));

					// Popolo le liste
					if (PropertyUtil.isPresent(msc))
						dpm.setListaMisureSicurezzaCumulo(msc);
					if (PropertyUtil.isPresent(pac))
						dpm.setListaPeneAccessorieCumulo(pac);

					// Sanzioni Sostitutive
					// 1 Semidetenzione
					SanzioniSostitutiveCumulo sdSS = (SanzioniSostitutiveCumulo) session.selectOne(
							"it.mig.sies.model.DatiPubblicoMinistero.getSemidetenzioneSSCumuloByIDEvento",
							Long.valueOf(idEvento));
					// 2 Liberta' Controllata
					SanzioniSostitutiveCumulo lcSS = (SanzioniSostitutiveCumulo) session.selectOne(
							"it.mig.sies.model.DatiPubblicoMinistero.getLibertaControllataSSCumuloByIDEvento",
							Long.valueOf(idEvento));
					// 3 Espulsione Stato
					SanzioniSostitutiveCumulo esSS = (SanzioniSostitutiveCumulo) session.selectOne(
							"it.mig.sies.model.DatiPubblicoMinistero.getEspulsioneStatoSSCumuloByIDEvento",
							Long.valueOf(idEvento));
					// 4 Pena Pecuniaria
					List<SanzioniSostitutiveCumulo> ppSS = session.selectList(
							"it.mig.sies.model.DatiPubblicoMinistero.getPenePecuniarieSSCumuloByIDEvento",
							Long.valueOf(idEvento));
					// 5 Lavoro Pubblica Utilita'
					SanzioniSostitutiveCumulo lpuSS = (SanzioniSostitutiveCumulo) session.selectOne(
							"it.mig.sies.model.DatiPubblicoMinistero.getLavoroPubblicaUtilitaSSCumuloByIDEvento",
							Long.valueOf(idEvento));
					// 6 CPP - Lavoro Sostitutivo
					ConversionePPCumulo lsCPP = (ConversionePPCumulo) session.selectOne(
							"it.mig.sies.model.DatiPubblicoMinistero.getLavoroSostitutivoCPPCumuloByIDEvento",
							Long.valueOf(idEvento));
					// 7 CPP - LibertÃ  Controllata
					ConversionePPCumulo lcCPP = (ConversionePPCumulo) session.selectOne(
							"it.mig.sies.model.DatiPubblicoMinistero.getLibertaControllataCPPCumuloByIDEvento",
							Long.valueOf(idEvento));
					// 8 GP - Permanenza Domiciliare
					SanzioniGPCumulo pdGP = (SanzioniGPCumulo) session.selectOne(
							"it.mig.sies.model.DatiPubblicoMinistero.getPermanenzaDomiciliareGPCumuloByIDEvento",
							Long.valueOf(idEvento));
					// 9 GP - Lavoro Sostitutivo
					SanzioniGPCumulo lsGP = (SanzioniGPCumulo) session.selectOne(
							"it.mig.sies.model.DatiPubblicoMinistero.getLavoroSostitutivoGPCumuloByIDEvento",
							Long.valueOf(idEvento));
					// 10 GP - Lavoro Pubblica Utilita'
					SanzioniGPCumulo lpuGP = (SanzioniGPCumulo) session.selectOne(
							"it.mig.sies.model.DatiPubblicoMinistero.getLavoroPubblicaUtilitaGPCumuloByIDEvento",
							Long.valueOf(idEvento));
					// 11 GP - Espulsione Stato
					SanzioniGPCumulo esGP = (SanzioniGPCumulo) session.selectOne(
							"it.mig.sies.model.DatiPubblicoMinistero.getEspulsioneStatoGPCumuloByIDEvento",
							Long.valueOf(idEvento));

					// Liberazione Anticipata
					LiberazioneAnticipataCumulo lac = (LiberazioneAnticipataCumulo) session.selectOne(
							"it.mig.sies.model.DatiPubblicoMinistero.getLiberazioneAnticipataCumuloByIDEvento",
							Long.valueOf(idEvento));

					// Richieste GE
					List<RichiesteGECumulo> rgec = session.selectList(
							"it.mig.sies.model.DatiPubblicoMinistero.getRichiesteGECumuloByIDEvento",
							Long.valueOf(idEvento));

					// Popolo gli altri dati
					SanzioniSostitutiveCumulo ssc = null;
					ConversionePPCumulo cppc = null;
					SanzioniGPCumulo sgpc = null;

					if (sdSS != null || lcSS != null || esSS != null || PropertyUtil.isPresent(ppSS)
							|| lpuSS != null)
						ssc = new SanzioniSostitutiveCumulo();
					if (lsCPP != null || lcCPP != null)
						cppc = new ConversionePPCumulo();
					if (pdGP != null || lsGP != null || lpuGP != null || esGP != null)
						sgpc = new SanzioniGPCumulo();

					if (sdSS != null) {
						ssc.setAnniSemidetenzione(sdSS.getAnniSemidetenzione());
						ssc.setMesiSemidetenzione(sdSS.getMesiSemidetenzione());
						ssc.setGiorniSemidetenzione(sdSS.getGiorniSemidetenzione());
					}
					if (lcSS != null) {
						ssc.setAnniLibertaControllata(lcSS.getAnniLibertaControllata());
						ssc.setMesiLibertaControllata(lcSS.getMesiLibertaControllata());
						ssc.setGiorniLibertaControllata(lcSS.getGiorniLibertaControllata());
					}
					if (esSS != null) {
						ssc.setAnniEspulsioneStato(esSS.getAnniEspulsioneStato());
						ssc.setMesiEspulsioneStato(esSS.getMesiEspulsioneStato());
						ssc.setGiorniEspulsioneStato(esSS.getGiorniEspulsioneStato());
						ssc.setCodiceTipoEspulsioneStato(esSS.getCodiceTipoEspulsioneStato());
					}
					if (ppSS != null) {
						for (SanzioniSostitutiveCumulo elem : ppSS) {
							if (elem.getImportoAmmenda() != 0)
								ssc.setImportoAmmenda(elem.getImportoAmmenda());
							if (elem.getImportoMulta() != 0)
								ssc.setImportoMulta(elem.getImportoMulta());
						}
					}
					if (lpuSS != null) {
						ssc.setAnniLavoroPubblicaUtilita(lpuSS.getAnniLavoroPubblicaUtilita());
						ssc.setMesiLavoroPubblicaUtilita(lpuSS.getMesiLavoroPubblicaUtilita());
						ssc.setGiorniLavoroPubblicaUtilita(lpuSS.getGiorniLavoroPubblicaUtilita());
						ssc.setOreLavoroPubblicaUtilita(lpuSS.getOreLavoroPubblicaUtilita());
						ssc.setCodTipoLavoroPubblicaUtilita(lpuSS.getCodTipoLavoroPubblicaUtilita());
					}
					if (lsCPP != null) {
						cppc.setAnniLavoroSostitutivo(lsCPP.getAnniLavoroSostitutivo());
						cppc.setMesiLavoroSostitutivo(lsCPP.getMesiLavoroSostitutivo());
						cppc.setGiorniLavoroSostitutivo(lsCPP.getGiorniLavoroSostitutivo());
					}
					if (lcCPP != null) {
						cppc.setAnniLibertaControllata(lcCPP.getAnniLibertaControllata());
						cppc.setMesiLibertaControllata(lcCPP.getMesiLibertaControllata());
						cppc.setGiorniLibertaControllata(lcCPP.getGiorniLibertaControllata());
					}
					if (pdGP != null) {
						sgpc.setAnniPermanenzaDomiciliare(pdGP.getAnniPermanenzaDomiciliare());
						sgpc.setMesiPermanenzaDomiciliare(pdGP.getMesiPermanenzaDomiciliare());
						sgpc.setGiorniPermanenzaDomiciliare(pdGP.getGiorniPermanenzaDomiciliare());
					}
					if (lsGP != null) {
						sgpc.setAnniLavoroSostitutivo(lsGP.getAnniLavoroSostitutivo());
						sgpc.setMesiLavoroSostitutivo(lsGP.getMesiLavoroSostitutivo());
						sgpc.setGiorniLavoroSostitutivo(lsGP.getGiorniLavoroSostitutivo());

					}
					if (lpuGP != null) {
						sgpc.setAnniLavoroPubblicaUtilita(lpuGP.getAnniLavoroPubblicaUtilita());
						sgpc.setMesiLavoroPubblicaUtilita(lpuGP.getMesiLavoroPubblicaUtilita());
						sgpc.setGiorniLavoroPubblicaUtilita(lpuGP.getGiorniLavoroPubblicaUtilita());

					}
					if (esGP != null) {
						sgpc.setAnniEspulsioneStato(esGP.getAnniEspulsioneStato());
						sgpc.setMesiEspulsioneStato(esGP.getMesiEspulsioneStato());
						sgpc.setGiorniEspulsioneStato(esGP.getGiorniEspulsioneStato());
						sgpc.setCodiceTipoEspulsioneStato(esGP.getCodiceTipoEspulsioneStato());
					}

					// Popolo il model dei dati PM
					if (ssc != null)
						dpm.setSanzioniSostitutiveCumulo(ssc);
					if (cppc != null)
						dpm.setConversionePPCumulo(cppc);
					if (sgpc != null)
						dpm.setSanzioniGPCumulo(sgpc);
					if (lac != null)
						dpm.setLiberazioneAnticipataCumulo(lac);
					if (PropertyUtil.isPresent(rgec))
						dpm.setListaRichiesteGECumulo(rgec);

					// pulisco la lista
					datiPubblicoMinisteroList.clear();
					// e la aggiorno di nuovo
					datiPubblicoMinisteroList.addFirst(dpm);
				}
			} else if (isAvvenutaEsecuzionePena) {
				Map<String, Long> parameters = new HashMap<>(2);
				parameters.put("idFascicoloSiep", Long.valueOf(idFascicoloSiep));
				parameters.put("idEvento", Long.valueOf(idEvento));
				datiPubblicoMinisteroList = session
						.selectList("it.mig.sies.model.DatiPubblicoMinistero.lookupAEP", parameters);
			} else {
				datiPubblicoMinisteroList = session
						.selectList("it.mig.sies.model.DatiPubblicoMinistero.lookupSP", Long.valueOf(idEvento));
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}

		// valore di ritorno
		return datiPubblicoMinisteroList;
	}

	/**
	 * MEV 16: Caricamento dei dati relativi al fascicolo per uno specifico idEvento
	 *
	 * @param idEvento
	 * @return DettagliFascicolo
	 */
	public DettagliFascicolo getFascicoloByID(String idEvento) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		DettagliFascicolo dettagliFascicolo = null;
		try {
			session = getSession();
			dettagliFascicolo = (DettagliFascicolo) session
					.selectOne("it.mig.sies.model.DettagliFascicolo.getFascicoloByID", Long.valueOf(idEvento));
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
		return dettagliFascicolo;
	}

	/**
	 * MEV 16: metodo che esegue la cancellazione logica del soggetto
	 *
	 * @param chiaviAnagrafica
	 */
	public void deleteLogicaSoggettoFC(
			it.mig.sies.type.foglicomplementari.ChiaviAnagrafica chiaviAnagrafica) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		try {
			session = getSession();
			Soggetto soggetto = new Soggetto();
			soggetto.setChiaveSies(chiaviAnagrafica.getSies().longValue());
			int i = session.update("it.mig.sies.model.Soggetto.deleteLogica", soggetto);
			logger.info("Update chiavi soggetto: NSC[" + soggetto.getChiaveNSC() + "] SIES["
					+ soggetto.getChiaveSies() + "] Risultato[" + i + "]");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
	}

	/**
	 * MEV 16: metodo che esegue la cancellazione logica del titolo principale
	 *
	 * @param chiaviProvvedimentoGiudiziario
	 */
	public void deleteLogicaTitoloGiudiziarioFC(
			it.mig.sies.type.foglicomplementari.ChiaviProvvedimentoGiudiziario chiaviProvvedimentoGiudiziario) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		try {
			session = getSession();
			Map<String, Long> parameters = new HashMap<>(2);
			parameters.put("sies", Long.valueOf(chiaviProvvedimentoGiudiziario.getSies().longValue()));
			int i = session.update("it.mig.sies.model.TitoloGiudiziario.deleteLogica", parameters);
			logger.info("Update chiavi Titolo Principale: NSC[" + parameters.get("nsc") + "] SIES["
					+ parameters.get("sies") + "] Risultato[" + i + "]");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
	}

	/**
	 * MEV 16: metodo che esegue la cancellazione logica dei dati del PM
	 *
	 * @param chiaviProvvedimentoEsecutivo
	 */
	public void deleteLogicaPM(
			it.mig.sies.type.foglicomplementari.ChiaviProvvedimentoEsecutivo chiaviProvvedimentoEsecutivo) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		try {
			session = getSession();
			DatiPubblicoMinistero dpm = new DatiPubblicoMinistero();
			dpm.setChiaveSies(chiaviProvvedimentoEsecutivo.getSies().longValue());
			int i = session.update("it.mig.sies.model.DatiPubblicoMinistero.deleteLogica", dpm);
			logger.info("Update chiavi DPM: NSC[" + dpm.getChiaveNSC() + "] SIES[" + dpm.getChiaveSies()
					+ "] Risultato[" + i + "]");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
	}

	/**
	 * MEV 16: Update del soggetto con le nuovi chiavi tornate come risposta
	 *
	 * @param chiaviAnagrafica
	 */
	public void updateSoggettoFC(it.mig.sies.type.foglicomplementari.ChiaviAnagrafica chiaviAnagrafica) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		try {
			// Apertura connessione
			session = getSession();
			Soggetto soggetto = new Soggetto();
			soggetto.setChiaveNSC(chiaviAnagrafica.getNsc().longValue());
			soggetto.setChiaveSies(chiaviAnagrafica.getSies().longValue());
			int i = session.update("it.mig.sies.model.Soggetto.update", soggetto);
			logger.info("Update chiavi soggetto: NSC[" + soggetto.getChiaveNSC() + "] SIES["
					+ soggetto.getChiaveSies() + "] Risultato[" + i + "]");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
	}

	/**
	 * MEV 16: Update dei dati del titolo principale con le nuove chiavi tornate come risposta
	 *
	 * @param chiaviProvvedimentoGiudiziario
	 */
	public void updateTitoloGiudiziarioFC(
			it.mig.sies.type.foglicomplementari.ChiaviProvvedimentoGiudiziario chiaviProvvedimentoGiudiziario) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		try {
			session = getSession();
			Map<String, Long> parameters = new HashMap<>(2);
			parameters.put("sies", Long.valueOf(chiaviProvvedimentoGiudiziario.getSies().longValue()));
			parameters.put("nsc", Long.valueOf(chiaviProvvedimentoGiudiziario.getNsc().longValue()));
			int i = session.update("it.mig.sies.model.TitoloGiudiziario.update", parameters);
			logger.info("Update chiavi Titolo Principale: NSC[" + parameters.get("nsc") + "] SIES["
					+ parameters.get("sies") + "] Risultato[" + i + "]");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
	}

	/**
	 * MEV 16: metodo che esegue l'aggiornamento dei dati del PM
	 *
	 * @param chiaviProvvedimentoEsecutivo
	 */
	public void updatePM(
			it.mig.sies.type.foglicomplementari.ChiaviProvvedimentoEsecutivo chiaviProvvedimentoEsecutivo) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		try {
			session = getSession();
			DatiPubblicoMinistero dpm = new DatiPubblicoMinistero();
			dpm.setChiaveNSC(chiaviProvvedimentoEsecutivo.getNsc().longValue());
			dpm.setChiaveSies(chiaviProvvedimentoEsecutivo.getSies().longValue());
			int i = session.update("it.mig.sies.model.DatiPubblicoMinistero.update", dpm);
			logger.info("Update chiavi DPM: NSC[" + dpm.getChiaveNSC() + "] SIES[" + dpm.getChiaveSies()
					+ "] Risultato[" + i + "]");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
	}

	/**
	 * MEV 16: metodo per l'aggiornamento della tabella "MISURA_CAUTELARE"
	 *
	 * @param chiaviProvvedimentoEsecutivo
	 */
	public void updateMisCautelare(
			it.mig.sies.type.foglicomplementari.ChiaviProvvedimentoEsecutivo chiaviProvvedimentoEsecutivo) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		try {
			session = getSession();
			DatiPubblicoMinistero dpm = new DatiPubblicoMinistero();
			dpm.setChiaveNSC(chiaviProvvedimentoEsecutivo.getNsc().longValue());
			dpm.setChiaveSies(chiaviProvvedimentoEsecutivo.getSies().longValue());
			int i = session.update("it.mig.sies.model.DatiPubblicoMinistero.updateMisuraCautelare", dpm);
			logger.info("Update chiavi MC: NSC[" + dpm.getChiaveNSC() + "] SIES[" + dpm.getChiaveSies()
					+ "] Risultato[" + i + "]");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
	}

	public void deleteLogicaMisCautelare(
			it.mig.sies.type.foglicomplementari.ChiaviProvvedimentoEsecutivo chiaviProvvedimentoEsecutivo) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		try {
			session = getSession();
			DatiPubblicoMinistero dpm = new DatiPubblicoMinistero();
			dpm.setChiaveSies(chiaviProvvedimentoEsecutivo.getSies().longValue());
			int i = session.update("it.mig.sies.model.DatiPubblicoMinistero.deleteLogicaMisuraCautelare",
					dpm);
			logger.info("Update chiavi DPM: NSC[" + dpm.getChiaveNSC() + "] SIES[" + dpm.getChiaveSies()
					+ "] Risultato[" + i + "]");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
	}

	/**
	 * MEV 16 CUMULO: aggiunto metodo di controllo
	 *
	 * @param codMotivo
	 * @return
	 */
	public boolean isCumulo(String codMotivo) {

		boolean isCumulo = false;

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		try {
			session = getSession();
			int count = (Integer) session.selectOne("it.mig.sies.model.TitoloGiudiziario.isCumulo",
					codMotivo);
			isCumulo = count > 0 ? true : false;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
		return isCumulo;
	}

	/**
	 * MEV 16 CUMULO: metodo per il recupero della descrizione dell'autoritÃ 
	 *
	 * @param codiceAutorita
	 * @return String
	 */
	public String getDescAutorita(String codiceAutorita) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		String descAutorita = "";
		try {
			session = getSession();
			descAutorita = (String) session.selectOne("it.mig.sies.model.TitoloGiudiziario.getDescAutorita",
					codiceAutorita);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
		return descAutorita;
	}

	/**
	 * MEV 16 CUMULO: metodo per il recupero della descrizione della sede autoritÃ 
	 *
	 * @param codiceSedeAutorita
	 * @return String
	 */
	public String getDescSedeAutorita(String codiceSedeAutorita) {

		// Scheda Intervento nÂ° 6 - Ottimizzazione SIUS Avvocati
		SqlSession session = null;
		String descSedeAutorita = "";
		try {
			session = getSession();
			descSedeAutorita = (String) session
					.selectOne("it.mig.sies.model.TitoloGiudiziario.getDescSedeAutorita", codiceSedeAutorita);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} finally {
			// Chiusura connessione
			closeSession(session);
		}
		return descSedeAutorita;
	}

}