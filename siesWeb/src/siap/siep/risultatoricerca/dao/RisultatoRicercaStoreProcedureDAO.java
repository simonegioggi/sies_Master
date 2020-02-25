package siap.siep.risultatoricerca.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;

import f3b.dao.DAOException;
import f3b.dao.StoreProcedureDAO;

/**
 * <p>
 * Title: RisultatoRicercaStoreProcedureDAO
 * </p>
 * <p>
 * Description: Store Procedure PULISCI.pulisci_evento
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 */
@SuppressWarnings("rawtypes")
public class RisultatoRicercaStoreProcedureDAO extends StoreProcedureDAO {

	public RisultatoRicercaStoreProcedureDAO(Connection lConn) {
		super(lConn);
		setStoreProcedure("RICERCA_APPLICAZIONE_BENEFICI");

		// Settare i campi chiave di Input e di output della Store Procedure
		setArgInput("cod_utente", STRING);
		setArgInput("cod_ufficio", STRING);
		setArgInput("anno_ini", BIG_DECIMAL);
		setArgInput("num_ini", BIG_DECIMAL);
		setArgInput("anno_fin", BIG_DECIMAL);
		setArgInput("num_fin", BIG_DECIMAL);
		setArgInput("data_reato", STRING);
		setArgInput("anni_pr", BIG_DECIMAL);
		setArgInput("mesi_pr", BIG_DECIMAL);
		setArgInput("giorni_pr", BIG_DECIMAL);
		setArgInput("data_pr", STRING);
		setArgInput("pos_giu_agg", BIG_DECIMAL);
		setArgInput("cod_pos_giu", STRING);
		setArgInput("anni_pena_comp", BIG_DECIMAL);
		setArgInput("mesi_pena_comp", BIG_DECIMAL);
		setArgInput("giorni_pena_comp", BIG_DECIMAL);
		setArgInput("nazione", STRING);

		setArgOutput("id_rec", BIG_DECIMAL);

	}

	public BigDecimal getReturn() throws DAOException {
		return getOutBigDecimal("id_rec");
	}

	public void setCodUfficio(String aValore) {
		setString("cod_ufficio", aValore);
	}

	public void setCodUtente(String aValore) {
		setString("cod_utente", aValore);
	}

	public void setAnnoIni(BigDecimal aValore) {
		setBigDecimal("anno_ini", aValore);
	}

	public void setNumIni(BigDecimal aValore) {
		setBigDecimal("num_ini", aValore);
	}

	public void setAnnoFin(BigDecimal aValore) {
		setBigDecimal("anno_fin", aValore);
	}

	public void setNumFin(BigDecimal aValore) {
		setBigDecimal("num_fin", aValore);
	}

	public void setDataReato(String aValore) {
		setString("data_reato", aValore);
	}

	public void setAnniPr(BigDecimal aValore) {
		setBigDecimal("anni_pr", aValore);
	}

	public void setMesiPr(BigDecimal aValore) {
		setBigDecimal("mesi_pr", aValore);
	}

	public void setGiorniPr(BigDecimal aValore) {
		setBigDecimal("giorni_pr", aValore);
	}

	public void setDataPr(String aValore) {
		setString("data_pr", aValore);
	}

	public void setPosGiuAgg(BigDecimal aValore) {
		setBigDecimal("pos_giu_agg", aValore);
	}

	public void setCodPosGiu(String aValore) {
		setString("cod_pos_giu", aValore);
	}

	public void setAnniPenaComp(BigDecimal aValore) {
		setBigDecimal("anni_pena_comp", aValore);
	}

	public void setMesiPenaComp(BigDecimal aValore) {
		setBigDecimal("mesi_pena_comp", aValore);
	}

	public void setGiorniPenaComp(BigDecimal aValore) {
		setBigDecimal("giorni_pena_comp", aValore);
	}

	public void setNazione(String aValore) {
		setString("nazione", aValore);
	}

	/**
	 * Esecuzione della Store Procedure.
	 * 
	 * @return Ritorna true se non si è verificato un errore
	 * @throws DAOException
	 */
	public boolean execute() throws DAOException {
		try {
			int lFieldType = 0;
			int lFieldsCount = 0;
			String lCall = "";
			String lField = null;
//			String lFieldKey = null;
			Object lValue = null;
			Enumeration lEnumFields = null;

			lCall = "{call " + mNameStoreProcedure;

			int lCountArg = 0;

			// Setto la stringa di chiamata alla Store Procedure impostando i parametri
			if (mArgInputs != null)
				lCountArg = mArgInputs.size();
			if (mArgOutputs != null)
				lCountArg += mArgOutputs.size();

			if (lCountArg != 0) {
				lCall += "(";
				while (lCountArg > 0) {
					lCall += "?,";
					lCountArg--;
				}
				lCall = lCall.substring(0, lCall.length() - 1);
				lCall += ")";
			}
			lCall += "}";

			// Prepare Call
			mCallStat = mCon.prepareCall(lCall);

			if (mArgInputs != null) {

				lEnumFields = mArgInputs.keys();

				while (lEnumFields.hasMoreElements()) {
					// lFieldsCount++;
					lField = (String) lEnumFields.nextElement();
					lFieldType = ((Integer) mArgInputs.get(lField)).intValue();

					// Se il campo non è stato valorizzato restituisce null
					lValue = mFieldsValues.get(lField);

					if (lField.equals("cod_utente"))
						lFieldsCount = 1;
					if (lField.equals("cod_ufficio"))
						lFieldsCount = 2;
					if (lField.equals("anno_ini"))
						lFieldsCount = 3;
					if (lField.equals("num_ini"))
						lFieldsCount = 4;
					if (lField.equals("anno_fin"))
						lFieldsCount = 5;
					if (lField.equals("num_fin"))
						lFieldsCount = 6;
					if (lField.equals("data_reato"))
						lFieldsCount = 7;
					if (lField.equals("anni_pr"))
						lFieldsCount = 8;
					if (lField.equals("mesi_pr"))
						lFieldsCount = 9;
					if (lField.equals("giorni_pr"))
						lFieldsCount = 10;
					if (lField.equals("data_pr"))
						lFieldsCount = 11;
					if (lField.equals("pos_giu_agg"))
						lFieldsCount = 12;
					if (lField.equals("cod_pos_giu"))
						lFieldsCount = 13;
					if (lField.equals("anni_pena_comp"))
						lFieldsCount = 14;
					if (lField.equals("mesi_pena_comp"))
						lFieldsCount = 15;
					if (lField.equals("giorni_pena_comp"))
						lFieldsCount = 16;
					if (lField.equals("nazione"))
						lFieldsCount = 17;

					switch (lFieldType) {
					case STRING: // Attributo STRING
						mCallStat.setString(lFieldsCount, (String) lValue);
						break;

					case INT: // Attributo INT
						mCallStat.setInt(lFieldsCount, ((Integer) lValue).intValue());
						break;

					case DATE: // Attributo DATE
						if (lValue != null)
							mCallStat.setTimestamp(lFieldsCount, new java.sql.Timestamp(
									((java.util.Date) lValue).getTime()));
						else
							mCallStat.setTimestamp(lFieldsCount, null);
						break;

					case BIG_DECIMAL: // Attributo BIG_DECIMAL
						mCallStat.setBigDecimal(lFieldsCount, (BigDecimal) lValue);
						break;

					default:
						throw new DAOException("Il tipo di dato del campo chiave non è ancora gestito.");
					}
				}
			}

			// Registrazione degli Output Parameter
			if (mArgOutputs != null) {
				lFieldsCount = 0;
				lEnumFields = mArgOutputs.keys();
				int lSizeInput = mArgInputs.size();

				while (lEnumFields.hasMoreElements()) {
					lFieldsCount++;
					lField = (String) lEnumFields.nextElement();
					lFieldType = ((Integer) mArgOutputs.get(lField)).intValue();

					// Se il campo non è stato valorizzato restituisce null
					mCallStat.registerOutParameter(lFieldsCount + lSizeInput, lFieldType);
				}
			}

			if (mCon != null && mCallStat != null)
				mCallStat.executeUpdate();

			return true;
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			throw new DAOException(sqlEx);
		} catch (Exception eEx) {
			eEx.printStackTrace();
			throw new DAOException("Errore Generico " + eEx);
		}

		// return false;
	}

}