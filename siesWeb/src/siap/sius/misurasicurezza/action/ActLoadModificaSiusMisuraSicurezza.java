package siap.sius.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.lock.model.LockModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.provvedimento.util.RicercaProvvedimentiUtil;
import siap.sius.titoloesecutivo.controller.ITitoloEsecutivo;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadSiusMisuraSicurezza
 * </p>
 * 
 * <p>
 * Description: Classe Action per la load Modifica di MisuraSicurezza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadModificaSiusMisuraSicurezza extends ActionSius implements ICostantiSiusMisuraSicurezza {
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
		Option lOptionT = new Option(DecodificheManager.getInstance().getTipoMisuraSicurezza(),
				llMisModRet.getCodTipo());

		if (super.getFiltroMinorenni().equalsIgnoreCase("true")) {
			lOptionT = new Option(DecodificheManager.getInstance().getTipoMisuraSicurezzaMinorenni(),
					llMisModRet.getCodTipo());
			// MEV10-s3: aggiunta nuova gestione liste per tipo natura
			String codNatura = this.getParameter("CodNatura");
			if (codNatura != null) {
				if ("02".equals(codNatura))
					lOptionT = new Option(DecodificheManager.getInstance().getTipoMSMinorenniNonDetentiva(),
							llMisModRet.getCodTipo());
				else if ("01".equals(codNatura))
					lOptionT = new Option(DecodificheManager.getInstance().getTipoMSMinorenniDetentiva(),
							llMisModRet.getCodTipo());
			} else {
				if ("02".equals(llMisModRet.getCodNatura()))
					lOptionT = new Option(DecodificheManager.getInstance().getTipoMSMinorenniNonDetentiva(),
							llMisModRet.getCodTipo());
				else if ("01".equals(llMisModRet.getCodNatura()))
					lOptionT = new Option(DecodificheManager.getInstance().getTipoMSMinorenniDetentiva(),
							llMisModRet.getCodTipo());
			}
			setRequestAttribute("codNatura", codNatura);

			FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
			setRequestAttribute("codTipoUfficio", lFasGPMod.getFascicoloSiusModel().getCodTipoUfficio());

		}

		// TODO carmela da verificare
		BigDecimal idFascicoloSius = llMisModRet.getFasSiuIdFascicoloSius();

		ITitoloEsecutivo lRifTitoloEsec = SIUSLookupRemote.getTitoloEsecutivoRemote();
		Option lOptionR = new Option(
				lRifTitoloEsec.ExRicercaTitoloEsecutivoByIdFascicoloSius(idFascicoloSius));

		if (llMisModRet.getFasSieIdFascicoloSiepRif() != null || llMisModRet.getSenIdSentenza() != null) {
			if (llMisModRet.getSenIdSentenza() != null) {
				lOptionR.setSelected(llMisModRet.getSenIdSentenza().toString() + "/P");
			} else {
				lOptionR.setSelected(llMisModRet.getFasSieIdFascicoloSiepRif().toString() + "/R");
			}
		}

		setRequestAttribute("riferimentoTitoloEsecutivo", "" + lOptionR);

		// variabile che verrà passata alla jsp
		setRequestAttribute("naturaMisuraSicurezza", "" + lOptionN);
		setRequestAttribute("tipoMisuraSicurezza", "" + lOptionT);
		setRequestAttribute("misurasicurezza", llMisModRet);

		// MERGE v10: sovrascrivo il "tipoMisuraSicurezza" poichè nella pagina ci aspettiamo un vettore
		// ComboBOX X Tipo Misura Sicurezza
		Vector lVec = (Vector) DecodificheManager.getInstance().getTipoMisuraSicurezza();
		setRequestAttribute("tipoMisuraSicurezza", lVec);

		// Imposta Modalità.
		setRequestAttribute("modalita", "M");

		if (llMisModRet.getFasSiuIdFascicoloSius() != null) {
			// Ricerca il Fascicolo Sius per metterlo in sessione
			// nel caso la Modifica venga richiamata dall'Elenco di procedimenti con Misure Sicurezza
			IFascicoloSius lCtrlFasc = SIUSLookupRemote.getFascicoloSiusRemote();
			FascicoloGPModel lFasRet = lCtrlFasc.ExRicercaFascicoloByKey(llMisModRet
					.getFasSiuIdFascicoloSius());

			if (lFasRet != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("lFasRet " + lFasRet);
				// Se sono già stati emessi provvedimenti o il fascicolo non è in stato 'Iscritto' non sono
				// consentite modifiche alle misure
				// 01/03/2016 Inizio
	   	     	// RicercaProvvedimentiUtil lRicerca = new RicercaProvvedimentiUtil(lFasRet.getGeneraleProcedimentoModel().getFasSiuIdFascicoloSius());
		     	RicercaProvvedimentiUtil lRicerca = new RicercaProvvedimentiUtil(lFasRet.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
	    	 	// 01/03/2016 Fine
				boolean lEsistenzaDoc = lRicerca.verificaEsistenzaProv();

				if (lEsistenzaDoc
						|| lFasRet.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("02") != 0)
					throw new SIUSException(SIUSException.USER_MESSAGE,
							"Impossibile modificare le misure per questo fascicolo ! ");

				setRequestAttribute("fascicoloSiusGP", lFasRet);
				setSessionAttribute("fascicoloSiusGP", lFasRet);
			}
		}

		return PG_LOAD_INSERISCI_SIUS_MISURASICUREZZA; // restituisce la jsp di VIEW
	}

}