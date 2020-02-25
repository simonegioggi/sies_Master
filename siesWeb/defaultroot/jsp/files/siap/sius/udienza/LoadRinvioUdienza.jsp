<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.udienza.action.ICostantiUdienza"%>
<%@ page import="siap.sius.generaleprocedimento.action.ICostantiGeneraleProcedimento"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="fascSospeso"   scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.S.] - Rinvio Udienza </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
  // Funzione dei controlli formali della form
  function Verify()
  {
    
    // Controllo validità data Udienza.
    var dataUdienza= document.LoadRinvioUdienza.<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>.value +'/'+
                     document.LoadRinvioUdienza.<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>.value +'/'+
                     document.LoadRinvioUdienza.<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>.value;
    if (dataUdienza == '//' &&
        (!document.LoadRinvioUdienza.<%=ICostantiUdienza.CAMPO_CHECK_RUOLO%>.checked)  )
    {
      alert('Indicare la nuova data o selezionare Nuovo ruolo!');
      return false;
    }

    if (dataUdienza.length == 10   &&
        (!document.LoadRinvioUdienza.<%=ICostantiUdienza.CAMPO_CHECK_RUOLO%>.checked)  )
    {
      if (! ControllaData(dataUdienza))
      {
        alert('Data Udienza non valida!');
        return false;
      }
    }
    if (dataUdienza.length == 10   &&
        (document.LoadRinvioUdienza.<%=ICostantiUdienza.CAMPO_CHECK_RUOLO%>.checked)  )
    {
      alert('Valorizzare Nuova data o selezionare Nuovo ruolo!');
      return false;
    }
    
    var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
    var data_emissione=	document.LoadRinvioUdienza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value +'/'+
                      	document.LoadRinvioUdienza.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value +'/'+
                      	document.LoadRinvioUdienza.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;
    

    // Set di controllo della data di emissione.
    if ( !ControllaData(data_emissione) )
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

    return true;
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

    // Funzione di pulizia data udienza se rinvio per ruolo.
    function CheckUncheck()
    {
      if (document.LoadRinvioUdienza.<%=ICostantiUdienza.CAMPO_CHECK_RUOLO%>.checked )
      {
        if (document.LoadRinvioUdienza.<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%> !=null)
          document.LoadRinvioUdienza.<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>.value = "";
        if (document.LoadRinvioUdienza.<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%> !=null)
          document.LoadRinvioUdienza.<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>.value = "";
        if (document.LoadRinvioUdienza.<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%> !=null)
          document.LoadRinvioUdienza.<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>.value = "";
        if (document.LoadRinvioUdienza.<%=ICostantiUdienza.CAMPO_NUM_COLLEGIO %> !=null)
          document.LoadRinvioUdienza.<%=ICostantiUdienza.CAMPO_NUM_COLLEGIO%>.value = "";
      }
    }
    function Uncheck()
    {
      document.LoadRinvioUdienza.<%=ICostantiUdienza.CAMPO_CHECK_RUOLO%>.value = "1";
      document.LoadRinvioUdienza.<%=ICostantiUdienza.CAMPO_CHECK_RUOLO%>.checked = false;
    }
    // Controllo sospensione fascicolo
    function ControlloSospensione()
    {
     <% if (fascSospeso.compareTo("SI") == 0)	{ %>
          if (! confirm("Attenzione: per questo procedimento è presente un'ordinanza di rimessione atti. Procedere con l'emissione di un nuovo provvedimento ?" ))
          {
              //str = "/jsp/Main.jsp?Action=siap.siep.ordineesecuzione.action.ActCancellaProvvedimento&" +a_parameter +"=" + a_entityname;
              	str = "/jsp/Main.jsp?Action=siap.sius.udienza.action.ActLoadFSPRinvioUdienza&";
                  window.location.href=str;
                  return false
         	} else
             	return true
      <% } %>
      return true;
    }
  </script>
</head>

<body class="corpo" 
	onLoad="javascript:document.LoadRinvioUdienza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();Javascript:return ControlloSospensione();">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label"> Funzione :</font>&nbsp;
        <font class="campo">Rinvio Udienza</font>
      </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
  </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadRinvioUdienza">
  <table cellspacing="2" cellpadding="2">
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
		<tr><td>&nbsp;</td></tr>
    <tr>
      <td class="label">Indicare se rinvio a : &nbsp;</td>
    </tr>

    <tr>
      <td class="l">Nuova Data Udienza </td>
      <td class="l" >
        <input type="text" readonly 
        			 name="<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA %>" 
        			 maxlength="2" size="2" 
        			 onBlur="javascript:value=FillDM(value);javascript:Uncheck()"> /
        <input type="text" readonly 
        			 name="<%= ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA %>" 
        			 maxlength="2" size="2" 
        			 onBlur="javascript:value=FillDM(value);javascript:Uncheck()"> /
        <input type="text" readonly 
        			 name="<%= ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>" 
        			 maxlength="4" size="4" 
        			 onBlur="javascript:Uncheck()">&nbsp;&nbsp;&nbsp;
        &nbsp; Num. Coll. <font class="ob">(*)</font>
        <input name="<%=ICostantiUdienza.CAMPO_NUM_COLLEGIO%>" 
        			 maxlength="2" size="2" readonly 
        			 onBlur="javascript:Uncheck()">
        <a href="Javascript:ElencoNuoveUdienze('LoadRinvioUdienza',
          '<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA %>',
          '<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA %>',
          '<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA %>',
          '<%=ICostantiUdienza.CAMPO_LUOGO_UDIENZA%>',
          '<%=ICostantiUdienza.CAMPO_ID_UDIENZA%>',
          '<%=ICostantiUdienza.CAMPO_NUM_COLLEGIO%>');">
          Lista Udienze stesso Collegio <img src="/images/filefolder.gif" border=0>
        </a>&nbsp;&nbsp;&nbsp;
        <a  href="Javascript:ListaUdienze('LoadRinvioUdienza',
          '<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA %>',
          '<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA %>',
          '<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA %>',
          '<%=ICostantiGeneraleProcedimento.CAMPO_ANNOTAZIONE%>',
          '<%=ICostantiUdienza.CAMPO_ID_UDIENZA%>',
          '<%=ICostantiUdienza.CAMPO_NUM_COLLEGIO%>');">
          Lista udienze <img src="/images/filefolder.gif" border=0>
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

    <tr>
      <td>&nbsp;</td>
    </tr>

    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma">
      </td>
    </tr>
  </table>

    <input type="HIDDEN" name="<%=ICostantiUdienza.CAMPO_ID_UDIENZA%>"  >
    <input type="HIDDEN" name="<%=ICostantiGeneraleProcedimento.CAMPO_ANNOTAZIONE%>"  >
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.udienza.action.ActRinvioUdienza">
    <input type="HIDDEN" name="<%=ICostantiUdienza.CAMPO_LUOGO_UDIENZA%>" >
  </form>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadRinvioUdienza");
    frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req", "Il campo Giorno Data Emissione è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req", "Il campo Mese Data Emissione é obbligatorio");
    frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req", "Il campo Anno Data Emissione é obbligatorio");
    frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");
    
    frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");

    //Chiama la funzione di Verify().
    frmvalidator.setAddnlValidationFunction("Verify");

  </script>

  </body>
</html>