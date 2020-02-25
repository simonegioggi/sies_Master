package siap.siep.richiesta.action;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActInserisciMisuraAlternativa</p>
 * <p>Description: Classe Action per l'inserimento di MisuraAlternativa</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.notifica.model.NotificaModel;

public class ActInserisciRichiestaMSEspPenaLibCond

		extends ActRichiesta implements ICostantiRichiesta {
	/**
	 * Azione di Inserimento del MisuraAlternativa
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lPage = null;

		// ricerca esistenza almeno una misura alternativa
		// MisuraAlternativaModel lMisAlMod = new MisuraAlternativaModel();
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		/* lMisAlMod = */lMisAltCtrl
				.ExRicercaMisuraAlternativaByFascicoloOrdinanza(lFascMod.getIdFascicoloSiep());

		EventoNotificaModel lEveMod = new EventoNotificaModel();

		// setto Evento
		lEveMod.getEvento().setCodTipoEvento("10");
		lEveMod.getEvento().setCodMotivo("0124");
		// 11/07/2011 La Data Emissione va inserita nel formato dd/MM/yyyy
		// lEveMod.getEvento().setDataEmissione(DateUtils.getSysDate());
		lEveMod.getEvento()
				.setDataEmissione(DateUtils.getDate(DateUtils.getSysDate("dd/MM/yyyy"), "dd/MM/yyyy"));
		lEveMod.getEvento().setCodUfficioEmittente(this.getCodUfficioUtenteConnesso());
		lEveMod.getEvento().setCodOperatoreInserimento(this.getCodUtenteConnesso());
		ComuneModel lComModAut = new ComuneModel(getCodComuneByDescr(
				getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT)));
		lEveMod.getEvento().setCodLuogoEmittente(lComModAut.getCodComune());
		// lEveMod.getEvento().setCodMagistrato(ICostantiMisuraAlternativa.CAMPO_COD_MAGISTRATO);
		// --NO lEveMod.getEvento().setFlagDocumentoRegistrato("N");
		lEveMod.getEvento().setCodTipoProvvedimento("-");
		lEveMod.getEvento().setCodEsito("-");
		lEveMod.getEvento().setCodLuogoDestinatario("-");
		lEveMod.getEvento().setCodTipoUfficioDestinatario("-");
		lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		lEveMod.getEvento().setDataInserimento(DateUtils.getSysDate());

		// setto Notifica
		NotificaModel[] lNotifiche = setNotificheRichiesta();
		lEveMod.setNotifiche(lNotifiche);

		// Inserisco dentro Tabella Evento e Notifica
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveNotModel = lCtrlEve.ExInserisciEventoNotifica(lEveMod);

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.richiesta.action.ActDettaglioRichiestaMS&" + ICostantiEvento.CAMPO_ID_EVENTO
				+ "=" + lEveNotModel.getEvento().getIdEvento();

		return lPage;
	}

}