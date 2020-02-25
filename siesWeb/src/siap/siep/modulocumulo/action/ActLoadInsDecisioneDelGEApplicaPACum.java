package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe Action per il Load della Form di Inserimento della decisione del G.E. a fronte di una Richiesta del
 * P.M. di: Applicazione Pena Accessoria (gestione Cumulo)
 * 
 * @author Intersistemi Italia S.p.A.
 *
 */
public class ActLoadInsDecisioneDelGEApplicaPACum extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {

	public String processRequest() throws F3BException {

		/* IstruttoriaCumuloModel lIstrCumulo = */super.getDatiIstruttoria();

		String lModalita = "I"; // default inserimento
		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		// cerco la Richiesta
		BigDecimal aIdRich = new BigDecimal(getRequestStringParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO));
		IRichiestePmInCumulo lCtrlRich = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		RichiestePmInCumuloModel lRicMod = lCtrlRich.ExRicercaRichiestePmInCumuloById(aIdRich);
		setRequestAttribute("RichiestaGE", lRicMod);

		// Ricerca del Titolo collegato alla Richiesta (tramite tabelle di Relazione RICHPM_TITOLO_CUM )
		TitoloCumulatoModel lTitoloMod = lCtrlRich.ExRicercaTitolo_ByRichiestaGE(aIdRich);
		setRequestAttribute("TitoloPA", lTitoloMod);

		// Distinzione tra INSERIMENTO e MODIFICA Decisione del GE
		if (lRicMod.getDecisioneGeSorvCum() != null
				&& lRicMod.getDecisioneGeSorvCum().getIdProvvedimentoGeSorvCum() != null) {
			lModalita = "M";
			setRequestAttribute("ProvvGECum", lRicMod.getDecisioneGeSorvCum());
		}

		setRequestAttribute("modalita", lModalita);

		// Combo: Ufficio giudice dell'esecuzione Emittente
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioPerCodice());
		lOption.setFilter(new String[] { "CAP", "CAS", "CASAP", "CSS", "GIP", "GIPM", "GUPM", "GUP", "TRIBSD",
				"CAPSM", "DIB", "DIBM", "-" });
		if (lRicMod != null && lRicMod.getDecisioneGeSorvCum() != null) {
			ProvvedimentoGeSorvCumModel lProvvMod = lRicMod.getDecisioneGeSorvCum();
			if (lProvvMod.getCodUfficioEmittente() != null) {
				UfficioModel lUffMod = this.getUfficioByCodUfficio(lProvvMod.getCodUfficioEmittente());
				lOption.setSelected(lUffMod.getCodTipoUfficio());
			}
		}

		setRequestAttribute("UfficioEmittente", "" + lOption);

		// combo Tipo P.A.
		Option lOptionPA = new Option(DecodificheUtils
				.getDecodesWithoutCode(DecodificheManager.getInstance().getTipoPeneAccessorie(), "999"));
		if (lRicMod.getDecisioneGeSorvCum() != null
				&& lRicMod.getDecisioneGeSorvCum().getCodTipoPenaAccessoriaD() != null) {
			lOptionPA.setSelected(lRicMod.getDecisioneGeSorvCum().getCodTipoPenaAccessoriaD());
		}
		setRequestAttribute("TipoPenaAccessoria_D", "" + lOptionPA);

		// Tipo Durata P.A.
		Option lOptionDur = new Option(DecodificheManager.getInstance().getDurataPeneAccessorie(), "-");
		if (lRicMod.getDecisioneGeSorvCum() != null
				&& lRicMod.getDecisioneGeSorvCum().getCodTipoDurataPaD() != null) {
			lOptionDur.setSelected(lRicMod.getDecisioneGeSorvCum().getCodTipoDurataPaD());
		}
		setRequestAttribute("DurataPenaAcc_D", "" + lOptionDur);

		return PG_INSMOD_DEC_GE_APPL_PENA_ACC;
	}
}
