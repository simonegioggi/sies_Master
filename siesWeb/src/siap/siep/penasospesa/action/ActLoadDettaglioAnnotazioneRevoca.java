package siap.siep.penasospesa.action;

import java.math.BigDecimal;
import java.util.Collection;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.controller.IComune;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/**
 * <p>
 * Title: ActDettaglioAnnotazioneProvvedimento
 * </p>
 * <p>
 * Description: Classe Action per il dettaglio del provveidmento sanzione sostitutiva
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 3.0
 */

public class ActLoadDettaglioAnnotazioneRevoca extends ActSIESDettaglioProvvedimento
		implements ICostantiPenaSospesa {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Evento
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lIdEvento);
		setRequestAttribute("evento", lEveMod);

		IAnnotazioneManuale lCtrlAnnMan = SIEPLookupRemote.getAnnotazioneManualeRemote();
		AnnotazioneManualeModel lAnnMan = lCtrlAnnMan.ExRicercaAnnotazioniManualiByIdEvento(lIdEvento);
		setRequestAttribute("AnnotazioneMan", lAnnMan);

		DecodificheModel lModel = new DecodificheModel();
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("TIPO_UFFICIO_EMITTENTE");
		Collection Uffi_Emi = lDecodifiche.ExRicercaDecodifiche(lModel);
		String strDescrUfficio = "";
		java.util.Iterator itxOggetto = Uffi_Emi.iterator();
		while (itxOggetto.hasNext()) {
			DecodificheModel lDecMod = (DecodificheModel) itxOggetto.next();
			if (lDecMod.getCode().equals(lAnnMan.getCodTipoUfficioSiep()))
				strDescrUfficio += lDecMod.getDescription();
		}
		setRequestAttribute("strDescrUfficio", strDescrUfficio);

		IComune comctrl = SICOLookupRemote.getComuneRemote();
		ComuneModel comunemod = comctrl.ExRicercaComuneByKey(lAnnMan.getCodLuogoUfficioSiep());
		setRequestAttribute("strDescrComune", comunemod.getDescrizione());

		if (!isRequestParameterNullObj("TipoPro"))
			return IWebConstants.ROOT_DIR + "files/siap/siep/penasospesa/DettaglioRichiestaRevoca.jsp";
		else
			return PG_DETTAGLIO_ANNOTAZIONEREVOCA;
	}

}