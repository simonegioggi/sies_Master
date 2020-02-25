<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.math.BigDecimal"%>

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

<jsp:useBean id="fascicoloSiusGP" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>

<jsp:useBean id="provvedimenti"   scope="request" class="java.util.Vector"/>
<% // Hashtable <BigDecimal,Vector<ImpugnazioneModel>> idEvento, elencoOpposizioni%>
<jsp:useBean id="opposizioni" scope="request" class="java.util.Hashtable"/>

<jsp:useBean id="tipoUfficio"  scope="request" class="java.lang.String"/>

<jsp:useBean id="flag_valida"  scope="request" class="java.lang.String"/>

<%
//==============================================================================
// JSP per la visualizzazione dell'elenco dei provvedimenti (02,03) su cui è
// possibile effettuare opposizione.
// Per ogni evento viene passato l'elenco delle OPPOSIZIONI già a sistema.
// n.b. alla jsp arrivano tutti i provvedimenti legati al fascicolo. La jsp 
//      scarta i provvedimenti non depositati EVENTO.DATA_TRASMISSIONE_ATTI null
//==============================================================================


%>

<html>
<head>
  <title>[S.I.E.S.] - Lista Provvedimenti per Opposizione</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Elenco Provvedimenti di Opposizione</font>
      </td>
    </tr>
   </table>
   
  <br>

  <% if (   fascicoloSiusGP != null 
         && fascicoloSiusGP.getFascicoloSiusModel()!=null 
         && fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()!=null
        ) { %>
  <table>
    <tr>
      <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
  </table>
  <% } %>

  <br>
  
<%
  //========================================================
  // Vengono visualizzati solo i provvedimenti depositati
  //========================================================
  boolean presenzaProvvedimentiDepositati = false;
  
  Iterator itx0 = provvedimenti.iterator();
  while ( itx0.hasNext())
  {
    EventoModel lProv = (EventoModel)itx0.next();
    if ( lProv.getDataTrasmissioneAtti() != null)
      presenzaProvvedimentiDepositati = true;
  }
%>
  
  <% if ( ! presenzaProvvedimentiDepositati ) { %>
  <table width="96%">
    <tr>  
      <td class="LBG">
        <font class="label"> Non ci sono provvedimenti depositati allegati al fascicolo. </font>
      </td>
    </tr>
  </table>
  <%  } else {%>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ListaAtti">
    <table width="96%">
    <div align=center>
      <tr>
        <td class="int" >Data emissione</td>
        <td class="int" >Tipo provvedimento</td>
        <td class="int" >Motivo provvedimento</td>
        <td class="int" >Esito provvedimento</td>
        <% if ("SI".equals(flag_valida)) { %>
          <td class="int">Documento<br>Validato</td>
        <% } %>
        <td class="int" >Azioni</td>
      </tr>
    </div>
    
    <%
    Iterator itx = provvedimenti.iterator();
    while ( itx.hasNext())
    {
      EventoModel lProv = (EventoModel)itx.next();
      
      String modificabile = "S";
      
      if (   "01".equals (fascicoloSiusGP.getFascicoloSiusModel().getCodStatoFascicolo())
          || "05".equals (fascicoloSiusGP.getFascicoloSiusModel().getCodStatoFascicolo())
          || !"S".equals (lProv.getFlagDocumentoRegistrato())
         )
      {
        modificabile = "N";
      }
      
      
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

        <% if (flag_valida.equals("SI")) { %>
        <td class="c">
          <% if ("S".equals(lProv.getFlagDocumentoRegistrato())) { %>
          <img src="/images/TickRed.gif">
          <% } else {%>
          &nbsp;
          <% } %>
        </td>
        <% } %>
        
        <%
        //
        String numeroOpposizioni = "0";
        Vector lListaOpposizioni = (Vector)opposizioni.get(lProv.getIdEvento());
        if (lListaOpposizioni!=null)
          numeroOpposizioni = Integer.toString(lListaOpposizioni.size());
           
        %>
        <td class="c" >
          <jsp:include page="<%=ICostantiImpugnazione.PG_BUTTONS_OPPOSIZIONE%>">
            <jsp:param name="CampoIdEntita"        value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
            <jsp:param name="ValoreIdEntita"       value="<%=lProv.getIdEvento()%>" />
            <jsp:param name="CodTipoProvvedimento" value="<%=lProv.getCodTipoProvvedimento()%>" />
            <jsp:param name="CodMotivo"            value="<%=lProv.getCodMotivo()%>" />
            <jsp:param name="numeroOpposizioni"    value="<%=numeroOpposizioni%>" />
            <jsp:param name="Modificabile"         value="<%=modificabile%>" />
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
  }  // endif provvedimentiDepositati
%>
  </body>
</html>