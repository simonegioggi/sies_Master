package siap.sico.utenzaAdn.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.security.controller.ISecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.utenzaAdn.controller.IAssocUtenteSiesAdn;
import siap.sico.utenzaAdn.controller.IUtenzaAdn;
import siap.sico.utenzaAdn.model.AssocUtenteSiesAdnModel;
import siap.sico.utenzaAdn.model.UtenzaAdnModel;
import siap.sico.util.SICOLookupRemote;

/**
 * MEV INTEGRAZIONE SIES ADN
 *
 * @author sgioggi
 *
 */
public class UtenzaAdnUtils {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public static List<AssocUtenteSiesAdnModel> verificaAssociazioneSiesAdn(String userId) {

		List<AssocUtenteSiesAdnModel> l = new ArrayList<>();
		IAssocUtenteSiesAdn iausa = null;
		try {
			iausa = ADNLookupRemote.getAssocUtenteSiesAdnRemote();
			l = iausa.verificaAssociazioneSiesAdn(userId);
		} catch (Exception ex) {
			siesLogger.error(ex.getMessage());
			siesLogger.error("Nessuna associazione SIES-ADN trovata: ritorno lista vuota!");
			l = new ArrayList<>();
		}
		return l;
	}

	public static BigDecimal inserisciUtenzaAdn(String userId) throws F3BException {

		BigDecimal bd = null;
		IUtenzaAdn iua = null;
		try {
			iua = ADNLookupRemote.getUtenzaAdnRemote();
			bd = iua.inserisciUtenzaAdn(userId);
		} catch (Exception ex) {
			siesLogger.error(ex.getMessage());
			siesLogger.error("Errore in inserimento Utenza ADN: ritorno l'eccezione!");
			throw ex;
		}
		return bd;
	}

	public static UtenzaAdnModel verificaEsistenzaUtenzaAdn(String userId) {

		UtenzaAdnModel uam = null;
		IUtenzaAdn iua = null;
		try {
			iua = ADNLookupRemote.getUtenzaAdnRemote();
			uam = iua.verificaEsistenzaUtenzaAdn(userId);
		} catch (Exception ex) {
			siesLogger.error(ex.getMessage());
			siesLogger.error("Errore in verifica esistenza Utenza ADN: ritorno null!");
			return null;
		}
		return uam;
	}

	public static AssocUtenteSiesAdnModel inserisciAssociazioneSiesAdn(String userId, BigDecimal id)
			throws F3BException {

		AssocUtenteSiesAdnModel ausam = null;
		IAssocUtenteSiesAdn iausa = null;
		try {
			iausa = ADNLookupRemote.getAssocUtenteSiesAdnRemote();
			ausam = iausa.inserisciAssociazioneSiesAdn(userId, id);
		} catch (Exception ex) {
			siesLogger.error(ex.getMessage());
			siesLogger.error("Errore in inserimento Associazione Utenza SIES-ADN: ritorno l'eccezione!");
			throw ex;
		}
		return ausam;
	}

	public static UtenteModel preLogin(UtenteModel um, boolean test) throws F3BException {

		ISecurity is = null;
		try {
			is = SICOLookupRemote.getSecurityRemote();
			um = is.preLogin(um, test);
		} catch (F3BException ex) {
			siesLogger.error(ex.getMessage());
			siesLogger.error("Errore nella pre Login: ritorno l'eccezione!");
			throw ex;
		}
		return um;
	}

}