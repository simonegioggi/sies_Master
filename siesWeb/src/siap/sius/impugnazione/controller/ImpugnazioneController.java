package siap.sius.impugnazione.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sius.avvocatura.dao.AvvisiAvvocatoDAO;
import siap.sius.avvocatura.model.AvvisiAvvocatoModel;
import siap.sius.depositodecreto.dao.DepositoDecretoSqlDAO;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcSqlDAO;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositosentenza.dao.DepositoSentenzaSqlDAO;
import siap.sius.depositosentenza.model.DepositoSentenzaModel;
import siap.sius.impugnazione.dao.ImpugnazioneDAO;
import siap.sius.impugnazione.dao.ImpugnazioneSqlDAO;
import siap.sius.impugnazione.model.ImpugnazioneModel;
import siap.sius.scadenzario.dao.ScadenzarioSiusDAO;
import siap.sius.stampa.controller.IStampaSius;
import siap.sius.util.SIUSLookupRemote;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.Utils;

/**
 * <p>
 * Title: ImpugnazioneController
 * </p>
 * <p>
 * Description: Classe Controller per Impugnazione
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ImpugnazioneController extends SiapController implements IImpugnazione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public ImpugnazioneModel ExInserisciImpugnazione(ImpugnazioneModel aImpugnazione, BigDecimal aIdEvento,
			BigDecimal aIdFascicolo, String aTipo,
			// MEV_AVVOCATURA - aggiunto parametro
			Vector<AvvisiAvvocatoModel> lAvvvisiAvvocato) throws F3BException {
		Connection lConn = null;
		ImpugnazioneDAO lImpDao = null;
		ImpugnazioneSqlDAO lImpSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;
		EventoDAO lEveDao = null;
		ScadenzarioSiusDAO lScaDao = null;
		DepositoOrdinanzaPcSqlDAO lDOPDao = null;
		DepositoDecretoSqlDAO lDDDao = null;
		DepositoSentenzaSqlDAO lDSPDao = null;
		// MEV_AVVOCATURA - Aggiunta variabile
		AvvisiAvvocatoDAO lAvvisiAvvocatoDao = null;

		EventoModel lEveMod = null;
		// ScadenzarioSiusModel lScaMod = null;

		try {
			lConn = getDBTransaction();
			lImpDao = new ImpugnazioneDAO(lConn);
			lImpSqlDao = new ImpugnazioneSqlDAO(lConn);
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			lScaDao = new ScadenzarioSiusDAO(lConn);
			lDOPDao = new DepositoOrdinanzaPcSqlDAO(lConn);
			lDDDao = new DepositoDecretoSqlDAO(lConn);
			lDSPDao = new DepositoSentenzaSqlDAO(lConn);

			// Recupero dell'Evento legato al provvedimento che si impugna.
			lEveSqlDao.ricercaEventoByKey(aIdEvento);
			lEveMod = new EventoModel((EventoModel) lEveSqlDao.getModelByKey());

			// Recupero "del Deposito Ordinanza" o del "Deposito Decreto" 
			// o "Deposito Sentenza" a seconda del tipo di Provvedimento.
			if (lEveMod.getCodTipoProvvedimento().equals("02")) // Decreto
			{
				// Lettura del DepositoDecreto per IdEventoGenerato.
				DepositoDecretoModel lDDMod = new DepositoDecretoModel();
				lDDDao.ricercaDepositoDecretoByIdEveGenerato(aIdEvento);
				lDDMod = (DepositoDecretoModel) lDDDao.getModelByKey();

				// Valorizzazione di DEP_DEC_ID_DEPOSITO_DECRETO.
				aImpugnazione.setDepDecIdDepositoDecreto(lDDMod.getIdDepositoDecreto());
			} else if (lEveMod.getCodTipoProvvedimento().equals("03")) // Ordinanza
			{
				// Lettura del DepositoOrdinanzaPC per IdEventoGenerato.
				DepositoOrdinanzaPcModel lDOMod = new DepositoOrdinanzaPcModel();
				lDOPDao.ricercaDepositoOrdinanzaPcByIdEveGenerato(aIdEvento);
				lDOMod = (DepositoOrdinanzaPcModel) lDOPDao.getModelByKey();

				// Valorizzazione di DEP_OPID_DEPOSITO_ORDINANZA.
				aImpugnazione.setDepOpidDepositoOrdinanzaPc(lDOMod.getIdDepositoOrdinanzaPc());
			} else if (lEveMod.getCodTipoProvvedimento().equals("01")) // Sentenza
			{
				// Lettura del DepositoSentenza per IdEventoGenerato.
				DepositoSentenzaModel lDSMod = new DepositoSentenzaModel();
				lDSPDao.ricercaDepositoSentenzaByIdEveGenerato(aIdEvento);
				lDSMod = (DepositoSentenzaModel) lDSPDao.getModelByKey();

				// Valorizzazione di DEP_OPID_DEPOSITO_ORDINANZA.
				aImpugnazione.setDepIdDepositoSentenza(lDSMod.getIdDepositoSentenza());

			} else
				// Né Decreto Né Ordinanza Nè Sentenza.
				throw new F3BException(F3BException.USER_MESSAGE, "Tipo Provvedimento non riconosciuto");

			// Calcolo del Progressivo PROGR_S7.
			lImpSqlDao.getProgressivoImpugnazione(aImpugnazione);
			lImpSqlDao.start();

			BigDecimal lBigDec = new BigDecimal(0);
			if (lImpSqlDao.next() && (lImpSqlDao.getBigDecimal("aMAX") != null))
				lBigDec = lImpSqlDao.getBigDecimal("aMAX");
			lImpSqlDao.stop();

			if (lBigDec == null)
				lBigDec = new BigDecimal(0);

			// Setto il PROGR_S7 del Model di Impugnazione con il MAX + 1
			aImpugnazione.setProgrS7(new BigDecimal(lBigDec.intValue() + 1));

			lImpDao.setDAOFromModel(aImpugnazione);
			BigDecimal lKeyImp = null;
			lKeyImp = lImpDao.insert();
			aImpugnazione.setIdImpugnazione(lKeyImp);

			// Si aggiorna l'Evento nel campo FLAG_PIU_MENO.
			// STUB 12/09/2003 In futuro il Campo FLAG_PIU_MENO sarà sostituito da COD_STATO_EVENTO.
			lEveDao.setFlagPiuMeno("R");
			lEveDao.selCondizioneUpdate(lEveMod.getIdEvento());
			lEveDao.update();

			// Infine bisogna cancellare il record dello scadenzario.
			// /lScaDao = new ScadenzarioSiusDAO(lConn);
			// /lScaDao.setCondizioniByIdFascicoloTipo(aIdFascicolo, aTipo);
			// /lScaDao.delete();

			// ******************************************************************************
			// MEV_AVVOCATURA - INIZIO - inserisco gli avvisi sulla tabella AVVISI_AVVOCATO
			// ******************************************************************************
			if (lAvvvisiAvvocato != null && lAvvvisiAvvocato.size() > 0) {
				lAvvisiAvvocatoDao = new AvvisiAvvocatoDAO(lConn);
				for (AvvisiAvvocatoModel avvisoAvvocato : lAvvvisiAvvocato) {
					// // setto id provvedimento
					// if(aImpugnazione.getDepDecIdDepositoDecreto() != null){
					// // Decreto
					// lAvvisiAvvocatoDao.setIdProvvedimento(aImpugnazione.getDepDecIdDepositoDecreto());
					// }
					// if(aImpugnazione.getDepOpidDepositoOrdinanzaPc() != null){
					// // Ordinanza
					// lAvvisiAvvocatoDao.setIdProvvedimento(aImpugnazione.getDepOpidDepositoOrdinanzaPc());
					// }
					// if(aImpugnazione.getDepIdDepositoSentenza() != null){
					// // Sentenza
					// lAvvisiAvvocatoDao.setIdProvvedimento(aImpugnazione.getDepIdDepositoSentenza());
					// }

					// setto id evento (modifica emma 22/08/2016)
					if (lEveMod.getIdEvento() != null) {
						// Decreto
						lAvvisiAvvocatoDao.setIdEvento(lEveMod.getIdEvento());
					}

					lAvvisiAvvocatoDao.setDAOFromModel(avvisoAvvocato);
					lAvvisiAvvocatoDao.insert();
					lAvvisiAvvocatoDao.stop();
				}
			}
			// ************************
			// MEV_AVVOCATURA - FINE
			// ************************

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("ImpugnazioneController.ExInserisciImpugnazione: " + ex);
		} finally {
			cleanup(lImpDao);
			cleanup(lEveSqlDao);
			cleanup(lEveDao);
			cleanup(lImpSqlDao);
			cleanup(lScaDao);
			cleanup(lDOPDao);
			cleanup(lDDDao);
			cleanup(lAvvisiAvvocatoDao);
			cleanup(lConn);
		}
		return aImpugnazione;
	}

	public Vector ExRicercaImpugnazione(ImpugnazioneModel aImpugnazione) throws F3BException {
		Connection lConn = null;
		Vector lImpugnazioni = new Vector();
		ImpugnazioneSqlDAO lImpDao = null;

		try {
			lConn = getDBConnection();
			lImpDao = new ImpugnazioneSqlDAO(lConn);
			lImpDao.ricercaImpugnazioneByKey(aImpugnazione.getIdImpugnazione());
			lImpugnazioni = new Vector(lImpDao.getModels());
			if (lImpugnazioni.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ImpugnazioneController.ExRicercaImpugnazione: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lImpDao);
			cleanup(lConn);
		}
		return lImpugnazioni;
	}

	/**
	 * La funzione ricerca le impugnazioni annullate per uno specifico Provvedimento.
	 * 
	 * @param aIdProv
	 *            : ID del Decreto o dell'Ordinanza
	 * @param aTipoProv
	 *            : tipo di provvedimento: '02' Ordinanza , '03' Decreto.
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaImpugnazioniAnnullateByProv(BigDecimal aIdProv, String aTipoProv)
			throws F3BException {
		Connection lConn = null;
		Vector lImpugnazioni = null;
		ImpugnazioneSqlDAO lImpDao = null;

		try {
			lConn = getDBConnection();
			lImpDao = new ImpugnazioneSqlDAO(lConn);

			lImpDao.ricercaImpugnazioniAnnullateByProv(aIdProv, aTipoProv);

			lImpugnazioni = new Vector(lImpDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException("ImpugnazioneController.ExRicercaImpugnazioneAnnullatiByProv: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("" + e);
		}

		finally {
			cleanup(lImpDao);
			cleanup(lConn);
		}
		return lImpugnazioni;
	}

	public ImpugnazioneModel ExRicercaImpugnazioneByKey(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		ImpugnazioneSqlDAO lImpDao = null;
		ImpugnazioneModel lImpMod;

		try {
			lConn = getDBConnection();
			lImpDao = new ImpugnazioneSqlDAO(lConn);
			lImpDao.ricercaImpugnazioneByKey(aKey);
			lImpMod = (ImpugnazioneModel) lImpDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ImpugnazioneController.ExRicercaImpugnazione: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lImpDao);
			cleanup(lConn);
		}
		return lImpMod;
	}

	public ImpugnazioneModel ExModificaImpugnazione(ImpugnazioneModel aImpugnazione) throws F3BException {
		Connection lConn = null;
		ImpugnazioneDAO lImpDao = null;
		ImpugnazioneModel lImpMod = new ImpugnazioneModel(aImpugnazione);

		try {
			lConn = getDBConnection();

			lImpDao = new ImpugnazioneDAO(lConn);

			// Impostazione dei soli campi modificabili dalla form.
			lImpDao.setCodTipoImpugnazione(aImpugnazione.getCodTipoImpugnazione());
			lImpDao.setSoggettoImpugnante(aImpugnazione.getSoggettoImpugnante());
			lImpDao.setDescrizioneAltro(aImpugnazione.getDescrizioneAltro());
			lImpDao.setDataRicorso(aImpugnazione.getDataRicorso());
			lImpDao.setAnnotazione(aImpugnazione.getAnnotazione());
			lImpDao.setDataArrivoCancelleria(aImpugnazione.getDataArrivoCancelleria());
			lImpDao.setDataTrasmissioneAtti(aImpugnazione.getDataTrasmissioneAtti());
			lImpDao.setCodAutoritaDestinataria(aImpugnazione.getCodAutoritaDestinataria());
			lImpDao.setDataDecisione(aImpugnazione.getDataDecisione());
			lImpDao.setCodTenoreDecisione(aImpugnazione.getCodTenoreDecisione());
			lImpDao.setDataRestituzioneAtti(aImpugnazione.getDataRestituzioneAtti());
			lImpDao.setFlagSospEsec(aImpugnazione.getFlagSospEsec());

			lImpDao.setCodOperatoreAggiornamento(aImpugnazione.getCodOperatoreAggiornamento());
			lImpDao.setCodUfficioAggiornamento(aImpugnazione.getCodUfficioAggiornamento());
			lImpDao.setDataAggiornamento(aImpugnazione.getDataAggiornamento());

			lImpDao.setCondizioneUpdate(aImpugnazione.getIdImpugnazione());

			lImpDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: ", ex);
			throw new F3BException("ImpugnazioneController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lConn);
			cleanup(lImpDao);
		}
		return lImpMod;
	}

	public void ExCancellaImpugnazione(ImpugnazioneModel aImpugnazione) throws F3BException {
		Connection lConn = null;
		ImpugnazioneDAO lImpDao = null;

		try {
			lConn = getDBConnection();
			lImpDao = new ImpugnazioneDAO(lConn);
			lImpDao.setCondizioneUpdate(aImpugnazione.getIdImpugnazione());
			lImpDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ImpugnazioneController.ExCancellaImpugnazione: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lImpDao);
			cleanup(lConn);
		}
	}

	/**
	 * Funzione di Annullamento di una Impugnazione o Opposizione
	 * <p>
	 * La funzione effettua l'update di un record nella tabella IMPUGNAZIONE NOn aggiorna più la tabella
	 * EVENTO.FLAG_PIU_MENO.
	 * 
	 * @param aImpugnazione
	 * @param aIdEvento
	 * @throws F3BException
	 */

	public void ExAnnullaImpugnazione(ImpugnazioneModel aImpugnazione, BigDecimal aIdEvento)
			throws F3BException {
		Connection lConn = null;
		ImpugnazioneDAO lImpDao = null;
		EventoDAO lEveDao = null;
		try {
			lConn = getDBConnection();
			// Update IMPUGNAZIONE
			lImpDao = new ImpugnazioneDAO(lConn);

			lImpDao.setFlagAnnullamento(aImpugnazione.getFlagAnnullamento());
			lImpDao.setDataAnnullamento(aImpugnazione.getDataAnnullamento());
			lImpDao.setMotivoAnnullamento(aImpugnazione.getMotivoAnnullamento());

			lImpDao.setCodOperatoreAggiornamento(aImpugnazione.getCodOperatoreAggiornamento());
			lImpDao.setCodUfficioAggiornamento(aImpugnazione.getCodUfficioAggiornamento());
			lImpDao.setDataAggiornamento(aImpugnazione.getDataAggiornamento());

			lImpDao.setCondizioneUpdate(aImpugnazione.getIdImpugnazione());

			lImpDao.update();
			lImpDao.stop();

			/*
			 * 16-11-2007 inibizione Update EVENTO // Update EVENTO lEveDao = new EventoDAO(lConn);
			 * lEveDao.setCodOperatoreAggiornamento(aImpugnazione.getCodOperatoreAggiornamento());
			 * lEveDao.setCodUfficioAggiornamento(aImpugnazione.getCodUfficioAggiornamento());
			 * lEveDao.setDataAggiornamento(aImpugnazione.getDataAggiornamento()); lEveDao.setFlagPiuMeno("");
			 * lEveDao.selCondizioneUpdate(aIdEvento); lEveDao.update(); lEveDao.stop();
			 */
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("ImpugnazioneController.ExAnnullaImpugnazione: Non posso leggere : "
					+ daoEx);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("" + e);
		}

		finally {
			cleanup(lImpDao);
			cleanup(lEveDao);
			cleanup(lConn);
		}
	}

	/**
	 * 
	 * @param aIdEvento
	 * @param aTipo
	 * @param aTipoImpugnazione
	 * @param aFlagAnnullate
	 * @return
	 * @throws F3BException
	 */
	public ImpugnazioneModel ExRicercaImpugnazioneByIdEventoTipoProvv(BigDecimal aIdEvento, String aTipo,
			String[] aTipoImpugnazione, String aFlagAnnullate) throws F3BException {
		Connection lConn = null;
		ImpugnazioneSqlDAO lImpDao = null;
		ImpugnazioneModel lImpMod;

		try {
			lConn = getDBConnection();
			lImpDao = new ImpugnazioneSqlDAO(lConn);
			lImpDao.ricercaImpugnazioneByIdEventoTipoProvv(aIdEvento, aTipo, aTipoImpugnazione,
					aFlagAnnullate);
			lImpMod = (ImpugnazioneModel) lImpDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ImpugnazioneController.ExRicercaImpugnazioneByIdEventoTipoProvv : "
					+ daoEx);
		} finally {
			cleanup(lImpDao);
			cleanup(lConn);
		}
		return lImpMod;
	}

	/**
	 * Verifica se per il procedimento individuato e' attualmente impugnato. STUB 12/09/2003 Al momento il test
	 * e' realizzato controllando il Campo FLAG_PIU_MENO, ma in futuro sarà gestito COD_STATO_EVENTO
	 * 
	 * @param aFascKey
	 * @return aResponse
	 * @throws F3BException
	 */
	public boolean ExVerificaImpugnazione(BigDecimal aFascKey, String aTipoEvento) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.error("ImpugnazioneController.ExVerificaImpugnazione aFascKey + aTipoEvento = " + aFascKey
				+ " " + aTipoEvento);
		boolean aResponse = false;
		Connection lConn = null;
		EventoSqlDAO lEveDao = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);

			lEveDao.ricercaEventoByFascicoloSius(aFascKey, aTipoEvento);
			lEveDao.start();
			while (lEveDao.next()) {
				if (!Utils.isNullObj(((EventoModel) lEveDao.getModel()).getFlagPiuMeno())
						&& ((EventoModel) lEveDao.getModel()).getFlagPiuMeno().compareTo("R") == 0) {
					aResponse = true;
					break;
				}
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ImpugnazioneController.ExVerificaImpugnazione: " + daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return aResponse;
	}

	/**
	 * Esecuzione stampa dell'Impugnazione
	 * <p>
	 * 
	 * @param aKeyImp
	 * @param aUfficioUtenteConnesso
	 * @return ByteArrayOutputStream
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaImpugnazione(BigDecimal aImpKey, BigDecimal aEveKey,
			BigDecimal aFasKey, String aIdTemplate, String aUfficioUtenteConnesso, UtenteModel aUtenteModel)
			throws F3BException {

		// Connection lConn = null;
		ByteArrayOutputStream lByteArrayOut = null;

		// Generazione documento di stampa
		IStampaSius lCtrlSt = SIUSLookupRemote.getStampaRemote();
		lByteArrayOut = lCtrlSt.ExPreStampaImpugnazione(aImpKey, aEveKey, aFasKey, aIdTemplate,
				aUfficioUtenteConnesso, aUtenteModel);

		// ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());
		return lByteArrayOut;
	}

	public String ExRicercaDataRicorso(BigDecimal aIdEvento, String aTipo) throws F3BException {
		Connection lConn = null;
		ImpugnazioneSqlDAO lImpDao = null;
		String retval = " ";
		try {
			lConn = getDBConnection();
			lImpDao = new ImpugnazioneSqlDAO(lConn);
			retval = lImpDao.getDataRicorso(aIdEvento, aTipo);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ImpugnazioneController.ExRicercaDataRicorso : " + daoEx);
		} finally {
			cleanup(lImpDao);
			cleanup(lConn);
		}
		return retval;
	}

	public String ExRicercaDateRicorsi(BigDecimal aIdEvento, String aTipo) throws F3BException {
		Connection lConn = null;
		ImpugnazioneSqlDAO lImpDao = null;

		Vector lRicorsi = new Vector();
		String lDateRicorsi = "";
		try {
			lConn = getDBConnection();
			lImpDao = new ImpugnazioneSqlDAO(lConn);

			Collection lColl = lImpDao.getDateRicorsi(aIdEvento, aTipo);
			if (lColl != null)
				lRicorsi = new Vector(lImpDao.getDateRicorsi(aIdEvento, aTipo));

			for (int i = 0; i < lRicorsi.size(); i++)
				lDateRicorsi = lRicorsi.toString() + "  ";

			// lDateRicorsi = new Vector(lImpDao.getDateRicorsi(aIdEvento, aTipo));

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ImpugnazioneController.ExRicercaDateRicorsi : " + daoEx);
		} finally {
			cleanup(lImpDao);
			cleanup(lConn);
		}
		return lDateRicorsi;
	}

	/**
	 * Conteggio del N.ro delle impugnazioni afferenti ad un particolare provvedimento.
	 * 
	 * @param aFascKey
	 * @param aTipoEvento
	 *            (02 - Decreto, 03 - Ordinanza)
	 * @param aTipoImpugnazione
	 *            - String[] con i codici da ricercare
	 * @param aFlagAnnullate
	 *            - S, N, Null
	 * @return Vector <ImpugnazioneModel>
	 * @throws F3BException
	 */
	public Vector<ImpugnazioneModel> ExRicercaImpugnazioni(BigDecimal aFascKey, String aTipoEvento,
			String[] aTipoImpugnazione, String aFlagAnnullate) throws F3BException {

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		// siesLogger.error("ImpugnazioneController.ExRicercaImpugnazioni aFascKey + aTipoEvento = "+aFascKey+" "+aTipoEvento);
		Connection lConn = null;
		Vector lVect = new Vector();
		EventoSqlDAO lEveDao = null;
		ImpugnazioneSqlDAO lImpDao = null;
		try {
			lConn = getDBConnection();

			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaEventoByFascicoloSius(aFascKey, aTipoEvento);
			Vector Eventi = new Vector(lEveDao.getModels());

			Iterator itx = Eventi.iterator();
			while (itx.hasNext()) {
				EventoModel lEveMod = (EventoModel) itx.next();
				if (!Utils.isNullObj(lEveMod.getIdEvento()) && !Utils.isNullObj(lEveMod.getFlagPiuMeno())
						&& lEveMod.getFlagPiuMeno().compareTo("R") == 0) {
					lImpDao = new ImpugnazioneSqlDAO(lConn);
					// 16/11/2007 Cambio metodo di ricerca Impugnazioni (si includono anche le annullate)
					// lImpDao.ricercaImpugnazioneByIdEventoTipoProvv(lEveMod.getIdEvento(),
					// lEveMod.getCodTipoProvvedimento()) ;
					// FIXME Errore va passato idDeposito ordinanza o idDeposito decreto e non idEvento
					// lImpDao.ricercaImpugnazioniDelProvvedimento(lEveMod.getIdEvento(),
					// lEveMod.getCodTipoProvvedimento()) ;

					lImpDao.ricercaImpugnazioneByIdEventoTipoProvv(lEveMod.getIdEvento(),
							lEveMod.getCodTipoProvvedimento(), aTipoImpugnazione, null);
					lVect = new Vector(lImpDao.getModels());
					break;
				}
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ImpugnazioneController.ExRicercaImpugnazioni: " + daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lImpDao);
			cleanup(lConn);
		}
		return lVect;
	}

	// 06/11/2007 Nuova ricerca elenco impugnazioni del provvedimento
	/**
	 * La funzione ricerca le impugnazioni per uno specifico Provvedimento.
	 * 
	 * @param aIdProv
	 *            : ID del Decreto o dell'Ordinanza
	 * @param aTipoProv
	 *            : tipo di provvedimento: '02' Ordinanza , '03' Decreto.
	 * @param aTipoImpugnazione
	 *            - eventuali codici tipo_ricorso da filtrare
	 * @param aFlagAnnullate
	 *            - eventuale filtro su annullamento S,N, null
	 * @return
	 * @throws F3BException
	 */
	public Vector<ImpugnazioneModel> ExRicercaImpugnazioniDelProvvedimento(BigDecimal aIdProv,
			String aTipoProv, String[] aTipoImpugnazione, String aFlagAnnullate) throws F3BException {
		Connection lConn = null;
		Vector<ImpugnazioneModel> lImpugnazioni = null;
		ImpugnazioneSqlDAO lImpDao = null;

		try {
			lConn = getDBConnection();
			lImpDao = new ImpugnazioneSqlDAO(lConn);

			lImpDao.ricercaImpugnazioniDelProvvedimento(aIdProv, aTipoProv, aTipoImpugnazione, aFlagAnnullate);

			lImpugnazioni = new Vector<ImpugnazioneModel>(lImpDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException", daoEx);
			throw new F3BException("ImpugnazioneController.ExRicercaImpugnazioniDelProvvedimento: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("" + e);
		}

		finally {
			cleanup(lImpDao);
			cleanup(lConn);
		}
		return lImpugnazioni;
	}

	/**
	 * 
	 * @param aIdEvento
	 * @param aTipo
	 * @param aTipoImpugnazione
	 * @param aFlagAnnullate
	 * @return
	 * @throws F3BException
	 */
	public Vector<ImpugnazioneModel> ExRicercaImpugnazioniByIdEventoTipoProvvTipoImpFlagAnn(
			BigDecimal aIdEvento, String aTipo, String[] aTipoImpugnazione, String aFlagAnnullate)
			throws F3BException {
		Connection lConn = null;
		ImpugnazioneSqlDAO lImpSqlDao = null;
		Vector<ImpugnazioneModel> lImpModVect = null;

		try {
			lConn = getDBConnection();
			lImpSqlDao = new ImpugnazioneSqlDAO(lConn);
			lImpSqlDao.ricercaImpugnazioneByIdEventoTipoProvv(aIdEvento, aTipo, aTipoImpugnazione,
					aFlagAnnullate);
			lImpModVect = new Vector<ImpugnazioneModel>(lImpSqlDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("", daoEx);
			throw new F3BException(
					"ImpugnazioneController.ExRicercaImpugnazioniByIdEventoTipoProvvTipoImpFlagAnn : "
							+ daoEx);
		} finally {
			cleanup(lImpSqlDao);
			cleanup(lConn);
		}

		return lImpModVect;
	}

	/**
	 * 
	 * @param aImpugnazione
	 * @param aIdEvento
	 * @param aIdFascicolo
	 * @return
	 * @throws F3BException
	 */
	public ImpugnazioneModel ExInserisciOpposizione(ImpugnazioneModel aImpugnazione, BigDecimal aIdEvento,
			Vector<AvvisiAvvocatoModel> lAvvvisiAvvocato) throws F3BException {
		Connection lConn = null;

		ImpugnazioneDAO lImpDao = null;
		ImpugnazioneSqlDAO lImpSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;
		EventoDAO lEveDao = null;

		DepositoOrdinanzaPcSqlDAO lDOPDao = null;
		DepositoDecretoSqlDAO lDDDao = null;
		// MEV_AVVOCATURA - aggiunta variabile
		AvvisiAvvocatoDAO lAvvisiAvvocatoDao = null;

		EventoModel lEveMod = null;

		try {
			lConn = getDBTransaction();

			lImpDao = new ImpugnazioneDAO(lConn);
			lImpSqlDao = new ImpugnazioneSqlDAO(lConn);
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			lDOPDao = new DepositoOrdinanzaPcSqlDAO(lConn);
			lDDDao = new DepositoDecretoSqlDAO(lConn);

			// Recupero dell'Evento legato al provvedimento che si impugna.
			lEveSqlDao.ricercaEventoByKey(aIdEvento);
			lEveMod = (EventoModel) lEveSqlDao.getModelByKey();

			if (lEveMod == null)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Provvedimento non recuperato al momento dell'iscrizione dell'Opposizione");

			// Recupero "del Deposito Ordinanza" o del "Deposito Decreto" a seconda del tipo di Provvedimento.
			if (lEveMod.getCodTipoProvvedimento().equals("02")) // Decreto
			{
				// Lettura del DepositoDecreto per IdEventoGenerato.
				DepositoDecretoModel lDDMod = new DepositoDecretoModel();
				lDDDao.ricercaDepositoDecretoByIdEveGenerato(aIdEvento);
				lDDMod = (DepositoDecretoModel) lDDDao.getModelByKey();

				// Valorizzazione di DEP_DEC_ID_DEPOSITO_DECRETO.
				aImpugnazione.setDepDecIdDepositoDecreto(lDDMod.getIdDepositoDecreto());
			} else if (lEveMod.getCodTipoProvvedimento().equals("03")) // Ordinanza
			{
				// Lettura del DepositoOrdinanzaPC per IdEventoGenerato.
				DepositoOrdinanzaPcModel lDOMod = new DepositoOrdinanzaPcModel();
				lDOPDao.ricercaDepositoOrdinanzaPcByIdEveGenerato(aIdEvento);
				lDOMod = (DepositoOrdinanzaPcModel) lDOPDao.getModelByKey();

				// Valorizzazione di DEP_OPID_DEPOSITO_ORDINANZA.
				aImpugnazione.setDepOpidDepositoOrdinanzaPc(lDOMod.getIdDepositoOrdinanzaPc());
			}
			// MEV10-s3: aggiunta gestione sentenza ("01")
			else if (lEveMod.getCodTipoProvvedimento().equals("01")) // Sentenza
			{
				// Lettura del DepositoSentenzaPC per IdEventoGenerato.
				// DepositoSentenzaModel lDSMod = new DepositoSentenzaModel();
				DepositoSentenzaSqlDAO lDepSentDao = new DepositoSentenzaSqlDAO(lConn);
				lDepSentDao.ricercaDepositoSentenzaByIdEveGenerato(aIdEvento);
				DepositoSentenzaModel lDepSenMod = (DepositoSentenzaModel) lDepSentDao.getModelByKey();
				// Valorizzazione di DEP_ID_DEPOSITO_SENTENZA.
				aImpugnazione.setDepIdDepositoSentenza(lDepSenMod.getIdDepositoSentenza());
				cleanup(lDepSentDao);
			} else
				// Né Decreto Né Ordinanza Nè Sentenza.
				throw new F3BException(F3BException.USER_MESSAGE, "Tipo Provvedimento non riconosciuto");

			//
			// Calcolo del Progressivo PROGR_S7.
			// n.b. Stesso registro Impugnazioni
			lImpSqlDao.getProgressivoImpugnazione(aImpugnazione);
			lImpSqlDao.start();

			BigDecimal lBigDec = new BigDecimal(0);
			if (lImpSqlDao.next() && (lImpSqlDao.getBigDecimal("aMAX") != null))
				lBigDec = lImpSqlDao.getBigDecimal("aMAX");
			lImpSqlDao.stop();

			if (lBigDec == null)
				lBigDec = new BigDecimal(0);

			// Setto il PROGR_S7 del Model di Impugnazione con il MAX + 1
			aImpugnazione.setProgrS7(new BigDecimal(lBigDec.intValue() + 1));

			// ===========================
			// Inserisco l'OPPOSIZIONE
			// ===========================
			lImpDao.setDAOFromModel(aImpugnazione);
			BigDecimal lKeyImp = lImpDao.insert();
			aImpugnazione.setIdImpugnazione(lKeyImp);

			// ******************************************************************************
			// MEV_AVVOCATURA - INIZIO - inserisco gli avvisi sulla tabella AVVISI_AVVOCATO
			// ******************************************************************************
			if (lAvvvisiAvvocato != null && lAvvvisiAvvocato.size() > 0) {
				lAvvisiAvvocatoDao = new AvvisiAvvocatoDAO(lConn);
				for (AvvisiAvvocatoModel avvisoAvvocato : lAvvvisiAvvocato) {
					// // setto id provvedimento
					// if(aImpugnazione.getDepDecIdDepositoDecreto() != null){
					// // Decreto
					// lAvvisiAvvocatoDao.setIdProvvedimento(aImpugnazione.getDepDecIdDepositoDecreto());
					// }
					// if(aImpugnazione.getDepOpidDepositoOrdinanzaPc() != null){
					// // Ordinanza
					// lAvvisiAvvocatoDao.setIdProvvedimento(aImpugnazione.getDepOpidDepositoOrdinanzaPc());
					// }
					// if(aImpugnazione.getDepIdDepositoSentenza() != null){
					// // Sentenza
					// lAvvisiAvvocatoDao.setIdProvvedimento(aImpugnazione.getDepIdDepositoSentenza());
					// }

					// setto id evento (modifica emma 22/08/2016)
					if (aIdEvento != null) {
						// Decreto
						lAvvisiAvvocatoDao.setIdEvento(lEveMod.getIdEvento());
					}

					lAvvisiAvvocatoDao.setDAOFromModel(avvisoAvvocato);
					lAvvisiAvvocatoDao.insert();
					lAvvisiAvvocatoDao.stop();
				}
			}
			// ************************
			// MEV_AVVOCATURA - FINE
			// ************************

			commit(lConn);
		} catch (DAOException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", ex);
			rollback(lConn);
			throw new F3BException("ImpugnazioneController.ExInserisciOpposizione: " + ex);
		} finally {
			cleanup(lImpDao);
			cleanup(lEveSqlDao);
			cleanup(lEveDao);
			cleanup(lImpSqlDao);
			cleanup(lDOPDao);
			cleanup(lDDDao);
			cleanup(lAvvisiAvvocatoDao);

			cleanup(lConn);
		}

		return aImpugnazione;
	}

}