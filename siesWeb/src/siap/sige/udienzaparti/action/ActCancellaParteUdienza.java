package siap.sige.udienzaparti.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienzaparti.controller.IPartiUdienza;
import siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

public class ActCancellaParteUdienza extends ActionSige implements ICostantiPartiUdienza, ICostantiUdienzaSige, ICostantiEvento {

	protected FascicoloSigeEstesoModel mFascicoloEsteso = null;

	/**
	 * Azione di cancellazione della Parte di una udienza
	 */
	public String processRequest() throws Exception {

		// Fascicolo Sige Esteso in sessione.
		mFascicoloEsteso = getFascicoloSigeEstesoInSessione();

		BigDecimal lIdChiaveAnno = mFascicoloEsteso.getFascicoloSige().getChiaveAnno();
		BigDecimal lIdChiaveProgr = mFascicoloEsteso.getFascicoloSige().getChiaveProgr();

		// parametri necessari per richiamare l'elenco delle parti associate ad una udienza
		String idEventoUdienza = null;
		String idUdienzaSIGE = null;
		String idUdienzaProcedimentoSIGE = null;
		// Indica il tipo di parte interessata all'udienza (O = Offese, C= Civili)
		String codTipoParte = null;

		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_PART)) {
			codTipoParte = getRequestStringParameter(CAMPO_COD_TIPO_PART);
		}

		if (!isRequestParameterNullObj(ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE)) {
			idUdienzaProcedimentoSIGE = getRequestStringParameter(ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE);
		}

		if (!isRequestParameterNullObj(CAMPO_ID_UDIENZA_SIGE)) {
			idUdienzaSIGE = getRequestStringParameter(CAMPO_ID_UDIENZA_SIGE);
		}

		// Identificativo evento udienza
		if (!isRequestParameterNullObj(CAMPO_ID_EVENTO_UDIENZA)) {
			idEventoUdienza = getRequestStringParameter(CAMPO_ID_EVENTO_UDIENZA);
		}

		// Identificativo della parte
		String lIdSoggetto = getRequestStringParameter(CAMPO_ID_SOGGETTO);

		// chiama il controller
		IPartiUdienza lCtrl = SIGELookupRemote.getPartiUdienzaRemote();
		lCtrl.ExCancellaParteUdienza(new BigDecimal(lIdSoggetto));

		StringBuffer next = new StringBuffer();
		next.append("siap.sige.udienzaparti.action.ActLoadVisualizzaParti");
		next.append("&" + CAMPO_ID_EVENTO + "=" + idEventoUdienza);
		next.append("&" + CAMPO_ID_UDIENZA_SIGE + "=" + idUdienzaSIGE);
		next.append("&" + ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE + "=" + idUdienzaProcedimentoSIGE);
		next.append("&" + CAMPO_COD_TIPO_PART + "=" + codTipoParte);
		next.append("&" + ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO + "=" + lIdChiaveAnno.toString());
		next.append("&" + ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR + "=" + lIdChiaveProgr.toString());

		return ritornoDopoCancellazione("Parte Udienza cancellata", next.toString());
	}
}
