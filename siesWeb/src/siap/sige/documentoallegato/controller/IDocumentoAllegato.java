package siap.sige.documentoallegato.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Vector;

import siap.sige.documentoallegato.model.DocumentoAllegatoModel;
import f3b.util.F3BException;

public interface IDocumentoAllegato {
	public DocumentoAllegatoModel ExRicercaFCByKeyEvento ( BigDecimal idEvento )
			  throws F3BException;
	
	public DocumentoAllegatoModel ExRicercaFCById( BigDecimal idDoc)
			  throws F3BException;
	
	public Vector<DocumentoAllegatoModel> ExRicercaFogliComplementariByIdEvento (BigDecimal idEvento)throws F3BException;
	
	public DocumentoAllegatoModel ExInserisciFoglioComplementare (DocumentoAllegatoModel aDocumentoAllegato) throws F3BException;
	
	public DocumentoAllegatoModel ExRicercaFoglioComplementareByFascicolo (BigDecimal idFascicolo) throws F3BException;
	
	public void ExEliminaSollecito (BigDecimal idDocumento)  throws F3BException;
	
	public ByteArrayOutputStream ExGetDocumentoByKey (BigDecimal  aKey) throws F3BException;
	
	public Vector <DocumentoAllegatoModel> ExRicercaSollecitoByIdEvento(BigDecimal lIdEvento) throws F3BException;
	
	public BigDecimal countDecretiDepositoFissazioneUdienzaNonValidati (BigDecimal idFascicolo) throws F3BException;
	
	public BigDecimal countDecretiDepositoFissazioneUdienzaValidati (BigDecimal idFascicolo) throws F3BException;
}
