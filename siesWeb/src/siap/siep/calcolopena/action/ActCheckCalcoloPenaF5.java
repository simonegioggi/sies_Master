package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.siep.calcolopena.controller.ICalcoloPenaF5;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.calcolopena.model.CheckCalcoloPenaModel;
//import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * Classe che ricostruisce i dati della pena un certo istante (evento), recuperando i dati che concorrono alla
 * determinazione della pena. Le modalità di recupero dei dati e di computo sono le stesse utilizzate in fase
 * di calcolo
 * 
 * @author
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActCheckCalcoloPenaF5 extends ActCalcoloPenaMain implements ICostantiCalcoloPena {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// ==========================================================================
		// Recupera dalla form i dati per selezionare i fascicoli su cui effettuare
		// i controlli
		// ==========================================================================
		FascicoloSiepModel lFascicoloPerRicerca = new FascicoloSiepModel();
		// lFascicoloPerRicerca.setFlagValidato("S");
		lFascicoloPerRicerca.setChiaveUfficio(getCodUfficioUtenteConnesso());
		String lChiaveUfficio = getCodUfficioUtenteConnesso();

		String lIscritto = "";
		if (getRequestStringParameter("TipoRicerca").equals("Tutti")) {
			lIscritto = "Tutti";
		} else if (getRequestStringParameter("TipoRicerca").equals("Migrati")) {
			lIscritto = "Migrati";
		} else if (getRequestStringParameter("TipoRicerca").equals("SIEP")) {
			lIscritto = "SIEP";
		}

		String lProgrAnno = "";
		for (int i = 1900; i < 2010; i++) {
			if (isRequestChecked(String.valueOf(i))) {
				lProgrAnno = lProgrAnno + "," + String.valueOf(i);
			}
		}

		if (lProgrAnno != null && lProgrAnno.length() > 0) {
			lProgrAnno = lProgrAnno.substring(1);
			lProgrAnno = "(" + lProgrAnno + ")";
		} else {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Selezionare almeno un Anno per il controllo");
			return IWebConstants.PG_MESSAGE;
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lChiaveUfficio = " + lChiaveUfficio);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lIscritto  = " + lIscritto);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lProgrAnno = " + lProgrAnno);

		// ==========================================================================
		// Seleziona l'elenco dei fascicoli che corrispondono ai criteri impostati
		// ==========================================================================
		ICalcoloPenaF5 lCtrlCalcoloPenaF5 = SIEPLookupRemote.getCalcoloPenaF5();
		Vector lListaFascicoli = lCtrlCalcoloPenaF5.exGetFascicoliPerCheckPena(lChiaveUfficio, lIscritto,
				lProgrAnno);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("Tot Fascicoli = " + lListaFascicoli.size());

		// ==========================================================================
		// Per ogni fascicolo selezionato effettua il calcolo della pena allo stato
		// attuale secondo i nuovi criteri (F5), recupera l'ultima pena residua
		// validata , inserendo il risultato in un vettore da passare alla form di
		// visualizzazione che effettuerà il confronto e la visualizzazione del risultato.
		// ==========================================================================
		Vector lListaCheckCalcoloPenaModel = new Vector();
		for (int i = 0; i < lListaFascicoli.size(); i++) {
			// if (i==1) break;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("i=" + i);

			FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) lListaFascicoli.elementAt(i);
			BigDecimal lFascID = lFascicoloModel.getIdFascicoloSiep();

			try {
				// ==========================================================================
				// Recupero la PENA COMPLESSIVA per verificare se trattasi di ergastolo
				// ==========================================================================
				PenaComplessivaModel lPenComplMod = new PenaComplessivaModel();
				lPenComplMod.setFasSieIdFascicoloSiep(lFascID);
				IPenaComplessiva lPCon = SIEPLookupRemote.getPenaComplessivaRemote();
				Vector lPComples = lPCon.ExRicercaPenaComplessivaNoError(lPenComplMod);
				if (lPComples.size() == 0) {
					// salto il fascicolo
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.error("Fascicolo " + lFascicoloModel.getChiaveAnno() + "/"
							+ lFascicoloModel.getChiaveProgr() + " scartato: manca pena complessiva");
					continue;
				}
				lPenComplMod = (PenaComplessivaModel) (lPComples.get(0));

				if (lPenComplMod.getCodTipoPenaDetentiva().equals("03")
						|| lPenComplMod.getCodTipoPenaDetentiva().equals("04")) {
					// non effettuo i controlli sugli ergastoli
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.error("Fascicolo " + lFascicoloModel.getChiaveAnno() + "/"
							+ lFascicoloModel.getChiaveProgr() + " scartato: ergastolo");
					continue;
				}

				// ==========================================================================
				// Recupero l'ultima pena validata per effettuare il confronto
				// ==========================================================================
				IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
				PenaResiduaModel lUltimaPenResVal = new PenaResiduaModel();
				lUltimaPenResVal = lPenResCtrl.ExRicercaPenaResiduaUltimaByDate(lFascID);

				if (lUltimaPenResVal == null || lUltimaPenResVal.getIdPenaResidua() == null) {
					// salto il fascicolo, non ho nulla con cui effettuare il confronto
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.error("Fascicolo " + lFascicoloModel.getChiaveAnno() + "/"
							+ lFascicoloModel.getChiaveProgr() + " scartato: no pena validata");
					continue;
				}

				// ==========================================================================
				// Recupero i dati della pena
				// ==========================================================================
				CalcoloPenaModel lCalcoloPenaMod = null;
				lCalcoloPenaMod = super.calcoloPena(lFascID, null);

				CheckCalcoloPenaModel lCheckModel = new CheckCalcoloPenaModel();
				lCheckModel.setFascicoloSiep(lFascicoloModel);
				lCheckModel.setCalcoloPenaModel(lCalcoloPenaMod);
				lCheckModel.setUltimaPenaValidata(lUltimaPenResVal);

				lListaCheckCalcoloPenaModel.add(lCheckModel);
			} catch (Exception e) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("Eccezione sul fascicolo: " + lFascID + " - "
						+ lFascicoloModel.getChiaveAnno() + "/" + lFascicoloModel.getChiaveProgr());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error(e.fillInStackTrace());
				CheckCalcoloPenaModel lCheckModel = new CheckCalcoloPenaModel();
				lListaCheckCalcoloPenaModel.add(lCheckModel);
				lCheckModel.setFascicoloSiep(lFascicoloModel);
				lCheckModel.setUltimaPenaValidata(null);
				lCheckModel.setErrCheck(true);
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lListaCheckCalcoloPenaModel.size() = " + lListaCheckCalcoloPenaModel.size());
		setRequestAttribute("ListaCheckCalcoloPenaModel", lListaCheckCalcoloPenaModel);

		// return "pippo";
		return IWebConstants.ROOT_DIR + "files/siap/siep/calcolopena/RisultatiCheckCalcoloPena.jsp";
	}

}