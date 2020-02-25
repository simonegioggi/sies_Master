package siap.siep.reato.action;

/**
* <p>Title: ActInserisciReato</p>
* <p>Description: Classe Action per l'inserimento di Reato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.ArrayList;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciUlterioriReati extends ActionSiap implements ICostantiReato {

	// Elenco Reati da inserire
	protected ArrayList mReati;
	// Reato principale cui aggiungere gli ulteriori reati
	protected ReatoModel mReatoPrincipale;

	/**
	 * Azione di Inserimento del Reato
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		if (!this.isRequestParameterNullObj("lTipoFunzione")) // paramentro passato solo nel caso di
																// iscrizione guidata
		{
			this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
		}
		// Lettura Fascicolo SIEP dalla sessione
		FascicoloSiepModel lFascicolo = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Preparazione della lista di reati da inserire tramite lettura della request
		letturaDati(lFascicolo.getIdFascicoloSiep());

		if (mReati.size() == 0)
			throw new F3BException(F3BException.USER_MESSAGE, "Specificare almeno un reato!");

		//
		IReato lCtrl = SIEPLookupRemote.getReatoRemote();

		lCtrl.ExInserisciUlterioriReati(mReatoPrincipale, mReati);

		// setta la risposta nella request
		setRequestAttribute("ComingFromInsert", "YES");

		// Prepara la pagina di destinazione
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.reato.action.ActRicercaUlterioriNorme&" + CAMPO_PROGR_REATO + "="
				+ mReatoPrincipale.getProgrReato();

		return lPage;
	}

	/**
	 * Lettura dei dati dalla form di input e valorizzazione dell'elenco mReati.
	 * 
	 * @param aIdFascicoloSiep
	 * @throws Exception
	 */
	protected void letturaDati(BigDecimal aIdFascicoloSiep) throws Exception {

		mReatoPrincipale = (ReatoModel) getSessionAttribute("reato");

		ReatoModel lReaMod = new ReatoModel();

		mReati = new ArrayList();

		String[] CodFonti = getRequestStringParameters(CAMPO_COD_FONTE);
		String[] AnnoFonti = getRequestStringParameters(CAMPO_ANNO_FONTE);
		String[] NumeroFonti = getRequestStringParameters(CAMPO_NUMERO_FONTE);
		String[] SottoNum = getRequestStringParameters(CAMPO_COD_SOTTONUMERAZIONE);
		String[] Commi = getRequestStringParameters(CAMPO_COMMA);
		// *****************************************************************************
		// Federica - a9-rr-078
		// aggiunto comma qualificante
		String[] CommiQualif = getRequestStringParameters(CAMPO_COMMA_QUALIFICANTE);
		// *****************************************************************************
		String[] Lettere = getRequestStringParameters(CAMPO_LETTERA);
		String[] Numeri = getRequestStringParameters(CAMPO_NUMERO);
		String[] Articoli = getRequestStringParameters(CAMPO_ARTICOLO);

		String[] Cablati = { "" };
		if (getRequestStringParameters("cablati") != null)
			Cablati = getRequestStringParameters("cablati");

		if (CodFonti.length > 0) {
			for (int i = 0; i < 5; i++) {
				if (CodFonti[i] != null && !CodFonti[i].equals("-")) {
					lReaMod = new ReatoModel();

					lReaMod.setCodFonte(CodFonti[i]);
					if (AnnoFonti[i] != null && !AnnoFonti[i].equals("")) {
						lReaMod.setAnnoFonte(new BigDecimal(AnnoFonti[i]));
					}
					lReaMod.setNumeroFonte(NumeroFonti[i]);
					lReaMod.setCodSottonumerazione(SottoNum[i]);
					lReaMod.setComma(Commi[i]);
					// ************************************************
					// Federica - a9-rr-078
					// aggiunto comma qualificante
					lReaMod.setCommaQualificante(CommiQualif[i]);
					// ************************************************
					lReaMod.setLettera(Lettere[i]);
					lReaMod.setNumero(Numeri[i]);
					lReaMod.setArticolo(Articoli[i]);

					// * Parte comune *
					lReaMod.setProgrNumeroManuale(mReatoPrincipale.getProgrNumeroManuale());

					lReaMod.setCodTipoReato(mReatoPrincipale.getCodTipoReato());

					// ?-- lReaMod.setDataReato( getRequestDateParameter(
					// CAMPO_ANNO_DATA_REATO,CAMPO_MESE_DATA_REATO,CAMPO_GIORNO_DATA_REATO) );

					lReaMod.setCodPeriodoConsumazione(mReatoPrincipale.getCodPeriodoConsumazione());
					lReaMod.setDescLuogo(mReatoPrincipale.getDescLuogo());

					// <Data1>
					lReaMod.setDataInizio(mReatoPrincipale.getDataInizio());
					lReaMod.setAnnoInizio(mReatoPrincipale.getAnnoInizio());
					lReaMod.setMeseInizio(mReatoPrincipale.getMeseInizio());
					lReaMod.setGiornoInizio(mReatoPrincipale.getGiornoInizio());

					// <Data2>
					lReaMod.setDataFine(mReatoPrincipale.getDataFine());
					lReaMod.setAnnoFine(mReatoPrincipale.getAnnoFine());
					lReaMod.setMeseFine(mReatoPrincipale.getMeseFine());
					lReaMod.setGiornoFine(mReatoPrincipale.getGiornoFine());

					lReaMod.setNote(mReatoPrincipale.getNote());

					// lReaMod.setCodTipoPenaDetentiva("-"); // Per le join
					// lReaMod.setCodTipoSanzione("-"); // Per le join

					lReaMod.setCodTipoPenaDetentiva(mReatoPrincipale.getCodTipoPenaDetentiva());
					lReaMod.setNumGiorni(mReatoPrincipale.getNumGiorni());
					lReaMod.setNumMesi(mReatoPrincipale.getNumMesi());
					lReaMod.setNumAnni(mReatoPrincipale.getNumAnni());
					lReaMod.setNumAnniIsolamentoDiurno(mReatoPrincipale.getNumAnniIsolamentoDiurno());
					lReaMod.setNumMesiIsolamentoDiurno(mReatoPrincipale.getNumMesiIsolamentoDiurno());
					lReaMod.setNumGiorniIsolamentoDiurno(mReatoPrincipale.getNumGiorniIsolamentoDiurno());
					lReaMod.setDataInizioIsolamentoDiurno(mReatoPrincipale.getDataInizioIsolamentoDiurno());
					lReaMod.setDataFineIsolamentoDiurno(mReatoPrincipale.getDataFineIsolamentoDiurno());
					lReaMod.setSanzionePecuniaria(mReatoPrincipale.getSanzionePecuniaria());
					lReaMod.setCodTipoSanzione(mReatoPrincipale.getCodTipoSanzione());

					lReaMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
					lReaMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
					lReaMod.setDataInserimento(DateUtils.getSysDate());

					if (aIdFascicoloSiep != null)
						lReaMod.setFasSieIdFascicoloSiep(aIdFascicoloSiep);

					mReati.add(lReaMod);
				}
			}
		}

		String fonte = new String("");
		String articolo = new String("");
		String comma = new String("");
		String numero = new String("");
		int spacePos1;
		int spacePos2;

		if (getRequestStringParameters("cablati") != null) {
			ReatoModel lReaMod2;
			for (int i = 0; i < Cablati.length - 1; i++) // -1 perchè c'è sempre almeno un cablati in hidden
			{
				lReaMod2 = new ReatoModel();
				lReaMod2.setCodOperatoreInserimento(this.getCodUtenteConnesso());
				lReaMod2.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
				lReaMod2.setDataInserimento(DateUtils.getSysDate());

				comma = "";
				numero = "";
				spacePos1 = Cablati[i].indexOf(" ");
				articolo = Cablati[i].substring(0, spacePos1).trim();

				spacePos2 = Cablati[i].lastIndexOf(" ");
				if (spacePos2 == spacePos1) {
					fonte = Cablati[i].substring(spacePos1).trim();
					comma = "";
				} else {
					fonte = Cablati[i].substring(spacePos1, spacePos2).trim();
					if (Cablati[i].substring(spacePos2, spacePos2 + 2).trim().equals("N")) {
						char n = 'N';
						char nullChar = ' ';
						numero = Cablati[i].substring(spacePos2).replace(n, nullChar).trim();
					} else if (Cablati[i].substring(spacePos2, spacePos2 + 2).trim().equals("C")) {
						char c = 'C';
						char nullChar = ' ';
						comma = Cablati[i].substring(spacePos2).replace(c, nullChar).trim();
					}
				}

				lReaMod2.setCodFonte(fonte);
				lReaMod2.setComma(comma);
				lReaMod2.setNumero(numero);
				// ************************************************
				// Federica - a9-rr-078
				// aggiunto comma qualificante
				lReaMod2.setCommaQualificante("-");
				// ************************************************
				lReaMod2.setArticolo(articolo);
				lReaMod2.setCodSottonumerazione("-");

				// * Parte comune *
				lReaMod2.setProgrNumeroManuale(mReatoPrincipale.getProgrNumeroManuale());

				lReaMod2.setCodTipoReato(mReatoPrincipale.getCodTipoReato());

				// ?-- lReaMod2.setDataReato( getRequestDateParameter(
				// CAMPO_ANNO_DATA_REATO,CAMPO_MESE_DATA_REATO,CAMPO_GIORNO_DATA_REATO) );

				lReaMod2.setCodPeriodoConsumazione(mReatoPrincipale.getCodPeriodoConsumazione());
				lReaMod2.setDescLuogo(mReatoPrincipale.getDescLuogo());

				// <Data1>
				lReaMod2.setDataInizio(mReatoPrincipale.getDataInizio());
				lReaMod2.setAnnoInizio(mReatoPrincipale.getAnnoInizio());
				lReaMod2.setMeseInizio(mReatoPrincipale.getMeseInizio());
				lReaMod2.setGiornoInizio(mReatoPrincipale.getGiornoInizio());

				// <Data2>
				lReaMod2.setDataFine(mReatoPrincipale.getDataFine());
				lReaMod2.setAnnoFine(mReatoPrincipale.getAnnoFine());
				lReaMod2.setMeseFine(mReatoPrincipale.getMeseFine());
				lReaMod2.setGiornoFine(mReatoPrincipale.getGiornoFine());

				lReaMod2.setNote(mReatoPrincipale.getNote());

				// lReaMod2.setCodTipoPenaDetentiva("-"); // Per le join
				// lReaMod2.setCodTipoSanzione("-"); // Per le join

				lReaMod2.setCodTipoPenaDetentiva(mReatoPrincipale.getCodTipoPenaDetentiva());
				lReaMod2.setNumGiorni(mReatoPrincipale.getNumGiorni());
				lReaMod2.setNumMesi(mReatoPrincipale.getNumMesi());
				lReaMod2.setNumAnni(mReatoPrincipale.getNumAnni());
				lReaMod2.setNumAnniIsolamentoDiurno(mReatoPrincipale.getNumAnniIsolamentoDiurno());
				lReaMod2.setNumMesiIsolamentoDiurno(mReatoPrincipale.getNumMesiIsolamentoDiurno());
				lReaMod2.setNumGiorniIsolamentoDiurno(mReatoPrincipale.getNumGiorniIsolamentoDiurno());
				lReaMod2.setDataInizioIsolamentoDiurno(mReatoPrincipale.getDataInizioIsolamentoDiurno());
				lReaMod2.setDataFineIsolamentoDiurno(mReatoPrincipale.getDataFineIsolamentoDiurno());
				lReaMod2.setSanzionePecuniaria(mReatoPrincipale.getSanzionePecuniaria());
				lReaMod2.setCodTipoSanzione(mReatoPrincipale.getCodTipoSanzione());

				lReaMod2.setCodOperatoreInserimento(this.getCodUtenteConnesso());
				lReaMod2.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
				lReaMod2.setDataInserimento(DateUtils.getSysDate());

				if (aIdFascicoloSiep != null)
					lReaMod2.setFasSieIdFascicoloSiep(aIdFascicoloSiep);

				mReati.add(lReaMod2);
			}
		}

	}

}