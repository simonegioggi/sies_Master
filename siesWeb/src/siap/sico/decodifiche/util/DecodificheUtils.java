package siap.sico.decodifiche.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import siap.sico.decodifiche.controller.IComune;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.prescrizione.model.PrescrizioneModel;
import f3b.model.DecodeModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: DecodificheUtils
 * </p>
 * <p>
 * Description: Classe di utilita' per il DecodeModel
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @author Luigi
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DecodificheUtils {

	/**
	 * Funzione getDescbyCode().
	 * 
	 * @param aCol
	 *            : Collection di DecodeModel.
	 * @param aCode
	 *            : String codice di cui occorre trovare la descrizione.
	 * @return String Descrizione corrispondente al codice. Descrizione: la funzione cerca nella collection di
	 *         DecodelModel passato come primo parametro il codice secondo parametro. Se tale codice esiste
	 *         viene restituita la descrizione corrispondente, altrimenti la funzione restituisce una stringa
	 *         vuota (""). Questa funzione può essere utilizzata quando la decodifica va fatta tra un numero
	 *         limitato di elementi (<10).
	 *         <p>
	 *         Copyright: Copyright (c) 2003
	 *         </p>
	 *         <p>
	 *         Company:
	 *         </p>
	 * @author Luigi
	 * @version 1.0
	 */

	public static String getDescbyCode(Collection aCol, String aCode) {
		String risultato = new String("");

		if (aCol != null) {
			Iterator itx = aCol.iterator();

			DecodeModel ldecodeModel;

			while (itx.hasNext()) {
				ldecodeModel = (DecodeModel) itx.next();
				if ((ldecodeModel.getCode()).equals(aCode)) {
					risultato = ldecodeModel.getDescription();
					break;
				}
			}
		}
		return risultato;
	}

	/**
	 * Funzione getCodAltebyCode().
	 * 
	 * @param aCol
	 *            : Collection di DecodificheModel.
	 * @param aCode
	 *            : String codice di cui occorre trovare la descrizione.
	 * @return String Codice Alternativo corrispondente al codice. Descrizione: la funzione cerca nella
	 *         collection di DecodificheModel passato come primo parametro il codice secondo parametro. Se
	 *         tale codice esiste viene restituito il Codice Alternativo corrispondente, altrimenti la
	 *         funzione restituisce una stringa vuota ("").
	 *         <p>
	 *         Copyright: Copyright (c) 2003
	 *         </p>
	 *         <p>
	 *         Company:
	 *         </p>
	 * @author Luigi
	 * @version 1.0
	 *
	 */

	public static String getCodAltebyCode(Collection aCol, String aCode) {
		String risultato = new String("");

		if (aCol != null) {
			Iterator itx = aCol.iterator();

			DecodificheModel ldecodeModel;

			while (itx.hasNext()) {
				ldecodeModel = (DecodificheModel) itx.next();
				if ((ldecodeModel.getCode()).equals(aCode)) {
					risultato = ldecodeModel.getCodiceAlternativo();
					break;
				}
			}
		}
		return risultato;
	}

	/**
	 * Funzione getCodebyCodAlt().
	 * 
	 * @param aCol
	 *            : Collection di DecodificheModel.
	 * @param aCodAlt
	 *            : String codice Alternativo di cui occorre trovare il codice.
	 * @return String Codice corrispondente al codice Alternativo. Descrizione: la funzione cerca nella
	 *         collection di DecodificheModel passato come primo parametro il codice alternativo secondo
	 *         parametro. Se tale codice Alternativo esiste viene restituito il Codice corrispondente,
	 *         altrimenti la funzione restituisce una stringa vuota ("").
	 *         <p>
	 *         Copyright: Copyright (c) 2006
	 *         </p>
	 *         <p>
	 *         Company: Bull
	 *         </p>
	 * @author Vincenzo
	 * @version 1.0
	 *
	 */

	public static String getCodebyCodAlt(Collection aCol, String aCodeAlt) {
		Iterator itx = aCol.iterator();
		String risultato = new String("");
		DecodificheModel ldecodeModel;

		while (itx.hasNext()) {
			ldecodeModel = (DecodificheModel) itx.next();
			if ((ldecodeModel.getCodiceAlternativo() != null)
					&& (ldecodeModel.getCodiceAlternativo()).equals(aCodeAlt)) {
				risultato = ldecodeModel.getCode();
				break;
			}
		}
		return risultato;
	}

	public static String getCodebyCodAlt2(Collection aCol, String aCodeAlt2) {
		Iterator itx = aCol.iterator();
		String risultato = new String("");
		DecodificheModel ldecodeModel;

		while (itx.hasNext()) {
			ldecodeModel = (DecodificheModel) itx.next();
			if ((ldecodeModel.getCodiceAlt2() != null) && (ldecodeModel.getCodiceAlt2()).equals(aCodeAlt2)) {
				risultato = ldecodeModel.getCode();
				break;
			}
		}
		return risultato;
	}

	/**
	 * Funzione getFiltrobyCode().
	 * 
	 * @param aCol
	 *            : Collection di DecodificheModel.
	 * @param aCode
	 *            : String codice di cui occorre trovare la descrizione.
	 * @return String "Filtro" (RV_ABBREVIATION della CG_REF_CODES) corrispondente al codice. Descrizione: la
	 *         funzione cerca nella collection di DecodificheModel passato come primo parametro il codice
	 *         secondo parametro. Se tale codice esiste viene restituito il valore di "Filtro"
	 *         (RV_ABBREVIATION della CG_REF_CODES) corrispondente, altrimenti la funzione restituisce una
	 *         stringa vuota ("").
	 *         <p>
	 *         Copyright: Copyright (c) 2003
	 *         </p>
	 *         <p>
	 *         Company:
	 *         </p>
	 * @author Luigi
	 * @version 1.0
	 *
	 */

	public static String getFiltrobyCode(Collection aCol, String aCode) {
		Iterator itx = aCol.iterator();

		String risultato = "";
		DecodificheModel ldecodeModel;

		while (itx.hasNext()) {
			ldecodeModel = (DecodificheModel) itx.next();
			if ((ldecodeModel.getCode()).equals(aCode)) {
				risultato = ldecodeModel.getFiltro();
				break;
			}
		}
		return risultato;
	}

	/**
	 * Funzione getCodebyDesc().
	 * 
	 * @param aCol
	 *            : Collection di DecodeModel.
	 * @param aDesc
	 *            : String descrizione di cui occorre trovare il codice.
	 * @return String Codice corrispondente alla descrizione. Descrizione: la funzione cerca nella collection
	 *         di DecodelModel passato come primo parametro la descrizione (secondo parametro). Se tale
	 *         descrizione esiste viene restituita il codice corrispondente, altrimenti la funzione
	 *         restituisce la stringa "-" corrispondente a nessuna codifica.
	 *         <p>
	 *         Copyright: Copyright (c) 2003
	 *         </p>
	 *         <p>
	 *         Company: Bull
	 *         </p>
	 * @version 1.0
	 *
	 */

	public static String getCodebyDesc(Collection aCol, String aDesc) {
		Iterator itx = aCol.iterator();
		String risultato = new String("-");
		DecodeModel ldecodeModel;

		while (itx.hasNext()) {
			ldecodeModel = (DecodeModel) itx.next();
			if ((ldecodeModel.getDescription()).equals(aDesc)) {
				risultato = ldecodeModel.getCode();
				break;
			}
		}
		return risultato;
	}

	// 20210627	MEV_21 Confronto tra descrizioni in upperCase.
	public static String getCodebyDescUpCase(Collection aCol, String aDesc) {
		Iterator itx = aCol.iterator();
		String risultato = new String("-");
		DecodeModel ldecodeModel;

		while (itx.hasNext()) {
			ldecodeModel = (DecodeModel) itx.next();
			if ((ldecodeModel.getDescription()).toUpperCase().equals(aDesc.toUpperCase())) {
				risultato = ldecodeModel.getCode();
				break;
			}
		}
		return risultato;
	}
	
	/**
	 * Funzione getDecodesWithoutCode().
	 * 
	 * @param aCol
	 *            : Collection di DecodeModel.
	 * @param aCode
	 *            : String codice.
	 * @return Collection. Descrizione: la funzione restituisce una Collection di Decodemodel ottenuta
	 *         eliminando dalla Collection di input gli elementi il cui codice è uguale a aCode.
	 *         <p>
	 *         Copyright: Copyright (c) 2003
	 *         </p>
	 *         <p>
	 *         Company: Bull
	 *         </p>
	 * @version 1.0
	 *
	 */
	public static Collection getDecodesWithoutCode(Collection aCol, String aCode) {
		Collection retColl = new ArrayList();
		if (aCol != null) {
			Iterator itx = aCol.iterator();
			DecodeModel ldecodeModel;

			while (itx.hasNext()) {
				ldecodeModel = (DecodeModel) itx.next();
				if (!(ldecodeModel.getCode()).equals(aCode)) {
					retColl.add(ldecodeModel);
				}
			}
		}
		return retColl;
	}

	/**
	 * Funzione getDecodesWithoutCodes().
	 * 
	 * @param aCol
	 *            : Collection di DecodeModel.
	 * @param aCodes
	 *            : String[] codice.
	 * @return Collection. Descrizione: la funzione restituisce una Collection di Decodemodel ottenuta
	 *         eliminando dalla Collection di input gli elementi il cui codice è contenuto in aCodes.
	 *         <p>
	 *         Copyright: Copyright (c) 2006
	 *         </p>
	 *         <p>
	 *         Company: Bull
	 *         </p>
	 * @version 1.0
	 *
	 */
	public static Collection getDecodesWithoutCodes(Collection aCol, String[] aCodes) {
		Collection retColl = new ArrayList();
		if (aCol != null) {
			if (aCodes != null) {
				Arrays.sort(aCodes);

				Iterator itx = aCol.iterator();
				DecodeModel ldecodeModel;

				while (itx.hasNext()) {
					ldecodeModel = (DecodeModel) itx.next();
					if (Arrays.binarySearch(aCodes, ldecodeModel.getCode()) < 0)

					{
						retColl.add(ldecodeModel);
					}
				}
			} else {
				retColl = aCol;
			}
		}

		return retColl;
	}

	/**
	 * Funzione getDecodesWithCodes().
	 * 
	 * @param aCol
	 *            : Collection di DecodeModel.
	 * @param aCodes
	 *            : String[] codice.
	 * @return Collection. Descrizione: la funzione restituisce una Collection di Decodemodel ottenuta
	 *         eliminando dalla Collection di input gli elementi il cui codice non è contenuto in aCodes.
	 *         <p>
	 *         Copyright: Copyright (c) 2006
	 *         </p>
	 *         <p>
	 *         Company: Bull
	 *         </p>
	 * @version 1.0
	 *
	 */
	public static Collection getDecodesWithCodes(Collection aCol, String[] aCodes) {
		Collection retColl = new ArrayList();
		if (aCol != null) {
			if (aCodes != null) {
				Arrays.sort(aCodes);

				Iterator itx = aCol.iterator();
				DecodeModel ldecodeModel;

				while (itx.hasNext()) {
					ldecodeModel = (DecodeModel) itx.next();
					if (Arrays.binarySearch(aCodes, ldecodeModel.getCode()) >= 0)

					{
						retColl.add(ldecodeModel);
					}
				}
			}
		}

		return retColl;
	}

	/**
	 * Funzione getDecodificheFiltrateByCodAlt().
	 * 
	 * @param aCol
	 *            : Collection di DecodificheModel in input.
	 * @param aCode
	 *            : String codice.
	 * @return Collection di DecodificheModel filtrata. Descrizione: la funzione restituisce una Collection di
	 *         DecodificheModel ottenuta filtrando la Collection in input in base ai valori assunti dal Codice
	 *         Alternativo (HIGH VALUE) dei suoi elementi. Vengono scelti gli elementi il cui Codice
	 *         Alternativo è uguale alla stringa aCode, oppure è nullo, oppure è "-".
	 *         <p>
	 *         Copyright: Copyright (c) 2003
	 *         </p>
	 *         <p>
	 *         Company: Bull
	 *         </p>
	 * @version 1.0
	 *
	 */
	public static Collection getDecodificheFiltrateByCodAlt(Collection aCol, String aCode) {
		Collection retColl = null;
		if (aCol != null) {
			retColl = new ArrayList();

			Iterator itx = aCol.iterator();
			DecodificheModel ldecodeModel;

			while (itx.hasNext()) {
				ldecodeModel = (DecodificheModel) itx.next();
				if (ldecodeModel.getCodiceAlternativo() == null
						|| ldecodeModel.getCodiceAlternativo().trim().length() < 1
						|| ldecodeModel.getCodiceAlternativo().equalsIgnoreCase("-")
						|| ldecodeModel.getCodiceAlternativo().equalsIgnoreCase(aCode)) {
					retColl.add(ldecodeModel);
				}
			}
		}
		return retColl;
	}

	/**
	 * Funzione getDecodificheFiltrateByCodAltValorizzato().
	 * 
	 * @param aCol
	 *            : Collection di DecodificheModel in input.
	 * @param aCode
	 *            : String codice.
	 * @return Collection di DecodificheModel filtrata. Descrizione: la funzione restituisce una Collection di
	 *         DecodificheModel ottenuta filtrando la Collection in input in base ai valori assunti dal Codice
	 *         Alternativo (HIGH VALUE) dei suoi elementi. A DIFFERENZA DEL METEDO
	 *         "getDecodificheFiltrateByCodAlt()" Vengono scelti solo gli elementi il cui Codice Alternativo è
	 *         uguale alla stringa aCode
	 *         <p>
	 *         Copyright: Copyright (c) 2003
	 *         </p>
	 *         <p>
	 *         Company: Bull
	 *         </p>
	 * @version 1.0
	 *
	 */
	public static Collection getDecodificheFiltrateByCodAltValorizzato(Collection aCol, String aCode) {

		Collection retColl = null;
		if (aCol != null) {
			retColl = new ArrayList();

			Iterator itx = aCol.iterator();
			DecodificheModel ldecodeModel;

			while (itx.hasNext()) {
				ldecodeModel = (DecodificheModel) itx.next();

				if (ldecodeModel.getCodiceAlternativo() != null
						&& ldecodeModel.getCodiceAlternativo().equalsIgnoreCase(aCode)) {
					retColl.add(ldecodeModel);
				}
			}
		}
		return retColl;
	}

	/**
	 * Funzione parsePrescrizioni().
	 * 
	 * @param decModCol
	 *            : Collection di DecodificheModel in input.
	 * @param aVect
	 *            : Vector in input.
	 * @return List di stringhe filtrata. Descrizione: la funzione restituisce una List di stringhe ottenuta
	 *         Decodificando la Collection in input o il Vector
	 * @version 1.0
	 */
	public static List parsePrescrizioni(Collection decModCol, Vector aVect) {

		// List di strinche di ritorno
		List retColl = null;

		// Controllo che almeno uno dei due parametri in ingresso sia valorizzato
		if (decModCol != null || aVect != null) {

			retColl = new ArrayList();

			Iterator itx = null;

			if (decModCol != null) {

				itx = decModCol.iterator();
			} else if (aVect != null) {

				itx = aVect.iterator();
			}

			DecodificheModel lDecodificheMod = null;

			PrescrizioneModel lPrescrizione = null;

			String DescrPrescrizione = "";

			String CodiceCampo = "";

			while (itx.hasNext()) {

				if (decModCol != null) {
					lDecodificheMod = new DecodificheModel((DecodificheModel) itx.next());

					// trova la descrizione
					DescrPrescrizione = lDecodificheMod.getDescription();

					CodiceCampo = lDecodificheMod.getCode();
				} else if (aVect != null) {
					lPrescrizione = (PrescrizioneModel) itx.next();

					// trova la descrizione
					DescrPrescrizione = lPrescrizione.getDescrTipoPrescrizione();
				}

				String stringaFinale = "";

				// Controlla la presenza dei caratteri speciali
				int PrimoIndice = DescrPrescrizione.indexOf("<?");

				if (PrimoIndice == -1) {
					// Controlla che sia un campo a testo libero
					if (DescrPrescrizione.equals("-")) {
						// campo testo libero
						if (decModCol != null) {
							stringaFinale += "<tr>";
							stringaFinale += " <td class=\"l\"><input value=\"01\" type=\"checkbox\" name=\"CAMPO_CK_"
									+ CodiceCampo + "\"></td>";
							stringaFinale += "<td class=\"l\"><input value=\"\" type=\"text\" name=\"CAMPO_TESTO_"
									+ CodiceCampo + "\" size=65 ></td>";
							stringaFinale += "</tr>";
						} else if (aVect != null) {
							stringaFinale += lPrescrizione.getDescrPrescrizione1();
						}
					} else {
						// solo descrizione
						if (decModCol != null) {
							stringaFinale += "<tr>";
							stringaFinale += " <td class=\"l\"><input value=\"01\" type=\"checkbox\" name=\"CAMPO_CK_"
									+ CodiceCampo + "\"></td>";
							stringaFinale += "<td class=\"l\">" + DescrPrescrizione + "</td>";
							stringaFinale += "</tr>";
						} else if (aVect != null) {
							stringaFinale += DescrPrescrizione;
						}
					}
				} else {
					// ESEGUE IL PARSER DELLA STRINGA PER TROVARE I CAMPI DA INSERIRE

					// String CodiceCampo = lPrescrizioneMod.getCode();

					// trasforma la stringa in una matrice di caratteri
					char[] caratteri = DescrPrescrizione.toCharArray();

					int indiceCampo = 0;

					String sizeCampo = "00";

					String testoeCampi = "";

					for (int i = 0; i < caratteri.length; i++) {
						// esegue i controlli in caso ci siano caratteri speciali per inserire i campi
						if (caratteri[i] == '<') {
							// Per campi testo "<?XXXX>"
							if (caratteri[i + 1] == '?' && caratteri[i + 6] == '>') {
								if (decModCol != null) {
									// Trova il size del campo
									sizeCampo = "" + caratteri[i + 4] + caratteri[i + 5];
									// Inserisce nella stringa il campo
									testoeCampi += "<input value=\"\" type=\"text\" name=\"CAMPO_TESTO_"
											+ CodiceCampo + "\" size=\"" + sizeCampo + "\">";
								} else if (aVect != null) {
									if (indiceCampo == 0) {
										testoeCampi += lPrescrizione.getDescrPrescrizione1();
									} else if (indiceCampo == 1) {
										testoeCampi += lPrescrizione.getDescrPrescrizione2();
									} else if (indiceCampo == 2) {
										testoeCampi += lPrescrizione.getDescrPrescrizione3();
									}
								}
								// incrementa l'indice dell'array dei caratteri quanti sono i caratteri
								// speciali
								i = i + 6;
								// Imposta l'indice al campo in caso ci fossero più campi da inserire nella
								// stringa
								// il valore serve ai javascript e alle action di inserimento per trovare i
								// campi
								indiceCampo = indiceCampo + 1;
							}
							// Campo per elenco Comuni "<?PUCMXXXX>"
							else if (caratteri[i + 1] == '?' && caratteri[i + 2] == 'P'
									&& caratteri[i + 3] == 'U' && caratteri[i + 4] == 'C'
									&& caratteri[i + 5] == 'M' && caratteri[i + 10] == '>') {

								if (decModCol != null) {
									// Trova il size del campo
									sizeCampo = "" + caratteri[i + 8] + caratteri[i + 9];
									// Inserisce nella stringa il campo
									testoeCampi += "<input value=\"\" type=\"text\" name=\"CAMPO_TESTO_"
											+ CodiceCampo
											+ "\" size=\""
											+ sizeCampo
											+ "\"><a href=\"Javascript:ListaComuni('LoadInserisciPrescrizione','CAMPO_TESTO_"
											+ CodiceCampo + "[" + indiceCampo
											+ "]');\"><img src=\"/images/filefolder.gif\" border=\"0\"></a>";
								} else if (aVect != null) {
									if (indiceCampo == 0) {
										testoeCampi += lPrescrizione.getDescrPrescrizione1();
									} else if (indiceCampo == 1) {
										testoeCampi += lPrescrizione.getDescrPrescrizione2();
									} else if (indiceCampo == 2) {
										testoeCampi += lPrescrizione.getDescrPrescrizione3();
									}
								}
								// incrementa l'indice dell'array dei caratteri quanti sono i caratteri
								// speciali
								i = i + 10;
								// Imposta l'indice al campo in caso ci fossero più campi da inserire nella
								// stringa
								// il valore serve ai javascript e alle action di inserimento per trovare i
								// campi
								indiceCampo = indiceCampo + 1;
							}

							// Campo per elenco Uffici di Sorveglianza "<?PUUDSXXX>"
							else if (caratteri[i + 1] == '?' && caratteri[i + 2] == 'P'
									&& caratteri[i + 3] == 'U' && caratteri[i + 4] == 'U'
									&& caratteri[i + 5] == 'D' && caratteri[i + 6] == 'S'
									&& caratteri[i + 10] == '>') {
								if (decModCol != null) {
									// Trova il size del campo
									sizeCampo = "" + caratteri[i + 8] + caratteri[i + 9];
									// Inserisce nella stringa il campo
									testoeCampi += "<input value=\"\" type=\"text\" name=\"CAMPO_TESTO_"
											+ CodiceCampo
											+ "\" size=\""
											+ sizeCampo
											+ "\"><a href=\"Javascript:ListaUDS('LoadInserisciPrescrizione','CAMPO_TESTO_"
											+ CodiceCampo + "[" + indiceCampo
											+ "]');\"><img src=\"/images/filefolder.gif\" border=\"0\"></a>";
								} else if (aVect != null) {
									if (indiceCampo == 0) {
										testoeCampi += lPrescrizione.getDescrPrescrizione1();
									} else if (indiceCampo == 1) {
										testoeCampi += lPrescrizione.getDescrPrescrizione2();
									} else if (indiceCampo == 2) {
										testoeCampi += lPrescrizione.getDescrPrescrizione3();
									}
								}
								// incrementa l'indice dell'array dei caratteri quanti sono i caratteri
								// speciali
								i = i + 10;
								// Imposta l'indice al campo in caso ci fossero più campi da inserire nella
								// stringa
								// il valore serve ai javascript e alle action di inserimento per trovare i
								// campi
								indiceCampo = indiceCampo + 1;
							}
						} else {
							testoeCampi += caratteri[i];
						}
					}

					// Scrive la riga con il testo e i campi
					if (decModCol != null) {
						stringaFinale += "<tr>";
						stringaFinale += " <td class=\"l\"><input value=\"01\" type=\"checkbox\" name=\"CAMPO_CK_"
								+ lDecodificheMod.getCode() + "\"></td>";
						stringaFinale += "<td class=\"l\">" + testoeCampi + "</td>";
						stringaFinale += "</tr>";
					} else if (aVect != null) {
						stringaFinale += testoeCampi;
					}

				}
				if (decModCol != null) {
					lDecodificheMod.setDescription(stringaFinale);
					retColl.add(lDecodificheMod);
					// retColl.add(stringaFinale);
				} else if (aVect != null) {
					lPrescrizione.setDescrTipoPrescrizione(stringaFinale);
					retColl.add(lPrescrizione);
				}

			}
		}

		return retColl;
	}

	/**
	 * Ritorna i dati del Comune dal codice.
	 * <p>
	 * 
	 * @param aCodComune
	 *            codice comune
	 * @return istanza del ComuneModel.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static ComuneModel getComuneByCod(String aCodComune) throws F3BException {
		ComuneModel lComune = null;
		if (aCodComune == null || aCodComune.trim().length() == 0)
			throw (new F3BException("Codice Comune assente !"));
		else {
			IComune lComCtrl = SICOLookupRemote.getComuneRemote();
			lComune = lComCtrl.ExRicercaComuneByKey(aCodComune);
		}
		return lComune;
	}

	public static Collection<DecodificheModel> getFilteredByCodAlt(Collection<DecodificheModel> aCol,
			String aRegExCode) {
		Collection<DecodificheModel> lRetColl = new ArrayList<DecodificheModel>();
		if (aCol != null) {
			for (DecodificheModel lDecode : aCol) {
				if (lDecode.getCodiceAlternativo() != null
						&& lDecode.getCodiceAlternativo().matches(aRegExCode)) {
					lRetColl.add(lDecode);
				}
			}
		}
		return lRetColl;
	}

	/**
	 * Funzione getCodAlt2byCode().
	 * 
	 * @param aCol
	 *            : Collection di DecodificheModel.
	 * @param aCode
	 *            : String codice di cui occorre trovare la descrizione.
	 * @return String Codice Alt2 corrispondente al secondo codice alternativo. Descrizione: la funzione cerca
	 *         nella collection di DecodificheModel passato come primo parametro il codice secondo parametro.
	 *         Se tale codice esiste viene restituito il Codice Alternativo corrispondente, altrimenti la
	 *         funzione restituisce una stringa vuota ("").
	 *         <p>
	 *         Copyright: Copyright (c) 2015
	 *         </p>
	 *         <p>
	 *         Company:
	 *         </p>
	 * @author Intersistemi S.p.A.
	 * @version 1.0
	 *
	 */

	public static String getCodAlt2byCode(Collection aCol, String aCode) {
		String risultato = new String("");

		if (aCol != null) {
			Iterator itx = aCol.iterator();

			DecodificheModel ldecodeModel;

			while (itx.hasNext()) {
				ldecodeModel = (DecodificheModel) itx.next();
				if ((ldecodeModel.getCode()).equals(aCode)) {
					risultato = ldecodeModel.getCodiceAlt2();
					break;
				}
			}
		}
		return risultato;
	}

	/**
	 * Verifica se nella collection di <DecodeModel> assata in imput esiste un elemento con codice aCode
	 * 
	 * @param aCol
	 * @param aCode
	 * @return
	 */
	public static boolean containsCode(Collection aCol, String aCode) {

		boolean contains = false;

		if (aCol != null) {
			Iterator itx = aCol.iterator();

			DecodificheModel ldecodeModel;

			while (itx.hasNext()) {
				ldecodeModel = (DecodificheModel) itx.next();
				if ((ldecodeModel.getCode()).equals(aCode)) {
					contains = true;
					break;
				}
			}
		}

		return contains;

	}

}