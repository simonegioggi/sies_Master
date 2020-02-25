package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.siep.SIEPException;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la load Inserimento richiesta al GE di Sostituzione di Pena Accessoria
 *
 * @author
 *
 */
public class ActLoadInsRichiestaGESostPA extends ActionModuloCumulo implements ICostantiRichiestePmInCumulo {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		IstruttoriaCumuloModel lIstrCumulo = super.getDatiIstruttoria();
		RichiestePmInCumuloModel lRicMod = null;

		String lPage = "";
		String lModalita = "I"; // default inserimento

		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		if ("I".equals(lModalita)) {
			// Inserimento
			this.getTitoliPAScelti(lIstrCumulo);
			lPage = PG_INS_RICH_GE_SOST_PA;
		} else if ("M".equals(lModalita)) {
			// Modifica
			BigDecimal aIdRich = new BigDecimal(getRequestStringParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO));
			IRichiestePmInCumulo lCtrlRich = SIEPLookupRemote.getRichiestePmInCumuloRemote();
			lRicMod = lCtrlRich.ExRicercaRichiestePmInCumuloById(aIdRich);

			setRequestAttribute("RichiestaAlGE", lRicMod);

			// Ricerca del Titolo e delle Pene Accessoria collegate alla Richiesta (tramite tabelle di
			// Relazione RICHPM_TITOLO_CUM e RICHPM_PENACC_CUM)
			TitoloCumulatoModel lTitoloMod = lCtrlRich.ExRicercaTitolo_e_PeneAccCumByRichiestaGE(aIdRich);

			setRequestAttribute("TitoloListaPA", lTitoloMod);

			lPage = PG_INS_RICH_GE_SOST_PA;
		} else {
			// Rilanciare Eccezione - Operazione non supportata
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Modalità operazione sconosciuta. Impossibile eseguire la richiesta.");
		}

		// combo Tipo P.A.
		Option lOption = new Option(DecodificheUtils
				.getDecodesWithoutCode(DecodificheManager.getInstance().getTipoPeneAccessorie(), "999"));
		if (lRicMod != null && lRicMod.getIdRichiestePmInCumulo() != null
				&& lRicMod.getCodTipoPenaAccessoria() != null) {
			lOption.setSelected(lRicMod.getCodTipoPenaAccessoria());
		}
		setRequestAttribute("TipoPenaAccessoria", "" + lOption);

		// Tipo Durata P.A.
		Option lOptionDur = new Option(DecodificheManager.getInstance().getDurataPeneAccessorie(), "-");
		if (lRicMod != null && lRicMod.getIdRichiestePmInCumulo() != null
				&& lRicMod.getCodTipoDurataPa() != null) {
			lOptionDur.setSelected(lRicMod.getCodTipoDurataPa());
		}
		setRequestAttribute("DurataPenaAcc", "" + lOptionDur);

		// Indulto/Amnistia
		Option lOptionTipoAnn = new Option(
				DecodificheManager.getInstance().getTipoAnnotazioneManualeBenefici());
		if (lRicMod != null && lRicMod.getCodTipoBeneficio() != null) {
			lOptionTipoAnn.setSelected(lRicMod.getCodTipoBeneficio());
		} else {
			lOptionTipoAnn.setSelected("002"); // Indulto
		}
		setRequestAttribute("TipoAnnotazioneManuale", "" + lOptionTipoAnn);

		// DPR
		// Se Inserimento, --> posizionamento combo DPR all'ultimo elemento. / Se Modifica, --> posizionamento
		// su Elemento Trovato
		Vector lVect = (Vector) DecodificheManager.getInstance().getDPR();
		DecodificheModel lDecMod = (DecodificheModel) lVect.lastElement();
		Option lOptionDPR = new Option(lVect);
		if ("M".equals(lModalita)) {
			if (lRicMod != null && lRicMod.getIdRichiestePmInCumulo() != null
					&& lRicMod.getCodDpr() != null) {
				lOptionDPR.setSelected(lRicMod.getCodDpr());
			}
		} else {
			lOptionDPR.setSelected(lDecMod.getCode());
		}
		setRequestAttribute("listaDPR", "" + lOptionDPR);

		setRequestAttribute("modalita", lModalita);

		return lPage;

	} // Chiude processRequest()

	private void getTitoliPAScelti(IstruttoriaCumuloModel aIstruttoriaModel) throws F3BException {
		// LogF3B.getLogger().debug("--XX-- Inserimento - Start getTitoliBeneficiScelti");
		String aIdTitoloCumulato = getRequestStringParameter(
				ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);
		Vector<String> listaIdPA = new Vector<>();

		ITitoloCumulato lCtrlT = SIEPLookupRemote.getTitoloCumulatoRemote();

		// Lista dei check Selezionati dall'Utente:
		String[] lIdTitoliSelezionati = null;
		if (!isRequestParameterNullObj(CAMPO_ID_TITOLO_PA_SELEZIONATI))
			lIdTitoliSelezionati = getRequestStringParameters(CAMPO_ID_TITOLO_PA_SELEZIONATI);

		String Ele = "";
		for (int i = 0; i < lIdTitoliSelezionati.length; i++) {
			Ele = lIdTitoliSelezionati[i];
			String[] EleSpl = Ele.split(";");

			listaIdPA.add(EleSpl[1]);
		}

		TitoloCumulatoModel lTitolo = lCtrlT
				.ExRicercaTitoloCumulatoPeneAccessorieCum(new BigDecimal(aIdTitoloCumulato), listaIdPA);

		setRequestAttribute("TitoloListaPA", lTitolo);
	}

} // Chiude Classe()
