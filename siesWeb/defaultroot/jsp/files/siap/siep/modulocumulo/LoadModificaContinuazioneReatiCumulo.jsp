<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.modulocumulo.model.ReatoCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiReatoCumulo"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>

<%@ page import="siap.siep.cumulo.action.ICostantiCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="reaticum"         scope="request" class="java.util.Vector"/>
<jsp:useBean id="stringareaticum"  scope="request" class="java.util.Vector"/>
<jsp:useBean id="table"            scope="request" class="java.util.Hashtable"/>

<html>
<head>
  <title>[S.I.E.S.] - Continuazione Reati (Cumulo) </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<SCRIPT LANGUAGE="JavaScript">
// parametri per il floating layer
floatX=10;
floatY=0;
layerwidth=100;
layerheight=130;
halign="center";
valign="top";
delayspeed=1;
</script>

<script language="JavaScript" src="<%=IWebConstants.JS_FLAYER%>"></script>

<SCRIPT LANGUAGE="JavaScript">

var arrReati = new Array();

function addContinuazione(modalita, tipoCont) 
{
  
  if (modalita == "INSERIMENTO") 
  {
  
    if (f.tipoContinuazione[0].checked) 
    {
      tc = f.tipoContinuazione[0].value;
      f.tipoContinuazione[0].checked = false;
    }
    else if(f.tipoContinuazione[1].checked) 
    {
      tc = f.tipoContinuazione[1].value;
      f.tipoContinuazione[1].checked = false;     
    }
    else 
    {
      alert ("Selezionare il tipo di continuazione");
      return; 
    }
    
  }
  else 
  {
    tc = tipoCont;
  }

  ind = f.continuazioni.options.length;
  
  f.lista_reati.length = 0;
  num = ind+1;
  str = "CONTINUAZIONE " + num + " - " + tc;
  opt = new Option(str, tc, false, true);

  f.continuazioni[ind] = opt;
  arrReati[ind] = new Array();  
 }

// ---------------------------------

function addReati() 
 {    
  if (f.continuazioni.selectedIndex == -1) 
  {
    alert("Selezionare una continuazione");
    return;
  }
  ind = f.lista_reati.length;
  r=0;
  for (i=0; i < f.chkReati.length; i++) 
  {
    if (f.chkReati[i].checked) 
    {
      if (!isReatoPresenteInLista(f.chkReati[i].value)) 
      {
          if (!isReatoPresenteInArray(f.chkReati[i].value)) 
          {
            arrNumReato = f.chkReati[i].value.split("@!");      
          
            opt = new Option("REATO " + arrNumReato[1], f.chkReati[i].value);
            f.lista_reati[ind+r] = opt;
            r++;        
          }
      }
      
      f.chkReati[i].checked = false;
    }
  }
    
  allineaArray();
 }

// ---------------------------------

function delReati() 
 {    
  ind = f.lista_reati.selectedIndex;
  if (ind != -1) 
  {
    f.lista_reati.options[ind] = null;
    allineaArray();
  }   
 }

function loadReati(i) 
 {
  f.lista_reati.length = 0; 
  for (y=0; y < arrReati[i].length; y++) 
  {
    arrNumReato = arrReati[i][y].split("@!");
    opt = new Option("REATO " + arrNumReato[1], arrReati[i][y]);
    
    f.lista_reati[y] = opt;
  }
 }

// ---------------------------------

function isReatoPresenteInLista(strReato) 
 {
  ret = false;
  for (y=0; y < f.lista_reati.length; y++) 
  {
    if (f.lista_reati.options[y].value == strReato) 
      ret = true;
  }
    return ret;
 }

// ---------------------------------

function abilitaElementiperLista() 
 {
  if (f.lista_reati.length == 1) 
  {
    f.continuazioni.disabled = true;
    f.NC.disabled = true;
    f.conferma.disabled = true;
  }
  else 
  {
    f.continuazioni.disabled = false;
    f.NC.disabled = false;
    f.conferma.disabled = false;
  }
}

// ---------------------------------

function disabilitaElementi(bool) 
 {
    
  f.NC.disabled = bool;
  f.conferma.disabled = bool;
  f.annulla.disabled = bool;
  f.inside.disabled = bool;
  f.out.disabled = bool;

 }

// ---------------------------------

function allineaArray() 
 {
    abilitaElementiperLista();
    arrReati[f.continuazioni.selectedIndex] = new Array();
    for (i=0; i < f.lista_reati.length; i++) 
    {
      arrReati[f.continuazioni.selectedIndex][i] = f.lista_reati.options[i].value;
    }   
 }

// ---------------------------------

function creaStringhe() 
{
  disabilitaElementi(true);
      
  str = "";
  strcont = "";
  for (i=0; i < arrReati.length; i++) 
  {
    if (arrReati[i].length > 1) 
    {
      for (y=0; y < arrReati[i].length; y++) 
      {
        arrNumReato = arrReati[i][y].split("@!");
        str += arrNumReato[0] + "-";
      }
      str = str.substring(0,str.length-1);
      str += "=";
      strcont += f.continuazioni.options[i].value + "=";
    }
  } 
  
  strcont = strcont.substring(0,strcont.length-1);
  str = str.substring(0,str.length-1);

  f.strreaticont.value = str;
  
  if (f.strreaticont.value == "") 
  {
    alert("Non è stata immessa nessuna continuazione");
    disabilitaElementi(false);
    return;
  }
  
  f.strtipocont.value = strcont;
  
  f.submit();
}

// ---------------------------------

function isReatoPresenteInArray(strReato) 
 {
  for (ir=0; ir < arrReati.length; ir++) 
  {
    for (yr=0; yr < arrReati[ir].length; yr++) 
    {
      if (strReato == arrReati[ir][yr]) 
      {
        alert("Reato già presente in altra continuazione");
        return true; 
      }
    }
  } 
  
  return false;
 }

// ---------------------------------

function abbandona() 
{
  if (confirm("Annullare le operazione effettuate?") == true) 
  {
    document.LoadModificaContinuazioneReatiCumulo.<%=IWebConstants.ACTION_FIELD%>.value = 'siap.siep.modulocumulo.action.ActRicercaReatoCumulo';
    document.LoadModificaContinuazioneReatiCumulo.submit();
  }   
}

// ---------------------------------

//==========================================================================
// Ritorna alla Griglia dei dati analitici
//==========================================================================
function tornaIndietro(action)
{
  document.LoadModificaContinuazioneReatiCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
  document.LoadModificaContinuazioneReatiCumulo.submit();
}


</script>

</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Gestione Continuazione Reati Relativi al Titolo Cumulato</font>
      </td>
      <td class="LBG">
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActRicercaReatoCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
    
  <br>
    <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
  <br> 
          

<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadModificaContinuazioneReatiCumulo">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActModificaContinuazioneReatiCumulo">
    
  <!-- Campi sempre presenti sulle form dei dati analitici -->
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">

  <input type="hidden" name="strtipocont"  value="" >
  <input type="hidden" name="strreaticont" value="" >

  <table width="40%"> 
    <tr><td class="TitoloNoCap" colspan=2><b>Reati</b></td>
    <%
  
    Iterator itx = reaticum.iterator();
    int cont = 0;
    String str = new String();
    String strReato = new String();
    String lProgressivo = new String();
    
    while ( itx.hasNext()) 
    {
        ReatoCumuloModel lReato = (ReatoCumuloModel)itx.next();
  
        if (lReato.getProgrCircostanza().intValue() == 1) 
        { 
          lProgressivo = (lReato.getProgrNumeroManuale() != null) ? lReato.getProgrNumeroManuale().toString() : lReato.getProgrReato().toString();
        %>
        <tr>
          <td class="l" valign="top">
            <input type="checkbox" name="chkReati" value="<%=lReato.getProgrReato().toString()%>@!<%=lProgressivo%>">
          </td>
          <td class=l>
            <font class="cRosso">REATO <%=lProgressivo%>: </font>
            <%  strReato = (String)stringareaticum.get(cont); %>
                
            <%= StringUtils.cStrForJS(strReato) %>
          </td>
        </tr>
        <%
          cont++;
        }
    }
 %>
 
  </table>

<script>
  if (NS4) {document.write('<LAYER NAME="floatlayer" LEFT="'+floatX+'" TOP="'+floatY+'">');}
  if ((IE4) || (NS6)) {document.write('<div id="floatlayer" style="position:absolute; left:'+floatX+'; top:'+floatY+';">');}
</script>

    <table width="40%">
      <tr>
        <td colspan="3" align="center"  class="TitoloNoCap"><b>Continuazione</b></td>
      </tr>
      
      <tr>
        <td>
          <table>
            <tr>
                  <td><input type="button" value="---->" name="inside" onClick="addReati()"></td>
            </tr>
            
            <tr>
                  <td> </td>
            </tr>

            <tr>
                  <td><input type="button" value="<----" name="out" onClick="delReati()"></td>
            </tr>

            <tr height=140>
                  <td>&nbsp;</td>
            </tr>
          </table>

        </td>

        <td>
          <table>
            <tr>
                <td><select name="continuazioni" onChange="loadReati(this.selectedIndex);" style="width: 250"></select></td>
            </tr>
            
            <tr>        
                <td><select name="lista_reati" size=8 style="width: 250"></select></td>
            </tr>

            <tr>
                <td><input type="button" name="NC" value="Nuova Continuazione" onClick="addContinuazione('INSERIMENTO', '');"></td>
            </tr>

            <tr>
              <td class="campo">
                <input type="radio" name="tipoContinuazione" value="C1">81 CP C1
                <input type="radio" name="tipoContinuazione" value="C2">81 CP C2
              </td>
            </tr>

            <tr height=50 valign=bottom>
              <td>
                <input type="button" name="conferma" value="Conferma" style="width: 90" onClick="creaStringhe();">
                  &nbsp;&nbsp;&nbsp;&nbsp;
                <input type="button" name="annulla" value="Annulla" style="width: 90" onClick="abbandona();">
              </td>
            </tr>
          </table>

<script>
  f = document.LoadModificaContinuazioneReatiCumulo;
  if (NS4) 
  {
    document.write('</LAYER>');
  }

  if ((IE4) || (NS6)) 
  {
    document.write('</DIV>');
  }
  
  ifloatX=floatX;
  ifloatY=floatY;
  define();
  window.onresize=define;
  lastX=-1;
  lastY=-1;
  adjust();
</script>

<script language="Javascript">
 <%
  if (table.size() > 0) 
  {
      String strCont = "";
      Collection coll = table.values();
      Iterator itxColl = coll.iterator();
      int ind1 = 0;
      int ind2 = 0;
      int conta = 0;
      
      Vector vectCont = new Vector();
      while (itxColl.hasNext()) 
      {
        vectCont = (Vector)itxColl.next();
        
        Iterator itxVect = vectCont.iterator();
        
        ind2 = 0;
        conta = 0;
        while (itxVect.hasNext()) 
        {
          strCont = (String)itxVect.next();
          if (conta == 0) 
          {
 %>
            addContinuazione("PRECARICAMENTO","<%=strCont%>");
            arrReati[<%=ind1%>] = new Array();
   <%
          }
          else 
          {
   %>
            arrReati[<%=ind1%>][<%=ind2%>] = "<%=strCont%>";
     <%
            ind2++;
          }
          conta++;
        }
        ind1++;
      }
   %>
      f.continuazioni.selectedIndex = 0;
      loadReati(0);
 
 <% } %>
        
</script>

 </body>
</html>