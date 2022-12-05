package siap.sius.provvedimento.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import f3b.util.Utils;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.ActionSius;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.impugnazione.controller.IImpugnazione;
import siap.sius.util.SIUSLookupRemote;

/**
 * Title: ActRicercaProvvedimentii
 * Description: Azione specializzazione per la ricerca dei Provvedimenti legati al fascicolo SIUS.
 *
 * @version 1.0
 */
public class ActRicercaProvvedimenti extends ActionSius implements ICostantiProvvedimento {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// Gestione del punto di ritorno
		setLinkRitorno();

		Vector v = null;
		BigDecimal idFascicolo = null;
		// MEV_9: tirato fuori dal ramo else ed impostato nella request come attributo
		FascicoloGPModel fgpm = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		setRequestAttribute("fascicoloSiusGP", fgpm);

		// Cambia la gestione del ritorno Luigi 30-4-2004
		if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS)) {
			idFascicolo = getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS);
		} else {
			idFascicolo = fgpm.getFascicoloSiusModel().getIdFascicoloSius();
		}
		IEvento mCtrl = SICOLookupRemote.getEventoRemote();
		v = mCtrl.ExRicercaEventoByFascicoloSius(idFascicolo, COD_EVENTO_PROVVEDIMENTO);
		setRequestAttribute("provvedimenti", v);

		IImpugnazione ii = SIUSLookupRemote.getImpugnazioneRemote();
		Vector dataRicorso = new Vector();
		if (v.size() > 0) {
			Iterator itx = v.iterator();
			while (itx.hasNext()) {
				EventoModel em = (EventoModel) itx.next();
				dataRicorso.add(ii.ExRicercaDataRicorso(em.getIdEvento(), em.getCodTipoProvvedimento()));
			}
		}
		setRequestAttribute("provvedimentiDataRicorso", dataRicorso);

		// Controlla se il fascicolo e' modificabile
		String modificabile = "NO";
		if (IsFascicoloSiusModificabile())
			modificabile = "SI";
		else
			modificabile = "NO";
		setRequestAttribute("isModificabile", modificabile);

		/* 
		 * ISSUE MEV : aggiunta estrazione data esecutivita
		 * Numero MEV : 9
		 * Autore    : sgioggi
		 * Data      : 5 dic 2022
		 * Branch    : MEV_9
		 */
		if (!Utils.isNullObj(fgpm) && !Utils.isNullObj(fgpm.getGeneraleProcedimentoModel())
				&& !Utils.isNullObj(fgpm.getGeneraleProcedimentoModel().getCodOggettoProcedimento())
				&& (fgpm.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("C050") == 0
				|| fgpm.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("C051") == 0)) {
			IDepositoOrdinanzaPc idop = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			DepositoOrdinanzaPcModel dopm = idop.ExRicercaDepositoOrdinanzaPcByGenProc(
					fgpm.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			Date dataEsecutivita = null;
			if (!Utils.isNullObj(dopm) && !Utils.isNullObj(dopm.getDataEsecutivita()))
				dataEsecutivita = dopm.getDataEsecutivita();
			setRequestAttribute("dataEsecutivita", dataEsecutivita);
		}
		//***** FINE INTERVENTO MEV_9 *****//

		return PG_ELENCOPROVVEDIMENTI;
	}

}