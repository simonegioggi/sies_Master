package siap.sius.motivazionedecreto.action;

/**
* <p>Title: ActInserisciMotivazioneDecreto</p>
* <p>Description: Classe Action per l'inserimento di MotivazioneDecreto</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.action.ICostantiEvento;
import siap.sius.ActionSius;
import siap.sius.motivazionedecreto.controller.IMotivazioneDecreto;
import siap.sius.motivazionedecreto.model.MotivazioneDecretoModel;
import siap.sius.util.SIUSLookupRemote;

public class ActInserisciMotivazioneDecretoInammissibilita extends ActionSius
		implements ICostantiMotivazioneDecreto {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento del Motivazione Decreto Inammissibilità.
	 * <p>
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws Exception
	 *             propaga errore dei eccezione.
	 */
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		MotivazioneDecretoModel[] lMotivazioni = letturaMotivazioni();

		// Chiamata la controller.
		IMotivazioneDecreto lCtrl = SIUSLookupRemote.getMotivazioneDecretoRemote();
		lCtrl.ExInserisciMotivazioniDecreto(lMotivazioni);

		// Prepara la pagina di destinazione, in questo caso
		// è il dettaglio del decreto d'inammissibilità.
		RedirectTo lRedirectTo = new RedirectTo();
		lRedirectTo.setPage(IWebConstants.PG_MAIN);
		lRedirectTo.setAction("siap.sius.depositodecreto.action.ActLoadDettaglioDecretoInammissibilita");
		lRedirectTo.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, lIdEvento.toString());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return lRedirectTo.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected MotivazioneDecretoModel[] letturaMotivazioni() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".letturaMotivazioni: inizio");

		Vector lMotivazioni = new Vector();
		BigDecimal lDepDecr = null;
		if (!isRequestParameterNullObj(CAMPO_DEP_DEC_ID_DEPOSITO_DECRETO))
			lDepDecr = getRequestBigDecimalParameter(CAMPO_DEP_DEC_ID_DEPOSITO_DECRETO);

		MotivazioneDecretoModel lMotMod = new MotivazioneDecretoModel();

		lMotMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lMotMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lMotMod.setDataInserimento(DateUtils.getSysDate());
		lMotMod.setDepDecIdDepositoDecreto(lDepDecr);

		if (isRequestChecked(CAMPO_CK_01)) {
			String[] lMotiviValues = getRequestStringParameters(CAMPO_CK_01);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("N.ro Motivi inseriti: " + lMotiviValues.length);

			for (int i = 0; i < lMotiviValues.length; i++) {
				String lCodMotivo = lMotiviValues[i];

				MotivazioneDecretoModel lMotMod1 = new MotivazioneDecretoModel(lMotMod);
				lMotMod1.setCodTipoMotivazione(lCodMotivo);
				String lNomeCampo1 = CAMPO_DESCR_MOTIVAZIONE + lCodMotivo;
				String lNomeCampo2 = CAMPO_ALTRA_MOTIVAZIONE + lCodMotivo;

				if (!isRequestParameterNullObj(lNomeCampo1))
					lMotMod1.setDescrMotivazione(getRequestStringParameter(lNomeCampo1));
				if (!isRequestParameterNullObj(lNomeCampo2))
					lMotMod1.setAltraMotivazione(getRequestStringParameter(lNomeCampo2));
				lMotivazioni.add(lMotMod1);
			}
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".letturaMotivazioni: fine");
		return (MotivazioneDecretoModel[]) lMotivazioni.toArray(new MotivazioneDecretoModel[0]);
	}

}