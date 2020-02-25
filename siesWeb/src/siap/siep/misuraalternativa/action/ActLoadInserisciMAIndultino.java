package siap.siep.misuraalternativa.action;

import java.util.Collection;
import java.util.Iterator;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;

/**
 * <p>
 * Title: ActLoadInserisciMAIndultino
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Concessione Indultino
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
public class ActLoadInserisciMAIndultino extends ActConcessione {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// tutti i controlli e la maggior parte delle request si trovano nel padre
		// passo la posizione precedente per vedere se esiste il verbale o no
		String lRitorno = this.getConcessione("27");
		if (!lRitorno.equals(""))
			return lRitorno;

		// setto il campo codice motivo
		Collection lmotivo = DecodificheManager.getInstance().getMotivoProvvedimentoMAConIndultino();

		String lDesMotivo = "";
		String lCodiceMotivo = "";
		if (lmotivo != null && !lmotivo.isEmpty()) {
			Iterator lIter = lmotivo.iterator();
			if (lIter.hasNext()) {
				DecodificheModel lDecMod = (DecodificheModel) lIter.next();
				lDesMotivo = lDecMod.getDescription();
				lCodiceMotivo = lDecMod.getCode();
			}
		}

		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS());
		setRequestAttribute("tipoUfficioSIUS", "" + lOption);

		setRequestAttribute("motivoProvv", lDesMotivo);
		setRequestAttribute("codicemotivo", lCodiceMotivo);

		setRequestAttribute("tipoMisura", "INDULTINO");

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());

		return PG_LOAD_INSERISCI_MA_CONCESSIONE;
	}

}