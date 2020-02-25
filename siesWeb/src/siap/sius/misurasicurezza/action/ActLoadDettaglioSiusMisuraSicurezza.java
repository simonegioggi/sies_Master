package siap.sius.misurasicurezza.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.rifasiep.controller.IRiferimentoFascicoloSiep;
import siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioSiusMisuraSicurezza
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci MisuraSicurezza in Sius
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadDettaglioSiusMisuraSicurezza extends ActionSius implements ICostantiSiusMisuraSicurezza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	protected MisuraSicurezzaModel mlMisMod = null;

	public String processRequest() throws Exception {

		// paramentro passato solo nel caso di iscrizione guidata
		if (!this.isRequestAttributeNullObj("lTipoFunzione")) {
			this.setRequestAttribute("lTipoFunzione", this.getRequestAttribute("lTipoFunzione"));
		}

		// paramentro passato solo nel caso di iscrizione guidata E VENGO DA DETTAGLIO SOGGETTO
		if (!this.isRequestParameterNullObj("lTipoFunzione")) {
			this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
		}

		BigDecimal lId = this.getRequestBigDecimalParameter(CAMPO_ID_MISURA_SICUREZZA);
		// chiama il controller
		IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		mlMisMod = lCtrl.ExRicercaMisuraSicurezzaByKey(lId);
		setRequestAttribute("misurasicurezza", mlMisMod);

		// TODO carmela recuperato Riferimento Titolo Esecutivo (Altro Titolo)
		if (mlMisMod.getFasSieIdFascicoloSiepRif() != null
				&& !mlMisMod.getFasSieIdFascicoloSiepRif().toString().equals("")) {
			IRiferimentoFascicoloSiep lRifFascSiep = SIUSLookupRemote.getRiferimentoFascicoloSiepRemote();
			RiferimentoFascicoloSiepModel rifFascSiep = lRifFascSiep
					.ExRicercaRiferimentoFascicoloSiepByKey(mlMisMod.getFasSieIdFascicoloSiepRif());
			setRequestAttribute("riferimentoTitoloEsecutivo", rifFascSiep);
		}
		// recuperato Riferimento Titolo Esecutivo (Titolo Principale)
		if (mlMisMod.getSenIdSentenza() != null && !mlMisMod.getSenIdSentenza().toString().equals("")) {
			IFascicoloSiep lFasSiep = SIEPLookupRemote.getFascicoloSiepRemote();
			FascicoloSiepModel fascSiep = lFasSiep
					.ExRicercaFascicoloSiepByIdSentenza(mlMisMod.getSenIdSentenza());
			setRequestAttribute("riferimentoTitoloEsecutivoPrincipale", fascSiep);
		}

		if (mlMisMod.getFasSiuIdFascicoloSius() != null) {
			// Ricerca il Fascicolo Sius per metterlo in sessione
			// nel caso il Dettaglio venga richiamato dall'Elenco di procedimenti con Misure Sicurezza
			IFascicoloSius lCtrlFasc = SIUSLookupRemote.getFascicoloSiusRemote();
			FascicoloGPModel lFasRet = lCtrlFasc.ExRicercaFascicoloByKey(mlMisMod.getFasSiuIdFascicoloSius());

			if (lFasRet != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lFasRet " + lFasRet);

				setRequestAttribute("fascicoloSiusGP", lFasRet);
				setSessionAttribute("fascicoloSiusGP", lFasRet);
			}
		}

		return PG_LOAD_DETTAGLIO_SIUS_MISURASICUREZZA;
	}

}