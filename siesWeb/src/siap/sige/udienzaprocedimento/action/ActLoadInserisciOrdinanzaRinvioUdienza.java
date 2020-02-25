package siap.sige.udienzaprocedimento.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.ufficio.model.UfficioModel;
import siap.sige.aula.controller.IAula;
import siap.sige.aula.model.AulaUdienzaModel;
import siap.sige.magistrato.controller.IMagistrato;
import siap.sige.magistrato.model.MagistratoModel;
import siap.sige.magistratoassegnatario.controller.IMagistratoAssegnatario;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.sezione.controller.ISezione;
import siap.sige.sezione.model.SezioneModel;
import siap.sige.sezione.util.SezioneUtils;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciOrdinanzaRinvioUdienza
 * </p>
 * <p>
 * Description: Classe Action per la load della form di inserimento Ordinanza Rinvio Udienza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadInserisciOrdinanzaRinvioUdienza extends ActLoadInserisciRinvioUdienza
		implements ICostantiUdienzaProcedimentoSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");
		super.mRetPage = PG_LOAD_INSERISCIORDINANZARINVIOUDIENZA;
		String lRetPage = super.processRequest();
		String idSezione = this.getIdSezione();
		Option lOptionSezioniUdienza = new Option(
				SezioneUtils.getElencoSezioni(getCodUfficioUtenteConnesso()), "-", Option.BLANK_ITEM);
		lOptionSezioniUdienza.setValueBlankItem("-");
		lOptionSezioniUdienza.setSelected(idSezione);
		setRequestAttribute("elencoSezioniUdienza", lOptionSezioniUdienza.toString());
		AulaUdienzaModel aulaPredefinita = this.getAulaUdienzaSige(idSezione);

		String descrizioneAula = "";
		String ingressoAula = "";
		String pianoAula = "";
		String idAula = "";

		if (aulaPredefinita != null) {
			descrizioneAula = (aulaPredefinita.getDescrizioneAula() == null ? ""
					: aulaPredefinita.getDescrizioneAula());
			ingressoAula = (aulaPredefinita.getDescrizioneIngresso() == null ? ""
					: aulaPredefinita.getDescrizioneIngresso());
			pianoAula = (aulaPredefinita.getNumeroPiano() == null ? ""
					: aulaPredefinita.getNumeroPiano().toString());
			idAula = aulaPredefinita.getIdAula().toString();
		}

		super.setRequestAttribute("descrizioneAula", descrizioneAula);
		super.setRequestAttribute("ingressoAula", ingressoAula);
		super.setRequestAttribute("pianoAula", pianoAula);
		super.setRequestAttribute("idAula", idAula);

		UfficioModel ufficio = super.getUfficioUtenteConnesso();
		String indirizzoUfficio = (ufficio.getIndirizzo() == null ? "" : ufficio.getIndirizzo());
		super.setRequestAttribute("luogoSvolgimento", indirizzoUfficio);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		try {
			// 20190519 [SG]: aggiunta gestione idUdienzaSige
			BigDecimal idUdienzaSige = null;
			if (!isRequestParameterNullEmptyObj(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE))
				idUdienzaSige = super.getRequestBigDecimalParameter(
						ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE);
			else if (!isRequestParameterNullEmptyObj("idUdiSig"))
				idUdienzaSige = super.getRequestBigDecimalParameter("idUdiSig");

			if (idUdienzaSige != null) {
				IUdienzaSige lCtrlUdienza = SIGELookupRemote.getUdienzaSigeRemote();
				UdienzaSigeModel lUdienzaSige = lCtrlUdienza.ExRicercaUdienzaSigeById(idUdienzaSige);
				Date dataUdienza = lUdienzaSige.getDataUdienza();

				String gg = DateUtils.getDateToString(dataUdienza, "dd");
				String mm = DateUtils.getDateToString(dataUdienza, "MM");
				String aaaa = DateUtils.getDateToString(dataUdienza, "yyyy");

				super.setRequestAttribute("gg", gg);
				super.setRequestAttribute("mm", mm);
				super.setRequestAttribute("aaaa", aaaa);
				super.setRequestAttribute("newIdUdienzaSige", lUdienzaSige.getIdUdienzaSige().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			siesLogger.error(e.getMessage());
		}

		// 20171018: [EC] controllo se esiste sezione per ufficio
		SezioneModel lSezMod = new SezioneModel();
		lSezMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
		lSezMod.setCodice("");
		lSezMod.setDescrizione("");
		ISezione iSezione = SIGELookupRemote.getSezioneRemote();
		Vector sezioni = null;
		try {
			sezioni = iSezione.ExRicercaSezione(lSezMod);
		} catch (Exception ex) {
			siesLogger.info("UFFICIO SENZA SEZIONI!");
			sezioni = new Vector();
		}
		setRequestAttribute("ufficioConSezioni", "" + !sezioni.isEmpty());

		return lRetPage;
	}

	private String getIdSezione() throws F3BException {

		String lSezioneUdienza = "-";
		mFasEsteso = getFascicoloSigeEstesoInSessione();
		IMagistratoAssegnatario lMagCtrl = SIGELookupRemote.getMagistratoAssegnatarioRemote();
		MagistratoAssegnatarioModel lMagAss = lMagCtrl
				.ExRicercaEstesaMagAssCorrenteXFascicolo(mFasEsteso.getFascicoloSige().getIdFascicoloSige());
		if (lMagAss != null) {
			IMagistrato lCtrl = SIGELookupRemote.getMagistratoRemote();
			// 20170918: [SG] aggiunto parametro di passaggio poichè il magistrato può essere inserito da un
			// ufficio differente da quello in cui ha delle udienze poichè trasferito
			MagistratoModel lMagMod = lCtrl.ExRicercaMagistratoByCod(lMagAss.getMagCodMagistrato(),
					getCodUfficioUtenteConnesso());
			if (lMagMod.getMagistratoSezioni().length > 0) {
				BigDecimal idSezMag = lMagMod.getMagistratoSezioni()[0].getSezIdSezione();
				if (idSezMag != null) {
					lSezioneUdienza = idSezMag.toString();
				}
			}
		}
		return lSezioneUdienza;
	}

	private AulaUdienzaModel getAulaUdienzaSige(String sezione) throws F3BException {

		if (sezione != null && sezione.equals("-"))
			return null;

		AulaUdienzaModel aulaUdienza = null;
		IAula lCtrl = SIGELookupRemote.getAulaRemote();
		aulaUdienza = lCtrl.ExRicercaAulaPredefinitaSezione(sezione);
		return aulaUdienza;
	}

}