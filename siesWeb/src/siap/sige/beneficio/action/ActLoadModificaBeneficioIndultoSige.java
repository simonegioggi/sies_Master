package siap.sige.beneficio.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.beneficio.action.ActLoadModificaBeneficioIndulto;
import siap.siep.penaaccessoria.controller.IPenaAccessoria;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.penaaccessoria.model.PenaAccSigeModel;
import siap.sige.sentenza.action.ICostantiFasSigeSentenza;

/**
 * <p>
 * Title: ActLoadModificaBeneficioIndultoSige
 * </p>
 * <p>
 * Description: Classe Action per la creazione e visualizzazione della form di modifica
 * <p>
 * per il beneficio di tipo Indulto.
 * <p>
 * Poichè la funzione è analoga a quella di SIEP, viene ereditata la funzione corrispondente
 * <p>
 * aggiungendo però l'attributo modo = "SIGE" per poter specializzare la jsp utilizzata
 * <p>
 * sia nel caso siep che sige.
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company: Agile
 * </p>
 * 
 * @version 1.0
 */

public class ActLoadModificaBeneficioIndultoSige extends ActLoadModificaBeneficioIndulto {
	public String processRequest() throws Exception {
		ricercaPene();
		setRequestAttribute("modo", "SIGE");
		return preparaDatiForm(); // restituisce la jsp di VIEW
	}

	@SuppressWarnings("rawtypes")
	protected void ricercaPene() throws Exception {

		PenaComplessivaModel lPenComMod = null;

		// Lettura dalla request dell'ID dell'aggregato FascicoloSige_Sentenza dalla sessione
		BigDecimal lIdFasSigeSen = (BigDecimal) getSessionAttribute(
				ICostantiFasSigeSentenza.CAMPO_ID_FAS_SIGE_SENTENZA);

		// Ricerca Pena Complessiva
		IPenaComplessiva lCtrl = SIEPLookupRemote.getPenaComplessivaRemote();
		DettaglioPenaComplessivaModel lDettPenComMod = lCtrl
				.ExRicercaPenaComplessivaCompletaByIdSIGE(lIdFasSigeSen);
		if (lDettPenComMod != null && lDettPenComMod.getPenaComplessivaSanzioneSostitutiva() != null)
			lPenComMod = lDettPenComMod.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva();

		setRequestAttribute("penacomplessiva", lPenComMod);

		// Ricerca Pena Accessoria
		PenaAccSigeModel lPenaAccessoriaSige = null;
		lPenaAccessoriaSige = new PenaAccSigeModel();
		lPenaAccessoriaSige.setFasSigeSenId(lIdFasSigeSen);
		IPenaAccessoria lCtrlPenAc = SIEPLookupRemote.getPenaAccessoriaRemote();
		Vector lVect = lCtrlPenAc.ExRicercaPenaAccessoriaNoError(lPenaAccessoriaSige);
		setRequestAttribute("peneaccessorie", lVect);
	}

}