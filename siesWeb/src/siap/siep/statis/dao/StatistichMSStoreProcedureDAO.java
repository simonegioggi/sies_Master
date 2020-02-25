package siap.siep.statis.dao;

import java.sql.Connection;

import f3b.dao.StoreProcedureDAO;

/**
 * MEV_39: aggiunta classe per invocazione STORE PROCEDURE
 * <p>
 * Title: StatistichMSStoreProcedureDAO
 * </p>
 */
public class StatistichMSStoreProcedureDAO extends StoreProcedureDAO {

	public StatistichMSStoreProcedureDAO(Connection lConn) {
		super(lConn);
	}

	public void setCodUfficioInserimento(String aValore) {
		setString("COD_UFFICIO_INSERIMENTO", aValore);
	}

	public void setDataInizio(String aValore) {
		setString("DATA_INIZIO", aValore);
	}

	public void setDataFine(String aValore) {
		setString("DATA_FINE", aValore);
	}

	public void setCodUfficioAccorpato_1(String aValore) {
		setString("COD_UFFICIO_ACCORPATO1", aValore);
	}

	public void setCodUfficioAccorpato_2(String aValore) {
		setString("COD_UFFICIO_ACCORPATO2", aValore);
	}

	public void setCodUfficioAccorpato_3(String aValore) {
		setString("COD_UFFICIO_ACCORPATO3", aValore);
	}

	public void setDataVerifica(String aValore) {
		setString("DATA_VERIFICA", aValore);
	}

	public void setStatiFascicoloStoreProcedure() {

		setStoreProcedure("ISPETTORATO_MS.stat_provvedimenti");

		// Settare i campi di Input e di output della Store Procedure
		setArgInput("COD_UFFICIO_INSERIMENTO", STRING);
		setArgInputPosition("COD_UFFICIO_INSERIMENTO", 1);

		setArgInput("COD_UFFICIO_ACCORPATO1", STRING);
		setArgInputPosition("COD_UFFICIO_ACCORPATO1", 2);

		setArgInput("COD_UFFICIO_ACCORPATO2", STRING);
		setArgInputPosition("COD_UFFICIO_ACCORPATO2", 3);

		setArgInput("COD_UFFICIO_ACCORPATO3", STRING);
		setArgInputPosition("COD_UFFICIO_ACCORPATO3", 4);

		setArgInput("DATA_VERIFICA", STRING);
		setArgInputPosition("DATA_VERIFICA", 5);		
		
		setArgInput("DATA_INIZIO", STRING);
		setArgInputPosition("DATA_INIZIO", 6);

		setArgInput("DATA_FINE", STRING);
		setArgInputPosition("DATA_FINE", 7);
	}

	public void setElaboraStatisticaAttMagStoreProcedure() {

		setStoreProcedure("ISPETTORATO_MS.attivita_magistrati");

		// Settare i campi Di Input e di output della Store Procedure
		setArgInput("DATA_INIZIO", STRING);
		setArgInputPosition("DATA_INIZIO", 1);

		setArgInput("DATA_FINE", STRING);
		setArgInputPosition("DATA_FINE", 2);

		setArgInput("COD_UFFICIO_INSERIMENTO", STRING);
		setArgInputPosition("COD_UFFICIO_INSERIMENTO", 3);
	}

}