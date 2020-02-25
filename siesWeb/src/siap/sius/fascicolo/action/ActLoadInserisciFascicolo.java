package siap.sius.fascicolo.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

public class ActLoadInserisciFascicolo extends ActionSiap implements ICostantiFascicoloSius {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		if (isSessionAttributeNullObj("fascicolo"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Selezionare il procedimento.");

		// Dettaglio Fascicolo SIEP.
		FascicoloSiepModel fascicolo = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		setRequestAttribute("fascicolo", fascicolo);

		// Imposta Tipo Atto.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAtto());
		setRequestAttribute("tipoAtto", "" + lOption);

		// Imposta Mittente Atto.
		lOption = new Option(DecodificheManager.getInstance().getMittenteAtto(), 60);
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

		// Imposta in dettagliofascicolo la Posizione Giuridica e la pena residua per il Fascicolo SIEP.

		// FascicoloSiepController lCtrl = new FascicoloSiepController();
		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();

		DettaglioFascicoloModel lDettaglio = null;

		lDettaglio = lCtrl.ExDettaglioFascicoloSiep(fascicolo.getIdFascicoloSiep());

		if (lDettaglio == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Fascicolo non presente");

		// Valorizzo la data fine pena e la posizione giuridica in 2 variabili poste nella request.
		Date dataFinePena = null;
		String posGiuridica = "-";

		if ((!Utils.isNullObj(lDettaglio.getPenaResidua()))
				&& (!Utils.isNullObj(lDettaglio.getPenaResidua().getDataFine()))
				&& (!Utils.isNullObj(lDettaglio.getPenaResidua().getFlagValidato()))
				&& lDettaglio.getPenaResidua().getFlagValidato().equals("S"))
			dataFinePena = lDettaglio.getPenaResidua().getDataFine();

		setRequestAttribute("dataFinePena", dataFinePena);

		// Imposta la posizione giuridica.
		// la Collection lPosizioneGiuridica viene composta dai 3 gruppi distinti di P.G.
		// 5/7/2006 - Modifica per lo spupazzamento delle combo della posizione giuridica
		// Messo new al posto di una eguaglianza tra reference....
		Collection lCollPosGiuIscrizione = DecodificheManager.getInstance().getPosizioneGiuridicaIscrizione();
		// Collection lCollPosGiuEsecuzione =
		// DecodificheManager.getInstance().getPosizioneGiuridicaEsecuzione();
		// Collection lCollPosGiuAltra = DecodificheManager.getInstance().getPosizioneGiuridicaAltraCausa();
		ArrayList lPosizioneGiuridica = new ArrayList(lCollPosGiuIscrizione);
		// boolean lConcat = lPosizioneGiuridica.addAll(lCollPosGiuEsecuzione);
		// lConcat = lPosizioneGiuridica.addAll(lCollPosGiuAltra);

		if ((lDettaglio.getPosizioneGiuridica() != null)) {
			posGiuridica = lDettaglio.getPosizioneGiuridica().getCodPosizioneGiuridica();
			// STUB 07/04/2004 lOption = new Option(
			// DecodificheManager.getInstance().getPosizioneGiuridicaEsecuzione(),
			// lDettaglio.getPosizioneGiuridica().getCodPosizioneGiuridica(), 66 );
			lOption = new Option(lPosizioneGiuridica, lDettaglio.getPosizioneGiuridica()
					.getCodPosizioneGiuridica(), 66);
		} else
			// STUB 07/04/2004 lOption = new Option(
			// DecodificheManager.getInstance().getPosizioneGiuridicaEsecuzione(), 66 );
			lOption = new Option(lPosizioneGiuridica, 66);

		setRequestAttribute("posGiuridica", posGiuridica);

		setRequestAttribute("posizioneGiuridica", "" + lOption);

		// Imposta l'elenco magistrati.
		String lCodUfficio = getCodUfficioUtenteConnesso();
		IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
		// Modifica del 18/11/2016 MEV_50
		// Vengono recuperati solo i Magistrati ancora in servizio
		// lOption = new Option( lMagCtrl.ExElencoCbxMagistratiByCodUfficio( lCodUfficio ) );
		lOption = new Option(lMagCtrl.ExElencoCbxMagistratiValidiByCodUfficio(lCodUfficio));
		setRequestAttribute("magistrato", "" + lOption);

		// STUB 29/12/2003 Imposta il luogo detenzione.
		// Attenzione: se il luogo detenzione è per la causa attuale, viene valorizzato idLuogoDetenzione;
		// se il luogo detenzione è per altra causa, viene valorizzato idAltraCausa.
		String luogoDetenzione = "";
		String idLuogoDetenzione = "";
		String idAltraCausa = "";

		if ((lDettaglio.getLuogoDetenzione() != null)
				&& (lDettaglio.getLuogoDetenzione().getIstDetIdIstitutoDetenzione() != null)
				&& (lDettaglio.getLuogoDetenzione().getDataFineDetenzione() == null)) {
			if (lDettaglio.getLuogoDetenzione().getIstitutoDetenzione().getDescrizione() != null)
				luogoDetenzione = lDettaglio.getLuogoDetenzione().getIstitutoDetenzione().getDescrizione();
			else
				luogoDetenzione = lDettaglio.getLuogoDetenzione().getIstitutoDetenzione()
						.getDescrTipoIstituto()
						+ " - " + lDettaglio.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune();

			idLuogoDetenzione = lDettaglio.getLuogoDetenzione().getIdLuogoDetenzione().toString();
		}

		else if ((lDettaglio.getAltraCausa() != null)
				&& lDettaglio.getAltraCausa().getIstDetIdIstitutoDetenzione() != null) {
			if (lDettaglio.getAltraCausa().getIstitutoDetenzione().getDescrizione() != null)
				luogoDetenzione = lDettaglio.getAltraCausa().getIstitutoDetenzione().getDescrizione();
			else
				luogoDetenzione = lDettaglio.getAltraCausa().getIstitutoDetenzione().getDescrTipoIstituto()
						+ " - " + lDettaglio.getAltraCausa().getIstitutoDetenzione().getDescrComune();

			idAltraCausa = lDettaglio.getAltraCausa().getIdAltraCausa().toString();
			if (lDettaglio.getLuogoDetenzione() != null
					&& lDettaglio.getLuogoDetenzione().getIdLuogoDetenzione() != null)
				idLuogoDetenzione = lDettaglio.getLuogoDetenzione().getIdLuogoDetenzione().toString();
		}

		setRequestAttribute("luogoDetenzione", luogoDetenzione);
		setRequestAttribute("idLuogoDetenzione", idLuogoDetenzione);
		setRequestAttribute("idAltraCausa", idAltraCausa);

		setRequestAttribute("modalita", "IF");
		// Redirect per tornare indietro
		String lTornaQui = StringUtils.urlEncode(IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=" + this.getRequest().getParameter(IWebConstants.ACTION_FIELD));
		setRequestAttribute("TornaQui", lTornaQui);

		return PG_LOAD_INSERISCIFASCICOLOSIUS;
	}

}