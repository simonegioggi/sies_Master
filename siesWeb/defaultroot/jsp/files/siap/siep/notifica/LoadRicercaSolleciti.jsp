<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.siep.rinnovo.action.ICostantiRinnovo" %>
<%@ page import="siap.siep.rinnovo.model.RinnovoModel" %>
<%@ page import="siap.siep.verbale.model.VerbaleModel" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.web.ISIAPCostantiWeb" %>

<jsp:useBean id="rinnovo" scope="request" class="java.util.Vector" />
<jsp:useBean id="notifica" scope="request" class="java.lang.String" />

<html>
<head>
	<title>[S.I.E.S.] - Elenco Solleciti</title>
	<link rel="STYLESHEET" type="text/css" href="/css/style.css">
</head>

<body class="corpo">
 <table>
    <tr>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Elenco Solleciti</font>
      </td>
    </tr>
 </table>
 <Table width="100%">
<% 	Iterator itx = rinnovo.iterator();
    while ( itx.hasNext())
    {
     RinnovoModel lRinMod = (RinnovoModel)itx.next();
      if(lRinMod != null)
       {%>
	     <tr>
        <td class=l>Data Sollecito</td>
        <td class=l><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lRinMod.getDataRinnovo(),"dd-MM-yyyy"))%></font>&nbsp;</td>
       </tr>
	     <tr>
         <td class=l>Organo da Sollecitare</td>
         <td class=l><font class="campo"><%=StringUtils.toStringJSP(lRinMod.getDescrTipoAutoritaRinnovo())%></font></td>
         <td class=l>Luogo</td>
         <td class=l><font class="campo"><%=StringUtils.toStringJSP(lRinMod.getDescrLuogoRinnovo())%></font></td>
      </tr>
	  <tr>
        <td class=l>Indirizzo</td>
        <td class=l><font class="campo"><%=StringUtils.toStringJSP(lRinMod.getNote(),"-")%></font>&nbsp;</td>
        <td class=c colspan="2">
        <table>	         
			<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_CANCELLA_SIEP%>">
				<jsp:param name="LinkAction" value="<%= "/jsp/Main.jsp?Action=siap.siep.rinnovo.action.ActCancellaRinnovo&"+ICostantiRinnovo.CAMPO_ID_RINNOVO+"="+lRinMod.getIdRinnovo()+"&fieldname="+lRinMod.getIdRinnovo()+"&LinkActionRitorno=siap.siep.notifica.action.ActLoadRicercaSolleciti"%>"/>								
			</jsp:include>	
		</table>	 
	   </td>
 
       </tr>

	    <tr>
       <td> &nbsp;</td>
      </tr>
<%    }
   }
%>
</table>
</body>
</html>