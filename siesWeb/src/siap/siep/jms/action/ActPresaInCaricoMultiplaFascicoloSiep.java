package siap.siep.jms.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import f3b.util.xml.TreeModel;
import f3b.web.IWebConstants;
import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.messaggio.model.RootJMSModel;
import siap.jms.util.ParserMessage;
import siap.sico.SICOException;
import siap.sico.lock.model.LockModel;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.jms.controller.IPresaInCaricoJMS;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActPresaInCaricoMultiplaFascicoloSiep - Azione che prende in carico piu' fascicoli di entità passate
 * tramite la struttura JMS da altre BDI
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActPresaInCaricoMultiplaFascicoloSiep extends ActionSiap implements ICostantiJMS {

	public String processRequest() throws Exception {

		// Controllo che non si stia lavorando su una entità in modifica ad altri
		LockModel lck = lockIfNotLocked("caricofascicolimul",
				getRequestStringParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO), getCodUtenteConnesso());
		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"La presa in carico di questo soggetto è in gestione ad un altro utente! <BR>Riprovare più tardi!");
			return IWebConstants.PG_MESSAGE;
		}
		BigDecimal lIdMess = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMess);

		if (lMess.getCodBdiDestinataria().equals(lMess.getCodBdiMittente())) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Procedimento richiesto è di un ufficio di questa BDI. Il trasferimento è completato!");
			return IWebConstants.PG_MESSAGE;
		}

		if (!this.isRequestParameterNullObj("Selection")) {
			MessaggioModel lMessReturn = null;

			// --------WHILE SUI DETTAGLI DEL FASCICOLO SIEP
			ParserMessage lParser = new ParserMessage(lMess.getTreeModel());
			Vector lFascicoli = lParser.getSoggetto().getDettaglioFascicoli();

			// 06/06/2005 Il vettore di fascicoli va individuato!
			BigDecimal lIdSoggetto = getRequestBigDecimalParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO);
			for (int jj = 0; jj < lParser.getSoggettoArray().size(); jj++) {
				if (((SoggettoModel) lParser.getSoggettoArray().get(jj)).getIdSoggetto()
						.compareTo(lIdSoggetto) == 0)
					lFascicoli = ((SoggettoModel) lParser.getSoggettoArray().get(jj)).getDettaglioFascicoli();
			}

			Vector fascicoli = new Vector();

			// 08/02/2008 Rivisto il ciclo che in precedenza considerava solo il primo fascicolo del gruppo.
			String[] lRadio = new String[fascicoli.size()];
			lRadio = this.getRequestStringParameters("Selection");

			Iterator lItxFasci = lFascicoli.iterator();

			int k = 0;
			while (lItxFasci.hasNext() && k < lRadio.length) {
				DettaglioFascicoloModel lDett = (DettaglioFascicoloModel) lItxFasci.next();

				if (lDett != null && lRadio[k].compareTo("on") == 0) {
					TreeModel lTreeRoot = new TreeModel(createRoot());
					lTreeRoot.add(new TreeModel(lDett));

					lMess.setTreeModel(lTreeRoot);

					IPresaInCaricoJMS lPres = SIEPLookupRemote.getPresaInCarico();
					lMessReturn = lPres.ExInserisciFascicoloSiep(lMess);
				}
				fascicoli.add(lDett.getFascicoloSiep());
				k++;
			}
			this.setRequestAttribute("fascicoli", fascicoli);
			this.setRequestAttribute("Messaggio", lMessReturn);
			this.setRequestAttribute("Selection", this.getRequestStringParameters("Selection"));
			this.setRequestAttribute("dettFascicoli", lFascicoli);

			return IWebConstants.ROOT_DIR + "files/siap/siep/jms/RapportoTrasferimentoFascicoloMultiplo.jsp";
		} else {
			throw new SICOException(SICOException.USER_MESSAGE, "Nessun Fascicolo selezionato da importare");
		}
	}

	/**
	 * Metodo privato per la creazione della root del messaggio da elaborare
	 *
	 * @return
	 */
	private RootJMSModel createRoot() {

		RootJMSModel aModel = new RootJMSModel();

		aModel.setCodTipoMessaggio("04");
		aModel.setCodTipoOperazione("00030");
		aModel.setDescrTipoMessaggio("RICHIESTA RICERCA");
		aModel.setDescrTipoOperazione("RICERCA FASCICOLO");
		aModel.setEsito("TROVATO");
		return aModel;
	}

}