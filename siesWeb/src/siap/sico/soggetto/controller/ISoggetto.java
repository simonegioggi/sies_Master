package siap.sico.soggetto.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.sico.soggetto.dao.SoggettoDAO;
import siap.sico.soggetto.model.SoggettoModel;
import f3b.dao.DAOException;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface ISoggetto {

	public Vector ExRicercaSoggetto(SoggettoModel aSoggetto) throws F3BException;

	public Vector ExRicercaSoggettoPerDistretto(SoggettoModel aSoggetto, String aDistretto, int aPage)
			throws F3BException;

	public SoggettoModel ExRicercaSoggettoByKey(BigDecimal aKey) throws F3BException;

	public SoggettoModel ExInserisciSoggetto(SoggettoModel aSoggetto) throws F3BException;

	public SoggettoModel ExModificaSoggetto(SoggettoModel aSoggetto) throws F3BException;

	// public void ExModificaKeyNSCByKey(SoggettoModel aSoggetto) throws F3BException;

	public void ExCancellaSoggetto(SoggettoModel aSoggetto) throws F3BException;

	public SoggettoModel ExVerifyCodCS(String aCodCS, SoggettoDAO aSogDao) throws DAOException;

	public SoggettoModel ExVerifyCodiceCUI$AFIS(String aCodCUI$AFIS, SoggettoDAO aSogDao) throws DAOException;

	public SoggettoModel ExVerifyAttoNascita(String aAttoNascita, SoggettoDAO aSogDao) throws DAOException;

	public BigDecimal ExGetCountSoggettiPerDistretto(SoggettoModel aSoggetto, String aCodDistretto)
			throws F3BException;

	/**
	 * Metodo che inserisce un soggetto non usando la sequence
	 * 
	 * @param aSoggetto
	 *            - Soggetto con IDSoggetto gia' preimpostato
	 * @param aConn
	 *            - Connessione del DB già aperta su cui non si fa ne' commit neè rollback
	 * @return String indica il codice di successo o insuccesso dell'operazione di inserimento
	 * @throws F3BException
	 */
	public String ExInserisciSoggettoWithoutSequence(SoggettoModel aSoggetto, Connection aConn)
			throws F3BException;

	public SoggettoModel ExModificaSoggettoStorici(SoggettoModel aSoggetto, Vector lKeyFascicoli,
			BigDecimal IdSoggettoVecchio) throws F3BException;

	public SoggettoModel ExModificaSoggettoStorico(SoggettoModel aSoggetto, String aProfilo,
			SoggettoModel aSoggettoVecchio, BigDecimal lKeyFascicoloUnivoco) throws F3BException;

	public BigDecimal ExGetCountSoggettiPerProcedimenti(SoggettoModel aSoggetto,
			String lCodUfficioUtenteConnesso, String lCodDistrettoUtenteConnesso, String TipoRicerca)
			throws F3BException;

	public BigDecimal ExGetEtaSoggetto(BigDecimal idSoggetto) throws F3BException;

	public SoggettoModel ExModificaSoggettoStoriciSius(SoggettoModel aSoggetto, Vector lKeyFascicoli,
			int lFascicoli, int lFascicoliAltriUff, BigDecimal IdSoggettoVecchio) throws F3BException;

	public Vector ExRicercaSoggettiOmonimi(SoggettoModel aSoggetto) throws F3BException;

	public SoggettoModel ExModificaSoggettoStoriciSige(SoggettoModel aSoggetto, Vector lKeyFascicoli,
			int lFascicoli, int lFascicoliAltriUff, BigDecimal IdSoggettoVecchio) throws F3BException;

	public void ExModificaKeyNSCByKey(SoggettoModel aSoggetto) throws F3BException;

	public Vector ExRicercaSoggettoConFascicoliPaged(SoggettoModel aSoggetto,
			String lCodUfficioUtenteConnesso, String lCodDistrettoUtenteConnesso, String TipoRicerca,
			int aPage, String majorOffice) throws F3BException;

	/**
	 * AMBROSINO 07/2009 - SUPER SOGGETTO Metodo che ricerca un soggetto per codice afis raggruppandolo per
	 * nome e cognome
	 * 
	 * @param aSoggetto
	 *            - Soggetto con IDSoggetto gia' preimpostato
	 * @param aConn
	 *            - Connessione del DB già aperta su cui non si fa ne' commit neè rollback
	 * @return String indica il codice di successo o insuccesso dell'operazione di inserimento
	 * @throws F3BException
	 */

	// public Vector ExRicercaSoggettoCui(SoggettoModel aSoggetto)
	// throws F3BException;

	public Vector ExRicercaSoggettiFascicoliOmonimi(SoggettoModel aSoggetto) throws F3BException;

	public SoggettoModel ExRicercaSoggettoIgnoto() throws F3BException;

}