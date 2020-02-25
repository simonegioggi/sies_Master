package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel;
import siap.siep.modulocumulo.model.RichPMTitoloCumModel;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe Action per il Load della Form di Inserimento della decisione del G.E. a fronte di una Richiesta del
 * P.M. di: Revoca Pena Principale Revoca Sanzione Sostitutiva
 * 
 * @author Intersistemi Italia S.p.A.
 *
 */
public class ActLoadInserisciDecisioneDelGERevocaPenaPrincCum extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {

	public String processRequest() throws F3BException {

		super.getDatiIstruttoria();

		String lModalita = "I"; // default inserimento
		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		// cerco la Richiesta
		BigDecimal aIdRich = new BigDecimal(getRequestStringParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO));
		IRichiestePmInCumulo lCtrlRich = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		RichiestePmInCumuloModel lRicMod = lCtrlRich.ExRicercaRichiestePmInCumuloById(aIdRich);

		setRequestAttribute("RichiestaGE", lRicMod);

		if (lRicMod.getDecisioneGeSorvCum() != null
				&& lRicMod.getDecisioneGeSorvCum().getIdProvvedimentoGeSorvCum() != null) {
			lModalita = "M";
			setRequestAttribute("ProvvGECum", lRicMod.getDecisioneGeSorvCum());
		}

		setRequestAttribute("modalita", lModalita);

		// Ricerca dei Titoli collegati alla Richiesta (tramite tabella di Relazione RICHPM_TITOLO_CUM)
		Vector<RichPMTitoloCumModel> VecRicPM = new Vector<>();
		VecRicPM = lCtrlRich.ExRicercaRichPMTitoliCum(aIdRich);

		// Ricerca completa dei dati Aggregati ai Titoli coinvolti nella Richiesta (Reati_Cumulo)
		Vector<TitoloCumulatoModel> VecTitoli = new Vector<>();
		VecTitoli = lCtrlRich.ExRicercaAltriDatiRichiestaGE(VecRicPM);

		setRequestAttribute("TitoliRichiesta", VecTitoli);

		// ==========================================================================
		//
		// ==========================================================================
		// Ufficio giudice dell'esecuzione Emittente
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

		return PG_INSMOD_DEC_GE_REVOCA_PENA_PRINC;
	}

}