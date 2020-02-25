package siap.sius.remissionedebito.action;

/**
* <p>Title: ActLoadInserisciRichiestaRemissioneDebito</p>
* <p>Description: Classe Action per la load inserisci di RichiestaRemissione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
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
import siap.sius.SIUSException;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.util.F3BException;
import f3b.web.html.Option;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadInserisciRichiestaRemissioneDebito extends ActionSius
		implements ICostantiSiusRemissioneDebito {

	public String processRequest() throws F3BException {

		if (this.IsFascicoloSiusModificabile() == false)
			throw new SIUSException(SIUSException.USER_MESSAGE, ICostantiFascicoloSius.MSG_NON_MODIFICABILE);

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
		setRequestAttribute("autoritaRemIst", "" + lOptionIst);
		setRequestAttribute("autoritaRem", "" + lOption);

		// tipo di provvedimento
		Option lOptionTP = new Option(DecodificheManager.getInstance().getTipoProvvedimenti());
		lOptionTP.setFilter(new String[] { "-", "01", "02", "54" });
		setRequestAttribute("tipoProvvedimento", "" + lOptionTP);

		// autorità di emissione del provvedimento: inserire tutti gli uffici giudicanti riconosciuti da SIES
		// + istituti detenzione
		Collection lAutEmitt = DecodificheManager.getInstance().getTipoAutoritaEmittente();
		lAutEmitt.addAll(lIstDet);
		Option lOptionAP = new Option(lAutEmitt);
		setRequestAttribute("autoritaProvvedimento", "" + lOptionAP);

		// Imposta la Modalità a Inserimento.
		setRequestAttribute("modalita", "I");

		// Restituisce la pagina di Inserimento dei Dati
		return PG_LOAD_INSERISCI_RICHIESTAREMISSIONE_DEBITO;
	}

}