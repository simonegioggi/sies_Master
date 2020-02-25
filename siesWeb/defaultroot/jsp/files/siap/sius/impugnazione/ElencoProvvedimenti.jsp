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
<%@ page import="siap.sius.impugnazione.action.ICostantiImpugnazione" %>

<jsp:useBean id="provvedimenti" scope="request" class="java.util.Vector"/>
<jsp:useBean id="fascicoloSiusGP" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="flag_valida"  scope="request" class="java.lang.String"/>
<jsp:useBean id="impugnazioni" scope="request" class="java.util.Vector"/>
<jsp:useBean id="numero_Impugnazioni"  scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficio"  scope="request" class="java.lang.String"/>

<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>

<%
//numero_Impugnazioni = "1";
%>


<%@page import="java.math.BigDecimal"%>
<html>
<head>
  <title>[S.I.E.S.] - Lista Provvedimenti per Ricorso/Impugnazione</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
</head>

<body class="corpo">

  <link rel="STYLESHEET" type="text/css" href="/css/style.css">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
<%
        String strTitolo="";
        if (tipoUfficio.compareTo("TDS")==0 )
          strTitolo = "Elenco Provvedimenti di Ricorso";
        else
          strTitolo = "Elenco Provvedimenti di Impugnazione/Ricorso";
%>
        <font class="campo"><%=strTitolo%></font>
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
  // 19/11/2007 Gestione Presenza Provvedimenti Depositati.
  boolean presenzaProvvedimentiDepositati = false;
  Iterator itx0 = provvedimenti.iterator();
  while ( itx0.hasNext())
  {
    EventoModel lProv = (EventoModel)itx0.next();
    if ( lProv.getDataTrasmissioneAtti() != null)
	presenzaProvvedimentiDepositati = true;
  }

  if ( ! presenzaProvvedimentiDepositati )
  {
%>
    <td class="LBG">
      <font class="label"> Non ci sono provvedimenti depositati allegati al fascicolo. </font>
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
        <td class="int" >Data emissione</td>
        <td class="int" >Tipo provvedimento</td>
        <td class="int" >Motivo provvedimento</td>
        <td class="int" >Esito provvedimento</td>
<%
        if (flag_valida.equals("SI"))
        {
%>
          <td class="int">Documento<br>Validato</td>
<%
        }
%>
        <td class="int" >Azioni</td>
      </tr>
    </div>
<%
      Iterator itx = provvedimenti.iterator();
      while ( itx.hasNext())
      {
        EventoModel lProv = (EventoModel)itx.next();
        // STUB 24/05/2004 Escludiamo i provvedimenti senza Data Trasmissione Atti (Valorizzata a seguito di Deposito Provvedimento )
        if ( lProv.getDataTrasmissioneAtti() != null)
        {
%>
          <tr>
            <td class="c">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lProv.getDataEmissione(),"dd-MM-yyyy"),"-") %>
            </td>
            <td class="c" ><%=StringUtils.toStringJSP(lProv.getDescrTipoProvvedimento(),"-")%></td>
            <td class="c" ><%=StringUtils.toStringJSP(lProv.getDescrMotivo(),"-")%></td>
            <td class="c" ><%=StringUtils.toStringJSP(lProv.getDescrEsito(),"-")%></td>
<%
            if (flag_valida.equals("SI"))
            {
%>
              <td class="c">
<%
              if (lProv.getFlagDocumentoRegistrato()!=null)
              {
                if (lProv.getFlagDocumentoRegistrato().compareTo("S")==0)
                {
%>
                  <img src="/images/TickRed.gif">
<%
                }
              }
%>
              </td>
<%
            }
%>
            <td class="c" >
<%
              String isBlob = "SI";
              if(lProv.getFlagDocumentoRegistrato() == null)
              {
                isBlob="NO";
              }
%>
              <jsp:include page="<%=ICostantiImpugnazione.PG_BUTTONS_RICERCA%>">
                <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
                <jsp:param name="ValoreIdEntita" value="<%=lProv.getIdEvento()%>" />
                <jsp:param name="CodTipoProvvedimento" value="<%=lProv.getCodTipoProvvedimento()%>" />
                <jsp:param name="CodMotivo" value="<%=lProv.getCodMotivo()%>" />
                <jsp:param name="FlagPiuMeno" value="<%=lProv.getFlagPiuMeno()%>" />
                <jsp:param name="Stampa" value="<%=isBlob%>" />
                <jsp:param name="numeroImpugnazioni" value="<%=numero_Impugnazioni%>" />
              </jsp:include>
            </td>
          </tr>
<%
        } // EndIf su DataTrasmissioneAtti
      } // endwhile

  %>
  </table>
    </FORM>
<%
  }  // endif provvedimenti.size()
%>
  </body>
</html>