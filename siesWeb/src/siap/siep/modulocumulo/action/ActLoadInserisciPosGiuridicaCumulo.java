package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IPosizioneGiuridicaCumulo;
import siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel;
import siap.siep.modulocumulo.model.PosizioneGiuridicaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/***
 * Action per la load inserisci posizione giuridica
 * 
 * @author d.fiorletta
 *
 */
public class ActLoadInserisciPosGiuridicaCumulo extends ActionModuloCumulo implements ICostantiModuloCumulo {

	public String processRequest() throws F3BException {
		// ==========================================================================
		// Recupero i dati del cumulo
		// ==========================================================================
		IstruttoriaCumuloModel lIstruttoria = super.getDatiIstruttoria();

		DatiFinaliCumuloAggregatoModel lDatiFinaliAggModel = super.getDatiFinaliCumuloAggregato();

		if (!isRequestParameterNullObj(MODALITA)
				&& MODALITA_MODIFICA.equals(getRequestStringParameter(MODALITA))) {
			// if (lDatiFinaliAggModel.getDatiFinaliCumulo().getCodPosizioneGiuridica()!=null){
			setRequestAttribute(ICostantiModuloCumulo.MODALITA, ICostantiModuloCumulo.MODALITA_MODIFICA);
		} else {
			setRequestAttribute(ICostantiModuloCumulo.MODALITA, ICostantiModuloCumulo.MODALITA_INSERIMENTO);
		}

		// ==========================================================================
		// Carico i record PosizioneGiuridicaCumuloModel relativi all'espiazione attuale
		// dei titolo assorbiti
		// ==========================================================================
		IPosizioneGiuridicaCumulo lCtrlPosGiuridicaCumulo = SIEPLookupRemote
				.getPosizioneGiuridicaCumuloRemote();
		Vector<PosizioneGiuridicaCumuloModel> lListaPosizioniAttuali = lCtrlPosGiuridicaCumulo
				.ExRicercaPosizioniGiuridicheTitoliByIdIstruttoria(lIstruttoria.getIdIstruttoriaCumulo());

		setRequestAttribute("listaPosizioniAttuali", lListaPosizioniAttuali);

		if (!isRequestParameterNullObj("FLAG_POS_GIU_TITOLO")
				&& "S".equals(getRequestStringParameter("FLAG_POS_GIU_TITOLO"))) {
			// Sono il precaricamento PG dal titolo cumulato

			PosizioneGiuridicaCumuloModel lPosGiuTit = preparaDatiPGTitolo(lListaPosizioniAttuali);

			if (!isRequestParameterNullObj(MODALITA)
					&& MODALITA_MODIFICA.equals(getRequestStringParameter(MODALITA))) {
				PosizioneGiuridicaCumuloModel lPosDatiFinali = lDatiFinaliAggModel
						.getPosizioneGiuridicaCumulo();

				lPosGiuTit.setIdPosizioneGiuridicaCum(lPosDatiFinali.getIdPosizioneGiuridicaCum());
			}

			lDatiFinaliAggModel.setPosizioneGiuridicaCumulo(lPosGiuTit);
			setRequestAttribute("FLAG_POS_GIU_TITOLO", "S");
		}

		// ==========================================================================
		// Caricare qui i dati delle combo
		// ==========================================================================
		// Posizioni Giuridiche
		Option lComboPGLibero = new Option(
				DecodificheManager.getInstance().getPosizioniGiuridicheCumLibero());
		if (lDatiFinaliAggModel.getPosizioneGiuridicaCumulo() != null)
			lComboPGLibero.setSelected(
					lDatiFinaliAggModel.getPosizioneGiuridicaCumulo().getCodPosizioneGiuridica());
		setRequestAttribute("posizioneGiuridicaLibero", "" + lComboPGLibero);

		Option lComboPGEspIst = new Option(
				DecodificheManager.getInstance().getPosizioniGiuridicheCumEspIst());
		if (lDatiFinaliAggModel.getPosizioneGiuridicaCumulo() != null)
			lComboPGEspIst.setSelected(
					lDatiFinaliAggModel.getPosizioneGiuridicaCumulo().getCodPosizioneGiuridica());
		setRequestAttribute("posizioneGiuridicaEspIst", "" + lComboPGEspIst);

		Option lComboPGEspAltro = new Option(
				DecodificheManager.getInstance().getPosizioniGiuridicheCumEspAltro());
		if (lDatiFinaliAggModel.getPosizioneGiuridicaCumulo() != null)
			lComboPGEspAltro.setSelected(
					lDatiFinaliAggModel.getPosizioneGiuridicaCumulo().getCodPosizioneGiuridica());
		setRequestAttribute("posizioneGiuridicaEspAltro", "" + lComboPGEspAltro);

		// Ufficio Emittente
		// Option lComboUffici = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS());
		Option lComboUffici = new Option(DecodificheManager.getInstance().getTipoUfficio());
		lComboUffici.setFilter(new String[] { "TDS", "UDS", "TDSM", "UDSM" });
		// FIXME da sostituire con getTipoUfficioSiusTDSMUDSM
		if (lDatiFinaliAggModel.getPosizioneGiuridicaCumulo() != null
				&& lDatiFinaliAggModel.getPosizioneGiuridicaCumulo().getUfficioSorv() != null)
			lComboUffici.setSelected(
					lDatiFinaliAggModel.getPosizioneGiuridicaCumulo().getUfficioSorv().getCodTipoUfficio());
		setRequestAttribute("tipoUfficioSIUS", "" + lComboUffici);

		// Tipo Provvedimento
		Option lComboTipoProvv = new Option(DecodificheManager.getInstance().getTipoProvvSorveglianza());
		if (lDatiFinaliAggModel.getPosizioneGiuridicaCumulo() != null
				&& lDatiFinaliAggModel.getPosizioneGiuridicaCumulo().getCodTipoProvvedimento() != null)
			lComboTipoProvv
					.setSelected(lDatiFinaliAggModel.getPosizioneGiuridicaCumulo().getCodTipoProvvedimento());
		setRequestAttribute("tipoProvvedimento", "" + lComboTipoProvv);

		return PG_LOAD_INSERISCI_POSIZIONE_GIURIDICA;
	}

	/**
	 * Recupera i dati della posizione giuridica (Espiazione Attuale) del Titolo selezionato in form e
	 * completa opportunamente i dati per essere precaricati in form
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private PosizioneGiuridicaCumuloModel preparaDatiPGTitolo(
			Vector<PosizioneGiuridicaCumuloModel> aListaPosizioniAttuali) throws F3BException {
		PosizioneGiuridicaCumuloModel lPosGiuTitRet = null;

		for (PosizioneGiuridicaCumuloModel lPosGiuTit : aListaPosizioniAttuali) {
			BigDecimal lIdPosTit = getRequestBigDecimalParameter(
					ICostantiPosizioneGiuridicaCumulo.CAMPO_ID_POSIZIONE_GIURIDICA_CUM + "_LOAD");

			if (lPosGiuTit.getIdPosizioneGiuridicaCum().compareTo(lIdPosTit) == 0) {
				Collection aColLib = DecodificheManager.getInstance().getPosizioniGiuridicheCumLibero();
				Collection aColEspIst = DecodificheManager.getInstance().getPosizioniGiuridicheCumEspIst();
				Collection aColEspAltro = DecodificheManager.getInstance()
						.getPosizioniGiuridicheCumEspAltro();

				lPosGiuTitRet = new PosizioneGiuridicaCumuloModel(lPosGiuTit);

				String lCodPos = lPosGiuTitRet.getCodPosizioneGiuridica();

				// Ripulisco i campi non significativi
				lPosGiuTitRet.setIdPosizioneGiuridicaCum(null);
				lPosGiuTitRet.setTitIdTitoloCumulato(null);

				// Setto TipoPos per il radio della form
				if (DecodificheUtils.containsCode(aColLib, lCodPos))
					lPosGiuTitRet
							.setTipoPosGiu(ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_TIPO_POS_LIBERO);
				else if (DecodificheUtils.containsCode(aColEspIst, lCodPos))
					lPosGiuTitRet
							.setTipoPosGiu(ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_TIPO_POS_ESPIST);
				else if (DecodificheUtils.containsCode(aColEspAltro, lCodPos))
					lPosGiuTitRet
							.setTipoPosGiu(ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_TIPO_POS_ESPALTRO);

				// Recuperio i dati dell'istituto se presenti
				if (lPosGiuTitRet.getIstDetIdIstitutoDetenzione() != null) {
					IIstitutoDetenzione lIstitutoCtrl = SIEPLookupRemote.getIstitutoDetenzioneRemote();

					IstitutoDetenzioneModel lIstituto = lIstitutoCtrl
							.ExRicercaIstitutoDetenzioneByKey(lPosGiuTitRet.getIstDetIdIstitutoDetenzione());
					lPosGiuTitRet.setIstitutoDetenzione(lIstituto);
				}

				// Recuperio i dati dell'uffico SIUS se presente
				if (lPosGiuTitRet.getChiaveUffFasSius() != null) {
					UfficioModel lUffSius = getUfficioByCodUfficio(lPosGiuTitRet.getChiaveUffFasSius());
					lPosGiuTitRet.setUfficioSorv(lUffSius);
				}

				break;
			}
		}

		return lPosGiuTitRet;
	}
}
