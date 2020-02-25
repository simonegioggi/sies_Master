<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.stralcio.action.ICostantiStralcio"%>

<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita" scope="request" class="java.lang.String" />

<%
/* Estrazione della data udienza o data iscrizione */
 String data1;
 if (fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio() != null)
  data1 = DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(),"dd/MM/yyyy");
 else
  data1 = DateUtils.getDateToString(fascicoloSiusGP.getFascicoloSiusModel().getDataIscrizione(),"dd/MM/yyyy");
%>

<html>
  <head>
    <title>[S.I.E.S.] - Load Inserisci stralcio SIUS</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>

    <script language="JavaScript">
      var desktop;
      function  Verifica()
      {
        var ritorno = true;
        var data_minima = '<%=data1%>';
        var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
        var data_stralcio = document.LoadInserisciStralcio.<%=ICostantiStralcio.CAMPO_GIORNO_DATA_STRALCIO%>.value+'/'+document.LoadInserisciStralcio.<%=ICostantiStralcio.CAMPO_MESE_DATA_STRALCIO%>.value+'/'+document.LoadInserisciStralcio.<%=ICostantiStralcio.CAMPO_ANNO_DATA_STRALCIO%>.value;

        // Controllo della data di Stralcio
        if (ritorno && (! ControllaData(data_stralcio)))
        {
          alert('Data Stralcio non valida: '+ data_stralcio );
          document.LoadInserisciStralcio.<%=ICostantiStralcio.CAMPO_GIORNO_DATA_STRALCIO%>.focus();
          return false;
        }
        // Controllo data di sistema >= Data Stralcio .
        else if( !CompareDate( data_stralcio, data_sistema) )
        {
          alert('Data Stralcio non può essere superiore alla data odierna!');
          document.LoadInserisciStralcio.<%=ICostantiStralcio.CAMPO_GIORNO_DATA_STRALCIO%>.focus();
          ritorno =  false;
        }
        else if ( !CompareDate( data_minima, data_stralcio) )
        {
          alert("Data Stralcio non può precedere: " + data_minima);
          document.LoadInserisciStralcio.<%=ICostantiStralcio.CAMPO_GIORNO_DATA_STRALCIO%>.focus();
          ritorno = false;
        }

        // Controllo selezione oggetti.
        var totale_oggetti=<%=fascicoloSiusGP.getTenori().length%>;
        var oggetti_selezionati=0;
<%
				for(int i=0;i<fascicoloSiusGP.getTenori().length;i++)
				{
%>
        	if ( document.LoadInserisciStralcio.<%=ICostantiStralcio.CAMPO_CHECKBOX%><%=i%>.checked)
        	oggetti_selezionati++;

      <%}%>

				if (oggetti_selezionati==0)
        {
          alert("Nessun oggetto selezionato. Stralcio impossibile.");
          ritorno = false;
        }
				if (oggetti_selezionati==totale_oggetti)
        {
          alert("Selezionati tutti gli oggetti. Stralcio impossibile.");
          ritorno = false;
        }

        // Controllo procedimento esistente destinatario dello stralcio.
        if (document.LoadInserisciStralcio.tipoStralcio[1].checked)
        {
        	var anno_sistema='<%=DateUtils.getSysDate("yyyy")%>'
        	var chiave_anno=document.LoadInserisciStralcio.<%= ICostantiStralcio.CAMPO_CHIAVE_ANNO%>.value;
        	var chiave_progr=document.LoadInserisciStralcio.<%= ICostantiStralcio.CAMPO_CHIAVE_PROGR%>.value;
        	if(chiave_anno == '')
        	{
          	alert("Valorizzare l'Anno del procedimento destinazione dello stralcio!");
          	document.LoadInserisciStralcio.<%=ICostantiStralcio.CAMPO_CHIAVE_ANNO%>.focus();
          	return false;
        	}
        	if(chiave_progr == ''	)
        	{
          	alert("Valorizzare il Numero del procedimento destinazione dello stralcio!");
          	document.LoadInserisciStralcio.<%=ICostantiStralcio.CAMPO_CHIAVE_PROGR%>.focus();
          	return false;
        	}

        	// Controllo chiave anno minimo. STUB 16/04/2007 Anno minimo cambiato da 1995 a 1990.
        	var anno_minimo='1990'
        	if(chiave_anno < anno_minimo)
        	{
          	alert("Il Campo Anno Procedimento non è valido");
          	ritorno = false;
        	}
        	// Controllo chiave anno <= anno sistema.
        	if(chiave_anno > anno_sistema)
        	{
          	alert("Il Campo Anno Procedimento non può superare l'anno corrente");
          	ritorno = false;
        	}
      	}
        return ritorno;
      }

      function radioBase()
      {
				document.LoadInserisciStralcio.<%=IWebConstants.ACTION_FIELD%>.value="siap.sius.stralcio.action.ActInserisciStralcio";
				var nodeProcedimento;

				nodeProcedimento=document.getElementById('divProcedimento');
				if(document.LoadInserisciStralcio.tipoStralcio[0].checked)
				{
          nodeProcedimento.style.visibility='hidden';
				}
        else if (document.LoadInserisciStralcio.tipoStralcio[1].checked )
				{
          nodeProcedimento.style.visibility='visible';
				}
        document.LoadInserisciStralcio.<%=ICostantiStralcio.CAMPO_GIORNO_DATA_STRALCIO%>.focus();
      }

    </script>

  </head>
<%
    String lAction = "";
    String lTitolo = "";

    if (modalita.equalsIgnoreCase("inserimento"))
    {
       lAction = "siap.sius.stralcio.action.ActInserisciStralcio";
       lTitolo = "Inserimento Stralcio Procedimento";
    } else if (modalita.equalsIgnoreCase("dettaglio"))
    {
       lTitolo = "Dettaglio Stralcio Procedimento";
    } else if (modalita.equalsIgnoreCase("modifica"))
    {
       lTitolo = "Modifica Stralcio Procedimento";
       lAction = "siap.sius.stralcio.action.ActModificaStralcio";
    }
%>

  <body class="corpo" onload="Javascript:radioBase();">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione : Stralcio Oggetti </font>&nbsp;
          <font class="campo"><%=lTitolo%></font>
      </td>
    	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>

    <tr>
    	<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
  </table>


  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciStralcio">
    <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="l">Data Stralcio<font class="ob">(*)</font></td>
      <td class="L">
        <input type="text" size="2" maxlength="2" name="<%=ICostantiStralcio.CAMPO_GIORNO_DATA_STRALCIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > /
        <input type="text" size="2" maxlength="2" name="<%=ICostantiStralcio.CAMPO_MESE_DATA_STRALCIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > /
        <input type="text" size="4" maxlength="4" name="<%=ICostantiStralcio.CAMPO_ANNO_DATA_STRALCIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>

    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="Titolo" colspan="2">Specificare esito per ciascun oggetto&nbsp;&nbsp;</td>
    </tr>

    <tr>
      <td class="Cliccabile" width="60%">Oggetto&nbsp;&nbsp;</td>
      <td class="Cliccabile" width="40%">Selezionare x stralciare</td>
    </tr>

<%
		for(int i=0;i<fascicoloSiusGP.getTenori().length;i++)
		{
%>
    <tr>
      <td class="l" width="60%"><%=fascicoloSiusGP.getTenori()[i].getDescrOggettoTenore()%></td>
      <td class="l">
        <input type="checkbox" name="<%=ICostantiStralcio.CAMPO_CHECKBOX%><%=i%>">
      </td>
    </tr>
  <%}%>
	<br>

  <tr><td class="Titolo" colspan="2">Specificare il Tipo di stralcio</td></tr>
  <tr>
     <td class="c" colspan="2">Stralcio in un nuovo procedimento&nbsp;<input type="radio" name="tipoStralcio" value="nuovoProcedimento" checked  onClick="radioBase();">
                   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Stralcio in un procedimento esistente&nbsp;<input type="radio" name="tipoStralcio" value="vecchioProcedimento"  onClick="radioBase();">
     </td>
  </tr>
 	</table>

  <div id="divProcedimento" style="visibility:hidden; position:relative; top:0px; width:100%;">
	  <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          						Anno <input title="Anno Procedimento" type="text" name="<%=ICostantiStralcio.CAMPO_CHIAVE_ANNO%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)"></td>
        <td class="l">Progressivo <input title="Numero Procedimento" type="text" name="<%=ICostantiStralcio.CAMPO_CHIAVE_PROGR%>" maxlength="5" size="5"></td>
      </tr>
	 	</table>
	</div>

  <tr>
    <td>
      <input class="bottone" type="submit" value="Conferma">
    </td>
 	</tr>

  </table>

  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
  <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>" >

  </FORM>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadInserisciStralcio");

    if (document.LoadInserisciStralcio.tipoStralcio[1].checked)
    {
	    frmvalidator.addValidation("<%= ICostantiStralcio.CAMPO_CHIAVE_ANNO%>","req", "Il campo Anno Procedimento è obbligatorio");
	    frmvalidator.addValidation("<%= ICostantiStralcio.CAMPO_CHIAVE_PROGR%>","req", "Il campo Progressivo Procedimento è obbligatorio");
	    frmvalidator.addValidation("<%= ICostantiStralcio.CAMPO_CHIAVE_ANNO%>","numeric");
    }
    frmvalidator.addValidation("<%= ICostantiStralcio.CAMPO_GIORNO_DATA_STRALCIO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiStralcio.CAMPO_MESE_DATA_STRALCIO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiStralcio.CAMPO_ANNO_DATA_STRALCIO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiStralcio.CAMPO_ANNO_DATA_STRALCIO%>","minlen=4","La lunghezza del campo Anno Data Stralcio deve essere di 4 caratteri");

    frmvalidator.addValidation("<%= ICostantiStralcio.CAMPO_GIORNO_DATA_STRALCIO%>","req", "Il campo Giorno Data Stralcio è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiStralcio.CAMPO_GIORNO_DATA_STRALCIO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiStralcio.CAMPO_MESE_DATA_STRALCIO%>","req", "Il campo Mese Data Stralcio è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiStralcio.CAMPO_MESE_DATA_STRALCIO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiStralcio.CAMPO_ANNO_DATA_STRALCIO%>","req", "Il campo Anno Data Stralcio è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiStralcio.CAMPO_ANNO_DATA_STRALCIO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiStralcio.CAMPO_ANNO_DATA_STRALCIO%>","minlen=4","Il campo Anno Data Stralcio deve essere di 4 caratteri");

    //Chiama la funzione di Verify().
    frmvalidator.setAddnlValidationFunction("Verifica");

  </script>

  </body>
</html>