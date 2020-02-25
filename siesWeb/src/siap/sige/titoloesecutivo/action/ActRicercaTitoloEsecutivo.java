package siap.sige.titoloesecutivo.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.web.ActionSige;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActRicercaTitoloEsecutivo
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActRicercaTitoloEsecutivo extends ActionSige implements ICostantiTitoloEsecutivo {

	public String processRequest() throws Exception {

		setRequestAttribute("modalita", getRequestStringParameter("modalita"));
		setRequestAttribute("LuogoUtenteConnesso", getUfficioUtenteConnesso().getDescrComune());

		// Istanzio il FascicoloSiepModel
		FascicoloSiepModel lFasMod = new FascicoloSiepModel();

		lFasMod.setChiaveUfficio(getCodUfficioByCodTipoUfficioDescrComune(
				getRequestStringParameter(ICostantiTitoloEsecutivo.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP),
				getRequestStringParameter(ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_FASCICOLO_SIEP)
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
		String aDescrProvvedimento = "";
		String aAutoritaEmittente = "-";
		String aDescrAutoritaEmittente = "";

		if (lFasMod == null) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Attenzione : Titolo Esecutivo non presente in archivio!");
		} else {
			aAutoritaCompetente = lFasMod.getCodTipoUfficio();
			aAutoritaEmittente = lFasMod.getSentenza().getCodTipoAutoritaEmittente();
			aDescrAutoritaEmittente = lFasMod.getSentenza().getDescrTipoAutoritaEmittente();
			aTipoProvvedimento = lFasMod.getSentenza().getCodTipoProvvedimento();
			aDescrProvvedimento = lFasMod.getSentenza().getDescrTipoProvvedimento();
			setRequestAttribute("DescrProvvedimento", "" + aDescrProvvedimento);
			setRequestAttribute("DescrAutoritaEmittente", "" + aDescrAutoritaEmittente);
			setRequestAttribute("FascSiepTrovato", lFasMod);
			setSessionAttribute("fascicolo", lFasMod);
		}

		// Si Imposta l'Autorita Competente.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficio());
		lOption.setFilter(new String[] { "GP", "PM", "PMM", "PGCAP", "TRIBSD", "CAPSM", "TDS", "UDS" }); // solo
																											// le
																											// Autorità
																											// competenti.
		lOption.setSelected(aAutoritaCompetente);
		setRequestAttribute("AutoritaCompetente", "" + lOption);

		// Si Imposta il Tipo Provvedimento (con Rv_Abbreviation = "C"). (NON UTILIZZATO)
		lOption = new Option(DecodificheManager.getInstance().getTipoProvvedimentoCumulo());
		lOption.setValueBlankItem("-");
		lOption.setAddBlankItem(true);
		lOption.setSelected(aTipoProvvedimento);
		setRequestAttribute("TipoProvvedimento", "" + lOption);

		// Si Imposta l'Autorita Emittente.
		lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente());
		lOption.setSelected(aAutoritaEmittente);
		setRequestAttribute("AutoritaEmittente", "" + lOption);

		return PG_ASSEGNA_TITOLOESECUTIVO; // restituisce la jsp di VIEW
	}

}