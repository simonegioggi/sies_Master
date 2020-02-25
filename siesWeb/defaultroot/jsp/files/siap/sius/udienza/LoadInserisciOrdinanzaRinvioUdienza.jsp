<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.udienza.action.ICostantiUdienza"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.generaleprocedimento.action.ICostantiGeneraleProcedimento"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocatoFascicoloSius"%>

<!-- STUB 12/11/2003 Modifiche per la gestione del dettaglio oggetto (vedi fieldcodesdet). -->
<jsp:useBean id="UtenteConnesso" 			scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="avvocato"    				scope="request" class="java.util.Vector" />
<jsp:useBean id="magistratorelatore"  scope="request" class="siap.sius.magistratorelatore.model.MagistratoRelatoreModel"/>
<jsp:useBean id="contenuto"     			scope="request" class="java.lang.String"/>
<jsp:useBean id="codContenuto"     		scope="request" class="java.lang.String"/>
<jsp:useBean id="codOggetti"    			scope="request" class="java.lang.String"/>
<jsp:useBean id="descOggetti"   			scope="request" class="java.lang.String"/>
<jsp:useBean id="codDettagli"   			scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicoloSiusGP" 		scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="fascSospeso"   scope="request" class="java.lang.String"/>
<!-- numero mac: 20191108014 - 12/nov/2019 - monica -  aggiunta annotazione-->
<jsp:useBean id="annotazione"   scope="request" class="java.lang.String"/>


<html>
<head>
  <title>[S.I.E.S.] - Gestione Udienza </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<%
/* Estrazione della data udienza  o data iscrizione */
 String data1;
 if (fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio() != null)
  data1 = DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(),"dd/MM/yyyy");
 else
  data1 = DateUtils.getDateToString(fascicoloSiusGP.getFascicoloSiusModel().getDataIscrizione(),"dd/MM/yyyy");

%>
  <script language="JavaScript">
  // Funzione dei controlli formali della form
  function Verify()
  {
    var ritorno = true;
    
    var data_minima	= '<%=data1%>';
    var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
    var dataUdienza	= document.LoadInserisciOrdinanzaRinvioUdienza.<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>.value +'/'+
                      document.LoadInserisciOrdinanzaRinvioUdienza.<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>.value +'/'+
                      document.LoadInserisciOrdinanzaRinvioUdienza.<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>.value;
                     
    var data_emissione=	document.LoadInserisciOrdinanzaRinvioUdienza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value +'/'+
                      	document.LoadInserisciOrdinanzaRinvioUdienza.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value +'/'+
                      	document.LoadInserisciOrdinanzaRinvioUdienza.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

    // Set di Controlli di validità data Udienza.
    // 1) Controllo se il campo data Udienza sia stata compilato
    if (dataUdienza == '//' &&
        (!document.LoadInserisciOrdinanzaRinvioUdienza.<%=ICostantiUdienza.CAMPO_CHECK_RUOLO%>.checked)  )
    {
      alert('Indicare la nuova data o selezionare Nuovo ruolo!');
      return false;
    }
	// 2) Controllo se il campo dataUdienza abbia una lunghezza di 10 chars, e che il campo
	// check box ruolo non sia stato segnato.
    if (dataUdienza.length == 10   &&
        (!document.LoadInserisciOrdinanzaRinvioUdienza.<%=ICostantiUdienza.CAMPO_CHECK_RUOLO%>.checked)  )
    {
      // Esegue il controllo formale della data.
      if (! ControllaData(dataUdienza))
      {
        alert('Data Udienza non valida!');
        return false;
      }
    }

    if (dataUdienza.length == 10   &&
        (document.LoadInserisciOrdinanzaRinvioUdienza.<%=ICostantiUdienza.CAMPO_CHECK_RUOLO%>.checked)  )
    {
      alert('Valorizzare Nuova data o selezionare Nuovo ruolo!');
      return false;
    }

    // Set di controllo della data di emissione.
    if (ritorno && (! ControllaData(data_emissione)))
    {
      alert('Data emissione non valida: '+ data_emissione );
      return false;
    }
    //1) Controllo data di sistema >= Data Emissione .
    else if( !CompareDate( data_emissione, data_sistema) )
    {
      alert('Data Emissione non può essere superiore alla data odierna!');
      return false;
    }

    <%
    // Verifica se sia stato assegnato un magistrato Realatore o un Esperto.
    if (magistratorelatore.getEsperto() == null && magistratorelatore.getMagistrato() == null)	
    { 
    %>
  		ritorno = false;
    <% 
    } 
    %>
    if (! ritorno)
    {
      alert (" Magistrato Relatore non assegnato");
      return false;
    }
    
    // Controllo avvocato solo per TDS
    <%
    if ( UtenteConnesso.getUfficioUtente().getCodTipoUfficio().compareTo("TDS") == 0)
    {
      int numAvvocati = avvocato.size();
      
      if ( numAvvocati == 0 )
      { 
    %>
        ritorno = false;
        alert('Avvocato obbligatorio!');
    <%
      }
    }
    %>
    
    return ritorno;
  }
  </script>

  <script language="JavaScript">
    var desktop;

    // Chiamata funzione elenco Udienze.
    function ListaUdienze( aNomeForm, aNomeCampoGG, aNomeCampoMM, aNomeCampoAA, aNomeCampoLuogo, aNomeCampoIdUdienza, aNomeCampoCollegio)
    {
      // Compone il link URL per passare i parametri alla ElencoUdienza.JSP
      var lLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.udienza.action.ActLoadRicercaUdienzaXProcedimenti";
          lLink += "&formname="+ aNomeForm;
          lLink += "&campoGG=" + aNomeCampoGG;
          lLink += "&campoMM=" + aNomeCampoMM;
          lLink += "&campoAA=" + aNomeCampoAA;
          lLink += "&campoLuogo=" + aNomeCampoLuogo;
          lLink += "&campoID=" + aNomeCampoIdUdienza;
          lLink += "&campoColl=" + aNomeCampoCollegio;

      desktop = window.open(lLink, "ElencoUdienza","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=850,height=600" );
    }

    // Chiamata funzione elenco date per nuova udienza
    function ElencoNuoveUdienze( aNomeForm, aNomeCampoGG, aNomeCampoMM, aNomeCampoAA, aNomeCampoLuogo, aIdUdienza ,aNomeCampoColl)
    {
      Uncheck();
      // Compone il link URL per passare i parametri alla ElencoUdienza.JSP
      var lLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.udienza.action.ActElencoNuoveUdienze";
          lLink += "&formname="+ aNomeForm;
          lLink += "&campoGG=" + aNomeCampoGG;
          lLink += "&campoMM=" + aNomeCampoMM;
          lLink += "&campoAA=" + aNomeCampoAA;
          lLink += "&campoLuogo=" + aNomeCampoLuogo;
          lLink += "&campoID=" + aIdUdienza;
          lLink += "&campoColl=" + aNomeCampoColl;

      desktop = window.open(lLink, "ElencoUdienza", "toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=850,height=600" );
    }

    // Chiamata funzione lista dei comuni.
    function ListaComuni(a_formname,a_fieldname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

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

  <script language="JavaScript">

    // Funzione di pulizia data udienza se rinvio per ruolo.
    function CheckUncheck()
    {
      if (document.LoadInserisciOrdinanzaRinvioUdienza.<%=ICostantiUdienza.CAMPO_CHECK_RUOLO%>.checked )
      {
        if (document.LoadInserisciOrdinanzaRinvioUdienza.<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%> !=null)
          document.LoadInserisciOrdinanzaRinvioUdienza.<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>.value = "";
        if (document.LoadInserisciOrdinanzaRinvioUdienza.<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%> !=null)
          document.LoadInserisciOrdinanzaRinvioUdienza.<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>.value = "";
        if (document.LoadInserisciOrdinanzaRinvioUdienza.<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%> !=null)
          document.LoadInserisciOrdinanzaRinvioUdienza.<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>.value = "";
        if (document.LoadInserisciOrdinanzaRinvioUdienza.<%=ICostantiUdienza.CAMPO_NUM_COLLEGIO %> !=null)
          document.LoadInserisciOrdinanzaRinvioUdienza.<%=ICostantiUdienza.CAMPO_NUM_COLLEGIO%>.value = "";
      }
    }

    function Uncheck()
    {
      document.LoadInserisciOrdinanzaRinvioUdienza.<%=ICostantiUdienza.CAMPO_CHECK_RUOLO%>.value = "1";
      document.LoadInserisciOrdinanzaRinvioUdienza.<%=ICostantiUdienza.CAMPO_CHECK_RUOLO%>.checked = false;
    }

    // Controllo sospensione fascicolo
    function ControlloSospensione()
    {
     <% if (fascSospeso.compareTo("SI") == 0)	{ %>
          if (! confirm("Attenzione: per questo procedimento è presente un'ordinanza di rimessione atti. Procedere con l'emissione di un nuovo provvedimento ?" ))
          {
              //str = "/jsp/Main.jsp?Action=siap.siep.ordineesecuzione.action.ActCancellaProvvedimento&" +a_parameter +"=" + a_entityname;
              	str = "/jsp/Main.jsp?Action=siap.sius.udienza.action.ActLoadFSPOrdinanzaRinvioUdienza&";
                  window.location.href=str;
                  return false
         	} else
             	return true
      <% } %>
      return true;
    }
  </script>


</head>

<%
			String lAzione = new String();
    	lAzione = "siap.sius.udienza.action.ActInserisciOrdinanzaRinvioUdienza";
    	String lActRet = new String("siap.sius.udienza.action.ActLoadInserisciOrdinanzaRinvioUdienza");
%>


<body class="corpo" 
	onLoad="javascript:document.LoadInserisciOrdinanzaRinvioUdienza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();Javascript:return ControlloSospensione();">

  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label"> Funzione :</font>&nbsp;
        <font class="campo">Ordinanza Rinvio Udienza</font>
      </td>
    </tr>
    
    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>

  </table>
  
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciOrdinanzaRinvioUdienza">
	<table cellspacing=2 cellpadding=2 width="100%">
	 <tr>
      <jsp:include page="<%=ICostantiMagistratoRelatore.PG_SINTESIMAGISTRATORELATORE%>">
      	<jsp:param name="MagRelRitorno" value="<%=lActRet%>"/>
      </jsp:include>
     </tr>
  </table>
    
    <jsp:include page="<%=ICostantiAvvocatoFascicoloSius.PG_INCLUDE_AVVOCATI%>">
      <jsp:param name="AvvRitorno" value="<%=lActRet%>"/>
    </jsp:include>
    
    <table cellspacing=2 cellpadding=2 width="100%">

    <!-- Sezione Contenuto Oggetti -->
    <tr>
      <td class="Titolo" colspan=6>  </td>
    </tr>
    <tr>
      <td class="l">Data Emissione<font class="ob">(*)</font></td>
      <td class="L">
        <input type="text" size="2" maxlength="2" 
        			 name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>" 
        			 onFocus="javascript:textboxSelect(this)" 
        			 onkeypress="return TicTabNumField(this,event)"  
        			 onBlur="javascript:value=FillDM(value)"> /
        <input type="text" size="2" maxlength="2" 
        			 name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>" 
        			 onFocus="javascript:textboxSelect(this)" 
        			 onkeypress="return TicTabNumField(this,event)"  
        			 onBlur="javascript:value=FillDM(value)"> /
        <input type="text" size="4" maxlength="4" 
        			 name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>" 
        			 onFocus="javascript:textboxSelect(this)" 
        			 onkeypress="return TicTabNumField(this,event)"  
        			 onBlur="javascript:value=FillYear(value)" >
      </td>
    </tr>
    
    <tr>
      <td class="label">Indicare se rinvio a : &nbsp;</td>
    </tr>

    <tr>
      <td class="l">Nuova Data Udienza </td>
       <%-- numero mac: 20191108014 - 06/dic/2019 - monica --> dentro ListaUdienze modificato il campo CAMPO_ANNOTAZIONE con CAMPO_LUOGO_UDIENZA --%>
      <td class="l" >
        <input type="text" readonly 
        			 name="<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>" 
        			 maxlength="2" size="2" 
        			 onBlur="javascript:value=FillDM(value);javascript:Uncheck()"> /
        <input type="text" readonly 
        			 name="<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>" 
        			 maxlength="2" size="2" 
        			 onBlur="javascript:value=FillDM(value);javascript:Uncheck()"> /
        <input type="text" readonly 
        			 name="<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>" 
        			 maxlength="4" size="4" 
        			 onBlur="javascript:Uncheck()">
        &nbsp;&nbsp;&nbsp;
        &nbsp; Num. Coll. <font class="ob">(*)</font>
        <input name="<%=ICostantiUdienza.CAMPO_NUM_COLLEGIO%>" 
        			 maxlength="2" size="2" 
        			 readonly 
        			 onBlur="javascript:Uncheck()">
        <a href="Javascript:ElencoNuoveUdienze('LoadInserisciOrdinanzaRinvioUdienza',
        									   '<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA %>',
          									   '<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA %>',
          									   '<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA %>',
											   '<%=ICostantiUdienza.CAMPO_LUOGO_UDIENZA%>',
									           '<%=ICostantiUdienza.CAMPO_ID_UDIENZA%>',
										       '<%=ICostantiUdienza.CAMPO_NUM_COLLEGIO%>');">
											   Lista Udienze stesso Collegio
						<img src="/images/filefolder.gif" border=0>
        </a>&nbsp;&nbsp;&nbsp;
        <a  href="Javascript:ListaUdienze('LoadInserisciOrdinanzaRinvioUdienza',
                                          '<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA %>',
                                          '<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA %>',
                                          '<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA %>',
                                          '<%=ICostantiUdienza.CAMPO_LUOGO_UDIENZA%>',
                                          '<%=ICostantiUdienza.CAMPO_ID_UDIENZA%>',
                                          '<%=ICostantiUdienza.CAMPO_NUM_COLLEGIO%>');">
                                          Lista udienze
        		<img src="/images/filefolder.gif" border=0>
        </a>
      </td>

    </tr>

    <tr>
      <td class="label">oppure &nbsp;</td>
    </tr>

    <tr>
      <td class="l">Nuovo ruolo &nbsp;</td>
      <td class="l"><input type=checkbox name="<%=ICostantiUdienza.CAMPO_CHECK_RUOLO%>" value=0 onClick ="Javascript:CheckUncheck()"> </td>
    </tr>

    <tr><td>&nbsp;</td></tr>

  <tr>
    <td class="l">Contenuto<font class="ob">(*)</font></td>
    <td class="L"> <%=contenuto%></td>
  </tr>

    <tr>
      <td class="l">Oggetto<font class="ob">(*)</font></td>
        <td class="l">
          <Textarea Title="Oggetto" name="<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>" cols=95 rows=3 readonly><%=descOggetti%></Textarea>
          <a href="Javascript:ListaOggetti('LoadInserisciOrdinanzaRinvioUdienza','<%=codContenuto%>', '<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>', '<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>', '<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>', document.LoadInserisciOrdinanzaRinvioUdienza.<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>.value, document.LoadInserisciOrdinanzaRinvioUdienza.<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>.value );">
          <img src="/images/fileselected.gif" title="Oggetti per il Contenuto selezionato" border=0></a>
          &nbsp;
          <a href="Javascript:ListaOggetti('LoadInserisciOrdinanzaRinvioUdienza','-', '<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>', '<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>', '<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>', document.LoadInserisciOrdinanzaRinvioUdienza.<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>.value, document.LoadInserisciOrdinanzaRinvioUdienza.<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>.value );">
          <img src="/images/filefolder.gif" title="Elenco di tutti gli Oggetti Selezionabili" border=0></a>
        </td>
    </tr>
    <tr><td>&nbsp;</td></tr>

    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma">
      </td>
    </tr>

    </table>

    <input type="HIDDEN" name="<%=ICostantiUdienza.CAMPO_ID_UDIENZA%>"  >
    <input type="HIDDEN" name="<%=ICostantiUdienza.CAMPO_LUOGO_UDIENZA %>" >
    <!-- numero mac: 20191108014 - 12/nov/2019 - monica -  aggiunta valorizzazione attributo annotazione-->   
    <input type="HIDDEN" name="<%=ICostantiGeneraleProcedimento.CAMPO_ANNOTAZIONE%>"  value="<%=annotazione%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>" value="<%=codOggetti%>" >
    <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>">
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>"  value="<%=codDettagli%>" >
   	<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=codContenuto%>">

  </form>

  <script language="JavaScript" type="text/javascript">

     var frmvalidator = new Validator("LoadInserisciOrdinanzaRinvioUdienza");
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