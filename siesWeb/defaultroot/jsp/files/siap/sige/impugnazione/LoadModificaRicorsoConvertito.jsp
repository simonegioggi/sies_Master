<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>
<%@page import="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"%>
<%@page import="f3b.web.IWebConstants"%>
<%@page import="f3b.util.DateUtils"%>
<%@page import="f3b.util.StringUtils" %>
<%@page import="siap.sige.fascicolo.action.ICostantiFascicoloSige" %>
<%@page import="siap.sige.impugnazione.action.ICostantiImpugnazioneSige"%>
<%@page import="siap.sige.impugnazione.model.ImpugnazioneSigeModel"%>

<jsp:useBean id="impugnazione"  scope="request" class="siap.sige.impugnazione.model.ImpugnazioneSigeModel"/>
<jsp:useBean id="ListaUffici"   scope="request" class="java.lang.String"/>
<jsp:useBean id="ProvenienteDa" scope="request" class="java.lang.String"/>

<%
ProvvedimentoSigeEventoModel provvedimento = impugnazione.getProvvedimentoSige();
%>
<html>
  <head>
    <script language="JavaScript1.2">
    </script>
    <title>[S.I.E.S.] - Ricorso  / Impugnazione</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript">
    function Verify() {
      
      var data_trasmissione=document.LoadModificaImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI %>.value+'/'+document.LoadModificaImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_MESE_DATA_TRASMISSIONE_ATTI %>.value+'/'+document.LoadModificaImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI %>.value;
      var data_canc=<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataArrivoCancelleria() ,"dd/MM/yyyy"))%>
      var data_atti=<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataRicorso() ,"dd/MM/yyyy"))%>
  
      if (data_trasmissione != '//' && !ControllaData(data_trasmissione)) {
        alert('Data trasmissione atti non valida');
        return false;
      }
     
      // Si Controlla che: data atto <= data arrivo in cancelleria.
      if (! CompareDate(data_canc, data_trasmissione)) {
          alert('Data Trasmissione atti < della Data arrivo in cancelleria');
          return false;
      }
     
      if (! CompareDate(data_atti, data_trasmissione)) {
          alert('Data Trasmissione atti < della Data arrivo in cancelleria');
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
        <td class=LBG><font class="label">Funzione :</font> <font class="campo">Iscrizione Ricorso</font>&nbsp;</td>
        <!-- BOTTONE DI RITORNO -->
        <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      </tr>
    </table>
  <br />

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadModificaImpugnazioneSige'>

    <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
    <br />

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="label">Avverso il Provvedimento : </td>
      </tr>
      <tr>
        <td> <font class="campo"><%=provvedimento.getProvvedimento().getDescrTipoProvvedimento()%> N. <%=provvedimento.getProvvedimento().getChiaveAnno() %>/<%=provvedimento.getProvvedimento().getChiaveProgr() %> </font> <font class="Label"> del </font> <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getProvvedimento().getDataEmissione(),"dd/MM/yyyy"))%> </font> <font class="Label"> depositato il </font> <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getProvvedimento().getDataDeposito(),"dd/MM/yyyy"))%> </font> </td>    
      </tr>
    </table>
    <br />
    
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l">Anno/Numero</td>
        <td class="L"><%=impugnazione.getAnnoS7()%>/<%=impugnazione.getProgrS7()%>
        </td>
      </tr>
      
       <!-- @emma10/07/2018 intervento post COLLAUDO 11.2 (aggiungo la label tipo come scritto su AF) -->
      <tr>
        <td class="l">Tipo</td>
        <td class="L">Opposizione</td>
      </tr>

      <tr>
        <td class="l">Presentato da </td>
        <td class="L">
          <%=impugnazione.getDescrSoggettoImpugnante() %>
        </td>
      </tr>

      <tr>
        <td class="l">Data atto </td>
        <td class="L">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataRicorso() ,"dd-MM-yyyy"))%>
        </td>
      </tr>

      <tr>
        <td class="l">Data arrivo in cancelleria </td>
        <td class="L">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataArrivoCancelleria() ,"dd-MM-yyyy"))%>
        </td>
      </tr>
      
       <!-- @emma10/07/2018 intervento post COLLAUDO 11.2 (aggiungo la label tipo come scritto su AF) -->
		<% if (impugnazione.getDataDecisione() != null){ %>
	      <tr>
	        <td class="l">Data decisione </td>
	        <td class="L"><%=DateUtils.getDateToString(impugnazione.getDataDecisione(),"dd-MM-yyyy") == null ? "-" : DateUtils.getDateToString(impugnazione.getDataDecisione(),"dd-MM-yyyy")%>
	        </td>
	      </tr>
		<% } 
	
	 	  if (impugnazione.getDescrTenoreDecisione() != null){ %> 
	      <tr>
	        <td class="l">Tenore decisione </td>
	        <td class="L"><%=(impugnazione.getDescrTenoreDecisione()==null?"-":impugnazione.getDescrTenoreDecisione()) %>
	        </td>
	      </tr>	     
		<% } %> 
              
      <tr>
        <td class="l">Data Trasmissione Atti </td>
        <td class="L">
          <input type="text" name="<%=ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataTrasmissioneAtti() ,"dd"))%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" />
          /
          <input type="text" name="<%=ICostantiImpugnazioneSige.CAMPO_MESE_DATA_TRASMISSIONE_ATTI %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataTrasmissioneAtti() ,"MM"))%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" />
          /
          <input type="text" name="<%=ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataTrasmissioneAtti() ,"yyyy"))%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" />
		  <!-- MEV 15 - Revisione SIGE -->
		  <a href="javascript:calendario('LoadModificaImpugnazioneSige','<%=ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>','<%=ICostantiImpugnazioneSige.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>','<%=ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>');">
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

      <tr>
        <td class="l">Note</td>
        <td class="l">
          <Textarea Title="Note" name="<%= ICostantiImpugnazioneSige.CAMPO_NOTE %>" cols=80 rows=5><%=StringUtils.toStringJSP(impugnazione.getAnnotazione()) %></textarea>
        </td>
      </tr>
    </table>

    <br /><br />
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td>
          <input onclick="Javascript:return Verify();" class="bottone" type="submit" value="Conferma">
        </td>
      </tr>
    </table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.impugnazione.action.ActInserisciImpugnazioneSige" />
    <input type="HIDDEN" name="<%=ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE%>" value="<%=impugnazione.getIdImpugnazioneSige().toString()%>" />
    <%--<input type="HIDDEN" name="<%=ICostantiImpugnazioneSige.CAMPO_SOGGETTO_IMPUGNANTE%>" value="<%=impugnazione.getSoggettoImpugnante() %>" /> --%>
    <!-- Valorizzo la Data Ricorso con la Data Decisione impostata in fase di "Iscrizione Ricorso"-->
    <input type="HIDDEN" name="<%=ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_RICORSO%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataDecisione() ,"yyyy"))%>" />
    <input type="HIDDEN" name="<%=ICostantiImpugnazioneSige.CAMPO_MESE_DATA_RICORSO%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataDecisione() ,"MM"))%>" />
    <input type="HIDDEN" name="<%=ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_RICORSO%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataDecisione() ,"dd"))%>" />
    
    <input type="HIDDEN" name="<%=ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>" value=" <%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataArrivoCancelleria() ,"yyyy"))%>" />
    <input type="HIDDEN" name="<%=ICostantiImpugnazioneSige.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>" value=" <%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataArrivoCancelleria() ,"MM"))%>" />
    <input type="HIDDEN" name="<%=ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>" value=" <%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataArrivoCancelleria() ,"dd"))%>" />
    
    <input type="HIDDEN" name="<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>" value="<%=impugnazione.getProvvIdProvvedimentoSige().toString() %>" />
    <input type="HIDDEN" name="<%=ICostantiImpugnazioneSige.CAMPO_COD_TIPO_IMPUGNAZIONE%>" value="01" />
    
    <!-- Visto che siamo nel caso di un Opposizione convertita in Ricorso, valorizzo il campo 
    	 Soggetto Impugnante con la descrizione "Opposizione N. annoOpp/numOpp", tale descrizione
    	 verrà visualizzata nella pagina di dettaglio del Ricorso, mentre non dovranno essere 
    	 visualizzati i campi Data Atto, Data Arrivo in Cancelleria e Presentato Da -->
    <input type="hidden" name="<%=ICostantiImpugnazioneSige.CAMPO_SOGGETTO_IMPUGNANTE%>" value="<%=ProvenienteDa%>" />
    
  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadModificaImpugnazioneSige");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","numeric");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","lt=31");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","numeric");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","lt=12");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","numeric");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","lt=2999");
  </script>
  </body>
</html>