package siap.sige.fascicolo.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.controller.UfficioUtils;
import siap.sico.util.SICOLookupRemote;
import siap.sige.SIGEException;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActRicercaCollegamento
 * </p>
 * <p>
 * Description: Classe Action per la Ricerca del Procedimento Collegato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActRicercaCollegamento extends ActionSige implements ICostantiFascicoloSige {

	public String processRequest() throws Exception {

		// Si ricava il Fascicolo dalla sessione
		FascicoloSigeEstesoModel lFasSigeEsteso = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");

		// Recupero del Codice Tipo Ufficio.
		String lCodTipoUfficio = getRequestStringParameter(CAMPO_COD_MITTENTE_ATTO);
		String lDescrComuneUfficio = getRequestStringParameter(CAMPO_DESCR_SEDE_MITTENTE);

		// Impostazione della provenienza.
		setRequestAttribute("provenienza", "R");

		// Ricerca Fascicolo da Collegare
		FascicoloSigeModel lFasSigePadre = new FascicoloSigeModel();
		String lCodUfficio = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUfficio, lDescrComuneUfficio);

		// Ricerca Nuovo Fascicolo Padre.
		FascicoloSigeModel lFasSigeRicerca = new FascicoloSigeModel();
		BigDecimal lChiaveAnno = getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO);
		BigDecimal lChiaveprog = getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR);
		lFasSigeRicerca.setChiaveAnno(lChiaveAnno);
		lFasSigeRicerca.setChiaveProgr(lChiaveprog);
		lFasSigeRicerca.setChiaveUfficio(lCodUfficio);

		IFascicoloSige lCtrl = SIGELookupRemote.getFascicoloSigeRemote();
		try {
			lFasSigePadre = lCtrl.ExRicercaFascicoloSigeByAnnoProgrCodUfficio(lFasSigeRicerca);
			// Impostazione della modalità.
			setRequestAttribute("modalita", "M");
		} catch (SIGEException SIGEex) {
			if (SIGEex.getErrorCode() == SIGEException.USER_MESSAGE) {
				setRequestAttribute("provenienza", "Attenzione. Fascicolo Inesistente!");
				if (lFasSigeEsteso.getFascicoloSige().getIdFascicoloSigeOrigine() != null) {
					setRequestAttribute("modalita", "I");
					lFasSigePadre = lCtrl.ExRicercaFascicoloSigeByKey(lFasSigeEsteso.getFascicoloSige()
							.getIdFascicoloSigeOrigine());
				}
			} else {
				// Impostazione della modalità.
				setRequestAttribute("modalita", "I");
				throw SIGEex;
			}
		}

		ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
		SoggettoModel lSoggFascPadre = new SoggettoModel();
		if (lFasSigePadre != null && lFasSigePadre.getSogIdSoggetto() != null) {
			lSoggFascPadre = lSogCtrl.ExRicercaSoggettoByKey(lFasSigePadre.getSogIdSoggetto());
		}
		setRequestAttribute("soggFascPadre", lSoggFascPadre);

		if (lFasSigePadre != null && lFasSigePadre.getDescrUfficio() != null
				&& lFasSigePadre.getDescrUfficio().equals("")) {
			lFasSigePadre.setDescrUfficio((UfficioUtils.getUfficioByCodUfficio(lFasSigePadre
					.getChiaveUfficio())).getDescrComune());
		}

		// Si Imposta il Tipo Ufficio.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSige(), lFasSigeEsteso
				.getFascicoloSige().getCodTipoUfficioInserimento(), 46);
		String[] lFilter = { "DIB", "DIBM", "CAP" };
		lOption.setFilter(lFilter);
		setRequestAttribute("tipoUfficioSIGE", "" + lOption);
		setRequestAttribute("fascicoloPadre", lFasSigePadre);
		setRequestAttribute("fascicoloSigeEsteso", lFasSigeEsteso);

		return PG_LOAD_MODIFICACOLLEGAMENTO;
	}

}