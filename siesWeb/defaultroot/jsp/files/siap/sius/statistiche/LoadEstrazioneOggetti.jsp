<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.magistrato.model.MagistratoModel" %>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"  %>

<%@ page import="siap.siep.statis.action.ICostantiStatis" %>
<%@ page import="siap.sius.statistiche.action.ICostantiStatistiche"  %>
<%@ page import="f3b.util.DateUtils"%>

<jsp:useBean id="magistrati" scope="request" class="java.util.Vector"/>
<jsp:useBean id="SecondoGiro" scope="request" class="java.lang.String"/>
		


<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

    <script language="JavaScript" type="text/javascript">
    function Verify() {

    if (document.c.<%= ICostantiStatistiche.CAMPO_GIORNO_INIZIALE%>.value.length==1)
       document.c.<%= ICostantiStatistiche.CAMPO_GIORNO_INIZIALE%>.value='0'+document.c.<%= ICostantiStatistiche.CAMPO_GIORNO_INIZIALE%>.value;
    if (document.c.<%= ICostantiStatistiche.CAMPO_MESE_INIZIALE%>.value.length==1)
       document.c.<%= ICostantiStatistiche.CAMPO_MESE_INIZIALE%>.value='0'+document.c.<%= ICostantiStatistiche.CAMPO_MESE_INIZIALE%>.value;

    if (document.c.<%= ICostantiStatistiche.CAMPO_GIORNO_FINALE%>.value.length==1)
       document.c.<%= ICostantiStatistiche.CAMPO_GIORNO_FINALE%>.value='0'+document.c.<%= ICostantiStatistiche.CAMPO_GIORNO_FINALE%>.value;
    if (document.c.<%= ICostantiStatistiche.CAMPO_MESE_FINALE%>.value.length==1)
        document.c.<%= ICostantiStatistiche.CAMPO_MESE_FINALE%>.value='0'+document.c.<%= ICostantiStatistiche.CAMPO_MESE_FINALE%>.value;

    var data_inizio=document.c.<%=ICostantiStatistiche.CAMPO_GIORNO_INIZIALE%>.value+'/'+document.c.<%=ICostantiStatistiche.CAMPO_MESE_INIZIALE%>.value+'/'+document.c.<%=ICostantiStatistiche.CAMPO_ANNO_INIZIALE%>.value;
    var data_fine=document.c.<%=ICostantiStatistiche.CAMPO_GIORNO_FINALE%>.value+'/'+document.c.<%=ICostantiStatistiche.CAMPO_MESE_FINALE%>.value+'/'+document.c.<%=ICostantiStatistiche.CAMPO_ANNO_FINALE%>.value;
	var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';;

      if(!ControllaDataPassaVuota(data_inizio))
      {
        alert('Data di inizio non valida');
        return false;
      }
      if(!ControllaDataPassaVuota(data_fine))
      {
        alert('Data di fine non valida');
        return false;
      }

      if(data_inizio.length==2) {

       alert('Inserire la Data di inizio');
       return false;
	  }

      if(data_fine.length==2) {

       alert('Inserire la Data di fine');
       return false;
	  }

      if(!CompareDate(data_inizio, data_sistema))
      {
        alert('La Data di inizio non può essere superiore alla Data odierna');
        return false;
      }

      if(!CompareDate(data_fine, data_sistema))
      {
        alert('La Data di fine non può essere superiore alla Data odierna');
        return false;
      }


      if(!CompareDate(data_inizio, data_fine))
      {
        alert('La Data di fine non può essere inferiore alla Data di inizio');
        return false;
      }
        <%if(SecondoGiro ==null || !SecondoGiro.equals("SI")) {%>
    		ciao();
     	<%}%>
        return true;
    }

    function enableBtn() {
		
		<%if(SecondoGiro !=null && SecondoGiro.equals("SI")) {%>
    		document.c.btnconf.disabled = false;
   	
    	<%}%>
    }

    </script>
     <script language="JavaScript">
    function ciao()
    {
      var node=document.getElementById('ciao');
      node.style.visibility='visible';
    }
     </script>   
     
  </head>

  <BODY class="corpo">

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="c">
	   <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font  class="label">Funzione :&nbsp;</font><font class="campo">STATISTICHE - MOVIMENTO PROVVEDIMENTI DISTINTI PER OGGETTI</font>
        </td>
      </tr>
    </table>
    <br>

 <jsp:include page="<%=ICostantiStatistiche.PG_INCLUDE_INTERVALLO_DATE %>"/> 
  
   <%if(SecondoGiro !=null && SecondoGiro.equals("SI")) {%>
    <table>
    <tr height=20> <td></td></tr>
    <tr> <td class="Titolo"  colspan ="4" >Opzioni sull'estrazione</td></tr>
      <tr>    
 	<td class="c" >
        <font class="label">
          Tipo di Estrazione
        </font>
      </td>   
       <td class="label" >
			<select name="<%=ICostantiStatistiche.CB_TIPO_ESTRAZIONE_OGGETTI%>" SIZE=6 onChange="enableBtn();">
			<option value="0" selected>Dettagliata</option>
			<option value="1">Aggregata</option>
			</select>        
          </td>
 	 <td class="c" >
        <font class="label">
           Magistrato
        </font>
      </td>
        <td class="label" >
			<select name="<%=ICostantiStatistiche.CB_LISTA_MAGISTRATI %>" SIZE=6 onChange="enableBtn();">
			<option value="0" selected>Tutti</option>
			<%
			Iterator itx = magistrati.iterator();

			while ( itx.hasNext()) {

				MagistratoModel lMagMod = (MagistratoModel) itx.next();
				%>
				<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
				<option value="<%=lMagMod.getCodMagistrato()%>"><%=lMagMod.getCognome()%>&nbsp;<%=StringUtils.toStringJSP(lMagMod.getNome())%></option><%
			}
			%>
			</select>
          </td>
        </tr>
        </table>
           <jsp:include page="<%=ICostantiStatistiche.PG_INCLUDE_COMBO_OGGETTI %>"> 
            <jsp:param name="pendenti" value="pendenti"/>  
   			</jsp:include>
 	<table>
    <tr height=20> <td></td></tr>
    <tr>
      <td>
        <input type=submit value="Conferma" class=bottone name="btnconf">
        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.statistiche.action.ActCreaStatisticheOggetti">
      </td>
		</tr>
    <tr>
      <td>
      </td>
	</tr>
</table>
<%}else{%>
<table>
    <tr height=20> <td></td></tr>
    <tr>
      <td>
        <input type=submit value="Caricamento" class=bottone name="btnMag">
        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.statistiche.action.ActLoadStatisticheOggetti">
      </td>
		</tr>
    <tr>
      <td>
      </td>
	</tr>
</table>
   <div align=center id="ciao" style="visibility:hidden;position:relative;">
      <table bgcolor="#EEEEEE">
        <tr>
          <td>
            <img src="/images/rotelle3.gif">
          </td>
          <td>
            <font size=+1 color=navy>
              Attendere... Caricamento in corso.
            </font>
          </td>
        </tr>
      </table>
    </div>
<%}%>
  </FORM>

<script language="JavaScript" type="text/javascript">
  	var frmvalidator  = new Validator("c");

  frmvalidator.addValidation("<%= ICostantiStatistiche.CAMPO_GIORNO_INIZIALE%>","numeric","Il campo Data Iniziale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%= ICostantiStatistiche.CAMPO_GIORNO_INIZIALE%>","maxlen=2","La lunghezza massima per il giorno di inizio è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiStatistiche.CAMPO_GIORNO_INIZIALE%>","gt=1", "Giorno iniziale non valido");
  frmvalidator.addValidation("<%= ICostantiStatistiche.CAMPO_GIORNO_INIZIALE%>","lt=31", "Giorno iniziale non valido");

  frmvalidator.addValidation("<%= ICostantiStatistiche.CAMPO_GIORNO_FINALE%>","numeric","Il campo Data Finale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%= ICostantiStatistiche.CAMPO_GIORNO_FINALE%>","maxlen=2","La lunghezza massima per il giorno di fine è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiStatistiche.CAMPO_GIORNO_FINALE%>","gt=1", "Giorno finale non valido");
  frmvalidator.addValidation("<%= ICostantiStatistiche.CAMPO_GIORNO_FINALE%>","lt=31", "Giorno finale non valido");

  frmvalidator.addValidation("<%= ICostantiStatistiche.CAMPO_MESE_INIZIALE%>","numeric","Il campo Data Iniziale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_INIZIALE%>","maxlen=2","La lunghezza massima per il mese di inizio è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiStatistiche.CAMPO_MESE_INIZIALE%>","gt=1", "Mese iniziale non valido");
  frmvalidator.addValidation("<%= ICostantiStatistiche.CAMPO_MESE_INIZIALE%>","lt=12", "Mese iniziale non valido");

  frmvalidator.addValidation("<%= ICostantiStatistiche.CAMPO_MESE_FINALE%>","numeric","Il campo Data Finale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_FINALE%>","maxlen=2","La lunghezza massima per il mese di fine è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiStatistiche.CAMPO_MESE_FINALE%>","gt=1", "Mese finale non valido");
  frmvalidator.addValidation("<%= ICostantiStatistiche.CAMPO_MESE_FINALE%>","lt=12", "Mese finale non valido");

  frmvalidator.addValidation("<%= ICostantiStatistiche.CAMPO_ANNO_INIZIALE%>","numeric","Il campo Data Iniziale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_INIZIALE%>","maxlen=4","La lunghezza massima per l'anno di inizio è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiStatistiche.CAMPO_ANNO_INIZIALE%>","minlen=4","La lunghezza minima per l'anno di inizio è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiStatistiche.CAMPO_ANNO_INIZIALE%>","gt=1900", "Anno iniziale non valido");

  frmvalidator.addValidation("<%= ICostantiStatistiche.CAMPO_ANNO_FINALE%>","numeric","Il campo Data Finale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_FINALE%>","maxlen=4","La lunghezza massima per l'anno di fine è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiStatistiche.CAMPO_ANNO_FINALE%>","minlen=4","La lunghezza minima per l'anno di fine è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiStatistiche.CAMPO_ANNO_FINALE%>","gt=1900", "Anno finale non valido");

  frmvalidator.setAddnlValidationFunction("Verify");

 </script>
</body>
</html>