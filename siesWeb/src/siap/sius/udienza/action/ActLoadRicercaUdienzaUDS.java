package siap.sius.udienza.action;

import siap.sico.assistentegiudiziario.controller.IAssistenteGiudiziario;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadRicercaUdienza
 * </p>
 * <p>
 * Description: Classe Action per la load ricerca di Udienza
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
public class ActLoadRicercaUdienzaUDS extends ActionSiap implements ICostantiUdienza {

	public String processRequest() throws F3BException {

		String lCodUfficio = getCodUfficioUtenteConnesso();
		String lCodComune = getCodComuneUtenteConnesso();
		// MEV10-s3: modificato il codice tipo ufficio: lo prelevo dalla tipologia di utente connesso
		String lCodTipoUfficioPM = "PM";
		String lCodTipoUfficio = getCodTipoUfficioConnesso();
		setRequestAttribute("codTipoUfficio", "" + lCodTipoUfficio);

		//
		// Inserimento ComboBOX.
		//
		// Crea lista elenco magistrati nelle 3 diverse figure.
		IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
		Option lOption = new Option(lMagCtrl.ExElencoCbxMagistratiByCodUfficio(lCodUfficio));
		setRequestAttribute("elencoPresidenti", "" + lOption);

		// 20170904: [SG] per i procuratori e gli esperti reimposto il codice tipo ufficio a PM (vedi sopra)
		// Crea lista elenco procuratori della Repubblica.
		lOption = new Option(lMagCtrl.ExElencoCbxMagByCodComuneCodTipoUff(lCodComune, lCodTipoUfficioPM));
		setRequestAttribute("elencoProcuratori", "" + lOption);

		// Crea lista elenco assistenti.
		IAssistenteGiudiziario lAssistenteCtrl = SICOLookupRemote.getAssistenteGiudiziarioRemote();
		lOption = new Option(lAssistenteCtrl.ExElencoCbxAssistenteGiudiziarioByCodUfficio(lCodUfficio));
		setRequestAttribute("elencoAssistenti", "" + lOption);

		return PG_LOAD_RICERCAUDIENZA_UDS; // restituisce la jsp di VIEW
	}

}