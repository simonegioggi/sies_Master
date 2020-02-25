package siap.siepe.jms.controller;

import java.math.BigDecimal;
import java.util.Iterator;
//import java.util.Enumeration;
//import java.io.ByteArrayInputStream;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.XModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.controller.IFascicoloSiepStampa;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siepe.SIEPEException;
import siap.siepe.assistentesociale.controller.IAssistenteSociale;
import siap.siepe.assistentesociale.model.AssistenteSocialeModel;
import siap.siepe.attivita.controller.IAttivita;
import siap.siepe.attivita.model.AttivitaModel;
import siap.siepe.espertoattivita.controller.IEspertoAttivita;
import siap.siepe.espertoattivita.model.EspertoAttivitaEspertoModel;
import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel;
//import siap.siepe.fascicolo.model.FascicoloSiepeModel;
import siap.siepe.relazione.controller.IRelazione;
import siap.siepe.relazione.model.RelazioneModel;
import siap.siepe.richiesta.controller.IRichiesta;
import siap.siepe.richiesta.model.RichiestaModel;
import siap.siepe.util.SIEPELookupRemote;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.stampa.controller.IStampaSius;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.report.ReportGenerator;
//import f3b.model.GenericModel;
import f3b.util.xml.TreeModel;

/**
 * 
 * <p>
 * Title:TrasmissioneJMScontroller
 * </p>
 * <p>
 * Description:
 * </p>
 * La classe contiene i metodi per assemblare i messaggi da trasmettere dagli Uffici UEPE.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TrasmissioneJMSController extends SiapController implements ITrasmissioneJMS {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Metodo che compone un MessaggioModel, opportunamente specializzato per l'attività.
	 * <p>
	 * 
	 * @param aKey
	 *            BigDecimal Id dell'attività da puntare.
	 * @param aFasEsteso
	 *            FascicoloSiepeEstesoModel Fascicolo Esteso.
	 * @param aUfficio
	 *            UfficioModel Dati dell'ufficio.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 * @return MessaggioModel ritorna il MessaggioModel composto.
	 */
	public MessaggioModel getMessageForAttivita(BigDecimal aKeyAttivita, FascicoloSiepeEstesoModel aFasEsteso,
			UfficioModel aUfficio) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".getMessageForAttivita: inizio");
		MessaggioModel lMessage = new MessaggioModel();

		// Creazione del TreeModel
		TreeModel lTreeRoot = getTreeModelForTrasmissioneAttivita(aKeyAttivita, aFasEsteso, aUfficio, null);

		// Inserimento del TreeModel nel messaggio
		lMessage.setTreeModel(lTreeRoot);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".getMessageForAttivita: fine");

		return lMessage;
	}

	/**
	* 
	*/
	public TreeModel getTreeModelForAttivita(BigDecimal aKeyAttivita, FascicoloSiepeEstesoModel aFasEsteso,
			UfficioModel aUfficio, UtenteModel aUtente) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".getTreeModelForAttivita: inizio");

		// Creazione della Root del TreeModel
		TreeModel lTreeRoot = new TreeModel(createRoot(aUfficio));

		if (aUtente != null)
			lTreeRoot.add(new TreeModel(aUtente));

		// aggiunta dei dati inerenti l'Attività
		lTreeRoot = addAttivita(aKeyAttivita, lTreeRoot);

		// aggiunta di tutti i dati inerenti il fascicolo SIEPE
		lTreeRoot = addFascicoloSiepeEsteso(aFasEsteso, aUtente, lTreeRoot);

		// Richiamo del ReportGenerator per debug
		/*
		 * Luigi 4-4-2007 ReportGenerator lRep = new ReportGenerator(); // Per stampa contenuto treemodel per
		 * stampa. lRep.parseTreeXML( lTreeRoot );
		 * 
		 * 
		 * // Richiamo del ReportGenerator per debug ReportGenerator lRep = new ReportGenerator(); // [FT] -
		 * 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 * siesLogger.debug("Treemodel nel messaggio " +lRep.debugTreeXML(lTreeRoot));
		 */

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".getTreeModelForAttivita: fine");

		return lTreeRoot;
	}

	public TreeModel getTreeModelForTrasmissioneAttivita(BigDecimal aKeyAttivita,
			FascicoloSiepeEstesoModel aFasEsteso, UfficioModel aUfficio, UtenteModel aUtente)
			throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".getTreeModelForTrasmissioneAttivita: inizio");

		// Creazione della Root del TreeModel
		TreeModel lTreeRoot = new TreeModel(createRoot(aUfficio));

		if (aUtente != null)
			lTreeRoot.add(new TreeModel(aUtente));

		// Aggiunta dei dati inerenti l'Attività
		// TEST POINT 2007-03-13 Commentato poichè sostituito con quello di seguito
		// lTreeRoot = addAttivita(aKeyAttivita, lTreeRoot);

		lTreeRoot = addAttivitaForTrasmissione(aKeyAttivita, lTreeRoot);

		// Aggiunta del FascicoloSiepeEsteso
		lTreeRoot = addFascicoloSiepeEstesoModel(aFasEsteso, aUtente, lTreeRoot);

		/*
		 * Luigi 4-4-2007 // Richiamo del ReportGenerator per debug ReportGenerator lRep = new
		 * ReportGenerator(); // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
		 * posto di LogF3B.getLogger() siesLogger.debug("Treemodel nel messaggio " +
		 * lRep.debugTreeXML(lTreeRoot));
		 * 
		 * // Per stampa in log del TreeModel lRep.parseTreeXML( lTreeRoot );
		 */

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".getTreeModelForTrasmissioneAttivita: fine");

		return lTreeRoot;
	}

	/**
	 * Metodo che compone un MessaggioModel, opportunamente specializzato per la richiesta.
	 * <p>
	 * 
	 * @param aKey
	 *            BigDecimal Id della richiesta da puntare.
	 * @param aFasEsteso
	 *            FascicoloSiepeEstesoModel Fascicolo Esteso.
	 * @param aUfficio
	 *            UfficioModel Dati dell'ufficio.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 * @return MessaggioModel ritorna il MessaggioModel composto.
	 */
	public MessaggioModel getMessageForRichiesta(BigDecimal aKey, FascicoloSiepeEstesoModel aFasEsteso,
			UfficioModel aUfficio) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".getMessageForRichiesta: inizio");
		MessaggioModel lMessage = new MessaggioModel();

		// Creazione del TreeModel
		TreeModel lTreeRoot = getTreeModelForTrasmissioneRichiesta(aKey, aFasEsteso, aUfficio, null);

		// Inserimento del TreeModel nel messaggio
		lMessage.setTreeModel(lTreeRoot);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".getMessageForRichiesta: fine");

		return lMessage;
	}

	/**
	 * Metodo che compone un MessaggioModel, opportunamente specializzato per la relazione.
	 * <p>
	 * 
	 * @param aKey
	 *            BigDecimal Id della richiesta da puntare.
	 * @param aFasEsteso
	 *            FascicoloSiepeEstesoModel Fascicolo Esteso.
	 * @param aUfficio
	 *            UfficioModel Dati dell'ufficio.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 * @return MessaggioModel ritorna il MessaggioModel composto.
	 */
	public MessaggioModel getMessageForRelazione(BigDecimal aKey, FascicoloSiepeEstesoModel aFasEsteso,
			UfficioModel aUfficio) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".getMessageForRelazione: inizio");
		MessaggioModel lMessage = new MessaggioModel();

		// Creazione del TreeModel
		TreeModel lTreeRoot = getTreeModelForTrasmissioneRelazione(aKey, aFasEsteso, aUfficio, null);

		// Inserimento del TreeModel nel messaggio
		lMessage.setTreeModel(lTreeRoot);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".getMessageForRelazione: fine");

		return lMessage;
	}

	public TreeModel getTreeModelForRichiesta(BigDecimal aKeyRichiesta, FascicoloSiepeEstesoModel aFasEsteso,
			UfficioModel aUfficio, UtenteModel aUtente) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".getTreeModelForTrasmissioneRichiesta: inizio");

		// Creazione della Root del TreeModel
		TreeModel lTreeRoot = new TreeModel(createRoot(aUfficio));

		if (aUtente != null)
			lTreeRoot.add(new TreeModel(aUtente));

		// aggiunta dei dati inerenti la Richiesta
		lTreeRoot = addRichiesta(aKeyRichiesta, lTreeRoot);

		// aggiunta del FascicoloSiepeEsteso
		lTreeRoot = addFascicoloSiepeEsteso(aFasEsteso, aUtente, lTreeRoot);

		// Richiamo del ReportGenerator per debug
		ReportGenerator lRep = new ReportGenerator();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Treemodel nel messaggio " + lRep.debugTreeXML(lTreeRoot));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".getTreeModelForTrasmissioneAttivita: fine");

		return lTreeRoot;
	}

	public TreeModel getTreeModelForTrasmissioneRichiesta(BigDecimal aKeyRichiesta,
			FascicoloSiepeEstesoModel aFasEsteso, UfficioModel aUfficio, UtenteModel aUtente)
			throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".getTreeModelForTrasmissioneRichiesta: inizio");

		// Creazione della Root del TreeModel
		TreeModel lTreeRoot = new TreeModel(createRoot(aUfficio));

		if (aUtente != null)
			lTreeRoot.add(new TreeModel(aUtente));

		// Aggiunta dei dati inerenti la Richiesta
		lTreeRoot = addRichiestaForTrasmissione(aKeyRichiesta, lTreeRoot);

		// Aggiunta del FascicoloSiepeEsteso
		lTreeRoot = addFascicoloSiepeEstesoModel(aFasEsteso, aUtente, lTreeRoot);

		// Richiamo del ReportGenerator per debug
		ReportGenerator lRep = new ReportGenerator();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Treemodel nel messaggio " + lRep.debugTreeXML(lTreeRoot));

		// Stampa del XML nel file di debug.log
		lRep.parseTreeXML(lTreeRoot);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".getTreeModelForTrasmissioneRichiesta: fine");

		return lTreeRoot;
	}

	public TreeModel getTreeModelForTrasmissioneRelazione(BigDecimal aKey,
			FascicoloSiepeEstesoModel aFasEsteso, UfficioModel aUfficio, UtenteModel aUtente)
			throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".getTreeModelForTrasmissioneRelazione: inizio");

		// Creazione della Root del TreeModel
		TreeModel lTreeRoot = new TreeModel(createRoot(aUfficio));

		if (aUtente != null)
			lTreeRoot.add(new TreeModel(aUtente));

		// Aggiunta dei dati inerenti la Relazione.
		lTreeRoot = addRelazione(aKey, lTreeRoot);

		// aggiunta del FascicoloSiepeEsteso
		lTreeRoot = addFascicoloSiepeEstesoModel(aFasEsteso, aUtente, lTreeRoot);

		// Richiamo del ReportGenerator per debug
		ReportGenerator lRep = new ReportGenerator();
		lRep.parseTreeXML(lTreeRoot);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Treemodel nel messaggio " + lRep.debugTreeXML(lTreeRoot));
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".getTreeModelForTrasmissioneRelazione: fine");

		return lTreeRoot;
	}

	public TreeModel getTreeModelForFascicolo(FascicoloSiepeEstesoModel aFasEsteso, UfficioModel aUfficio,
			UtenteModel aUtente) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".getTreeModelForFascicolo: inizio");

		// Creazione della Root del TreeModel
		TreeModel lTreeRoot = new TreeModel(createRoot(aUfficio));

		if (aUtente != null)
			lTreeRoot.add(new TreeModel(aUtente));

		// aggiunta del fascicolo SIEPE
		// lTreeRoot.add(new TreeModel(aFasEsteso.getFascicoloSiepe()));

		// aggiunta di tutti i dati inerenti il fascicolo SIEPE
		lTreeRoot = addFascicoloSiepeEsteso(aFasEsteso, aUtente, lTreeRoot);

		// Richiamo del ReportGenerator per debug
		ReportGenerator lRep = new ReportGenerator();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Treemodel nel messaggio " + lRep.debugTreeXML(lTreeRoot));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".getTreeModelForFascicolo: fine");

		return lTreeRoot;
	}

	/**
	 * Crea la root del Documento, ion esso vengono inseriti i dati afferenti all'ufficio.
	 * <p>
	 * 
	 * @param aUfficio
	 *            dati dell'ufficio, passti come arg.
	 * @return l'XModel opportunamente cvalorizzato.
	 */
	private XModel createRoot(UfficioModel aUfficio) {
		XModel lStampa = new XModel();

		lStampa.setTipoUfficio(aUfficio.getDescrTipoUfficio());
		lStampa.setDataElaborazione(DateUtils.getSysDate());
		lStampa.setIndirizzo(aUfficio.getIndirizzo());
		lStampa.setTelefono(aUfficio.getTelefono());
		lStampa.setFax(aUfficio.getFax());
		lStampa.setEMail(aUfficio.getEMail());
		lStampa.setUfficioCAP(aUfficio.getCap());
		lStampa.setUfficio(aUfficio.getDescrComune());

		return lStampa;
	}

	/**
	 * La funzione aggiunge al TreeModel passato come parametro tutti i dati concernenti il Fascicolo SIEPE.
	 * <p>
	 * 
	 * @param aFasEsteso
	 * @param aTree
	 * @param aFasEsteso
	 * @param aTree
	 * @return
	 */
	private TreeModel addFascicoloSiepeEsteso(FascicoloSiepeEstesoModel aFasEsteso, UtenteModel aUtente,
			TreeModel aTree) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".addFascicoloSiepeEsteso: inizio");

		TreeModel lTreeFasSiepe = null;

		// Fascicolo SIEPE
		if (aFasEsteso.getFascicoloSiepe() != null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("#### " + getClass().getName() + ".addFascicoloSiepeEsteso: Fascicolo siepe ");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("####### aFasEsteso.getFascicoloSiepe() : " + aFasEsteso.getFascicoloSiepe());
			// aTree.add(new TreeModel(aFasEsteso.getFascicoloSiepe()));
			// Fascicolo Siepe
			lTreeFasSiepe = new TreeModel(aFasEsteso.getFascicoloSiepe());
		} else
			throw new SIEPEException(F3BException.USER_MESSAGE, "Fascicolo SIEPE Assente !");

		// Soggetto
		if (aFasEsteso.getSoggetto() != null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("#### " + getClass().getName()
					+ ".addFascicoloSiepeEsteso: InserisceSoggetto in TreeModel");
			// Il soggetto viene aggiunto all'Albero principale (TreeRoot)
			aTree.add(new TreeModel(aFasEsteso.getSoggetto()));
		}

		// Fascicolo SIEP
		if (aFasEsteso.getFascicoloSiep() != null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("#### " + getClass().getName() + ".addFascicoloSiepeEsteso: Fascicolo siep ");

			if (aUtente != null) {
				// Se viene passato l'UtenteModel viene richiamata la stampa del Fascicolo Siep per ricavare i
				// dati del Fascicolo Siep
				addDatiSiep(aFasEsteso.getFascicoloSiep(), aUtente, lTreeFasSiepe);
			} else {
				lTreeFasSiepe.add(new TreeModel(aFasEsteso.getFascicoloSiep()));
			}
		}

		// Fascicolo SIUS
		if (aFasEsteso.getFascicoloSius() != null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("#### " + getClass().getName() + ".addFascicoloSiepeEsteso: Fascicolo sius ");
			if (aFasEsteso.getFascicoloSius().getFascicoloSiusModel() != null) {
				lTreeFasSiepe.add(new TreeModel(aFasEsteso.getFascicoloSius().getFascicoloSiusModel()));
			}
			// Generale Procedimento
			if (aFasEsteso.getFascicoloSius().getGeneraleProcedimentoModel() != null) {
				lTreeFasSiepe
						.add(new TreeModel(aFasEsteso.getFascicoloSius().getGeneraleProcedimentoModel()));
			}
		}

		// 20070616 - Si Recupera l'entità evento ed si inserisce nel fascicolo siepe esteso
		// per poi eseguire l'istruzione successiva, al fine di geracherizzare l'alberatura
		// dei model.
		if (aFasEsteso.getFascicoloSiepe().getEveIdEvento() != null) {
			IEvento lEvento = SICOLookupRemote.getEventoRemote();
			EventoModel lEventoModel = lEvento
					.ExRicercaEventoByKey(aFasEsteso.getFascicoloSiepe().getEveIdEvento());
			// Inserisce l'evento recuperato nel fascicolo siepe esteso.
			aFasEsteso.setEvento(lEventoModel);
		}

		// Evento
		if (aFasEsteso.getEvento() != null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("####### aFasEsteso.getEvento() : " + aFasEsteso.getEvento());
			addDatiEvento(aFasEsteso.getEvento(), lTreeFasSiepe);
		}

		// L'albero con i dati del Fascicolo SIEPE Esteso aggiunti all'albero principale
		aTree.add(lTreeFasSiepe);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".addFascicoloSiepeEsteso: fine");

		return aTree;
	}

	/**
	 * La funzione aggiunge al TreeModel passato come parametro l'intero Fascicolo Siepe Esteso per la fase di
	 * trasmissione dei dati di fascicolo. Inoltre, recupera le sole entità Evento e Deposito decreto.
	 * <p>
	 * 
	 * @param aFasEsteso
	 *            Fascicolo Esteso Model
	 * @param aUtente
	 *            UtenteModel
	 * @param aTree
	 *            TreeModel
	 * @return TreeModel
	 */
	private TreeModel addFascicoloSiepeEstesoModel(FascicoloSiepeEstesoModel aFasEsteso, UtenteModel aUtente,
			TreeModel aTree) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".addFascicoloSiepeEstesoModel: inizio");

		// 20070616 - Si Recupera l'entità evento e si inserisce nel fascicolo siepe esteso
		// per poi eseguire l'istruzione successiva, al fine di mettere in gerarchia nell'alberatura
		// dei model.
		addEvento(aFasEsteso);

		// Recupera ed inserisce deposito decreto.
		if (aFasEsteso.getEvento() != null)
			addDepositoDecreto(aFasEsteso.getEvento(), aTree);

		// Evento etc...
		// if (aFasEsteso.getEvento() != null)
		// addDatiEvento(aFasEsteso.getEvento(), aTree);

		aTree.add(new TreeModel(aFasEsteso));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".addFascicoloSiepeEstesoModel: fine");

		return aTree;
	}

	/**
	 * Ricerca l'attività a partire dalla sua chiave, costruisce un TreeModel con i dati trovati e lo aggiunge
	 * al TreeModel ricevuto come parametro.
	 * <p>
	 * 
	 * @param aKeyAttivita
	 * @param aTree
	 * @return
	 * @throws Exception
	 */
	/*
	 * 2007-03-13 Commentato poichè ottimizzato I fase
	 * 
	 * private TreeModel addAttivita(BigDecimal aKeyAttivita, TreeModel aTree) throws F3BException { // [FT] -
	 * 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 * siesLogger.debug(getClass().getName() + ".addAttivita: inizio");
	 * 
	 * // Ricerca Attività IAttivita lAttCtrl = SIEPELookupRemote.getAttivitaRemote(); AttivitaModel lAttivita
	 * = lAttCtrl.ExRicercaAttivitaByKey(aKeyAttivita);
	 * 
	 * // Ricerca Relazioni if( lAttivita != null ) {
	 * 
	 * IRelazione lRelCtrl = SIEPELookupRemote.getRelazioneRemote(); Vector lRelazioni =
	 * lRelCtrl.ExRicercaRelazioniByAttivita(aKeyAttivita); if (lRelazioni.size() > 0) { RelazioneModel[]
	 * lRelArray = new RelazioneModel[lRelazioni.size()] ; Iterator lItRel = lRelazioni.iterator(); int i = 0;
	 * while (lItRel.hasNext()) { RelazioneModel lRelazione = (RelazioneModel) lItRel.next(); // Occorre
	 * rifare la ricerca puntuale per ricavare anche il BLOB RelazioneModel lRelazioneBlob =
	 * lRelCtrl.ExRicercaRelazioneByKey(lRelazione.getIdRelazione()); if (lRelazioneBlob != null) {
	 * lRelazione.setDocPerTrasferimento(lRelazioneBlob.getDocBlobOut()); lRelArray[i]=lRelazione; i++; } }
	 * lAttivita.setRelazioni(lRelArray);
	 * 
	 * }
	 * 
	 * 
	 * if (lAttivita != null) { // Si ricava il BLOB per il trasferimento
	 * lAttivita.setDocPerTrasferimento(lAttCtrl.ExGetDocBlob(lAttivita)); // Costruzione del TreeModel
	 * TreeModel lTreeAttivita = new TreeModel(lAttivita);
	 * 
	 * // Ricerca Relazioni lTreeAttivita = addRelazioniXAttivita(aKeyAttivita, lTreeAttivita); // Ricerca
	 * Esperti lTreeAttivita = addEspertiXAttivita(aKeyAttivita, lTreeAttivita);
	 * 
	 * // Il TreeModel Attività viene aggiunto a quello ricevuto come argomento aTree.add(lTreeAttivita); }
	 * 
	 * if (lAttivita.getAssSocIdAssSociale() != null) { // Chiama il controller per il recupero dei dati
	 * dell'Assistente Sociale. IAssistenteSociale lCtrlAssSoc =
	 * SIEPELookupRemote.getAssistenteSocialeRemote(); AssistenteSocialeModel lAssSocMod =
	 * lCtrlAssSoc.ExRicercaAssistenteSocialeByKey(lAttivita.getAssSocIdAssSociale()); // Costruzione del
	 * TreeModel TreeModel lTreeAssistente = new TreeModel(lAssSocMod); aTree.add(lTreeAssistente); }
	 * 
	 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	 * LogF3B.getLogger() siesLogger.debug(getClass().getName() + ".addAttivita: fine");
	 * 
	 * return aTree; }
	 */

	/**
	 * Ricerca l'attività a partire dalla sua chiave, costruisce un TreeModel con i dati trovati e lo aggiunge
	 * al TreeModel ricevuto come parametro.
	 * <p>
	 * 
	 * @param aKeyAttivita
	 * @param aTree
	 * @return
	 * @throws Exception
	 */
	/*
	 * 2007-03-13 Commentato poiche ottimizzato ( II fase ) private TreeModel addAttivita(BigDecimal
	 * aKeyAttivita, TreeModel aTree) throws F3BException { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la
	 * variabile di istanza siesLogger al posto di LogF3B.getLogger() siesLogger.debug(getClass().getName() +
	 * ".addAttivita: inizio");
	 * 
	 * // Ricerca Attività IAttivita lAttCtrl = SIEPELookupRemote.getAttivitaRemote(); AttivitaModel lAttivita
	 * = lAttCtrl.ExRicercaAttivitaByKey(aKeyAttivita);
	 * 
	 * // Ricerca Relazioni if( lAttivita != null ) { IRelazione lRelCtrl =
	 * SIEPELookupRemote.getRelazioneRemote(); Vector lRelazioni =
	 * lRelCtrl.ExRicercaRelazioniWithBlobByAttivita( aKeyAttivita );
	 * lAttivita.setRelazioni((RelazioneModel[])lRelazioni.toArray(new RelazioneModel[0])); }
	 * 
	 * if (lAttivita != null) { // Si ricava il BLOB per il trasferimento
	 * lAttivita.setDocPerTrasferimento(lAttCtrl.ExGetDocBlob(lAttivita)); // Costruzione del TreeModel
	 * TreeModel lTreeAttivita = new TreeModel(lAttivita);
	 * 
	 * // Ricerca Relazioni lTreeAttivita = addRelazioniXAttivita(aKeyAttivita, lTreeAttivita); // Ricerca
	 * Esperti lTreeAttivita = addEspertiXAttivita(aKeyAttivita, lTreeAttivita);
	 * 
	 * // Il TreeModel Attività viene aggiunto a quello ricevuto come argomento aTree.add(lTreeAttivita); }
	 * 
	 * if (lAttivita.getAssSocIdAssSociale() != null) { // Chiama il controller per il recupero dei dati
	 * dell'Assistente Sociale. IAssistenteSociale lCtrlAssSoc =
	 * SIEPELookupRemote.getAssistenteSocialeRemote(); AssistenteSocialeModel lAssSocMod =
	 * lCtrlAssSoc.ExRicercaAssistenteSocialeByKey(lAttivita.getAssSocIdAssSociale()); // Costruzione del
	 * TreeModel TreeModel lTreeAssistente = new TreeModel(lAssSocMod); aTree.add(lTreeAssistente); }
	 * 
	 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	 * LogF3B.getLogger() siesLogger.debug(getClass().getName() + ".addAttivita: fine");
	 * 
	 * return aTree; }
	 */

	/**
	 * Ricerca l'attività a partire dalla sua chiave, costruisce un TreeModel con i dati trovati e lo aggiunge
	 * al TreeModel ricevuto come parametro. Questo metodo viene utilizzato per l'organizzazione dei dati per
	 * la stampa, escludendo i BLOB.
	 * <p>
	 * 
	 * @param aKeyAttivita
	 *            Chiave attività.
	 * @param aTree
	 *            TreeModel da popolare.
	 * @return TreeModel opportunamente popolato.
	 * @throws Exception
	 *             propaga errore di eccezione.
	 */
	private TreeModel addAttivita(BigDecimal aKeyAttivita, TreeModel aTree) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".addAttivita: inizio");

		// Ricerca Attività
		IAttivita lAttCtrl = SIEPELookupRemote.getAttivitaRemote();
		AttivitaModel lAttivita = lAttCtrl.ExRicercaAttivitaByKey(aKeyAttivita);

		if (lAttivita != null) {
			// Costruzione del TreeModel
			TreeModel lTreeAttivita = new TreeModel(lAttivita);

			// Ricerca Relazioni
			lTreeAttivita = addRelazioniXAttivita(aKeyAttivita, lTreeAttivita);

			// Ricerca Esperti
			lTreeAttivita = addEspertiXAttivita(aKeyAttivita, lTreeAttivita);

			// Aggiunge Assistente sociale.
			addAssistenteSociale(lAttivita.getAssSocIdAssSociale(), lTreeAttivita);

			// Il TreeModel Attività viene aggiunto a quello ricevuto come argomento
			aTree.add(lTreeAttivita);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".addAttivita: fine");

		return aTree;
	}

	/**
	 * Ricerca l'attività a partire dalla sua chiave, costruisce un TreeModel con i dati trovati e lo aggiunge
	 * al TreeModel ricevuto come parametro. Questo metodo viene utilizzato per l'organizzazione dei dati per
	 * la trasmissione verso JMS, includendo i dati BLOB
	 * <p>
	 * 
	 * @param aKeyAttivita
	 *            chiave dell'attività
	 * @param aTree
	 *            TreeModel dei dati.
	 * @return TreeModel opportunamente popolato
	 * @throws Exception
	 *             propaga errore di eccezione.
	 */
	private TreeModel addAttivitaForTrasmissione(BigDecimal aKeyAttivita, TreeModel aTree)
			throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".addAttivitaForTrasmissione: inizio");

		// Ricerca Attività
		IAttivita lAttCtrl = SIEPELookupRemote.getAttivitaRemote();
		AttivitaModel lAttivita = lAttCtrl.ExRicercaAttivitaByKey(aKeyAttivita);

		// Ricerca Relazioni
		if (lAttivita != null) {
			// Prelava relazioni è le inserice nell'array.
			IRelazione lRelCtrl = SIEPELookupRemote.getRelazioneRemote();
			Vector lRelazioni = lRelCtrl.ExRicercaRelazioniWithBlobByAttivita(aKeyAttivita);
			lAttivita.setRelazioni((RelazioneModel[]) lRelazioni.toArray(new RelazioneModel[0]));

			// Si ricava il BLOB per il trasferimento
			lAttivita.setDocPerTrasferimento(lAttCtrl.ExGetDocBlob(lAttivita));

			// Costruzione del TreeModel
			TreeModel lTreeAttivita = new TreeModel(lAttivita);

			// Ricerca Esperti
			lTreeAttivita = addEspertiXAttivita(aKeyAttivita, lTreeAttivita);

			// Il TreeModel Attività viene aggiunto a quello ricevuto come argomento
			aTree.add(lTreeAttivita);

			// L'assistente sociale in fase di trsmissione viene aggiunto al tree model
			// allo stesso livello di attività.
			addAssistenteSociale(lAttivita.getAssSocIdAssSociale(), aTree);

		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".addAttivitaForTrasmissione: fine");

		return aTree;
	}

	/**
	 * Ricerca la richiesta a partire dalla sua chiave, costruisce un TreeModel con i dati trovati e lo
	 * aggiunge al TreeModel ricevuto come parametro.
	 * <p>
	 * 
	 * @param aKeyRichiesta
	 *            id della richiesta
	 * @param aTree
	 *            treemodel al quale aggiungere la richiesta
	 * @return TreeModel opportunamente popolato
	 * @throws Exception
	 */
	/*
	 * 2007-03-13 Commentato poichè ottimizzato I fase
	 * 
	 * private TreeModel addRichiesta(BigDecimal aKeyRichiesta, TreeModel aTree) throws F3BException { // [FT]
	 * - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 * siesLogger.debug(getClass().getName() + ".addRichiesta: inizio");
	 * 
	 * // Ricerca Richiesta IRichiesta lRicCtrl = SIEPELookupRemote.getRichiestaRemote(); RichiestaModel
	 * lRichiesta = lRicCtrl.ExRicercaRichiestaByKey(aKeyRichiesta);
	 * 
	 * if (lRichiesta != null) { Vector lRelazioni = lRelCtrl.ExRicercaRelazioniByRichiesta(aKeyRichiesta);
	 * 
	 * if (lRelazioni.size() > 0) { RelazioneModel[] lRelArray = new RelazioneModel[lRelazioni.size()] ;
	 * Iterator lItRel = lRelazioni.iterator(); int i = 0; while (lItRel.hasNext()) { RelazioneModel
	 * lRelazione = (RelazioneModel) lItRel.next(); // Occorre rifare la ricerca puntuale per ricavare anche
	 * il BLOB RelazioneModel lRelazioneBlob = lRelCtrl.ExRicercaRelazioneByKey(lRelazione.getIdRelazione());
	 * if (lRelazioneBlob != null) { lRelazione.setDocPerTrasferimento(lRelazioneBlob.getDocBlobOut());
	 * lRelArray[i]=lRelazione; i++; } }
	 * 
	 * 
	 * }
	 * 
	 * // Si ricava il BLOB per il trasferimento
	 * lRichiesta.setDocPerTrasferimento(lRicCtrl.ExGetDocBlob(lRichiesta)); // Costruzione del TreeModel
	 * TreeModel lTreeRichiesta = new TreeModel(lRichiesta);
	 * 
	 * // Ricerca Relazioni lTreeRichiesta = addRelazioniXRichiesta(aKeyRichiesta, lTreeRichiesta);
	 * 
	 * // Il TreeModel Richiesta viene aggiunto a quello ricevuto come argomento aTree.add(lTreeRichiesta); }
	 * 
	 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	 * LogF3B.getLogger() siesLogger.debug(getClass().getName() + ".addRichiesta: fine");
	 * 
	 * return aTree; }
	 */
	/**
	 * Ricerca la richiesta a partire dalla sua chiave, costruisce un TreeModel con i dati trovati e lo
	 * aggiunge al TreeModel ricevuto come parametro.
	 * <p>
	 * 
	 * @param aKeyRichiesta
	 *            id della richiesta
	 * @param aTree
	 *            treemodel al quale aggiungere la richiesta
	 * @return TreeModel opportunamente popolato
	 * @throws Exception
	 */
	/*
	 * 2007-03-13 Commentato poichè ottimizzato ( II fase ) private TreeModel addRichiesta(BigDecimal
	 * aKeyRichiesta, TreeModel aTree) throws F3BException { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la
	 * variabile di istanza siesLogger al posto di LogF3B.getLogger() siesLogger.debug(getClass().getName() +
	 * ".addRichiesta: inizio");
	 * 
	 * // Ricerca Richiesta IRichiesta lRicCtrl = SIEPELookupRemote.getRichiestaRemote(); RichiestaModel
	 * lRichiesta = lRicCtrl.ExRicercaRichiestaByKey(aKeyRichiesta);
	 * 
	 * if (lRichiesta != null) { IRelazione lRelCtrl = SIEPELookupRemote.getRelazioneRemote(); Vector
	 * lRelazioni = lRelCtrl.ExRicercaRelazioniWithBlobByRichiesta( aKeyRichiesta );
	 * lRichiesta.setRelazioni((RelazioneModel[])lRelazioni.toArray(new RelazioneModel[0]));
	 * 
	 * // Si ricava il BLOB per il trasferimento
	 * lRichiesta.setDocPerTrasferimento(lRicCtrl.ExGetDocBlob(lRichiesta)); // Costruzione del TreeModel
	 * TreeModel lTreeRichiesta = new TreeModel(lRichiesta);
	 * 
	 * // Ricerca Relazioni lTreeRichiesta = addRelazioniXRichiesta(aKeyRichiesta, lTreeRichiesta);
	 * 
	 * // Il TreeModel Richiesta viene aggiunto a quello ricevuto come argomento aTree.add(lTreeRichiesta); }
	 * 
	 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	 * LogF3B.getLogger() siesLogger.debug(getClass().getName() + ".addRichiesta: fine");
	 * 
	 * return aTree; }
	 */

	/**
	 * Ricerca la richiesta a partire dalla sua chiave, costruisce un TreeModel con i dati trovati e lo
	 * aggiunge al TreeModel ricevuto come parametro. Questo metodo viene utilizzato per l'organizzazione dei
	 * dati per la stampa.
	 * <p>
	 * 
	 * @param aKeyRichiesta
	 *            id della richiesta.
	 * @param aTree
	 *            treemodel al quale aggiungere la richiesta.
	 * @return TreeModel opportunamente popolato.
	 * @throws Exception
	 *             propaga errore di eccezione.
	 */
	private TreeModel addRichiesta(BigDecimal aKeyRichiesta, TreeModel aTree) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".addRichiesta: inizio");

		// Ricerca Richiesta
		IRichiesta lRicCtrl = SIEPELookupRemote.getRichiestaRemote();
		RichiestaModel lRichiesta = lRicCtrl.ExRicercaRichiestaByKey(aKeyRichiesta);

		if (lRichiesta != null) {
			// Costruzione del TreeModel.
			TreeModel lTreeRichiesta = new TreeModel(lRichiesta);

			// Ricerca Relazioni.
			lTreeRichiesta = addRelazioniXRichiesta(aKeyRichiesta, lTreeRichiesta);

			// Il TreeModel Richiesta viene aggiunto a quello ricevuto come argomento.
			aTree.add(lTreeRichiesta);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".addRichiesta: fine");

		return aTree;
	}

	/**
	 * Ricerca la richiesta a partire dalla sua chiave, costruisce un TreeModel con i dati trovati e lo
	 * aggiunge al TreeModel ricevuto come parametro. Questo metodo viene utilizzato quando si desidera
	 * organizzare i dati per la trasmissione.
	 * <p>
	 * 
	 * @param aKeyRichiesta
	 *            id della richiesta.
	 * @param aTree
	 *            treemodel al quale aggiungere la richiesta.
	 * @return TreeModel opportunamente popolato.
	 * @throws Exception
	 *             propaga errore di eccezione.
	 */
	private TreeModel addRichiestaForTrasmissione(BigDecimal aKeyRichiesta, TreeModel aTree)
			throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".addRichiestaForTrasmissione: inizio");

		// Ricerca Richiesta.
		IRichiesta lRicCtrl = SIEPELookupRemote.getRichiestaRemote();
		RichiestaModel lRichiesta = lRicCtrl.ExRicercaRichiestaByKey(aKeyRichiesta);

		if (lRichiesta != null) {
			// Ricerca le relazioni con i blob e le inserisce nell'array incapsulato
			// nel model della Richiesta.
			IRelazione lRelCtrl = SIEPELookupRemote.getRelazioneRemote();
			Vector lRelazioni = lRelCtrl.ExRicercaRelazioniWithBlobByRichiesta(aKeyRichiesta);
			lRichiesta.setRelazioni((RelazioneModel[]) lRelazioni.toArray(new RelazioneModel[0]));

			// Si ricava il BLOB per il trasferimento.
			lRichiesta.setDocPerTrasferimento(lRicCtrl.ExGetDocBlob(lRichiesta));

			// Costruzione del TreeModel.
			TreeModel lTreeRichiesta = new TreeModel(lRichiesta);

			// Il TreeModel Richiesta viene aggiunto a quello ricevuto come argomento.
			aTree.add(lTreeRichiesta);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".addRichiestaForTrasmissione: fine");

		return aTree;
	}

	/**
	 * Ricerca l'attività a partire dalla sua chiave, costruisce un TreeModel con i dati trovati e lo aggiunge
	 * al TreeModel ricevuto come parametro.
	 * <p>
	 * 
	 * @param aKeyAttivita
	 * @param aTree
	 * @return
	 * @throws Exception
	 */
	/*
	 * private TreeModel addRelazione(BigDecimal aKey, TreeModel aTree) throws F3BException { // [FT] -
	 * 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 * siesLogger.debug(getClass().getName() + ".addRelazione: inizio");
	 * 
	 * // Ricerca Relazione IRelazione lRelCtrl = SIEPELookupRemote.getRelazioneRemote(); RelazioneModel
	 * lRelazione = lRelCtrl.ExRicercaRelazioneByKey(aKey);
	 * 
	 * if (lRelazione != null) { // Si ricava il BLOB per il trasferimento
	 * lRelazione.setDocPerTrasferimento(lRelCtrl.ExGetDocBlob(lRelazione)); // Costruzione del TreeModel
	 * TreeModel lTreeAttivita = new TreeModel(lAttivita);
	 * 
	 * if (lAttivita.getAssSocIdAssSociale() != null) { // Chiama il controller per il recupero dei dati
	 * dell'Assistente Sociale. IAssistenteSociale lCtrlAssSoc = SIEPELookupRemote.
	 * getAssistenteSocialeRemote(); AssistenteSocialeModel lAssSocMod = lCtrlAssSoc.
	 * ExRicercaAssistenteSocialeByKey(lAttivita.getAssSocIdAssSociale()); // Aggiunge il TreeModel dell'Ass.
	 * Soci. a quello dell'Attività lTreeAttivita.add(new TreeModel(lAssSocMod)); } // Ricerca Relazioni
	 * lTreeAttivita = addRelazioni(aKeyAttivita, lTreeAttivita);
	 * 
	 * // Il TreeModel Attività viene aggiunto a quello ricevuto come argomento aTree.add(lTreeAttivita); } //
	 * [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	 * LogF3B.getLogger() siesLogger.debug(getClass().getName() + ".addAttivita: fine"); return aTree; }
	 */
	/**
	 * Ricerca le Relazioni a partire dall'ID Attività passato come primo argomento. Per ognuna delle
	 * Relazioni rilevate, si costruisce il TreeModel ed aggiunto a quello passato come secondo argomento;
	 * questo ultimo costituisce anche il ritorno.
	 * <p>
	 * 
	 * @param aKeyAttivita
	 * @param aTree
	 * @return aTree
	 * @throws F3BException
	 */
	// 2007-03-14 Commentato poichè in fase di stampa non è necessario
	// inviare i BLOB. Pertanto si sostituisce con il metodo avente lo
	// stesso nome ma con implemntazione diversa.( Vedere il metodo sottostante ).
	/*
	 * private TreeModel addRelazioniXAttivita(BigDecimal aKeyAttivita, TreeModel aTree) throws F3BException {
	 * // Ricerca Relazioni IRelazione lCtrlRel = SIEPELookupRemote.getRelazioneRemote(); Vector lRelazioni =
	 * lCtrlRel.ExRicercaRelazioniByAttivita(aKeyAttivita);
	 * 
	 * if (lRelazioni.size() > 0) { Iterator lItRel = lRelazioni.iterator(); while (lItRel.hasNext()) {
	 * RelazioneModel lRelazione = (RelazioneModel) lItRel.next(); // Occorre rifare la ricerca puntuale per
	 * ricavare anche il BLOB RelazioneModel lRelazioneBlob =
	 * lCtrlRel.ExRicercaRelazioneByKey(lRelazione.getIdRelazione()); if (lRelazioneBlob != null) {
	 * lRelazione.setDocPerTrasferimento(lRelazioneBlob.getDocBlobOut()); aTree.add(new
	 * TreeModel(lRelazione)); } } } return aTree; }
	 */

	/**
	 * Ricerca le Relazioni a partire dall'ID Attività passato come primo argomento. Per ognuna delle
	 * Relazioni rilevate, si costruisce il TreeModel ed aggiunto a quello passato come secondo argomento;
	 * questo ultimo costituisce anche il ritorno.
	 * <p>
	 * 
	 * @param aKeyAttivita
	 * @param aTree
	 * @return aTree
	 * @throws F3BException
	 */
	private TreeModel addRelazioniXAttivita(BigDecimal aKeyAttivita, TreeModel aTree) throws F3BException {
		// Ricerca Relazioni per attività.
		IRelazione lCtrlRel = SIEPELookupRemote.getRelazioneRemote();
		Vector lRelazioni = lCtrlRel.ExRicercaRelazioniByAttivita(aKeyAttivita);

		if (lRelazioni.size() > 0) {
			Iterator lItRel = lRelazioni.iterator();
			while (lItRel.hasNext()) {
				RelazioneModel lRelazione = (RelazioneModel) lItRel.next();
				if (lRelazione != null)
					aTree.add(new TreeModel(lRelazione));
			}
		}

		return aTree;
	}

	/**
	 * Ricerca gli Esperti assegnati ad una specifica attività.
	 * <p>
	 * 
	 * @param aKeyAttivita
	 * @param aTree
	 * @return aTree
	 * @throws F3BException
	 */
	private TreeModel addEspertiXAttivita(BigDecimal aKeyAttivita, TreeModel aTree) throws F3BException {
		// Ricerca Esperti assegnati all'Attività

		// Chiama il controller che realizza la Ricerca
		IEspertoAttivita lCtrl = SIEPELookupRemote.getEspertoAttivitaRemote();
		Vector lEsperti = lCtrl.ExRicercaEspertiXAttivita(aKeyAttivita);

		if (lEsperti.size() > 0) {
			Iterator lItRel = lEsperti.iterator();
			while (lItRel.hasNext()) {
				EspertoAttivitaEspertoModel aEspertoAttivita = (EspertoAttivitaEspertoModel) lItRel.next();
				aTree.add(new TreeModel(aEspertoAttivita));
			}
		}
		return aTree;
	}

	/**
	 * Ricerca l'Assistente sociale assegnato ad una specifica attività.
	 * <p>
	 * 
	 * @param aKeyAssSoc
	 *            id dell'assistente siciale da cercare.
	 * @param aTree
	 *            TreModel al quale aggiungere l'entità Assistente sociale.
	 * @throws F3BException
	 *             propaga errore di eccezione
	 */
	private void addAssistenteSociale(BigDecimal aKeyAssSoc, TreeModel aTree) throws F3BException {
		if (aKeyAssSoc != null) {
			// Chiama il controller per il recupero dei dati dell'Assistente Sociale.
			IAssistenteSociale lCtrlAssSoc = SIEPELookupRemote.getAssistenteSocialeRemote();
			AssistenteSocialeModel lAssSocMod = lCtrlAssSoc.ExRicercaAssistenteSocialeByKey(aKeyAssSoc);
			// Costruzione del TreeModel
			aTree.add(new TreeModel(lAssSocMod));
		}
		// return aTree;
	}

	/**
	 * Ricerca le Relazioni a partire dall'ID Richiesta passato come primo argomento. Per ognuna delle
	 * Relazioni rilevate, si costruisce il TreeModel ed aggiunto a quello passato come secondo argomento;
	 * questo ultimo costituisce anche il ritorno.
	 * <p>
	 * 
	 * @param aKeyRichiesta
	 *            id della richiesta.
	 * @param aTree
	 *            Treemodel
	 * @return aTree Treemodel in uscita.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	// 2007-03-13 Commentato poichè in fase di stampa non è necessario
	// inviare i BLOB. Pertanto si sostituisce con il metodo avente lo
	// stesso nome ma con implementazione diversa.( Vedere il metodo sottostante ).
	/*
	 * private TreeModel addRelazioniXRichiesta(BigDecimal aKeyRichiesta, TreeModel aTree) throws F3BException
	 * { // Ricerca Relazioni IRelazione lCtrlRel = SIEPELookupRemote.getRelazioneRemote(); Vector lRelazioni
	 * = lCtrlRel.ExRicercaRelazioniByRichiesta(aKeyRichiesta); if (lRelazioni.size() > 0) { Iterator lItRel =
	 * lRelazioni.iterator(); while (lItRel.hasNext()) { RelazioneModel lRelazione = (RelazioneModel)
	 * lItRel.next(); // Occorre rifare la ricerca puntuale per ricavare anche il BLOB RelazioneModel
	 * lRelazioneBlob = lCtrlRel.ExRicercaRelazioneByKey(lRelazione.getIdRelazione()); if (lRelazioneBlob !=
	 * null) { lRelazione.setDocPerTrasferimento(lRelazioneBlob.getDocBlobOut()); aTree.add(new
	 * TreeModel(lRelazione)); } } } return aTree; }
	 */

	/**
	 * Ricerca le Relazioni a partire dall'ID Richiesta passato come primo argomento. Per ognuna delle
	 * Relazioni rilevate, si costruisce il TreeModel ed aggiunto a quello passato come secondo argomento;
	 * questo ultimo costituisce anche il ritorno.
	 * <p>
	 * 
	 * @param aKeyRichiesta
	 *            id della richiesta.
	 * @param aTree
	 *            Treemodel
	 * @return aTree Treemodel in uscita.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	private TreeModel addRelazioniXRichiesta(BigDecimal aKeyRichiesta, TreeModel aTree) throws F3BException {
		// Ricerca Relazioni
		IRelazione lCtrlRel = SIEPELookupRemote.getRelazioneRemote();
		Vector lRelazioni = lCtrlRel.ExRicercaRelazioniByRichiesta(aKeyRichiesta);

		if (lRelazioni.size() > 0) {
			Iterator lItRel = lRelazioni.iterator();
			while (lItRel.hasNext()) {
				RelazioneModel lRelazione = (RelazioneModel) lItRel.next();

				if (lRelazione != null)
					aTree.add(new TreeModel(lRelazione));
			}
		}

		return aTree;
	}

	/**
	 * Ricerca la Relazione a partire dal suo ID Relazione passato come primo argomento. Dopo la relazione, al
	 * TreeModel viene aggregata l'attività o la richiesta a seconda del Puntamento della Relazione stessa.
	 * Quindi, si costruisce il TreeModel ed aggiunto il tutto a quello passato come secondo argomento; questo
	 * ultimo costituisce anche il ritorno.
	 * <p>
	 * 
	 * @param aKey
	 *            id per la quale ricercare la relazione.
	 * @param aTree
	 *            Treemodel al quale aggiungere la relazione.
	 * @return aTree ritorna il treemodel.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	private TreeModel addRelazione(BigDecimal aKey, TreeModel aTree) throws F3BException {
		// Ricerca Relazione
		IRelazione lCtrlRel = SIEPELookupRemote.getRelazioneRemote();
		RelazioneModel lRelazione = lCtrlRel.ExRicercaRelazioneByKey(aKey);

		if (lRelazione != null) {
			Vector lRelazioni = new Vector();
			lRelazione.setDocPerTrasferimento(lRelazione.getDocBlobOut().toByteArray());
			// Si pongono a null i DocBlobIn e DocBlobOut, al fine di evitare
			// un errore di non Serializable.
			lRelazione.setDocBlobIn(null);
			lRelazione.setDocBlobOut(null);

			aTree.add(new TreeModel(lRelazione));

			// Ricerca Attivita/Richiesta, al fine di inserire
			// nel TreeModel allo stesso livello della relazione
			// l'attività o la richiesta.
			if (lRelazione.getAttIdAttivita() != null) {
				IAttivita lCtrlAtt = SIEPELookupRemote.getAttivitaRemote();
				AttivitaModel lAttivita = lCtrlAtt.ExRicercaAttivitaByKey(lRelazione.getAttIdAttivita());

				// Inserisce la relazione nel model Attività.
				lRelazioni.add(lRelazione);
				lAttivita.setRelazioni((RelazioneModel[]) lRelazioni.toArray(new RelazioneModel[0]));

				aTree.add(new TreeModel(lAttivita));
			} else if (lRelazione.getRicIdRichiesta() != null) {
				IRichiesta lCtrlRic = SIEPELookupRemote.getRichiestaRemote();
				RichiestaModel lRichiesta = lCtrlRic.ExRicercaRichiestaByKey(lRelazione.getRicIdRichiesta());

				// Inserisce la relazione nel model Richiesta.
				lRelazioni.add(lRelazione);
				lRichiesta.setRelazioni((RelazioneModel[]) lRelazioni.toArray(new RelazioneModel[0]));

				aTree.add(new TreeModel(lRichiesta));
			}
		}

		return aTree;
	}

	/**
	 * La funzione richiama la funzione che Elabora i dati per la stampa del Fascicolo Siep e li aggiunge al
	 * TreModel in costruzione.
	 * 
	 * @param aFasSiep
	 * @param aUtente
	 * @param aTree
	 * @return
	 * @throws F3BException
	 */
	private TreeModel addDatiSiep(FascicoloSiepModel aFasSiep, UtenteModel aUtente, TreeModel aTree)
			throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("####### addDatiSiep(); Inizio ###### ");

		// Ricerca dei dati del Fascicolo Siep
		IFascicoloSiepStampa lCtrStam = SIEPLookupRemote.getFascicoloSiepStampaRemote();
		TreeModel lTreeSiep = lCtrStam.prelevaDatiStampaFascicolo(aFasSiep, aUtente);

		// Si naviga il TreeModel risultato della Stampa Fascicolo
		if (lTreeSiep != null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("####### addDatiSiep(); aggiungi aTreeSiep ###### ");
			aTree.add(lTreeSiep);
		}
		return aTree;
	}

	/**
	 * Richiama una funzione dello StampaController di SIUS per costruire l'albero dei dati relativi
	 * all'Evento. Questo, se non nullo, viene aggiunto al TreeModel passato come parametro.
	 * <p>
	 * 
	 * @param aEvento
	 * @param aTree
	 * @return
	 * @throws F3BException
	 */
	private TreeModel addDatiEvento(EventoModel aEvento, TreeModel aTree) throws F3BException {
		// Ricerca dei dati relativi all'Evento
		IStampaSius lCtrStam = SIUSLookupRemote.getStampaRemote();
		TreeModel lTreeEvento = lCtrStam.ExPrelevaDatiEvento(aEvento);

		if (lTreeEvento != null)
			aTree.add(lTreeEvento);

		return aTree;
	}

	/**
	 * Recupera ed imposta nel FascicoloSiepeEstesoModel la sola entità evento.
	 * <p>
	 * 
	 * @param aFasEsteso
	 * @throws F3BException
	 *             Propaga l'errore di eccezione
	 */
	private void addEvento(FascicoloSiepeEstesoModel aFasEsteso) throws F3BException {
		if (aFasEsteso.getFascicoloSiepe().getEveIdEvento() != null) {
			IEvento lEvento = SICOLookupRemote.getEventoRemote();
			EventoModel lEventoModel = lEvento
					.ExRicercaEventoByKey(aFasEsteso.getFascicoloSiepe().getEveIdEvento());
			// Inserisce l'evento recuperato nel fascicolo siepe esteso.
			aFasEsteso.setEvento(lEventoModel);
		}
	}

	/**
	 * Recupera il deposito decreto, attraverso l'id evento, e aggiunge lo stesso nel tree model
	 * <p>
	 * 
	 * @param aEvento
	 *            Istanza di EventoModel
	 * @param aTree
	 *            Istanza di TreeModel
	 * @throws F3BException
	 *             propaga errori di eccezione
	 */
	private void addDepositoDecreto(EventoModel aEvento, TreeModel aTree) throws F3BException {
		DepositoDecretoModel lDepMod = null;

		// Dati di Deposito Decreto.
		if (aEvento.getCodTipoProvvedimento().compareTo("02") == 0) {
			// Si preferisce chiamare il Controller
			IDepositoDecreto lDepCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
			lDepMod = lDepCtrl.ExRicercaDepositoDecretoByIdEvento(aEvento.getIdEvento());
		}

		if (lDepMod != null)
			aTree.add(new TreeModel(lDepMod));
	}

}