package siap.siep.archiviazione.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActAggiornaAssorCumuloAltro
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di Evento
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActAggiornaAssorCumuloAltro extends ActArchiviazione implements ICostantiEvento {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		// EVENTO
		BigDecimal lIdEvento = getRequestBigDecimalParameter(CAMPO_ID_EVENTO);
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEveMod = lCtrl.ExRicercaEventoByKey(lIdEvento);

		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		Date lDataEmissione = lEveMod.getDataTrasmissioneAtti();
		ArrayList lNotifiche = new ArrayList();

		// SETTO NOTIFICA ALTRA AUTORITA
		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C)
				&& this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C) != null
				&& !this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C)
						.equals("-"))

		{
			String lPolizia = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C);
			String lSedePolizia = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C);
			NotificaModel lNotModPol = new NotificaModel();
			if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_C)) {
				String lNotePolizia = this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_C);
				lNotModPol.setNote(lNotePolizia);
			}
			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(lCodiceUfficio);
			lNotModPol.setCodTipoNotifica("C");
			lNotModPol.setDataInvio(lDataEmissione);
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(lPolizia);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedePolizia));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setAutoritaEsterna(lAut);
			lNotModPol.setEveIdEvento(lIdEvento);

			lNotifiche.add(lNotModPol);

		}

		// SETTO NOTIFICA ALTRA AUTORITA TESTO LIBERO
		if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE)
				&& this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE) != null
				&& !this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE).equals("")) {

			NotificaModel lNotMod = new NotificaModel();
			lNotMod.setNote(this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE));

			lNotMod.setCodEsito("-");
			lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
			lNotMod.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setCodUfficioInserimento(lCodiceUfficio);
			lNotMod.setCodTipoNotifica("C");
			lNotMod.setDataInvio(lDataEmissione);
			lNotMod.setEveIdEvento(lIdEvento);
			lNotifiche.add(lNotMod);
		}

		// inserimento
		INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
		lCtrlNot.ExInserisciNotifiche(lNotifiche);

		String lPage = null;

		if (!this.isRequestParameterNullObj("tipobottone")
				&& this.getRequestStringParameter("tipobottone") != null
				&& this.getRequestStringParameter("tipobottone").equals("altro")) {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.archiviazione.action.ActLoadDettaglioAssorCumuloAltro&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lIdEvento;
		} else {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.archiviazione.action.ActLoadDettaglioAssorCumulo&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lIdEvento;
		}

		return lPage;
	}

}