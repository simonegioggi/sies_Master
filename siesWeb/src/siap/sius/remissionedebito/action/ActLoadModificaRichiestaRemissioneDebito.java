package siap.sius.remissionedebito.action;

/**
* <p>Title: ActLoadModificaRichiestaRemissioneDebito</p>
* 
* <p>Description: Classe Action per la load Modifica di RichiestaRemissione</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.remissionedebito.controller.IRichiestaRemissione;
import siap.sius.remissionedebito.model.RichiestaRemissioneModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;
import f3b.web.html.Option;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadModificaRichiestaRemissioneDebito extends ActionSius
		implements ICostantiSiusRemissioneDebito {

	/**
	 * Azione di caricamento della pagina di Modifica dei dati. Si occupa anche di precaricare tutti i dati da
	 * visualizzare in tale pagina (es: combo)
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		BigDecimal aIdFascicoloSius = getRequestBigDecimalParameter(
				ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS);
		Date lDataIrrevocabilita = null;

		// Si Preleva dalla sessione il model fascicoloSiusGP
		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Controllo congruenza dati di sessione.
		if (lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius().compareTo(aIdFascicoloSius) == 0
				&& lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null) {
			// Lettura della Data Irrevocabilità di SIEP.
			IFascicoloSiep lCtrlFasSiep = SIEPLookupRemote.getFascicoloSiepRemote();
			FascicoloSiepModel lFasSiep = lCtrlFasSiep
					.ExRicercaFascicoloByKey(lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
			if (lFasSiep != null)
				lDataIrrevocabilita = lFasSiep.getDataIrrevocabilita();
		}
		setRequestAttribute("dataIrrevocabilita", lDataIrrevocabilita);

		IRichiestaRemissione lCtrl = SIUSLookupRemote.getRichiestaRemissioneRemote();
		RichiestaRemissioneModel lRicMod = new RichiestaRemissioneModel();
		lRicMod.setIdRichiestaRemissione(getRequestBigDecimalParameter(CAMPO_ID_RICHIESTA_REMISSIONE));
		lRicMod = lCtrl.ExRicercaRichiestaRemissioneById(lRicMod.getIdRichiestaRemissione());
		setRequestAttribute("richiestaremissione", lRicMod);

		// autorità per la remissione
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
		lOption.setFilter(new String[] { "-", "36", "99", "57", "37", "98", "38" });

		// 2011-10-11 Commentato poichè non è una nuova istanza, pertanto qualunque
		// operazione di modifica viene eseguita sull'originale e non sulla copia.
		// PM
		// Collection lIstDet = DecodificheManager.getInstance().getTipoAutoritaIstituto();
		Collection lIstDet = new ArrayList(DecodificheManager.getInstance().getTipoAutoritaIstituto());

		// Eliminazione elemento "-" già presente in lista
		DecodificheModel dashElTipoAut = new DecodificheModel("-", "-", "TIPO_AUTORITA", null, null, null,
				null, null, null);
		lIstDet.remove(dashElTipoAut);
		// fine Eliminazione

		Option lOptionIst = new Option(lIstDet);

		lOption.setSelected(lRicMod.getCodTipoAutoritaEmittente());
		lOptionIst.setSelected(lRicMod.getCodTipoAutoritaEmittente());
		setRequestAttribute("autoritaRemIst", "" + lOptionIst);
		setRequestAttribute("autoritaRem", "" + lOption);

		// tipo di provvedimeto
		Option lOptionTP = new Option(DecodificheManager.getInstance().getTipoProvvedimenti());
		lOptionTP.setFilter(new String[] { "-", "01", "02", "54" });
		lOptionTP.setSelected(lRicMod.getCodTipoProvvedimento());
		setRequestAttribute("tipoProvvedimento", "" + lOptionTP);

		// autorità di emissione del provvedimento: inserire tutti gli uffici giudicanti riconosciuti da SIES
		// + istituti detenzione
		Collection lAutEmitt = DecodificheManager.getInstance().getTipoAutoritaEmittente();
		lAutEmitt.addAll(lIstDet);
		Option lOptionAP = new Option(lAutEmitt);
		lOptionAP.setSelected(lRicMod.getCodAutoritaEmittenteProvv());
		setRequestAttribute("autoritaProvvedimento", "" + lOptionAP);

		// Imposta la Modalità a Modifica.
		setRequestAttribute("modalita", "M");

		return PG_LOAD_MODIFICA_RICHIESTA_REMISSIONE_DEBITO;
	}

}