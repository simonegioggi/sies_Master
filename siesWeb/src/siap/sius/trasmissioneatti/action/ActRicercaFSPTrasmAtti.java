package siap.sius.trasmissioneatti.action;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sius.fascicolo.action.ActRicercaFSPuntuale;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActRicercaFSPTrasmAtti
 * </p>
 * <p>
 * Description: Classe Action per la ricerca puntuale del Fascicolo SIUS finalizzata alla trasmissione atti
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActRicercaFSPTrasmAtti extends ActRicercaFSPuntuale implements ICostantiTrasmissioneAtti {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		UtenteModel lUtenteConnesso = (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		String StrCodiceUfficioUtente = lUtenteConnesso.getUfficioUtente().getCodUfficio();
		setRequestAttribute("CodiceUfficioUtente", StrCodiceUfficioUtente);

		// Istanzio il Model
		FascicoloGPModel lFasGP = new FascicoloGPModel();

		lFasGP.getFascicoloSiusModel().setChiaveUfficio(StrCodiceUfficioUtente);

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO))
			lFasGP.getFascicoloSiusModel().setChiaveAnno(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO));
		else
			siesLogger.info(">>>>>>>>>>> ERRORE : CAMPO_CHIAVE_ANNO  non valorizzato ");

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR))
			lFasGP.getFascicoloSiusModel().setChiaveProgr(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR));
		else
			siesLogger.info(">>>>>>>>>>> ERRORE : CAMPO_CHIAVE_PROGR  non valorizzato ");

		// Imposta Tipo Ufficio.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSius());
		setRequestAttribute("tipoUfficioSius", "" + lOption);

		// FascicoloSiusController lCtrl = new FascicoloSiusController();
		IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();

		FascicoloGPModel lFasGPMod = lCtrl.ExRicercaFascicoloByAnnoProgrCodUfficio(
				getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO),
				getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR), StrCodiceUfficioUtente);

		// Metto in sessione il fascicolo SIUS per consentire le funzionalità annesse.
		setRequestAttribute("fascicoloSiusGP", lFasGPMod);
		setSessionAttribute("fascicoloSiusGP", lFasGPMod);

		return PG_LOAD_TRASMISSIONEATTI;
	}

}