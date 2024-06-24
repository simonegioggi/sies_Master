<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.RedirectTo"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>

<%@ page import="siap.sius.esecuzionesanzionesostitutiva.action.ICostantiEsecuzioneSS"%>


<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />

<jsp:useBean id="contenuto" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto" scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione" scope="request" class="java.util.Date"/>

<jsp:useBean id="tipoPeneSostitutive" scope="request" class="java.lang.String"/>

<%
  TenoreModel[] tenori = (TenoreModel[])request.getAttribute("tenori");
  String[] esiti  = (String[])request.getAttribute("esiti");

  // MERGE v10: per i maggiorenni la tabella "tableInForma" non deve essere visibile
  UtenteModel lUteMod = (UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
  UfficioModel lUffMod = lUteMod.getUfficioUtente();
  String CodUff = new String(lUffMod.getCodTipoUfficio());
  String labelUfficio = "";
  boolean isUffMinor = false;
  if ("TDSM".equals(CodUff) || "UDSM".equals(CodUff))
    isUffMinor = true;
%>

<html>
  <head>
    <title>[S.I.E.S.] - Emissione Ordinanza Riesame Misura Sicurezza</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="/html/verifyCombo.js"></script>

<script language="JavaScript">
function AbilitaCampiEsiti()
{
  nodeRevocaNuovaPena = document.getElementById('idDivRevocaNuovaPena');
  var isRevoca="false";
  
  if (typeof (document.InserisciOrdinanzaRevocaPenaSostitutiva.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[0][0])=="undefined" ) 
  {
    for (j = 0; j < document.InserisciOrdinanzaRevocaPenaSostitutiva.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.length ; j++ )
    {
      if (   document.InserisciOrdinanzaRevocaPenaSostitutiva.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].selected   
          && (   document.InserisciOrdinanzaRevocaPenaSostitutiva.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="3114"
              || document.InserisciOrdinanzaRevocaPenaSostitutiva.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="3115"
             )
         )
      {
        isRevoca="true";
      }
    } // fine ciclo for
  } // Fine caso singolo oggetto
  else    
  { // Nel caso di più oggetti, la div d rideterminazione è visibile se almeno un esito è di Revoca
    // Scorro gli Oggetti
    alert("tipo 2");
    for (j = 0; j < document.InserisciOrdinanzaRevocaPenaSostitutiva.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.length ; j++ )
    {
      // Scorro gli esiti
      for (i = 0; i < document.InserisciOrdinanzaRevocaPenaSostitutiva.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].length ; i++ )
      {
        if (   document.InserisciOrdinanzaRevocaPenaSostitutiva.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j][i].selected
            && (   document.InserisciOrdinanzaRevocaPenaSostitutiva.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j][i].value == "3114" 
                || document.InserisciOrdinanzaRevocaPenaSostitutiva.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j][i].value == "3115" 
               )
           )
        {
          isRevoca="true";
        }
      } 
    }
  } // Fine caso più oggetti
  
  
  if (isRevoca=="true")
    nodeRevocaNuovaPena.style.display="block";
  else
    nodeRevocaNuovaPena.style.display="none";
}     
</script>

<script language="JavaScript">

  function Verify()
  {
    var lEsiti=document.InserisciOrdinanzaRevocaPenaSostitutiva.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
    var ritorno = VerifyCombo(lEsiti,"Esito");
    
    //
    nodeRevocaNuovaPena = document.getElementById('idDivRevocaNuovaPena');
    if (nodeRevocaNuovaPena.style.display == 'block') {
      // Controllo quantum Pena Revocata
      anniRevocata   = document.InserisciOrdinanzaRevocaPenaSostitutiva.<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_ARRESTO_REV%>.value;
      mesiRevocata   = document.InserisciOrdinanzaRevocaPenaSostitutiva.<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_ARRESTO_REV%>.value;
      giorniRevocata = document.InserisciOrdinanzaRevocaPenaSostitutiva.<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_ARRESTO_REV%>.value;
      
      if (anniRevocata=='' && mesiRevocata=='' && giorniRevocata=='' )
      {
        alert('Indicare i quantum di pena revocata');
        document.InserisciOrdinanzaRevocaPenaSostitutiva.<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_ARRESTO_REV%>.focus();
        return false;
      }
      
      // Controllo della data decorrenza Revoca
      var data_decorrenza = document.InserisciOrdinanzaRevocaPenaSostitutiva.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_DECORRENZA%>.value
                      +'/'+ document.InserisciOrdinanzaRevocaPenaSostitutiva.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_DECORRENZA%>.value
                      +'/'+ document.InserisciOrdinanzaRevocaPenaSostitutiva.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_DECORRENZA%>.value;

      if (! ControllaDataPassaVuota(data_decorrenza))
      {
        alert('Data decorrenza Revoca non valida! ' + data_decorrenza);
        document.InserisciOrdinanzaRevocaPenaSostitutiva.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_DECORRENZA%>.focus();
        return  false;
      } 

      // Pena Sostitutiva Piu' Grave
      if (document.InserisciOrdinanzaRevocaPenaSostitutiva.<%= ICostantiEsecuzioneSS.CAMPO_COD_TIPO_SANZIONE%>.value=='-')
      {
        alert('Pena Sostitutiva più grave obbligatoria');
        document.InserisciOrdinanzaRevocaPenaSostitutiva.<%= ICostantiEsecuzioneSS.CAMPO_COD_TIPO_SANZIONE%>.focus();
        return false;
      }
      
      // Quantum Pena Da Espiare
      anniRideterminati   = document.InserisciOrdinanzaRevocaPenaSostitutiva.<%= ICostantiEsecuzioneSS.CAMPO_NUM_ANNI_SANZIONE%>.value;
      mesiRideterminati   = document.InserisciOrdinanzaRevocaPenaSostitutiva.<%= ICostantiEsecuzioneSS.CAMPO_NUM_MESI_SANZIONE%>.value;
      giorniRideterminati = document.InserisciOrdinanzaRevocaPenaSostitutiva.<%= ICostantiEsecuzioneSS.CAMPO_NUM_GIORNI_SANZIONE%>.value;
      
      if (anniRideterminati=='' && mesiRideterminati=='' && giorniRideterminati=='' )
      {
        alert('Indicare i quantum di pena da espiare ');
        document.InserisciOrdinanzaRevocaPenaSostitutiva.<%= ICostantiEsecuzioneSS.CAMPO_NUM_ANNI_SANZIONE%>.focus();
        return false;
      }
    }
    
    
    return ritorno;
    
  }
</script >

</head>


<body class="corpo">

  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione : </font> <font class="campo">Emissione Ordinanza Revoca Pena Sostitutiva</font>&nbsp;
      </td>
    </tr>
    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
  </table>


<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciOrdinanzaRevocaPenaSostitutiva">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaRevocaPS" >
  <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=contenuto%>" >
  <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA%>" value="<%=tipo_decreto%>" >
  <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%> >
  <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ID_CSSA_COMP%>">

  <table cellspacing="2" cellpadding="2" width="50%">
    <tr>
      <td class="l" width==30%> Data Emissione</td>
      <td class="l" width==70%> <%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%></td>
    </tr>  
    <tr>
      <td class="l">Eventuale Motivazione</td>
      <td class="l">
        <TEXTAREA title="Eventuale Motivazione" cols="70" rows="4"
                  name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE %>"  ></textarea>
      </td>
    </tr>      
  </table>

  <br>     
    
  <table cellspacing="2" cellpadding="2" style="width: 90%;">
    <tr>
      <td class="Titolo" colspan=6 > Specificare esito per ciascun oggetto: </td>
    </tr>
    <tr>
      <td class="l" colspan=2 > Oggetto </td>
      <td class="l" colspan=2 > Esito </td>
    </tr>
    <% for (int i=0; i< tenori.length;i++) { %>
    <tr>
      <td class="l"colspan=2 >
        <input Title="Oggetto" name="<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE %>" value="<%=tenori[i].getDescrOggettoTenore()%>"  
        readonly size="100%">
        <input type="hidden" name="<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>" value="<%=tenori[i].getCodOggettoTenore()%>" >
        <input type="hidden" name="<%= ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO %>" value="<%=tenori[i].getCodDettaglioOggetto()%>" >
      </td>
      <td class="l" colspan="2" >
        <select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>" onchange="Javascript:return AbilitaCampiEsiti();">
           <%=esiti[i]%>
        </select>
      </td>
    </tr>
    <% } %>
  </table>
    
  <br>

            
  <%-- 
  ====================================================================================
    NUOVO Pena Riconvertita 
  ====================================================================================
  Revoca e converte in pena detentiva   (0267)
  Revoca e converte in altra pena sostitutiva   (0268)
  --%>
  <div id="idDivRevocaNuovaPena" style="display:none;"> 
    <table cellspacing="2" cellpadding="2" width="90%" >
      <tr>
        <td>
          <font class="label">Quantum pena revocata</font>
        </td>
        <td>
          <font class="label">Anni</font>&nbsp;
          <input type="text" title="Anni" size="4" maxlength="2" 
                 name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_ARRESTO_REV %>"
                 value=""
                 ONKEYPRESS="return TicTabNumField(this,event)"   >
          <font class="label">Mesi</font>&nbsp;
          <input type="text" title="Mesi" size="4" maxlength="2" 
                 name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_ARRESTO_REV %>" 
                 value="" 
                 ONKEYPRESS="return TicTabNumField(this,event)"  >
          <font class="label">Giorni</font>&nbsp;
          <input type="text" title="Giorni" size="4" maxlength="2" 
                 name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_ARRESTO_REV %>" 
                 value="" 
                 ONKEYPRESS="return TicTabNumField(this,event)"  >
        </td>
      </tr>
    
      <tr>
        <td>
          <font class="label">Data Decorrenza Revoca</font>
        </td>  
        <td>
          <input type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_DECORRENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
          <input type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_DECORRENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
          <input type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_DECORRENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
    
      <tr>
        <td>  
          <font class="label" >Pena Sostitutiva piu' grave</font>&nbsp;
        </td>  
        <td>          
          <select title="Tipo Pena Sostitutiva" name="<%= ICostantiEsecuzioneSS.CAMPO_COD_TIPO_SANZIONE %>" >
          <option value="-">-</option>
          <%=tipoPeneSostitutive%>
          </select>
        </td>
      </tr> 

      <tr>
        <td>
          <font class="label">Rideterminazione Quantum Pena Da Espiare</font>
        </td>
        <td>
          <font class="label">Anni</font>&nbsp;
          <input type="text" title="Anni" size="4" maxlength="2" 
                 name="<%= ICostantiEsecuzioneSS.CAMPO_NUM_ANNI_SANZIONE %>"
                 value=""  
                 ONKEYPRESS="return TicTabNumField(this,event)"   >
          <font class="label">Mesi</font>&nbsp;
          <input type="text" title="Mesi" size="4" maxlength="2" 
                 name="<%= ICostantiEsecuzioneSS.CAMPO_NUM_MESI_SANZIONE %>" 
                 value=""  
                 ONKEYPRESS="return TicTabNumField(this,event)"  >
          <font class="label">Giorni</font>&nbsp;
          <input type="text" title="Giorni" size="4" maxlength="2" 
                 name="<%= ICostantiEsecuzioneSS.CAMPO_NUM_GIORNI_SANZIONE %>"
                 value=""  
                 ONKEYPRESS="return TicTabNumField(this,event)"   >
        </td>
      </tr>
    </table>
  </div>
                
    <br>

  <table cellspacing="2" cellpadding="2" width="90%">
    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma" >
      </td>
    </tr>
  </table>

</form>
  
  
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("InserisciOrdinanzaRevocaPenaSostitutiva");

    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
 </body>
</html>