package siap.sige.udienzaparti.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import f3b.util.Utils;
import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.sige.SIGEException;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienzaparti.controller.IPartiUdienza;
import siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel;
import siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title: ActLoadVisualizzaParti
 * </p>
 * <p>
 * Description: Classe Action per la visualizzazione delle parti (offese, civili) associate ad una udienza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Engineering
 * </p>
 *
 * @version 1.0
 */
public class ActLoadVisualizzaParti extends ActionSige
		implements ICostantiPartiUdienza, ICostantiUdienzaSige, ICostantiEvento {

	String mRetPage = PG_LOAD_VISUALIZZA_PARTI_UDIENZA;

	public String processRequest() throws Exception {

		// Gestione pulsante di ritorno.
		// Ticket#20200528012 - SIGE - inserimento parte civile / offesa su quadro "emissione ordinanza"
		// cambiata gestione ritorno poichè tornava sempre prima su se stessa
		// setLinkRitorno();
		gestioneRitorno();

		if (IsFascicoloSigeIscrittoCompetenza() == false)
			throw new SIGEException(SIGEException.USER_MESSAGE, ICostantiFascicoloSige.MSG_NON_MODIFICABILE);

		String idEventoUdienza = null;
		String idUdienzaSIGE = null;
		String idUdienzaProcedimentoSIGE = null;
		// Indica il tipo di parte interessata all'udienza (O = Offese, C= Civili)
		String codTipoParte = null;

		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_PART))
			codTipoParte = getRequestStringParameter(CAMPO_COD_TIPO_PART);

		// MERGE v10: aggiunto codice di controllo
		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel mFasEsteso = getFascicoloSigeEstesoInSessione();
		if (Utils.isPresent(mFasEsteso.getUdienzaProcedimento())
				&& Utils.isPresent(mFasEsteso.getUdienzaProcedimento().getIdUdienzaProcedimentoSige())) {
			if (!isRequestParameterNullObj(CAMPO_ID_UDIENZA_SIGE))
				idUdienzaSIGE = getRequestStringParameter(CAMPO_ID_UDIENZA_SIGE);
			if (!isRequestParameterNullObj(
					ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE))
				idUdienzaProcedimentoSIGE = getRequestStringParameter(
						ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE);
			if (!isRequestParameterNullObj(CAMPO_ID_EVENTO))
				idEventoUdienza = getRequestStringParameter(CAMPO_ID_EVENTO);
		}

		// chiama il controller
		List<AnagraficaPartiUdienzaModel> lParti = new ArrayList<>();
		if (Utils.isPresent(idUdienzaProcedimentoSIGE)) {
			IPartiUdienza lCtrl = SIGELookupRemote.getPartiUdienzaRemote();
			lParti = lCtrl.ExRicercaPartiUdienzaByIdUdienza(new BigDecimal(idUdienzaProcedimentoSIGE),
					codTipoParte);
		}

		// se non sono presenti parti associate all'udienza
		// visualizzo la pagina di "Inserimento Parti"
		if (lParti.isEmpty())
			mRetPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.sige.udienzaparti.action.ActLoadInserisciParteUdinza&TornaQui=20";

		setRequestAttribute("partiUdienza", lParti);
		setRequestAttribute("codTipoParte", codTipoParte);
		setRequestAttribute("idEventoUdienza", idEventoUdienza);
		setRequestAttribute("idUdienzaSige", idUdienzaSIGE);
		setRequestAttribute("idUdienzaProcedimentoSige", idUdienzaProcedimentoSIGE);
		setRequestAttribute("modificabile", "Si");

		return mRetPage;
	}

}