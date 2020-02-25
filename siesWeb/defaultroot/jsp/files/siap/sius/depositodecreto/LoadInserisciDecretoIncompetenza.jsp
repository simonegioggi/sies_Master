<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>



<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sius.motivazionedecreto.action.ICostantiMotivazioneDecreto"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>
<%@ page import="siap.sius.provvedimento.util.RicercaProvvedimentiUtil"%>

<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />

<jsp:useBean id="modalita"    		scope="request" class="java.lang.String"/>
<jsp:useBean id="contenuto"   		scope="request" class="java.lang.String"/>
<jsp:useBean id="codContenuto"    scope="request" class="java.lang.String"/>
<jsp:useBean id="codOggetti"  		scope="request" class="java.lang.String"/>
<jsp:useBean id="descOggetti" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratorelatore"   scope="request" class="siap.sius.magistratorelatore.model.MagistratoRelatoreModel"/>
<jsp:useBean id="tipoUfficioCompetente" scope="request" class="java.lang.String"/>

<jsp:useBean id="codDettagli"   scope="request" class="java.lang.String"/>
<jsp:useBean id="lFascicolo"    scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/>

<%
/* Check sospensione */
String lFascSospeso = "NO";
RicercaProvvedimentiUtil lRicerca = new RicercaProvvedimentiUtil(fascicoloSiusGP.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());  
if (lRicerca.verificaEsistenzaSospensione())
	lFascSospeso = "SI";
%>

<html>
<head>
  <title>[S.I.E.S.] - Emissione Decreto Incompetenza</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

  <script language="JavaScript">
    var desktop;
    function ListaComuni(a_formname,a_fieldname,codTipoUfficio)
    {
       desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    function ListaUDSTDS(a_formname,a_fieldname,a_ufficio)
    {
      node=document.getElementById(a_ufficio);
      if(node.value=="UDS")
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }else{
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Tribunali di Sorveglianza:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
    }
    // Funzione dei controlli formali della form

    function Verify()
    {
        var fascSospeso = '<%=lFascSospeso%>';
        
        // Controllo sospensione fascicolo
        if (fascSospeso == 'SI' )	{ 
        if (! confirm("Attenzione: per questo procedimento è presente un'ordinanza di rimessione atti. Procedere con l'emissione di un nuovo provvedimento ?" ))
             return false;
        }
      
      // Controllo obbligatorietà contenuto.
 /*     var contenuto=document.LoadInserisciDecretoIncompetenza.<%= ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>[document.LoadInserisciDecretoIncompetenza.<%= ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.selectedIndex].value;
      if(contenuto =="-")
      {
        alert("Il Campo Contenuto è obbligatorio");
        return false;
      }
*/
<% 		if (magistratorelatore.getEsperto() == null && magistratorelatore.getMagistrato() == null ) 
      {
%>			alert('Il Magistrato Relatore è obbligatorio');
        return false;
<% 		} 
			else if (magistratorelatore.getMagistrato() == null) 
			{
%>			alert('<%=magistratorelatore.getEsperto().getCognome()%> è un esperto, occorre un magistrato');
        return false;
<% 		
			} 
%>
      // Controllo validità data Emissione.
      var dataEmissione=document.LoadInserisciDecretoIncompetenza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value +'/'+
                        document.LoadInserisciDecretoIncompetenza.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value +'/'+
                        document.LoadInserisciDecretoIncompetenza.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

      if (! ControllaData(dataEmissione))
      {
        alert('Data emissione non valida!');
        return false;
      }
      
      // Ufficio competente è obbligatorio in caso di "Trasmissione Atti"
      if (document.LoadInserisciDecretoIncompetenza.<%=ICostantiMotivazioneDecreto.CAMPO_CK_01%>[1].checked )
      {
      	if (document.LoadInserisciDecretoIncompetenza.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>.value == "-")
      	{
       		alert ("L'Ufficio Competente è obbligatorio in caso di Trasmissione Atti !" );
      		return false;
      	}
      }	    
      return true;
    }
  </script>

  <script language="JavaScript">
    var desktop;
    // Chiamata funzione lista Oggetti
    function ListaOggetti(a_formname,a_field_contenuto, a_fieldname, a_fieldcodes, a_fieldcodesdet, i_fieldcodes, i_fieldcodesdet )
    {
      // Compone il link URL per passare i parametri alla ElencoUdienza.JSP
      var aLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadListaOggetti";
          aLink += "&formname="+a_formname;
          aLink += "&field_contenuto="+a_field_contenuto;
          aLink += "&fieldname="+a_fieldname;
          aLink += "&fieldcodes="+a_fieldcodes;
          aLink += "&fieldcodesdet="+a_fieldcodesdet;
          aLink += "&ifieldcodes="+i_fieldcodes;
          aLink += "&ifieldcodesdet="+i_fieldcodesdet;
      desktop = window.open(aLink, "Lista_Oggetti","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=760,height=500");
    }
  </script>
</head>

<body class="corpo">
  <table>
    <tr>
    	<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :  </font>&nbsp;
<%
        // Imposta l'azione da Chiamare.
        String lAzione = new String();
        lAzione = "siap.sius.depositodecreto.action.ActInserisciDecretoIncompetenza";
%>
        <font class="campo">Emissione Decreto Incompetenza</font>
      </td>
    </tr>
    
    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
    
    <tr>
    	<jsp:include page="<%=ICostantiMagistratoRelatore.PG_SINTESIMAGISTRATORELATORE%>">
    		<jsp:param name="MagRelRitorno" value="siap.sius.depositodecreto.action.ActLoadInserisciDecretoIncompetenza"/>
    	</jsp:include>
    </tr>

  </table>

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciDecretoIncompetenza">
  <table cellspacing=2 cellpadding=2>
    <!-- Sezione Contenuto Oggetti -->

    <tr>
      <td class="l">Data Emissione<font class="ob">(*)</font></td>
      <td class="L">
        <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> /
        <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> /
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
      </td>
    </tr>

  	<tr>
    	<td class="l">Contenuto<font class="ob">(*)</font></td>
    	<td class="L"> <%=contenuto%></td>
  	</tr>

    <tr>
      <td class="l">Oggetto<font class="ob">(*)</font></td>
        <td class="l">
          <Textarea Title="Oggetto" name="<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>" cols=88 rows=3 readonly><%=descOggetti%></Textarea>
          <a href="Javascript:ListaOggetti('LoadInserisciDecretoIncompetenza',document.LoadInserisciDecretoIncompetenza.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.value, '<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>', '<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>', '<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>', document.LoadInserisciDecretoIncompetenza.<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>.value, document.LoadInserisciDecretoIncompetenza.<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>.value );">
          <img src="/images/fileselected.gif" title="Oggetti per il Contenuto selezionato" border=0></a>
          &nbsp;
          <a href="Javascript:ListaOggetti('LoadInserisciDecretoIncompetenza','-', '<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>', '<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>', '<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>', document.LoadInserisciDecretoIncompetenza.<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>.value, document.LoadInserisciDecretoIncompetenza.<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>.value );">
          <img src="/images/filefolder.gif" title="Elenco di tutti gli Oggetti Selezionabili" border=0></a>
        </td>
    </tr>

    <tr>
    	<td class="l">Motivazioni</td>
      <td class="l"><Textarea title="Note" name="<%=ICostantiDepositoDecreto.CAMPO_NOTE%>" cols=88 rows=3></Textarea></td>
    </tr>

    <tr>
    	<td class="l" colspan=2> Dispone:</td>
    </tr>

    <tr>
    	<td class="l"><input value="C1" type="radio" name="<%=ICostantiMotivazioneDecreto.CAMPO_CK_01%>"></td>
      <td class="l">Restituzione degli atti</td>
    </tr>

    <tr>
      <td class="l"><input value="C2" type="radio" name="<%=ICostantiMotivazioneDecreto.CAMPO_CK_01%>"></td>
      <td class="l">Trasmissione degli atti</td>
    </tr>

    <tr>
      <td class="l"><input value="C0" type="radio" name="<%= ICostantiMotivazioneDecreto.CAMPO_CK_01%>" checked> </td>
      <td class="l">
        <input title="AltraMotivazione" name="<%= ICostantiMotivazioneDecreto.CAMPO_ALTRA_MOTIVAZIONE%>" value="" size="60" maxlength="60" >
      </td>
    </tr>
<%
     FascicoloGPModel lModel = new FascicoloGPModel();
%>
    <tr>
       <td class="l">Ufficio Competente<font class=ob>(*)</font></td>
       <td class="L">
         <select title="tipoUfficioCompetente" class=small name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>" >
           <%= tipoUfficioCompetente%>
         </select>

          <input Title="Sede Procura" name="<%=ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO%>"
             value="<%=lModel.getFascicoloSiusModel().getDescrComuneUfficio()%>" type="text" maxlength="35" size="35">
             <a href="Javascript:ListaComuni('LoadInserisciDecretoIncompetenza','<%= ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO %>',document.LoadInserisciDecretoIncompetenza.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>[document.LoadInserisciDecretoIncompetenza.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>.selectedIndex].value);">
             <img src="/images/filefolder.gif" border=0> </a>
       </td>
     </tr>

    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma" >
      </td>
    </tr>

    </table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>" value="<%=codOggetti%>">
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>" value="<%=codDettagli%>">
   	<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=codContenuto%>">

  </form>

  <script language="JavaScript" type="text/javascript">

    var frmvalidator = new Validator("LoadInserisciDecretoIncompetenza");
    // Controllo data emissione.
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req", "Il campo Giorno Data Emissione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req", "Il campo Mese Data Emissione é obbligatorio");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req", "Il campo Anno Data Emissione é obbligatorio");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");

    // Controllo campo oggetto.
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO%>", "req","E' necessario selezionare almeno un oggetto");
    //Chiama la funzione di Verify().
    frmvalidator.setAddnlValidationFunction("Verify");

  </script>

  </body>
</html>