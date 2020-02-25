package siap.sico.ufficio.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.ufficio.model.UfficiProvvedimentoModel;
import siap.sico.ufficio.model.UfficioModel;

@SuppressWarnings("rawtypes")
public interface IUfficio {

	public Vector ExGetUfficio() throws F3BException;

	public Vector ListaTipoUfficiDescr() throws F3BException;

	public Vector ListaUfficiAccorpati(String tipoUfficio, String ufficioCompetente) throws F3BException;

	public Vector ListaDistretti() throws F3BException;

	public Vector ListaTDSM() throws F3BException;

	public Vector ListaPMM() throws F3BException;

	public Vector ListaUfficiDistretto(String codDistretto) throws F3BException;

	public Vector ListaUfficiMinorDistretto(String codDistretto) throws F3BException;

	public boolean verifyUfficioByDescrComune(String aDescrComune) throws F3BException;

	public UfficioModel getUfficioByCodTipoUffDescrComune(String aCodTipoUfficio, String aCodComune)
			throws F3BException;

	public String getDescTipoUffByCodUfficio(String aCodTipoUfficio) throws F3BException;

	public UfficioModel getUfficioByKey(String aCodiceUfficio) throws F3BException;

	public UfficioModel getUfficioAccorpatoByAccorpanteIncrement(String aCodUfficioAccorpante,
			String aIncremento) throws F3BException;

	public UfficioModel ExRicercaSedeByFascicolo(BigDecimal aFascicolo) throws F3BException;

	public UfficioModel ExRicercaSedeUfficioEmittenteByFascicolo(BigDecimal aFascicolo) throws F3BException;

	public Vector ListaUDS() throws F3BException;

	public Vector ListaUDSM() throws F3BException;

	public Vector ListaUfficiPerTipo(String aCodTipoUfficio) throws F3BException;

	public Vector ListaUfficiPerTipo(String aCodTipoUfficio, String aFlagAccorp) throws F3BException;

	public Vector ListaUfficiPerTipoUfficiCumulo(String aCodTipoUfficio) throws F3BException;

	public Vector ListaUfficiCompletaDistretto(String codDistretto) throws F3BException;

	public UfficioModel getUfficioUDSTDS(String aCodDistretto, String aCodTipoUfficio, String aCodComune)
			throws F3BException;

	public String getPrefissoUtenteUfficio(String aCodDistretto) throws F3BException;

	public Vector ListaUfficiCompletaDistrettoAbilitatiLogin(String CodDistretto) throws F3BException;

	public Vector ExGetListaComuniUfficiPerDistretto(String aDistretto, String aComune, String aCodUfficio)
			throws F3BException;

	// MEV_39 03/01/2018 modificata firma metodo
	public Vector ListaUfficiInteressatiProvvedimento(BigDecimal aIdFascicoloSius,
			BigDecimal aNumFascUnificati, String aCodOggettoProceeeee) throws F3BException;

	public boolean stressoDistretto(BigDecimal aCodUfficio1, BigDecimal aCodUfficio2, Connection aConn)
			throws F3BException;

	public UfficioModel ExModificaUfficio(UfficioModel aUfficio) throws F3BException;

	public UfficioModel ExRicercaUfficioByCod(String aCod) throws F3BException;

	public Vector ListaUfficiProcuraXDistrettoAbilitatiLogin(String CodDistretto) throws F3BException;

	public UfficioModel getUfficioByCodTipoUffCodComune(String aCodTipoUfficio, String aCodComune)
			throws F3BException;

	// MEV_39: aggiunti metodi di ricerca
	public UfficiProvvedimentoModel getUfficioPMEsecDest(BigDecimal idFascicoloSiepOrigine,
			String codTipoUfficio, String chiaveUfficioSiepOrigine) throws F3BException;

	public UfficiProvvedimentoModel getUfficioPGCAPEsecDest(String codTipoUfficio,
			String codComuneUtenteConnesso) throws F3BException;

	// 13/03/2018 metodo introdotto per anomalia m_dg.DOG07.28-02-2018.0007015.U (parametro scadenziario
	// mancante)
	public String getTipoUfficioUtente(String aCodDistretto) throws F3BException;

}