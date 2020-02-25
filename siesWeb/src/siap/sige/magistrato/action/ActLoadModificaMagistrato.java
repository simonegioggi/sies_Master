package siap.sige.magistrato.action;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.sige.magistrato.controller.IMagistrato;
import siap.sige.magistrato.model.MagistratoModel;
import siap.sige.magistratosezione.model.MagistratoSezioneModel;
import siap.sige.sezione.util.SezioneUtils;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadModificaMagistrato
 * </p>
 * <p>
 * Description: Classe Action per la load modifica Magistrato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia S.p.A.
 * </p>
 */
public class ActLoadModificaMagistrato extends ActionSiap implements ICostantiMagistrato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		Option lOption;
		String lId = getRequestStringParameter(CAMPO_COD_MAGISTRATO);

		// Lock
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "Magistrato", lId,
				getCodUtenteConnesso(), getSession().getId());

		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il  " + lck.getEntity()
					+ " è in gestione ad un altro utente!<BR>Riprovare più tardi !");
			return IWebConstants.PG_MESSAGE;
		}

		if (lId == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Manca il Codice Magistrato.");

		// Chiama il controller.
		IMagistrato lCtrl = SIGELookupRemote.getMagistratoRemote();
		// 20170918: [SG] aggiunto parametro di passaggio poichè il magistrato può essere inserito da un
		// ufficio differente da quello in cui ha delle udienze poichè trasferito
		MagistratoModel lMagMod = lCtrl.ExRicercaMagistratoByCod(lId, getCodUfficioUtenteConnesso());

		// ComboBOX Disponibilità.
		if (lMagMod.getFlagStato() != null)
			lOption = new Option(DecodificheManager.getInstance().getFlagStato(), lMagMod.getFlagStato());
		else
			lOption = new Option(DecodificheManager.getInstance().getFlagStato());
		setRequestAttribute("elencoFlagStato", "" + lOption);

		String[] lIdSezioni = new String[lMagMod.getMagistratoSezioni().length];

		MagistratoSezioneModel magistratoSezioneModel = new MagistratoSezioneModel();
		for (int i = 0; i < lIdSezioni.length; i++) {
			if (lMagMod.getMagistratoSezioni()[i].getSezIdSezione() != null) {
				lIdSezioni[i] = lMagMod.getMagistratoSezioni()[i].getSezIdSezione().toString();
				// magistratoSezioneModel = lMagMod.getMagistratoSezioni()[i];
			} else {
				lIdSezioni[i] = "";
			}
			// il primo elemento è valido: FLG_VALIDO_SN='S'
			magistratoSezioneModel = lMagMod.getMagistratoSezioni()[0];
		}

		// lOption = new Option( SezioneUtils.getElencoSezioni(getCodUfficioUtenteConnesso()), lIdSezioni[0],
		// Option.NO_BLANK_ITEM);
		// if(lIdSezioni[0] != null ){
		
		if (lIdSezioni.length > 0) {
			// 20171013: [EC] nella pagina di modifica devono essere visibili solo le sezioni per cui esiste una relazione con il magistrato_sezioni
//			lOption = new Option(SezioneUtils.getElencoSezioni(getCodUfficioUtenteConnesso()), lIdSezioni,
//					Option.NO_BLANK_ITEM);
			lOption = new Option(SezioneUtils.getElencoSezioniModificabiliBycodufficio(lId, getCodUfficioUtenteConnesso()), lIdSezioni,
					Option.NO_BLANK_ITEM);
		} else {
			lOption = new Option(SezioneUtils.getElencoSezioniModificabiliBycodufficio(lId,getCodUfficioUtenteConnesso()),
					Option.NO_BLANK_ITEM);
		}
		setRequestAttribute("elencoSezioni", lOption.toString());

		// Imposta Modalità.
		setRequestAttribute("modalita", "M");
		setRequestAttribute("magistrato", lMagMod);
		setRequestAttribute("magistratosezione", magistratoSezioneModel);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return PG_LOAD_INSERISCIMAGISTRATO;
	}

}