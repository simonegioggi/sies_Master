<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator"%>

<jsp:useBean id="foro" scope="request" class="java.lang.String"/>
<jsp:useBean id="comune" scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>

<html>
<head>
<title>[S.I.E.S.] - GestioneAvvocato </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript">
  var desktop;

 function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

  function Verify()
  {

  }

</script>

</head>

		<body class="corpo">
		<table>
			<tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
			<td class=LBG><font class="label">Funzione :</font>&nbsp;&nbsp;
 <%
			 AvvocatoModel lAvvocato = null;
			 String lAction = new String();
			 if( modalita.equals("R") )
			 {
			   lAction = "siap.siep.avvocato.action.ActRicercaDifensore";
			   lAvvocato=new AvvocatoModel();
%>
				<font class="campo">Ricerca Difensore</font>
		 <%}%>
	</td>
</tr>
</table>
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadRicercaAvvocato">
<table cellspacing=2 cellpadding=2>
		<tr>
				<td class="l">Cognome </td>
				<td class="l"><input  size=35 maxlength=35 title="Campo Cognome" type="text" name="<%= ICostantiAvvocato.CAMPO_COGNOME %>"  ></td>
		</tr>
		<tr>
				<td class="l">Nome </td>
				<td class="l"><input size=35 maxlength=35  title="Campo Nome" type="text" name="<%= ICostantiAvvocato.CAMPO_NOME %>"  ></td>
		</tr>

		<tr>
				<td class="l">Foro </td>	   
				<td class="l">
				<select name="<%=ICostantiAvvocato.CAMPO_FORO%>" size="1">
        	 <%=foro%>
        </select>
       	</td>
		</tr>

		<tr><td>&nbsp;</td></tr>
    <tr>
        <td colspan=2>
		    <input type="Hidden" name="Action" value="<%=lAction%>">
        <input class=bottone  type="submit" value="Conferma" onclick="return Verify();">
        </td>
    </tr>
  </table>
</form>
<script language="JavaScript" type="text/javascript">
	var frmvalidator  = new Validator("LoadRicercaAvvocato");
  frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_COGNOME %>","alpha");
  frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_NOME %>","alpha");
  frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_FORO %>","alpha");
</script>
</body>
</html>