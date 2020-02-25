package siap.siep.statis.dao;

/**
* <p>Title: IspTempiIscrizioneSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella IspTempiIscrizione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import siap.siep.statis.model.IspTempiIscrizioneModel;
import siap.siep.statis.model.IspTempiModel;

public class IspTempiSqlDAO extends SqlDAO {

	public IspTempiSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODI RICERCA()
	//

	public void ricercaRiepilogoGeneraleTempiRicezioneIscrizione(int aAnno) throws DAOException {
		String lStatement = new String("");

		lStatement += "SELECT " + aAnno + " ANNO, entro5, entro20, entro30, entro60, entro90, oltre90 from "
				+ "(SELECT count(*) entro5  FROM ISP_TEMPI_ISCRIZIONE where TEMPO_RICEZIONE_ISCRIZIONE < 6 and (DATA_ISCRIZIONE > to_date('3112"
				+ (aAnno - 1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE < to_date('0101"
				+ (aAnno + 1) + "', 'ddmmyyyy')) ) A, "
				+ "(SELECT count(*) entro20 FROM ISP_TEMPI_ISCRIZIONE where (TEMPO_RICEZIONE_ISCRIZIONE between 6 and 20) and (DATA_ISCRIZIONE > to_date('3112"
				+ (aAnno - 1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE < to_date('0101"
				+ (aAnno + 1) + "', 'ddmmyyyy')) ) B, "
				+ "(SELECT count(*) entro30 FROM ISP_TEMPI_ISCRIZIONE where (TEMPO_RICEZIONE_ISCRIZIONE between 21 and 30) and (DATA_ISCRIZIONE > to_date('3112"
				+ (aAnno - 1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE < to_date('0101"
				+ (aAnno + 1) + "', 'ddmmyyyy')) ) C, "
				+ "(SELECT count(*) entro60 FROM ISP_TEMPI_ISCRIZIONE where (TEMPO_RICEZIONE_ISCRIZIONE between 31 and 60) and (DATA_ISCRIZIONE > to_date('3112"
				+ (aAnno - 1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE < to_date('0101"
				+ (aAnno + 1) + "', 'ddmmyyyy')) ) D, "
				+ "(SELECT count(*) entro90 FROM ISP_TEMPI_ISCRIZIONE where (TEMPO_RICEZIONE_ISCRIZIONE between 61 and 90) and (DATA_ISCRIZIONE > to_date('3112"
				+ (aAnno - 1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE < to_date('0101"
				+ (aAnno + 1) + "', 'ddmmyyyy')) ) E, "
				+ "(SELECT count(*) oltre90 FROM ISP_TEMPI_ISCRIZIONE where TEMPO_RICEZIONE_ISCRIZIONE > 90 and (DATA_ISCRIZIONE > to_date('3112"
				+ (aAnno - 1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE < to_date('0101"
				+ (aAnno + 1) + "', 'ddmmyyyy')) ) F ";

		setStatement(lStatement);
	}

	public void ricercaRiepilogoGeneraleTempiGiudicatoIscrizione(int aAnno) throws DAOException {
		String lStatement = new String("");

		lStatement += "SELECT " + aAnno + " ANNO, entro5, entro20, entro30, entro60, entro90, oltre90 from "
				+ "(SELECT count(*) entro5  FROM ISP_TEMPI_ISCRIZIONE where TEMPO_GIUDICATO_ISCRIZIONE < 6 and (DATA_ISCRIZIONE > to_date('3112"
				+ (aAnno - 1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE < to_date('0101"
				+ (aAnno + 1) + "', 'ddmmyyyy')) ) A, "
				+ "(SELECT count(*) entro20 FROM ISP_TEMPI_ISCRIZIONE where (TEMPO_GIUDICATO_ISCRIZIONE between 6 and 20) and (DATA_ISCRIZIONE > to_date('3112"
				+ (aAnno - 1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE < to_date('0101"
				+ (aAnno + 1) + "', 'ddmmyyyy')) ) B, "
				+ "(SELECT count(*) entro30 FROM ISP_TEMPI_ISCRIZIONE where (TEMPO_GIUDICATO_ISCRIZIONE between 21 and 30) and (DATA_ISCRIZIONE > to_date('3112"
				+ (aAnno - 1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE < to_date('0101"
				+ (aAnno + 1) + "', 'ddmmyyyy')) ) C, "
				+ "(SELECT count(*) entro60 FROM ISP_TEMPI_ISCRIZIONE where (TEMPO_GIUDICATO_ISCRIZIONE between 31 and 60) and (DATA_ISCRIZIONE > to_date('3112"
				+ (aAnno - 1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE < to_date('0101"
				+ (aAnno + 1) + "', 'ddmmyyyy')) ) D, "
				+ "(SELECT count(*) entro90 FROM ISP_TEMPI_ISCRIZIONE where (TEMPO_GIUDICATO_ISCRIZIONE between 61 and 90) and (DATA_ISCRIZIONE > to_date('3112"
				+ (aAnno - 1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE < to_date('0101"
				+ (aAnno + 1) + "', 'ddmmyyyy')) ) E, "
				+ "(SELECT count(*) oltre90 FROM ISP_TEMPI_ISCRIZIONE where TEMPO_GIUDICATO_ISCRIZIONE > 90 and (DATA_ISCRIZIONE > to_date('3112"
				+ (aAnno - 1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE < to_date('0101"
				+ (aAnno + 1) + "', 'ddmmyyyy')) ) F ";

		setStatement(lStatement);
	}

	public void ricercaRiepilogoGeneraleTempiGiudicatoRicezione(int aAnno) throws DAOException {
		String lStatement = new String("");

		lStatement += "SELECT " + aAnno + " ANNO, entro5, entro20, entro30, entro60, entro90, oltre90 from "
				+ "(SELECT count(*) entro5  FROM ISP_TEMPI_RICEZIONE where TEMPO_GIUDICATO_RICEZIONE < 6 and (DATA_ARRIVO_ATTO > to_date('3112"
				+ (aAnno - 1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ARRIVO_ATTO < to_date('0101"
				+ (aAnno + 1) + "', 'ddmmyyyy')) ) A, "
				+ "(SELECT count(*) entro20 FROM ISP_TEMPI_RICEZIONE where (TEMPO_GIUDICATO_RICEZIONE between 6 and 20) and (DATA_ARRIVO_ATTO > to_date('3112"
				+ (aAnno - 1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ARRIVO_ATTO < to_date('0101"
				+ (aAnno + 1) + "', 'ddmmyyyy')) ) B, "
				+ "(SELECT count(*) entro30 FROM ISP_TEMPI_RICEZIONE where (TEMPO_GIUDICATO_RICEZIONE between 21 and 30) and (DATA_ARRIVO_ATTO > to_date('3112"
				+ (aAnno - 1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ARRIVO_ATTO < to_date('0101"
				+ (aAnno + 1) + "', 'ddmmyyyy')) ) C, "
				+ "(SELECT count(*) entro60 FROM ISP_TEMPI_RICEZIONE where (TEMPO_GIUDICATO_RICEZIONE between 31 and 60) and (DATA_ARRIVO_ATTO > to_date('3112"
				+ (aAnno - 1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ARRIVO_ATTO < to_date('0101"
				+ (aAnno + 1) + "', 'ddmmyyyy')) ) D, "
				+ "(SELECT count(*) entro90 FROM ISP_TEMPI_RICEZIONE where (TEMPO_GIUDICATO_RICEZIONE between 61 and 90) and (DATA_ARRIVO_ATTO > to_date('3112"
				+ (aAnno - 1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ARRIVO_ATTO < to_date('0101"
				+ (aAnno + 1) + "', 'ddmmyyyy')) ) E, "
				+ "(SELECT count(*) oltre90 FROM ISP_TEMPI_RICEZIONE where TEMPO_GIUDICATO_RICEZIONE > 90 and (DATA_ARRIVO_ATTO > to_date('3112"
				+ (aAnno - 1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ARRIVO_ATTO < to_date('0101"
				+ (aAnno + 1) + "', 'ddmmyyyy')) ) F ";

		setStatement(lStatement);
	}

	public void ricercaRiepilogoGeneraleTempiIscrizioneEmissione(int aAnno) throws DAOException {
		String lStatement = new String("");

		lStatement += "SELECT " + aAnno + " ANNO, entro5, entro20, entro30, entro60, entro90, oltre90 from "
				+ "(SELECT count(*) entro5  FROM ISP_TEMPI_EMISSIONE where TEMPI_ISCRIZIONE_EMISSIONE < 6 and (DATA_ISCRIZIONE_FASCICOLO > to_date('3112"
				+ (aAnno - 1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE_FASCICOLO < to_date('0101"
				+ (aAnno + 1) + "', 'ddmmyyyy')) ) A, "
				+ "(SELECT count(*) entro20 FROM ISP_TEMPI_EMISSIONE where (TEMPI_ISCRIZIONE_EMISSIONE between 6 and 20) and (DATA_ISCRIZIONE_FASCICOLO > to_date('3112"
				+ (aAnno - 1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE_FASCICOLO < to_date('0101"
				+ (aAnno + 1) + "', 'ddmmyyyy')) ) B, "
				+ "(SELECT count(*) entro30 FROM ISP_TEMPI_EMISSIONE where (TEMPI_ISCRIZIONE_EMISSIONE between 21 and 30) and (DATA_ISCRIZIONE_FASCICOLO > to_date('3112"
				+ (aAnno - 1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE_FASCICOLO < to_date('0101"
				+ (aAnno + 1) + "', 'ddmmyyyy')) ) C, "
				+ "(SELECT count(*) entro60 FROM ISP_TEMPI_EMISSIONE where (TEMPI_ISCRIZIONE_EMISSIONE between 31 and 60) and (DATA_ISCRIZIONE_FASCICOLO > to_date('3112"
				+ (aAnno - 1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE_FASCICOLO < to_date('0101"
				+ (aAnno + 1) + "', 'ddmmyyyy')) ) D, "
				+ "(SELECT count(*) entro90 FROM ISP_TEMPI_EMISSIONE where (TEMPI_ISCRIZIONE_EMISSIONE between 61 and 90) and (DATA_ISCRIZIONE_FASCICOLO > to_date('3112"
				+ (aAnno - 1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE_FASCICOLO < to_date('0101"
				+ (aAnno + 1) + "', 'ddmmyyyy')) ) E, "
				+ "(SELECT count(*) oltre90 FROM ISP_TEMPI_EMISSIONE where TEMPI_ISCRIZIONE_EMISSIONE > 90 and (DATA_ISCRIZIONE_FASCICOLO > to_date('3112"
				+ (aAnno - 1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE_FASCICOLO < to_date('0101"
				+ (aAnno + 1) + "', 'ddmmyyyy')) ) F ";

		setStatement(lStatement);
	}

	// MEV 27 - STATISTICA SUI TEMPI ISCRIZIONE PROCEDIMENTI DI CLASSE VII (Converione Pene Pecuniarie)
	public void RicercaRiepilogoTempiIscrizioniCPP(int aAnno, int Tipo, String aggmmIni, String aggmmFin,
			int aAnnoIni, int aAnnoFin) throws DAOException {
		// Parametri: Tipo = Tipo di Distinta (individua lo stato del procedimento);

		String lStatement = new String("");

		String data1 = "";
		String data2 = "";

		if (aAnno == aAnnoIni) {
			data1 = aggmmIni + aAnno;
			data2 = "3112" + aAnno;
		} else if (aAnno == aAnnoFin) {
			data1 = "0101" + aAnno;
			data2 = aggmmFin + aAnno;
		} else // if(aAnno > aAnnoIni && aAnno < aAnnoFin)
		{
			data1 = "0101" + aAnno;
			data2 = "3112" + aAnno;
		}

		if (Tipo == 1) // tra data Comunicazione Impossibilità Esazione e Arrivo in Cancelleria
		{
			lStatement += "SELECT " + aAnno
					+ " ANNO, 'DISTINTA_1' TIPO_DISTINTA, entro5, entro20, entro30, entro60, entro90, oltre90 from "
					+ "(SELECT count(*) entro5  FROM ISP_TEMPI_ISCRIZIONE_CPP where TEMPI_ARRIVO_COMIMPESA < 6 and (DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) A, "
					+ "(SELECT count(*) entro20 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_ARRIVO_COMIMPESA between 6 and 20) and (DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) B, "
					+ "(SELECT count(*) entro30 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_ARRIVO_COMIMPESA between 21 and 30) and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) C, "
					+ "(SELECT count(*) entro60 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_ARRIVO_COMIMPESA between 31 and 60) and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) D, "
					+ "(SELECT count(*) entro90 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_ARRIVO_COMIMPESA between 61 and 90) and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) E, "
					+ "(SELECT count(*) oltre90 FROM ISP_TEMPI_ISCRIZIONE_CPP where TEMPI_ARRIVO_COMIMPESA > 90 and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) F ";

		} else if (Tipo == 2) // Tra data Arrivo in Cancelleria e Iscrizione
		{
			lStatement += "SELECT " + aAnno
					+ " ANNO, 'DISTINTA_2' TIPO_DISTINTA, entro5, entro20, entro30, entro60, entro90, oltre90 from "
					+ "(SELECT count(*) entro5  FROM ISP_TEMPI_ISCRIZIONE_CPP where TEMPI_ARRIVO_ISCRIZIONE < 6 and (DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) A, "
					+ "(SELECT count(*) entro20 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_ARRIVO_ISCRIZIONE between 6 and 20) and (DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) B, "
					+ "(SELECT count(*) entro30 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_ARRIVO_ISCRIZIONE between 21 and 30) and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) C, "
					+ "(SELECT count(*) entro60 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_ARRIVO_ISCRIZIONE between 31 and 60) and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) D, "
					+ "(SELECT count(*) entro90 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_ARRIVO_ISCRIZIONE between 61 and 90) and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) E, "
					+ "(SELECT count(*) oltre90 FROM ISP_TEMPI_ISCRIZIONE_CPP where TEMPI_ARRIVO_ISCRIZIONE > 90 and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) F ";

		} else if (Tipo == 3) // tra Data Iscrizione Data Trasmissione Ufficio Sorveglianza
		{

			lStatement += "SELECT " + aAnno
					+ " ANNO, 'DISTINTA_3' TIPO_DISTINTA, entro5, entro20, entro30, entro60, entro90, oltre90 from "
					+ "(SELECT count(*) entro5  FROM ISP_TEMPI_ISCRIZIONE_CPP where TEMPI_ISCRIZIONE_TRASMISSIONE < 6 and (DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) A, "
					+ "(SELECT count(*) entro20 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_ISCRIZIONE_TRASMISSIONE between 6 and 20) and (DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) B, "
					+ "(SELECT count(*) entro30 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_ISCRIZIONE_TRASMISSIONE between 21 and 30) and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) C, "
					+ "(SELECT count(*) entro60 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_ISCRIZIONE_TRASMISSIONE between 31 and 60) and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) D, "
					+ "(SELECT count(*) entro90 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_ISCRIZIONE_TRASMISSIONE between 61 and 90) and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) E, "
					+ "(SELECT count(*) oltre90 FROM ISP_TEMPI_ISCRIZIONE_CPP where TEMPI_ISCRIZIONE_TRASMISSIONE > 90 and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) F ";

		} else if (Tipo == 4) // tra data Trasmissione Ufficio Sorveglianza data Decisione Ufficio
								// Sorveglianza
		{

			lStatement += "SELECT " + aAnno
					+ " ANNO, 'DISTINTA_4' TIPO_DISTINTA, entro5, entro20, entro30, entro60, entro90, oltre90 from "
					+ "(SELECT count(*) entro5  FROM ISP_TEMPI_ISCRIZIONE_CPP where TEMPI_TRASM_PROV_SORV < 6 and (DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) A, "
					+ "(SELECT count(*) entro20 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_TRASM_PROV_SORV between 6 and 20) and (DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) B, "
					+ "(SELECT count(*) entro30 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_TRASM_PROV_SORV between 21 and 30) and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) C, "
					+ "(SELECT count(*) entro60 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_TRASM_PROV_SORV between 31 and 60) and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) D, "
					+ "(SELECT count(*) entro90 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_TRASM_PROV_SORV between 61 and 90) and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) E, "
					+ "(SELECT count(*) oltre90 FROM ISP_TEMPI_ISCRIZIONE_CPP where TEMPI_TRASM_PROV_SORV > 90 and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) F ";

		}

		setStatement(lStatement);

	} // CHIUDE RicercaRiepilogoTempiIscrizioniCPP()

	public void RicercaRiepilogoTempiIscrizioniCPPPerMese(int aAnno, int Tipo, String aggmmIni,
			String aggmmFin, String aTipoRice, String aQuale_Trim_Sem) throws DAOException {
		// Parametri: Tipo = Tipo di Distinta (individua lo stato del procedimento);
		// aTipoRice = Tipo di Ricerca (Semestrale / Trimestrale)
		// aQuale_Trim_Sem = Quale Semestre o Trimestre (PRIMO, SECONDO, etc...)

		String lStatement = new String("");

		String data1 = "";
		String data2 = "";
		String lPeriodo = "";

		data1 = aggmmIni + aAnno;
		data2 = aggmmFin + aAnno;
		lPeriodo = aQuale_Trim_Sem + " " + aTipoRice.toUpperCase();

		if (Tipo == 1) // tra data Comunicazione Impossibilità Esazione e Arrivo in Cancelleria
		{
			lStatement += "SELECT '" + lPeriodo
					+ "' PERIODO, 'DISTINTA_1' TIPO_DISTINTA, entro5, entro20, entro30, entro60, entro90, oltre90 from "
					+ "(SELECT count(*) entro5  FROM ISP_TEMPI_ISCRIZIONE_CPP where TEMPI_ARRIVO_COMIMPESA < 6 and (DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) A, "
					+ "(SELECT count(*) entro20 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_ARRIVO_COMIMPESA between 6 and 20) and (DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) B, "
					+ "(SELECT count(*) entro30 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_ARRIVO_COMIMPESA between 21 and 30) and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) C, "
					+ "(SELECT count(*) entro60 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_ARRIVO_COMIMPESA between 31 and 60) and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) D, "
					+ "(SELECT count(*) entro90 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_ARRIVO_COMIMPESA between 61 and 90) and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) E, "
					+ "(SELECT count(*) oltre90 FROM ISP_TEMPI_ISCRIZIONE_CPP where TEMPI_ARRIVO_COMIMPESA > 90 and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) F ";

		} else if (Tipo == 2) // Tra data Arrivo in Cancelleria e Iscrizione
		{
			lStatement += "SELECT '" + lPeriodo
					+ "' PERIODO, 'DISTINTA_2' TIPO_DISTINTA, entro5, entro20, entro30, entro60, entro90, oltre90 from "
					+ "(SELECT count(*) entro5  FROM ISP_TEMPI_ISCRIZIONE_CPP where TEMPI_ARRIVO_ISCRIZIONE < 6 and (DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) A, "
					+ "(SELECT count(*) entro20 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_ARRIVO_ISCRIZIONE between 6 and 20) and (DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) B, "
					+ "(SELECT count(*) entro30 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_ARRIVO_ISCRIZIONE between 21 and 30) and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) C, "
					+ "(SELECT count(*) entro60 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_ARRIVO_ISCRIZIONE between 31 and 60) and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) D, "
					+ "(SELECT count(*) entro90 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_ARRIVO_ISCRIZIONE between 61 and 90) and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) E, "
					+ "(SELECT count(*) oltre90 FROM ISP_TEMPI_ISCRIZIONE_CPP where TEMPI_ARRIVO_ISCRIZIONE > 90 and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) F ";

		} else if (Tipo == 3) // tra Data Iscrizione Data Trasmissione Ufficio Sorveglianza
		{

			lStatement += "SELECT '" + lPeriodo
					+ "' PERIODO, 'DISTINTA_3' TIPO_DISTINTA, entro5, entro20, entro30, entro60, entro90, oltre90 from "
					+ "(SELECT count(*) entro5  FROM ISP_TEMPI_ISCRIZIONE_CPP where TEMPI_ISCRIZIONE_TRASMISSIONE < 6 and (DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) A, "
					+ "(SELECT count(*) entro20 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_ISCRIZIONE_TRASMISSIONE between 6 and 20) and (DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) B, "
					+ "(SELECT count(*) entro30 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_ISCRIZIONE_TRASMISSIONE between 21 and 30) and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) C, "
					+ "(SELECT count(*) entro60 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_ISCRIZIONE_TRASMISSIONE between 31 and 60) and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) D, "
					+ "(SELECT count(*) entro90 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_ISCRIZIONE_TRASMISSIONE between 61 and 90) and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) E, "
					+ "(SELECT count(*) oltre90 FROM ISP_TEMPI_ISCRIZIONE_CPP where TEMPI_ISCRIZIONE_TRASMISSIONE > 90 and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) F ";

		} else if (Tipo == 4) // tra data Trasmissione Ufficio Sorveglianza data Decisione Ufficio
								// Sorveglianza
		{

			lStatement += "SELECT '" + lPeriodo
					+ "' PERIODO, 'DISTINTA_4' TIPO_DISTINTA, entro5, entro20, entro30, entro60, entro90, oltre90 from "
					+ "(SELECT count(*) entro5  FROM ISP_TEMPI_ISCRIZIONE_CPP where TEMPI_TRASM_PROV_SORV < 6 and (DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) A, "
					+ "(SELECT count(*) entro20 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_TRASM_PROV_SORV between 6 and 20) and (DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) B, "
					+ "(SELECT count(*) entro30 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_TRASM_PROV_SORV between 21 and 30) and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) C, "
					+ "(SELECT count(*) entro60 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_TRASM_PROV_SORV between 31 and 60) and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) D, "
					+ "(SELECT count(*) entro90 FROM ISP_TEMPI_ISCRIZIONE_CPP where (TEMPI_TRASM_PROV_SORV between 61 and 90) and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) E, "
					+ "(SELECT count(*) oltre90 FROM ISP_TEMPI_ISCRIZIONE_CPP where TEMPI_TRASM_PROV_SORV > 90 and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"
					+ data1 + "', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('" + data2
					+ "', 'ddmmyyyy') ) ) F ";

		}

		setStatement(lStatement);

	} // Chiude RicercaRiepilogoTempiIscrizioniCPPPerMese()

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		IspTempiModel aModel = new IspTempiModel();

		aModel.setEntro5(getInteger("ENTRO5"));
		aModel.setEntro20(getInteger("ENTRO20"));
		aModel.setEntro30(getInteger("ENTRO30"));
		aModel.setEntro60(getInteger("ENTRO60"));
		aModel.setEntro90(getInteger("ENTRO90"));
		aModel.setOltre90(getInteger("OLTRE90"));
		aModel.setAnno(getInteger("ANNO"));

		return aModel;
	}

	public GenericModel getRiepilogoCPPModel() throws DAOException {
		IspTempiModel aModel = new IspTempiModel();

		aModel.setEntro5(getInteger("ENTRO5"));
		aModel.setEntro20(getInteger("ENTRO20"));
		aModel.setEntro30(getInteger("ENTRO30"));
		aModel.setEntro60(getInteger("ENTRO60"));
		aModel.setEntro90(getInteger("ENTRO90"));
		aModel.setOltre90(getInteger("OLTRE90"));
		aModel.setAnno(getInteger("ANNO"));
		aModel.setTipo(getString("TIPO_DISTINTA"));

		return aModel;
	}

	public GenericModel getRiepilogoPerMeseCPPModel() throws DAOException {
		IspTempiModel aModel = new IspTempiModel();

		aModel.setEntro5(getInteger("ENTRO5"));
		aModel.setEntro20(getInteger("ENTRO20"));
		aModel.setEntro30(getInteger("ENTRO30"));
		aModel.setEntro60(getInteger("ENTRO60"));
		aModel.setEntro90(getInteger("ENTRO90"));
		aModel.setOltre90(getInteger("OLTRE90"));
		aModel.setPeriodo(getString("PERIODO"));
		aModel.setTipo(getString("TIPO_DISTINTA"));

		return aModel;
	}

	public void selCondizione(IspTempiIscrizioneModel aModel) {
		// String lCondizioni = new String();
		// boolean lInserito = false;
	}

	public String setCondizioni(IspTempiIscrizioneModel aModel) {
		String lCondizioni = new String();
		// boolean lInserito = false;
		return lCondizioni;
	}

}