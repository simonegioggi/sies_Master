package siap.sico.decodifiche.controller;

import java.util.Collection;

import siap.sico.decodifiche.model.DecodificheModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface IDecodifiche {
	public Collection ExRicercaDecodifiche(DecodificheModel aModel) throws F3BException;

	public Collection ExRicercaDecodificheRvAbbreviation(DecodificheModel lModel) throws F3BException;

	public DecodificheModel ExRicercaDecodificheProvvAnnMan(String aCodice) throws F3BException;

	public Collection ExRicercaDecodificheOrdinatePerCodice(DecodificheModel aModel) throws F3BException;

	public Collection ExRicercaDecodificheOrdinatePerDescrizione(DecodificheModel aModel) throws F3BException;

	public Collection ExRicercaDecodificheOrdinatePerCodiceAlternativo(DecodificheModel aModel)
			throws F3BException;

	public Collection ExRicercaDecodificheFiltroNull(DecodificheModel aModel) throws F3BException;

	public Collection ExRicercaDecodificheFiltroNullOrRvAbbreviation(DecodificheModel aModel)
			throws F3BException;

	public DecodificheModel ExRicercaDecodificheByHighValue(String aValue) throws F3BException;

	public Collection ExRicercaEsitiByOggetto(String lCodOggetto) throws F3BException;

	public Collection ExRicercaEsitiCompatibiliByEsitoOggetto(String lCodOggetto, String lCodEsito)
			throws F3BException;

	public String ExRicercaCodEsitiProvByCodTenore(String lCodOggetto) throws F3BException;

	public Collection ExListaOggetti(String aContenuto, String aCodTipoUfficio) throws F3BException;

	public Collection ExListaContenuti(String aCodTipoUfficio) throws F3BException;

	public Collection ExListaMotivoProvvMA(String aMisAlt) throws F3BException;

	public Collection ExListaMotivoOS(String aOSLibAnt) throws F3BException;

	public Collection ExListaMotivoOSLiberazioneAnticipataMA(String aOSLibAnt) throws F3BException;

	public Collection ExListaMotivoProvvSospProvvMA(String aMisAlt) throws F3BException;

	public Collection ExListaMotivoProvvSospProvvAD(String aMisAlt) throws F3BException;

	public Collection ExListaMotivoProvvRipristinoAD(String aMisAlt) throws F3BException;

	public Collection ExListaMotivoProvvRevocaAD(String aMisAlt) throws F3BException;

	public Collection ExListaMotivoRevocaProvvMA(String aMisAlt) throws F3BException;

	// 02/12/2010 Inizio : Aggiunto Daniela
	public Collection ExListaMotivoCessazioneProvvMA(String aMisAlt) throws F3BException;

	// 02/12/2010 Fine

	public Collection ExListaMotivoRipristinoMA(String aMisAlt) throws F3BException;

	public Collection ExListaMotivoDicEffMA(String aMisAlt) throws F3BException;

	public Collection ExListaMotivoProvvDetDomSpeciale(String aTipo) throws F3BException;

	public Collection ExListaMotivoProvvProrogaUltPeriodo(String aTipo) throws F3BException;

	public Collection ExListaMotivoProvvDetDomSpecialeAmmAff(String aTipo) throws F3BException;

	public Collection ExListaMotivoProvvRipristinoDetDomSpeciale(String aTipo) throws F3BException;

	public Collection ExListaMotivoProvvAmmProvvisoria(String aTipo) throws F3BException;

	public Collection ExListaMotivoProvvMADetDomTemp(String aTipo) throws F3BException;

	public Collection ExListaMotivoProvvMAReLibCond(String aTipo) throws F3BException;

	public Collection ExListaTipoDecreto() throws F3BException;

	public Collection ExListaMotivoProvvMACOLibCond(String aTipo) throws F3BException;

	public Collection ExListaMotivoProvvUltPeriodoMA(String aTipo) throws F3BException;

	public Collection ExListaOggettiCompleta(String aContenuto, String aCodTipoUfficio) throws F3BException;

	public Collection ExListaMotivoMAPreEff(String aTipo) throws F3BException;

	public Collection ExListaAutoritaSospensione(Collection aTipoRegistroOrdinanza) throws F3BException;

	public Collection ExListaOggettiSospensione(Collection aTipoRegistroOrdinanza) throws F3BException;

	public Collection ExListaOggettiRevoca(Collection aTipoRegistroOrdinanza) throws F3BException;

	public Collection ExListaMotiviProvvedimentoSospensione(Collection aTipoRegistroOrdinanza)
			throws F3BException;

	public Collection ExListaMotiviProvvedimentoRevoca(Collection aTipoRegistroOrdinanza) throws F3BException;

	public Collection ExListaEsitiTenoreSospensione(Collection aTipoRegistroOrdinanza) throws F3BException;

	public Collection ExListaEsitiTenoreRevoca(Collection aTipoRegistroOrdinanza) throws F3BException;

	public Collection ExListaMotivoProvvedimentoProsecProvvMA(String aTipo) throws F3BException;

	public Collection ExListaMotivoProvvedimentoProsecMA51Bis(String aTipo) throws F3BException;

	public Collection ExListaMotivoProvvedimentoEstDefMA(String aTipo) throws F3BException;

	public Collection ExListaAutoritaSospTDSUDS() throws F3BException;

	public Collection ExListaOggettiSospensioneDifferimento() throws F3BException;

	/**
	 * Restituisce la lista degli oggetti dei Differimenti Provvisori
	 * 
	 * @return
	 * @throws F3BException
	 */
	public Collection ExListaOggettiSospensioneDifferimentoProvv() throws F3BException;

	/**
	 * Restituisce la lista degli oggetti dei Differimenti Definitivo
	 * 
	 * @return
	 * @throws F3BException
	 */
	public Collection ExListaOggettiSospensioneDifferimentoDef() throws F3BException;

	/**
	 * Restituisce la lista degli oggetti della Revoca del Differimenti
	 * 
	 * @return
	 * @throws F3BException
	 */
	public Collection ExListaOggettiRevocaDifferimento() throws F3BException;

	/**
	 * Restituisce la lista dei Motivi Provvedimento per il Differimento
	 * 
	 * @return
	 * @throws F3BException
	 */
	public Collection ExListaMotivoProvvSospDifferimento() throws F3BException;

	/**
	 * Restituisce la lista dei Motivi Provvedimento per il Differimento Provvisorio
	 * 
	 * @return
	 * @throws F3BException
	 */
	public Collection ExListaMotivoProvvSospDifferimentoProvv() throws F3BException;

	/**
	 * Restituisce la lista dei Motivi Provvedimento per il Differimento Definitivo
	 * 
	 * @return
	 * @throws F3BException
	 */
	public Collection ExListaMotivoProvvSospDifferimentoDef() throws F3BException;

	/**
	 * Restituisce la lista dei Motivi di Revoca del Differimento
	 * 
	 * @return
	 * @throws F3BException
	 */
	public Collection ExListaMotivoProvvRevocaDifferimento() throws F3BException;

	/**
	 * Restituisce la lista dei Motivi di Rigetto del Differimento
	 * 
	 * @return
	 * @throws F3BException
	 */
	public Collection ExListaMotivoProvvRigettoDifferimento() throws F3BException;
	
	public Collection ExListaMotivoProvvedimentoRigettoMA() throws F3BException;

	public Collection ExRicercaDecodificheRwLowValue(DecodificheModel aModel) throws F3BException;

	public Collection ExListaMotiviInammissibilita(String aTipoUff) throws Exception;

	public Collection ExListaMotiviInammissibilitaxSottoSistema(String aTipoUff, String aSottoSistema)
			throws Exception;

	public Collection ExListaOggettiSospensioneDecisioneSor() throws F3BException;

	public Collection ExListaMotiviInammissibilitaCPP(String aTipoUff) throws Exception;

	public Collection ExListaMotiviInammissibilitaRD(String aTipoUff) throws Exception;

	/**
	 * Recupero lista motivo provvedimenti espulsione in base al tipo(Concessione,Rinuncia Opposizione..)
	 * 
	 * @param aTipo
	 * @return
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public Collection ExListaMotivoProvvedimentoEspulsione(String aTipo) throws F3BException;

	/**
	 * Definizione metodo di recupero lista dei tipi uffici cumulo filtrati per RV_HIGH_VALUE pari a T,S e C e
	 * ordinati per al decrizione ossia RV_MEANING.
	 * <p>
	 * 
	 * @return Insieme di dati.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public Collection ExListaTipiUfficioCumuloRifSiep() throws F3BException;

	/**
	 * Definizione metodo di recupero lista dei tipi uffici cumulo filtrati per RV_HIGH_VALUE pari a T,S e C
	 * per RV_ABBREVIATION pari a V e ordinati per al decrizione ossia RV_MEANING.
	 * <p>
	 * 
	 * @return Insieme di dati.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public Collection ExListaTipiUfficioCumuloUfficioLoginRifSiep() throws F3BException;

	/**
	 * Definizione metodo di recupero lista dei tipi uffici cumulo filtrati per RV_HIGH_VALUE pari a T,S,C e D
	 * e ordinati per al decrizione ossia RV_MEANING.
	 * <p>
	 * 
	 * @return Insieme di dati.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public Collection ExListaTipiUfficioCumuloRifMSic() throws F3BException;

	/**
	 * Recupero la lista delle tipologie di Rigetto del Differimento ESITO_TENORE - C014. Attenzione!!! nel
	 * campo Code viene caricato il valore dell'RV_ABBREVIATION invece del RV_LOW_VALUE in quanto sono questi
	 * i codici di interesse (anche per la costruzione delle Option)
	 * <p>
	 * 
	 * @return Insieme di dati.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public Collection ExRicercaTipologiaRigettoDiff() throws F3BException;

	public Collection ExRicercaAttivitaByIncarico(String aCodIncarico) throws F3BException;

	public DecodificheModel ExRicercaDecodificheByAbbByHigh(DecodificheModel aModel) throws F3BException;

	public Collection ExRicercaAndSetCodHighValue(DecodificheModel aModel) throws F3BException;

	public Collection ExListaTipiUfficioSige() throws F3BException;

	public Collection ExListaTipiUfficioSigeAccorpato() throws F3BException;

	public Collection ExListaOggettiSige() throws F3BException;

	public Collection ExRicercaEsitiByOggettoSige(String lCodOggetto) throws F3BException;

	public Collection ExRicercaDatiProvvSigeByOggetto(String lCodOggetto) throws F3BException;

	public Collection ExListaContenutiSige() throws F3BException;

	public Collection ExListaOggettiSigePerContenuto(String aCodContenuto) throws F3BException;

	public String ExRicercaDescrByCodOggettoSige(String codOggettoSige) throws F3BException;

	public Collection<DecodificheModel> ExRicercaDecodificheTipoAutorita() throws F3BException;

	public Collection<DecodificheModel> ExRicercaDecodificheTipoMisureMinorenni() throws F3BException;

	// MEV10-s3: aggiunte collection per gestire invio mail segnalazione
	public Collection ExTitoloPersona(DecodificheModel aModel) throws F3BException;

	public Collection ExFunzionalita(DecodificheModel aModel) throws F3BException;

	public Collection ExAzione(DecodificheModel aModel) throws F3BException;

	public Collection ExTipoSegnalazione(DecodificheModel aModel) throws F3BException;

	public Collection ExGravitaSegnalazione(DecodificheModel aModel) throws F3BException;

	// altre gestioni
	public Collection<DecodificheModel> ExRicercaDecodificheTipoMSMinorenniByNatura(String codice)
			throws F3BException;
	// FINE MEV10-s3
	
	// //@emma 20072018 intervento post COLLAUDO 11.2 
	public Collection<DecodificheModel> ricercaAllTipoAutoritaNotIn(String [] listaNotInValue) throws F3BException;
	
	// //@emma 20072018 intervento post COLLAUDO 11.2 
	public Collection ExRicercaEsitiCompatibiliByEsitoOggettoU023(String lCodOggetto, String lCodEsito)
			throws F3BException;
	
	//@emma 20190325 intervento per richieste 11.2.1
	public String ExRicercaDescrByCodContenutoSige(String codContenutoSige) throws F3BException;

}