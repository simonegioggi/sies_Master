package siap.sius.fascicolo.action;

/**
* <p>Title: ActRicercaFSPuntuale</p>
* <p>Description: Classe Action per la ricerca puntuale del Fascicolo SIUS</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.depositodecreto.action.ICostantiDepositoDecreto;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

public class ActRicercaFSPuntuale extends ActionSius implements ICostantiFascicoloSius {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	protected FascicoloGPModel mFasGPMod = null;// occorre ereditarlo
	// 07/06/2004 Aggiunto attributo per parametrizzare il controllo dello stato fascicolo (=false evita i
	// controlli per filtrare le operazioni sui procedimenti).
	protected boolean mControl = true;

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

		String lRetPage = PG_DETTAGLIOFASCICOLOSIUS;
		UtenteModel lUtenteConnesso = (UtenteModel) getSessionAttribute(
				ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		String StrCodiceUfficioUtente = lUtenteConnesso.getUfficioUtente().getCodUfficio();
		setRequestAttribute("CodiceUfficioUtente", StrCodiceUfficioUtente);

		// Istanzio il Model
		FascicoloGPModel lFasGP = new FascicoloGPModel();

		lFasGP.getFascicoloSiusModel().setChiaveUfficio(StrCodiceUfficioUtente);

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO))
			lFasGP.getFascicoloSiusModel().setChiaveAnno(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR))
			lFasGP.getFascicoloSiusModel().setChiaveProgr(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR));

		// FascicoloSiepController lCtrl = new FascicoloSiepController();
		IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();

		// STUB 07/06/2004 Sdoppio la chiamata
		if (this.mControl)
			mFasGPMod = lCtrl.ExRicercaFascicoloByAnnoProgrCodUfficio(
					getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO),
					getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR), StrCodiceUfficioUtente);
		else
			mFasGPMod = lCtrl.ExRicercaFascicoloByAnnoProgrCodUfficioNoControl(
					getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO),
					getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR), StrCodiceUfficioUtente);

		/*
		 * ISSUE MEV : aggiunto controllo su tipo procedimento C050 e C051
		 * Numero MEV : 9
		 * Autore : Gioggi
		 * Data : 12 nov 2020
		 * Branch : MEV_9
		 */
		if (mFasGPMod != null && mFasGPMod.getGeneraleProcedimentoModel() != null
				&& mFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento() != null) {
			if (!(mFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().equals(
					ICostantiDepositoDecreto.COD_OGGETTO_CONCESSIONE_MISURE_ALTERNATIVE_ALLA_DETENZIONE)
					|| mFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().equals(
							ICostantiDepositoDecreto.COD_OGGETTO_CONCESSIONE_MISURE_PENALI_DI_COMUNITA_MISURE_ALTERNATIVE_ALLA_DETENZIONE))
					&& (this instanceof siap.sius.depositodecreto.action.ActLoadInserisciDesignazioneMagistratoRelatore
							|| this instanceof siap.sius.depositodecreto.action.ActLoadInserisciConfermaDecisioneMagistratoRelatore)) {
				String descrOggettoProcedimento = mFasGPMod.getGeneraleProcedimentoModel()
						.getDescrOggettoProcedimento();
				throw new F3BException(F3BException.USER_MESSAGE,
						"Operazione non consentita per il Procedimento di " + descrOggettoProcedimento);
			}
		}
		// ***** FINE INTERVENTO MEV_9 *****//

		// 30/04/2007 Si Consente alla fase di "Richiesta atti" di operare con i Procedimenti di ESECUZIONE
		// MISURE ALTERNATIVE.
		// 03/08/2007 Si Consente alla fase di "Richiesta atti" di operare con i Procedimenti di ESECUZIONE
		// SANZIONI SOSTITUTIVE.
		// Si esegue controllo per impedire di operare con Procedimenti di :
		// - Esecuzione Misure Alternative = U004
		// - Esecuzione Sanzioni Sostitutive = U019
		// - Esecuzione Misure Sicurezza = U024
		// N.B.: 28/04/2008 Si esclude il controllo per le azioni ActRicercaFSPRichiestaAtti,
		// ActRicercaStatoAtti e ActRicercaFSPSanzioniSostitutive
		// riferite al menu di "Richiesta Atti".
		if (mFasGPMod != null && mFasGPMod.getGeneraleProcedimentoModel() != null
				&& (mFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
						.compareTo(COD_OGGETTO_PROCEDIMENTO_MA) == 0
						|| mFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
								.compareTo(COD_OGGETTO_PROCEDIMENTO_SS) == 0
						|| mFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
								.compareTo(COD_OGGETTO_PROCEDIMENTO_MS) == 0)
				&& (!(this instanceof siap.sius.richiestaatti.action.ActRicercaFSPRichiestaAtti)
						&& !(this instanceof siap.sius.richiestaatti.action.ActRicercaFSPSanzioniSostitutive)
						&& !(this instanceof siap.sius.richiestaatti.action.ActRicercaStatoAtti))) {
			String lDescrOggettoProcedimento = mFasGPMod.getGeneraleProcedimentoModel()
					.getDescrOggettoProcedimento();
			throw new F3BException(F3BException.USER_MESSAGE,
					"Operazione non consentita per il Procedimento di " + lDescrOggettoProcedimento);
		} else {
			setRequestAttribute("fascicoloSiusGP", mFasGPMod);
			// Metto in sessione il fascicolo SIUS per consentire le funzionalità annesse.
			setSessionAttribute("fascicoloSiusGP", mFasGPMod);
			// 13/01/2004 Metto in sessione il fascicolo SIEP da cui ha origine il Fascicolo SIUS.
			IFascicoloSiep lCtrlSIEP = SIEPLookupRemote.getFascicoloSiepRemote();
			FascicoloSiepModel lFasSIEP = lCtrlSIEP.ExRicercaFascicoloByKeyNoError(
					mFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
			setSessionAttribute("fascicolo", lFasSIEP);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

		return lRetPage;
	}

}