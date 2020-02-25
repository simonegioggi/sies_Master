package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.siep.modulocumulo.controller.ICircostanzaCumulo;
import siap.siep.modulocumulo.model.CircostanzaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActCancellaCircostanzaCumulo
 * </p>
 * <p>
 * Description: Classe Action che si occupa di effettuare la cancellazione logica o fisica
 * </p>
 * <p>
 * di Circostanza_Cumulo (Aggravanti/Attenuati relative al titolo Cumulato)
 * </p>
 */
public class ActCancellaCircostanzaCumulo extends ActionModuloCumulo implements ICostantiCircostanzaCumulo {

	public String processRequest() throws Exception {

		// BigDecimal lIdIstruttoria = getRequestBigDecimalParameter(
		// ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
		// BigDecimal lIdTitolo = getRequestBigDecimalParameter(
		// ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);

		BigDecimal lIdCirco = getRequestBigDecimalParameter(CAMPO_ID_CIRCOSTANZA_CUMULO);

		ICircostanzaCumulo lCtrl = SIEPLookupRemote.getCircostanzaCumuloRemote();
		CircostanzaCumuloModel mlCirMod = lCtrl.ExRicercaCircostanzaCumuloByKey(lIdCirco);

		// String lFlag = getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_FLAG_STATO);
		mlCirMod.setFlagStato(getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_FLAG_STATO));

		// String lMotivo = getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_MOTIVO_MODIFICA);
		mlCirMod.setMotivoModifica(getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_MOTIVO_MODIFICA));

		mlCirMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		mlCirMod.setDataAggiornamento(DateUtils.getSysDate());
		mlCirMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());

		lCtrl.ExCancellaCircostanzaCumulo(mlCirMod);

		String lPage = "";
		// lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
		// "=siap.siep.modulocumulo.action.ActRicercaCircostanzaCumulo";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.modulocumulo.action.ActRicercaReatoCumulo";

		return lPage;
	}

}