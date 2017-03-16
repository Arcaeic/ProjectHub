<?php
$con = mysql_connect("localhost","root","password");
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

mysql_select_db("GameGrumps", $con) ;

$sql="INSERT INTO Characters (Id, Date, Type, Name, Genre, Winner)
VALUES ('$_Post[Id]','$_POST[Date]','$_POST[Type]','$_POST[Name]','$_POST[Genre]','$_POST[Winner]')";

if (!mysql_query($sql,$con))
  {
  die('Error: ' . mysql_error());
  }

mysql_close($con);
?>

 <script language="javascript" type="text/javascript">
     <!--
     window.setTimeout('window.location="http://localhost/mysql2/ControlPanel.php"; ',1);
     // -->
 </script>




