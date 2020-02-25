package siap.sius.documentoallegato.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.utente.model.UtenteModel;
import siap.sius.avvocatura.model.AvvisiAvvocatoModel;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: DocumentoAllegatoController
 * </p>
 * <p>
 * Description: Classe Controller per DocumentoAllegato
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
public interface IDocumentoAllegato {

	public DocumentoAllegatoModel ExInserisciDocumentoAllegato(DocumentoAllegatoModel aDocumentoAllegato)
			throws F3BException;

	public DocumentoAllegatoModel ExRicercaDocumentoAllegatoByKey(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaDocumentoAllegatoByIdEvento(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaDocumentoAllegato(DocumentoAllegatoModel aDocumentoAllegato) throws F3BException;

	public Vector ExRicercaDocumentoAllegato(BigDecimal aIdEvento) throws F3BException;

	public DocumentoAllegatoModel ExRicercaDocumentoAllegatoByIdEventoCodTipo(BigDecimal aIdEvento,
			String aCodTipo) throws F3BException;

	public void ExModificaDocumentoAllegato(DocumentoAllegatoModel aDocumentoAllegato) throws F3BException;

	public void ExCancellaDocumentoAllegato(DocumentoAllegatoModel aDocumentoAllegato) throws F3BException;

	public ByteArrayOutputStream ExStampaDocumento(DocumentoAllegatoModel aDocumentoAllegato,
			UtenteModel aUtenteModel) throws F3BException;

	public DocumentoAllegatoModel ExUpdateDocument(DocumentoAllegatoModel aDocumentoAllegato)
			throws F3BException;

	public ByteArrayOutputStream ExGetDocumentoByKey(BigDecimal aKey) throws F3BException;

	public boolean IsValidato(BigDecimal aIdEvento, String aCodTipo) throws F3BException;

	public Vector ExRicercaSollecitoByIdEvento(BigDecimal aKey) throws F3BException;

	public DocumentoAllegatoModel ExInserisciFoglioComplementare(DocumentoAllegatoModel aDocumentoAllegato,
			String aCodTipo, BigDecimal aId) throws F3BException;

	public void ExModificaFoglioComplementare(DocumentoAllegatoModel aDocumentoAllegato, String aCodTipo,
			BigDecimal aId) throws F3BException;

	public void ExAggiornaValidazione(DocumentoAllegatoModel aDocumentoAllegato) throws F3BException;

	public byte[] ExGetDocPerTrasferimento(BigDecimal aIdDocumentoAllegato) throws F3BException; // STUB
																									// 12/04/2005.

	public void ExAnnullaDocumentoAllegato(DocumentoAllegatoModel aModel) throws F3BException;

	public Vector ExRicercaDocAllAnnulatiByIdEventoCodTipo(BigDecimal aIdEvento, String aCodTipo)
			throws F3BException;

	public DocumentoAllegatoModel ExRicercaFoglioComplementareByAnnoNumUfficio(
			DocumentoAllegatoModel aDocumentoAllegato) throws F3BException;

	public ByteArrayOutputStream ExStampaFoglioComp(BigDecimal aIdDocAllegato, String aCodUff,
			UtenteModel aUtenteModel) throws F3BException;

	public DocumentoAllegatoModel ExRicercaDocumentoAllegatoByKeyEvento(BigDecimal aKey) throws F3BException;

	public DocumentoAllegatoModel ExInserisciFoglioComplementareNsc(
			DocumentoAllegatoModel aDocumentoAllegato, String aCodTipo) throws F3BException;

	public void ExModificaFoglioComplementareNsc(DocumentoAllegatoModel aDocumentoAllegato, String aCodTipo)
			throws F3BException;

	// MERGE v10: aggiunto metodo di ricerca senza condizioni
	public DocumentoAllegatoModel ricDocAllByIdEvento(BigDecimal lIdEvento) throws F3BException;

	// MEV_AVVOCATURA: aggiungo un nuovo metodo ExUpdateDocument, con in input il lFlgAvvocatura
	// per inserire un avviso
	/**
	 * @param lModel
	 * @param lFlgAvvocatura
	 * @throws F3BException
	 */
	public DocumentoAllegatoModel ExUpdateDocument(DocumentoAllegatoModel lModel, Vector<AvvisiAvvocatoModel> lAvvvisiAvvocato) throws F3BException;

}
