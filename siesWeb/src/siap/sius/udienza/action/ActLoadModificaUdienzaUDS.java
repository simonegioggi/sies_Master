package siap.sius.udienza.action;

import java.math.BigDecimal;

import siap.sico.assistentegiudiziario.controller.IAssistenteGiudiziario;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.udienza.controller.IUdienza;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadModificaUdienza
 * </p>
 * <p>
 * Description: Classe Action per la load modifica Udienza
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
public class ActLoadModificaUdienzaUDS extends ActionSiap implements ICostantiUdienza {

	UdienzaModel mUdiMod = null;

	public String processRequest() throws F3BException {

		// Lock
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "Udienza",
				getRequestStringParameter(CAMPO_ID_UDIENZA), getCodUtenteConnesso(), getSession().getId());
		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "La  " + lck.getEntity()
					+ " è in gestione ad un altro utente!<BR>Riprovare più tardi !");
			return IWebConstants.PG_MESSAGE;
		}

		// valorizzazione della request
		preparaRequest();
		// Imposta la risposta nella request.
		setRequestAttribute("modalita", "M");
		setRequestAttribute("udienza", mUdiMod);

		return PG_LOAD_INSERISCIUDIENZA_UDS;
	}

	/**
	 * metodo creato per poter essere utilizzato sia da ActLoatModificaUdienza che da
	 * ActLoatInserisciCopiaUdienza.
	 */
	protected void preparaRequest() throws F3BException {

		// Si ricavano Ufficio, Comune, TipoUfficio
		String lCodUfficio = getCodUfficioUtenteConnesso();
		String lCodComune = getCodComuneUtenteConnesso();
		// MEV10-s3: modificato il codice tipo ufficio: lo prelevo dalla tipologia di utente connesso
		String lCodTipoUfficioPM = "PM";
		String lCodTipoUfficio = getCodTipoUfficioConnesso();
		setRequestAttribute("codTipoUfficio", "" + lCodTipoUfficio);

		// ricava ID_UDIENZA
		BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_UDIENZA);

		// chiama il controller per ricercare l'Udienza
		IUdienza lCtrl = SIUSLookupRemote.getUdienzaRemote();
		mUdiMod = lCtrl.ExRicercaUdienzaByKeyUDS(lId);

		// Inserire Eventuali ComboBOX.
		// Crea lista elenco magistrati.
		IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
		Option lOption = new Option(lMagCtrl.ExElencoCbxMagistratiByCodUfficio(lCodUfficio),
				mUdiMod.getCodPresidente());
		setRequestAttribute("elencoPresidenti", "" + lOption);

		// 20170904: [SG] per i procuratori e gli esperti reimposto il codice tipo ufficio a PM (vedi sopra)
		// Crea lista elenco procuratori.
		lOption = new Option(lMagCtrl.ExElencoCbxMagByCodComuneCodTipoUff(lCodComune, lCodTipoUfficioPM),
				mUdiMod.getCodPg());
		setRequestAttribute("elencoProcuratori", "" + lOption);

		// Crea lista elenco assistenti
		IAssistenteGiudiziario lAssistenteCtrl = SICOLookupRemote.getAssistenteGiudiziarioRemote();
		lOption = new Option(lAssistenteCtrl.ExElencoCbxAssistenteGiudiziarioByCodUfficio(lCodUfficio), ""
				+ mUdiMod.getCodIdAssistente());
		setRequestAttribute("elencoAssistenti", "" + lOption);
		return;
	}

}