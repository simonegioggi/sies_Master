package siap.sius.impugnazione.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.sius.impugnazione.controller.IImpugnazione;
import siap.sius.impugnazione.model.ImpugnazioneModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActModificaImpugnazione
 * </p>
 * <p>
 * Description: Classe Action per la modifica dell'Impugnazione
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
public class ActModificaImpugnazione extends ActionSiap implements ICostantiImpugnazione {

	/**
	 * Azione di Inserimento del Impugnazione
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		ImpugnazioneModel lImpMod = new ImpugnazioneModel();

		// Recupero il Fascicolo SIUS dalla sessione
		// FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Preleva dalla sessione i dati dell'utente connesso.
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		// String lCodComune = getCodComuneUtenteConnesso();

		// Preleva dalla request l'IdEvento.
		// String lIdEvento = this.getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		lImpMod.setCodOperatoreAggiornamento(lCodiceOperatore);
		lImpMod.setCodUfficioAggiornamento(lCodiceUfficio);
		lImpMod.setDataAggiornamento(DateUtils.getSysDate());
		lImpMod.setIdImpugnazione(getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE));
		lImpMod.setCodTipoImpugnazione(getRequestStringParameter(CAMPO_COD_TIPO_IMPUGNAZIONE));
		lImpMod.setSoggettoImpugnante(getRequestStringParameter(CAMPO_SOGGETTO_IMPUGNANTE));

		// Modifca del 16/11/2016 MEV_50
		lImpMod.setDescrizioneAltro(getStringParameter(CAMPO_DESCRIZIONE_ALTRO));

		lImpMod.setDataRicorso(getRequestDateParameter(CAMPO_ANNO_DATA_RICORSO, CAMPO_MESE_DATA_RICORSO,
				CAMPO_GIORNO_DATA_RICORSO));
		lImpMod.setDataArrivoCancelleria(getRequestDateParameter(CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA,
				CAMPO_MESE_DATA_ARRIVO_CANCELLERIA, CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA));
		lImpMod.setDataTrasmissioneAtti(getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
				CAMPO_MESE_DATA_TRASMISSIONE_ATTI, CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));
		lImpMod.setCodAutoritaDestinataria(getRequestStringParameter(CAMPO_COD_AUTORITA_DESTINATARIA));
		lImpMod.setDataDecisione(getRequestDateParameter(CAMPO_ANNO_DATA_DECISIONE,
				CAMPO_MESE_DATA_DECISIONE, CAMPO_GIORNO_DATA_DECISIONE));
		lImpMod.setCodTenoreDecisione(getRequestStringParameter(CAMPO_COD_TENORE_DECISIONE));
		lImpMod.setDataRestituzioneAtti(getRequestDateParameter(CAMPO_ANNO_DATA_RESTITUZIONE_ATTI,
				CAMPO_MESE_DATA_RESTITUZIONE_ATTI, CAMPO_GIORNO_DATA_RESTITUZIONE_ATTI));
		lImpMod.setAnnotazione(getRequestStringParameter(CAMPO_NOTE)); // Da inserire nella form
		lImpMod.setFlagSospEsec(getRequestStringParameter(CAMPO_FLAG_SOSP_ESEC)); // 08/05/2007 Sospensione
																					// Esecuzione
																					// Provvedimento

		IImpugnazione lCtrl = SIUSLookupRemote.getImpugnazioneRemote();
		ImpugnazioneModel llImpModRet = lCtrl.ExModificaImpugnazione(lImpMod);

		// Setta la risposta nella request.
		setRequestAttribute("impugnazione", llImpModRet);
		setRequestAttribute("modalita", "M");

		// Imposta ComboBOX Tipo Ricorso.
		Option lOption = null;
		if (Utils.isNullObj(lImpMod))
			lOption = new Option(DecodificheManager.getInstance().getTipoRicorso());
		else
			lOption = new Option(DecodificheManager.getInstance().getTipoRicorso(),
					lImpMod.getCodTipoImpugnazione());
		setRequestAttribute("tipoRicorso", "" + lOption);

		// Imposta ComboBOX Soggetto Impugnante.
		if (Utils.isNullObj(lImpMod))
			lOption = new Option(DecodificheManager.getInstance().getSoggettoImpugnante());
		else
			lOption = new Option(DecodificheManager.getInstance().getSoggettoImpugnante(),
					lImpMod.getSoggettoImpugnante());
		setRequestAttribute("soggettoImpugnante", "" + lOption);

		// Imposta ComboBOX Tipo DecisioneCassazione.
		if (Utils.isNullObj(lImpMod))
			lOption = new Option(DecodificheManager.getInstance().getTenoreDecisioneRicorso());
		else
			lOption = new Option(DecodificheManager.getInstance().getTenoreDecisioneRicorso(),
					lImpMod.getCodTenoreDecisione());

		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		if (strCodTipoUfficio.compareTo("TDS") == 0) {
			String[] lFilterTDR = { "01", "02", "03", "04", "05", "06" };
			lOption.setFilter(lFilterTDR);
		}
		setRequestAttribute("tenoreDecisioneRicorso", "" + lOption);

		// Imposta ComboBOX Autorita Destinataria
		lOption = new Option(DecodificheManager.getInstance().getTipoUfficio());
		String[] lFilter = { "CSS" };
		lOption.setFilter(lFilter);
		setRequestAttribute("ListaUffici", "" + lOption);

		// Prepara la pagina di destinazione, puntando all'azione di dettaglio.
		RedirectTo lPage = new RedirectTo();
		lPage.setPage(IWebConstants.PG_MAIN);
		lPage.setAction("siap.sius.impugnazione.action.ActLoadDettaglioImpugnazione");
		lPage.setParameter(ICostantiImpugnazione.CAMPO_ID_IMPUGNAZIONE, "" + llImpModRet.getIdImpugnazione());

		return "" + lPage; // restituisce la jsp di VIEW

	}

	private String getStringParameter(String paramName) throws F3BException {

		if (isRequestParameterNullObj(paramName))
			return null;
		String val = super.getRequestStringParameter(paramName);
		return val;
	}

}