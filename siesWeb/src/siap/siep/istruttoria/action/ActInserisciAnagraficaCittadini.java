package siap.siep.istruttoria.action;

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.notifica.model.NotificaModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInserisciAnagrafica
 * </p>
 * <p>
 * Description: ActInserisciAnagrafica
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciAnagraficaCittadini extends ActionSiap implements ICostantiIstruttoria {

	public String processRequest() throws Exception {

		EventoNotificaModel lEve = new EventoNotificaModel();

		lEve.getEvento().setCodTipoEvento("05"); // Tipo Evento = RIchiesta Istruttoria
		lEve.getEvento().setCodTipoProvvedimento("-"); // Tipo Provvedimento = Ordinanza

		String lFlagAccertmento = getRequestStringParameter("tipo");

		if (lFlagAccertmento.equals("R")) {
			lEve.getEvento().setCodMotivo("0054");
		} else if (lFlagAccertmento.equals("AR")) {
			lEve.getEvento().setCodMotivo("0579");
		} else if (lFlagAccertmento.equals("RI")) {
			lEve.getEvento().setCodMotivo("0580");
		}

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		lEve.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEve.getEvento().setDataEmissione(lDataEmissione);

		UfficioModel lUff = getUfficioUtenteConnesso();

		lEve.getEvento().setCodOperatoreInserimento(getCodUtenteConnesso());

		lEve.getEvento().setCodLuogoEmittente(lUff.getCodComune());
		lEve.getEvento().setCodUfficioEmittente(lUff.getCodUfficio());
		lEve.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEve.getEvento().setCodUfficioInserimento(lUff.getCodUfficio());
		lEve.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEve.getEvento().setCodEsito("-");
		lEve.getEvento().setCodLuogoDestinatario("-");
		lEve.getEvento().setCodTipoUfficioDestinatario("-");

		// Se vengo da IstruttoriaCUMULO
		if (!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)
				&& getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) != null
				&& !getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO).equals(
						"")) {
			lEve.getEvento()
					.setIstruidIstruttoriaCumulo(
							new BigDecimal(
									getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)));
		}

		NotificaModel lNotifiche[] = new NotificaModel[1];
		NotificaModel lNot = new NotificaModel();

		lNot.setCodTipoNotifica("N");
		lNot.setDataInvio(lDataEmissione);
		lNot.setCodEsito("-");
		lNot.setCodOperatoreInserimento(getCodUtenteConnesso());
		lNot.setDataInserimento(DateUtils.getSysDate());
		lNot.setCodUfficioInserimento(lUff.getCodUfficio());

		AutoritaEsternaModel lAutEstMod = new AutoritaEsternaModel();

		if (lFlagAccertmento.equals("R") || lFlagAccertmento.equals("AR")) {
			lAutEstMod.setCodTipoAutorita("-");
			ComuneModel lCom = getCodComuneByDescrFlagVal(getRequestStringParameter(ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO));
			lAutEstMod.setCodSede(lCom.getCodComune());

		} else {
			lAutEstMod.setCodTipoAutorita(getRequestStringParameter(AUTORITA));
			ComuneModel lCom = getCodComuneByDescrFlagVal(getRequestStringParameter(ICostantiIstruttoria.SEDE_AUTORITA));
			lAutEstMod.setCodSede(lCom.getCodComune());
		}
		lAutEstMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAutEstMod.setDataInserimento(DateUtils.getSysDate());
		lAutEstMod.setCodUfficioInserimento(lUff.getCodUfficio());
		lNot.setAutoritaEsterna(lAutEstMod);

		if (!isRequestParameterNullObj(ICostantiIstruttoria.CAMPO_NOTE)) {
			lNot.setNote(getRequestStringParameter(ICostantiIstruttoria.CAMPO_NOTE));
		}

		lNotifiche[0] = lNot;

		lEve.setNotifiche(lNotifiche);

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lRetModel = lCtrl.ExInserisciEventoNotifica(lEve);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.istruttoria.action.ActDettaglioAnagraficaCittadini&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento() + "&modalita=I";

		return lPage;
	}

}