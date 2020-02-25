<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.model.FunzioneModel" %>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento" %>

<%@ page import="siap.sius.documentoallegato.action.ICostantiDocumentoAllegato" %>

<jsp:useBean id="provvedimenti"     scope="request" class="java.util.Vector"/>
<jsp:useBean id="fascicoloSiusGP" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="flag_valida"  scope="request" class="java.lang.String"/>

<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>

<html>
<head>
  <title>[S.I.E.S.] - Lista Provvedimenti</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
</head>

<body class="corpo">

  <link rel="STYLESHEET" type="text/css" href="/css/style.css">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Compilazione Foglio Complementare</font>
      </td>
    </tr>
   </table>
<br>
<%
  if (flag_valida.equals(""))
    flag_valida="SI";


  if (fascicoloSiusGP != null)
  {
%>
   <table>
      <tr>
        <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
      </tr>
   </table>
<%
  } // endif fascicoloSiusGP
%>
<br>
<%
  if ( provvedimenti.size() == 0 )
  {
%>
        <td class="LBG">
          <font class="label"> Procedimento privo di provvedimenti depositati. </font>
        </td>
<%
  } else
  {
%>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ListaAtti">
  <table width="96%">
  <div align=center>
    <tr>
      <td class="int" width=10%>Data emissione</td>
      <td class="int" width=20%>Tipo provvedimento</td>
      <td class="int" width=35%>Motivo provvedimento</td>
      <td class="int" width=20%>Esito provvedimento</td>
      <td class="int" width=15%>Data Deposito</td>

<%
      if (flag_valida.equals("SI"))
      {
%>
      <td class="int" width=3%>Documento<br>Validato</td>
<%    }
%>
      <td class="int" width=7%>Azioni</td>
    </tr>
  </div>
<%
    Iterator itx = provvedimenti.iterator();
    while ( itx.hasNext())
    {
      EventoModel lProv = (EventoModel)itx.next();
%>
    <tr>
      <td class="l">
      <%=StringUtils.toStringJSP(DateUtils.getDateToString(lProv.getDataEmissione(),"dd-MM-yyyy"),"-") %>
      </td>
      <td class="l" ><%=StringUtils.toStringJSP(lProv.getDescrTipoProvvedimento(),"-")%></td>
      <td class="l" ><%=StringUtils.toStringJSP(lProv.getDescrMotivo(),"-")%></td>
      <td class="l" ><%=StringUtils.toStringJSP(lProv.getDescrEsito(),"-")%></td>
      <td class="l" ><%=StringUtils.toStringJSP(DateUtils.getDateToString(lProv.getDataTrasmissioneAtti(),"dd-MM-yyyy"),"-")%></td>
<%
      if (flag_valida.equals("SI"))
      {
%>
      	<td class="c">
<%
      		if (lProv.getFlagDocumentoRegistrato() != null)
      		{
        		if (lProv.getFlagDocumentoRegistrato().compareTo("S")==0) 
        		{
%>
      				<img src="/images/TickRed.gif">
<%
   					}
        		else if (lProv.getFlagDocumentoRegistrato().compareTo("A")==0) 
        		{
%>
        			<font class="cRosso">ANNULLATO</font>
<%
        		}
        		else if (lProv.getFlagDocumentoRegistrato().compareTo("N")==0) 
        		{
%>
							<%="-"%>
<%         		  
        		}
        		
      		}
      		else
      		{
%> 
						<%="-"%>
<% 
      		}
%>					
      	</td>
<%      
			}
%>
       <td class="l" >
<%      String isBlob = "SI"; if(lProv.getFlagDocumentoRegistrato() == null){isBlob="NO";}
        String isAllegato = "NO";  if(lProv.getNumAllegati() > 0){isAllegato="SI";}
 %>
<%
      if (flag_valida.equals("SI"))
      {
%>
        <jsp:include page="<%=ICostantiProvvedimento.PG_BUTTONS_CFC%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lProv.getIdEvento()%>" />
          <jsp:param name="Allegato" value="<%=isAllegato%>" />
        </jsp:include>
<%     }
%>

       </td>
    </tr>

  <%
   } // endwhile
  %>
  </table>

    </FORM>
<%
  }  // endif provvedimenti.size()
%>
  </body>
</html>