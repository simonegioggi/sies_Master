package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.siep.SIEPException;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la load Inserimento e Modifica delle Richieste del PM al GE di Applicazione Pena Eccessoria -
 * (Gestione Cumulo)
 *
 * @author Intersistemi Italia S.p.A.
 */

public class ActLoadInsRichiestaGEApplicaPA extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		IstruttoriaCumuloModel lIstrCumulo = super.getDatiIstruttoria();

		if (!ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA.equals(lIstrCumulo.getFlagStato())) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"L'istruttoria risulta chiusa. Non è possibile procedere all'emissione di ulteriori richieste");
			return IWebConstants.PG_MESSAGE;
		}

		RichiestePmInCumuloModel lRicMod = null;

		String lPage = "";
		String lModalita = "I"; // default inserimento
		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		if ("I".equals(lModalita)) {
			// Inserimento
			lPage = PG_INS_RICH_GE_APP_PA;
		} else if ("M".equals(lModalita)) {
			// Modifica
			BigDecimal lIdRich = getRequestBigDecimalParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO);
			IRichiestePmInCumulo lCtrlRich = SIEPLookupRemote.getRichiestePmInCumuloRemote();
			lRicMod = lCtrlRich.ExRicercaRichiestePmInCumuloById(lIdRich);
			setRequestAttribute("RichiestaAlGE", lRicMod);

			// Ricerca del Titolo collegato alla Richiesta (tramite tabelle di Relazione RICHPM_TITOLO_CUM )
			TitoloCumulatoModel lTitoloMod = lCtrlRich.ExRicercaTitolo_ByRichiestaGE(lIdRich);
			setRequestAttribute("TitoloPA", lTitoloMod);

			lPage = PG_MOD_RICH_GE_APP_PA;

		} else {
			// Rilanciare Eccezione - Operazione non supportata
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Modalità operazione sconosciuta. Impossibile eseguire la richiesta.");
		}

		// ================================
		// combo per Tipo Annotazione
		Option lOptionTipo = new Option(DecodificheManager.getInstance().getTipoAnnotazioneManualeTutte());
		lOptionTipo.setFilter(new String[] { "029", "030", "-" });
		if (lRicMod != null && lRicMod.getIdRichiestePmInCumulo() != null) {
			if (lRicMod.getCodTipoAnnotazione() != null) {
				lOptionTipo.setSelected(lRicMod.getCodTipoAnnotazione());
			}
		}
		setRequestAttribute("TipoAnnotazione", "" + lOptionTipo);

		// Combo prt Tipo Pena Accessoria
		Option lOption = new Option(DecodificheUtils
				.getDecodesWithoutCode(DecodificheManager.getInstance().getTipoPeneAccessorie(), "999"));
		if (lRicMod != null && lRicMod.getIdRichiestePmInCumulo() != null) {
			if (lRicMod.getCodTipoPenaAccessoria() != null) {
				lOption.setSelected(lRicMod.getCodTipoPenaAccessoria());
			}
		}
		setRequestAttribute("TipoPenaAccessoria", "" + lOption);

		// combo Indulto/Amnistia
		Option lOptionTipoAnn = new Option(
				DecodificheManager.getInstance().getTipoAnnotazioneManualeBenefici());
		if (lRicMod != null && lRicMod.getCodTipoBeneficio() != null) {
			lOptionTipoAnn.setSelected(lRicMod.getCodTipoBeneficio());
		} else {
			lOptionTipoAnn.setSelected("002"); // Indulto
		}
		setRequestAttribute("TipoAnnotazioneIndulto", "" + lOptionTipoAnn);

		// Tipo Durata P.A.
		Option lOptionDur = new Option(DecodificheManager.getInstance().getDurataPeneAccessorie(), "-");
		if (lRicMod != null && lRicMod.getIdRichiestePmInCumulo() != null
				&& lRicMod.getCodTipoDurataPa() != null) {
			lOptionDur.setSelected(lRicMod.getCodTipoDurataPa());
		}
		setRequestAttribute("DurataPenaAcc", "" + lOptionDur);

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

		setRequestAttribute("modalita", lModalita);

		return lPage;
	}
}
