package siap.siep.misuracautelare.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.util.CalendarUtil;
import siap.siep.util.CaricaHTML_Servlet;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TotaleMisureModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3487564714749216421L;

	long mTotaleAnni;
	long mTotaleMesi;
	long mTotaleGiorni;

	// COSTRUTTORE DI DEFAULT
	public TotaleMisureModel() {
		mTotaleAnni = 0;
		mTotaleMesi = 0;
		mTotaleGiorni = 0;
	}

	// COSTRUTTORE DI COPIA
	public TotaleMisureModel(TotaleMisureModel aModel) {
		mTotaleAnni = aModel.mTotaleAnni;
		mTotaleMesi = aModel.mTotaleMesi;
		mTotaleGiorni = aModel.mTotaleGiorni;
	}

	// COSTRUTTORE MODEL
	public TotaleMisureModel(long aNumAnni, long aNumMesi, long aNumGiorni) {
		mTotaleAnni = aNumAnni;
		mTotaleMesi = aNumMesi;
		mTotaleGiorni = aNumGiorni;
	}

	//
	// METODI GET()
	//

	public long getTotaleAnni() {
		return mTotaleAnni;
	}

	public long getTotaleMesi() {
		return mTotaleMesi;
	}

	public long getTotaleGiorni() {
		return mTotaleGiorni;
	}

	//
	// METODI SET()
	//

	public void setTotaleAnni(long aValore) {
		mTotaleAnni = aValore;
	}

	public void setTotaleMesi(long aValore) {
		mTotaleMesi = aValore;
	}

	public void setTotaleGiorni(long aValore) {
		mTotaleGiorni = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mTotaleAnni + " - " + mTotaleMesi + " - " + mTotaleGiorni;

		return lStr;
	}

	public void calcolaTotaleMisure(Vector aMisure) throws Exception {
		// MisuraCautelareModel lMis = new MisuraCautelareModel();
		// Iterator lItx = aMisure.iterator();
		//
		// long lTotale = 0;
		//
		// while (lItx.hasNext())
		// {
		// lMis = ((MisuraCautelareModel) lItx.next());
		//
		// // MEV 10 S3
		// // Nel calcolo vengono conteggite solo le Misure Cautelari Computabili
		// if(lMis.getFlagComputabile() != null && lMis.getFlagComputabile().equals("S") ){
		// if (lMis.getNumGiorni()!=null)
		// lTotale += lMis.getNumGiorni().intValue();
		// if (lMis.getNumMesi()!=null)
		// lTotale += (lMis.getNumMesi().intValue() * 30);
		// if (lMis.getNumAnni()!=null)
		// lTotale += (lMis.getNumAnni().intValue() * 360);
		// }
		//
		// mTotaleMesi = lTotale/30;
		// mTotaleAnni = mTotaleMesi/12;
		//
		// mTotaleGiorni = lTotale - (mTotaleMesi *30);
		// mTotaleMesi = mTotaleMesi - (mTotaleAnni * 12);
		// }

		// inizio calcolo totale custodia cautelare Anni Mesi Giorni
		// dove consideriamo periodi sovrapposti solo per Misure Cautelari
		// "Cessata al momento del passaggio in giudicato computabili"
		Date DItempdata;
		Date DFtempdata;
		String DItemp = "";
		String DFtemp = "";
		int sommaColonnaAnni = 0;
		int sommaColonnaMesi = 0;
		int sommaColonnaGiorni = 0;
		Vector vectorPeriodi = new Vector();
		int sommaColonnaAnniModManuale = 0;
		int sommaColonnaMesiModManuale = 0;
		int sommaColonnaGiorniModManuale = 0;

		CalendarModel ctot = new CalendarModel();
		CalendarUtil cu = new CalendarUtil();
		CalendarModel cm = new CalendarModel();
		Iterator itx = aMisure.iterator();
		while (itx.hasNext()) {
			MisuraCautelareModel lMis = (MisuraCautelareModel) itx.next();
			if (lMis.getGiorni() == null && lMis.getFlagModificaManuale() == null
					&& lMis.getFlagComputabile().equals("S")) {
				if (lMis.getCodOperatoreInserimento() == null || (lMis.getCodOperatoreInserimento() != null
						&& lMis.getCodOperatoreInserimento().length() > 2
						&& !lMis.getCodOperatoreInserimento().substring(0, 3).equalsIgnoreCase("RES"))) {
					vectorPeriodi.add(lMis.getDataInizio());
					vectorPeriodi.add(lMis.getDataFine());
				}
			}

			// inizio calcoloGioniMesiAnni A.S. settembre2015
			if (lMis.getDataFine() != null && lMis.getDataInizio() != null) {
				Date datainizio = lMis.getDataInizio();
				String ggInizio = DateUtils.getDayToString(datainizio);
				String mmInizio = DateUtils.getMonthToString(datainizio);
				String aaInizio = DateUtils.getYearToString(datainizio);

				Date datafine = lMis.getDataFine();
				String ggFine = DateUtils.getDayToString(datafine);
				String mmFine = DateUtils.getMonthToString(datafine);
				String aaFine = DateUtils.getYearToString(datafine);

				String calcoloGioniMesiAnni = "";
				try {
					calcoloGioniMesiAnni = CaricaHTML_Servlet.getQuantumIntervallo(ggInizio, mmInizio,
							aaInizio, ggFine, mmFine, aaFine);
				} catch (Exception e) {
					e.printStackTrace();
				}
				String sep = "~#";
				String[] aPairs = new String[3];
				aPairs = calcoloGioniMesiAnni.split(sep);

				cm.setNumAnni(Integer.parseInt(aPairs[0]));
				cm.setNumMesi(Integer.parseInt(aPairs[1]));
				cm.setNumGiorni(Integer.parseInt(aPairs[2]));

				// int numGiorniTotale = 0;
				// inizio calcolo giorni/mesi/anni per misura cautelare computo periodo messa alla prova
				if (lMis.getCodTipoMisura().equalsIgnoreCase("CL") && lMis.getFlagModificaManuale() == null) {
					int giorni = cm.getNumGiorni();
					int mesi = cm.getNumMesi();
					int anni = cm.getNumAnni();

					int numGiorni = anni * 360 + mesi * 30 + giorni;
					// numGiorniTotale = numGiorni;
					// numGiorni = numGiorni/3;

					BigDecimal numGiorniAnniMesiGiorni = new BigDecimal(numGiorni);
					BigDecimal periodiGiorniUnoTre = new BigDecimal(3);
					BigDecimal numGiorniAnniMesiGiorniDivTre = new BigDecimal(0);
					BigDecimal numDiff = new BigDecimal(0);
					BigDecimal uno = new BigDecimal(1);
					numGiorniAnniMesiGiorniDivTre = numGiorniAnniMesiGiorni.divide(periodiGiorniUnoTre, 1,
							RoundingMode.HALF_UP);
					// int num = tot.intValue();
					BigDecimal numGiorniAnniMesiGiorniDivTreInteroNegato = new BigDecimal(
							numGiorniAnniMesiGiorniDivTre.intValue()).negate();
					// numDiff = numGiorniAnniMesiGiorniDivTre;
					numDiff = numGiorniAnniMesiGiorniDivTre.add(numGiorniAnniMesiGiorniDivTreInteroNegato);

					// Se il decimale è >= 6 allora arrotondamento per eccesso.
					// Se il decimale è <= 5 allora arrotondamento per difetto.
					BigDecimal zero5 = new BigDecimal("0.5");
					if (numDiff.compareTo(zero5) == 0 || numDiff.compareTo(zero5) == -1) {
						// Se il decimale è <= 5 allora arrotondamento per difetto.
						numGiorniAnniMesiGiorniDivTre = new BigDecimal(
								numGiorniAnniMesiGiorniDivTre.intValue());
						// numGiorniAnniMesiGiorniDivTre = numGiorniAnniMesiGiorniDivTre.add(uno);
					} else {
						numGiorniAnniMesiGiorniDivTre = numGiorniAnniMesiGiorniDivTre.add(uno);
					}

					numGiorni = numGiorniAnniMesiGiorniDivTre.intValue();
					mesi = 0;
					anni = 0;
					// I conteggi sono effettuati usando gli algoritmi di ricalcolaGAM
					if (numGiorni > 30) {
						int tmp = numGiorni / 30;
						mesi += tmp;
						numGiorni -= tmp * 30;
					}
					if (numGiorni == 30) {
						mesi++;
						numGiorni = 0;
					}
					if (mesi > 12) {
						int tmp = mesi / 12;
						anni += tmp;
						mesi -= tmp * 12;
					}

					if (mesi == 12) {
						anni++;
						mesi = 0;
					}

					/*
					 * if (mesi<12) { anni=0; }
					 */
					cm.setNumAnni(anni);
					cm.setNumMesi(mesi);
					cm.setNumGiorni(numGiorni);
				} else {
					cm.setNumAnni(lMis.getNumAnni());
					cm.setNumMesi(lMis.getNumMesi());
					cm.setNumGiorni(lMis.getNumGiorni());
					// if (lMis.getGiorni() != null)
					// numGiorniTotale = Integer.parseInt(lMis.getGiorni().toString());
					// else
					// numGiorniTotale = Integer.parseInt("0");
				}
				// fine calcolo giorni/mesi/anni per misura cautelare computo periodo messa alla prova

				if (lMis.getFlagComputabile().equals("S")) {
					// somma giorni/mesi/anni solo per le Misure cautelari migrate dal sistema RES
					if (lMis.getCodOperatoreInserimento() != null
							&& lMis.getCodOperatoreInserimento().length() > 2
							&& lMis.getCodOperatoreInserimento().substring(0, 3).equalsIgnoreCase("RES")) {
						ctot = cu.sommaGiorni(ctot, cm);
					}
				}
			}
			ctot = cu.ricalcolaGAM(ctot);
			// fine calcoloGioniMesiAnni A.S. settembre2015

			if (lMis.getGiorni() != null && lMis.getFlagComputabile().equals("S")) {
				sommaColonnaAnniModManuale = sommaColonnaAnniModManuale
						+ Integer.parseInt(lMis.getNumAnni().toString());
				sommaColonnaMesiModManuale = sommaColonnaMesiModManuale
						+ Integer.parseInt(lMis.getNumMesi().toString());
				sommaColonnaGiorniModManuale = sommaColonnaGiorniModManuale
						+ Integer.parseInt(lMis.getNumGiorni().toString());
			}

		}

		if (vectorPeriodi != null && vectorPeriodi.size() != 0) {
			for (int i = 0; i < vectorPeriodi.size() - 1; i = i + 2) {
				// trova l'elemento minimo
				int jMin = i;
				for (int j = i + 2; j < vectorPeriodi.size(); j = j + 1) {
					if (DateUtils.isLower((Date) vectorPeriodi.get(j), (Date) vectorPeriodi.get(jMin)))// vectorPeriodi[j]<vectorPeriodi[jMin]
						jMin = j;
				}
				// scambia gli elementi con indice i e jMin
				if (i != jMin) {
					// scambia
					DItemp = (String) vectorPeriodi.get(jMin);
					DFtemp = (String) vectorPeriodi.get(jMin + 1);
					vectorPeriodi.set(jMin, vectorPeriodi.get(i));
					vectorPeriodi.set(jMin + 1, vectorPeriodi.get(i + 1));
					vectorPeriodi.set(i, DItemp);
					vectorPeriodi.set(i + 1, DFtemp);
				}
			}
		}

		// popolo una tabella di sei colonne e n righe quanti sono i periodi
		// nomi colonne: Data-inizio | Data-finale | Anni | Mesi | Giorni | Periodi-continuativi | numero
		// periodo
		// nella cella Periodi-continuativi che hanno uguale numero sono continuativi
		// ==> si deve fare la somma dei giorni mesi anni
		int nRighe = vectorPeriodi.size() / 2;
		int nColonne = 7;
		String[][] matricePeriodi = new String[nRighe][nColonne];
		// popolo Data-inizio | Data-finale |
		int r = 0;
		int c = 0;
		for (int i = 0; i < vectorPeriodi.size() - 1; i = i + 2) {
			matricePeriodi[r][c] = vectorPeriodi.get(i).toString();
			r++;
		}
		r = 0;
		c = 1;
		for (int i = 1; i <= vectorPeriodi.size() - 1; i = i + 2) {
			matricePeriodi[r][c] = vectorPeriodi.get(i).toString();
			r++;
		}

		// popolo le colonne | Anni | Mesi | Giorni | Periodi-continuativi | numero periodo
		r = 0;
		int numPeriodiContinuativi = 0;
		for (int i = 0; i < vectorPeriodi.size() - 1; i = i + 2) {
			DItempdata = (Date) vectorPeriodi.get(i);
			String ggInizio = DateUtils.getDayToString(DItempdata).toString();
			String mmInizio = DateUtils.getMonthToString(DItempdata).toString();
			String aaInizio = DateUtils.getYearToString(DItempdata).toString();

			DFtempdata = (Date) vectorPeriodi.get(i + 1);
			String ggFine = DateUtils.getDayToString(DFtempdata).toString();
			String mmFine = DateUtils.getMonthToString(DFtempdata).toString();
			String aaFine = DateUtils.getYearToString(DFtempdata).toString();

			String calcoloGioniMesiAnni = CaricaHTML_Servlet.getQuantumIntervallo(ggInizio, mmInizio,
					aaInizio, ggFine, mmFine, aaFine);
			String sep = "~#";
			String[] aPairs = new String[3];
			aPairs = calcoloGioniMesiAnni.split(sep);
			String numAnni = aPairs[0];
			String numMesi = aPairs[1];
			String numGiorni = aPairs[2];

			boolean periodiContinuativi = false;

			// controllo i periodi se sono continuativi(sovrapposti)
			if (i == 0) {
				periodiContinuativi = true;// non cambia il numero del periodo
			} else {
				Date DIprimoPeriodo = (Date) vectorPeriodi.get(i - 1);
				Date DFsecondoPeriodo = (Date) vectorPeriodi.get(i);
				// System.out.print("isEquals ");
				// System.out.print("isLower ");
				// System.out.print("getIntervallo ");

				if (DateUtils.isEquals(DIprimoPeriodo, DFsecondoPeriodo)
						|| (DateUtils.isLower(DIprimoPeriodo, DFsecondoPeriodo)
								&& DateUtils.getIntervallo(DIprimoPeriodo, DFsecondoPeriodo) == 1)) {
					periodiContinuativi = true;// non cambia il numero del periodo
				} else {
					periodiContinuativi = false;// cambia il numero del periodo
				}
			}

			int cAnni = 2;
			int cMesi = 3;
			int cGiorni = 4;
			int cPeriodiContinuativi = 5;
			int cNumeroPeriodo = 6;
			matricePeriodi[r][cAnni] = numAnni;
			matricePeriodi[r][cMesi] = numMesi;
			matricePeriodi[r][cGiorni] = numGiorni;
			if (periodiContinuativi) {
				// Periodi sono Continuativi
				matricePeriodi[r][cPeriodiContinuativi] = Integer.toString(numPeriodiContinuativi);
			} else {
				numPeriodiContinuativi = numPeriodiContinuativi + 1;
				matricePeriodi[r][cPeriodiContinuativi] = Integer.toString(numPeriodiContinuativi);
				periodiContinuativi = false;
			}
			matricePeriodi[r][cNumeroPeriodo] = Integer.toString(i);// uguale al numero del periodo: primo
																	// periodo "0" secondo "2" terzo "4"
			r++;
		}

		// somma anni mesi giorni per periodi consecutivi
		String[][] matricePeriodiConsecutivi = new String[nRighe][nColonne];
		boolean matricePeriodiConsecutiviEsiste = false;// non esiste
		// Vector vectorPeriodiTemp = new Vector();
		boolean periodiContinuativi = false;
		int nRigPC = 0;
		// int nColPC = 0;

		String ggInizio = "";
		String mmInizio = "";
		String aaInizio = "";
		String ggFine = "";
		String mmFine = "";
		String aaFine = "";
		// int numeroPeriodo = 0;
		Date data = new Date();
		DItempdata = new Date();
		DFtempdata = new Date();
		for (int i = 0; i < nRighe; i++) {
			if (i == 0) {
				periodiContinuativi = false;
			} else {
				if (matricePeriodi[i - 1][5].equalsIgnoreCase(matricePeriodi[i][5])) {
					periodiContinuativi = true;
				} else {
					periodiContinuativi = false;
					nRigPC = nRigPC + 1;
				}
			}

			if (periodiContinuativi) {
				// SI periodi Consecutivi
				ggInizio = DateUtils.getDayToString(DItempdata);
				mmInizio = DateUtils.getMonthToString(DItempdata);
				aaInizio = DateUtils.getYearToString(DItempdata);
				for (int k = 0; k < vectorPeriodi.size(); k = k + 1) {
					data = (Date) vectorPeriodi.get(k);
					String ggVectorPeriodi = DateUtils.getDayToString(data);
					String mmVectorPeriodi = DateUtils.getMonthToString(data);
					String aaVectorPeriodi = DateUtils.getYearToString(data);
					String ggMatricePeriodi = matricePeriodi[i][1].substring(8, 10);
					String mmMatricePeriodi = matricePeriodi[i][1].substring(5, 7);
					String aaMatricePeriodi = matricePeriodi[i][1].substring(0, 4);
					if ((ggVectorPeriodi.equalsIgnoreCase(ggMatricePeriodi)
							|| ("0" + ggVectorPeriodi).equalsIgnoreCase(ggMatricePeriodi))
							&& (mmVectorPeriodi.equalsIgnoreCase(mmMatricePeriodi)
									|| ("0" + mmVectorPeriodi).equalsIgnoreCase(mmMatricePeriodi))
							&& aaVectorPeriodi.equalsIgnoreCase(aaMatricePeriodi)) {
						DFtempdata = data;
						break;
					}

				}

				ggFine = DateUtils.getDayToString(DFtempdata);
				mmFine = DateUtils.getMonthToString(DFtempdata);
				aaFine = DateUtils.getYearToString(DFtempdata);

				String calcoloGioniMesiAnni = CaricaHTML_Servlet.getQuantumIntervallo(ggInizio, mmInizio,
						aaInizio, ggFine, mmFine, aaFine);
				String sep = "~#";
				String[] aPairs = new String[3];
				aPairs = calcoloGioniMesiAnni.split(sep);
				String numAnni = aPairs[0];
				String numMesi = aPairs[1];
				String numGiorni = aPairs[2];

				matricePeriodiConsecutivi[nRigPC][0] = DateUtils.getDateToString(DItempdata, "dd/MM/yyyy");
				matricePeriodiConsecutivi[nRigPC][1] = DateUtils.getDateToString(DFtempdata, "dd/MM/yyyy");
				matricePeriodiConsecutivi[nRigPC][2] = numAnni;
				matricePeriodiConsecutivi[nRigPC][3] = numMesi;
				matricePeriodiConsecutivi[nRigPC][4] = numGiorni;
				matricePeriodiConsecutivi[nRigPC][5] = matricePeriodi[i][5];
				matricePeriodiConsecutiviEsiste = true;
			} else {
				// NO periodi Consecutivi
				if (i == 0) {
					DItempdata = (Date) vectorPeriodi.get(i);
				} else {
					for (int k = 0; k < vectorPeriodi.size() - 1; k = k + 1) {
						data = (Date) vectorPeriodi.get(k);

						String ggVectorPeriodi = DateUtils.getDayToString(data);
						String mmVectorPeriodi = DateUtils.getMonthToString(data);
						String aaVectorPeriodi = DateUtils.getYearToString(data);
						String ggMatricePeriodi = matricePeriodi[i][0].substring(8, 10);
						String mmMatricePeriodi = matricePeriodi[i][0].substring(5, 7);
						String aaMatricePeriodi = matricePeriodi[i][0].substring(0, 4);
						if ((ggVectorPeriodi.equalsIgnoreCase(ggMatricePeriodi)
								|| ("0" + ggVectorPeriodi).equalsIgnoreCase(ggMatricePeriodi))
								&& (mmVectorPeriodi.equalsIgnoreCase(mmMatricePeriodi)
										|| ("0" + mmVectorPeriodi).equalsIgnoreCase(mmMatricePeriodi))
								&& aaVectorPeriodi.equalsIgnoreCase(aaMatricePeriodi)) {
							DItempdata = data;
							break;
						}
					}
				}
				ggInizio = DateUtils.getDayToString(DItempdata);
				mmInizio = DateUtils.getMonthToString(DItempdata);
				aaInizio = DateUtils.getYearToString(DItempdata);

				if (i == 0) {
					DFtempdata = (Date) vectorPeriodi.get(i + 1);
				} else {
					for (int k = 0; k < vectorPeriodi.size(); k = k + 1) {
						data = (Date) vectorPeriodi.get(k);
						String ggVectorPeriodi = DateUtils.getDayToString(data);
						String mmVectorPeriodi = DateUtils.getMonthToString(data);
						String aaVectorPeriodi = DateUtils.getYearToString(data);
						String ggMatricePeriodi = matricePeriodi[i][1].substring(8, 10);
						String mmMatricePeriodi = matricePeriodi[i][1].substring(5, 7);
						String aaMatricePeriodi = matricePeriodi[i][1].substring(0, 4);
						if ((ggVectorPeriodi.equalsIgnoreCase(ggMatricePeriodi)
								|| ("0" + ggVectorPeriodi).equalsIgnoreCase(ggMatricePeriodi))
								&& (mmVectorPeriodi.equalsIgnoreCase(mmMatricePeriodi)
										|| ("0" + mmVectorPeriodi).equalsIgnoreCase(mmMatricePeriodi))
								&& aaVectorPeriodi.equalsIgnoreCase(aaMatricePeriodi)) {
							DFtempdata = data;
							break;
						}

					}
				}
				ggFine = DateUtils.getDayToString(DFtempdata);
				mmFine = DateUtils.getMonthToString(DFtempdata);
				aaFine = DateUtils.getYearToString(DFtempdata);

				String calcoloGioniMesiAnni = CaricaHTML_Servlet.getQuantumIntervallo(ggInizio, mmInizio,
						aaInizio, ggFine, mmFine, aaFine);
				String sep = "~#";
				String[] aPairs = new String[3];
				aPairs = calcoloGioniMesiAnni.split(sep);
				String numAnni = aPairs[0];
				String numMesi = aPairs[1];
				String numGiorni = aPairs[2];
				matricePeriodiConsecutivi[nRigPC][0] = DateUtils.getDateToString(DItempdata, "dd/MM/yyyy");
				matricePeriodiConsecutivi[nRigPC][1] = DateUtils.getDateToString(DFtempdata, "dd/MM/yyyy");
				matricePeriodiConsecutivi[nRigPC][2] = numAnni;
				matricePeriodiConsecutivi[nRigPC][3] = numMesi;
				matricePeriodiConsecutivi[nRigPC][4] = numGiorni;
				matricePeriodiConsecutivi[nRigPC][5] = matricePeriodi[i][5].toString();
				matricePeriodiConsecutiviEsiste = true;
			}
		}

		// somma colonna | Anni | Mesi | Giorni |
		// int sommaColonnaAnni = 0;
		// int sommaColonnaMesi = 0;
		// int sommaColonnaGiorni = 0;
		if (aMisure.size() > 0) {
			if (matricePeriodiConsecutiviEsiste) { // ci sono periodi consecutivi ==> in ogni riga della
													// tabella è stato inserito un periodo consecutivo
				for (int i = 0; i <= nRigPC; i++) {
					if (matricePeriodiConsecutivi[i][2] != null)
						sommaColonnaAnni = sommaColonnaAnni
								+ Integer.parseInt(matricePeriodiConsecutivi[i][2]);
					if (matricePeriodiConsecutivi[i][3] != null)
						sommaColonnaMesi = sommaColonnaMesi
								+ Integer.parseInt(matricePeriodiConsecutivi[i][3]);
					if (matricePeriodiConsecutivi[i][4] != null)
						sommaColonnaGiorni = sommaColonnaGiorni
								+ Integer.parseInt(matricePeriodiConsecutivi[i][4]);
				}
			}

			sommaColonnaAnni = sommaColonnaAnni + sommaColonnaAnniModManuale;
			sommaColonnaMesi = sommaColonnaMesi + sommaColonnaMesiModManuale;
			sommaColonnaGiorni = sommaColonnaGiorni + sommaColonnaGiorniModManuale;

			int giorni = sommaColonnaGiorni;
			while (giorni > 0) {
				giorni = giorni - 30;
				if (giorni >= 0) {
					sommaColonnaMesi = sommaColonnaMesi + 1;
					sommaColonnaGiorni = giorni;
				}
			}
			int mesi = sommaColonnaMesi;
			while (mesi > 0) {
				mesi = mesi - 12;
				if (mesi >= 0) {
					sommaColonnaAnni = sommaColonnaAnni + 1;
					sommaColonnaMesi = mesi;
				}
			}
			// int anni = sommaColonnaAnni;
		}

		int sommaColonnaAnniMisureCautelariMigrateRES = 0;
		int sommaColonnaMesiMisureCautelariMigrateRES = 0;
		int sommaColonnaGiorniMisureCautelariMigrateRES = 0;
		sommaColonnaAnniMisureCautelariMigrateRES = ctot.getNumAnni();
		sommaColonnaMesiMisureCautelariMigrateRES = ctot.getNumMesi();
		sommaColonnaGiorniMisureCautelariMigrateRES = ctot.getNumGiorni();
		int sommaColonnaAnniMisureCautelariMigrateRES_SIEP = sommaColonnaAnniMisureCautelariMigrateRES
				+ sommaColonnaAnni;
		int sommaColonnaMesiMisureCautelariMigrateRES_SIEP = sommaColonnaMesiMisureCautelariMigrateRES
				+ sommaColonnaMesi;
		int sommaColonnaGiorniMisureCautelariMigrateRES_SIEP = sommaColonnaGiorniMisureCautelariMigrateRES
				+ sommaColonnaGiorni;

		mTotaleAnni = sommaColonnaAnniMisureCautelariMigrateRES_SIEP;
		mTotaleMesi = sommaColonnaMesiMisureCautelariMigrateRES_SIEP;
		mTotaleGiorni = sommaColonnaGiorniMisureCautelariMigrateRES_SIEP;

		// mTotaleAnni = sommaColonnaAnni;
		// mTotaleMesi = sommaColonnaMesi;
		// mTotaleGiorni = sommaColonnaGiorni;
		// fine calcolo totale custodia cautelare Anni Mesi Giorni
		// dove consideriamo periodi sovrapposti solo per Misure Cautelari

	}

	public Vector calcolaTotaleParzialeMisure(Vector aMisure) throws Exception {
		// inizio calcolo totale custodia cautelare Anni Mesi Giorni
		// dove consideriamo periodi sovrapposti solo per Misure Cautelari
		// "Cessata al momento del passaggio in giudicato computabili"
		Date DItempdata;
		Date DFtempdata;
		String DItemp = "";
		String DFtemp = "";
		int sommaColonnaAnni = 0;
		int sommaColonnaMesi = 0;
		int sommaColonnaGiorni = 0;
		Vector vectorPeriodi = new Vector();
		int sommaColonnaAnniModManuale = 0;
		int sommaColonnaMesiModManuale = 0;
		int sommaColonnaGiorniModManuale = 0;

		CalendarModel ctot = new CalendarModel();
		CalendarUtil cu = new CalendarUtil();
		CalendarModel cm = new CalendarModel();
		Iterator itx = aMisure.iterator();
		while (itx.hasNext()) {
			MisuraCautelareModel lMis = (MisuraCautelareModel) itx.next();
			if (lMis.getGiorni() == null && lMis.getFlagModificaManuale() == null
					&& lMis.getFlagComputabile().equals("S")) {
				if (lMis.getCodOperatoreInserimento() == null || (lMis.getCodOperatoreInserimento() != null
						&& lMis.getCodOperatoreInserimento().length() > 2
						&& !lMis.getCodOperatoreInserimento().substring(0, 3).equalsIgnoreCase("RES"))) {
					vectorPeriodi.add(lMis.getDataInizio());
					vectorPeriodi.add(lMis.getDataFine());
				}
			}

			// inizio calcoloGioniMesiAnni A.S. settembre2015
			if (lMis.getDataFine() != null && lMis.getDataInizio() != null) {
				Date datainizio = lMis.getDataInizio();
				String ggInizio = DateUtils.getDayToString(datainizio);
				String mmInizio = DateUtils.getMonthToString(datainizio);
				String aaInizio = DateUtils.getYearToString(datainizio);

				Date datafine = lMis.getDataFine();
				String ggFine = DateUtils.getDayToString(datafine);
				String mmFine = DateUtils.getMonthToString(datafine);
				String aaFine = DateUtils.getYearToString(datafine);

				String calcoloGioniMesiAnni = "";
				try {
					calcoloGioniMesiAnni = CaricaHTML_Servlet.getQuantumIntervallo(ggInizio, mmInizio,
							aaInizio, ggFine, mmFine, aaFine);
				} catch (Exception e) {
					e.printStackTrace();
				}
				String sep = "~#";
				String[] aPairs = new String[3];
				aPairs = calcoloGioniMesiAnni.split(sep);

				cm.setNumAnni(Integer.parseInt(aPairs[0]));
				cm.setNumMesi(Integer.parseInt(aPairs[1]));
				cm.setNumGiorni(Integer.parseInt(aPairs[2]));

				// int numGiorniTotale = 0;
				// inizio calcolo giorni/mesi/anni per misura cautelare computo periodo messa alla prova
				if (lMis.getCodTipoMisura().equalsIgnoreCase("CL") && lMis.getFlagModificaManuale() == null) {
					int giorni = cm.getNumGiorni();
					int mesi = cm.getNumMesi();
					int anni = cm.getNumAnni();

					int numGiorni = anni * 360 + mesi * 30 + giorni;
					// numGiorniTotale = numGiorni;
					// numGiorni = numGiorni/3;

					BigDecimal numGiorniAnniMesiGiorni = new BigDecimal(numGiorni);
					BigDecimal periodiGiorniUnoTre = new BigDecimal(3);
					BigDecimal numGiorniAnniMesiGiorniDivTre = new BigDecimal(0);
					BigDecimal numDiff = new BigDecimal(0);
					BigDecimal uno = new BigDecimal(1);
					numGiorniAnniMesiGiorniDivTre = numGiorniAnniMesiGiorni.divide(periodiGiorniUnoTre, 1,
							RoundingMode.HALF_UP);
					// int num = tot.intValue();
					BigDecimal numGiorniAnniMesiGiorniDivTreInteroNegato = new BigDecimal(
							numGiorniAnniMesiGiorniDivTre.intValue()).negate();
					// numDiff = numGiorniAnniMesiGiorniDivTre;
					numDiff = numGiorniAnniMesiGiorniDivTre.add(numGiorniAnniMesiGiorniDivTreInteroNegato);

					// Se il decimale è >= 6 allora arrotondamento per eccesso.
					// Se il decimale è <= 5 allora arrotondamento per difetto.
					BigDecimal zero5 = new BigDecimal("0.5");
					if (numDiff.compareTo(zero5) == 0 || numDiff.compareTo(zero5) == -1) {
						// Se il decimale è <= 5 allora arrotondamento per difetto.
						numGiorniAnniMesiGiorniDivTre = new BigDecimal(
								numGiorniAnniMesiGiorniDivTre.intValue());
						// numGiorniAnniMesiGiorniDivTre = numGiorniAnniMesiGiorniDivTre.add(uno);
					} else {
						numGiorniAnniMesiGiorniDivTre = numGiorniAnniMesiGiorniDivTre.add(uno);
					}

					numGiorni = numGiorniAnniMesiGiorniDivTre.intValue();
					mesi = 0;
					anni = 0;
					// I conteggi sono effettuati usando gli algoritmi di ricalcolaGAM
					if (numGiorni > 30) {
						int tmp = numGiorni / 30;
						mesi += tmp;
						numGiorni -= tmp * 30;
					}
					if (numGiorni == 30) {
						mesi++;
						numGiorni = 0;
					}
					if (mesi > 12) {
						int tmp = mesi / 12;
						anni += tmp;
						mesi -= tmp * 12;
					}

					if (mesi == 12) {
						anni++;
						mesi = 0;
					}

					/*
					 * if (mesi<12) { anni=0; }
					 */
					cm.setNumAnni(anni);
					cm.setNumMesi(mesi);
					cm.setNumGiorni(numGiorni);
				} else {
					cm.setNumAnni(lMis.getNumAnni());
					cm.setNumMesi(lMis.getNumMesi());
					cm.setNumGiorni(lMis.getNumGiorni());
					// if (lMis.getGiorni() != null)
					// numGiorniTotale = Integer.parseInt(lMis.getGiorni().toString());
					// else
					// numGiorniTotale = Integer.parseInt("0");
				}
				// fine calcolo giorni/mesi/anni per misura cautelare computo periodo messa alla prova

				if (lMis.getFlagComputabile().equals("S")) {
					// somma giorni/mesi/anni solo per le Misure cautelari migrate dal sistema RES
					if (lMis.getCodOperatoreInserimento() != null
							&& lMis.getCodOperatoreInserimento().length() > 2
							&& lMis.getCodOperatoreInserimento().substring(0, 3).equalsIgnoreCase("RES")) {
						ctot = cu.sommaGiorni(ctot, cm);
					}
				}
			}
			ctot = cu.ricalcolaGAM(ctot);
			// fine calcoloGioniMesiAnni A.S. settembre2015

			if (lMis.getGiorni() != null && lMis.getFlagComputabile().equals("S")) {
				sommaColonnaAnniModManuale = sommaColonnaAnniModManuale
						+ Integer.parseInt(lMis.getNumAnni().toString());
				sommaColonnaMesiModManuale = sommaColonnaMesiModManuale
						+ Integer.parseInt(lMis.getNumMesi().toString());
				sommaColonnaGiorniModManuale = sommaColonnaGiorniModManuale
						+ Integer.parseInt(lMis.getNumGiorni().toString());
			}

		}

		if (vectorPeriodi != null && vectorPeriodi.size() != 0) {
			for (int i = 0; i < vectorPeriodi.size() - 1; i = i + 2) {
				// trova l'elemento minimo
				int jMin = i;
				for (int j = i + 2; j < vectorPeriodi.size(); j = j + 1) {
					if (DateUtils.isLower((Date) vectorPeriodi.get(j), (Date) vectorPeriodi.get(jMin)))// vectorPeriodi[j]<vectorPeriodi[jMin]
						jMin = j;
				}
				// scambia gli elementi con indice i e jMin
				if (i != jMin) {
					// scambia
					DItemp = (String) vectorPeriodi.get(jMin);
					DFtemp = (String) vectorPeriodi.get(jMin + 1);
					vectorPeriodi.set(jMin, vectorPeriodi.get(i));
					vectorPeriodi.set(jMin + 1, vectorPeriodi.get(i + 1));
					vectorPeriodi.set(i, DItemp);
					vectorPeriodi.set(i + 1, DFtemp);
				}
			}
		}

		// popolo una tabella di sei colonne e n righe quanti sono i periodi
		// nomi colonne: Data-inizio | Data-finale | Anni | Mesi | Giorni | Periodi-continuativi | numero
		// periodo
		// nella cella Periodi-continuativi che hanno uguale numero sono continuativi
		// ==> si deve fare la somma dei giorni mesi anni
		int nRighe = vectorPeriodi.size() / 2;
		int nColonne = 7;
		String[][] matricePeriodi = new String[nRighe][nColonne];
		// popolo Data-inizio | Data-finale |
		int r = 0;
		int c = 0;
		for (int i = 0; i < vectorPeriodi.size() - 1; i = i + 2) {
			matricePeriodi[r][c] = vectorPeriodi.get(i).toString();
			r++;
		}
		r = 0;
		c = 1;
		for (int i = 1; i <= vectorPeriodi.size() - 1; i = i + 2) {
			matricePeriodi[r][c] = vectorPeriodi.get(i).toString();
			r++;
		}

		// popolo le colonne | Anni | Mesi | Giorni | Periodi-continuativi | numero periodo
		r = 0;
		int numPeriodiContinuativi = 0;
		for (int i = 0; i < vectorPeriodi.size() - 1; i = i + 2) {
			DItempdata = (Date) vectorPeriodi.get(i);
			String ggInizio = DateUtils.getDayToString(DItempdata).toString();
			String mmInizio = DateUtils.getMonthToString(DItempdata).toString();
			String aaInizio = DateUtils.getYearToString(DItempdata).toString();

			DFtempdata = (Date) vectorPeriodi.get(i + 1);
			String ggFine = DateUtils.getDayToString(DFtempdata).toString();
			String mmFine = DateUtils.getMonthToString(DFtempdata).toString();
			String aaFine = DateUtils.getYearToString(DFtempdata).toString();

			String calcoloGioniMesiAnni = CaricaHTML_Servlet.getQuantumIntervallo(ggInizio, mmInizio,
					aaInizio, ggFine, mmFine, aaFine);
			String sep = "~#";
			String[] aPairs = new String[3];
			aPairs = calcoloGioniMesiAnni.split(sep);
			String numAnni = aPairs[0];
			String numMesi = aPairs[1];
			String numGiorni = aPairs[2];

			boolean periodiContinuativi = false;

			// controllo i periodi se sono continuativi(sovrapposti)
			if (i == 0) {
				periodiContinuativi = true;// non cambia il numero del periodo
			} else {
				Date DIprimoPeriodo = (Date) vectorPeriodi.get(i - 1);
				Date DFsecondoPeriodo = (Date) vectorPeriodi.get(i);
				// System.out.print("isEquals ");
				// System.out.print("isLower ");
				// System.out.print("getIntervallo ");

				if (DateUtils.isEquals(DIprimoPeriodo, DFsecondoPeriodo)
						|| (DateUtils.isLower(DIprimoPeriodo, DFsecondoPeriodo)
								&& DateUtils.getIntervallo(DIprimoPeriodo, DFsecondoPeriodo) == 1)) {
					periodiContinuativi = true;// non cambia il numero del periodo
				} else {
					periodiContinuativi = false;// cambia il numero del periodo
				}
			}

			int cAnni = 2;
			int cMesi = 3;
			int cGiorni = 4;
			int cPeriodiContinuativi = 5;
			int cNumeroPeriodo = 6;
			matricePeriodi[r][cAnni] = numAnni;
			matricePeriodi[r][cMesi] = numMesi;
			matricePeriodi[r][cGiorni] = numGiorni;
			if (periodiContinuativi) {
				// Periodi sono Continuativi
				matricePeriodi[r][cPeriodiContinuativi] = Integer.toString(numPeriodiContinuativi);
			} else {
				numPeriodiContinuativi = numPeriodiContinuativi + 1;
				matricePeriodi[r][cPeriodiContinuativi] = Integer.toString(numPeriodiContinuativi);
				periodiContinuativi = false;
			}
			matricePeriodi[r][cNumeroPeriodo] = Integer.toString(i);// uguale al numero del periodo: primo
																	// periodo "0" secondo "2" terzo "4"
			r++;
		}

		// somma anni mesi giorni per periodi consecutivi
		String[][] matricePeriodiConsecutivi = new String[nRighe][nColonne];
		boolean matricePeriodiConsecutiviEsiste = false;// non esiste
		// Vector vectorPeriodiTemp = new Vector();
		boolean periodiContinuativi = false;
		int nRigPC = 0;
		// int nColPC = 0;

		String ggInizio = "";
		String mmInizio = "";
		String aaInizio = "";
		String ggFine = "";
		String mmFine = "";
		String aaFine = "";
		// int numeroPeriodo = 0;
		Date data = new Date();
		DItempdata = new Date();
		DFtempdata = new Date();
		for (int i = 0; i < nRighe; i++) {
			if (i == 0) {
				periodiContinuativi = false;
			} else {
				if (matricePeriodi[i - 1][5].equalsIgnoreCase(matricePeriodi[i][5])) {
					periodiContinuativi = true;
				} else {
					periodiContinuativi = false;
					nRigPC = nRigPC + 1;
				}
			}

			if (periodiContinuativi) {
				// SI periodi Consecutivi
				ggInizio = DateUtils.getDayToString(DItempdata);
				mmInizio = DateUtils.getMonthToString(DItempdata);
				aaInizio = DateUtils.getYearToString(DItempdata);
				for (int k = 0; k < vectorPeriodi.size(); k = k + 1) {
					data = (Date) vectorPeriodi.get(k);
					String ggVectorPeriodi = DateUtils.getDayToString(data);
					String mmVectorPeriodi = DateUtils.getMonthToString(data);
					String aaVectorPeriodi = DateUtils.getYearToString(data);
					String ggMatricePeriodi = matricePeriodi[i][1].substring(8, 10);
					String mmMatricePeriodi = matricePeriodi[i][1].substring(5, 7);
					String aaMatricePeriodi = matricePeriodi[i][1].substring(0, 4);
					if ((ggVectorPeriodi.equalsIgnoreCase(ggMatricePeriodi)
							|| ("0" + ggVectorPeriodi).equalsIgnoreCase(ggMatricePeriodi))
							&& (mmVectorPeriodi.equalsIgnoreCase(mmMatricePeriodi)
									|| ("0" + mmVectorPeriodi).equalsIgnoreCase(mmMatricePeriodi))
							&& aaVectorPeriodi.equalsIgnoreCase(aaMatricePeriodi)) {
						DFtempdata = data;
						break;
					}

				}

				ggFine = DateUtils.getDayToString(DFtempdata);
				mmFine = DateUtils.getMonthToString(DFtempdata);
				aaFine = DateUtils.getYearToString(DFtempdata);

				String calcoloGioniMesiAnni = CaricaHTML_Servlet.getQuantumIntervallo(ggInizio, mmInizio,
						aaInizio, ggFine, mmFine, aaFine);
				String sep = "~#";
				String[] aPairs = new String[3];
				aPairs = calcoloGioniMesiAnni.split(sep);
				String numAnni = aPairs[0];
				String numMesi = aPairs[1];
				String numGiorni = aPairs[2];

				matricePeriodiConsecutivi[nRigPC][0] = DateUtils.getDateToString(DItempdata, "dd/MM/yyyy");
				matricePeriodiConsecutivi[nRigPC][1] = DateUtils.getDateToString(DFtempdata, "dd/MM/yyyy");
				matricePeriodiConsecutivi[nRigPC][2] = numAnni;
				matricePeriodiConsecutivi[nRigPC][3] = numMesi;
				matricePeriodiConsecutivi[nRigPC][4] = numGiorni;
				matricePeriodiConsecutivi[nRigPC][5] = matricePeriodi[i][5];
				matricePeriodiConsecutiviEsiste = true;
			} else {
				// NO periodi Consecutivi
				if (i == 0) {
					DItempdata = (Date) vectorPeriodi.get(i);
				} else {
					for (int k = 0; k < vectorPeriodi.size() - 1; k = k + 1) {
						data = (Date) vectorPeriodi.get(k);

						String ggVectorPeriodi = DateUtils.getDayToString(data);
						String mmVectorPeriodi = DateUtils.getMonthToString(data);
						String aaVectorPeriodi = DateUtils.getYearToString(data);
						String ggMatricePeriodi = matricePeriodi[i][0].substring(8, 10);
						String mmMatricePeriodi = matricePeriodi[i][0].substring(5, 7);
						String aaMatricePeriodi = matricePeriodi[i][0].substring(0, 4);
						if ((ggVectorPeriodi.equalsIgnoreCase(ggMatricePeriodi)
								|| ("0" + ggVectorPeriodi).equalsIgnoreCase(ggMatricePeriodi))
								&& (mmVectorPeriodi.equalsIgnoreCase(mmMatricePeriodi)
										|| ("0" + mmVectorPeriodi).equalsIgnoreCase(mmMatricePeriodi))
								&& aaVectorPeriodi.equalsIgnoreCase(aaMatricePeriodi)) {
							DItempdata = data;
							break;
						}
					}
				}
				ggInizio = DateUtils.getDayToString(DItempdata);
				mmInizio = DateUtils.getMonthToString(DItempdata);
				aaInizio = DateUtils.getYearToString(DItempdata);

				if (i == 0) {
					DFtempdata = (Date) vectorPeriodi.get(i + 1);
				} else {
					for (int k = 0; k < vectorPeriodi.size(); k = k + 1) {
						data = (Date) vectorPeriodi.get(k);
						String ggVectorPeriodi = DateUtils.getDayToString(data);
						String mmVectorPeriodi = DateUtils.getMonthToString(data);
						String aaVectorPeriodi = DateUtils.getYearToString(data);
						String ggMatricePeriodi = matricePeriodi[i][1].substring(8, 10);
						String mmMatricePeriodi = matricePeriodi[i][1].substring(5, 7);
						String aaMatricePeriodi = matricePeriodi[i][1].substring(0, 4);
						if ((ggVectorPeriodi.equalsIgnoreCase(ggMatricePeriodi)
								|| ("0" + ggVectorPeriodi).equalsIgnoreCase(ggMatricePeriodi))
								&& (mmVectorPeriodi.equalsIgnoreCase(mmMatricePeriodi)
										|| ("0" + mmVectorPeriodi).equalsIgnoreCase(mmMatricePeriodi))
								&& aaVectorPeriodi.equalsIgnoreCase(aaMatricePeriodi)) {
							DFtempdata = data;
							break;
						}

					}
				}
				ggFine = DateUtils.getDayToString(DFtempdata);
				mmFine = DateUtils.getMonthToString(DFtempdata);
				aaFine = DateUtils.getYearToString(DFtempdata);

				String calcoloGioniMesiAnni = CaricaHTML_Servlet.getQuantumIntervallo(ggInizio, mmInizio,
						aaInizio, ggFine, mmFine, aaFine);
				String sep = "~#";
				String[] aPairs = new String[3];
				aPairs = calcoloGioniMesiAnni.split(sep);
				String numAnni = aPairs[0];
				String numMesi = aPairs[1];
				String numGiorni = aPairs[2];
				matricePeriodiConsecutivi[nRigPC][0] = DateUtils.getDateToString(DItempdata, "dd/MM/yyyy");
				matricePeriodiConsecutivi[nRigPC][1] = DateUtils.getDateToString(DFtempdata, "dd/MM/yyyy");
				matricePeriodiConsecutivi[nRigPC][2] = numAnni;
				matricePeriodiConsecutivi[nRigPC][3] = numMesi;
				matricePeriodiConsecutivi[nRigPC][4] = numGiorni;
				matricePeriodiConsecutivi[nRigPC][5] = matricePeriodi[i][5].toString();
				matricePeriodiConsecutiviEsiste = true;
			}
		}

		// impostiamo MisCautTotParziale per la stampa ==> non viene visualizzato il totale parziale
		for (int t = 0; t < aMisure.size(); t = t + 1) {
			MisuraCautelareModel lMis1 = (MisuraCautelareModel) aMisure.get(t);
			lMis1.setMisCautTotParziale("N");
		}

		// calcolo i totali parziali
		// somma colonna | Anni | Mesi | Giorni |
		// int sommaColonnaAnniParziali = 0;
		// int sommaColonnaMesiParziali = 0;
		// int sommaColonnaGiorniParziali = 0;
		MisuraCautelareModel misuraCautelareModel = new MisuraCautelareModel();
		// boolean esistonoSoloMisContinuative = false;
		if (aMisure.size() > 0) {
			if (matricePeriodiConsecutiviEsiste) { // ci sono periodi consecutivi ==> in ogni riga della
													// tabella è stato inserito un periodo consecutivo
				// String flgContinuativa = "0";
				for (int i = 0; i <= nRigPC; i++) {
					// if(matricePeriodiConsecutivi[i][5]!=null &&
					// matricePeriodiConsecutivi[i][5].equalsIgnoreCase(flgContinuativa)){
					// if (matricePeriodiConsecutivi[i][2]!=null){
					// sommaColonnaAnniParziali = sommaColonnaAnniParziali +
					// Integer.parseInt(matricePeriodiConsecutivi[i][2]);
					// }
					// if (matricePeriodiConsecutivi[i][3]!=null){
					// sommaColonnaMesiParziali = sommaColonnaMesiParziali +
					// Integer.parseInt(matricePeriodiConsecutivi[i][3]);
					// }
					// if (matricePeriodiConsecutivi[i][4]!=null){
					// sommaColonnaGiorniParziali = sommaColonnaGiorniParziali +
					// Integer.parseInt(matricePeriodiConsecutivi[i][4]);
					// }
					// dataIintervalloContinuativoCorrente = matricePeriodiConsecutivi[i][0]; //data inizio
					// dataFintervalloContinuativoCorrente = matricePeriodiConsecutivi[i][1]; //data fine
					// esistonoSoloMisContinuative=true;
					// } else {
					// flgContinuativa = matricePeriodiConsecutivi[i][5];
					// misuraCautelareModel = new MisuraCautelareModel();
					// misuraCautelareModel.setNumAnni(new BigDecimal(sommaColonnaAnniParziali));
					// misuraCautelareModel.setNumMesi(new BigDecimal(sommaColonnaMesiParziali));
					// misuraCautelareModel.setNumGiorni(new BigDecimal(sommaColonnaGiorniParziali));
					// misuraCautelareModel.setMisCautTotParziale("S");
					// //Inserisce l'elemento specificato nel vettore "aMisure" nella posizione "i"
					// //sposta l'elemento attualmente in quella posizione(se presente)
					// //e tutti gli elementi successivi a destra(aggiunge uno a loro indici).
					//
					// //imposto solo per la stampa la 6 colonna
					// //con l'indice dove inserire l'elemento (con i totali parziali) nella lista misure
					// cautelari
					// int indiceTotaliParziali=0;
					// boolean indiceTotaliParzialiPresente=false;
					// if(aMisure.size()>0){
					// esistonoSoloMisContinuative = false;
					// for (int t=0;t<aMisure.size()-1;t=t+1)
					// {
					// MisuraCautelareModel lMis1 = (MisuraCautelareModel)aMisure.get(t);
					// MisuraCautelareModel lMis2 = (MisuraCautelareModel)aMisure.get(t+1);
					// if(DateUtils.isEquals(lMis1.getDataFine(), lMis2.getDataInizio()) ||
					// (DateUtils.isLower(lMis1.getDataFine(), lMis2.getDataInizio())
					// && DateUtils.getIntervallo(lMis1.getDataFine(), lMis2.getDataInizio())==1)){
					// //periodiContinuativi
					// indiceTotaliParzialiPresente=true;
					// indiceTotaliParziali=t+2;
					// break;
					// }
					// }
					// if(indiceTotaliParzialiPresente && misuraCautelareModel!=null ){
					// if
					// (!dataIintervalloContinuativoEsistente.equalsIgnoreCase(dataIintervalloContinuativoCorrente)
					// &&
					// !dataFintervalloContinuativoEsistente.equalsIgnoreCase(dataFintervalloContinuativoCorrente)
					// ){
					// aMisure.add(indiceTotaliParziali, misuraCautelareModel);//inserire l'elemento nel
					// vettore all'indice i e traslare gli altri elementi di uno
					// indiceTotaliParzialiPresente = false;
					// dataIintervalloContinuativoEsistente = dataIintervalloContinuativoCorrente; //data
					// inizio
					// dataFintervalloContinuativoEsistente = dataFintervalloContinuativoCorrente; //data fine
					// }
					// }
					// }
					// }

					// imposto solo per la stampa la 6 colonna
					// con l'indice dove inserire l'elemento (con i totali parziali) nella lista misure
					// cautelari
					int indiceTotaliParziali = 0;
					Vector aMisureTemp = new Vector(aMisure);
					if (matricePeriodiConsecutivi[i][1] != null) {
						// for (int t=0;t<aMisure.size();t=t+1)
						for (int t = 0; t < aMisureTemp.size(); t = t + 1) {
							String giornoFine = "";
							String meseFine = "";
							String annoFine = "";
							// MisuraCautelareModel lMis1 = (MisuraCautelareModel)aMisure.get(t);
							MisuraCautelareModel lMis1 = (MisuraCautelareModel) aMisureTemp.get(t);
							if (lMis1.getDataFine() != null) {
								giornoFine = DateUtils.getDayToString(lMis1.getDataFine());
								meseFine = DateUtils.getMonthToString(lMis1.getDataFine());
								annoFine = DateUtils.getYearToString(lMis1.getDataFine());
							}

							String ggMatricePeriodiConsecutivi = matricePeriodiConsecutivi[i][1].substring(0,
									2);
							String mmMatricePeriodiConsecutivi = matricePeriodiConsecutivi[i][1].substring(3,
									5);
							String aaMatricePeriodiConsecutivi = matricePeriodiConsecutivi[i][1].substring(6,
									10);

							ggMatricePeriodiConsecutivi = (ggMatricePeriodiConsecutivi.startsWith("0")
									? ggMatricePeriodiConsecutivi.substring(1)
									: ggMatricePeriodiConsecutivi);
							mmMatricePeriodiConsecutivi = (mmMatricePeriodiConsecutivi.startsWith("0")
									? mmMatricePeriodiConsecutivi.substring(1)
									: mmMatricePeriodiConsecutivi);

							if (giornoFine.equalsIgnoreCase(ggMatricePeriodiConsecutivi)
									&& meseFine.equalsIgnoreCase(mmMatricePeriodiConsecutivi)
									&& annoFine.equalsIgnoreCase(aaMatricePeriodiConsecutivi)
									&& lMis1.getMisCautContinuativa().equalsIgnoreCase("S")) {
								indiceTotaliParziali = t + 1;
								misuraCautelareModel = new MisuraCautelareModel();
								misuraCautelareModel
										.setNumAnni(new BigDecimal(matricePeriodiConsecutivi[i][2]));
								misuraCautelareModel
										.setNumMesi(new BigDecimal(matricePeriodiConsecutivi[i][3]));
								misuraCautelareModel
										.setNumGiorni(new BigDecimal(matricePeriodiConsecutivi[i][4]));
								misuraCautelareModel.setMisCautTotParziale("S");
								aMisure.add(indiceTotaliParziali, misuraCautelareModel);// inserire l'elemento
																						// nel vettore
																						// all'indice i e
																						// traslare gli altri
																						// elementi di uno
								aMisureTemp.remove(t);
								break;
							}
						}
					}

					if (matricePeriodiConsecutivi[i][2] != null) {
						sommaColonnaAnni = sommaColonnaAnni
								+ Integer.parseInt(matricePeriodiConsecutivi[i][2]);
					}
					if (matricePeriodiConsecutivi[i][3] != null) {
						sommaColonnaMesi = sommaColonnaMesi
								+ Integer.parseInt(matricePeriodiConsecutivi[i][3]);
					}
					if (matricePeriodiConsecutivi[i][4] != null) {
						sommaColonnaGiorni = sommaColonnaGiorni
								+ Integer.parseInt(matricePeriodiConsecutivi[i][4]);
					}
				}
			}

			sommaColonnaAnni = sommaColonnaAnni + sommaColonnaAnniModManuale;
			sommaColonnaMesi = sommaColonnaMesi + sommaColonnaMesiModManuale;
			sommaColonnaGiorni = sommaColonnaGiorni + sommaColonnaGiorniModManuale;

			int giorni = sommaColonnaGiorni;
			while (giorni > 0) {
				giorni = giorni - 30;
				if (giorni >= 0) {
					sommaColonnaMesi = sommaColonnaMesi + 1;
					sommaColonnaGiorni = giorni;
				}
			}
			int mesi = sommaColonnaMesi;
			while (mesi > 0) {
				mesi = mesi - 12;
				if (mesi >= 0) {
					sommaColonnaAnni = sommaColonnaAnni + 1;
					sommaColonnaMesi = mesi;
				}
			}
			// int anni = sommaColonnaAnni;
		}

		int sommaColonnaAnniMisureCautelariMigrateRES = 0;
		int sommaColonnaMesiMisureCautelariMigrateRES = 0;
		int sommaColonnaGiorniMisureCautelariMigrateRES = 0;
		sommaColonnaAnniMisureCautelariMigrateRES = ctot.getNumAnni();
		sommaColonnaMesiMisureCautelariMigrateRES = ctot.getNumMesi();
		sommaColonnaGiorniMisureCautelariMigrateRES = ctot.getNumGiorni();
		int sommaColonnaAnniMisureCautelariMigrateRES_SIEP = sommaColonnaAnniMisureCautelariMigrateRES
				+ sommaColonnaAnni;
		int sommaColonnaMesiMisureCautelariMigrateRES_SIEP = sommaColonnaMesiMisureCautelariMigrateRES
				+ sommaColonnaMesi;
		int sommaColonnaGiorniMisureCautelariMigrateRES_SIEP = sommaColonnaGiorniMisureCautelariMigrateRES
				+ sommaColonnaGiorni;

		mTotaleAnni = sommaColonnaAnniMisureCautelariMigrateRES_SIEP;
		mTotaleMesi = sommaColonnaMesiMisureCautelariMigrateRES_SIEP;
		mTotaleGiorni = sommaColonnaGiorniMisureCautelariMigrateRES_SIEP;

		// mTotaleAnni = sommaColonnaAnni;
		// mTotaleMesi = sommaColonnaMesi;
		// mTotaleGiorni = sommaColonnaGiorni;
		// fine calcolo totale custodia cautelare Anni Mesi Giorni
		// dove consideriamo periodi sovrapposti solo per Misure Cautelari

		// //SE SONO TUTTE CONTINUATIVE
		// if(esistonoSoloMisContinuative){
		// if(aMisure.size()>0){
		// int posTotParz=aMisure.size();
		// for (int t=0;t<aMisure.size();t=t+1)
		// {
		// MisuraCautelareModel lMis1 = (MisuraCautelareModel)aMisure.get(t);
		// if(t==posTotParz)
		// lMis1.setMisCautTotParziale("S");
		// else
		// lMis1.setMisCautTotParziale("N");
		// }
		// }
		// }

		return aMisure;
	}

}