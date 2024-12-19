<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.depositodecreto.model.DepositoDecretoModel"%>
<%@ page import="siap.sius.esecuzionemisuraalternativa.model.EsecuzioneMisuraAlternativaModel"%>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata"%>

<jsp:useBean id="contenuto"        scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"     scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione"   scope="request" class="java.util.Date"/>
<jsp:useBean id="decreto"          scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoModel"/>

<%
	TenoreModel[] tenori = (TenoreModel[])request.getAttribute("tenori");
	String[] esiti	= (String[])request.getAttribute("esiti");
%>

<html>
  <head>
    <title>[S.I.E.S.] - Emissione Ordinanza Reclamo Permesso</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="/html/verifyCombo.js"> </script>

    <script language="JavaScript">
    // STUB 21/07/2004 Controllo obbligatorietà esiti.
    function Verify()
    {
      var ritorno = true;
      var esito = document.InserisciOrdinanzaReclamoPermesso.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
      ritorno = VerifyCombo(esito, "Esito");
      if (ritorno)
          ritorno = ControlliDate();
      return ritorno;
    }

    function ControlliDate()
    {
     var ritorno = true;
     var data_emissione = '<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%>';
     var data_emissione_decreto = document.InserisciOrdinanzaReclamoPermesso.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value+'/'+document.InserisciOrdinanzaReclamoPermesso.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_TRASMISSIONE%>.value+'/'+document.InserisciOrdinanzaReclamoPermesso.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_TRASMISSIONE%>.value;

     // Controlli su Data Emissione Decreto
     if (ritorno && (data_emissione_decreto.length > 2 ))
     {
       if (! ControllaData(data_emissione_decreto))
       {
         alert('Data Emissione Decreto non valida');
         ritorno =  false;
       }
       else if ( !CompareDate( data_emissione_decreto, data_emissione) )
       {
         alert("Data Emissione Decreto deve precedere " + data_emissione + " !");
         ritorno =  false;
       }
     }
     return ritorno;
    }

  </script>
    <script language="JavaScript">
      // Chiamata all'elenco degli UDS
      var desktop;

      function ListaUDS(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
    </script>
  <script language="JavaScript">

    function Init()
    {
    <% if (decreto != null && decreto.getIdDepositoDecreto() != null)
       { %>
          InitDecreto();
    <% } %>
       return;
    }

    function InitDecreto()
    {
       document.InserisciOrdinanzaReclamoPermesso.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value = "<%=DateUtils.getDateToString(decreto.getDataEmissione() ,"dd")%>";
       document.InserisciOrdinanzaReclamoPermesso.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_TRASMISSIONE%>.value = "<%=DateUtils.getDateToString(decreto.getDataEmissione(),"MM")%>";
       document.InserisciOrdinanzaReclamoPermesso.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_TRASMISSIONE%>.value = "<%=DateUtils.getDateToString(decreto.getDataEmissione(),"yyyy")%>";
       document.InserisciOrdinanzaReclamoPermesso.<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>.value = "<%=decreto.getDescrUfficioInserimento()%>";
    }

 </script>

 </head>
 <%
  String lAction = new String();
  lAction = "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaPermesso";
 %>


  <body class="corpo" onLoad="Javascript:Init();">

    <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione : </font> <font class="campo">
<%  if (contenuto != null && contenuto.equals(ICostantiDepositoOrdinanzaPc.OGG_ORD_RECLAMO_LICENZA)) { %>      
      Emissione Ordinanza Reclamo Licenza
<%  } else { %> 
      Emissione Ordinanza Reclamo Permesso
<%  } %>
      </font>&nbsp;
      </td>
    </tr>
    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
    </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciOrdinanzaReclamoPermesso">
    <table width=35%>
   <tr>
     <td class="l" width="30%"> Data Emissione</td>
     <td class="l" width="70%"> <%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%></td>
   </tr>
    </table>

    <tr> <td>&nbsp;</td> </tr>
     <table cellspacing="2" cellpadding="2" style="width: 90%;">
    <tr>
        <td class="Titolo" colspan=6 > Specificare esito per ciascun oggetto: </td>
    </tr>
    <tr>
        <td class="l" colspan=2 > Oggetto </td>
        <td class="l" colspan=2 > Esito </td>
    </tr>
    <%
   for (int i=0; i< tenori.length;i++)
    {
    %>
       <tr>
        <td class="l"colspan=2 >
          <input Title="Oggetto" name="<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE %>" value="<%=tenori[i].getDescrOggettoTenore()%>"  readonly size=60%>
          <input Title="Cod Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>" value="<%=tenori[i].getCodOggettoTenore()%>" >
          <input Title="Cod Dettaglio Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO %>" value="<%=tenori[i].getCodDettaglioOggetto()%>" >
        </td>
          <td class="l"colspan=2 >
           <select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>">
             <%=esiti[i]%>
          </select>
        </td>
      </tr>
    <%
    }
    %>
    </table>
<br>
 <table cellspacing="2" cellpadding="2" style="width: 90%;">
   <tr>
	<td class="l">Ulteriore descrizione della decisione</td>
    <td class="l"><TEXTAREA title="Ulteriore descrizione della decisione" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE %>" cols="70" rows="4" ></textarea></td>
  </tr>
 
  <tr> </tr>
  <tr>
    <td class="Titolo" colspan=6> Estremi decreto permesso reclamato: <td>
  </tr>
    <tr>
      <td class="l">Data Emissione <br> (gg-mm-aaaa)</td>
      <td class="L">
        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_TRASMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_TRASMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_TRASMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
      </td>
      <td class="l">Ufficio Sorveglianza  </td>
      <td class="l">
        <input Title="Magistrato" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP %>" value="" size=35 >
        <a href="Javascript:ListaUDS('InserisciOrdinanzaReclamoPermesso','<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>');">
        <img src="/images/filefolder.gif" border=0></a></td>
      </td>
    </tr>
  <tr> <td>&nbsp;</td> </tr>

  <tr>
<%  if (contenuto != null && contenuto.equals(ICostantiDepositoOrdinanzaPc.OGG_ORD_RECLAMO_LICENZA)) { %>
    	<td class="Titolo" colspan=6> In caso di accoglimento del reclamo dell'interessato, indicare: <td>
<%  } else { %>
		<td class="Titolo" colspan=6> In caso di accoglimento del reclamo, indicare: <td>
<%  } %>    
  </tr>

    <tr>
<%  if (contenuto != null && contenuto.equals(ICostantiDepositoOrdinanzaPc.OGG_ORD_RECLAMO_LICENZA)) { %>
      <td class="l"> Durata licenza &nbsp;</td>
<%  } else { %>
	  <td class="l"> Durata permesso &nbsp;</td>
<%  } %> 
      <td class="l"> giorni &nbsp;
        <input Title="Giorni Permesso" name="<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_GIORNI %>" value="" size="2" maxlength="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
        &nbsp;&nbsp;&nbsp;e/o ore
        <input Title="Ore Permesso" name="<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORE %>" value="" size="2" maxlength="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
      </td>
    </tr>
  <tr> <td>&nbsp;</td> </tr>

  <tr> <td>&nbsp;</td> </tr>
    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma"  >
      </td>
    </tr>
 </table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>"  >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=contenuto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA%>" value="<%=tipo_decreto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%> >

  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("InserisciOrdinanzaReclamoPermesso");
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>


 </body>

</html>