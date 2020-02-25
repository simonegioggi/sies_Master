package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.modulocumulo.controller.IPenaAccessoriaCumulo;
import siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * @author d.fiorletta
 */
public class ActRicercaPeneAccessorieCumulo extends ActionModuloCumulo
		implements ICostantiPenaAccessoriaCumulo {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		// ==========================================================================
		// Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
		// di DettaglioTitoloCumulato.jsp
		// ==========================================================================
		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		// ==========================================================================
		// Effettua la ricerca delle Pena Accessorie Presenti Associate al Titolo
		// n.b. cancellare o commentare i campi non presenti nella form
		// ==========================================================================
		BigDecimal lIdTitolo = getRequestBigDecimalParameter(
				ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);
		PenaAccessoriaCumuloModel lPenMod = new PenaAccessoriaCumuloModel();
		lPenMod.setTitIdTitoloCumulato(lIdTitolo);

		// =========================================================
		// Istanzio il controller ed effettuo la ricerca semplice
		// =========================================================
		IPenaAccessoriaCumulo lCtrl = SIEPLookupRemote.getPenaAccessoriaCumuloRemote();
		Vector lVect = lCtrl.ExRicercaPenaAccessoriaCumulo(lPenMod);

		setRequestAttribute("ListaPeneAccessorie", lVect);

		return PG_ELENCO_PENEACCESSORIE_CUMULO;
	}

}