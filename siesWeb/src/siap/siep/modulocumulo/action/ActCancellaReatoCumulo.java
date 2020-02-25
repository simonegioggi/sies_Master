package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.siep.modulocumulo.controller.IReatoCumulo;
import siap.siep.modulocumulo.model.ReatoCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action che si occupa di effettuare la cancellazione logica o fisica di un reato
 */
public class ActCancellaReatoCumulo extends ActionModuloCumulo implements ICostantiReatoCumulo {

	public String processRequest() throws Exception {

		// BigDecimal lIdIstruttoria = getRequestBigDecimalParameter(
		// ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
		// BigDecimal lIdTitolo = getRequestBigDecimalParameter(
		// ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);

		BigDecimal lIdReatoCumulo = getRequestBigDecimalParameter(ICostantiReatoCumulo.CAMPO_ID_REATO_CUM);

		IReatoCumulo lReaCtrl = SIEPLookupRemote.getReatoCumuloRemote();
		ReatoCumuloModel lReato = lReaCtrl.ExRicercaReatoCumuloByKey(lIdReatoCumulo);

		lReato.setFlagStato(getRequestStringParameter(CAMPO_FLAG_STATO));
		lReato.setMotivoModificaNote(getRequestStringParameter(ICostantiReatoCumulo.CAMPO_MOTIVO_MODIFICA));

		lReato.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lReato.setDataAggiornamento(DateUtils.getSysDate());
		lReato.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());

		lReaCtrl.ExCancellaReatoCumulo(lReato);

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.modulocumulo.action.ActRicercaReatoCumulo";

		return lPage;
	}

}