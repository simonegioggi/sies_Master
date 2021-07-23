package siap.sige.avvocato.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.avvocato.model.AvvocatoModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.sige.avvocato.controller.IAvvocato;
import siap.sige.avvocato.model.AvvocatoFascicoloSigeModel;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.sentenza.controller.IFasSigeSentenza;
import siap.sige.util.SIGELookupRemote;
import siap.web.ISIAPCostantiWeb;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadInserisciAssegnaAvvocato extends ActionSiap
		implements ICostantiAvvocato, ICostantiFascicoloSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		IAvvocato lCtrl = SIGELookupRemote.getAvvocatoRemote();
		if (!isRequestParameterNullObj("numeroDifensori")
				&& getRequestStringParameter("numeroDifensori").equals("2"))
			throw new F3BException(F3BException.USER_MESSAGE,
					"Attenzione: Sono già assegnati due difensori!");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" tipoDifensore ");

		if (!isRequestParameterNullObj(CAMPO_ID_AVVOCATO)) {
			String lAvvId = getRequestStringParameter(CAMPO_ID_AVVOCATO);

			AvvocatoModel lAvv = lCtrl.ExRicercaAvvocatoByKey(new BigDecimal(lAvvId));
			setRequestAttribute("avvocato", lAvv);

			if (isSessionAttributeNullObj("FascicoloSigeEsteso")) {
				FascicoloSigeEstesoModel fascicoloModel = ricercaFascicolo();
				setSessionAttribute("FascicoloSigeEsteso", fascicoloModel);
				BigDecimal id_fascicolo = (((FascicoloSigeEstesoModel) getSessionAttribute(
						"FascicoloSigeEsteso")).getFascicoloSige().getIdFascicoloSige());
				int avvocati = countAvvocatiAssegnati(id_fascicolo);

				if (avvocati >= 2)
					throw new F3BException(F3BException.USER_MESSAGE,
							"Attenzione: Sono già assegnati due difensori!");
			}
		}

		if (!isRequestParameterNullObj("tipoDifensore") && (getRequestStringParameter("tipoDifensore")
				.equalsIgnoreCase("D'UFFICIO")
				|| getRequestStringParameter("tipoDifensore").equalsIgnoreCase("DELLA FASE DI GIUDIZIO")))
			throw new F3BException(F3BException.USER_MESSAGE,
					"I difensori possono essere due solo se entrambi sono di fiducia!");

		Option lOption = new Option(DecodificheManager.getInstance().getTipoAvvocato());
		String[] lFilter = { "-", "01", "02" };
		lOption.setFilter(lFilter);
		setRequestAttribute("tipoAvvocato", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("autoritaEsterna", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaDif", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getMotivoDesignazione());
		setRequestAttribute("motivoDesignazione", "" + lOption);

		setRequestAttribute("modalita", "I");

		if (!isRequestParameterNullObj(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE))
			setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE,
					getRequestStringParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE));

		gestioneRitorno();

		// 19/03/2010 Nuova gestione Combo per Foro avvocato.
		//// IAvvocato lCtrl = SIGELookupRemote.getAvvocatoRemote();
		// Vector lVect = lCtrl.ExRicercaForo();
		// setRequestAttribute("foro", lVect);

		UfficioModel lUffUte = getUfficioUtenteConnesso();
		String lDescrComune = lUffUte.getDescrComune();
		setRequestAttribute("comune", lDescrComune);

		// 19/03/2010 Nuova gestione Combo per Foro avvocato.
		lOption = new Option(DecodificheManager.getInstance().getForo(), lDescrComune.toUpperCase().trim(),
				Option.NO_BLANK_ITEM);
		setRequestAttribute("foro", "" + lOption);

		// MEV_21 Nuova gestione Combo per Stato di Nascita
		lOption = new Option(DecodificheManager.getInstance().getNazioni(), "-");
		setRequestAttribute("nazione", "" + lOption);

		// MEV_21 Nuova gestione Combo per Stato Difensore
		lOption = new Option(DecodificheManager.getInstance().getListaAttivitaAvvocato(), "-");
		setRequestAttribute("statoAvv", "" + lOption);

		// Ricerca Sentenze assegnate al Fascicolo (Ulteriori Titoli Esecutivi)
		AvvocatoFascicoloSigeModel lAvvFascMod = new AvvocatoFascicoloSigeModel();
		lAvvFascMod.setFasSigeIdFascicoloSige(
				((FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso")).getFascicoloSige()
						.getIdFascicoloSige());
		BigDecimal lId = lAvvFascMod.getFasSigeIdFascicoloSige();
		IFasSigeSentenza lFasSenCtrl = SIGELookupRemote.getFasSigeSentenzaRemote();
		Vector lSentenze = lFasSenCtrl.ExRicercaSentenzeAssegnateFascicolo(lId);
		setRequestAttribute("sentenze", lSentenze);

		return PG_LOAD_INSERISCIAVVOCATO; // restituisce la jsp di VIEW
	}

	private FascicoloSigeEstesoModel ricercaFascicolo() throws F3BException {

		FascicoloSigeModel lFascicolo = new FascicoloSigeModel();
		lFascicolo.setChiaveAnno(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO));
		lFascicolo.setChiaveProgr(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR));

		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		String strDescrComune = getUfficioUtenteConnesso().getDescrComune();

		String lCodUfficio = getCodUfficioByCodTipoUfficioDescrComune(strCodTipoUfficio, strDescrComune);
		lFascicolo.setChiaveUfficio(lCodUfficio);
		IFascicoloSige lCtrl = SIGELookupRemote.getFascicoloSigeRemote();
		FascicoloSigeEstesoModel lFasEst = lCtrl.ExRicercaFascicoloSigeByAnnoNumCodUfficio(lFascicolo);
		lFascicolo = lFasEst.getFascicoloSige();
		return lFasEst;
	}

	private int countAvvocatiAssegnati(BigDecimal idFascicolo) throws F3BException {

		IAvvocato lCtrlAvv = SIGELookupRemote.getAvvocatoRemote();
		ArrayList<AvvocatoModel> lVect = lCtrlAvv.ExRicercaAvvocatiByFascicoloNoError(idFascicolo);
		return lVect.size();
	}

}