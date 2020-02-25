package siap.siep.istanza.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istanza.controller.IIstanza;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActRicercaIstanzaByFascicolo
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActRicercaIstanzaByFascicolo extends ActionSiap implements ICostantiIstanza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		BigDecimal lKey = null;
		if (this.isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP)) {
			if (this.isSessionAttributeNullObj("fascicolo")) {
				return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
			}

			FascicoloSiepModel lFascMod = (FascicoloSiepModel) this.getSessionAttribute("fascicolo");
			lKey = lFascMod.getIdFascicoloSiep();
		} else {
			lKey = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);
		}

		IIstanza lCtrl = SIEPLookupRemote.getIstanzaRemote();
		Vector lVect = lCtrl.ExRicercaIstanzaByFascicolo(lKey);

		setRequestAttribute("istanzeeventi", lVect);

		// 10-03-06 --Dario --Viviana
		// grazie alla redirect nel caso in cui annullo un istanza posso ritornare dopo aver inserito le
		// motivazioni
		// sull'elenco delle istanze associate al fascicolo
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction(getClass().getName());
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

		return PG_RICERCA_EVENTO_ISTANZA;
	}

}