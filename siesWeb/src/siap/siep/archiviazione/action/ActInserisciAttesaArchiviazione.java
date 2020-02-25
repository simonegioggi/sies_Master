package siap.siep.archiviazione.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInserisciAttesaArchiviazione
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di Evento
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciAttesaArchiviazione extends ActionSiap
		implements ICostantiArchiviazione, ICostantiEvento {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// EVENTO
		EventoNotificaModel lEveNotMod = new EventoNotificaModel();
		EventoModel lEveMod = new EventoModel();

		lEveMod.setCodTipoEvento("01");
		lEveMod.setCodTipoProvvedimento("25");
		lEveMod.setCodMotivo(getRequestStringParameter(CAMPO_COD_OGGETTO_DEFINIZIONE));
		lEveMod.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_DEFINIZIONE,
				CAMPO_MESE_DATA_DEFINIZIONE, CAMPO_GIORNO_DATA_DEFINIZIONE));
		lEveMod.setDataTrasmissioneAtti(DateUtils.getSysDate());

		lEveMod.setCodUfficioEmittente(this.getCodUfficioUtenteConnesso());
		lEveMod.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());

		lEveMod.setCodTipoUfficioDestinatario("-");
		lEveMod.setCodLuogoDestinatario("-");
		lEveMod.setCodUfficioDestinatario("-");
		lEveMod.setCodEsito("-");

		lEveMod.setCodMagistrato(calcolaMagistrato());

		lEveMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		lEveMod.setFlagVideoSiep("S");
		lEveMod.setFlagStampaSiep("S");

		lEveMod.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEveMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lEveMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lEveMod.setDataInserimento(DateUtils.getSysDate());
		lEveNotMod.setEvento(lEveMod);

		// setto il campo note
		CampoNotaModel lCamNot = new CampoNotaModel();
		lCamNot.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lCamNot.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lCamNot.setDataInserimento(DateUtils.getSysDate());
		lCamNot.setFasSieIdFascicoloSiep(lIdFascicolo);
		if (getRequestStringParameter(CAMPO_NOTE) != null
				&& !getRequestStringParameter(CAMPO_NOTE).equals(""))
			lCamNot.setDescr(getRequestStringParameter(CAMPO_NOTE));
		else
			lCamNot.setDescr("-");

		lCamNot.setProgressivo(new BigDecimal(1));

		Vector lCampNote = new Vector();
		lCampNote.add(lCamNot);
		lEveNotMod.setCampoNote((CampoNotaModel[]) lCampNote.toArray(new CampoNotaModel[0]));

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveRet = lCtrl.ExInserisciEventoNotifica(lEveNotMod);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.archiviazione.action.ActLoadDettaglioAttesaArchiviazione&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEveRet.getEvento().getIdEvento();

		return lPage;
	}

	/**
	 * calcolaMagistrato
	 * 
	 * @return
	 */
	protected String calcolaMagistrato() throws F3BException {

		String lCodiceMagistrato = getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO);

		if (lCodiceMagistrato.compareTo("") == 0) {
			MagistratoModel lMagMod = new MagistratoModel();
			lMagMod.setCognome(getRequestStringParameter(ICostantiMagistrato.CAMPO_COGNOME).toUpperCase());
			lMagMod.setNome(getRequestStringParameter(ICostantiMagistrato.CAMPO_NOME).toUpperCase());

			IMagistrato lCtrl = SICOLookupRemote.getMagistratoRemote();
			Vector lVect = new Vector();
			try {
				lVect = lCtrl.ExRicercaMagistrato(lMagMod);
			} catch (F3BException exF3b) {
				throw new F3BException(F3BException.USER_MESSAGE, "Magistrato Inesistente");
			}

			lMagMod = (MagistratoModel) lVect.firstElement();
			lCodiceMagistrato = lMagMod.getCodMagistrato();
		}

		return lCodiceMagistrato;
	}

}