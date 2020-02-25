package siap.siep.modulocumulo.controller;


/**
* <p>Title: SoggettoCumulatoController</p>
* <p>Description: Classe Controller per il SoggettoCumulato</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.util.F3BException;

import siap.siep.modulocumulo.model.SoggettoCumulatoModel;


public interface ISoggettoCumulato
{
	public SoggettoCumulatoModel ExInserisciSoggetto_Cumulato(SoggettoCumulatoModel aSoggetto)
		    throws F3BException;
	
	public SoggettoCumulatoModel ExRicercaSoggettoCumulatoByKey(BigDecimal aKey) throws F3BException;
	

	public SoggettoCumulatoModel ExModificaSoggettoCumulato(SoggettoCumulatoModel aSoggetto) throws F3BException;
	
	public void ExCancellaSoggettoCumulato(SoggettoCumulatoModel aSoggetto) throws F3BException;
	
	//MEV 26 Cumulo Step2
	public String ExInserisciSoggetto_CumulatoWithoutSequence(SoggettoCumulatoModel aSoggetto, Connection lConn) throws F3BException;
	
}
