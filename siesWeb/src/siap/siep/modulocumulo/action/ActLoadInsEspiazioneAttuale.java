package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Collection;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.modulocumulo.controller.IDatiFinaliCumulo;
import siap.siep.modulocumulo.controller.IPosizioneGiuridicaCumulo;
import siap.siep.modulocumulo.model.DatiFinaliCumuloModel;
import siap.siep.modulocumulo.model.PosizioneGiuridicaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la load inserimento/Modifica dei dati dell'espiazione attuale
 *
 *
 * @author
 *
 */
public class ActLoadInsEspiazioneAttuale extends ActionModuloCumulo {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		// ==========================================================================
		// Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
		// di DettaglioTitoloCumulato.jsp
		// ==========================================================================
		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		// Carico se presente DatiFinaliCumulo
		IDatiFinaliCumulo lCtrl = SIEPLookupRemote.getDatiFinaliCumuloRemote();
		DatiFinaliCumuloModel lDatiFinali = lCtrl.ExRicercaDatiFinaliCumuloByIdIstrutt(getIdIstruttoria());

		setRequestAttribute("DatiFinaliCumulo", lDatiFinali);

		String lModalita = "I"; // default inserimento
		PosizioneGiuridicaCumuloModel lPosGiuridica = new PosizioneGiuridicaCumuloModel();

		if (!isRequestParameterNullObj("modalita")) {
			lModalita = getRequestStringParameter("modalita");
		}
		if ("I".equals(lModalita)) {
			// Inserimento

		} else if ("M".equals(lModalita)) {
			// Modifica
			// BigDecimal lId = getRequestBigDecimalParameter("id");
			BigDecimal lId = getRequestBigDecimalParameter(
					ICostantiPosizioneGiuridicaCumulo.CAMPO_ID_POSIZIONE_GIURIDICA_CUM);

			// Recupero i dati e li passo alla form
			IPosizioneGiuridicaCumulo lCtrlPGC = SIEPLookupRemote.getPosizioneGiuridicaCumuloRemote();
			lPosGiuridica = lCtrlPGC.ExRicercaPosizioneGiuridicaCumuloById(lId);

			String lCodPos = lPosGiuridica.getCodPosizioneGiuridica();

			Collection aColLib = DecodificheManager.getInstance().getPosizioniGiuridicheCumLibero();
			Collection aColEspIst = DecodificheManager.getInstance().getPosizioniGiuridicheCumEspIst();
			Collection aColEspAltro = DecodificheManager.getInstance().getPosizioniGiuridicheCumEspAltro();

			if (DecodificheUtils.containsCode(aColLib, lCodPos))
				lPosGiuridica.setTipoPosGiu(ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_TIPO_POS_LIBERO);
			else if (DecodificheUtils.containsCode(aColEspIst, lCodPos))
				lPosGiuridica.setTipoPosGiu(ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_TIPO_POS_ESPIST);
			else if (DecodificheUtils.containsCode(aColEspAltro, lCodPos))
				lPosGiuridica.setTipoPosGiu(ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_TIPO_POS_ESPALTRO);

			if (lPosGiuridica.getIstDetIdIstitutoDetenzione() != null) {
				IIstitutoDetenzione lIstitutoCtrl = SIEPLookupRemote.getIstitutoDetenzioneRemote();

				IstitutoDetenzioneModel lIstituto = lIstitutoCtrl
						.ExRicercaIstitutoDetenzioneByKey(lPosGiuridica.getIstDetIdIstitutoDetenzione());
				lPosGiuridica.setIstitutoDetenzione(lIstituto);
			}
			if (lPosGiuridica.getChiaveUffFasSius() != null) {
				UfficioModel lUffSius = getUfficioByCodUfficio(lPosGiuridica.getChiaveUffFasSius());
				lPosGiuridica.setUfficioSorv(lUffSius);
			}

			setRequestAttribute("lPosizioneGiuridicaCumulo", lPosGiuridica);
			setRequestAttribute("tipoPosGiu", lPosGiuridica.getTipoPosGiu().toString().trim());

		} else {
			// Rilanciare Eccezione - Operazione non supportata
		}

		// ==========================================================================
		// Caricare qui i dati delle combo
		// ==========================================================================
		// Posizioni Giuridiche
		Option lComboPGLibero = new Option(
				DecodificheManager.getInstance().getPosizioniGiuridicheCumLibero());
		if ("M".equals(lModalita) && lPosGiuridica != null)
			lComboPGLibero.setSelected(lPosGiuridica.getCodPosizioneGiuridica());
		setRequestAttribute("posizioneGiuridicaLibero", "" + lComboPGLibero);

		Option lComboPGEspIst = new Option(
				DecodificheManager.getInstance().getPosizioniGiuridicheCumEspIst());
		if ("M".equals(lModalita) && lPosGiuridica != null)
			lComboPGEspIst.setSelected(lPosGiuridica.getCodPosizioneGiuridica());
		setRequestAttribute("posizioneGiuridicaEspIst", "" + lComboPGEspIst);

		Option lComboPGEspAltro = new Option(
				DecodificheManager.getInstance().getPosizioniGiuridicheCumEspAltro());
		if ("M".equals(lModalita) && lPosGiuridica != null)
			lComboPGEspAltro.setSelected(lPosGiuridica.getCodPosizioneGiuridica());
		setRequestAttribute("posizioneGiuridicaEspAltro", "" + lComboPGEspAltro);

		// Ufficio Emittente
		Option lComboUffici = new Option(DecodificheManager.getInstance().getTipoUfficio());
		lComboUffici.setFilter(new String[] { "TDS", "UDS", "TDSM", "UDSM" });
		if (lPosGiuridica != null && lPosGiuridica.getUfficioSorv() != null)
			lComboUffici.setSelected(lPosGiuridica.getUfficioSorv().getCodTipoUfficio());
		setRequestAttribute("tipoUfficioSIUS", "" + lComboUffici);

		// Tipo Provvedimento
		Option lComboTipoProvv = new Option(DecodificheManager.getInstance().getTipoProvvSorveglianza());
		if (lPosGiuridica != null && lPosGiuridica.getCodTipoProvvedimento() != null)
			lComboTipoProvv.setSelected(lPosGiuridica.getCodTipoProvvedimento());
		setRequestAttribute("tipoProvvedimento", "" + lComboTipoProvv);

		setRequestAttribute("modalita", lModalita);

		return IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/LoadInsEspiazioneAttuale.jsp";
	}
}
