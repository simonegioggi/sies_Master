package siap.sius.prescrizione.action;

import java.util.ArrayList;
import java.util.List;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.fascicolo.model.FascicoloGPModel;

/**
 * <p>
 * Title: ActLoadInserisciPrescrizioneNew
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Prescrizione per Sanzioni Sostitutive/ Pene Pecuniarie
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadInserisciPrescrizioneNew extends ActionSiap implements ICostantiPrescrizione {
   
	public String processRequest() throws Exception {

		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Preleva il cod Oggetto procedimento
		String mCodOggettoProc = lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento();
		String mCodTipoRegistro = lFasGPMod.getGeneraleProcedimentoModel().getCodTipoRegistro();
		if (mCodOggettoProc == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Oggetto Procedimento assente !");
		// Filtro per prescrizioni

		String FiltroPrescrizione = "";

		// In caso di Sanzioni Sostitutive o Conversione Pene pecuniarie.
		if (mCodOggettoProc.equals(ICostantiDepositoOrdinanzaPc.OGG_AUTO_SANZ_SOSTITUTIVE)
				|| mCodOggettoProc.equals(ICostantiDepositoOrdinanzaPc.OGG_APPL_SANZ_SOSTITUTIVE)
				// MEV_2023-35 si aggiunge un nuovo codice 
				|| mCodOggettoProc.equals(ICostantiDepositoOrdinanzaPc.OGG_APPL_PENE_SOSTITUTIVE)
				// MEV_2023-35 - FINE
				|| mCodOggettoProc.equals(ICostantiDepositoOrdinanzaPc.OGG_CONV_PENE_PECUNIARIE)) {
			FiltroPrescrizione = "ESS";
		}

		// In caso di Applicazione Misure Sicurezza o Dichiarazione Delinquenza Abitale
		if (mCodOggettoProc.equals(ICostantiDepositoOrdinanzaPc.OGG_APPL_MIS_SICUREZZA)
				|| mCodOggettoProc.equals(ICostantiDepositoOrdinanzaPc.OGG_DICH_DELINQ_ABITUALE)
				|| mCodTipoRegistro.equals("S09")) {
			FiltroPrescrizione = "AMS";
		}

		// Messaggio per mancanza filtro
		if (FiltroPrescrizione.equals(""))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun Filtro Prescrizioni");

		// Cerca le prescrizioni
		List lPrescrizioni = new ArrayList(DecodificheUtils.getDecodificheFiltrateByCodAltValorizzato(
				DecodificheManager.getInstance().getTipoPrescrizione(), FiltroPrescrizione));

		// Messaggio per prescrizioni non trovate
		if (lPrescrizioni.isEmpty())
			throw new SIUSException(SIUSException.USER_MESSAGE, "Non sono state trovate Prescrizioni");

		List parsePrescrizioni = DecodificheUtils.parsePrescrizioni(lPrescrizioni, null);

		// Inserisce nella Request le prescrizioni trovate e decodificate
		// Gestione Valida non solo per le Sanzioni Sostitutive ma anche per Pene Pecuniarie e Misure
		// Sicurezza
		setRequestAttribute("PrescrizioniSanzioniSostitutive", parsePrescrizioni);

		// Passaggio parametri
		String lNextAction = "siap.sius.depositodecreto.action.ActLoadDettaglioDecretoDeposito";
		if (!isRequestParameterNullObj("nextaction"))
			lNextAction = getRequestStringParameter("nextaction");

		setRequestAttribute("nextaction", lNextAction);
		setRequestAttribute("modalita", "I");
		setRequestAttribute(ICostantiEvento.CAMPO_ID_EVENTO,
				getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));

		return PG_LOAD_INSERISCIPRESCRIZIONE_NEW; // restituisce la jsp di VIEW
	}

}