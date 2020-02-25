package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Collection;

import f3b.util.F3BException;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.modulocumulo.controller.IPosizioneGiuridicaCumulo;
import siap.siep.modulocumulo.model.PosizioneGiuridicaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

@SuppressWarnings("rawtypes")
public class ActDettaglioEspiazioneAttuale extends ActionModuloCumulo implements ICostantiModuloCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	// private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {
		// ==========================================================================
		// Recupero i dati del cumulo
		// ==========================================================================
		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		BigDecimal idPosGiuridica = null;
		PosizioneGiuridicaCumuloModel lPosGiuridica = null;
		if (!isRequestParameterNullObj(ICostantiPosizioneGiuridicaCumulo.CAMPO_ID_POSIZIONE_GIURIDICA_CUM))
			idPosGiuridica = getRequestBigDecimalParameter(
					ICostantiPosizioneGiuridicaCumulo.CAMPO_ID_POSIZIONE_GIURIDICA_CUM);

		if (idPosGiuridica != null) {
			IPosizioneGiuridicaCumulo lCtrlPosGiu = SIEPLookupRemote.getPosizioneGiuridicaCumuloRemote();
			lPosGiuridica = lCtrlPosGiu.ExRicercaPosizioneGiuridicaCumuloById(idPosGiuridica);

			String lCodPos = lPosGiuridica.getCodPosizioneGiuridica();

			Collection aColLib = DecodificheManager.getInstance().getPosizioniGiuridicheCumLibero();
			Collection aColEspIst = DecodificheManager.getInstance().getPosizioniGiuridicheCumEspIst();
			Collection aColEspAltro = DecodificheManager.getInstance().getPosizioniGiuridicheCumEspAltro();

			// siesLogger.debug("aColEspIst = "+aColEspIst);
			// siesLogger.debug("lCodPos = "+lCodPos);
			// siesLogger.debug("containsCode = "+DecodificheUtils.containsCode (aColEspIst, lCodPos));

			if (DecodificheUtils.containsCode(aColLib, lCodPos)) {
				lPosGiuridica.setTipoPosGiu(ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_TIPO_POS_LIBERO);
				setRequestAttribute("TipoPosGiu",
						ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_TIPO_POS_LIBERO);
			} else if (DecodificheUtils.containsCode(aColEspIst, lCodPos)) {
				lPosGiuridica.setTipoPosGiu(ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_TIPO_POS_ESPIST);
				setRequestAttribute("TipoPosGiu",
						ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_TIPO_POS_ESPIST);
			} else if (DecodificheUtils.containsCode(aColEspAltro, lCodPos)) {
				lPosGiuridica.setTipoPosGiu(ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_TIPO_POS_ESPALTRO);
				setRequestAttribute("TipoPosGiu",
						ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_TIPO_POS_ESPALTRO);
			}
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
		}

		setRequestAttribute("lPosizioneGiuridicaCumulo", lPosGiuridica);

		return PG_LOAD_DETTAGLIO_ESPIAZIONE_ATTUALE;
	}

}