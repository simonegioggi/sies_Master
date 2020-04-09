package siap.siep.penapecuniaria.action;

/**
 * <p>Title: ActRicercaFascicoloSiep</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.rifasiep.action.ICostantiRifFascicoloSiep;

public class ActRicercaFascicoloSiep extends ActionSiap
		implements ICostantiRifFascicoloSiep, ICostantiPenaPecuniaria {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		setRequestAttribute("modalita", "I");
		setRequestAttribute("LuogoUtenteConnesso", this.getUfficioUtenteConnesso().getDescrComune());

		// Istanzio il FascicoloSiepModel
		FascicoloSiepModel lFasMod = new FascicoloSiepModel();

		lFasMod.setChiaveUfficio(this.getCodUfficioByCodTipoUfficioDescrComune(
				this.getRequestStringParameter(ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP),
				this.getRequestStringParameter(ICostantiRifFascicoloSiep.CAMPO_SEDE_UFF_FASCICOLO_SIEP)
						.toUpperCase()));

		if (!isRequestParameterNullObj(CAMPO_ANNO_FASCICOLO_SIEP))
			lFasMod.setChiaveAnno(getRequestBigDecimalParameter(CAMPO_ANNO_FASCICOLO_SIEP));

		if (!isRequestParameterNullObj(CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN))
			lFasMod.setChiaveProgr(getRequestBigDecimalParameter(CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN));

		// Lettura e passaggio nella request del fascicolo SIEP.
		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		lFasMod = lCtrl.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFasMod);

		// Impostazione dell'autorità competente e del fascicolo SIEP trovato nella request.
		// String aAutoritaCompetente = "PM";
		// String aTipoProvvedimento = "-";
		// String aAutoritaEmittente = "-";
		if (lFasMod == null)
			throw new F3BException(F3BException.USER_MESSAGE,
					"Attenzione : Titolo Esecutivo non presente in archivio!");
		else {
			// aAutoritaCompetente = lFasMod.getCodTipoUfficio();
			// aAutoritaEmittente = lFasMod.getSentenza().getCodTipoAutoritaEmittente();
			// aTipoProvvedimento = lFasMod.getSentenza().getCodTipoProvvedimento();
			setRequestAttribute("FascSiepTrovato", lFasMod);
		}

		// Attributi x LoadInserisciRichiestaConversione.
		BigDecimal lidFascicoloSiep = null;
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		lidFascicoloSiep = lFascMod.getIdFascicoloSiep();

		IRichiestaConversione lCtrlRic = SIEPLookupRemote.getRichiestaConversioneRemote();
		RichiestaConversioneModel lRicMod = new RichiestaConversioneModel();

		IPenaComplessiva lCtrlPC = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaModel lPCMod = new PenaComplessivaModel();
		IPenaResidua lCtrlPR = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPRMod = new PenaResiduaModel();

		/* 
		 * ISSUE MAC : Gli attributi multaResidua e ammendaResidua vengono passati alla request come
		 * BigDecimal e non più come String 
		 * Numero MAC : 20200408012
		 * Autore    : monica
		 * Data      : 08/apr/2020
		 * Branch    : mac-otrs-20200408012
		 */
		setRequestAttribute("multaResidua", new BigDecimal("0"));
		setRequestAttribute("ammendaResidua", new BigDecimal("0"));
		//***** FINE INTERVENTO mac-otrs-20200408012 *****//
		
		int lFascProg = lFascMod.getChiaveProgr().intValue();
		if (lFascProg > 70000 && lFascProg < 80000) {
			// se sono in classe VII vado a cercare la richiesta di conversione legata al procedimento
			lRicMod = lCtrlRic.ExRicercaRichiestaConversioneByIdFascicoloSiep(lidFascicoloSiep);
			// se sono in classe VII vado a cercare la pena complessiva per passare i quantum.
			lPCMod = lCtrlPC.ExRicercaPenaComplessivaByIdFascicolo(lidFascicoloSiep);
			/* 
			 * ISSUE MAC : Gli attributi multaResidua e ammendaResidua vengono passati alla request come
			 * BigDecimal e non più come String 
			 * Numero MAC : 20200408012
			 * Autore    : monica
			 * Data      : 08/apr/2020
			 * Branch    : mac-otrs-20200408012
			 */
			setRequestAttribute("multaResidua",lPCMod.getImportoMulta());
			setRequestAttribute("ammendaResidua",lPCMod.getImportoAmmenda());
			//***** FINE INTERVENTO mac-otrs-20200408012 *****//
		} else {
			// se sono in classe I vado a cercare la richiesta di conversione legata al procedimento collegato
			// ossia di classe VII
			lRicMod = lCtrlRic.ExRicercaRichiestaConversioneByIdFascicoloSiepClasseI(lidFascicoloSiep);
			// se sono in classe I vado a cercare la pena residua per passare i quantum.
			lPRMod = lCtrlPR.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lidFascicoloSiep);
			/* 
			 * ISSUE MAC : Gli attributi multaResidua e ammendaResidua vengono passati alla request come
			 * BigDecimal e non più come String 
			 * Numero MAC : 20200408012
			 * Autore    : monica
			 * Data      : 08/apr/2020
			 * Branch    : mac-otrs-20200408012
			 */
			if (lPRMod != null && lPRMod.getImportoMulta() != null)
				setRequestAttribute("multaResidua", lPRMod.getImportoMulta());
			if (lPRMod != null && lPRMod.getImportoAmmenda() != null)
				setRequestAttribute("ammendaResidua", lPRMod.getImportoAmmenda());
			//***** FINE INTERVENTO mac-otrs-20200408012 *****//
		}

		setRequestAttribute("richiestaconversione", lRicMod);

		if (lRicMod != null && lRicMod.getIdRichiestaConversione() != null)
			return PG_LOAD_DETTAGLIO_RICHIESTA_CONVERSIONE;

		// Autorità per la conversione.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
		lOption.setFilter(new String[] { "-", "36", "99", "57", "37", "98", "38" });
		setRequestAttribute("autoritaConv", "" + lOption);

		// Si Imposta l'Autorita Competente.
		lOption = new Option(DecodificheManager.getInstance().getTipoUfficio());
		lOption.setFilter(new String[] { "PM", "PMM", "PGCAP" }); // solo le Autorità competenti.
		lOption.setSelected("PM");
		setRequestAttribute("AutoritaCompetente", "" + lOption);

		// esegue la query per recuperare l'elenco degli uffici accorpati
		IUfficio lUACon = SICOLookupRemote.getUfficioRemote();
		Vector lUffAccTotali = lUACon.ListaUfficiAccorpati(null, null);
		setRequestAttribute("ufficiAccorpati", lUffAccTotali);

		// restituisce la jsp di VIEW
		return PG_LOAD_INSERISCIRICHIESTACONVERSIONE;
	}

}