<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.evento.model.EventoFascicoloModel"%>

<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione"%>


<jsp:useBean id="aggregatofascicoliEventi" scope="request" class="java.util.Vector"/>

<html>
  <head>
    <title>[S.I.E.S.] - Ricerca Provvedimenti non validati </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">

function conferma(a_action, a_parameter, a_entityname ,a_parameter2 ,a_entityname2)
{
var documentoRegistrato = a_entityname2;
var nonValidati = 'S';
    if (documentoRegistrato=="S")
  {
       var  desktop = window.open("<%= IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=" + a_action + "&" + a_parameter + "=" +a_entityname, "Cancella_provvedimento","  top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
         window.parent.close();
   }else{
      str = "/jsp/Main.jsp?Action=siap.siep.ordineesecuzione.action.ActCancellaProvvedimento&" +a_parameter +"=" + a_entityname+"&nonValidati="+nonValidati;
        if (window.confirm('Confermi la cancellazione ?'))
        {
               window.location.href=str;
        }
    }
}
</SCRIPT>
  </head>
<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione : Elenco Provvedimenti Non Validati </font>&nbsp;&nbsp;

    </tr>
  </table>
<br>
<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
<br>
  <table>
    <tr>
      <td class="int">Numero SIEP</td>
      <td class="int">Cognome e Nome</td>
      <td class="int">Luogo e Data Nasicta</td>
      <td class="int">Descrizione Provvedimento</td>

      <td class="int">Azioni</td>
    </tr>
<%
  Iterator itx = aggregatofascicoliEventi.iterator();
  while ( itx.hasNext())
  {

EventoFascicoloModel lEveFasc = (EventoFascicoloModel)itx.next();
FascicoloSiepModel lFascMod = (FascicoloSiepModel)lEveFasc.getFascicoloSiep();
//EventoModel lEvento = (EventoModel)itx.next();
%>
    <tr>
      <td class="c"><font class="label"><%=lFascMod.getChiaveAnno()%>/<%=lFascMod.getChiaveProgr()%></font></td>
<%    if (lFascMod.getSoggetto() != null)
      { %>
        <td class="c"><font class="label"><%=lFascMod.getSoggetto().getCognome() +" " +lFascMod.getSoggetto().getNome()%></font></td>
        <td class="c"><font class="label"><%=lFascMod.getSoggetto().getDescrComuneNascita() +" " + DateUtils.getDateToString(lFascMod.getSoggetto().getDataNascita(),"dd-MM-yyyy")%></font></td>

<%    }
      else
      { %>
        <td class="c">&nbsp;</td>
        <td class="c">&nbsp;</td>

<%    } %>


<%for(int i=0;i<lEveFasc.getEventi().size();i++)
{
EventoModel lEve = (EventoModel)lEveFasc.getEventi().get(i);
%>

<td class="c"><font class="label"><%=lEve.getDescrMotivo()%></font></td>
<%
	String isBlob="SI";
				if(lEve.getFlagDocumentoRegistrato() == null)
        {
          isBlob="NO";
        }
        //Flag Documento Registrato
        String lDocReg = "N";
        if(  lEve.getFlagDocumentoRegistrato() != null
          && lEve.getFlagDocumentoRegistrato().equals("S"))
        {
          lDocReg = "S";
        }

if(  lEve.getFlagDocumentoRegistrato() != null
          && lEve.getFlagDocumentoRegistrato().equals("A"))
        {
          lDocReg = "A";
        }
if(  lEve.getCodOperatoreInserimento() != null
          && lEve.getCodOperatoreInserimento().startsWith("res-"))
        {

          lDocReg = "M";
        }

 String modificabile = "SI";
%>

<td class="c">
         <jsp:include page="<%=ICostantiOrdineEsecuzione.PG_BUTTONS%>">
          <jsp:param name="CampoIdEntita"  value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lEve.getIdEvento()%>" />
          <jsp:param name="CampoIdEntitaProvv" value="campo" />
          <jsp:param name="ValoreIdEntitaProvv" value="<%=lEve.getFlagDocumentoRegistrato()%>" />
          <jsp:param name="TipoProvvedimento" value="<%=lEve.getCodTipoProvvedimento()%>" />
          <jsp:param name="MotivoEvento" value="<%=lEve.getCodMotivo()%>" />
          <jsp:param name="TipoEvento" value="<%=lEve.getCodTipoEvento()%>" />
          <jsp:param name="modalita" value="R" />
          <jsp:param name="docRegistrato" value="<%=lDocReg%>" />
          <jsp:param name="Evento" value="<%=isBlob%>" />
          <jsp:param name="Modificabile" value="<%=modificabile%>" />
          <jsp:param name="EventoCancellareAnnullare" value="<%=lEve.getIdEvento()%>" />
        </jsp:include>
      </td>
    <tr>
<td >&nbsp;</td>
        <td >&nbsp;</td><td >&nbsp;</td>
<%}%>
</tr><tr><td>&nbsp;<td></tr>
<%
  }
%>

    </table>
  </body>
</html>