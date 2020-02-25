<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.model.FunzioneModel" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>

<%@ page import="siap.sius.impugnazione.action.ICostantiImpugnazione"%>
<%@ page import="siap.sius.impugnazione.model.ImpugnazioneModel"%>

<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="impugnazione" scope="request" class="siap.sius.impugnazione.model.ImpugnazioneModel"/>
<jsp:useBean id="provvedimento" scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="tipoRicorso" scope="request" class="java.lang.String"/>
<jsp:useBean id="soggettoImpugnante" scope="request" class="java.lang.String"/>
<jsp:useBean id="tenoreDecisioneRicorso" scope="request" class="java.lang.String"/>
<jsp:useBean id="ListaUffici" scope="request" class="java.lang.String"/>
<jsp:useBean id="numOrdDec" scope="request" class="java.lang.String"/>
<jsp:useBean id="dataOrdDec" scope="request" class="java.lang.String"/>
<jsp:useBean id="dataDeposito" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficio" scope="request" class="java.lang.String"/>

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
    function Verify()
    {
      // Controllo della data atto.
      var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
      var data_atto=document.LoadModificaImpugnazione.<%=ICostantiImpugnazione.CAMPO_GIORNO_DATA_RICORSO%>.value+'/'+document.LoadModificaImpugnazione.<%=ICostantiImpugnazione.CAMPO_MESE_DATA_RICORSO%>.value+'/'+document.LoadModificaImpugnazione.<%=ICostantiImpugnazione.CAMPO_ANNO_DATA_RICORSO%>.value;
      if (! ControllaData(data_atto))
      {
        alert('Data atto non valida');
        return false;
      }
      // Si Controlla che: data emissione <= data atto.
      var data_emissione='<%=DateUtils.getDateToString(provvedimento.getDataEmissione(), "dd/MM/yyyy" )%>';
      if (! CompareDate(data_emissione, data_atto))
      {
        alert('Data Emissione Provvedimento > della Data atto');
        return false;
      }

      // Controllo della data arrivo in cancelleria.
      var data_canc=document.LoadModificaImpugnazione.<%=ICostantiImpugnazione.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>.value+'/'+document.LoadModificaImpugnazione.<%=ICostantiImpugnazione.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>.value+'/'+document.LoadModificaImpugnazione.<%=ICostantiImpugnazione.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>.value;      if (! ControllaData(data_canc))
      {
        alert('Data arrivo in cancelleria non valida');
        return false;
      }

      // Si Controlla che: data atto <= data arrivo in cancelleria.
      if (! CompareDate(data_atto, data_canc))
      {
        alert('Data atto > della Data arrivo in cancelleria');
        return false;
      }

      // Si Controlla che: data arrivo in cancelleria <= data Trasmissione atti.
      //var data_trasm=document.LoadModificaImpugnazione.<%=ICostantiImpugnazione.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value+'/'+document.LoadModificaImpugnazione.<%=ICostantiImpugnazione.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value+'/'+document.LoadModificaImpugnazione.<%=ICostantiImpugnazione.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>.value;
      //if (! CompareDate(data_canc, data_trasm))
      //{
      //  alert('Data arrivo in cancelleria > della Data Trasmissione atti ');
      //  return false;
      //}

      // Si Controlla che la data arrivo in cancelleria <= data Decisione (se valorizzata).
      var data_decisione=document.LoadModificaImpugnazione.<%=ICostantiImpugnazione.CAMPO_GIORNO_DATA_DECISIONE%>.value+'/'+document.LoadModificaImpugnazione.<%=ICostantiImpugnazione.CAMPO_MESE_DATA_DECISIONE%>.value+'/'+document.LoadModificaImpugnazione.<%=ICostantiImpugnazione.CAMPO_ANNO_DATA_DECISIONE%>.value;
      if (data_decisione!="//")
      {
        if (! CompareDate(data_canc, data_decisione))
        {
          alert('Data arrivo in cancelleria > della Data Decisione ');
          return false;
        }
      }

      // Si Controlla che, se valorizzata, data Trasmissione atti <= data odierna.
      var data_trasm=document.LoadModificaImpugnazione.<%=ICostantiImpugnazione.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value+'/'+document.LoadModificaImpugnazione.<%=ICostantiImpugnazione.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value+'/'+document.LoadModificaImpugnazione.<%=ICostantiImpugnazione.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>.value;
      if (data_trasm!="//")
      {
        if (! CompareDate(data_trasm, data_sistema))
        {
          alert('Data Trasmissione atti > della Data di Sistema ');
          return false;
        }
      }
      // Si Controlla la data Decisione (se valorizzata).
      if (data_decisione!="//")
      {
        if (! CompareDate(data_decisione, data_sistema))
        {
          alert('Data decisione > della data odierna');
          return false;
        }

        // Si Controlla che: data decisione <= data restituzione atti.
        var data_restituzione=document.LoadModificaImpugnazione.<%=ICostantiImpugnazione.CAMPO_GIORNO_DATA_RESTITUZIONE_ATTI%>.value+'/'+document.LoadModificaImpugnazione.<%=ICostantiImpugnazione.CAMPO_MESE_DATA_RESTITUZIONE_ATTI%>.value+'/'+document.LoadModificaImpugnazione.<%=ICostantiImpugnazione.CAMPO_ANNO_DATA_RESTITUZIONE_ATTI%>.value;
        if ( data_restituzione != '//' && (! CompareDate(data_decisione, data_restituzione)) )
        if (! CompareDate(data_decisione, data_restituzione))
        {
          alert('Data decisione > della Data restituzione atti');
          return false;
        }

        if (! CompareDate(data_canc, data_decisione))
        {
          alert('Data arrivo in cancelleria > della Data Decisione ');
          return false;
        }
      }

      // Controllo della data arrivo in cancelleria <= data di sistema.
      if (! CompareDate(data_canc, data_sistema))
      {
        alert('Data arrivo in cancelleria > della data odierna');
        return false;
      }
      return true;
    }

    function abilitaDisabilitaDescAltro() {
    	if(document.LoadModificaImpugnazione.<%=ICostantiImpugnazione.CAMPO_SOGGETTO_IMPUGNANTE%>[document.LoadModificaImpugnazione.<%=ICostantiImpugnazione.CAMPO_SOGGETTO_IMPUGNANTE%>.selectedIndex].value =='08'){
    		document.LoadModificaImpugnazione.<%=ICostantiImpugnazione.CAMPO_DESCRIZIONE_ALTRO%>.disabled = false;
        } else {
       		document.LoadModificaImpugnazione.<%=ICostantiImpugnazione.CAMPO_DESCRIZIONE_ALTRO%>.value = "";
        	document.LoadModificaImpugnazione.<%=ICostantiImpugnazione.CAMPO_DESCRIZIONE_ALTRO%>.disabled = true;
        }
    }
    </script>
  </head>

  <body class="corpo" onload="abilitaDisabilitaDescAltro();">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=LBG><font class="label">Funzione :</font>&nbsp;
<%
          String lAction = new String();
          String lTitolo = new String();
          if (tipoUfficio.compareTo("TDS")==0)
            lTitolo = "Ricorso";
          else
            lTitolo = "Impugnazione / Ricorso";

          FascicoloGPModel lFascicolo = new FascicoloGPModel();
%>
          <font class="campo">Modifica <%=lTitolo%></font>
        </td>
  <!-- BOTTONE DI RITORNO -->
  <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>

      </tr>
    </table>
  <br>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadModificaImpugnazione'>

    <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    <br>

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="label">Avverso il Provvedimento : </td>
      </tr>
      <tr>
<%
      if (numOrdDec.compareTo("")==0 )
      {
%>
        <td class="campo"><%=provvedimento.getDescrTipoProvvedimento() +" di "+ provvedimento.getDescrMotivo()+" del "+ DateUtils.getDateToString ( provvedimento.getDataEmissione(), "dd/MM/yyyy" ) %></td>
    <%} else {%>
        <td> <font class="campo"><%=provvedimento.getDescrTipoProvvedimento()%> N. <%=numOrdDec%> </font> <font class="Label"> del </font> <font class="campo"> <%=dataOrdDec%> </font> <font class="Label"> depositato il </font> <font class="campo"> <%=dataDeposito%> </font> </td>
    <%}%>
      </tr>
    </table>
    <br>

   <!-- LISTA DEI RICORSI ANNULLATI -->
    <jsp:include page="<%=ICostantiImpugnazione.PG_LISTA_ANNULLATE%>">
    <jsp:param name="ValoreIdEvento" value="<%=provvedimento.getIdEvento()%>" />
    <jsp:param name="ValoreTipoProv" value="<%=provvedimento.getCodTipoProvvedimento()%>" />
     </jsp:include>

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l">Anno/Numero&nbsp;<%=lTitolo%></td>
        <td class="L"><%=impugnazione.getAnnoS7()%>/<%=impugnazione.getProgrS7()%>
        </td>
      </tr>

      <tr>
        <td class="l">Tipo </td>
        <td class="L" >
<%
        if (impugnazione.getCodTipoImpugnazione().equals("-") )
        {
%>
          <%=impugnazione.getDescrTipoImpugnazione()%>
          <input type="HIDDEN" name="<%=ICostantiImpugnazione.CAMPO_COD_TIPO_IMPUGNAZIONE%>" value="<%=impugnazione.getCodTipoImpugnazione()%>" >
<%      }else{
%>
          <select title="tipoRicorso" class=small name="<%=ICostantiImpugnazione.CAMPO_COD_TIPO_IMPUGNAZIONE%>" >
            <%= tipoRicorso %>
          </select>
<%      }
%>
        </td>
      </tr>

      <tr>
        <td class="l">Presentato da </td>
        <td class="L">
<%
        if( impugnazione.getSoggettoImpugnante().equals("-") )
        {
%>
          <%=impugnazione.getDescrSoggettoImpugnante()%>
          <input type="HIDDEN" name="<%=ICostantiImpugnazione.CAMPO_SOGGETTO_IMPUGNANTE%>" value="<%=impugnazione.getSoggettoImpugnante()%>" >
<%      }else{
%>
          <select title="soggettoImpugnante" class=small name="<%=ICostantiImpugnazione.CAMPO_SOGGETTO_IMPUGNANTE%>" onchange="Javascript: abilitaDisabilitaDescAltro();">
            <%= soggettoImpugnante %>
          </select>
<%      }
%>
        </td>
      </tr>
	  
	  <!-- Inizio Modifica del 16/11/2016 MEV_50 -->
      <tr>
        <td class="l">Descrizione</td>
        <td class="L">
<%
        if(!impugnazione.getSoggettoImpugnante().equals("-") )
        {
%>
			<input type="text" name="<%= ICostantiImpugnazione.CAMPO_DESCRIZIONE_ALTRO %>" value="<%=StringUtils.toStringJSP(impugnazione.getDescrizioneAltro())%>" maxlength="100" size="30" >
<%
        } else {
%>        
			<input type="text" name="<%= ICostantiImpugnazione.CAMPO_DESCRIZIONE_ALTRO %>" maxlength="100" size="30" >
<%      } %>
        </td>
      </tr>
	  <!-- Fine Modifica del 16/11/2016 MEV_50 -->
	        
      <tr>
        <td class="l">Data atto </td>
<%
        if( Utils.isNullObj(impugnazione.getDataRicorso()) )
        {
%>
<td class="l">
          -
          <input type="HIDDEN" name="<%=ICostantiImpugnazione.CAMPO_GIORNO_DATA_RICORSO%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataRicorso() ,"dd"))%>" >
          <input type="HIDDEN" name="<%=ICostantiImpugnazione.CAMPO_MESE_DATA_RICORSO%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataRicorso() ,"MM"))%>" >
          <input type="HIDDEN" name="<%=ICostantiImpugnazione.CAMPO_ANNO_DATA_RICORSO%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataRicorso() ,"yyyy"))%>" >
<%      }else{
%>
        <td class="L">
          <input type="text" name="<%= ICostantiImpugnazione.CAMPO_GIORNO_DATA_RICORSO %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataRicorso() ,"dd"))%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          /
          <input type="text" name="<%= ICostantiImpugnazione.CAMPO_MESE_DATA_RICORSO %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataRicorso() ,"MM")) %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          /
          <input type="text" name="<%= ICostantiImpugnazione.CAMPO_ANNO_DATA_RICORSO %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataRicorso() ,"yyyy")) %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
<%      }
%>
        </td>
      </tr>

      <tr>
        <td class="l">Data arrivo in cancelleria </td>
        <td class="L">
<%
        if (Utils.isNullObj(impugnazione.getDataArrivoCancelleria()))
        {
%>
          -
          <input type="HIDDEN" name="<%= ICostantiImpugnazione.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataArrivoCancelleria() ,"dd")) %>" >
          <input type="HIDDEN" name="<%= ICostantiImpugnazione.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataArrivoCancelleria() ,"MM")) %>" >
          <input type="HIDDEN" name="<%= ICostantiImpugnazione.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataArrivoCancelleria() ,"yyyy")) %>" >
<%
        }
        else
        {
%>
          <input type="text" name="<%= ICostantiImpugnazione.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataArrivoCancelleria() ,"dd")) %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          /
          <input type="text" name="<%= ICostantiImpugnazione.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataArrivoCancelleria() ,"MM")) %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          /
          <input type="text" name="<%= ICostantiImpugnazione.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataArrivoCancelleria() ,"yyyy")) %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
<%      }
%>
        </td>
      </tr>

      <tr>
        <td class="l">Data trasmissione atti </td>
        <td class="L">
<%
        if (Utils.isNullObj(impugnazione.getDataTrasmissioneAtti()))
        {
%>
          -
          <input type="HIDDEN" name="<%= ICostantiImpugnazione.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI %>"  value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataTrasmissioneAtti() ,"dd")) %>" >
          <input type="HIDDEN" name="<%= ICostantiImpugnazione.CAMPO_MESE_DATA_TRASMISSIONE_ATTI %>"  value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataTrasmissioneAtti() ,"MM")) %>" >
          <input type="HIDDEN" name="<%= ICostantiImpugnazione.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI %>"  value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataTrasmissioneAtti() ,"yyyy")) %>" >
<%
        }
        else
        {
%>
          <input type="text" name="<%= ICostantiImpugnazione.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI %>"  value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataTrasmissioneAtti() ,"dd")) %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          /
          <input type="text" name="<%= ICostantiImpugnazione.CAMPO_MESE_DATA_TRASMISSIONE_ATTI %>"  value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataTrasmissioneAtti() ,"MM")) %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          /
          <input type="text" name="<%= ICostantiImpugnazione.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI %>"  value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataTrasmissioneAtti() ,"yyyy")) %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
<%      }
%>
        </td>
      </tr>

      <tr>
        <td class="l">Autorità destinataria </td>
        <td class="L">
<%
        if( impugnazione.getCodAutoritaDestinataria().equals("-") )
        {
%>
          <%=impugnazione.getDescrAutoritaDestinataria()%>
          <input type="HIDDEN" name="<%=ICostantiImpugnazione.CAMPO_COD_AUTORITA_DESTINATARIA%>" value="<%=impugnazione.getCodAutoritaDestinataria()%>" >
<%      }else{
%>
          <select title="ListaUffici" class=small name="<%=ICostantiImpugnazione.CAMPO_COD_AUTORITA_DESTINATARIA%>" >
            <%= ListaUffici %>
          </select>
<%      }
%>
        </td>
      </tr>

      <tr>
        <td class="l">Sospensione esecuzione provvedimento </td>
        <td class="L">
          <font>NO</font>
          	<input type=radio name="<%=ICostantiImpugnazione.CAMPO_FLAG_SOSP_ESEC%>" value="N"
<%
        			if( impugnazione.getFlagSospEsec().equals("N") )
              {%>checked="checked"<%}
%>
            > &nbsp;&nbsp;&nbsp;
          <font>SI</font>
          	<input type=radio name="<%=ICostantiImpugnazione.CAMPO_FLAG_SOSP_ESEC%>" value="S"
<%
        			if( impugnazione.getFlagSospEsec().equals("S") )
              {%>checked="checked"<%}
%>
            >
        </td>
      </tr>

      <tr>
        <td class="l">Data decisione </td>
        <td class="L">
<%
        if (Utils.isNullObj(impugnazione.getDataDecisione()))
        {
%>
          -
          <input type="HIDDEN" name="<%= ICostantiImpugnazione.CAMPO_GIORNO_DATA_DECISIONE %>"  value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataDecisione() ,"dd")) %>" >
          <input type="HIDDEN" name="<%= ICostantiImpugnazione.CAMPO_MESE_DATA_DECISIONE %>"  value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataDecisione() ,"MM")) %>" >
          <input type="HIDDEN" name="<%= ICostantiImpugnazione.CAMPO_ANNO_DATA_DECISIONE %>"  value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataDecisione() ,"yyyy")) %>" >
<%
        }
        else
        {
%>
          <input type="text" name="<%= ICostantiImpugnazione.CAMPO_GIORNO_DATA_DECISIONE %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataDecisione() ,"dd")) %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          /
          <input type="text" name="<%= ICostantiImpugnazione.CAMPO_MESE_DATA_DECISIONE %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataDecisione() ,"MM")) %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          /
          <input type="text" name="<%= ICostantiImpugnazione.CAMPO_ANNO_DATA_DECISIONE %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataDecisione() ,"yyyy")) %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
      <%}%>
        </td>
      </tr>

      <tr>
        <td class="l">Tenore decisione </td>
        <td class="L">
<%
        if( impugnazione.getCodTenoreDecisione().equals("-") )
        {
%>
          <%=impugnazione.getDescrTenoreDecisione()%>
          <input type="HIDDEN" name="<%=ICostantiImpugnazione.CAMPO_COD_TENORE_DECISIONE%>" value="<%=impugnazione.getCodTenoreDecisione()%>" >
<%      }else{
%>
          <select title="tenoreDecisioneRicorso" class=small name="<%=ICostantiImpugnazione.CAMPO_COD_TENORE_DECISIONE%>">
            <%= tenoreDecisioneRicorso %>
          </select>
<%      }
%>
        </td>
      </tr>

      <tr>
        <td class="l">Data restituzione atti </td>
        <td class="L">
<%
        if (Utils.isNullObj(impugnazione.getDataRestituzioneAtti()))
        {
%>
          -
          <input type="HIDDEN" name="<%= ICostantiImpugnazione.CAMPO_GIORNO_DATA_RESTITUZIONE_ATTI %>"  value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataRestituzioneAtti() ,"dd")) %>" >
          <input type="HIDDEN" name="<%= ICostantiImpugnazione.CAMPO_MESE_DATA_RESTITUZIONE_ATTI %>"  value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataRestituzioneAtti() ,"MM")) %>" >
          <input type="HIDDEN" name="<%= ICostantiImpugnazione.CAMPO_ANNO_DATA_RESTITUZIONE_ATTI %>"  value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataRestituzioneAtti() ,"yyyy")) %>" >
<%
        }
        else
        {
%>
          <input type="text" name="<%= ICostantiImpugnazione.CAMPO_GIORNO_DATA_RESTITUZIONE_ATTI %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataRestituzioneAtti() ,"dd")) %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          /
          <input type="text" name="<%= ICostantiImpugnazione.CAMPO_MESE_DATA_RESTITUZIONE_ATTI %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataRestituzioneAtti() ,"MM")) %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          /
          <input type="text" name="<%= ICostantiImpugnazione.CAMPO_ANNO_DATA_RESTITUZIONE_ATTI %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataRestituzioneAtti() ,"yyyy")) %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
        </td>
      <%}%>
      </tr>

      <tr>
        <td class="l">Note</td>
        <td class="l">
<%
        if (Utils.isNullObj(impugnazione.getAnnotazione()) || impugnazione.getAnnotazione().compareTo("null")==0)
        {
%>
          <Textarea Title="Note" name="<%= ICostantiImpugnazione.CAMPO_NOTE %>" cols=80 rows=5></textarea>
<%      }else{
%>
          <Textarea Title="Note" name="<%= ICostantiImpugnazione.CAMPO_NOTE %>" cols=80 rows=5><%=StringUtils.toStringJSP(impugnazione.getAnnotazione()) %></textarea>
<%      }
%>
        </td>
      </tr>

    </table>

    <br><br>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td>
          <input onclick="Javascript:return Verify();" class="bottone" type="submit" value="Conferma">
        </td>
      </tr>
    </table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.impugnazione.action.ActModificaImpugnazione" >
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=provvedimento.getIdEvento()%>" >
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>" value="<%=provvedimento.getCodTipoProvvedimento()%>" >
    <input type="HIDDEN" name="<%=ICostantiImpugnazione.CAMPO_ID_IMPUGNAZIONE%>" value="<%=impugnazione.getIdImpugnazione().toString()%>" >

  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadModificaImpugnazione");

    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_GIORNO_DATA_RICORSO%>","req","Il campo Giorno della Data Atto è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_GIORNO_DATA_RICORSO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_GIORNO_DATA_RICORSO%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_GIORNO_DATA_RICORSO%>","lt=31");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_MESE_DATA_RICORSO%>","req","Il campo Mese della Data Atto è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_MESE_DATA_RICORSO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_MESE_DATA_RICORSO%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_MESE_DATA_RICORSO%>","lt=12");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_ANNO_DATA_RICORSO%>","req","Il campo Anno della Data Atto è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_ANNO_DATA_RICORSO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_ANNO_DATA_RICORSO%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_ANNO_DATA_RICORSO%>","lt=2999");

    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>","req","Il campo Giorno della Data Arrivo in Cancelleria è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>","lt=31");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>","req","Il campo Mese della Data Arrivo in Cancelleria è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>","lt=12");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>","req","Il campo Anno della Data Arrivo in Cancelleria è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>","lt=2999");

    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","numeric");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","lt=31");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","numeric");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","lt=12");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","numeric");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","lt=2999");

    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_GIORNO_DATA_DECISIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_GIORNO_DATA_DECISIONE%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_GIORNO_DATA_DECISIONE%>","lt=31");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_MESE_DATA_DECISIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_MESE_DATA_DECISIONE%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_MESE_DATA_DECISIONE%>","lt=12");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_ANNO_DATA_DECISIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_ANNO_DATA_DECISIONE%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_ANNO_DATA_DECISIONE%>","lt=2999");

    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_GIORNO_DATA_RESTITUZIONE_ATTI%>","numeric");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_GIORNO_DATA_RESTITUZIONE_ATTI%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_GIORNO_DATA_RESTITUZIONE_ATTI%>","lt=31");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_MESE_DATA_RESTITUZIONE_ATTI%>","numeric");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_MESE_DATA_RESTITUZIONE_ATTI%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_MESE_DATA_RESTITUZIONE_ATTI%>","lt=12");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_ANNO_DATA_RESTITUZIONE_ATTI%>","numeric");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_ANNO_DATA_RESTITUZIONE_ATTI%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiImpugnazione.CAMPO_ANNO_DATA_RESTITUZIONE_ATTI%>","lt=2999");

  </script>

  </body>
</html>