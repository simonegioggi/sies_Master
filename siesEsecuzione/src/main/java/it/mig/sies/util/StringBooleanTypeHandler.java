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
        implements TypeHandler {
	
	/**
	 * Prende il risultato della colonna dal ResultSet
	 * @param columnName Nome colonna
	 * @param rs ResultSet
	 * @throws SQLException
	 * */
    public Object getResult(ResultSet rs, String columnName)
            throws SQLException {
        return valueOf((String) rs.getObject(columnName));
    }
    
    /**
	 * Prende il risultato della colonna dal CallableStatement
	 * @param i posizione da recuperare
	 * @param cs CallableStatement
	 * @throws SQLException
	 * */
    public Object getResult(CallableStatement cs, int i) throws SQLException {
        return valueOf((String) cs.getObject(i));
    }
    
    /**
	 * Effettua il settaggio del parametro in un PreparedStatement
	 * @throws SQLException
	 * */
    public void setParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        Boolean bValue = (Boolean) parameter;
        ps.setString(i, bValue.booleanValue() ? "Y" : "N");
    }
    
    /**
	 * Effettua il mapping Stringa-Boolean
	 * */
    public Object valueOf(String value) {
        if (value.equals("Y")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}