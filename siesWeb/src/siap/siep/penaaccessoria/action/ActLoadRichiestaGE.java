package siap.siep.penaaccessoria.action;

import java.util.Collection;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;

/**
 * <p>
 * Title: ActLoadRichiestaGE
 * </p>
 * <p>
 * Description: Classe Action per la load Esecuzione PenaAccessoria
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadRichiestaGE extends ActionSiap implements ICostantiPenaAccessoria {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// Data Fascicolo SIEP
		Date lDataInserimento = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		// Identificativi di Eventuale Pena accessoria di partenza.
		String idPenaAccessoria = getRequestStringParameter(ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA);
		String codTipoPenaAccessoria = getRequestStringParameter(
				ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA);
		String descrTipoPenaAccessoria = getRequestStringParameter(
				ICostantiPenaAccessoria.CAMPO_DESCR_TIPO_PENA_ACCESSORIA);

		// Il parametro descrTipoPenaAccessoria potrebbe non essere impostato.
		if (descrTipoPenaAccessoria == null || descrTipoPenaAccessoria.length() < 2)
			descrTipoPenaAccessoria = DecodificheUtils.getDescbyCode(
					DecodificheManager.getInstance().getTipoPeneAccessorie(), codTipoPenaAccessoria);

		setRequestAttribute(ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA, "" + idPenaAccessoria);
		setRequestAttribute(ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA,
				"" + codTipoPenaAccessoria);
		setRequestAttribute(ICostantiPenaAccessoria.CAMPO_DESCR_TIPO_PENA_ACCESSORIA,
				"" + descrTipoPenaAccessoria);

		// Configurazione combo TipoPenaAccessoria.
		// Option lOption = new Option( DecodificheManager.getInstance().getTipoPeneAccessorie());
		Option lOption = new Option(DecodificheUtils
				.getDecodesWithoutCode(DecodificheManager.getInstance().getTipoPeneAccessorie(), "999"));
		setRequestAttribute("TipoPenaAccessoria", "" + lOption);

		// Parametri per configurare la Richiesta al GE.
		String codTipoRichiestaGE = getRequestStringParameter(
				ICostantiPenaAccessoria.CAMPO_COD_TIPO_RICHIESTA_GE);

		// Descrizione Tipo Richiesta GE.
		Collection lColTipoRichiestaGE = null;
		DecodificheModel lModel = new DecodificheModel();

		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("TIPO_RICHIESTA_GE");
		lColTipoRichiestaGE = lDecodifiche.ExRicercaDecodifiche(lModel);
		String descrTipoRichiestaGE = DecodificheUtils.getDescbyCode(lColTipoRichiestaGE, codTipoRichiestaGE);

		setRequestAttribute("DataRichiestaGE_GG", getRequestStringParameter(CAMPO_GIORNO_DATA_RICHIESTAGE));
		setRequestAttribute("DataRichiestaGE_MM", getRequestStringParameter(CAMPO_MESE_DATA_RICHIESTAGE));
		setRequestAttribute("DataRichiestaGE_AA", getRequestStringParameter(CAMPO_ANNO_DATA_RICHIESTAGE));

		setRequestAttribute(ICostantiPenaAccessoria.CAMPO_COD_TIPO_RICHIESTA_GE, "" + codTipoRichiestaGE);
		setRequestAttribute("CodTipoRichiestaGE", "" + codTipoRichiestaGE.trim());
		setRequestAttribute("DescrTipoRichiestaGE", "" + descrTipoRichiestaGE.trim());

		// Costruzione combo "Tipo Durata Pena Accessoria".
		lOption = new Option(DecodificheManager.getInstance().getDurataPeneAccessorie());
		setRequestAttribute("DurataPeneAccessorie", "" + lOption);

		// Gestione LISTA DESTINATARI.
		if (codTipoRichiestaGE.equals(null) || codTipoRichiestaGE.length() < 1) {

		} else {
			switch (Integer.parseInt(codTipoRichiestaGE.trim())) {
			case 1: // Applicazione Pena Accessoria
			case 2: // Quantificazione Pena Accessoria
			case 3: // Condono Pena Accessoria
			case 4: // Applicazione e Condono Pena Accessoria
			case 5: // Revoca Pena Accessoria
			case 6: // Sostituzione Pena Accessoria
			case 7: // Sostituzione e Condono Pena Accessoria
			case 8: // Depenalizzazione Pena Accessoria
			{
				// DESTINATARI : Corte D'Appello,Corte di Assise, Corte di Assise di Appello, Corte Suprema di
				// Cassazione,
				// Gip Presso il Tribunale Ordinario, Gip Presso il Tribunale per i Minorenni, Giudice di
				// Pace,
				// Gup Presso il Tribunale per i Minorenni, Gup Presso Tribunale Ordinario, Sezione Distaccata
				// di Tribunale,
				// Sezione Minorenni per la Corte di Appello, Tribunale Ordinario, Tribunale per i Minorenni.

				lOption = new Option(DecodificheManager.getInstance().getTipoUfficio());
				lOption.setFilter(new String[] { "-", "CAP", "CAS", "CASAP", "CAPMI", "CAPMID", "CSS", "GIP",
						"GIPM", "GP", "GIPMI", "GUP", "GUPM", "GUPMI", "PT", "TRIBSD", "CAPSM", "TMI", "DIB",
						"DIBM" }); // solo le Autorità competenti.
				lOption.setSelected("CAP");
				setRequestAttribute("Destinatario1", "" + lOption);

				// DESTINATARI : Tutte le autorità
				Collection lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				lOption = new Option(lTipoAutorita, "-");
				setRequestAttribute("Destinatario2", "" + lOption);
				setRequestAttribute("Destinatario3", "" + lOption);

				break;
			}

			}
		}
		return PG_LOAD_RICHIESTAGE; // restituisce la jsp di VIEW
	}

}