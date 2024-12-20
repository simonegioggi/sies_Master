package siap.sius.fascicolo.action;

import java.math.BigDecimal;
import java.util.Collection;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.luogodetenzione.controller.ILuogoDetenzione;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.esecuzionemisuraalternativa.action.ICostantiEsecuzioneMA;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.util.FascicoloUtils;
import siap.sius.util.SIUSLookupRemote;

public class ActLoadInsFascicoloDaSoggettoUDS extends ActionSiap implements ICostantiFascicoloSius {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// STUB 02/04/2004 Gestione Iscrizione Procedimento di esecuzione da soggetto.
		String idFascicoloSius = null;
		String idSoggetto = null;
		this.setLinkRitorno();

		if (!this.isRequestParameterNullObj("IdFascicoloSius"))
			idFascicoloSius = getRequestStringParameter("IdFascicoloSius");

		FascicoloGPModel lFasGPMod = new FascicoloGPModel(); // 31/07/2007

		// Se provengo dall'"Elenco Procedimenti di Esecuzione per Soggetto" (o dall'"Elenco Procedimenti di
		// Esecuzione Misure Alternative"),
		// ho il parametro "IdFascicoloSius" nella request.
		// Con IdFascicoloSius leggo i dati del Procedimento SIUS e del Procedimento SIEP.
		if (idFascicoloSius != null && idFascicoloSius.length() > 1) {
			IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			lFasGPMod = lCtrl.ExRicercaFascicoloByKey(new BigDecimal(idFascicoloSius));

			// Si Passa nella Request il fascicolo SIUS per consentire l'impostazione dei campi non
			// modificabili.
			setRequestAttribute("fascicoloEsecuzione", lFasGPMod);

			// Rimuovo dalla sessione un eventuale fascicolo SIEP precedente.
			if (!isSessionAttributeNullObj("fascicolo"))
				removeSessionAttribute("fascicolo");

			// STUB 10/05/2004 - Si Legge e si pone in sessione il soggetto.
			if (!this.isRequestParameterNullObj(ICostantiEsecuzioneMA.CAMPO_ID_SOGGETTO)) {
				idSoggetto = getRequestStringParameter(ICostantiEsecuzioneMA.CAMPO_ID_SOGGETTO);
				SoggettoModel lSogMod = new SoggettoModel();
				ISoggetto lCtrlSog = SICOLookupRemote.getSoggettoRemote();
				lSogMod = lCtrlSog.ExRicercaSoggettoByKey(new BigDecimal(idSoggetto));
				setSessionAttribute("soggetto", lSogMod);
			}
		}

		// Dettaglio Soggetto tramite Dettaglio HTML.
		setRequestAttribute("soggetto", getSessionAttribute("soggetto"));

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

		// Imposta Oggetto.
		// lOption = new Option( DecodificheManager.getInstance().getMotivoProvvedimento(), 75);
		// setRequestAttribute("oggetto", "" + lOption );

		// Imposta Posizione Giuridica.
		// STUB 26/05/2005 Variazione: lPosizioneGiuridica contiene le posizioni dell'esecuzione.
		// lOption = new Option( DecodificheManager.getInstance().getPosizioneGiuridicaIscrizione() );
		lOption = new Option(DecodificheManager.getInstance().getPosizioneGiuridicaEsecuzione());
		if (idFascicoloSius != null && idFascicoloSius.length() > 1) {
			lOption.setSelected(lFasGPMod.getGeneraleProcedimentoModel().getCodPosGiuridica());
		}
		setRequestAttribute("posizioneGiuridica", "" + lOption);

		// Imposta l'elenco magistrati.
		String lCodUfficio = getCodUfficioUtenteConnesso();
		IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
		// Modifica del 18/11/2016 MEV_50
		// Vengono recuperati solo i Magistrati ancora in servizio
		// lOption = new Option(lMagCtrl.ExElencoCbxMagistratiByCodUfficio(lCodUfficio));
		lOption = new Option(lMagCtrl.ExElencoCbxMagistratiValidiByCodUfficio(lCodUfficio));
		if (idFascicoloSius != null && idFascicoloSius.length() > 1) {
			lOption.setSelected(lFasGPMod.getGeneraleProcedimentoModel().getCodAutoritaDelegata());
		}
		setRequestAttribute("magistrato", "" + lOption);

		setRequestAttribute("modalita", "IS");

		// STUB 02/04/2004 Se devo iscrivere un procedimento normale o di EMA padre, punto a
		// PG_LOAD_INSERISCIFASCICOLODASOGGETTOUDS,
		// Se invece devo iscrivere una misura alternativa figlia, punto a PG_LOAD_INSERISCIFASCICOLOSIUSUDS.
		if (idFascicoloSius != null && idFascicoloSius.length() > 1) {
			setRequestAttribute("modalita", "IM");
			LuogoDetenzioneModel luoDetMod;
			String luogoDetenzione = "";
			String idLuogoDetenzione = "";

			ILuogoDetenzione lCtrl = SIEPLookupRemote.getLuogoDetenzioneRemote();
			luoDetMod = lCtrl
					.ExRicercaLuogoDetenzioneCorrenteByFascicoloSius(new BigDecimal(idFascicoloSius));

			if ((luoDetMod != null) && (luoDetMod.getIstDetIdIstitutoDetenzione() != null)) {
				if (luoDetMod.getIstitutoDetenzione().getDescrizione() != null)
					luogoDetenzione = luoDetMod.getIstitutoDetenzione().getDescrizione();
				else
					luogoDetenzione = luoDetMod.getIstitutoDetenzione().getDescrTipoIstituto() + " - "
							+ luoDetMod.getIstitutoDetenzione().getDescrComune();

				idLuogoDetenzione = luoDetMod.getIdLuogoDetenzione().toString();
			}

			setRequestAttribute("luogoDetenzione", luogoDetenzione);
			setRequestAttribute("idLuogoDetenzione", idLuogoDetenzione);

			return PG_LOAD_INSERISCIFASCICOLOSIUSUDS;
		} else
			return PG_LOAD_INSERISCIFASCICOLODASOGGETTOUDS;
	}

}