package siap.sico.ufficio.dao;

import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;
import siap.sico.ufficio.model.UfficioModel;

/**
 * <p>
 * Title: UfficioDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella Ufficio
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
public class UfficioDAO extends TableDAO {

	public UfficioDAO(Connection con) {
		super(con);
		setTable("UFFICIO");

		// Settare la Sequence e i campi chiave

		setFieldKey("COD_UFFICIO", STRING);

		setField("COD_UFFICIO", STRING);
		setField("COD_TIPO_UFFICIO", STRING);
		setField("COD_PROVINCIA", STRING);
		setField("COD_COMUNE", STRING);
		setField("COD_DISTRETTO", STRING);
		setField("DATA_CARICAMENTO_REGE", DATE);
		setField("COD_UFFICIO_COMPETENTE", STRING);
		setField("INDIRIZZO", STRING);
		setField("CAP", STRING);
		setField("TELEFONO", STRING);
		setField("FAX", STRING);
		setField("E_MAIL", STRING);
		setField("COD_OPERATORE_AGG", STRING);
		setField("DATA_AGG", DATE);
		setField("COD_UFFICIO_AGG", STRING);

	}

	//
	// METODI GET()
	//
	public String getCodUfficio() throws DAOException {
		return getString("COD_UFFICIO");
	}

	public String getCodTipoUfficio() throws DAOException {
		return getString("COD_TIPO_UFFICIO");
	}

	public String getCodProvincia() throws DAOException {
		return getString("COD_PROVINCIA");
	}

	public String getCodComune() throws DAOException {
		return getString("COD_COMUNE");
	}

	public String getCodDistretto() throws DAOException {
		return getString("COD_DISTRETTO");
	}

	public Date getDataCaricamentoRege() throws DAOException {
		return getDate("DATA_CARICAMENTO_REGE");
	}

	public String getCodUfficioCompetente() throws DAOException {
		return getString("COD_UFFICIO_COMPETENTE");
	}

	public String getIndirizzo() throws DAOException {
		return getString("INDIRIZZO");
	}

	public String getCap() throws DAOException {
		return getString("CAP");
	}

	public String getTelefono() throws DAOException {
		return getString("TELEFONO");
	}

	public String getFax() throws DAOException {
		return getString("FAX");
	}

	public String getEMail() throws DAOException {
		return getString("E_MAIL");
	}

	public String getCodOperatoreAgg() throws DAOException {
		return getString("COD_OPERATORE_AGG");
	}

	public Date getDataAgg() throws DAOException {
		return getDate("DATA_AGG");
	}

	public String getCodUfficioAgg() throws DAOException {
		return getString("COD_UFFICIO_AGG");
	}

	//
	// METODI SET()
	//
	public void setCodUfficio(String aValore) {
		setString("COD_UFFICIO", aValore);
	}

	public void setCodTipoUfficio(String aValore) {
		setString("COD_TIPO_UFFICIO", aValore);
	}

	public void setCodProvincia(String aValore) {
		setString("COD_PROVINCIA", aValore);
	}

	public void setCodComune(String aValore) {
		setString("COD_COMUNE", aValore);
	}

	public void setCodDistretto(String aValore) {
		setString("COD_DISTRETTO", aValore);
	}

	public void setDataCaricamentoRege(Date aValore) {
		setDate("DATA_CARICAMENTO_REGE", aValore);
	}

	public void setCodUfficioCompetente(String aValore) {
		setString("COD_UFFICIO_COMPETENTE", aValore);
	}

	public void setIndirizzo(String aValore) {
		setString("INDIRIZZO", aValore);
	}

	public void setCap(String aValore) {
		setString("CAP", aValore);
	}

	public void setTelefono(String aValore) {
		setString("TELEFONO", aValore);
	}

	public void setFax(String aValore) {
		setString("FAX", aValore);
	}

	public void setEMail(String aValore) {
		setString("E_MAIL", aValore);
	}

	public void setCodOperatoreAgg(String aValore) {
		setString("COD_OPERATORE_AGG", aValore);
	}

	public void setDataAgg(Date aValore) {
		setDate("DATA_AGG", aValore);
	}

	public void setCodUfficioAgg(String aValore) {
		setString("COD_UFFICIO_AGG", aValore);
	}

	public GenericModel getModel() throws DAOException {
		return new UfficioModel(getCodUfficio(), getCodTipoUfficio(), "", getCodDistretto(),
				getCodProvincia(), "", getCodComune(), "", getDataCaricamentoRege(),
				getCodUfficioCompetente(), getIndirizzo(), getCap(), getTelefono(), getFax(), getEMail(), null // aUfficiAccorpati
		/*
		 * , getCodOperatoreAgg() , getDataAgg() , getCodUfficioAgg() ,""
		 */
		);
	}

	public void setDAOFromModel(UfficioModel aModel) throws DAOException {
		setCodUfficio(aModel.getCodUfficio());
		setCodTipoUfficio(aModel.getCodTipoUfficio());
		setCodProvincia(aModel.getCodProvincia());
		setCodComune(aModel.getCodComune());
		setCodDistretto(aModel.getCodDistretto());
		// setDataCaricamentoRege( aModel.getDataCaricamentoRege() );
		setCodUfficioCompetente(aModel.getCodUfficioCompetente());
		setIndirizzo(aModel.getIndirizzo());
		setCap(aModel.getCap());
		setTelefono(aModel.getTelefono());
		setFax(aModel.getFax());
		setEMail(aModel.getEMail());
		setCodOperatoreAgg(aModel.getCodOperatoreAgg());
		setDataAgg(aModel.getDataAgg());
		setCodUfficioAgg(aModel.getCodUfficioAgg());
	}

	public void setDAOFromModelForUpdate(UfficioModel aModel) throws DAOException {
		// setCodUfficio( aModel.getCodUfficio() );
		// setCodTipoUfficio( aModel.getCodTipoUfficio() );
		// ((setCodProvincia( aModel.getCodProvincia() );
		// setCodComune( aModel.getCodComune() );
		// setCodDistretto( aModel.getCodDistretto() );
		// setDataCaricamentoRege( aModel.getDataCaricamentoRege() );
		// setCodUfficioCompetente( aModel.getCodUfficioCompetente() );
		setIndirizzo(aModel.getIndirizzo());
		setCap(aModel.getCap());
		setTelefono(aModel.getTelefono());
		setFax(aModel.getFax());
		setEMail(aModel.getEMail());
		setCodOperatoreAgg(aModel.getCodOperatoreAgg());
		setDataAgg(aModel.getDataAgg());
		setCodUfficioAgg(aModel.getCodUfficioAgg());

		setCondizioneUpdate(aModel.getCodUfficio());
	}

	public void setCondizione(UfficioModel aModel) {
		String lCondizioni = new String();

		boolean lInserito = false;
		if (lInserito)
			setCondition(lCondizioni);
	}

	public void setCondizioneUpdate(String key) {
		setCondition(" COD_UFFICIO = '" + key + "'");
	}

}
