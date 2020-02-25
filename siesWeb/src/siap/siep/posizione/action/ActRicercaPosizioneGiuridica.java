package siap.siep.posizione.action;

/**
* <p>Title: ActRicercaPosizioneGiuridica</p>
* <p>Description: Classe Action per la ricerca di PosizioneGiuridica</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.web.ActionSiap;
import siap.siep.altracausa.controller.IAltraCausa;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.posizione.controller.IPosizioneGiuridicaLuogoDetenzione;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;

public class ActRicercaPosizioneGiuridica extends ActionSiap implements ICostantiPosizioneGiuridica {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		Vector lVectPos = new Vector();
		boolean flagIdFasc = false;
		BigDecimal lIdFasc = null;

		if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP)) {
			lIdFasc = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);
			flagIdFasc = true;
		} else if (!isRequestAttributeNullObj(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP)) {
			lIdFasc = (BigDecimal) getRequestAttribute(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);
			flagIdFasc = true;
		}
		if (flagIdFasc) {
			IPosizioneGiuridicaLuogoDetenzione lCtrl = SIEPLookupRemote
					.getPosizioneGiuridicaLuogoDetenzioneRemote();
			lVectPos = lCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaByIdFascicolo(lIdFasc);
		}

		if (lVectPos.isEmpty()) {
			// throw new F3BException(F3BException.USER_MESSAGE, "Nessuna Posizione Giuridica trovata");
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessuna Posizione Giuridica trovata");

			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo");
			lRedirigi.setParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP, "" + lIdFasc);

			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE; // restituisce la jsp di VIEW }

		} else {
			// inizio gestione desc posizione giuridica
			// I) se la posizione giuridica !="07" ==> parto dalla tabella POSIZIONE_GIURIDICA tramite
			// CG_REF_CODES prendo la DescrPosizioneGiuridica
			// II) se la posizione giuridica ="07" && COD_MASCHERA="L" non è presente altra causa la
			// DescrPosizioneGiuridica="Libero"
			// else la posizione giuridica ="07" ==> è presente altra causa la DescrPosizioneGiuridica si
			// considera
			// la desc di CG_REF_CODES tramite la tabella ALTRA_CAUSA partendo dalla tabella
			// POSIZIONE_GIURIDICA.ALT_CAU_ID_ALTRA_CAUSA
			// II.1) per la vecchia gestione posizione giuridica ="07" &&
			// POSIZIONE_GIURIDICA.ALT_CAU_ID_ALTRA_CAUSA=null && COD_MASCHERA=NULL
			// trovo la descrizione sulla tabella ALTRA_CAUSA tramite FAS_ID_FASCICOLO_SIEP se trovo
			// ALTRA_CAUSA metto la descrizione
			// di altra causa altrimenti libero
			IAltraCausa lAcCtrl = SIEPLookupRemote.getAltraCausa();
			Vector lVectPosizione = new Vector();
			for (int i = 0; i < lVectPos.size(); i++) {
				PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltMod = (PosizioneGiuridicaLuogoDetenzioneAltraCausaModel) lVectPos
						.get(i);

				if (lPosLuoAltMod.getPosizioneGiuridica() != null && !lPosLuoAltMod.getPosizioneGiuridica()
						.getCodPosizioneGiuridica().equalsIgnoreCase("07")) {
					// è già presente la descrizione giusta
				} else if (lPosLuoAltMod.getPosizioneGiuridica() != null
						&& lPosLuoAltMod.getPosizioneGiuridica().getCodPosizioneGiuridica()
								.equalsIgnoreCase("07")
						&& lPosLuoAltMod.getPosizioneGiuridica().getAltCauIdAltraCausa() == null) {
					AltraCausaModel altraCausa = lAcCtrl.ExRicercaAltraCausaIstitutoByFascicolo(
							lPosLuoAltMod.getPosizioneGiuridica().getFasSieIdFascicoloSiep());
					if (altraCausa != null
							&& lPosLuoAltMod.getPosizioneGiuridica().getCodMaschera() == null) {
						// vecchia gestione
						lPosLuoAltMod.getPosizioneGiuridica()
								.setDescrPosizioneGiuridica(altraCausa.getDescrTipoPosGiuridica());
					} else if (lPosLuoAltMod.getPosizioneGiuridica().getCodMaschera() == "L") {
						lPosLuoAltMod.getPosizioneGiuridica().setDescrPosizioneGiuridica("Libero");
					}
				} else if (lPosLuoAltMod.getPosizioneGiuridica() != null
						&& lPosLuoAltMod.getPosizioneGiuridica().getCodPosizioneGiuridica()
								.equalsIgnoreCase("07")
						&& lPosLuoAltMod.getPosizioneGiuridica().getAltCauIdAltraCausa() != null) {
					// Ricerca Altra Causa
					AltraCausaModel altraCausa = lAcCtrl.ExRicercaAltraCausaIstitutoByKey(
							lPosLuoAltMod.getPosizioneGiuridica().getAltCauIdAltraCausa());
					lPosLuoAltMod.getPosizioneGiuridica()
							.setDescrPosizioneGiuridica(altraCausa.getDescrTipoPosGiuridica());
				}
				lVectPosizione.addElement(lPosLuoAltMod);
			}

			lVectPos = new Vector(lVectPosizione);
			// fine gestione desc posizione giuridica

			setRequestAttribute("posizionigiuridiche", lVectPos);

			return PG_RICERCAPOSIZIONEGIURIDICA;

		}

	}
}
