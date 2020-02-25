<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.model.FunzioneModel" %>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.titoloesecutivo.action.ICostantiTitoloEsecutivo" %>
<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel"%>

<jsp:useBean id="AutoritaCompetente" scope="request" class="java.lang.String"/>
<jsp:useBean id="LuogoUtenteConnesso" scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficiAccorpati" scope="request" class="java.util.Vector" />

<html>
  <head>
    <title>[S.I.E.S.] - Assegnazione Titolo Esecutivo Principale</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
      var desktop;
      function ListaComuni(a_formname,a_fieldname,codTipoUfficio)
      {
         desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
      function TrasformaRes(a_formname,a_fieldname,a_fieldname2)
      {
        desktop = window.open("/jsp/Main.jsp?Action=siap.sico.util.ActCalcolaNumeroRes&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname2="+a_fieldname2,"Calcola_Numero_Res","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
      function TrasformaPret(a_formname,a_fieldname,a_fieldname2)
      {
        desktop = window.open("/jsp/Main.jsp?Action=siap.sico.util.ActCalcolaNumeroPret&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname2="+a_fieldname2,"Calcola_Numero_Res","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

      function Verify()
      {
        // Impostazione action di ricerca.
        document.LoadAssegnaTitoloEsecutivo.<%=IWebConstants.ACTION_FIELD%>.value="siap.sius.titoloesecutivo.action.ActRicercaTitoloEsecutivo"

        // Controllo Anno/Numero Fascicolo SIEP.
        if ( document.LoadAssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>.value.length<4 || document.LoadAssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>.value<1900 || isNaN(document.LoadAssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>.value) )
        {
          alert ("Anno Fascicolo SIEP Non Valido");
          document.LoadAssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>.focus();
          return false;
        }
        if ( document.LoadAssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN%>.value.length<=0 || document.LoadAssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN %>.value<0 || isNaN(document.LoadAssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN %>.value) )
        {
          alert ("Numero Fascicolo SIEP Non Valido");
          document.LoadAssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN%>.focus();
          return false;
        }
        // Controllo obbligatorietà autorità Competente.
        if (document.LoadAssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>.value=="-")
        {
          alert("Il tipo autorità competente è un campo obbligatorio");
          document.LoadAssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>.focus();
          return false;
        }

        if (document.LoadAssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_FASCICOLO_SIEP%>.value=="")
        {
          alert("Il luogo per l'autorità competente è un campo obbligatorio");
          document.LoadAssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_FASCICOLO_SIEP%>.focus();
          return false;
        }
        return true;
      }
      </script>
  </head>

  <body class="corpo" onLoad="loadUfficiAccorpatiByDesc();">
  <link rel="STYLESHEET" type="text/css" href="/css/style.css">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
<%
          String lAction = new String();
          lAction = "siap.sius.titoloesecutivo.action.ActAssegnaTitoloEsecutivo";
          if( modalita.equals("I") )
          {
              lAction = "siap.sius.titoloesecutivo.action.ActAssegnaTitoloEsecutivo";
%>
            <font class="campo">Assegnazione Titolo Esecutivo Principale</font>
<%
          }
          else if( modalita.equals("M") )
          {
            lAction = "siap.sius.titoloesecutivo.action.ActModificaTitoloEsecutivo";
%>
            <font class="campo">Ridefinizione Titolo Esecutivo Principale</font>
<%
          }
%>
      </td>
    </tr>
  </table>

  <br>

  <table>
    <tr>
      <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
  </table>
  <br>
  <table>
    <tr>
      <td class="lVerdeNB">N.B. Se il Titolo Esecutivo di riferimento è su altra BDI, ricercare prima il procedimento SIEP con apposita funzione </td>
    </tr>
  </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadAssegnaTitoloEsecutivo'>
    <table cellspacing=0 cellpadding=0 width=95%>

    <tr>
      <td class="LBG" colspan="2" >
        <font class="label">Estremi del Titolo Esecutivo Principale</font>&nbsp;
      </td>
    </tr>

    <tr>
      <td class="l"width="32%">Anno/Numero SIEP <font class=ob>(*)</font></td>
      <td class="l">
        <input type="text" name="<%=ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        /<input type="text" name="<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN %>" maxlength="14" size="14" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        <input type="hidden" name="<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP%>" value="">
         &nbsp;&nbsp;<a href="Javascript:TrasformaRes('LoadAssegnaTitoloEsecutivo','<%= ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>','<%= ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN %>');">
         R.E.S.<img src="/images/filefolder.gif" border=0></a>&nbsp;&nbsp;
         <a href="Javascript:TrasformaPret('LoadAssegnaTitoloEsecutivo','<%= ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>','<%= ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN %>');">
         P.T.<img src="/images/filefolder.gif" border=0></a>
      </td>
    </tr>

    <tr>
      <td class="l">Autorità <font class=ob>(*)</font></td>
      <td class="L">
        <select class=medium name="<%=ICostantiTitoloEsecutivo.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>" onchange="javascript:resetField();">
          <%= AutoritaCompetente %>
        </select>
      </td>
    </tr>

    <tr>
      <td class="l">Luogo <font class=ob>(*)</font></td>
      <td class="l">
        <input name="<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_FASCICOLO_SIEP%>" type="text" maxlength="35" size="35" readonly="readonly">
        <a href="Javascript:ListaComuni('LoadAssegnaTitoloEsecutivo','<%= ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_FASCICOLO_SIEP %>' , document.LoadAssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>[document.LoadAssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>.selectedIndex].value);">
        <img src="/images/filefolder.gif" border=0> </a>
      </td>
    </tr>

    <tr>
      <td class="l">Ufficio Accorpato</td>
      <td class="l">
         	<select name="<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_ACCORPATO%>">
         	<option value="0" >-</option>
         	</select>
      </td>
    </tr>

    </table>
    <br>
    <tr>
      <td>
        <input class="bottone" type="submit" name="Ricerca" value="Conferma" onClick="javascript:return loadNumProgOrigin();">
      </td>
    </tr>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
    <input type="HIDDEN" name="modalita" value="<%=modalita%>" >

  </FORM>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadAssegnaTitoloEsecutivo");

    frmvalidator.addValidation("<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN%>","maxlen=14","La lunghezza massima per il Numero Fascicolo è di 14 caratteri");
    frmvalidator.addValidation("<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN%>","numeric");

    frmvalidator.addValidation("<%=ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP%>","maxlen=4","La lunghezza massima per l'Anno Fascicolo è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP%>","minlen=4","La lunghezza minima per l'Anno Fascicolo è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP%>","numeric");

    frmvalidator.setAddnlValidationFunction("Verify");

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
  		var ufficioAccorpatoSelect = document.LoadAssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_ACCORPATO%>;
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
        document.LoadAssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP%>.value = "";
        var numProg = document.LoadAssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN%>.value;
        var ufficioAccorpato = document.LoadAssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_ACCORPATO%>.value;
        var parts=ufficioAccorpato.split("-");
        var offSetInt = parseInt(parts[0]);
        if (numProg){
            var newProg = parseInt(numProg) + offSetInt;
            document.LoadAssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP%>.value = newProg;
        }
        return true;
	}

    function resetField(){
        document.LoadAssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_FASCICOLO_SIEP%>.value="";
        var selectUfficioAccorpato = document.LoadAssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_ACCORPATO%>;
        selectUfficioAccorpato.options.length = 0;
        selectUfficioAccorpato.options[selectUfficioAccorpato.options.length] = new Option("-", "0");
    }

    function loadUfficiAccorpatiByDesc(){
		var i=0;
		var ufficioAccorpatoSelect = document.LoadAssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_ACCORPATO%>;
		var ufficioBaseDesc = document.LoadAssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_FASCICOLO_SIEP%>.value;
		var ufficioTipoSelect = document.LoadAssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>;
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