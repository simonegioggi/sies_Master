package siap.siep.misuracautelarebdmc.controller;

/**
* <p>Title: MisuraCautelareBdmcController</p>
* <p>Description: Classe Controller per MisuraCautelareBdmc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.sico.evento.model.EventoModel;
import siap.siep.misuracautelarebdmc.model.MisuraCautelareBdmcModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface IMisuraCautelareBdmc {

	public MisuraCautelareBdmcModel ExInserisciMisuraCautelareBdmc(
			MisuraCautelareBdmcModel aMisuraCautelareBdmc) throws F3BException;

	public Vector ExRicercaMisuraCautelareBdmc(MisuraCautelareBdmcModel aMisuraCautelareBdmc)
			throws F3BException;

	public void ExModificaMisuraCautelareBdmc(MisuraCautelareBdmcModel aMisuraCautelareBdmc,
			EventoModel aEvento) throws F3BException;

	public void ExModificaMisuraCautelareBdmcNoCommit(Connection lConn,
			MisuraCautelareBdmcModel aMisuraCautelareBdmc, EventoModel aEvento) throws F3BException;

	public void ExCancellaMisuraCautelareBdmc(MisuraCautelareBdmcModel aMisuraCautelareBdmc)
			throws F3BException;

	public BigDecimal ExGetCountMisuraCautelareBdmc(MisuraCautelareBdmcModel aMisuraCautelareBdmc)
			throws F3BException;

	public MisuraCautelareBdmcModel ExRicercaMisuraCautelareBdmcById(BigDecimal aIdMisuraCautelare)
			throws F3BException;

	public Vector ExRicercaMisuraCautelareBdmcPaged(MisuraCautelareBdmcModel aMisuraCautelareBdmc, int aPage)
			throws F3BException;

}