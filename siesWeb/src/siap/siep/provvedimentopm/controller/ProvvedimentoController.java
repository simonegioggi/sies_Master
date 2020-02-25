package siap.siep.provvedimentopm.controller;

/**
 * <p>Title: ProvvedimentoController</p>
 * <p>Description: Classe Controller per Provvedimento</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Vector;

import org.w3c.dom.Document;

import siap.controller.SiapController;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoFascicoloSiepModel;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.provvedimentopm.dao.ProvvedimentoDAO;
import siap.siep.provvedimentopm.dao.ProvvedimentoSqlDAO;
import siap.siep.provvedimentopm.model.ProvvedimentoModel;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoModel;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.util.SIAPPathProperties;
//import org.apache.crimson.tree.XmlDocument;
import f3b.dao.DAOException;
import f3b.util.F3BException;
import f3b.util.report.ReportGenerator;
import f3b.util.xml.ModelTreeInputSource;
import f3b.util.xml.ParserModelTree;
import f3b.util.xml.TreeModel;
import f3b.util.xml.XMLUtils;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ProvvedimentoController extends SiapController implements IProvvedimento {

	// NUOVA INFRASTRUTTURA: aggiunte variabili di classe
	private SIAPPathProperties mPathProperties = SIAPPathProperties.getInstance();
	private String mPath = null;

	/**
	 * Inserisce il Provvedimento
	 * 
	 * @param aProvvedimento
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExInserisciProvvedimento(ProvvedimentoModel aProvvedimento,
			UtenteModel aUtente) throws F3BException {
		Connection lConn = null;

		ProvvedimentoDAO lProDao = null;
		ByteArrayOutputStream lByteArrayOut = null;

		try {
			TreeModel lTree = this.prelevaDati(aProvvedimento, aUtente);
			// NUOVA INFRASTRUTTURA: cambiato il path per differenziare macchina windows da macchina UNIX
			mPath = mPathProperties.getProperty("TEMP");
			lByteArrayOut = (ByteArrayOutputStream) this.generaDocumento(this.parseTreeXML(lTree), mPath);
			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

			// aProvvedimento.setDocumentoOut( lByteArrayOut );
			aProvvedimento.setDocumentoIn(lByteArrayInput);

			lConn = getDBTransaction();
			lProDao = new ProvvedimentoDAO(lConn);
			lProDao.setDAOFromModel(aProvvedimento);

			/*BigDecimal lIndex = */lProDao.insert();

			// Set del Blob
			// lProDao.setDocBlob(lByteArrayInput, lIndex);

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("ProvvedimentoController.ExInserisciProvvedimento: " + ex);
		} finally {
			cleanup(lProDao);
			cleanup(lConn);
		}

		return lByteArrayOut;
	}

	/**
	 * Seleziona un singolo documento rtf sul DB e lo restituisce come ByteArrayOutputStream
	 * 
	 * @param aProvvedimento
	 * @return Array con il Documento recuperato dal DB
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExGetDocumento(ProvvedimentoModel aProvvedimento) throws F3BException

	{
		Connection lConn = null;
		ProvvedimentoDAO lProDao = null;

		try {
			ByteArrayOutputStream lByteArrayOut = null;

			lConn = getDBConnection();
			lProDao = new ProvvedimentoDAO(lConn);

			lProDao.setIdProvvedimento(aProvvedimento.getIdProvvedimento());
			lProDao.selByKey();

			// ProvvedimentoModel lProMod = new ProvvedimentoModel();

			lProDao.start(1);

			if (lProDao.next())
				lByteArrayOut = lProDao.getDocBlob();

			lProDao.stop();

			return lByteArrayOut;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			cleanup(lProDao);
			cleanup(lConn);
		}

	}

	public Vector ExRicercaProvvedimento(ProvvedimentoModel aProvvedimento) throws F3BException {
		Connection lConn = null;
		Vector lProvvedimenti = new Vector();
		ProvvedimentoSqlDAO lProDao = null;

		try {
			lConn = getDBConnection();
			lProDao = new ProvvedimentoSqlDAO(lConn);
			lProDao.ricercaProvvedimento(aProvvedimento);

			lProDao.start();

			while (lProDao.next())
				lProvvedimenti.add((ProvvedimentoModel) lProDao.getModel());

			if (lProvvedimenti.size() == 0)
				throw new SIEPException(SIEPException.USER_MESSAGE, "Nessun Elemento trovato");

		} catch (DAOException daoEx) {
			throw new F3BException("ProvvedimentoController.ExRicercaProvvedimento: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lProDao);
			cleanup(lConn);
		}

		return lProvvedimenti;
	}

	public void ExModificaProvvedimento(ProvvedimentoModel aProvvedimento) throws F3BException {
		Connection lConn = null;
//		Vector lProvvedimenti = new Vector();
		ProvvedimentoDAO lProDao = null;

		try {
			lConn = getDBConnection();
			lProDao = new ProvvedimentoDAO(lConn);
			lProDao.setDAOFromModel(aProvvedimento);
			lProDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("ProvvedimentoController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lProDao);
			cleanup(lConn);
		}
	}

	public void ExCancellaProvvedimento(ProvvedimentoModel aProvvedimento) throws F3BException {
		Connection lConn = null;
		ProvvedimentoDAO lProDao = null;

		try {
			lConn = getDBConnection();
			lProDao = new ProvvedimentoDAO(lConn);
			lProDao.selCondizioneUpdate(aProvvedimento.getIdProvvedimento());
			lProDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException("ProvvedimentoController.ExCancellaProvvedimento: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lProDao);
			cleanup(lConn);
		}
	}

	/**
	 * Metodo per la generazione del documento. ( Presumibilmente da Generalizzare )
	 * <p>
	 * 
	 * @param aXML
	 * @param aTemplateRTF
	 * @return
	 * @throws F3BException
	 */
	private OutputStream generaDocumento(ByteArrayInputStream aXML, String aFileTemplateRTF)
			throws F3BException {
		ByteArrayOutputStream lOut = new ByteArrayOutputStream();
		try {
			FileInputStream lFis = new FileInputStream(aFileTemplateRTF);
			ReportGenerator lReport = new ReportGenerator(aXML, lFis, lOut);
			lReport.process();

			lFis.close();
			aXML.close();
		} catch (Throwable t) {
			t.printStackTrace();
			throw new F3BException(F3BException.USER_MESSAGE, "Errore nella generazione del report");
		}

		return lOut;
	}

	public TreeModel prelevaDati(ProvvedimentoModel aProModel, UtenteModel aUtente) throws F3BException {
		// Fascicolo
		FascicoloSiepModel lFasModel = new FascicoloSiepModel();
		lFasModel.setIdFascicoloSiep(aProModel.getFasSieIdFascicoloSiep());
		lFasModel = this.ricercaFascicolo(lFasModel);

		// Soggetto
		SoggettoModel lSogModel = new SoggettoModel();
		lSogModel.setIdSoggetto(lFasModel.getSogIdSoggetto());
		lSogModel = this.ricercaSoggetto(lSogModel);

		// Sentenza
		SentenzaModel lSenModel = new SentenzaModel();
		lSenModel.setIdSentenza(lFasModel.getSenIdSentenza());
		lSenModel = this.ricercaSentenza(lSenModel);

		// Posizione Giuridica
		PosizioneGiuridicaModel lPosModel = new PosizioneGiuridicaModel();
		lPosModel.setFasSieIdFascicoloSiep(lFasModel.getIdFascicoloSiep());
		lPosModel = this.ricercaPosizione(lPosModel);

		// Reati
		ReatoModel lReatoModel = new ReatoModel();
		lReatoModel.setFasSieIdFascicoloSiep(lFasModel.getIdFascicoloSiep());
		Vector lReati = this.ricercaReati(lReatoModel);

		// Avvocati
		AvvocatoFascicoloSiepModel lAvvFascModel = new AvvocatoFascicoloSiepModel();
		lAvvFascModel.setFasSieIdFascicoloSiep(lFasModel.getIdFascicoloSiep());
		Vector lAvvocati = this.ricercaAvvocati(lAvvFascModel);

		// Copia del Model con i dati del provvedimento
		ProvvedimentoModel lDocMod = new ProvvedimentoModel(aProModel);

		TreeModel lTreeDocMod = new TreeModel(lDocMod);
		TreeModel lTreeFasMod = new TreeModel(lFasModel);
		TreeModel lTreeSogMod = new TreeModel(lSogModel);
		TreeModel lTreeSenMod = new TreeModel(lSenModel);
		TreeModel lTreePosMod = new TreeModel(lPosModel);

		// Stabilisco le gerarchie
		lTreeDocMod.add(new TreeModel(aUtente.getUfficioUtente()));
		lTreeDocMod.add(new TreeModel(aUtente));
		lTreeDocMod.add(lTreeFasMod);
		lTreeDocMod.add(lTreeSogMod);
		lTreeDocMod.add(lTreeSenMod);
		lTreeDocMod.add(lTreePosMod);

		// Reati
		Iterator lItx = lReati.iterator();
		while (lItx.hasNext()) {
			// Carico la gerarchia
			TreeModel lTreeReatoMod = new TreeModel(((ReatoModel) lItx.next()));
			lTreeFasMod.add(lTreeReatoMod);
		}

		// Avvocati
		lItx = lAvvocati.iterator();
		while (lItx.hasNext()) {
			// Carico la gerarchia
			TreeModel lTreeAvvocatoMod = new TreeModel(((AvvocatoModel) lItx.next()));
			lTreeFasMod.add(lTreeAvvocatoMod);
		}

		return lTreeDocMod;
	}

	public FascicoloSiepModel ricercaFascicolo(FascicoloSiepModel aModel) throws F3BException {
		IFascicoloSiep lFascCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		Vector lFascicoli = lFascCtrl.ExRicercaFascicoloSiep(aModel);
		FascicoloSiepModel lModel = new FascicoloSiepModel();
		lModel = (FascicoloSiepModel) lFascicoli.get(0);

		return lModel;
	}

	public SoggettoModel ricercaSoggetto(SoggettoModel aModel) throws F3BException {
		ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
		Vector lSoggetti = lSogCtrl.ExRicercaSoggetto(aModel);
		SoggettoModel lModel = new SoggettoModel();
		lModel = (SoggettoModel) lSoggetti.get(0);

		return lModel;
	}

	public SentenzaModel ricercaSentenza(SentenzaModel aModel) throws F3BException {
		ISentenza lSenCtrl = SIEPLookupRemote.getSentenzaRemote();
		Vector lSentenze = lSenCtrl.ExRicercaSentenza(aModel);
		SentenzaModel lModel = new SentenzaModel();
		lModel = (SentenzaModel) lSentenze.get(0);

		return lModel;
	}

	public PosizioneGiuridicaModel ricercaPosizione(PosizioneGiuridicaModel aModel) throws F3BException {
		IPosizioneGiuridica lPosGiuridica = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaModel lModel = new PosizioneGiuridicaModel();
		lModel = lPosGiuridica.ExRicercaPosizioneGiuridicaCorrente(aModel);

		return lModel;
	}

	public Vector ricercaReati(ReatoModel aModel) throws F3BException {
		IReato lReatoCtrl = SIEPLookupRemote.getReatoRemote();
		Vector lReato = lReatoCtrl.ExRicercaReato(aModel);

		return lReato;
	}

	/**
	 *
	 * @param aAvvFascModel
	 * @return
	 * @throws F3BException
	 */
	public Vector ricercaAvvocati(AvvocatoFascicoloSiepModel aAvvFascModel) throws F3BException {
		IAvvocato lAvvocatoCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvocatoCtrl
				.ExRicercaAvvocatiAttualiFascicolo(new AvvocatoModel(), aAvvFascModel);

		return lAvvocati;
	}

	/**
	 * Metodo che si occupa di effettuare il parseTree ( Chissà forse da generalizzare )
	 * <p>
	 * 
	 * @param aTreeModel
	 * @return
	 * @throws F3BException
	 */
	private ByteArrayInputStream parseTreeXML(TreeModel aTreeModel) throws F3BException {
		ByteArrayInputStream lXML;

		try {
			ModelTreeInputSource lIs = new ModelTreeInputSource(aTreeModel);
			ParserModelTree lParser = new ParserModelTree();
			Document lDocXML = lParser.parse(lIs);
//			CharArrayWriter lWri = new CharArrayWriter();
			XMLUtils.serialize(lDocXML, System.out);
//			OutputStream out = null;
//			XMLUtils.serialize(lDocXML, out);
			lXML = new ByteArrayInputStream(System.out.toString().getBytes());
		} catch (Throwable t) {
			throw new F3BException("Errore di generazione report" + t);
		}

		return lXML;
	}

}