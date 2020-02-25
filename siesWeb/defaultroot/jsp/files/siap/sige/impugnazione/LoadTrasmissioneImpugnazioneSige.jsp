<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige" %>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"%>
<%@ page import="siap.sige.impugnazione.action.ICostantiImpugnazioneSige"%>
<%@ page import="siap.sige.impugnazione.model.ImpugnazioneSigeModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>

<jsp:useBean id="FascicoloSigeEsteso" scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" />
<jsp:useBean id="impugnazione" scope="request" class="siap.sige.impugnazione.model.ImpugnazioneSigeModel"/>
<jsp:useBean id="provvedimento" scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeModel"/>
<jsp:useBean id="uffici" scope="request" class="java.lang.String"/>
<jsp:useBean id="IdEvento" scope="request" class="java.lang.String"/>
<jsp:useBean id="postTitle" scope="request" class="java.lang.String"/>
<jsp:useBean id="codTipoImpugnazione" scope="request" class="java.lang.String"/>
<%
	String lAction = new String();
	lAction = "siap.sige.impugnazione.action.ActConfermaTrasmissioneImpugnazioneSige";
	
	String lTitolo = new String();
	String lTipoImpugnazione = "";
	if (codTipoImpugnazione.compareTo(ICostantiImpugnazioneSige.COD_TIPO_OPPOSIZIONE)==0){
		  lTitolo = "Trasferimento Provvedimento Opposizione";
		  lTipoImpugnazione = "Opposizione";
	} else {
		  lTitolo = "Trasferimento Provvedimento Ricorso";
	      lTipoImpugnazione = "Ricorso";
	}
%>

<html>
  <head>
    <script language="JavaScript1.2">
    </script>
    <title>[S.I.E.S.] - Impugnazione / Ricorso</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>

    <script language="JavaScript">
    function Verify()
    {
        var destinatario = document.LoadTrasmettiImpugnazioneSige.<%= ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>[document.LoadTrasmettiImpugnazioneSige.<%= ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>.selectedIndex].value;
        if(destinatario == '-')
        {
          alert("Il Campo Destinatario è obbligatorio");
          return false;
        }
        if(document.LoadTrasmettiImpugnazioneSige.<%= ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO%>.value == "")
        {
          alert("Il Campo Sede Destinatario è obbligatorio");
          return false;
        }

       return true;
    }

    // Lista Uffici per TIPO_UFFICIO
    function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    </script>
  </head>

  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=LBG><font class="label">Funzione:</font> <font class="campo"><%=lTitolo%></font>&nbsp;

  <!-- BOTTONE DI RITORNO -->
  <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>

      </tr>
    </table>
  <br />


  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadTrasmettiImpugnazioneSige'>

    <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
    <br>

    <table cellspacing=2 cellpadding=2>
      <tr>
<%
      if (provvedimento.getDataDeposito() == null)
      {
%>
        	<td class="campo"><%=provvedimento.getDescrTipoProvvedimento() +" di "+ provvedimento.getDescrTipoProvvedimentoSige() +" del "+ DateUtils.getDateToString ( provvedimento.getDataEmissione(), "dd/MM/yyyy" ) %> depositato il <%=DateUtils.getDateToString ( provvedimento.getDataEmissione(), "dd/MM/yyyy" ) %></td>
    <%} else {%>
        	<td> <font class="campo"><%=provvedimento.getDescrTipoProvvedimento()%> N. <%=provvedimento.getChiaveAnno() %>/<%=provvedimento.getChiaveProgr() %></font> <font class="Label"> del </font> <font class="campo"> <%=DateUtils.getDateToString ( provvedimento.getDataEmissione(), "dd/MM/yyyy" )%> </font> <font class="Label"> depositato il </font> <font class="campo"> <%=DateUtils.getDateToString ( provvedimento.getDataEmissione(), "dd/MM/yyyy" )%> </font> </td>
    <%}%>
      </tr>
    </table>
    
    <table cellspacing=2 cellpadding=2> 
      <tr>
            <td class="l">Anno/Numero</td>
            <td class="L"><%=impugnazione.getAnnoS7()%>/<%=impugnazione.getProgrS7()%></td>
      </tr>
      <tr>
            <td class="l">Tipo</td>
            <td class="L"><%=lTipoImpugnazione%></td>
      </tr>
    </table>

    <br />

    <table cellspacing=2 cellpadding=2>
       <tr>
        <td class="l">Destinatario </td>
        <td class="L">
         <select Title="Destinatario" name="<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>" >
           <%=uffici%>
         </select>
        </td>
      </tr>
      <tr>
        <td class="l">Sede Destinatario </td>
        <td class="L">
          <input title="Sede Destinatario" type="text" name="<%= ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO %>"  maxlength="35" size="35">
             <a href="Javascript:ListaUfficiPerTipo('LoadTrasmettiImpugnazioneSige','<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO%>',document.LoadTrasmettiImpugnazioneSige.<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>[document.LoadTrasmettiImpugnazioneSige.<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>.selectedIndex].value);">
                <img src="/images/filefolder.gif" border=0>
             </a>
        </td>
      </tr>
    </table>

    <br /><br />
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td>
          <input class="bottone" type="submit" value="Conferma">
        </td>
      </tr>
    </table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
    <input type="HIDDEN" name="<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>" value="<%=provvedimento.getIdProvvedimentoSige()%>" >
    <input type="HIDDEN" name="<%=ICostantiProvvedimentoSige.CAMPO_COD_TIPO_PROVVEDIMENTO%>" value="<%=provvedimento.getCodTipoProvvedimento()%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>" value="<%=FascicoloSigeEsteso.getFascicoloSige().getIdFascicoloSige()%>" >
    <input type="HIDDEN" name="<%=ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE%>" value="<%=impugnazione.getIdImpugnazioneSige().toString()%>" >
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=IdEvento%>">
  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadTrasmettiImpugnazioneSige");
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
  </body>
</html>