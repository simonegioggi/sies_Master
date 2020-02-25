package siap.sius.fascicolo.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.controller.ILuogoDetenzione;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.util.FascicoloUtils;

public class ActLoadInserisciFascicoloDaSiusUDS extends ActionSiap implements ICostantiFascicoloSius {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// Recupero del fascicolo Sius origine dalla sessione.
		if (isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Selezionare il procedimento SIUS.");

		FascicoloGPModel lFasGPOrigine = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Se il Procedimento SIUS Origine è di EMA, si Passa nella Request come fascicoloEsecuzione per
		// consentire l'impostazione dei campi non modificabili.
		// 01/08/2007 Se il Procedimento SIUS Origine è di ESS, si Passa nella Request come
		// fascicoloEsecuzione per consentire l'impostazione dei campi non modificabili.
		if (lFasGPOrigine.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U004") == 0
				|| lFasGPOrigine.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
						.compareTo("U019") == 0)
			setRequestAttribute("fascicoloEsecuzione", lFasGPOrigine);

		// Si passa l'Id del fascicolo origine nella request.
		String idFascicoloOrigine = lFasGPOrigine.getFascicoloSiusModel().getIdFascicoloSius().toString();
		setRequestAttribute("idFascicoloOrigine", idFascicoloOrigine);

		// Imposta Tipo Atto.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAtto());
		setRequestAttribute("tipoAtto", "" + lOption);

		// Imposta Mittente Atto.
		lOption = new Option(DecodificheManager.getInstance().getMittenteAtto(), 36);
		setRequestAttribute("mittenteAtto", "" + lOption);

		// Imposta Contenuto.
		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		// MERGE v10 COLLAUDO: aggiunta casistica per i minorenni
		if ("UDSM".equals(strCodTipoUfficio))
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoUDSM(), 75);
		else
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoUDS(), 75);

		setRequestAttribute("contenuto", "" + lOption);

		// STUB 01/07/2004 Realizzazione filtro sui contenuti per "S22"
		FascicoloUtils lFascicoloUtils = new FascicoloUtils();
		// 20170320: aggiunto parametro di passaggio
		String[] lFilter = lFascicoloUtils.filtraContenutiMA(strCodTipoUfficio);
		lOption.setFilter(lFilter);
		setRequestAttribute("contenutoEsecuzione", "" + lOption);

		// STUB 01/08/2007 Differenziato filtro sui contenuti per "S12"
		if (lFasGPOrigine.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U019") == 0) {
			// MEV_66: aggiunto parametro di passaggio
			lFilter = lFascicoloUtils.filtraContenutiSS(strCodTipoUfficio);
			lOption.setFilter(lFilter);
			setRequestAttribute("contenutoEsecuzione", "" + lOption);
		}

		// STUB 28/04/2011 Differenziato filtro sui contenuti per "S09"
		if (lFasGPOrigine.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U024") == 0) {
			// MEV63: aggiunto parametro di passaggio per distinzione ufficio minori
			lFilter = lFascicoloUtils.filtraContenutiMS(strCodTipoUfficio);
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

		String posGiuridica = "";
		String luogoDetenzione = "";
		String idLuogoDetenzione = "";
		String idAltraCausa = "";

		// Imposta l'elenco magistrati.
		String lCodUfficio = getCodUfficioUtenteConnesso();
		IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
		// Modifica del 18/11/2016 MEV_50
		// Vengono recuperati solo i Magistrati ancora in servizio
		// lOption = new Option(lMagCtrl.ExElencoCbxMagistratiByCodUfficio(lCodUfficio));
		lOption = new Option(lMagCtrl.ExElencoCbxMagistratiValidiByCodUfficio(lCodUfficio));

		if (lFasGPOrigine.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U004") == 0
				|| lFasGPOrigine.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
						.compareTo("U019") == 0) {
			lOption.setSelected(lFasGPOrigine.getGeneraleProcedimentoModel().getCodAutoritaDelegata());
		}
		setRequestAttribute("magistrato", "" + lOption);

		// Imposta la posizione giuridica.
		// La Collection lPosizioneGiuridica viene composta dai 3 gruppi distinti di P.G.
		// 5/7/2006 - Modifica per evitare l'imbroglio delle combo della posizione giuridica
		// Messo new al posto di una eguaglianza tra reference....
		Collection lCollPosGiuIscrizione = DecodificheManager.getInstance().getPosizioneGiuridicaIscrizione();
		Collection lCollPosGiuEsecuzione = DecodificheManager.getInstance().getPosizioneGiuridicaEsecuzione();
		Collection lCollPosGiuAltra = DecodificheManager.getInstance().getPosizioneGiuridicaAltraCausa();
		ArrayList lPosizioneGiuridica = new ArrayList(lCollPosGiuIscrizione);
		lPosizioneGiuridica.addAll(lCollPosGiuEsecuzione);
		lPosizioneGiuridica.addAll(lCollPosGiuAltra);
		lOption = new Option(lPosizioneGiuridica, 66);
		if (lFasGPOrigine.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U004") == 0
				|| lFasGPOrigine.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
						.compareTo("U019") == 0) {
			if ((lFasGPOrigine.getGeneraleProcedimentoModel().getCodPosGiuridica() != null))
				lOption.setSelected(lFasGPOrigine.getGeneraleProcedimentoModel().getCodPosGiuridica());
		}
		setRequestAttribute("posizioneGiuridica", "" + lOption);

		// Si Legge e si pone in sessione il soggetto.
		SoggettoModel lSogMod = new SoggettoModel();
		ISoggetto lCtrlSog = SICOLookupRemote.getSoggettoRemote();
		lSogMod = lCtrlSog.ExRicercaSoggettoByKey(lFasGPOrigine.getFascicoloSiusModel().getSogIdSoggetto());
		setSessionAttribute("soggetto", lSogMod);
		setRequestAttribute("soggetto", lSogMod);

		// Rimuovo dalla sessione un eventuale fascicolo SIEP precedente ed eventualmente metto in sessione
		// quello relativo al procedimento origine.
		if (!isSessionAttributeNullObj("fascicolo"))
			removeSessionAttribute("fascicolo");

		// Se valorizzato il campo FasSieIdFascicoloSiep del Fascicolo SIUS origine, leggo e metto in sessione
		// il Fascicolo SIEP.
		FascicoloSiepModel fascicolo = new FascicoloSiepModel();
		if (lFasGPOrigine.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null) {
			fascicolo.setIdFascicoloSiep(lFasGPOrigine.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
			IFascicoloSiep lCtrlSiep = SIEPLookupRemote.getFascicoloSiepRemote();
			fascicolo = lCtrlSiep.ExRicercaFascicoloByKey(
					lFasGPOrigine.getFascicoloSiusModel().getFasSieIdFascicoloSiep());

			setSessionAttribute("fascicolo", fascicolo);
		}

		// Eredita Luogo detenzione dal fascicolo sius padre se presente
		LuogoDetenzioneModel luogoDetenzioneSIUS = new LuogoDetenzioneModel();
		ILuogoDetenzione lCtrlLuoDetSius = SIEPLookupRemote.getLuogoDetenzioneRemote();
		luogoDetenzioneSIUS = lCtrlLuoDetSius.ExRicercaLuogoDetenzioneCorrenteByFascicoloSius(
				lFasGPOrigine.getFascicoloSiusModel().getIdFascicoloSius());

		if ((luogoDetenzioneSIUS != null) && (luogoDetenzioneSIUS.getDataFineDetenzione() == null)
				&& (luogoDetenzioneSIUS.getIstDetIdIstitutoDetenzione() != null)) {
			if (luogoDetenzioneSIUS.getIstitutoDetenzione().getDescrizione() != null)
				luogoDetenzione = luogoDetenzioneSIUS.getIstitutoDetenzione().getDescrizione();
			else
				luogoDetenzione = luogoDetenzioneSIUS.getIstitutoDetenzione().getDescrTipoIstituto() + " - "
						+ luogoDetenzioneSIUS.getIstitutoDetenzione().getDescrComune();

			idLuogoDetenzione = luogoDetenzioneSIUS.getIdLuogoDetenzione().toString();
		}

		// Fascicolo SIEP eventualmente appena posto in sessione.
		DettaglioFascicoloModel lDettaglio = null;
		if (!isSessionAttributeNullObj("fascicolo")) {
			if (fascicolo.getIdFascicoloSiep()
					.compareTo(lFasGPOrigine.getFascicoloSiusModel().getFasSieIdFascicoloSiep()) == 0) {
				// Imposta in dettagliofascicolo la Posizione Giuridica e la pena residua per il Fascicolo
				// SIEP.
				IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();

				lDettaglio = lCtrl.ExDettaglioFascicoloSiep(fascicolo.getIdFascicoloSiep());

				if (lDettaglio == null)
					throw new F3BException(F3BException.USER_MESSAGE, "Fascicolo non presente");

				// Valorizzo la data fine pena e la posizione giuridica in 2 variabili poste nella request.
				Date dataFinePena = null;

				if ((!Utils.isNullObj(lDettaglio.getPenaResidua()))
						&& (!Utils.isNullObj(lDettaglio.getPenaResidua().getDataFine()))
						&& (!Utils.isNullObj(lDettaglio.getPenaResidua().getFlagValidato()))
						&& lDettaglio.getPenaResidua().getFlagValidato().equals("S")
						&& (lFasGPOrigine.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
								.compareTo("U004") != 0)
						&& (lFasGPOrigine.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
								.compareTo("U019") != 0))
					dataFinePena = lDettaglio.getPenaResidua().getDataFine();

				setRequestAttribute("dataFinePena", dataFinePena);

				if ((lDettaglio.getPosizioneGiuridica() != null)
						&& (lFasGPOrigine.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
								.compareTo("U004") != 0)
						&& (lFasGPOrigine.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
								.compareTo("U019") != 0)) {
					posGiuridica = lDettaglio.getPosizioneGiuridica().getCodPosizioneGiuridica();
					lOption = new Option(lPosizioneGiuridica,
							lDettaglio.getPosizioneGiuridica().getCodPosizioneGiuridica(), 66);
					setRequestAttribute("posizioneGiuridica", "" + lOption);
				}

				// Imposta il luogo detenzione.
				// Attenzione: se il luogo detenzione è per la causa attuale, viene valorizzato
				// idLuogoDetenzione;
				// se il luogo detenzione è per altra causa, viene valorizzato idAltraCausa.

				if ((luogoDetenzione != null) && (luogoDetenzione.length() <= 0)) {

					if ((lDettaglio.getLuogoDetenzione() != null)
							&& (lDettaglio.getLuogoDetenzione().getIstDetIdIstitutoDetenzione() != null)
							&& (lDettaglio.getLuogoDetenzione().getDataFineDetenzione() == null)) {
						if (lDettaglio.getLuogoDetenzione().getIstitutoDetenzione().getDescrizione() != null)
							luogoDetenzione = lDettaglio.getLuogoDetenzione().getIstitutoDetenzione()
									.getDescrizione();
						else
							luogoDetenzione = lDettaglio.getLuogoDetenzione().getIstitutoDetenzione()
									.getDescrTipoIstituto() + " - "
									+ lDettaglio.getLuogoDetenzione().getIstitutoDetenzione()
											.getDescrComune();

						idLuogoDetenzione = lDettaglio.getLuogoDetenzione().getIdLuogoDetenzione().toString();
					}

					else if ((lDettaglio.getAltraCausa() != null)
							&& lDettaglio.getAltraCausa().getIstDetIdIstitutoDetenzione() != null) {
						if (lDettaglio.getAltraCausa().getIstitutoDetenzione().getDescrizione() != null)
							luogoDetenzione = lDettaglio.getAltraCausa().getIstitutoDetenzione()
									.getDescrizione();
						else
							luogoDetenzione = lDettaglio.getAltraCausa().getIstitutoDetenzione()
									.getDescrTipoIstituto() + " - "
									+ lDettaglio.getAltraCausa().getIstitutoDetenzione().getDescrComune();

						idAltraCausa = lDettaglio.getAltraCausa().getIdAltraCausa().toString();
						if (lDettaglio.getLuogoDetenzione() != null
								&& lDettaglio.getLuogoDetenzione().getIdLuogoDetenzione() != null)
							idLuogoDetenzione = lDettaglio.getLuogoDetenzione().getIdLuogoDetenzione()
									.toString();
					}
				}
			}
		}

		// I Procedimenti Collegati ad un EMA-ESS ereditano Posizione Giuridica, Fine Pena, Luogo Detenzione
		// Residenza, Domicilio, Collaboratore

		if (lFasGPOrigine.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U004") == 0
				|| lFasGPOrigine.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
						.compareTo("U019") == 0) {
			LuogoDetenzioneModel luoDetMod;

			ILuogoDetenzione lCtrl = SIEPLookupRemote.getLuogoDetenzioneRemote();
			luoDetMod = lCtrl.ExRicercaLuogoDetenzioneCorrenteByFascicoloSius(
					lFasGPOrigine.getFascicoloSiusModel().getIdFascicoloSius());

			if ((luoDetMod != null) && (luoDetMod.getIstDetIdIstitutoDetenzione() != null)) {
				if (luoDetMod.getIstitutoDetenzione().getDescrizione() != null)
					luogoDetenzione = luoDetMod.getIstitutoDetenzione().getDescrizione();
				else
					luogoDetenzione = luoDetMod.getIstitutoDetenzione().getDescrTipoIstituto() + " - "
							+ luoDetMod.getIstitutoDetenzione().getDescrComune();

				idLuogoDetenzione = luoDetMod.getIdLuogoDetenzione().toString();
			}

		}

		setRequestAttribute("fascicolo", fascicolo);
		setRequestAttribute("posGiuridica", posGiuridica);
		setRequestAttribute("luogoDetenzione", luogoDetenzione);
		setRequestAttribute("idLuogoDetenzione", idLuogoDetenzione);
		setRequestAttribute("idAltraCausa", idAltraCausa);

		// Redirect per tornare indietro
		String lTornaQui = StringUtils.urlEncode(IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=" + this.getRequest().getParameter(IWebConstants.ACTION_FIELD));
		setRequestAttribute("TornaQui", lTornaQui);

		// Selezione della FORM di Inserimento.
		if (lDettaglio == null) {
			// Impostazione della modalità - IS = Inserimento Da Soggetto.
			// IM = Inserimento Procedimento di Esecuzione Misura.
			// 10/10/2007 Se il Procedimento SIUS Origine è di EMM o di ESS, si Imposta un diverso
			// inserimento.
			if (lFasGPOrigine.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
					.compareTo("U004") == 0
					|| lFasGPOrigine.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
							.compareTo("U019") == 0) {
				setRequestAttribute("modalita", "IM");
				return PG_LOAD_INSERISCIFASCICOLOSIUSUDS;
			} else {
				setRequestAttribute("modalita", "IS");
				return PG_LOAD_INSERISCIFASCICOLODASOGGETTOUDS;
			}
		} else {
			// Impostazione della modalità - IU = Inserimento Fascicolo per UDS.
			// IE = Inserimento Procedimento di Esecuzione.
			// 10/10/2007 Se il Procedimento SIUS Origine è di EMM o di ESS, si Imposta un diverso
			// inserimento.
			if (lFasGPOrigine.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
					.compareTo("U004") == 0
					|| lFasGPOrigine.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
							.compareTo("U019") == 0)
				// setRequestAttribute("modalita", "IE");
				// Se il SIUS Origine è di EMM o di ESS eredito comunque dall'origine
				setRequestAttribute("modalita", "IM");
			else
				setRequestAttribute("modalita", "IU");

			return PG_LOAD_INSERISCIFASCICOLOSIUSUDS;
		}
	}

}