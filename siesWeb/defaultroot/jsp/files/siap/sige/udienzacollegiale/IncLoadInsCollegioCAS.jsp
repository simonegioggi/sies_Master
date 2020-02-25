<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sige.sezione.action.ICostantiSezione"%>
<jsp:useBean id="collegio" 		scope="request" class="siap.sige.collegio.model.CollegioModel" />

<%
	String lPresidente ="";
	if (collegio != null && collegio.getCollegioMagistrati()!=null && collegio.getCollegioMagistrati().length > 0 )
		lPresidente = collegio.getCollegioMagistrati()[0].getMagistrato().getCognome()+" "+collegio.getCollegioMagistrati()[0].getMagistrato().getNome(); 

	String lGiudice ="";
	if (collegio != null && collegio.getCollegioMagistrati()!=null && collegio.getCollegioMagistrati().length > 1 )
		lGiudice = collegio.getCollegioMagistrati()[1].getMagistrato().getCognome()+" "+collegio.getCollegioMagistrati()[1].getMagistrato().getNome();

%>
    <tr>
      <td class="l">Presidente</td>
			<td class="l">
        <input type="text" maxlength="28" size="26"
							 value="<%=lPresidente%>"		
               name="Magistrato" readonly >
      <font class="l">&nbsp;&nbsp;Giudice</font>
			<font class="l">
        <input type="text" maxlength="25" size="24"
							 value="<%=lGiudice%>"		
               name="Magistrato" readonly >
      </td>
    </tr>