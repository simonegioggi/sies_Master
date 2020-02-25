<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento"%>

<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />

<jsp:useBean id="contenuto"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"    scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione"  scope="request" class="java.util.Date"/>
<jsp:useBean id="lFasSiusOrigine" scope="request" class="siap.sius.fascicolo.model.FascicoloSiusModel" />
<jsp:useBean id="lPerMod"	 				scope="request" class="siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel" />
<jsp:useBean id="TornaQui"     		scope="request" class="java.lang.String"/>

<%
	TenoreModel[] tenori = (TenoreModel[])request.getAttribute("tenori");
	String[] esiti	= (String[])request.getAttribute("esiti");
%>

<%
// Flag che indica la presenza del fascicolo origine;
  boolean isFascicoloOrigine = (lFasSiusOrigine != null && lFasSiusOrigine.getIdFascicoloSius()  != null) ? true : false;
  String  idFascicoloOrigine = (isFascicoloOrigine ? lFasSiusOrigine.getIdFascicoloSius().toString() : "");
  String lCodTipoProv = ICostantiProvvedimento.COD_ORDINANZA;
   // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
 String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>

<html>
  <head>
    <title>[S.I.E.S.] - Emissione Ordinanza Conversione SS</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="/html/verifyCombo.js"></script>

  <script language="JavaScript">
    // STUB 21/07/2004 Controllo obbligatorietà esiti.
    function Verify()
    {
      var lEsiti=document.InserisciOrdinanzaConversioneSS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
      var ritorno = VerifyCombo(lEsiti,"Esito");
      return ritorno;
    }
  </script >

 </head>
 <%
  String lAction = new String();
  lAction = "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS";
 %>

  <body class="corpo" >

    <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione : </font> <font class="campo">Emissione Ordinanza Conversione SS</font>&nbsp;
      </td>
    </tr>
    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
    </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciOrdinanzaConversioneSS">
    <table width=35%>
   <tr>
     <td class="l" width==30%> Data Emissione</td>
     <td class="l" width==70%> <%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%></td>
   </tr>
    </table>
 <table cellspacing="2" cellpadding="2" width="90%">

 		<tr>
			<td class="l">Eventuale Motivazione</td>
    	<td class="l"><TEXTAREA title="Ulteriore descrizione della decisione" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE %>" cols="70" rows="4" ></textarea></td>
		</tr>
 </table>
 <table cellspacing="2" cellpadding="2" style="width: 90%;">
    <tr> <td>&nbsp;</td> </tr>

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
 <table cellspacing="2" cellpadding="2" width="90%">

       <tr>
		<td class="l">Sanzione Sostitutiva Espiata</td>
		<td class="c">
	              <font class="label">Anni</font>
	              <font class="campo"><%=StringUtils.toStringJSP(lPerMod.getEspiataAA())%></font>
	              <font class="label">Mesi</font>
	              <font class="campo"> <%=StringUtils.toStringJSP(lPerMod.getEspiataMM())%></font>
	              <font class="label">Giorni</font>
	              <font class="campo"> <%=StringUtils.toStringJSP(lPerMod.getEspiataGG())%></font>&nbsp;
	          </td>
	</tr>
	<tr>
		<td class="l">Sanzione Sostitutiva residua da Espiare</td>
		<td class="c">
	              <font class="label">Anni</font>
	              <font class="campo"><%=StringUtils.toStringJSP(lPerMod.getResiduaAA()) %></font>
	              <font class="label">Mesi</font>
	              <font class="campo"> <%=StringUtils.toStringJSP(lPerMod.getResiduaMM())%></font>
	              <font class="label">Giorni</font>
	              <font class="campo"> <%=StringUtils.toStringJSP(lPerMod.getResiduaGG())%></font>&nbsp; 
	          </td>
	</tr>
	
</table>
 <table cellspacing="2" cellpadding="2" width="100%">
  	<tr>
 		<td class="l" colspan="2">Pena detentiva da espiare: Reclusione </td>
	    <td class="L">
	       Anni <input title="Anni" type="text" size="2" maxlength="2" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_DETENZIONE_DOM %>" > 
	       Mesi <input title="Mesi" type="text" size="2" maxlength="2" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_DETENZIONE_DOM %>"  >
	       Giorni <input title="Giorni" type="text" size="4" maxlength="4" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_DETENZIONE_DOM %>" >
	    </td>
	      
        <td class="l">Arresto 
	       Anni <input title="Anni" type="text" size="2" maxlength="2" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_ARRESTO_REV %>"  > 
	       Mesi <input title="Mesi" type="text" size="2" maxlength="2" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_ARRESTO_REV %>"  > 
	       Giorni <input title="Giorni" type="text" size="4" maxlength="4" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_ARRESTO_REV %>"  >
	    </td>
	</tr> 	
</table>
<br>
<% 
  if ( isFascicoloOrigine) { 
%>
	<table cellspacing="2" cellpadding="2" style="width: 90%;">
		<tr>
	        <td class="Titolo" colspan=6 > Provvedimento da Convertire </td>
	    </tr>
		<tr>
      		<td class="L">
        		<font class="label">Procedimento Collegato N.</font>
        		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=lFasSiusOrigine.getIdFascicoloSius()%><%=retParam%>">
          		<%=lFasSiusOrigine.getChiaveAnno()%>
          		/
          		<%=lFasSiusOrigine.getChiaveProgr()%>&nbsp;
            	</a>
         		&nbsp;<%=lFasSiusOrigine.getDescrTipoUfficio()%>&nbsp;<%=lFasSiusOrigine.getDescrComuneUfficio()%>&nbsp;&nbsp;
 			</td>
	    </tr>
	</table>
<% 
  } 
%>

<table cellspacing="2" cellpadding="2" style="width: 90%;">
	<tr> <td>&nbsp;</td> </tr>
    <tr>
    	<td>
        	<input class="bottone" type="submit" value="Conferma" >
        </td>
    </tr>
</table>

<br>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=contenuto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA%>" value="<%=tipo_decreto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%> >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ID_CSSA_COMP%>">

  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("InserisciOrdinanzaConversioneSS");
    frmvalidator.setAddnlValidationFunction("Verify");
    frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_DETENZIONE_DOM%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_DETENZIONE_DOM%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_DETENZIONE_DOM%>","numeric");

   	frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_ARRESTO_REV%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_ARRESTO_REV%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_ARRESTO_REV%>","numeric");

  
  </script>

 </body>

</html>