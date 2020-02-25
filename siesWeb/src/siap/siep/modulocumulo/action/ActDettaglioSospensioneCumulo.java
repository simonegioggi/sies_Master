package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.ArrayList;

import f3b.log.LogF3B;
import f3b.util.F3BException;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

/**
 * Action per la visualizzazione del Dettaglio del provvedimento di Sospensione (modulo cumulo)
 * 
 * @author
 *
 */
@SuppressWarnings("rawtypes")
public class ActDettaglioSospensioneCumulo extends ActionModuloCumulo
		implements ICostantiComputiCumulo, ICostantiStatoEsecTitoloCumulato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		BigDecimal idStatoEsec = null;
		if (!isRequestParameterNullObj(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO))
			idStatoEsec = getRequestBigDecimalParameter(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);

		IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();

		StatoEsecTitoloCumulatoModel lStato = lCtrlStato
				.ExRicercaStatoEsecTitoloCumulatoByIdFull(idStatoEsec);

		setRequestAttribute("Provvedimento", lStato);

		ComputiCumuloModel lComputo = lStato.getListaComputi().elementAt(0);

		// ==========================================================================
		// OGGETTO DECISIONE
		// ==========================================================================
		String lCodContenutoDecisione = lComputo.getCodOggettoDecisione();
		siesLogger.debug("lCodContenutoDecisione = " + lCodContenutoDecisione);

		// ==========================================================================
		// CONTENUTO DECISIONE (Dominio OGGETTO_SOSPENSIONI)
		// ==========================================================================
		// Per il dettaglio bisogna risalire al Contenuto
		ArrayList lContenutoDecisione = (ArrayList) DecodificheManager.getInstance()
				.getListaOggettiSospensione();

		DecodificheModel lDecodeContenuto = null;
		for (int i = 0; i < lContenutoDecisione.size(); i++) {
			ArrayList lArrayRegistro = (ArrayList) lContenutoDecisione.get(i);

			// siesLogger.debug(">>>>>>Primo Registro..... ");
			for (int j = 0; j < lArrayRegistro.size(); j++) {
				DecodificheModel lDecode = (DecodificheModel) lArrayRegistro.get(j);

				// siesLogger.debug("lDecodeContenuto = "+lDecode.getContesto()+" - "
				// +lDecode.getCode()+" - "
				// +lDecode.getFiltro()+" - "
				// +lDecode.getCodiceAlternativo()+" - "
				// +lDecode.getDescription());

				// Applico il filtro per Registro e CodContenuto per identificare il
				// Decodifiche corretto
				if (lDecode.getCodiceAlternativo().equals(lComputo.getCodTipoRegistroOrdinanza())
						&& lDecode.getFiltro().equals(lCodContenutoDecisione)) {
					siesLogger.debug("Trovato contenuto decisione");
					lDecodeContenuto = lDecode;
				}
			}
		}

		// siesLogger.debug("lDecodeContenuto trovato= "+lDecodeContenuto.getContesto()+" - "
		// +lDecodeContenuto.getCode()+" - "
		// +lDecodeContenuto.getFiltro()+" - "
		// +lDecodeContenuto.getCodiceAlternativo()+" - "
		// +lDecodeContenuto.getDescription());

		setRequestAttribute("ContenutoDecisione", lDecodeContenuto.getDescription());

		return PG_LOAD_DETTAGLIO_SOSPENSIONE_CUMULO;
	}

}