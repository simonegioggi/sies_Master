package siap.sius.udienza.action;

import java.math.BigDecimal;

import siap.sico.assistentegiudiziario.controller.IAssistenteGiudiziario;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.esperto.controller.IEsperto;
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
public class ActLoadModificaUdienza extends ActionSiap implements ICostantiUdienza {

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

		return PG_LOAD_INSERISCIUDIENZA;
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
		String lCodTipoUfficioPGCAP = "PGCAP";
		String lCodTipoUfficio = getCodTipoUfficioConnesso();
		setRequestAttribute("codTipoUfficio", "" + lCodTipoUfficio);

		// ricava ID_UDIENZA
		BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_UDIENZA);


		// chiama il controller per ricercare l'Udienza
		IUdienza lCtrl = SIUSLookupRemote.getUdienzaRemote();
		mUdiMod = lCtrl.ExRicercaUdienzaByKey(lId);

		// Inserire Eventuali ComboBOX.
		// Crea lista elenco magistrati.
		IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
		Option lOption = new Option(lMagCtrl.ExElencoCbxMagistratiByCodUfficio(lCodUfficio),
				mUdiMod.getCodPresidente());
		setRequestAttribute("elencoPresidenti", "" + lOption);
		lOption.setSelected(mUdiMod.getCodGiudice1());
		setRequestAttribute("elencoGiudici1", "" + lOption);
		lOption.setSelected(mUdiMod.getCodGiudice2());
		setRequestAttribute("elencoGiudici2", "" + lOption);

		// 20170904: [SG] per i procuratori e gli esperti reimposto il codice tipo ufficio a PGCAP (vedi
		// sopra) x2
		// Crea lista elenco procuratori.
		lOption = new Option(lMagCtrl.ExElencoCbxMagByCodComuneCodTipoUff(lCodComune, lCodTipoUfficioPGCAP),
				mUdiMod.getCodPg());
		setRequestAttribute("elencoProcuratori", "" + lOption);

		IEsperto lEspertoCtrl = SIUSLookupRemote.getEspertoRemote();

		// Crea lista elenco esperti.
		// MERGE v10: cambiata query per aggiunta inserimento riga vuota per tipo ufficio
		lOption = new Option(lEspertoCtrl.ExElencoCbxEspertiByCodAndTipoUff(lCodUfficio, lCodTipoUfficioPGCAP), ""
				+ mUdiMod.getCodIdEsperto1());
		setRequestAttribute("elencoEsperti1", "" + lOption);
		lOption.setSelected("" + mUdiMod.getCodIdEsperto2());
		setRequestAttribute("elencoEsperti2", "" + lOption);

		IAssistenteGiudiziario lAssistenteCtrl = SICOLookupRemote.getAssistenteGiudiziarioRemote();

		// Crea lista elenco assistenti.
		lOption = new Option(lAssistenteCtrl.ExElencoCbxAssistenteGiudiziarioByCodUfficio(lCodUfficio), ""
				+ mUdiMod.getCodIdAssistente()/* , Option.BLANK_ITEM */);
		setRequestAttribute("elencoAssistenti", "" + lOption);
		return;
	}

}