package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.siep.SIEPException;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la load di Inserimento/Modifica della richiesta al GE di Revoca Pena Accessoria
 *
 * @author InterSistemiItalia S.p.A.
 *
 */
public class ActLoadInsRichiestaGERevocaPA extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {
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
			lPage = PG_INS_RICH_GE_REV_PA;
		} else if ("M".equals(lModalita)) {
			// Modifica
			BigDecimal aIdRich = new BigDecimal(getRequestStringParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO));
			IRichiestePmInCumulo lCtrlRich = SIEPLookupRemote.getRichiestePmInCumuloRemote();
			lRicMod = lCtrlRich.ExRicercaRichiestePmInCumuloById(aIdRich);

			setRequestAttribute("RichiestaAlGE", lRicMod);

			Vector<TitoloCumulatoModel> lVecTitoliPA = new Vector<>();
			// Ricerca dei Titoli e delle Pene Accessoria collegate alla Richiesta (tramite tabelle di
			// Relazione RICHPM_TITOLO_CUM e RICHPM_PENACC_CUM)
			lVecTitoliPA = lCtrlRich.ExRicercaTitoli_e_PeneAccCumByRichiestaGE(aIdRich);

			setRequestAttribute("ListaTitoliPA", lVecTitoliPA);

			lPage = PG_MOD_RICH_GE_REV_PA;
		} else {
			// Rilanciare Eccezione - Operazione non supportata
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Modalità operazione sconosciuta. Impossibile eseguire la richiesta.");
		}

		// combo per Tipo Annotazione
		Option lOptionTipo = new Option(DecodificheManager.getInstance().getTipoAnnotazioneManualeTutte());
		lOptionTipo.setFilter(new String[] { "026", "027", "028", "-" });
		if (lRicMod != null && lRicMod.getIdRichiestePmInCumulo() != null) {
			if (lRicMod.getCodTipoAnnotazione() != null) {
				lOptionTipo.setSelected(lRicMod.getCodTipoAnnotazione());
			}
		}
		setRequestAttribute("TipoAnnotazione", "" + lOptionTipo);

		// combo Indulto/Amnistia
		Option lOptionTipoAnn = new Option(
				DecodificheManager.getInstance().getTipoAnnotazioneManualeBenefici());
		if (lRicMod != null && lRicMod.getCodTipoBeneficio() != null) {
			lOptionTipoAnn.setSelected(lRicMod.getCodTipoBeneficio());
		} else {
			lOptionTipoAnn.setSelected("002"); // Indulto
		}
		setRequestAttribute("TipoAnnotazioneIndu", "" + lOptionTipoAnn);

		// combo DPR
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

		// Combo per Depenalizzazione : Tipo fonte leg.
		Option lOptionF = new Option(DecodificheManager.getInstance().getTipoFonteReato());
		if (lRicMod != null && lRicMod.getIdRichiestePmInCumulo() != null) {
			if (lRicMod.getCodFonte() != null) {
				lOptionF.setSelected(lRicMod.getCodFonte());
			}
		}
		setRequestAttribute("TipiFontiReato", "" + lOptionF);

		// Combo per Depenalizzazione : Tipo sottonumerzione Bis, Ter...
		Option lOptionS = new Option(DecodificheManager.getInstance().getSottonumerazione());
		if (lRicMod != null && lRicMod.getIdRichiestePmInCumulo() != null) {
			if (lRicMod.getCodSottonumerazione() != null) {
				lOptionS.setSelected(lRicMod.getCodSottonumerazione());
			}
		}
		setRequestAttribute("TipiSottonumerazione", "" + lOptionS);

		setRequestAttribute("modalita", lModalita);

		return lPage;
	}

	private void getTitoliPAScelti(IstruttoriaCumuloModel aIstruttoriaModel) throws F3BException {
		Vector<TitoloCumulatoModel> lVecTitoli = null;

		ITitoloCumulato lCtrlT = SIEPLookupRemote.getTitoloCumulatoRemote();

		// Lista dei check Selezionati dall'Utente:
		String[] lIdTitoliSelezionati = null;
		if (!isRequestParameterNullObj(CAMPO_ID_TITOLO_PA_SELEZIONATI))
			lIdTitoliSelezionati = getRequestStringParameters(CAMPO_ID_TITOLO_PA_SELEZIONATI);

		lVecTitoli = lCtrlT.ExRicercaTitoliCumulatiPeneAccessorieCum(lIdTitoliSelezionati);
		setRequestAttribute("ListaTitoliPA", lVecTitoli);

	}

}
