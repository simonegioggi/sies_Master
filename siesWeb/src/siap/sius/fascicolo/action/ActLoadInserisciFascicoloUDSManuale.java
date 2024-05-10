package siap.sius.fascicolo.action;

import java.math.BigDecimal;
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
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.util.FascicoloUtils;
import siap.sius.util.SIUSLookupRemote;

/**
 *
 * <p>
 * Title: ActLoadInserisciFascicoloUDSManuale
 * </p>
 * <p>
 * Description: Load della form dell'inserimento del fascicolo UDS manuale
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
public class ActLoadInserisciFascicoloUDSManuale extends ActionSiap implements ICostantiFascicoloSius {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// Gestione del Bottone di ritorno.
		this.setLinkRitorno();

		String idFascicoloSius = null;
		if (!this.isRequestParameterNullObj("IdFascicoloSius"))
			idFascicoloSius = getRequestStringParameter("IdFascicoloSius");

		FascicoloGPModel lFasGPMod = new FascicoloGPModel(); // 31/07/2007

		// Se provengo dalla Ricerca Procedimenti di Esecuzione, ho il parametro "IdFascicoloSius" nella
		// request.
		// Con IdFascicoloSius leggo i dati del Procedimento SIUS e del Procedimento SIEP.
		if (idFascicoloSius != null && idFascicoloSius.length() > 1) {
			lFasGPMod.getFascicoloSiusModel().setIdFascicoloSius(new BigDecimal(idFascicoloSius));

			IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			lFasGPMod = lCtrl.ExRicercaFascicoloByKey(getRequestBigDecimalParameter(CAMPO_ID_FASCICOLO_SIUS));

			// Si Passa nella Request il fascicolo SIUS per consentire l'impostazione dei campi non
			// modificabili.
			setRequestAttribute("fascicoloEsecuzione", lFasGPMod);

			// Rimuovo dalla sessione un eventuale fascicolo SIEP precedente.
			if (!isSessionAttributeNullObj("fascicolo"))
				removeSessionAttribute("fascicolo");

			// Se valorizzato il campo FasSieIdFascicoloSiep, leggo e metto in sessione il Fascicolo SIEP.
			if (lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null) {
				FascicoloSiepModel lFasMod = new FascicoloSiepModel();
				lFasMod.setIdFascicoloSiep(lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());

				IFascicoloSiep lCtrlSiep = SIEPLookupRemote.getFascicoloSiepRemote();
				lFasMod = lCtrlSiep.ExRicercaFascicoloByKey(
						lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());

				setSessionAttribute("fascicolo", lFasMod);
			}
		}

		if (isSessionAttributeNullObj("fascicolo"))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Procedimento SIEP di Origine non Individuato.");

		// Dettaglio Fascicolo SIEP.
		FascicoloSiepModel fascicolo = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		setRequestAttribute("fascicolo", getSessionAttribute("fascicolo"));

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

		// STUB 31/07/2007 Differenziato filtro sui contenuti per "S12"
		if (idFascicoloSius != null && idFascicoloSius.length() > 1 && lFasGPMod
				.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U019") == 0) {
			// MEV_66: aggiunto parametro di passaggio
			lFilter = lFascicoloUtils.filtraContenutiSS(strCodTipoUfficio);
			lOption.setFilter(lFilter);
			setRequestAttribute("contenutoEsecuzione", "" + lOption);
		}

		// STUB 28/04/2011 Differenziato filtro sui contenuti per "S09"
		if (idFascicoloSius != null && idFascicoloSius.length() > 1 && lFasGPMod
				.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U024") == 0) {
			// MEV63: aggiunto parametro di passaggio per distinzione ufficio minori
			lFilter = lFascicoloUtils.filtraContenutiMS(strCodTipoUfficio);
			lOption.setFilter(lFilter);
			setRequestAttribute("contenutoEsecuzione", "" + lOption);
		}
		
        // MEV_2023-35  Differenziato filtro sui contenuti per "S30" Pene Sospese
        if (idFascicoloSius != null && idFascicoloSius.length() > 1 
            && lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U126") == 0) 
        {
            lFilter = lFascicoloUtils.filtraContenutiPS(strCodTipoUfficio);
            lOption.setFilter(lFilter);
            setRequestAttribute("contenutoEsecuzione", "" + lOption);
        }
        // MEV_2023-35 - FINE

		// Imposta la Collection Contenuto.
		// MEV_66: distinguo per ufficio minorile
		Collection lCol;
		if ("UDSM".equals(strCodTipoUfficio))
			lCol = (DecodificheManager.getInstance()).getOggettoProcedimentoUDSM();
		else
			lCol = (DecodificheManager.getInstance()).getOggettoProcedimentoUDS();
		// FINE MEV_66
		setRequestAttribute("collContenuto", lCol);

		// Imposta in dettagliofascicolo la Posizione Giuridica e la pena residua per il Fascicolo SIEP.
		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		DettaglioFascicoloModel lDettaglio = null;
		lDettaglio = lCtrl.ExDettaglioFascicoloSiep(fascicolo.getIdFascicoloSiep());

		if (lDettaglio == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Fascicolo SIEP non presente");

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
		// 5/7/2006 - Modifica per evitare l'imbroglio delle combo della posizione giuridica
		// Messo new al posto di una eguaglianza tra reference....
		Collection lCollPosGiuIscrizione = DecodificheManager.getInstance().getPosizioneGiuridicaIscrizione();
		Collection lCollPosGiuEsecuzione = DecodificheManager.getInstance().getPosizioneGiuridicaEsecuzione();
		Collection lCollPosGiuAltra = DecodificheManager.getInstance().getPosizioneGiuridicaAltraCausa();
		ArrayList lPosizioneGiuridica = new ArrayList(lCollPosGiuIscrizione);
		lPosizioneGiuridica.addAll(lCollPosGiuEsecuzione);
		lPosizioneGiuridica.addAll(lCollPosGiuAltra);

		if ((lDettaglio.getPosizioneGiuridica() != null)) {
			posGiuridica = lDettaglio.getPosizioneGiuridica().getCodPosizioneGiuridica();
			lOption = new Option(lPosizioneGiuridica,
					lDettaglio.getPosizioneGiuridica().getCodPosizioneGiuridica(), 66);
		} else
			lOption = new Option(lPosizioneGiuridica, 66);

		setRequestAttribute("posGiuridica", posGiuridica);

		setRequestAttribute("posizioneGiuridica", "" + lOption);

		// Imposta l'elenco magistrati.
		String lCodUfficio = getCodUfficioUtenteConnesso();
		IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
		// Modifica del 18/11/2016 MEV_50
		// Vengono recuperati solo i Magistrati ancora in servizio
		// lOption = new Option(lMagCtrl.ExElencoCbxMagistratiByCodUfficio(lCodUfficio));
		lOption = new Option(lMagCtrl.ExElencoCbxMagistratiValidiByCodUfficio(lCodUfficio));
		setRequestAttribute("magistrato", "" + lOption);

		// STUB 09/01/2004 Imposta il luogo detenzione.
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
						.getDescrTipoIstituto() + " - "
						+ lDettaglio.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune();

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

		// Redirect per tornare indietro
		String lTornaQui = StringUtils.urlEncode(IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=" + this.getRequest().getParameter(IWebConstants.ACTION_FIELD));
		setRequestAttribute("TornaQui", lTornaQui);

		// Impostazione della modalità - IU = Inserimento Fascicolo per UDS.
		// IE = Inserimento Procedimento di Esecuzione.
		if ((idFascicoloSius != null && idFascicoloSius.length() > 1))
			setRequestAttribute("modalita", "IE");
		else
			setRequestAttribute("modalita", "IU");

		return PG_LOAD_INSERISCIFASCICOLOSIUSUDSMANUALE;
	}

}