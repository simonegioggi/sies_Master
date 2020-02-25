package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import f3b.util.F3BException;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.model.RichiesteInviateCumModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe Action per il caricamento del dettaglio della richiesta Emessa del PM al GE
 * 
 * @author Intersistemi Italia S.p.A.
 *
 */
public class ActDettaglioRichiestaDelPMEmessa extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {

	public String processRequest() throws F3BException {

		/* IstruttoriaCumuloModel lIstrCumulo = */super.getDatiIstruttoria();

		// cerco la Richiesta
		BigDecimal aIdRich = new BigDecimal(getRequestStringParameter(CAMPO_ID_RICHIESTA_INVIATA_CUM));
		IRichiestePmInCumulo lCtrlRich = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		RichiesteInviateCumModel lRicMod = (RichiesteInviateCumModel) lCtrlRich
				.ExRicercaRichiesteInviateCumuloById(aIdRich);

		setRequestAttribute("RichiestaInviata", lRicMod);

		// Magistrato
		MagistratoModel lMagMod = null;
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		if (lRicMod != null && lRicMod.getIdRichiesteInviateCum() != null
				&& lRicMod.getCodMagistrato() != null) {
			lMagMod = (MagistratoModel) lCtrlM.ExRicercaMagistratoByCod(lRicMod.getCodMagistrato());
		}

		setRequestAttribute("Magistrato", lMagMod);

		/*
		 * N.B. La lista con tutte le singole Richieste che riguardano questa Emissione è aggregata al
		 * RichiesteInviateCumModel
		 */

		return PG_DETT_RICHIESTA_EMESSA;
	}

}