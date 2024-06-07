<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.penapecuniaria.action.ICostantiPenaPecuniaria"%>
<%@ page import="siap.siep.penapecuniaria.model.RichiestaConversioneModel"%>
<%@ page import="siap.siep.penapecuniaria.model.RichiestaConversioneEstesaModel"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento"%>
<%@ page import="siap.sius.penapecuniaria.action.ICostantiSiusPenaPecuniaria"%>


<jsp:useBean id="fascicoloSiusGP"       scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="contenuto"             scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"          scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione"        scope="request" class="java.util.Date"/>
<jsp:useBean id="RataMancatoPagamento"  scope="request" class="siap.siep.rateizzazionepp.model.RateizzazionePPModel"/>

<jsp:useBean id="TornaQui"              scope="request" class="java.lang.String"/>

<%
  TenoreModel[] tenori = (TenoreModel[]) request.getAttribute("tenori");
  String[] esiti = (String[]) request.getAttribute("esiti");

  String lCodTipoProv = ICostantiProvvedimento.COD_ORDINANZA;
   // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>
<html>
  <head>
    <title>[S.I.E.S.] - Emissione Ordinanza Revoca Conversione PP Sostitutiva</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="/html/verifyCombo.js"></script>

  <script language="JavaScript">

    function Verify()
    {
      var lEsiti = document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
      var ritorno = VerifyCombo(lEsiti,"Esito");

      // Verifica Campi obbligatori


      return ritorno;
    }
    
    function selectEsito()
    {
      var sizeTenori = <%=tenori.length%>

      // Esito 'Dispone conversione...'
      var esitoSelezionato = document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.selectedIndex].value;
      
      if ( esitoSelezionato in { '3222':1, '3223':1 , '3224':1}  )     
      {
        node=document.getElementById("divConversione");
        node.style.display='block';
      }
      else {
        node=document.getElementById("divConversione");
        node.style.display='none';
      }
          
      if (sizeTenori > 1) {
        document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE%>[0].focus();
      } else {
        document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE%>.focus();
      }
    }
    
  </script >

 </head>
 <%
  String lAction = "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaConversioneRevocaPPS";
 %>

<body class="corpo" >

  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione : </font> <font class="campo">Emissione Ordinanza Revoca e Conversione Pena Pecuniaria Sostitutiva</font>&nbsp;</td>
    </tr>
    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
  </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciOrdinanzaConversionePP">  
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=contenuto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA%>" value="<%=tipo_decreto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%> >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ID_CSSA_COMP%>" >
    
    <table width=35%>
      <tr>
        <td class="l" width==30%> Data Emissione</td>
        <td class="l" width==70%> <%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%></td>
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
<% for (int i=0; i< tenori.length;i++){%>
      <tr>
        <td class="l"colspan=2 >
          <input Title="Oggetto" name="<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE %>" value="<%=tenori[i].getDescrOggettoTenore()%>"  readonly size=60%>
          <input Title="Cod Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>" value="<%=tenori[i].getCodOggettoTenore()%>" >
          <input Title="Cod Dettaglio Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO %>" value="<%=tenori[i].getCodDettaglioOggetto()%>" >
        </td>
        <td class="l"colspan=2 >
         <select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>" onChange="selectEsito();">
           <%=esiti[i]%>
         </select>
        </td>
      </tr>
 <% } %>
  </table>
  
  <table cellspacing="2" cellpadding="2" style="width: 90%;">
    <tr>
      <td class="l">Motivazione</td>
      <td class="l"><TEXTAREA title="Motivazione della decisione" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE %>" cols="70" rows="4" ></textarea></td>
    </tr>
    <tr>
      <td class="l">Ulteriore descrizione della decisione</td>
      <td class="l"><TEXTAREA title="Ulteriore Descrizione della decisione" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE %>" cols="70" rows="4" ></textarea></td>
    </tr>
  </table>

<div id="divConversione" style="display:block" >  
  <table cellspacing="2" cellpadding="2" width="90%">
    <tr>
      <td class="Titolo" colspan="6" >Pena Pecuniaria Sostitutiva Convertita</td>
    </tr>

    <tr>
      <td colspan="3" width="20%" class="l"> 
        <font class="label"> Importo non pagato: </font>&nbsp;
        <% 
        String lImportoNonPagatoStr = ""; //"n.d.";
        if (RataMancatoPagamento.getIdRateizzazionePP()!=null) {
          BigDecimal lImportoNonPagato = RataMancatoPagamento.getImportoDaPagare();
          if (lImportoNonPagato!=null)
            lImportoNonPagatoStr = StringUtils.toEuroFormat(lImportoNonPagato);
        } 
        %>
        <font class="campo"> <%=lImportoNonPagatoStr %></font><br>
        <font class="label"> Da convertire</font>&nbsp;
          <input type="text" Title="Importo da convertire" size="7" maxlength="16" style="text-align: right;"
                 name="<%=ICostantiSiusPenaPecuniaria.CAMPO_INTERO_IMPORTO_MULTA%>" 
                 onkeypress="return TicTabNumField(this,event)">&nbsp;,&nbsp;
          <input type="text" Title="Importo da convertire" size="2" maxlength="2" 
                 name="<%=ICostantiSiusPenaPecuniaria.CAMPO_DECIMALE_IMPORTO_MULTA%>" 
                 onkeypress="return TicTabNumField(this,event)"> 
        <font class="label"> EURO</font>&nbsp;
      </td>
      
      <td colspan="3" class="l"> 
        <font class="label"> Q u a n t u m  &nbsp;&nbsp;  P e n a &nbsp;&nbsp;  S o s t i t  u t i v a &nbsp;&nbsp; A &nbsp;&nbsp; S e g u i t o &nbsp;&nbsp; C o n v e r s i o n e <br></font>
        <br>
        <font class="label">Anni</font>
        <font class="campo"> <input title="Anni" type="text" size="2" maxlength="2" 
                                    name="<%=ICostantiSiusPenaPecuniaria.CAMPO_NUM_ANNI_SS%>" 
                                    onkeypress="return TicTabNumField(this,event)"></font>
        <font class="label">Mesi</font>
        <font class="campo"> <input title="Mesi" type="text" size="2" maxlength="2" 
                                    name="<%=ICostantiSiusPenaPecuniaria.CAMPO_NUM_MESI_SS%>" 
                                    onkeypress="return TicTabNumField(this,event)"> </font>
        <font class="label">Giorni</font>
        <font class="campo"> <input title="Giorni" type="text" size="4" maxlength="4" 
                                    name="<%=ICostantiSiusPenaPecuniaria.CAMPO_NUM_GIORNI_SS%>" 
                                    onkeypress="return TicTabNumField(this,event)"> </font>
        
        <font class="label">Semiliberta' Sostitutiva</font>
        <input value="01" type="radio" checked name="<%=ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE%>" >&nbsp; &nbsp;
        <font class="label">Detenzione Domiciliare sostitutiva </font>
        <input value="02" type="radio" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE%>" >&nbsp; &nbsp;
        <font class="label">Lavoro Pubblica Utilita' sostitutiva </font>
        <input value="03" type="radio" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE%>" >&nbsp; &nbsp;
      </td>
    </tr>
  </table>
  <table cellspacing="2" cellpadding="2" width="90%">
    <tr>
      <td class="label">Inserimento Prescrizioni <input value="06" type="checkbox" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_PRESCRIZIONI%>"></td>
    </tr>
  </table>
</div>
  
<br>
  
<div id="divFinale" style="display:block" >
  <table>
    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma" onclick="javascript:return Verify();" >
      </td>
    </tr>
  </table>
</div>

<br>


</form>
  
<script language="JavaScript" type="text/javascript">
  var frmvalidator = new Validator("InserisciOrdinanzaConversionePP");
  frmvalidator.setAddnlValidationFunction("Verify");
</script>

 </body>

</html>