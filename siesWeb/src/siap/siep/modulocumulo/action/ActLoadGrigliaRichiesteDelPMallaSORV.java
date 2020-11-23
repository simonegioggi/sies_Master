package siap.siep.modulocumulo.action;

import java.util.Vector;

import f3b.util.F3BException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per il caricamento delle Griglia per la gestione delle Richieste del P.M. dell'Esecuzione agli
 * uffici di Sorveglianza (TDS e UDS)
 *
 * @author
 */
public class ActLoadGrigliaRichiesteDelPMallaSORV extends ActionModuloCumulo
		implements ICostantiModuloCumulo {

	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		// ==========================================================================
		// Recupero i dati del cumulo
		// ==========================================================================
		IstruttoriaCumuloModel lIstruttoriaModel = super.getDatiIstruttoria();

		IRichiestePmInCumulo lCtrlRic = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		Vector<RichiestePmInCumuloModel> VecRichiesteSORV = null;

		// Questa Query produce in Output un Elenco di richieste ; Il secondo parametro indica il
		// TipoRichiesta (al GE, alla SORV., ...); Il terzo parametro indica quali richieste cercare: tutte -
		// dainviare - inviate
		VecRichiesteSORV = lCtrlRic.ExRicercaRichiestePmInCumuloByIdIstruttoriaTipoRichiesta(
				lIstruttoriaModel.getIdIstruttoriaCumulo(), "02", "tutte");
		setRequestAttribute("ListaRichiesteSORV", VecRichiesteSORV);

		return PG_LOAD_GRIGLIA_RICHIESTE_PM_ALLA_SORV;
	}

}