package siap.siep.reatopredisposto.action;

import java.math.BigDecimal;
import java.util.ArrayList;

import siap.sico.web.ActionSiap;
import siap.siep.reatopredisposto.controller.ReatoPredispostoController;
import siap.siep.reatopredisposto.model.ReatoPredispostoModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInserisciReatoPredisposto
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di ReatoPredisposto
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciReatoPredisposto extends ActionSiap implements ICostantiReatoPredisposto {

	/**
	 * Azione di Inserimento del ReatoPredisposto
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		ReatoPredispostoModel lReaMod;

		ArrayList lReati = new ArrayList();

		String[] CodFonti = getRequestStringParameters(CAMPO_COD_FONTE);
		String[] AnnoFonti = getRequestStringParameters(CAMPO_ANNO_FONTE);
		String[] NumeroFonti = getRequestStringParameters(CAMPO_NUMERO_FONTE);
		String[] SottoNum = getRequestStringParameters(CAMPO_COD_SOTTONUMERAZIONE);
		String[] Commi = getRequestStringParameters(CAMPO_COMMA);
		String[] Lettere = getRequestStringParameters(CAMPO_LETTERA);
		String[] Numeri = getRequestStringParameters(CAMPO_NUMERO);
		String[] Articoli = getRequestStringParameters(CAMPO_ARTICOLO);
		String[] Cablati2 = null;

		// Se c'è una sola riga significa che provengo da Modifica
		if (CodFonti.length != 1) {

			try {
				Cablati2 = getRequestStringParameters("cablati2");
			} catch (F3BException f3e) {
			}

			if (Cablati2 != null) {
				for (int i = 0; i < Cablati2.length; i++) {

					lReaMod = new ReatoPredispostoModel();

					lReaMod.setNomeElemento(getRequestStringParameter(CAMPO_NOME_ELEMENTO));

					String[] arrDati = Cablati2[i].split("-");

					lReaMod.setCodFonte(arrDati[0]);
					lReaMod.setArticolo(arrDati[1]);

					if (arrDati.length > 2)
						lReaMod.setComma(arrDati[2]);

					lReaMod.setCodSottonumerazione("-");

					lReaMod.setNoteElemento(getRequestStringParameter(CAMPO_NOTE_ELEMENTO));
					lReaMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
					lReaMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
					lReaMod.setDataInserimento(DateUtils.getSysDate());

					lReati.add(lReaMod);
				}
			}
		}

		for (int i = 0; i < CodFonti.length; i++) {

			if (CodFonti[i] != null && !CodFonti[i].equals("-")) {

				lReaMod = new ReatoPredispostoModel();

				// serve solo per Modifica
				if (CodFonti.length == 1)
					lReaMod.setIdReatoPredisposto(getRequestBigDecimalParameter(CAMPO_ID_REATO_PREDISPOSTO));

				lReaMod.setNomeElemento(getRequestStringParameter(CAMPO_NOME_ELEMENTO));

				lReaMod.setCodFonte(CodFonti[i]);

				if (AnnoFonti[i] != null && !AnnoFonti[i].equals(""))
					lReaMod.setAnnoFonte(new BigDecimal(AnnoFonti[i]));

				lReaMod.setNumeroFonte(NumeroFonti[i]);
				lReaMod.setCodSottonumerazione(SottoNum[i]);
				lReaMod.setComma(Commi[i]);
				lReaMod.setLettera(Lettere[i]);
				lReaMod.setNumero(Numeri[i]);
				lReaMod.setArticolo(Articoli[i]);

				lReaMod.setNoteElemento(getRequestStringParameter(CAMPO_NOTE_ELEMENTO));

				lReaMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
				lReaMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
				lReaMod.setDataInserimento(DateUtils.getSysDate());

				lReati.add(lReaMod);
			}
		}

		// ---Aggiungere in SIEPLookupRemote il metodo getReatoPredispostoRemote()

		if (lReati.size() == 0)
			throw new F3BException(F3BException.USER_MESSAGE, "Specificare almeno una norma!");

		// IReatoPredisposto lCtrl = SIEPLookupRemote.getReatoPredispostoRemote();
		ReatoPredispostoController lCtrl = new ReatoPredispostoController();

		String lPage = "";

		// Se c'è una sola riga significa che provengo da Modifica
		if (CodFonti.length == 1) {

			lReaMod = new ReatoPredispostoModel();
			lReaMod = (ReatoPredispostoModel) lReati.get(0);

			ReatoPredispostoModel llReaModRet = lCtrl.ExModificaReatoPredisposto(lReaMod);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.reatopredisposto.action.ActLoadDettaglioReatoPredisposto&"
					+ CAMPO_ID_REATO_PREDISPOSTO + "=" + llReaModRet.getIdReatoPredisposto().toString();
		} else {
			ReatoPredispostoModel llReaModRet = lCtrl.ExInserisciReatiPredisposti(lReati);

			// setta la risposta nella request
			setRequestAttribute("ComingFromInsert", "YES");

			// Prepara la pagina di destinazione
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.reatopredisposto.action.ActRicercaReatoPredisposto&" + CAMPO_NOME_ELEMENTO
					+ "=" + llReaModRet.getNomeElemento();
		}
		return lPage;
	}

}