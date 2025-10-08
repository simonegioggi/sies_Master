package siap.siep.nuovaistanza.action;

import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.nuovaistanza.controller.INuovaIstanza;
import siap.siep.nuovaistanza.model.NuovaIstanzaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActModificaNuovaIstanza - Classe Action per la modifica di NuovaIstanza
 *
 * @version 5.0
 */
public class ActModificaNuovaIstanza extends ActionNuovaIstanza implements ICostantiNuovaIstanza {

	public String processRequest() throws Exception {

		INuovaIstanza lCtrl = SIEPLookupRemote.getNuovaIstanzaRemote();
		NuovaIstanzaModel lNuoMod = lCtrl
				.ExRicercaNuovaIstanzaById(this.getRequestBigDecimalParameter(CAMPO_ID_NUOVA_ISTANZA));

		NuovaIstanzaModel lNuoModel = getModificaNuovaIstanza(lNuoMod);

		// 20210825 MEV_21 In modifica Istanza si aggiorna l'Avvocato solo con un nuovo Avvocato
		if ((this.getRequestBigDecimalParameter(ICostantiNuovaIstanza.CAMPO_AVV_ID_AVVOCATO) == null)
				|| (this.getRequestBigDecimalParameter(ICostantiNuovaIstanza.CAMPO_AVV_ID_AVVOCATO) != null
						&& this.getRequestStringParameter(ICostantiAvvocato.CAMPO_ID_AVVOCATO).length() > 1
						&& this.getRequestStringParameter(ICostantiNuovaIstanza.CAMPO_AVV_ID_AVVOCATO) != this
								.getRequestStringParameter(ICostantiAvvocato.CAMPO_ID_AVVOCATO)))
			lNuoMod.setAvvIdAvvocato(getIdAvvocatoInserito());

		// 20210825 MEV_21 In modifica Istanza si aggiorna l'Avvocato presentante solo con un nuovo Avvocato
		// Presentante
		if ("D".equals(this.getRequestStringParameter(ICostantiNuovaIstanza.CAMPO_FLAG_PRESDEP))) {
			if ((this.getRequestBigDecimalParameter(
					ICostantiNuovaIstanza.CAMPO_AVV_ID_AVVOCATO_PRESENTANTE) == null)
					|| (this.getRequestBigDecimalParameter(
							ICostantiNuovaIstanza.CAMPO_AVV_ID_AVVOCATO_PRESENTANTE) != null
							&& this.getRequestStringParameter(ICostantiAvvocato.CAMPO_ID_AVVOCATO_P)
									.length() > 1
							&& this.getRequestStringParameter(
									ICostantiNuovaIstanza.CAMPO_AVV_ID_AVVOCATO_PRESENTANTE) != this
											.getRequestStringParameter(
													ICostantiAvvocato.CAMPO_ID_AVVOCATO_P)))
				lNuoMod.setAvvIdAvvocatoPresentante(getIdAvvocatoPresInserito());
		} else {
			lNuoMod.setAvvIdAvvocatoPresentante(null);
		}
		// modifica istanza
		lCtrl.ExModificaNuovaIstanza(lNuoModel);

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.nuovaistanza.action.ActLoadDettaglioNuovaIstanza";
		lPage += "&" + ICostantiEvento.CAMPO_ID_EVENTO + "=" + lNuoMod.getEveIdEvento().toString();

		return lPage;
	}

}