package siap.sius.richiestaatti.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.controller.TemplateManager;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.dao.NotificaDAO;
import siap.sige.provvedimento.dao.ProvvedimentoSigeDAO;
import siap.sius.SIUSException;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.dao.DocumentoAllegatoDAO;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.motivazionedecreto.dao.MotivazioneDecretoDAO;
import siap.sius.stampa.controller.IStampaSius;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: RichiestaAttiController
 * </p>
 * <p>
 * Description: Classe controller di gestione stampa delle Rihieste atti istruttori
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class RichiestaAttiController extends SiapController implements IRichiestaAtti {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Esecuzione stampa Solleciti
	 * <p>
	 *
	 * @param aModel
	 * @return ByteArrayOutputStream
	 * @throws F3BException
	 */
	public DocumentoAllegatoModel ExInserisciSollecito(EventoNotificaModel aEveNot,
			DocumentoAllegatoModel aDocAllegato, UfficioModel lUfficio, UtenteModel lUtenteMod)
			throws F3BException {

		ByteArrayOutputStream lByteArrayOut = null;

		// Preleva i dati per la generazione.
		// TreeModel lTree = this.prelevaDati( aEveNot.getEvento() , lUfficio ,lUtenteMod);

		// Invoca il Report generator.
		// ReportGenerator lReport = new ReportGenerator();

		// Preleva dalla tabella Template il nome del template RTF.
		IEvento lEventoCtrl = SICOLookupRemote.getEventoRemote();
		TemplateModel lTemplateMod = lEventoCtrl.ExRicercaTemplateByCodMotivo("0518");

		String lNomeTemplate = lTemplateMod.getPathRicerca() + lTemplateMod.getNomeTemplate();

		// lByteArrayOut = (ByteArrayOutputStream)lReport.generateDocument(lTree,lNomeTemplate);

		///////////////////////////////// Luigi 20-9-2005
		aEveNot.getEvento().setTemIdTemplate(lNomeTemplate);
		// Generazione documento di stampa
		IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();
		lByteArrayOut = lCtrlSta.ExPreStampaRichiestaAtti(aEveNot.getEvento(), lUfficio.getCodUfficio(),
				lUtenteMod);
		/////////////////////////////////

		ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

		// Si imposta il ByteArrayInput ovverro il doc generato nel docallegato
		// precisamente nel attributo DocBlobIn.
		aDocAllegato.setDocBlobIn(lByteArrayInput);

		// Inserisce il documento generato nel model di ritorno
		aDocAllegato.setDocBlobOut(lByteArrayOut);

		// Inserisce il documento allegato
		IDocumentoAllegato lDocCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
		DocumentoAllegatoModel aDocAllegatoRet = lDocCtrl.ExInserisciDocumentoAllegato(aDocAllegato);
		return aDocAllegatoRet;
	}

	/**
	 * Metodo di wrapper verso metodo di generaione stampa del controller di stampa.
	 * <p>
	 *
	 * @param aEvento
	 *            EventoModel contente tutti i dati necessari per la stampa.
	 * @param lUfficio
	 *            UfficioModel contente i dati dell'ufficio.
	 * @param lUtenteMod
	 *            UtenteModel contente i dati dell'utente connesso.
	 * @return L'EventoNotificaModel contente oltre i dati Evento e Notifica anche il Blob
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public EventoNotificaModel ExStampaRichiestaAtti(EventoModel aEvento, UfficioModel lUfficio,
			UtenteModel lUtenteMod) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .ExStampaRichiestaAtti: inizio ");

		ByteArrayOutputStream lByteArrayOut = null;
		EventoNotificaModel lEveNotifica = null;

		// Preleva i dati per la generazione.
		// TreeModel lTree = this.prelevaDati( aEvento, lUfficio ,lUtenteMod);

		// Invoca il Report generator.
		// ReportGenerator lReport = new ReportGenerator();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("######  Valore dell'Template in evento : " + aEvento.getTemIdTemplate());

		// Se l'id del template non è stato valorizzato, significa che la ricerca avviene attraverso
		// il codice Motivo.
		if (aEvento.getTemIdTemplate() == null) {
			IEvento lEventoCtrl = SICOLookupRemote.getEventoRemote();
			TemplateModel lTemplateMod = lEventoCtrl.ExRicercaTemplateByCodMotivo(aEvento.getCodMotivo());
			// Imposta l'id del template nell'evento
			aEvento.setTemIdTemplate(lTemplateMod.getIdTemplate());
		}

		// Recupera dalla TemplateManger il path completo del template.
		// Nota : Per conformità, anche se l'informazione desiderata è già disponibile
		// nel TemplateModel, si richiede al TemplateManager il path completo e il nome
		// del template RTF. Questa operazione risulta ridondante ma ci si adegua alla
		// filosofia del TemplateManger.
		String lNomeTemplate = TemplateManager.getInstance().getTemplateName(aEvento.getTemIdTemplate());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("#### NOME TEMPLATE >>>" + lNomeTemplate);

		///////////////////////////////// Luigi 20-9-2005
		aEvento.setTemIdTemplate(lNomeTemplate);

		// Generazione documento di stampa
		IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();
		lByteArrayOut = lCtrlSta.ExPreStampaRichiestaAtti(aEvento, lUfficio.getCodUfficio(), lUtenteMod);
		/////////////////////////////////

		ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(">>>>>> Generato il Documento .");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>> EVENTO : " + aEvento.toString());

		// Si imposta il ByteArrayInput ovverro il doc generato nell'evento
		// precisamente nel attributo DocBlobIn.
		aEvento.setDocBlobIn(lByteArrayInput);

		// Inserisce il documento generato nel model di ritorno
		// In esso inserisce il Nome del template di ritorno
		// e il documento generato.
		lEveNotifica = new EventoNotificaModel();
		// Non viene utilizzato
		// lEveNotifica.setNomeTemplate( lTemplateMod.getNomeTemplate() );
		lEveNotifica.setEvento(aEvento);
		lEveNotifica.getEvento().setDocBlobOut(lByteArrayOut);

		Connection lConn = null;
		EventoDAO lEveDao = null;

		try {
			// Preleva connessione dal Db
			lConn = getDBConnection();

			// Prepara un EventoDAO
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdateBlob(aEvento);

			// Seleziona le condizioni di Update
			lEveDao.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDao.update();

			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoex);
			throw new F3BException(
					"RichiestaAttiController.ExStampaRichiestaAtti: Non posso inserire il documento nell'evento : "
							+ daoex);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("" + getClass().getName() + " .ExStampaRichiestaAtti: fine ");
		}

		return lEveNotifica;
	}

	/**
	 * Esegue la cancellazione ddei dati inerenti una Richiesta Atti, che sono:.
	 * <p>
	 * Notifica, Campo Note, Documento Allegato (Sollecito), Evento.
	 *
	 * @param aIdEvento:
	 *            Chiave Identificativa dell'Evento.
	 * @throws Exception
	 */

	public void ExCancellaRichiestaAtti(BigDecimal aIdEvento) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .ExCancellaRichiestaAtti: inizio ");

		NotificaDAO lNotDAO = null;
		CampoNotaDAO lCampoNotaDao = null;
		DocumentoAllegatoDAO lDocAllDAO = null;
		EventoDAO lEveDao = null;
		MotivazioneDecretoDAO lMotivDecDao = null;
		ProvvedimentoSigeDAO provDao = null;
		Connection lConn = null;

		try {
			lConn = getDBConnection();

			// cancellazione Notifiche collegate
			lNotDAO = new NotificaDAO(lConn);
			lNotDAO.setCondizioneEvento(aIdEvento);
			lNotDAO.delete();

			// cancellazione Campi Note collegati
			lCampoNotaDao = new CampoNotaDAO(lConn);
			lCampoNotaDao.setCondizioneEvento(aIdEvento);
			lCampoNotaDao.delete();

			// cancellazione Motivazioni Decreti collegati (motivazioni Parere Inammissibilità)
			lMotivDecDao = new MotivazioneDecretoDAO(lConn);
			lMotivDecDao.setCondizioneByEve(aIdEvento);
			lMotivDecDao.delete();

			// cancellazione Documenti Allegati collegati
			lDocAllDAO = new DocumentoAllegatoDAO(lConn);
			lDocAllDAO.setCondizioneByEve(aIdEvento);
			lDocAllDAO.delete();

			provDao = new ProvvedimentoSigeDAO(lConn);
			provDao.selCondizioneByIdEvento(aIdEvento);
			provDao.delete();

			// cancellazione Evento collegato
			lEveDao = new EventoDAO(lConn);
			lEveDao.selCondizioneUpdate(aIdEvento);
			lEveDao.delete();

			commit(lConn);
		} catch (Exception e) {
			rollback(lConn);
			throw new SIUSException("RichiestaAttiController.ExCancellaRichiestaAtti: " + e);
		} finally {
			cleanup(lNotDAO);
			cleanup(lCampoNotaDao);
			cleanup(lDocAllDAO);
			cleanup(lEveDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lMotivDecDao);
			cleanup(provDao);
			cleanup(lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("" + getClass().getName() + " .ExCancellaRichiestaAtti: fine ");
		}
		return;
	}

}