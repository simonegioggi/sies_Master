<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel"%>
<%@ page import="siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>


<jsp:useBean id="fascicoloSiusGP" 			scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="contenuto"     				scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"     			scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione"     		scope="request" class="java.util.Date"/>
<jsp:useBean id="sanzione_sostitutiva"  scope="request" class="siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel"/>
<jsp:useBean id="tipoUfficioCompetente" scope="request" class="java.lang.String"/>
<jsp:useBean id="sanzione_residua"     	scope="request" class="siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel"/>

<%
	TenoreModel[] tenori = (TenoreModel[])request.getAttribute("tenori");
	String[] esiti	= (String[])request.getAttribute("esiti");
%>

<html>
  <head>
    <title>[S.I.E.S.] - Emissione Ordinanza  di Applicazione Sanzioni Sostitutive</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="/html/verifyCombo.js"></script>


  <script language="JavaScript">
    var desktop;
    function ListaComuni(a_formname,a_fieldname,codTipoUfficio)
    {
       desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
  </script >

  <script language="JavaScript">
   // Funzione di controllo congrueità dei dati inseriti
    function Verify()
    {
      var lEsiti=document.InserisciOrdinanzaApplicazioneSS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
     // alert ("Num Esiti ->" + lEsiti.length);
      
      var ritorno = VerifyCombo(lEsiti,"Esito");
      if (ritorno)
      	ritorno = VerificaCampiObbligatori(lEsiti);
      	
      return ritorno;
    }
  </script >
  
  <script language="JavaScript">
  
  // Flag per l'obbligatorietà di campi in funzione di esito selezionato
    var applica = false;
  	var restituisce = false;
  
    function ResetFlagEsiti ()
    {	
     	applica = false;
  		restituisce = false;
    }
    
    // Valorizza i flag in base al valore dell'esito passato
    function SettaValoriFlagEsiti (aCodEsito)
    {	
    	// alert ("Codice esito ->" +  aCodEsito);
    	
    	// Se selezionato esito "Determina modalità di esec ..."
    	if (aCodEsito == "1140")
    		applica = true;
    	// Se selezionato esito "... restituisce"  o "...dispone trasmissione "	
     	if (aCodEsito == "1146"  || aCodEsito == "1146" )
    		restituisce = true;
     }
   </script >    
     
    <script language="JavaScript">
    function VerificaCampiObbligatori(lCombo)
    {	
    	ResetFlagEsiti();
    
        // Size della Combo = 1
      	if (typeof (lCombo[0][0]) =="undefined" )
      		SettaValoriFlagEsiti(lCombo.value)
      	else
      	{
         for (j = 0; j < lCombo.length ; j++ )
           for (i = 0; i < lCombo[j].length ; i++ )
            if ( lCombo[j][i].selected )
 				SettaValoriFlagEsiti(lCombo[j][i].value);
      	}
      	var ret = true;
    	if (applica)
    		ret = ControlloDurataSS();
    		
    	if (ret && restituisce)
    		ret = ControlloUffComp();
    		
    	return ret;
    }
    
  </script >
  <script language="JavaScript">     
     // Funzione di controllo della valorizzazione della durata della Sanzione Sostitutiva
      function ControlloDurataSS()
    {	
        // alert ("Controllo campo durata Sanzione");
    
    	var ritorno = true;
 
    	var anni = document.InserisciOrdinanzaApplicazioneSS.<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_DETENZIONE_DOM %>.value;
     	var mesi = document.InserisciOrdinanzaApplicazioneSS.<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_DETENZIONE_DOM %>.value;
     	var giorni = document.InserisciOrdinanzaApplicazioneSS.<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_DETENZIONE_DOM %>.value;
    	
    	if(anni.length == 0 && mesi.length == 0 && giorni.length == 0)
    	{
    		ritorno = false;
    		alert ("Inserire la durata della Sanzione Sostitutiva ! ");
    	}
    	return ritorno;
    }
  </script >
  
  
  <script language="JavaScript">     
      
    // Funzione di controllo della valorizzazione dell'Ufficio Competente cui restituire
     function ControlloUffComp()
    {	
         // alert ("Controllo Ufficio Competente");
    
    	var ritorno = true;
    	var codUfficio = document.InserisciOrdinanzaApplicazioneSS.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>.value;
    	if(codUfficio.length <= 1)
    	{
    		ritorno = false;
    		alert ("Inserire l'Ufficio Competente ! ");
    	}
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
      <td class=LBG><font class="label">Funzione : </font> <font class="campo">Emissione Ordinanza Applicazione Sanzioni Sostitutive</font>&nbsp;
      </td>
    </tr>
    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
    </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciOrdinanzaApplicazioneSS">
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
           <select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>" >
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
			<td class="l">Ulteriore descrizione della decisione</td>
    	<td class="l"><TEXTAREA title="Ulteriore descrizione della decisione" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE %>" cols="70" rows="4" ></textarea></td>
		</tr>

    <tr>
      <td class="l">Dispositivo </td>
        <td class="l"> <Textarea Title="Dispositivo" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_NATURA_PROVVEDIMENTO %>" cols="70" rows="4"></Textarea></td>
    </tr>

   <% 
   String Anni = "", Mesi = "", Giorni = "", FlagReadOnly = "";
   
   if (sanzione_residua != null && ((sanzione_residua.getNumAnni() != null) ||
		      (sanzione_residua.getNumMesi() != null) || (sanzione_residua.getNumGiorni() != null))) 
   { 
			   Anni = StringUtils.toStringJSP(sanzione_residua.getNumAnni(), "0");
			   Mesi = StringUtils.toStringJSP(sanzione_residua.getNumMesi(), "0");
			   Giorni = StringUtils.toStringJSP(sanzione_residua.getNumGiorni(), "0");
		       FlagReadOnly = "readonly";
   } 
   else if (sanzione_sostitutiva != null && ((sanzione_sostitutiva.getNumAnni() != null) ||
      (sanzione_sostitutiva.getNumMesi() != null) || (sanzione_sostitutiva.getNumGiorni() != null))) 
   { 
	   Anni = StringUtils.toStringJSP(sanzione_sostitutiva.getNumAnni(), "0");
	   Mesi = StringUtils.toStringJSP(sanzione_sostitutiva.getNumMesi(), "0");
	   Giorni = StringUtils.toStringJSP(sanzione_sostitutiva.getNumGiorni(), "0");
       FlagReadOnly = "readonly";
    } 
    %>

    <tr>
      <td class="l">Sanzione Sostitutiva da espiare: </td>
      
      <td class="L">
       Anni<input value="<%=Anni%>" title="Anni" type="text" size="3" maxlength="3" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_DETENZIONE_DOM %>"  <%=FlagReadOnly%> >
       Mesi<input value="<%=Mesi%>" title="Mesi" type="text" size="3" maxlength="3" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_DETENZIONE_DOM %>"  <%=FlagReadOnly%> >
       Giorni<input value="<%=Giorni%>" title="Giorni" type="text" size="4" maxlength="4" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_DETENZIONE_DOM %>"  <%=FlagReadOnly%> >
      </td>

    </tr>
       <tr>
        <td class="l">Ufficio Competente<font class=ob>(*)</font></td>
        <td class="L">
          <select title="tipoUfficioCompetente" class=small name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>" >
            <%=tipoUfficioCompetente%>
          </select>
           <input Title="Sede Procura" name="<%=ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO%>"
              value="" type="text" maxlength="35" size="35">
              <a href="Javascript:ListaComuni('InserisciOrdinanzaApplicazioneSS','<%= ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO %>',document.InserisciOrdinanzaApplicazioneSS.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>[document.InserisciOrdinanzaApplicazioneSS.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>.selectedIndex].value);">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
      </tr>
 
 
  <tr> <td>&nbsp;</td> </tr>
      <tr>
        <td class="l">Inserimento Prescrizioni <input value="06" type="checkbox" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_PRESCRIZIONI%>"></td>
      </tr>
  <tr> <td>&nbsp;</td> </tr>
    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma" >
      </td>
    </tr>
 </table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=contenuto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA%>" value="<%=tipo_decreto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%> >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ID_CSSA_COMP%>">

  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("InserisciOrdinanzaApplicazioneSS");
    frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_DETENZIONE_DOM%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_DETENZIONE_DOM%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_DETENZIONE_DOM%>","numeric");
    
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>

 </body>

</html>