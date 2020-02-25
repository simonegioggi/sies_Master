package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.evento.controller.IEvento;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;

/**
 * Action per il caricamento delle Griglia della Richieste Isctruttorie
 *
 * @author
 */
public class ActLoadGrigliaRichiesteIstruttorie extends ActionModuloCumulo implements ICostantiModuloCumulo {
	/**
	 * Può essere invocata direttamente dal menù verticale o dalle funzioni di dettaglio, in questo caso viene
	 * passato l'id dell'istruttoria su cui si sta lavorando
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		setRequestAttribute("fascicolo", lFascicoloModel);
		// ==========================================================================
		// Recupero i dati del cumulo
		// ==========================================================================
		super.getDatiIstruttoria();

		BigDecimal IdIstru = this
				.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		String[] lTipoEvento = { "05" };

		// Vector lVect =
		// lCtrl.ExRicercaEventoNotificaByFascicoloSiepChiaveUfficio(lFascicoloModel.getIdFascicoloSiep(),
		// lFascicoloModel.getChiaveUfficio(), lTipoEvento);
		Vector lVect = lCtrl.ExRicercaEventoByTipoEveKeyIstruttoriaCumulo(IdIstru, lTipoEvento);
		setRequestAttribute("ListaRichieste", lVect);

		return PG_LOAD_GRIGLIA_RICH_ISTRUTTORIE;
	}

}