package siap.sius.fascicolo.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

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
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

public class ActLoadInserisciFascicoloDaSius extends ActionSiap implements ICostantiFascicoloSius {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// Recupero del fascicolo Sius origine dalla sessione.
		if (isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Selezionare il procedimento SIUS.");

		FascicoloGPModel lFasGPOrigine = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

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
		if (strCodTipoUfficio.equals("TDS"))
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoTDS(), 75);
		else if (strCodTipoUfficio.equals("UDS"))
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoUDS(), 75);
		else if (strCodTipoUfficio.equals("TDSM"))
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoTDSM(), 75);
		else if (strCodTipoUfficio.equals("UDSM"))
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoUDSM(), 75);
		else
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimento(), 75);
		setRequestAttribute("contenuto", "" + lOption);

		String posGiuridica = "";
		String luogoDetenzione = "";
		String idLuogoDetenzione = "";
		String idAltraCausa = "";

		// Imposta l'elenco magistrati.
		String lCodUfficio = getCodUfficioUtenteConnesso();
		IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
		// Modifica del 18/11/2016 MEV_50
		// Vengono recuperati solo i Magistrati ancora in servizio
		// lOption = new Option( lMagCtrl.ExElencoCbxMagistratiByCodUfficio( lCodUfficio ) );
		lOption = new Option(lMagCtrl.ExElencoCbxMagistratiValidiByCodUfficio(lCodUfficio));
		setRequestAttribute("magistrato", "" + lOption);

		// Imposta la posizione giuridica.
		// La Collection lPosizioneGiuridica viene composta dai 3 gruppi distinti di P.G.
		Collection lCollPosGiuIscrizione = DecodificheManager.getInstance().getPosizioneGiuridicaIscrizione();
		Collection lCollPosGiuEsecuzione = DecodificheManager.getInstance().getPosizioneGiuridicaEsecuzione();
		Collection lCollPosGiuAltra = DecodificheManager.getInstance().getPosizioneGiuridicaAltraCausa();
		ArrayList lPosizioneGiuridica = new ArrayList(lCollPosGiuIscrizione);
		lPosizioneGiuridica.addAll(lCollPosGiuEsecuzione);
		lPosizioneGiuridica.addAll(lCollPosGiuAltra);
		lOption = new Option(lPosizioneGiuridica, 66);
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
			fascicolo = lCtrlSiep.ExRicercaFascicoloByKey(lFasGPOrigine.getFascicoloSiusModel()
					.getFasSieIdFascicoloSiep());

			setSessionAttribute("fascicolo", fascicolo);
		}

		// Eredita Luogo detenzione dal fascicolo sius padre se presente
		LuogoDetenzioneModel luogoDetenzioneSIUS = new LuogoDetenzioneModel();
		ILuogoDetenzione lCtrlLuoDetSius = SIEPLookupRemote.getLuogoDetenzioneRemote();
		luogoDetenzioneSIUS = lCtrlLuoDetSius.ExRicercaLuogoDetenzioneCorrenteByFascicoloSius(lFasGPOrigine
				.getFascicoloSiusModel().getIdFascicoloSius());

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
			if (fascicolo.getIdFascicoloSiep().compareTo(
					lFasGPOrigine.getFascicoloSiusModel().getFasSieIdFascicoloSiep()) == 0) {
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
						&& lDettaglio.getPenaResidua().getFlagValidato().equals("S"))
					dataFinePena = lDettaglio.getPenaResidua().getDataFine();

				setRequestAttribute("dataFinePena", dataFinePena);

				if ((lDettaglio.getPosizioneGiuridica() != null)) {
					posGiuridica = lDettaglio.getPosizioneGiuridica().getCodPosizioneGiuridica();
					lOption = new Option(lPosizioneGiuridica, lDettaglio.getPosizioneGiuridica()
							.getCodPosizioneGiuridica(), 66);
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
									.getDescrTipoIstituto()
									+ " - "
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
									.getDescrTipoIstituto()
									+ " - "
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
		setRequestAttribute("fascicolo", fascicolo);
		setRequestAttribute("posGiuridica", posGiuridica);
		setRequestAttribute("luogoDetenzione", luogoDetenzione);
		setRequestAttribute("idLuogoDetenzione", idLuogoDetenzione);
		setRequestAttribute("idAltraCausa", idAltraCausa);

		// Redirect per tornare indietro
		String lTornaQui = StringUtils.urlEncode(IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=" + this.getRequest().getParameter(IWebConstants.ACTION_FIELD));
		setRequestAttribute("TornaQui", lTornaQui);

		// Impostazione Modalità ---> IS = "Inserimento da Soggetto";
		// IF = Inserimento da Fascicolo SIEP;
		if (lDettaglio == null) {
			setRequestAttribute("modalita", "IS");
		} else
			setRequestAttribute("modalita", "IF");

		return PG_LOAD_INSERISCIFASCICOLOSIUS;
	}

}