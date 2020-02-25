package siap.sige.udienzaparti.action;

import java.math.BigDecimal;

import siap.sico.avvocato.model.AvvocatoModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.web.ActionSiap;
import siap.sige.avvocato.controller.IAvvocato;
import siap.sige.udienzaparti.model.PartiUdienzaDifensoreModel;
import siap.sige.util.SIGELookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActDeassegnaDifensore
 * </p>
 * <p>
 * Description: Classe Action per la Deassegnazione di un difensore
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActDeassegnaDifensore extends ActionSiap implements ICostantiPartiUdienza {

	public String processRequest() throws Exception {

		String idSoggetto = "";
		if (!this.isRequestParameterNullObj(ICostantiPartiUdienza.CAMPO_ID_SOGGETTO)) {
			idSoggetto = this.getRequestStringParameter(ICostantiPartiUdienza.CAMPO_ID_SOGGETTO);
		}

		// Identificativo evento udienza
		String lIdEventoUdienza = getRequestStringParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV);

		BigDecimal lId = getRequestBigDecimalParameter("tipo");

		// Passa la action di destinazione
		IAvvocato lCtrl = SIGELookupRemote.getAvvocatoRemote();

		PartiUdienzaDifensoreModel lPartiUdienzaDifensore = new PartiUdienzaDifensoreModel();
		lPartiUdienzaDifensore.setAvvIdAvvocato(lId);
		lPartiUdienzaDifensore.setSoggIdSoggetto(new BigDecimal(idSoggetto));
		lPartiUdienzaDifensore.setDataFineValidita(DateUtils.getSysDate());
		lPartiUdienzaDifensore.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lPartiUdienzaDifensore.setDataAggiornamento(DateUtils.getSysDate());
//		Vector lVect = null;

		PartiUdienzaDifensoreModel lAvvModificato = new PartiUdienzaDifensoreModel();

		lAvvModificato = lCtrl.ExDeassegnaDifensoreParteUdienza(lPartiUdienzaDifensore);

		AvvocatoModel lAvvMod = new AvvocatoModel();

		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);

		try {
//			lVect = new Vector();
			/*lVect = */lCtrl.ExRicercaDifensoreAttualiParteUdienza(lAvvMod, lPartiUdienzaDifensore);
		} catch (Exception e) {
		}

		String nextAct = "siap.sige.udienzaparti.action.ActLoadModificaParteUdienza&" + CAMPO_ID_SOGGETTO
				+ "=" + lAvvModificato.getSoggIdSoggetto() + "&" + ICostantiSecurity.CAMPO_ID_ENTITA_PROVV
				+ "=" + lIdEventoUdienza;
		String retPage = ritornoDopoCancellazione("Deassegnazione  Difensore Avvenuta Correttamente!",
				nextAct);
		return retPage;
	}

}