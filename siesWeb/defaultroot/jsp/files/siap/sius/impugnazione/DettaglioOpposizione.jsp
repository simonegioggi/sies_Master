<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>


<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.sico.evento.model.EventoModel" %>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.impugnazione.model.ImpugnazioneModel"%>
<%@ page import="siap.sius.impugnazione.action.ICostantiImpugnazione"%>

<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />

<jsp:useBean id="provvedimento"   scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="impugnazione"    scope="request" class="siap.sius.impugnazione.model.ImpugnazioneModel"/>

<jsp:useBean id="numOrdDec"     scope="request" class="java.lang.String"/>
<jsp:useBean id="dataOrdDec"    scope="request" class="java.lang.String"/>
<jsp:useBean id="dataDeposito"  scope="request" class="java.lang.String"/>


<jsp:useBean id="ElencoTemplate"     scope="request" class="java.lang.String"/>


<html>
  <head>
    <title>[S.I.E.S.] - Opposizione</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

 <script language="JavaScript">
   function stampaImpugnazione()
   {
     var template = "&CodTemplate=";
     var  hrefStampa = "<%=IWebConstants.ACTION_FIELD%>=siap.sius.impugnazione.action.ActStampaImpugnazione&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=provvedimento.getIdEvento()%>&IdImpugnazione=<%=impugnazione.getIdImpugnazione()%>&IdFascicoloSius=<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius().toString()%>";

     // Se nel dettaglio esiste la Lista di Template si legge il valore
     if (document.DettaglioImpugnazione.<%=ICostantiImpugnazione.CAMPO_COD_TEMPLATE%> != undefined)
     {
        template =  template + document.DettaglioImpugnazione.<%=ICostantiImpugnazione.CAMPO_COD_TEMPLATE%>.value;
        hrefStampa = hrefStampa + template;
     }

     stampa2("<%=ISIAPCostantiWeb.PG_STAMPA%>",  hrefStampa);
   }
 </script>


</head>

<body class="corpo">
  <FORM name='DettaglioImpugnazione'>

  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione : </font>&nbsp;
        <font class="campo">Dettaglio Opposizione</font>
      </td>

      <!-- BOTTONE DI STAMPA -->
      <td class="LBG">
        <a href="Javascript:stampaImpugnazione()" >
          <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa Opposizione" width="24" height="24" border="0">
        </a>
      </td>

        <!-- BOTTONE DI TRASMISSIONE ( 28/01/2008 Solo se non annullato)-->
        <% if(impugnazione.getDataAnnullamento() == null && impugnazione.getFlagAnnullamento() == null && impugnazione.getFlagAnnullamento() != ("S"))
        {%>
        <td class="LBG">
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.impugnazione.action.ActLoadTrasmissioneImpugnazione&IdEvento=<%=provvedimento.getIdEvento()%>&IdImpugnazione=<%=impugnazione.getIdImpugnazione()%>&<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>=<%=provvedimento.getCodTipoProvvedimento()%>" >
            <img  align="middle" src="/images/net24.gif" alt="Trasmissione Opposizione" width="24" height="24" border="0">
          </a>
        </td>
      <%}%>

        <!-- BOTTONE DI RITORNO -->
        <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      </tr>
    </table>
    <br>

    <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    <br>

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="label">Avverso il Provvedimento : </td>
      </tr>

      <tr>
        <% if (numOrdDec.compareTo("")==0 ) { %>
        <td class="campo"><%=provvedimento.getDescrTipoProvvedimento() +" di "+ provvedimento.getDescrMotivo()+" del "+ DateUtils.getDateToString ( provvedimento.getDataEmissione(), "dd/MM/yyyy" ) %></td>
        <%} else {%>
        <td> <font class="campo"><%=provvedimento.getDescrTipoProvvedimento()%> N. <%=numOrdDec%> </font> <font class="Label"> del </font> <font class="campo"> <%=dataOrdDec%> </font> <font class="Label"> depositato il </font> <font class="campo"> <%=dataDeposito%> </font> </td>
        <%}%>
      </tr>
    </table>
      
    <br>

    <table cellspacing=2 cellpadding=2>
      <% if(impugnazione.getDataAnnullamento() != null && impugnazione.getFlagAnnullamento() != null && impugnazione.getFlagAnnullamento().equalsIgnoreCase("S")) { %>
      <tr>
        <td class="l"><font class="cRosso"><%=impugnazione.getDescrTipoImpugnazione() + " "%> ANNULLATA</font></td>
      </tr>
      <tr>
        <td class="l"> Data di annullamento </td>
        <td class="L"><%=DateUtils.getDateToString(impugnazione.getDataAnnullamento(),"dd-MM-yyyy")%></td>
      </tr>
      <tr>
        <td class="l">Motivo annullamento</td>
        <td class="l"><%=(impugnazione.getMotivoAnnullamento() == null ) ? "-" : impugnazione.getMotivoAnnullamento()%>
        </td>
      </tr>
      <% } %>
      
      <tr>
        <td class="l">Anno/Numero</td>
        <td class="L"><%=impugnazione.getAnnoS7()%>/<%=impugnazione.getProgrS7()%></td>
      </tr>

      <tr>
        <td class="l">Tipo </td>
        <td class="L"><%=impugnazione.getDescrTipoImpugnazione()%>
        </td>
      </tr>

      <tr>
        <td class="l">Presentato da </td>
        <td class="L"><%=impugnazione.getDescrSoggettoImpugnante()%>
        </td>
      </tr>

      <tr>
        <td class="l">Data atto </td>
        <td class="L"><%=DateUtils.getDateToString(impugnazione.getDataRicorso(),"dd-MM-yyyy")%>
        </td>
      </tr>

      <tr>
        <td class="l">Data arrivo in cancelleria </td>
        <td class="L"><%=DateUtils.getDateToString(impugnazione.getDataArrivoCancelleria(),"dd-MM-yyyy")%>
      </tr>

      <tr>
        <td class="l">Data decisione </td>
        <td class="L"><%=DateUtils.getDateToString(impugnazione.getDataDecisione(),"dd-MM-yyyy") == null ? "-" : DateUtils.getDateToString(impugnazione.getDataDecisione(),"dd-MM-yyyy")%>
        </td>
      </tr>

      <tr>
        <td class="l">Tenore decisione </td>
        <td class="L"><%=impugnazione.getDescrTenoreDecisione() %>
        </td>
      </tr>

      <tr>
        <td class="l">Data restituzione atti </td>
        <td class="L"><%=DateUtils.getDateToString(impugnazione.getDataRestituzioneAtti(),"dd-MM-yyyy") == null ? "-" : DateUtils.getDateToString(impugnazione.getDataRestituzioneAtti(),"dd-MM-yyyy")%>
        </td>
      </tr>

      <tr>
        <td class="l">Note</td>
        <td class="l"><%=(impugnazione.getAnnotazione() == null || impugnazione.getAnnotazione().compareTo("null")==0) ? "-" : impugnazione.getAnnotazione()%>
        </td>
      </tr>

      <tr>
        <td class="L">Documento da stampare </td>
        <td class="L">
          <select title="Documento da stampare" class=small name="<%=ICostantiImpugnazione.CAMPO_COD_TEMPLATE%>">
            <%= ElencoTemplate %>
          </select>
        </td>
      </tr>


    </table>

  </form>
  </body>
</html>