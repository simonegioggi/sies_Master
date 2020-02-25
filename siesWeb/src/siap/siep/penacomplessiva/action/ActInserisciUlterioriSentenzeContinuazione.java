package siap.siep.penacomplessiva.action;

/**
* <p>Title: ActInserisciPenaComplessiva</p>
* <p>Description: Classe Action per l'inserimento di PenaComplessiva</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import siap.sico.web.ActionSiap;
import siap.siep.continuazione.action.ICostantiContinuazione;
import siap.siep.continuazione.model.ContinuazioneModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciUlterioriSentenzeContinuazione extends ActionSiap
		implements ICostantiPenaComplessiva {

	/**
	 * Azione di Inserimento del PenaComplessiva
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_PENA_COMPLESSIVA);

		// CONTINUAZIONE CON ALTRI REATI
		List lList = new ArrayList();

		String[] lTipoContinuazione = getRequestStringParameters(
				ICostantiContinuazione.CAMPO_COD_TIPO_CONTINUAZIONE);

		String[] lAnnoSentenza = getRequestStringParameters(ICostantiContinuazione.CAMPO_ANNO_SENTENZA);
		String[] lNumSentenza = getRequestStringParameters(ICostantiContinuazione.CAMPO_NUM_SENTENZA);

		String[] lGiornoDataSentenza = getRequestStringParameters(
				ICostantiContinuazione.CAMPO_GIORNO_DATA_SENTENZA);
		String[] lMeseDataSentenza = getRequestStringParameters(
				ICostantiContinuazione.CAMPO_MESE_DATA_SENTENZA);
		String[] lAnnoDataSentenza = getRequestStringParameters(
				ICostantiContinuazione.CAMPO_ANNO_DATA_SENTENZA);

		String[] lTipoUfficio = getRequestStringParameters(ICostantiContinuazione.CAMPO_COD_TIPO_AUTORITA);
		String[] lLuogoUfficio = getRequestStringParameters(ICostantiContinuazione.CAMPO_COD_LUOGO_AUTORITA);

		String[] lAnnoRegePM = getRequestStringParameters(ICostantiContinuazione.CAMPO_ANNO_REGE_PM);
		String[] lNumRegePM = getRequestStringParameters(ICostantiContinuazione.CAMPO_NUM_REGE_PM);

		String[] lAnnoRegeGenerico = getRequestStringParameters("ARG");
		String[] lNumRegeGenerico = getRequestStringParameters("NRG");
		String[] TipoRGGenerico = getRequestStringParameters("TipoRG");

		ContinuazioneModel lCont = null;
		// Viene considerata valida una sentenza in continuazione
		// se ha almeno o
		// l'anno e il numero sentenza
		// o la data sentenza
		// valorizzati
		for (int i = 0; i < 3; i++) {
			if (lAnnoSentenza[i] != null && !lAnnoSentenza[i].equals("") && lNumSentenza[i] != null
					&& !lNumSentenza[i].equals("")) {
				lCont = new ContinuazioneModel();

				lCont.setPenComIdPenaComplessiva(lId);

				lCont.setCodTipoContinuazione(lTipoContinuazione[i]);

				if (lAnnoSentenza[i] != null && !lAnnoSentenza[i].equals(""))
					lCont.setAnnoSentenza(new BigDecimal(lAnnoSentenza[i]));
				lCont.setNumSentenza(lNumSentenza[i]);

				lCont.setDataSentenza(DateUtils.getDate(lAnnoDataSentenza[i], lMeseDataSentenza[i],
						lGiornoDataSentenza[i]));

				lCont.setCodTipoAutorita(lTipoUfficio[i]);
				lCont.setCodLuogoAutorita(getCodComuneByDescr(lLuogoUfficio[i]).getCodComune());

				// Controllo esistenza ufficio
				if (lTipoUfficio[i] != null && !lTipoUfficio[i].equals("") && !lTipoUfficio[i].equals("-")
						&& lLuogoUfficio[i] != null && !lLuogoUfficio[i].equals("")
						&& !lLuogoUfficio[i].equals("-")) {
					getCodUfficioByCodTipoUfficioDescrComune(lTipoUfficio[i], lLuogoUfficio[i]);
				}

				if (lAnnoRegePM[i] != null && !lAnnoRegePM[i].equals(""))
					lCont.setAnnoRegePm(new BigDecimal(lAnnoRegePM[i]));
				lCont.setNumRegePm(lNumRegePM[i]);

				if (TipoRGGenerico[i].equalsIgnoreCase("gip")) {
					if (lAnnoRegeGenerico[i] != null && !lAnnoRegeGenerico[i].equals("")) {
						lCont.setAnnoRegeGip(new BigDecimal(lAnnoRegeGenerico[i]));
					}
					lCont.setNumRegeGip(lNumRegeGenerico[i]);
				}
				if (TipoRGGenerico[i].equalsIgnoreCase("dib")) {
					if (lAnnoRegeGenerico[i] != null && !lAnnoRegeGenerico[i].equals("")) {
						lCont.setAnnoRegeDib(new BigDecimal(lAnnoRegeGenerico[i]));
					}
					lCont.setNumRegeDib(lNumRegeGenerico[i]);
				}
				if (TipoRGGenerico[i].equalsIgnoreCase("cas")) {
					if (lAnnoRegeGenerico[i] != null && !lAnnoRegeGenerico[i].equals("")) {
						lCont.setAnnoRegeCas(new BigDecimal(lAnnoRegeGenerico[i]));
					}
					lCont.setNumRegeCas(lNumRegeGenerico[i]);
				}
				if (TipoRGGenerico[i].equalsIgnoreCase("cap")) {
					if (lAnnoRegeGenerico[i] != null && !lAnnoRegeGenerico[i].equals("")) {
						lCont.setAnnoRegeCap(new BigDecimal(lAnnoRegeGenerico[i]));
					}
					lCont.setNumRegeCap(lNumRegeGenerico[i]);
				}
				if (TipoRGGenerico[i].equalsIgnoreCase("casap")) {
					if (lAnnoRegeGenerico[i] != null && !lAnnoRegeGenerico[i].equals("")) {
						lCont.setAnnoRegeCasap(new BigDecimal(lAnnoRegeGenerico[i]));
					}
					lCont.setNumRegeCasap(lNumRegeGenerico[i]);
				}

				lCont.setCodOperatoreInserimento(getCodUtenteConnesso());
				lCont.setDataInserimento(DateUtils.getSysDate());
				lCont.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

				lList.add(lCont);
			}
		}

		IPenaComplessiva lCtrl = SIEPLookupRemote.getPenaComplessivaRemote();

		lCtrl.ExInserisciUlterioriContinuazioni(lList);

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.penacomplessiva.action.ActLoadDettaglioPenaComplessiva&"
				+ CAMPO_ID_PENA_COMPLESSIVA + "=" + lId.toString();

		return lPage;
	}

}