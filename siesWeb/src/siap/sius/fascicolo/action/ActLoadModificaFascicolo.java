package siap.sius.fascicolo.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.esecuzionemisuraalternativa.controller.IEsecuzioneMA;
import siap.sius.esecuzionemisuraalternativa.model.EsecuzioneMisuraAlternativaModel;
import siap.sius.esecuzionemisurasicurezza.controller.IEsecuzioneMS;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.esecuzionesanzionesostitutiva.controller.IEsecuzioneSS;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.util.FascicoloUtils;
import siap.sius.util.SIUSLookupRemote;

public class ActLoadModificaFascicolo extends ActionSius implements ICostantiFascicoloSius {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		setLinkRitorno();
		BigDecimal lIdFascicolo = null;
		lIdFascicolo = getRequestBigDecimalParameter(CAMPO_ID_FASCICOLO_SIUS);

		// Riempio il model.
		FascicoloGPModel lFasGPMod = new FascicoloGPModel();
		lFasGPMod.getFascicoloSiusModel().setIdFascicoloSius(lIdFascicolo);

		IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
		lFasGPMod = lCtrl.ExRicercaFascicoloByKey(lIdFascicolo);

		setRequestAttribute("fascicoloSiusGP", lFasGPMod);
		// Metto in sessione il fascicolo SIUS per consentire le funzionalità annesse.
		setSessionAttribute("fascicoloSiusGP", lFasGPMod);

		// 01/08/2007 La modificabilità non viene testata per i fascicoli di E.M.A. e di E.S.S. e di E.M.S.
		if (lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U004") != 0
				&& lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U019") != 0
				&& lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
						.compareTo("U024") != 0) {
			if (this.IsFascicoloSiusModificabile() == false)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						ICostantiFascicoloSius.MSG_NON_MODIFICABILE);
		}

		// Lock
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "ProcedimentoSIUS",
				lIdFascicolo.toString(), getCodUtenteConnesso(), getSession().getId());
		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il  " + lck.getEntity() + " è in gestione ad un altro utente!<BR>Riprovare più tardi !");
			return IWebConstants.PG_MESSAGE;
		}

		// STUB Rimuovo dalla sessione un eventuale fascicolo SIEP precedente.
		if (!isSessionAttributeNullObj("fascicolo"))
			removeSessionAttribute("fascicolo");

		// Se valorizzato il campo FasSieIdFascicoloSiep, leggo e metto in sessione il Fascicolo SIEP.
		if (lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null) {
			FascicoloSiepModel lFasMod = new FascicoloSiepModel();
			lFasMod.setIdFascicoloSiep(lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());

			IFascicoloSiep lCtrlSiep = SIEPLookupRemote.getFascicoloSiepRemote();
			lFasMod = lCtrlSiep
					.ExRicercaFascicoloByKey(lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());

			setSessionAttribute("fascicolo", lFasMod);
		}

		// Preimposto tutti i campi di decodifica dai dati del model.
		// Imposta Tipo Atto.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAtto(),
				lFasGPMod.getGeneraleProcedimentoModel().getCodTipoAtto());
		setRequestAttribute("tipoAtto", "" + lOption);

		// Imposta Mittente Atto.
		lOption = new Option(DecodificheManager.getInstance().getMittenteAtto(),
				lFasGPMod.getGeneraleProcedimentoModel().getCodTipoMittenteAtto(), 36);
		setRequestAttribute("mittenteAtto", "" + lOption);

		// Imposta Contenuto.
		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		// MEV10-s3: aggiunte or condition per gestire trib. sorv. minori ed uff. sorv. minori
		if ("TDS".equals(strCodTipoUfficio) || "TDSM".equals(strCodTipoUfficio)) {
			if ("TDS".equals(strCodTipoUfficio))
				lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoTDS(),
						lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento(), 75);
			else if ("TDSM".equals(strCodTipoUfficio))
				lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoTDSM(),
						lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento(), 75);
			// MEV_66: aggiunta impostazione del contenuto
			setRequestAttribute("contenuto", "" + lOption);
		} else if ("UDS".equals(strCodTipoUfficio) || "UDSM".equals(strCodTipoUfficio)) {
			if ("UDS".equals(strCodTipoUfficio))
				lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoUDS(),
						lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento(), 75);
			else if ("UDSM".equals(strCodTipoUfficio))
				lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoUDSM(),
						lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento(), 75);
			// MEV_66: aggiunta impostazione del contenuto
			setRequestAttribute("contenuto", "" + lOption);
			if (lFasGPMod.getGeneraleProcedimentoModel().getCodTipoRegistro().equals("S22") && lFasGPMod
					.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U004") != 0) {
				// STUB 01/07/2004 Realizzazione filtro sui contenuti per "S22"
				FascicoloUtils lFascicoloUtils = new FascicoloUtils();
				// STUB 14/06/2006 String[] lFilter = lFascicoloUtils.filtraContenutiMA();
				String[] lFilter = lFascicoloUtils.filtraContenutiMAconU037();
				lOption.setFilter(lFilter);
				setRequestAttribute("contenutoEsecuzione", "" + lOption);
			}
			// STUB 01/08/2007 Realizzazione filtro sui contenuti per "S12"
			if (lFasGPMod.getGeneraleProcedimentoModel().getCodTipoRegistro().equals("S12") && lFasGPMod
					.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U019") != 0) {
				FascicoloUtils lFascicoloUtils = new FascicoloUtils();
				// MEV_66: aggiunto parametro di passaggio
				String[] lFilter = lFascicoloUtils.filtraContenutiSS(strCodTipoUfficio);
				lOption.setFilter(lFilter);
				setRequestAttribute("contenutoEsecuzione", "" + lOption);
			}
			// STUB 28/04/2011 Realizzazione filtro sui contenuti per "S09"
			if (lFasGPMod.getGeneraleProcedimentoModel().getCodTipoRegistro().equals("S09") && lFasGPMod
					.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U024") != 0) {
				FascicoloUtils lFascicoloUtils = new FascicoloUtils();
				// MEV63: aggiunto parametro di passaggio per distinzione ufficio minori
				String[] lFilter = lFascicoloUtils.filtraContenutiMS(strCodTipoUfficio);
				lOption.setFilter(lFilter);
				setRequestAttribute("contenutoEsecuzione", "" + lOption);
			}

			// Imposta la Collection Contenuto.
			// MEV_66: distinguo per ufficio minorile
			Collection lCol;
			if ("UDSM".equals(strCodTipoUfficio))
				lCol = (DecodificheManager.getInstance()).getOggettoProcedimentoUDSM();
			else
				lCol = (DecodificheManager.getInstance()).getOggettoProcedimentoUDS();
			// FINE MEV_66
			setRequestAttribute("collContenuto", lCol);
		} else {
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimento(),
					lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento(), 75);
			// MEV_66: aggiunta impostazione del contenuto
			setRequestAttribute("contenuto", "" + lOption);
		}

		// Imposta la posizione giuridica.
		String posGiuridica = lFasGPMod.getGeneraleProcedimentoModel().getCodPosGiuridica();

		lOption = new Option(DecodificheManager.getInstance().getPosizioneGiuridicaEsecuzione(),
				posGiuridica);

		setRequestAttribute("posizioneGiuridica", "" + lOption);

		// Imposta il magistrato.
		// 18/12/2003 COD_MAGISTRATO Impostato in COD_AUTORITA_DELEGATA di GeneraleProcedimento.
		String magistrato = lFasGPMod.getGeneraleProcedimentoModel().getCodAutoritaDelegata();

		String lCodUfficio = getCodUfficioUtenteConnesso();
		IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
		// Modifica del 18/11/2016 MEV_50
		// Vengono recuperati solo i Magistrati ancora in servizio
		if (Utils.isNullObj(magistrato))
			// lOption = new Option(lMagCtrl.ExElencoCbxMagistratiByCodUfficio(lCodUfficio));
			lOption = new Option(lMagCtrl.ExElencoCbxMagistratiValidiByCodUfficio(lCodUfficio));
		else
			// lOption = new Option(lMagCtrl.ExElencoCbxMagistratiByCodUfficio(lCodUfficio), magistrato);
			lOption = new Option(lMagCtrl.ExElencoCbxMagistratiValidiByCodUfficio(lCodUfficio), magistrato);

		setRequestAttribute("magistrato", "" + lOption);

		// Imposta la modalità per la modifica.
		setRequestAttribute("modalita", "M");

		// STUB 04/05/2004 Se UDS o UDSM Smista su PG_LOAD_MODIFICAFASCICOLOSIUSUDS
		// MEV_66: aggiungo per ufficio minorile
		if ("UDS".equals(strCodTipoUfficio) || "UDSM".equals(strCodTipoUfficio)) {
			// STUB 13/12/2004 Se il Procedimento è di E.M.A (U004) viene effettuata la lettura dell'E.M.A.
			if (lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U004") == 0) {
				IEsecuzioneMA lEMACtrl = SIUSLookupRemote.getEsecuzioneMARemote();
				EsecuzioneMisuraAlternativaModel lEMAModel = lEMACtrl
						.ExRicercaEsecuzioneMisuraAlternativaByIdFascicolo(
								lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

				if (lEMAModel == null || lEMAModel.getIdEsecuzioneMisuraAlternati().equals(null))
					throw new SIUSException(SIUSException.USER_MESSAGE,
							"Attenzione! ESECUZIONE MISURA ALTERNATIVA Assente!");
				else
					setRequestAttribute("misuraAlternativa", lEMAModel);

				// STUB 29/12/2004 Occorre segnalare la presenza di provvedimenti di EMA (figli).
				Vector lVect = lEMACtrl.ExRicercaDettaglioEsecuzioneMA(
						lEMAModel.getIdEsecuzioneMisuraAlternati(),
						lFasGPMod.getFascicoloSiusModel().getSogIdSoggetto(),
						this.getCodUfficioUtenteConnesso());
				if (!lVect.isEmpty())
					setRequestAttribute("figliEsecuzione", "S");
			}
			// STUB 01/08/2007 Se il Procedimento è di E.S.S. (U019) viene effettuata la lettura dell'E.S.S.
			if (lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U019") == 0) {
				IEsecuzioneSS lESSCtrl = SIUSLookupRemote.getEsecuzioneSSRemote();
				EsecuzioneSanzioneSostitutivaModel lESSModel = lESSCtrl
						.ExRicercaEsecuzioneSanzioneSostitutivaByIdFascicolo(
								lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

				if (lESSModel == null || lESSModel.getIdEsecuzioneSanzioneSost().equals(null))
					throw new SIUSException(SIUSException.USER_MESSAGE,
							"Attenzione! ESECUZIONE SANZIONE SOSTITUTIVA Assente!");
				else
					setRequestAttribute("sanzioneSostitutiva", lESSModel);

				// Occorre segnalare la presenza di provvedimenti di E.S.S. (figli).
				Vector lVect = lESSCtrl.ExRicercaDettaglioEsecuzioneSS(
						lESSModel.getIdEsecuzioneSanzioneSost(),
						lFasGPMod.getFascicoloSiusModel().getSogIdSoggetto(),
						this.getCodUfficioUtenteConnesso());
				if (!lVect.isEmpty())
					setRequestAttribute("figliEsecuzione", "S");

			}
			// STUB 28/04/2011 Se il Procedimento è di E.M.S. (U024) viene effettuata la lettura dell'E.M.S.
			if (lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U024") == 0) {
				IEsecuzioneMS lEMSCtrl = SIUSLookupRemote.getEsecuzioneMSRemote();
				EsecuzioneMisuraSicurezzaModel lEMSModel = lEMSCtrl
						.ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo(
								lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

				if (lEMSModel == null || lEMSModel.getIdEsecuzioneMisuraSicurezza().equals(null))
					throw new SIUSException(SIUSException.USER_MESSAGE,
							"Attenzione! ESECUZIONE MISURA SICUREZZA Assente!");
				else
					setRequestAttribute("misuraSicurezza", lEMSModel);

				// Occorre segnalare la presenza di provvedimenti di E.S.S. (figli).
				Vector lVect = lEMSCtrl.ExRicercaDettaglioEsecuzioneMS(
						lEMSModel.getIdEsecuzioneMisuraSicurezza(),
						lFasGPMod.getFascicoloSiusModel().getSogIdSoggetto(),
						this.getCodUfficioUtenteConnesso());
				if (!lVect.isEmpty())
					setRequestAttribute("figliEsecuzione", "S");

			}
			return PG_LOAD_MODIFICAFASCICOLOSIUSUDS;
		} else
			return PG_LOAD_INSERISCIFASCICOLOSIUS;
	}

}