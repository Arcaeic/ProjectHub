<?php
$con = mysql_connect("localhost","root","password");
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

// Create database
if (mysql_query("CREATE DATABASE GameGrumps",$con))
  {
  echo "Database created";
  }
else
  {
  echo "Error creating database: " . mysql_error();
  }

// Create table
mysql_select_db("GameGrumps", $con);
$sql = "CREATE TABLE Characters
(
Id varchar(3),
Date varchar(12),
Type varchar(20),
Name varchar(20),
Genre varchar(20),
Winner varchar(5)
)";

// Execute query
mysql_query($sql,$con);

mysql_close($con);
?>

 <script language="javascript" type="text/javascript">
     <!--
     window.setTimeout('window.location="http://localhost/mysql2/ControlPanel.php"; ',1);
     // -->
 </script>
