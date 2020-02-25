package siap.sige.magistrato.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import siap.sige.magistrato.model.MagistratoModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: MagistratoController
 * </p>
 * <p>
 * Description: Classe Controller per Magistrato
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
@SuppressWarnings("rawtypes")
public interface IMagistrato {

	public MagistratoModel ExInserisciMagistrato(MagistratoModel aMagistrato) throws F3BException;

	public MagistratoModel ExInserisciMagistratoMultiSezione(MagistratoModel aMagistrato,
			String provenienzaInsMod) throws F3BException;

	public ArrayList<MagistratoModel> ExRicercaMagistrato(MagistratoModel aMagistrato, int numPage)
			throws F3BException;

	/*
	 * public MagistratoModel ExRicercaMagistratoByKey (BigDecimal aKey) throws F3BException;
	 */

	// 20170918: [SG] aggiunto parametro di passaggio poichè il magistrato può essere inserito da un
	// ufficio differente da quello in cui ha delle udienze poichè trasferito
	public MagistratoModel ExRicercaMagistratoByCod(String aCod, String codUfficioAppartenenza)
			throws F3BException;

	/*
	 * public Vector ExRicercaMagistratoByCodUfficio (String aCodUfficio) throws F3BException;
	 */
	/*
	 * public Vector ExElencoCbxMagistratiByCodUfficio (String aCodUfficio) throws F3BException;
	 * 
	 * public Vector ExElencoCbxMagByCodComuneCodTipoUff (String aCodComune, String aCodTipoUfficio) throws
	 * F3BException;
	 */
	public MagistratoModel ExModificaMagistrato(MagistratoModel aMagistrato) throws F3BException;

	public MagistratoModel ExModificaMagistratoMultiSezione(MagistratoModel aMagistrato) throws F3BException;

	public MagistratoModel ExModificaMagistratoMultiSezioneValida(MagistratoModel aMagistrato)
			throws F3BException;

	public boolean controlloCollegamentoUdienza(MagistratoModel aMagistrato) throws F3BException;

	public void ExCancellaMagistrato(String aCodMagistrato, String aCodUff) throws F3BException;

	public Vector ExRicercaMagistratoByCognomeSezione(String aCognome, String aSezione, String aUfficio)
			throws F3BException;

	public Collection ExElencoCbxMagistratiByCodUfficio(String aCodUfficio) throws F3BException;

	public Collection ExElencoCbxMagByCodComuneCodTipoUff(String aCodComune, String aCodTipoUfficio)
			throws F3BException;

	public int ExGetNumRicercaMagistratoSezioneValida(MagistratoModel aMagistrato) throws F3BException;

	/*
	 * public MagistratoModel ExRicercaMagistratoByFascicolo (BigDecimal aFascicolo ) throws F3BException;
	 */
	/*
	 * public Vector ExRicercaMagistratoPaged ( MagistratoModel aMagistrato,int aPage ) throws F3BException;
	 * 
	 * public BigDecimal ExGetCountMagistratiPaged(MagistratoModel aMagistrato) throws F3BException;
	 */
	/*
	 * public String ExInserisciMagistratoWithoutSequence(MagistratoModel aMagistrato, Connection lConn)
	 * throws F3BException;
	 */

	public int ExGetNumRicercaMagistrato(MagistratoModel aMagistrato) throws F3BException;

	public MagistratoModel ExModificaMagistratoNoSezioni(MagistratoModel aMagistrato) throws F3BException;

	public Vector<MagistratoModel> ExRicercaMagistratoByIdSezioneUffApp(BigDecimal idSezione, String lUffAppa)
			throws F3BException;

	// 20170919: [SG] aggiunta query per controllo preventivo
	// 20171012: [EC] mi faccio tornare la lista degli uffici dove è attivo
	public Vector<String> controllaMagistratoAttivoAltriUffici(MagistratoModel lMagMod) throws F3BException;

	// 20171013: [EC] aggiungo metodo per recuperare il magistato per codice ed ufficio appartenenza elazionato con le sezioni
	public MagistratoModel ExRicercaMagistratoByCodEdUfficioAppartenenza(
			String codMagistrato, String codUfficioUtenteConnesso)throws F3BException;
	
	// metodo aggiunto per nuova gestione udienze monocratiche/collegiali per sies 11.2.1
	public MagistratoModel ExRicercaMagistratoByCodETipoUfficio(String codMagistrato, String tipoUfficio, String sedeUfficio)throws F3BException;
	
	

}