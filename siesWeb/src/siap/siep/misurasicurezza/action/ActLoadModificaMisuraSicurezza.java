package siap.siep.misurasicurezza.action;

//import per le combo
import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.rifasiep.controller.IRiferimentoFascicoloSiep;
import siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadInserisciMisuraSicurezza
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di MisuraSicurezza
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
public class ActLoadModificaMisuraSicurezza extends ActionSiap implements ICostantiMisuraSicurezza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Controllo che non si stia lavorando su una entità in modifica ad altri
		LockModel lck = lockIfNotLocked("misura di sicurezza",
				getRequestStringParameter(CAMPO_ID_MISURA_SICUREZZA), getCodUtenteConnesso());
		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "La " + lck.getEntity()
					+ " è in gestione ad un altro utente! <BR>Riprovare più tardi!");
			return IWebConstants.PG_MESSAGE;
		}

		BigDecimal lID = this.getRequestBigDecimalParameter(CAMPO_ID_MISURA_SICUREZZA);
		IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		MisuraSicurezzaModel llMisModRet = lCtrl.ExRicercaMisuraSicurezzaByKey(lID);

		// Inserire Eventuali ComboBOX
		Option lOptionN = new Option(DecodificheManager.getInstance().getNaturaMisuraSicurezza(),
				llMisModRet.getCodNatura());
		// Option lOptionT = new Option(
		// DecodificheManager.getInstance().getTipoMisuraSicurezza(),llMisModRet.getCodTipo());
		Vector lVec = ((Vector) DecodificheManager.getInstance().getTipoMisuraSicurezza());

		// variabile che verrà passata alla jsp
		setRequestAttribute("naturaMisuraSicurezza", "" + lOptionN);
		setRequestAttribute("tipoMisuraSicurezza", lVec);

		// Ricerca Eventuale altro Titolo Esecutivo Associato alla M.S.
		RiferimentoFascicoloSiepModel lRifMod = null;
		if (llMisModRet.getFasSieIdFascicoloSiepRif() != null) {
			IRiferimentoFascicoloSiep lCtrlS = SIUSLookupRemote.getRiferimentoFascicoloSiepRemote();
			lRifMod = lCtrlS
					.ExRicercaRiferimentoFascicoloSiepByKey(llMisModRet.getFasSieIdFascicoloSiepRif());
		}

		llMisModRet.setRiferimentoFascicoloSiep(lRifMod);
		setRequestAttribute("misurasicurezza", llMisModRet);

		// Imposta Modalità.
		setRequestAttribute("modalita", "M");

		if (llMisModRet.getFasSieIdFascicoloSiep() != null) {
			// Ricerca il Fascicolo Siep per metterlo in sessione
			// nel caso la Modifica venga richiamata dall'Elenco di procedimenti con Misure Sicurezza
			IFascicoloSiep lCtrlFasc = SIEPLookupRemote.getFascicoloSiepRemote();
			FascicoloSiepModel lFasRet = lCtrlFasc.ExRicercaFascicoloByKey(llMisModRet
					.getFasSieIdFascicoloSiep());

			if (lFasRet != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lFasRet " + lFasRet);

				setRequestAttribute("fascicolo", lFasRet);
				setSessionAttribute("fascicolo", lFasRet);
			}
		}

		return PG_LOAD_INSERISCIMISURASICUREZZA; // restituisce la jsp di VIEW
	}

}