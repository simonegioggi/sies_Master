<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="siap.sius.avvocato.action.ICostantiAvvocato" %>
<%@ page import="siap.sius.avvocato.model.AvvocatoModel" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.web.IWebConstants"%>

<jsp:useBean id="isModificabile" scope="request" class="java.lang.String" />
<jsp:useBean id="avvocato" scope="request" class="java.util.Vector" />

<html>
<head>
	<title>[S.I.E.S.] - Lista Avvocati legati ad un Provvedimento</title>
	<link rel="STYLESHEET" type="text/css" href="/css/style.css">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>

</head>

<body class=corpo>
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Avvocati</font></td>
    </tr>
     <tr></tr>
     <tr>
       <jsp:include page="<%=siap.sius.fascicolo.action.ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
    <tr>
      <td>&nbsp;</td>
     </tr>
  </table>


 <Table width="100%">
  <tr>
    <td class=int>Nome</td>
    <td class=int>Foro</td>
    <td class=int>Indirizzo</td>
    <td class=int>Telefono</td>
    <td class=int>Tipo</td>
    <td class=int>Azioni</td>

  </tr>
 <%
  	Iterator itx = avvocato.iterator();

  	while ( itx.hasNext())
  	{
    	AvvocatoModel lAvv = (AvvocatoModel)itx.next();
	%>
	<tr>
        <td class=l><%=StringUtils.toStringJSP(lAvv.getCognome(),"-") + " " + StringUtils.toStringJSP(lAvv.getNome(),"-")%></td>
        <td class=l><%=StringUtils.toStringJSP(lAvv.getForo(),"-")%></td>
        <td class=l><%=StringUtils.toStringJSP(lAvv.getIndirizzo(),"-")%></td>
        <td class=l><%=StringUtils.toStringJSP(lAvv.getTelefono(),"-")%></td>
        <td class=l><%=StringUtils.toStringJSP(lAvv.getDescrTipo() ,"-")%></td>

        <%--  SI INSERISCE L'AZIONE CHE SI VUOLE ESEGUIRE RELATIVA ALLA COLONNA Azioni--%>
        <%if (isModificabile.compareTo("SI")==0 ){%>
          <td class="l">
            <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
               <jsp:param name="CampoIdEntita" value="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>" />
               <jsp:param name="ValoreIdEntita" value="<%=lAvv.getIdAvvocato()%>" />
            </jsp:include>
           </td>
        <%}else{%>
          <td class="l">&nbsp;</td>
        <%}%>
        </tr>
	<%
	}
%>
</table>

</body>
</html>