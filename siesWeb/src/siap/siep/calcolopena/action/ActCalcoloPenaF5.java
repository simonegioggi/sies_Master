package siap.siep.calcolopena.action;

import org.apache.log4j.Logger;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
//import siap.siep.calcolopena.controller.ICalcoloPenaF5;
import siap.siep.calcolopena.model.CalcoloPenaModel;
//import siap.siep.calcolopena.model.EventiCalcoloPenaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
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
@SuppressWarnings("rawtypes")
public class ActCalcoloPenaF5 extends ActCalcoloPenaMain implements ICostantiCalcoloPena {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	// private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// BigDecimal lFascID=((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep();
		BigDecimal lFascID = null;
		BigDecimal lIdEvento = null;

		if (!this.isSessionAttributeNullObj("fascicolo")) {
			// Se provengo del Menù Scelta Rapida devo recuparere i dati dalla sessione
			// Cerco in sessione il fascicolo per recuperare l'id
			lFascID = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();
		} else {
			// Se non ho l'id fascicolo ne sulla request ne in sessione restituisco la
			// pagina di ricerca fascicolo
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO)) {
			lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		}

		// ==========================================================================
		// Recupero la PENA COMPLESSIVA per verificare se trattasi di ergastolo
		// ==========================================================================
		PenaComplessivaModel lPenComplMod = new PenaComplessivaModel();
		lPenComplMod.setFasSieIdFascicoloSiep(lFascID);
		IPenaComplessiva lPCon = SIEPLookupRemote.getPenaComplessivaRemote();
		Vector lPComples = lPCon.ExRicercaPenaComplessivaNoError(lPenComplMod);

		if (lPComples.size() == 0) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Pena Complessiva mancante");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
					+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
					+ ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}
		lPenComplMod = (PenaComplessivaModel) (lPComples.get(0));

		setRequestAttribute("lPenComplMod", lPenComplMod);

		// ==========================================================================
		// Recupero i dati della pena
		// ==========================================================================
		CalcoloPenaModel lCalcoloPenaMod = null;

		lCalcoloPenaMod = super.calcoloPena(lFascID, lIdEvento);

		Date lDataInizioPena = super.getDataDecorrenzaPena(lFascID, lIdEvento);
		setRequestAttribute("lCalcoloPenaMod", lCalcoloPenaMod);
		setRequestAttribute("lDataInizioPena", lDataInizioPena);

		// L'ultima pena residua è associata a una Interruzione/sospensione/
		// revoca... Passo alla form anche la pena da espiare a seguito interruzione
		// Quella associata alla sospensione

		// ==========================================================================
		// Recupero ultima pena validata se esiste per effettuare un confronto tra
		// quanto a sistema e quanto calcolato
		// n.b. solo se sto visualizzando la pena allo stato attuale (lIdEvento==null)
		// se invece sto visualizzando lo stato della pena a un certo istante
		// non effettuo controlli (PER ORA)
		// ==========================================================================
		if (lIdEvento == null) {
			IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
			PenaResiduaModel lUltimaPenResVal = new PenaResiduaModel();
			lUltimaPenResVal = lPenResCtrl.ExRicercaPenaResiduaUltimaByDate(lFascID);

			setRequestAttribute("lUltimaPenResVal", lUltimaPenResVal);
		}

		// ==========================================================================
		//
		// ==========================================================================
		// ICalcoloPenaF5 lCalcPenaF5 = SIEPLookupRemote.getCalcoloPenaF5();
		// Vector lEventi = lCalcPenaF5.exGetEventiPerCalcoloPena (lFascID);
		//
		// for (int i=0; i<lEventi.size();i++)
		// {
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug( lEventi.elementAt(i).toString() );
		// }

		return ICostantiCalcoloPena.PG_F5;
	}

}

