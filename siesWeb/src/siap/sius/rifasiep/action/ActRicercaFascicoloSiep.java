package siap.sius.rifasiep.action;

import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActRicercaFascicoloSiep
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class ActRicercaFascicoloSiep extends ActionSiap implements ICostantiRifFascicoloSiep {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		setRequestAttribute("modalita", "I");
		setRequestAttribute("LuogoUtenteConnesso", getUfficioUtenteConnesso().getDescrComune());

		// Istanzio il FascicoloSiepModel
		FascicoloSiepModel lFasMod = new FascicoloSiepModel();

		lFasMod.setChiaveUfficio(getCodUfficioByCodTipoUfficioDescrComune(
				getRequestStringParameter(ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP),
				getRequestStringParameter(ICostantiRifFascicoloSiep.CAMPO_SEDE_UFF_FASCICOLO_SIEP)
						.toUpperCase()));

		if (!isRequestParameterNullObj(CAMPO_ANNO_FASCICOLO_SIEP))
			lFasMod.setChiaveAnno(getRequestBigDecimalParameter(CAMPO_ANNO_FASCICOLO_SIEP));

		if (!isRequestParameterNullObj(CAMPO_PROGR_FASCICOLO_SIEP))
			lFasMod.setChiaveProgr(getRequestBigDecimalParameter(CAMPO_PROGR_FASCICOLO_SIEP));

		// Lettura e passaggio nella request del fascicolo SIEP.
		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		lFasMod = lCtrl.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFasMod);

		// Impostazione dell'autorità competente e del fascicolo SIEP trovato nella request.
		String aAutoritaCompetente = "PM";
		String aTipoProvvedimento = "-";
		String aAutoritaEmittente = "-";
		if (lFasMod == null)
			throw new F3BException(F3BException.USER_MESSAGE,
					"Attenzione : Titolo Esecutivo non presente in archivio!");
		else {
			aAutoritaCompetente = lFasMod.getCodTipoUfficio();
			aAutoritaEmittente = lFasMod.getSentenza().getCodTipoAutoritaEmittente();
			aTipoProvvedimento = lFasMod.getSentenza().getCodTipoProvvedimento();
			setRequestAttribute("FascSiepTrovato", lFasMod);
		}

		// Si Imposta l'Autorita Competente.
		// Option lOption = new Option( DecodificheManager.getInstance().getTipoUfficio());
		// lOption.setFilter( new String[] {"GP", "PM", "PMM", "PGCAP", "TRIBSD", "CAPSM", "TDS", "UDS"} );
		// //solo le Autorità competenti.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaCumulo(), "-");
		lOption.setSelected(aAutoritaCompetente);
		setRequestAttribute("AutoritaCompetente", "" + lOption);

		// Si Imposta il Tipo Provvedimento (con Rv_Abbreviation = "C").
		lOption = new Option(DecodificheManager.getInstance().getTipoProvvedimentoCumulo());
		lOption.setValueBlankItem("-");
		lOption.setAddBlankItem(true);
		lOption.setSelected(aTipoProvvedimento);
		setRequestAttribute("TipoProvvedimenti", "" + lOption);

		// Si Imposta l'Autorita Emittente.
		// lOption = new Option( DecodificheManager.getInstance().getTipoAutoritaEmittente());
		lOption = new Option(DecodificheManager.getInstance().getTipoUfficioCumuloSentenzaDecreto(), "-");
		lOption.setSelected(aAutoritaEmittente);
		setRequestAttribute("AutoritaEmittente", "" + lOption);

		// esegue la query per recuperare l'elenco degli uffici accorpati
		IUfficio lUACon = SICOLookupRemote.getUfficioRemote();
		Vector lUffAccTotali = lUACon.ListaUfficiAccorpati(null, null);
		setRequestAttribute("ufficiAccorpati", lUffAccTotali);

		return PG_LOAD_INSERISCIRIFASIEP; // restituisce la jsp di VIEW
	}

}