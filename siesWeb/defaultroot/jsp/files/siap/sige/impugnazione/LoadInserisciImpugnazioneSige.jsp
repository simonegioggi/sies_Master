<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige" %>
<%@ page import="siap.sige.impugnazione.action.ICostantiImpugnazioneSige"%>
<%@ page import="siap.sige.impugnazione.model.ImpugnazioneSigeModel"%>
<jsp:useBean id="impugnazione" scope="request" class="siap.sige.impugnazione.model.ImpugnazioneSigeModel"/>
<jsp:useBean id="provvedimento" scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"/>
<jsp:useBean id="codTipoImpugnazione" scope="session" class="java.lang.String"/>
<jsp:useBean id="soggettoImpugnante" scope="request" class="java.lang.String"/>
<jsp:useBean id="ListaUffici" scope="request" class="java.lang.String"/>

<%
  String lTitolo = "Ricorso";
  if (codTipoImpugnazione.equals(ICostantiImpugnazioneSige.COD_TIPO_OPPOSIZIONE))
      lTitolo = "Opposizione";
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
      // Controllo della data atto.
      var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
      var data_atto=document.LoadInserisciImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_RICORSO%>.value+'/'+document.LoadInserisciImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_MESE_DATA_RICORSO%>.value+'/'+document.LoadInserisciImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_RICORSO%>.value;
      if (! ControllaData(data_atto))
      {
        alert('Data atto non valida');
        return false;
      }

      
      // Controllo della data arrivo in cancelleria.
      var data_canc=document.LoadInserisciImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>.value+'/'+document.LoadInserisciImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>.value+'/'+document.LoadInserisciImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>.value;
       if (! ControllaData(data_canc)) {
           alert('Data arrivo in cancelleria non valida');
           return false;
       }
       // Si Controlla che: data atto <= data arrivo in cancelleria.
       if (data_canc != '//' && (! CompareDate(data_atto, data_canc)) ) {
           alert('Data atto > della Data arrivo in cancelleria');
           return false;
       }
        
      
       // Controllo della data arrivo in cancelleria <= data di sistema.
       if (! CompareDate(data_canc, data_sistema)) {
          alert('Data arrivo in cancelleria > della data odierna');
          return false;
       }
       
      // Si Controlla che: data emissione <= data atto.
       var data_emissione='<%=DateUtils.getDateToString(provvedimento.getProvvedimento().getDataEmissione(), "dd/MM/yyyy" )%>';
       if (! CompareDate(data_emissione, data_atto))
       {
         alert('Data Emissione Provvedimento > della Data atto');
         return false;
       }
       return true;
    }

    function calendario(a_formname,a_field_year,a_field_month,a_field_day)
    {
      desktop = 
          window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
    }
    </script>
  </head>
  


  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=LBG><font class="label">Funzione:</font> <font class="campo"><%=lTitolo%>&nbsp; </td>
  <!-- BOTTONE DI RITORNO -->
  <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>

      </tr>
    </table>
  <br />
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadInserisciImpugnazioneSige'>
    <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
    <br />

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="label">Avverso il Provvedimento : </td>
      </tr>

      <tr>
        <td> <font class="campo"><%=provvedimento.getProvvedimento().getDescrTipoProvvedimento()%> N. <%=(provvedimento.getProvvedimento().getChiaveAnno()==null?"-": provvedimento.getProvvedimento().getChiaveAnno())%>/<%=(provvedimento.getProvvedimento().getChiaveProgr()==null?"-": provvedimento.getProvvedimento().getChiaveProgr())%> </font> <font class="Label"> del </font> <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getProvvedimento().getDataEmissione(),"dd/MM/yyyy"))%> </font> <font class="Label"> depositato il </font> <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getProvvedimento().getDataDeposito(),"dd/MM/yyyy"),"-")%> </font> </td>    
      </tr>

    </table>
    <br />

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l">Presentato da </td>
        <td class="L">

          <select title="Soggetto Impugnante" class=small name="<%=ICostantiImpugnazioneSige.CAMPO_SOGGETTO_IMPUGNANTE%>" >
            <%= soggettoImpugnante %>
          </select>

        </td>
      </tr>

      <tr>
        <td class="l">Data atto<font class="ob">(*)</font></td>
        <td class="L">
          <input type="text" name="<%= ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_RICORSO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" />
          /
          <input type="text" name="<%= ICostantiImpugnazioneSige.CAMPO_MESE_DATA_RICORSO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" />
          /
          <input type="text" name="<%= ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_RICORSO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" />
		
		  <!-- MEV 15 - Revisione SIGE -->
		  <a href="javascript:calendario('LoadInserisciImpugnazioneSige','<%=ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_RICORSO%>','<%=ICostantiImpugnazioneSige.CAMPO_MESE_DATA_RICORSO%>','<%=ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_RICORSO%>');">
      	      <img src="/images/calendario.gif" border=0>
       	  </a>
        </td>

      </tr>

      <tr>
        <td class="l">Data arrivo in cancelleria<font class="ob">(*)</font></td>
        <td class="L">
          <input type="text" name="<%= ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" />
          /
          <input type="text" name="<%= ICostantiImpugnazioneSige.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" />
          /
          <input type="text" name="<%= ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" />
		  
		  <!-- MEV 15 - Revisione SIGE -->
		  <a href="javascript:calendario('LoadInserisciImpugnazioneSige','<%=ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>','<%=ICostantiImpugnazioneSige.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>','<%=ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>');">
      	      <img src="/images/calendario.gif" border=0>
       	  </a>

        </td>
      </tr>
<%
if (codTipoImpugnazione.equals(ICostantiImpugnazioneSige.COD_TIPO_RICORSO)) { 
%>            
      <tr>
        <td class="l">Data Trasmissione Atti </td>
        <td class="L">
          <input type="text" name="<%= ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          /
          <input type="text" name="<%= ICostantiImpugnazioneSige.CAMPO_MESE_DATA_TRASMISSIONE_ATTI %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          /
          <input type="text" name="<%= ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
		  
		  <!-- MEV 15 - Revisione SIGE -->
		  <a href="javascript:calendario('LoadInserisciImpugnazioneSige','<%=ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>','<%=ICostantiImpugnazioneSige.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>','<%=ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>');">
      	      <img src="/images/calendario.gif" border=0>
       	  </a>

        </td>
      </tr>
      
      <tr>
        <td class="l">Autorità Destinataria </td>
        <td class="L">
         <select title="Autorità Destinataria" class=small name="<%=ICostantiImpugnazioneSige.CAMPO_COD_AUTORITA_DESTINATARIA %>" >
            <%=ListaUffici %>
          </select>
        </td>
      </tr>
<%
}
%>
      <tr>
        <td class="l">Note</td>
        <td class="l">          
          <Textarea Title="Note" name="<%= ICostantiImpugnazioneSige.CAMPO_NOTE %>" cols=80 rows=5><%=StringUtils.toStringJSP(impugnazione.getAnnotazione()) %></textarea>
        </td>
      </tr>

    </table>

    <br><br>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td>
          <%--input onclick="Javascript:return Verify();" class="bottone" type="submit" value="Conferma"--%>
          <input class="bottone" type="submit" value="Conferma">
        </td>
      </tr>
    </table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.impugnazione.action.ActInserisciImpugnazioneSige" />
    <input type="HIDDEN" name="<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>" value="<%=provvedimento.getProvvedimento().getIdProvvedimentoSige()%>" />
    <input type="HIDDEN" name="<%=ICostantiProvvedimentoSige.CAMPO_COD_TIPO_PROVVEDIMENTO%>" value="<%=provvedimento.getProvvedimento().getCodTipoProvvedimento()%>" />
    <input type="HIDDEN" name="<%=ICostantiImpugnazioneSige.CAMPO_COD_TIPO_IMPUGNAZIONE%>" value="<%=codTipoImpugnazione%>" >
  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadInserisciImpugnazioneSige");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_RICORSO%>","req","Il campo Giorno della Data Atto è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_RICORSO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_RICORSO%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_RICORSO%>","lt=31");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_MESE_DATA_RICORSO%>","req","Il campo Mese della Data Atto è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_MESE_DATA_RICORSO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_MESE_DATA_RICORSO%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_MESE_DATA_RICORSO%>","lt=12");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_RICORSO%>","req","Il campo Anno della Data Atto è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_RICORSO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_RICORSO%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_RICORSO%>","lt=2999");

    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>","req","Il campo Giorno della Data Arrivo in Cancelleria è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>","lt=31");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>","req","Il campo Mese della Data Arrivo in Cancelleria è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>","lt=12");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>","req","Il campo Anno della Data Arrivo in Cancelleria è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>","lt=2999");

	<% if (codTipoImpugnazione.equals(ICostantiImpugnazioneSige.COD_TIPO_RICORSO)) { %>
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","numeric");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","lt=31");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","numeric");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","lt=12");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","numeric");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","lt=2999");
    <% } %>
    //Chiama la funzione di Verify().
    frmvalidator.setAddnlValidationFunction("Verify");

  </script>

  </body>
</html>