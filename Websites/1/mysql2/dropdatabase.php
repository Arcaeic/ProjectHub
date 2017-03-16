<?php
$link = mysql_connect('localhost', 'root', 'password');
if (!$link) {
    die('Could not connect: ' . mysql_error());
}

$sql = 'DROP DATABASE GameGrumps';
if (mysql_query($sql, $link)) {
    echo "Database GameGrumps was successfully dropped\n";
} else {
    echo 'Error dropping database: ' . mysql_error() . "\n";
}
?>

 <script language="javascript" type="text/javascript">
     <!--
     window.setTimeout('window.location="http://localhost/mysql2/ControlPanelnodata.html"; ',1);
     // -->
 </script>
