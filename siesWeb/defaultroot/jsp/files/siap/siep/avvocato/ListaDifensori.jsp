<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="java.util.Vector" %>
<%@ page import="f3b.util.StringUtils" %>

<jsp:useBean id="avvocati"    scope="request" class="java.util.Vector"/>
<jsp:useBean id="nome"    scope="request" class="java.lang.String"/>
<jsp:useBean id="cognome"    scope="request" class="java.lang.String"/>
<jsp:useBean id="foro"    scope="request" class="java.lang.String"/>

<html>
<head>
<title>[S.I.E.S.] - GestioneAvvocato </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript">
function conferma(a_action, a_parameter, a_entityname, a_entityvalue, a_destnname,  a_destvalue, a_destnnameforo,  a_destvalueforo, a_destvaluef  )
{
        str = "/jsp/Main.jsp?Action=" + a_action + "&" +a_parameter +"=" + a_entityname + "&" +a_entityvalue +  "=" + a_destnname + "&" +a_destvalue + "=" +a_destnnameforo+ "&" +a_destvalueforo+ "="+a_destvaluef;
        if (window.confirm('Confermi la cancellazione ?'))
                {
                        window.location.href=str;
                }
}
</script>
</head>

<body class="corpo">
  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>" >
    <table>
  		<tr>
    		<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
    		<td class="LBG">
      		<font class="label">Funzione :</font>&nbsp;
      		<font class="campo">Elenco Avvocati</font>
    		</td>
   		</tr>
 		</table>     
  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
  <br>

  <div align=center>
<table>
    <tr>
      <td class="int">Cognome Nome</td>
      <td class="int" >Foro</td>
      <td class="int">Telefono</td>
      <td class="int">E-Mail</td>
      <td class="int">Codice Fiscale</td>
      <td class="int">Sospeso fino al</td>
      <td class="int">Radiato dal</td>
      <td class="int">Non in attività per</td>
      <td class="int" width=5%>Azioni</td>
    </tr>
 <%
  Iterator itx = avvocati.iterator();

  while ( itx.hasNext())
  {

      AvvocatoModel lAvvocato = (AvvocatoModel)itx.next();
%>
	<tr>
    	<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
      	<td class=c><%=lAvvocato.getCognome()%>&nbsp;<%=lAvvocato.getNome()%>&nbsp;</td>
      	<td class=c><%=lAvvocato.getForo()%>&nbsp;</td>
 		<td class=c>
       		<%=StringUtils.toStringJSP(lAvvocato.getTelefono())%>&nbsp;
  		</td>
   		<td class=c>
       		<%=StringUtils.toStringJSP(lAvvocato.getEMail())%>&nbsp;
  		</td>
     	<td class=c>
       		<%=StringUtils.toStringJSP(lAvvocato.getCodiceFiscale())%>&nbsp;
  		</td>
 		<td class=c>
          	<%=StringUtils.toStringJSP(DateUtils.getDateToString(lAvvocato.getDataSospensione(),"dd-MM-yyyy"))%>&nbsp;
 		</td>
 		<td class=c>
          	<%=StringUtils.toStringJSP(DateUtils.getDateToString(lAvvocato.getDataRadiazione(),"dd-MM-yyyy"))%>&nbsp;
 		</td>
      	<td class=c>&nbsp;<%=StringUtils.toStringJSP(lAvvocato.getDescrNonAttivita())%></td>
		<input type="hidden" name="prova" value="<%=nome%>">
 		<td class=l>
	        <jsp:include page="<%=IWebConstants.PG_BUTTONS_AVVOCATO%>">
	           	<jsp:param name="CampoIdEntita" value="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>" />
	           	<jsp:param name="ValoreIdEntita" value="<%=lAvvocato.getIdAvvocato()%>" />
	           	<jsp:param name="cognome" value="cognome" />
	          	<jsp:param name="valorecognome" value="<%=StringUtils.cStrForJS(cognome)%>" />
	           	<jsp:param name="nome" value="nome" />
	           	<jsp:param name="valorenome" value="<%=StringUtils.cStrForJS(nome)%>" />
	           	<jsp:param name="foro" value="foro" />
	           	<jsp:param name="valoreforo" value="<%=foro%>" />
	           	<jsp:param name="valoreufficio" value="<%=lAvvocato.getCodUffAppartenenza()%>" />
          	</jsp:include>
      	</td>
	</tr>
<%
  }
%>
    </table>

</div>
</form>
	</body>
</html>