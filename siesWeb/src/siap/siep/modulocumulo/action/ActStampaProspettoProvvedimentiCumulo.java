package siap.siep.modulocumulo.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per il caricamento della stampa del prospetto dei titoli cumulati nella Isctruttoria
 *
 */

public class ActStampaProspettoProvvedimentiCumulo extends ActionModuloCumulo
		implements ICostantiModuloCumulo {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdIstruttoriaCumulo = this
				.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);

		// ==========================================================================
		// Recupero l'istruttoria
		// ==========================================================================
		IstruttoriaCumuloModel lIstruttoriaModel = super.getDatiIstruttoria();

		if (!ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA.equals(lIstruttoriaModel.getFlagStato())) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"L'istruttoria corrente non risulta Aperta. Non è possibile generare i Prospetti.");
		}

		String lOrdinamento = ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_DESC;

		if (lIstruttoriaModel != null && lIstruttoriaModel.getOrdinamentoTitoli() != null
				&& !lIstruttoriaModel.getOrdinamentoTitoli().equals("")) {
			// Nessun ordinamento selezionato, verificaìo se presente sul record ISTRUTTORIA_CUMULO
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Nessun ordinamento selezionato");

			lOrdinamento = lIstruttoriaModel.getOrdinamentoTitoli();
		}

		// Ricerco i titoli
		IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
		Vector lListaTitoli = lIstrCtrl.ExRicercaTitoliValidiByIstruttoriaOrderBy(lIdIstruttoriaCumulo,
				lOrdinamento);

		UtenteModel lUtenteMod = this.getUtenteConnesso();

		IUfficio ctrlU = SICOLookupRemote.getUfficioRemote();
		UfficioModel lUfficioMod = ctrlU.getUfficioByKey(lIstruttoriaModel.getChiaveUfficio());

		TemplateModel lTemMod = new TemplateModel();
		ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();

		lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(null, null, "C999", null);

		ByteArrayOutputStream lReport = lIstrCtrl.ExStampaProspettoTitoliCumulo(lIstruttoriaModel,
				lListaTitoli, lFascicoloModel, lUtenteMod, lUfficioMod, lTemMod.getIdTemplate());

		// Prepara la pagina di destinazione
		setRequestAttribute("report", lReport);

		return IWebConstants.PG_DOWNLOAD;
	}
}