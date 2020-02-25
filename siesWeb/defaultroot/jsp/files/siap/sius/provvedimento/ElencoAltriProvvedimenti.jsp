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

<jsp:useBean id="provvedimenti"     scope="request" class="java.util.Vector"/>
<jsp:useBean id="fascicoloSiusGP" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="flag_valida"  scope="request" class="java.lang.String"/>


<html>
<head>
  <title>[S.I.E.S.] - Lista Provvedimenti</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src=<%=IWebConstants.JS_CONFIRM %>></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
</head>

<body class="corpo">
  <link rel="STYLESHEET" type="text/css" href="/css/style.css">
  <table>
    <tr>
			<td class="LBG">
				<a href="Javascript:window.print();">
					<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
				</a>
			</td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Ricerca Altri Atti</font>
      </td>
  <!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
   </table>
<br>

<% String lFunAnnullaValidazione = "siap.sius.provvedimento.action.ActAnnullaValidazioneProvvedimento";%>
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
    	<font class="label"> Non ci sono provvedimenti allegati al fascicolo. </font>
    </td>
<%
  } 
  else
  {
%>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ListaAtti">
  <table width="96%">
  <div align=center>
    <tr>
      <td class="int" width=15%>Data emissione</td>
      <td class="int" width=17%>Tipo provvedimento</td>
      <td class="int" width=40%>Motivo provvedimento</td>
      <td class="int" width=15%>Esito provvedimento</td>
<%
      if (flag_valida.equals("SI"))
      {
%>
      	<td class="int" width=3%>Documento<br>Validato</td>
<%    
			}
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
      <td class="l"><%=StringUtils.toStringJSP(lProv.getDescrTipoProvvedimento(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(lProv.getDescrMotivo(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(lProv.getDescrEsito(),"-")%></td>
<%
      if (flag_valida.equals("SI"))
      {
%>
      <td class="c">
<%
		if (lProv.getFlagDocumentoRegistrato()!=null){
      	  if (lProv.getFlagDocumentoRegistrato().compareTo("S")==0) {
      	  	if (lProv.getCodTipoEvento().compareTo("13")!=0) { %>
						<a href="Javascript:annulla('Vuoi annullare la validazione ?','<%=lFunAnnullaValidazione%>','<%=ICostantiEvento.CAMPO_ID_EVENTO%>','<%=lProv.getIdEvento()%>');">
							<img src="/images/TickRed.gif" alt="Annulla validazione atto"  border="0">
						</a>
<%						} else { 	%>
							<img src="/images/TickRed.gif" alt="Flag validazione atto"  border="0">	
<%	
						}
	
			}
      	}
%> 
			</td>
<%    
		}
%>
       <td class="l">
        <%String isBlob = "SI"; if(lProv.getFlagDocumentoRegistrato() == null){isBlob="NO";}  %>
<%
      if (flag_valida.equals("SI"))
      {
%>
        <jsp:include page="<%=ICostantiProvvedimento.PG_BUTTONS_ALTRI%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lProv.getIdEvento()%>" />
          <jsp:param name="CodTipoProvvedimento" value="<%=lProv.getCodTipoProvvedimento()%>" />
          <jsp:param name="Stampa" value="<%=isBlob%>" />
          <jsp:param name="CodEsito" value="<%=lProv.getCodEsito()%>" />
        </jsp:include>
<%
      } else {
%>
        <jsp:include page="<%=ICostantiProvvedimento.PG_BUTTONS_NOTIFICHE%>">
          <jsp:param name="CampoIdEntita" 	value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
          <jsp:param name="ValoreIdEntita" 	value="<%=lProv.getIdEvento()%>" />
          <jsp:param name="FLAG_PIU_MENO" 	value="<%=lProv.getFlagPiuMeno()%>" />
        </jsp:include>
<%
			}
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