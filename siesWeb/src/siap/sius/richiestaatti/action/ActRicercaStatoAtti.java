package siap.sius.richiestaatti.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.notifica.controller.INotifica;
//import siap.sico.evento.controller.IEvento;
//import siap.sico.util.SICOLookupRemote;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.fascicolo.action.ActRicercaFSPuntuale;
import siap.sius.fascicolo.model.FascicoloGPModel;

/**
 * <p>
 * Title: ActRicercaStatoAtti
 * </p>
 * <p>
 * Description: Azione specializzazione della ActRicercaFSPuntuale. Usa il metodo <code>processrequest()<code>
 * del padre per ricavare il FascicoloSIUS. Questa classe a sua volta sarà ancora specializzata per mettere a
 * disposizione il metodo <code>ricercaAtti()<code> che effettua la ricerca degli Atti Istruttori richiesti.
 * </p>
 * <p>
 * Copyright: Bull Italia S.p.A.Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActRicercaStatoAtti extends ActRicercaFSPuntuale implements ICostantiRichiestaAtti {

	INotifica mCtrl = null; // Istanza del controller

	// ricerca gli eventi collegati al fascicolo e li passa nella Request
	@SuppressWarnings("rawtypes")
	protected String ricercaAtti(BigDecimal aIdFascicoloSius) throws Exception {

		if (mCtrl == null)
			mCtrl = SIEPLookupRemote.getNotificaRemote();
		Vector lVect = mCtrl.ExRicercaNotificheByFascicoloSius(aIdFascicoloSius, "05");

		setRequestAttribute("atti", lVect);
		return PG_ELENCOATTIRICHIESTI;
	}

	public String processRequest() throws Exception {

		String lPage = new String();

		if (isRequestParameterNullObj("noQuery") || isSessionAttributeNullObj("fascicoloSiusGP"))
			super.processRequest();
		else
			mFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		// Bottone di ritorno
		this.setLinkRitorno();

		lPage = ricercaAtti(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		setRequestAttribute("modalita", "M");
		// Ritorna la JSP di view dell'elenco Atti dopo i dati Fascicolo SIUS.
		return lPage;
	}

}