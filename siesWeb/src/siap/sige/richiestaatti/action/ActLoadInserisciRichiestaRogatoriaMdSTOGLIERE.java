package siap.sige.richiestaatti.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.avvocato.model.AvvocatoModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sige.avvocato.controller.IAvvocato;
import siap.sige.avvocato.model.AvvocatoFascicoloSigeModel;
import siap.sige.collegio.controller.ICollegio;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciRichistaRogatoriaMdS
 * </p>
 * <p>
 * Description: Classe di Azione responsabile della composizione dei dati per le combobox e ritorna la
 * chiamata alla corrispondente JSP.
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
public class ActLoadInserisciRichiestaRogatoriaMdSTOGLIERE extends ActRicercaFSigePuntuale
		implements ICostantiRichiestaAtti {

	protected FascicoloSigeEstesoModel mFasEsteso;

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Attivazione punto di Ritorno
		setLinkRitorno();

		// LISTA DESTINATARIO 1
		Collection lCol = DecodificheManager.getInstance().getTipoUfficio();
		String[] lStringFilter = { "-", "UDS" };
		Option lOption1 = new Option(lCol);
		lOption1.setFilter(lStringFilter);

		setRequestAttribute("autorita", "" + lOption1);

		ricercaAvvocatiMagistrato();

		// Si Imposta l'Ufficio Competente.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficio());
		// lOption.setFilter( new String[] {"-", "TDS", "UDS", "CAP", "CAS", "CASAP", "CAPMI", "CAPMID",
		// "CSS", "GIPMI", "GIP", "GIPM", "GP", "GUP", "GUPM", "GUPMI", "PT", "PM", "PMM", "PMPT", "PGCAP",
		// "PGMI", "PGMID", "PMI", "TRIBSD", "CAPSM", "TMI", "DIB", "DIBM"} ); //solo le Autorità Emittenti.
		lOption.setFilter(new String[] { "-", "CAP", "CAS", "CASAP", "CAPMI", "CAPMID", "CSS", "GIPMI", "GIP",
				"GIPM", "GP", "GUP", "GUPM", "GUPMI", "PT", "TRIBSD", "CAPSM", "TMI", "DIB", "DIBM" }); // solo
																										// le
																										// Autorità
																										// Emittenti.
		setRequestAttribute("tipoUfficioCompetente", "" + lOption);

		return PG_LOAD_RICHIESTA_ROGATORIA;

	}

	@SuppressWarnings("rawtypes")
	protected void ricercaAvvocatiMagistrato() throws Exception {
		// Ricerca Avvocati
		IAvvocato lCtrlAvv = SIGELookupRemote.getAvvocatoRemote();
		Vector lAvvocati = new Vector();

		BigDecimal lIdFascicolo = null;
		FascicoloSigeEstesoModel lFasEsteso = null;

		// if (!isRequestParameterNullObj(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE))
		// {
		// lIdFascicolo = getRequestBigDecimalParameter(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE);
		// }
		// else
		// {
		lFasEsteso = this.getFascicoloSigeEstesoInSessione();
		lIdFascicolo = lFasEsteso.getFascicoloSige().getIdFascicoloSige();
		// }

		AvvocatoModel lAvvMod = new AvvocatoModel();
		AvvocatoFascicoloSigeModel lAvvFascMod = new AvvocatoFascicoloSigeModel();
		// lAvvFascMod.setFasSigeIdFascicoloSige(mFasEsteso.getFascicoloSige().getIdFascicoloSige());
		lAvvFascMod.setFasSigeIdFascicoloSige(lIdFascicolo);
		lAvvocati = lCtrlAvv.ExRicercaDifensoreAttualiFascicolo(lAvvMod, lAvvFascMod);

		if (lAvvocati.size() > 0)
			setRequestAttribute("avvocato", lAvvocati);

		// Magistrato Assegnatario
		// MagistratoAssegnatarioModel lMagAss = mFasEsteso.getMagAssegnatario();
		MagistratoAssegnatarioModel lMagAss = lFasEsteso.getMagAssegnatario();
		setRequestAttribute("magistratoassegnatario", lMagAss);

		// Solo la prima volta vengono messi i Tenori in sessione
		if (isRequestParameterNullObj(IWebConstants.FLAG_RITORNO)) {
			// Ricerca tenori attivi
			TenoreSigeModel lTenore = new TenoreSigeModel();
			lTenore.setFasIdFascicoloSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
			ITenoreSige lTenCtrl = SIGELookupRemote.getTenoreSigeRemote();
			Vector lTenori = lTenCtrl.ExRicercaTenoriEstesiAttivi(lTenore);

			setSessionAttribute("tenori", lTenori);
		}

		// In caso di Udienza gia fissata si visualizzano i dati del collegio.
		// Eventuale lettura del collegio.
		CollegioModel lColMod = null;
		if (lFasEsteso.getUdienzaProcedimento() != null
				&& lFasEsteso.getUdienzaProcedimento().getUdiIdUdienzaSige() != null) {
			ICollegio lCtrl = SIGELookupRemote.getCollegioRemote();
			lColMod = lCtrl.ExRicercaCollegioByIdUdienzaSige(
					lFasEsteso.getUdienzaProcedimento().getUdiIdUdienzaSige());
		}
		setRequestAttribute("collegio", lColMod);
	}

}