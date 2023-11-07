package siap.siep.pagoPA.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.siep.pagoPA.model.BollettinoPagopaModel;

/**
 * Title: IBollettinoPagopa Description: Interfaccia per la gestione del Bollettino PagoPA
 *
 * @author sgioggi
 * @since MEV_2023-13
 * @version 1.0
 */
public interface IBollettinoPagopa {

	BollettinoPagopaModel ExInserisciBollettinoPagopa(BollettinoPagopaModel com) throws F3BException;

	public void ExCancellaBollettinoPagopa(BigDecimal idBollettinoPagopa) throws F3BException;

	public BollettinoPagopaModel ExRicercaBollettinoPagopaByKey(BigDecimal idBollettinoPagopa)
			throws F3BException;

	public void ExModificaBollettinoPagopa(BollettinoPagopaModel com) throws F3BException;

	public Vector<BollettinoPagopaModel> ExRicercaBollettinoPagopaByFasSieIdFascicoloSiep(
			BigDecimal idFascicoloSiep) throws F3BException;

	ByteArrayOutputStream ExGetBollettino(BigDecimal idBollettinoPagopa) throws F3BException;

	String[] ExRicercaCodiciUfficiProduzione(String codUfficio) throws F3BException;

	public Vector<BollettinoPagopaModel> ExRicercaBollettinoPagopaNonPagati(int dayOffset)
			throws F3BException;

	public void ExAggiornaStatoPagamentoBollettinoPagopa(BollettinoPagopaModel com) throws F3BException;

	public Vector<BollettinoPagopaModel> ExRicercaDebitoriConPosizioniAperte(int dayOffset,
			int generatiDaGiorni) throws F3BException;

	public Vector<BollettinoPagopaModel> ExRicercaDebitoriConPosizioniAperteInScadenza(
			int inScadenzaTraGiorni, int controllateDaGiorni, int generatiDaGiorni, int controllarePerGiorni)
			throws F3BException;

	public Vector<BollettinoPagopaModel> ExRicercaBollettiniSenzaCFConPosizioniAperte (
			int inScadenzaTraGiorni, int controllateDaGiorni, int generatiDaGiorni, int controllarePerGiorni)
			throws F3BException;	
	
	public BollettinoPagopaModel ExRicercaBollettinoPagopaByIUV(String codiceCRS) throws F3BException;

	// MEV_2023-33
	public Vector<BollettinoPagopaModel> ExRicercaBollettinoPagopaByFasSieIdFascicoloSiepIdEvento(
			BigDecimal idFascicoloSiep, BigDecimal idEvento) throws F3BException;
	
	public Vector<BollettinoPagopaModel> ExRicercaBollettiniPagopaByIdRateizzazione (
			BigDecimal idRateizzazione) throws F3BException;

}