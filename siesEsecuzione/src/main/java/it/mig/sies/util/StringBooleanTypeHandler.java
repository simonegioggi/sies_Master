package it.mig.sies.util;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

/**
 * SIES FASE 2 - Handler definito per poter gestire tramite MyBatis 
 * la conversione di un carattere Y/N ad un boolean
 * 
 * @author Federico Paparoni
 * */

public class StringBooleanTypeHandler
        implements TypeHandler<Boolean> {
	
	/**
	 * Prende il risultato della colonna dal ResultSet
	 * @param columnName Nome colonna
	 * @param rs ResultSet
	 * @throws SQLException
	 * */
    @Override
    public Boolean getResult(ResultSet rs, String columnName)
            throws SQLException {
        return valueOf((String) rs.getObject(columnName));
    }

    /**
	 * Prende il risultato della colonna dal ResultSet tramite indice
	 * @param i posizione da recuperare
	 * @param rs ResultSet
	 * @throws SQLException
	 * */
    @Override
    public Boolean getResult(ResultSet rs, int i) throws SQLException {
        return valueOf((String) rs.getObject(i));
    }
    
    /**
	 * Prende il risultato della colonna dal CallableStatement
	 * @param i posizione da recuperare
	 * @param cs CallableStatement
	 * @throws SQLException
	 * */
    @Override
    public Boolean getResult(CallableStatement cs, int i) throws SQLException {
        return valueOf((String) cs.getObject(i));
    }
    
    /**
	 * Effettua il settaggio del parametro in un PreparedStatement
	 * @throws SQLException
	 * */
    @Override
    public void setParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, Boolean.TRUE.equals(parameter) ? "Y" : "N");
    }
    
    /**
	 * Effettua il mapping Stringa-Boolean
	 * */
    public Boolean valueOf(String value) {
        return "Y".equalsIgnoreCase(value);
    }
}