<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige" %>
<%@ page import="siap.sige.impugnazione.action.ICostantiImpugnazioneSige"%>
<%@ page import="siap.sige.impugnazione.model.ImpugnazioneSigeModel"%>

<jsp:useBean id="impugnazione" scope="request" class="siap.sige.impugnazione.model.ImpugnazioneSigeModel"/>
<jsp:useBean id="provvedimento" scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"/>
<jsp:useBean id="codTipoImpugnazione" scope="session" class="java.lang.String"/>
<jsp:useBean id="soggettoImpugnante" scope="request" class="java.lang.String"/>
<jsp:useBean id="ListaUffici" scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficiRecuperoCrediti" scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficiPubbliciMinisteri" scope="request" class="java.lang.String"/>
<jsp:useBean id="showDestinatari" scope="request" class="java.lang.String"/>

<jsp:useBean id="sedeRecuperoCrediti" scope="request" class="java.lang.String"/>
<jsp:useBean id="sedePubblicoMinistero" scope="request" class="java.lang.String"/>

<%
    String lTitolo = "Ricorso";
    if (codTipoImpugnazione.equals(ICostantiImpugnazioneSige.COD_TIPO_OPPOSIZIONE))
        lTitolo = "Opposizione";
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
    <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>listeDestSIUS.js" ></script>

    <script language="JavaScript">
    function Verify()
    {
      // Controllo della data atto.
      var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
      var data_atto=document.LoadModificaImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_RICORSO%>.value+'/'+document.LoadModificaImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_MESE_DATA_RICORSO%>.value+'/'+document.LoadModificaImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_RICORSO%>.value;
      if (! ControllaData(data_atto))
      {
        alert('Data atto non valida');
        return false;
      }
      // Si Controlla che: data emissione <= data atto.
      var data_emissione='<%=DateUtils.getDateToString(provvedimento.getProvvedimento().getDataEmissione(), "dd/MM/yyyy" )%>';
      if (! CompareDate(data_emissione, data_atto))
      {
        alert('Data Emissione Provvedimento > della Data atto');
        return false;
      }

      // Controllo della data arrivo in cancelleria.
      var data_canc=document.LoadModificaImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>.value+'/'+document.LoadModificaImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>.value+'/'+document.LoadModificaImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>.value;      if (! ControllaData(data_canc))
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

      // Controllo della data arrivo in cancelleria <= data di sistema.
      if (! CompareDate(data_canc, data_sistema))
      {
        alert('Data arrivo in cancelleria > della data odierna');
        return false;
      }
      return true;
    }

    function calendario(a_formname,a_field_year,a_field_month,a_field_day)
    {
      desktop = 
          window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
    }
    
    function effettoTree(){
    	node=document.getElementById("frameDestina");
    	node.style.display = (node.style.display == "none")? "block" : "none";
    	document.images["imageDestina"].src = (node.style.display == "none")? "/images/expand.gif" : "/images/collapse.gif";
    	return false;
    }
    
    function ListaComuni(a_formname,a_fieldname)    {
    	window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    function SelListProcura() {
          ListaProcure('LoadModificaImpugnazioneSige','<%=ICostantiImpugnazioneSige.CAMPO_COD_SEDE_DESTINATARIO_PUBBLICO_MINISTERO%>');  
    }
    
    // Lista Uffici per TIPO_UFFICIO    
    //@emma 09072018 intervento post COLLAUDO 11.2 
    function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
    {       
       window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
     // Lista comuni per TIPO_UFFICIO    
    //@emma 09072018 intervento post COLLAUDO 11.2 
   function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio)
 	{
 		 window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
 	}
     
    </script>
  </head>

  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=LBG><font class="label">Funzione :</font>&nbsp;

          <font class="campo">Modifica <%=lTitolo%></font>
        </td>
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
<tr>
        <td> <font class="campo"><%=provvedimento.getProvvedimento().getDescrTipoProvvedimento()%> N. <%=(provvedimento.getProvvedimento().getChiaveAnno()==null?"-": provvedimento.getProvvedimento().getChiaveAnno())%>/<%=(provvedimento.getProvvedimento().getChiaveProgr()==null?"-": provvedimento.getProvvedimento().getChiaveProgr())%> </font> <font class="Label"> del </font> <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getProvvedimento().getDataEmissione(),"dd/MM/yyyy"))%> </font> <font class="Label"> depositato il </font> <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getProvvedimento().getDataDeposito(),"dd/MM/yyyy"),"-")%> </font> </td>
      </tr>
      </tr>
    </table>
    <br />

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l">Anno/Numero&nbsp;<%=lTitolo%></td>
        <td class="L"><%=impugnazione.getAnnoS7()%>/<%=impugnazione.getProgrS7()%>
        </td>
      </tr>
<!-- Siamo nel caso di un Opposizione convertita in Ricorso, visualizzo il campo 
     Soggetto Impugnante che contiene la descrizione "Opposizione N. annoOpp/numOpp", 
     mentre non dovranno essere visualizzati i campi Data Atto, Data Arrivo 
     in Cancelleria e Presentato Da -->
<% if(impugnazione.getSoggettoImpugnante() != null && impugnazione.getSoggettoImpugnante().startsWith("Opposizione")){ %>
	  <tr>
        <td class="l">Proveniente da </td>
        <td class="L"><%=impugnazione.getSoggettoImpugnante()%>
        </td>
      </tr>
<% } else { %>
      <tr>
        <td class="l">Presentato da </td>
        <td class="L">

          <select title="soggettoImpugnante" class=small name="<%=ICostantiImpugnazioneSige.CAMPO_SOGGETTO_IMPUGNANTE%>" >
            <%= soggettoImpugnante %>
          </select>
        </td>
      </tr>

      <tr>
        <td class="l">Data atto </td>
        <td class="L">
          <input type="text" name="<%= ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_RICORSO %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataRicorso() ,"dd"))%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" />
          /
          <input type="text" name="<%= ICostantiImpugnazioneSige.CAMPO_MESE_DATA_RICORSO %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataRicorso() ,"MM")) %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" />
          /
          <input type="text" name="<%= ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_RICORSO %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataRicorso() ,"yyyy")) %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" />

		  <!-- MEV 15 - Revisione SIGE -->
		  <a href="javascript:calendario('LoadModificaImpugnazioneSige','<%=ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_RICORSO%>','<%=ICostantiImpugnazioneSige.CAMPO_MESE_DATA_RICORSO%>','<%=ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_RICORSO%>');">
      	      <img src="/images/calendario.gif" border=0>
       	  </a>

        </td>
      </tr>

      <tr>
        <td class="l">Data arrivo in cancelleria </td>
        <td class="L">

          <input type="text" name="<%=ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataArrivoCancelleria() ,"dd")) %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" />
          /
          <input type="text" name="<%=ICostantiImpugnazioneSige.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataArrivoCancelleria() ,"MM")) %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" />
          /
          <input type="text" name="<%=ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(impugnazione.getDataArrivoCancelleria() ,"yyyy")) %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" />

		  <!-- MEV 15 - Revisione SIGE -->
		  <a href="javascript:calendario('LoadModificaImpugnazioneSige','<%=ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>','<%=ICostantiImpugnazioneSige.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>','<%=ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>');">
      	      <img src="/images/calendario.gif" border=0>
       	  </a>
        </td>
      </tr>
<% } %>

<%
if (codTipoImpugnazione.equals(ICostantiImpugnazioneSige.COD_TIPO_RICORSO)) { 
%>            
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
<%
if (showDestinatari.equalsIgnoreCase("true")) {
%>    
<table style="width: 100%;" cellpadding="2" cellspacing="2">
    <tr>
      <td class="Titolo">
        <div align="left"><a><img name="imageDestina" src="/images/expand.gif"  onClick="return effettoTree();" alt="" border=0></a>Destinatari </div>
      </td>
    </tr>

    <tr id="frameDestina" style="display:none"> 
      <td> 
        <table>
		<tr><td colspan=6>&nbsp;</td>
	</tr>
    <tr>
        <td class="l">Ufficio Pubblico Ministero</td>
        <td class="l">
          <select title="Destinatario" name="<%=ICostantiImpugnazioneSige.CAMPO_COD_DESTINATARIO_PUBBLICO_MINISTERO %>">
            <%=ufficiPubbliciMinisteri %>
          </select>
        </td>
 
        <td class="l">Sede</td>
        <td class="l">
           <input value="<%=sedePubblicoMinistero %>" type="text" name="<%=ICostantiImpugnazioneSige.CAMPO_COD_SEDE_DESTINATARIO_PUBBLICO_MINISTERO %>" maxlength="35" size="35" title="Sede">
           <!-- <a href="javascript:SelListProcura();"><img src="/images/filefolder.gif" border=0></a> -->
           <a href="Javascript:ListaUfficiPerTipo('LoadModificaImpugnazioneSige','<%=ICostantiImpugnazioneSige.CAMPO_COD_SEDE_DESTINATARIO_PUBBLICO_MINISTERO%>',document.LoadModificaImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_COD_DESTINATARIO_PUBBLICO_MINISTERO%>[document.LoadModificaImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_COD_DESTINATARIO_PUBBLICO_MINISTERO%>.selectedIndex].value);">
                					<img src="/images/filefolder.gif" border=0>
                 				</a>
        </td>
     </tr>
     <tr>
        <td class="l">Ufficio Recupero Crediti</td>
        <td class="l">
          <select title="Destinatario" name="<%=ICostantiImpugnazioneSige.CAMPO_COD_DESTINATARIO_UFFICIO_RECUPERO_CREDITI%>">
            <%= ufficiRecuperoCrediti %>
          </select>
        </td>
       
        <td class="l">Sede</td>
        <td class="l">
           <input type="text" value="<%=sedeRecuperoCrediti %>" name="<%=ICostantiImpugnazioneSige.CAMPO_COD_SEDE_DESTINATARIO_UFFICIO_RECUPERO_CREDITI%>" maxlength="35" size="35" title="Sede">
<%--            <a href="Javascript:ListaComuni('LoadModificaImpugnazioneSige','<%=ICostantiImpugnazioneSige.CAMPO_COD_SEDE_DESTINATARIO_UFFICIO_RECUPERO_CREDITI%>');"><img src="/images/filefolder.gif" border=0></a> --%>
				<a href="Javascript:ListaUfficiComuni('LoadModificaImpugnazioneSige','<%=ICostantiImpugnazioneSige.CAMPO_COD_SEDE_DESTINATARIO_UFFICIO_RECUPERO_CREDITI%>',document.LoadModificaImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_COD_DESTINATARIO_UFFICIO_RECUPERO_CREDITI%>[document.LoadModificaImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_COD_DESTINATARIO_UFFICIO_RECUPERO_CREDITI%>.options.selectedIndex].value);">
            					<img src="/images/filefolder.gif" border=0></a>
        </td>
      </tr>
		<tr><td colspan=6>&nbsp;</td></tr>
	</table>
 </table>
 <%
}
 %>
    
   
    <br /><br />
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td>
          <input onclick="Javascript:return Verify();" class="bottone" type="submit" value="Conferma">
        </td>
      </tr>
    </table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.impugnazione.action.ActModificaImpugnazioneSige" />
    <input type="HIDDEN" name="<%=ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE%>" value="<%=impugnazione.getIdImpugnazioneSige().toString()%>" />

  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadModificaImpugnazioneSige");
    <% if (impugnazione != null && impugnazione.getSoggettoImpugnante() != null && 
    	   !impugnazione.getSoggettoImpugnante().startsWith("Opposizione")) { %>
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
    <% }%>  
    
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
  </script>
  </body>
</html>