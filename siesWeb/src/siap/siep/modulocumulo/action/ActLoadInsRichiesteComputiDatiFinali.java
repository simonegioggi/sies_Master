package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.siep.modulocumulo.controller.IComputiCumulo;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la load inserimento delle Richieste con anticipazione nella fas e Dati Finali Cumulo
 *
 * @author d.fiorletta
 *
 */
public class ActLoadInsRichiesteComputiDatiFinali extends ActionModuloCumulo
		implements ICostantiDatiFinaliCumulo, ICostantiComputiCumulo {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// ==========================================================================
		// Recupero i dati del cumulo
		// ==========================================================================
		super.getDatiIstruttoria();
		super.getDatiFinaliCumuloAggregato();

		/* BigDecimal lIdIstruttoria = */getIdIstruttoria();

		// I=Inserisci, M=Modifica, C=Cancella (non previsto)
		String lModalita = "I"; // default
		if (!isRequestParameterNullObj("modalita") && !"".equals(getRequestStringParameter("modalita")))
			lModalita = getRequestStringParameter("modalita");

		setRequestAttribute("modalita", lModalita);

		ComputiCumuloModel lComputo = null;

		if ("M".equals(lModalita)) {
			BigDecimal lIdComputo = getRequestBigDecimalParameter(CAMPO_ID_COMPUTI_CUMULO);

			IComputiCumulo lCtrlComputi = SIEPLookupRemote.getComputiCumuloRemote();

			lComputo = lCtrlComputi.ExRicercaComputiCumuloById(lIdComputo);

			setRequestAttribute("ComputoCumulo", lComputo);
		}

		// Caricamento combo tipo beneficio
		Option lOptionTipoBen = new Option(
				DecodificheManager.getInstance().getTipoAnnotazioneManualeBenefici());
		// lOptionTipoBen.setSelected("002");
		if (lComputo != null && lComputo.getCodTipoAnnotazione() != null)
			lOptionTipoBen.setSelected(lComputo.getCodTipoAnnotazione());

		setRequestAttribute("TipoAnnotazioneManuale", "" + lOptionTipoBen);

		// Caricamento combo DPR
		Option lOptionDPR = new Option(DecodificheManager.getInstance().getDPR());

		Vector lVect = (Vector) DecodificheManager.getInstance().getDPR();

		if (lComputo != null && lComputo.getCodDpr() != null)
			lOptionDPR.setSelected(lComputo.getCodDpr());
		else { // Ultimo, il più recente
			DecodificheModel lDecMod = (DecodificheModel) lVect.lastElement();
			lOptionDPR.setSelected(lDecMod.getCode());
		}
		setRequestAttribute("listaDPR", "" + lOptionDPR);

		//
		return PG_LOAD_INSERISCI_RICHIESTE_GE;
	}
}
