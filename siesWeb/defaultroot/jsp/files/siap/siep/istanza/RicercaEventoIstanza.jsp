<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.istanza.action.ICostantiIstanza"%>
<%@ page import="siap.siep.istanza.model.IstanzaSoggettoEventoFascicoloSiepModel"%>
<%@ page import="siap.siep.istanza.model.IstanzaModel"%>

<jsp:useBean id="istanzeeventi" scope="request" class="java.util.Vector"/>


<html>
  <head>
    <title>[S.I.E.S.] - Ricerca Istanza </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
// Richiamo della finestra di pop-up per inserire motivazione Annullamento
function conferma(a_action, a_entityname1, a_entityvalue1 ,a_entityname2 ,a_entityvalue2)
{
   if (window.confirm("Confermi l'annullamento?"))
   {
       var  desktop = window.open("/jsp/Main.jsp?Action=" + a_action + "&" + a_entityname1 + "=" +a_entityvalue1 + "&" + a_entityname2 + "=" +a_entityvalue2 +"&<%=IWebConstants.GOTO_PAGE%>=<%=(String)request.getAttribute(IWebConstants.GOTO_PAGE)%>", "Annulla"," top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
       window.parent.close();
    }
}

function chiama(idIstanza)
{
    window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istanza.action.ActLoadAnnullaIstanza&<%=ICostantiIstanza.CAMPO_ID_ISTANZA%>="+idIstanza,"Annulla_Istanza", "top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
}
</script>
</head>
<body class="corpo">
 <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione : Ricerca Istanza</font>&nbsp;&nbsp;
     </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <div align=center>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="int">Data Emissione</td>
      <td class="int">Autorità</td>
      <td class="int">Descrizione Provvedimento</td>
      <td class="int">Documento<br>Validato</td>
 	    <td class="int">Azioni</td>
    </tr>
<%
  //Iterator itx = eventi.iterator();
  Iterator itx = istanzeeventi.iterator();
  while ( itx.hasNext())
  {
    //EventoModel lEvento = (EventoModel)itx.next();
    IstanzaSoggettoEventoFascicoloSiepModel lIstanzaEvento = (IstanzaSoggettoEventoFascicoloSiepModel)itx.next();
    EventoModel lEvento = lIstanzaEvento.getEvento();
    IstanzaModel lIstanza = lIstanzaEvento.getIstanza();
%>
    <tr>
      <td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEvento.getDataEmissione(),"dd-MM-yyyy"))%></td>
      <td class="l"><%=lEvento.getDescrUfficioEmittente()+ " " + lEvento.getDescrLuogoEmittente()  %></td>
      <td class="l"><%=StringUtils.toStringJSP(lEvento.getDescrMotivo(),"-")%></td>
      <td class="c">
<%
        String lAnnullato = "N";
        if (lEvento.getFlagDocumentoRegistrato()!=null){if (lEvento.getFlagDocumentoRegistrato().compareTo("S")==0)
        {
%>
          <img src="/images/TickRed.gif">
<%
        }else if(lEvento.getFlagDocumentoRegistrato().compareTo("A")==0)
        {
          lAnnullato = "S";
%>
             <a class="cliccabile" href="javascript:chiama('<%=lIstanza.getIdIstanza()%>');" title="ANNULLAMENTO">
             <font class="cRosso">ANNULLATO</font></a>
<%
         }
  }
%>
      &nbsp;
      </td>
      <td class="c">
<%
        String isBlob="SI";
        if(lEvento.getFlagDocumentoRegistrato() == null)
        {
          isBlob="NO";
        }
%>
        <jsp:include page="<%=ICostantiIstanza.PG_BUTTONS_ISTANZA%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lEvento.getIdEvento()%>" />
          <jsp:param name="MotivoEvento" value="<%=lEvento.getCodMotivo()%>" />
          <jsp:param name="modalita" value="R" />
          <jsp:param name="Evento" value="<%=isBlob%>" />
          <jsp:param name="CampoIdEntitaIstanza" value="<%=ICostantiIstanza.CAMPO_ID_ISTANZA%>" />
          <jsp:param name="ValoreIdEntitaIstanza" value="<%=lIstanza.getIdIstanza()%>" />
           <jsp:param name="annullato" value="<%=lAnnullato%>" />
        </jsp:include>
      </td>
    </tr>
<%
  }
%>
    </table>
  </div>
	</body>
</html>