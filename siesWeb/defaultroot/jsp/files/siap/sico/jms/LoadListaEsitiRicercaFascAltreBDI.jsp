<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.sico.jms.action.ICostantiSicoJMS"%>
<%@ page import="siap.siep.jms.action.ICostantiSiepJMS"%>
<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="tipoOperazione" scope="request" class="java.lang.String"/>
<jsp:useBean id="descrComune" scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficiAccorpati" scope="request" class="java.util.Vector" />

<html>
<head>
  <title>[S.I.E.S.] - Ricerche Altre BDI - Lista Esiti Ricerca Fascicolo SIEP altre BDI </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
    function Verify()
    {
      var data_inizio=document.LoadListaEsitiRicercaFascAltreBDI.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>.value+'/'+document.LoadListaEsitiRicercaFascAltreBDI.<%=ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>.value+'/'+document.LoadListaEsitiRicercaFascAltreBDI.<%=ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>.value;
      var data_fine=document.LoadListaEsitiRicercaFascAltreBDI.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>.value+'/'+document.LoadListaEsitiRicercaFascAltreBDI.<%=ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>.value+'/'+document.LoadListaEsitiRicercaFascAltreBDI.<%=ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>.value;

      if(!ControllaDataPassaVuota(data_inizio))
      {
        alert('Data iniziale non valida');
        return false;
      }
      if(!ControllaDataPassaVuota(data_fine))
      {
        alert('Data finale non valida');
        return false;
      }

      if(!(data_inizio.length==2 || data_fine.length==2) )
      {
        if(!CompareDate(data_inizio,data_fine))
        {
          alert('La Data di ricerca finale non può essere inferiore alla data iniziale');
          return false;
        }
      }
      // Controlli di incrocio Campi dell'ufficio.
      if (document.LoadListaEsitiRicercaFascAltreBDI.<%=ICostantiSicoJMS.CAMPO_TIPO_UFFICIO%>.value!="-" &&
          document.LoadListaEsitiRicercaFascAltreBDI.<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>.value=="")
      {
        alert('Valorizzare la Sede Ufficio');
        document.LoadListaEsitiRicercaFascAltreBDI.<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>.focus();
        return false;
      }
      
      if (document.LoadListaEsitiRicercaFascAltreBDI.<%=ICostantiSicoJMS.CAMPO_TIPO_UFFICIO%>.value=="-" &&
          document.LoadListaEsitiRicercaFascAltreBDI.<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>.value!="")
      {
        alert("Valorizzare l' Ufficio");
        document.LoadListaEsitiRicercaFascAltreBDI.<%=ICostantiSicoJMS.CAMPO_TIPO_UFFICIO%>.focus();
        return false;
      }
      loadNumProgOrigin();
      return true;
    }

    function TrasformaRes(a_formname,a_fieldname,a_fieldname2)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.util.ActCalcolaNumeroRes&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname2="+a_fieldname2,"Calcola_Numero_Res","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

    function TrasformaPret(a_formname,a_fieldname,a_fieldname2)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.util.ActCalcolaNumeroPret&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname2="+a_fieldname2,"Calcola_Numero_Res","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

    function ListaUffici(a_formname,a_fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }

    function ListaDistretti(a_formname,a_fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }

    function ListaUfficiMinor(a_formname,a_fieldname) {
    	// desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname+"&minor=yes", "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadUfficiMinorDistretto&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }
    
    function ChoosePopup()
    {
        var selectTipoUfficio = document.LoadListaEsitiRicercaFascAltreBDI.<%=ICostantiSicoJMS.CAMPO_TIPO_UFFICIO%>;
        var indiceTipoUfficio = selectTipoUfficio.options.selectedIndex;
        var codTipoUfficio = selectTipoUfficio[indiceTipoUfficio].value;
        if (codTipoUfficio == 'PM'){
      	  	 ListaUffici('LoadListaEsitiRicercaFascAltreBDI','<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>');
         } else if (codTipoUfficio == 'PGCAP'){
        	 ListaDistretti('LoadListaEsitiRicercaFascAltreBDI','<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>');
         } else if (codTipoUfficio == 'PMM'){
        	 ListaUfficiMinor('LoadListaEsitiRicercaFascAltreBDI','<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>');
         }
    }
  </script>

  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>

</head>
  <body class="corpo" onLoad="loadUfficiAccorpatiByDesc();">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Esiti Ricerca Fascicolo SIEP da altre BDI </font>
        </td>
      </tr>
    </table>
    
    <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadListaEsitiRicercaFascAltreBDI'>
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.jms.action.ActListaEsitiRicercaFascAltreBDI">

      <table cellspacing=2 cellpadding=2>
        <tr>
          <td class="Titolo" colspan="6"> Selezione del Procedimento cercato</td>
        </tr>
        <tr>
          <td class="L"> Anno/Numero Procedimento &nbsp;&nbsp;</td>
          <td class="L">
            <input type="text" title="Anno Procedimento" name="<%=ICostantiSicoJMS.CHIAVE_ANNO_SIEP%>" maxlength="4" size="4">
            /
            <input type="text" title="Numero Procedimento" name="<%=ICostantiSicoJMS.CHIAVE_PROGR_SIEP_ORIGIN%>" maxlength="14" size="14">
            <input type="hidden" name="<%=ICostantiSicoJMS.CHIAVE_PROGR_SIEP%>" value="">
            &nbsp;&nbsp;<a href="Javascript:TrasformaRes('LoadListaEsitiRicercaFascAltreBDI','<%=ICostantiSicoJMS.CHIAVE_ANNO_SIEP%>','<%=ICostantiSicoJMS.CHIAVE_PROGR_SIEP_ORIGIN%>');">
            R.E.S.<img src="/images/filefolder.gif" border=0></a>&nbsp;&nbsp;&nbsp;&nbsp;
            <a href="Javascript:TrasformaPret('LoadListaEsitiRicercaFascAltreBDI','<%=ICostantiSicoJMS.CHIAVE_ANNO_SIEP%>','<%=ICostantiSicoJMS.CHIAVE_PROGR_SIEP_ORIGIN%>');">
            P.T.<img src="/images/filefolder.gif" border=0></a>
          </td>
        </tr>

        <tr>
          <td class="L">Ufficio </td>
          <td class="l">
            <select name="<%=ICostantiSicoJMS.CAMPO_TIPO_UFFICIO%>" onchange="javascript:resetField();">
            <option value="-" >-</option>
            <option value="PM" >PROCURA REPUBBLICA PRESSO TRIBUNALE</option>
            <option value="PMM">PROCURA DELLA REPUBBLICA PRESSO IL TRIBUNALE PER I MINORENNI</option>
            <option value="PGCAP">PROCURA GENERALE PRESSO CORTE D'APPELLO</option>
            </select>
          </td>
        </tr>

        <tr>
          <td class="l">Sede </td><td class="L">
            <input title="Sede Autorita Esterna"  type="text" name="<%= ICostantiSiepJMS.CAMPO_SEDE_UFFICIO %>" value="<%=descrComune%>" maxlength="35" size="35"  readonly="readonly">
            <input type="hidden" Title="distrettoUffcio" name="distrettoUffcio" value="" size=35 >
            <a href="Javascript:ChoosePopup();">
            <img src="/images/filefolder.gif" border=0></a>
          </td>
        </tr>

        <tr>
          <td class="l">Ufficio Accorpato</td><td class="L">
         	<select name="<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO_ACCORPATO%>">
         	<option value="0" >-</option>
         	</select>
          </td>
        </tr>

      </table>

      <table cellspacing=2 cellpadding=2>
        <tr>
          <td class="Titolo" colspan="6"> Selezione della Data di ricerca </td>
        </tr>
        <tr>
          <td class="l">
            <table cellspacing=2 cellpadding=2>
              <tr>
                <td class="label">Dalla data &nbsp;</td>
                <td class="label">
                  <input Title="Data di ricerca inizio" type="text" name="<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
                   -
                  <input Title="Data di ricerca inizio" type="text" name="<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
                   -
                  <input Title="Data di ricerca inizio" type="text" name="<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
                </td>

                <td class="label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Alla data </td>
                <td class="label">
                  <input Title="Data di ricerca fine" type="text" name="<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
                   -
                  <input Title="Data di ricerca fine" type="text" name="<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
                   -
                  <input Title="Data di ricerca fine" type="text" name="<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>

      <table cellspacing=2 cellpadding=2>
        <tr>
          <td class="Titolo">
            Selezione del Tipo Esito
          </td>
        </tr>
        <tr>
          <%-- Esito --%>
          <td class="l">
                  <table cellspacing=2 cellpadding=2>
              <tr>
          <td class="label" >
            <input type=radio name="<%=ICostantiSicoJMS.CAMPO_TIPO_ESITO%>" value=0 CHECKED ></td>
          </td>
          <td class="label" >
            Tutti
          </td>
        </tr>

        <tr>
          <td class="label" >
            <input type=radio name="<%=ICostantiSicoJMS.CAMPO_TIPO_ESITO%>" value=1></td>
          </td>
          <td class="label" >
            In attesa di risposta&nbsp;
          </td>
        </tr>

        <tr>
          <td class="label" >
            <input type=radio name="<%=ICostantiSicoJMS.CAMPO_TIPO_ESITO%>" value=2></td>
          </td>
          <td class="label" >
            Esito Positivo
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>

<table cellspacing=2 cellpadding=2>
  <tr>
    <td class="Titolo">
      Selezione dell'utente che ha effettuato la ricerca
    </td>
  </tr>
  <tr>
    <td class="l">
      <table cellspacing=2 cellpadding=2>
        <tr>
          <td class="label" >
            <input type=radio name="<%=ICostantiSicoJMS.CAMPO_TIPO_UTENTE%>" value=0 ></td>
          </td>
          <td class="label" >
            Tutti
          </td>
        </tr>
        <tr>
          <td class="label" >
            <input type=radio name="<%=ICostantiSicoJMS.CAMPO_TIPO_UTENTE%>" value=1 CHECKED ></td>
          </td>
          <td class="label" >
            Utente Collegato
          </td>
        </tr>

        <tr>
          <td class="label" >
            <input type=radio name="<%=ICostantiSicoJMS.CAMPO_TIPO_UTENTE%>" value=2></td>
          </td>
          <td class="label" >
            Utente con codice :&nbsp;&nbsp;&nbsp;&nbsp;
          </td>
          <td><input Title="Codice Utente" type="text" name="<%= ICostantiSicoJMS.CAMPO_COD_UTENTE %>" maxlength="11" size="8" ></td>
        </tr>
      </table>
    </td>
  </tr>
</table>

      <BR>
      <table cellspacing=2 cellpadding=2>
        <tr>
          <td>
            <input onclick="Javascript:return Verify();" class="bottone" type="submit" name="RICERCA" value="Ricerca">
          </td>
        </tr>
      </table>
    </form>

    <script language="JavaScript" type="text/javascript">

      var frmvalidator  = new Validator("LoadListaEsitiRicercaFascAltreBDI");

      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>","maxlen=2","La lunghezza massima per il giorno di inizio è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>","minlen=2","La lunghezza minima per il giorno di inizio è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>","numeric");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>","gt=1");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>","lt=31");

      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>","maxlen=2","La lunghezza massima per il mese di inizio è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>","minlen=2","La lunghezza minima per il mese di inizio è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>","numeric");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>","gt=1");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>","lt=12");

      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>","maxlen=4","La lunghezza massima per l'anno di inizio è di 4 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>","minlen=4","La lunghezza minima per l'anno di inizio è di 4 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>","numeric");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>","gt=1900");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>","lt=3000");

      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>","maxlen=2","La lunghezza massima per il giorno di fine è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>","minlen=2","La lunghezza minima per il giorno di fine è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>","numeric");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>","gt=1");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>","lt=31");

      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>","maxlen=2","La lunghezza massima per il mese di fine è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>","minlen=2","La lunghezza minima per il mese di fine è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>","numeric");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>","gt=1");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>","lt=12");

      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>","maxlen=4","La lunghezza massima per l'anno di fine è di 4 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>","minlen=4","La lunghezza minima per l'anno di fine è di 4 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>","numeric");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>","gt=1900");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>","lt=3000");

      var ufficiAccorpatiArray = new Array();

      <%
      Iterator uaIter = ufficiAccorpati.iterator();
      int uaIndice = 0;
      while (uaIter.hasNext())
      {
      	UfficioAccorpatoModel uaModel = (UfficioAccorpatoModel) uaIter.next();
      %>
      ufficiAccorpatiArray[<%=uaIndice%>] = new Array("<%=uaModel.getDescrizione()%>"
    		  ,"<%=uaModel.getIncrProgressivo()%>"
    		  ,"<%=uaModel.getCodUfficio()%>"
    		  ,"<%=uaModel.getCodUfficioNew()%>"
    		  ,"<%=uaModel.getCodTipoUfficio()%>"
    		  ,"<%=uaModel.getCodTipoUfficioNew()%>"
    		  ,"<%=uaModel.getDescrizioneNewUfficio()%>"); 
      <%
      uaIndice ++;
      }
      %>

      function transCoding(cod){
    	  var ret = cod;
    	  if (cod=='DIB'){
    		  ret = 'Tribunale Ordinario';
    	  } else if (cod=='TRIBSD'){
    		  ret = 'Sezione Distaccata Tribunale';
    	  } else if (cod=='CAS'){
    		  ret = 'Corte Assise';
    	  } else if (cod=='GIP'){
    		  ret = 'Gip presso Tribunale';
    	  } else if (cod=='PM'){
    		  ret = 'Procura presso Tribunale';
    	  } else if (cod=='PGCAP'){
    		  ret = 'Procura presso Corte Appello';
    	  }
    	  return ret;
      }

      function loadUfficiAccorpati(codUfficio){
    		var i=0;
    		var ufficioAccorpatoSelect = document.LoadListaEsitiRicercaFascAltreBDI.<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO_ACCORPATO%>;
    		ufficioAccorpatoSelect.options.length = 0;
    		ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option("-", "0");
    		while(i<ufficiAccorpatiArray.length){
    			var ufficio = ufficiAccorpatiArray[i];
    			if (ufficio[3]==codUfficio){
    				ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option(ufficio[0]+" ("+transCoding(ufficio[4])+")", ufficio[1]+"-"+ufficio[2]);
    			}
    			i++;
    		}
    	}

      function loadNumProgOrigin(){
          document.LoadListaEsitiRicercaFascAltreBDI.<%=ICostantiSicoJMS.CHIAVE_PROGR_SIEP%>.value = "";
          var numProg = document.LoadListaEsitiRicercaFascAltreBDI.<%=ICostantiSicoJMS.CHIAVE_PROGR_SIEP_ORIGIN%>.value;
          var ufficioAccorpato = document.LoadListaEsitiRicercaFascAltreBDI.<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO_ACCORPATO%>.value;
          var parts=ufficioAccorpato.split("-");
          var offSetInt = parseInt(parts[0]);
          if (numProg){
              var newProg = parseInt(numProg) + offSetInt;
              document.LoadListaEsitiRicercaFascAltreBDI.<%=ICostantiSicoJMS.CHIAVE_PROGR_SIEP%>.value = newProg;
          }
          return true;
  	}

      function resetField(){
          document.LoadListaEsitiRicercaFascAltreBDI.<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>.value="";
          var selectUfficioAccorpato = document.LoadListaEsitiRicercaFascAltreBDI.<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO_ACCORPATO%>;
          selectUfficioAccorpato.options.length = 0;
          selectUfficioAccorpato.options[selectUfficioAccorpato.options.length] = new Option("-", "0");
      }

      function loadUfficiAccorpatiByDesc(){
  		var i=0;
  		var ufficioAccorpatoSelect = document.LoadListaEsitiRicercaFascAltreBDI.<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO_ACCORPATO%>;
  		var ufficioBaseDesc = document.LoadListaEsitiRicercaFascAltreBDI.<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>.value;
  		var ufficioTipoSelect = document.LoadListaEsitiRicercaFascAltreBDI.<%=ICostantiSicoJMS.CAMPO_TIPO_UFFICIO%>;
  		if (ufficioTipoSelect.selectedIndex>0){
  			var ufficioTipo = ufficioTipoSelect.options[ufficioTipoSelect.selectedIndex].value;
  			ufficioAccorpatoSelect.options.length = 0;
  			ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option("-", "0");
  			while(i<ufficiAccorpatiArray.length){
  				var ufficio = ufficiAccorpatiArray[i];
  				if (ufficio[6]==ufficioBaseDesc && ufficio[5]==ufficioTipo){
  					ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option(ufficio[0]+" ("+transCoding(ufficio[4])+")", ufficio[1]+"-"+ufficio[2]);
  				}
  				i++;
  			}
  		}
  	}      

      </script>

  </body>
</html>