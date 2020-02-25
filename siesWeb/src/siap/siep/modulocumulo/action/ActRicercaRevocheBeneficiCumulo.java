package siap.siep.modulocumulo.action;

import java.util.Vector;

import f3b.util.F3BException;
import siap.siep.modulocumulo.controller.IBeneficioCumulo;
import siap.siep.modulocumulo.model.BeneficioCumuloModel;
import siap.siep.util.SIEPLookupRemote;

public class ActRicercaRevocheBeneficiCumulo extends ActionModuloCumulo implements ICostantiBeneficiCumulo {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		// ==========================================================================
		// Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
		// di DettaglioTitoloCumulato.jsp
		// ==========================================================================
		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		// ==============================================================
		// Effettuo la ricerca dei benefici collegati al Cumulo
		// ==============================================================
		BeneficioCumuloModel lBenMod = new BeneficioCumuloModel();
		lBenMod.setTitIdTitoloCumulato(
				getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));

		lBenMod.setCodNaturaBeneficio("R"); // 'R' Cerca solo le 'REVOCHE'

		IBeneficioCumulo lCtrl = SIEPLookupRemote.getBeneficioCumuloRemote();
		Vector lVect = lCtrl.ExRicercaBeneficioCumulo(lBenMod);

		// TODO implementare la ricerca by TitidTitolo
		setRequestAttribute("ListaRevocheBenefici", lVect);

		return PG_ELENCO_REVOCHE_BENEFICI_CUMULO;
	}

}